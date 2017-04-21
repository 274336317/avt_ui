/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.testcase.testcaseview;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Display;

import com.coretek.spte.core.util.Utils;

/**
 * 监听测试用例文件被删除，更新测试用例视图
 * 
 * @author SunDawei 2012-6-7
 */
public class CaseChangeListener implements IResourceChangeListener
{

	private TestCaseViewPart	viewPart;

	public CaseChangeListener(TestCaseViewPart viewPart)
	{
		this.viewPart = viewPart;
	}

	public void resourceChanged(IResourceChangeEvent event)
	{
		IResourceDelta delta = event.getDelta();
		if (event.getType() == IResourceChangeEvent.PRE_DELETE || event.getType() == IResourceChangeEvent.PRE_CLOSE)
		{// 删除工程或关闭工程
			IProject project = (IProject) event.getResource();
			if (Utils.isSoftwareTestingProject(project))
			{
				this.viewPart.refereshTable();
			}

		} else if (event.getType() == IResourceChangeEvent.POST_CHANGE)
		{
			try
			{
				delta.accept(new Visitor());
			} catch (CoreException e)
			{
				e.printStackTrace();
			}
		}

	}

	private class Visitor implements IResourceDeltaVisitor
	{

		public boolean visit(IResourceDelta delta) throws CoreException
		{
			IResource resource = delta.getResource();
			if (resource != null && resource.getType() == IResource.FILE)
			{
				IFile file = (IFile) resource;
				if (Utils.isCasFile(file))
				{
					if (delta.getKind() == IResourceDelta.ADDED || delta.getKind() == IResourceDelta.REMOVED || delta.getKind() == IResourceDelta.MOVED_FROM || delta.getKind() == IResourceDelta.MOVED_TO)
					{
						Display.getDefault().syncExec(new Runnable()
						{

							public void run()
							{
								viewPart.refereshTable();

							}

						});
					}
				}

			}
			return true;
		}

	}

}
