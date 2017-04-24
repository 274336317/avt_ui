/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template.build.codeTemplate;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.coretek.common.template.Attribute;
import com.coretek.common.template.build.file.FieldTypes;

/**
 * ��������ļ��е�Entity�ڵ�
 * 
 * @author ���Ρ 2011-12-23
 */
public abstract class Entity implements Serializable
{

	private static final long	serialVersionUID	= -6436045747693435322L;

	private Entity				parent;										// ��xml
	// Ԫ���ϻ�ȡ�ĸ��ڵ�

	private String				xmlName;										// ��Ӧxml�ϵĽڵ���

	private String				xpath;											// �ڵ��xpath·��

	private List<Entity>		children			= new ArrayList<Entity>();	// �ӽڵ�

	private List<Entity>		logicChildren		= new ArrayList<Entity>();	// �߼��ӽڵ�

	/**
	 * ��ȡ���е�����
	 * 
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public List<Attribute> getAttributes() throws IllegalArgumentException, IllegalAccessException
	{
		List<Attribute> attributes = new ArrayList<Attribute>();
		Field[] fields = this.getClass().getDeclaredFields();
		for (Field field : fields)
		{
			field.setAccessible(true);
			FieldRules fr = field.getAnnotation(FieldRules.class);
			if (fr == null)
				continue;
			Attribute attribute = new Attribute(field.get(this), FieldTypes.getFieldType(fr.type()), field.getName(), fr.xmlName());
			attributes.add(attribute);

		}

		return attributes;
	}

	/**
	 * ��ȡ���Ե�ֵ
	 * 
	 * @param attributeName
	 * @return
	 */
	public Attribute getAttribute(String attributeName)
	{
		Field[] fields = this.getClass().getDeclaredFields();
		for (Field field : fields)
		{
			if (field.getName().equals(attributeName))
			{
				field.setAccessible(true);
				FieldRules fr = field.getAnnotation(FieldRules.class);
				Attribute attribute = null;
				try
				{
					attribute = new Attribute(field.get(this), FieldTypes.getFieldType(fr.type()), field.getName(), fr.xmlName());
				} catch (IllegalArgumentException e)
				{
					e.printStackTrace();
				} catch (IllegalAccessException e)
				{
					e.printStackTrace();
				}
				return attribute;
			}

		}

		return null;
	}

	public List<Entity> getLogicChildren()
	{
		return logicChildren;
	}

	public String getXpath()
	{
		return xpath;
	}

	public void setXpath(String xpath)
	{
		this.xpath = xpath;
	}

	public Entity getParent()
	{

		return parent;
	}

	public String getXmlName()
	{
		return xmlName;
	}

	public void setXmlName(String xmlName)
	{
		this.xmlName = xmlName;
	}

	public void setParent(Entity parent)
	{
		this.parent = parent;
		if (parent != null && parent.getChildren() != null && parent.getChildren().indexOf(this) < 0)
			parent.addChild(this);
	}

	public List<Entity> getChildren()
	{
		return children;
	}

	public void setChildren(List<Entity> children)
	{

		this.children = children;
	}

	/**
	 * ����ӽڵ�
	 * 
	 * @param entity
	 */
	public void addChild(Entity entity)
	{

		this.children.add(entity);
	}

	public void addLogicChild(Entity logicChild)
	{
		this.logicChildren.add(logicChild);
	}

	/**
	 * ��ȡICD�ڵ���߼��ӽڵ�
	 * 
	 * @return
	 */
	public Entity getLogicParent()
	{

		return null;
	}

	public void setLogicParent(Entity logicParent)
	{
		logicParent.addLogicChild(this);
	}

	public String getXPath()
	{
		if (this.parent == null)
		{
			return "/" + this.xpath;
		}

		return this.parent.getXPath() + "/" + this.xpath;
	}

	/**
	 * ��ȡ���Ե�ֵ
	 * 
	 * @param fieldName �ֶ���
	 * @return �ֶ�ֵ
	 */
	public Object getFieldValue(String fieldName)
	{
		Class clazz = this.getClass();
		Field field;
		try
		{
			field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(this);
		} catch (SecurityException e)
		{
			e.printStackTrace();
		} catch (NoSuchFieldException e)
		{
			e.printStackTrace();
		} catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * �������Ե�ֵ
	 * 
	 * @param fieldName �ֶ���
	 * @param value �ֶ�ֵ</br>
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public void setFieldValue(String fieldName, Object value) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException
	{
		Class clazz = this.getClass();
		Field field = clazz.getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(this, value);
	}

	@Override
	public String toString()
	{
		Field[] fields = this.getClass().getDeclaredFields();
		StringBuilder builder = new StringBuilder();
		for (Field field : fields)
		{
			field.setAccessible(true);
			try
			{
				builder.append(field.getName()).append("=").append(field.get(this)).append(";\n");
			} catch (IllegalArgumentException e)
			{
				e.printStackTrace();
			} catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
		return builder.toString();
	}

}