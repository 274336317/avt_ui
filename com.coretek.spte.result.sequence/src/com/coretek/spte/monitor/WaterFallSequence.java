/************************************************************************
 * �����������ɼ������޹�˾ ��Ȩ����
 * Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor;

import java.util.List;
import java.util.logging.Logger;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;

import com.coretek.common.logging.LoggingPlugin;
import com.coretek.common.template.Attribute;
import com.coretek.common.template.ClazzManager;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.template.TemplateUtils;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.cfg.ErrorTypesEnum;
import com.coretek.spte.dataCompare.CompareResult;
import com.coretek.spte.dataCompare.Result;
import com.coretek.spte.monitor.figures.ConnFgr;
import com.coretek.spte.monitor.figures.HeaderFgr;
import com.coretek.spte.monitor.figures.ItemBorderAnchor;

/**
 * �ٲ����͵�����ͼ��ͼ�ΰ���ʱ����Ⱥ�˳����ϵ�����ʾ��
 * 
 * @author ���Ρ 2012-3-1
 */
public class WaterFallSequence extends SequenceWithTime
{

	private static final Logger	logger	= LoggingPlugin.getLogger(WaterFallSequence.class);

	private CompareResult		compareResult;

	public WaterFallSequence(List<Result> msgs, int scale, CompareResult compareResult)
	{
		super(msgs, scale);
		this.compareResult = compareResult;
	}

	/**
	 * @return the compareResult <br/>
	 * 
	 */
	public CompareResult getCompareResult()
	{
		return compareResult;
	}

	/**
	 * @param caseManager the caseManager to set <br/>
	 * 
	 */
	public void setCaseManager(ClazzManager caseManager)
	{
		this.caseManager = caseManager;
	}

	/**
	 * ���ؿ̶�ֵ
	 * 
	 * @return the scale <br/>
	 */
	public int getScale()
	{
		return scale;
	}

	public void repaint()
	{
		this.msgContainerFgr.removeAllMsgs();
		int startTime = this.currentPageNum * this.pageTime - this.pageTime;
		this.timerFgr.repaint(startTime, this.scale);
		this.paintMsgs(startTime, startTime + this.pageTime);
		this.updatePageInfo();
	}

	/**
	 * ������Ϣ </br>
	 */
	public void paint(Composite panel)
	{
		if (this.resultList.size() == 0)
		{
			MessageDialog.openWarning(Display.getCurrent().getActiveShell(), "����", "û��ִ�н����");
			return;
		}
		super.paint(panel);
		this.showHeaderPanel(headerPanel);
		this.showSequencePanel(sequencePanel);

		// �����ÿҳ���ܱ�ʾ��ʱ�䳤��
		this.pageTime = this.scale * this.nodeNumber;
		this.showGotoPanel(timePanel);

		this.computePageSum();

		if (this.pageSum > 1)
		{
			// ����last
			this.enableLast();
			// ����next
			this.enableNext();
		}
		this.updatePageInfo();
		// ������һҳ
		this.paintMsgs(this.resultList.get(0).getSpteMsg().getTimeStamp(), this.nodeNumber * this.scale);

	}

	/**
	 * �����û����õ��²������»�����
	 * 
	 * @param scale ÿ���̶��������ֵ
	 * @param timeBound ʱ�䷶Χ</br>
	 */
	public void updateCfg(int scale, int timeBound)
	{
		boolean flag = false;
		if (this.scale != scale)
		{
			flag = true;

			this.scale = scale;
			this.msgContainerFgr.removeAllMsgs();
			// �����ÿҳ����ʾ��ʱ��
			this.pageTime = this.nodeNumber * this.scale;
		}

		if (flag)
		{
			// Ĭ����ת����һҳ��ʼ�ػ�
			this.currentPageNum = 1;
			this.computePageSum();
			this.repaint();
		}

	}

