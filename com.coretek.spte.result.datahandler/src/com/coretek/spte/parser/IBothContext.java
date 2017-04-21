/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.parser;

/**
 * 同时开启执行与监控的环境类
 * 
 * @author 孙大巍 2012-3-27
 */
public interface IBothContext extends IExecutorContext
{

	/**
	 * 设置测试用例文件路径 </br>
	 * 
	 * @param casePath
	 */
	public void setCasePath(String casePath);

}
