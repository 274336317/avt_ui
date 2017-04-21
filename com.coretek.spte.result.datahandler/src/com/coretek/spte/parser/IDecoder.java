package com.coretek.spte.parser;

import com.coretek.common.template.ClazzManager;

/**
 * 数据解析器，负责将输入的数据解析成特定的对象。
 * 
 * @author 孙大巍 2012-3-20
 */
public interface IDecoder
{

	/**
	 * 设置icd文件解析出来的对象管理器
	 * 
	 * @param icdClazzManager
	 */
	public void setIcdManager(ClazzManager icdClazzManager);

	/**
	 * 设置case文件解析出来的对象管理器
	 * 
	 * @param caseClazzManager
	 */
	public void setCaseManager(ClazzManager caseClazzManager);

	/**
	 * 解析数据
	 * 
	 * @param data
	 * @return </br>
	 */
	public Object decode(byte[] data);

}