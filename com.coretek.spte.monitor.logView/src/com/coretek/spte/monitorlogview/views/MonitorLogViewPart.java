/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitorlogview.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
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
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.spte.monitor.manager.Event;
import com.coretek.spte.monitor.ui.manager.MonitorEventManager;
import com.coretek.spte.monitorlogview.MonitorLogPlugin;
import com.coretek.spte.monitorlogview.internal.QueryLogMsgThread;
import com.coretek.spte.monitorlogview.internal.RefreshTableManager;
import com.coretek.spte.testcase.Field;
import com.coretek.spte.testcase.Message;
import com.coretek.spte.testcase.Period;

/**
 * �����־��ͼ
 * 
 * @author ���� 2012-3-14
 */
public class MonitorLogViewPart extends ViewPart
{

	// ��Ϣ�����ͼID
	public static final String	ID						= "com.coretek.spte.monitorlogview.views";

	public MonitorLogObservable	observable				= new MonitorLogObservable();

	// ��ǰ��Ϣ���
	private int					count					= 0;

	// ��ǰʱ���
	private long				currentTimeStamp;

	// ����ʱ���
	private long				endTimeStamp;

	// �Ƿ��������
	private volatile boolean	isLockMonitor			= false;

	// ��Ϣ�б�
	private ArrayList<SPTEMsg>	msgList					= new ArrayList<SPTEMsg>();

	// ��ʼʱ���
	private long				startTimeStamp;

	// ����ͼ����
	private TableViewer			tableViewer;

	private TreeViewer			treeViewer;

	// �Ƿ��������
	private volatile boolean	termination				= true;

	private boolean				hex						= true;										// �Ƿ���16���Ƶ���ʽ�����ź�ֵ����

	private static final int	DEFAUTL_EXPAND_LEVEL	= TreeViewer.ALL_LEVELS;

	// ����������
	private int					sortDirection[]			= { SWT.DOWN, SWT.DOWN, SWT.DOWN, SWT.DOWN };

	/**
	 * �����Ϣ
	 * 
	 * @param arg0 ��Ϣ
	 * @return ����Ƿ�ɹ�
	 */
	public boolean add(SPTEMsg arg0)
	{
		return msgList.add(arg0);
	}

	/**
	 * ���������Ϣ�б�
	 * 
	 * @param arg0 ��Ϣ�б�
	 * @return ����Ƿ�ɹ�
	 */
	public boolean addAll(Collection<? extends SPTEMsg> arg0)
	{
		return msgList.addAll(arg0);
	}

	/**
	 * ���������Ϣ�б�
	 * 
	 * @param arg0
	 * @param arg1
	 * @return
	 */
	public boolean addAll(int arg0, Collection<? extends SPTEMsg> arg1)
	{
		return msgList.addAll(arg0, arg1);
	}

	/**
	 * ������Ϣ�б�
	 */
	public void clear()
	{
		msgList.clear();
	}

	/*
	 * (non-Javadoc) ������ͼ�ؼ�
	 * 
	 * @see
	 * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 * .Composite) <br/> <b>����</b> ���� </br> <b>����</b> 2012-3-14
	 */
	@Override
	public void createPartControl(Composite parent)
	{
		this.getObservable().addObserver(MonitorEventManager.getMonitorEventManager());
		MonitorEventManager.getMonitorEventManager().addObserver((Observer) this.getObservable());

		GridLayout grid = new GridLayout();
		grid.numColumns = 1;
		parent.setLayout(grid);

		Composite topPanel = new Composite(parent, SWT.NONE);
		GridLayout form2 = new GridLayout();
		form2.numColumns = 5;
		form2.horizontalSpacing = 0;
		form2.verticalSpacing = 0;
		form2.marginWidth = 0;
		form2.marginHeight = 0;
		topPanel.setLayout(form2);

		ToolBar toolBar = new ToolBar(topPanel, SWT.HORIZONTAL | SWT.RIGHT | SWT.FLAT);

		ToolItem item = new ToolItem(toolBar, SWT.PUSH);
		item.setToolTipText(Messages.getString("MonitorLogViewPart_Reset"));

		item.setImage(MonitorLogPlugin.getImageDescriptor("icons/refresh.gif").createImage());
		item.addSelectionListener(new SelectionListener()
		{

			public void widgetDefaultSelected(SelectionEvent e)
			{
				// TODO Auto-generated method stub

			}

			public void widgetSelected(SelectionEvent e)
			{
				tableViewer.setInput(null);
			}
		});

		item = new ToolItem(toolBar, SWT.CHECK);
		item.setImage(MonitorLogPlugin.getImageDescriptor("icons/hex.png").createImage());
		item.setToolTipText("�л���10����");
		item.setSelection(!hex);
		item.addSelectionListener(new SelectionAdapter()
		{

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				ToolItem toolItem = (ToolItem) e.widget;
				hex = !toolItem.getSelection();
				if (tableViewer != null)
				{
					tableViewer.setLabelProvider(new TableLabelProvider());

				}
				if (treeViewer != null)
				{
					treeViewer.setLabelProvider(new TreeLableProvider());
					treeViewer.expandAll();
				}
				if (hex)
				{
					toolItem.setToolTipText("�л���10����");
				} else
				{
					toolItem.setToolTipText("�л���16����");
				}

			}
		});

