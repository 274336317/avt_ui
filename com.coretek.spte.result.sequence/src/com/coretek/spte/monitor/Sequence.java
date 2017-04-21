/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TypedListener;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.template.ClazzManager;
import com.coretek.common.template.ICDMsg;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.spte.FunctionNode;
import com.coretek.spte.cfg.CfgPlugin;
import com.coretek.spte.cfg.ErrorTypesEnum;
import com.coretek.spte.cfg.HandlerTypesEnum;
import com.coretek.spte.dataCompare.Result;
import com.coretek.spte.monitor.actions.AbstractMouseListener;
import com.coretek.spte.monitor.actions.AbstractMouseTrackListener;
import com.coretek.spte.monitor.figures.HeaderContainerFgr;
import com.coretek.spte.monitor.figures.HeaderFgr;
import com.coretek.spte.monitor.figures.SequenceContainerFgr;
import com.coretek.spte.testcase.TestCase;

/**
 * 监控序列图
 * 
 * @author 孙大巍 2012-3-1
 */
public abstract class Sequence
{

	/** 头的宽度 */
	public final static int			HEADER_WIDTH			= 100;

	/** 头与头之间的间隔 */
	public final static int			HEADER_DISTANCE			= 100;

	/** 头的高度 */
	public final static int			HEADER_HEIGHT			= 40;

	/** 第一个头的偏移量 */
	public final static int			FIRST_HEADER_X_OFFSET	= 100;

	/** 序列图画布的默认高度 */
	public final static int			CANVAS_DEFAULT_HEIGHT	= 5000;

	/** 序列图画布的默认宽度 */
	public final static int			CANVAS_DEFAULT_WIDTH	= 5000;

	/** 节点的宽度 */
	public final static int			NODE_WIDTH				= 5;

	/** 节点的高度 */
	public final static int			NODE_HEIGHT				= 20;

	/** 节点之间的间隔 */
	public final static int			NODE_INTERVAL			= 5;

	public final static String		EVENT_SCALE				= "scale";

	public final static String		EVENT_TIME_BOUND		= "timeBound";

	// 父面板
	protected Composite				panel;

	// 头的个数
	protected int					headerSum				= 0;

	// 头部画布
	protected Canvas				headerCanvas;

	// 头图形容器
	protected HeaderContainerFgr	headerContainerFgr;

	// 时序图容器
	protected SequenceContainerFgr	msgContainerFgr;

	// 时序图面板
	protected ScrolledComposite		sequencePanel;

	// 时序图底部的右部面板
	protected Composite				rightPlaceHolder;

	// 时序图底部的中部面板
	protected Composite				midlePanel;

	// 时序图底部的左部面板
	protected Composite				leftPlaceHolder;

	// 头面板
	protected Composite				headerPanel;

	// 比较结果或监控结果
	protected List<Result>			resultList;

	// ICD对象集合
	protected ClazzManager			icdManager;

	protected ClazzManager			caseManager;

	/** 被测对象集合 */
	protected List<Entity>			testedObjects;

	// 节点的个数
	protected int					nodeNumber				= 200;

	// 当前页号
	protected int					currentPageNum			= 1;

	protected int					pageSum;

	// 第一页
	protected Label					lblFirst;

	// 上一页
	protected Label					lblPrevious;

	// 下一页
	protected Label					lblNext;

	// 最后一页
	protected Label					lblLast;

	protected Text					txtPageNum;

	protected Composite				timePanel;

	// 做为头的节点
	private List<FunctionNode>		headerNodes;

	// 测试用例
	private TestCase				testCase;

	public Sequence(final List<Result> resultList)
	{
		this.resultList = resultList;
	}

	/**
	 * @return the testCase <br/>
	 * 
	 */
	public TestCase getTestCase()
	{
		return testCase;
	}

	/**
	 * 获取在头容器中显示的节点集合
	 * 
	 * @return the headerNodes <br/>
	 * 
	 */
	public List<FunctionNode> getHeaderNodes()
	{

		return Collections.unmodifiableList(this.headerNodes);
	}

