/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.testcase.curve.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.Polyline;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.part.ViewPart;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.template.Constants;
import com.coretek.common.template.ICDField;
import com.coretek.common.template.ICDFunctionSubDomain;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.core.TestResultBean;
import com.coretek.spte.monitor.manager.Event;
import com.coretek.spte.monitor.ui.manager.MonitorEventManager;
import com.coretek.spte.monitor.ui.manager.TimeStampManager;
import com.coretek.spte.testcase.Field;
import com.coretek.spte.testcase.Period;
import com.coretek.testcase.curve.CurveViewPlugin;
import com.coretek.testcase.curve.dialogs.ConfigFieldsDialog;
import com.coretek.testcase.curve.dialogs.ConfigFieldsPropertyDialog;
import com.coretek.testcase.curve.figures.ContainerFgr;
import com.coretek.testcase.curve.figures.PointFgr;
import com.coretek.testcase.curve.internal.model.CurveConst;
import com.coretek.testcase.curve.internal.model.FieldElement;
import com.coretek.testcase.curve.internal.model.FieldElementSet;
import com.coretek.testcase.curve.internal.model.FieldElementSetManager;
import com.coretek.testcase.curve.preference.CurvePreferenceManager;

/**
 * ���߼����ͼ
 * 
 * @author ���� 2012-3-14
 */
public class CurveView extends ViewPart
{

	public CurveViewObservable		observable		= new CurveViewObservable();

	// RGB��Imageӳ��
	private static Map<RGB, Image>	map				= new HashMap<RGB, Image>();

	// ���߻�������
	private Canvas					canvas;

	// ���߶���
	private ContainerFgr			container;

	// ��ǰʱ���
	private long					currentTimeStamp;

	// ����ʱ���
	private long					endTimeStamp;

	// ��ת��ť
	private Button					goBtn;

	// �Ƿ��������
	private volatile boolean		isLockMonitor	= false;

	// �Ƿ���������
	private boolean					isMouseDown		= false;

	// ��ǰ��ͼ�Ƿ�ע��
	private boolean					isRegister		= false;

	// �źŶ��󼯺Ϲ�����
	private FieldElementSetManager	manager;

	// ����������ʱ��X�������λ��
	private int						mouseLocate_X	= 0;

	// ���߻�ͼ�߳�
	private PaintThread				paintThread		= null;

	// �����ͼ��ʱ����
	private int						paintTime;

	// ������������
	private Composite				parent;

	// ��ѡ�����ù�����
	private CurvePreferenceManager	preferenceManager;

	// ��ʼʱ���
	private long					startTimeStamp;

	// ������ͼ����
	private TableTreeViewer			tableTreeViewer;

	// ����ͼ����
	private TableViewer				tableViewer;

	// �Ƿ��������
	private volatile boolean		termination		= true;

	// ��ת��ʱ����ı���
	private Text					timeTxt;

	private FieldElementSet			selectFieldElementSet;

	// ��ؽڵ�����ͼ��
	private Image					configImage;

	// ���ü�����򹤾�����ťͼ��
	private Image					configZoneImage;

	// ��ռ�����ݹ�������ťͼ��
	private Image					refreshImage;

	// ������������ťͼ��
	private Image					clearImage;

	// ��һҳ��������ťͼ��
	private Image					previousImage;

	// ��һҳ��������ťͼ��
	private Image					nextImage;

	// �л�δ16����ͼ��
	private Image					hexIamge;

	// �Ƿ�δ16������ʾ��Ϣֵ
	private boolean					hex;

	/**
	 * ������ͼ��ͼ�������������</br>
	 */
	public void clearData()
	{
		List<FieldElementSet> fields = getManager().getAllFields();
		for (FieldElementSet field : fields)
		{
			field.getMap().keySet().clear();
		}

		clearScreen();
	}

	/**
	 * ������ͼ��ͼ�����������</br>
	 */
	public void clearScreen()
	{
		getCanvas().getDisplay().syncExec(new Runnable()
		{
			public void run()
			{
				container.clearSreen();
			}
		});

	}

	/*
	 * (non-Javadoc) ������ͼ�ؼ�
	 * 
	 * @see
	 * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 * .Composite) <br/>
	 */
	@SuppressWarnings("unchecked")
	public void createPartControl(Composite parent)
	{
		init();
		this.parent = parent;

		parent.setLayout(new GridLayout());
		Composite mainPanel = new Composite(parent, SWT.NONE);
		GridLayout grid = new GridLayout();
		grid.numColumns = 1;
		mainPanel.setLayout(grid);
		paintTopToolBar(mainPanel);

		SashForm horizontalSashForm = new SashForm(this.parent, SWT.HORIZONTAL);

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 300;
		horizontalSashForm.setLayoutData(gd);

		SashForm verticalSashForm = new SashForm(horizontalSashForm, SWT.VERTICAL);

		gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 300;
		verticalSashForm.setLayoutData(gd);

		canvas = new Canvas(verticalSashForm, SWT.NO_BACKGROUND);
		canvas.setSize(CurveConst.CANVAS_WIDTH + 225, CurveConst.CANVAS_HEIGHT);
		canvas.setBackground(ColorConstants.darkGray);
		canvas.addMouseTrackListener(new CanvasMouseListener());

		canvas.addKeyListener(new CanvasMouseListener());
		canvas.addMouseListener(new MouseClickListener());

		LightweightSystem lws = new LightweightSystem(canvas);
		this.container = new ContainerFgr(this);
		this.container.setFill(true);
		this.container.setOpaque(true);
		org.eclipse.draw2d.geometry.Rectangle rec = new org.eclipse.draw2d.geometry.Rectangle(canvas.getClientArea().x, canvas.getClientArea().y, canvas.getClientArea().width, canvas.getClientArea().height);
		this.container.setBounds(rec);
		this.container.setOpaque(true);

		// ���沼�Ĵ�С�����仯ʱ���»�
		canvas.addControlListener(new CanvasMouseListener());

		List<Figure> children = (List<Figure>) CurveView.this.container.getChildren();
		for (Figure fgr : children)
		{
			if (fgr instanceof PointFgr)
			{
				Polyline line = new Polyline();
				Point p = new Point(fgr.getBounds().x, fgr.getBounds().y);
				line.setStart(p);

				if (children.indexOf(fgr) != children.size() - 1)
				{
					Figure fgr2 = (Figure) children.get(children.indexOf(fgr) + 1);
					p = new Point(fgr2.getBounds().x, fgr2.getBounds().y);
					line.setEnd(p);
				}
			}
		}
		lws.setContents(this.container);

		createTableViewer(verticalSashForm);

		createTableTreeViewer(horizontalSashForm);

		horizontalSashForm.setWeights(new int[] { 8, 2 });
		verticalSashForm.setWeights(new int[] { 8, 2 });
	}

