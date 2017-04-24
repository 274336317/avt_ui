/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.utils;

import java.io.Closeable;
import java.io.IOException;
import java.util.logging.Logger;

import com.coretek.common.logging.LoggingPlugin;

/**
 * ����һЩʵ�÷���
 * 
 * @author ���Ρ 2012-4-5
 */
public final class IOUtils
{

	private static final Logger	logger	= LoggingPlugin.getLogger(IOUtils.class);

	/**
	 * �ر���
	 * 
	 * @param closeable ���رյ�������
	 * @return
	 */
	public static boolean close(Closeable closeable)
	{
		if (closeable != null)
		{
			try
			{
				closeable.close();
			} catch (IOException e)
			{
				LoggingPlugin.logException(logger, e);
				return false;
			}
		}

		return true;
	}

}