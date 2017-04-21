package com.coretek.spte.core.parts;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;

import com.coretek.spte.core.InstanceUtils;
import com.coretek.spte.core.SPTEPlugin;
import com.coretek.spte.core.dialogs.NewMsgDlg;
import com.coretek.spte.core.figures.MsgFgr;
import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.MsgConnMdl;
import com.coretek.spte.core.models.TestMdl;
import com.coretek.spte.core.models.TestNodeContainerMdl;
import com.coretek.spte.core.models.TestNodeMdl;
import com.coretek.spte.core.models.TestToolMdl;
import com.coretek.spte.core.tools.ConnMouseMotionListener;
import com.coretek.spte.core.util.SPTEConstants;
import com.coretek.spte.core.util.TestCaseStatus;

/**
 * 消息连线控制器
 * 
 * @author 孙大巍
 * @date 2010-8-18
 * 
 */
public class MsgConnPart extends AbtConnPart
{
	protected TitleAreaDialog	dialog;

	protected TitleAreaDialog	dialog2;

	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		String pName = evt.getPropertyName();
		MsgConnMdl model = (MsgConnMdl) this.getModel();
		if (pName.equals(AbtConnMdl.PROP_NAME) || pName.equals(SPTEConstants.EVENT_PROP_RESULT_MESSAGE_CHANGED))
		{
			refreshVisuals();
		} else if (pName.equals(SPTEConstants.CHANGE_MESSAGE_REQUEST_TYPE))
		{
			refreshVisuals();
		} else if (pName.equals(AbtConnMdl.PROP_COLOR))
		{
			this.refreshVisuals();
		} else if (pName.equals(SPTEConstants.PROP_FAILED_STATUS_CHANGED))
		{
			boolean failed = (Boolean) evt.getNewValue();
			if (failed)
			{
				model.setColor(ColorConstants.red);// 执行失败
			} else
			{
				model.setColor(model.getDefaultColor());// 执行成功
			}
		} else if (pName.equals(SPTEConstants.EVENT_STATUS_CHANGED))
		{
			if (TestCaseStatus.Debug == evt.getNewValue())
			{// 调试状态
				model.setColor(SPTEConstants.COLOR_DEBUG);
			} else if (TestCaseStatus.Editing == evt.getNewValue())
			{// 编辑状态
				model.setColor(model.getDefaultColor());
			}
		}
	}

	@Override
	public void performRequest(Request req)
	{
		super.performRequest(req);
		TestNodeMdl item = (TestNodeMdl) ((AbtConnMdl) this.getModel()).getSource();
		TestMdl stepMode = (TestMdl) ((TestNodeContainerMdl) item.getParent()).getParent();
		if (stepMode instanceof TestToolMdl)
		{
			// this.dialog2 = new MsgDlg(SPTEPlugin.getActiveWorkbenchShell(),
			// ((AbtConnMdl) this.getModel()),
			// this,SPTEConstants.MESSAGE_TYPE_SEND);
			this.dialog2 = new NewMsgDlg(SPTEPlugin.getActiveWorkbenchShell(), ((AbtConnMdl) this.getModel()), this, SPTEConstants.MESSAGE_TYPE_SEND);
		} else
		{
			// this.dialog2 = new MsgDlg(SPTEPlugin.getActiveWorkbenchShell(),
			// ((AbtConnMdl) this.getModel()),
			// this,SPTEConstants.MESSAGE_TYPE_RECEIVE);
			this.dialog2 = new NewMsgDlg(SPTEPlugin.getActiveWorkbenchShell(), ((AbtConnMdl) this.getModel()), this, SPTEConstants.MESSAGE_TYPE_RECEIVE);
		}

		if (this.dialog2.open() == Window.OK)
		{
			((NewMsgDlg) dialog2).setName();
		}
	}

	protected IFigure createFigure()
	{
		MsgFgr figure = new MsgFgr((AbtConnMdl) this.getModel());
		figure.addMouseMotionListener(new ConnMouseMotionListener((AbtConnMdl) this.getModel()));
		return figure;
	}

	@Override
	public void setSelected(int value)
	{
		super.setSelected(value);
		if (value != EditPart.SELECTED_NONE)
		{
			((AbtConnMdl) this.getModel()).setColor(ColorConstants.orange);
			((AbtConnMdl) this.getModel()).setDefaultColor(ColorConstants.orange);
		} else
		{
			//((AbtConnMdl) this.getModel()).setDefaultColor(ColorConstants.darkGreen);
			((AbtConnMdl) this.getModel()).setDefaultColor(InstanceUtils.getInstance().getMesOrBackgroudDefaultColor());
			((AbtConnMdl) this.getModel()).setColor(((AbtConnMdl) this.getModel()).getDefaultColor());
		}
	}
}