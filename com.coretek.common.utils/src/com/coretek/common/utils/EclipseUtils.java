/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.utils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.UUID;
import java.util.logging.Logger;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;

import com.coretek.common.logging.LoggingPlugin;

/**
 * In this class, we added some useful method for eclipse.
 * 
 * >>>Welcome to testing using and contributing your code!<<<
 * 
 * @author SunDawei
 * @date 2012-1-16
 */
public class EclipseUtils
{

	private static final Logger	logger	= LoggingPlugin.getLogger(EclipseUtils.class.getName());

	/**
	 * 关闭editor
	 * 
	 * @param file
	 */
	public static void closeEditor(IFile file)
	{
		IEditorReference[] editors = EclipseUtils.getAllEditors();
		for (IEditorReference editor : editors)
		{
			try
			{
				FileEditorInput input = (FileEditorInput) editor.getEditorInput();
				if (input.getFile().equals(file))
				{
					EclipseUtils.getActivePage().closeEditors(new IEditorReference[] { editor }, false);
				}
			} catch (PartInitException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取工作空间下的所有工程
	 * 
	 * @return
	 */
	public static IProject[] getAllProjects()
	{
		IWorkspace space = ResourcesPlugin.getWorkspace();
		return space.getRoot().getProjects();
	}

	/**
	 * 获取时序编辑器中的文件
	 * 
	 * @return 文件
	 */
	public static IFile getInputOfEditor(IEditorPart editorPart)
	{
		if (editorPart == null)
		{
			return null;
		}
		IEditorInput input = editorPart.getEditorInput();
		if (input instanceof IFileEditorInput)
		{
			IFile file = ((IFileEditorInput) input).getFile();
			return file;
		}
		return null;
	}

	/**
	 * 根据当前被激活的editor来获取工程
	 * 
	 * @return 工程
	 */
	public static IProject getCurrentProject()
	{
		IFile file = getInputOfActiveEditor();
		if (file != null)
		{
			return ((IFile) file).getProject();
		}
		return null;
	}

	/**
	 * 获取时序编辑器中的文件
	 * 
	 * @return 文件
	 */
	public static IFile getInputOfActiveEditor()
	{
		IEditorPart editorPart = EclipseUtils.getActiveEditor();
		if (editorPart == null)
		{
			return null;
		}
		IEditorInput input = editorPart.getEditorInput();
		if (input instanceof IEditorInput)
		{
			IFile file = ((IFileEditorInput) input).getFile();
			return file;
		}
		return null;
	}

	/**
	 * 通过工程名获取工程
	 * 
	 * @param projectName
	 * @return
	 */
	public static IProject getProject(String projectName)
	{

		return ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
	}

	/**
	 * 获得当前打开的editor(s)
	 * 
	 * @return
	 */
	public static IEditorReference[] getAllEditors()
	{
		IWorkbenchPage page = EclipseUtils.getActivePage();
		if (page == null)
		{
			return new IEditorReference[0];
		}
		return page.getEditorReferences();
	}

	/**
	 * Getting the plugin's absolute path
	 * 
	 * @param plugin the give plugin
	 * @return </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-1-16
	 */
	public static File getPluginLoaction(AbstractUIPlugin plugin)
	{
		Bundle bundle = plugin.getBundle();
		URL url = FileLocator.find(bundle, new Path(""), null);
		try
		{
			url = FileLocator.resolve(url);
		} catch (IOException e1)
		{
			e1.printStackTrace();
			return null;
		}
		if (url == null)
		{
			logger.warning("Can not get the URL of plugin" + plugin.getBundle().getSymbolicName());
			return null;
		}
		File file = null;
		try
		{
			URI uri = url.toURI();
			if (uri == null)
			{
				return null;
			}
			file = new File(uri);
			if (!file.exists())
			{
				logger.warning("uri=" + uri + " does not exist!");
			}
		} catch (URISyntaxException e)
		{
			e.printStackTrace();
		}
		return file;
	}

	/**
	 * Getting the current workspace's path
	 * 
	 * @return the workspace's path</br> <b>Author</b> SunDawei </br>
	 *         <b>Date</b> 2012-1-16
	 */
	public static IPath getWorkspacePath()
	{

		return ResourcesPlugin.getWorkspace().getRoot().getLocation();
	}

	/**
	 * Get a uuid
	 * 
	 * @return </br>
	 */
	public static String getUUID()
	{
		return UUID.randomUUID().toString();
	}

	/**
	 * 打开某个视图
	 * 
	 * @param viewId 视图的id号
	 */
	public static IViewPart openView(String viewId)
	{
		IWorkbenchPage page = getActivePage();
		if(page  == null)
		{
			return null;
		}
		IViewPart part = page.findView(viewId);
		if (part == null)
		{
			try
			{
				page.showView(viewId);
			} catch (PartInitException e)
			{
				e.printStackTrace();
			}
		} else
		{
			page.bringToTop(part);
		}

		return part;
	}

	public static IWorkbenchWindow getActiveWindow()
	{
		IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench != null)
		{
			return workbench.getActiveWorkbenchWindow();
		}
		return null;
	}

	public static IWorkbenchPage getActivePage()
	{
		IWorkbenchWindow window = getActiveWindow();
		if (window != null)
		{
			return window.getActivePage();
		}
		return null;
	}

	/**
	 * 获得当前活动的editor
	 * 
	 * @return
	 */
	public static IEditorPart getActiveEditor()
	{
		IWorkbenchPage page = getActivePage();
		if (page != null)
		{
			return page.getActiveEditor();
		}
		return null;
	}
}
