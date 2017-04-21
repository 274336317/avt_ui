/************************************************************************
 *				北京科银京成技术有限公司 版权所有
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
	 * 生成模板文件
	 * 
	 * @return
	 * @throws ExportReportException
	 */
	File exportReport() throws ExportReportException;

	/**
	 * 注入数据
	 * 
	 * @param data
	 * @throws ExportReportException
	 */
	void setData(Object data) throws ExportReportException;

	/**
	 * 设置所生成的报告文件
	 * 
	 * @param file
	 */
	void setFile(File file);

	void setTemplatePath(String path);
}
