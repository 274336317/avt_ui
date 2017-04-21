package com.coretek.spte.core.util;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.graphics.Color;

/**
 * 常量
 * 
 * @author lifs
 * @date 2010-08-21
 * 
 */
public final class SPTEConstants
{

	private SPTEConstants()
	{

	}

	/**
	 * 时间间隔线宽度
	 */
	public final static int		FK_LINE_OFFSET						= 50;

	/**
	 * 选择消息后或时间间隔后，请求类型type
	 */
	public final static String	CHANGE_MESSAGE_REQUEST_TYPE			= "Change Message Name Policy";

	/**
	 * 选择被测对象后，请求类型type
	 */
	public final static String	CHANGE_TESTED_OBJECT_REQUEST_TYPE	= "Change Tested Object Policy";

	/**
	 * 初始化测试工具/被测对象时的初始化节点高度
	 */
	public final static int		INIT_LIFELINE_HEIGHT				= 8054;

	/**
	 * 测试工具/被测对象的最大高度
	 */
	public final static int		MAX_HEIGHT_OF_LIFELINE				= 100550;

	/**
	 * 配置消息生成的结果文件后缀名
	 */
	public final static String	RESULT_FILE_POST_FIX				= ".cas";

	/**
	 * icd文件的后缀名
	 * 
	 */
	public final static String	ICD_FILE_POST_FIX					= ".xml";

	/**
	 * 工程中的icd文件夹名字
	 */
	public final static String	ICD_FOLDER_NAME						= "ICD";

	/**
	 * 接收消息的类型
	 */
	public final static String	MESSAGE_TYPE_RECEIVE				= "recv";

	/**
	 * 发送消息的类型
	 */
	public final static String	MESSAGE_TYPE_SEND					= "send";

	/**
	 * 模块文件夹
	 */
	public static final String	MODULE_DIR_NAME						= ".module";

	/**
	 * 创建时间间隔消息
	 */
	public static final String	REQ_TYPE_TIMER						= "timer";

	/**
	 * 创建普通消息
	 */
	public static final String	REQ_TYPE_MESSAGE					= "message";

	/**
	 * 创建背景消息
	 */
	public static final String	REQ_TYPE_BACKGROUND					= "background";

	/**
	 * 创建周期消息
	 */
	public static final String	REQ_TYPE_FIXED_MESSAGE				= "fixed_message";

	/**
	 * 创建并行消息
	 */
	public static final String	REQ_TYPE_PARALLEL					= "parallel_message";

	/**
	 * 消息类型
	 */
	public static final String	REQ_TYPE_KEY						= "TYPE";

	public static final String	REQ_TYPE_TESTER						= "tester";

	public static final String	REQ_TYPE_POSTIL						= "postil";

	public static final String	REQ_TYPE_TESTED						= "tested";

	public static final String	REQ_TYPE_REFERENCE					= "reference";

	public static final String	REQ_TYPE_SET_REFERENCE				= "set_reference";

	/**
	 * 测试用例引用模型的缺省值
	 */
	public static final String	REFERENCE_MODEL_DEFAULT_NAME		= "测试用例引用";

	/**
	 * 解析.cas文件的时候使用的事件名字
	 */
	public static final String	PARSER_EVENT_REFERENCE				= "parser_reference";

	public static final String	PARSER_EVENT_MESSAGE				= "parser_message";

	public static final String	PARSER_EVENT_INTERVAL				= "parser_interval";

	public static final String	PARSER_EVENT_CYCLE					= "parser_cycle";

	public static final String	PARSER_EVENT_EMULATOR				= "parser_emulator";

	public static final String	PARSER_EVENT_TESTED_MODULE			= "parser_tested_module";

	/**
	 * 测试集的属性文件名字
	 */
	public static final String	TEST_SUITE_PROPERTY_FILE_NAME		= ".folderProperty";

	public final static String	PROP_OBJECT_INITED					= "Object_has_been_initialized";

	public final static String	PROP_CHANGED						= "changed";

	public final static String	PROP_MESSAGE_CHANGED				= "message_changed";

	public final static String	PROP_FAILED_STATUS_CHANGED			= "failed_status_changed";

	public final static String	EVENT_PROP_MESSAGE_CHANGED			= "message_changed";

	public final static String	EVENT_PROP_RESULT_MESSAGE_CHANGED	= "result_message_changed";

	public final static String	EVENT_PROP_RESULT_INTERVAL_CHANGED	= "result_interval_changed";

	public final static String	EVENT_PROP_SELECTED					= "prop_selected";

	public final static String	EVENT_STATUS_CHANGED				= "status_changed";

	public final static String	EVENT_VISIBLE_CHANGED				= "visible_changed";

	/**
	 * 按键事件
	 */
	public final static String	EVENT_DEBUG_CMD						= "debug_cmd";
	/**
	 * 调试开启
	 */
	public final static String	EVENT_DEBUG_BEGIN					= "debug_begin";
	/**
	 * 调试结束
	 */
	public final static String	EVENT_DEBUG_STOP					= "debug_stop";

	/**
	 * 调试状态下的消息颜色
	 */
	public final static Color	COLOR_DEBUG							= ColorConstants.orange;

	public final static Color	backgroundColor						= new Color(null, 246, 246, 246);

	/**
	 * 无条件周期消息
	 */
	public final static String	CYCLE_TYPE_PERD						= "PERD";

	/**
	 * 条件周期消息
	 */
	public final static String	CYCLE_TYPE_COND						= "COND";

	/**
	 * 发送消息
	 */
	public final static String	SIGNAL_DIRECTION_SEND				= "发送";

	/**
	 * 接收消息
	 */
	public final static String	SIGNAL_DIRECTION_RECV				= "接收";
}
