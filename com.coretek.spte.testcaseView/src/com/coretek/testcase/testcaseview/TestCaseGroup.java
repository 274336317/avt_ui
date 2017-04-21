package com.coretek.testcase.testcaseview;

import java.util.HashSet;
import java.util.Set;

import com.coretek.common.utils.StringUtils;

/**
 * 测试用例组名
 * 
 * @author 孙大巍
 * @date 2010-12-13
 */
public class TestCaseGroup
{
	// 唯一标识符
	private String				UUID;

	private String				name;

	// 包含的测试用例
	private Set<TestCase>		testCases	= new HashSet<TestCase>();

	// 父节点
	private TestCaseGroup		parentGroup;

	// 子节点
	private Set<TestCaseGroup>	childGroups	= new HashSet<TestCaseGroup>();

	public Set<TestCaseGroup> getChildGroups()
	{
		Set<TestCaseGroup> temp = new HashSet<TestCaseGroup>();
		for (TestCaseGroup group : childGroups)
		{
			temp.add(group);
		}
		return temp;
	}

	public boolean deleteChildGroup(TestCaseGroup group)
	{
		return this.childGroups.remove(group);
	}

	public boolean addChildGroup(TestCaseGroup group)
	{
		return this.childGroups.add(group);
	}

	public void setChildGroup(Set<TestCaseGroup> childGroup)
	{
		this.childGroups = childGroup;
	}

	public TestCaseGroup getParentGroup()
	{
		return parentGroup;
	}

	public void setParentGroup(TestCaseGroup parentGroup)
	{
		this.parentGroup = parentGroup;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Set<TestCase> getTestCases()
	{
		Set<TestCase> temp = new HashSet<TestCase>();
		for (TestCase test : testCases)
		{
			temp.add(test);
		}
		return temp;
	}

	public boolean addTestCase(TestCase test)
	{
		if (this.testCases.contains(test))
		{
			return true;
		}
		return this.testCases.add(test);
	}

	public boolean deleteTestCase(TestCase test)
	{
		return this.testCases.remove(test);
	}

	public String getUUID()
	{
		return UUID;
	}

	public void setUUID(String UUID)
	{
		if (StringUtils.isNotNull(UUID))
			this.UUID = UUID;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (obj instanceof TestCaseGroup)
		{
			TestCaseGroup group = (TestCaseGroup) obj;
			if (StringUtils.isNotNull(group.UUID) && group.UUID.equals(this.UUID))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		int result = 17;
		result = result * 31 + this.UUID.hashCode();
		return result;
	}

	public void clearChildGroups()
	{
		this.childGroups.clear();
	}

	public void clearTestCases()
	{
		this.testCases.clear();
	}
}
