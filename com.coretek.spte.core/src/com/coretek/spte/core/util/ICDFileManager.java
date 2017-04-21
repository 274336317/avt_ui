/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.core.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.coretek.common.logging.LoggingPlugin;
import com.coretek.common.utils.StringUtils;

/**
 * This class's instance manages all of the icdFiles in the icd projects in the
 * current workspace.
 * 
 * @author SunDawei 2012-5-2
 */
public class ICDFileManager implements PropertyChangeListener
{

	private final static Logger		logger	= LoggingPlugin.getLogger(ICDFileManager.class);

	private Set<ICDFileDecorator>	files	= new HashSet<ICDFileDecorator>();

	private static ICDFileManager	manager	= new ICDFileManager();

	private ICDFileManager()
	{

	}

	/**
	 * Get the instance of ICDFileManager. It was a singleton.
	 * 
	 * @return
	 */
	public static ICDFileManager getInstance()
	{

		return manager;
	}

	/**
	 * Add a icd file
	 * 
	 * @param file
	 */
	public void addICDFile(IFile file)
	{
		ICDFileDecorator icdFile = new ICDFileDecorator(file);
		if (!files.contains(icdFile) && Utils.isICDFile(file))
		{
			icdFile.addPropertyChangeListener(this);
			files.add(icdFile);
		}
	}

	/**
	 * To check whether the icd file used by caseFile exists or not.
	 * 
	 * @param caseFile
	 * @return </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-5-24
	 */
	public boolean icdFileExists(IFile caseFile)
	{
		boolean result = true;
		String icdPath = Utils.getICDFilePath(caseFile);
		if (StringUtils.isNotNull(icdPath))
		{
			IPath path = new Path(icdPath);
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			if (file == null || !file.exists())
				result = false;
		} else
		{
			result = false;
		}
		return result;
	}

