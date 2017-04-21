package com.coretek.spte.core.parts;

import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;

import com.coretek.spte.core.SPTEPlugin;
import com.coretek.spte.core.dialogs.NewPeriodMsgDlg;
import com.coretek.spte.core.figures.PeriodChildMsgFgr;
import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.AbtCycleMsgMdl;
import com.coretek.spte.core.models.PeriodChildMsgMdl;
import com.coretek.spte.core.models.PeriodParentMsgMdl;
import com.coretek.spte.core.models.TestMdl;
import com.coretek.spte.core.models.TestNodeContainerMdl;
import com.coretek.spte.core.models.TestNodeMdl;
import com.coretek.spte.core.models.TestToolMdl;
import com.coretek.spte.core.policies.PeriodChildMsgPolicy;
import com.coretek.spte.core.tools.ConnMouseMotionListener;
import com.coretek.spte.core.util.SPTEConstants;
import com.coretek.spte.core.util.Utils;

/**
 * 周期消息的子连线控制器
 * 
 * @author 孙大巍
 * @date 2010-8-30
 * 
 */
public class PeriodChildMsgPart extends AbtPeriodMsgPart
{	
	public PeriodChildMsgPart()
	{

	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#performRequest(org.eclipse.gef.Request)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void performRequest(Request req)
	{
		PeriodParentMsgMdl model = (PeriodParentMsgMdl)((PeriodChildMsgMdl) this.getModel()).getParent();
		TestNodeMdl item = (TestNodeMdl) model.getSource();
		TestMdl stepMode = (TestMdl) ((TestNodeContainerMdl) item.getParent()).getParent();

		if (!Utils.isSystemReady(item))
		{// 被测对象未被设置，则不允许对消息进行编辑
			return;
		}
		if (Utils.isEditingStatus())
		{
			TestNodePart part = (TestNodePart)this.getSource();
			int index = part.getParent().getChildren().indexOf(part);
			index = index -2;
			part = (TestNodePart)part.getParent().getChildren().get(index);
			List list2 = part.getSourceConnections();
			PeriodParentMsgPart parentPart = null;
			for(Object obj: list2)
			{
				if(obj instanceof PeriodParentMsgPart)
				{
					parentPart = (PeriodParentMsgPart)obj;
				}
			}
			TestNodeMdl tn = (TestNodeMdl)part.getModel();
			List<AbtConnMdl> list = tn.getAllOutgoings();
			PeriodParentMsgMdl parent = null;
			for(AbtConnMdl mdl: list)
			{
				if(mdl instanceof PeriodParentMsgMdl)
				{
					parent = (PeriodParentMsgMdl)mdl;
					break;
				}
			}
			TitleAreaDialog	dialog = null;
			if (stepMode instanceof TestToolMdl)
			{
				dialog = new NewPeriodMsgDlg(SPTEPlugin.getActiveWorkbenchShell(), ((AbtConnMdl) parent), parentPart, SPTEConstants.MESSAGE_TYPE_SEND);
			} else
			{
				dialog = new NewPeriodMsgDlg(SPTEPlugin.getActiveWorkbenchShell(), ((AbtConnMdl) parent), parentPart, SPTEConstants.MESSAGE_TYPE_RECEIVE);
			}

			if (dialog.open() == Window.OK)
			{
				((NewPeriodMsgDlg) dialog).setName();
			}
		}
	}

	@Override
	protected void createEditPolicies()
	{
		// 支持连线选中
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
		// 支持删除周期连线
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new PeriodChildMsgPolicy());
	}

	@Override
	public IFigure createFigure()
	{
		if (((AbtConnMdl) this.getModel()).getName() == null || ((AbtConnMdl) this.getModel()).getName().trim().length() == 0)
		{
			((AbtConnMdl) this.getModel()).setName("周期消息");
		}
		PeriodChildMsgFgr figure = new PeriodChildMsgFgr((AbtConnMdl) this.getModel());
		figure.addMouseMotionListener(new ConnMouseMotionListener((AbtConnMdl) this.getModel()));
		return figure;
	}

	@Override
	public void setSelected(int value)
	{
		super.setSelected(value);

		if (value != EditPart.SELECTED_NONE)
		{
			((AbtCycleMsgMdl) this.getModel()).setColor(ColorConstants.orange);
			((PeriodParentMsgMdl) ((PeriodChildMsgMdl) this.getModel()).getParent()).setColor(ColorConstants.orange);
			((PeriodParentMsgMdl) ((PeriodChildMsgMdl) this.getModel()).getParent()).setDefaultColor(ColorConstants.orange);
			((AbtCycleMsgMdl) this.getModel()).setDefaultColor(ColorConstants.orange);
		} else
		{
			((PeriodParentMsgMdl) ((PeriodChildMsgMdl) this.getModel()).getParent()).setDefaultColor(ColorConstants.darkGreen);
			((AbtCycleMsgMdl) this.getModel()).setDefaultColor(ColorConstants.darkGreen);
			((AbtCycleMsgMdl) this.getModel()).setColor(((AbtCycleMsgMdl) this.getModel()).getDefaultColor());
			((PeriodParentMsgMdl) ((PeriodChildMsgMdl) this.getModel()).getParent()).setColor(((PeriodParentMsgMdl) ((PeriodChildMsgMdl) this.getModel()).getParent()).getDefaultColor());
		}
	}
}