	@Override
	public void dispose()
	{
		MonitorEventManager.getMonitorEventManager().addObserver(this.getObservable());
		getObservable().deleteObserver(observable);

		if (!this.isTerminated())
		{
			this.setTermination(true);
		}

		Collection<Image> images = map.values();
		for (Image image : images)
		{
			image.dispose();
		}
		map.clear();
		manager.clear();
		if (clearImage != null && !clearImage.isDisposed())
		{
			clearImage.dispose();
		}
		if (configImage != null && !configImage.isDisposed())
		{
			configImage.dispose();
		}
		if (configZoneImage != null && !configZoneImage.isDisposed())
		{
			configZoneImage.dispose();
		}
		if (refreshImage != null && !refreshImage.isDisposed())
		{
			refreshImage.dispose();
		}
		if (previousImage != null && !previousImage.isDisposed())
		{
			previousImage.dispose();
		}
		if (nextImage != null && !nextImage.isDisposed())
		{
			nextImage.dispose();
		}
		if (hexIamge != null && !hexIamge.isDisposed())
		{
			hexIamge.dispose();
		}
		super.dispose();
	}

	/**
	 * ��û�������
	 * 
	 * @return �������� </br>
	 */
	public Canvas getCanvas()
	{
		return canvas;
	}

	/**
	 * ������߻�ͼ����
	 * 
	 * @return ���߻�ͼ���� </br>
	 */
	public ContainerFgr getContainer()
	{
		return container;
	}

	/**
	 * ��õ�ǰʱ���
	 * 
	 * @return ��ǰʱ���
	 */
	public synchronized long getCurrentTimeStamp()
	{
		return currentTimeStamp;
	}

