package com.coretek.spte.core.debug.result;

import java.util.ArrayList;
import java.util.List;

/**
 * 接收到的执行器返回值
 * 
 * @author 孙大巍
 * 
 *         2011-3-15
 */
public class DebugReceivedResult
{

	private String				messagePath;

	private String				uuid;

	private String				testCase;											// 测试用例文件路径

	private List<FieldAndValue>	fieldAndValues	= new ArrayList<FieldAndValue>();

	private int					index			= -1;

	private String				ownerUUID;

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

	public String getMessagePath()
	{
		return messagePath;
	}

	public void setMessagePath(String messagePath)
	{
		this.messagePath = messagePath;
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

	public List<FieldAndValue> getFieldAndValues()
	{
		return fieldAndValues;
	}

	public void setFieldAndValues(List<FieldAndValue> fieldAndValues)
	{
		this.fieldAndValues = fieldAndValues;
	}

	public void addFieldAndValue(FieldAndValue fav)
	{
		this.fieldAndValues.add(fav);
	}
}
