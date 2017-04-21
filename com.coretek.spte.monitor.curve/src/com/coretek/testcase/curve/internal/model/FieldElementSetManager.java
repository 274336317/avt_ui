/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.testcase.curve.internal.model;

import java.util.ArrayList;
import java.util.List;

import com.coretek.common.template.SPTEMsg;

/**
 * �ź�Ԫ�ؼ��Ϲ�����
 * 
 * @author ���� 2012-3-14
 */
public class FieldElementSetManager
{
	// һ��������ͼһ������ʾ������źų���
	private long						cachePageTimeLength;

	// һ��������ͼͬʱ֧�ֵ�����ź���
	private int							fieldElementSetLength;

	// �źż����б�
	private ArrayList<FieldElementSet>	fieldSets	= new ArrayList<FieldElementSet>();

	// �û����õ���ϢID
	private List<String>				messageIds	= new ArrayList<String>();

	// һ��������ͼ��֧�ֵ�����ź�ҳ
	private long						pageItemLength;

	// һ��������ͼ��֧�ֵ��ź�ҳ�����������
	private long						pageSubItemLength;

	public FieldElementSetManager(int fieldElementSetLength, long cachePageTimeLength, long pageItemLength, long pageSubItemLength)
	{
		super();
		this.fieldElementSetLength = fieldElementSetLength;
		this.cachePageTimeLength = cachePageTimeLength;
		this.pageItemLength = pageItemLength;
		this.pageSubItemLength = pageSubItemLength;
	}

	/**
	 * �򻺴�ҳ������ź���
	 * 
	 * @param element �źż���
	 * @param field �ź���
	 */
	public synchronized void addElement(FieldElement element, FieldElementSet field)
	{
		field.addElement(element);
	}

	/**
	 * ���źż����б�����źż���
	 * 
	 */
	public synchronized void addField(FieldElementSet cfb)
	{
		this.fieldSets.add(cfb);
	}

	/**
	 * �����źż����б�
	 */
	public synchronized void clear()
	{
		for (FieldElementSet field : fieldSets)
		{
			field.getMap().keySet().clear();
		}
		this.fieldSets.clear();
	}

	/**
	 * �����źż����б��е�����
	 */
	public synchronized void clearData()
	{
		for (FieldElementSet field : fieldSets)
		{
			field.getMap().keySet().clear();
		}
	}

	/**
	 * ����źż����б�
	 * 
	 * @return �źż����б�
	 */
	public synchronized List<FieldElementSet> getAllFields()
	{
		List<FieldElementSet> fields = new ArrayList<FieldElementSet>();
		fields.addAll(this.fieldSets);
		return fields;
	}

	/**
	 * ���������ͼһ������ʾ������źų���
	 * 
	 * @return ������ͼһ������ʾ������źų���
	 */
	public long getCachePageTimeLength()
	{
		return cachePageTimeLength;
	}

	/**
	 * ���һ��������ͼͬʱ֧�ֵ�����ź���
	 * 
	 * @return һ��������ͼͬʱ֧�ֵ�����ź���
	 */
	public int getFieldElementSetLength()
	{
		return fieldElementSetLength;
	}

	/**
	 * ����û����õ���ϢID�б�
	 * 
	 * @return ��ϢID�б�
	 */
	public List<String> getMessageIds()
	{
		if (messageIds != null && messageIds.size() > 0)
		{
			return messageIds;
		} else
		{
			List<FieldElementSet> fields = getAllFields();
			if (fields != null && fields.size() > 0)
			{
				for (FieldElementSet field : fields)
				{
					if (field != null)
					{
						SPTEMsg msg = field.getMonitorMsgNode();
						if (!messageIds.contains(msg.getMsg().getId()))
						{
							messageIds.add(msg.getMsg().getId());
						}
					}
				}
				if (messageIds != null && messageIds.size() > 0)
				{
					return messageIds;
				}
			}
			return new ArrayList<String>(0);
		}
	}

	/**
	 * ���һ��������ͼ��֧�ֵ�����ź�ҳ
	 * 
	 * @return һ��������ͼ��֧�ֵ�����ź�ҳ
	 */
	public long getPageItemLength()
	{
		return pageItemLength;
	}

	/**
	 * ���һ��������ͼ��֧�ֵ��ź�ҳ�����������
	 * 
	 * @return һ��������ͼ��֧�ֵ��ź�ҳ�����������
	 */
	public long getPageSubItemLength()
	{
		return pageSubItemLength;
	}

	/**
	 * �ж��źż����б��Ƿ�Ϊ��
	 * 
	 * @return �źż����б��Ƿ�Ϊ��
	 */
	public synchronized boolean isEmpty()
	{
		return this.fieldSets.isEmpty();
	}

	/**
	 * �жϵ�ǰҳ���Ƿ񻺴�
	 * 
	 * @param pageNum �����ҳ��
	 * @return ��ǰҳ���Ƿ񻺴�
	 */
	public synchronized boolean isPageCache(long pageNum)
	{
		List<FieldElementSet> fields = getAllFields();
		if (fields.size() > 0)
		{
			return fields.get(0).isPageCache(pageNum);
		}
		return false;
	}

	/**
	 * �жϿ�ʼʱ���������ʱ����Ƿ񻺴��
	 * 
	 * @param startTimeStamp ��ʼʱ���
	 * @param endTimeStamp ����ʱ���
	 * @return ��ʼʱ���������ʱ����Ƿ񻺴��
	 */
	public synchronized boolean isPageCache(long startTimeStamp, long endTimeStamp)
	{
		List<FieldElementSet> fields = getAllFields();
		if (fields.size() > 0)
		{
			return fields.get(0).isPageCache(startTimeStamp, endTimeStamp);
		}
		return false;
	}

	/**
	 * * ����������ͼһ������ʾ������źų���
	 * 
	 * @param cachePageTimeLength ������ͼһ������ʾ������źų���
	 * @param cachePageTimeLength
	 */
	public void setCachePageTimeLength(long cachePageTimeLength)
	{
		this.cachePageTimeLength = cachePageTimeLength;
	}

	/**
	 * * ����һ��������ͼͬʱ֧�ֵ�����ź���
	 * 
	 * @param fieldElementSetLength һ��������ͼͬʱ֧�ֵ�����ź���
	 * @param fieldElementSetLength
	 */
	public void setFieldElementSetLength(int fieldElementSetLength)
	{
		this.fieldElementSetLength = fieldElementSetLength;
	}

	/**
	 * �����źż����б�
	 * 
	 * @param fields �źż����б�
	 */
	public synchronized void setFields(List<FieldElementSet> fields)
	{
		this.fieldSets.addAll(fields);
	}

	/**
	 * * ���� һ��������ͼ��֧�ֵ�����ź�ҳ
	 * 
	 * @param pageItemLength һ��������ͼ��֧�ֵ�����ź�ҳ
	 * @param pageItemLength
	 */
	public void setPageItemLength(long pageItemLength)
	{
		this.pageItemLength = pageItemLength;
	}

	/**
	 * ����һ��������ͼ��֧�ֵ��ź�ҳ�����������
	 * 
	 * @param pageSubItemLength һ��������ͼ��֧�ֵ��ź�ҳ�����������
	 */
	public void setPageSubItemLength(long pageSubItemLength)
	{
		this.pageSubItemLength = pageSubItemLength;
	}
}