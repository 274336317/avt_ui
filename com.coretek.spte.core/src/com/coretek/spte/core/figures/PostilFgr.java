package com.coretek.spte.core.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Rectangle;

import com.coretek.spte.core.InstanceUtils;
import com.coretek.spte.core.models.PostilMdl;

/**
 * __________________________________________________________________________________
 * 
 * @Class PostilFgr.java
 * @Description ע��ͼ��
 * @Auther MENDY
 * @Date 2016-5-16 ����02:22:32
 */
public class PostilFgr extends Shape
{

	private PostilMdl postilMdl;

	public PostilFgr()
	{
		super();
	}

	public PostilFgr(PostilMdl postilMdl)
	{
		super();
		this.postilMdl = postilMdl;
	}

	/**
	 * __________________________________________________________________________________
	 * 
	 * @Class PostilFgr
	 * @Function fillShape
	 * @Description ��ǩλ��
	 * @Auther MENDY
	 * @param graphics
	 *        void
	 * @Date 2016-5-19 ����03:38:35
	 */
	@Override
	protected void fillShape(Graphics graphics)
	{
		Rectangle r = getBounds();
		int x = r.x;
		int y = r.y;
		graphics.setBackgroundColor(InstanceUtils.getInstance().getLableColor());
		graphics.setForegroundColor(InstanceUtils.getInstance().getLableColor());
		graphics.setLineWidth(1);
		if (this.postilMdl.getRightChildrenMdl() != null)
		{
			// graphics.drawLine(460, y, x + 805, y);
			graphics.drawLine(x, y, x + 258, y);
		}
		if (this.postilMdl.getLeftChildrenMdl() != null)
		{
			// graphics.drawLine(x, y, x + 185, y);
			graphics.drawLine(525, y, x + 2905, y);
		}
	}

	@Override
	protected void outlineShape(Graphics graphics)
	{

	}

}
