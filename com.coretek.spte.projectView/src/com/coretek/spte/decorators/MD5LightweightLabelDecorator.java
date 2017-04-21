/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.decorators;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;

import com.coretek.spte.core.util.ICDFileManager;
import com.coretek.spte.core.util.Utils;
import com.coretek.spte.core.util.ValidateError;
import com.coretek.testcase.projectView.ProjectPlugin;

/**
 * @author SunDawei 2012-5-4
 */
public class MD5LightweightLabelDecorator implements ILightweightLabelDecorator
{

	public final static String	MD5_NOT_MATCH_MARKER	= "com.coretek.spte.projectView.md5notmatchedmarker";

	public final static String	ICD_NOT_EXIST_MARKER	= "com.coretek.spte.projectView.icdfilenotexistsmarker";

	public MD5LightweightLabelDecorator()
	{

	}

	private void clearMarkers(IFile file)
	{
		try
		{
			IMarker markers[] = file.findMarkers(MD5_NOT_MATCH_MARKER, true, IResource.DEPTH_INFINITE);
			if (markers != null)
			{
				for (IMarker marker : markers)
				{
					marker.delete();
				}
			}
			markers = file.findMarkers(ICD_NOT_EXIST_MARKER, true, IResource.DEPTH_INFINITE);
			if (markers != null)
			{
				for (IMarker marker : markers)
				{
					marker.delete();
				}
			}
		} catch (CoreException e)
		{
			e.printStackTrace();
		}

	}

	public void decorate(Object element, IDecoration decoration)
	{
		if (element instanceof IFile)
		{
			IFile file = (IFile) element;
			if ("cas".equals(file.getFileExtension()))
			{
				this.clearMarkers(file);
				ValidateError error = ICDFileManager.validateCaseFile(file);
				if (error == ValidateError.ICDFILENOTEXISTS)
				{
					this.decorateError(decoration);
					try
					{
						IMarker marker = file.createMarker(ICD_NOT_EXIST_MARKER);
						marker.setAttribute("value", "icdfilenotexistsmarker");
						marker.setAttribute(IMarker.MESSAGE, "使用的ICD文件不存在");
						marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
					} catch (CoreException e)
					{
						e.printStackTrace();
					}
				} else if (error == ValidateError.MD5NOTMATCH)
				{
					this.decorateWarning(decoration);
					try
					{
						IMarker marker = file.createMarker(MD5_NOT_MATCH_MARKER);
						marker.setAttribute("value", "md5notmatched");
						marker.setAttribute(IMarker.MESSAGE, "使用的ICD文件内容已经被更改");
						marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_NORMAL);
						marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
					} catch (CoreException e)
					{
						e.printStackTrace();
					}
				}
			}

		} else if (element instanceof IProject)
		{
			IProject prj = (IProject) element;
			this.decorateProject(prj, decoration);
		}

	}

	private void decorateError(IDecoration decoration)
	{
		decoration.addOverlay(ProjectPlugin.getImageDescriptor("icons/error_co.gif"), IDecoration.TOP_LEFT);
	}

	private void decorateWarning(IDecoration decoration)
	{
		decoration.addOverlay(ProjectPlugin.getImageDescriptor("icons/warning_co.gif"), IDecoration.TOP_LEFT);
	}

	private void decorateProject(IProject prj, IDecoration decoration)
	{
		if (prj.exists())
		{
			if (Utils.isSoftwareTestingProject(prj))
			{
				decoration.addSuffix("[软件测试工程]");
				decoration.setForegroundColor(ColorConstants.red);
			} else if (Utils.isICDProject(prj))
			{
				decoration.addSuffix("[ICD管理工程]");
			}
		}

	}

	public void addListener(ILabelProviderListener listener)
	{

	}

	public void dispose()
	{

	}

	public boolean isLabelProperty(Object element, String property)
	{

		return true;
	}

	public void removeListener(ILabelProviderListener listener)
	{

	}

}