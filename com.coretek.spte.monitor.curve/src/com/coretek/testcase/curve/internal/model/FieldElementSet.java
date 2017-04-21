/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.testcase.curve.internal.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.graphics.RGB;

import com.coretek.common.template.ICDField;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.utils.StringUtils;

/**
 * �ź�Ԫ�ؼ���
 * 
 * @author ���� 2012-3-14
 */
public class FieldElementSet
{

	private static final long		serialVersionUID	= -378150288233272831L;

	// �ź���ɫ
	private RGB						color;

	// �źŵ�ICDField����
	private ICDField				field;

	// �źŵĸ������
	private SPTEMsg					monitorMsgNode;

	// �ź���������
	private int						lineType;

	// �źż��Ϲ�����
	private FieldElementSetManager	manager;

	// ҳ���뻺��ҳ�ļ�ֵ��
	private Map<Long, CachePage>	map					= new HashMap<Long, CachePage>();

	// �ź���Сֵ
	private int						maxValue;

	// �ź���Сֵ
	private int						minValue;

	// �źŵ�uuid
	private String					uuid;

	// �ź��Ƿ�ɼ�
	private boolean					visible;

	/**
	 * �źż��Ϲ�����
	 * 
	 * @param manager �źż��Ϲ�����
	 */
	public FieldElementSet(FieldElementSetManager manager)
	{
		this.manager = manager;
		this.uuid = StringUtils.getUUID();
	}

	/**
	 * ���ź��б�������źŶ���
	 * 
	 * @param element �źŶ���
	 */
	public synchronized void addElement(FieldElement element)
	{
		long time = element.getTime();
		long pageNum = time / getManager().getCachePageTimeLength();
		if (getMap().containsKey(Long.valueOf(pageNum)))
		{
			if (getMap().get(Long.valueOf(pageNum)).getFields().size() < getManager().getPageSubItemLength())
			{
				getMap().get(Long.valueOf(pageNum)).addFieldElement(element);
			}
		} else
		{
			if (getMap().keySet().size() > getManager().getPageItemLength())
			{
				Iterator<Long> iterator = getMap().keySet().iterator();
				long minCachePage = 0;
				if (iterator.hasNext())
				{
					long curCachePage = iterator.next();
					minCachePage = curCachePage;
				}
				while (iterator.hasNext())
				{
					long curCachePage = iterator.next();
					if (minCachePage > curCachePage)
					{
						minCachePage = curCachePage;
					}
				}

				CachePage cachePage = getMap().remove(minCachePage);
				cachePage.clearFieldElement();
				cachePage.clearSPTEMsgElement();
			}
			CachePage cachePage = new CachePage(pageNum * getManager().getCachePageTimeLength(), (pageNum + 1) * getManager().getCachePageTimeLength() - 1);
			cachePage.addFieldElement(element);
			getMap().put(Long.valueOf(pageNum), cachePage);
		}
	}

	/**
	 * ����Ϣ�б��������Ϣ����
	 * 
	 * @param element ��Ϣ����
	 */
	public synchronized void addSPTEMsgElement(SPTEMsg element)
	{
		long time = element.getTimeStamp();
		long pageNum = time / getManager().getCachePageTimeLength();
		if (getMap().containsKey(Long.valueOf(pageNum)))
		{
			if (getMap().get(Long.valueOf(pageNum)).getFields().size() < getManager().getPageSubItemLength())
			{
				getMap().get(Long.valueOf(pageNum)).addSPTEMsgElement(element);
			}
		} else
		{
			if (getMap().keySet().size() > getManager().getPageItemLength())
			{
				Iterator<Long> iterator = getMap().keySet().iterator();
				long minCachePage = 0;
				if (iterator.hasNext())
				{
					long curCachePage = iterator.next();
					minCachePage = curCachePage;
				}
				while (iterator.hasNext())
				{
					long curCachePage = iterator.next();
					if (minCachePage > curCachePage)
					{
						minCachePage = curCachePage;
					}
				}

				CachePage cachePage = getMap().remove(minCachePage);
				cachePage.clearFieldElement();
				cachePage.clearSPTEMsgElement();
			}
			CachePage cachePage = new CachePage(pageNum * getManager().getCachePageTimeLength(), (pageNum + 1) * getManager().getCachePageTimeLength() - 1);
			cachePage.addSPTEMsgElement(element);
			getMap().put(Long.valueOf(pageNum), cachePage);
		}
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		if (obj == this)
			return true;
		if (this.hashCode() == obj.hashCode())
		{
			return true;
		}
		return false;
	}

	/**
	 * ����źŵ���ɫ
	 * 
	 * @return �źŵ���ɫ
	 */
	public synchronized RGB getColor()
	{
		return color;
	}

