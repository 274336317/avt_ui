/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template;

import java.util.ArrayList;
import java.util.List;

/**
 * 单位类型
 * 
 * @author 孙大巍
 * @date 2012-1-3
 */
public class ICDUnitType extends Helper
{

	private String			unitName;							// 单位名称

	private int				ID;								// ID

	private List<ICDUnit>	units	= new ArrayList<ICDUnit>(); // 单位集合

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
	 * 获取单位
	 * 
	 * @param code 单位代码
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
