package com.coretek.spte.ui.model;

/**
 * 监控的消息缓存
 * 
 * @author 王思敏 2012-4-5
 */
public interface IMonitorCache extends IMessageCache
{
	/**
	 * 查看解析操作是否完成
	 * 
	 * @return 如果解析操作执行完毕，则返回true，否则返回false</br> <b>作者</b> 孙大巍 </br> <b>日期</b>
	 *         2012-3-23
	 */
	public boolean isFinished();

}