	/**
	 * @return the resultList <br/>
	 */
	public List<Result> getResultList()
	{
		return resultList;
	}

	/**
	 * 获取整个图形的父面板
	 * 
	 * @return the panel <br/>
	 */
	protected final Composite getPanel()
	{
		return panel;
	}

	/**
	 * @return the sequencePanel <br/>
	 */
	public ScrolledComposite getSequencePanel()
	{
		return sequencePanel;
	}

	/**
	 * 画出当前页的消息
	 */
	public void paint(Composite panel)
	{
		// 头容器
		headerPanel = new Composite(panel, SWT.BORDER);
		headerPanel.setBackground(ColorConstants.white);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 60;
		headerPanel.setLayoutData(gridData);

		// 时序图容器
		sequencePanel = new ScrolledComposite(panel, SWT.H_SCROLL | SWT.V_SCROLL);
		sequencePanel.setBackground(ColorConstants.white);
		gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		sequencePanel.setLayoutData(gridData);

		// 时间面板容器
		this.timePanel = new Composite(panel, SWT.BORDER);
		this.timePanel.setBackground(ColorConstants.white);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 50;
		this.timePanel.setLayoutData(gridData);
	}

	/**
	 * 重新画当前页
	 */
	public abstract void repaint();

	/**
	 * 更新时间刻度和时间范围
	 * 
	 * @param scale
	 * @param timeBound
	 */
	public abstract void updateCfg(int scale, int timeBound);

	/**
	 * 获取将要显示的节点的所有IDs
	 * 
	 * @return
	 */
	protected Set<Integer> getNodeIDsToShow()
	{
		Set<Integer> set = new HashSet<Integer>();
		for (Result result : this.resultList)
		{
			ICDMsg icdMsg = result.getSpteMsg().getICDMsg();
			set.addAll(icdMsg.getDestIDs());
			set.add(Integer.valueOf(icdMsg.getAttribute("sourceFunctionID").getValue().toString()));
		}

		return set;
	}

