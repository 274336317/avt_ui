package com.coretek.testcase.projectView.action;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages
{
	private static final String		RESOURCE_BUNDLE_ZH	= "com.coretek.testcase.projectView.action.Messages";	//$NON-NLS-1$

	private static ResourceBundle	fgResourceBundle;
	static
	{
		try
		{
			fgResourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE_ZH);
		} catch (MissingResourceException x)
		{
			fgResourceBundle = null;
		}
	}

	private Messages()
	{
	}

	public static String getString(String key)
	{

		try
		{
			return fgResourceBundle.getString(key);
		} catch (MissingResourceException e)
		{
			return "!" + key + "!";//$NON-NLS-2$ //$NON-NLS-1$
		} catch (NullPointerException e)
		{
			return "#" + key + "#"; //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * Gets a string from the resource bundle and formats it with the argument
	 * 
	 * @param key the string used to get the bundle value, must not be null
	 */
	public static String getFormattedString(String key, Object arg)
	{
		String format = null;
		try
		{
			format = fgResourceBundle.getString(key);
		} catch (MissingResourceException e)
		{
			return "!" + key + "!";//$NON-NLS-2$ //$NON-NLS-1$
		}
		if (arg == null)
			arg = ""; //$NON-NLS-1$
		return MessageFormat.format(format, new Object[] { arg });
	}

	/**
	 * Gets a string from the resource bundle and formats it with arguments
	 */
	public static String getFormattedString(String key, String[] args)
	{
		return MessageFormat.format(fgResourceBundle.getString(key), args);
	}

}