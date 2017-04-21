package com.coretek.spte.ui.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.coretek.common.template.SPTEMsg;
import com.coretek.spte.parser.IMessageWriter;

/**
 * 开监控时的数据查询与管理。当缓存中的数据无法满足监控的读取要求时， 返回一个长度为0的数组，而不是到数据库中进行查询。
 * 
 * @author Sim.Wang 2012-3-26
 */
public class MonitorCache extends MessageDAO implements IMonitorCache, IMessageWriter
{
	private ReentrantReadWriteLock	lock	= new ReentrantReadWriteLock(true);

	private SPTECache				page	= new SPTECache();					// 缓存数据放在此对象中

	private volatile boolean		needCache;									// 标识是否需要将数据放入缓存当中

	/**
	 * 构建一个MonitorCache对象
	 * 
	 * @param dataPath 数据库路径
	 * @param icdPath icd文件路径
	 * @param needCache 标识是否需要将消息放入缓存中，如何为true则标识需要
	 */
	public MonitorCache(String dataPath, String icdPath, boolean needCache)
	{
		super(dataPath, icdPath);

		this.needCache = needCache;
	}

	/**
	 * 清空缓存
	 */
	public void clearCache()
	{
		this.page.clear();
	}

	@Override
	public boolean write(SPTEMsg spteMsg)
	{
		try
		{
			lock.writeLock().lock();
			if (this.needCache)
			{
				long timestamp = spteMsg.getTimeStamp();
				// 放入缓存
				page.addSPTEMsg(spteMsg);
				if (page.getBeginTime() < 0)
					page.setBeginTime(timestamp);
				page.setEndTime(timestamp);
			}

		} finally
		{
			lock.writeLock().unlock();
		}

		this.addToInsertQueue(spteMsg);

		return true;
	}

	/**
	 * 获取满足 时间戳 在指定时间范围内的数据
	 * 
	 * @param time 起始时间
	 * @param length 时间跨度
	 * @return
	 */
	public SPTEMsg[] getMessage(long time, long length)
	{
		try
		{
			lock.readLock().lock();
			// 从缓存中读取
			return getMessage(time, length, null, null);
		} finally
		{
			lock.readLock().unlock();
		}
	}

	/**
	 * 获取满足 时间戳 在指定时间范围内 并且 topicId 也在指定范围内的数据
	 * 
	 * @param time 起始时间
	 * @param length 时间跨度
	 * @param range topicId字段的取值范围
	 * @return
	 */
	public SPTEMsg[] getMsgLimitByTpcId(long time, int length, String[] range)
	{
		try
		{
			lock.readLock().lock();
			// 从缓存中读取
			return getMessage(time, length, TOPICID, range);
		} finally
		{
			lock.readLock().unlock();
		}

	}

	/**
	 * 获取满足 时间戳 在指定时间范围内 并且 msgId 也在指定范围内的数据
	 * 
	 * @param time 起始时间
	 * @param length 时间跨度
	 * @param range msgId字段的取值范围
	 * @return
	 */
	public SPTEMsg[] getMsgLimitByMsgId(long time, int length, String[] range)
	{
		try
		{
			lock.readLock().lock();
			// 从缓存中读取
			return getMessage(time, length, MSGID, range);
		} finally
		{
			lock.readLock().unlock();
		}

	}

	/**
	 * 到缓存中读取数据 如果缓存中的数据不能满足需要，且欲查询的数据已经写入数据库 则到数据库中直接读取数据
	 * 
	 * @param time
	 * @param length
	 * @param field
	 * @param range
	 * @return
	 */
	private SPTEMsg[] getMessage(long time, long length, String field, String[] range)
	{
		// 缓存中包括所有的数据
		if (page.getBeginTime() <= time && page.getEndTime() >= (time + length))
		{
			/* 取值区间在缓存中 */
			return getMsgInBufMem(time, length, field, range);
		}
		return new SPTEMsg[0];
	}

	/**
	 * 在缓存中读取满足要求的数据
	 * 
	 * @param time
	 * @param length
	 * @param field
	 * @param range
	 * @return
	 */
	private SPTEMsg[] getMsgInBufMem(long time, long length, String field, String[] range)
	{
		if (page.getBeginTime() > (time + length) || page.getEndTime() < time)
		{
			return new SPTEMsg[0];
		}
		List<SPTEMsg> spteMsgs = new ArrayList<SPTEMsg>();

		for (SPTEMsg sptemsg : page.getSPTEMsgs())
		{
			// 时间范围判断
			if (sptemsg.getTimeStamp() >= time && sptemsg.getTimeStamp() <= (time + length))
			{
				// 指定字段判断
				if (field != null && range != null)
				{
					for (String rg : range)
					{
						if ((field.equals(TOPICID) && rg.equals(sptemsg.getMsg().getTopicId())) || (field.equals(MSGID) && rg.equals(sptemsg.getMsg().getId())))
						{
							spteMsgs.add(sptemsg);
							break;
						}
					}
				} else
				{
					spteMsgs.add(sptemsg);
				}

			} else if (sptemsg.getTimeStamp() > (time + length))
			{
				// 已取完需要的数据，跳出循环
				break;
			}
		}
		return spteMsgs.toArray(new SPTEMsg[spteMsgs.size()]);
	}
}