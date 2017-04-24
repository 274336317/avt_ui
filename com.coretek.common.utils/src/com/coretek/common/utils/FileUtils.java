/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/**
 * 文件常用方法
 * 
 * @author 孙大巍 2011-12-5
 */
public class FileUtils
{

	/**
	 * 从java's file转换到IFile
	 * 
	 * @param absolutePath
	 * @return
	 */
	public static IFile convertJFileToIFile(String absolutePath)
	{
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IPath location = Path.fromOSString(absolutePath);
		IFile file = workspace.getRoot().getFileForLocation(location);
		return file;
	}

	/**
	 * 删除文件或文件夹。
	 * 
	 * @param file <br/>
	 * @throws Exception 当删除文件失败时会抛出此异常，但是在此之前删除的文档都不会被恢复
	 */
	public static boolean delete(File file)
	{
		if (!file.exists())
		{
			return true;
		}
		if (file.isDirectory())
		{
			for (File f : file.listFiles())
			{
				if (!delete(f))
				{
					return false;
				}
			}
		}
		return file.delete();
	}

	/**
	 * 拷贝两个文件
	 * 
	 * @param sourceFile
	 * @param destFile <br/>
	 * @throws Exception
	 */
	public static void copyFile(File sourceFile, File destFile) throws Exception
	{
		FileInputStream fis = new FileInputStream(sourceFile);
		File df = destFile;
		if(df.exists())
		{
			df.delete();
		}
		
		if (!df.createNewFile())
		{
			throw new Exception("创建目标文件" + df.getAbsolutePath() + "失败");
		}
		FileOutputStream fos = new FileOutputStream(df);
		byte contents[] = new byte[1024 * 10];
		int counter = 0;
		while ((counter = fis.read(contents)) != -1)
		{
			fos.write(contents, 0, counter);
		}
		fos.flush();
		fos.close();
		fis.close();
		contents = null;
	}

	/**
	 * 计算出某个文件夹下包含多少子文件。
	 * 
	 * @param file 文件夹
	 * @param countFolderIn 如果为true，则将文件夹也算到总数当中
	 * @return 目标文件夹下的所有子文件数量<br/>
	 */
	public static int getSumOfSubFiles(File file, boolean countFolderIn)
	{
		int sum = 0;
		if (!file.isDirectory())
		{
			throw new IllegalArgumentException("期望传入一个文件夹却传入一个文件");
		}
		File[] subFiles = file.listFiles();
		if (countFolderIn)
		{
			sum = subFiles.length;
		} else
		{
			for (File subFile : subFiles)
			{
				if (subFile.isFile())
				{
					sum++;
				}
			}
		}

		for (File subFile : subFiles)
		{
			if (subFile.isDirectory())
			{
				sum = sum + getSumOfSubFiles(subFile, countFolderIn);
			}
		}
		return sum;
	}

	/**
	 * 将一个输入源中的内容保存到文件当中。在保存之前会检查目标文件是否存在，如果存在则删除它，
	 * 然后在新建一个文件。注意，在输出结束之后会将传入的输入流关闭，调用者无需再进行关闭操作。
	 * 
	 * @param file 写入的目标文件
	 * @param input 读取内容的输入流
	 * @throws IOException 当出现IO异常的时候会抛出此异常
	 */
	public static void writeToFile(File file, InputStream input) throws IOException
	{
		if (!file.exists())
		{
			if (file.createNewFile())
				;
		} else
		{
			file.delete();
			file.createNewFile();
		}
		PrintWriter writer = new PrintWriter(file);
		InputStreamReader isr = new InputStreamReader(input);
		BufferedReader reader = new BufferedReader(isr);
		String str = null;
		while ((str = reader.readLine()) != null)
		{
			writer.println(str);
		}
		writer.flush();
		writer.close();
		isr.close();
		input.close();
	}

	/**
	 * 将文件或文件夹中的内容拷贝到目标文件夹当中。如果目标地址已经存在，则删除它然后新建一个。
	 * 此函数用于拷贝容量比较小的文件或文件夹。如果用户要拷贝容量较大的文件或文件夹，可能会导致 调用线程阻塞时间较长。
	 * 
	 * @param source 源文件或文件夹
	 * @param dest 目标文件或文件夹 <br/>
	 * @throws Exception 当在创建文件目录发生错误时，会抛出此异常
	 */
	public static boolean copyFile(String source, String dest) throws Exception
	{
		File file = new File(source);
		if (!file.exists())
		{
			throw new IllegalArgumentException("源文件不存在");
		}
		if (file.isDirectory())
		{
			File[] subFiles = file.listFiles();
			File destFile = new File(dest);
			if (destFile.isFile())
			{
				throw new IllegalArgumentException("源文件和目标文件的类型不匹配");
			}
			if (!destFile.exists())
			{
				if (!destFile.mkdir())
				{
					throw new Exception("创建目标文件夹失败");
				}
			} else
			{
				delete(destFile);
				if (!destFile.mkdir())
				{
					throw new Exception("创建目标文件夹失败");
				}
			}
			for (File subFile : subFiles)
			{
				if (subFile.isDirectory())
				{
					File subFolder = new File(dest + File.separator + subFile.getName());
					if (!subFolder.exists())
					{
						if (!subFolder.mkdir())
						{
							throw new Exception("创建目标文件夹 " + subFolder.getAbsolutePath() + "失败");
						}
					}
					copyFile(subFile.getAbsolutePath(), dest + File.separator + subFile.getName());
				} else
				{
					File df = new File(dest + File.separator + subFile.getName());
					copyFile(subFile, df);
				}
			}
		} else
		{
			File df = new File(dest);
			copyFile(file, df);
		}

		return true;
	}
}