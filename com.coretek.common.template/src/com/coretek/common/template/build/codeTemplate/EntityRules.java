/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template.build.codeTemplate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ��ע�����ڱ�ʶ���Ƿ������ק����¼xpath����¼������Ӧ��xml�ڵ�����
 * 
 * @author ���Ρ 2011-12-24
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EntityRules
{
	/**
	 * xml�ϵ�xpath·��
	 * 
	 * @return </br>
	 */
	String xpath() default "";

	/**
	 * ����xml�϶�Ӧ�Ľڵ�����
	 * 
	 * @return </br>
	 */
	String xmlName() default "";

	/**
	 * ��������Ϣ��ͼ����ʾ���ֶ�
	 * 
	 * @return
	 */
	String displayField() default "";

	/**
	 * ��������Ϣ��ͼ�ϱ�ʶ���ڵ��Ƿ�����϶�
	 * 
	 * @return
	 */
	boolean dragable() default false;

	/**
	 * ��ȡ�����ļ���ָ����Ҫ�̳е���
	 * 
	 * @return
	 */
	String superClazz() default "com.coretek.common.template.build.codeTemplate.Entity";
}
