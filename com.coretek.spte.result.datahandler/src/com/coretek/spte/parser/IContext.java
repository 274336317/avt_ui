/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.parser;

import com.coretek.common.template.SPTEMsg;

/**
 * 测试用例执行上下文
 * 
 * @author 孙大巍 2012-3-23
 */
public interface IContext
{
	/**
	 * 初始化环境
	 * 
	 * @param icdPath icd文件的绝对路径
	 * @param msgDBPath 消息数据库
	 * @param endian
	 */
	public void init(String icdPath, String msgDBPath);

	/**
	 * 关闭解析环境
	 */
	public void shutDown();

	/**
	 * 查询数据库中保存的消息
	 * 
	 * @param startTime 起始时间
	 * @param length 时间长度
	 * @return
	 */
	public SPTEMsg[] querySPTEMsgs(long startTime, long length);

	/**
	 * 查询消息
	 * 
	 * @param time 起点时间
	 * @param length 长度
	 * @param range 主题号数组
	 * @return
	 */
	public SPTEMsg[] getMsgLimitByMsgId(long time, int length, String[] range);

	/**
	 * 清空缓存
	 */
	public void clearCache();

}
