/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
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
 * �����Ӧ��xml�ϵ� "�ź�" �ڵ�. ���������רҵ��ʿ���벻Ҫ�޸Ĵ��ļ�.
 * 
 * @author GENERATED BY SPTE_CODE_ROBOT
 */
@EntityRules(xpath = "�ź�", xmlName = "�ź�", displayField = "", dragable = false, superClazz = "com.coretek.common.template.build.codeTemplate.Entity")
public class Signal extends com.coretek.common.template.build.codeTemplate.Entity
{
	// �����Զ�Ӧ��"�źŵ�λ"
	@FieldRules(xmlName = "�źŵ�λ", display = true, type = "String", node = true, editable = false, textNode = false)
	private String	signalUnit;
	// �����Զ�Ӧ��"�ź����ֵ"
	@FieldRules(xmlName = "�ź����ֵ", display = true, type = "Double", node = true, editable = false, textNode = false)
	private Double	signalMaxValue;
	// �����Զ�Ӧ��"�ź����鳤��"
	@FieldRules(xmlName = "�ź����鳤��", display = true, type = "Integer", node = true, editable = false, textNode = false)
	private Integer	signalArrayLength;
	// �����Զ�Ӧ��"�����Чλ"
	@FieldRules(xmlName = "�����Чλ", display = true, type = "String", node = true, editable = false, textNode = false)
	private String	lowestBitValue;
	// �����Զ�Ӧ��"�źų���"
	@FieldRules(xmlName = "�źų���", display = true, type = "String", node = true, editable = false, textNode = false)
	private String	signalLength;
	// �����Զ�Ӧ��"�źű�ʶ��"
	@FieldRules(xmlName = "�źű�ʶ��", display = true, type = "String", node = true, editable = false, textNode = false)
	private String	signalSymbol;
	// �����Զ�Ӧ��"�����Чλ"
	@FieldRules(xmlName = "�����Чλ", display = true, type = "String", node = true, editable = false, textNode = false)
	private String	highestBitValue;
	// �����Զ�Ӧ��"�ź�����"
	@FieldRules(xmlName = "�ź�����", display = true, type = "String", node = true, editable = false, textNode = false)
	private String	signalName;
	// �����Զ�Ӧ��"�ź���Сֵ"
	@FieldRules(xmlName = "�ź���Сֵ", display = true, type = "Double", node = true, editable = false, textNode = false)
	private Double	signalMinValue;
	// �����Զ�Ӧ��"�ź�����"
	@FieldRules(xmlName = "�ź�����", display = true, type = "String", node = true, editable = false, textNode = false)
	private String	signalType;
	// �����Զ�Ӧ��"�Ƿ��з���"
	@FieldRules(xmlName = "�Ƿ��з���", display = true, type = "String", node = true, editable = false, textNode = false)
	private String	unsigned;
	// �����Զ�Ӧ��"�ź�ID"
	@FieldRules(xmlName = "�ź�ID", display = true, type = "Integer", node = true, editable = false, textNode = false)
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
		} else if(signalLength.indexOf("��") >= 0)
		{
			int index = signalLength.indexOf("��");
			String temp = signalLength.substring(0, index);
			if(StringUtils.isNumber(temp))
			{
				int length = Integer.valueOf(temp).intValue() * 32;
				this.signalLength = String.valueOf(length);
			} else 
			{
				throw new RuntimeException("�޷������źų��ȡ��ַ�����" + signalLength);
			}
		} else if(signalLength.indexOf("λ")>=0)
		{
			int index = signalLength.indexOf("λ");
			String temp = signalLength.substring(0, index);
			if(StringUtils.isNumber(temp))
			{
				this.signalLength = temp;
			} else 
			{
				throw new RuntimeException("�޷������źų��ȡ��ַ�����" + signalLength);
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