/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.testcase.report;

import java.io.File;

/**
 * 
 * @author ZHANG Yi 2012-05-23
 */
public interface IReportExporter
{

	/**
	 * ����ģ���ļ�
	 * 
	 * @return
	 * @throws ExportReportException
	 */
	File exportReport() throws ExportReportException;

	/**
	 * ע������
	 * 
	 * @param data
	 * @throws ExportReportException
	 */
	void setData(Object data) throws ExportReportException;

	/**
	 * ���������ɵı����ļ�
	 * 
	 * @param file
	 */
	void setFile(File file);

	void setTemplatePath(String path);
}
