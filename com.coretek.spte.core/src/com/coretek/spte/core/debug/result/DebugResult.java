package com.coretek.spte.core.debug.result;

/**
 * ���Խ���������յ���ֵ�ͱȽϵĽ��
 * 
 * @author ���Ρ
 * 
 *         2011-3-15
 */
public class DebugResult
{

	private DebugComparedResult	comparedResult; // ִ�������յ��������ķ�����Ϣ֮��ıȽϽ��

	private DebugReceivedResult	receivedResult; // ִ�������յ��ķ�����������Ϣ

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
