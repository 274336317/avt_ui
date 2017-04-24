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
 * ����ע�⣬���ڱ�ʶ��������ֶ���������������
 * 
 * @author ���Ρ 2011-12-25
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ReferenceRules
{
	/**
	 * ���õ�����
	 * 
	 * @return
	 */
	public String condition() default "";

	/**
	 * ��������Ӧ����
	 * 
	 * @return
	 */
	public String type() default "";

}
