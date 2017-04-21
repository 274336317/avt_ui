package com.coretek.spte.core.tools;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.gef.Request;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.tools.ConnectionCreationTool;

import com.coretek.spte.core.models.PeriodParentMsgMdl;
import com.coretek.spte.core.util.SPTEConstants;

/**
 * 周期消息
 * 
 * @author 孙大巍
 * @date 2010-8-30
 * 
 */
public class PeriodMsgTool extends ConnectionCreationTool
{
	@Override
	protected Request createTargetRequest()
	{
		CreateRequest req = new CreateConnectionRequest();
		Map<String, String> map = new HashMap<String, String>();
		map.put(SPTEConstants.REQ_TYPE_KEY, SPTEConstants.REQ_TYPE_FIXED_MESSAGE);
		req.setExtendedData(map);
		req.setFactory(getFactory());
		req.setType(SPTEConstants.REQ_TYPE_FIXED_MESSAGE);
		return req;
	}

	public PeriodMsgTool(PeriodParentMsgMdl model)
	{
		super();
	}
}