	/**
	 * ���ʱ���֮����ź��б�
	 * 
	 * @param startTimeStamp ��ʼʱ���
	 * @param endTimeStamp ����ʱ���
	 * @return �ź��б�
	 */
	public synchronized List<FieldElement> getElementsToShow(long startTimeStamp, long endTimeStamp)
	{
		List<FieldElement> list = new ArrayList<FieldElement>();

		int startPageNum = (int) (startTimeStamp / getManager().getCachePageTimeLength());
		int endPageNum = (int) (endTimeStamp / getManager().getCachePageTimeLength());

		for (int i = startPageNum; i < endPageNum + 1; i++)
		{
			if (getMap().get(Long.valueOf(i)) != null)
			{
				list.addAll(getMap().get(Long.valueOf(i)).getElementsToShow(startTimeStamp, endTimeStamp));
			}
		}

		return list;
	}

	/**
	 * ����ź�
	 * 
	 * @return �ź�
	 */
	public ICDField getField()
	{
		return field;
	}

	/**
	 * �����������
	 * 
	 * @return ��������
	 */
	public int getLineType()
	{
		return lineType;
	}

	/**
	 * ����źż��Ϲ�����
	 * 
	 * @return �źż��Ϲ�����
	 */
	public FieldElementSetManager getManager()
	{
		return manager;
	}

	/**
	 * ���ҳ���뻺��ҳ��ֵ��
	 * 
	 * @return ҳ���뻺��ҳ��ֵ��
	 */
	public synchronized Map<Long, CachePage> getMap()
	{
		return map;
	}

	/**
	 * �����Сֵ
	 * 
	 * @return ��Сֵ
	 */
	public int getMaxValue()
	{
		return maxValue;
	}

	/**
	 * �����Сֵ
	 * 
	 * @return ��Сֵ
	 */
	public int getMinValue()
	{
		return minValue;
	}

	/**
	 * ���ʱ���֮�����Ϣ�б�
	 * 
	 * @param startTimeStamp ��ʼʱ���
	 * @param endTimeStamp ����ʱ���
	 * @return ��Ϣ�б�
	 */
	public synchronized List<SPTEMsg> getSPTEMsgElementsToShow(long startTimeStamp, long endTimeStamp)
	{
		List<SPTEMsg> list = new ArrayList<SPTEMsg>();

		int startPageNum = (int) (startTimeStamp / getManager().getCachePageTimeLength());
		int endPageNum = (int) (endTimeStamp / getManager().getCachePageTimeLength());

		for (int i = startPageNum; i < endPageNum + 1; i++)
		{
			if (getMap().get(Long.valueOf(i)) != null)
			{
				list.addAll(getMap().get(Long.valueOf(i)).getSPTEMsgElementsToShow(startTimeStamp, endTimeStamp));
			}
		}
		return list;
	}

	@Override
	public int hashCode()
	{
		int result = 17;
		result = result * 31 + this.uuid.hashCode();
		return result;
	}

	/**
	 * ��ǰҳ�Ƿ񻺴�
	 * 
	 * @param pageNum ҳ��
	 * @return �Ƿ񻺴�
	 */
	public synchronized boolean isPageCache(long pageNum)
	{
		return getMap().containsKey(Long.valueOf(pageNum));
	}

	/**
	 * ��ǰҳ�Ƿ񻺴�
	 * 
	 * @param startTimeStamp ��ʼʱ���
	 * @param endTimeStamp ����ʱ���
	 * @return ��ǰҳ�Ƿ񻺴�
	 */
	public synchronized boolean isPageCache(long startTimeStamp, long endTimeStamp)
	{
		if (getMap().get(Long.valueOf(startTimeStamp / getManager().getCachePageTimeLength())) != null)
		{
			return getMap().get(Long.valueOf(startTimeStamp / getManager().getCachePageTimeLength())).isFieldElementCache(startTimeStamp, endTimeStamp);
		}
		return false;
	}

	/**
	 * �ж��ź��Ƿ�ɼ�
	 * 
	 * @return �ź��Ƿ�ɼ�
	 */
	public boolean isVisible()
	{
		return visible;
	}

	/**
	 * �����źŵ���ɫ
	 * 
	 * @param color �źŵ���ɫ
	 */
	public synchronized void setColor(RGB color)
	{
		this.color = color;
	}

	/**
	 * �����ź�
	 * 
	 * @param field �ź�
	 */
	public void setField(ICDField field)
	{
		this.field = field;
	}

	/**
	 * ������������
	 * 
	 * @param lineType ��������
	 */
	public void setLineType(int lineType)
	{
		this.lineType = lineType;
	}

	/**
	 * ����ҳ���뻺��ҳ��ֵ��
	 * 
	 * @param map ҳ���뻺��ҳ��ֵ��
	 */
	public synchronized void setMap(Map<Long, CachePage> map)
	{
		this.map = map;
	}

	/**
	 * �������ֵ
	 * 
	 * @param maxValue ���ֵ
	 */
	public void setMaxValue(int maxValue)
	{
		this.maxValue = maxValue;
	}

