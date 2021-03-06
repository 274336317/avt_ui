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

/**
 * 此类对应于xml上的 "功能域消息" 节点. 如果您不是专业人士，请不要修改此文件.
 * 
 * @author GENERATED BY SPTE_CODE_ROBOT
 */
@EntityRules(xpath = "功能域消息", xmlName = "功能域消息", displayField = "msgTopicName", dragable = false, superClazz = "com.coretek.common.template.build.codeTemplate.Entity")
public class FunctionDomainMsg extends com.coretek.common.template.build.codeTemplate.Entity
{
	// 此属性对应于"消息传输周期"
	@FieldRules(xmlName = "消息传输周期", display = true, type = "Integer", node = true, editable = false, textNode = false)
	private Integer							msgTransPeriod;
	// 此属性对应于"广播属性"
	@FieldRules(xmlName = "广播属性", display = true, type = "String", node = true, editable = false, textNode = false)
	private String							brocast;
	// 此属性对应于"源功能ID"
	@FieldRules(xmlName = "源功能ID", display = true, type = "Integer", node = true, editable = false, textNode = false)
	private Integer							sourceFunctionID;
	// 此属性对应于"最大传输延迟"
	@FieldRules(xmlName = "最大传输延迟", display = true, type = "Integer", node = true, editable = false, textNode = false)
	private Integer							maxTranDelay;
	// 此属性对应于"消息主题标识符"
	@FieldRules(xmlName = "消息主题标识符", display = true, type = "String", node = true, editable = false, textNode = false)
	private String							msgTopicSymbol;
	// 此属性对应于"消息主题名称"
	@FieldRules(xmlName = "消息主题名称", display = true, type = "String", node = true, editable = false, textNode = false)
	private String							msgTopicName;
	// 此属性对应于"消息ID"
	@FieldRules(xmlName = "消息ID", display = true, type = "String", node = true, editable = false, textNode = false)
	private String							msgID;
	// 此属性对应于"延迟单位"
	@FieldRules(xmlName = "延迟单位", display = true, type = "String", node = true, editable = false, textNode = false)
	private String							delayUnit;
	// 此属性对应于"周期单位"
	@FieldRules(xmlName = "周期单位", display = true, type = "String", node = true, editable = false, textNode = false)
	private String							periodUnit;
	// 此属性对应于"消息传输类型"
	@FieldRules(xmlName = "消息传输类型", display = true, type = "String", node = true, editable = false, textNode = false)
	private String							msgTransType;
	// 此属性对应于"消息名"
	@FieldRules(xmlName = "消息名", display = true, type = "String", node = true, editable = false, textNode = false)
	private String							msgName;
	// 此属性对应于"上级消息ID"
	@FieldRules(xmlName = "上级消息ID", display = true, type = "String", node = true, editable = false, textNode = false)
	private String							parentMsgID;
	// 引用属性
	@ReferenceRules(condition = "sourceFunctionID=ID", type = "com.coretek.spte.FunctionDomain")
	private com.coretek.spte.FunctionDomain	logicParent;
	// 引用属性
	@ReferenceRules(condition = "msgTopicSymbol=topicSymbol", type = "com.coretek.spte.Topic")
	private com.coretek.spte.Topic			topic;

	public Integer getMsgTransPeriod()
	{
		return this.msgTransPeriod;
	}

	public void setMsgTransPeriod(Integer msgTransPeriod)
	{
		this.msgTransPeriod = msgTransPeriod;
	}

	public String getBrocast()
	{
		return this.brocast;
	}

	public void setBrocast(String brocast)
	{
		this.brocast = brocast;
	}

	public Integer getSourceFunctionID()
	{
		return this.sourceFunctionID;
	}

	public void setSourceFunctionID(Integer sourceFunctionID)
	{
		this.sourceFunctionID = sourceFunctionID;
	}

	public Integer getMaxTranDelay()
	{
		return this.maxTranDelay;
	}

	public void setMaxTranDelay(Integer maxTranDelay)
	{
		this.maxTranDelay = maxTranDelay;
	}

	public String getMsgTopicSymbol()
	{
		return this.msgTopicSymbol;
	}

	public void setMsgTopicSymbol(String msgTopicSymbol)
	{
		this.msgTopicSymbol = msgTopicSymbol;
	}

	public String getMsgTopicName()
	{
		return this.msgTopicName;
	}

	public void setMsgTopicName(String msgTopicName)
	{
		this.msgTopicName = msgTopicName;
	}

	public String getMsgID()
	{
		return this.msgID;
	}

	public void setMsgID(String msgID)
	{
		this.msgID = msgID;
	}

	public String getDelayUnit()
	{
		return this.delayUnit;
	}

	public void setDelayUnit(String delayUnit)
	{
		this.delayUnit = delayUnit;
	}

	public String getPeriodUnit()
	{
		return this.periodUnit;
	}

	public void setPeriodUnit(String periodUnit)
	{
		this.periodUnit = periodUnit;
	}

	public String getMsgTransType()
	{
		return this.msgTransType;
	}

	public void setMsgTransType(String msgTransType)
	{
		this.msgTransType = msgTransType;
	}

	public String getMsgName()
	{
		return this.msgName;
	}

	public void setMsgName(String msgName)
	{
		this.msgName = msgName;
	}

	public String getParentMsgID()
	{
		return this.parentMsgID;
	}

	public void setParentMsgID(String parentMsgID)
	{
		this.parentMsgID = parentMsgID;
	}

	public com.coretek.spte.FunctionDomain getLogicParent()
	{
		return this.logicParent;
	}

	public void setLogicParent(com.coretek.spte.FunctionDomain logicParent)
	{
		super.setLogicParent(logicParent);
		this.logicParent = logicParent;
	}

	public com.coretek.spte.Topic getTopic()
	{
		return this.topic;
	}

	public void setTopic(com.coretek.spte.Topic topic)
	{
		this.topic = topic;
	}
}
