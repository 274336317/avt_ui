/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template;

import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.common.template.build.codeTemplate.EntityRules;
import com.coretek.common.template.build.codeTemplate.FieldRules;
import com.coretek.spte.testcase.Message;

/**
 * 主要是用于将测试用例中的各种对象生成xml格式的信息。
 * 
 * @author 孙大巍 2011-12-29
 */
public class XMLBean extends Entity implements Cloneable
{

	private static final long	serialVersionUID	= -1051389401517892258L;
	/**
	 * 发送消息
	 */
	public final static String	SEND_MSG			= "send";
	/**
	 * 接收消息
	 */
	public final static String	RECV_MSG			= "recv";

	// 标记是否为周期消息
	private boolean				periodMsg;

	public XMLBean()
	{

	}

	/**
	 * @param periodMsg the periodMsg to set <br/>
	 */
	public void setPeriodMsg(boolean periodMsg)
	{
		this.periodMsg = periodMsg;
	}

	/**
	 * 判断是否是发送消息
	 * 
	 * @return
	 */
	public boolean isSend()
	{
		if (this.getFieldValue("direction") == null)
			return false;
		return this.getFieldValue("direction").equals(SEND_MSG);
	}

	/**
	 * 判断是否为接收消息
	 * 
	 * @return
	 */
	public boolean isRecv()
	{
		if (this.getFieldValue("direction") == null)
			return false;
		return this.getFieldValue("direction").equals(RECV_MSG);
	}

	/**
	 * 获取消息ID
	 * 
	 * @return
	 */
	public String getId()
	{
		String ID = "";
		if (this instanceof Message)
		{
			ID = ((Message) this).getId();
		}
		return ID;
	}

	/**
	 * 将对象数据转换为xml格式数据。
	 * 
	 * @return </br>
	 */
	public String toXML()
	{
		java.lang.reflect.Field[] fields = this.getClass().getDeclaredFields();
		StringBuilder builder = new StringBuilder();
		EntityRules entityRule = this.getClass().getAnnotation(EntityRules.class);
		if (entityRule == null)
		{
			return "";
		}
		builder.append("<").append(entityRule.xmlName());
		for (java.lang.reflect.Field field : fields)
		{

			FieldRules rule = field.getAnnotation(FieldRules.class);
			field.setAccessible(true);
			if (rule != null && !rule.node())
			{// 节点属性
				try
				{
					String contents = field.get(this) != null ? field.get(this).toString() : "";
					builder.append(" ").append(rule.xmlName()).append("=\"").append(contents).append("\"");
				} catch (IllegalArgumentException e)
				{
					e.printStackTrace();
				} catch (IllegalAccessException e)
				{
					e.printStackTrace();
				}
			}
		}

		builder.append(" >").append("\n");

		for (java.lang.reflect.Field field : fields)
		{
			FieldRules rule = field.getAnnotation(FieldRules.class);
			if (rule != null && rule.node())
			{// 节点
				try
				{
					field.setAccessible(true);
					String contents = field.get(this) != null ? field.get(this).toString() : "";
					builder.append("    <").append(rule.xmlName()).append(">").append(contents).append("</").append(rule.xmlName()).append(">").append("\n");
				} catch (IllegalArgumentException e)
				{
					e.printStackTrace();
				} catch (IllegalAccessException e)
				{
					e.printStackTrace();
				}
			}
		}

		for (Entity child : this.getChildren())
		{
			builder.append("    ").append(((XMLBean) child).toXML());
		}

		builder.append("    </").append(entityRule.xmlName()).append(">").append("\n");

		return builder.toString();
	}

	@Override
	public String toString()
	{

		return this.toXML();
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}

}