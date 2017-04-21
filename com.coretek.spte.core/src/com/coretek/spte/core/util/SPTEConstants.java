package com.coretek.spte.core.util;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.graphics.Color;

/**
 * ����
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
	 * ʱ�����߿��
	 */
	public final static int		FK_LINE_OFFSET						= 50;

	/**
	 * ѡ����Ϣ���ʱ��������������type
	 */
	public final static String	CHANGE_MESSAGE_REQUEST_TYPE			= "Change Message Name Policy";

	/**
	 * ѡ�񱻲�������������type
	 */
	public final static String	CHANGE_TESTED_OBJECT_REQUEST_TYPE	= "Change Tested Object Policy";

	/**
	 * ��ʼ�����Թ���/�������ʱ�ĳ�ʼ���ڵ�߶�
	 */
	public final static int		INIT_LIFELINE_HEIGHT				= 8054;

	/**
	 * ���Թ���/�����������߶�
	 */
	public final static int		MAX_HEIGHT_OF_LIFELINE				= 100550;

	/**
	 * ������Ϣ���ɵĽ���ļ���׺��
	 */
	public final static String	RESULT_FILE_POST_FIX				= ".cas";

	/**
	 * icd�ļ��ĺ�׺��
	 * 
	 */
	public final static String	ICD_FILE_POST_FIX					= ".xml";

	/**
	 * �����е�icd�ļ�������
	 */
	public final static String	ICD_FOLDER_NAME						= "ICD";

	/**
	 * ������Ϣ������
	 */
	public final static String	MESSAGE_TYPE_RECEIVE				= "recv";

	/**
	 * ������Ϣ������
	 */
	public final static String	MESSAGE_TYPE_SEND					= "send";

	/**
	 * ģ���ļ���
	 */
	public static final String	MODULE_DIR_NAME						= ".module";

	/**
	 * ����ʱ������Ϣ
	 */
	public static final String	REQ_TYPE_TIMER						= "timer";

	/**
	 * ������ͨ��Ϣ
	 */
	public static final String	REQ_TYPE_MESSAGE					= "message";

	/**
	 * ����������Ϣ
	 */
	public static final String	REQ_TYPE_BACKGROUND					= "background";

	/**
	 * ����������Ϣ
	 */
	public static final String	REQ_TYPE_FIXED_MESSAGE				= "fixed_message";

	/**
	 * ����������Ϣ
	 */
	public static final String	REQ_TYPE_PARALLEL					= "parallel_message";

	/**
	 * ��Ϣ����
	 */
	public static final String	REQ_TYPE_KEY						= "TYPE";

	public static final String	REQ_TYPE_TESTER						= "tester";

	public static final String	REQ_TYPE_POSTIL						= "postil";

	public static final String	REQ_TYPE_TESTED						= "tested";

	public static final String	REQ_TYPE_REFERENCE					= "reference";

	public static final String	REQ_TYPE_SET_REFERENCE				= "set_reference";

	/**
	 * ������������ģ�͵�ȱʡֵ
	 */
	public static final String	REFERENCE_MODEL_DEFAULT_NAME		= "������������";

	/**
	 * ����.cas�ļ���ʱ��ʹ�õ��¼�����
	 */
	public static final String	PARSER_EVENT_REFERENCE				= "parser_reference";

	public static final String	PARSER_EVENT_MESSAGE				= "parser_message";

	public static final String	PARSER_EVENT_INTERVAL				= "parser_interval";

	public static final String	PARSER_EVENT_CYCLE					= "parser_cycle";

	public static final String	PARSER_EVENT_EMULATOR				= "parser_emulator";

	public static final String	PARSER_EVENT_TESTED_MODULE			= "parser_tested_module";

	/**
	 * ���Լ��������ļ�����
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
	 * �����¼�
	 */
	public final static String	EVENT_DEBUG_CMD						= "debug_cmd";
	/**
	 * ���Կ���
	 */
	public final static String	EVENT_DEBUG_BEGIN					= "debug_begin";
	/**
	 * ���Խ���
	 */
	public final static String	EVENT_DEBUG_STOP					= "debug_stop";

	/**
	 * ����״̬�µ���Ϣ��ɫ
	 */
	public final static Color	COLOR_DEBUG							= ColorConstants.orange;

	public final static Color	backgroundColor						= new Color(null, 246, 246, 246);

	/**
	 * ������������Ϣ
	 */
	public final static String	CYCLE_TYPE_PERD						= "PERD";

	/**
	 * ����������Ϣ
	 */
	public final static String	CYCLE_TYPE_COND						= "COND";

	/**
	 * ������Ϣ
	 */
	public final static String	SIGNAL_DIRECTION_SEND				= "����";

	/**
	 * ������Ϣ
	 */
	public final static String	SIGNAL_DIRECTION_RECV				= "����";
}
