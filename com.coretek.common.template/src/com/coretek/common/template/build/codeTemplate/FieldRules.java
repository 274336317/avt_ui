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
 * 对象属性注解，用于标识出属性是否允许显示、编辑 以及属性所对应的xml名字和此属性在xml上是否为一个节点
 * 
 * @author 孙大巍 2011-12-24
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldRules
{

	/**
	 * 用于记录对象字段对应的xml名字
	 * 
	 * @return
	 */
	String xmlName() default "";

	/**
	 * 用于标识对象的字段是否在界面显示。
	 * 
	 * @return 如果要显示则返回true，否则返回false</br>
	 */
	boolean display() default false;

	/**
	 * 标识对象字段所对应的xml元素是节点还是节点的属性。
	 * 
	 * @return 如果是节点返回true，如果是属性返回false
	 */
	boolean node() default false;

	/**
	 * 字段的数据类型
	 * 
	 * @return
	 */
	String type() default "String";

	/**
	 * 表示此字段是否允许编辑
	 * 
	 * @return
	 */
	boolean editable() default false;

	/**
	 * 表示此字段是否为文本节点
	 * 
	 * @return
	 */
	boolean textNode() default false;
}
