package com.coretek.spte.core;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.graphics.Color;

public class InstanceUtils
{

	private static InstanceUtils _Instance = null;

	public static InstanceUtils getInstance()
	{
		if (null == _Instance)
		{
			_Instance = new InstanceUtils();
		}
		return _Instance;
	}

	public Color getColor(String m_MesType)
	{
		Color m_Color = ColorConstants.darkGreen;
		;
		if ("��ͨ��Ϣ".equals(m_MesType))
		{
			m_Color = new Color(null, 102, 187, 0);
		}
		if ("������Ϣ".equals(m_MesType))
		{
			m_Color = new Color(null, 85, 153, 221);
		}
		return m_Color;
	}

	/**
	 * __________________________________________________________________________________
	 * 
	 * @Class InstanceUtils
	 * @Function getMesOrBackgroudDefaultColor
	 * @Description ��ȡ��ͨ���߱���������ϢĬ����ɫ
	 * @Auther MENDY
	 * @return
	 *         Color
	 * @Date 2016-5-12 ����05:07:11
	 */
	public Color getMesOrBackgroudDefaultColor()
	{
		return new Color(null, 102, 187, 0);
	}

	/**
	 * __________________________________________________________________________________
	 * 
	 * @Class InstanceUtils
	 * @Function getPeriodOrParallelDefaultColor
	 * @Description ��ȡ���ڻ��߲�����ϢĬ����ɫ
	 * @Auther MENDY
	 * @return
	 *         Color
	 * @Date 2016-5-12 ����05:15:11
	 */
	public Color getPeriodOrParallelDefaultColor()
	{
		return new Color(null, 85, 153, 221);
	}

	/**
	 * __________________________________________________________________________________
	 * 
	 * @Class InstanceUtils
	 * @Function getLableColor
	 * @Description ��ȡ��ǩ������ɫ
	 * @Auther MENDY
	 * @return
	 *         Color
	 * @Date 2016-5-16 ����03:58:57
	 */
	public Color getLableColor()
	{
		return new Color(null, 204, 204, 204);
	}

	/**
	 * __________________________________________________________________________________
	 * 
	 * @Class InstanceUtils
	 * @Function getIntervalColor
	 * @Description ��ȡʱ����������ɫ
	 * @Auther MENDY
	 * @return
	 *         Color
	 * @Date 2016-5-17 ����04:44:55
	 */
	public Color getIntervalColor()
	{
		return new Color(null, 51, 51, 51);
	}

	/**
	 * __________________________________________________________________________________
	 * 
	 * @Class InstanceUtils
	 * @Function getLifelineColor
	 * @Description ��ȡ�ڵ�������������ɫ
	 * @Auther MENDY
	 * @return
	 *         Color
	 * @Date 2016-5-17 ����05:43:44
	 */
	public Color getLifelineColor()
	{
		return new Color(null, 85, 153, 221);
	}

	/**
	 * ��Ϣ����
	 */
	private String _MesType;

	public void setMesType(String _MesType)
	{
		this._MesType = _MesType;
	}

	public String getMesType()
	{
		return _MesType;
	}

}
