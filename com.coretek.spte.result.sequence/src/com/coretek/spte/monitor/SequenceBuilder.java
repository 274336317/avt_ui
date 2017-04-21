/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor;

import java.util.List;

import com.coretek.spte.dataCompare.CompareResult;
import com.coretek.spte.dataCompare.Result;

/**
 * 时序图构器
 * 
 * @author 孙大巍 2012-3-1
 */
public interface SequenceBuilder
{
	/**
	 * 构建一个具体的Sequence对象
	 * 
	 * @param msgs 消息集合
	 * @return
	 */
	Sequence buildSequence(List<Result> resultList, CompareResult compareResult);

	/**
	 * 构建一个按照时间顺序显示执行结果的序列图对象
	 * 
	 * @param resultList
	 * @param compareResult
	 * @param scale 时间刻度值
	 * @return
	 */
	WaterFallSequence buildWaterFallSequence(List<Result> resultList, CompareResult compareResult, int scale);

	/**
	 * 构建一个以某个时间为中点前后显示一定范围的执行结果的序列图对象
	 * 
	 * @param resultList
	 * @param timeBound 时间范围
	 * @param selectedTime 被选中当作时间中点的时间点
	 * @param scale 时间的刻度值
	 * @return
	 */
	Sequence buildMiddleSequence(List<Result> resultList, CompareResult compareResult, int timeBound, int selectedTime, int scale);

}