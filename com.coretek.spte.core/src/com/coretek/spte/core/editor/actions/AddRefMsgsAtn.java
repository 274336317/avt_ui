/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.core.editor.actions;

import java.util.List;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;

import com.coretek.common.template.XMLBean;
import com.coretek.common.utils.EclipseUtils;
import com.coretek.spte.core.commands.AddingRefMsgsCmd;
import com.coretek.spte.core.dialogs.TestCaseSelectionDlg;
import com.coretek.spte.core.editor.SPTEEditor;
import com.coretek.spte.core.models.TestNodeMdl;
import com.coretek.spte.core.models.TestToolMdl;
import com.coretek.spte.core.parts.TestNodePart;
import com.coretek.spte.core.util.Utils;

/**
 * @author SunDawei 2012-5-19
 */
public class AddRefMsgsAtn extends SelectionAction
{

	public static final String	ID	= "addRefMsgsAtn";

	/**
	 * @param part </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-5-19
	 */
	public AddRefMsgsAtn(IWorkbenchPart part)
	{
		super(part);
		this.setLazyEnablementCalculation(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#init() <br/>
	 * <b>Author</b> SunDawei </br> <b>Date</b> 2012-5-19
	 */
	@Override
	protected void init()
	{
		super.init();
		this.setText("引用消息");
		this.setId(ID);
	}

	/**
	 * @param part
	 * @param style </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-5-19
	 */
	public AddRefMsgsAtn(IWorkbenchPart part, int style)
	{
		super(part, style);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 * <br/> <b>Author</b> SunDawei </br> <b>Date</b> 2012-5-19
	 */
	@Override
	protected boolean calculateEnabled()
	{
		boolean result = false;
		List<? extends Object> list = this.getSelectedObjects();
		if (list != null && list.size() == 1)
		{
			Object obj = list.get(0);
			if (obj instanceof TestNodePart)
			{
				TestNodePart targetNode = (TestNodePart) obj;
				TestNodeMdl node = (TestNodeMdl) targetNode.getModel();
				if (node.getParent() instanceof TestToolMdl)
				{// 将被选中的node转换为被测对象内包含的对应的node
					node = (TestNodeMdl) Utils.getTestedObject(node).getChildren().get(node.getParent().getChildren().indexOf(node));
				}

				if (Utils.isInsideCycleMessage(node) || Utils.isCycleChildMessage(node))
				{
					// 禁止在周期消息之间 及 子节点的node上执行添加节点的操作
					result = false;
				} else if (Utils.isBetweenTimeredConnections(node))
				{
					// 禁止在拥有时间间隔的node上执行添加节点的操作
					result = false;
				} else
				{
					result = true;
				}
			}
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run() <br/> <b>Author</b> SunDawei
	 * </br> <b>Date</b> 2012-5-19
	 */
	@Override
	public void run()
	{
		TestNodePart part = (TestNodePart) this.getSelectedObjects().get(0);
		TestNodeMdl node = (TestNodeMdl) part.getModel();
		TestCaseSelectionDlg dlg = new TestCaseSelectionDlg(Display.getCurrent().getActiveShell(), node);
		SPTEEditor editor = (SPTEEditor) EclipseUtils.getActiveEditor();
		if (dlg.open() == Window.OK)
		{
			List<XMLBean> list = dlg.getTargetMsgs();
			AddingRefMsgsCmd cmd = new AddingRefMsgsCmd(node, list, editor.getFighterClazzManager());
			editor.getEditDomain().getCommandStack().execute(cmd);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#isEnabled() <br/>
	 * <b>Author</b> SunDawei </br> <b>Date</b> 2012-5-19
	 */
	@Override
	public boolean isEnabled()
	{

		return super.isEnabled();
	}

}