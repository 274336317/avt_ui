/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.internal.util.BundleUtility;
import org.osgi.framework.Bundle;

import com.coretek.common.logging.LoggingPlugin;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.common.template.build.codeTemplate.FieldRules;
import com.coretek.common.template.build.file.FieldTypes;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.Destnation;
import com.coretek.spte.Destnations;
import com.coretek.spte.EnumBean;
import com.coretek.spte.EnumValueAndKey;
import com.coretek.spte.Fighter;
import com.coretek.spte.FunctionCell;
import com.coretek.spte.FunctionCellMsg;
import com.coretek.spte.FunctionCells;
import com.coretek.spte.FunctionDomain;
import com.coretek.spte.FunctionDomainMsg;
import com.coretek.spte.FunctionDomains;
import com.coretek.spte.FunctionNode;
import com.coretek.spte.FunctionNodeMsg;
import com.coretek.spte.FunctionNodeMsgs;
import com.coretek.spte.FunctionNodes;
import com.coretek.spte.FunctionSubDomain;
import com.coretek.spte.FunctionSubDomainMsg;
import com.coretek.spte.FunctionSubDomains;
import com.coretek.spte.LayerInfo;
import com.coretek.spte.LayerTotal;
import com.coretek.spte.Signal;
import com.coretek.spte.Topic;
import com.coretek.spte.Version;
import com.coretek.spte.testcase.ICDFile;
import com.coretek.spte.testcase.Message;
import com.coretek.spte.testcase.MessageBlock;
import com.coretek.spte.testcase.Period;
import com.coretek.spte.testcase.TestCase;
import com.coretek.spte.testcase.TestedObject;
import com.coretek.spte.testcase.TestedObjects;
import com.coretek.spte.unit.Unit;
import com.coretek.spte.unit.UnitType;

/**
 * �ṩһЩ����ʹ��ģ������ķ���
 * 
 * @author ���Ρ 2011-12-29
 */
public final class TemplateUtils {

	private static final Logger logger = LoggingPlugin.getLogger(TemplateUtils.class.getName());

	/** ������Ϣ��ʶ�� */
	public final static String PERIOD_MSG = "PERIOD";

	/** ������Ĳ㼶���� */
	public final static String FUNCTION_DOMAIN = "������";

	/** ��������Ĳ㼶���� */
	public final static String FUNCTION_SUB_DOMAIN = "��������";

	/** ���ܵ�Ԫ�Ĳ㼶���� */
	public final static String FUNCTION_CELL = "���ܵ�Ԫ";

	/** ���ܽڵ�Ĳ㼶���� */
	public final static String FUNCTION_NODE = "���ܽڵ�";

	/** ���ܵ�Ԫ��Ϣ */
	public final static String FUNCTION_CELL_MSG = "FunctionCellMsg";

	/** ���ܽڵ���Ϣ */
	public final static String FUNCTION_NODE_MSG = "FunctionNodeMsg";

	/**
	 * �㲥
	 */
	public final static String BROCAST_BROADCAST = "BROADCAST";

	/**
	 * �㲥
	 */
	public final static String BROADCAST_POINT = "POINT";

	/**
	 * �鲥
	 */
	public final static String BROADCAST_GROUP = "GROUP";

	public final static String MSG_TRANS_TYPE_EVENT = "EVENT";

	/**
	 * ������Ϣ
	 */
	public final static String MSG_TRANS_TYPE_PERIOD = "PERIOD";

	public final static String MSG_TRANS_TYPE_INSTANCY = "INSTANCY";

	public final static String FUNCTION_DOMAIN_LEVEL = "00";

	public final static String FUNCTION_SUB_DOMAIN_LEVEL = "11";

	public final static String FUNCTION_NODE_LEVEL = "22";

	public final static String FUNCTION_CELL_LEVEL = "21";

