/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.cfg;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

import com.coretek.spte.cfg.CfgPlugin;

/**
 * �����ĵ�����ҳ
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
		 * addField( new BooleanFieldEditor( OPEN, "�Ƿ������򿪱���",
		 * getFieldEditorParent()));
		 */
		FileFieldEditor reportTemplateField = new FileFieldEditor(TEMPLATE_REPORT, "���Ա���ģ���ļ�", getFieldEditorParent());
		reportTemplateField.setFileExtensions(new String[] { "*.ftl" });
		addField(reportTemplateField);
		FileFieldEditor descriptionTemplateField = new FileFieldEditor(TEMPLATE_DESCRIPTION, "����˵��ģ���ļ�", getFieldEditorParent());
		descriptionTemplateField.setFileExtensions(new String[] { "*.ftl" });
		addField(descriptionTemplateField);
		DirectoryFieldEditor reportFilePath = new DirectoryFieldEditor(SAVE, "�ļ�����·��", getFieldEditorParent());
		addField(reportFilePath);
	}

	public void init(IWorkbench workbench)
	{
	}

}