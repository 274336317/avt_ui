/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.coretek.common.logging.LoggingPlugin;
import com.coretek.common.template.ClazzManager;
import com.coretek.common.template.TemplateUtils;
import com.coretek.common.template.XMLBean;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.FunctionNode;
import com.coretek.spte.FunctionNodeMsg;
import com.coretek.spte.dataCompare.Result;
import com.coretek.spte.testcase.Message;
import com.coretek.spte.testcase.MessageBlock;
import com.coretek.spte.testcase.Period;
import com.coretek.spte.testcase.TestCase;

/**
 * ѡ�񱻲����ĶԻ���
 * 
 * @author ���Ρ 2012-4-23
 */
public class SaveAsTestCaseDialog extends MessageDialog
{
	private static final Logger	log	= LoggingPlugin.getLogger(SaveAsTestCaseDialog.class);

	private List<FunctionNode>	nodes;														// ��ѡ�ڵ�

	private List<Result>		resultList;

	private CheckboxTableViewer	ctv;

	private ClazzManager		icdManager;

	private TestCase			sourceCase;												// �����յĲ�������

	private TestCase			destCase;													// ���ɵĲ�������

	private Text				txtPath;													// ����·��

	private Text				txtFile;													// ������

	private String				path;

	private String				fileName;

	public SaveAsTestCaseDialog(Shell parentShell, List<FunctionNode> nodes, List<Result> resultList, ClazzManager icdManager, TestCase sourceCase)
	{
		super(parentShell, "���Ϊ��������", null, null, MessageDialog.NONE, new String[] { IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL }, 0);
		this.nodes = nodes;
		this.resultList = resultList;
		this.icdManager = icdManager;
		this.sourceCase = sourceCase;
	}

	/**
	 * ��ȡ���ɵĲ�������
	 * 
	 * @return the destCase <br/>
	 * 
	 */
	public TestCase getDestCase()
	{
		return destCase;
	}

	/**
	 * ��ȡ�û����õı���·��
	 * 
	 * @return the path <br/>
	 * 
	 */
	public String getPath()
	{
		return path;
	}

	/**
	 * ��ȡ�û����õ��ļ���
	 * 
	 * @return the fileName <br/>
	 * 
	 */
	public String getFileName()
	{
		return fileName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.MessageDialog#createCustomArea(org.eclipse.
	 * swt.widgets.Composite) <br/> <b>����</b> ���Ρ </br> <b>����</b> 2012-4-23
	 */
	@Override
	protected Control createCustomArea(Composite parent)
	{
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		parent.setLayout(layout);
		Composite panel = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		panel.setLayoutData(gridData);
		layout = new GridLayout();
		layout.numColumns = 1;
		panel.setLayout(layout);

		this.showICDInfo(panel);
		this.showNodes(panel, this.nodes);
		this.showSaveSettings(panel);

		return panel;
	}

	/**
	 * ��ʾ��������
	 * 
	 * @param parent
	 * @return </br>
	 */
	private void showSaveSettings(Composite panel)
	{
		Group group = new Group(panel, SWT.NONE);
		group.setText("��������");
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		group.setLayoutData(gridData);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		group.setLayout(layout);

		this.showSavePath(group);

		this.showFileName(group);

	}

