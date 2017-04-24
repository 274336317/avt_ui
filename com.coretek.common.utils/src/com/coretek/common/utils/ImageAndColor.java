/**
 * 
 */
package com.coretek.common.utils;

import org.eclipse.swt.graphics.Color;

/**
 * __________________________________________________________________________________
 * 
 * @Class ImageAndColor.java
 * @Description
 * @Auther MENDY
 * @Date 2016-5-30 ����03:12:54
 */
public class ImageAndColor {

	private static ImageAndColor _instance;

	synchronized public static ImageAndColor getDefault() {
		if (_instance == null) {
			_instance = new ImageAndColor();
		}
		return _instance;
	}

	/**
	 * __________________________________________________________________________________
	 * 
	 * @Class ImageAndColor
	 * @Function getColor
	 * @Description
	 * @Auther MENDY
	 * @param which
	 *            1-��ɫ��2-���ɫ��3-��ɫ��4-��ɫ��5-��ɫ
	 * @return Color
	 * @Date 2016-5-30 ����03:34:33
	 */
	public Color getColor(final int which) {
		Color m_Color = null;
		switch (which) {
		    case 1:
				m_Color = _DBE2E8;
				break;
			case 2:
				m_Color = _6C96C0;
				break;
			case 3:
				m_Color = _RED;
				break;
			case 4:
				m_Color = _BLUE;
				break;
			case 5:
				m_Color = _BLACK;
				break;
			default:
				m_Color = _WHITE;
		}
		return m_Color;
	}

	/**
	 * ��ɫ
	 */
	protected Color _DBE2E8 = new Color(null, 219, 226, 232);
	/**
	 * ���ɫ
	 */
	protected Color _6C96C0 = new Color(null, 108, 150, 192);
	/**
	 * ��ɫ
	 */
	protected Color _RED = new Color(null, 255, 0, 0);
	/**
	 * ��ɫ
	 */
	protected Color _BLUE = new Color(null, 0, 0, 255);
	/**
	 * ��ɫ
	 */
	protected Color _BLACK = new Color(null, 0, 0, 0);
	/**
	 * ��ɫ
	 */
	protected Color _WHITE = new Color(null, 255, 255, 255);

}
