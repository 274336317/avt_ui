package com.coretek.spte.core.debug.result;

/**
 * 执行器比较返回信息之后返回的比较结果
 * 
 * @author 孙大巍
 * 
 *         2011-3-16
 */
public class DebugComparedResult
{

	private boolean	failed;

	private String	messageName;

	private String	uuid;

	private String	testCase;

	private String	result;

	private String	reason;

	private int		index	= -1;

	private String	ownerUUID;

	public String getOwnerUUID()
	{
		return ownerUUID;
	}

	public void setOwnerUUID(String ownerUUID)
	{
		this.ownerUUID = ownerUUID;
	}

	public int getIndex()
	{
		return index;
	}

	public void setIndex(int index)
	{
		this.index = index;
	}

	public String getResult()
	{
		return result;
	}

	public void setResult(String result)
	{
		this.result = result;
	}

	public String getReason()
	{
		return reason;
	}

	public void setReason(String reason)
	{
		this.reason = reason;
	}

	public boolean isFailed()
	{
		return failed;
	}

	public void setFailed(boolean failed)
	{
		this.failed = failed;
	}

	public String getMessageName()
	{
		return messageName;
	}

	public void setMessageName(String messageName)
	{
		this.messageName = messageName;
	}

	public String getUuid()
	{
		return uuid;
	}

	public void setUuid(String uuid)
	{
		this.uuid = uuid;
	}

	public String getTestCase()
	{
		return testCase;
	}

	public void setTestCase(String testCase)
	{
		this.testCase = testCase;
	}
}
