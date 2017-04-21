package com.coretek.testcase.testcaseview;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.utils.StringUtils;

/**
 * 
 * ²âÊÔÓÃÀý¹ýÂËÆ÷
 * 
 * @author Ëï´óÎ¡
 * @date 2010-12-14
 */
public class Filter extends ViewerFilter
{
	private static Filter	filter;

	private String			projectName;

	private String			suiteName;

	private String			keyWords;

	public String getProjectName()
	{
		return projectName;
	}

	public void setProjectName(String projectName)
	{
		this.projectName = projectName;
	}

	public String getSuiteName()
	{
		return suiteName;
	}

	public void setSuiteName(String suiteName)
	{
		this.suiteName = suiteName;
	}

	public String getKeyWords()
	{
		return keyWords;
	}

	public void setKeyWords(String keyWords)
	{
		this.keyWords = keyWords;
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element)
	{
		TestCase test = (TestCase) element;
		if (!this.filterProjectName(test, projectName))
		{
			return false;
		}
		if (!this.filterSuiteName(test, suiteName))
		{
			return false;
		}
		if (!this.filterKeyWords(test, keyWords))
		{
			return false;
		}
		return true;
	}

	private boolean filterProjectName(TestCase test, String projectName)
	{
		if (!test.getProjectName().equals(projectName) && StringUtils.isNotNull(projectName))
		{
			return false;
		}
		return true;
	}

	private boolean filterSuiteName(TestCase test, String suiteName)
	{
		if (StringUtils.isNotNull(suiteName) && suiteName.contains("\\"))
		{
			if (!test.getSuitePath().equals(suiteName))
				return false;
		} else if (!test.getSuiteName().equals(suiteName) && StringUtils.isNotNull(suiteName))
		{
			return false;
		}

		return true;
	}

	private boolean filterKeyWords(TestCase test, String keyWords)
	{
		if (StringUtils.isNotNull(keyWords) && !Messages.getString("I18N_WRONG").equals(keyWords))
		{
			if (!test.getCaseName().contains(keyWords))
			{
				return false;
			}
		}

		return true;
	}

	public static Filter getInstance()
	{
		if (filter == null)
		{
			filter = new Filter();
		}
		return filter;
	}

}
