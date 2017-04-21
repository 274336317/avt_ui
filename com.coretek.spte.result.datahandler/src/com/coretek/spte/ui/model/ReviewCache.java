package com.coretek.spte.ui.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.coretek.common.template.SPTEMsg;

/**
 * 消息回放时的数据查询与管理
 * 
 * @author Sim.Wang 2012-3-26
 */
public class ReviewCache extends BaseDAO implements IReviewCache
{
	private SPTECache	page	= new SPTECache();	// 缓存数据放在此对象中

	public ReviewCache(String dataPath, String icdPath)
	{
		super(dataPath, icdPath);
	}

	/**
	 * 清空缓存
	 */
	public void clearCache()
	{
		this.page.clear();
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
		// 从缓存中读取
		return getMsgFromBufMem(time, length, null, null);
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
		// 从缓存中读取
		return getMsgFromBufMem(time, length, MSGID, range);
	}

	/**
	 * 到缓存中读取数据 如果缓存中的数据不能满足需要，则通过数据库更新缓存 再到缓存中读取数据
	 * 
	 * @param time
	 * @param length
	 * @param field
	 * @param range
	 * @return
	 */
	private synchronized SPTEMsg[] getMsgFromBufMem(long time, long length, String field, String[] range)
	{
		// 缓存中包括所有的数据
		if (page.getBeginTime() <= time && page.getEndTime() >= (time + length))
		{
			/* 取值区间在缓存中 */
			return getMsgsFromCache(time, length, field, range);
		}

		List<SPTEMsg> spteMsgs;
		if ((page.getBeginTime() <= time && time <= page.getEndTime()) && (page.getEndTime() < (time + length)))
		{// 需要查询的数据的最大时间戳大于缓存中的最大时间戳，但是最小时间戳落在缓存中，此时需要从数据库中查询出满足最大时间戳的消息
			// 得到之前的缓存数据
			spteMsgs = page.getSPTEMsgs();
			// 从数据库中添加超出部分数据
			spteMsgs.addAll(read((page.getEndTime() + 1), (time + length - page.getEndTime() - 1)));

			// 删除多余的数据
			Iterator<SPTEMsg> it = spteMsgs.iterator();
			while (it.hasNext())
			{
				SPTEMsg msg = it.next();
				if (msg.getTimeStamp() < time)
				{
					it.remove();
				} else
				{
					break;
				}
			}

		} else if ((time < page.getBeginTime()) && (page.getBeginTime() <= (time + length) && (time + length) >= page.getEndTime()))
		{// 需要查询的消息的时间范围中，最小时间戳小于缓存中的最小时间戳，但是最大时间戳落在缓存中，此时需要从数据库中加载满足最小时间戳的消息

			// 从数据库取得超出部分数据
			spteMsgs = read(time, page.getBeginTime() - time - 1);
			// 再合并缓存中的数据
			spteMsgs.addAll(page.getSPTEMsgs());
			// 删除多余的数据
			Iterator<SPTEMsg> it = spteMsgs.iterator();
			while (it.hasNext())
			{
				SPTEMsg msg = it.next();
				if (msg.getTimeStamp() > (time + length))
					it.remove();
			}

		} else
		{// 需要查询的消息时间范围最小值小于缓存中的最小值，而最大值大于缓存中的最大值
			// 重新填充缓存
			this.clearCache();
			spteMsgs = read(time, length);
		}
		// 放入缓存
		page.setSPTEMsgs(spteMsgs);
		page.setBeginTime(time);
		page.setEndTime(time + length);

		return spteMsgs.toArray(new SPTEMsg[spteMsgs.size()]);
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
	private SPTEMsg[] getMsgsFromCache(long time, long length, String field, String[] range)
	{
		if (page.getBeginTime() > (time + length) || page.getEndTime() < time)
			return new SPTEMsg[0];

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
						String topicId = sptemsg.getMsg().getTopicId();
						if ((field.equals(TOPICID) && rg.equals(topicId)) || (field.equals(MSGID) && rg.equals(topicId)))
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
