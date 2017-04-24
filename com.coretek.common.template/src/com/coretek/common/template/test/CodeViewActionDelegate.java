/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template.test;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import com.coretek.common.template.build.JavaCodeGenerator;

/**
 * @author 孙大巍 2011-12-27
 */
public class CodeViewActionDelegate implements IViewActionDelegate
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IViewActionDelegate#init(org.eclipse.ui.IViewPart)
	 * <br/> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-27
	 */
	@Override
	public void init(IViewPart view)
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 * <br/> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-27
	 */
	@Override
	public void run(IAction action)
	{
		// JavaCodeGenerator.main(new String[0]);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action
	 * .IAction, org.eclipse.jface.viewers.ISelection) <br/> <b>作者</b> 孙大巍 </br>
	 * <b>日期</b> 2011-12-27
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection)
	{
		// TODO Auto-generated method stub

	}

}
