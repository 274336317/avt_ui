/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.core.util;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;

import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.coretek.common.utils.MD5Util;
import com.coretek.common.utils.StringUtils;

/**
 * Wrapped icd file and provide md5 digest method and property listening.
 * 
 * @author SunDawei 2012-4-28
 */
public class ICDFileDecorator extends File
{

	/** */
	private static final long		serialVersionUID	= 8299680313148656927L;

	protected PropertyChangeSupport	listeners			= new PropertyChangeSupport(this);

	private String					md5;		

	private transient IFile			eFile;

	/**
	 * @param pathname
	 * @deprecated
	 */
	public ICDFileDecorator(String pathname)
	{
		super(pathname);
		IPath path = new Path(pathname);
		Workspace space = (Workspace) ResourcesPlugin.getWorkspace();
		this.eFile = space.getRoot().getFileForLocation(path);
		if (this.eFile == null || !this.eFile.exists())
		{
			throw new IllegalArgumentException("The pathname [" + pathname + "] is illegal.");
		}
	}

	public ICDFileDecorator(IFile eFile)
	{
		super(eFile.getLocation().toFile().getAbsolutePath());
		this.eFile = eFile;
	}

	public void removePropertyChangeListener(PropertyChangeListener l)
	{
		listeners.removePropertyChangeListener(l);
	}

	/**
	 * Get the icd file's path relative to ICD Project
	 * 
	 * @return </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-5-2
	 */
	public String getRelativePath()
	{

		return this.eFile.getFullPath().toPortableString();
	}

	/**
	 * Get the IFile object of ICDFile
	 * 
	 * @return the eFile <br/>
	 *         <b>Author</b> SunDawei </br> <b>Date</b> 2012-4-28
	 */
	public IFile getEFile()
	{
		return eFile;
	}

	protected void firePropertyChange(String prop, Object old, Object newValue)
	{
		listeners.firePropertyChange(prop, old, newValue);
	}

	public void addPropertyChangeListener(PropertyChangeListener l)
	{
		listeners.addPropertyChangeListener(l);
	}

	/**
	 * Get the ICD file's md5 digest
	 * 
	 * @return </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-4-28
	 */
	public String getMD5Digest()
	{
		if (StringUtils.isNull(this.md5))
			this.md5 = MD5Util.getMD5Digest(this);

		return this.md5;
	}

	/**
	 * Regenerating md5 digest
	 * 
	 * @return </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-4-28
	 */
	public String updateMD5Digest()
	{
		String old = this.md5;
		this.md5 = MD5Util.getMD5Digest(this);
		if (StringUtils.isNotNull(this.md5))
		{
			if (!this.md5.equals(old))
			{
				this.firePropertyChange("md5", old, this.md5);
			}
		}
		return this.md5;
	}
}