	/**
	 * ������Сֵ
	 * 
	 * @param minValue ��Сֵ
	 */
	public void setMinValue(int minValue)
	{
		this.minValue = minValue;
	}

	/**
	 * �����ź��Ƿ�ɼ�
	 * 
	 * @param visible �ź��Ƿ�ɼ�
	 */
	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
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
		private long				endTime		= -1;

		// �ź��б�
		private List<FieldElement>	fields		= new ArrayList<FieldElement>();

		// ��Ϣ�б�
		private List<SPTEMsg>		spteMsgs	= new ArrayList<SPTEMsg>();

		// ��ʼʱ���
		private long				startTime	= -1;

		public CachePage(long startTime, long endTime)
		{
			this.startTime = startTime;
			this.endTime = endTime;
		}

		/**
		 * ���ź��б�������źŶ���
		 * 
		 * @param element �źŶ���
		 */
		public synchronized void addFieldElement(FieldElement element)
		{
			if (element.getTime() > getCurrentPageFieldEndTime() && element.getTime() < getEndTime())
			{
				fields.add(element);
			}
		}

		/**
		 * ����Ϣ�б��������Ϣ����
		 * 
		 * @param spteMsg ��Ϣ����
		 */
		public synchronized void addSPTEMsgElement(SPTEMsg spteMsg)
		{
			if (spteMsg.getTimeStamp() > getCurrentPageEndTime() && spteMsg.getTimeStamp() < getEndTime())
			{
				spteMsgs.add(spteMsg);
			}
		}

		/**
		 * �����ź��б����
		 */
		public synchronized void clearFieldElement()
		{
			if (fields.size() > 0)
			{
				fields.clear();
			}
		}

		/**
		 * ������Ϣ�б�
		 */
		public synchronized void clearSPTEMsgElement()
		{
			if (spteMsgs.size() > 0)
			{
				spteMsgs.clear();
			}
		}

		/**
		 * ��õ�ǰҳ�Ľ���ʱ���
		 * 
		 * @return ��ǰҳ�Ľ���ʱ���
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
		 * ��õ�ǰҳ�Ľ���ʱ���
		 */
		private long getCurrentPageFieldEndTime()
		{
			if (fields.size() > 0)
			{
				return fields.get(fields.size() - 1).getTime();
			} else
			{
				return getStartTime();
			}
		}

		/**
		 * ����ڿ�ʼʱ��������ʱ���֮����ź��б�
		 * 
		 * @param startTimeStamp ��ʼʱ���
		 * @param endTimeStamp ����ʱ���
		 * @return ��ʱ���֮����ź��б�
		 */
		public synchronized List<FieldElement> getElementsToShow(long startTimeStamp, long endTimeStamp)
		{
			List<FieldElement> list = new ArrayList<FieldElement>();
			long current = startTimeStamp;

			for (FieldElement field : fields)
			{
				long timeStamp = field.getTime();
				if ((timeStamp >= startTimeStamp) && (timeStamp <= endTimeStamp) && (timeStamp > current))
				{
					list.add(field);
					current = timeStamp;
				} else if (timeStamp > endTimeStamp)
				{
					break;
				}
			}

			return list;
		}

		/**
		 * ��ý���ʱ���
		 */
		public long getEndTime()
		{
			return endTime;
		}

		/**
		 * ����ź��б�
		 * 
		 * @return �ź��б�
		 */
		public List<FieldElement> getFields()
		{
			return fields;
		}

		/**
		 * ����ڿ�ʼʱ��������ʱ���֮�����Ϣ�б�
		 * 
		 * @param startTimeStamp ��ʼʱ���
		 * @param endTimeStamp ����ʱ���
		 * @return ��ʱ���֮�����Ϣ�б�
		 */
		public synchronized List<SPTEMsg> getSPTEMsgElementsToShow(long startTimeStamp, long endTimeStamp)
		{
			List<SPTEMsg> list = new ArrayList<SPTEMsg>();
			long current = startTimeStamp;

			for (SPTEMsg msg : spteMsgs)
			{
				long timeStamp = msg.getTimeStamp();
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
		 * ��ÿ�ʼʱ���
		 * 
		 * @return ��ʼʱ���
		 */
		public long getStartTime()
		{
			return startTime;
		}

		/**
		 * ��ʼʱ���������ʱ������ź��Ƿ񱻻���
		 * 
		 * @param startTimeStamp ��ʼʱ���
		 * @param endTimeStamp ����ʱ���
		 * @return ��ǰ�ź��Ƿ񱻻���
		 */
		public synchronized boolean isFieldElementCache(long startTimeStamp, long endTimeStamp)
		{
			for (FieldElement field : fields)
			{
				long timeStamp = field.getTime();
				if ((timeStamp >= startTimeStamp) && (timeStamp <= endTimeStamp))
				{
					return true;
				}
			}

			return false;
		}
	}
}
