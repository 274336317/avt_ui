/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.testcase.testResult;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.coretek.spte.dataCompare.CompareResult;

/**
 * @author SunDawei 2012-6-6
 */
public class TableLabelProvider extends LabelProvider implements ITableLabelProvider
{

	private Image	pastImage;

	private Image	failedImage;

	public TableLabelProvider(Image pastImage, Image failedImage)
	{
		this.pastImage = pastImage;
		this.failedImage = failedImage;
	}

	public Image getColumnImage(Object element, int columnIndex)
	{
		if (columnIndex == 1)
		{
			CompareResult result = (CompareResult) element;
			if (result.isStatus())
			{
				return pastImage;
			}
			return failedImage;
		}
		return null;
	}

	public String getColumnText(Object element, int columnIndex)
	{
		CompareResult result = (CompareResult) element;
		switch (columnIndex)
		{
			case 0:
			{
				return "";
			}
			case 1:
			{
				return "";
			}
			case 2:
			{

				return result.getCasePath();
			}
			case 3:
			{

				return result.getProjectName();
			}
			case 4:
			{
				if (!result.isStatus())
				{
					return "���н���д�" + "" + "��ʧ����:" + result.getAllLostCount() + ",��ʱ����:" + result.getTimeOut() + ",����ֵ����:" + result.getErrorValue() + ",δ��������:" + result.getUnexpected();

				} else
					return "��";
			}

		}
		return "";
	}

}
