/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitorview.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.coretek.common.template.SPTEMsg;
import com.coretek.common.utils.StringUtils;

/**
 * �ڵ�Ԫ�ؼ���
 * 
 * @author ���� 2012-3-14
 */
public class NodeElementSet
{

	private static final long		serialVersionUID	= -378150288233272831L;

	// ��Ϣ�ڵ�
	private SPTEMsg					monitorMsgNode;

	// �ڵ�Ԫ�ع�����
	private NodeElementSetManager	manager;

	// ��Ϣҳ�š�����ҳ��ֵ��
	private Map<Long, CachePage>	map					= new HashMap<Long, CachePage>();

	// ��Ϣ�б�
	private int						maxElementLength;

	// ��Ϣ�б�
	private ArrayList<SPTEMsg>		msgs				= new ArrayList<SPTEMsg>();

	// ��Ϣ�б�
	private String					uuid;

	public NodeElementSet()
	{
		this.uuid = StringUtils.getUUID();
	}

	/**
	 * ���ܽڵ㼯�ϵ��Թ�����
	 * 
	 * @param manager ���ܽڵ㼯�Ϲ�����
	 */
	public NodeElementSet(NodeElementSetManager manager)
	{
		this();
		this.manager = manager;
	}

	/**
	 * �򻺴�ҳ�������ϢԪ��
	 * 
	 * @param element ��ϢԪ��
	 */
	public synchronized void addSPTEMsgElement(SPTEMsg element)
	{
		long time = element.getTimeStamp();
		long pageNum = time / getManager().getCachePageTimeLength();
		if (getMap().containsKey(Long.valueOf(pageNum)))
		{
			if (getMap().get(Long.valueOf(pageNum)).getSpteMsgs().size() < getManager().getPageSubItemLength())
			{
				getMap().get(Long.valueOf(pageNum)).addSPTEMsgElement(element);
			}
		} else
		{
			if (getMap().keySet().size() > getManager().getPageItemLength())
			{
				Iterator<Long> iterator = getMap().keySet().iterator();
				if (iterator.hasNext())
				{
					CachePage cachePage = getMap().remove(iterator.next());
					cachePage.clearSPTEMsgElement();
				}
			}
			CachePage cachePage = new CachePage(pageNum * getManager().getCachePageTimeLength(), (pageNum + 1) * getManager().getCachePageTimeLength() - 1);
			cachePage.addSPTEMsgElement(element);
			getMap().put(Long.valueOf(pageNum), cachePage);
		}
	}

	/**
	 * ��ù��ܽڵ㼯�Ϲ�����
	 * 
	 * @return ���ܽڵ㼯�Ϲ�����
	 */
	public NodeElementSetManager getManager()
	{
		return manager;
	}

	/**
	 * ���ҳ���뻺��ҳ�ļ�ֵ��
	 * 
	 * @return ҳ���뻺��ҳ�ļ�ֵ��
	 */
	public synchronized Map<Long, CachePage> getMap()
	{
		return map;
	}

	/**
	 * ��ù��ܽڵ㼯�ϵ����Ԫ�س���
	 * 
	 * @return ���ܽڵ㼯�ϵ����Ԫ�س���
	 */
	public int getMaxElementLength()
	{
		return maxElementLength;
	}

	/**
	 * �����Ϣ�б�
	 * 
	 * @return ��Ϣ�б�
	 */
	public ArrayList<SPTEMsg> getMsgs()
	{
		return msgs;
	}

