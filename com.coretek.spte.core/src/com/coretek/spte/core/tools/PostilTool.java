package com.coretek.spte.core.tools;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.gef.Request;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.tools.CreationTool;

import com.coretek.spte.core.models.PostilMdl;
import com.coretek.spte.core.util.SPTEConstants;

public class PostilTool extends CreationTool
{
	@Override
	protected Request createTargetRequest()
	{
		Map<String, String> map = new HashMap<String, String>(1);
		map.put(SPTEConstants.REQ_TYPE_KEY, SPTEConstants.REQ_TYPE_POSTIL);
		CreateRequest request = new CreateRequest();
		request.setExtendedData(map);
		ElementFactory postilFactory = new ElementFactory(new PostilMdl());
		request.setFactory(postilFactory);
		return request;
	}

	@Override
	protected boolean handleMove()
	{
		updateTargetRequest();
		updateTargetUnderMouse();
		setCurrentCommand(getCommand());
		showTargetFeedback();
		return true;
	}

}
