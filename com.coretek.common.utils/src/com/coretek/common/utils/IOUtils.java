/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.utils;

import java.io.Closeable;
import java.io.IOException;
import java.util.logging.Logger;

import com.coretek.common.logging.LoggingPlugin;

/**
 * 流的一些实用方法
 * 
 * @author 孙大巍 2012-4-5
 */
public final class IOUtils
{

	private static final Logger	logger	= LoggingPlugin.getLogger(IOUtils.class);

	/**
	 * 关闭流
	 * 
	 * @param closeable 被关闭的流对象
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