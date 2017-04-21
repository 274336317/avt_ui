package com.coretek.spte.ui.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.coretek.common.template.SPTEMsg;

/**
 * SPTEMsg对象缓存，用于保存从消息数据库中转换来的SPTEMsg集合。 注意，此类不是线程安全的！当你要进行多线程操作时，请自己确保线程安全。
 * 
 * @author Sim.Wang 2012-3-26
 */
public class SPTECache
{
	private List<SPTEMsg>	spteMsgs	= Collections.synchronizedList(new LinkedList<SPTEMsg>());

	// 开始时间，也是最小时间
	private volatile long	beginTime	= -1;

	// 结束时间，也是最大时间
	private volatile long	endTime		= -1;

	public SPTECache()
	{

	}

	public void addSPTEMsg(SPTEMsg spteMsg)
	{
		spteMsgs.add(spteMsg);
	}

	public List<SPTEMsg> getSPTEMsgs()
	{
		return spteMsgs;
	}

	public void setSPTEMsgs(List<SPTEMsg> spteMsgs)
	{
		this.spteMsgs = spteMsgs;
	}

	public long getBeginTime()
	{
		return beginTime;
	}

	public void setBeginTime(long fromTime)
	{
		this.beginTime = fromTime;
	}

	public long getEndTime()
	{
		return endTime;
	}

	public void setEndTime(long toTime)
	{
		this.endTime = toTime;
	}

	public void clear()
	{
		this.beginTime = -1;
		this.endTime = -1;
		this.spteMsgs.clear();
		this.spteMsgs = Collections.synchronizedList(new LinkedList<SPTEMsg>());
		System.gc();
	}
}