	/**
	 * Collecting all of icd files in the icd project(s) in the workspace </br>
	 * <b>Author</b> SunDawei </br> <b>Date</b> 2012-5-2
	 */
	public void collectAllICDFile()
	{
		this.files.clear();
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (IProject prj : projects)
		{
			if (Utils.isICDProject(prj))
			{
				try
				{
					IResource[] resources = prj.members();
					for (IResource resource : resources)
					{
						if (resource instanceof IFile)
						{
							IFile file = (IFile) resource;
							if (file.isAccessible() && Utils.isICDFile(file))
							{
								this.addICDFile(file);
							}
						} else if (resource instanceof IFolder)
						{
							IFolder folder = (IFolder) resource;
							List<IFile> files = this.getIFile(folder);
							if (files != null)
							{
								for (IFile file : files)
								{
									this.addICDFile(file);
								}

							}
						}
					}
				} catch (CoreException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Get all of case files in the given folder
	 * 
	 * @param folder
	 * @return
	 * @throws CoreException </br> <b>Author</b> SunDawei </br> <b>Date</b>
	 *             2012-5-9
	 */
	private List<IFile> getIFile(IFolder folder) throws CoreException
	{
		List<IFile> files = new ArrayList<IFile>();
		IResource resources[] = folder.members();
		for (IResource resource : resources)
		{
			if (resource instanceof IFile)
			{
				IFile file = (IFile) resource;
				if (Utils.isICDFile(file))
				{
					files.add(file);
				}
			} else if (resource instanceof IFolder)
			{
				IFolder subFolder = (IFolder) resource;
				files.addAll(this.getIFile(subFolder));
			}
		}

		return files;
	}

	/**
	 * To check the md5 of the given IFile that represents some TestCase object
	 * whether equals the md5 of its icdFile or not.
	 * 
	 * @param caseFile the testCase file
	 * @return </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-5-2
	 */
	public boolean checkMD5Digest(IFile caseFile)
	{
		boolean result = false;
		InputStream input = null;
		try
		{
			input = caseFile.getContents();
			String md5 = Utils.getMD5StoredInCaseFile(input);
			input.close();
			if (StringUtils.isNotNull(md5))
			{
				input = caseFile.getContents();
				String icdPath = Utils.getICDFilePath(input);
				if (StringUtils.isNotNull(icdPath))
				{
					IPath path = new Path(icdPath);
					IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
					if (file == null || !file.exists())
					{
						return false;
					}
					ICDFileDecorator deco = new ICDFileDecorator(file);
					String icdMD5 = deco.getMD5Digest();
					if (icdMD5.equals(md5))
					{
						result = true;
					}
				}
			}
		} catch (CoreException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			if (input != null)
				try
				{
					input.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
		}

		return result;
	}

	/**
	 * 验证即将被打开的测试用例是否合法，比如它所引用的ICD文件必须存在并且MD5值要匹配
	 * 
	 * @param caseFile
	 * @return 
	 */
	public static ValidateError validateCaseFile(IFile caseFile)
	{
		String icdPath = Utils.getICDFilePath(caseFile);
		if (StringUtils.isNull(icdPath))
		{
			return ValidateError.ICDFILENOTEXISTS;
		}

		IPath path = new Path(icdPath);
		IFile icdFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
		if (icdFile == null || !icdFile.exists())
		{
			return ValidateError.ICDFILENOTEXISTS;
		}

		File file = icdFile.getLocation().toFile();
		if (!file.exists())
		{
			return ValidateError.ICDFILENOTEXISTS;
		} else if (!ICDFileManager.getInstance().checkMD5Digest(caseFile))
		{
			return ValidateError.MD5NOTMATCH;
		}
		return null;
	}

	/**
	 * Get the given case file's md5. If the case file is not being managed by
	 * ICDFileManager, this method will return null.
	 * 
	 * @param file
	 * @return
	 */
	public String getMD5(File file)
	{
		for (ICDFileDecorator dec : this.files)
		{
			if (dec.getAbsolutePath().equals(file.getAbsolutePath()))
			{
				return dec.getMD5Digest();
			}
		}
		return null;
	}

	/**
	 * Get the given case file's md5. If the case file is not being managed by
	 * ICDFileManager, this method will return null.
	 * 
	 * @param file
	 * @return </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-5-9
	 */
	public String getMD5(IFile file)
	{
		for (ICDFileDecorator dec : this.files)
		{
			if (dec.getEFile().equals(file))
			{
				return dec.getMD5Digest();
			}
		}
		return null;
	}

	/**
	 * To check all of the testCase in the software testing projects in the
	 * workspace whether equals their icd file's md5 or not.
	 * 
	 * @return the collection of testCase whose md5 digest does not equal that
	 *         of icd file's md5</br> <b>Author</b> SunDawei </br> <b>Date</b>
	 *         2012-5-2
	 */
	public List<IFile> checkAllMD5Digest()
	{
		List<IFile> list = new ArrayList<IFile>();
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (IProject prj : projects)
		{
			if (Utils.isSoftwareTestingProject(prj))
			{
				List<IFile> iFiles = Utils.getAllCasesInProject(prj);
				for (IFile file : iFiles)
				{
					if (!this.checkMD5Digest(file))
					{
						list.add(file);
					}
				}
			}
		}

		return null;
	}

	/**
	 * To update the given file's md5 digest
	 * 
	 * @param file
	 * @return if the given file has been added to ICDFileManager return true,
	 *         otherwise return false</br> <b>Author</b> SunDawei </br>
	 *         <b>Date</b> 2012-5-12
	 */
	public boolean updateFile(IFile file)
	{
		if (this.contains(file))
		{
			for (ICDFileDecorator dec : this.files)
			{
				if (dec.getEFile().equals(file))
				{
					dec.updateMD5Digest();
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * To check whether or not the given file has been added to ICDFileManager
	 * 
	 * @param file
	 * @return </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-5-12
	 */
	public boolean contains(IFile file)
	{
		for (ICDFileDecorator dec : this.files)
		{
			if (dec.getEFile().equals(file))
			{
				return true;
			}
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seejava.beans.PropertyChangeListener#propertyChange(java.beans.
	 * PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt)
	{
		// For now, we do nothing!
	}

	/**
	 * Remove the given resource
	 * 
	 * @param resource </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-5-9
	 */
	public void remove(IResource resource)
	{
		if (resource instanceof IContainer)
		{
			IContainer container = (IContainer) resource;
			IProject prj = container.getProject();
			if (Utils.isICDProject(prj))
			{
				List<IFile> files = Utils.getAllICDFilesInFolder(container);
				for (IFile file : files)
				{
					this.remove(file);
				}
			}
		} else if (resource instanceof IFile)
		{
			IFile file = (IFile) resource;
			this.remove(file);
		}

	}

	/**
	 * Remove the given icd file
	 * 
	 * @param icdFile </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-5-9
	 */
	public void remove(IFile icdFile)
	{
		Iterator<ICDFileDecorator> it = this.files.iterator();
		while (it.hasNext())
		{
			ICDFileDecorator deco = it.next();
			if (deco.getEFile().equals(icdFile))
			{
				it.remove();
				logger.info("ICD文件[" + deco.getEFile().getFullPath() + "]被删除");
				deco = null;
				System.gc();
				break;
			}
		}
	}
}