		buildViewer(parent);

		this.makeActions();
	}

	/*
	 * (non-Javadoc) ��ͼ���ٴ���
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose() <br/> <b>����</b> ���� </br>
	 * <b>����</b> 2012-3-14
	 */
	@Override
	public void dispose()
	{
		MonitorEventManager.getMonitorEventManager().deleteObserver((Observer) this.getObservable());
		getObservable().deleteObserver(observable);
		if (!this.isTerminated())
		{
			this.setTermination(true);
		}

		super.dispose();
	}

	/**
	 * �����Ϣ���
	 * 
	 * @return ��Ϣ���
	 */
	public synchronized int getCount()
	{
		return count;
	}

	/**
	 * ��õ�ǰʱ���
	 * 
	 * @return ��ǰʱ���
	 */
	public long getCurrentTimeStamp()
	{
		return currentTimeStamp;
	}

	/**
	 * ��ý���ʱ���
	 * 
	 * @return ����ʱ���
	 */
	public synchronized long getEndTimeStamp()
	{
		return endTimeStamp;
	}

	/**
	 * �����Ϣ�б�
	 * 
	 * @return ��Ϣ�б�
	 */
	public ArrayList<SPTEMsg> getMsgs()
	{
		return msgList;
	}

	/**
	 * ��ÿ�ʼʱ���
	 * 
	 * @return ��ʼʱ���
	 */
	public synchronized long getStartTimeStamp()
	{
		return startTimeStamp;
	}

	/**
	 * �����Ϣ����ͼ
	 * 
	 * @return ��Ϣ����ͼ
	 */
	public TableViewer getTableViewer()
	{
		return tableViewer;
	}

	public TreeViewer getTreeViewer()
	{
		return treeViewer;
	}

	/**
	 * ��ʼ��ʱ���
	 */
	void init()
	{
		this.getViewSite().getShell().getDisplay().syncExec(new Runnable()
		{
			public void run()
			{
				if (tableViewer != null)
				{
					tableViewer.setInput(null);
				}
				if (treeViewer != null)
				{
					treeViewer.setInput(null);
				}

			}
		});

		setStartTimeStamp(0);
		setEndTimeStamp(0);
		setCurrentTimeStamp(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.ViewPart#init(org.eclipse.ui.IViewSite) <br/>
	 * <b>����</b> ���� </br> <b>����</b> 2012-3-14
	 */
	@Override
	public void init(IViewSite site) throws PartInitException
	{
		super.init(site);

	}

	/**
	 * @return �Ƿ��������
	 */
	public boolean isLockMonitor()
	{
		return isLockMonitor;
	}

	/**
	 * �ж��Ƿ���ֹ���
	 * 
	 * @return �Ƿ���ֹ���
	 */
	public boolean isTerminated()
	{
		return termination;
	}

	/**
	 * 
	 */
	private void makeActions()
	{

	}

	/**
	 * ѡ��ǰ���һ����ϢΪ��ǰ��Ϣ
	 * 
	 * @param event �¼�����
	 */
	@SuppressWarnings("unchecked")
	public void selectEventHandle(Event event)
	{
		Object inputElement = getTableViewer().getInput();
		if (inputElement instanceof List<?>)
		{
			List<SPTEMsg> lists = (List<SPTEMsg>) inputElement;

			// ���ݵ�ǰ��ͼ�Ŀ�ʼ����ǰ������ʱ���������ͼ��ʾ�ĵ�ǰ��
			for (int i = lists.size() - 1; i > 0; i--)
			{
				if (lists.get(i).getTimeStamp() >= event.getStartTime() && lists.get(i).getTimeStamp() <= event.getCurrentTime())
				{
					getTableViewer().getTable().setSelection(i);
					getTableViewer().refresh();
					break;
				}
			}
		}
	}

	/**
	 * ������Ϣ���
	 * 
	 * @param count ��Ϣ���
	 */
	public synchronized void setCount(int count)
	{
		this.count = count;
	}

	/**
	 * ���õ�ǰʱ���
	 * 
	 * @param currentTimeStamp ��ǰʱ���
	 */
	public void setCurrentTimeStamp(long currentTimeStamp)
	{
		this.currentTimeStamp = currentTimeStamp;
	}

	/**
	 * ���ý���ʱ���
	 * 
	 * @param endTimeStamp ����ʱ���
	 */
	public synchronized void setEndTimeStamp(long endTimeStamp)
	{
		this.endTimeStamp = endTimeStamp;
	}

	@Override
	public void setFocus()
	{

	}

	/**
	 * �����Ƿ��������
	 * 
	 * @param isLock �Ƿ��������
	 */
	public void setLockMonitor(boolean isLock)
	{
		this.isLockMonitor = isLock;
	}

	/**
	 * ���ÿ�ʼʱ���
	 * 
	 * @param startTimeStamp ��ʼʱ���
	 */
	public synchronized void setStartTimeStamp(long startTimeStamp)
	{
		this.startTimeStamp = startTimeStamp;
	}

	/**
	 * �����Ƿ���ֹ���
	 * 
	 * @param termination �Ƿ���ֹ���
	 */
	public void setTermination(boolean termination)
	{
		this.termination = termination;
	}

	/**
	 * �������
	 */
	public void startMonitor()
	{
		if (this.isTerminated())
		{
			setTermination(false);
		}
	}

	/**
	 * ֹͣ���
	 */
	public void stopMonitor()
	{
		if (!this.isTerminated())
		{
			this.setTermination(true);
		}
	}

	/**
	 * @return the observable
	 */
	public Observable getObservable()
	{
		return observable;
	}

	/**
	 * @param event
	 */
	private void startMonitor(Event event)
	{
		getMsgs().clear();
		init();
		startMonitor();
	}

	/**
	 * @param event
	 */
	private void loadHistory(Event event)
	{
		getMsgs().clear();
		init();
	}

	/**
	 * @param event
	 */
	private void selectTimeStamp(Event event)
	{
		long startTime = event.getStartTime();
		long endTime = event.getEndTime();
		long current = event.getCurrentTime();

		setStartTimeStamp(startTime);
		setEndTimeStamp(endTime);
		setCurrentTimeStamp(current);
		QueryLogMsgThread queryThread = new QueryLogMsgThread(Messages.getString("MonitorLogViewPart_Thread_Query_Field"), this, event.getStartTime(), event.getEndTime(), event.getCurrentTime());
		queryThread.start();
	}

	/**
	 * @param event
	 */
	private void generateTime(Event event)
	{

		if (this.isTerminated())
		{
			setTermination(false);
		}
		if (!isLockMonitor())
		{
			for (SPTEMsg msg : event.getSpteMsgs())
			{
				getMsgs().add(msg);
			}
			RefreshTableManager manager = new RefreshTableManager(this, event.getStartTime(), event.getEndTime(), event.getEndTime());
			manager.refreshData();
		}

	}

	protected void buildViewer(Composite parent)
	{
		treeViewer = new TreeViewer(parent, SWT.FULL_SELECTION | SWT.BORDER);
		TreeColumn column = new TreeColumn(treeViewer.getTree(), SWT.NONE);
		column.setText(Messages.getString("MonitorLogViewPart_No"));
		column.setWidth(50);
		column = new TreeColumn(this.treeViewer.getTree(), SWT.NONE);
		column.setText(Messages.getString("MonitorLogViewPart_TimeStamp"));
		column.setWidth(60);
		column = new TreeColumn(this.treeViewer.getTree(), SWT.NONE);
		column.setText(Messages.getString("MonitorLogViewPart_Message_ID"));
		column.setWidth(60);
		column = new TreeColumn(this.treeViewer.getTree(), SWT.NONE);
		column.setText(Messages.getString("MonitorLogViewPart_Message_Name"));
		column.setWidth(100);
		column = new TreeColumn(this.treeViewer.getTree(), SWT.NONE);
		column.setText(Messages.getString("MonitorLogViewPart_Source_ID"));
		column.setWidth(60);
		column = new TreeColumn(this.treeViewer.getTree(), SWT.NONE);
		column.setText("�ź���");
		column.setWidth(100);
		column = new TreeColumn(this.treeViewer.getTree(), SWT.NONE);
		column.setText("�ź�ֵ");
		column.setWidth(100);
		treeViewer.setLabelProvider(new TreeLableProvider());
		treeViewer.setContentProvider(new ITreeContentProvider()
		{

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
			{
				// System.out.println("input change"+oldInput+"==="+newInput);
				// if(viewer instanceof TreeViewer) {
				// System.out.println("========");
				// ((TreeViewer) viewer).expandAll();
				// }
			}

			@Override
			public void dispose()
			{
				// TODO Auto-generated method stub

			}

			@Override
			public Object[] getElements(Object inputElement)
			{
				if (inputElement instanceof List<?>)
				{
					List<SPTEMsg> list = (List<SPTEMsg>) inputElement;
					setCount(0);
					return list.toArray(new SPTEMsg[list.size()]);
				}
				return (SPTEMsg[]) inputElement;
			}

			@Override
			public boolean hasChildren(Object element)
			{
				if (element instanceof SPTEMsg)
				{
					return true;
				}
				if (element instanceof Field)
				{
					if (((Field) element).getChildren() == null || ((Field) element).getChildren().size() > 0)
					{
						return true;
					}
				}
				return false;
			}

			@Override
			public Object getParent(Object element)
			{
				if (element instanceof Field)
				{
					if (((Field) element).getParent() != null)
					{
						return ((Field) element).getParent();
					}

				}
				return null;
			}

			@Override
			public Object[] getChildren(Object parentElement)
			{
				if (parentElement instanceof SPTEMsg)
				{
					Message msg = ((SPTEMsg) parentElement).getMsg();
					List<Entity> children = new ArrayList<Entity>(0);
					for (Entity en : msg.getChildren())
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
				}
				// if (parentElement instanceof Message) {
				// // �����Message,���ȡ���������Field
				// Message message = (Message) parentElement;
				//
				// List<Entity> children = new ArrayList<Entity>(0);
				// for (Entity en : message.getChildren()) {
				// if (en instanceof Period) {
				// children.addAll(en.getChildren());
				// } else {
				// children.add(en);
				// }
				// }
				// return children.toArray();
				// }

				else if (parentElement instanceof Field)
				{
					// �����Field, ���ж������Ƿ���Field�ӽڵ�
					Field field = (Field) parentElement;

					if (field.getChildren() != null && field.getChildren().size() > 0)
					{
						return field.getChildren().toArray();
					}
				}

				return null;
			}
		});
		GridData gd = new GridData(GridData.FILL_BOTH);
		treeViewer.getTree().setLayoutData(gd);
		treeViewer.getTree().setLinesVisible(true);
		treeViewer.getTree().setHeaderVisible(true);
		this.treeViewer.addDoubleClickListener(new IDoubleClickListener()
		{
			public void doubleClick(DoubleClickEvent event)
			{
				Object object = ((IStructuredSelection) event.getSelection()).getFirstElement();

				// System.out.println(object+""+event.getSelection());
				if (object instanceof SPTEMsg)
				{
					SPTEMsg msg = (SPTEMsg) object;
					long time = msg.getTimeStamp();
					try
					{
						if (observable.countObservers() > 0)
						{
							// observable.geta
							observable.notifyObservers(new Event(Event.EVENT_TIME_SELECTED, time - 5000, time, time));
						}
					} catch (Exception rr)
					{
						rr.printStackTrace();
					}
				}
			}
		});
		treeViewer.setAutoExpandLevel(DEFAUTL_EXPAND_LEVEL);

	}

	public void buildViewer2(Composite parent)
	{
		this.tableViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.SINGLE);

		TableColumn column = new TableColumn(this.tableViewer.getTable(), SWT.NONE);
		column.setText(Messages.getString("MonitorLogViewPart_No"));
		column.setWidth(50);

		column = new TableColumn(this.tableViewer.getTable(), SWT.NONE);
		column.setText(Messages.getString("MonitorLogViewPart_TimeStamp"));
		column.setWidth(60);
		column.addSelectionListener(new SelectionListener()
		{

			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				if (sortDirection[0] == SWT.UP)
				{
					tableViewer.getTable().setSortDirection(SWT.DOWN);
					sortDirection[0] = SWT.DOWN;
				} else
				{
					tableViewer.getTable().setSortDirection(SWT.UP);
					sortDirection[0] = SWT.UP;
				}

				tableViewer.getTable().setSortColumn((TableColumn) (TableColumn) e.getSource());
				((LogViewerSorter) tableViewer.getSorter()).doSort(2);

				tableViewer.refresh();
			}

		});

		column = new TableColumn(this.tableViewer.getTable(), SWT.NONE);
		column.setText(Messages.getString("MonitorLogViewPart_Message_ID"));
		column.setWidth(60);
		column.addSelectionListener(new SelectionListener()
		{

			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				if (sortDirection[1] == SWT.UP)
				{
					tableViewer.getTable().setSortDirection(SWT.DOWN);
					sortDirection[1] = SWT.DOWN;
				} else
				{
					tableViewer.getTable().setSortDirection(SWT.UP);
					sortDirection[1] = SWT.UP;
				}
				tableViewer.getTable().setSortColumn(((TableColumn) e.getSource()));
				((LogViewerSorter) tableViewer.getSorter()).doSort(3);
				tableViewer.refresh();
			}

		});

		column = new TableColumn(this.tableViewer.getTable(), SWT.NONE);
		column.setText(Messages.getString("MonitorLogViewPart_Message_Name"));
		column.setWidth(60);
		column.addSelectionListener(new SelectionListener()
		{

			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				if (sortDirection[2] == SWT.UP)
				{
					tableViewer.getTable().setSortDirection(SWT.DOWN);
					sortDirection[2] = SWT.DOWN;
				} else
				{
					tableViewer.getTable().setSortDirection(SWT.UP);
					sortDirection[2] = SWT.UP;
				}
				tableViewer.getTable().setSortColumn((TableColumn) e.getSource());
				((LogViewerSorter) tableViewer.getSorter()).doSort(4);
				tableViewer.refresh();
			}

		});

		column = new TableColumn(this.tableViewer.getTable(), SWT.NONE);
		column.setText(Messages.getString("MonitorLogViewPart_Source_ID"));
		column.setWidth(40);
		column.addSelectionListener(new SelectionListener()
		{

			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				if (sortDirection[3] == SWT.UP)
				{
					tableViewer.getTable().setSortDirection(SWT.DOWN);
					sortDirection[3] = SWT.DOWN;
				} else
				{
					tableViewer.getTable().setSortDirection(SWT.UP);
					sortDirection[3] = SWT.UP;
				}
				tableViewer.getTable().setSortColumn((TableColumn) e.getSource());
				((LogViewerSorter) tableViewer.getSorter()).doSort(5);
				tableViewer.refresh();
			}

		});

		column = new TableColumn(this.tableViewer.getTable(), SWT.NONE);
		column.setText(Messages.getString("MonitorLogViewPart_Content"));
		column.setWidth(200);

		this.tableViewer.setContentProvider(new TableContentProvider());
		this.tableViewer.setLabelProvider(new TableLabelProvider());
		this.tableViewer.setSorter(new LogViewerSorter());
		// this.tableViewer.setInput(Utils.getAllCasesInWorkspace());
		this.tableViewer.getTable().setHeaderVisible(true);

		GridData gd = new GridData(GridData.FILL_BOTH);
		this.tableViewer.getTable().setLayoutData(gd);
		this.tableViewer.getTable().setLinesVisible(true);
		this.tableViewer.addDoubleClickListener(new IDoubleClickListener()
		{
			public void doubleClick(DoubleClickEvent event)
			{
				Object object = ((IStructuredSelection) event.getSelection()).getFirstElement();
				if (object instanceof SPTEMsg)
				{
					SPTEMsg msg = (SPTEMsg) object;
					long time = msg.getTimeStamp();
					try
					{
						if (observable.countObservers() > 0)
						{
							// observable.geta
							observable.notifyObservers(new Event(Event.EVENT_TIME_SELECTED, time - 5000, time, time));
						}
					} catch (Exception rr)
					{
						rr.printStackTrace();
					}
				}
			}
		});

		tableViewer.getTable().setSortDirection(SWT.UP);
	}

	private class TreeLableProvider extends LabelProvider implements ITableLabelProvider
	{

		@Override
		public Image getColumnImage(Object element, int columnIndex)
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex)
		{
			switch (columnIndex)
			{
				case 0:
				{
					if (element instanceof SPTEMsg)
					{
						int currentCount = getCount();
						currentCount++;
						setCount(currentCount);
						return Integer.toString(currentCount);
					}
					break;

				}
				case 1:
				{
					if (element instanceof SPTEMsg)
					{
						return Long.toString(((SPTEMsg) element).getTimeStamp());
					}
					break;
				}
				case 2:
				{
					if (element instanceof SPTEMsg)
					{
						return ((SPTEMsg) element).getMsg().getId();
					}
					break;
				}
				case 3:
				{
					if (element instanceof SPTEMsg)
					{
						return ((SPTEMsg) element).getMsg().getName();
					}
					break;
				}
				case 4:
				{
					if (element instanceof SPTEMsg)
					{
						return ((SPTEMsg) element).getMsg().getSrcId();
					}
					break;
				}
				case 5:
				{
					if (element instanceof Field)
					{
						return ((Field) element).getName();
					}
					break;
				}
				case 6:
				{
					if (element instanceof Field)
					{
						Field result = (Field) element;
						if (result.getChildren() != null && result.getChildren().size() > 0)
						{
							return "";
						}
						if (hex)
						{
							return "0x" + Integer.toHexString(Integer.parseInt(result.getValue()));
						} else
						{
							return result.getValue();
						}
					}
					break;
				}
			}
			return "";
		}

	}

	public class MonitorLogObservable extends Observable implements Observer
	{
		/**
		 * ʵ�ֹ۲��߽ӿ�
		 */
		public void update(Observable obs, Object arg)
		{
			Event event = (Event) arg;
			switch (event.getEventType())
			{
				// ������������¼������д���
				case Event.EVENT_LOCK:
				{
					setLockMonitor(true);
					break;
				}

					// ���ܽ�������¼������д���
				case Event.EVENT_UNLOCK:
				{
					setLockMonitor(false);
					break;
				}

					// ������������¼������д���
				case Event.EVENT_START:
				{
					startMonitor(event);
					break;
				}

					// ���ܼ��ؼ����ʷ��¼�¼������д���
				case Event.EVENT_LOAD:
				{
					loadHistory(event);
					break;
				}

					// ����ֹͣ����¼������д���
				case Event.EVENT_STOP:
				{
					stopMonitor();
					break;
				}

					// �����û�ѡ��ĳ��ʱ����¼������д���
				case Event.EVENT_TIME_SELECTED:
				{
					selectTimeStamp(event);

					break;
				}

					// ����ϵͳ������ʱ����¼������д���
				case Event.EVENT_TIME_GENERATED:
				{
					generateTime(event);
					break;
				}

				default:
					break;
			}
		}

		public void notifyObservers(Object arg)
		{
			this.setChanged();
			super.notifyObservers(arg);
			this.clearChanged();
		}
	}

	/**
	 * @author ���� 2012-3-14
	 */
	private class LogViewerSorter extends ViewerSorter
	{

		// ������������
		private static final int	ASCENDING	= 0;

		// ���ս�������
		private static final int	DESCENDING	= 1;

		// �������е��к�
		private int					column;

		// ������
		private int					direction;

		public void doSort(int column)
		{
			if (column == this.column)
			{
				// Same column as last sort; toggle the direction
				direction = 1 - direction;
			} else
			{
				// New column; do an ascending sort
				this.column = column;
				direction = ASCENDING;
			}
		}

		public int compare(Viewer viewer, Object e1, Object e2)
		{
			int rc = 0;
			if (e1 instanceof SPTEMsg)
			{
				SPTEMsg result1 = (SPTEMsg) e1;
				SPTEMsg result2 = (SPTEMsg) e2;
				// Determine which column and do the appropriate sort
				switch (column)
				{
					case 2:
					{
						rc = new Long(result1.getTimeStamp()).compareTo(new Long(result2.getTimeStamp()));
						break;
					}
					case 3:
					{
						rc = result1.getMsg().getId().compareTo(result2.getMsg().getId());
						break;
					}
					case 4:
					{
						rc = result1.getMsg().getName().compareTo(result2.getMsg().getName());
						break;
					}
					case 5:
					{
						rc = result1.getMsg().getSrcId().compareTo(result2.getMsg().getSrcId());
						break;
					}
				}

				if (direction == DESCENDING)
					rc = -rc;
			}
			return rc;
		}
	}

	/**
	 * @author ���� 2012-3-14
	 */
	private class TableContentProvider implements IStructuredContentProvider
	{

		public void dispose()
		{

		}

		@SuppressWarnings("unchecked")
		public Object[] getElements(Object inputElement)
		{

			if (inputElement instanceof List<?>)
			{
				List<SPTEMsg> list = (List<SPTEMsg>) inputElement;
				setCount(0);
				return list.toArray(new SPTEMsg[list.size()]);
			}
			return (SPTEMsg[]) inputElement;
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
		{

		}

	}

	/**
	 * @author ���� 2012-3-14
	 */
	private class TableLabelProvider extends LabelProvider implements ITableLabelProvider
	{

		public Image getColumnImage(Object element, int columnIndex)
		{
			return null;
		}

		public String getColumnText(Object element, int columnIndex)
		{

			if (element instanceof SPTEMsg || element instanceof Field)
			{
				SPTEMsg result1 = null;
				Field result2 = null;
				if (element instanceof SPTEMsg)
				{
					result1 = (SPTEMsg) element;
				}
				if (element instanceof Field)
				{
					result2 = (Field) element;
					;
				}

				switch (columnIndex)
				{
					case 0:
					{// ��Ϣ���
						int currentCount = getCount();
						currentCount++;
						setCount(currentCount);
						return Integer.toString(currentCount);
					}
					case 1:
					{// ��Ϣ���
						if (result1 != null)
						{
							return Long.toString(result1.getTimeStamp());
						}
						break;
					}
					case 2:
					{// ��ϢID
						if (result1 != null)
						{
							return result1.getMsg().getId();
						}
						break;

					}
					case 3:
					{// ��Ϣ����
						if (result1 != null)
						{
							return result1.getMsg().getName();
						}
						break;

					}
					case 4:
					{// ��ϢԴID
						if (result1 != null)
						{
							return result1.getMsg().getSrcId();
						}
						break;

					}
					case 5:
					{// ��Ϣ���źš��ź�ֵ
						if (result1 == null)
							return "";
						List<Entity> children = result1.getMsg().getChildren();
						StringBuffer sb = new StringBuffer();

						for (int i = 0; i < children.size(); i++)
						{
							Entity entry = children.get(i);
							if (entry instanceof Period)
							{// ������Ϣ
								List<Entity> periodChildren = entry.getChildren();
								for (int j = 0; j < periodChildren.size(); j++)
								{
									Entity fieldEntity = periodChildren.get(j);
									if (fieldEntity instanceof Field)
									{// ����"�ź����� = �ź�ֵ���ź����� = �ź�ֵ��......"�ķ�ʽ����
										recursionFieldEntity(sb, fieldEntity);
									}
								}
							} else if (entry instanceof Field)
							{// �ź�
								recursionFieldEntity(sb, entry);
							}
						}
						return sb.toString();
					}
				}
			}
			return "";
		}

		/**
		 * �ݹ鴦������ź�
		 * 
		 * @param sb ����ַ�������
		 * @param fieldEntity �����ź�
		 */
		private void recursionFieldEntity(StringBuffer sb, Entity fieldEntity)
		{
			String fieldName;
			try
			{
				Field field = (Field) fieldEntity;
				fieldName = field.getName();
				String value = (String) field.getValue();
				if (hex)
				{
					value = "0x" + Integer.toHexString(Integer.parseInt(field.getValue()));
				}

				if (fieldName != null && !fieldName.equals("") && value != null && !value.equals(""))
				{
					if (sb.length() > 0)
					{
						sb.append("; ");
					}
					// ����"�ź����� = �ź�ֵ���ź����� = �ź�ֵ��......"�ķ�ʽ����
					sb.append(fieldName).append("=").append(value);
				}

				List<Entity> children = field.getChildren();
				if (children != null && children.size() > 0)
				{
					for (int j = 0; j < children.size(); j++)
					{
						Entity entity = children.get(j);
						if (entity instanceof Field)
						{
							recursionFieldEntity(sb, entity);
						}
					}
				}
			} catch (IllegalArgumentException e)
			{
				e.printStackTrace();
			}
			return;
		}
	}
}
