/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.testcase.curve.internal.model;

/**
 * ���߳���
 * 
 * @author ���� 2012-3-14
 */
public final class CurveConst
{
	// �����߶�
	public static int					CANVAS_HEIGHT			= 550;

	// �������
	public static int					CANVAS_WIDTH			= 1080;

	// ���߳���ʵ��,����
	private volatile static CurveConst	data;

	// X��ĳ���
	public static int					X_AXIS_LENGTH			= 1000;

	// Y�����ƫ��
	public final static int				Y_AXIS_V_BOTTOM_OFFSET	= 30;

	// Y�����ƫ��
	public final static int				Y_AXIS_V_TOP_OFFSET		= 10;

	public static CurveConst getInstance()
	{
		if (data == null)
		{
			data = new CurveConst();
		}
		return data;
	}

	/**
	 * ��������
	 */
	private double	scale	= 1.0;

	private CurveConst()
	{

	}

	public synchronized double getScale()
	{
		return scale;
	}

	public synchronized void reset()
	{
		this.scale = 1.0;
	}

	public synchronized void setScale(double scale)
	{
		this.scale = scale * this.scale;
	}

}