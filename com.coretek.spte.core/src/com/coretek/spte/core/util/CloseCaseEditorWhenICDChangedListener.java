/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.core.util;

import java.util.List;
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
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.part.FileEditorInput;

import com.coretek.common.logging.LoggingPlugin;
import com.coretek.common.utils.EclipseUtils;
import com.coretek.spte.core.editor.SPTEEditor;

/**
 * 用于监听ICD文件发生变化时，依赖与变化的ICD文件的测试用例，如果在编辑器中被打开，则关闭编辑器。
 * 
 * @author SunDawei 2012-7-27
 */
public class CloseCaseEditorWhenICDChangedListener implements IResourceChangeListener
{

	private final static Logger log = LoggingPlugin.getLogger(CloseCaseEditorWhenICDChangedListener.class);

	public void resourceChanged(IResourceChangeEvent event)
	{
		IResourceDelta delta = event.getDelta();
		if (event.getType() == IResourceChangeEvent.PRE_DELETE)
		{// 删除工程
			IProject project = (IProject) event.getResource();
			log.info("监听到工程 [" + project.getName() + "] 被删除。");
			this.closeEditors(project);

		}
		else if (event.getType() == IResourceChangeEvent.POST_CHANGE)
		{
			try
			{
				delta.accept(new Visitor());
			}
			catch (CoreException e)
			{
				e.printStackTrace();
			}
		}
		else if (event.getType() == IResourceChangeEvent.PRE_CLOSE)
		{// 关闭工程
			IProject project = (IProject) event.getResource();
			log.info("监听到工程 [" + project.getName() + "] 被关闭了。");
			this.closeEditors(project);
		}

	}

	/**
	 * 关闭测试用例编辑器
	 * 
	 * @param project
	 */
	private void closeEditors(IProject project)
	{
		List<IFile> list = Utils.getAllICDFilesInProject(project);
		IEditorReference[] ers = EclipseUtils.getAllEditors();
		for (IEditorReference re : ers)
		{
			IEditorPart part = re.getEditor(false);
			if (part.getClass() == SPTEEditor.class)
			{
				SPTEEditor editor = (SPTEEditor) part;
				FileEditorInput input = (FileEditorInput) editor.getEditorInput();
				IFile caseFile = input.getFile();
				IFile icdFile = Utils.getICDFile(caseFile);
				for (IFile icdFile2 : list)
				{
					if (icdFile2.equals(icdFile))
					{
						EclipseUtils.closeEditor(caseFile);
					}
				}
			}
		}
	}

	/**
	 * 关闭测试用例编辑器
	 * 
	 * @param icdFile
	 */
	private void closeEditors(final IFile icdFile)
	{
		Display.getDefault().syncExec(new Runnable()
		{

			public void run()
			{
				IEditorReference[] ers = EclipseUtils.getAllEditors();
				for (IEditorReference re : ers)
				{
					IEditorPart part = re.getEditor(false);
					if (part.getClass() == SPTEEditor.class)
					{
						SPTEEditor editor = (SPTEEditor) part;
						FileEditorInput input = (FileEditorInput) editor.getEditorInput();
						IFile caseFile = input.getFile();
						IFile icdFile2 = Utils.getICDFile(caseFile);
						if (icdFile.equals(icdFile2))
						{
							EclipseUtils.closeEditor(caseFile);
						}
					}
				}
			}

		});

	}

	private class Visitor implements IResourceDeltaVisitor
	{

		public boolean visit(IResourceDelta delta) throws CoreException
		{
			IResource resource = delta.getResource();
			if (resource != null && resource.getType() == IResource.FILE)
			{
				IFile file = (IFile) resource;
				handleFile(file, delta);
			}
			return true;
		}

	}

	private void handleFile(IFile file, IResourceDelta delta)
	{
		IProject prj = file.getProject();
		if (prj.isAccessible() && prj.isOpen() && Utils.isICDProject(prj))
		{
			if (delta.getKind() == IResourceDelta.REMOVED)
			{
				// ICD文件被删除
				log.config("ICD文件[" + file.getFullPath() + "]被删除.");
				this.closeEditors(file);
			}
			else if ((delta.getFlags() & IResourceDelta.REPLACED) != 0)
			{
				// ICD文件被替换
				log.config("ICD文件[" + file.getFullPath() + "]被替换.");
				this.closeEditors(file);
			}
		}
	}
}