	/**
	 * �ӻ���ҳ�л�ȡ������ʾ����ϢԪ���б�
	 * 
	 * @param currentTimeStamp ��ʼʱ���
	 * @param endTimeStamp ����ʱ���
	 * @return ������ʾ����ϢԪ���б�
	 */
	public synchronized List<SPTEMsg> getSPTEMsgElementsToShow(long currentTimeStamp, long endTimeStamp)
	{
		List<SPTEMsg> list = new ArrayList<SPTEMsg>();

		int startPageNum = (int) (currentTimeStamp / getManager().getCachePageTimeLength());
		int endPageNum = (int) (endTimeStamp / getManager().getCachePageTimeLength());

		for (int i = startPageNum; i < endPageNum + 1; i++)
		{
			if (getMap().get(Long.valueOf(i)) != null)
			{
				list.addAll(getMap().get(Long.valueOf(i)).getSPTEMsgElementsToShow(currentTimeStamp, endTimeStamp));
			}
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode() 2012-3-14
	 */
	@Override
	public int hashCode()
	{
		int result = 17;
		result = result * 31 + this.uuid.hashCode();
		return result;
	}

	/**
	 * �жϵ�ǰҳ���Ƿ񻺴�
	 * 
	 * @param pageNum ҳ��
	 * @return �Ƿ񻺴�
	 */
	public synchronized boolean isPageCache(long pageNum)
	{
		return getMap().containsKey(Long.valueOf(pageNum));
	}

	/**
	 * �жϵ�ǰʱ������Ƿ񻺴�
	 * 
	 * @param startTimeStamp ��ʼʱ���
	 * @param endTimeStamp ����ʱ���
	 * @return �Ƿ񻺴�
	 */
	public synchronized boolean isPageCache(long startTimeStamp, long endTimeStamp)
	{
		if (getMap().get(Long.valueOf(startTimeStamp / getManager().getCachePageTimeLength())) != null)
		{
			return getMap().get(Long.valueOf(startTimeStamp / getManager().getCachePageTimeLength())).isSPTEMsgElementCache(startTimeStamp, endTimeStamp);
		}
		return false;
	}

	/**
	 * ���ù��ܽڵ㼯�Ϲ�����
	 * 
	 * @param map ���ܽڵ㼯�Ϲ�����
	 */
	public synchronized void setMap(Map<Long, CachePage> map)
	{
		this.map = map;
	}

	/**
	 * ���ù��ܽڵ㼯�ϵ����Ԫ�س���
	 * 
	 * @param maxElementLength ���ܽڵ㼯�ϵ����Ԫ�س���
	 */
	public void setMaxElementLength(int maxElementLength)
	{
		this.maxElementLength = maxElementLength;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<field");

		sb.append(" >");
		sb.append("\n");

		sb.append("</field>");
		sb.append("\n");
		return sb.toString();
	}

	public SPTEMsg getMonitorMsgNode()
	{
		return monitorMsgNode;
	}

	public void setMonitorMsgNode(SPTEMsg monitorMsgNode)
	{
		this.monitorMsgNode = monitorMsgNode;
	}

	/**
	 * @author ���� 2012-3-14
	 */
	private static class CachePage
	{

		// ����ʱ���
		private long			endTime		= -1;

		// ��Ϣ�б�
		private List<SPTEMsg>	spteMsgs	= new ArrayList<SPTEMsg>();

		// ��ʼʱ���
		private long			startTime	= -1;

		/**
		 * ����ҳ���Թ�����
		 * 
		 * @param startTime ��ʼʱ���
		 * @param endTime ����ʱ���
		 */
		public CachePage(long startTime, long endTime)
		{
			this.startTime = startTime;
			this.endTime = endTime;
		}

		/**
		 * �򻺴�ҳ�������ϢԪ��
		 * 
		 * @param spteMsg ��ϢԪ��
		 */
		public synchronized void addSPTEMsgElement(SPTEMsg spteMsg)
		{
			if (spteMsg.getTimeStamp() > getCurrentPageEndTime() && spteMsg.getTimeStamp() < getEndTime())
			{
				spteMsgs.add(spteMsg);
			}
		}

		/**
		 * �ӻ���ҳ��������ϢԪ���б�
		 */
		public synchronized void clearSPTEMsgElement()
		{
			if (spteMsgs.size() > 0)
			{
				spteMsgs.clear();
			}
		}

		/**
		 * @return ����ʱ���
		 */
		private long getCurrentPageEndTime()
		{
			if (spteMsgs.size() > 0)
			{
				return spteMsgs.get(spteMsgs.size() - 1).getTimeStamp();
			} else
			{
				return getStartTime();
			}
		}

		/**
		 * ����ʱ���
		 * 
		 * @return ����ʱ���
		 */
		public long getEndTime()
		{
			return endTime;
		}

		/**
		 * �ӻ���ҳ�л�ȡ������ʾ����ϢԪ���б�
		 * 
		 * @param startTimeStamp ��ʼʱ���
		 * @param endTimeStamp ����ʱ���
		 * @return ������ʾ����ϢԪ���б�
		 */
		public synchronized List<SPTEMsg> getSPTEMsgElementsToShow(long startTimeStamp, long endTimeStamp)
		{
			List<SPTEMsg> list = new ArrayList<SPTEMsg>();
			long current = startTimeStamp;

			for (SPTEMsg msg : spteMsgs)
			{
				long timeStamp = msg.getTimeStamp();

				// ��ȡ���ڿ�ʼʱ�����С�ڽ���ʱ����������ڵ�ǰʱ�������ϢԪ��
				if ((timeStamp >= startTimeStamp) && (timeStamp <= endTimeStamp) && (timeStamp > current))
				{
					list.add(msg);
					current = timeStamp;
				} else if (timeStamp > endTimeStamp)
				{
					break;
				}
			}

			return list;
		}

		/**
		 * ��û���ҳ�е���Ϣ�б�
		 * 
		 * @return ��Ϣ�б�
		 */
		public List<SPTEMsg> getSpteMsgs()
		{
			return spteMsgs;
		}

		/**
		 * ��ʼʱ���
		 * 
		 * @return ��ʼʱ���
		 */
		public long getStartTime()
		{
			return startTime;
		}

		/**
		 * �жϵ�ǰʱ������Ƿ񻺴�
		 * 
		 * @param startTimeStamp ��ʼʱ���
		 * @param endTimeStamp ����ʱ���
		 * @return �Ƿ񻺴�
		 */
		public synchronized boolean isSPTEMsgElementCache(long startTimeStamp, long endTimeStamp)
		{
			for (SPTEMsg field : spteMsgs)
			{
				long timeStamp = field.getTimeStamp();
				if ((timeStamp >= startTimeStamp) && (timeStamp <= endTimeStamp))
				{
					return true;
				}
			}

			return false;
		}
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof NodeElementSet)
		{
			NodeElementSet set = (NodeElementSet) obj;
			return set.uuid.equals(this.uuid);
		}
		return false;
	}
}
