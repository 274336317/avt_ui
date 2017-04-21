/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor;

import java.util.List;

import com.coretek.spte.dataCompare.CompareResult;
import com.coretek.spte.dataCompare.Result;

/**
 * @author 孙大巍 2012-3-12
 */
public class SequenceBuilderImpl implements SequenceBuilder
{

	private static SequenceBuilder	builder;

	public synchronized static SequenceBuilder getInstance()
	{
		if (builder == null)
		{
			builder = new SequenceBuilderImpl();
		}

		return builder;
	}

	@Override
	public Sequence buildMiddleSequence(List<Result> resultList, CompareResult compareResult, int timeBound, int selectedTime, int scale)
	{

		return new MiddleSequence(resultList, scale, selectedTime, timeBound);
	}

	@Override
	public Sequence buildSequence(List<Result> resultList, CompareResult compareResult)
	{

		return new ResultSequenceWithoutTime(resultList, compareResult);
	}

	@Override
	public WaterFallSequence buildWaterFallSequence(List<Result> resultList, CompareResult compareResult, int scale)
	{
		return new WaterFallSequence(resultList, scale, compareResult);
	}

}
