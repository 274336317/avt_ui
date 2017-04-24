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
 * 引用注解，用于标识出对象的字段是引用其它对象
 * 
 * @author 孙大巍 2011-12-25
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ReferenceRules
{
	/**
	 * 引用的条件
	 * 
	 * @return
	 */
	public String condition() default "";

	/**
	 * 引用所对应的类
	 * 
	 * @return
	 */
	public String type() default "";

}
