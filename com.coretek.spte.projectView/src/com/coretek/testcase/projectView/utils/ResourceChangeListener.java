/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.testcase.projectView.utils;

import java.util.logging.Logger;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Display;

import com.coretek.common.logging.LoggingPlugin;
import com.coretek.common.utils.EclipseUtils;
import com.coretek.spte.core.util.ICDFileManager;
import com.coretek.spte.core.util.Utils;
import com.coretek.testcase.projectView.views.ProjectView;

/**
 * 监听工作空间的资源变化，以便检查ICD文件的MD5值
 * 
 * @author SunDawei 2012-5-19
 */
public class ResourceChangeListener implements IResourceChangeListener
{
	private final static Logger	logger	= LoggingPlugin.getLogger(ResourceChangeListener.class);

	public void resourceChanged(IResourceChangeEvent event)
	{
		IResourceDelta delta = event.getDelta();
		if (event.getType() == IResourceChangeEvent.POST_CHANGE || event.getType() == IResourceChangeEvent.PRE_DELETE)
		{
			if (delta != null)
			{
				try
				{
					delta.accept(new IResourceDeltaVisitor()
					{

						public boolean visit(IResourceDelta delta) throws CoreException
						{
							IResource resource = delta.getResource();
							if (resource.getType() == IResource.PROJECT)
							{
								IProject prj = (IProject) resource;
								return handleProject(prj, delta);
							}
							if (resource != null && resource.getType() == IResource.FILE)
							{
								IFile file = (IFile) resource;
								handleFile(file, delta);
							}

							return true;
						}

					});
				} catch (CoreException e)
				{
					e.printStackTrace();
				}
			} else if (event.getResource() != null && event.getResource() instanceof IProject)
			{
				logger.config("ICD工程[" + event.getResource().getName() + "]被删除");
				IProject prj = (IProject) event.getResource();
				if (Utils.isICDProject(prj))
				{
					ICDFileManager.getInstance().remove(prj);
					this.refreshProjectView();
				}
			}

		}

	}

	private boolean handleProject(IProject prj, IResourceDelta delta)
	{
		if (prj.exists() && Utils.isICDProject(prj))
		{
			if (delta.getKind() == IResourceDelta.REMOVED)
			{
				logger.config("ICD工程[" + prj.getName() + "]被删除");
				ICDFileManager.getInstance().remove(prj);
				this.refreshProjectView();
				return false;
			}
			return true;
		}
		return false;
	}

	private void handleFile(IFile file, IResourceDelta delta)
	{
		IProject prj = file.getProject();
		if (prj.isAccessible() && prj.isOpen() && Utils.isICDProject(prj))
		{
			if ((delta.getFlags() & IResourceDelta.CONTENT) != 0)
			{
				if (Utils.isICDFile(file))
				{
					// ICD文件内容被改变
					logger.config("ICD文件[" + file.getFullPath() + "]内容被改变");
					if (!ICDFileManager.getInstance().updateFile(file))
					{
						logger.warning("更新icd文件[" + file.getFullPath() + "]的md5值失败！icdFile=" + file.getLocation().toString());
					}
				}
			} else if (delta.getKind() == IResourceDelta.ADDED && Utils.isICDFile(file))
			{
				// ICD文件添加
				logger.config("ICD文件[" + file.getFullPath() + "]添加");
				ICDFileManager.getInstance().addICDFile(file);
			} else if ((delta.getFlags() & IResourceDelta.MOVED_FROM) != 0)
			{
				// ICD文件被移动
				logger.config("ICD文件MOVED_FROM");
			} else if ((delta.getFlags() & IResourceDelta.MOVED_TO) != 0)
			{
				// ICD文件被移动
				logger.config("ICD文件[" + file.getFullPath() + "] MOVED_TO");
				ICDFileManager.getInstance().remove(file);
			} else if (delta.getKind() == IResourceDelta.REMOVED)
			{
				// ICD文件被删除
				ICDFileManager.getInstance().remove(file);
			} else if ((delta.getFlags() & IResourceDelta.REPLACED) != 0)
			{
				// ICD文件被替换
				logger.config("ICD文件[" + file.getFullPath() + "]被替换");
			}
			refreshProjectView();
		}
	}

	private void refreshProjectView()
	{
		if (Display.getDefault() != null)
		{
			Display.getDefault().syncExec(new Runnable()
			{
				public void run()
				{
					ProjectView part = (ProjectView) EclipseUtils.getActivePage().findView("com.coretek.tools.ide.ui.DiagramView");
					if (part != null)
						part.refresh();
				}

			});
		}
	}
}