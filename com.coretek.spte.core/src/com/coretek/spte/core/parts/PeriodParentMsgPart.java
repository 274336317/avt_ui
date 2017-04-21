package com.coretek.spte.core.parts;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.spte.core.InstanceUtils;
import com.coretek.spte.core.SPTEPlugin;
import com.coretek.spte.core.dialogs.NewPeriodMsgDlg;
import com.coretek.spte.core.figures.PeriodParentMsgFgr;
import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.AbtCycleMsgMdl;
import com.coretek.spte.core.models.PeriodParentMsgMdl;
import com.coretek.spte.core.models.TestMdl;
import com.coretek.spte.core.models.TestNodeContainerMdl;
import com.coretek.spte.core.models.TestNodeMdl;
import com.coretek.spte.core.models.TestToolMdl;
import com.coretek.spte.core.policies.ConnSelectMsgPolicy;
import com.coretek.spte.core.policies.PeriodParentMsgPolicy;
import com.coretek.spte.core.tools.ConnMouseMotionListener;
import com.coretek.spte.core.util.SPTEConstants;
import com.coretek.spte.core.util.Utils;

/**
 * ������Ϣ�ĸ����߿�����
 * 
 * @author ���Ρ
 * @date 2010-8-30
 * 
 */
/**
 * __________________________________________________________________________________
 * 
 * @Class PeriodParentMsgPart.java
 * @Description ������Ϣ���Ӳ���
 * @Auther MENDY
 * @Date 2016-5-17 ����03:39:09
 */
public class PeriodParentMsgPart extends AbtPeriodMsgPart
{

	private TitleAreaDialog dialog2;

	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		String pName = evt.getPropertyName();
		if (pName.equals(AbtConnMdl.PROP_NAME) || pName.equals(SPTEConstants.EVENT_PROP_RESULT_MESSAGE_CHANGED))
		{
			refreshVisuals();
		}
		else if (pName.equals(SPTEConstants.CHANGE_MESSAGE_REQUEST_TYPE))
		{
			refreshVisuals();
		}
		else if (pName.equals(AbtConnMdl.PROP_COLOR))
		{
			this.refreshVisuals();
		}
		else if (pName.equals(SPTEConstants.PROP_FAILED_STATUS_CHANGED))
		{
			boolean failed = (Boolean) evt.getNewValue();
			AbtConnMdl model = (AbtConnMdl) this.getModel();
			if (failed)
			{
				// ִ��ʧ��
				model.setColor(ColorConstants.red);
				model.setDefaultColor(ColorConstants.red);
			}
			else
			{
				// ִ�гɹ�
				model.setColor(model.getDefaultColor());
			}
		}
	}

	@Override
	public void performRequest(Request req)
	{
		PeriodParentMsgMdl model = (PeriodParentMsgMdl) this.getModel();
		TestNodeMdl item = (TestNodeMdl) model.getSource();
		TestMdl stepMode = (TestMdl) ((TestNodeContainerMdl) item.getParent()).getParent();

		if (!Utils.isSystemReady(item))
		{
			// �������δ�����ã����������Ϣ���б༭
			return;
		}
		if (Utils.isEditingStatus())
		{
			if (stepMode instanceof TestToolMdl)
			{
				this.dialog2 = new NewPeriodMsgDlg(SPTEPlugin.getActiveWorkbenchShell(), ((AbtConnMdl) this.getModel()), this, SPTEConstants.MESSAGE_TYPE_SEND);
			}
			else
			{
				this.dialog2 = new NewPeriodMsgDlg(SPTEPlugin.getActiveWorkbenchShell(), ((AbtConnMdl) this.getModel()), this, SPTEConstants.MESSAGE_TYPE_RECEIVE);
			}

			if (this.dialog2.open() == Window.OK)
			{
				((NewPeriodMsgDlg) dialog2).setName();
			}
		}
	}

	public PeriodParentMsgPart()
	{

	}

	@Override
	protected void createEditPolicies()
	{
		// ֧������ѡ��
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());

		// ֧��ɾ����������
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new PeriodParentMsgPolicy());

		// ѡ����Ϣ���޸�����
		installEditPolicy(SPTEConstants.CHANGE_MESSAGE_REQUEST_TYPE, new ConnSelectMsgPolicy());
	}

	@Override
	public IFigure createFigure()
	{
		PeriodParentMsgMdl model = (PeriodParentMsgMdl) this.getModel();
		if (model.getName() == null || model.getName().trim().length() == 0)
		{
			model.setName(Messages.getString("I18N_CYCLE_MSG")); // ������Ϣ
		}
		//PeriodParentMsgFgr figure = new PeriodParentMsgFgr((PeriodParentMsgMdl) this.getModel());
		//figure.addMouseMotionListener(new ConnMouseMotionListener((AbtConnMdl) this.getModel()));
		PeriodParentMsgFgr figure = new PeriodParentMsgFgr(model);
		figure.addMouseMotionListener(new ConnMouseMotionListener(model));
		return figure;
	}

	@Override
	public void setSelected(int value)
	{
		super.setSelected(value);
		if (value != EditPart.SELECTED_NONE)
		{
			((AbtCycleMsgMdl) this.getModel()).setColor(ColorConstants.orange);
			((PeriodParentMsgMdl) this.getModel()).getFixedChild().setColor(ColorConstants.orange);
			((PeriodParentMsgMdl) this.getModel()).setDefaultColor(ColorConstants.orange);
			((PeriodParentMsgMdl) this.getModel()).getFixedChild().setDefaultColor(ColorConstants.orange);
		}
		else
		{
			// ������Ϣ���δѡ����Ϣ��ɫ
			((PeriodParentMsgMdl) this.getModel()).setDefaultColor(InstanceUtils.getInstance().getPeriodOrParallelDefaultColor());
			((PeriodParentMsgMdl) this.getModel()).getFixedChild().setDefaultColor(InstanceUtils.getInstance().getPeriodOrParallelDefaultColor());
			((AbtCycleMsgMdl) this.getModel()).setColor(((AbtCycleMsgMdl) this.getModel()).getDefaultColor());
			((PeriodParentMsgMdl) this.getModel()).getFixedChild().setColor(((PeriodParentMsgMdl) this.getModel()).getFixedChild().getDefaultColor());
		}
	}
}