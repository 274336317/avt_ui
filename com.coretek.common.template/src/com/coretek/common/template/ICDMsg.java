/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template;

import java.util.ArrayList;
import java.util.List;

/**
 * ICD文件中的功能对象消息+主题的结合。
 * 
 * @author 孙大巍
 * @date 2012-1-3
 */
public class ICDMsg extends Helper
{

	private static final long	serialVersionUID	= 7885710315731077591L;
	private List<ICDField>		fields				= new ArrayList<ICDField>();	// 信号
	// 消息的目的功能对象，可能会是功能单元、功能节点
	private List<Integer>		destIDs				= new ArrayList<Integer>();

	/**
	 * 添加目的功能对象ID
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
	 * 添加信号
	 * 
	 * @param field </br>
	 */
	public void addField(ICDField field)
	{
		this.fields.add(field);
	}

}