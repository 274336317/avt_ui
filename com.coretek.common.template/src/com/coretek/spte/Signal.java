/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte;

import java.util.List;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.common.template.build.codeTemplate.EntityRules;
import com.coretek.common.template.build.codeTemplate.FieldRules;
import com.coretek.common.template.build.codeTemplate.ReferenceRules;
import com.coretek.common.utils.StringUtils;

/**
 * 此类对应于xml上的 "信号" 节点. 如果您不是专业人士，请不要修改此文件.
 * 
 * @author GENERATED BY SPTE_CODE_ROBOT
 */
@EntityRules(xpath = "信号", xmlName = "信号", displayField = "", dragable = false, superClazz = "com.coretek.common.template.build.codeTemplate.Entity")
public class Signal extends com.coretek.common.template.build.codeTemplate.Entity
{
	// 此属性对应于"信号单位"
	@FieldRules(xmlName = "信号单位", display = true, type = "String", node = true, editable = false, textNode = false)
	private String	signalUnit;
	// 此属性对应于"信号最大值"
	@FieldRules(xmlName = "信号最大值", display = true, type = "Double", node = true, editable = false, textNode = false)
	private Double	signalMaxValue;
	// 此属性对应于"信号数组长度"
	@FieldRules(xmlName = "信号数组长度", display = true, type = "Integer", node = true, editable = false, textNode = false)
	private Integer	signalArrayLength;
	// 此属性对应于"最低有效位"
	@FieldRules(xmlName = "最低有效位", display = true, type = "String", node = true, editable = false, textNode = false)
	private String	lowestBitValue;
	// 此属性对应于"信号长度"
	@FieldRules(xmlName = "信号长度", display = true, type = "String", node = true, editable = false, textNode = false)
	private String	signalLength;
	// 此属性对应于"信号标识符"
	@FieldRules(xmlName = "信号标识符", display = true, type = "String", node = true, editable = false, textNode = false)
	private String	signalSymbol;
	// 此属性对应于"最高有效位"
	@FieldRules(xmlName = "最高有效位", display = true, type = "String", node = true, editable = false, textNode = false)
	private String	highestBitValue;
	// 此属性对应于"信号名称"
	@FieldRules(xmlName = "信号名称", display = true, type = "String", node = true, editable = false, textNode = false)
	private String	signalName;
	// 此属性对应于"信号最小值"
	@FieldRules(xmlName = "信号最小值", display = true, type = "Double", node = true, editable = false, textNode = false)
	private Double	signalMinValue;
	// 此属性对应于"信号类型"
	@FieldRules(xmlName = "信号类型", display = true, type = "String", node = true, editable = false, textNode = false)
	private String	signalType;
	// 此属性对应于"是否有符号"
	@FieldRules(xmlName = "是否有符号", display = true, type = "String", node = true, editable = false, textNode = false)
	private String	unsigned;
	// 此属性对应于"信号ID"
	@FieldRules(xmlName = "信号ID", display = true, type = "Integer", node = true, editable = false, textNode = false)
	private Integer	signalID;

	public String getSignalUnit()
	{
		return this.signalUnit;
	}

	public void setSignalUnit(String signalUnit)
	{
		this.signalUnit = signalUnit;
	}

	public Double getSignalMaxValue()
	{
		return this.signalMaxValue;
	}

	public void setSignalMaxValue(Double signalMaxValue)
	{
		this.signalMaxValue = signalMaxValue;
	}

	public Integer getSignalArrayLength()
	{
		return this.signalArrayLength;
	}

	public void setSignalArrayLength(Integer signalArrayLength)
	{
		this.signalArrayLength = signalArrayLength;
	}

	public String getLowestBitValue()
	{
		return this.lowestBitValue;
	}

	public void setLowestBitValue(String lowestBitValue)
	{
		this.lowestBitValue = lowestBitValue;
	}

	public String getSignalLength()
	{
		return this.signalLength;
	}

	public void setSignalLength(String signalLength)
	{
		if(StringUtils.isNumber(signalLength))
		{
			this.signalLength = signalLength;
		} else if(signalLength.indexOf("字") >= 0)
		{
			int index = signalLength.indexOf("字");
			String temp = signalLength.substring(0, index);
			if(StringUtils.isNumber(temp))
			{
				int length = Integer.valueOf(temp).intValue() * 32;
				this.signalLength = String.valueOf(length);
			} else 
			{
				throw new RuntimeException("无法解析信号长度。字符串：" + signalLength);
			}
		} else if(signalLength.indexOf("位")>=0)
		{
			int index = signalLength.indexOf("位");
			String temp = signalLength.substring(0, index);
			if(StringUtils.isNumber(temp))
			{
				this.signalLength = temp;
			} else 
			{
				throw new RuntimeException("无法解析信号长度。字符串：" + signalLength);
			}
		}
		
	}

	public String getSignalSymbol()
	{
		return this.signalSymbol;
	}

	public void setSignalSymbol(String signalSymbol)
	{
		this.signalSymbol = signalSymbol;
	}

	public String getHighestBitValue()
	{
		return this.highestBitValue;
	}

	public void setHighestBitValue(String highestBitValue)
	{
		this.highestBitValue = highestBitValue;
	}

	public String getSignalName()
	{
		return this.signalName;
	}

	public void setSignalName(String signalName)
	{
		this.signalName = signalName;
	}

	public Double getSignalMinValue()
	{
		return this.signalMinValue;
	}

	public void setSignalMinValue(Double signalMinValue)
	{
		this.signalMinValue = signalMinValue;
	}

	public String getSignalType()
	{
		return this.signalType;
	}

	public void setSignalType(String signalType)
	{
		this.signalType = signalType;
	}

	public String getUnsigned()
	{
		return this.unsigned;
	}

	public void setUnsigned(String unsigned)
	{
		this.unsigned = unsigned;
	}

	public Integer getSignalID()
	{
		return this.signalID;
	}

	public void setSignalID(Integer signalID)
	{
		this.signalID = signalID;
	}
}
