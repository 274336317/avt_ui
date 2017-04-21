package com.coretek.spte.core.xml.parser;

/**
 * 测试结果XML文件节点、属性
 * 
 * @author linxy
 * 
 */
public interface ITestResultXmlConstants
{

	/**
	 * 根节点TestResult
	 */
	public final String	ROOT						= "TestResult";
	/**
	 * 节点result（ROOT下）
	 */
	public final String	NODE_RESULT					= "Result";
	/**
	 * 节点Message（ROOT下）
	 */
	public final String	NODE_MESSAGE				= "Message";
	/**
	 * 节点Signal（NODE_RESULT下）
	 */
	public final String	NODE_SIGNAL					= "Signal";
	/**
	 * 节点filed（NODE_SIGNAL下）
	 */
	public final String	NODE_FIELD					= "Field";
	/**
	 * SIGNAL属性SignalID
	 */
	public final String	ATTR_SIGNAL_ID				= "SignalID";
	/**
	 * MESSAGE属性name
	 */
	public final String	ATTR_MESSAGE_NAME			= "Name";
	/**
	 * MESSAGE属性uuid
	 */
	public final String	ATTR_MESSAGE_UUID			= "UUID";
	/**
	 * MESSAGE属性testcase
	 */
	public final String	ATTR_MESSAGE_TESTCASE		= "TestCase";
	/**
	 * MESSAGE属性OwnerUUID
	 */
	public final String	ATTR_MESSAGE_OWNERUUID		= "OwnerUUID";
	/**
	 * FIELD属性fieldID
	 */
	public final String	ATTR_FIELD_ID				= "fieldID";

	/**
	 * FILE属性name
	 */
	public final String	ATTR_FILE_NAME				= "name";
	/**
	 * RESULT属性pass
	 */
	public final String	ATTR_RESULT_PASS			= "pass";
	/**
	 * RESULT属性reason
	 */
	public final String	ATTR_RESULT_REASON			= "Reason";
	/**
	 * RESULT属性messagename
	 */
	public final String	ATTR_RESULT_MESSAGENAME		= "messagename";
	/**
	 * RESULT属性uuid
	 */
	public final String	ATTR_RESULT_UUID			= "uuid";
	/**
	 * RESULT属性uuid
	 */
	public final String	ATTR_RESULT_TIMES			= "times";
	/**
	 * RESULT属性testcase
	 */
	public final String	ATTR_RESULT_TESTCASE		= "testcase";
	/**
	 * RESULT属性describe
	 */
	public final String	ATTR_RESULT_DESCRIBE		= "describe";
	/**
	 * RESULT属性expectedvalue
	 */
	public final String	ATTR_FIELD_EXPECTEDVALUE	= "expectedvalue";

	public final String	ATTR_RESULT_ACTUALVALUE		= "ActualValue";
	/**
	 * RESULT属性actualvalue
	 */
	public final String	ATTR_FIELD_ACTUALVALUE		= "actualvalue";
	/**
	 * RESULT属性OwnerUUID
	 */
	public final String	ATTR_RESULT_OWNERUUID		= "OwnerUUID";
	/**
	 * RESULT属性MessageField
	 */
	public final String	ATTR_RESULT_MESSAGEFIELD	= "MessageField";
	/**
	 * RESULT属性ExpectedValue
	 */
	public final String	ATTR_RESULT_EXPECTEDVALUE	= "ExpectedValue";
}
