package com.coretek.spte.ui.model;

import com.coretek.common.template.SPTEMsg;

public interface IReviewCache extends IMessageCache
{

	/**
	 * 获取消息的种类
	 * 
	 * @return </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-3-23
	 */
	public SPTEMsg[] getAllKindsOfSPTEMsgs();
}
