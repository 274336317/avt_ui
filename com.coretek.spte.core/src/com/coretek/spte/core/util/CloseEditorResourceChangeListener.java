/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Display;

import com.coretek.common.logging.LoggingPlugin;
import com.coretek.common.utils.EclipseUtils;

/**
 * 监听测试用例文件是否被删除，如果被删除则关闭掉已经在编辑器中打开的测试用例
 * 
 * @author SunDawei 2012-6-7
 */
public class CloseEditorResourceChangeListener implements IResourceChangeListener
{
	private final static Logger	log	= LoggingPlugin.getLogger(CloseEditorResourceChangeListener.class);

	public void resourceChanged(IResourceChangeEvent event)
	{
		IResourceDelta delta = event.getDelta();
		if (event.getType() == IResourceChangeEvent.PRE_DELETE)
		{// 删除工程
			IProject project = (IProject) event.getResource();
			log.info("监听到工程 [" + project.getName() + "] 被删除。");
			this.handleProject(project);

		} else if (event.getType() == IResourceChangeEvent.POST_CHANGE)
		{
			try
			{
				delta.accept(new Visitor());
			} catch (CoreException e)
			{
				e.printStackTrace();
			}
		} else if (event.getType() == IResourceChangeEvent.PRE_CLOSE)
		{// 关闭工程
			IProject project = (IProject) event.getResource();
			log.info("监听到工程 [" + project.getName() + "] 被关闭了。");
			this.handleProject(project);
		}

	}

	private void handleProject(IProject project)
	{
		final List<IFile> files = new ArrayList<IFile>();
		if (Utils.isICDProject(project))
		{
			List<IFile> icdFiles = Utils.getAllICDFilesInProject(project);
			List<String> icdFilesStr = changeIFileList2String(icdFiles, true);
			for (IFile caseFile : Utils.getAllCasesInWorkspace())
			{
				String icdFileStr = Utils.getICDFilePath(caseFile);
				if (icdFilesStr.contains(icdFileStr))
				{
					files.add(caseFile);
				}
			}
		} else if (Utils.isSoftwareTestingProject(project))
		{
			files.addAll(Utils.getAllCasesInProject(project));
		}
		Display.getDefault().syncExec(new Runnable()
		{

			public void run()
			{
				for (IFile file : files)
				{
					EclipseUtils.closeEditor(file);
				}
			}
		});
	}

	/**
	 * 将IFile转化为其路径标示，用于判定是否为同一个IFile
	 * 
	 * @param files 所要转化的IFile列表
	 * @param truncate 是否截断工作空间的所在路径
	 * @return
	 */
	private List<String> changeIFileList2String(List<IFile> files, boolean truncate)
	{
		List<String> strs = new ArrayList<String>();
		String workspaceStr = ResourcesPlugin.getWorkspace().getRoot().getLocation().toPortableString();
		for (IFile file : files)
		{
			String str = file.getLocation().toPortableString();
			if (truncate)
			{
				str = str.replace(workspaceStr, "");
			}
			strs.add(str);
		}
		return strs;
	}

	private static class Visitor implements IResourceDeltaVisitor
	{
		public boolean visit(IResourceDelta delta) throws CoreException
		{
			IResource resource = delta.getResource();
			if (resource != null && resource.getType() == IResource.FILE && delta.getKind() == IResourceDelta.REMOVED)
			{
				final IFile file = (IFile) resource;
				Display.getDefault().syncExec(new Runnable()
				{

					public void run()
					{
						EclipseUtils.closeEditor(file);
					}

				});
			}
			return true;
		}
	}
}