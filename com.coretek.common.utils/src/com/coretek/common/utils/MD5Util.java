/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.eclipse.core.resources.IFile;

import com.coretek.common.utils.StringUtils;

/**
 * The utility class for md5 digest
 * 
 * @author SunDawei 2012-4-28
 */
public class MD5Util
{

	/**
	 * Get the md5 digest of input file
	 * 
	 * @param file
	 * @return
	 */
	public static String getMD5Digest(File file)
	{
		String strMD5 = StringUtils.EMPTY_STRING;
		InputStream input = null;
		try
		{
			MessageDigest digest = MessageDigest.getInstance("MD5");
			input = new FileInputStream(file);
			byte[] bytes = new byte[1024];
			int counter = 0;
			while ((counter = input.read(bytes)) > 0)
			{
				digest.update(bytes, 0, counter);
			}
			byte[] md5 = digest.digest();
			strMD5 = Arrays.toString(md5);
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			if (input != null)
			{
				try
				{
					input.close();
					input = null;
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}

		return strMD5;
	}

	/**
	 * Get the md5 digest of input file
	 * 
	 * @param file
	 * @return
	 */
	public static String getMD5Digest(IFile file)
	{

		return getMD5Digest(file.getLocation().toFile());
	}

}