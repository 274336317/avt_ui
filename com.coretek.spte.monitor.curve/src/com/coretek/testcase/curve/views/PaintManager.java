/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.testcase.curve.views;

/**
 * ��ͼ�����������ݵ�ǰ������ͼ����ʼʱ���������ʱ�������ǰʱ������Ƶ�ǰ֡������ͼ��
 * 
 * @author ���� 2012-3-14
 */
public class PaintManager
{

	private CurveView	viewPart;

	public PaintManager(CurveView viewPart)
	{
		this.viewPart = viewPart;
	}

	/**
	 * ���ݵ�ǰ������ͼ����ʼʱ���������ʱ�������ǰʱ������Ƶ�ǰ֡������ͼ
	 */
	public void paint()
	{
		if (viewPart.getCanvas() == null || viewPart.getCanvas().isDisposed())
		{
			return;
		}
		viewPart.getCanvas().getDisplay().syncExec(new Runnable()
		{
			public void run()
			{
				viewPart.getContainer().repaintScale();
			}
		});
	}
}