	/**
	 * * ���������ͼ
	 * 
	 * @return ������ͼ
	 */
	public CurveView getDefault()
	{
		return this;
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
	 * ����źż��Ϲ�����
	 * 
	 * @return �źż��Ϲ�����
	 */
	public synchronized FieldElementSetManager getManager()
	{
		return manager;
	}

	/**
	 * ���������ͼ�ػ�ļ��ʱ��
	 * 
	 * @return ������ͼ�ػ�ļ��ʱ��
	 */
	public int getPaintTime()
	{
		return paintTime;
	}

	/**
	 * �����ѡ�����ù�����
	 * 
	 * @return ��ѡ�����ù�����
	 */
	public CurvePreferenceManager getPreferenceManager()
	{
		return preferenceManager;
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
	 * ��ñ�����ͼ����
	 * 
	 * @return ������ͼ����
	 */
	/**
	 * @return
	 */
	public TableTreeViewer getTableTreeViewer()
	{
		return tableTreeViewer;
	}

	public TableViewer getTableViewer()
	{
		return tableViewer;
	}

	/**
	 * ������갴�º�����ƶ��¼�
	 * 
	 * @param hDistance ��갴�º��ƶ���ƫ�ƾ��� </br>
	 */
	public void handelMouseEvet(int hDistance)
	{
		if (getDefault().isTerminated())
		{
			org.eclipse.swt.graphics.Rectangle r = getCanvas().getBounds();

			List<FieldElementSet> fields = getManager().getAllFields();
			int length = fields.size();

			int x = r.x;
			int y = r.y + CurveConst.Y_AXIS_V_TOP_OFFSET;

			// �ɻ�����ĸ߶�
			int height = r.height - (CurveConst.Y_AXIS_V_TOP_OFFSET + CurveConst.Y_AXIS_V_BOTTOM_OFFSET);

			// �ɻ�����Ŀ��
			int width = r.width - (length + 1) / 2 * getPreferenceManager().getColWidth() - (length) / 2 * getPreferenceManager().getColWidth() - 10;

			// ���ÿɻ�ͼ����
			Rectangle paintRect = new Rectangle(x + (length + 1) / 2 * getPreferenceManager().getColWidth(), y, width, height);

			long realTime = (long) Math.round(hDistance * Double.valueOf((getPreferenceManager().getTimestampLengthEachPage()) / Double.valueOf(paintRect.width)));

			long startTime = getDefault().getStartTimeStamp();
			startTime -= realTime;
			if (startTime < 0)
			{
				startTime = 0;
			}
			setStartTimeStamp(startTime);
			long endTime = startTime + getPreferenceManager().getTimestampLengthEachPage();
			setEndTimeStamp(endTime);

			long pageLength = getManager().getCachePageTimeLength();
			int startPageNum = (int) (startTime / pageLength);
			int endPageNum = (int) (endTime / pageLength);

			for (int i = startPageNum; i < endPageNum + 1; i++)
			{
				PaintThread paintThread = new PaintThread(Messages.getString("CurveView_Thread_Refresh_Curve"), this, true);
				QueryFieldMsgThread queryThread = new QueryFieldMsgThread(Messages.getString("CurveView_Thread_Query_Field"), this, paintThread, startTime, endTime + getManager().getCachePageTimeLength());
				queryThread.run();
				paintThread.run();
			}
		}
	}

	/**
	 * ������갴�µ��¼�
	 * 
	 * @param e ��갴�µ��¼�</br>
	 */
	public void handelMouseMoveEvet(MouseEvent e)
	{
		if (getDefault().isTerminated() && isMouseDown())
		{
			int hDistance = e.x - mouseLocate_X;
			mouseLocate_X = e.x;

			handelMouseEvet(hDistance);
		}
	}

	/**
	 * ��ͼ��ʼ������ </br>
	 */
	public void init()
	{
		hex = true;
		setStartTimeStamp(0);
		setEndTimeStamp(0);
		setCurrentTimeStamp(0);
		preferenceManager = new CurvePreferenceManager();
		preferenceManager.init();
		setPaintTime(preferenceManager.getPaintIntervalTime());
		manager = new FieldElementSetManager(preferenceManager.getFieldElementNUM(), preferenceManager.getTimestampLengthEachPage(), preferenceManager.getPageNum(), preferenceManager.getPageSubItemNum());
	}

	/**
	 * @return �Ƿ��������</br>
	 */
	public boolean isLockMonitor()
	{
		return isLockMonitor;
	}

	/**
	 * �ж��������Ƿ���
	 * 
	 * @return �������Ƿ���</br>
	 */
	public boolean isMouseDown()
	{
		return isMouseDown;
	}

	/**
	 * �Ƿ�ע��
	 * 
	 * @return �Ƿ�ע��</br>
	 */
	public boolean isRegister()
	{
		return isRegister;
	}

	/**
	 * ���״̬�Ƿ�ֹͣ
	 * 
	 * @return ���״̬ </br>
	 */
	public boolean isTerminated()
	{
		return termination;
	}

	/**
	 * ��ǰ��ͼ�ĵ�ǰʱ�����ת��ʱ����ı���ָ����ʱ��� </br>
	 */
	private void jumpToTime()
	{
		if (this.isTerminated())
		{
			if (timeTxt.getText() == "")
			{
				return;
			}
			long time = Long.parseLong(timeTxt.getText());
			setStartTimeStamp(time);
			long endTime = time + getPreferenceManager().getTimestampLengthEachPage();
			setEndTimeStamp(endTime);
			setCurrentTimeStamp(time);
			jumpToTime(time);
		}
	}

	/**
	 * ��ǰ��ͼ�ĵ�ǰʱ�����ת��ʱ����ı���ָ����ʱ���
	 * 
	 * @param startTime </br>
	 */
	private void jumpToTime(long startTime)
	{
		List<FieldElementSet> fields = getManager().getAllFields();
		if (fields.size() == 0)
		{
			MessageBox msgBox = new MessageBox(this.getDefault().getCanvas().getShell(), SWT.OK);
			msgBox.setText(Messages.getString("CurveView_Warning"));
			msgBox.setMessage(Messages.getString("CurveView_Not_Config_Field"));
			msgBox.open();
			return;
		}

		if (this.isTerminated())
		{
			long endTime = startTime + getPreferenceManager().getTimestampLengthEachPage();

			long length = getManager().getCachePageTimeLength();
			int startPageNum = (int) (startTime / length);
			int endPageNum = (int) (endTime / length);

			for (int i = startPageNum; i < endPageNum + 1; i++)
			{
				if (!getManager().isPageCache(i))
				{
					PaintThread paintThread = new PaintThread(Messages.getString("CurveView_Thread_Refresh_Curve"), this, true);
					paintThread.start();
					QueryFieldMsgThread queryThread = new QueryFieldMsgThread(Messages.getString("CurveView_Thread_Query_Field"), this, paintThread, startTime, endTime);
					queryThread.start();
				} else if (!getManager().isPageCache(startTime, endTime))
				{
					PaintThread paintThread = new PaintThread(Messages.getString("CurveView_Thread_Refresh_Curve"), this, true);
					paintThread.start();
					QueryFieldMsgThread queryThread = new QueryFieldMsgThread(Messages.getString("CurveView_Thread_Query_Field"), this, paintThread, startTime, endTime);
					queryThread.start();
				} else
				{
					PaintThread paintThread = new PaintThread(Messages.getString("CurveView_Thread_Refresh_Curve"), this, false);
					paintThread.start();
				}
			}

			try
			{
				if (TimeStampManager.getTimeStampManager() != null)
				{
					long time = getCurrentTimeStamp() - 30;
					if (time < 0)
					{
						time = 0;
					}

					if (observable.countObservers() > 0)
					{
						observable.notifyObservers(new Event(Event.EVENT_TIME_SELECTED, getStartTimeStamp(), getEndTimeStamp(), getCurrentTimeStamp()));
					}
				}
			} catch (Exception rr)
			{
				rr.printStackTrace();
			}
		}
	}

	/**
	 * ��ͼ����</br>
	 */
	public void layout()
	{
		int width = canvas.getBounds().width;
		int height = canvas.getBounds().height;
		container.setSize(width, height);
		container.setBounds(new Rectangle(0, 0, width, height));
		canvas.redraw();
		if (this.isTerminated())
		{
			PaintThread paintThread = new PaintThread(Messages.getString("CurveView_Thread_Refresh_Curve"), this, false);
			paintThread.start();
		}
	}

	/**
	 * ��ͼ��λ����</br>
	 */
	public void reset()
	{
		clearScreen();
		setStartTimeStamp(0);
		setEndTimeStamp(0);
		setCurrentTimeStamp(0);
		if (manager != null)
		{
			getManager().clearData();
		}
	}

	/**
	 * �������߻�ͼ����
	 * 
	 * @param container ���߻�ͼ���� </br>
	 */
	public void setContainer(ContainerFgr container)
	{
		this.container = container;
	}

	/**
	 * ���õ�ǰʱ���
	 * 
	 * @param currentTimeStamp ��ǰʱ��� </br>
	 */
	public synchronized void setCurrentTimeStamp(long currentTimeStamp)
	{
		if (currentTimeStamp < getStartTimeStamp())
		{
			this.currentTimeStamp = getStartTimeStamp();
		} else if (currentTimeStamp > getEndTimeStamp())
		{
			this.currentTimeStamp = getEndTimeStamp();
		} else
		{
			this.currentTimeStamp = currentTimeStamp;
		}
	}

	/**
	 * ���ý���ʱ���
	 * 
	 * @param endTimeStamp ����ʱ��� </br>
	 */
	public synchronized void setEndTimeStamp(long endTimeStamp)
	{
		this.endTimeStamp = endTimeStamp;
	}

	/*
	 * ������ͼ����(non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus() <br/>
	 */
	@Override
	public void setFocus()
	{
		parent.setFocus();
	}

	/**
	 * �����Ƿ��������
	 * 
	 * @param isLock �Ƿ�������� </br>
	 */
	public void setLockMonitor(boolean isLock)
	{
		this.isLockMonitor = isLock;
	}

	/**
	 * �����������Ƿ���
	 * 
	 * @param isMouseDown �������Ƿ��� </br>
	 */
	public void setMouseDown(boolean isMouseDown)
	{
		this.isMouseDown = isMouseDown;
	}

	/**
	 * ����������ͼ�ػ�ļ��ʱ��
	 * 
	 * @param paintTime �ػ�ļ��ʱ�� </br>
	 */
	public void setPaintTime(int paintTime)
	{
		this.paintTime = paintTime;
	}

	/**
	 * ����ע��
	 * 
	 * @param isRegister ע��</br>
	 */
	public void setRegister(boolean isRegister)
	{
		this.isRegister = isRegister;
	}

	/**
	 * ���ÿ�ʼʱ���
	 * 
	 * @param startTimeStamp ��ʼʱ��� </br>
	 */
	public synchronized void setStartTimeStamp(long startTimeStamp)
	{
		this.startTimeStamp = startTimeStamp;
	}

	/**
	 * ���ü��״̬
	 * 
	 * @param termination ���״̬ </br>
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
	 * �������ʱ���
	 */
	public void generateTime(Event event)
	{
		if (this.isTerminated())
		{
			setTermination(false);
		}
		if (isLockMonitor())
		{
			return;
		}
		long startTime = event.getStartTime();
		long endTime = event.getEndTime();

		long length = getManager().getCachePageTimeLength();
		int startPageNum = (int) (startTime / length);
		int endPageNum = (int) (endTime / length);

		if (paintThread == null || !paintThread.isAlive())
		{
			paintThread = new PaintThread(Messages.getString("CurveView_Thread_Refresh_Curve"), this, false);
			paintThread.start();
		}

		for (int i = startPageNum; i < endPageNum + 1; i++)
		{
			if (!getManager().isPageCache(i))
			{
				process(event);
			} else if (!getManager().isPageCache(startTime, endTime))
			{
				process(event);
			}
		}
	}

	private void process(Event event)
	{
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
			FieldElementParser parser = new FieldElementParser(this);
			parser.parse(spteMsgs.toArray(new SPTEMsg[spteMsgs.size()]));
			if (!this.isTerminated())
			{
				this.setEndTimeStamp(event.getEndTime());

				// �����ǰ���߻�ͼ���ڼ��״̬����ݵ�ǰ��ѯ��õ����½���ʱ��������µĵ�ǰʱ���
				if (this.getCurrentTimeStamp() <= getEndTimeStamp())
				{
					this.setCurrentTimeStamp(getEndTimeStamp());
				}
			}

		}
	}

	/**
	 * ѡ����ʱ���</br>
	 * 
	 * @param event ����¼�
	 */
	private void selectTimeStamp(Event event)
	{
		long startTime = event.getStartTime();
		long endTime = event.getEndTime();
		long current = event.getCurrentTime();

		setStartTimeStamp(startTime);
		setEndTimeStamp(endTime);
		setCurrentTimeStamp(current);

		long length = getManager().getCachePageTimeLength();
		int startPageNum = (int) (startTime / length);
		int endPageNum = (int) (endTime / length);

		for (int i = startPageNum; i < endPageNum + 1; i++)
		{
			if (!getManager().isPageCache(i))
			{
				PaintThread paintThread = new PaintThread(Messages.getString("CurveView_Thread_Refresh_Curve"), this, true);
				paintThread.start();
				QueryFieldMsgThread queryThread = new QueryFieldMsgThread(Messages.getString("CurveView_Thread_Query_Field"), this, paintThread, startTime, endTime);
				queryThread.start();
			} else if (!getManager().isPageCache(startTime, endTime))
			{
				PaintThread paintThread = new PaintThread(Messages.getString("CurveView_Thread_Refresh_Curve"), this, true);
				paintThread.start();
				QueryFieldMsgThread queryThread = new QueryFieldMsgThread(Messages.getString("CurveView_Thread_Query_Field"), this, paintThread, startTime, endTime);
				queryThread.start();
			} else
			{
				PaintThread paintThread = new PaintThread(Messages.getString("CurveView_Thread_Refresh_Curve"), this, false);
				paintThread.start();
			}
		}
	}

	/**
	 * ѡ����ʱ���</br>
	 * 
	 * @param event ����¼�
	 */
	private void loadHistory(Event event)
	{
		clearScreen();
		getManager().clearData();
		setStartTimeStamp(0);
		setEndTimeStamp(getPreferenceManager().getTimestampLengthEachPage());
		setCurrentTimeStamp(0);
		jumpToTime(0);
	}

	/**
	 * �������</br>
	 * 
	 * @param event ����¼�
	 */
	private void startMonitor(Event event)
	{
		getManager().clearData();
		init();
		startMonitor();
	}

	/**
	 * ����¼�Դ</br>
	 * 
	 * @return the observable
	 */
	public CurveViewObservable getObservable()
	{
		return observable;
	}

	/**
	 * ��һҳ</br>
	 */
	private void nextPage()
	{
		long time = getStartTimeStamp();
		time = time + getPreferenceManager().getTimestampLengthEachPage();
		setStartTimeStamp(time);
		long endTime = time + getPreferenceManager().getTimestampLengthEachPage();
		setEndTimeStamp(endTime);
		long current = getCurrentTimeStamp();
		current = current + getPreferenceManager().getTimestampLengthEachPage();
		setCurrentTimeStamp(current);
		jumpToTime(time);
	}

	public FieldElementSet getSelectFieldElementSet()
	{
		return selectFieldElementSet;
	}

	public void setSelectFieldElementSet(FieldElementSet selectFieldElementSet)
	{
		this.selectFieldElementSet = selectFieldElementSet;
	}

	/**
	 * ���Ƹ���ͼ�������ϵ����ù��߰�ť
	 * 
	 * @param toolBar
	 */
	protected void paintConfigItem(ToolBar toolBar)
	{
		ToolItem item = new ToolItem(toolBar, SWT.PUSH);
		item.setToolTipText(Messages.getString("CurveView_Config_Monitor_Field"));
		configImage = CurveViewPlugin.getImageDescriptor("icons/configs.gif").createImage();
		item.setImage(configImage);
		item.addSelectionListener(new SelectionListener()
		{

			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			public void widgetSelected(SelectionEvent e)
			{
				ConfigFieldsDialog dialog = new ConfigFieldsDialog(getDefault().getViewSite().getShell(), getDefault());
				dialog.open();
				canvas.redraw();
			}
		});
	}

	/**
	 * ���Ƹ���ͼ�������ϼ�ؽڵ����ð�ť
	 * 
	 * @param toolBar
	 */
	protected void paintConfigDataItem(ToolBar toolBar)
	{
		ToolItem item = new ToolItem(toolBar, SWT.PUSH);
		item.setToolTipText(Messages.getString("CurveView_Config_Monitor_Data_Zone"));
		configZoneImage = CurveViewPlugin.getImageDescriptor("icons/config_wiz.gif").createImage();
		item.setImage(configZoneImage);
		item.addSelectionListener(new SelectionListener()
		{
			public void widgetDefaultSelected(SelectionEvent e)
			{
			}

			public void widgetSelected(SelectionEvent e)
			{
				ConfigFieldsPropertyDialog dialog = new ConfigFieldsPropertyDialog(getDefault().getViewSite().getShell(), getDefault());
				dialog.open();
				canvas.redraw();
			}
		});

	}

	/**
	 * ���Ƹ���ͼ��������������հ�ť
	 * 
	 * @param toolBar
	 */
	protected void paintClearDataItem(ToolBar toolBar)
	{
		ToolItem item = new ToolItem(toolBar, SWT.PUSH);
		item.setToolTipText(Messages.getString("CurveView_Clear_Data"));
		refreshImage = CurveViewPlugin.getImageDescriptor("icons/refresh.gif").createImage();
		item.setImage(refreshImage);
		item.addSelectionListener(new SelectionListener()
		{
			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			public void widgetSelected(SelectionEvent e)
			{
				setStartTimeStamp(0);
				clearData();
			}
		});
	}

	/**
	 * ���Ƹ���ͼ��������������ť
	 * 
	 * @param toolBar
	 */
	protected void paintClearScreenItem(ToolBar toolBar)
	{
		ToolItem item = new ToolItem(toolBar, SWT.PUSH);
		item.setToolTipText(Messages.getString("CurveView_Clear_Screen"));
		clearImage = CurveViewPlugin.getImageDescriptor("icons/defaultview_misc.gif").createImage();
		item.setImage(clearImage);
		item.addSelectionListener(new SelectionListener()
		{
			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			public void widgetSelected(SelectionEvent e)
			{
				setStartTimeStamp(0);
				clearScreen();
			}
		});
	}

	/**
	 * ����16�����л���ť
	 * 
	 * @param toolBar
	 */
	protected void paintHexChangeItem(ToolBar toolBar)
	{
		ToolItem item = new ToolItem(toolBar, SWT.CHECK);
		item.setToolTipText("�л���10����");
		hexIamge = CurveViewPlugin.getImageDescriptor("icons/hex.png").createImage();
		item.setImage(hexIamge);
		item.setSelection(!hex);
		item.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				ToolItem toolItem = (ToolItem) e.widget;
				hex = !toolItem.getSelection();
				tableViewer.setLabelProvider(new FieldTableLabelProvider());
				tableTreeViewer.setLabelProvider(new MsgTableLabelProvider());
				if (hex)
				{
					toolItem.setToolTipText("�л���10����");
				} else
				{
					toolItem.setToolTipText("�л���16����");
				}
			}
		});
	}

	/**
	 * ������һҳ��ť
	 * 
	 * @param toolBar
	 */
	protected void paintPreviousItem(ToolBar toolBar)
	{
		ToolItem item = new ToolItem(toolBar, SWT.PUSH);
		item.setToolTipText(Messages.getString("CurveView_Previous_Page"));
		previousImage = CurveViewPlugin.getImageDescriptor("icons/shift_l_edit.gif").createImage();
		item.setImage(previousImage);
		item.addSelectionListener(new SelectionListener()
		{

			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			public void widgetSelected(SelectionEvent e)
			{
				prepiousPage();
			}

			private void prepiousPage()
			{
				long time = getStartTimeStamp();
				time = time - getPreferenceManager().getTimestampLengthEachPage();
				if (time < 0)
				{
					time = 0;
				}
				setStartTimeStamp(time);
				long endTime = time + getPreferenceManager().getTimestampLengthEachPage();
				setEndTimeStamp(endTime);
				long current = getCurrentTimeStamp();
				current = current - getPreferenceManager().getTimestampLengthEachPage();
				if (current < 0)
				{
					current = 0;
				}
				setCurrentTimeStamp(current);
				jumpToTime(time);
			}
		});

	}

	/**
	 * ������һҳ��ť
	 * 
	 * @param toolBar
	 */
	protected void paintNextItem(ToolBar toolBar)
	{
		ToolItem item = new ToolItem(toolBar, SWT.PUSH);
		item.setToolTipText(Messages.getString("CurveView_Next_Page"));
		nextImage = CurveViewPlugin.getImageDescriptor("icons/shift_r_edit.gif").createImage();
		item.setImage(nextImage);
		item.addSelectionListener(new SelectionListener()
		{

			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			public void widgetSelected(SelectionEvent e)
			{
				nextPage();
			}
		});
	}

	protected void paintJumpItem(Composite topPanel)
	{
		Label lab = new Label(topPanel, SWT.NONE);
		lab.setText(Messages.getString("CurveView_TimeStamp"));
		GridData layoutData = new GridData();
		lab.setLayoutData(layoutData);

		timeTxt = new Text(topPanel, SWT.BORDER);
		layoutData = new GridData();
		layoutData.widthHint = 60;
		timeTxt.setLayoutData(layoutData);
		timeTxt.addVerifyListener(new VerifyListener()
		{

			public void verifyText(VerifyEvent e)
			{
				e.doit = e.text.length() == 0 || Character.isDigit(e.text.charAt(0));
			}
		});
		timeTxt.addTraverseListener(new TraverseListener()
		{

			public void keyTraversed(TraverseEvent e)
			{
				if (e.character == '\r')
				{
					jumpToTime();
				}
			}

		});

		goBtn = new Button(topPanel, SWT.NONE);
		goBtn.setText(Messages.getString("CurveView_Jump"));
		layoutData = new GridData();
		goBtn.setLayoutData(layoutData);
		goBtn.addMouseListener(new MouseListener()
		{

			public void mouseDoubleClick(MouseEvent e)
			{
			}

			public void mouseDown(MouseEvent e)
			{
				jumpToTime();
			}

			public void mouseUp(MouseEvent e)
			{
			}
		});
	}

	/**
	 * ����������ͼ�Ĺ�����
	 * 
	 * @param mainPanel
	 */
	protected void paintTopToolBar(Composite mainPanel)
	{
		Composite topPanel = new Composite(mainPanel, SWT.NONE);
		GridLayout grid = new GridLayout();
		grid.horizontalSpacing = 0;
		grid.verticalSpacing = 0;
		grid.marginWidth = 0;
		grid.marginHeight = 0;
		grid.numColumns = 9;
		topPanel.setLayout(grid);

		ToolBar toolBar = new ToolBar(topPanel, SWT.HORIZONTAL);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		toolBar.setLayoutData(gd);
		paintConfigItem(toolBar);
		paintConfigDataItem(toolBar);
		paintClearDataItem(toolBar);
		paintClearScreenItem(toolBar);
		paintHexChangeItem(toolBar);

		paintPreviousItem(toolBar);
		paintNextItem(toolBar);

		paintJumpItem(topPanel);

	}

	/**
	 * ���ص�ǰ������ͼ�Ƿ�����16���Ƶ���ʽ�����ź�ֵ
	 * 
	 * @return
	 */
	public boolean isHex()
	{
		return hex;
	}

	protected void createTableViewer(SashForm verticalSashForm)
	{
		this.tableViewer = new TableViewer(verticalSashForm, SWT.BORDER | SWT.FULL_SELECTION | SWT.SINGLE);

		TableColumn column = new TableColumn(this.tableViewer.getTable(), SWT.NONE);
		column.setText(Messages.getString("CurveView_Legend")); // ͼ��
		column.setResizable(false);
		column.setWidth(80);

		column = new TableColumn(this.tableViewer.getTable(), SWT.NONE);
		column.setText(Messages.getString("CurveView_Field_Name")); // �ź���
		column.setWidth(80);

		column = new TableColumn(this.tableViewer.getTable(), SWT.NONE);
		column.setText(Messages.getString("CurveView_TimeStamp")); // ʱ���
		column.setWidth(80);

		column = new TableColumn(this.tableViewer.getTable(), SWT.NONE);
		column.setText(Messages.getString("CurveView_Field_Value")); // �ź�ֵ
		column.setWidth(80);

		column = new TableColumn(this.tableViewer.getTable(), SWT.NONE);
		column.setText(Messages.getString("CurveView_Unit")); // ��λ
		column.setWidth(80);

		column = new TableColumn(this.tableViewer.getTable(), SWT.NONE);
		column.setText(Messages.getString("CurveView_Field_Array_Length")); // �ź����鳤��
		column.setWidth(100);

		column = new TableColumn(this.tableViewer.getTable(), SWT.NONE);
		column.setText(Messages.getString("CurveView_Start_Word")); // ��ʼ��
		column.setWidth(80);

		column = new TableColumn(this.tableViewer.getTable(), SWT.NONE);
		column.setText(Messages.getString("CurveView_Field_Length")); // �źų���
		column.setWidth(80);

		column = new TableColumn(this.tableViewer.getTable(), SWT.NONE);
		column.setText(Messages.getString("CurveView_Start_Bit")); // ��ʼλ
		column.setWidth(80);

		column = new TableColumn(this.tableViewer.getTable(), SWT.NONE);
		column.setText(Messages.getString("CurveView_MinValue")); // ��Сֵ
		column.setWidth(80);

		column = new TableColumn(this.tableViewer.getTable(), SWT.NONE);
		column.setText(Messages.getString("CurveView_MaxValue")); // ���ֵ
		column.setWidth(80);

		this.tableViewer.setContentProvider(new FieldTableContentProvider());
		this.tableViewer.setLabelProvider(new FieldTableLabelProvider());
		this.tableViewer.getTable().setHeaderVisible(true);

		GridData gd = new GridData(GridData.FILL_BOTH);
		this.tableViewer.getTable().setLayoutData(gd);
		this.tableViewer.getTable().setLinesVisible(true);
	}

	protected void createTableTreeViewer(SashForm horizontalSashForm)
	{
		this.tableTreeViewer = new TableTreeViewer(horizontalSashForm, SWT.BORDER | SWT.FULL_SELECTION | SWT.SINGLE);
		TableColumn column = new TableColumn(this.tableTreeViewer.getTableTree().getTable(), SWT.NONE);
		column.setText(Messages.getString("CurveView_Message")); // ��Ϣ
		column.setWidth(90);

		column = new TableColumn(this.tableTreeViewer.getTableTree().getTable(), SWT.NONE);
		column.setText(Messages.getString("CurveView_TimeStamp_Or_FieldName")); // ʱ���/����
		column.setWidth(60);

		column = new TableColumn(this.tableTreeViewer.getTableTree().getTable(), SWT.NONE);
		column.setText(Messages.getString("CurveView_Field_Value")); // �ź�ֵ
		column.setWidth(50);

		column = new TableColumn(this.tableTreeViewer.getTableTree().getTable(), SWT.NONE);
		column.setText(Messages.getString("CurveView_Start_Word")); // ��ʼ��
		column.setWidth(40);

		column = new TableColumn(this.tableTreeViewer.getTableTree().getTable(), SWT.NONE);
		column.setText(Messages.getString("CurveView_Field_Length")); // �źų���
		column.setWidth(40);

		column = new TableColumn(this.tableTreeViewer.getTableTree().getTable(), SWT.NONE);
		column.setText(Messages.getString("CurveView_Start_Bit")); // ��ʼλ
		column.setWidth(40);

		column = new TableColumn(this.tableTreeViewer.getTableTree().getTable(), SWT.NONE);
		column.setText(Messages.getString("CurveView_LSB")); // LSB
		column.setWidth(40);

		column = new TableColumn(this.tableTreeViewer.getTableTree().getTable(), SWT.NONE);
		column.setText(Messages.getString("CurveView_MSB")); // MSB
		column.setWidth(80);

		this.tableTreeViewer.setContentProvider(new MsgTreeContentProvider());
		this.tableTreeViewer.setLabelProvider(new MsgTableLabelProvider());
		// this.tableViewer.setInput(Utils.getAllCasesInWorkspace());
		this.tableTreeViewer.getTableTree().getTable().setHeaderVisible(true);

		GridData gd = new GridData(GridData.FILL_BOTH);
		this.tableTreeViewer.getTableTree().getTable().setLayoutData(gd);
		this.tableTreeViewer.getTableTree().getTable().setLinesVisible(true);
		this.tableTreeViewer.expandAll();
	}

	public class CurveViewObservable extends Observable implements Observer
	{
		/**
		 * ͨ���¼����¼�����
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

	private static class FieldTableContentProvider implements IStructuredContentProvider
	{

		public void dispose()
		{
		}

		@SuppressWarnings("unchecked")
		public Object[] getElements(Object inputElement)
		{
			if (inputElement instanceof List<?>)
			{
				List<FieldElementSet> list = (List<FieldElementSet>) inputElement;
				return list.toArray(new FieldElementSet[list.size()]);
			}
			return (FieldElementSet[]) inputElement;
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
		{

		}
	}

	/**
	 * @author ���� 2012-3-14
	 */
	private class FieldTableLabelProvider extends LabelProvider implements ITableLabelProvider
	{

		public Image getColumnImage(Object element, int columnIndex)
		{
			if (columnIndex == 0)
			{
				if (element instanceof FieldElementSet)
				{
					// ����źż����б����RGB
					FieldElementSet result = (FieldElementSet) element;
					RGB rgb = result.getColor();

					// �ź�ӳ������Ƿ��Ѿ��������ǰ��Imageӳ�����
					if (!map.keySet().contains(rgb))
					{
						Display display = Display.getCurrent();
						PaletteData pData = new PaletteData(0xFF, 0xFF00, 0xFF0000);

						// ͨ���źż����е�RGB�����źŶ�Ӧ��ͼ��
						int fillColor = pData.getPixel(rgb);
						ImageData iData = new ImageData(80, 12, 24, pData);
						for (int i = 0; i < 80; i++)
						{
							for (int j = 0; j < 12; j++)
							{
								iData.setPixel(i, j, fillColor);
								iData.setAlpha(i, j, 0);
							}
						}

						// �������źŶ�Ӧ��ͼ�����浽ӳ�����
						Image image = new Image(display, iData);
						map.put(rgb, image);
						return image;
					} else
					{

						// ��ͼ������ӳ�����ֱ�ӻ��Imageӳ�����
						Image image = map.get(rgb);
						return image;
					}
				}
			}
			return null;
		}

		public String getColumnText(Object element, int columnIndex)
		{

			if (element instanceof FieldElementSet)
			{
				FieldElementSet result1 = (FieldElementSet) element;

				ICDField result = result1.getField();
				switch (columnIndex)
				{
					// �ź�����
					case 1:
					{
						try
						{
							return result.getAttribute("signalName").getValue().toString();
						} catch (Exception e)
						{
							return "";
						}
					}
						// ��ǰ�źŵ�ʱ���
					case 2:
					{
						long startTime = getStartTimeStamp();
						long endTime = getEndTimeStamp();
						long currentTime = getCurrentTimeStamp();
						if (startTime == currentTime && currentTime <= endTime)
						{
							List<FieldElement> lists = result1.getElementsToShow(startTime, endTime);
							int size = lists.size();
							if (size > 0)
							{
								for (int i = 0; i < lists.size() - 1; i++)
								{
									if (lists.get(i).getTime() >= startTime && lists.get(i).getTime() <= endTime)
									{
										return Long.toString(lists.get(0).getTime());
									}
								}
							} else
							{
								return Long.toString(currentTime);
							}
						} else if (startTime < currentTime && currentTime == endTime)
						{
							List<FieldElement> lists = result1.getElementsToShow(startTime, endTime);
							int size = lists.size();
							if (size > 0)
							{
								for (int i = lists.size() - 1; i > 0; i--)
								{
									if (lists.get(i).getTime() >= startTime && lists.get(i).getTime() <= currentTime)
									{
										return Long.toString(lists.get(size - 1).getTime());
									}
								}
							} else
							{
								return Long.toString(currentTime);
							}
						} else if (startTime < currentTime && currentTime < endTime)
						{
							List<FieldElement> lists = result1.getElementsToShow(startTime, currentTime);
							int size = lists.size();
							if (size > 0)
							{
								for (int i = lists.size() - 1; i > 0; i--)
								{
									if (lists.get(i).getTime() >= startTime && lists.get(i).getTime() <= currentTime)
									{
										return Long.toString(lists.get(size - 1).getTime());
									}
								}
							} else
							{
								return Long.toString(currentTime);
							}
						}

						return "";
					}
						// ��ǰ�źŵ�ֵ
					case 3:
					{

						long startTime = getStartTimeStamp();
						long endTime = getEndTimeStamp();
						long currentTime = getCurrentTimeStamp();
						int value = 0;
						if (startTime == currentTime && currentTime <= endTime)
						{
							List<FieldElement> lists = result1.getElementsToShow(startTime, endTime);
							int size = lists.size();
							if (size > 0)
							{
								for (int i = 0; i < lists.size() - 1; i++)
								{
									if (lists.get(i).getTime() >= startTime && lists.get(i).getTime() <= endTime)
									{
										value = lists.get(0).getValue();
									}
								}
							}
						} else if (startTime < currentTime && currentTime == endTime)
						{
							List<FieldElement> lists = result1.getElementsToShow(startTime, endTime);
							int size = lists.size();
							if (size > 0)
							{
								for (int i = lists.size() - 1; i > 0; i--)
								{
									if (lists.get(i).getTime() >= startTime && lists.get(i).getTime() <= currentTime)
									{
										value = lists.get(size - 1).getValue();
									}
								}
							}
						} else if (startTime < currentTime && currentTime < endTime)
						{
							List<FieldElement> lists = result1.getElementsToShow(startTime, currentTime);
							int size = lists.size();
							if (size > 0)
							{
								for (int i = lists.size() - 1; i > 0; i--)
								{
									if (lists.get(i).getTime() >= startTime && lists.get(i).getTime() <= currentTime)
									{
										value = lists.get(size - 1).getValue();
									}
								}
							}
						}
						String str = "";
						if (hex)
						{
							str = "0x" + Integer.toHexString(value);
						} else
						{
							str = Integer.toString(value);
						}

						return str;
					}

						// �źŵĵ�λ
					case 4:
					{
						try
						{
							return result.getAttribute(Constants.ICD_FIELD_UNIT_CODE).getValue().toString();
						} catch (Exception e)
						{
							return "";
						}
					}
						// �ź�����ĳ���
					case 5:
					{
						return result.getAttribute("signalArrayLength").getValue().toString();
					}

						// �źŵĿ�ʼ��
					case 6:
					{
						return result.getAttribute("startWord").getValue().toString();
					}
						// �źŵĳ���
					case 7:
					{
						return result.getAttribute("signalLength").getValue().toString();
					}
						// �źŵĿ�ʼλ
					case 8:
					{
						return result.getAttribute("startBit").getValue().toString();
					}

						// �źŵ���Сֵ
					case 9:
					{
						return Integer.toString(result1.getMinValue());
					}
						// �źŵ����ֵ
					case 10:
					{
						return Integer.toString(result1.getMaxValue());
					}

				}
			}

			return StringUtils.EMPTY_STRING;
		}
	}

	/**
	 * @author ���� 2012-3-14
	 */
	private class MouseClickListener implements MouseListener
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse
		 * .swt.events.MouseEvent) <br/>
		 */
		public void mouseDoubleClick(MouseEvent e)
		{

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events
		 * .MouseEvent) <br/>
		 */
		public void mouseDown(MouseEvent e)
		{
			mouseLocate_X = e.x;
			if (!isMouseDown())
			{
				setMouseDown(true);
			}

			if (getDefault().isTerminated())
			{
				org.eclipse.swt.graphics.Rectangle r = getCanvas().getBounds();
				List<FieldElementSet> fields = getManager().getAllFields();
				int length = fields.size();

				int x = r.x;
				int y = r.y + CurveConst.Y_AXIS_V_TOP_OFFSET;

				// ���ÿɻ�ͼ����
				Rectangle paintRect = new Rectangle(x + (length + 1) / 2 * getPreferenceManager().getColWidth(), y, r.width - (length + 1) / 2 * getPreferenceManager().getColWidth() - (length) / 2 * getPreferenceManager().getColWidth() - 10, r.height - (CurveConst.Y_AXIS_V_TOP_OFFSET + CurveConst.Y_AXIS_V_BOTTOM_OFFSET));

				long realTime = (long) Math.round((e.x + 3 - (x + (length + 1) / 2 * getPreferenceManager().getColWidth())) * Double.valueOf((getPreferenceManager().getTimestampLengthEachPage()) / Double.valueOf(paintRect.width)));

				long time = getDefault().getStartTimeStamp();
				time += realTime;
				if (time < 0)
				{
					time = 0;
				}
				setCurrentTimeStamp(time);
				handelMouseEvet(0);

				try
				{
					if (observable.countObservers() > 0)
					{
						observable.notifyObservers(new Event(Event.EVENT_TIME_SELECTED, getStartTimeStamp(), getEndTimeStamp(), getCurrentTimeStamp()));
					}
				} catch (Exception rr)
				{
					rr.printStackTrace();
				}
			}
		}

		public void mouseUp(MouseEvent e)
		{
			handelMouseMoveEvet(e);
			if (isMouseDown())
			{
				setMouseDown(false);
			}
		}
	}

	/**
	 * @author ���� 2012-3-14
	 */
	public class MsgTableLabelProvider extends LabelProvider implements ITableLabelProvider
	{

		public Image getColumnImage(Object element, int columnIndex)
		{
			return null;
		}

		public String getColumnText(Object element, int columnIndex)
		{

			if (element instanceof FieldElementSet)
			{
				FieldElementSet result = (FieldElementSet) element;
				SPTEMsg spteMsg = (SPTEMsg) result.getMonitorMsgNode();
				switch (columnIndex)
				{
					// �����ʾ��Ϣ����Ϣ����
					case 0:
					{
						return spteMsg.getMsg().getName();
					}
						// �����ʾ��Ϣ�ĵ�ǰʱ���
					case 1:
					{
						long startTime = getStartTimeStamp();
						long endTime = getEndTimeStamp();
						long currentTime = getCurrentTimeStamp();
						if (startTime == currentTime && currentTime <= endTime)
						{
							List<FieldElement> lists = result.getElementsToShow(startTime, endTime);
							int size = lists.size();
							if (size > 0)
							{
								for (int i = 0; i < lists.size() - 1; i++)
								{
									if (lists.get(i).getTime() >= startTime && lists.get(i).getTime() <= endTime)
									{
										return Long.toString(lists.get(0).getTime());
									}
								}
							} else
							{
								return Long.toString(currentTime);
							}
						} else if (startTime < currentTime && currentTime == endTime)
						{
							List<FieldElement> lists = result.getElementsToShow(startTime, endTime);
							int size = lists.size();
							if (size > 0)
							{
								for (int i = lists.size() - 1; i > 0; i--)
								{
									if (lists.get(i).getTime() >= startTime && lists.get(i).getTime() <= currentTime)
									{
										return Long.toString(lists.get(size - 1).getTime());
									}
								}
							} else
							{
								return Long.toString(currentTime);
							}
						} else if (startTime < currentTime && currentTime < endTime)
						{
							List<FieldElement> lists = result.getElementsToShow(startTime, currentTime);
							int size = lists.size();
							if (size > 0)
							{
								for (int i = lists.size() - 1; i > 0; i--)
								{
									if (lists.get(i).getTime() >= startTime && lists.get(i).getTime() <= currentTime)
									{
										return Long.toString(lists.get(size - 1).getTime());
									}
								}
							} else
							{
								return Long.toString(currentTime);
							}
						}

						return "";
					}
				}

			} else if (element instanceof Period)
			{ // ������Ϣ
				switch (columnIndex)
				{
					case 0:
					{
						return Messages.getString("CurveView_Period_Message");
					}
				}

			} else if (element instanceof Field)
			{ // �ź�
				Field result = (Field) element;
				switch (columnIndex)
				{
					// �źŵ�ID
					case 0:
					{
						return result.getId().toString();
					}
						// �ź�����
					case 1:
					{
						return result.getName();
					}
						// �ź�ֵ
					case 2:
					{
						if (hex)
							return "0x" + Integer.toHexString((Integer.parseInt(result.getValue())));
						else
						{
							return Integer.toString(Integer.parseInt(result.getValue()));
						}
					}
						// �źŵ�ƫ����
					case 3:
					{
						return Integer.toString(result.getOffsetword());
					}
						// �źŵĿ��
					case 4:
					{
						return Integer.toString(result.getWidth());
					}

						// �źŵ�ƫ��λ
					case 5:
					{
						return Integer.toString(result.getOffsetbit());
					}
						// �źŵ�LSB
					case 6:
					{
						return result.getLsb();
					}
						// �źŵ�MSB
					case 7:
					{
						return result.getMsb();
					}
				}
			}

			return "";
		}
	}

	class MsgTreeContentProvider implements ITreeContentProvider
	{

		public void dispose()
		{

		}

		public Object[] getChildren(Object parentElement)
		{
			Object[] objs = null;
			if (parentElement instanceof FieldElementSet)
			{
				FieldElementSet result = (FieldElementSet) parentElement;
				long startTime = getStartTimeStamp();
				long endTime = getEndTimeStamp();
				long currentTime = getCurrentTimeStamp();

				if (startTime == currentTime && currentTime <= endTime)
				{
					List<SPTEMsg> lists = result.getSPTEMsgElementsToShow(startTime, endTime);
					int size = lists.size();
					if (size > 0)
					{
						objs = lists.get(0).getMsg().getChildren().toArray();
					}
				} else if (startTime < currentTime && currentTime == endTime)
				{
					List<SPTEMsg> lists = result.getSPTEMsgElementsToShow(startTime, endTime);
					int size = lists.size();
					if (size > 0)
					{
						objs = lists.get(size - 1).getMsg().getChildren().toArray();
					}
				} else if (startTime < currentTime && currentTime < endTime)
				{
					List<SPTEMsg> lists = result.getSPTEMsgElementsToShow(startTime, currentTime);
					int size = lists.size();
					if (size > 0)
					{
						objs = lists.get(size - 1).getMsg().getChildren().toArray();
					}
				}
			} else if (parentElement instanceof Period)
			{
				Period result = (Period) parentElement;
				objs = result.getChildren().toArray();
			}
			return objs;
		}

		@SuppressWarnings("unchecked")
		public Object[] getElements(Object inputElement)
		{
			if (inputElement instanceof List<?>)
			{
				if (((List) inputElement).size() > 0 && ((List) inputElement).get(0) instanceof FieldElementSet)
				{
					List<FieldElementSet> list = (List<FieldElementSet>) inputElement;
					return list.toArray(new FieldElementSet[list.size()]);
				}
			} else if (inputElement instanceof Object[])
			{
				return (Object[]) inputElement;
			}
			return null;
		}

		public Object getParent(Object element)
		{
			if (element instanceof ICDField)
			{
				return ((ICDField) element);
			} else if (element instanceof ICDFunctionSubDomain)
			{
				ICDFunctionSubDomain folder = (ICDFunctionSubDomain) element;
				return folder.getParent();
			}

			return null;
		}

		public boolean hasChildren(Object element)
		{
			if (element instanceof Field)
			{
				return false;
			}
			return true;
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
		{

		}
	}

	/**
	 * 
	 * Canvas�ϵļ�����������
	 * 
	 * @date 2012-10-18
	 */
	private class CanvasMouseListener implements MouseTrackListener, KeyListener, ControlListener
	{

		private Cursor	handCursor;

		private Cursor	handCursor2;

		public void mouseEnter(MouseEvent e)
		{
			Canvas canvas = (Canvas) e.getSource();
			handCursor2 = new Cursor(e.display, SWT.CURSOR_HAND);
			canvas.setCursor(handCursor2);
		}

		public void mouseExit(MouseEvent e)
		{
			if (handCursor != null)
			{
				handCursor.dispose();
				handCursor = null;
			}
			if (handCursor2 != null)
			{
				handCursor2.dispose();
				handCursor2 = null;
			}
		}

		public void mouseHover(MouseEvent e)
		{
			handCursor = new Cursor(e.display, SWT.CURSOR_HAND);
			Canvas canvas = (Canvas) e.getSource();
			canvas.setCursor(handCursor);
		}

		public void keyPressed(KeyEvent event)
		{
			if (isTerminated())
			{
				switch (event.keyCode)
				{
					case SWT.ARROW_UP:
					{// ��ֱ����Ŵ�
						magnifyVertical();
						break;
					}
					case SWT.ARROW_DOWN:
					{// ��ֱ������С
						deflateVertical();
						break;
					}
					case SWT.ARROW_LEFT:
					{// ˮƽ������С
						previousHorizontalPoint();
						break;
					}
					case SWT.ARROW_RIGHT:
					{// ˮƽ����Ŵ�
						nextHorizontalPoint();
						break;
					}
				}
			}
		}

		private void nextHorizontalPoint()
		{
			FieldElementSet set = getSelectFieldElementSet();
			long current = getCurrentTimeStamp();
			List<FieldElement> list = set.getElementsToShow(current + 1, current + 300);
			if (list != null && list.size() > 0)
			{
				if (list.get(0).getTime() > getEndTimeStamp())
				{
					setEndTimeStamp(list.get(0).getTime());
					setStartTimeStamp(list.get(0).getTime() - manager.getCachePageTimeLength());
				}
				setCurrentTimeStamp(list.get(0).getTime());
				canvas.redraw();
				jumpToTime(getStartTimeStamp());
				// ͨ���¼��ķ�ʽ֪ͨ�����۲��ߵ�ǰѡ���¼�
				getObservable().notifyObservers(new Event(Event.EVENT_TIME_SELECTED, (int) list.get(0).getTime(), (int) list.get(0).getTime() + 10));
			}

		}

		private void previousHorizontalPoint()
		{
			FieldElementSet set = getSelectFieldElementSet();
			long current = getCurrentTimeStamp();
			List<FieldElement> list;
			if (current == 0)
			{
				return;
			}
			if (current - 300 > 0)
			{
				list = set.getElementsToShow(current - 300, current - 1);
			} else
			{
				list = set.getElementsToShow(0, current - 1);
			}
			if (list != null && list.size() > 0)
			{
				if (list.get(0).getTime() < getStartTimeStamp())
				{
					setStartTimeStamp(list.get(list.size() - 1).getTime());
					setEndTimeStamp(list.get(list.size() - 1).getTime() + manager.getCachePageTimeLength());
				}
				setCurrentTimeStamp(list.get(list.size() - 1).getTime());
				canvas.redraw();
				jumpToTime(getStartTimeStamp());
				// ͨ���¼��ķ�ʽ֪ͨ�����۲��ߵ�ǰѡ���¼�
				getObservable().notifyObservers(new Event(Event.EVENT_TIME_SELECTED, (int) list.get(list.size() - 1).getTime(), (int) list.get(list.size() - 1).getTime() + 10));
			}
		}

		private void deflateVertical()
		{
			List<FieldElementSet> fields = getManager().getAllFields();
			for (int i = 0; i < fields.size(); i++)
			{
				FieldElementSet set = fields.get(i);

				int value = set.getMaxValue();
				value = value - Math.round(value / getPreferenceManager().getyIntervalNum());
				set.setMaxValue(value);
			}

			canvas.redraw();
			jumpToTime(getStartTimeStamp());
		}

		private void magnifyVertical()
		{
			List<FieldElementSet> fields = getManager().getAllFields();
			for (int i = 0; i < fields.size(); i++)
			{
				FieldElementSet set = fields.get(i);

				int value = set.getMaxValue();
				value = value + Math.round(value / getPreferenceManager().getyIntervalNum());
				set.setMaxValue(value);
			}

			canvas.redraw();
			jumpToTime(getStartTimeStamp());
		}

		public void keyReleased(KeyEvent arg0)
		{
			// TODO Auto-generated method stub

		}

		public void controlMoved(ControlEvent e)
		{
			this.resize();
		}

		public void controlResized(ControlEvent e)
		{
			this.resize();
		}

		private void resize()
		{
			layout();
		}

	}

}