	/**
	 * ����ҳ���� </br>
	 */
	private void computePageSum()
	{
		if (this.resultList.size() != 0)
		{
			SPTEMsg tailMsg = this.resultList.get(this.resultList.size() - 1).getSpteMsg();
			// �������Ҫ����ҳ
			this.pageSum = (int) tailMsg.getTimeStamp() / (this.scale * this.nodeNumber);
			if (tailMsg.getTimeStamp() % (this.scale * this.nodeNumber) > 0)
				this.pageSum++;
			this.updateStatusOfNavigators();
		}
	}

	/**
	 * ���µ�����ť��״̬ </br>
	 */
	private void updateStatusOfNavigators()
	{
		if (pageSum < 2)
		{
			// ��last��ǩ��ֹ
			this.disableLast();
			// ��previous��ǩ��ֹ
			this.disablePrevious();
			// ��first��ǩ��ֹ
			this.disableFirst();
			// ��next��ǩ��ֹ
			this.disableNext();
		} else
		{
			// ��last��ǩ����
			this.enableLast();
			// ��previous��ǩ��ֹ
			this.disablePrevious();
			// ��first��ǩ��ֹ
			this.disableFirst();
			// ��next��ǩ����
			this.enableNext();
		}
	}

	/**
	 * ����ҳ��Ϣ
	 */
	private void updatePageInfo()
	{
		StringBuilder sb = new StringBuilder("��");
		sb.append(this.currentPageNum);
		sb.append("ҳ");
		sb.append("/");
		sb.append(this.pageSum);
		sb.append("ҳ");
		this.txtPageNum.setText(sb.toString());
	}

	/**
	 * ����ʱ���ڿ�ʼʱ�䵽����ʱ��֮�ڵ�������Ϣ
	 * 
	 * @param startTime ��ʼʱ��
	 * @param endTime ����ʱ�� </br>
	 */
	private void paintMsgs(long startTime, long endTime)
	{
		for (int i = 0; i < this.resultList.size(); i++)
		{
			Result result = this.resultList.get(i);
			SPTEMsg msg = result.getSpteMsg();
			if (msg.getTimeStamp() >= startTime && msg.getTimeStamp() < endTime)
			{
				int relativeTime = (int) msg.getTimeStamp() - (currentPageNum - 1) * this.nodeNumber * this.scale;
				int position = relativeTime / this.scale;
				if (msg.getICDMsg().getAttribute("sourceFunctionID") == null)
				{
					logger.warning(StringUtils.concat("�޷���ȡ��Ϣ��ԴID��msg=\n", msg.getICDMsg()));
					return;
				}
				boolean foundSrc = false;
				// ���ҳ�Դ���ܶ���
				for (Object child : this.headerContainerFgr.getChildren())
				{
					if (child instanceof HeaderFgr)
					{
						int srcId = (Integer) msg.getICDMsg().getAttribute("sourceFunctionID").getValue();
						HeaderFgr fgr = (HeaderFgr) child;
						if (srcId == fgr.getFunctionId())
						{
							foundSrc = true;
							Attribute att = msg.getICDMsg().getAttribute("msgTransType");
							Color arrowColor = null;
							if (!TemplateUtils.MSG_TRANS_TYPE_PERIOD.equals(att.getValue().toString()))
							{
								arrowColor = ColorConstants.blue;
							} else
							{
								arrowColor = ColorConstants.darkGreen;
							}
							ConnFgr conn = new ConnFgr(ColorConstants.darkGreen, arrowColor, result, caseManager, testedObjects, icdManager, this.compareResult);
							List<Integer> destIDs = msg.getICDMsg().getDestIDs();
							if (destIDs == null || destIDs.size() == 0)
							{
								logger.warning(StringUtils.concat("��ϢΪ�㲥��Ϣ��ȴ�Ҳ���Ŀ��ID��msg=\n", msg.getICDMsg()));
								return;
							} else if (destIDs.size() != 1)
							{
								logger.warning(StringUtils.concat("��ϢΪ�㲥��Ϣ��ȴ�����ж��Ŀ��ID��msg=\n", msg.getICDMsg()));
								return;
							}
							boolean foundDest = false;
							Integer destID = destIDs.get(0);
							for (Object kid : this.headerContainerFgr.getChildren())
							{
								if (kid instanceof HeaderFgr)
								{
									HeaderFgr hf = (HeaderFgr) kid;
									if (destID == hf.getFunctionId())
									{
										foundDest = true;
										conn.setSourceAnchor(new ItemBorderAnchor(fgr.getNodes().get(position)));
										conn.setTargetAnchor(new ItemBorderAnchor(hf.getNodes().get(position)));
										this.msgContainerFgr.add(conn);
										break;
									}
								}
							}
							if (!foundDest)
							{
								logger.warning(StringUtils.concat("�޷��ҵ���Ϣ��Ŀ�Ĺ��ܽڵ����msg=\n", msg.getICDMsg()));
								return;
							}
						}
					}
				}
				// �Ҳ���Դ���ܽڵ�
				if (!foundSrc)
				{
					logger.warning(StringUtils.concat("�Ҳ���Դ���ܽڵ㡣msg=\n", msg.getICDMsg()));
					return;
				}

			}
		}
	}

