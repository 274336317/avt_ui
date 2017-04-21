package com.coretek.spte.core.parts;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;

import com.coretek.common.utils.EclipseUtils;
import com.coretek.spte.core.commands.DirectPostilCommand;
import com.coretek.spte.core.dialogs.PostilDlg;
import com.coretek.spte.core.editor.SPTEEditor;
import com.coretek.spte.core.models.PostilChildMdl;
import com.coretek.spte.core.policies.PostilDirectEditPolicy;

/**
 * 标签子模型的控制器
 * 
 * @author duyisen 2012-3-15
 */
public class PostilChildPart extends AbtPart
{

	protected DirectEditManager	manager;

	@Override
	public IFigure createFigure()
	{
		Label label = new Label();
		label.setMaximumSize(new Dimension(300, 200));
		label.setValid(true);
		label.setCursor(Cursors.HAND);
		label.setLabelAlignment(PositionConstants.TOP);
		label.setTextPlacement(PositionConstants.NORTH);
		label.setText(((PostilChildMdl) this.getModel()).getText());
		Rectangle modeRe = ((PostilChildMdl) this.getModel()).getConstraints();
		Rectangle rectangle = new Rectangle(modeRe.x, modeRe.y, modeRe.width + 20, modeRe.height + 10);
		label.setBounds(rectangle);
		return label;
	}

	public IFigure getFigure()
	{
		IFigure fgr = super.getFigure();
		return fgr;
	}

	protected void refreshVisuals()
	{
		super.refreshVisuals();
		PostilChildMdl postilChildModel = (PostilChildMdl) getModel();
		((GraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), postilChildModel.getConstraints());
	}

	@Override
	public void activate()
	{
		super.activate();
		((PostilChildMdl) getModel()).addPropertyChangeListener(this);
	}

	@Override
	public void deactivate()
	{
		((PostilChildMdl) getModel()).removePropertyChangeListener(this);
		super.deactivate();
	}

	public void propertyChange(PropertyChangeEvent evt)
	{
		if (evt.getPropertyName().equals(PostilChildMdl.P_CONSTRAINT))
		{
			refreshVisuals();
		} else if (evt.getPropertyName().equals(PostilChildMdl.P_TEXT))
		{
			Label label = (Label) getFigure();
			label.setTextPlacement(PositionConstants.NORTH);
			label.setTextAlignment(PositionConstants.LEFT);
			String txt = ((PostilChildMdl) getModel()).getText();
			label.setText(txt);
			label.setBounds(((PostilChildMdl) getModel()).getConstraints());
			label.invalidate();
		}

	}

	@Override
	protected void createEditPolicies()
	{
		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new PostilDirectEditPolicy());
	}

	public void performRequest(Request request)
	{
		PostilChildMdl mdl = (PostilChildMdl) getModel();
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT || request.getType() == RequestConstants.REQ_OPEN)
		{
			PostilDlg dlg = new PostilDlg(Display.getCurrent().getActiveShell(), (PostilChildMdl) getModel());
			if (dlg.open() != Window.CANCEL)
			{
				if (dlg.getText().equals(mdl.getText()))
				{
					return;
				}
				DirectPostilCommand cmd = new DirectPostilCommand((PostilChildMdl) getModel(), dlg.getText());
				SPTEEditor editor = (SPTEEditor) EclipseUtils.getActiveEditor();
				editor.getEditDomain().getCommandStack().execute(cmd);
			}
		}
	}

	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection)
	{
		return null;
	}

	public ConnectionAnchor getSourceConnectionAnchor(Request request)
	{
		return null;
	}

	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection)
	{
		return null;
	}

	public ConnectionAnchor getTargetConnectionAnchor(Request request)
	{
		return null;
	}

}