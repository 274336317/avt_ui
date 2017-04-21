package com.coretek.spte.ui.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.coretek.common.template.SPTEMsg;
import com.coretek.spte.parser.IMessageWriter;

/**
 * �����ʱ�����ݲ�ѯ������������е������޷������صĶ�ȡҪ��ʱ�� ����һ������Ϊ0�����飬�����ǵ����ݿ��н��в�ѯ��
 * 
 * @author Sim.Wang 2012-3-26
 */
public class MonitorCache extends MessageDAO implements IMonitorCache, IMessageWriter
{
	private ReentrantReadWriteLock	lock	= new ReentrantReadWriteLock(true);

	private SPTECache				page	= new SPTECache();					// �������ݷ��ڴ˶�����

	private volatile boolean		needCache;									// ��ʶ�Ƿ���Ҫ�����ݷ��뻺�浱��

	/**
	 * ����һ��MonitorCache����
	 * 
	 * @param dataPath ���ݿ�·��
	 * @param icdPath icd�ļ�·��
	 * @param needCache ��ʶ�Ƿ���Ҫ����Ϣ���뻺���У����Ϊtrue���ʶ��Ҫ
	 */
	public MonitorCache(String dataPath, String icdPath, boolean needCache)
	{
		super(dataPath, icdPath);

		this.needCache = needCache;
	}

	/**
	 * ��ջ���
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
				// ���뻺��
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
	 * ��ȡ���� ʱ��� ��ָ��ʱ�䷶Χ�ڵ�����
	 * 
	 * @param time ��ʼʱ��
	 * @param length ʱ����
	 * @return
	 */
	public SPTEMsg[] getMessage(long time, long length)
	{
		try
		{
			lock.readLock().lock();
			// �ӻ����ж�ȡ
			return getMessage(time, length, null, null);
		} finally
		{
			lock.readLock().unlock();
		}
	}

	/**
	 * ��ȡ���� ʱ��� ��ָ��ʱ�䷶Χ�� ���� topicId Ҳ��ָ����Χ�ڵ�����
	 * 
	 * @param time ��ʼʱ��
	 * @param length ʱ����
	 * @param range topicId�ֶε�ȡֵ��Χ
	 * @return
	 */
	public SPTEMsg[] getMsgLimitByTpcId(long time, int length, String[] range)
	{
		try
		{
			lock.readLock().lock();
			// �ӻ����ж�ȡ
			return getMessage(time, length, TOPICID, range);
		} finally
		{
			lock.readLock().unlock();
		}

	}

	/**
	 * ��ȡ���� ʱ��� ��ָ��ʱ�䷶Χ�� ���� msgId Ҳ��ָ����Χ�ڵ�����
	 * 
	 * @param time ��ʼʱ��
	 * @param length ʱ����
	 * @param range msgId�ֶε�ȡֵ��Χ
	 * @return
	 */
	public SPTEMsg[] getMsgLimitByMsgId(long time, int length, String[] range)
	{
		try
		{
			lock.readLock().lock();
			// �ӻ����ж�ȡ
			return getMessage(time, length, MSGID, range);
		} finally
		{
			lock.readLock().unlock();
		}

	}

	/**
	 * �������ж�ȡ���� ��������е����ݲ���������Ҫ��������ѯ�������Ѿ�д�����ݿ� �����ݿ���ֱ�Ӷ�ȡ����
	 * 
	 * @param time
	 * @param length
	 * @param field
	 * @param range
	 * @return
	 */
	private SPTEMsg[] getMessage(long time, long length, String field, String[] range)
	{
		// �����а������е�����
		if (page.getBeginTime() <= time && page.getEndTime() >= (time + length))
		{
			/* ȡֵ�����ڻ����� */
			return getMsgInBufMem(time, length, field, range);
		}
		return new SPTEMsg[0];
	}

	/**
	 * �ڻ����ж�ȡ����Ҫ�������
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
			// ʱ�䷶Χ�ж�
			if (sptemsg.getTimeStamp() >= time && sptemsg.getTimeStamp() <= (time + length))
			{
				// ָ���ֶ��ж�
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
				// ��ȡ����Ҫ�����ݣ�����ѭ��
				break;
			}
		}
		return spteMsgs.toArray(new SPTEMsg[spteMsgs.size()]);
	}
}