package com.coretek.spte.ui.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.coretek.common.template.SPTEMsg;

/**
 * ��Ϣ�ط�ʱ�����ݲ�ѯ�����
 * 
 * @author Sim.Wang 2012-3-26
 */
public class ReviewCache extends BaseDAO implements IReviewCache
{
	private SPTECache	page	= new SPTECache();	// �������ݷ��ڴ˶�����

	public ReviewCache(String dataPath, String icdPath)
	{
		super(dataPath, icdPath);
	}

	/**
	 * ��ջ���
	 */
	public void clearCache()
	{
		this.page.clear();
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
		// �ӻ����ж�ȡ
		return getMsgFromBufMem(time, length, null, null);
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
		// �ӻ����ж�ȡ
		return getMsgFromBufMem(time, length, MSGID, range);
	}

	/**
	 * �������ж�ȡ���� ��������е����ݲ���������Ҫ����ͨ�����ݿ���»��� �ٵ������ж�ȡ����
	 * 
	 * @param time
	 * @param length
	 * @param field
	 * @param range
	 * @return
	 */
	private synchronized SPTEMsg[] getMsgFromBufMem(long time, long length, String field, String[] range)
	{
		// �����а������е�����
		if (page.getBeginTime() <= time && page.getEndTime() >= (time + length))
		{
			/* ȡֵ�����ڻ����� */
			return getMsgsFromCache(time, length, field, range);
		}

		List<SPTEMsg> spteMsgs;
		if ((page.getBeginTime() <= time && time <= page.getEndTime()) && (page.getEndTime() < (time + length)))
		{// ��Ҫ��ѯ�����ݵ����ʱ������ڻ����е����ʱ�����������Сʱ������ڻ����У���ʱ��Ҫ�����ݿ��в�ѯ���������ʱ�������Ϣ
			// �õ�֮ǰ�Ļ�������
			spteMsgs = page.getSPTEMsgs();
			// �����ݿ�����ӳ�����������
			spteMsgs.addAll(read((page.getEndTime() + 1), (time + length - page.getEndTime() - 1)));

			// ɾ�����������
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
		{// ��Ҫ��ѯ����Ϣ��ʱ�䷶Χ�У���Сʱ���С�ڻ����е���Сʱ������������ʱ������ڻ����У���ʱ��Ҫ�����ݿ��м���������Сʱ�������Ϣ

			// �����ݿ�ȡ�ó�����������
			spteMsgs = read(time, page.getBeginTime() - time - 1);
			// �ٺϲ������е�����
			spteMsgs.addAll(page.getSPTEMsgs());
			// ɾ�����������
			Iterator<SPTEMsg> it = spteMsgs.iterator();
			while (it.hasNext())
			{
				SPTEMsg msg = it.next();
				if (msg.getTimeStamp() > (time + length))
					it.remove();
			}

		} else
		{// ��Ҫ��ѯ����Ϣʱ�䷶Χ��СֵС�ڻ����е���Сֵ�������ֵ���ڻ����е����ֵ
			// ������仺��
			this.clearCache();
			spteMsgs = read(time, length);
		}
		// ���뻺��
		page.setSPTEMsgs(spteMsgs);
		page.setBeginTime(time);
		page.setEndTime(time + length);

		return spteMsgs.toArray(new SPTEMsg[spteMsgs.size()]);
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
	private SPTEMsg[] getMsgsFromCache(long time, long length, String field, String[] range)
	{
		if (page.getBeginTime() > (time + length) || page.getEndTime() < time)
			return new SPTEMsg[0];

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
				// ��ȡ����Ҫ�����ݣ�����ѭ��
				break;
			}
		}

		return spteMsgs.toArray(new SPTEMsg[spteMsgs.size()]);
	}
}
