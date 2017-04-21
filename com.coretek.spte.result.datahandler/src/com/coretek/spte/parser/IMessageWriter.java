package com.coretek.spte.parser;

import com.coretek.common.template.SPTEMsg;

/**
 * 消息输出器
 * 
 * @author 孙大巍 2012-3-20
 */
public interface IMessageWriter
{

	/**
	 * 输出消息
	 * 
	 * @param msg
	 * @return 输入成功则返回true，否则返回false</br>
	 */
	boolean write(SPTEMsg msg);
}
