/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template;

import java.util.ArrayList;
import java.util.List;

/**
 * ��λ����
 * 
 * @author ���Ρ
 * @date 2012-1-3
 */
public class ICDUnitType extends Helper
{

	private String			unitName;							// ��λ����

	private int				ID;								// ID

	private List<ICDUnit>	units	= new ArrayList<ICDUnit>(); // ��λ����

	public ICDUnitType(int id, String unitName)
	{
		this.ID = id;
		this.unitName = unitName;
	}

	public ICDUnitType(int id)
	{
		this.ID = id;
	}

	/**
	 * @return the unitName <br/>
	 * 
	 */
	public String getUnitName()
	{
		return unitName;
	}

	/**
	 * @param unitName the unitName to set <br/>
	 * 
	 */
	public void setUnitName(String unitName)
	{
		this.unitName = unitName;
	}

	/**
	 * @return the iD <br/>
	 * 
	 */
	public int getID()
	{
		return ID;
	}

	/**
	 * @param iD the iD to set <br/>
	 * 
	 */
	public void setID(int iD)
	{
		ID = iD;
	}

	/**
	 * @return the units <br/>
	 * 
	 */
	public List<ICDUnit> getUnits()
	{
		return units;
	}

	/**
	 * @param units the units to set <br/>
	 * 
	 */
	public void setUnits(List<ICDUnit> units)
	{
		this.units = units;
	}

	public void addUnit(ICDUnit unit)
	{
		this.units.add(unit);
	}

	/**
	 * ��ȡ��λ
	 * 
	 * @param code ��λ����
	 * @return </br>
	 */
	public ICDUnit getUnit(Integer code)
	{
		for (ICDUnit unit : units)
		{
			if (unit.getID().equals(code))
				return unit;
		}

		return null;
	}

}
