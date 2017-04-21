package com.coretek.tools.ide.ui.action.testcase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeViewer;

import com.coretek.testcase.projectView.action.Messages;

/**
 * ִ�в�������,��.cas�е���Ϣ���ݴ��ݸ�ExecutionManager.exe
 * 
 * @author Administrator
 * @date 2010.10.21
 */
public class ExecuteTestSuiteAction extends Action
{

	private IFolder		folder;

	private TreeViewer	viewer;

	public ExecuteTestSuiteAction()
	{
		super();
	}

	public ExecuteTestSuiteAction(IFolder folder, TreeViewer viewer)
	{
		super();
		setText(Messages.getString("ExecuteTestSuiteAction.title"));
		this.folder = folder;
		this.viewer = viewer;
		this.setEnabled(isCasFileExist());
	}

	/**
	 * �ж��Ƿ����cas�ļ�
	 * 
	 * @return boolean
	 */
	private boolean isCasFileExist()
	{
		try
		{
			for (IResource file : folder.members())
			{
				if (file instanceof IFile)
				{
					String extension = ((IFile) file).getFileExtension();
					if (extension != null && extension.equals("cas"))
					{
						return true;
					}
				}
			}
		} catch (CoreException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return false;
	}

	public void run()
	{
		ExecuteTestSuiteJob job = new ExecuteTestSuiteJob(folder);
		job.setUser(true);
		job.schedule();
	}

}
