/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template;

/**
 * ��λ
 * 
 * @author ���Ρ
 * @date 2012-1-3
 */
public class ICDUnit extends Helper
{

	private Integer	ID;

	private String	name;			// ��λ����

	private String	displayName;

	public ICDUnit(int id, String name, String displayName)
	{
		this.ID = id;
		this.name = name;
		this.displayName = displayName;
	}

	/**
	 * @return the iD <br/>
	 * 
	 */
	public Integer getID()
	{
		return ID;
	}

	/**
	 * @param iD the iD to set <br/>
	 * 
	 */
	public void setID(Integer iD)
	{
		ID = iD;
	}

	/**
	 * @return the name <br/>
	 * 
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set <br/>
	 * 
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the displayName <br/>
	 * 
	 */
	public String getDisplayName()
	{
		return displayName;
	}

	/**
	 * @param displayName the displayName to set <br/>
	 * 
	 */
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}

}
