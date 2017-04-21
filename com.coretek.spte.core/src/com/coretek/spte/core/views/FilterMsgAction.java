package com.coretek.spte.core.views;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * �����޹���Ϣ
 * 
 * @author duyisen 2012-4-18
 */
public class FilterMsgAction extends Action
{

	private MessageView	view;

	public FilterMsgAction(MessageView view, String text, ImageDescriptor imageDescriptor)
	{
		super(text, imageDescriptor);
		this.view = view;
	}

	public void run()
	{
		MessageView.setFilterMsg(false);
		view.refresh();
	}

}
