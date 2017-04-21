package com.coretek.spte.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;

import com.coretek.common.utils.StringUtils;
import com.coretek.spte.core.util.Utils;

/**
 * ²âÊÔ½á¹û
 * 
 * @author Ëï´óÎ¡
 * @date 2011-1-15
 */
public class TestResultBean implements PropertyChangeListener
{

	private IFile			testCase;

	private List<String>	resultInfos	= new ArrayList<String>();

	private boolean			failed;

	public IFile getTestCase()
	{
		return testCase;
	}

	public void setTestCase(IFile testCase)
	{
		this.testCase = testCase;
	}

	public List<String> getResultInfos()
	{
		return resultInfos;
	}

	public void setResultInfos(List<String> resultInfos)
	{
		this.resultInfos = resultInfos;
	}

	public boolean isFailed()
	{
		return failed;
	}

	public void setFailed(boolean failed)
	{
		this.failed = failed;
	}

	public void propertyChange(PropertyChangeEvent evt)
	{
		String str = (String) evt.getNewValue();
		if (!"OVER".equals(str) && StringUtils.isNotNull(str))
		{
			this.resultInfos.add(str);
			if (str.indexOf("FAILED") >= 0)
			{
				this.failed = true;
			}
		}

	}

	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		for (String str : resultInfos)
		{
			sb.append(str.toString());
		}
		return sb.toString();
	}

}
