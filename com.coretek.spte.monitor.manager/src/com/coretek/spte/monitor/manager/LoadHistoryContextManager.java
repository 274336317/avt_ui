/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.manager;

import com.coretek.common.template.SPTEMsg;
import com.coretek.spte.parser.IReviewContext;
import com.coretek.spte.parser.ReviewContext;

/**
 * ��ʷ��¼�����Ĺ�����
 * 
 * @author ���Ρ 2012-3-27
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
	 * ��ȡ���ݿ��б����ICD�ļ�·��
	 * 
	 * @return
	 */
	public String getIcdPath()
	{
		return this.reviewContext.getIcdPath();
	}

}