/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.testcase.projectView.views;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;
import static com.coretek.testcase.projectView.utils.ProjectUtils.*;

import com.coretek.tools.ide.internal.ui.UIPluginImages;

/**
 * @author SunDawei 2012-5-4
 */
public class SPTEDecoratingLabelProvider extends DecoratingLabelProvider
{

	private String type;

	/**
	 * @param provider
	 * @param decorator
	 */
	public SPTEDecoratingLabelProvider(ILabelProvider provider, ILabelDecorator decorator, String type)
	{
		super(provider, decorator);
		this.type = type;
	}

	// 重写getImage(), 改变 文件图标
	public Image getImage(Object element)
	{
		if (element instanceof IProject)
		{
			try
			{
				// 处理创建功能对应的图片
				IProject m_Pro = (IProject) element;
				IProjectDescription m_ProDes = m_Pro.getDescription();
				if (m_ProDes != null && m_ProDes.getNatureIds().length > 0)
				{
					for (String m_NatureId : m_ProDes.getNatureIds())
					{
						if ("com.coretek.spte.projectView.testProjectNature".equals(m_NatureId))
						{
							return UIPluginImages.get(UIPluginImages.IMG_OBJS_PROJECT);
						}
						if ("com.coretek.spte.projectView.icdProjectNature".equals(m_NatureId))
						{
							return UIPluginImages.get(UIPluginImages.IMG_OBJS_PROJECT_ICD);
						}
					}
				}

				if ("ICD".equals(type))
				{
					return UIPluginImages.get(UIPluginImages.IMG_OBJS_PROJECT_ICD);
				}
			}
			catch (CoreException e)
			{
				e.printStackTrace();
			}
		}
		if (element instanceof IFolder)
		{
			// 获取测试集的图片
			String folderType = com.coretek.testcase.projectView.utils.ProjectUtils.getFolderProperty((IFolder) element, FODLDER_TYPE);
			if (FODLDER_TYPE_TEST_SUITE.equalsIgnoreCase(folderType))
			{
				// 根据测试集目录属性文件中记录的属性，从而分别不同文件夹
				return UIPluginImages.get(UIPluginImages.IMG_OBJS_TEST_SUITE);
			}
			else
			{
				return UIPluginImages.get(UIPluginImages.IMG_OBJS_FILE_ICD);
			}
		}
		else if (element instanceof IFile)
		{
			String fileExtension = ((IFile) element).getFileExtension();
			// 获取Case测试用例的图片
			if (fileExtension != null && fileExtension.equals(XML_CAS_FILE_EXTENSION))
			{
				ILabelDecorator decorator = this.getLabelDecorator();
				Image image = UIPluginImages.get(UIPluginImages.IMG_OBJS_TEST_CASE);
				image = decorator.decorateImage(image, element);
				if (image != null)
					return image;
			}

		}

		return this.getLabelProvider().getImage(element);
	}

}