/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
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
 * �������ͼ
 * 
 * @author ���Ρ 2012-3-1
 */
public abstract class Sequence
{

	/** ͷ�Ŀ�� */
	public final static int			HEADER_WIDTH			= 100;

	/** ͷ��ͷ֮��ļ�� */
	public final static int			HEADER_DISTANCE			= 100;

	/** ͷ�ĸ߶� */
	public final static int			HEADER_HEIGHT			= 40;

	/** ��һ��ͷ��ƫ���� */
	public final static int			FIRST_HEADER_X_OFFSET	= 100;

	/** ����ͼ������Ĭ�ϸ߶� */
	public final static int			CANVAS_DEFAULT_HEIGHT	= 5000;

	/** ����ͼ������Ĭ�Ͽ�� */
	public final static int			CANVAS_DEFAULT_WIDTH	= 5000;

	/** �ڵ�Ŀ�� */
	public final static int			NODE_WIDTH				= 5;

	/** �ڵ�ĸ߶� */
	public final static int			NODE_HEIGHT				= 20;

	/** �ڵ�֮��ļ�� */
	public final static int			NODE_INTERVAL			= 5;

	public final static String		EVENT_SCALE				= "scale";

	public final static String		EVENT_TIME_BOUND		= "timeBound";

	// �����
	protected Composite				panel;

	// ͷ�ĸ���
	protected int					headerSum				= 0;

	// ͷ������
	protected Canvas				headerCanvas;

	// ͷͼ������
	protected HeaderContainerFgr	headerContainerFgr;

	// ʱ��ͼ����
	protected SequenceContainerFgr	msgContainerFgr;

	// ʱ��ͼ���
	protected ScrolledComposite		sequencePanel;

	// ʱ��ͼ�ײ����Ҳ����
	protected Composite				rightPlaceHolder;

	// ʱ��ͼ�ײ����в����
	protected Composite				midlePanel;

	// ʱ��ͼ�ײ��������
	protected Composite				leftPlaceHolder;

	// ͷ���
	protected Composite				headerPanel;

	// �ȽϽ�����ؽ��
	protected List<Result>			resultList;

	// ICD���󼯺�
	protected ClazzManager			icdManager;

	protected ClazzManager			caseManager;

	/** ������󼯺� */
	protected List<Entity>			testedObjects;

	// �ڵ�ĸ���
	protected int					nodeNumber				= 200;

	// ��ǰҳ��
	protected int					currentPageNum			= 1;

	protected int					pageSum;

	// ��һҳ
	protected Label					lblFirst;

	// ��һҳ
	protected Label					lblPrevious;

	// ��һҳ
	protected Label					lblNext;

	// ���һҳ
	protected Label					lblLast;

	protected Text					txtPageNum;

	protected Composite				timePanel;

	// ��Ϊͷ�Ľڵ�
	private List<FunctionNode>		headerNodes;

	// ��������
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
	 * ��ȡ��ͷ��������ʾ�Ľڵ㼯��
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
	 * ��ȡ����ͼ�εĸ����
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
	 * ������ǰҳ����Ϣ
	 */
	public void paint(Composite panel)
	{
		// ͷ����
		headerPanel = new Composite(panel, SWT.BORDER);
		headerPanel.setBackground(ColorConstants.white);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 60;
		headerPanel.setLayoutData(gridData);

		// ʱ��ͼ����
		sequencePanel = new ScrolledComposite(panel, SWT.H_SCROLL | SWT.V_SCROLL);
		sequencePanel.setBackground(ColorConstants.white);
		gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		sequencePanel.setLayoutData(gridData);

		// ʱ���������
		this.timePanel = new Composite(panel, SWT.BORDER);
		this.timePanel.setBackground(ColorConstants.white);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 50;
		this.timePanel.setLayoutData(gridData);
	}

	/**
	 * ���»���ǰҳ
	 */
	public abstract void repaint();

	/**
	 * ����ʱ��̶Ⱥ�ʱ�䷶Χ
	 * 
	 * @param scale
	 * @param timeBound
	 */
	public abstract void updateCfg(int scale, int timeBound);

	/**
	 * ��ȡ��Ҫ��ʾ�Ľڵ������IDs
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
	 * ��װͷ����
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

		// ��û���κ���Ϣ�Ľڵ���˵�
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

		// ��ͷ���Ĵ�С�����仯ʱӦ�����»�headerCanvas
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
	 * ��first��ǩ��ֹ
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
	 * ��first��ǩ����
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
	 * ��previous��ǩ��ֹ
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
	 * ��previous��ǩ����
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
	 * ��next��ǩ����
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
	 * ��next��ǩ��ֹ
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
	 * ��last��ǩ����
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
	 * ��last��ǩ��ֹ
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
	 * ��ʾʱ��ͼ����ͼ��
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
	 * ��ʾ����
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
	 * ��װʱ��ͼ����
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
		// ����ͼ����
		msgContainerFgr = this.showMsgContianerFgr();
		lws.setContents(msgContainerFgr);
	}

	/**
	 * ����ִ�н�����ؽ��
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
	 * ��Ⱦ������ </br>
	 */
	private void renderError()
	{
		IPreferenceStore store = CfgPlugin.getDefault().getPreferenceStore();
		HandlerTypesEnum unexpected = HandlerTypesEnum.valueOf(store.getInt(ErrorTypesEnum.UNEXPECTED.getName()));
		HandlerTypesEnum timeout = HandlerTypesEnum.valueOf(store.getInt(ErrorTypesEnum.TIMEOUT.getName()));
		// ��������������������������
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
	 * ��װ��Ϣҳ���
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
	 * ��Ϣҳ�󲿷����
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
	 * ��Ϣҳ�м䲿�����
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
				// ��first��ֹ
				disableFirst();
				// ��previous��ֹ
				disablePrevious();

				if (pageSum > 1)
				{
					// ����next
					enableNext();
					// ����last
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
					// ��previous��ǩ��ֹ
					disablePrevious();
					// ��first��ǩ��ֹ
					disableFirst();
				}

				if (pageSum > 1)
				{
					// ��next��ǩ����
					enableNext();
					// ��last��ǩ����
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
					// ��ֹnext
					disableNext();
					// ��last��ǩ��ֹ
					disableLast();
				} else
				{
					lblNext.setImage(this.imgNext);
					// ��last��ǩ��ֹ
					enableLast();
				}

				if (pageSum > 1)
				{
					// ��previous��ǩ����
					enablePrevious();
					// ��first��ǩ����
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
				// ��last��ǩ��ֹ
				disableLast();
				// ��previous��ǩ����
				enablePrevious();
				// ��first��ǩ����
				enableFirst();
				// ��next��ǩ��ֹ
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
	 * ��Ϣҳ�Ҳ������
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