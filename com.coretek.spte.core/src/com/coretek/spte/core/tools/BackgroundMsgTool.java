package com.coretek.spte.core.tools;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.gef.Request;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.tools.ConnectionCreationTool;

import com.coretek.spte.core.util.SPTEConstants;

/**
 * ±³¾°ÏûÏ¢
 * 
 * @author Ëï´óÎ¡
 * 
 *         2011-3-29
 */
public class BackgroundMsgTool extends ConnectionCreationTool
{

	@Override
	protected Request createTargetRequest()
	{
		CreateRequest req = new CreateConnectionRequest();
		Map<String, String> map = new HashMap<String, String>(1);
		map.put(SPTEConstants.REQ_TYPE_KEY, SPTEConstants.REQ_TYPE_BACKGROUND);
		req.setExtendedData(map);
		req.setFactory(getFactory());
		return req;
	}
}
