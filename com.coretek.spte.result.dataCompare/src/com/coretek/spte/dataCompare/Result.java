package com.coretek.spte.dataCompare;

import java.util.ArrayList;
import java.util.List;

import com.coretek.common.template.SPTEMsg;
import com.coretek.spte.cfg.ErrorTypesEnum;
import com.coretek.spte.cfg.HandlerTypesEnum;

/**
 * 对比结果
 * 
 * @author SunDawei 2012-7-30
 */
public class Result
{
	private List<ErrorTypesEnum>	errorTypes		= new ArrayList<ErrorTypesEnum>();

	private List<HandlerTypesEnum>	handlerTypes	= new ArrayList<HandlerTypesEnum>();

	private String					dipicts;

	private SPTEMsg					spteMsg;												// 实际运行消息

	private SPTEMsg					expectedMsg;											// 期望消息

	public Result(List<ErrorTypesEnum> errorTypes, HandlerTypesEnum handlerType, SPTEMsg spteMsg, String dipicts)
	{
		this.errorTypes.addAll(errorTypes);
		if (handlerType != null)
			this.handlerTypes.add(handlerType);
		this.dipicts = dipicts;
		this.spteMsg = spteMsg;
	}

	public void addErrorType(ErrorTypesEnum errorType)
	{
		this.errorTypes.add(errorType);
	}

	public List<ErrorTypesEnum> getErrorTypes()
	{
		return errorTypes;
	}

	public List<HandlerTypesEnum> getHandlerType()
	{
		return this.handlerTypes;
	}

	public void addHandlerType(HandlerTypesEnum handlerType)
	{
		this.handlerTypes.add(handlerType);
	}

	public String getDipicts()
	{
		return dipicts;
	}

	public SPTEMsg getSpteMsg()
	{
		return spteMsg;
	}

	/**
	 * @return the expectedMsg <br/>
	 */
	public SPTEMsg getExpectedMsg()
	{
		return expectedMsg;
	}

	public void setExpectedMsg(SPTEMsg expectedMsg)
	{
		this.expectedMsg = expectedMsg;
	}

}