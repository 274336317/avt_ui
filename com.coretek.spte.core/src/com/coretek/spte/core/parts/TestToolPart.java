package com.coretek.spte.core.parts;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.spte.core.figures.TestToolBorderAnchor;
import com.coretek.spte.core.figures.TestToolFgr;
import com.coretek.spte.core.models.AbtNode;
import com.coretek.spte.core.models.TestMdl;
import com.coretek.spte.core.models.TestToolMdl;
import com.coretek.spte.core.tools.DisplayNodeNameListener;
import com.coretek.spte.core.tools.TestMdlMouseMotionListener;

/**
 * ���Թ���
 * 
 * @author ���Ρ
 * @date 2011-2-19
 */
public class TestToolPart extends TestPart
{
	private FlowPage	flowPage;

	private TextFlow	textFlow;

	/**
	 * ������ͼ
	 */
	@Override
	protected IFigure createFigure()
	{
		this.flowPage = new FlowPage();
		this.textFlow = new TextFlow();
		this.flowPage.setOpaque(false);
		this.flowPage.add(this.textFlow);
		IFigure figure = new TestToolFgr((TestToolMdl) this.getModel());
		figure.addMouseMotionListener(new TestMdlMouseMotionListener());

		figure.setToolTip(this.flowPage);
		String title = Messages.getString("I18N_TEST_TOOL_NODES");
		figure.addMouseMotionListener(new DisplayNodeNameListener((TestMdl) getModel(), textFlow, title));
		return figure;
	}

	@Override
	public void refreshVisuals()
	{
		refreshParent();
		TestToolFgr nf = (TestToolFgr) this.getFigure();
		TestToolMdl node = (TestToolMdl) getModel();
		Point loc = node.getLocation();
		Dimension size = node.getSize();
		Rectangle rectangle = new Rectangle(loc, size);

		nf.setName(((AbtNode) this.getModel()).getName());
		((GraphicalEditPart) getParent()).setLayoutConstraint(this, nf, rectangle);
		node.setRootLocation(nf.getLocation());
	}

	public void refreshParent()
	{
		this.getParent().refresh();
	}

	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection)
	{
		return new TestToolBorderAnchor(getFigure());
	}

	/**
	 * ��ȡ���ӵ�Դê��
	 */
	public ConnectionAnchor getSourceConnectionAnchor(Request request)
	{
		return new TestToolBorderAnchor(getFigure());
	}

	/**
	 * ��ȡ���ӵ�Ŀ��ê��
	 */
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection)
	{
		return new TestToolBorderAnchor(getFigure());
	}

	public ConnectionAnchor getTargetConnectionAnchor(Request request)
	{
		return new TestToolBorderAnchor(getFigure());
	}
}