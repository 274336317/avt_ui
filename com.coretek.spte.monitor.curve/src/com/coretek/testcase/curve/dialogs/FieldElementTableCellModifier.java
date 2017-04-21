/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.testcase.curve.dialogs;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Item;

import com.coretek.common.utils.StringUtils;
import com.coretek.testcase.curve.internal.model.FieldElementSet;

/**
 * 表元素修改器
 * 
 * @author 尹军 2012-3-14
 */
public class FieldElementTableCellModifier implements ICellModifier
{
	private TableViewer	viewer;

	public FieldElementTableCellModifier(TableViewer viewer)
	{
		this.viewer = viewer;
	}

	public boolean canModify(Object element, String property)
	{
		if (element instanceof FieldElementSet)
		{
			if (this.isMinValue(property) || this.isMaxValue(property) || this.isLineType(property))
			{
				return true;
			}
		}
		return false;
	}

	public Object getValue(Object element, String property)
	{
		if (element instanceof FieldElementSet)
		{
			FieldElementSet item = (FieldElementSet) element;
			if (property.equalsIgnoreCase(ConfigFieldsPropertyDialog.ATTR_MIN_VALUE))
			{// 最小值
				return Integer.toString(item.getMinValue());
			} else if (property.equalsIgnoreCase(ConfigFieldsPropertyDialog.ATTR_MAX_VALUE))
			{// 最大值
				return Integer.toString(item.getMaxValue());
			} else if (property.equalsIgnoreCase(ConfigFieldsPropertyDialog.ATTR_LINE_TYPE))
			{// 连线类型
				return item.getLineType();
			}

		}
		return null;
	}

	public void modify(Object element, String property, Object value)
	{
		Object obj = new Object();
		if (element instanceof Item)
		{
			obj = ((Item) element).getData();
		}
		if (obj instanceof FieldElementSet)
		{
			FieldElementSet item = (FieldElementSet) obj;
			if (this.isMinValue(property))
			{// 最小值
				if (StringUtils.isNotNull((String) value))
				{
					item.setMinValue(Integer.parseInt(((String) value).trim()));
				}
			} else if (this.isMaxValue(property))
			{// 最大值
				if (StringUtils.isNotNull((String) value))
				{
					item.setMaxValue(Integer.parseInt(((String) value).trim()));
				}
			} else if (this.isLineType(property))
			{// 连线类型
				int comboIndex = ((Integer) value).intValue();
				item.setLineType(comboIndex);
			}
		}

		viewer.refresh();
	}

	/**
	 * 判断属性是否为最小值
	 * 
	 * @param property
	 * @return
	 */
	private boolean isMinValue(String property)
	{
		return property.equalsIgnoreCase(ConfigFieldsPropertyDialog.ATTR_MIN_VALUE);
	}

	/**
	 * 判断属性是否为最大值
	 * 
	 * @param property
	 * @return
	 */
	private boolean isMaxValue(String property)
	{
		return property.equalsIgnoreCase(ConfigFieldsPropertyDialog.ATTR_MAX_VALUE);
	}

	/**
	 * 判断属性是否为连线类型
	 * 
	 * @param property
	 * @return
	 */
	private boolean isLineType(String property)
	{
		return property.equalsIgnoreCase(ConfigFieldsPropertyDialog.ATTR_LINE_TYPE);
	}
}
