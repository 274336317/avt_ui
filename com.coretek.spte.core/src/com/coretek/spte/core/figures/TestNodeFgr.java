package com.coretek.spte.core.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

import com.coretek.spte.core.InstanceUtils;
import com.coretek.spte.core.models.TestNodeMdl;
import com.coretek.spte.core.util.SPTEConstants;
import com.coretek.spte.core.util.TestCaseStatus;

/**
 * �����ߵĽڵ���ͼ
 * 
 * @author ���Ρ
 * @date 2010-8-21
 */
public class TestNodeFgr extends Shape
{

	private Label label;

	private TestNodeMdl model;

	private Color color = new Color(null, 246, 246, 246);

	public TestNodeFgr()
	{
		super();
		init();
	}

	public TestNodeFgr(TestNodeMdl model)
	{
		this.model = model;
		init();
	}

	public void init()
	{
		createLabel();
		this.add(label);
	}

	public void createLabel()
	{
		label = new Label();
		label.setText(model.getName());
		label.setTextPlacement(PositionConstants.NORTH);
		label.setTextAlignment(PositionConstants.LEFT);
	}

	/**
	 * __________________________________________________________________________________
	 * 
	 * @Class TestNodeFgr
	 * @Function getLeftAnchorLoc
	 * @Description ��ȡ��ߵ�ê��λ��
	 * �����޸���Ϣ��������λ��
	 * @Auther MENDY
	 * @return
	 *         Point
	 * @Date 2016-5-17 ����05:40:52
	 */
	public Point getLeftAnchorLoc()
	{
		Point rst = Point.SINGLETON;
		 rst.x = this.getBounds().x;
		 rst.y = this.getBounds().y + 11;
		return rst;
	}

	/**
	 * __________________________________________________________________________________
	 * 
	 * @Class TestNodeFgr
	 * @Function getRightAnchorLoc
	 * @Description ��ȡ�ұߵ�ê��λ��
	 * @Auther MENDY
	 * @return
	 *         Point
	 * @Date 2016-5-17 ����05:40:42
	 */
	public Point getRightAnchorLoc()
	{
		Point rst = Point.SINGLETON;
		 rst.x = this.label.getTextBounds().x + 3;
		 rst.y = this.label.getTextBounds().y + 17;
		return rst;
	}

	@Override
	protected void fillShape(Graphics graphics)
	{
	}

	@Override
	protected void outlineShape(Graphics graphics)
	{
	}

	@Override
	public void paint(Graphics graphics)
	{
		if (!this.model.isParent())
		{
			graphics.pushState();
			Rectangle bound = this.getBounds().getCopy();
			bound.height -= 3;// ÿ�������߼�����
			bound.width -= 4; // ÿ�������ߵĿ��
			if (this.model.isSelected() || this.model.getStatus() == TestCaseStatus.Debug)
			{
				graphics.setBackgroundColor(SPTEConstants.COLOR_DEBUG);
				graphics.setForegroundColor(SPTEConstants.COLOR_DEBUG);
			}
			else
			{
				graphics.setBackgroundColor(InstanceUtils.getInstance().getLifelineColor());
				graphics.setForegroundColor(InstanceUtils.getInstance().getLifelineColor());
			}
			graphics.fillGradient(bound, false);
			graphics.setLineStyle(SWT.LINE_SOLID);
			graphics.popState();
		}
		super.paint(graphics);
	}

	@Override
	public void setBounds(Rectangle rect)
	{
		rect.y -= 12; 
		super.setBounds(rect);
	}

	public void setName(String text)
	{
		this.label.setText(text);
	}

	public void dispose()
	{
		this.label = null;
		this.model = null;
		this.color.dispose();
		this.color = null;
	}
}
