package com.coretek.common.utils;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;

/**
 * 数学表达式执行器
 * 
 * @author 孙大巍
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
	 * 获取数学表达式解析器的实例
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
	 * 运算表达式
	 * 
	 * @return Object:表达式运算结果，如果表达式不能进行运算，则返回表达式本身
	 */
	public Object evaluate(String expression)
	{
		String result = null;
		if (StringUtils.isNotNull(expression))
		{
			try
			{
				// 运算表达式
				result = this.evaluator.evaluate(expression);
			} catch (EvaluationException e)
			{
				// 无法对表达式进行运算则返回该表达式本身
				result = expression;
			}
		}

		return result;
	}

}