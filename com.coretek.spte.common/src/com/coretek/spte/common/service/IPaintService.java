/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.common.service;

/**
 * 绘制图形服务。实现者需要将此服务注册到OSGI的服务注册表中，供使用者调用。 目前定义此服务接口主要是为了方便曲线视图调用时序图进行图形绘图。
 * 
 * @author SunDawei
 * 
 * @date 2012-9-24
 */
public interface IPaintService
{
	/**
	 * 需要绘制的中间时间
	 * 
	 * @param time
	 */
	void paint(long time);

}
