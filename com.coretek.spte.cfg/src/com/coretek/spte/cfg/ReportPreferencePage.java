/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.cfg;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

import com.coretek.spte.cfg.CfgPlugin;

/**
 * 测试文档配置页
 * 
 * @author ZHANG Yi 2012-05-23
 */
public class ReportPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{

	public static final String	EXPORT					= "report_export";
	public static final String	OPEN					= "report_open";
	public static final String	TEMPLATE_REPORT			= "report_template";
	public static final String	TEMPLATE_DESCRIPTION	= "description_template";
	public static final String	SAVE					= "save_path";

	public ReportPreferencePage()
	{
		super(GRID);
		setPreferenceStore(CfgPlugin.getDefault().getPreferenceStore());
	}

	public void createFieldEditors()
	{

		/*
		 * addField( new BooleanFieldEditor( OPEN, "是否立即打开报告",
		 * getFieldEditorParent()));
		 */
		FileFieldEditor reportTemplateField = new FileFieldEditor(TEMPLATE_REPORT, "测试报告模板文件", getFieldEditorParent());
		reportTemplateField.setFileExtensions(new String[] { "*.ftl" });
		addField(reportTemplateField);
		FileFieldEditor descriptionTemplateField = new FileFieldEditor(TEMPLATE_DESCRIPTION, "测试说明模板文件", getFieldEditorParent());
		descriptionTemplateField.setFileExtensions(new String[] { "*.ftl" });
		addField(descriptionTemplateField);
		DirectoryFieldEditor reportFilePath = new DirectoryFieldEditor(SAVE, "文件保存路径", getFieldEditorParent());
		addField(reportFilePath);
	}

	public void init(IWorkbench workbench)
	{
	}

}