package com.coretek.spte.core.dnd;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.requests.CreateRequest;

import com.coretek.common.template.Helper;

/**
 * ��������Ϣ��ͼ����ק��Ϣ��ͼ�α༭���еġ�ק������
 * 
 * @author ���Ρ
 * 
 *         2011-4-24
 */
public class MsgDropListener extends TemplateTransferDropTargetListener
{

	public MsgDropListener(EditPartViewer viewer)
	{
		super(viewer);
	}

	@Override
	protected Request getTargetRequest()
	{
		CreateRequest request = new DNDCreateRequest();
		Helper dropEntity = (Helper) ((TemplateTransfer) this.getTransfer()).getTemplate();
		request.setFactory(new GEFCreationFactory(dropEntity));
		return request;
	}
}
