package com.coretek.spte.parser;

import com.coretek.common.template.SPTEMsg;

/**
 * 历史记录加载上下文，用于从数据库中加载消息到内存并进行适当的缓冲
 * 
 * @author SunDawei
 * 
 * @date 2012-10-16
 */
public interface IReviewContext extends IContext
{

	/**
	 * 获取消息的种类
	 * 
	 * @return
	 */
	public SPTEMsg[] getAllKindsOfSPTEMsgs();

	/**
	 * 获取保存在数据库中的ICD文件路径
	 * 
	 * @return
	 */
	public String getIcdPath();

}
