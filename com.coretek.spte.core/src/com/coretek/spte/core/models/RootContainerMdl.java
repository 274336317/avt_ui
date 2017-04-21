package com.coretek.spte.core.models;

import java.util.Iterator;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.coretek.spte.core.tools.STextPropertyDescriptor;
import com.coretek.spte.core.util.TestCaseStatus;
import com.coretek.spte.core.util.Utils;
import com.coretek.spte.dataCompare.CompareResult;

/**
 * ��ײ������
 * 
 * @author ���Ρ
 * @date 2010-9-1
 * 
 */
public class RootContainerMdl extends AbtElement
{
	private static final long	serialVersionUID	= -6624418415289898977L;
	/**
	 * �༭��״̬
	 */
	private TestCaseStatus		status				= TestCaseStatus.Editing;

	/**
	 * ����ִ�н��
	 */
	private CompareResult		result;

	private String				ownerUUID;										// ���ڲ鿴�����ʱ�򣬴����Ա�ʾ���ö����uuid

	public RootContainerMdl()
	{
		this.setName(this.status.getText());
		this.setDescription(this.status.getName());
		this.status = TestCaseStatus.Editing;
	}

	public String getOwnerUUID()
	{
		return ownerUUID;
	}

	public CompareResult getResult()
	{
		return result;
	}

	public void setResult(CompareResult result)
	{
		this.result = result;
	}

	public void setOwnerUUID(String ownerUUID)
	{
		this.ownerUUID = ownerUUID;
	}

	public RootContainerMdl(TestCaseStatus status)
	{
		this.status = status;
		this.setName(this.status.getText());
		this.setDescription(this.status.getName());
	}

	public TestCaseStatus getStatus()
	{
		return status;
	}

	/**
	 * �����״̬����Ϊediting,���ɾ�������д洢�����в��Խ����Ϣ
	 * 
	 * @param status
	 */
	public void setStatus(TestCaseStatus status)
	{
		if (this.status != status)
		{
			if (status == TestCaseStatus.Editing)
			{
				// �����е���Ϣ���߻ָ���ԭʼ״̬
				List<MsgConnMdl> list = Utils.getAllMsgs(this.getChildren().get(0).getChildren().get(0).getChildren().get(0).getChildren());
				for (MsgConnMdl msg : list)
				{
					msg.setFailed(false);
					msg.setStatus(TestCaseStatus.Editing);
					// if(msg instanceof RefConnMdl) {
					// msg.setColor(ColorConstants.buttonDarker);
					// } else {
					// msg.setColor(ColorConstants.darkGreen);
					// }
					if (msg instanceof PeriodParentMsgMdl)
					{// ������Ϣ
						PeriodParentMsgMdl cycle = (PeriodParentMsgMdl) msg;
						TestNodeMdl sourceNode = (TestNodeMdl) cycle.getSource();
						TestNodeMdl targetNode = (TestNodeMdl) cycle.getTarget();
						cycle.setStatus(TestCaseStatus.Editing);
						sourceNode.setStatus(TestCaseStatus.Editing);
						targetNode.setStatus(TestCaseStatus.Editing);

						TestNodeMdl nextSourceNode = Utils.getNextNode(sourceNode);
						nextSourceNode.setStatus(TestCaseStatus.Editing);

						TestNodeMdl nextTargetNode = Utils.getNextNode(targetNode);
						nextTargetNode.setStatus(TestCaseStatus.Editing);

						nextSourceNode = Utils.getNextNode(nextSourceNode);
						nextSourceNode.setStatus(TestCaseStatus.Editing);

						nextTargetNode = Utils.getNextNode(nextTargetNode);
						nextTargetNode.setStatus(TestCaseStatus.Editing);
					} else
					{
						TestNodeMdl pSource = (TestNodeMdl) msg.getSource();
						TestNodeMdl pTarget = (TestNodeMdl) msg.getTarget();
						pSource.setStatus(TestCaseStatus.Editing);
						pTarget.setStatus(TestCaseStatus.Editing);
					}
				}
			}
			this.status = status;
			ContainerMdl sub = (ContainerMdl) this.getChildren().get(0);
			sub.setName(status.getText());
		}
	}

	protected IPropertyDescriptor[]	descriptors	= new IPropertyDescriptor[] { new STextPropertyDescriptor(PROP_NAME, "name"), new STextPropertyDescriptor(PROP_DESCRIPTION, "description") };

	public IPropertyDescriptor[] getPropertyDescriptors()
	{
		return descriptors;
	}

	public Object getPropertyValue(Object id)
	{
		if (PROP_NAME.equals(id))
			return getName();
		if (PROP_DESCRIPTION.equals(id))
			return getDescription();

		return "";
	}

	public void setPropertyValue(Object id, Object value)
	{
		if (PROP_NAME.equals(id))
			setName((String) value);
		if (PROP_DESCRIPTION.equals(id))
			setDescription((String) value);
	}

	public void addFChildOnly(AbtElement child)
	{
		child.setRootModel(this.rootModel);
		for (Iterator<AbtElement> iter = child.children.iterator(); iter.hasNext();)
		{
			AbtElement c = (AbtElement) iter.next();
			c.setRootModel(this.rootModel);
		}
		getChildren().add(child);
		child.setParent(this);
		this.fireChildenChange(child);
	}
}