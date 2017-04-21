/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.testcase.testcaseview;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.coretek.common.utils.StringUtils;

/**
 * @author SunDawei 2012-6-7
 */
public class TableLabelProvider extends LabelProvider implements ITableLabelProvider
{

	public Image getColumnImage(Object element, int columnIndex)
	{
		return null;
	}

	public String getColumnText(Object element, int columnIndex)
	{
		TestCase file = (TestCase) element;
		switch (columnIndex)
		{
			case 0:
			{
				return StringUtils.EMPTY_STRING;
			}
			case 1:
			{// ����������ʶ
				return file.getCaseName();
			}
			case 2:
			{// �����б�
				if (file.getGroup() == null)
					return StringUtils.EMPTY_STRING;
				else
					return file.getGroup().getName();
			}
			case 3:
			{// ���Լ�·��
				return file.getSuitePath();
			}
			case 4:
			{// ������
				return file.getProjectName();
			}

		}
		return StringUtils.EMPTY_STRING;
	}

}
