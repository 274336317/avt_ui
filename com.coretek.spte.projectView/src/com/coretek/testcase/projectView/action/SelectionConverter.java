package com.coretek.testcase.projectView.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * 
 * @author Administrator
 * 
 */
public class SelectionConverter
{
	public static IStructuredSelection convertSelectionToResources(ISelection s)
	{
		List converted = new ArrayList();
		if (s instanceof StructuredSelection)
		{
			Object[] elements = ((StructuredSelection) s).toArray();
			for (int i = 0; i < elements.length; i++)
			{
				Object e = elements[i];
				if (e instanceof IResource)
				{
					converted.add(e);
				} else if (e instanceof IAdaptable)
				{
					IResource r = (IResource) ((IAdaptable) e).getAdapter(IResource.class);
					if (r != null)
					{
						converted.add(r);
					}
				}
			}
		}
		return new StructuredSelection(converted.toArray());
	}

	public static boolean allResourcesAreOfType(IStructuredSelection selection, int resourceMask)
	{
		Iterator resources = selection.iterator();
		while (resources.hasNext())
		{
			Object next = resources.next();
			if (next instanceof IAdaptable)
			{
				IAdaptable element = (IAdaptable) next;
				IResource resource = (IResource) element.getAdapter(IResource.class);

				if (resource == null)
				{
					return false;
				}
				if (!resourceIsType(resource, resourceMask))
				{
					return false;
				}
			}
		}
		return true;
	}

	public static boolean resourceIsType(IResource resource, int resourceMask)
	{
		return (resource.getType() & resourceMask) != 0;
	}

	/**
	 * Returns the selection adapted to IResource. Returns null if any of the
	 * entries are not adaptable.
	 * 
	 * @param selection the selection
	 * @param resourceMask resource mask formed by bitwise OR of resource type
	 *            constants (defined on <code>IResource</code>)
	 * @return IStructuredSelection or null if any of the entries are not
	 *         adaptable.
	 * @see IResource#getType()
	 */
	public static IStructuredSelection allResources(IStructuredSelection selection, int resourceMask)
	{
		Iterator adaptables = selection.iterator();
		List result = new ArrayList();
		while (adaptables.hasNext())
		{
			Object next = adaptables.next();
			if (next instanceof IAdaptable)
			{
				IResource resource = (IResource) ((IAdaptable) next).getAdapter(IResource.class);
				if (resource == null)
				{
					return null;
				} else if (resourceIsType(resource, resourceMask))
				{
					result.add(resource);
				}
			} else
			{
				return null;
			}
		}
		return new StructuredSelection(result);

	}
}
