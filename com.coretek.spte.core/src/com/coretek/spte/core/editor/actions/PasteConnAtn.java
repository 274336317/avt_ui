package com.coretek.spte.core.editor.actions;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.spte.core.commands.PasteMsgCmd;
import com.coretek.spte.core.commands.PastePeriodMsgCmd;
import com.coretek.spte.core.commands.PasteTimerCmd;
import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.IntervalConnMdl;
import com.coretek.spte.core.models.MsgConnMdl;
import com.coretek.spte.core.models.PeriodParentMsgMdl;
import com.coretek.spte.core.models.TestNodeMdl;
import com.coretek.spte.core.parts.TestNodePart;
import com.coretek.spte.core.util.Utils;

/**
 * �����Ϣ ������� 1.�����һ��ʱ������ʱ�򣬲��������û����Ϣ���ߵ�item�ϣ�����������Ѿ���Ϊ����ʱ����������˵�item�ϣ�
 * ����������ӱ�ѡ�е�item����û������itemӵ����Ϣ���ߵ�Item��
 * 2.�����һ����Ϣ���ߵ�ʱ�򣬲���������Ѿ�ӵ����Ϣ���ߵ�item�ϣ����������ʱ�������м�item�ϣ����������������Ϣ���м�item��
 * 
 * @author ���Ρ
 * @date 2010-9-3
 * 
 */
public class PasteConnAtn extends SelectionAction
{

	@SuppressWarnings("unchecked")
	@Override
	public List getSelectedObjects()
	{
		return super.getSelectedObjects();
	}

	@Override
	protected void init()
	{
		super.init();

		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setText(Messages.getString("I18N_PASTE"));
		setId(ActionFactory.PASTE.getId());
		setHoverImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE_DISABLED));
		setEnabled(true);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isEnabled()
	{
		if (this.getSelection() == null)
		{
			return false;
		}
		List<AbtConnMdl> models = (List<AbtConnMdl>) Clipboard.getDefault().getContents();

		// ���һ��ģ��
		if (models != null && models.size() == 1)
		{
			if (!(this.getSelection() instanceof StructuredSelection))
			{
				return false;
			}
			StructuredSelection selection = (StructuredSelection) this.getSelection();
			if (selection.getFirstElement() instanceof TestNodePart)
			{
				TestNodePart item = (TestNodePart) selection.getFirstElement();
				TestNodeMdl itemModel = (TestNodeMdl) item.getModel();
				// ���ʱ���������������û����Ϣ���ߵ�item�ϣ�Ҳ����������ӱ�ѡ�е�item���µ�����itemû����Ϣ������
				if (models.get(0) instanceof IntervalConnMdl)
				{
					if (!Utils.hasConnection((TestNodeMdl) (item).getModel()))
					{
						return false;
					}
					if (!Utils.hasMessageFromSelectedItem((TestNodeMdl) (item).getModel()))
					{
						return false;
					}
				} else if (models.get(0) instanceof MsgConnMdl)
				{// �����Ϣ�����������ʱ����֮���item�ϣ����������������Ϣ�м䣬�����������Ѿ�ӵ����Ϣ���ߵ�item��
					if (Utils.hasConnection(itemModel))
					{
						return false;
					}
					if (Utils.isInsideCycleMessage(itemModel))
					{
						return false;
					}
					if (Utils.isBetweenTimeredConnections(itemModel))
					{
						return false;
					}
				}
			}
		}

		// ������ģ��
		for (Object object : this.getSelectedObjects())
		{
			if (object instanceof TestNodePart)
			{
				Object copiedContent = Clipboard.getDefault().getContents();
				if (copiedContent != null && copiedContent instanceof List)
				{
					if (((List) copiedContent).size() > 0)
						return true;
				}
			}
		}

		return false;
	}

	@Override
	public void run()
	{
		Command command = this.createPasteCommand();
		((PasteMsgCmd) command).setSelectedItem((TestNodeMdl) ((TestNodePart) this.getSelectedObjects().get(0)).getModel());
		if (command.canExecute())
		{
			execute(command);
		}
	}

	public PasteConnAtn(IWorkbenchPart part)
	{
		super(part);

	}

	@Override
	protected boolean calculateEnabled()
	{
		if (!Utils.isEditingStatus())
		{
			return false;
		}
		Command command = createPasteCommand();
		return command != null && command.canExecute();
	}

	@SuppressWarnings("unchecked")
	private Command createPasteCommand()
	{
		if (this.getSelectedObjects() == null || this.getSelectedObjects().size() == 0)
		{
			return null;
		}
		List<AbtConnMdl> models = (List<AbtConnMdl>) Clipboard.getDefault().getContents();
		if (this.getSelectedObjects().get(0) instanceof TestNodePart)
		{
			if (models != null && models.size() > 0)
			{
				if (models.get(0) instanceof PeriodParentMsgMdl)
				{
					return new PastePeriodMsgCmd();
				} else if (models.get(0) instanceof MsgConnMdl)
				{
					return new PasteMsgCmd();
				} else if (models.get(0) instanceof IntervalConnMdl)
				{
					return new PasteTimerCmd();
				}
			}
		}
		return null;
	}

	@Override
	protected void handleSelectionChanged()
	{
		super.handleSelectionChanged();
	}
}