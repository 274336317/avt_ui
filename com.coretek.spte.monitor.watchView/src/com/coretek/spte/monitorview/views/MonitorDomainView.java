/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitorview.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.part.ViewPart;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.template.Constants;
import com.coretek.common.template.ICDField;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.template.TemplateUtils;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.spte.monitor.manager.Event;
import com.coretek.spte.monitor.ui.manager.MonitorEventManager;
import com.coretek.spte.monitorview.MsgWatchViewPlugin;
import com.coretek.spte.monitorview.dialogs.CfgFunctionNodeDialog;
import com.coretek.spte.monitorview.internal.NodeElementParser;
import com.coretek.spte.monitorview.internal.NodeElementSet;
import com.coretek.spte.monitorview.internal.NodeElementSetManager;
import com.coretek.spte.monitorview.internal.QueryNodeMsgThread;
import com.coretek.spte.monitorview.internal.RefreshTableTreeDataManager;
import com.coretek.spte.monitorview.internal.RefreshTableTreeDataThread;
import com.coretek.spte.monitorview.preference.MonitorNodePreferenceManager;
import com.coretek.spte.testcase.Field;
import com.coretek.spte.testcase.Message;
import com.coretek.spte.testcase.Period;

/**
 * 监控消息视图
 * 
 * @author 尹军 2012-3-14
 */
public class MonitorDomainView extends ViewPart implements Observer
{

	// 从视图内容提供器获得的SPTE消息列表
	private static List<SPTEMsg>			list			= null;

	// 当前视图选择的当前时间戳
	private long							currentTimeStamp;

	// 当前视图选择的结束时间戳
	private long							endTimeStamp;

	// 是否锁定监控
	private volatile boolean				isLockMonitor	= false;

	// 消息节点管理器
	private boolean							isRegister		= false;

	// 消息节点管理器
	private NodeElementSetManager			manager;

	// 从数据库查询到的SPTE消息列表
	private ArrayList<SPTEMsg>				msgs			= new ArrayList<SPTEMsg>();

	// 首选项配置管理器
	private MonitorNodePreferenceManager	preferenceManager;

	// 刷新视图表线程
	private RefreshTableTreeDataThread		refresh			= null;

	// 当前视图选择的开始时间戳
	private long							startTimeStamp;

	// 当前视图消息表
	private TableTreeViewer					tableViewer;

	// 当前视图是否终止监控
	private volatile boolean				termination		= true;

	private Image							refreshImage;

	private Image							configsImage;

	private Image							hexImage;

	private boolean							hex				= true;

	@Override
	public void createPartControl(Composite parent)
	{
		parent.setLayout(new GridLayout());
		Composite mainPanel = new Composite(parent, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_BOTH);
		mainPanel.setLayoutData(gd);
		GridLayout grid = new GridLayout();
		grid.numColumns = 1;
		mainPanel.setLayout(grid);

		Composite topPanel = new Composite(mainPanel, SWT.NONE);
		grid = new GridLayout();
		grid.horizontalSpacing = 0;
		grid.verticalSpacing = 0;
		grid.marginWidth = 0;
		grid.marginHeight = 0;
		topPanel.setLayout(grid);

		ToolBar toolBar = new ToolBar(topPanel, SWT.HORIZONTAL | SWT.RIGHT | SWT.FLAT);

		ToolItem item = new ToolItem(toolBar, SWT.PUSH);
		item.setToolTipText(Messages.getString("MonitorDomainView_Setting_Field_For_Monitor"));
		configsImage = MsgWatchViewPlugin.getImageDescriptor("icons/configs.gif").createImage();
		item.setImage(configsImage);
		item.addSelectionListener(new SelectionListener()
		{

			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			public void widgetSelected(SelectionEvent e)
			{
				openCfgDialog();
			}
		});

		gd = new GridData(GridData.FILL_HORIZONTAL);
		toolBar.setLayoutData(gd);

		item = new ToolItem(toolBar, SWT.PUSH);
		item.setToolTipText(Messages.getString("MonitorDomainView_Reset"));

		refreshImage = MsgWatchViewPlugin.getImageDescriptor("icons/refresh.gif").createImage();
		item.setImage(refreshImage);
		item.addSelectionListener(new SelectionListener()
		{

			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			public void widgetSelected(SelectionEvent e)
			{
				List<NodeElementSet> fields = getManager().getAllFields();
				for (NodeElementSet field : fields)
				{
					field.getMsgs().clear();
				}
				fields.clear();
				getTableViewer().setInput(null);
				getTableViewer().refresh();
			}
		});
		item = new ToolItem(toolBar, SWT.CHECK);
		item.setToolTipText("切换到10进制");
		hexImage = MsgWatchViewPlugin.getImageDescriptor("icons/hex.png").createImage();
		item.setImage(hexImage);
		item.setSelection(!hex);
		item.addSelectionListener(new SelectionAdapter()
		{

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				ToolItem toolItem = (ToolItem) e.widget;
				hex = !toolItem.getSelection();
				tableViewer.setLabelProvider(new TableLabelProvider());
				if (hex)
				{
					toolItem.setToolTipText("切换到10进制");
				} else
				{
					toolItem.setToolTipText("切换到16进制");
				}
			}
		});