	/**
	 * ��ʾ����·�����
	 * 
	 * @param parent </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-4-25
	 */
	private void showSavePath(Composite parent)
	{
		Label label = new Label(parent, SWT.RIGHT);
		label.setText("����·��");
		GridData gridData = new GridData();
		gridData.widthHint = 50;
		gridData.heightHint = 15;
		label.setLayoutData(gridData);

		this.txtPath = new Text(parent, SWT.BORDER);
		gridData = new GridData();
		gridData.widthHint = 200;
		gridData.heightHint = 15;
		this.txtPath.setLayoutData(gridData);

		Button btn = new Button(parent, SWT.PUSH);
		btn.setText("���...");
		btn.addSelectionListener(new SelectionListener()
		{

			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				DirectoryDialog dialog = new DirectoryDialog(Display.getCurrent().getActiveShell());
				txtPath.setText(dialog.open());
			}

		});
	}

	/**
	 * ��ʾ�ļ������
	 * 
	 * @param parent </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-4-25
	 */
	private void showFileName(Composite parent)
	{
		Label label = new Label(parent, SWT.RIGHT);
		label.setText("�ļ���");
		GridData gridData = new GridData();
		gridData.widthHint = 50;
		gridData.heightHint = 15;
		label.setLayoutData(gridData);

		this.txtFile = new Text(parent, SWT.BORDER);
		gridData = new GridData();
		gridData.widthHint = 200;
		gridData.heightHint = 15;
		this.txtFile.setLayoutData(gridData);

	}

	/**
	 * ��ʾICD�ļ���Ϣ
	 * 
	 * @param parent </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-4-23
	 */
	private void showICDInfo(Composite panel)
	{
		Group parent = new Group(panel, SWT.NONE);
		parent.setText("ICD��Ϣ");

		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		parent.setLayoutData(gridData);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		parent.setLayout(layout);

		this.showICDName(parent);
		this.showICDVersion(parent);

	}

	/**
	 * ��ʾICD�ļ�������
	 * 
	 * @param parent </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-4-25
	 */
	private void showICDName(Composite parent)
	{
		Label label = new Label(parent, SWT.RIGHT);
		label.setText("�ļ���:");
		GridData gridData = new GridData();
		gridData.widthHint = 50;
		label.setLayoutData(gridData);

		Text text = new Text(parent, SWT.BORDER);
		text.setText(TemplateUtils.getICDOfTestCase(this.sourceCase));
		gridData = new GridData();
		gridData.widthHint = 200;
		text.setLayoutData(gridData);
		text.setEditable(false);
	}

	/**
	 * ��ʾICD�ļ��İ汾��
	 * 
	 * @param parent </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-4-25
	 */
	private void showICDVersion(Composite parent)
	{
		Label label = new Label(parent, SWT.RIGHT);
		label.setText("�汾��:");
		GridData gridData = new GridData();
		gridData.widthHint = 50;
		label.setLayoutData(gridData);

		Text text = new Text(parent, SWT.BORDER);
		text.setText(TemplateUtils.getVerion(icdManager));
		gridData = new GridData();
		gridData.widthHint = 200;
		text.setLayoutData(gridData);
		text.setEditable(false);
	}

	/**
	 * ��ʾ��ѡ�ڵ�
	 * 
	 * @param parent </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-4-23
	 */
	private void showNodes(Composite panel, List<FunctionNode> nodes)
	{
		Group group = new Group(panel, SWT.NONE);
		group.setText("��ѡ�ڵ�");
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		group.setLayoutData(gridData);
		group.setLayout(new FillLayout());

		Table table = new Table(group, SWT.CHECK | SWT.VIRTUAL);

		TableColumn column = new TableColumn(table, SWT.CENTER);
		column.setText("�ڵ�");
		column.setWidth(200);
		ctv = new CheckboxTableViewer(table);
		ctv.setLabelProvider(new TableLabelProvider());
		ctv.setContentProvider(new TableContentProvider());
		ctv.setInput(nodes);
	}

	/**
	 * �ж���Ϣ�Ƿ�Ϊ������Ϣ�ĵ�һ������
	 * 
	 * @param msg
	 * @return </br>
	 */
	private boolean isTheFirstPeriod(Message msg)
	{
		List<Entity> children = msg.getChildren();
		if (children == null || children.size() == 0)
			return false;
		Entity entity = children.get(0);
		if (entity instanceof Period)
		{
			Period period = (Period) entity;
			if (period.getValue().intValue() == 1)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * ����������Ϣ
	 * 
	 * @param source ����Դ��Ϣ
	 * @param send �Ƿ�Ϊ������Ϣ
	 * @param recv �Ƿ�Ϊ������Ϣ
	 * @param counter ������
	 * @return </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-4-25
	 */
	private Message getPeriodMsg(Message source, boolean send, boolean recv, int counter)
	{
		Message wanted = this.getMessage(source, send, recv);
		if (wanted != null)
		{
			Period period = (Period) wanted.getChildren().get(0);
			period.setValue(Integer.valueOf(1));

			for (int i = counter, length = resultList.size(); i < length; i++)
			{
				Result result2 = this.resultList.get(i);
				Message source2 = result2.getSpteMsg().getMsg();
				if (source2.getUuid().equals(source.getUuid()))
				{
					try
					{
						Message wanted2 = (Message) source2.clone();
						List<Entity> periods = wanted2.getChildren();
						int periodValue = wanted.getChildren().size() + 1;
						period = (Period) periods.get(0);
						period.setValue(periodValue);
						wanted.addChild(period);
					} catch (CloneNotSupportedException e)
					{
						e.printStackTrace();
						break;
					}
				}
			}
		}

		return wanted;
	}

	/**
	 * ������Ϣ
	 * 
	 * @param source ������Ϣ�Ĳ���
	 * @param send
	 * @param recv
	 * @return </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-4-25
	 */
	private Message getMessage(Message source, boolean send, boolean recv)
	{
		Message wanted = null;
		if (recv)
		{// ������Ϣ
			try
			{
				wanted = (Message) source.clone();
				wanted.setDirection(XMLBean.RECV_MSG);
			} catch (CloneNotSupportedException e)
			{
				e.printStackTrace();
			}
		} else if (send)
		{// ������Ϣ
			try
			{
				wanted = (Message) source.clone();
				wanted.setDirection(XMLBean.SEND_MSG);
			} catch (CloneNotSupportedException e)
			{
				e.printStackTrace();
			}
		} else
		{
			log.warning(StringUtils.concat("��Ϣ", source.getUuid(), "�Ȳ��Ƿ�����ϢҲ���ǽ�����Ϣ"));
		}
		if (wanted != null)
			wanted.setUuid(StringUtils.getUUID());

		return wanted;
	}

	/**
	 * ��ȡ���û�ѡ��Ϊ�������Ľڵ㼯��
	 * 
	 * @param objs
	 * @return </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-4-25
	 */
	private List<Entity> getTestedObjects()
	{
		Object[] objs = this.ctv.getCheckedElements();
		List<Entity> testedObjects = new ArrayList<Entity>(objs.length);
		for (Object obj : objs)
		{
			testedObjects.add((Entity) obj);
		}

		return testedObjects;
	}

	/**
	 * ���ɲ�������
	 * 
	 * @param destMsgs
	 * @return </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-4-25
	 */
	private TestCase getTestCase(List<Entity> destMsgs)
	{
		TestCase destCase = null;
		try
		{
			destCase = (TestCase) this.sourceCase.clone();
			List<Entity> kids = destCase.getChildren();
			for (Entity entity : kids)
			{
				if (entity instanceof MessageBlock)
				{
					MessageBlock block = (MessageBlock) entity;
					block.setChildren(destMsgs);
					break;
				}
			}
		} catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}

		return destCase;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.MessageDialog#buttonPressed(int) <br/>
	 */
	@Override
	protected void buttonPressed(int buttonId)
	{
		if (Window.OK == buttonId)
		{
			this.path = this.txtPath.getText();
			this.fileName = this.txtFile.getText();

			List<Entity> testedObjects = this.getTestedObjects();

			if (testedObjects.size() != 0)
			{
				// ���ɵ�Ŀ����Ϣ����
				List<Entity> destMsgs = this.getDestMsgs(testedObjects);
				this.destCase = this.getTestCase(destMsgs);
			}
		}
		super.buttonPressed(buttonId);
		System.gc();
	}

	/**
	 * ��ȡ��ִ�н�����ɵ���Ϣ����
	 * 
	 * @param testedObjects
	 * @return </br>
	 */
	private List<Entity> getDestMsgs(List<Entity> testedObjects)
	{
		// ���ɵ�Ŀ����Ϣ����
		List<Entity> destMsgs = new ArrayList<Entity>(100);
		int counter = 0;
		for (Result result : resultList)
		{
			counter++;
			Message source = result.getSpteMsg().getMsg();
			FunctionNodeMsg nodeMsg = this.icdManager.getFunctionNodeMsg(source.getId());
			boolean recv = TemplateUtils.isRecvMsg(nodeMsg, testedObjects);
			boolean send = TemplateUtils.isSendMsg(nodeMsg, testedObjects);
			Message wanted = null;// ���������ɵ���Ϣ
			if (this.isTheFirstPeriod(source))
			{// ������Ϣ���ǵ�һ������
				wanted = this.getPeriodMsg(source, send, recv, counter);

			} else if (!source.isPeriodMessage())
			{// ��������Ϣ
				wanted = this.getMessage(source, send, recv);

			}

			if (wanted != null)
				destMsgs.add(wanted);

		}

		return destMsgs;
	}

	private static class TableContentProvider implements IStructuredContentProvider
	{

		@Override
		public Object[] getElements(Object inputElement)
		{
			if (inputElement instanceof List<?>)
			{
				List<?> list = (List<?>) inputElement;

				return list.toArray();
			}
			return new Object[0];
		}

		@Override
		public void dispose()
		{

		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
		{

		}

	}

	private static class TableLabelProvider implements ITableLabelProvider
	{

		@Override
		public Image getColumnImage(Object element, int columnIndex)
		{

			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex)
		{
			if (element instanceof FunctionNode)
			{
				FunctionNode node = (FunctionNode) element;
				return node.getName();
			}
			return StringUtils.EMPTY_STRING;
		}

		@Override
		public void addListener(ILabelProviderListener listener)
		{

		}

		@Override
		public void dispose()
		{

		}

		@Override
		public boolean isLabelProperty(Object element, String property)
		{

			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener)
		{

		}

	}
}