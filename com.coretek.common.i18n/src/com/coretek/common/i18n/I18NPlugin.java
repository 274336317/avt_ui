package com.coretek.common.i18n;

import java.util.Locale;

import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.utils.StringUtils;

/**
 * 国际化插件
 * 
 * @author 孙大巍 2011-12-9
 */
public class I18NPlugin extends AbstractUIPlugin implements IStartup
{

	// The plug-in ID
	public static final String	PLUGIN_ID	= "com.coretek.common.i18n";	//$NON-NLS-1$

	// The shared instance
	private static I18NPlugin	plugin;

	/**
	 * The constructor
	 */
	public I18NPlugin()
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception
	{
		super.start(context);
		plugin = this;
		String name = I18NPlugin.getDefault().getPreferenceStore().getString(I18NWorkbenchPreferencePage.LANGUAGE);
		if (StringUtils.isNull(name))
			name = Language.Chinese.getName();
		Language lan = Language.valueOf(name);
		if (lan == null)
			lan = Language.Chinese;
		Locale.setDefault(lan.getLocale());
		Messages.init(lan.getLocale());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception
	{
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static I18NPlugin getDefault()
	{
		return plugin;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IStartup#earlyStartup() <br/> <b>作者</b> 孙大巍 </br>
	 * <b>日期</b> 2012-2-18
	 */
	@Override
	public void earlyStartup()
	{
		// TODO Auto-generated method stub

	}

}
