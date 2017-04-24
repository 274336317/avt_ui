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
 * ��������ע�⣬���ڱ�ʶ�������Ƿ�������ʾ���༭ �Լ���������Ӧ��xml���ֺʹ�������xml���Ƿ�Ϊһ���ڵ�
 * 
 * @author ���Ρ 2011-12-24
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldRules
{

	/**
	 * ���ڼ�¼�����ֶζ�Ӧ��xml����
	 * 
	 * @return
	 */
	String xmlName() default "";

	/**
	 * ���ڱ�ʶ������ֶ��Ƿ��ڽ�����ʾ��
	 * 
	 * @return ���Ҫ��ʾ�򷵻�true�����򷵻�false</br>
	 */
	boolean display() default false;

	/**
	 * ��ʶ�����ֶ�����Ӧ��xmlԪ���ǽڵ㻹�ǽڵ�����ԡ�
	 * 
	 * @return ����ǽڵ㷵��true����������Է���false
	 */
	boolean node() default false;

	/**
	 * �ֶε���������
	 * 
	 * @return
	 */
	String type() default "String";

	/**
	 * ��ʾ���ֶ��Ƿ�����༭
	 * 
	 * @return
	 */
	boolean editable() default false;

	/**
	 * ��ʾ���ֶ��Ƿ�Ϊ�ı��ڵ�
	 * 
	 * @return
	 */
	boolean textNode() default false;
}
