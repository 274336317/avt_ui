/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor;

import java.util.List;

import com.coretek.spte.dataCompare.CompareResult;
import com.coretek.spte.dataCompare.Result;

/**
 * ʱ��ͼ����
 * 
 * @author ���Ρ 2012-3-1
 */
public interface SequenceBuilder
{
	/**
	 * ����һ�������Sequence����
	 * 
	 * @param msgs ��Ϣ����
	 * @return
	 */
	Sequence buildSequence(List<Result> resultList, CompareResult compareResult);

	/**
	 * ����һ������ʱ��˳����ʾִ�н��������ͼ����
	 * 
	 * @param resultList
	 * @param compareResult
	 * @param scale ʱ��̶�ֵ
	 * @return
	 */
	WaterFallSequence buildWaterFallSequence(List<Result> resultList, CompareResult compareResult, int scale);

	/**
	 * ����һ����ĳ��ʱ��Ϊ�е�ǰ����ʾһ����Χ��ִ�н��������ͼ����
	 * 
	 * @param resultList
	 * @param timeBound ʱ�䷶Χ
	 * @param selectedTime ��ѡ�е���ʱ���е��ʱ���
	 * @param scale ʱ��Ŀ̶�ֵ
	 * @return
	 */
	Sequence buildMiddleSequence(List<Result> resultList, CompareResult compareResult, int timeBound, int selectedTime, int scale);

}