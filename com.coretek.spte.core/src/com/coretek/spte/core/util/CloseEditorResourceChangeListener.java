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
 * �������������ļ��Ƿ�ɾ���������ɾ����رյ��Ѿ��ڱ༭���д򿪵Ĳ�������
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
		{// ɾ������
			IProject project = (IProject) event.getResource();
			log.info("���������� [" + project.getName() + "] ��ɾ����");
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
		{// �رչ���
			IProject project = (IProject) event.getResource();
			log.info("���������� [" + project.getName() + "] ���ر��ˡ�");
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
	 * ��IFileת��Ϊ��·����ʾ�������ж��Ƿ�Ϊͬһ��IFile
	 * 
	 * @param files ��Ҫת����IFile�б�
	 * @param truncate �Ƿ�ضϹ����ռ������·��
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