package com.coretek.testcase.projectView.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;

@SuppressWarnings("deprecation")
public class FiltersContentProvider implements IStructuredContentProvider
{

	private static List				definedFilters;

	private static List				defaultFilters;

	private ResourcePatternFilter	resourceFilter;

	static final String				FILTERS_TAG	= "resourceFilters";

	/**
	 * Create a FiltersContentProvider using the selections from the supplied
	 * resource filter.
	 * 
	 * @param filter the resource pattern filter
	 */
	public FiltersContentProvider(ResourcePatternFilter filter)
	{
		this.resourceFilter = filter;
	}

	/*
	 * (non-Javadoc) Method declared on IContentProvider.
	 */
	public void dispose()
	{
	}

	/**
	 * Returns the filters which are enabled by default.
	 * 
	 * @return a list of strings
	 */
	public static List getDefaultFilters()
	{
		if (defaultFilters == null)
		{
			readFilters();
		}
		return defaultFilters;
	}

	/**
	 * Returns the filters currently defined for the navigator.
	 * 
	 * @return a list of strings
	 */
	public static List getDefinedFilters()
	{
		if (definedFilters == null)
		{
			readFilters();
		}
		return definedFilters;
	}

	/*
	 * (non-Javadoc) Method declared on IStructuredContentProvider.
	 */
	public Object[] getElements(Object inputElement)
	{
		return getDefinedFilters().toArray();
	}

	/**
	 * Return the initially selected elements.
	 * 
	 * @return an array with the initial selections
	 */
	public String[] getInitialSelections()
	{
		return this.resourceFilter.getPatterns();
	}

	/*
	 * (non-Javadoc) Method declared on IContentProvider.
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
	{
	}

	/**
	 * Reads the filters currently defined for the workbench.
	 */
	private static void readFilters()
	{
		definedFilters = new ArrayList();
		defaultFilters = new ArrayList();
		IExtensionPoint extension = Platform.getExtensionRegistry().getExtensionPoint(IDEWorkbenchPlugin.IDE_WORKBENCH + '.' + FILTERS_TAG);
		if (extension != null)
		{
			IExtension[] extensions = extension.getExtensions();
			for (int i = 0; i < extensions.length; i++)
			{
				IConfigurationElement[] configElements = extensions[i].getConfigurationElements();
				for (int j = 0; j < configElements.length; j++)
				{
					String pattern = configElements[j].getAttribute("pattern");
					if (pattern != null)
					{
						definedFilters.add(pattern);
					}
					String selected = configElements[j].getAttribute("selected");
					if (selected != null && selected.equalsIgnoreCase("true"))
					{
						defaultFilters.add(pattern);
					}
				}

			}
		}
	}
}