		this.tableViewer = new TableTreeViewer(mainPanel, SWT.BORDER | SWT.FULL_SELECTION | SWT.SINGLE);
		TableColumn column = new TableColumn(this.tableViewer.getTableTree().getTable(), SWT.SCROLL_LOCK | SWT.NO_SCROLL);
		column.setText(Messages.getString("MonitorDomainView_Message_Or_Field"));
		column.setWidth(100);

		column = new TableColumn(this.tableViewer.getTableTree().getTable(), SWT.SCROLL_LOCK | SWT.NO_SCROLL);
		column.setText(Messages.getString("MonitorDomainView_Field_Name"));
		column.setWidth(100);

		column = new TableColumn(this.tableViewer.getTableTree().getTable(), SWT.SCROLL_LOCK | SWT.NO_SCROLL);
		column.setText(Messages.getString("MonitorDomainView_Field_Value"));
		column.setWidth(60);

		column = new TableColumn(this.tableViewer.getTableTree().getTable(), SWT.NONE);
		column.setText(Messages.getString("MonitorDomainView_Unit"));
		column.setWidth(50);

		column = new TableColumn(this.tableViewer.getTableTree().getTable(), SWT.NONE);
		column.setText(Messages.getString("MonitorDomainView_Start_Word"));
		column.setWidth(60);

		column = new TableColumn(this.tableViewer.getTableTree().getTable(), SWT.NONE);
		column.setText(Messages.getString("MonitorDomainView_Word_Length"));
		column.setWidth(60);

		column = new TableColumn(this.tableViewer.getTableTree().getTable(), SWT.NONE);
		column.setText(Messages.getString("MonitorDomainView_Start_Bit"));
		column.setWidth(60);

		column = new TableColumn(this.tableViewer.getTableTree().getTable(), SWT.NONE);
		column.setText(Messages.getString("MonitorDomainView_Time_Binary"));
		column.setWidth(100);

		this.tableViewer.setContentProvider(new ViewContentProvider());
		this.tableViewer.setLabelProvider(new TableLabelProvider());
		this.tableViewer.getTableTree().getTable().setHeaderVisible(true);

		gd = new GridData(GridData.FILL_BOTH);
		this.tableViewer.getTableTree().setLayoutData(gd);
		this.tableViewer.getTableTree().getTable().setLayoutData(gd);
		this.tableViewer.getTableTree().getTable().setLinesVisible(true);

