package com.coretek.spte.core.util;

/**
 * 测试用例状态
 * 
 * @author 孙大巍
 * 
 */
public enum TestCaseStatus
{

	Editing("Editing", "编辑状态"), // 编辑状态
	ViewResult("ViewResult", "查看运行结果"), // 查看运行结果
	Debug("Debug", "调试状态"); // 调试

	private String	name;

	private String	text;

	TestCaseStatus(String name, String text)
	{
		this.name = name;
		this.text = text;
	}

	public String getName()
	{
		return name;
	}

	public String getText()
	{
		return text;
	}

	@Override
	public String toString()
	{
		return text;
	}
}
