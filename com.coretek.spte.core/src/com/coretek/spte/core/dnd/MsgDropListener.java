package com.coretek.spte.core.dnd;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.requests.CreateRequest;

import com.coretek.common.template.Helper;

/**
 * 监听从消息视图中拖拽消息到图形编辑器中的“拽”动作
 * 
 * @author 孙大巍
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
