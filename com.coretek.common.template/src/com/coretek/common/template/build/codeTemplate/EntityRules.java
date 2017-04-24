/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template.build.codeTemplate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 此注解用于标识类是否可以拖拽、记录xpath、记录类所对应的xml节点名字
 * 
 * @author 孙大巍 2011-12-24
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EntityRules
{
	/**
	 * xml上的xpath路径
	 * 
	 * @return </br>
	 */
	String xpath() default "";

	/**
	 * 类在xml上对应的节点名字
	 * 
	 * @return </br>
	 */
	String xmlName() default "";

	/**
	 * 用于在消息视图上显示的字段
	 * 
	 * @return
	 */
	String displayField() default "";

	/**
	 * 用于在消息视图上标识出节点是否可以拖动
	 * 
	 * @return
	 */
	boolean dragable() default false;

	/**
	 * 获取规则文件中指定需要继承的类
	 * 
	 * @return
	 */
	String superClazz() default "com.coretek.common.template.build.codeTemplate.Entity";
}
