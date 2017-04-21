package com.coretek.spte.ui.model;

import com.coretek.common.template.SPTEMsg;

/**
 * 消息缓存，用于缓存解析之后的消息或者从数据库中加载的消息
 * 
 * @author SunDawei
 * 
 * @date 2012-10-8
 */
public interface IMessageCache
{
	/**
	 * 查询数据库中保存的消息
	 * 
	 * @param startTime 起始时间
	 * @param length 时间长度
	 * @return
	 */
	public SPTEMsg[] getMessage(long startTime, long length);

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