	@Override
	protected void showGotoPanelLeft(Composite timePanel)
	{
		super.showGotoPanelLeft(timePanel);
		GridLayout layout = new GridLayout(3, false);
		layout.horizontalSpacing = 15;
		leftPlaceHolder.setLayout(layout);
		Label label = new Label(leftPlaceHolder, SWT.NONE);
		label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 2));
		label.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
		label.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		label.setText("��Ϣ����:" + compareResult.getReslutList().size());
		int loss = compareResult.getAllLostCount();
		showCompareResultItem(leftPlaceHolder, "��ʧ����", loss);
		int timeout = compareResult.getTimeOut();
		showCompareResultItem(leftPlaceHolder, "��ʱ����", timeout);
		int error = compareResult.getErrorValue();
		showCompareResultItem(leftPlaceHolder, "�������", error);
		int unexpected = compareResult.getUnexpected();
		showCompareResultItem(leftPlaceHolder, "δ��������", unexpected);
	}

	/**
	 * ����Ϣ�����󲿷���ʾÿ�ִ�����Ϣ�����ͼ����Ӧ������
	 * 
	 * @param leftPlaceHolder
	 * @param type
	 * @param count
	 * @author zhangyi@tech.coretek.com.cn
	 */
	private void showCompareResultItem(Composite leftPlaceHolder, String type, int count)
	{
		StyledText text = new StyledText(leftPlaceHolder, SWT.READ_ONLY);
		StyleRange range = new StyleRange();
		GridData gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);

		text.setLayoutData(gridData);
		text.setCursor(Display.getCurrent().getSystemCursor(SWT.CURSOR_HAND));
		text.setToolTipText(StringUtils.concat("��ʾ��һ��", type.substring(0, type.lastIndexOf("����")), "��Ϣ"));
		if (type.length() < 5)
		{
			type = StringUtils.concat("  ", type);
		}
		text.setText(StringUtils.concat(type, ":", count));
		if (type.startsWith("  "))
		{
			range.start = 2;
		} else
		{
			range.start = 0;
		}
		range.length = text.getText().trim().length();
		range.underline = true;
		range.underlineColor = Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);
		text.setStyleRange(range);
		text.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
		text.setBlockSelection(false);
		text.addMouseListener(new MouseListener()
		{

			@Override
			public void mouseUp(MouseEvent e)
			{

			}

			@Override
			public void mouseDown(MouseEvent e)
			{
				StyledText label = (StyledText) e.widget;
				if (label.getText().trim().startsWith("��ʧ����") && !label.getText().endsWith(":0"))
				{
					LossInfoDialog dialog = new LossInfoDialog(label.getShell(), compareResult.getAllLost());
					dialog.open();
				}
				if (label.getText().trim().startsWith("��ʱ����") && !label.getText().endsWith(":0"))
				{
					showFirstErrorMsg(ErrorTypesEnum.TIMEOUT.getName());
					SequenceViewPart view = (SequenceViewPart) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(SequenceViewPart.ID);
					view.setErrorTypeNext(ErrorTypesEnum.TIMEOUT.getName());
					view.setErrorTypeLast(ErrorTypesEnum.TIMEOUT.getName());
					changeActionTooltip(view, "��ʱ");
				}
				if (label.getText().trim().startsWith("�������") && !label.getText().endsWith(":0"))
				{
					showFirstErrorMsg(ErrorTypesEnum.WRONGVALUE.getName());
					SequenceViewPart view = (SequenceViewPart) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(SequenceViewPart.ID);
					view.setErrorTypeNext(ErrorTypesEnum.WRONGVALUE.getName());
					view.setErrorTypeLast(ErrorTypesEnum.WRONGVALUE.getName());
					changeActionTooltip(view, "����");
				}
				if (label.getText().trim().startsWith("δ��������") && !label.getText().endsWith(":0"))
				{
					showFirstErrorMsg(ErrorTypesEnum.UNEXPECTED.getName());
					SequenceViewPart view = (SequenceViewPart) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(SequenceViewPart.ID);
					view.setErrorTypeNext(ErrorTypesEnum.UNEXPECTED.getName());
					view.setErrorTypeLast(ErrorTypesEnum.UNEXPECTED.getName());
					changeActionTooltip(view, "δ����");
				}
			}

			@Override
			public void mouseDoubleClick(MouseEvent e)
			{

			}
		});
	}

	/**
	 * �ڴ�����Ϣ����м����û��������������Ϣ���͵��¼��� �޸�ʱ��ͼ��ͼ����������Ϣ������������Tooltip
	 * 
	 * @param view
	 * @param str </br> <b>Author</b> ZHANG Yi </br> <b>Date</b> 2012-5-10
	 */
	private void changeActionTooltip(SequenceViewPart view, String str)
	{
		ActionContributionItem item1 = (ActionContributionItem) view.getViewSite().getActionBars().getToolBarManager().find(LastErrorDelegate.ID);
		item1.getAction().setToolTipText(new StringBuilder("��һ��").append(str).append("��Ϣ").toString());
		ActionContributionItem item2 = (ActionContributionItem) view.getViewSite().getActionBars().getToolBarManager().find(NextErrorDelegate.ID);
		item2.getAction().setToolTipText(new StringBuilder("��һ��").append(str).append("��Ϣ").toString());
	}

	/**
	 * ��ʱ��ͼ�������ʾ������Ϣ���͵ĵ�һ��������Ϣ
	 * 
	 * @param type ������Ϣ����
	 */
	private void showFirstErrorMsg(String type)
	{
		Result result = null;
		for (Result r : resultList)
		{
			if (r.getErrorTypes().size() != 0)
			{
				for (ErrorTypesEnum errorType : r.getErrorTypes())
				{
					if (type.equals(errorType.getName()))
					{
						if (result == null)
						{
							result = r;
						}
						if (result != null && r.getSpteMsg().getTimeStamp() < result.getSpteMsg().getTimeStamp())
						{
							result = r;
						}
						break;
					}
				}

			}
		}
		if (result == null)
		{
			return;
		}
		long time = result.getSpteMsg().getTimeStamp();
		int page = (int) (time / pageTime) + 1;
		currentPageNum = page;
		enableAll();
		if (currentPageNum == pageSum)
		{
			disableNext();
			disableLast();
		}
		if (currentPageNum == 1)
		{
			disableFirst();
			disablePrevious();
		}
		repaint();
		int position = (int) result.getSpteMsg().getTimeStamp() - (currentPageNum - 1) * this.nodeNumber * this.scale;
		int canvasHeight = getSequencePanel().getContent().getBounds().height;
		sequencePanel.setOrigin(0, (position - scale) * canvasHeight / pageTime);
	}

	/**
	 * ����ҳ�浼���е����а�ť <b>
	 */
	public void enableAll()
	{
		enableFirst();
		enableLast();
		enableLast();
		enablePrevious();
	}

}