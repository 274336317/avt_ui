/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.parser;

/**
 * 消息解析器。从输入数据中解析出消息。
 * 
 * @author 孙大巍 2012-3-20
 */
public interface IMessageParser
{

	/**
	 * 设置输入流读取器
	 * 
	 * @param istream
	 */
	public void setInDataStream(IStreamReader istream);

	/**
	 * 设置输出流读取器
	 * 
	 * @param ostream
	 */
	public void setOutDataStream(IMessageWriter ostream);

	/**
	 * 查看解析工作是否已经完成
	 * 
	 * @return 如果解析完成，则返回true，否则返回false
	 */
	public boolean isFinished();

	/**
	 * 开启解析操作。
	 * 
	 * @return
	 */
	public boolean start();
}