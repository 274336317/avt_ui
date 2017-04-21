package com.coretek.spte.core.tools;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.gef.Request;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.tools.CreationTool;

import com.coretek.spte.core.util.SPTEConstants;

/**
 * ≤‚ ‘π§æﬂ
 * 
 * @author ÀÔ¥ÛŒ°
 * @date 2010-11-25
 * 
 */
public class TestObjectTool extends CreationTool
{
	@Override
	protected Request createTargetRequest()
	{
		Map<String, String> map = new HashMap<String, String>(1);
		map.put(SPTEConstants.REQ_TYPE_KEY, SPTEConstants.REQ_TYPE_TESTER);
		CreateRequest request = new CreateRequest();
		request.setExtendedData(map);
		request.setFactory(getFactory());
		return request;
	}
}
