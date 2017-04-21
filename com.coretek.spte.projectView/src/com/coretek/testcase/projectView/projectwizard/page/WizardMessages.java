package com.coretek.testcase.projectView.projectwizard.page;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class WizardMessages
{
	private static final String		BUNDLE_ID	= "com.coretek.testcase.projectView.projectwizard.page.WizardMessages"; //$NON-NLS-1$

	private static ResourceBundle	resourceBundle;

	static
	{
		try
		{
			resourceBundle = ResourceBundle.getBundle(BUNDLE_ID);
		} catch (MissingResourceException x)
		{
			resourceBundle = null;
		}
	}

	public static String getFormattedString(String key, String arg)
	{
		return MessageFormat.format(getString(key), new String[] { arg });
	}

	public static String getFormattedString(String key, String[] args)
	{
		return MessageFormat.format(getString(key), args);
	}

	public static String getString(String key)
	{
		try
		{
			return resourceBundle.getString(key);
		} catch (MissingResourceException e)
		{
			return "!" + key + "!"; //$NON-NLS-1$ //$NON-NLS-2$
		} catch (NullPointerException e)
		{
			return "#" + key + "#"; //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
}
