/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template.parser;

import java.io.File;

/**
 * 解析器
 * 
 * @author 孙大巍 2011-12-23
 */
public interface IParser
{

	/**
	 * 解析具体的文件，生成对应的对象
	 * 
	 * @param file
	 * @return </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-23
	 */
	Object parse(File file);

}
