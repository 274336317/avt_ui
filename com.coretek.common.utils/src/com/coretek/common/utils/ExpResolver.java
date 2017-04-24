package com.coretek.common.utils;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

/**
 * ��ѧ���ʽִ����
 * 
 * @author ���Ρ
 * @date 2010-12-29
 */
public class ExpResolver
{

	private Evaluator			evaluator;

	private static ExpResolver	exp;

	public ExpResolver()
	{
		evaluator = new Evaluator();
	}

	/**
	 * ��ȡ��ѧ���ʽ��������ʵ��
	 * 
	 * @return instance of ExpResolver
	 */
	public static ExpResolver getExpResolver()
	{
		if (exp == null)
		{
			exp = new ExpResolver();
		}
		return exp;
	}

	/**
	 * ������ʽ
	 * 
	 * @return Object:���ʽ��������������ʽ���ܽ������㣬�򷵻ر��ʽ����
	 */
	public Object evaluate(String expression)
	{
		String result = null;
		if (StringUtils.isNotNull(expression))
		{
			try
			{
				// ������ʽ
				result = this.evaluator.evaluate(expression);
			} catch (EvaluationException e)
			{
				// �޷��Ա��ʽ���������򷵻ظñ��ʽ����
				result = expression;
			}
		}

		return result;
	}

}