	/**
	 * ��ȡ����������schema�ļ�
	 * 
	 * @return
	 */
	public static File getTestCaseSchemaFile() {
		Bundle bundle = Platform.getBundle(TemplateEnginePlugin.PLUGIN_ID);
		if (!BundleUtility.isReady(bundle)) {
			return null;
		}
		URL fullPathString = BundleUtility.find(bundle, "rules/testCaseStructure.xsd");
		URL locatedURL = null;
		String path = null;
		try {
			locatedURL = FileLocator.toFileURL(fullPathString);
			if (locatedURL != null)
				path = new Path(locatedURL.getPath()).toOSString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (StringUtils.isNotNull(path)) {
			File file = new File(path);
			return file;
		}
		return null;
	}

	/**
	 * ��ȡ���������е�������Ϣ�Լ�ʱ��������
	 * 
	 * @param testCase
	 * @return
	 */
	public static List<XMLBean> getAllMsgs(TestCase testCase) {
		List<XMLBean> list = new ArrayList<XMLBean>();
		List<Entity> entities = testCase.getChildren();
		for (Entity entity : entities) {
			if (entity instanceof MessageBlock) {
				MessageBlock block = (MessageBlock) entity;
				List<Entity> children = block.getChildren();
				for (Entity child : children) {
					list.add((XMLBean) child);
				}
			}
		}
		return list;
	}

	/**
	 * �жϽڵ���Ϣ�Ƿ�Ϊ�㲥��Ϣ
	 * 
	 * @param nodeMsg
	 * @return </br>
	 */
	public static boolean isBroadcast(ICDFunctionNodeMsg nodeMsg) {

		return TemplateUtils.BROCAST_BROADCAST.equals(nodeMsg.getAttribute("brocast").getValue());
	}

	public static boolean isBroadcast(ICDFunctionCellMsg cellMsg) {

		return TemplateUtils.BROCAST_BROADCAST.equals(cellMsg.getAttribute("brocast").getValue());
	}

	/**
	 * �жϽڵ���Ϣ�Ƿ�Ϊ������󼯺��ڲ�����Ϣ
	 * 
	 * @param nodeMsg
	 * @param testObjectsID
	 *            ��������ID����
	 * @return ������򷵻�true���򷵻�false</br>
	 */
	public static boolean isInternalMsgOfTestedObjects(ICDFunctionNodeMsg nodeMsg, List<String> testObjectsID) {
		// �㲥��Ϣ���������ڲ���Ϣ
		if (isBroadcast(nodeMsg))
			return false;
		// ��Ϣ��Դ�ڵ��
		String srcID = nodeMsg.getAttribute(Constants.ICD_MSG_SRC_ID).getValue().toString();
		// ��Ϣ��Ŀ�Ľڵ�ż���
		List<Integer> desIDs = nodeMsg.getSpteMsg().getICDMsg().getDestIDs();
		for (String testedID : testObjectsID) {
			if (testedID.equals(srcID)) {// ��Ϣ�ķ���ԴΪ�������
				for (int desId : desIDs) {
					int matchedCounter = 0;
					for (String testid : testObjectsID) {
						if (String.valueOf(desId).equals(testid)) {
							matchedCounter++;
						}
					}
					if (matchedCounter >= testObjectsID.size()) {
						return true;
					}
				}

				break;
			}
		}

		return false;
	}

	public static boolean isInternalMsgOfTestedObjects(ICDFunctionCellMsg cellMsg, List<String> testObjectsID) {
		// �㲥��Ϣ���������ڲ���Ϣ
		if (isBroadcast(cellMsg))
			return false;
		// ��Ϣ��Դ�ڵ��
		String srcID = cellMsg.getAttribute(Constants.ICD_MSG_SRC_ID).getValue().toString();
		// ��Ϣ��Ŀ�Ľڵ�ż���
		List<Integer> desIDs = cellMsg.getSpteMsg().getICDMsg().getDestIDs();
		for (String testedID : testObjectsID) {
			if (testedID.equals(srcID)) {// ��Ϣ�ķ���ԴΪ�������
				for (int desId : desIDs) {
					int matchedCounter = 0;
					for (String testid : testObjectsID) {
						if (String.valueOf(desId).equals(testid)) {
							matchedCounter++;
						}
					}
					if (matchedCounter >= testObjectsID.size()) {
						return true;
					}
				}

				break;
			}
		}

		return false;
	}

	/**
	 * �жϽڵ���Ϣ�Ƿ�Ϊ���Թ����ڲ�����Ϣ
	 * 
	 * @param nodeMsg
	 * @param testObjectsID
	 *            ��������ID����
	 * @return
	 */
	public static boolean isInternalMsgOfToolObjects(ICDFunctionNodeMsg nodeMsg, List<String> testObjectsID) {
		// �㲥��Ϣ���������ڲ���Ϣ
		if (isBroadcast(nodeMsg))
			return false;
		// ��Ϣ��Դ�ڵ��
		String srcID = nodeMsg.getAttribute(Constants.ICD_MSG_SRC_ID).getValue().toString();
		// ��Ϣ��Ŀ�Ľڵ�ż���
		List<Integer> destIDs = nodeMsg.getSpteMsg().getICDMsg().getDestIDs();
		for (String testedID : testObjectsID) {
			if (testedID.equals(srcID)) {// Դ�ڵ�ͱ���������
				return false;
			}
		}
		for (String testedID : testObjectsID) {
			for (Integer destID : destIDs) {
				if (String.valueOf(destID).equals(testedID)) {
					return false;
				}
			}

		}

		return true;
	}

	public static boolean isInternalMsgOfToolObjects(ICDFunctionCellMsg cellMsg, List<String> testObjectsID) {
		// �㲥��Ϣ���������ڲ���Ϣ
		if (isBroadcast(cellMsg))
			return false;
		// ��Ϣ��Դ�ڵ��
		String srcID = cellMsg.getAttribute(Constants.ICD_MSG_SRC_ID).getValue().toString();
		// ��Ϣ��Ŀ�Ľڵ�ż���
		List<Integer> destIDs = cellMsg.getSpteMsg().getICDMsg().getDestIDs();
		for (String testedID : testObjectsID) {
			if (testedID.equals(srcID)) {// Դ�ڵ�ͱ���������
				return false;
			}
		}
		for (String testedID : testObjectsID) {
			for (Integer destID : destIDs) {
				if (String.valueOf(destID).equals(testedID)) {
					return false;
				}
			}

		}

		return true;
	}

	/**
	 * ��ȡICD�ļ���������ĳ����������й�������Ϣ
	 * 
	 * @param domains
	 * @param topicSymbol
	 * @return
	 */
	public static List<ICDFunctionDomainMsg> getDomainMsgsByTopicSymbol(List<ICDFunctionDomain> domains, String topicSymbol) {
		List<ICDFunctionDomainMsg> domainMsgs = new ArrayList<ICDFunctionDomainMsg>();
		for (ICDFunctionDomain domain : domains) {
			for (ICDFunctionDomainMsg domainMsg : domain.getDomainMsg()) {
				if (domainMsg.getAttribute(Constants.ICD_MSG_TOPIC_SYMBOL).getValue().equals(topicSymbol)) {
					domainMsgs.add(domainMsg);
				}
			}
		}
		return domainMsgs;
	}

	/**
	 * ��ȡICD�ļ���������ĳ����������й���������Ϣ
	 * 
	 * @param domains
	 * @param topicSymbol
	 * @return
	 */
	public static List<ICDFunctionSubDomainMsg> getSubDomainMsgsByTopicSymbol(List<ICDFunctionDomain> domains, String topicSymbol) {
		List<ICDFunctionSubDomainMsg> subDomainMsgs = new ArrayList<ICDFunctionSubDomainMsg>();
		List<ICDFunctionSubDomain> subDomains = getSubDomains(domains);
		for (ICDFunctionSubDomain subDomain : subDomains) {
			for (ICDFunctionSubDomainMsg subDomainMsg : subDomain.getSubDomainMsgs()) {
				if (subDomainMsg.getAttribute(Constants.ICD_MSG_TOPIC_SYMBOL).getValue().equals(topicSymbol)) {
					subDomainMsgs.add(subDomainMsg);
				}
			}
		}

		return subDomainMsgs;
	}

	/**
	 * ��ȡICD�ļ���������ĳ����������й��ܽڵ���Ϣ
	 * 
	 * @param domains
	 * @param topicSymbol
	 * @return
	 */
	public static List<ICDFunctionNodeMsg> getNodeMsgsByTopicSymbol(List<ICDFunctionDomain> domains, String topicSymbol) {
		List<ICDFunctionNodeMsg> nodeMsgs = new ArrayList<ICDFunctionNodeMsg>();
		List<ICDFunctionSubDomain> subDomains = getSubDomains(domains);
		for (ICDFunctionDomain domain : domains) {
			for (ICDFunctionDomainMsg domainMsg : domain.getDomainMsg()) {
				for (ICDFunctionNodeMsg nodeMsg : domainMsg.getNodeMsgs()) {
					if (nodeMsg.getAttribute(Constants.ICD_MSG_TOPIC_SYMBOL).getValue().equals(topicSymbol)) {
						nodeMsgs.add(nodeMsg);
					}
				}
			}
		}
		for (ICDFunctionSubDomain subDomain : subDomains) {
			for (ICDFunctionSubDomainMsg subDomainMsg : subDomain.getSubDomainMsgs()) {
				for (ICDFunctionNodeMsg nodeMsg : subDomainMsg.getNodeMsgs()) {
					if (nodeMsg.getAttribute(Constants.ICD_MSG_TOPIC_SYMBOL).getValue().equals(topicSymbol)) {
						nodeMsgs.add(nodeMsg);
					}
				}
			}
			for (ICDFunctionNode node : subDomain.getNodes()) {
				for (ICDFunctionNodeMsg nodeMsg : node.getNodeMsgs()) {
					if (nodeMsg.getAttribute(Constants.ICD_MSG_TOPIC_SYMBOL).getValue().equals(topicSymbol)) {
						nodeMsgs.add(nodeMsg);
					}
				}
			}
		}
		return nodeMsgs;
	}

	/**
	 * ��ȡICD�ļ���������ĳ����������й��ܵ�Ԫ��Ϣ
	 * 
	 * @param domains
	 * @param topicSymbol
	 * @return
	 */
	public static List<ICDFunctionCellMsg> getCellMsgsByTopicSymbol(List<ICDFunctionDomain> domains, String topicSymbol) {
		List<ICDFunctionCellMsg> cellMsgs = new ArrayList<ICDFunctionCellMsg>();
		List<ICDFunctionSubDomain> subDomains = getSubDomains(domains);
		for (ICDFunctionDomain domain : domains) {
			for (ICDFunctionDomainMsg domainMsg : domain.getDomainMsg()) {
				for (ICDFunctionCellMsg cellMsg : domainMsg.getCellMsgs()) {
					if (cellMsg.getAttribute(Constants.ICD_MSG_TOPIC_SYMBOL).getValue().equals(topicSymbol)) {
						cellMsgs.add(cellMsg);
					}
				}
			}
		}
		for (ICDFunctionSubDomain subDomain : subDomains) {
			for (ICDFunctionSubDomainMsg subDomainMsg : subDomain.getSubDomainMsgs()) {
				for (ICDFunctionCellMsg cellMsg : subDomainMsg.getCellMsgs()) {
					if (cellMsg.getAttribute(Constants.ICD_MSG_TOPIC_SYMBOL).getValue().equals(topicSymbol)) {
						cellMsgs.add(cellMsg);
					}
				}
			}
			for (ICDFunctionCell cell : subDomain.getCells()) {
				for (ICDFunctionCellMsg cellMsg : cell.getCellMsgs()) {
					if (cellMsg.getAttribute(Constants.ICD_MSG_TOPIC_SYMBOL).getValue().equals(topicSymbol)) {
						cellMsgs.add(cellMsg);
					}
				}
			}
		}

		return cellMsgs;
	}

	/**
	 * ��ȡ������(ICDFunctionDomain)�µ���������(ICDFunctionSubDomain)����
	 * 
	 * @param domains
	 *            ������
	 * @return
	 */
	public static List<ICDFunctionSubDomain> getSubDomains(List<ICDFunctionDomain> domains) {
		List<ICDFunctionSubDomain> subDomains = new ArrayList<ICDFunctionSubDomain>();
		for (ICDFunctionDomain domain : domains) {
			subDomains.addAll(domain.getSubDomains());
		}

		return subDomains;
	}

	/**
	 * ��ȡ������������ֶζ����Ӧ���źš�
	 * 
	 * @param spteMsg
	 * @param field
	 *            ���������ֶ�
	 * @return
	 */
	public static ICDField getICDField(SPTEMsg spteMsg, com.coretek.spte.testcase.Field field) {
		ICDMsg icdMsg = spteMsg.getICDMsg();
		List<ICDField> icdFields = icdMsg.getFields();
		if (field.getParent() == null || field.getParent() instanceof Period || field.getParent() instanceof Message) {
			// ��һ��field
			for (ICDField icdField : icdFields) {
				if (icdField.getAttribute(Constants.ICD_FIELD_SYMBOL).getValue().equals(field.getId())) {
					return icdField;
				}
			}
			logger.warning("����symbol=" + field.getId() + " �Ҳ�����Ӧ��ICDField����");
		} else {// �����field������������Field��
			com.coretek.spte.testcase.Field parentField = (com.coretek.spte.testcase.Field) field.getParent();
			List<com.coretek.spte.testcase.Field> path = new ArrayList<com.coretek.spte.testcase.Field>();
			path.add(parentField);
			// �ݹ���ҳ����а���field��Field����ļ���
			while (parentField.getParent() != null && !(parentField.getParent() instanceof Period) && !(parentField.getParent() instanceof Message)) {
				path.add(parentField);
				parentField = (com.coretek.spte.testcase.Field) parentField.getParent();
			}

			for (ICDField icdField : icdFields) {
				if (icdField.getAttribute(Constants.ICD_FIELD_SYMBOL).getValue().equals(parentField.getId())) {
					icdFields = icdField.getIcdFields();
					break;
				}
			}

			if (path.size() == 1) {
				for (ICDField icdField : icdFields) {
					if (icdField.getAttribute(Constants.ICD_FIELD_SYMBOL).getValue().equals(field.getId())) {
						return icdField;
					}
				}
			} else {
				for (int i = path.size() - 2; i >= 0; i--) {
					parentField = path.get(i);
					ICDField temp = null;
					for (ICDField icdField : icdFields) {
						if (icdField.getAttribute(Constants.ICD_FIELD_SYMBOL).getValue().equals(parentField.getId())) {
							temp = icdField;
							break;
						}
					}
					if (i == 0) {
						return temp;
					}
					if (temp != null) {
						icdFields = temp.getIcdFields();
					} else {
						logger.warning("����symbol=" + field.getId() + " �Ҳ�����Ӧ��ICDField����");
					}
				}
			}

		}

		return null;
	}

	/**
	 * ����������Ϣ�У����ͱ�ź�List<Field>��ӳ���
	 * 
	 * @param message
	 * @return
	 */
	public static Map<String, List<Entity>> getTestCaseFieldsMap(Message message) {
		Map<String, List<Entity>> tCFieldMap = new HashMap<String, List<Entity>>();
		if (null == message) {
			return tCFieldMap;
		}
		if (message.isPeriodMessage()) {
			for (Entity en : message.getChildren()) {
				if (en instanceof Period) {
					String key = Integer.toString(((Period) en).getValue());
					tCFieldMap.put(key, en.getChildren());
				}
			}
		}
		return tCFieldMap;
	}

	/**
	 * ���ݹ��ܶ����ID��ȡ���ܶ��������
	 * 
	 * @param icdManager
	 * @param functionID
	 * @param clazz
	 * @return
	 */
	public static String getFunctionName(ClazzManager icdManager, Integer functionID, Class<? extends Entity> clazz) {
		String name = null;
		if (clazz.equals(FunctionDomain.class)) {
			name = getFunctionDomainName(icdManager, functionID);
		} else if (clazz.equals(FunctionSubDomain.class)) {
			name = getFunctionSubDomainName(icdManager, functionID);
		} else if (clazz.equals(FunctionCell.class)) {
			name = getFunctionCellName(icdManager, functionID);
		} else if (clazz.equals(FunctionNode.class)) {
			name = getFunctionNodeName(icdManager, functionID);
		} else {
			logger.warning("�����clazz��������ȷ.clazz=" + clazz.getName());
		}

		return name;
	}

	/**
	 * ��ȡ�����������
	 * 
	 * @param icdManager
	 * @param functionID
	 * @return
	 */
	public static String getFunctionDomainName(ClazzManager icdManager, Integer functionID) {
		List<Entity> entities = icdManager.getAllFunctionDomains();
		for (Entity entity : entities) {
			if (functionID.equals(entity.getFieldValue("ID"))) {
				return String.valueOf(entity.getFieldValue("name"));
			}
		}
		logger.warning("����ID=" + functionID + " �Ҳ�����Ӧ�Ĺ��������");

		return null;
	}

	/**
	 * ��ȡ�������������
	 * 
	 * @param icdManager
	 * @param functionID
	 *            ���������
	 * @return
	 */
	public static String getFunctionSubDomainName(ClazzManager icdManager, Integer functionID) {
		List<Entity> entities = icdManager.getAllFunctionSubDomains();
		for (Entity entity : entities) {
			if (functionID.equals(entity.getFieldValue("ID"))) {
				return String.valueOf(entity.getFieldValue("name"));
			}
		}
		logger.warning("����ID=" + functionID + " �Ҳ�����Ӧ�Ĺ����������");

		return null;
	}

	/**
	 * ��ȡ���ܵ�Ԫ������
	 * 
	 * @param icdManager
	 * @param functionID
	 *            ���ܵ�Ԫ��
	 * @return
	 */
	public static String getFunctionCellName(ClazzManager icdManager, Integer functionID) {
		List<FunctionCell> entities = icdManager.getAllFunctionCells();
		for (Entity entity : entities) {
			if (functionID.equals(entity.getFieldValue("ID"))) {
				return String.valueOf(entity.getFieldValue("name"));
			}
		}
		logger.warning("����ID=" + functionID + " �Ҳ�����Ӧ�Ĺ��ܵ�Ԫ����");

		return null;
	}

	/**
	 * ��ȡ���ܽڵ������
	 * 
	 * @param icdManager
	 * @param functionID
	 *            ���ܽڵ�ID
	 * @return
	 */
	public static String getFunctionNodeName(ClazzManager icdManager, Integer functionID) {
		List<FunctionNode> entities = icdManager.getAllFunctionNodes();
		for (Entity entity : entities) {
			if (functionID.equals(entity.getFieldValue("ID"))) {
				return String.valueOf(entity.getFieldValue("name"));
			}
		}
		logger.warning("����ID=" + functionID + " �Ҳ�����Ӧ�Ĺ��ܽڵ����");

		return null;
	}

	/**
	 * ��FunctionNode����ת��ΪICDFunctionNode����
	 * 
	 * @param icdManager
	 * @param icdSubDomain
	 * @param node
	 */
	private static void convertFromFunctionNode(ClazzManager icdManager, ICDFunctionSubDomain icdSubDomain, FunctionNode node) {
		ICDFunctionNode icdNode = new ICDFunctionNode(icdSubDomain);
		icdSubDomain.addNode(icdNode);
		try {
			icdNode.setAttributes(node.getAttributes());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return;
		}

		// ��ȡ���ܽڵ��µ�������Ϣ��ת��ΪICDFunctionNodeMsg����
		for (Entity logicChild : node.getLogicChildren()) {
			if (logicChild instanceof FunctionNodeMsg) {
				ICDFunctionNodeMsg icdNodeMsg = new ICDFunctionNodeMsg(icdNode);
				icdNode.addNodeMsg(icdNodeMsg);
				try {
					icdNodeMsg.setAttributes(logicChild.getAttributes());
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					break;
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					break;
				}
				List<SPTEMsg> spteMsgs;
				try {
					spteMsgs = getSPTEMsgsOfFunctionMsg(icdManager, TemplateEngine.getEngine().getUnitManager(), logicChild, new ArrayList<Entity>(), false);
					if (spteMsgs == null || spteMsgs.size() == 0) {
						spteMsgs = getSPTEMsgsOfFunctionMsg(icdManager, TemplateEngine.getEngine().getUnitManager(), logicChild, new ArrayList<Entity>(), false);
					}
					for (SPTEMsg spteMsg : spteMsgs) {
						icdNodeMsg.setSpteMsg(spteMsg);
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}

			}
		}
	}

	/**
	 * ��FunctionCell����ת��ΪICDFunctionCell����
	 * 
	 * @param icdSubDomain
	 * @param icdManager
	 * @param cell
	 */
	private static void convertFromFunctionCell(ICDFunctionSubDomain icdSubDomain, ClazzManager icdManager, FunctionCell cell) {
		ICDFunctionCell icdCell = new ICDFunctionCell(icdSubDomain);
		icdSubDomain.addCell(icdCell);
		try {
			icdCell.setAttributes(cell.getAttributes());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return;

		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return;
		}
		// ��ȡ���ܵ�Ԫ�µ�������Ϣ��ת��ΪICDFunctionCellMsg����
		for (Entity logicChild : cell.getLogicChildren()) {
			if (logicChild instanceof FunctionCellMsg) {
				ICDFunctionCellMsg icdCellMsg = new ICDFunctionCellMsg(icdCell);
				try {
					icdCellMsg.setAttributes(logicChild.getAttributes());
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					break;
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					break;
				}
				List<SPTEMsg> spteMsgs;
				try {
					spteMsgs = getSPTEMsgsOfFunctionMsg(icdManager, TemplateEngine.getEngine().getUnitManager(), logicChild, new ArrayList<Entity>(), false);

					for (SPTEMsg spteMsg : spteMsgs) {
						icdCellMsg.setSpteMsg(spteMsg);
						icdCell.addCellMsg(icdCellMsg);
					}
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}

			}
		}
	}

	/**
	 * ������������FunctionSubDomainMsg����ת��ΪICDFunctionSubDomainMsg����
	 * ����ӵ�ICDFunctionSubDomain�����С�
	 * 
	 * @param icdManager
	 * @param icdSubDomain
	 * @param subDomainMsg
	 */
	private static void convertFromFunctionSubDomainMsg(ClazzManager icdManager, ICDFunctionSubDomain icdSubDomain, FunctionSubDomainMsg subDomainMsg) {
		try {
			List<SPTEMsg> spteMsgs = getSPTEMsgsOfFunctionMsg(icdManager, TemplateEngine.getEngine().getUnitManager(), subDomainMsg, new ArrayList<Entity>(0), false);
			ICDFunctionSubDomainMsg icdDomainMsg = new ICDFunctionSubDomainMsg(icdSubDomain);
			icdSubDomain.addSubDomainMsg(icdDomainMsg);
			icdDomainMsg.setAttributes(subDomainMsg.getAttributes());
			for (SPTEMsg spteMsg : spteMsgs) {
				// ���ܵ�Ԫ��Ϣ
				if (FUNCTION_CELL_MSG.equals(spteMsg.getMsg().getModelType())) {
					ICDFunctionCellMsg cellMsg = new ICDFunctionCellMsg(spteMsg, icdSubDomain);
					cellMsg.setAttributes(spteMsg.getICDMsg().getAttributes());
					icdDomainMsg.addCellMsg(cellMsg);
				} else if (FUNCTION_NODE_MSG.equals(spteMsg.getMsg().getModelType())) {// ���ܽڵ���Ϣ
					ICDFunctionNodeMsg nodeMsg = new ICDFunctionNodeMsg(spteMsg, icdSubDomain);
					nodeMsg.setAttributes(spteMsg.getICDMsg().getAttributes());
					icdDomainMsg.addNodeMsg(nodeMsg);
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * ��ȡ���������Ϣ��ת��ΪICDFunctionDomainMsg��Ȼ��ת�����Ķ�����ӵ�ICDFunctionDomain�����С�
	 * 
	 * @param icdManager
	 * @param icdDomain
	 * @param functionDomain
	 */
	private static void convertFromFunctionDomain(ClazzManager icdManager, ICDFunctionDomain icdDomain, FunctionDomain functionDomain) {
		// ��ȡ���������Ϣ��ת��ΪICDFunctionDomainMsg
		for (Entity logicChild : functionDomain.getLogicChildren()) {
			if (logicChild instanceof FunctionDomainMsg) {
				FunctionDomainMsg domainMsg = (FunctionDomainMsg) logicChild;
				try {
					List<SPTEMsg> spteMsgs = getSPTEMsgsOfFunctionMsg(icdManager, TemplateEngine.getEngine().getUnitManager(), domainMsg, new ArrayList<Entity>(0), false);
					ICDFunctionDomainMsg icdDomainMsg = new ICDFunctionDomainMsg(icdDomain);
					icdDomain.addDomainMsg(icdDomainMsg);
					icdDomainMsg.setAttributes(domainMsg.getAttributes());
					for (SPTEMsg spteMsg : spteMsgs) {
						// ���ܵ�Ԫ��Ϣ
						if (FUNCTION_CELL_MSG.equals(spteMsg.getMsg().getModelType())) {
							ICDFunctionCellMsg cellMsg = new ICDFunctionCellMsg(spteMsg, icdDomain);
							cellMsg.setAttributes(spteMsg.getICDMsg().getAttributes());
							icdDomainMsg.addCellMsg(cellMsg);
						} else if (FUNCTION_NODE_MSG.equals(spteMsg.getMsg().getModelType())) {// ���ܽڵ���Ϣ
							ICDFunctionNodeMsg nodeMsg = new ICDFunctionNodeMsg(spteMsg, icdDomain);
							nodeMsg.setAttributes(spteMsg.getICDMsg().getAttributes());
							icdDomainMsg.addNodeMsg(nodeMsg);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}

			}
		}

	}

	/**
	 * ��ȡ���еĹ�����
	 * 
	 * @param icdManager
	 * @return </br>
	 */
	public static List<ICDFunctionDomain> getAllFunctionDomains(ClazzManager icdManager) {
		List<ICDFunctionDomain> icdDomains = new ArrayList<ICDFunctionDomain>();
		// ��ȡICD�ļ��е�������
		List<Entity> entities = icdManager.getAllFunctionDomains();
		for (Entity entity : entities) {
			FunctionDomain functionDomain = (FunctionDomain) entity;
			ICDFunctionDomain icdDomain = new ICDFunctionDomain();
			icdDomains.add(icdDomain);
			try {
				icdDomain.setAttributes(functionDomain.getAttributes());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				break;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				break;
			}
			// ��ȡ���������Ϣ��ת��ΪICDFunctionDomainMsg
			convertFromFunctionDomain(icdManager, icdDomain, functionDomain);

			// ��ȡ���µ��������򲢽���ת��ΪICDFunctionSubDomain����
			for (Entity subDomain : getAllFunctionSubDomainsOfFunctionbDomain(entity)) {
				convertFromFunctionSubDomain(icdManager, (FunctionSubDomain) subDomain, icdDomain);
			}

		}

		return icdDomains;
	}

	/**
	 * ��FunctionSubDomainת��ΪICDFunctionSubDomain����
	 * 
	 * @param icdManager
	 * @param subDomain
	 * @param icdDomain
	 */
	public static void convertFromFunctionSubDomain(ClazzManager icdManager, FunctionSubDomain subDomain, ICDFunctionDomain icdDomain) {
		ICDFunctionSubDomain icdSubDomain = new ICDFunctionSubDomain(icdDomain);
		icdDomain.addSubDomain(icdSubDomain);
		try {
			icdSubDomain.setAttributes(subDomain.getAttributes());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return;
		}
		// ��ȡ�����������Ϣ��ת��ΪICDFunctionSubDomainMsg
		for (Entity logicChild : subDomain.getLogicChildren()) {
			if (logicChild instanceof FunctionSubDomainMsg) {
				convertFromFunctionSubDomainMsg(icdManager, icdSubDomain, (FunctionSubDomainMsg) logicChild);
			}
		}

		// ��ȡ�����µ����й��ܵ�Ԫ��ת��ΪICDFunctionCell����
		List<FunctionCell> functionCells = getAllFunctionCellsOfFunctionSubDomain((FunctionSubDomain) subDomain);
		for (FunctionCell cell : functionCells) {
			convertFromFunctionCell(icdSubDomain, icdManager, cell);
		}
		// ��ȡ�����µ����й��ܽڵ㲢ת��ΪICDFunctionNode����
		for (FunctionNode node : getAllFunctionNodesOfFunctionSubDomain((FunctionSubDomain) subDomain)) {
			convertFromFunctionNode(icdManager, icdSubDomain, node);
		}
	}

	/**
	 * ��ȡicd�ļ�����·��
	 * 
	 * @param entity
	 *            ��������
	 * @return ���޷��ҵ�ƥ��Ľڵ�ʱ���᷵��null</br>
	 */
	public static String getICDOfTestCase(Entity entity) {
		if (entity instanceof TestCase) {
			List<Entity> entities = entity.getChildren();
			for (Entity en : entities) {
				if (en instanceof ICDFile) {
					IPath path = new Path(((ICDFile) en).getFile());
					IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
					return file.getLocation().toString();
				}
			}
		}
		return null;
	}

	/**
	 * ��ȡ�����µĲ㼶��Ϣ
	 * 
	 * @param entity
	 *            ���� </br>
	 */
	public static List<Entity> getAllLayersOfFigther(Entity entity) {
		List<Entity> entities = new ArrayList<Entity>();
		if (entity == null) {
			logger.warning("����Ĳ���Ϊ�ա�");
			return entities;
		}
		if (entity instanceof Fighter) {
			List<Entity> children = entity.getChildren();
			for (Entity child : children) {
				if (child instanceof LayerTotal) {
					entities.addAll(child.getChildren());
					break;
				}
			}
		} else {
			logger.warning("��������Ĳ�������ΪFighter��ȴ����" + entity.getClass().getName());
		}

		return entities;
	}

	/**
	 * ��ȡ�����µĲ㼶�� ����
	 * 
	 * @param entity
	 *            ���� </br>
	 */
	public static List<String> getAllLayersNameOfFigther(Entity entity) {
		List<String> names = new ArrayList<String>();
		for (Entity en : getAllLayersOfFigther(entity)) {
			if (en instanceof LayerInfo) {
				names.add(((LayerInfo) en).getLayerName());
			}
		}
		return names;
	}

	/**
	 * ��ȡ�����µĲ㼶�ı���
	 * 
	 * @param entity
	 *            ����
	 * @param layer
	 *            ���� </br>
	 */
	public static String getLevelOfFigther(Entity entity, String layer) {
		String level = null;
		List<Entity> entities = getAllLayersOfFigther(entity);
		for (Entity en : entities) {
			if (en instanceof LayerInfo) {
				if (((LayerInfo) en).getLayerName().equals(layer)) {
					level = ((LayerInfo) en).getLayerCode();
					break;
				}
			}
		}

		return level;
	}

	/**
	 * ��ȡ�����µ����й�����
	 * 
	 * @param entity
	 *            ����
	 */
	public static List<Entity> getAllFunctionDomainOfFighter(Entity entity) {
		List<Entity> entities = new ArrayList<Entity>();
		if (entity == null) {
			logger.warning("����Ĳ���Ϊ�ա�");
			return entities;
		}
		if (entity instanceof Fighter) {
			for (Entity en : entity.getChildren()) {
				if (en instanceof Version) {
					for (Entity ent : en.getChildren()) {
						if (ent instanceof FunctionDomains) {
							entities.addAll(ent.getChildren());
							break;
						}
					}
					break;
				}

			}
		} else {
			logger.warning("��������Ĳ�������ΪFighter��ȴ����" + entity.getClass().getName());
		}

		return entities;
	}

	/**
	 * ��ȡ�����µ����й�������
	 * 
	 * @param entity
	 *            ����
	 */
	public static List<Entity> getAllFunctionSubDomainOfFighter(Entity entity) {
		List<Entity> entities = new ArrayList<Entity>();
		if (entity == null) {
			logger.warning("����Ĳ���Ϊ�ա�");
			return entities;
		}
		if (entity instanceof Fighter) {
			for (Entity en : entity.getChildren()) {
				if (en instanceof Version) {
					for (Entity ent : en.getChildren()) {
						if (ent instanceof FunctionSubDomains) {
							entities.addAll(ent.getChildren());
							break;
						}
					}
					break;
				}

			}
		} else {
			logger.warning("��������Ĳ�������ΪFighter��ȴ����" + entity.getClass().getName());
		}
		return entities;
	}

	/**
	 * ��ȡ�����µ����й��ܽڵ�
	 * 
	 * @param entity
	 *            ����
	 */
	public static List<Entity> getAllFunctionNodeOfFighter(Entity entity) {
		List<Entity> entities = new ArrayList<Entity>();
		if (entity == null) {
			logger.warning("����Ĳ���Ϊ�ա�");
			return entities;
		}
		if (entity instanceof Fighter) {
			for (Entity en : entity.getChildren()) {
				if (en instanceof Version) {
					for (Entity ent : en.getChildren()) {
						if (ent instanceof FunctionNodes) {
							entities.addAll(ent.getChildren());
							break;
						}
					}
					break;
				}
			}
		} else {
			logger.warning("��������Ĳ�������ΪFighter��ȴ����" + entity.getClass().getName());
		}
		return entities;
	}

	/**
	 * ��ȡ�����µ����й��ܵ�Ԫ
	 * 
	 * @param entity
	 *            ����
	 */
	public static List<Entity> getAllFunctionCellOfFighter(Entity entity) {
		List<Entity> entities = new ArrayList<Entity>();
		if (entity == null) {
			logger.warning("����Ĳ���Ϊ�ա�");
			return entities;
		}
		if (entity instanceof Fighter) {
			for (Entity en : entity.getChildren()) {
				if (en instanceof Version) {
					for (Entity ent : en.getChildren()) {
						if (ent instanceof FunctionCells) {
							entities.addAll(ent.getChildren());
							break;
						}
					}
					break;
				}
			}
		} else {
			logger.warning("��������Ĳ�������ΪFighter��ȴ����" + entity.getClass().getName());
		}
		return entities;
	}

	/**
	 * ��ȡ������ѡ������͵����нڵ�
	 * 
	 * @param entity
	 *            ����
	 */
	public static List<Entity> getSelectTypeofFighter(Entity entity, String type) {
		List<Entity> entities = new ArrayList<Entity>();
		if (null == type || null == entity) {
			logger.warning("����Ĳ���Ϊ�ա�");
			return entities;
		}
		if (type.equals(FUNCTION_DOMAIN)) {
			entities = getAllFunctionDomainOfFighter(entity);
		} else if (type.equals(FUNCTION_SUB_DOMAIN)) {
			entities = getAllFunctionSubDomainOfFighter(entity);
		} else if (type.equals(FUNCTION_CELL)) {
			entities = getAllFunctionCellOfFighter(entity);
		} else if (type.equals(FUNCTION_NODE)) {
			entities = getAllFunctionNodeOfFighter(entity);
		}
		return entities;
	}

	/**
	 * ���ظû����ж�Ӧ�Ĳ��Զ����б�
	 * 
	 * @param fighter
	 *            icd�еĻ���
	 * @param testObjects
	 *            ���������ļ��е�TestedObjects
	 */
	public static List<Entity> getTestedObjectsOfICD(Entity fighter, Entity testObjects) {
		List<Entity> entities = new ArrayList<Entity>();
		if (null == fighter || null == testObjects) {
			logger.warning("����Ĳ���Ϊ�ա�");
			return entities;
		}
		if (fighter instanceof Fighter) {
			if (testObjects instanceof TestedObjects) {
				List<Entity> layersList = getAllLayersOfFigther(fighter);
				String level = ((TestedObjects) testObjects).getLevel();
				for (Entity en : layersList) {
					if (en instanceof LayerInfo) {
						if (((LayerInfo) en).getLayerCode().equals(level)) {
							String layerName = ((LayerInfo) en).getLayerName();
							List<Entity> selectTypeNodes = getSelectTypeofFighter(fighter, layerName);
							List<Entity> testObjectsChildren = testObjects.getChildren();
							for (Entity s : selectTypeNodes) {
								for (Entity t : testObjectsChildren) {
									if (t instanceof TestedObject) {
										String id = ((TestedObject) t).getId();
										String name = ((TestedObject) t).getName();
										if (s instanceof FunctionDomain) {
											if (((FunctionDomain) s).getName().equals(name) && ((FunctionDomain) s).getID().toString().equals(id)) {
												entities.add(s);
											}
										} else if (s instanceof FunctionSubDomain) {
											if (((FunctionSubDomain) s).getName().equals(name) && ((FunctionSubDomain) s).getID().toString().equals(id)) {
												entities.add(s);
											}
										} else if (s instanceof FunctionCell) {
											if (((FunctionCell) s).getName().equals(name) && ((FunctionCell) s).getID().toString().equals(id)) {
												entities.add(s);
											}
										} else if (s instanceof FunctionNode) {
											if (((FunctionNode) s).getName().equals(name) && ((FunctionNode) s).getID().toString().equals(id)) {
												entities.add(s);
											}
										}

									}
								}
							}

						}
					}
				}

			} else {
				logger.warning("��������Ĳ�������ΪTestedObjects��ȴ����" + testObjects.getClass().getName());
			}

		} else {
			logger.warning("��������Ĳ�������ΪFighter��ȴ����" + fighter.getClass().getName());
		}
		return entities;
	}

	/**
	 * ��ȡ�����µİ汾��Ϣ
	 * 
	 * @param entity
	 *            ����
	 */
	public static String getVersionIDOfFighter(Entity entity) {
		String ID = null;
		if (entity == null) {
			logger.warning("����Ĳ���Ϊ�ա�");
			return ID;
		}
		if (entity instanceof Fighter) {
			for (Entity en : entity.getChildren()) {
				if (en instanceof Version) {
					ID = ((Version) en).getVersionCode();
				}
			}
		} else {
			logger.warning("��������Ĳ�������ΪFighter��ȴ����" + entity.getClass().getName());
		}
		return ID;
	}

	/**
	 * ��ȡICD�ļ��İ汾��
	 * 
	 * @param icdManager
	 * @return
	 */
	public static String getVerion(ClazzManager icdManager) {
		Entity entity = icdManager.getFighter();
		return getVersionIDOfFighter(entity);
	}

	/**
	 * ��ȡ���������µ����й��ܽڵ�
	 * 
	 * @param entity
	 *            ��������
	 * @return
	 */
	public static List<FunctionNode> getAllFunctionNodesOfFunctionSubDomain(FunctionSubDomain entity) {
		List<FunctionNode> entities = new ArrayList<FunctionNode>();
		if (entity == null) {
			logger.warning("����Ĳ���Ϊ�ա�");
			return entities;
		}
		logger.fine("���������:\n" + entity.toString());
		List<Entity> logicChildren = entity.getLogicChildren();
		logger.fine("�߼��ӽڵ�ĸ���Ϊ:" + logicChildren.size());
		for (Entity logicChild : logicChildren) {
			if (logicChild instanceof FunctionNode) {
				logger.fine("���ܽڵ������Ϊ:" + logicChild.toString());
				entities.add((FunctionNode) logicChild);
			}
		}

		return entities;
	}

	/**
	 * ��ȡ���������µ����й��ܵ�Ԫ
	 * 
	 * @param entity
	 *            ��������
	 * @return
	 */
	public static List<FunctionCell> getAllFunctionCellsOfFunctionSubDomain(FunctionSubDomain entity) {
		List<FunctionCell> entities = new ArrayList<FunctionCell>();
		if (entity == null) {
			logger.warning("����һ���յĹ����������");
			return entities;
		}
		logger.fine("���������:\n" + entity.toString());
		List<Entity> logicChildren = entity.getLogicChildren();
		for (Entity logicChild : logicChildren) {
			if (logicChild instanceof FunctionCell) {
				logger.fine("���ܵ�Ԫ������Ϊ:" + logicChild.toString());
				entities.add((FunctionCell) logicChild);
			}
		}
		logger.fine("��ȡ���������µ����й��ܵ�Ԫ�ĸ���Ϊ:" + entities.size());
		return entities;
	}

	/**
	 * ��ȡ��������Ϣ������Ŀ��ID
	 * 
	 * @param msg
	 * @return
	 */
	private static List<Destnation> getDestnations(FunctionDomainMsg msg) {
		List<Destnation> destList = new ArrayList<Destnation>();
		List<Entity> children = msg.getChildren();
		Destnations dests = null;
		for (Entity child : children) {
			if (child instanceof Destnations) {
				dests = (Destnations) child;
				break;
			}
		}
		if (dests == null) {
			logger.warning("�ڹ�������Ϣ���Ҳ���Ŀ�Ľڵ�");
			return destList;
		}
		children = dests.getChildren();
		for (Entity dest : children) {
			Destnation destnation = (Destnation) dest;
			destList.add(destnation);
		}
		return destList;
	}

	/**
	 * ��ȡ���ܽڵ���Ϣ�µ�����Ŀ�Ĺ���ID
	 * 
	 * @param msg
	 * @return </br>
	 */
	public static List<Destnation> getDestnations(FunctionNodeMsg msg) {
		List<Destnation> destList = new ArrayList<Destnation>();
		List<Entity> children = msg.getChildren();
		Destnations dests = null;
		for (Entity child : children) {
			if (child instanceof Destnations) {
				dests = (Destnations) child;
				break;
			}
		}
		if (dests == null) {
			logger.warning("�ڹ��ܽڵ���Ϣ���Ҳ���Ŀ�Ľڵ�");
			return destList;
		}
		children = dests.getChildren();
		for (Entity dest : children) {
			Destnation destnation = (Destnation) dest;
			destList.add(destnation);
		}
		return destList;
	}

	/**
	 * ��ȡ���ܵ�Ԫ��Ϣ�µ�����Ŀ�Ĺ���ID
	 * 
	 * @param msg
	 * @return </br>
	 */
	private static List<Destnation> getDestnations(FunctionCellMsg msg) {
		List<Destnation> destList = new ArrayList<Destnation>();
		List<Entity> children = msg.getChildren();
		Destnations dests = null;
		for (Entity child : children) {
			if (child instanceof Destnations) {
				dests = (Destnations) child;
				break;
			}
		}
		if (dests == null) {
			logger.warning("�ڹ��ܵ�Ԫ��Ϣ���Ҳ���Ŀ�Ľڵ�");
			return destList;
		}
		children = dests.getChildren();
		for (Entity dest : children) {
			Destnation destnation = (Destnation) dest;
			destList.add(destnation);
		}
		return destList;
	}

	/**
	 * ��ȡ����������Ϣ�µ�����Ŀ�Ĺ���ID
	 * 
	 * @param msg
	 * @return </br>
	 */
	private static List<Destnation> getDestnations(FunctionSubDomainMsg msg) {
		List<Destnation> destList = new ArrayList<Destnation>();
		List<Entity> children = msg.getChildren();
		Destnations dests = null;
		for (Entity child : children) {
			if (child instanceof Destnations) {
				dests = (Destnations) child;
				break;
			}
		}
		if (dests == null) {
			logger.warning("�ڹ���������Ϣ���Ҳ���Ŀ�Ľڵ�");
			return destList;
		}
		children = dests.getChildren();
		for (Entity dest : children) {
			Destnation destnation = (Destnation) dest;
			destList.add(destnation);
		}

		return destList;
	}

	/**
	 * ��ȡ���ܽڵ���Ϣ�µ�����Ŀ�Ĺ���ID
	 * 
	 * @param clazzManager
	 * @param msg
	 * @return </br>
	 */
	private static List<Destnation> getDestFunctionNodes(ClazzManager clazzManager, /* FunctionNodeMsg */Entity msg) {
		// Ŀ��ID����
		List<Destnation> destList = new ArrayList<Destnation>();
		List<Entity> children = msg.getChildren();
		Destnations dests = null;
		for (Entity child : children) {
			if (child instanceof Destnations) {
				dests = (Destnations) child;
				break;
			}
		}

		if (dests == null) {
			logger.warning("�ڹ��ܽڵ���Ϣ���Ҳ���Ŀ�Ľڵ�");
			return destList;
		}

		children = dests.getChildren();
		for (Entity dest : children) {
			Destnation destnation = (Destnation) dest;
			destList.add(destnation);
		}

		logger.fine("����Ŀ��IDs�ĸ���Ϊ��" + destList.size());

		return destList;
	}

	/**
	 * ��ȡ���ܽڵ���Ϣ�µ�����Ŀ�Ĺ���ID������Ƿ�����Ϣ����Ҫ���˵����Թ��ߣ�����ǽ�����Ϣ����Ҫ���˵��������
	 * 
	 * @param clazzManager
	 * @param msg
	 * @param testedObjects
	 * @return </br>
	 */
	private static List<Destnation> getDestFunctionNodes(ClazzManager clazzManager, FunctionNodeMsg msg, List<Entity> testedObjects) {
		// Ŀ��ID����
		List<Destnation> destList = getDestFunctionNodes(clazzManager, msg);

		// �ж��Ƿ�����Ϣ���ǽ�����Ϣ������Ƿ�����Ϣ����Ҫ���˵����Թ��ߣ�����ǽ�����Ϣ����Ҫ���˵��������
		if (testedObjects.size() > 0) {
			try {
				boolean send = isSendMsg(msg, testedObjects);
				boolean recv = isRecvMsg(msg, testedObjects);
				if (recv) {// ������Ϣ
					// ���˵�������󼯺��еĹ��ܽڵ�
					for (Entity entity : testedObjects) {
						if (entity instanceof FunctionCell) {
							logger.warning("�û������ܵ�Ԫ����Ϊ�������");
							return destList;
						}

						FunctionNode node = (FunctionNode) entity;
						Iterator<Destnation> it = destList.iterator();
						while (it.hasNext()) {
							Destnation dest = it.next();
							if (node.getID().equals(dest.getDestID())) {
								it.remove();
							}
						}
					}
				} else if (send) {// ������Ϣ
					// ���˵����Թ��߼����еĹ��ܽڵ�
					for (Entity entity : testedObjects) {
						if (entity instanceof FunctionCell) {
							logger.warning("�û������ܵ�Ԫ����Ϊ�������");
							return destList;
						}

						FunctionNode node = (FunctionNode) entity;
						Iterator<Destnation> it = destList.iterator();
						while (it.hasNext()) {
							Destnation dest = it.next();
							if (!node.getID().equals(dest.getDestID())) {
								it.remove();
							}
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		logger.fine("����Ŀ��IDs�ĸ���Ϊ��" + destList.size());

		return destList;
	}

	/**
	 * ��ȡ���ܵ�Ԫ��Ϣ�µ�����Ŀ�Ĺ���ID
	 * 
	 * @param msg
	 * @return </br>
	 * @throws Exception
	 */
	private static List<Destnation> getDestFunctionCells(ClazzManager clazzManager, FunctionCellMsg msg, List<Entity> testedObjects) throws Exception {
		List<Destnation> destList = new ArrayList<Destnation>();
		List<Entity> children = msg.getChildren();
		Destnations dests = null;
		for (Entity child : children) {
			if (child instanceof Destnations) {
				dests = (Destnations) child;
				break;
			}
		}
		if (dests == null) {
			logger.warning("�ڹ��ܵ�Ԫ��Ϣ���Ҳ���Ŀ�Ľڵ�");
			return destList;
		}
		children = dests.getChildren();
		for (Entity dest : children) {
			Destnation destnation = (Destnation) dest;
			destList.add(destnation);
		}
		// �ж��Ƿ�����Ϣ���ǽ�����Ϣ������Ƿ�����Ϣ����Ҫ���˵����Թ��ߣ�����ǽ�����Ϣ����Ҫ���˵��������
		if (testedObjects.size() > 0) {
			boolean send = isSendMsg(msg, testedObjects);
			boolean recv = isRecvMsg(msg, testedObjects);
			if (recv) {// ������Ϣ
				// ���˵�������󼯺��еĹ��ܵ�Ԫ
				for (Entity entity : testedObjects) {
					FunctionCell domain = (FunctionCell) entity;
					Iterator<Destnation> it = destList.iterator();
					while (it.hasNext()) {
						Destnation dest = it.next();
						if (domain.getID().equals(dest.getDestID())) {
							it.remove();
						}
					}
				}
			} else if (send) {// ������Ϣ
				// ���˵����Թ��߼����еĹ��ܵ�Ԫ
				for (Entity entity : testedObjects) {
					FunctionCell domain = (FunctionCell) entity;
					Iterator<Destnation> it = destList.iterator();
					while (it.hasNext()) {
						Destnation dest = it.next();
						if (!domain.getID().equals(dest.getDestID())) {
							it.remove();
						}
					}
				}
			}

		}
		logger.config("����Ŀ��IDs�ĸ���Ϊ��" + destList.size());

		return destList;
	}

	/**
	 * ��ȡ����������Ϣ�µ�����Ŀ�Ĺ���ID
	 * 
	 * @param clazzManager
	 * @param msg
	 * @return </br>
	 */
	private static List<Destnation> getDestFunctionSubDomains(ClazzManager clazzManager, FunctionSubDomainMsg msg) {
		List<Destnation> destList = new ArrayList<Destnation>();
		List<Entity> children = msg.getChildren();
		Destnations dests = null;
		for (Entity child : children) {
			if (child instanceof Destnations) {
				dests = (Destnations) child;
				break;
			}
		}
		if (dests == null) {
			logger.warning("�ڹ���������Ϣ���Ҳ���Ŀ�Ľڵ�");
			return destList;
		}
		children = dests.getChildren();
		for (Entity dest : children) {
			Destnation destnation = (Destnation) dest;
			destList.add(destnation);
		}

		return destList;
	}

	/**
	 * ��ȡ����������Ϣ�µ�����Ŀ�Ĺ���ID�������ϢΪ������Ϣ������Ҫ���˵����Թ��ߣ����Ϊ������Ϣ����Ҫ���˵��������
	 * 
	 * @param clazzManager
	 * @param msg
	 * @param testedObjects
	 *            ������󼯺�
	 * @return </br>
	 */
	private static List<Destnation> getDestFunctionSubDomains(ClazzManager clazzManager, FunctionSubDomainMsg msg, List<Entity> testedObjects) {
		List<Destnation> destList = getDestFunctionSubDomains(clazzManager, msg);

		// �ж��Ƿ�����Ϣ���ǽ�����Ϣ������Ƿ�����Ϣ����Ҫ���˵����Թ��ߣ�����ǽ�����Ϣ����Ҫ���˵��������
		if (testedObjects.size() > 0) {
			try {
				boolean send = isSendMsg(msg, testedObjects);
				boolean recv = isRecvMsg(msg, testedObjects);
				if (recv) {// ������Ϣ
					// ���˵�������󼯺��еĹ�������
					for (Entity entity : testedObjects) {
						FunctionSubDomain domain = (FunctionSubDomain) entity;
						Iterator<Destnation> it = destList.iterator();
						while (it.hasNext()) {
							Destnation dest = it.next();
							if (domain.getID().equals(dest.getDestID())) {
								it.remove();
							}
						}
					}
				} else if (send) {// ������Ϣ
					// ���˵����Թ��߼����еĹ�������
					for (Entity entity : testedObjects) {
						FunctionSubDomain domain = (FunctionSubDomain) entity;
						Iterator<Destnation> it = destList.iterator();
						while (it.hasNext()) {
							Destnation dest = it.next();
							if (!domain.getID().equals(dest.getDestID())) {
								it.remove();
							}
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		logger.fine("����Ŀ��IDs�ĸ���Ϊ��" + destList.size());

		return destList;
	}

	/**
	 * ��ȡ��������Ϣ�µ�����Ŀ�Ĺ���ID
	 * 
	 * @param clazzManager
	 * @param msg
	 * @param testedObjects
	 * @return
	 */
	private static List<Destnation> getDestFunctionDomains(ClazzManager clazzManager, FunctionDomainMsg msg, List<Entity> testedObjects) {
		List<Destnation> destList = new ArrayList<Destnation>();
		List<Entity> children = msg.getChildren();
		Destnations dests = null;
		for (Entity child : children) {
			if (child instanceof Destnations) {
				dests = (Destnations) child;
				break;
			}
		}
		if (dests == null) {
			logger.warning("�ڹ�������Ϣ���Ҳ���Ŀ�Ľڵ�");
			return destList;
		}
		children = dests.getChildren();
		for (Entity dest : children) {
			Destnation destnation = (Destnation) dest;
			destList.add(destnation);
		}
		// �ж��Ƿ�����Ϣ���ǽ�����Ϣ������Ƿ�����Ϣ����Ҫ���˵����Թ��ߣ�����ǽ�����Ϣ����Ҫ���˵��������
		if (testedObjects.size() > 0) {
			try {
				boolean send = isSendMsg(msg, testedObjects);
				boolean recv = isRecvMsg(msg, testedObjects);
				if (recv) {// ������Ϣ
					// ���˵�������󼯺��еĹ�����
					for (Entity entity : testedObjects) {
						FunctionDomain domain = (FunctionDomain) entity;
						Iterator<Destnation> it = destList.iterator();
						while (it.hasNext()) {
							Destnation dest = it.next();
							if (domain.getID().equals(dest.getDestID())) {
								it.remove();
							}
						}
					}
				} else if (send) {// ������Ϣ
					// ���˵����Թ��߼����еĹ�����
					for (Entity entity : testedObjects) {
						FunctionDomain domain = (FunctionDomain) entity;
						Iterator<Destnation> it = destList.iterator();
						while (it.hasNext()) {
							Destnation dest = it.next();
							if (!domain.getID().equals(dest.getDestID())) {
								it.remove();
							}
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		logger.config("����Ŀ��IDs�ĸ���Ϊ��" + destList.size());

		return destList;
	}

	/**
	 * �жϹ�����Ϣ�Ƿ�Ϊ������Ϣ�������ϢΪ�㲥��Ϣ����Ӧ�õ���������Ϣ����
	 * 
	 * @param functionMsg
	 *            ������Ϣ
	 * @param testedObjects
	 *            ������󼯺�
	 * @return ����Ƿ�����Ϣ����true�����򷵻�false</br>
	 * @throws Exception
	 *             ���޷��ж���Ϣ�Ƿ�Ϊ������Ϣʱ���׳����쳣
	 */
	private static boolean isSendMsg(Entity functionMsg, List<Entity> testedObjects) {
		if (testedObjects == null || testedObjects.size() == 0) {

			return false;
		}

		if (functionMsg instanceof FunctionDomainMsg) {// ��������Ϣ

			return isSendMsg((FunctionDomainMsg) functionMsg, testedObjects);

		} else if (functionMsg instanceof FunctionSubDomainMsg) {// ����������Ϣ

			return isSendMsg((FunctionSubDomainMsg) functionMsg, testedObjects);

		} else if (functionMsg instanceof FunctionCellMsg) {// ���ܵ�Ԫ��Ϣ

			return isSendMsg((FunctionCellMsg) functionMsg, testedObjects);

		} else if (functionMsg instanceof FunctionNodeMsg) {// ���ܽڵ���Ϣ

			return isSendMsg((FunctionNodeMsg) functionMsg, testedObjects);

		}
		logger.warning("�޷�ȷ���Ƿ�����Ϣ���ǽ�����Ϣ");

		return false;
	}

	/**
	 * �жϹ�����Ϣ�Ƿ�Ϊ������Ϣ�������ϢΪ�㲥��Ϣ����Ӧ�õ���������Ϣ����
	 * 
	 * @param functionMsg
	 *            ������Ϣ
	 * @param testedObjects
	 *            ������󼯺�
	 * @return ����Ƿ�����Ϣ����true�����򷵻�false</br>
	 * @throws Exception
	 *             ���޷��ж���Ϣ�Ƿ�Ϊ������Ϣʱ���׳����쳣
	 */
	private static boolean isRecvMsg(Entity functionMsg, List<Entity> testedObjects) {
		if (testedObjects == null || testedObjects.size() == 0) {

			return false;
		}

		if (functionMsg instanceof FunctionDomainMsg) {// ��������Ϣ

			return isRecvMsg((FunctionDomainMsg) functionMsg, testedObjects);

		} else if (functionMsg instanceof FunctionSubDomainMsg) {// ����������Ϣ

			return isRecvMsg((FunctionSubDomainMsg) functionMsg, testedObjects);

		} else if (functionMsg instanceof FunctionCellMsg) {// ���ܵ�Ԫ��Ϣ

			return isRecvMsg((FunctionCellMsg) functionMsg, testedObjects);

		} else if (functionMsg instanceof FunctionNodeMsg) {// ���ܽڵ���Ϣ

			return isRecvMsg((FunctionNodeMsg) functionMsg, testedObjects);

		}
		logger.warning("�޷�ȷ����Ϣ�ǽ�����Ϣ");

		return false;
	}

	/**
	 * �жϹ�������Ϣ�Ƿ�Ϊ������Ϣ
	 * 
	 * @param domainMsg
	 * @param testedObjects
	 * @return
	 */
	private static boolean isSendMsg(FunctionDomainMsg domainMsg, List<Entity> testedObjects) {
		/*
		 * ��Ҫ�ж��Ƿ�����Ϣ���ǽ�����Ϣ�� ����Ϣ��ԴID�뱻���Զ����ID���ʱ����Ϊ������Ϣ������Ϣ�Ĺ���Ŀ��ID��
		 * �����Զ����ID���ʱ����Ϊ������Ϣ��
		 */
		// �ж��Ƿ�Ϊ������Ϣ
		FunctionDomainMsg msg = domainMsg;
		// �ж��Ƿ�Ϊ������Ϣ
		// ��ȡ��Ϣ�ķ���Ŀ��IDs
		List<Destnation> destList = getDestnations(msg);
		for (Destnation dest : destList) {
			// ����Ŀ��ID
			Integer destID = dest.getDestID();
			for (Entity testedObject : testedObjects) {
				if (destID.equals(testedObject.getFieldValue("ID")))
					return true;
			}
		}
		return false;
	}

	/**
	 * �жϹ�������Ϣ�Ƿ�Ϊ������Ϣ
	 * 
	 * @param domainMsg
	 * @param testedObjects
	 * @return
	 */
	private static boolean isRecvMsg(FunctionDomainMsg domainMsg, List<Entity> testedObjects) {
		/*
		 * ��Ҫ�ж��Ƿ�����Ϣ���ǽ�����Ϣ�� ����Ϣ��ԴID�뱻���Զ����ID���ʱ����Ϊ������Ϣ������Ϣ�Ĺ���Ŀ��ID��
		 * �����Զ����ID���ʱ����Ϊ������Ϣ��
		 */
		// �ж��Ƿ�Ϊ������Ϣ
		FunctionDomainMsg msg = domainMsg;
		Integer srcID = msg.getSourceFunctionID();// ԴID
		for (Entity function : testedObjects) {
			if (function instanceof FunctionDomain) {
				FunctionDomain domain = (FunctionDomain) function;
				if (srcID.equals(domain.getID())) {
					return true;// ������Ϣ
				}
			} else if (function instanceof FunctionSubDomain) {
				FunctionSubDomain domain = (FunctionSubDomain) function;
				if (srcID.equals(domain.getID())) {
					return true;// ������Ϣ
				}
			} else if (function instanceof FunctionCell) {
				FunctionCell domain = (FunctionCell) function;
				if (srcID.equals(domain.getID())) {
					return true;// ������Ϣ
				}
			} else if (function instanceof FunctionNode) {
				FunctionNode domain = (FunctionNode) function;
				if (srcID.equals(domain.getID())) {
					return true;// ������Ϣ
				}
			}
		}

		return false;
	}

	/**
	 * �жϹ���������Ϣ�Ƿ�Ϊ������Ϣ
	 * 
	 * @param subMsg
	 * @param testedObjects
	 * @return
	 */
	private static boolean isSendMsg(FunctionSubDomainMsg subMsg, List<Entity> testedObjects) {
		/*
		 * ��Ҫ�ж��Ƿ�����Ϣ���ǽ�����Ϣ�� ����Ϣ��ԴID�뱻���Զ����ID���ʱ����Ϊ������Ϣ������Ϣ�Ĺ���Ŀ��ID��
		 * �����Զ����ID���ʱ���򱻷��Ͷ���
		 */
		FunctionSubDomainMsg msg = subMsg;
		// �ж��Ƿ�Ϊ������Ϣ
		// ��ȡ��Ϣ�ķ���Ŀ��IDs
		List<Destnation> destList = getDestnations(msg);
		for (Destnation dest : destList) {
			// ����Ŀ��ID
			Integer destID = dest.getDestID();
			for (Entity testedObject : testedObjects) {
				if (destID.equals(testedObject.getFieldValue("ID")))
					return true;
			}
		}
		return false;
	}

	/**
	 * �жϹ���������Ϣ�Ƿ�Ϊ������Ϣ
	 * 
	 * @param subMsg
	 * @param testedObjects
	 * @return
	 */
	private static boolean isRecvMsg(FunctionSubDomainMsg subMsg, List<Entity> testedObjects) {
		/*
		 * ��Ҫ�ж��Ƿ�����Ϣ���ǽ�����Ϣ�� ����Ϣ��ԴID�뱻���Զ����ID���ʱ����Ϊ������Ϣ������Ϣ�Ĺ���Ŀ��ID��
		 * �����Զ����ID���ʱ���򱻷��Ͷ���
		 */
		// �ж��Ƿ�Ϊ������Ϣ
		FunctionSubDomainMsg msg = subMsg;
		Integer srcID = msg.getSourceFunctionID();// ԴID
		for (Entity function : testedObjects) {
			if (function instanceof FunctionSubDomain) {
				FunctionSubDomain domain = (FunctionSubDomain) function;
				if (srcID.equals(domain.getID())) {
					return true;// ������Ϣ
				}
			}
		}
		return false;
	}

	/**
	 * �жϹ��ܵ�Ԫ��Ϣ�Ƿ�Ϊ������Ϣ
	 * 
	 * @param cellMsg
	 * @param testedObjects
	 * @return
	 */
	private static boolean isSendMsg(FunctionCellMsg cellMsg, List<Entity> testedObjects) {
		String brocast = cellMsg.getBrocast();
		if ("BROADCAST".equals(brocast)) {
			for (Entity testedObject : testedObjects) {
				if (cellMsg.getSourceFunctionID().equals(testedObject.getFieldValue("ID"))) {
					return false;
				}
			}
			return true;
		}

		/*
		 * ��Ҫ�ж��Ƿ�����Ϣ���ǽ�����Ϣ�� ����Ϣ��ԴID�뱻���Զ����ID���ʱ����Ϊ������Ϣ������Ϣ�Ĺ���Ŀ��ID��
		 * �����Զ����ID���ʱ���򱻷��Ͷ���
		 */
		// �ж��Ƿ�Ϊ������Ϣ
		FunctionCellMsg msg = cellMsg;
		// �ж��Ƿ�Ϊ������Ϣ
		// ��ȡ��Ϣ�ķ���Ŀ��IDs

		List<Destnation> destList = getDestnations(msg);

		for (Destnation dest : destList) {
			// ����Ŀ��ID
			Integer destID = dest.getDestID();
			for (Entity testedObject : testedObjects) {
				if (destID.equals(testedObject.getFieldValue("ID")))
					return true;
			}
		}

		return false;
	}

	/**
	 * �жϹ��ܵ�Ԫ��Ϣ�Ƿ�Ϊ������Ϣ
	 * 
	 * @param cellMsg
	 * @param testedObjects
	 * @return
	 */
	private static boolean isRecvMsg(FunctionCellMsg cellMsg, List<Entity> testedObjects) {
		/*
		 * ��Ҫ�ж��Ƿ�����Ϣ���ǽ�����Ϣ�� ����Ϣ��ԴID�뱻���Զ����ID���ʱ����Ϊ������Ϣ������Ϣ�Ĺ���Ŀ��ID��
		 * �����Զ����ID���ʱ���򱻷��Ͷ���
		 */
		// �ж��Ƿ�Ϊ������Ϣ
		FunctionCellMsg msg = cellMsg;
		Integer srcID = msg.getSourceFunctionID();// ԴID
		for (Entity function : testedObjects) {
			if (function instanceof FunctionCell) {
				FunctionCell domain = (FunctionCell) function;
				if (srcID.equals(domain.getID())) {
					return true;// ������Ϣ
				}
			}
		}

		return false;
	}

	/**
	 * �жϹ��ܽڵ���Ϣ�Ƿ�Ϊ������Ϣ
	 * 
	 * @param nodeMsg
	 * @param testedObjects
	 * @return
	 */
	public static boolean isSendMsg(FunctionNodeMsg nodeMsg, List<Entity> testedObjects) {
		String brocast = nodeMsg.getBrocast();
		if ("BROADCAST".equals(brocast)) {
			for (Entity testedObject : testedObjects) {
				if (nodeMsg.getSourceFunctionID().equals(testedObject.getFieldValue("ID"))) {
					return false;
				}
			}
			return true;
		}

		/*
		 * ��Ҫ�ж��Ƿ�����Ϣ���ǽ�����Ϣ�� ����Ϣ��ԴID�뱻���Զ����ID���ʱ����Ϊ������Ϣ������Ϣ�Ĺ���Ŀ��ID��
		 * �����Զ����ID���ʱ���򱻷��Ͷ���
		 */
		// �ж��Ƿ�Ϊ������Ϣ
		// ��ȡ��Ϣ�ķ���Ŀ��IDs
		List<Destnation> destList = getDestnations(nodeMsg);
		// �ж��Ƿ�Ϊ�㲥��Ϣ������ǣ����ж���Ϣ�ķ���Դ�Ƿ�Ϊ�������������ǣ���Ϊ������Ϣ
		for (Destnation dest : destList) {
			// ����Ŀ��ID
			Integer destID = dest.getDestID();
			for (Entity testedObject : testedObjects) {
				if (destID.equals(testedObject.getFieldValue("ID")))
					return true;
			}
		}

		return false;
	}

	/**
	 * �жϹ��ܽڵ���Ϣ�Ƿ�Ϊ������Ϣ
	 * 
	 * @param nodeMsg
	 * @param testedObjects
	 * @return
	 */
	public static boolean isRecvMsg(FunctionNodeMsg nodeMsg, List<Entity> testedObjects) {
		/*
		 * ��Ҫ�ж��Ƿ�����Ϣ���ǽ�����Ϣ�� ����Ϣ��ԴID�뱻���Զ����ID���ʱ����Ϊ������Ϣ������Ϣ�Ĺ���Ŀ��ID��
		 * �����Զ����ID���ʱ���򱻷��Ͷ���
		 */
		// �ж��Ƿ�Ϊ������Ϣ
		FunctionNodeMsg msg = nodeMsg;
		Integer srcID = msg.getSourceFunctionID();// ԴID
		for (Entity function : testedObjects) {
			if (function instanceof FunctionNode) {
				FunctionNode node = (FunctionNode) function;
				if (srcID.equals(node.getID())) {
					return true;// ������Ϣ
				}
			}
		}

		return false;
	}

	/**
	 * ��ȡ�������µ����й��ܽڵ�
	 * 
	 * @param domain
	 * @return
	 */
	public static List<FunctionNode> getAllFunctionNodes(FunctionDomain domain) {
		List<FunctionNode> nodes = new ArrayList<FunctionNode>();
		List<Entity> subDomains = getAllFunctionSubDomainsOfFunctionbDomain(domain);
		for (Entity subDomain : subDomains) {
			List<FunctionNode> entities = getAllFunctionNodesOfFunctionSubDomain((FunctionSubDomain) subDomain);
			for (Entity entity : entities) {
				if (nodes.indexOf(entity) < 0) {
					nodes.add((FunctionNode) entity);
				}
			}
		}
		return nodes;
	}

	/**
	 * ��ȡ�������µ����й��ܵ�Ԫ
	 * 
	 * @param domain
	 * @return
	 */
	private static List<FunctionCell> getAllFunctionCells(FunctionDomain domain) {
		List<FunctionCell> nodes = new ArrayList<FunctionCell>();
		List<Entity> subDomains = getAllFunctionSubDomainsOfFunctionbDomain(domain);
		for (Entity subDomain : subDomains) {
			List<FunctionCell> entities = getAllFunctionCellsOfFunctionSubDomain((FunctionSubDomain) subDomain);
			for (Entity entity : entities) {
				if (nodes.indexOf(entity) < 0) {
					nodes.add((FunctionCell) entity);
				}
			}
		}
		return nodes;
	}

	/**
	 * ��ȡĿ�Ĺ�������󼯺�
	 * 
	 * @param clazzManager
	 * @param destList
	 * @return
	 */
	private static List<FunctionDomain> getDestFunctionDomains(ClazzManager clazzManager, List<Destnation> destList) {
		List<Entity> domains = clazzManager.getAllFunctionDomains();
		List<FunctionDomain> nodes = new ArrayList<FunctionDomain>();
		for (Entity domain : domains) {
			FunctionDomain fd = (FunctionDomain) domain;
			for (Destnation dest : destList) {
				if (dest.getDestID().equals(fd.getID()))
					nodes.add(fd);
			}
		}
		return nodes;
	}

	/**
	 * ��ȡĿ�Ĺ���������󼯺�
	 * 
	 * @param clazzManager
	 * @param destList
	 * @return
	 */
	private static List<FunctionSubDomain> getDestFunctionSubDomains(ClazzManager clazzManager, List<Destnation> destList) {
		List<Entity> domains = clazzManager.getAllFunctionSubDomains();
		List<FunctionSubDomain> nodes = new ArrayList<FunctionSubDomain>();
		for (Entity domain : domains) {
			FunctionSubDomain fd = (FunctionSubDomain) domain;
			for (Destnation dest : destList) {
				if (dest.getDestID().equals(fd.getID()))
					nodes.add(fd);
			}
		}
		return nodes;
	}

	/**
	 * ��ȡĿ�Ĺ��ܽڵ���󼯺�
	 * 
	 * @param clazzManager
	 * @param destList
	 * @return
	 */
	private static List<FunctionNode> getDestFunctionNodes(ClazzManager clazzManager, List<Destnation> destList) {
		List<FunctionNode> nodes = new ArrayList<FunctionNode>();
		for (Entity entity : clazzManager.getAllFunctionNodes()) {
			if (entity instanceof FunctionNode) {
				FunctionNode node = (FunctionNode) entity;
				for (Destnation dest : destList) {
					if (dest.getDestID().equals(node.getID()))
						nodes.add(node);
				}
			}
		}
		return nodes;
	}

	/**
	 * ��ȡĿ�Ĺ��ܽڵ���󼯺�
	 * 
	 * @param clazzManager
	 * @param destList
	 * @return
	 */
	private static List<FunctionCell> getDestFunctionCells(ClazzManager clazzManager, List<Destnation> destList, List<Entity> testedObjects) {
		List<FunctionCell> cells = new ArrayList<FunctionCell>();
		for (Entity entity : clazzManager.getAllFunctionCells()) {
			if (entity instanceof FunctionCell) {
				FunctionCell cell = (FunctionCell) entity;
				for (Destnation dest : destList) {
					if (dest.getDestID().equals(cell.getID()))
						cells.add(cell);
				}
			}
		}
		return cells;
	}

	/**
	 * ��ȡ��������Ϣ�����й���������Ϣ
	 * 
	 * @param clazzManager
	 * @param domainMsg
	 * @return
	 */
	private static List<FunctionSubDomainMsg> getAllFunctionSubDomainMsgs(ClazzManager clazzManager, FunctionDomainMsg domainMsg) {
		List<FunctionSubDomainMsg> subDomainMsgs = clazzManager.getAllFunctionSubDomainMsgs();
		Iterator<FunctionSubDomainMsg> it = subDomainMsgs.iterator();
		while (it.hasNext()) {
			FunctionSubDomainMsg msg = it.next();
			if (msg.getParentMsg() != null && !msg.getParentMsg().equals(domainMsg))
				it.remove();
			else if (msg.getParentMsg() == null)
				it.remove();
		}

		return subDomainMsgs;
	}

	/**
	 * ��ȡ����������Ϣ�����й��ܽڵ���Ϣ
	 * 
	 * @param clazzManager
	 * @param subDomain
	 * @return
	 */
	private static List<FunctionNodeMsg> getAllFunctionNodeMsgs(ClazzManager clazzManager, FunctionSubDomainMsg subDomainMsg) {
		List<FunctionNodeMsg> nodeMsgs = clazzManager.getAllFunctionNodeMsgs();
		Iterator<FunctionNodeMsg> it = nodeMsgs.iterator();
		while (it.hasNext()) {
			FunctionNodeMsg msg = it.next();
			if (msg.getParentMsg() != null && !msg.getParentMsg().equals(subDomainMsg))
				it.remove();
			else if (msg.getParentMsg() == null) {
				it.remove();
			}
		}

		return nodeMsgs;
	}

	/**
	 * ��ȡ����������Ϣ�����й��ܵ�Ԫ��Ϣ
	 * 
	 * @param clazzManager
	 * @param subDomain
	 * @return
	 */
	private static List<FunctionCellMsg> getAllFunctionCellMsgs(ClazzManager clazzManager, FunctionSubDomainMsg subDomain) {
		List<FunctionCellMsg> nodeMsgs = clazzManager.getAllFunctionCellMsgs();
		Iterator<FunctionCellMsg> it = nodeMsgs.iterator();
		while (it.hasNext()) {
			FunctionCellMsg msg = it.next();
			if (msg.getParentMsg() != null && !msg.getParentMsg().equals(subDomain))
				it.remove();
			else if (msg.getParentMsg() == null)
				it.remove();
		}

		return nodeMsgs;
	}

	/**
	 * ��ȡSPTEMsg����
	 * 
	 * @param clazzManager
	 * @param message
	 *            ����������Ϣ
	 * @return
	 */
	public static SPTEMsg getSPTEMsg(ClazzManager clazzManager, Message message) {
		Integer topicId = Integer.valueOf(message.getTopicId());
		Integer srcId = Integer.valueOf(message.getSrcId());
		Entity nodeMsg = clazzManager.getFunctionNodeMsg(message.getId());
		// �Թ��ܵ�Ԫ���в���
		if (nodeMsg == null) {
			nodeMsg = clazzManager.getFunctionCellMsg(message.getId());
		}
		try {
			List<SPTEMsg> msgs = getSPTEMsgsOfFunctionMsg(clazzManager, TemplateEngine.getEngine().getUnitManager(), nodeMsg, new ArrayList<Entity>(), false);
			if (msgs.size() > 0) {
				msgs.get(0).setMsg(message);
				return msgs.get(0);
			}
			logger.warning("�����û������topicId=" + topicId + " srcId=" + srcId + " �Ҳ���SPTEMsg����");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * ��ȡSPTEMsg����
	 * 
	 * @param clazzManager
	 * @param topicId
	 *            ����ID
	 * @param srcId
	 *            ��ϢԴID
	 * @return
	 */
	public static SPTEMsg getSPTEMsg(ClazzManager clazzManager, Integer topicId, Integer srcId, String level) {

		return getSPTEMsg(clazzManager, TemplateEngine.getEngine().getUnitManager(), topicId, srcId, level);
	}

	/**
	 * ��ȡSPTEMsg����
	 * 
	 * @param clazzManager
	 *            icd�������
	 * @param unitManager
	 *            ��λ�������
	 * @param topicId
	 *            ����ID
	 * @param srcId
	 *            ��Ϣ��ԴID
	 * @return
	 */
	private static SPTEMsg getSPTEMsg(ClazzManager clazzManager, ClazzManager unitManager, Integer topicId, Integer srcId, String level) {
		Entity entity = null;
		if ("21".equals(level)) {
			entity = clazzManager.getFunctionCellMsg(srcId, topicId);
		}
		if ("22".equals(level)) {
			entity = clazzManager.getFunctionNodeMsg(srcId, topicId);
		}
		if (entity == null)
			return null;

		try {
			List<SPTEMsg> msgs = getSPTEMsgsOfFunctionMsg(clazzManager, unitManager, entity, new ArrayList<Entity>(), false);
			if (msgs.size() > 0) {
				return msgs.get(0);
			}
			logger.warning("�����û������topicId=" + topicId + " srcId" + srcId + " �Ҳ�����Ӧ��SPTEMsg����");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * ��ȡ��Ϣ��Ӧ���ڵ�Ĳ㼶
	 * 
	 * @param msg
	 *            ��Ϣ
	 * @return ��Ϣ��Ӧ���ڵ�Ĳ㼶
	 */
	public static String getParentLevelOfMsg(Helper msg) {
		String level = "";
		if (msg instanceof ICDFunctionDomainMsg) {
			level = FUNCTION_DOMAIN_LEVEL;
		} else if (msg instanceof ICDFunctionSubDomainMsg) {
			level = FUNCTION_SUB_DOMAIN_LEVEL;
		} else if (msg instanceof ICDFunctionNodeMsg) {
			level = FUNCTION_NODE_LEVEL;
		} else if (msg instanceof ICDFunctionCellMsg) {
			level = FUNCTION_CELL_LEVEL;
		}
		return level;
	}

	/**
	 * ��ȡ����������Ϣ����
	 * 
	 * @param clazzManager
	 *            icd���������
	 * @param msg
	 *            ��Ϣ
	 * @param testedObjects
	 *            �����Զ��󼯺�
	 * @param ignoreCell
	 *            �Ƿ���Թ��ܵ�Ԫ��Ϣ
	 * @return
	 * @throws Exception
	 *             ���޷��ж���Ϣ�Ƿ�Ϊ������Ϣʱ���׳����쳣
	 */
	public static List<SPTEMsg> filter(ClazzManager clazzManager, Object msg, List<Entity> testedObjects, boolean ignoreCell) throws Exception {
		if (msg instanceof ICDFunctionDomainMsg) {
			ICDFunctionDomainMsg icdDomainMsg = (ICDFunctionDomainMsg) msg;
			List<FunctionDomainMsg> msgs = clazzManager.getAllFunctionDomainMsgs();
			for (FunctionDomainMsg domainMsg : msgs) {
				if (domainMsg.getMsgID().equals(icdDomainMsg.getAttribute("msgID").getValue().toString()))
					return getSPTEMsgsOfFunctionMsg(clazzManager, TemplateEngine.getEngine().getUnitManager(), domainMsg, testedObjects, ignoreCell);
			}
			logger.warning("�Ҳ���msgID=" + icdDomainMsg.getAttribute("msgID").getValue() + "��������Ϣ��");
		} else if (msg instanceof ICDFunctionSubDomainMsg) {
			ICDFunctionSubDomainMsg icdSubMsg = (ICDFunctionSubDomainMsg) msg;
			List<FunctionSubDomainMsg> msgs = clazzManager.getAllFunctionSubDomainMsgs();
			for (FunctionSubDomainMsg subMsg : msgs) {
				if (subMsg.getMsgID().equals(icdSubMsg.getAttribute("msgID").getValue().toString()))
					return getSPTEMsgsOfFunctionMsg(clazzManager, TemplateEngine.getEngine().getUnitManager(), subMsg, testedObjects, ignoreCell);
			}
			logger.warning("�Ҳ���msgID=" + icdSubMsg.getAttribute("msgID").getValue() + "����������Ϣ��");
		} else if (msg instanceof ICDFunctionNodeMsg) {
			ICDFunctionNodeMsg nodeMsg = (ICDFunctionNodeMsg) msg;
			List<FunctionNodeMsg> msgs = clazzManager.getAllFunctionNodeMsgs();
			for (FunctionNodeMsg subMsg : msgs) {
				if (subMsg.getMsgID().equals(nodeMsg.getAttribute("msgID").getValue().toString()))
					return getSPTEMsgsOfFunctionMsg(clazzManager, TemplateEngine.getEngine().getUnitManager(), subMsg, testedObjects, ignoreCell);
			}
			logger.warning("�Ҳ���msgID=" + nodeMsg.getAttribute("msgID").getValue() + "���ܽڵ���Ϣ��");
		} else if (msg instanceof ICDFunctionCellMsg) {
			ICDFunctionCellMsg cellMsg = (ICDFunctionCellMsg) msg;
			List<FunctionCellMsg> msgs = clazzManager.getAllFunctionCellMsgs();
			for (FunctionCellMsg subMsg : msgs) {
				if (subMsg.getMsgID().equals(cellMsg.getAttribute("msgID").getValue().toString()))
					return getSPTEMsgsOfFunctionMsg(clazzManager, TemplateEngine.getEngine().getUnitManager(), subMsg, testedObjects, ignoreCell);
			}
			logger.warning("�Ҳ���msgID=" + cellMsg.getAttribute("msgID").getValue() + "���ܵ�Ԫ��Ϣ��");
		}

		logger.warning("����Ĳ������Ͳ���ȷ��");

		return new ArrayList<SPTEMsg>(0);
	}

	/**
	 * ��ȡ���ܶ�����Ϣ����Ӧ�Ĳ���������Ϣ���ϡ�
	 * 
	 * @param icdManager
	 *            ICD���������
	 * @param functionMsg
	 *            ������Ϣ
	 * @param testedObjects
	 *            �������
	 * @param ignoreCell
	 *            �Ƿ���Թ��ܵ�Ԫ
	 * @return
	 * @throws Exception
	 *             ���޷��ж���Ϣ�Ƿ�Ϊ������Ϣʱ���׳����쳣
	 */

	private static List<SPTEMsg> getSPTEMsgsOfFunctionMsg(ClazzManager icdManager, ClazzManager unitManager, Entity functionMsg, List<Entity> testedObjects, boolean ignoreCell) throws Exception {
		List<SPTEMsg> beans = new ArrayList<SPTEMsg>();
		Set<Entity> msgs = new HashSet<Entity>();
		List<Destnation> destList = null;// ����Ŀ��IDs
		boolean isSend = isSendMsg(functionMsg, testedObjects);
		boolean isRecv = isRecvMsg(functionMsg, testedObjects);
		if (functionMsg == null)
			return beans;
		if (functionMsg instanceof FunctionDomainMsg) {// ��������Ϣ
			FunctionDomainMsg domainMsg = (FunctionDomainMsg) functionMsg;
			destList = getDestFunctionDomains(icdManager, domainMsg, testedObjects);

		} else if (functionMsg instanceof FunctionSubDomainMsg) {// ����������Ϣ
			FunctionSubDomainMsg subMsg = (FunctionSubDomainMsg) functionMsg;
			destList = getDestFunctionSubDomains(icdManager, subMsg, testedObjects);

		} else if (functionMsg instanceof FunctionCellMsg) {// ���ܵ�Ԫ��Ϣ
			FunctionCellMsg cellMsg = (FunctionCellMsg) functionMsg;
			destList = getDestFunctionCells(icdManager, cellMsg, testedObjects);

		} else if (functionMsg instanceof FunctionNodeMsg) {// ���ܽڵ���Ϣ
			FunctionNodeMsg nodeMsg = (FunctionNodeMsg) functionMsg;
			destList = getDestFunctionNodes(icdManager, nodeMsg, testedObjects);

		} else {
			logger.warning("�޷��жϳ���Ϣ�����͡�class=" + functionMsg.getClass().getName());
			return beans;
		}

		if (functionMsg instanceof FunctionDomainMsg) {// ����Ϣ
			FunctionDomainMsg domainMsg = (FunctionDomainMsg) functionMsg;
			// ��ȡ����Ϣ������������Ϣ
			List<FunctionSubDomainMsg> subDomainMsgs = getAllFunctionSubDomainMsgs(icdManager, domainMsg);
			// ��ȡ���������µ����й��ܽڵ���Ϣ
			List<FunctionNodeMsg> nodeMsgs = new ArrayList<FunctionNodeMsg>();
			for (FunctionSubDomainMsg subDomainMsg : subDomainMsgs) {
				nodeMsgs.addAll(getAllFunctionNodeMsgs(icdManager, subDomainMsg));
			}

			/*
			 * ��ȡĿ�Ĺ���������µ����й��ܽڵ��빦�ܵ�ԪIDs
			 */
			List<FunctionDomain> destDomains = getDestFunctionDomains(icdManager, destList);// Ŀ�Ĺ��ܶ��󼯺�
			List<FunctionNode> nodes = new ArrayList<FunctionNode>();
			List<Integer> nodeIDs = new ArrayList<Integer>();// Ŀ��������й��ܽڵ�IDs
			for (FunctionDomain destDomain : destDomains) {
				nodes.addAll(getAllFunctionNodes(destDomain));
			}
			for (FunctionNode node : nodes) {
				nodeIDs.add(node.getID());
			}

			// ��ȡ���ܽڵ���Ϣ��Ŀ�ļ���
			for (FunctionNodeMsg msg : nodeMsgs) {
				List<Destnation> nodeDestList = getDestnations(msg);// Դ���ܽڵ���Ϣ��Ŀ��IDs
				if (msg.getBrocast().equals("BROADCAST")) {
					msgs.add(msg);
				} else {
					// ��Ϣ����
					for (Destnation dest : nodeDestList) {
						for (Integer nodeID : nodeIDs) {
							if (nodeID.equals(dest.getDestID()))
								msgs.add(msg);
						}
					}
				}

			}
			// ���δ���ú��Թ��ܵ�Ԫ��Ϣ�����ȡ���ܵ�Ԫ��Ϣ��Ŀ�ļ���
			if (!ignoreCell) {
				// ��ȡ���������µ����й��ܵ�Ԫ��Ϣ
				List<FunctionCellMsg> cellMsgs = new ArrayList<FunctionCellMsg>();
				for (FunctionSubDomainMsg subDomainMsg : subDomainMsgs) {
					cellMsgs.addAll(getAllFunctionCellMsgs(icdManager, subDomainMsg));
				}
				List<FunctionCell> cells = new ArrayList<FunctionCell>();
				nodeIDs = new ArrayList<Integer>();// Ŀ�Ĺ��ܵ�ԪIDs
				for (FunctionDomain destDomain : destDomains) {
					cells.addAll(getAllFunctionCells(destDomain));
				}

				for (FunctionCell cell : cells) {
					nodeIDs.add(cell.getID());
				}

				for (FunctionCellMsg msg : cellMsgs) {
					List<Destnation> nodeDestList = getDestnations(msg);// Դ���ܵ�Ԫ��Ϣ��Ŀ��IDs
					if (msg.getBrocast().equals("BROADCAST")) {
						msgs.add(msg);
					} else {
						// ��Ϣ����
						for (Destnation dest : nodeDestList) {
							for (Integer nodeID : nodeIDs) {
								if (nodeID.equals(dest.getDestID()))
									msgs.add(msg);
							}
						}
					}
				}
			}
		} else if (functionMsg instanceof FunctionSubDomainMsg) {// ����������Ϣ
			// ��ȡĿ�Ĺ��ܶ���s�µ����й��ܽڵ��빦�ܵ�Ԫ
			FunctionSubDomainMsg subMsg = (FunctionSubDomainMsg) functionMsg;
			List<FunctionSubDomain> destDomains = getDestFunctionSubDomains(icdManager, destList);// Ŀ�Ĺ��ܶ��󼯺�
			// ����������destDomains�е����й��ܽڵ��빦�ܵ�Ԫ��IDs
			List<FunctionNode> nodes = new ArrayList<FunctionNode>();
			List<Integer> nodeIDs = new ArrayList<Integer>();// Ŀ����������й��ܽڵ�IDs
			for (FunctionSubDomain destDomain : destDomains) {
				nodes.addAll(getAllFunctionNodesOfFunctionSubDomain(destDomain));
			}
			for (FunctionNode node : nodes) {
				nodeIDs.add(node.getID());
			}
			// ��ȡ���������µ����й��ܽڵ���Ϣ
			List<FunctionNodeMsg> nodeMsgs = getAllFunctionNodeMsgs(icdManager, subMsg);

			for (FunctionNodeMsg msg : nodeMsgs) {
				List<Destnation> nodeDestList = getDestnations(msg);// Դ���ܽڵ���Ϣ��Ŀ��IDs
				if (msg.getBrocast().equals("BROADCAST")) {
					msgs.add(msg);
				} else {
					for (Destnation dest : nodeDestList) {
						for (Integer nodeID : nodeIDs) {
							if (nodeID.equals(dest.getDestID())) {
								// �����鲥��Ϣ���ж��Ŀ�Ľڵ�ţ������Ϣ�ظ����
								msgs.add(msg);
							}

						}
					}
				}

			}

			if (!ignoreCell) {
				// ��ȡ���������µ����й��ܵ�Ԫ��Ϣ
				List<FunctionCellMsg> cellMsgs = getAllFunctionCellMsgs(icdManager, subMsg);
				List<FunctionCell> cells = new ArrayList<FunctionCell>();
				nodeIDs = new ArrayList<Integer>();// Ŀ�Ĺ��ܵ�ԪIDs
				for (FunctionSubDomain destDomain : destDomains) {
					cells.addAll(getAllFunctionCellsOfFunctionSubDomain(destDomain));
				}

				for (FunctionCell cell : cells) {
					nodeIDs.add(cell.getID());
				}

				for (FunctionCellMsg msg : cellMsgs) {
					List<Destnation> nodeDestList = getDestnations(msg);// Դ���ܵ�Ԫ��Ϣ��Ŀ��IDs
					if (msg.getBrocast().equals("BROADCAST")) {
						msgs.add(msg);
					} else {
						for (Destnation dest : nodeDestList) {
							for (Integer nodeID : nodeIDs) {
								if (nodeID.equals(dest.getDestID()))
									msgs.add(msg);
							}
						}
					}
				}
			}
		} else if (functionMsg instanceof FunctionNodeMsg) {// ���ܽڵ���Ϣ
			FunctionNodeMsg nodeMsg = (FunctionNodeMsg) functionMsg;
			// ��ȡĿ�Ĺ��ܽڵ㼯��
			List<FunctionNode> destNodes = getDestFunctionNodes(icdManager, destList);
			List<FunctionNode> filteredNodes = new ArrayList<FunctionNode>();
			if (testedObjects.size() > 0) {
				if (isSend) {// ����Ƿ�����Ϣ����Ӧ�ù��˵�����Ŀ��Ϊ���Թ��ߵ���Ϣ
					for (FunctionNode node : destNodes) {
						if (testedObjects.indexOf(node) >= 0) {
							filteredNodes.add(node);
						}
					}
				} else if (isRecv) {// ����ǽ�����Ϣ����Ӧ�ù��˵�����Ŀ��Ϊ���⹤�ߵ���Ϣ
					for (FunctionNode node : destNodes) {
						if (testedObjects.indexOf(node) < 0) {
							filteredNodes.add(node);
						}
					}
				}

				/*
				 * ����Ϣ�Ƿ�����Ϣʱ���������Ŀ��ȫΪ���Թ��ߣ������Ϣ��������������Ӧ�ñ�ת��ΪSPTEMsg��Ϣ ��
				 * ����Ϣ�ǽ�����Ϣʱ���������Ŀ��ȫΪ�����Զ��������ϢҲ��������������Ӧ�ñ�ת��ΪSPTEMsg��Ϣ��
				 * ����ϢΪ�㲥��Ϣʱ������������
				 */
				if (filteredNodes.size() == 0 && !nodeMsg.getBrocast().equals("BROADCAST"))
					return beans;
			}

			msgs.add(nodeMsg);

		} else if (functionMsg instanceof FunctionCellMsg && !ignoreCell) {// ���ܵ�Ԫ��Ϣ
			FunctionCellMsg cellMsg = (FunctionCellMsg) functionMsg;
			// ��ȡĿ�Ĺ��ܽڵ㼯��
			List<FunctionCell> destNodes = getDestFunctionCells(icdManager, destList, testedObjects);
			List<FunctionCell> filteredNodes = new ArrayList<FunctionCell>();
			if (testedObjects.size() > 0) {
				if (isSend) {// ����Ƿ�����Ϣ����Ӧ�ù��˵�����Ŀ��Ϊ���Թ��ߵ���Ϣ
					// ���˵�����Ϊ�������ĵ�Ԫ
					for (FunctionCell node : destNodes) {
						if (testedObjects.indexOf(node) >= 0) {
							filteredNodes.add(node);
						}
					}
				} else if (isRecv) {// ����ǽ�����Ϣ����Ӧ�ù��˵�����Ŀ��Ϊ���⹤�ߵ���Ϣ
					// ���˵�����Ϊ�������ĵ�Ԫ
					for (FunctionCell node : destNodes) {
						if (testedObjects.indexOf(node) < 0) {
							filteredNodes.add(node);
						}
					}
				}

				/*
				 * ����Ϣ�Ƿ�����Ϣʱ���������Ŀ��ȫΪ���Թ��ߣ������Ϣ��������������Ӧ�ñ�ת��ΪSPTEMsg��Ϣ ��
				 * ����Ϣ�ǽ�����Ϣʱ���������Ŀ��ȫΪ�����Զ��������ϢҲ��������������Ӧ�ñ�ת��ΪSPTEMsg��Ϣ��
				 * ����ϢΪ�㲥��Ϣʱ������������
				 */
				if (filteredNodes.size() == 0 && !cellMsg.getBrocast().equals("BROADCAST"))
					return beans;
			}

			msgs.add(cellMsg);
		}

		return setMessageDirection(msgs, functionMsg, icdManager, unitManager, testedObjects);
	}

	/**
	 * ����SPTEMsg����ķ��ͷ����Լ��Ƿ�Ϊ������Ϣ
	 * 
	 * @param msgs
	 * @param functionMsg
	 * @param icdManager
	 * @param unitManager
	 * @param testedObjects
	 * @return
	 * @throws Exception
	 */
	private static List<SPTEMsg> setMessageDirection(Collection<Entity> msgs, Entity functionMsg, ClazzManager icdManager, ClazzManager unitManager, List<Entity> testedObjects) throws Exception {
		List<SPTEMsg> beans = new ArrayList<SPTEMsg>(msgs.size());
		boolean send = isSendMsg(functionMsg, testedObjects);
		boolean recv = isRecvMsg(functionMsg, testedObjects);
		for (Entity msg : msgs) {
			SPTEMsg m = null;
			if (msg instanceof FunctionCellMsg) {
				m = convertFromFunctionCellMsg((FunctionCellMsg) msg, icdManager, unitManager);

			} else if (msg instanceof FunctionNodeMsg) {
				m = convertFromFunctionNodeMsg((FunctionNodeMsg) msg, icdManager, unitManager);

			}
			if (m == null) {
				return new ArrayList<SPTEMsg>(0);
			}
			m.getMsg().setPeriodMsg(isPeriod(msg));
			if (testedObjects.size() != 0) {
				if (send)
					m.getMsg().setDirection(XMLBean.SEND_MSG);
				else if (recv)
					m.getMsg().setDirection(XMLBean.RECV_MSG);

			}

			beans.add(m);
		}

		return beans;
	}

	public static String getIdOfEnumLiteral(ICDField icdField, int value) {
		for (ICDEnum icdEnum : icdField.getIcdEnums()) {
			if (icdEnum.getValue() == value) {
				return icdEnum.getSymbol();
			}
		}
		return StringUtils.EMPTY_STRING;
	}

	/**
	 * 
	 * @param clazzManager
	 *            ICD���������
	 * @param functionMsg
	 *            ������Ϣ
	 * @param testedObjects
	 *            �������
	 * @param send
	 *            �Ƿ��Ƿ�����Ϣ
	 * @param ignoreCell
	 *            �Ƿ���Թ��ܵ�Ԫ
	 * @throws Exception
	 *             ���޷��ж���Ϣ�Ƿ�Ϊ������Ϣʱ���׳����쳣
	 */
	public static List<SPTEMsg> filterAllNodeMsg(ClazzManager clazzManager, List<Entity> testedObjects, boolean send, boolean ignoreCell) throws Exception {
		List<SPTEMsg> spteMsgs = new ArrayList<SPTEMsg>();
		ClazzManager unitManager = TemplateEngine.getEngine().getUnitManager();
		if (testedObjects.size() == 0) {
			return spteMsgs;
		}
		for (Entity testedObject : testedObjects) {

			if (testedObject instanceof FunctionDomain) {
				List<FunctionDomainMsg> Msgs = clazzManager.getAllFunctionDomainMsgs();
				for (Entity msg : Msgs) {
					spteMsgs.addAll(getSPTEMsgsOfFunctionMsg(clazzManager, unitManager, msg, testedObjects, ignoreCell));
				}
			} else if (testedObject instanceof FunctionSubDomain) {
				List<FunctionSubDomainMsg> Msgs = clazzManager.getAllFunctionSubDomainMsgs();
				for (Entity msg : Msgs) {
					spteMsgs.addAll(getSPTEMsgsOfFunctionMsg(clazzManager, unitManager, msg, testedObjects, ignoreCell));
				}
			} else if (testedObject instanceof FunctionNode) {
				List<FunctionNodeMsg> Msgs = clazzManager.getAllFunctionNodeMsgs();
				for (Entity msg : Msgs) {
					boolean isSend = isSendMsg(msg, testedObjects);
					if (isSend == send) {
						spteMsgs.addAll(getSPTEMsgsOfFunctionMsg(clazzManager, unitManager, msg, testedObjects, ignoreCell));
					}

				}
			} else if (testedObject instanceof FunctionCell) {
				if (!ignoreCell) {
					List<FunctionCellMsg> Msgs = clazzManager.getAllFunctionCellMsgs();
					for (Entity msg : Msgs) {
						boolean isSend = isSendMsg(msg, testedObjects);
						if (isSend == send)
							spteMsgs.addAll(getSPTEMsgsOfFunctionMsg(clazzManager, unitManager, msg, testedObjects, ignoreCell));
					}
				}
			}
		}
		return spteMsgs;
	}

	/**
	 * ��ȡ��Ϣ�б��еķ��ͻ��߽�����Ϣ
	 * 
	 * @param msgs
	 * @param isSend
	 * @return
	 */
	public static List<SPTEMsg> filterSpteMsgOfSend(List<SPTEMsg> msgs, boolean isSend) {
		List<SPTEMsg> spteMsg = new ArrayList<SPTEMsg>();

		if (isSend) {// ɸѡ��������Ϣ
			for (SPTEMsg msg : msgs) {
				if (msg.getMsg().isSend()) {
					spteMsg.add(msg);
				}
			}
		} else {// ɸѡ��������Ϣ
			for (SPTEMsg msg : msgs) {
				if (msg.getMsg().isRecv()) {
					spteMsg.add(msg);
				}
			}
		}

		return spteMsg;
	}

	/**
	 * ��ȡ��Ϣ�б��еķ��ͻ��߽�����Ϣ
	 * 
	 * @param msgs
	 * @param isPeriod
	 * @return
	 */
	public static List<SPTEMsg> filterSpteMsgOfPeriod(List<SPTEMsg> msgs, boolean isPeriod) {
		List<SPTEMsg> spteMsg = new ArrayList<SPTEMsg>();

		try {
			if (isPeriod) {// ɸѡ��������Ϣ
				for (SPTEMsg msg : msgs) {
					if (msg.getMsg().isPeriodMessage()) {
						spteMsg.add(msg);
					}
				}
			} else {
				for (SPTEMsg msg : msgs) {
					if (!msg.getMsg().isPeriodMessage()) {
						spteMsg.add(msg);
					}
				}
			}
		} catch (SecurityException e) {

			e.printStackTrace();
		} catch (IllegalArgumentException e) {

			e.printStackTrace();
		}
		return spteMsg;
	}

	/**
	 * ��ȡ���������ԵĹ��ܽڵ���Ϣ
	 * 
	 * @param entity
	 *            ����
	 * @return
	 */
	public static List<Entity> getAllFunctionNodeMsgOfFighter(Entity entity) {
		List<Entity> entities = new ArrayList<Entity>();
		if (entity == null) {
			logger.warning("����Ĳ���Ϊ�ա�");
			return entities;
		}
		if (entity instanceof Fighter) {
			for (Entity en : entity.getChildren()) {
				if (en instanceof Version) {
					for (Entity ent : en.getChildren()) {
						if (ent instanceof FunctionNodeMsgs) {
							entities.addAll(ent.getChildren());
						}
					}
					break;
				}

			}
		} else {
			logger.warning("��������Ĳ�������ΪFighter��ȴ����" + entity.getClass().getName());
		}
		return entities;
	}

	/**
	 * ��ȡ�������µ����й�������
	 * 
	 * @param entity
	 *            ������
	 * @return
	 */
	public static List<Entity> getAllFunctionSubDomainsOfFunctionbDomain(Entity entity) {
		List<Entity> entities = new ArrayList<Entity>();
		if (entity == null) {
			logger.warning("����Ĳ���Ϊ��");
			return entities;
		}
		if (entity instanceof FunctionDomain) {
			logger.fine("�����������:\n" + entity.toString());
			List<Entity> logicChildren = entity.getLogicChildren();
			for (Entity logicChild : logicChildren) {
				if (logicChild instanceof FunctionSubDomain) {
					logger.fine("�������������:\n" + logicChild.toString());
					entities.add(logicChild);
				}
			}
		} else {
			logger.warning("��������Ĳ�������ΪFunctionSubDomain��ȴ����" + entity.getClass().getName());
		}
		logger.fine("��ȡ�������µ����й����������Ϊ:" + entities.size());
		return entities;
	}

	/**
	 * �ж�һ����Ϣ�Ƿ�Ϊ������Ϣ
	 * 
	 * @param entity
	 *            ������Ϣ����
	 * @return
	 */
	public static boolean isPeriod(Entity entity) {
		if (entity == null) {
			logger.warning("����Ĳ���Ϊ��");
			return false;
		}

		// ������Ϣ���ж�����Ϊ����Ϣ��"��Ϣ��������"���Ϊperiod��Ϊ������Ϣ
		if (entity instanceof FunctionSubDomainMsg) {
			FunctionSubDomainMsg nodeMsg = (FunctionSubDomainMsg) entity;
			if (PERIOD_MSG.equals(nodeMsg.getMsgTransType())) {
				return true;
			}
		} else if (entity instanceof FunctionDomainMsg) {
			FunctionDomainMsg msg = (FunctionDomainMsg) entity;
			if (PERIOD_MSG.equals(msg.getMsgTransType())) {
				return true;
			}
		} else if (entity instanceof FunctionCellMsg) {
			FunctionCellMsg cell = (FunctionCellMsg) entity;
			if (PERIOD_MSG.equals(cell.getMsgTransType()))
				return true;
		} else if (entity instanceof FunctionNodeMsg) {
			FunctionNodeMsg node = (FunctionNodeMsg) entity;
			if (PERIOD_MSG.equals(node.getMsgTransType()))
				return true;
		} else {
			logger.warning("����������Ϊ:" + entity.getClass().getName());
		}
		return false;
	}

	/**
	 * �ӹ��ܽڵ���Ϣת��Ϊ����������Ϣ
	 * 
	 * @param msg
	 *            ���ܽڵ���Ϣ
	 * @return
	 * @throws Exception
	 *             ���Ҳ�����Ӧ������ʱ����ִ��쳣
	 */
	private static SPTEMsg convertFromFunctionNodeMsg(FunctionNodeMsg msg, ClazzManager clazzManager, ClazzManager unitManager) throws Exception {
		if (msg == null) {
			logger.warning("����Ĺ��ܽڵ����Ϊ��");
			return null;
		}
		FunctionNodeMsg nodeMsg = (FunctionNodeMsg) msg;
		Message message = new Message();
		message.setSrcId(nodeMsg.getSourceFunctionID().toString());// ����Դ
		message.setUuid(UUID.randomUUID().toString());
		message.setId(nodeMsg.getMsgID());// ��ϢID
		Entity entityTopic = clazzManager.getTopic(nodeMsg.getMsgTopicSymbol());
		if (entityTopic != null) {
			Topic topic = (Topic) entityTopic;
			message.setTopicId(topic.getTopicID().toString());
			message.setWidth(topic.getDataLength());
		} else {
			logger.severe("����topicSymbol=" + nodeMsg.getMsgTopicSymbol() + " �Ҳ�����Ӧ�����⡣");
			return null;
		}
		message.setSendDuration(nodeMsg.getMsgTransPeriod());// ��Ϣ���
		message.setModelType(FUNCTION_NODE_MSG);// ������ϢΪ���ܽڵ�����
		message.setName(nodeMsg.getMsgName());
		List<Destnation> destnations = TemplateUtils.getDestnations(nodeMsg);
		StringBuilder sb = new StringBuilder();
		for (int i = 0, length = destnations.size(); i < length; i++) {
			Destnation dest = destnations.get(i);
			if (i == 0) {
				sb.append(dest.getDestID().toString());
			} else {
				sb.append(",");
				sb.append(dest.getDestID().toString());
			}
		}
		message.setDesId(sb.toString());

		Topic topic = msg.getTopic();
		ICDMsg icdMsg = new ICDMsg();
		List<Destnation> dests = getDestnations(msg);
		for (Destnation dest : dests) {
			icdMsg.addDestID(dest.getDestID());
		}
		Field[] fields = msg.getClass().getDeclaredFields();
		for (Field field : fields) {
			FieldRules fr = field.getAnnotation(FieldRules.class);
			if (fr == null)
				continue;
			field.setAccessible(true);

			try {
				Attribute att = new Attribute(field.get(msg), FieldTypes.getFieldType(fr.type()), field.getName(), fr.xmlName());
				icdMsg.addAttribute(att);
			} catch (IllegalArgumentException e) {
				LoggingPlugin.logException(logger, e);
			} catch (IllegalAccessException e) {
				LoggingPlugin.logException(logger, e);
			}

		}
		// ��ȡ�����е��������ԣ�����Щ����д��ICDMsg������
		fields = topic.getClass().getDeclaredFields();
		for (Field field : fields) {
			FieldRules fr = field.getAnnotation(FieldRules.class);
			if (fr == null)
				continue;

			field.setAccessible(true);

			try {
				Attribute att = new Attribute(field.get(topic), FieldTypes.getFieldType(fr.type()), field.getName(), fr.xmlName());
				icdMsg.addAttribute(att);
			} catch (IllegalArgumentException e) {
				LoggingPlugin.logException(logger, e);
			} catch (IllegalAccessException e) {
				LoggingPlugin.logException(logger, e);
			}

		}

		SPTEMsg spteMsg = new SPTEMsg(message, icdMsg);
		List<Entity> entyties = topic.getChildren();
		// ������������������źţ�����Щ�ź����ɶ�Ӧ��ICDField����
		for (Entity entity : entyties) {
			if (entity instanceof com.coretek.spte.ContainedSignals) {// ��������ź�
				List<Entity> children = entity.getChildren();
				for (Entity child : children) {
					if (child instanceof com.coretek.spte.ContainedSignal) {
						com.coretek.spte.ContainedSignal containedSignal = (com.coretek.spte.ContainedSignal) child;
						Signal signal = clazzManager.getSingal(containedSignal.getSignalSymbol());
						ICDField icdField = getSignal(signal, clazzManager, unitManager);
						icdMsg.addField(icdField);
						addAttribute(containedSignal, icdField);
						List<Entity> containedSingals = child.getChildren();// ���������ź�
						for (Entity containedSignal2 : containedSingals) {
							if (containedSignal2 instanceof com.coretek.spte.topic.ContainedSignals) {
								List<Entity> containedSingalList = containedSignal2.getChildren();
								for (Entity containedSignal3 : containedSingalList) {
									Signal signal2 = clazzManager.getSingal(((com.coretek.spte.topic.ContainedSignal) containedSignal3).getSignalSymbol());
									if (signal2 != null) {
										ICDField icdField2 = getSignal(signal2, clazzManager, unitManager);
										addAttribute(containedSignal2, icdField2);
										icdField.addICDField(icdField2);
									}
								}

							}
						}

					}
				}
			}
		}

		String transType = msg.getMsgTransType();
		if (!transType.equals(PERIOD_MSG)) {// ��������Ϣ
			List<ICDField> icdFields = spteMsg.getICDMsg().getFields();
			for (ICDField icdField : icdFields) {
				com.coretek.spte.testcase.Field field = convertToCaseField(icdField);
				spteMsg.getMsg().addChild(field);
				parseICDField(icdField, field);
			}
			spteMsg.getMsg().setPeriodMsg(false);
			spteMsg.getMsg().setParallel(false);
			spteMsg.getMsg().setPeriodCount(0);
			spteMsg.getMsg().setPeriodDuration(0);
			spteMsg.getMsg().setAmendValue("0");
			if (null != spteMsg.getICDMsg().getAttribute("dataLength") && null != spteMsg.getICDMsg().getAttribute("dataLength").getValue()) {
				spteMsg.getMsg().setWidth((Integer) spteMsg.getICDMsg().getAttribute("dataLength").getValue());
			} else {
				spteMsg.getMsg().setWidth(0);
			}
		} else {// ������Ϣ
			Period period = new Period();
			period.setValue(1);
			List<ICDField> icdFields = spteMsg.getICDMsg().getFields();
			for (ICDField icdField : icdFields) {
				com.coretek.spte.testcase.Field field = convertToCaseField(icdField);
				period.addChild(field);
				parseICDField(icdField, field);
			}
			spteMsg.getMsg().addChild(period);
			spteMsg.getMsg().setParallel(false);
			spteMsg.getMsg().setPeriodCount(1);
			spteMsg.getMsg().setPeriodDuration(spteMsg.getMsg().getSendDuration());
			spteMsg.getMsg().setPeriodMsg(true);
			spteMsg.getMsg().setAmendValue("0");
			if (null != spteMsg.getICDMsg().getAttribute("dataLength") && null != spteMsg.getICDMsg().getAttribute("dataLength").getValue()) {
				spteMsg.getMsg().setWidth((Integer) spteMsg.getICDMsg().getAttribute("dataLength").getValue());
			} else {
				spteMsg.getMsg().setWidth(0);
			}
		}

		logger.fine("���ɵĲ���������Ϣ������:\n" + message.toXML());

		return spteMsg;
	}

	/**
	 * ��ICDField����ת��Ϊ���������е�Field����
	 * 
	 * @param icdField
	 * @return
	 */
	private static com.coretek.spte.testcase.Field convertToCaseField(ICDField icdField) {
		com.coretek.spte.testcase.Field field = new com.coretek.spte.testcase.Field();
		field.setValue("0");
		Attribute att = icdField.getAttribute(Constants.ICD_FIELD_START_WORD);
		if (null == att) {
			field.setOffsetword(0);
		} else {
			field.setOffsetword(Integer.valueOf(att.getValue().toString()));
		}

		att = icdField.getAttribute(Constants.ICD_FIELD_SYMBOL);
		field.setId(att.getValue().toString());
		att = icdField.getAttribute(Constants.ICD_FIELD_UNSIGNED);
		if (att == null || att.getValue() == null || StringUtils.isNull(att.getValue().toString())) {
			field.setSigned(null);
		} else {
			field.setSigned(Boolean.valueOf(att.getValue().toString()));
		}

		att = icdField.getAttribute(Constants.ICD_FIELD_LENGTH);
		field.setWidth(Integer.valueOf(att.getValue().toString()));
		att = icdField.getAttribute(Constants.ICD_FIELD_NAME);
		field.setName(att.getValue().toString());
		field.setValue("0");
		att = icdField.getAttribute(Constants.ICD_FIELD_START_BIT);
		if (att == null || att.getValue() == null || StringUtils.isNull(att.getValue().toString())) {
			field.setOffsetbit(0);
		} else
			field.setOffsetbit(Integer.valueOf(att.getValue().toString()));
		return field;
	}

	/**
	 * ������ICDField��������������ICDField����
	 * 
	 * @param icdField
	 * @param field
	 */
	private static void parseICDField(ICDField icdField, com.coretek.spte.testcase.Field field) {
		if (icdField.getIcdFields() != null && icdField.getIcdFields().size() != 0) {
			for (ICDField icdField2 : icdField.getIcdFields()) {
				com.coretek.spte.testcase.Field field2 = convertToCaseField(icdField2);
				field.addChild(field2);
				parseICDField(icdField2, field2);
			}
		}
	}

	/**
	 * Ϊ�ֶ���ӵ�λ����
	 * 
	 * @param signal
	 * @param icdField
	 * @param unitManager
	 */
	private static void addUnit(Signal signal, ICDField icdField, ClazzManager unitManager) {
		if (signal.getSignalUnit() != null && signal.getSignalUnit().trim().length() != 0) {
			int unitID = Integer.valueOf(signal.getSignalUnit());
			UnitType unitType = unitManager.getUnitType(unitID);
			if (unitType != null) {
				ICDUnitType icdUnitType = new ICDUnitType(unitType.getID());
				ICDUnit selectICDUnit = null;
				addAttribute(unitType, icdUnitType);
				for (Entity entity : unitType.getChildren()) {
					if (entity instanceof Unit) {
						Unit unit = (Unit) entity;
						ICDUnit icdUnit = new ICDUnit(unit.getID(), unit.getName(), unit.getDisplayName());
						addAttribute(unit, icdUnit);
						icdUnitType.addUnit(icdUnit);
						if (unit.getID().equals(unitID)) {
							selectICDUnit = icdUnit;
						}
					}
				}
				icdField.setIcdUnit(selectICDUnit);
				icdField.setIcdUnitType(icdUnitType);
			}
		}
	}

	/**
	 * 
	 * @param signal
	 * @param clazzManager
	 * @param unitManager
	 * @return
	 */
	private static ICDField getSignal(Signal signal, ClazzManager clazzManager, ClazzManager unitManager) {
		ICDField child = new ICDField();
		addAttribute(signal, child);
		addUnit(signal, child, unitManager);
		List<Entity> entities = signal.getChildren();
		for (Entity entity : entities) {
			if (entity instanceof com.coretek.spte.singal.ContainedSignal) {
				com.coretek.spte.singal.ContainedSignal csl = (com.coretek.spte.singal.ContainedSignal) entity;
				Signal signal2 = clazzManager.getSingal(csl.getSignalSymbol());
				if (signal2 == null) {
					logger.warning("����signalSymbol=" + csl.getSignalSymbol() + " ���ܹ���ȡ��Ӧ���źš�");
					continue;
				}
				child.addICDField(getSignal(signal2, clazzManager, unitManager));

				ICDField child2 = new ICDField();
				addAttribute(entity, child2);
			} else if (entity instanceof EnumValueAndKey) {// ö��ֵ��
				EnumValueAndKey vk = (EnumValueAndKey) entity;

				for (Entity enumEntity : vk.getChildren()) {
					if (enumEntity instanceof EnumBean) {
						EnumBean eb = (EnumBean) enumEntity;
						ICDEnum icdEnum = new ICDEnum(eb.getValue(), eb.getSymbol());
						child.addICDEnum(icdEnum);
					}
				}
			}
		}

		return child;
	}

	/**
	 * 
	 * @param obj
	 * @param helper
	 */
	private static void addAttribute(Entity obj, Helper helper) {
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			FieldRules fr = field.getAnnotation(FieldRules.class);
			if (fr == null)
				continue;
			try {
				Attribute att = new Attribute(field.get(obj), FieldTypes.getFieldType(fr.type()), field.getName(), fr.xmlName());
				helper.addAttribute(att);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				break;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				break;
			}

		}
	}

	/**
	 * �ӹ��ܵ�Ԫ��Ϣת��ΪSPTEMsg����
	 * 
	 * @param msg
	 * @param clazzManager
	 * @param unitManager
	 * @return
	 */
	private static SPTEMsg convertFromFunctionCellMsg(FunctionCellMsg msg, ClazzManager clazzManager, ClazzManager unitManager) {
		if (msg == null) {
			logger.warning("����Ĳ���Ϊ��");
			return null;
		}
		FunctionCellMsg cellMsg = (FunctionCellMsg) msg;
		Message message = new Message();
		message.setSrcId(cellMsg.getSourceFunctionID().toString());// ����Դ
		message.setUuid(UUID.randomUUID().toString());
		message.setId(cellMsg.getMsgID());// ����ID
		Entity entityTopic = clazzManager.getTopic(cellMsg.getMsgTopicSymbol());
		if (entityTopic != null) {
			Topic topic = (Topic) entityTopic;
			message.setTopicId(topic.getTopicID().toString());
		} else {
			logger.severe("����topicSymbol=" + cellMsg.getMsgTopicSymbol() + " �Ҳ�����Ӧ�����⡣");
			return null;
		}
		message.setSendDuration(cellMsg.getMsgTransPeriod());// ��Ϣ���
		message.setModelType(FUNCTION_CELL_MSG);// ������ϢΪ���ܽڵ�����
		message.setName(cellMsg.getMsgName());

		Topic topic = msg.getTopic();
		// ����ICD������󹩷���������ʹ��
		ICDMsg icdMsg = new ICDMsg();
		Field[] fields = msg.getClass().getDeclaredFields();
		for (Field field : fields) {
			FieldRules fr = field.getAnnotation(FieldRules.class);
			field.setAccessible(true);
			if (fr == null)
				continue;
			try {
				Attribute att = new Attribute(field.get(msg), FieldTypes.getFieldType(fr.type()), field.getName(), fr.xmlName());
				icdMsg.addAttribute(att);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				return null;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				return null;
			}

		}

		fields = topic.getClass().getDeclaredFields();
		for (Field field : fields) {
			FieldRules fr = field.getAnnotation(FieldRules.class);
			field.setAccessible(true);
			if (fr == null)
				continue;
			try {
				Attribute att = new Attribute(field.get(topic), FieldTypes.getFieldType(fr.type()), field.getName(), fr.xmlName());
				icdMsg.addAttribute(att);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				return null;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				return null;
			}

		}
		List<Destnation> destnations = TemplateUtils.getDestnations(cellMsg);
		StringBuilder sb = new StringBuilder();
		for (int i = 0, length = destnations.size(); i < length; i++) {
			Destnation dest = destnations.get(i);
			if (i == 0) {
				sb.append(dest.getDestID().toString());
			} else {
				sb.append(",");
				sb.append(dest.getDestID().toString());
			}
		}
		message.setDesId(sb.toString());

		SPTEMsg spteMsg = new SPTEMsg(message, icdMsg);
		List<Destnation> dests = getDestnations(msg);
		for (Destnation dest : dests) {
			icdMsg.addDestID(dest.getDestID());
		}
		List<Entity> entyties = topic.getChildren();

		for (Entity entity : entyties) {
			if (entity instanceof com.coretek.spte.ContainedSignals) {// ��������ź�
				List<Entity> children = entity.getChildren();
				for (Entity child : children) {
					if (child instanceof com.coretek.spte.ContainedSignal) {
						com.coretek.spte.ContainedSignal containedSignal = (com.coretek.spte.ContainedSignal) child;
						Signal signal = clazzManager.getSingal(containedSignal.getSignalSymbol());
						ICDField icdField = getSignal(signal, clazzManager, unitManager);
						icdMsg.addField(icdField);
						addAttribute(containedSignal, icdField);
						List<Entity> containedSingals = child.getChildren();// ���������ź�
						for (Entity containedSignal2 : containedSingals) {
							if (containedSignal2 instanceof com.coretek.spte.topic.ContainedSignals) {
								List<Entity> containedSingalList = containedSignal2.getChildren();
								for (Entity containedSignal3 : containedSingalList) {
									Signal signal2 = clazzManager.getSingal(((com.coretek.spte.topic.ContainedSignal) containedSignal3).getSignalSymbol());
									if (signal2 != null) {
										ICDField icdField2 = getSignal(signal2, clazzManager, unitManager);
										addAttribute(containedSignal2, icdField2);
										icdField.addICDField(icdField2);
									}
								}

							}
						}

					}
				}
			}
		}

		String transType = msg.getMsgTransType();
		if (!transType.equals(PERIOD_MSG)) {// ��������Ϣ
			List<ICDField> icdFields = spteMsg.getICDMsg().getFields();
			for (ICDField icdField : icdFields) {
				com.coretek.spte.testcase.Field field = convertToCaseField(icdField);
				spteMsg.getMsg().addChild(field);
				parseICDField(icdField, field);
			}
			spteMsg.getMsg().setPeriodMsg(false);
			spteMsg.getMsg().setParallel(false);
			spteMsg.getMsg().setPeriodCount(0);
			spteMsg.getMsg().setPeriodDuration(0);
			spteMsg.getMsg().setAmendValue("0");
			if (null != spteMsg.getICDMsg().getAttribute("dataLength") && null != spteMsg.getICDMsg().getAttribute("dataLength").getValue()) {
				spteMsg.getMsg().setWidth((Integer) spteMsg.getICDMsg().getAttribute("dataLength").getValue());
			} else {
				spteMsg.getMsg().setWidth(0);
			}
		} else {// ������Ϣ
			Period period = new Period();
			List<ICDField> icdFields = spteMsg.getICDMsg().getFields();
			for (ICDField icdField : icdFields) {
				com.coretek.spte.testcase.Field field = convertToCaseField(icdField);
				period.addChild(field);
				parseICDField(icdField, field);
			}
			spteMsg.getMsg().addChild(period);
			spteMsg.getMsg().setParallel(false);
			spteMsg.getMsg().setPeriodCount(1);
			spteMsg.getMsg().setPeriodDuration(0);
			spteMsg.getMsg().setPeriodMsg(true);
			spteMsg.getMsg().setAmendValue("0");
			if (null != spteMsg.getICDMsg().getAttribute("dataLength") && null != spteMsg.getICDMsg().getAttribute("dataLength").getValue()) {
				spteMsg.getMsg().setWidth((Integer) spteMsg.getICDMsg().getAttribute("dataLength").getValue());
			} else {
				spteMsg.getMsg().setWidth(0);
			}
		}

		logger.config("���ɵĲ���������Ϣ������:\n" + message.toXML());

		return spteMsg;
	}

	/**
	 * ��ȡ��λ����
	 * 
	 * @param id
	 *            ��λ��ID
	 * @param clazzManager
	 *            �������
	 * @return ���޷��ҵ�ƥ��ĵ�λʱ���᷵��null
	 */
	public static Entity getUnit(Integer id, ClazzManager clazzManager) {
		List<Entity> entities = clazzManager.getAllEntities();
		for (Entity entity : entities) {
			if (entity instanceof Unit) {
				if (id.equals(entity.getFieldValue("ID"))) {
					return entity;
				}
			}
		}
		logger.warning("δ�ҵ�ID=" + id + "�ĵ�λ����");
		return null;
	}

}