/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template;

import java.util.ArrayList;
import java.util.List;

/**
 * ICD�ļ��еĹ��ܶ�����Ϣ+����Ľ�ϡ�
 * 
 * @author ���Ρ
 * @date 2012-1-3
 */
public class ICDMsg extends Helper
{

	private static final long	serialVersionUID	= 7885710315731077591L;
	private List<ICDField>		fields				= new ArrayList<ICDField>();	// �ź�
	// ��Ϣ��Ŀ�Ĺ��ܶ��󣬿��ܻ��ǹ��ܵ�Ԫ�����ܽڵ�
	private List<Integer>		destIDs				= new ArrayList<Integer>();

	/**
	 * ���Ŀ�Ĺ��ܶ���ID
	 * 
	 * @param destID
	 */
	public void addDestID(Integer destID)
	{
		this.destIDs.add(destID);
	}

	/**
	 * @return the destIDs <br/>
	 * 
	 */
	public List<Integer> getDestIDs()
	{
		return destIDs;
	}

	/**
	 * @param destIDs the destIDs to set <br/>
	 * 
	 */
	public void setDestIDs(List<Integer> destIDs)
	{
		this.destIDs = destIDs;
	}

	/**
	 * @return the fields <br/>
	 * 
	 */
	public List<ICDField> getFields()
	{
		return fields;
	}

	/**
	 * @param fields the fields to set <br/>
	 * 
	 */
	public void setFields(List<ICDField> fields)
	{
		this.fields = fields;
	}

	/**
	 * ����ź�
	 * 
	 * @param field </br>
	 */
	public void addField(ICDField field)
	{
		this.fields.add(field);
	}

}