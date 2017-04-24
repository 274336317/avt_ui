/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template;

import java.util.ArrayList;
import java.util.List;

/**
 * ICD中的信号。在ICD中，所有的信号都是通过信号标识符(sginalSymbol)来区分的。
 * 
 * @author 孙大巍
 * @date 2012-1-3
 */
public class ICDField extends Helper
{

	private static final long	serialVersionUID	= 6963437403205245061L;

	// 包含的信号
	private List<ICDField>		icdFields			= new ArrayList<ICDField>();

	private List<ICDEnum>		icdEnums			= new ArrayList<ICDEnum>();

	// 包含本信号的信号
	private ICDField			parentField;

	// 信号对应的单位类型，可以为空
	private ICDUnitType			icdUnitType			= null;

	// 信号对应的单位代码，可以为空
	private ICDUnit				icdUnit				= null;

	/**
	 * @return the parentField <br/>
	 * 
	 */
	public ICDField getParentField()
	{
		return parentField;
	}

	/**
	 * @param parentField the parentField to set <br/>
	 * 
	 */
	public void setParentField(ICDField parentField)
	{
		this.parentField = parentField;
	}

	/**
	 * 添加包含信号
	 * 
	 * @param icdField
	 */
	public void addICDField(ICDField icdField)
	{
		this.icdFields.add(icdField);
		icdField.setParentField(this);
	}

	/**
	 * @return the icdFields <br/>
	 * 
	 */
	public List<ICDField> getIcdFields()
	{
		return icdFields;
	}

	/**
	 * @param icdFields the icdFields to set <br/>
	 * 
	 */
	public void setIcdFields(List<ICDField> icdFields)
	{
		this.icdFields = icdFields;
	}

	public ICDUnitType getIcdUnitType()
	{
		return icdUnitType;
	}

	public void setIcdUnitType(ICDUnitType icdUnitType)
	{
		this.icdUnitType = icdUnitType;
	}

	public ICDUnit getIcdUnit()
	{
		return icdUnit;
	}

	public void setIcdUnit(ICDUnit icdUnit)
	{
		this.icdUnit = icdUnit;
	}

	/**
	 * @return the icdEnums <br/>
	 * 
	 */
	public List<ICDEnum> getIcdEnums()
	{
		return icdEnums;
	}

	/**
	 * @param icdEnums the icdEnums to set <br/>
	 * 
	 */
	public void setIcdEnums(List<ICDEnum> icdEnums)
	{
		this.icdEnums = icdEnums;
	}

	public void addICDEnum(ICDEnum icdEnum)
	{
		this.icdEnums.add(icdEnum);
	}

	public Attribute getAttribute(String name)
	{
		for (Attribute att : attributes)
		{
			if (att.getName().equals(name))
			{
				return att;
			}
		}

		return null;
	}

}
