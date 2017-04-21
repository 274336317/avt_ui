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
 * ���ڼ���ICD�ļ������仯ʱ��������仯��ICD�ļ��Ĳ�������������ڱ༭���б��򿪣���رձ༭����
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
		{// ɾ������
			IProject project = (IProject) event.getResource();
			log.info("���������� [" + project.getName() + "] ��ɾ����");
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
		{// �رչ���
			IProject project = (IProject) event.getResource();
			log.info("���������� [" + project.getName() + "] ���ر��ˡ�");
			this.closeEditors(project);
		}

	}

	/**
	 * �رղ��������༭��
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
	 * �رղ��������༭��
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
				// ICD�ļ���ɾ��
				log.config("ICD�ļ�[" + file.getFullPath() + "]��ɾ��.");
				this.closeEditors(file);
			}
			else if ((delta.getFlags() & IResourceDelta.REPLACED) != 0)
			{
				// ICD�ļ����滻
				log.config("ICD�ļ�[" + file.getFullPath() + "]���滻.");
				this.closeEditors(file);
			}
		}
	}
}