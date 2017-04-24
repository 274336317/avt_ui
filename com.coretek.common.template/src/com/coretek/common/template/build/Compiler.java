/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template.build;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Java代码编译器
 * 
 * @author 孙大巍 2011-12-23
 */
public class Compiler
{

	/**
	 * </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-23
	 */
	public Compiler()
	{

	}

	public boolean compile(String javac, String classPath, String sourcePath)
	{
		String command = "cmd /c start /b /normal D:/apache-ant-1.8.1/bin/ant.bat -buildfile F:/auto/build.xml jar";
		try
		{
			Process process = Runtime.getRuntime().exec(command);
			// InputStream input = process.getInputStream();
			// InputStreamReader reader = new InputStreamReader(input);
			// BufferedReader br = new BufferedReader(reader);
			// String str = null;
			// while((str = br.readLine()) != null) {
			// System.err.println(str);
			// }
			// reader.close();
			try
			{
				Thread.sleep(3000);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			process.destroy();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}

}
