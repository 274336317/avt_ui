package com.coretek.spte.monitor.manager;

import java.util.concurrent.TimeoutException;

import com.coretek.common.template.SPTEMsg;

/**
 * 上下文管理器接口
 * 
 * @author SunDawei
 * 
 * @date 2012-10-8
 */
public interface IContextManager
{

	public boolean shutDown() throws TimeoutException;

	/**
	 * 查询时间范围内的消息
	 * 
	 * @param time 查询起点
	 * @param length 时间长度
	 * @param range 消息编号数组
	 * @return </br>
	 */
	public SPTEMsg[] getMsgLimitByMsgId(long time, int length, String[] range);

	/**
	 * 获取一段时间内的消息
	 * 
	 * @param startTime 开始时间
	 * @param length 时间长度
	 * @return </br>
	 */
	public SPTEMsg[] querySPTEMsgs(long startTime, long length);

	/**
	 * 初始化
	 * 
	 * @param icdPath
	 * @param msgDBPath</br>
	 */
	public void initParser(String icdPath, String msgDBPath);

	/**
	 * 清空缓存 </br>
	 */
	public void clearCache();

}
