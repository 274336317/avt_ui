/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.manager;

import com.coretek.common.template.SPTEMsg;
import com.coretek.spte.parser.IReviewContext;
import com.coretek.spte.parser.ReviewContext;

/**
 * 历史记录上下文管理器
 * 
 * @author 孙大巍 2012-3-27
 */
public class LoadHistoryContextManager implements IContextManager
{
	private IReviewContext	reviewContext;

	public SPTEMsg[] getAllKindsOfMsgs()
	{
		return this.reviewContext.getAllKindsOfSPTEMsgs();
	}

	@Override
	public SPTEMsg[] getMsgLimitByMsgId(long time, int length, String[] range)
	{
		return this.reviewContext.getMsgLimitByMsgId(time, length, range);
	}

	@Override
	public void initParser(String icdPath, String msgDBPath)
	{
		this.reviewContext = new ReviewContext();
		this.reviewContext.init(icdPath, msgDBPath);
	}

	@Override
	public SPTEMsg[] querySPTEMsgs(long startTime, long length)
	{
		return this.reviewContext.querySPTEMsgs(startTime, length);
	}

	@Override
	public boolean shutDown()
	{
		this.reviewContext.shutDown();
		return false;
	}

	@Override
	public void clearCache()
	{
		this.reviewContext.clearCache();
	}

	/**
	 * 获取数据库中保存的ICD文件路径
	 * 
	 * @return
	 */
	public String getIcdPath()
	{
		return this.reviewContext.getIcdPath();
	}

}