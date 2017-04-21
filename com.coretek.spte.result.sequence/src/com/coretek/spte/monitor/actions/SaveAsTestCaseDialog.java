/************************************************************************
 *				北京科银京成技术有限公司 版权所有
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
 * 选择被测对象的对话框
 * 
 * @author 孙大巍 2012-4-23
 */
public class SaveAsTestCaseDialog extends MessageDialog
{
	private static final Logger	log	= LoggingPlugin.getLogger(SaveAsTestCaseDialog.class);

	private List<FunctionNode>	nodes;														// 候选节点

	private List<Result>		resultList;

	private CheckboxTableViewer	ctv;

	private ClazzManager		icdManager;

	private TestCase			sourceCase;												// 被参照的测试用例

	private TestCase			destCase;													// 生成的测试用例

	private Text				txtPath;													// 保存路径

	private Text				txtFile;													// 保存名

	private String				path;

	private String				fileName;

	public SaveAsTestCaseDialog(Shell parentShell, List<FunctionNode> nodes, List<Result> resultList, ClazzManager icdManager, TestCase sourceCase)
	{
		super(parentShell, "另存为测试用例", null, null, MessageDialog.NONE, new String[] { IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL }, 0);
		this.nodes = nodes;
		this.resultList = resultList;
		this.icdManager = icdManager;
		this.sourceCase = sourceCase;
	}

	/**
	 * 获取生成的测试用例
	 * 
	 * @return the destCase <br/>
	 * 
	 */
	public TestCase getDestCase()
	{
		return destCase;
	}

	/**
	 * 获取用户设置的保存路径
	 * 
	 * @return the path <br/>
	 * 
	 */
	public String getPath()
	{
		return path;
	}

	/**
	 * 获取用户设置的文件名
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
	 * swt.widgets.Composite) <br/> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-4-23
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
	 * 显示保存设置
	 * 
	 * @param parent
	 * @return </br>
	 */
	private void showSaveSettings(Composite panel)
	{
		Group group = new Group(panel, SWT.NONE);
		group.setText("保存设置");
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		group.setLayoutData(gridData);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		group.setLayout(layout);

		this.showSavePath(group);

		this.showFileName(group);

	}

	/**
	 * 显示保存路径组件
	 * 
	 * @param parent </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-4-25
	 */
	private void showSavePath(Composite parent)
	{
		Label label = new Label(parent, SWT.RIGHT);
		label.setText("保存路径");
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
		btn.setText("浏览...");
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
	 * 显示文件名组件
	 * 
	 * @param parent </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-4-25
	 */
	private void showFileName(Composite parent)
	{
		Label label = new Label(parent, SWT.RIGHT);
		label.setText("文件名");
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
	 * 显示ICD文件信息
	 * 
	 * @param parent </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-4-23
	 */
	private void showICDInfo(Composite panel)
	{
		Group parent = new Group(panel, SWT.NONE);
		parent.setText("ICD信息");

		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		parent.setLayoutData(gridData);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		parent.setLayout(layout);

		this.showICDName(parent);
		this.showICDVersion(parent);

	}

	/**
	 * 显示ICD文件的名字
	 * 
	 * @param parent </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-4-25
	 */
	private void showICDName(Composite parent)
	{
		Label label = new Label(parent, SWT.RIGHT);
		label.setText("文件名:");
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
	 * 显示ICD文件的版本号
	 * 
	 * @param parent </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-4-25
	 */
	private void showICDVersion(Composite parent)
	{
		Label label = new Label(parent, SWT.RIGHT);
		label.setText("版本号:");
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
	 * 显示候选节点
	 * 
	 * @param parent </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-4-23
	 */
	private void showNodes(Composite panel, List<FunctionNode> nodes)
	{
		Group group = new Group(panel, SWT.NONE);
		group.setText("候选节点");
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		group.setLayoutData(gridData);
		group.setLayout(new FillLayout());

		Table table = new Table(group, SWT.CHECK | SWT.VIRTUAL);

		TableColumn column = new TableColumn(table, SWT.CENTER);
		column.setText("节点");
		column.setWidth(200);
		ctv = new CheckboxTableViewer(table);
		ctv.setLabelProvider(new TableLabelProvider());
		ctv.setContentProvider(new TableContentProvider());
		ctv.setInput(nodes);
	}

	/**
	 * 判断消息是否为周期消息的第一个周期
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
	 * 生成周期消息
	 * 
	 * @param source 复制源消息
	 * @param send 是否为发送消息
	 * @param recv 是否为接收消息
	 * @param counter 计数器
	 * @return </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-4-25
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
	 * 生成消息
	 * 
	 * @param source 生成消息的参照
	 * @param send
	 * @param recv
	 * @return </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-4-25
	 */
	private Message getMessage(Message source, boolean send, boolean recv)
	{
		Message wanted = null;
		if (recv)
		{// 接收消息
			try
			{
				wanted = (Message) source.clone();
				wanted.setDirection(XMLBean.RECV_MSG);
			} catch (CloneNotSupportedException e)
			{
				e.printStackTrace();
			}
		} else if (send)
		{// 发送消息
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
			log.warning(StringUtils.concat("消息", source.getUuid(), "既不是发送消息也不是接收消息"));
		}
		if (wanted != null)
			wanted.setUuid(StringUtils.getUUID());

		return wanted;
	}

	/**
	 * 获取被用户选择为被测对象的节点集合
	 * 
	 * @param objs
	 * @return </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-4-25
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
	 * 生成测试用例
	 * 
	 * @param destMsgs
	 * @return </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-4-25
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
				// 生成的目标消息集合
				List<Entity> destMsgs = this.getDestMsgs(testedObjects);
				this.destCase = this.getTestCase(destMsgs);
			}
		}
		super.buttonPressed(buttonId);
		System.gc();
	}

	/**
	 * 获取由执行结果生成的消息集合
	 * 
	 * @param testedObjects
	 * @return </br>
	 */
	private List<Entity> getDestMsgs(List<Entity> testedObjects)
	{
		// 生成的目标消息集合
		List<Entity> destMsgs = new ArrayList<Entity>(100);
		int counter = 0;
		for (Result result : resultList)
		{
			counter++;
			Message source = result.getSpteMsg().getMsg();
			FunctionNodeMsg nodeMsg = this.icdManager.getFunctionNodeMsg(source.getId());
			boolean recv = TemplateUtils.isRecvMsg(nodeMsg, testedObjects);
			boolean send = TemplateUtils.isSendMsg(nodeMsg, testedObjects);
			Message wanted = null;// 被复制生成的消息
			if (this.isTheFirstPeriod(source))
			{// 周期消息并是第一个周期
				wanted = this.getPeriodMsg(source, send, recv, counter);

			} else if (!source.isPeriodMessage())
			{// 非周期消息
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