	/**
	 * 组装头容器
	 * 
	 * @param headerPanel </br>
	 */
	protected void showHeaderPanel(final Composite headerPanel)
	{
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		headerPanel.setLayout(gridLayout);
		this.headerCanvas = new Canvas(headerPanel, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		this.headerCanvas.setLayoutData(gridData);
		LightweightSystem lws = new LightweightSystem(this.headerCanvas);

		this.headerContainerFgr = new HeaderContainerFgr();
		this.headerContainerFgr.setFill(true);
		this.headerContainerFgr.setOpaque(true);
		this.headerContainerFgr.setOutline(true);
		this.headerContainerFgr.setBackgroundColor(ColorConstants.white);
		lws.setContents(this.headerContainerFgr);

		Set<Integer> IDs = this.getNodeIDsToShow();

		this.headerNodes = this.icdManager.getAllFunctionNodes();

		// 将没有任何消息的节点过滤掉
		Iterator<FunctionNode> it = this.headerNodes.iterator();
		while (it.hasNext())
		{
			FunctionNode node = it.next();
			if (!IDs.contains(node.getID()))
			{
				it.remove();
			}
		}
		this.headerSum = this.headerNodes.size();
		for (int i = 0; i < this.headerSum; i++)
		{
			FunctionNode node = this.headerNodes.get(i);
			HeaderFgr headerFgr = new HeaderFgr(node.getID(), node.getName());
			headerFgr.setBounds(new Rectangle((HEADER_WIDTH + HEADER_DISTANCE) * i, 10, HEADER_WIDTH, HEADER_HEIGHT));

			this.headerContainerFgr.add(headerFgr);
		}

		// 当头面板的大小发生变化时应当重新画headerCanvas
		headerPanel.addControlListener(new ControlListener()
		{
			@Override
			public void controlMoved(ControlEvent e)
			{
				this.resize();
			}

			@Override
			public void controlResized(ControlEvent e)
			{
				this.resize();
			}

			private void resize()
			{
				int width = headerPanel.getBounds().width;
				int height = headerPanel.getBounds().height;
				headerCanvas.setSize(width, height);
				headerContainerFgr.setBounds(new Rectangle(0, 0, width, height));
				headerCanvas.redraw();
			}

		});
		int width = headerPanel.getBounds().width;
		int height = headerPanel.getBounds().height;
		headerCanvas.setSize(width, height);
		headerContainerFgr.setBounds(new Rectangle(0, 0, width, height));
	}

	/**
	 * 将first标签禁止
	 * 
	 */
	protected void disableFirst()
	{
		TypedListener typedListener = (TypedListener) lblFirst.getListeners(SWT.MouseUp)[0];
		AbstractMouseListener listener = (AbstractMouseListener) typedListener.getEventListener();
		listener.setDisabled(true);
		lblFirst.setImage(SequencePlugin.getDefault().getImage("/icons/firstDisabled.gif"));

		typedListener = (TypedListener) lblFirst.getListeners(SWT.MouseHover)[0];
		AbstractMouseTrackListener trackListener = (AbstractMouseTrackListener) typedListener.getEventListener();
		trackListener.setDisabled(true);
	}

	/**
	 * 将first标签激活
	 * 
	 */
	protected void enableFirst()
	{
		TypedListener typedListener = (TypedListener) lblFirst.getListeners(SWT.MouseUp)[0];
		AbstractMouseListener listener = (AbstractMouseListener) typedListener.getEventListener();
		listener.setDisabled(false);
		lblFirst.setImage(SequencePlugin.getDefault().getImage("/icons/firstPage.gif"));

		typedListener = (TypedListener) lblFirst.getListeners(SWT.MouseHover)[0];
		AbstractMouseTrackListener trackListener = (AbstractMouseTrackListener) typedListener.getEventListener();
		trackListener.setDisabled(false);
	}

	/**
	 * 将previous标签禁止
	 * 
	 */
	protected void disablePrevious()
	{
		TypedListener typedListener = (TypedListener) lblPrevious.getListeners(SWT.MouseUp)[0];
		AbstractMouseListener listener = (AbstractMouseListener) typedListener.getEventListener();
		listener.setDisabled(true);
		Image image = SequencePlugin.getDefault().getImage("/icons/previousDisabled.gif");
		lblPrevious.setImage(image);

		typedListener = (TypedListener) lblPrevious.getListeners(SWT.MouseHover)[0];
		AbstractMouseTrackListener trackListener = (AbstractMouseTrackListener) typedListener.getEventListener();
		trackListener.setDisabled(true);
	}

	/**
	 * 将previous标签激活
	 * 
	 */
	protected void enablePrevious()
	{
		TypedListener typedListener = (TypedListener) lblPrevious.getListeners(SWT.MouseUp)[0];
		AbstractMouseListener listener = (AbstractMouseListener) typedListener.getEventListener();
		listener.setDisabled(false);
		Image image = SequencePlugin.getDefault().getImage("/icons/previousPage.gif");
		lblPrevious.setImage(image);

		typedListener = (TypedListener) lblPrevious.getListeners(SWT.MouseHover)[0];
		AbstractMouseTrackListener trackListener = (AbstractMouseTrackListener) typedListener.getEventListener();
		trackListener.setDisabled(false);
	}

	/**
	 * 将next标签激活
	 * 
	 */
	protected void enableNext()
	{
		TypedListener typedListener = (TypedListener) lblNext.getListeners(SWT.MouseUp)[0];
		AbstractMouseListener listener = (AbstractMouseListener) typedListener.getEventListener();
		listener.setDisabled(false);
		lblNext.setImage(SequencePlugin.getDefault().getImage("/icons/nextPage.gif"));

		typedListener = (TypedListener) lblNext.getListeners(SWT.MouseHover)[0];
		AbstractMouseTrackListener trackListener = (AbstractMouseTrackListener) typedListener.getEventListener();
		trackListener.setDisabled(false);
	}

	/**
	 * 将next标签禁止
	 */
	protected void disableNext()
	{
		TypedListener typedListener = (TypedListener) lblNext.getListeners(SWT.MouseUp)[0];
		AbstractMouseListener listener = (AbstractMouseListener) typedListener.getEventListener();
		listener.setDisabled(true);

		lblNext.setImage(SequencePlugin.getDefault().getImage("/icons/nextDisabled.gif"));
		typedListener = (TypedListener) lblNext.getListeners(SWT.MouseHover)[0];
		AbstractMouseTrackListener trackListener = (AbstractMouseTrackListener) typedListener.getEventListener();
		trackListener.setDisabled(true);
	}

	/**
	 * 将last标签激活
	 */
	protected void enableLast()
	{
		TypedListener typedListener = (TypedListener) lblLast.getListeners(SWT.MouseUp)[0];
		AbstractMouseListener listener = (AbstractMouseListener) typedListener.getEventListener();
		listener.setDisabled(false);

		typedListener = (TypedListener) lblLast.getListeners(SWT.MouseHover)[0];
		AbstractMouseTrackListener trackListener = (AbstractMouseTrackListener) typedListener.getEventListener();
		trackListener.setDisabled(false);
		lblLast.setImage(SequencePlugin.getDefault().getImage("/icons/lastPage.gif"));
	}

	/**
	 * 将last标签禁止
	 */
	protected void disableLast()
	{
		TypedListener typedListener = (TypedListener) lblLast.getListeners(SWT.MouseUp)[0];
		AbstractMouseListener listener = (AbstractMouseListener) typedListener.getEventListener();
		listener.setDisabled(true);

		typedListener = (TypedListener) lblLast.getListeners(SWT.MouseHover)[0];
		AbstractMouseTrackListener trackListener = (AbstractMouseTrackListener) typedListener.getEventListener();
		trackListener.setDisabled(true);
		lblLast.setImage(SequencePlugin.getDefault().getImage("/icons/lastDisabled.gif"));
	}

	/**
	 * 显示时序图容器图形
	 * 
	 * @return
	 */
	protected SequenceContainerFgr showMsgContianerFgr()
	{
		SequenceContainerFgr containerFgr = new SequenceContainerFgr();
		containerFgr.setFill(true);
		containerFgr.setOpaque(true);
		containerFgr.setOutline(true);
		containerFgr.setBackgroundColor(ColorConstants.white);
		containerFgr.setBounds(new Rectangle(0, 0, CANVAS_DEFAULT_WIDTH, CANVAS_DEFAULT_HEIGHT));

		return containerFgr;
	}

	/**
	 * 显示画布
	 * 
	 * @return
	 */
	protected Canvas showCanvas()
	{
		final Canvas canvas = new Canvas(sequencePanel, SWT.NONE);
		GridData gridData = new GridData();
		if (sequencePanel.getBounds().height > CANVAS_DEFAULT_HEIGHT)
		{
			gridData.heightHint = sequencePanel.getBounds().height;
		} else
		{
			gridData.heightHint = CANVAS_DEFAULT_HEIGHT;
		}

		gridData.widthHint = this.headerSum * (HEADER_WIDTH + HEADER_DISTANCE) + 100;
		canvas.setSize(gridData.widthHint, gridData.heightHint);
		canvas.setLayoutData(gridData);
		canvas.setBackground(ColorConstants.white);
		sequencePanel.setContent(canvas);
		canvas.addControlListener(new ControlListener()
		{
			int	x	= canvas.getBounds().x;

			@Override
			public void controlMoved(ControlEvent e)
			{
				if (x == canvas.getBounds().x)
				{
					return;
				}
				this.x = canvas.getBounds().x;
				headerCanvas.setBounds(canvas.getBounds().x, 0, canvas.getBounds().width, canvas.getBounds().height);
				headerCanvas.redraw();
			}

			@Override
			public void controlResized(ControlEvent e)
			{

			}

		});

		return canvas;
	}

	/**
	 * 组装时序图容器
	 * 
	 * @param sequencePanel
	 */
	protected void showSequencePanel(final ScrolledComposite sequencePanel)
	{
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		sequencePanel.setLayout(gridLayout);
		Canvas canvas = this.showCanvas();
		LightweightSystem lws = new LightweightSystem(canvas);
		// 序列图容器
		msgContainerFgr = this.showMsgContianerFgr();
		lws.setContents(msgContainerFgr);
	}

	/**
	 * 设置执行结果或监控结果
	 * 
	 * @param resultList the resultList to set <br/>
	 * 
	 */
	public void setResultList(List<Result> resultList, ClazzManager icdManager, ClazzManager caseManager, List<Entity> testedObjects, TestCase testCase)
	{
		this.icdManager = icdManager;
		this.resultList = resultList;
		this.caseManager = caseManager;
		this.testedObjects = testedObjects;
		this.testCase = testCase;
		this.renderError();

	}

	/**
	 * @return the icdManager <br/>
	 * 
	 */
	public ClazzManager getIcdManager()
	{
		return icdManager;
	}

	public void setIcdManager(ClazzManager icdManager)
	{
		this.icdManager = icdManager;
	}

	/**
	 * @return the caseManager <br/>
	 * 
	 */
	public ClazzManager getCaseManager()
	{
		return caseManager;
	}

	/**
	 * @return the testedObjects <br/>
	 * 
	 */
	public List<Entity> getTestedObjects()
	{
		return testedObjects;
	}

	/**
	 * 渲染错误处理 </br>
	 */
	private void renderError()
	{
		IPreferenceStore store = CfgPlugin.getDefault().getPreferenceStore();
		HandlerTypesEnum unexpected = HandlerTypesEnum.valueOf(store.getInt(ErrorTypesEnum.UNEXPECTED.getName()));
		HandlerTypesEnum timeout = HandlerTypesEnum.valueOf(store.getInt(ErrorTypesEnum.TIMEOUT.getName()));
		// 将错误处理与错误类型相关联起来
		for (Result result : this.resultList)
		{
			if (result.getErrorTypes().size() != 0)
			{
				for (ErrorTypesEnum type : result.getErrorTypes())
				{
					if (type != null)
					{
						switch (type)
						{
							case TIMEOUT:
							{
								result.addHandlerType(timeout);
								break;
							}
							case UNEXPECTED:
							{
								result.addHandlerType(unexpected);
								break;
							}
						}
					}

				}

			}
		}
	}

	/**
	 * 组装消息页面板
	 * 
	 */
	protected void showGotoPanel(final Composite timePanel)
	{
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.makeColumnsEqualWidth = true;
		timePanel.setLayout(gridLayout);
		showGotoPanelLeft(timePanel);
		showGotoPanelMiddle(timePanel);
		showGotoPanleRight(timePanel);
	}

	/**
	 * 消息页左部分面板
	 * 
	 * @param timePanel
	 */
	protected void showGotoPanelLeft(final Composite timePanel)
	{
		leftPlaceHolder = new Composite(timePanel, SWT.NONE);
		leftPlaceHolder.setBackground(ColorConstants.white);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		leftPlaceHolder.setLayoutData(gridData);
	}

	/**
	 * 消息页中间部分面板
	 * 
	 * @param timePanel
	 */
	protected void showGotoPanelMiddle(final Composite timePanel)
	{

		midlePanel = new Composite(timePanel, SWT.BORDER);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 5;
		midlePanel.setLayout(gridLayout);
		GridData gridData = new GridData();
		gridData.widthHint = 270;
		midlePanel.setLayoutData(gridData);

		lblFirst = new Label(midlePanel, SWT.NONE);
		Image img = SequencePlugin.getDefault().getImage("/icons/firstDisabled.gif");
		lblFirst.setImage(img);
		lblFirst.setToolTipText(Messages.getString("firstPage"));
		gridData = new GridData();
		gridData.widthHint = 32;
		gridData.heightHint = 22;
		lblFirst.setLayoutData(gridData);

		lblFirst.addMouseListener(new AbstractMouseListener(true)
		{

			Image	imgFirstMouseDown	= SequencePlugin.getDefault().getImage("/icons/firstMouseDown.gif");

			@Override
			public void mouseDoubleClick(MouseEvent e)
			{

			}

			@Override
			public void mouseDown(MouseEvent e)
			{
				if (!this.isDisabled())
				{
					Label btnFirst = (Label) e.getSource();
					btnFirst.setImage(imgFirstMouseDown);
				}

			}

			@Override
			public void mouseUp(MouseEvent e)
			{
				if (this.isDisabled())
					return;

				currentPageNum = 1;
				// 将first禁止
				disableFirst();
				// 将previous禁止
				disablePrevious();

				if (pageSum > 1)
				{
					// 激活next
					enableNext();
					// 激活last
					enableLast();
				}

				repaint();
			}

		});

		lblFirst.addMouseTrackListener(new AbstractMouseTrackListener(true)
		{

			Image	imgFirstMouseOn	= SequencePlugin.getDefault().getImage("/icons/firstMouseOn.gif");

			Image	imgFirstPage	= SequencePlugin.getDefault().getImage("/icons/firstPage.gif");

			@Override
			public void mouseEnter(MouseEvent e)
			{

			}

			@Override
			public void mouseExit(MouseEvent e)
			{
				if (!this.isDisabled())
					lblFirst.setImage(imgFirstPage);

			}

			@Override
			public void mouseHover(MouseEvent e)
			{
				if (!this.isDisabled())
					lblFirst.setImage(imgFirstMouseOn);

			}

		});

		lblPrevious = new Label(midlePanel, SWT.NONE);
		img = SequencePlugin.getDefault().getImage("/icons/previousDisabled.gif");
		lblPrevious.setImage(img);
		lblPrevious.setToolTipText(Messages.getString("previousPage"));
		gridData = new GridData();
		gridData.widthHint = 32;
		gridData.heightHint = 22;
		lblPrevious.setLayoutData(gridData);
		lblPrevious.addMouseListener(new AbstractMouseListener(true)
		{

			Image	imgPreviousMouseDown	= SequencePlugin.getDefault().getImage("/icons/previousMouseDown.gif");

			@Override
			public void mouseDoubleClick(MouseEvent e)
			{

			}

			@Override
			public void mouseDown(MouseEvent e)
			{
				if (!this.isDisabled())
					lblPrevious.setImage(imgPreviousMouseDown);

			}

			@Override
			public void mouseUp(MouseEvent e)
			{
				if (this.isDisabled())
					return;
				currentPageNum--;
				if (currentPageNum == 1)
				{
					// 将previous标签禁止
					disablePrevious();
					// 将first标签禁止
					disableFirst();
				}

				if (pageSum > 1)
				{
					// 将next标签激活
					enableNext();
					// 将last标签激活
					enableLast();
				}

				repaint();
			}

		});
		lblPrevious.addMouseTrackListener(new AbstractMouseTrackListener(true)
		{

			Image	imgPreviousMouseOn	= SequencePlugin.getDefault().getImage("/icons/previousMouseOn.gif");

			Image	imgPreviousPage		= SequencePlugin.getDefault().getImage("/icons/previousPage.gif");

			@Override
			public void mouseEnter(MouseEvent e)
			{

			}

			@Override
			public void mouseExit(MouseEvent e)
			{
				if (this.isDisabled())
					return;

				lblPrevious.setImage(imgPreviousPage);

			}

			@Override
			public void mouseHover(MouseEvent e)
			{
				if (this.isDisabled())
					return;

				lblPrevious.setImage(imgPreviousMouseOn);
			}

		});

		txtPageNum = new Text(midlePanel, SWT.BORDER);
		txtPageNum.setFocus();
		gridData = new GridData();
		gridData.widthHint = 100;
		gridData.heightHint = 15;
		txtPageNum.setLayoutData(gridData);

		lblNext = new Label(midlePanel, SWT.NONE);
		img = SequencePlugin.getDefault().getImage("/icons/nextDisabled.gif");

		lblNext.setImage(img);
		lblNext.setToolTipText(Messages.getString("nextPage"));
		gridData = new GridData();
		gridData.widthHint = 32;
		gridData.heightHint = 22;
		lblNext.setLayoutData(gridData);

		lblNext.addMouseListener(new AbstractMouseListener(true)
		{

			Image	imgNextMouseDown	= SequencePlugin.getDefault().getImage("/icons/nextMouseDown.gif");

			Image	imgNext				= SequencePlugin.getDefault().getImage("/icons/nextDisabled.gif");

			@Override
			public void mouseDoubleClick(MouseEvent e)
			{

			}

			@Override
			public void mouseDown(MouseEvent e)
			{
				if (this.isDisabled())
					return;

				lblNext.setImage(imgNextMouseDown);
			}

			@Override
			public void mouseUp(MouseEvent e)
			{
				if (this.isDisabled())
					return;
				currentPageNum++;
				if (currentPageNum == pageSum)
				{
					// 禁止next
					disableNext();
					// 将last标签禁止
					disableLast();
				} else
				{
					lblNext.setImage(this.imgNext);
					// 将last标签禁止
					enableLast();
				}

				if (pageSum > 1)
				{
					// 将previous标签激活
					enablePrevious();
					// 将first标签激活
					enableFirst();
				}

				repaint();
			}

		});
		lblNext.addMouseTrackListener(new AbstractMouseTrackListener(true)
		{

			Image	imgNextMouseOn	= SequencePlugin.getDefault().getImage("/icons/nextMouseOn.gif");

			Image	imgNextPage		= SequencePlugin.getDefault().getImage("/icons/nextPage.gif");

			@Override
			public void mouseEnter(MouseEvent e)
			{

			}

			@Override
			public void mouseExit(MouseEvent e)
			{
				if (this.isDisabled())
					return;

				lblNext.setImage(imgNextPage);

			}

			@Override
			public void mouseHover(MouseEvent e)
			{
				if (this.isDisabled())
					return;

				lblNext.setImage(imgNextMouseOn);
			}

		});

		lblLast = new Label(midlePanel, SWT.NONE);

		img = SequencePlugin.getDefault().getImage("/icons/lastDisabled.gif");
		lblLast.setImage(img);
		lblLast.setToolTipText(Messages.getString("lastPage"));
		gridData = new GridData();
		gridData.widthHint = 32;
		gridData.heightHint = 22;
		lblLast.setLayoutData(gridData);
		lblLast.addMouseListener(new AbstractMouseListener(true)
		{

			Image	imgLastMouseDown	= SequencePlugin.getDefault().getImage("/icons/lastMouseDown.gif");

			@Override
			public void mouseDoubleClick(MouseEvent e)
			{

			}

			@Override
			public void mouseDown(MouseEvent e)
			{
				if (this.isDisabled())
					return;

				lblLast.setImage(imgLastMouseDown);
			}

			@Override
			public void mouseUp(MouseEvent e)
			{
				if (this.isDisabled())
					return;

				currentPageNum = pageSum;
				this.setDisabled(true);
				// 将last标签禁止
				disableLast();
				// 将previous标签激活
				enablePrevious();
				// 将first标签激活
				enableFirst();
				// 将next标签禁止
				disableNext();

				repaint();
			}

		});
		lblLast.addMouseTrackListener(new AbstractMouseTrackListener(true)
		{

			Image	imgLastMouseOn	= SequencePlugin.getDefault().getImage("/icons/lastMouseOn.gif");

			Image	imgLastPage		= SequencePlugin.getDefault().getImage("/icons/lastPage.gif");

			@Override
			public void mouseEnter(MouseEvent e)
			{

			}

			@Override
			public void mouseExit(MouseEvent e)
			{
				if (this.isDisabled())
					return;

				lblLast.setImage(imgLastPage);

			}

			@Override
			public void mouseHover(MouseEvent e)
			{
				if (this.isDisabled())
					return;

				lblLast.setImage(imgLastMouseOn);
			}

		});
	}

	/**
	 * 消息页右部分面板
	 * 
	 * @param timePanel
	 */
	protected void showGotoPanleRight(final Composite timePanel)
	{
		rightPlaceHolder = new Composite(timePanel, SWT.NONE);
		rightPlaceHolder.setBackground(ColorConstants.white);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		rightPlaceHolder.setLayoutData(gridData);
	}

}