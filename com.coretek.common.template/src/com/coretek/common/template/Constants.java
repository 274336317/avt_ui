/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template;

/**
 * 定义ICD中对象的属性名
 * 
 * @author 孙大巍
 * @date 2012-1-10
 */
public interface Constants
{

	/**
	 * ICD 文件中的信号标识符
	 */
	public final static String	ICD_FIELD_SYMBOL		= "signalSymbol";

	public final static String	ICD_FIELD_ID			= "signalID";

	/**
	 * ICD文件中的信号名称
	 */
	public final static String	ICD_FIELD_NAME			= "signalName";

	public final static String	ICD_FIELD_LSB			= "LSB";

	public final static String	ICD_FIELD_MSB			= "MSB";

	/**
	 * ICD文件中的信号单位代码
	 */
	public final static String	ICD_FIELD_UNIT_CODE		= "unitCode";

	/**
	 * ICD文件中的起始字
	 */
	public final static String	ICD_FIELD_START_WORD	= "startWord";

	/**
	 * ICD文件中的起始位
	 */
	public final static String	ICD_FIELD_START_BIT		= "startBit";

	/**
	 * ICD文件中的起始位
	 */
	public final static String	ICD_FIELD_TYPE			= "signalType";

	/**
	 * ICD文件中的是否有符号
	 */
	public final static String	ICD_FIELD_UNSIGNED		= "unsigned";

	/**
	 * ICD文件中的信号长度
	 */
	public final static String	ICD_FIELD_LENGTH		= "signalLength";

	/**
	 * ICD文件中的信号数组长度
	 */
	public final static String	ICD_FIELD_ARRAY_LENGTH	= "signalArrayLength";

	/**
	 * ICD文件中的信号最小值
	 */
	public final static String	ICD_FIELD_MIN_VALUE		= "signalMinValue";

	/**
	 * ICD文件中的信号最大值
	 */
	public final static String	ICD_FIELD_MAX_VALUE		= "signalMaxValue";

	/**
	 * ICD文件中的信号最高有效位
	 */
	public final static String	ICD_FIELD_H_BIT_VALUE	= "highestBitValue";

	/**
	 * ICD文件中的信号最低有效位
	 */
	public final static String	ICD_FIELD_L_BIT_VALUE	= "lowestBitValue";

	/**
	 * ICD文件中消息的消息名
	 */
	public final static String	ICD_MSG_NAME			= "msgName";

	public final static String	ICD_MSG_ID				= "msgID";

	/**
	 * ICD文件中消息的广播属性
	 */
	public final static String	ICD_MSG_BROCAST			= "brocast";

	/**
	 * ICD文件中消息的源功能ID
	 */
	public final static String	ICD_MSG_SRC_ID			= "sourceFunctionID";

	/**
	 * ICD文件中消息的消息主题名称
	 */
	public final static String	ICD_MSG_TOPIC_NAME		= "msgTopicName";

	/**
	 * ICD文件中消息的消息主题标识符
	 */
	public final static String	ICD_MSG_TOPIC_SYMBOL	= "msgTopicSymbol";

	/**
	 * ICD文件中消息的消息传输类型
	 */
	public final static String	ICD_MSG_TRANS_TYPE		= "msgTransType";

	/**
	 * ICD文件中消息的消息传输周期
	 */
	public final static String	ICD_MSG_TRANS_PERIOD	= "msgTransPeriod";

	/**
	 * ICD文件中消息的周期单位
	 */
	public final static String	ICD_MSG_PERIOD_UNIT		= "periodUnit";

	/**
	 * ICD文件中消息的延迟单位
	 */
	public final static String	ICD_MSG_DELAY_UNIT		= "delayUnit";

	/**
	 * ICD文件中消息的最大传输延迟
	 */
	public final static String	ICD_MSG_MAX_TRANS_DELAY	= "maxTransDelay";

	/**
	 * ICD文件中主题的主题类型
	 */
	public final static String	ICD_TOPIC_TYPE			= "topicType";

	public final static String	ICD_TOPIC_ID			= "topicID";

	/**
	 * ICD文件中主题的主题名称
	 */
	public final static String	ICD_TOPIC_NAME			= "topicName";

	/**
	 * ICD文件中主题的主题标识符
	 */
	public final static String	ICD_TOPIC_SYMBOL		= "topicSymbol";

	/**
	 * ICD文件中主题的格式类型
	 */
	public final static String	ICD_TOPIC_FORMAT_TYPE	= "formatType";

	/**
	 * ICD文件中主题的数据长度
	 */
	public final static String	ICD_TOPIC_DATA_LENGTH	= "dataLength";

	/**
	 * ICD文件中的功能对象ID
	 */
	public final static String	ICD_FUNCTION_ID			= "ID";

	/**
	 * ICD文件中的功能名称
	 */
	public final static String	ICD_FUNCTION_NAME		= "name";

	/**
	 * ICD文件中的信号值
	 */
	public final static String	ICD_FIELD_VALUE			= "value";

}