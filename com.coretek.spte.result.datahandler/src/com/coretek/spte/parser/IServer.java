/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.parser;

/**
 * 负责与执行器通信的服务器
 * 
 * @author 孙大巍 2012-3-20
 */
public interface IServer extends IStreamReader
{

	/**
	 * 开始运行服务器
	 * 
	 * @param port 端口号
	 * @param cacheSize 缓存大小
	 * @return 开启失败返回false，成功则返回true
	 */
	public boolean start(int port, int cacheSize);

	/**
	 * 关闭服务器
	 */
	public void shutDown();

}