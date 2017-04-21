/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.logging;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;

/**
 * ����Eclipseƽ̨���������쳣
 * 
 * @author ���Ρ
 * @date 2012-1-13
 */
public class LogListener implements ILogListener
{

	private static final Logger	logger	= LoggingPlugin.getLogger(LogListener.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.ILogListener#logging(org.eclipse.core.runtime
	 * .IStatus, java.lang.String) <br/> <b>����</b> ���Ρ </br> <b>����</b> 2012-1-13
	 */
	@Override
	public void logging(IStatus status, String plugin)
	{
		switch (status.getSeverity())
		{
			case IStatus.WARNING:
			{
				logger.warning(new StringBuilder(plugin).append("\n").append(status.getException()).toString());
				break;
			}
			case IStatus.ERROR:
			{
				if (status.getException() != null)
				{
					StringWriter stringWriter = new StringWriter();
					PrintWriter writer = new PrintWriter(stringWriter);
					status.getException().printStackTrace(writer);
					logger.severe(new StringBuilder(plugin).append("\n").append(stringWriter).toString());
					writer.flush();
					writer.close();
					try
					{
						stringWriter.close();
					} catch (IOException e)
					{
						e.printStackTrace();
					}
				}

				break;
			}
			case IStatus.INFO:
			{
				logger.info(new StringBuilder(plugin).append("\n").append(status.getException()).toString());
				break;
			}
		}

	}

}