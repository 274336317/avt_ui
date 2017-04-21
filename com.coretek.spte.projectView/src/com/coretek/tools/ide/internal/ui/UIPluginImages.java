/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     QNX Software System
 *******************************************************************************/
package com.coretek.tools.ide.internal.ui;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;

import com.coretek.testcase.projectView.ProjectPlugin;

/**
 * Bundle of all images.
 */
public class UIPluginImages
{

	// The plugin registry
	private static ImageRegistry		imageRegistry				= new ImageRegistry(ProjectPlugin.getStandardDisplay());

	// Subdirectory (under the package containing this class) where 16 color
	// images are
	private static URL					fgIconBaseURL;

	static
	{
		try
		{
			fgIconBaseURL = new URL(ProjectPlugin.getDefault().getBundle().getEntry("/"), "icons/");
		} catch (MalformedURLException e)
		{

		}
	}

	private static final String			NAME_PREFIX					= ProjectPlugin.PLUGIN_ID + '.';

	private static final int			NAME_PREFIX_LENGTH			= NAME_PREFIX.length();

	public static final String			T_OBJ						= "obj16/";

	public static final String			T_WIZBAN					= "wizban/";

	public static final String			T_LCL						= "lcl16/";

	public static final String			T_TOOL						= "tool16/";

	public static final String			T_VIEW						= "view16/";

	public static final String			T_OVR						= "ovr16/";

	// 测试集正常状态时图标
	/**
	 * 测试集图片
	 */
	public static final String			IMG_OBJS_TEST_SUITE			= NAME_PREFIX + "205_testSets.gif";
	public static final ImageDescriptor	DESC_TEST_SUITE_NORMAL		= createManaged(T_OBJ, IMG_OBJS_TEST_SUITE);
	/**
	 * 测试工程图片
	 */
	public static final String			IMG_OBJS_PROJECT			= NAME_PREFIX + "203_testProject.gif";
	public static final ImageDescriptor	DESC_IMG_OBJS_PROJECT		= createManaged(T_OBJ, IMG_OBJS_PROJECT);
	/**
	 * ICD工程图片
	 */
	public static final String			IMG_OBJS_PROJECT_ICD			= NAME_PREFIX + "201_icdProject.gif";
	public static final ImageDescriptor	DESC_IMG_OBJS_PROJECT_ICD		= createManaged(T_OBJ, IMG_OBJS_PROJECT_ICD);
	/**
	 * ICD文件图片
	 */
	public static final String			IMG_OBJS_FILE_ICD			= NAME_PREFIX + "202_icdFile.gif";
	public static final ImageDescriptor	DESC_IMG_OBJS_FILE_ICD		= createManaged(T_OBJ, IMG_OBJS_FILE_ICD);

	// 测试用例文件图标
//	public static final String			IMG_OBJS_TEST_CASE			= NAME_PREFIX + "test_case_normal.ico";
	/**
	 * 测试用例图片
	 */
	public static final String			IMG_OBJS_TEST_CASE			= NAME_PREFIX + "204_testCase.gif";
	public static final ImageDescriptor	DESC_TEST_CASE_NORMAL		= createManaged(T_OBJ, IMG_OBJS_TEST_CASE);

	public static final String			IMG_MENU_COLLAPSE_ALL		= NAME_PREFIX + "collapseall.gif";

	public static final String			IMG_MENU_TOGGLE_LINK		= NAME_PREFIX + "synced.gif";

	public static final String			IMG_MD5_NOT_MATCHED			= NAME_PREFIX + "warning_md5.gif";
	public static final ImageDescriptor	DESC_IMG_MD5_NOT_MATCHED	= createManaged(T_OBJ, IMG_MD5_NOT_MATCHED);

	public static final String			IMG_MD5_NO_ICD_FILE			= NAME_PREFIX + "warning_error.gif";
	public static final ImageDescriptor	DESC_IMG_MD5_NO_ICD_FILE	= createManaged(T_OBJ, IMG_MD5_NO_ICD_FILE);

	private static ImageDescriptor createManaged(String prefix, String name)
	{
		return createManaged(imageRegistry, prefix, name);
	}

	private static ImageDescriptor createManaged(ImageRegistry registry, String prefix, String name)
	{
		ImageDescriptor result = ImageDescriptor.createFromURL(makeIconFileURL(prefix, name.substring(NAME_PREFIX_LENGTH)));
		registry.put(name, result);
		return result;
	}

	public static Image get(String key)
	{
		return imageRegistry.get(key);
	}

	private static ImageDescriptor create(String prefix, String name)
	{
		return ImageDescriptor.createFromURL(makeIconFileURL(prefix, name));
	}

	private static URL makeIconFileURL(String prefix, String name)
	{
		StringBuffer buffer = new StringBuffer(prefix);
		buffer.append(name);
		try
		{
			return new URL(fgIconBaseURL, buffer.toString());
		} catch (MalformedURLException e)
		{
			return null;
		}
	}

	/**
	 * Sets all available image descriptors for the given action.
	 */
	public static void setImageDescriptors(IAction action, String type, String relPath)
	{
		if (relPath.startsWith(NAME_PREFIX))
			relPath = relPath.substring(NAME_PREFIX_LENGTH);
		action.setDisabledImageDescriptor(create("d" + type, relPath));
		action.setImageDescriptor(create("e" + type, relPath));
	}

	public static void dispose()
	{
		imageRegistry.dispose();
	}
}