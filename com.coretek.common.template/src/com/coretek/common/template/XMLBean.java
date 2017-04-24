/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template;

import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.common.template.build.codeTemplate.EntityRules;
import com.coretek.common.template.build.codeTemplate.FieldRules;
import com.coretek.spte.testcase.Message;

/**
 * ��Ҫ�����ڽ����������еĸ��ֶ�������xml��ʽ����Ϣ��
 * 
 * @author ���Ρ 2011-12-29
 */
public class XMLBean extends Entity implements Cloneable
{

	private static final long	serialVersionUID	= -1051389401517892258L;
	/**
	 * ������Ϣ
	 */
	public final static String	SEND_MSG			= "send";
	/**
	 * ������Ϣ
	 */
	public final static String	RECV_MSG			= "recv";

	// ����Ƿ�Ϊ������Ϣ
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
	 * �ж��Ƿ��Ƿ�����Ϣ
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
	 * �ж��Ƿ�Ϊ������Ϣ
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
	 * ��ȡ��ϢID
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
	 * ����������ת��Ϊxml��ʽ���ݡ�
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
			{// �ڵ�����
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
			{// �ڵ�
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