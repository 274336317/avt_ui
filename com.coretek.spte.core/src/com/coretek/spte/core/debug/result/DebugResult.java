package com.coretek.spte.core.debug.result;

/**
 * 调试结果，包括收到的值和比较的结果
 * 
 * @author 孙大巍
 * 
 *         2011-3-15
 */
public class DebugResult
{

	private DebugComparedResult	comparedResult; // 执行器接收到服务器的返回信息之后的比较结果

	private DebugReceivedResult	receivedResult; // 执行器接收到的服务器返回信息

	public DebugComparedResult getComparedResult()
	{
		return comparedResult;
	}

	public void setComparedResult(DebugComparedResult comparedResult)
	{
		this.comparedResult = comparedResult;
	}

	public DebugReceivedResult getReceivedResult()
	{
		return receivedResult;
	}

	public void setReceivedResult(DebugReceivedResult receivedResult)
	{
		this.receivedResult = receivedResult;
	}
}
