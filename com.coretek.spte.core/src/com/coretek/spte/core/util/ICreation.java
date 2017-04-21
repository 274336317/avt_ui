package com.coretek.spte.core.util;

/**
 * 创建连接
 * 
 * @author 孙大巍
 * @date 2010-9-21
 * 
 */
public interface ICreation
{

	/**
	 * 逻辑验证
	 * 
	 * @return
	 */
	boolean validate();

	/**
	 * 执行业务逻辑
	 * 
	 * @return
	 */
	Object execute();
}
