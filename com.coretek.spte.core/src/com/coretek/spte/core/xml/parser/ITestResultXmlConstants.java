package com.coretek.spte.core.xml.parser;

/**
 * ���Խ��XML�ļ��ڵ㡢����
 * 
 * @author linxy
 * 
 */
public interface ITestResultXmlConstants
{

	/**
	 * ���ڵ�TestResult
	 */
	public final String	ROOT						= "TestResult";
	/**
	 * �ڵ�result��ROOT�£�
	 */
	public final String	NODE_RESULT					= "Result";
	/**
	 * �ڵ�Message��ROOT�£�
	 */
	public final String	NODE_MESSAGE				= "Message";
	/**
	 * �ڵ�Signal��NODE_RESULT�£�
	 */
	public final String	NODE_SIGNAL					= "Signal";
	/**
	 * �ڵ�filed��NODE_SIGNAL�£�
	 */
	public final String	NODE_FIELD					= "Field";
	/**
	 * SIGNAL����SignalID
	 */
	public final String	ATTR_SIGNAL_ID				= "SignalID";
	/**
	 * MESSAGE����name
	 */
	public final String	ATTR_MESSAGE_NAME			= "Name";
	/**
	 * MESSAGE����uuid
	 */
	public final String	ATTR_MESSAGE_UUID			= "UUID";
	/**
	 * MESSAGE����testcase
	 */
	public final String	ATTR_MESSAGE_TESTCASE		= "TestCase";
	/**
	 * MESSAGE����OwnerUUID
	 */
	public final String	ATTR_MESSAGE_OWNERUUID		= "OwnerUUID";
	/**
	 * FIELD����fieldID
	 */
	public final String	ATTR_FIELD_ID				= "fieldID";

	/**
	 * FILE����name
	 */
	public final String	ATTR_FILE_NAME				= "name";
	/**
	 * RESULT����pass
	 */
	public final String	ATTR_RESULT_PASS			= "pass";
	/**
	 * RESULT����reason
	 */
	public final String	ATTR_RESULT_REASON			= "Reason";
	/**
	 * RESULT����messagename
	 */
	public final String	ATTR_RESULT_MESSAGENAME		= "messagename";
	/**
	 * RESULT����uuid
	 */
	public final String	ATTR_RESULT_UUID			= "uuid";
	/**
	 * RESULT����uuid
	 */
	public final String	ATTR_RESULT_TIMES			= "times";
	/**
	 * RESULT����testcase
	 */
	public final String	ATTR_RESULT_TESTCASE		= "testcase";
	/**
	 * RESULT����describe
	 */
	public final String	ATTR_RESULT_DESCRIBE		= "describe";
	/**
	 * RESULT����expectedvalue
	 */
	public final String	ATTR_FIELD_EXPECTEDVALUE	= "expectedvalue";

	public final String	ATTR_RESULT_ACTUALVALUE		= "ActualValue";
	/**
	 * RESULT����actualvalue
	 */
	public final String	ATTR_FIELD_ACTUALVALUE		= "actualvalue";
	/**
	 * RESULT����OwnerUUID
	 */
	public final String	ATTR_RESULT_OWNERUUID		= "OwnerUUID";
	/**
	 * RESULT����MessageField
	 */
	public final String	ATTR_RESULT_MESSAGEFIELD	= "MessageField";
	/**
	 * RESULT����ExpectedValue
	 */
	public final String	ATTR_RESULT_EXPECTEDVALUE	= "ExpectedValue";
}
