/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.testcase.report;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.logging.LoggingPlugin;
import com.coretek.common.utils.StringUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 
 * @author ZHANG Yi 2012-05-23
 */
public class FreeMarkerReportExporter implements IReportExporter
{

	private final static Logger	logger				= LoggingPlugin.getLogger(FreeMarkerReportExporter.class);

	private String				templatePath;																	//$NON-NLS-1$

	private static final String	DEFAULT_CHARSET		= "UTF-8";													//$NON-NLS-1$

	private static final String	DEFAULT_FILETYPE	= ".doc";													//$NON-NLS-1$

	private String				fileType			= DEFAULT_FILETYPE;

	private String				filePath;

	private File				file;

	private Map<String, Object>	data				= new HashMap<String, Object>();

	private String				type;

	public FreeMarkerReportExporter()
	{
		super();

	}

	public FreeMarkerReportExporter(String templatePath, Map<String, Object> rootMap, String type)
	{
		this(rootMap, type);
		this.templatePath = templatePath;

	}

	public FreeMarkerReportExporter(Map<String, Object> rootMap, String type)
	{
		super();
		this.type = type;
		this.data = rootMap;
	}

	public Map<String, Object> getData()
	{
		return data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coretek.testcase.report.IReportExporter#setData(java.lang.Object)
	 * <br/> <b>Author</b> SunDawei </br> <b>Date</b> 2012-5-23
	 */
	public void setData(Object data) throws ExportReportException
	{
		if (!(data instanceof Map))
			throw new ExportReportException(Messages.getString("FreeMarkerReportExporter_DATA_MUST_MAP")); //$NON-NLS-1$
		else
		{
			this.data = (Map<String, Object>) data;
		}
	}

	public String getFileType()
	{
		return fileType;
	}

	/**
	 * 设置生成的报告文件类型，即其后缀名
	 * 
	 * @param fileType </br> <b>Author</b> ZHANG Yi</br> <b>Date</b> 2012-5-23
	 */
	public void setFileType(String fileType)
	{
		if (!fileType.startsWith(".")) { //$NON-NLS-1$
			fileType = "." + fileType; //$NON-NLS-1$
		}
		this.fileType = fileType;
	}

	public String getFilePath()
	{
		return filePath;
	}

	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}

	/**
	 * 生成报告文件
	 * 
	 * @author ZHANG YI
	 * @date 2012-05-23
	 */
	public File exportReport() throws ExportReportException
	{
		if (data == null || data.isEmpty())
		{
			throw new ExportReportException(Messages.getString("FreeMarkerReportExporter_DATA_NOT_NULL")); //$NON-NLS-1$
		}
		Configuration cfg = new Configuration();
		cfg.setDefaultEncoding(DEFAULT_CHARSET);
		cfg.setOutputEncoding(DEFAULT_CHARSET);
		Template t = null;
		try
		{
			if (templatePath != null && templatePath.trim().length() != 0)
			{
				File tempFile = new File(templatePath);
				if (tempFile.isFile())
				{
					cfg.setDirectoryForTemplateLoading(tempFile.getParentFile());
					String template = tempFile.getName();
					t = cfg.getTemplate(template);
					logger.info(Messages.getString("FreeMarkerReportExporter_TEMPLATE_FILE") + tempFile.getAbsolutePath()); //$NON-NLS-1$
				} else
				{
					logger.severe(Messages.getString("FreeMarkerReportExporter_NO_TEMPLATE_FILE")); //$NON-NLS-1$
					throw new ExportReportException(Messages.getString("FreeMarkerReportExporter_NO_TEMPLATE_FILE")); //$NON-NLS-1$
				}
			} else
			{
				logger.warning(StringUtils.concat(Messages.getString("FreeMarkerReportExporter_DEFAULT_FILE"), ":", type)); //$NON-NLS-1$
				cfg.setClassForTemplateLoading(this.getClass(), ""); //$NON-NLS-1$
				t = cfg.getTemplate(type + ".ftl"); //$NON-NLS-1$
			}
			if (t != null)
			{
				Writer out = new OutputStreamWriter(new FileOutputStream(file), DEFAULT_CHARSET);
				t.process(data, out);
				if (out != null)
				{
					out.close();
				}
			}
		} catch (UnsupportedEncodingException e)
		{
			logger.severe(e.getMessage());
			throw new ExportReportException(e);
		} catch (FileNotFoundException e)
		{
			logger.severe(e.getMessage());
			throw new ExportReportException(e);
		} catch (TemplateException e)
		{
			logger.severe(e.getMessage());
			throw new ExportReportException(e);
		} catch (IOException e)
		{
			logger.severe(e.getMessage());
			throw new ExportReportException(e);
		}
		logger.info(Messages.getString("FreeMarkerReportExporter_REPORT_SUCCESS") + file.getAbsolutePath()); //$NON-NLS-1$
		return file;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coretek.testcase.report.IReportExporter#setFile(java.io.File)
	 * <br/> <b>Author</b> ZHANG Yi </br> <b>Date</b> 2012-5-30
	 */
	public void setFile(File file)
	{
		this.file = file;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coretek.testcase.report.IReportExporter#setTemplatePath(java.lang
	 * .String) <br/> <b>Author</b> ZHANG Yi </br> <b>Date</b> 2012-5-30
	 */
	public void setTemplatePath(String path)
	{
		// TODO Auto-generated method stub

	}
}
