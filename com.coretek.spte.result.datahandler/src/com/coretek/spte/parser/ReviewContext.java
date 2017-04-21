package com.coretek.spte.parser;

import com.coretek.common.template.SPTEMsg;
import com.coretek.spte.ui.model.BaseDAO;
import com.coretek.spte.ui.model.IReviewCache;
import com.coretek.spte.ui.model.ReviewCache;

/**
 * 加载历史记录上下文
 * 
 * @author Sim.Wang 2012-3-27
 */
public class ReviewContext implements IReviewContext
{
	private IReviewCache	messageCache;

	@Override
	public SPTEMsg[] querySPTEMsgs(long startTime, long length)
	{
		return messageCache.getMessage(startTime, length);
	}

	@Override
	public SPTEMsg[] getMsgLimitByMsgId(long time, int length, String[] range)
	{
		return messageCache.getMsgLimitByMsgId(time, length, range);
	}

	@Override
	public SPTEMsg[] getAllKindsOfSPTEMsgs()
	{
		return messageCache.getAllKindsOfSPTEMsgs();
	}

	@Override
	public void init(String icdPath, String msgDBPath)
	{
		messageCache = new ReviewCache(msgDBPath, icdPath);
	}

	@Override
	public void shutDown()
	{
		messageCache.clearCache();
		((ReviewCache) messageCache).closeDBConnection();
	}

	@Override
	public void clearCache()
	{
		if (this.messageCache != null)
			this.messageCache.clearCache();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coretek.spte.parser.IReviewContext#getIcdPath()
	 */
	@Override
	public String getIcdPath()
	{
		return ((BaseDAO) this.messageCache).getIcdPath();
	}

}