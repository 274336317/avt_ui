package com.coretek.spte.core.views;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * 展示所有节点和消息
 * 
 * @author duyisen 2012-4-18
 */
public class ShowAllNodeAction extends Action
{

	private MessageView	view;

	public ShowAllNodeAction(MessageView view, String text, ImageDescriptor imageDescriptor)
	{
		super(text, imageDescriptor);
		this.view = view;
	}

	public void run()
	{
		MessageView.setFilterMsg(true);
		view.refresh();
	}

}