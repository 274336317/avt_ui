/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
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
 * �ļ����÷���
 * 
 * @author ���Ρ 2011-12-5
 */
public class FileUtils
{

	/**
	 * ��java's fileת����IFile
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
	 * ɾ���ļ����ļ��С�
	 * 
	 * @param file <br/>
	 * @throws Exception ��ɾ���ļ�ʧ��ʱ���׳����쳣�������ڴ�֮ǰɾ�����ĵ������ᱻ�ָ�
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
	 * ���������ļ�
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
			throw new Exception("����Ŀ���ļ�" + df.getAbsolutePath() + "ʧ��");
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
	 * �����ĳ���ļ����°����������ļ���
	 * 
	 * @param file �ļ���
	 * @param countFolderIn ���Ϊtrue�����ļ���Ҳ�㵽��������
	 * @return Ŀ���ļ����µ��������ļ�����<br/>
	 */
	public static int getSumOfSubFiles(File file, boolean countFolderIn)
	{
		int sum = 0;
		if (!file.isDirectory())
		{
			throw new IllegalArgumentException("��������һ���ļ���ȴ����һ���ļ�");
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
	 * ��һ������Դ�е����ݱ��浽�ļ����С��ڱ���֮ǰ����Ŀ���ļ��Ƿ���ڣ����������ɾ������
	 * Ȼ�����½�һ���ļ���ע�⣬���������֮��Ὣ������������رգ������������ٽ��йرղ�����
	 * 
	 * @param file д���Ŀ���ļ�
	 * @param input ��ȡ���ݵ�������
	 * @throws IOException ������IO�쳣��ʱ����׳����쳣
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
	 * ���ļ����ļ����е����ݿ�����Ŀ���ļ��е��С����Ŀ���ַ�Ѿ����ڣ���ɾ����Ȼ���½�һ����
	 * �˺������ڿ��������Ƚ�С���ļ����ļ��С�����û�Ҫ���������ϴ���ļ����ļ��У����ܻᵼ�� �����߳�����ʱ��ϳ���
	 * 
	 * @param source Դ�ļ����ļ���
	 * @param dest Ŀ���ļ����ļ��� <br/>
	 * @throws Exception ���ڴ����ļ�Ŀ¼��������ʱ�����׳����쳣
	 */
	public static boolean copyFile(String source, String dest) throws Exception
	{
		File file = new File(source);
		if (!file.exists())
		{
			throw new IllegalArgumentException("Դ�ļ�������");
		}
		if (file.isDirectory())
		{
			File[] subFiles = file.listFiles();
			File destFile = new File(dest);
			if (destFile.isFile())
			{
				throw new IllegalArgumentException("Դ�ļ���Ŀ���ļ������Ͳ�ƥ��");
			}
			if (!destFile.exists())
			{
				if (!destFile.mkdir())
				{
					throw new Exception("����Ŀ���ļ���ʧ��");
				}
			} else
			{
				delete(destFile);
				if (!destFile.mkdir())
				{
					throw new Exception("����Ŀ���ļ���ʧ��");
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
							throw new Exception("����Ŀ���ļ��� " + subFolder.getAbsolutePath() + "ʧ��");
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