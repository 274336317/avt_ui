/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitorview.internal;

import java.util.ArrayList;
import java.util.List;

import com.coretek.common.template.SPTEMsg;

/**
 * �ڵ�Ԫ�ع�����
 * 
 * @author ���� 2012-3-14
 */

public final class NodeElementSetManager
{

	// һҳ�����ʱ�������
	private long						cachePageTimeLength;

	// ���ܽڵ��б�
	private ArrayList<NodeElementSet>	fieldSets	= new ArrayList<NodeElementSet>();

	// ��ϢID�б�
	private List<String>				messageIds	= new ArrayList<String>();

	// ���ҳ�ĳ���
	private long						pageItemLength;

	// ҳ�����������
	private long						pageSubItemLength;

	/**
	 * @param cachePageTimeLength һҳ�����ʱ�������
	 * @param pageItemLength ���ҳ�ĳ���
	 * @param pageSubItemLength ҳ�����������
	 */
	public NodeElementSetManager(long cachePageTimeLength, long pageItemLength, long pageSubItemLength)
	{
		super();
		this.cachePageTimeLength = cachePageTimeLength;
		this.pageItemLength = pageItemLength;
		this.pageSubItemLength = pageSubItemLength;
	}

	/**
	 * ���ܽڵ㼯���б���ӹ��ܽڵ㼯��
	 * 
	 * @param cfb ���ܽڵ㼯��</br>
	 */
	public synchronized void addField(NodeElementSet cfb)
	{
		this.fieldSets.add(cfb);
	}

	/**
	 * �����ܽڵ��б� </br>
	 */
	public synchronized void clear()
	{
		for (NodeElementSet field : fieldSets)
		{
			field.getMsgs().clear();
		}
		this.fieldSets.clear();
	}

	/**
	 * �����ܽڵ��б������</br>
	 */
	public synchronized void clearData()
	{
		for (NodeElementSet field : fieldSets)
		{
			field.getMsgs().clear();
		}
	}

	/**
	 * ��ù��ܽڵ��б�
	 * 
	 * @return ���ܽڵ��б�</br>
	 */
	public synchronized List<NodeElementSet> getAllFields()
	{
		List<NodeElementSet> fields = new ArrayList<NodeElementSet>();
		fields.addAll(this.fieldSets);
		return fields;
	}

	/**
	 * ��û���ҳ��ʱ�������
	 * 
	 * @return ����ҳ��ʱ�������</br>
	 */
	public long getCachePageTimeLength()
	{
		return cachePageTimeLength;
	}

	/**
	 * ����û����õ���ϢID�б�
	 * 
	 * @return ��ϢID�б� </br>
	 */
	public List<String> getMessageIds()
	{
		if (messageIds != null && messageIds.size() > 0)
		{
			return messageIds;
		} else
		{
			List<NodeElementSet> fields = getAllFields();
			if (fields != null && fields.size() > 0)
			{
				for (NodeElementSet field : fields)
				{
					if (field != null)
					{
						SPTEMsg msg = field.getMonitorMsgNode();
						if (msg instanceof SPTEMsg)
						{ // ���ܵ�Ԫ��Ϣ

							if (!messageIds.contains(msg.getMsg().getId()))
							{
								messageIds.add(msg.getMsg().getId());
							}
						}
					}
				}
				if (messageIds != null && messageIds.size() > 0)
				{
					return messageIds;
				}
			}
		}
		return new ArrayList<String>();
	}

	/**
	 * ���һ����ͼ��֧�ֵ����ҳ��
	 * 
	 * @return һ����ͼ��֧�ֵ����ҳ�� </br>
	 */
	public long getPageItemLength()
	{
		return pageItemLength;
	}

	/**
	 * ���һ����ͼ��֧�ֵ��ź�ҳ�����������
	 * 
	 * @return �����ͼ��֧�ֵ��ź�ҳ�����������</br>
	 */
	public long getPageSubItemLength()
	{
		return pageSubItemLength;
	}

	/**
	 * �жϹ��ܽڵ㼯���б��Ƿ�Ϊ��
	 * 
	 * @return ���ܽڵ㼯���б��Ƿ�Ϊ��</br>
	 */
	public synchronized boolean isEmpty()
	{
		return this.fieldSets.isEmpty();
	}

	/**
	 * ���ܽڵ㼯���б���ӹ��ܽڵ㼯���б�
	 * 
	 * @param fields ���ܽڵ㼯���б� </br>
	 */
	public synchronized void setFields(List<NodeElementSet> fields)
	{
		this.fieldSets.addAll(fields);
	}
}