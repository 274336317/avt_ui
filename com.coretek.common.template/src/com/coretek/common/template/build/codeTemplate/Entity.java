/************************************************************************
 *				北京科银京成技术有限公司 版权所有
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
 * 代表规则文件中的Entity节点
 * 
 * @author 孙大巍 2011-12-23
 */
public abstract class Entity implements Serializable
{

	private static final long	serialVersionUID	= -6436045747693435322L;

	private Entity				parent;										// 从xml
	// 元素上获取的父节点

	private String				xmlName;										// 对应xml上的节点名

	private String				xpath;											// 节点的xpath路径

	private List<Entity>		children			= new ArrayList<Entity>();	// 子节点

	private List<Entity>		logicChildren		= new ArrayList<Entity>();	// 逻辑子节点

	/**
	 * 获取所有的属性
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
	 * 获取属性的值
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
	 * 添加子节点
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
	 * 获取ICD节点的逻辑子节点
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
	 * 获取属性的值
	 * 
	 * @param fieldName 字段名
	 * @return 字段值
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
	 * 设置属性的值
	 * 
	 * @param fieldName 字段名
	 * @param value 字段值</br>
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