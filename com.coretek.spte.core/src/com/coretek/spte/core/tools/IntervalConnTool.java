package com.coretek.spte.core.tools;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.gef.Request;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.tools.ConnectionCreationTool;

import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.util.SPTEConstants;

/**
 * 创建时间间隔连线
 * 
 * @author 孙大巍
 * @date 2010-11-24
 * 
 */
public class IntervalConnTool extends ConnectionCreationTool
{
	public IntervalConnTool(AbtConnMdl newObject, boolean b)
	{
		super();
	}

	@Override
	protected Request createTargetRequest()
	{
		CreateRequest req = new CreateConnectionRequest();
		req.setFactory(getFactory());
		Map<String, String> map = new HashMap<String, String>(1);
		map.put(SPTEConstants.REQ_TYPE_KEY, SPTEConstants.REQ_TYPE_TIMER);
		req.setExtendedData(map);
		return req;
	}
}