		init();
	}

	@Override
	public void dispose()
	{
		// 将自身注册为监听者
		MonitorEventManager.getMonitorEventManager().deleteObserver(this);

		if (!this.isTerminated())
		{
			this.setTermination(true);
		}
		if (refreshImage != null && !refreshImage.isDisposed())
		{
			refreshImage.dispose();
		}
		if (configsImage != null && !configsImage.isDisposed())
		{
			configsImage.dispose();
		}
		if (hexImage != null && !hexImage.isDisposed())
		{
			hexImage.dispose();
		}
		manager.clear();
		super.dispose();
	}

	/**
	 * 获得当前视图的当前时间戳
	 * 
	 * @return 获得当前视图的当前时间戳
	 */
	public long getCurrentTimeStamp()
	{
		return currentTimeStamp;
	}

	/**
	 * 获得当前视图的结束时间戳
	 * 
	 * @return 当前视图的结束时间戳
	 */
	public long getEndTimeStamp()
	{
		return endTimeStamp;
	}

	/**
	 * 获得消息节点管理器
	 * 
	 * @return 消息节点管理器
	 */
	public NodeElementSetManager getManager()
	{
		return manager;
	}

	/**
	 * 获得消息列表
	 * 
	 * @return 消息列表
	 */
	public synchronized ArrayList<SPTEMsg> getMsgs()
	{
		return msgs;
	}

	/**
	 * 获得首选项配置管理器
	 * 
	 * @return 首选项管理器
	 */
	public MonitorNodePreferenceManager getPreferenceManager()
	{
		return preferenceManager;
	}

	/**
	 * 获得当前视图的开始时间戳
	 */
	public synchronized long getStartTimeStamp()
	{
		return startTimeStamp;
	}

	/**
	 * 获得消息节点表
	 */
	public TableTreeViewer getTableViewer()
	{
		return tableViewer;
	}

	/**
	 * 初始化视图参数
	 */
	public void init()
	{
		setStartTimeStamp(0);
		setCurrentTimeStamp(0);
		preferenceManager = new MonitorNodePreferenceManager();
		preferenceManager.init();
		manager = new NodeElementSetManager(preferenceManager.getTimestampLengthEachPage(), preferenceManager.getPageNum(), preferenceManager.getPageSubItemNum());
	}

	/**
	 * @return 是否锁定监控
	 */
	public boolean isLockMonitor()
	{
		return isLockMonitor;
	}

	/**
	 * 是否注册
	 * 
	 * @return 是否注册
	 */
	public boolean isRegister()
	{
		return isRegister;
	}

	/**
	 * 监控状态是否停止
	 * 
	 * @return 监控状态
	 */
	public boolean isTerminated()
	{
		return termination;
	}

	/**
	 * 打开配置对话框
	 */
	public void openCfgDialog()
	{
		CfgFunctionNodeDialog dialog = new CfgFunctionNodeDialog(this.getViewSite().getShell(), this);
		if (dialog.open() == Window.OK)
		{
			setRegister(true);
		}
	}

	/**
	 * 复位视图参数
	 */
	public void reset()
	{
		setStartTimeStamp(0);
		setEndTimeStamp(0);
		setCurrentTimeStamp(0);
		RefreshTableTreeDataManager manager = new RefreshTableTreeDataManager(this);
		manager.refreshData(null);
	}

	/**
	 * * 通过当前时间戳设置当前视图需显示的消息
	 * 
	 * @param event
	 */
	public void selectEventHandel(Event event)
	{
		RefreshTableTreeDataManager manager = new RefreshTableTreeDataManager(this);
		if (isTerminated())
		{
			long current = getCurrentTimeStamp();

			List<NodeElementSet> fields = getManager().getAllFields();
			if (fields != null && fields.size() > 0)
			{
				for (NodeElementSet field : fields)
				{
					List<SPTEMsg> list = field.getSPTEMsgElementsToShow(current, current + 30);
					if (list != null && list.size() > 0)
					{
						if (getMsgs() != null && getMsgs().size() > 0)
						{
							getMsgs().clear();
						}
						getMsgs().add(list.get(list.size() - 1));
					}
				}

				if (getMsgs() != null && getMsgs().size() > 0)
				{
					manager.refreshData(getMsgs());
				}
			}
		}
	}

	/**
	 * 设置当前视图的当前时间戳
	 * 
	 * @param currentTimeStamp 视图的当前时间戳
	 */
	public void setCurrentTimeStamp(long currentTimeStamp)
	{
		this.currentTimeStamp = currentTimeStamp;
	}

	/**
	 * 获得当前视图的结束时间戳
	 * 
	 * @param endTimeStamp 当前视图的结束时间戳
	 */
	public void setEndTimeStamp(long endTimeStamp)
	{
		this.endTimeStamp = endTimeStamp;
	}

	@Override
	public void setFocus()
	{

	}

	/**
	 * 设置是否锁定监控
	 * 
	 * @param isLock 是否锁定监控
	 */
	public void setLockMonitor(boolean isLock)
	{
		this.isLockMonitor = isLock;
	}

	/**
	 * * 设置消息节点管理器
	 * 
	 * @param manager
	 */
	public void setManager(NodeElementSetManager manager)
	{
		this.manager = manager;
	}

	/**
	 * 设置消息列表
	 * 
	 * @param msgs 消息列表
	 */
	public synchronized void setMsgs(ArrayList<SPTEMsg> msgs)
	{
		this.msgs = msgs;
	}

	/**
	 * 设置注册
	 * 
	 * @param isRegister 注册
	 */
	public void setRegister(boolean isRegister)
	{
		this.isRegister = isRegister;
	}

	/**
	 * * 设置当前视图的开始时间戳
	 * 
	 * @param startTimeStamp
	 */
	public synchronized void setStartTimeStamp(long startTimeStamp)
	{
		this.startTimeStamp = startTimeStamp;
	}

	/**
	 * * 设置监控状态
	 * 
	 * @param termination 监控状态
	 */
	public void setTermination(boolean termination)
	{
		this.termination = termination;
	}

	/**
	 * 启动监控
	 */
	public void startMonitor()
	{
		if (this.isTerminated())
		{
			setTermination(false);
		}
	}

	/**
	 * 停止监控
	 */
	public void stopMonitor()
	{
		if (!this.isTerminated())
		{
			this.setTermination(true);
		}
	}

	public void update(Observable obs, Object arg)
	{
		Event event = (Event) arg;
		switch (event.getEventType())
		{
			// 接受锁定监控事件并进行处理
			case Event.EVENT_LOCK:
			{
				setLockMonitor(true);
				break;
			}
				// 接受解锁监控事件并进行处理
			case Event.EVENT_UNLOCK:
			{
				setLockMonitor(false);
				break;
			}

				// 接受启动监控事件并进行处理
			case Event.EVENT_START:
			{
				getManager().clearData();
				reset();
				startMonitor();
				break;
			}

				// 接受加载监控历史记录事件并进行处理
			case Event.EVENT_LOAD:
			{
				getManager().clearData();
				reset();
				break;
			}

				// 接受停止监控事件并进行处理
			case Event.EVENT_STOP:
			{
				stopMonitor();
				break;
			}

				// 接受用户选择某个时间戳事件并进行处理
			case Event.EVENT_TIME_SELECTED:
			{
				setStartTimeStamp(event.getStartTime());
				setEndTimeStamp(event.getEndTime());
				setCurrentTimeStamp(event.getCurrentTime());

				RefreshTableTreeDataThread ref = new RefreshTableTreeDataThread(Messages.getString("MonitorDomainView_Thread_Refresh_Data"), this, true);

				QueryNodeMsgThread queryThread = new QueryNodeMsgThread(Messages.getString("MonitorDomainView_Thread_Query_Data"), this, ref, event.getStartTime(), event.getStartTime() + getManager().getCachePageTimeLength());
				queryThread.run();
				ref.run();

				break;
			}

				// 接受系统产生的时间戳事件并进行处理
			case Event.EVENT_TIME_GENERATED:
			{
				if (this.isTerminated())
				{
					setTermination(false);
				}
				if (isLockMonitor())
				{
					break;
				}
				if (refresh == null || !refresh.isAlive())
				{
					refresh = new RefreshTableTreeDataThread(Messages.getString("MonitorDomainView_Thread_Refresh_Data"), this, false);
					refresh.start();
				}
				List<String> messageIds = this.getManager().getMessageIds();
				SPTEMsg[] msgs = event.getSpteMsgs();
				List<SPTEMsg> spteMsgs = new ArrayList<SPTEMsg>();
				if ((messageIds != null && messageIds.size() > 0) && (msgs != null && msgs.length > 0))
				{
					for (SPTEMsg msg : msgs)
					{
						if (messageIds != null && messageIds.size() > 0)
						{
							for (String id : messageIds)
							{
								if (id.equals(msg.getMsg().getId()))
								{
									spteMsgs.add(msg);
									break;
								}
							}
						}
					}
					NodeElementParser parser = new NodeElementParser(this);
					parser.parserNodeElementData(msgs);
					if (!this.isTerminated())
					{
						this.setCurrentTimeStamp(event.getEndTime());
					}

				}

				break;
			}

			default:
				break;
		}
	}

	/**
	 * 消息信号标签提供者
	 * 
	 * @author 尹军 2012-3-14
	 */
	private class TableLabelProvider extends LabelProvider implements ITableLabelProvider
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java
		 * .lang.Object, int)
		 */
		public Image getColumnImage(Object element, int columnIndex)
		{
			if (columnIndex == 1)
			{

			}
			return null;
		}

		/*
		 * (non-Javadoc) 获得列文本
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.
		 * lang.Object, int)
		 */
		public String getColumnText(Object element, int columnIndex)
		{
			if (element instanceof Message)
			{
				if (columnIndex == 0)
				{
					try
					{
						return ((Message) element).getId().toString();
					} catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				if (columnIndex == 7)
				{
					for (SPTEMsg showspte : list)
					{
						if (showspte.getMsg().hashCode() == ((Message) element).hashCode())
						{
							return Long.toString(showspte.getTimeStamp()).toString();
						}
					}
				}
				return "";
			} else if (element instanceof Field)
			{
				Field result = (Field) element;
				switch (columnIndex)
				{
					case 0:
					{ // 信号ID
						return result.getId().toString();
					}
					case 1:
					{ // 信号名
						return result.getName().toString();
					}
					case 2:
					{ // 信号值
						if (hex)
						{
							return "0x" + Integer.toHexString(Integer.parseInt(result.getValue()));
						} else
						{
							return result.getValue();
						}
					}
					case 3:
					{ // 信号单位
						for (SPTEMsg msg : list)
						{
							List<Entity> children = msg.getMsg().getChildren();

							for (int i = 0; i < children.size(); i++)
							{
								Entity entry = children.get(i);
								if (entry instanceof Period)
								{
									List<Entity> periodChildren = entry.getChildren();
									for (int j = 0; j < periodChildren.size(); j++)
									{
										Entity fieldEntity = periodChildren.get(j);
										if (fieldEntity instanceof Field)
										{
											try
											{
												if (((Field) fieldEntity).hashCode() == result.hashCode())
												{
													ICDField icdfield = TemplateUtils.getICDField(msg, result);
													try
													{
														return icdfield.getAttribute(Constants.ICD_FIELD_UNIT_CODE).getValue().toString();
													} catch (Exception e)
													{
														return "";
													}
												}
											} catch (IllegalArgumentException e)
											{
												e.printStackTrace();
											}
										}
									}
								} else if (entry instanceof Field)
								{
									try
									{
										if (((Field) entry).hashCode() == result.hashCode())
										{
											ICDField icdfield = TemplateUtils.getICDField(msg, result);
											try
											{
												return icdfield.getAttribute(Constants.ICD_FIELD_UNIT_CODE).getValue().toString();
											} catch (Exception e)
											{
												return "";
											}
										}
									} catch (IllegalArgumentException e)
									{
										e.printStackTrace();
									}
								}
							}
						}
						return "";
					}
					case 4:
					{ // 信号偏移字
						return Integer.toString(result.getOffsetword());
					}
					case 5:
					{ // 信号字宽
						return Integer.toString(result.getWidth());
					}
					case 6:
					{ // 信号偏移位
						return Integer.toString(result.getOffsetbit());
					}

					case 7:
					{ // 信号值的二进制
						return Integer.toBinaryString(Integer.parseInt(result.getValue()));
					}

				}
			}

			return "";
		}
	}

	/**
	 * 消息信号内容提供者
	 * 
	 * @author 尹军 2012-3-14
	 */

	private static class ViewContentProvider implements ITreeContentProvider
	{
		public void dispose()
		{

		}

		public Object[] getChildren(Object parentElement)
		{

			if (parentElement instanceof Message)
			{
				// 如果是Message,则获取下面的所以Field
				Message message = (Message) parentElement;

				List<Entity> children = new ArrayList<Entity>(0);
				for (Entity en : message.getChildren())
				{
					if (en instanceof Period)
					{
						children.addAll(en.getChildren());
					} else
					{
						children.add(en);
					}
				}
				return children.toArray();

			} else if (parentElement instanceof Field)
			{
				// 如果是Field, 则判断其下是否还有Field子节点
				Field field = (Field) parentElement;

				if (field.getChildren() != null && field.getChildren().size() > 0)
				{
					return field.getChildren().toArray();
				}
			}

			return null;
		}

		@SuppressWarnings("unchecked")
		public Object[] getElements(Object inputElement)
		{
			List<Entity> objs = new ArrayList<Entity>(0);

			// 传入的是一个SPTEMsg对象数组
			if (inputElement instanceof Object[])
			{
				SPTEMsg[] groups = (SPTEMsg[]) inputElement;

				// 获取出所有SPTEMsg中的Message
				for (SPTEMsg sptemsg : groups)
				{
					objs.add(sptemsg.getMsg());
				}
			} else if (inputElement instanceof List<?>)
			{
				list = (List<SPTEMsg>) inputElement;

				// 获取出所有SPTEMsg中的Message
				for (SPTEMsg sptemsg : list)
				{
					objs.add(sptemsg.getMsg());
				}
				return objs.toArray(new Message[objs.size()]);
			}

			return objs.toArray(new Message[objs.size()]);
		}

		public Object getParent(Object element)
		{
			if (element instanceof Field)
			{
				if (((Field) element).getParent() != null)
				{
					return ((Field) element).getParent();
				}

			} else if (element instanceof Message)
			{
				return null;
			}

			return null;
		}

		public boolean hasChildren(Object element)
		{
			if (element instanceof Field)
			{
				if (((Field) element).getChildren() == null || ((Field) element).getChildren().size() <= 0)
				{
					return false;
				}
			}
			return true;
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
		{

		}
	}
}