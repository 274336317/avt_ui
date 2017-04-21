package com.coretek.testcase.testcaseview;

import com.coretek.common.i18n.messages.Messages;

/**
 * 
 * 返回系统默认的两个测试用例分组，一个是所有自定义列表，另一个是所有列出的用例
 * 
 * @author 孙大巍
 * @date 2010-12-13
 */
public class GroupFactory
{

	private static TestCaseGroup	customizedList;

	private static TestCaseGroup	allCases;

	public static TestCaseGroup getCustomizedList()
	{
		if (customizedList == null)
		{
			customizedList = new TestCaseGroup();
			customizedList.setName(Messages.getString("I18N_ALL_CUSTOMIZED_LIST"));
			customizedList.setUUID("0478cc79-73f3-40b2-a66c-b39457295c74");
		}
		return customizedList;
	}

	public static TestCaseGroup getAllCases()
	{
		if (allCases == null)
		{
			allCases = new TestCaseGroup();
			allCases.setName(Messages.getString("I18N_ALL_LISTED_CASES"));
			allCases.setUUID("ea6c27e5-1579-45d2-9251-f49060db70da");
		}
		return allCases;
	}
}
