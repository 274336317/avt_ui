/************************************************************************
 *				北京科银京成技术有限公司 版权所有
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
 * 提供一些方便使用模板引擎的方法
 * 
 * @author 孙大巍 2011-12-29
 */
public final class TemplateUtils {

	private static final Logger logger = LoggingPlugin.getLogger(TemplateUtils.class.getName());

	/** 周期消息标识符 */
	public final static String PERIOD_MSG = "PERIOD";

	/** 功能域的层级名称 */
	public final static String FUNCTION_DOMAIN = "功能域";

	/** 功能子域的层级名称 */
	public final static String FUNCTION_SUB_DOMAIN = "功能子域";

	/** 功能单元的层级名称 */
	public final static String FUNCTION_CELL = "功能单元";

	/** 功能节点的层级名称 */
	public final static String FUNCTION_NODE = "功能节点";

	/** 功能单元消息 */
	public final static String FUNCTION_CELL_MSG = "FunctionCellMsg";

	/** 功能节点消息 */
	public final static String FUNCTION_NODE_MSG = "FunctionNodeMsg";

	/**
	 * 广播
	 */
	public final static String BROCAST_BROADCAST = "BROADCAST";

	/**
	 * 点播
	 */
	public final static String BROADCAST_POINT = "POINT";

	/**
	 * 组播
	 */
	public final static String BROADCAST_GROUP = "GROUP";

	public final static String MSG_TRANS_TYPE_EVENT = "EVENT";

	/**
	 * 周期消息
	 */
	public final static String MSG_TRANS_TYPE_PERIOD = "PERIOD";

	public final static String MSG_TRANS_TYPE_INSTANCY = "INSTANCY";

	public final static String FUNCTION_DOMAIN_LEVEL = "00";

	public final static String FUNCTION_SUB_DOMAIN_LEVEL = "11";

	public final static String FUNCTION_NODE_LEVEL = "22";

	public final static String FUNCTION_CELL_LEVEL = "21";

	/**
	 * 获取测试用例的schema文件
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
	 * 获取测试用例中的所有消息以及时间间隔对象
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
	 * 判断节点消息是否为广播消息
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
	 * 判断节点消息是否为被测对象集合内部的消息
	 * 
	 * @param nodeMsg
	 * @param testObjectsID
	 *            被测对象的ID集合
	 * @return 如果是则返回true否则返回false</br>
	 */
	public static boolean isInternalMsgOfTestedObjects(ICDFunctionNodeMsg nodeMsg, List<String> testObjectsID) {
		// 广播消息不能算做内部消息
		if (isBroadcast(nodeMsg))
			return false;
		// 消息的源节点号
		String srcID = nodeMsg.getAttribute(Constants.ICD_MSG_SRC_ID).getValue().toString();
		// 消息的目的节点号集合
		List<Integer> desIDs = nodeMsg.getSpteMsg().getICDMsg().getDestIDs();
		for (String testedID : testObjectsID) {
			if (testedID.equals(srcID)) {// 消息的发送源为被测对象
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
		// 广播消息不能算做内部消息
		if (isBroadcast(cellMsg))
			return false;
		// 消息的源节点号
		String srcID = cellMsg.getAttribute(Constants.ICD_MSG_SRC_ID).getValue().toString();
		// 消息的目的节点号集合
		List<Integer> desIDs = cellMsg.getSpteMsg().getICDMsg().getDestIDs();
		for (String testedID : testObjectsID) {
			if (testedID.equals(srcID)) {// 消息的发送源为被测对象
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
	 * 判断节点消息是否为测试工具内部的消息
	 * 
	 * @param nodeMsg
	 * @param testObjectsID
	 *            被测对象的ID集合
	 * @return
	 */
	public static boolean isInternalMsgOfToolObjects(ICDFunctionNodeMsg nodeMsg, List<String> testObjectsID) {
		// 广播消息不能算做内部消息
		if (isBroadcast(nodeMsg))
			return false;
		// 消息的源节点号
		String srcID = nodeMsg.getAttribute(Constants.ICD_MSG_SRC_ID).getValue().toString();
		// 消息的目的节点号集合
		List<Integer> destIDs = nodeMsg.getSpteMsg().getICDMsg().getDestIDs();
		for (String testedID : testObjectsID) {
			if (testedID.equals(srcID)) {// 源节点和被测对象相关
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
		// 广播消息不能算做内部消息
		if (isBroadcast(cellMsg))
			return false;
		// 消息的源节点号
		String srcID = cellMsg.getAttribute(Constants.ICD_MSG_SRC_ID).getValue().toString();
		// 消息的目的节点号集合
		List<Integer> destIDs = cellMsg.getSpteMsg().getICDMsg().getDestIDs();
		for (String testedID : testObjectsID) {
			if (testedID.equals(srcID)) {// 源节点和被测对象相关
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
	 * 获取ICD文件中引用了某个主题的所有功能域消息
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
	 * 获取ICD文件中引用了某个主题的所有功能子域消息
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
	 * 获取ICD文件中引用了某个主题的所有功能节点消息
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
	 * 获取ICD文件中引用了某个主题的所有功能单元消息
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
	 * 获取功能域(ICDFunctionDomain)下的所有子域(ICDFunctionSubDomain)对象。
	 * 
	 * @param domains
	 *            功能域
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
	 * 获取与测试用例中字段对象对应的信号。
	 * 
	 * @param spteMsg
	 * @param field
	 *            测试用例字段
	 * @return
	 */
	public static ICDField getICDField(SPTEMsg spteMsg, com.coretek.spte.testcase.Field field) {
		ICDMsg icdMsg = spteMsg.getICDMsg();
		List<ICDField> icdFields = icdMsg.getFields();
		if (field.getParent() == null || field.getParent() instanceof Period || field.getParent() instanceof Message) {
			// 第一级field
			for (ICDField icdField : icdFields) {
				if (icdField.getAttribute(Constants.ICD_FIELD_SYMBOL).getValue().equals(field.getId())) {
					return icdField;
				}
			}
			logger.warning("根据symbol=" + field.getId() + " 找不到对应的ICDField对象。");
		} else {// 传入的field被包含在其它Field中
			com.coretek.spte.testcase.Field parentField = (com.coretek.spte.testcase.Field) field.getParent();
			List<com.coretek.spte.testcase.Field> path = new ArrayList<com.coretek.spte.testcase.Field>();
			path.add(parentField);
			// 递归查找出所有包含field的Field对象的集合
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
						logger.warning("根据symbol=" + field.getId() + " 找不到对应的ICDField对象。");
					}
				}
			}

		}

		return null;
	}

	/**
	 * 返回周期消息中，发送编号和List<Field>的映射表
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
	 * 根据功能对象的ID获取功能对象的名字
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
			logger.warning("传入的clazz参数不正确.clazz=" + clazz.getName());
		}

		return name;
	}

	/**
	 * 获取功能域的名字
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
		logger.warning("根据ID=" + functionID + " 找不到对应的功能域对象。");

		return null;
	}

	/**
	 * 获取功能子域的名字
	 * 
	 * @param icdManager
	 * @param functionID
	 *            功能子域号
	 * @return
	 */
	public static String getFunctionSubDomainName(ClazzManager icdManager, Integer functionID) {
		List<Entity> entities = icdManager.getAllFunctionSubDomains();
		for (Entity entity : entities) {
			if (functionID.equals(entity.getFieldValue("ID"))) {
				return String.valueOf(entity.getFieldValue("name"));
			}
		}
		logger.warning("根据ID=" + functionID + " 找不到对应的功能子域对象。");

		return null;
	}

	/**
	 * 获取功能单元的名字
	 * 
	 * @param icdManager
	 * @param functionID
	 *            功能单元号
	 * @return
	 */
	public static String getFunctionCellName(ClazzManager icdManager, Integer functionID) {
		List<FunctionCell> entities = icdManager.getAllFunctionCells();
		for (Entity entity : entities) {
			if (functionID.equals(entity.getFieldValue("ID"))) {
				return String.valueOf(entity.getFieldValue("name"));
			}
		}
		logger.warning("根据ID=" + functionID + " 找不到对应的功能单元对象。");

		return null;
	}

	/**
	 * 获取功能节点的名字
	 * 
	 * @param icdManager
	 * @param functionID
	 *            功能节点ID
	 * @return
	 */
	public static String getFunctionNodeName(ClazzManager icdManager, Integer functionID) {
		List<FunctionNode> entities = icdManager.getAllFunctionNodes();
		for (Entity entity : entities) {
			if (functionID.equals(entity.getFieldValue("ID"))) {
				return String.valueOf(entity.getFieldValue("name"));
			}
		}
		logger.warning("根据ID=" + functionID + " 找不到对应的功能节点对象。");

		return null;
	}

	/**
	 * 从FunctionNode对象转换为ICDFunctionNode对象
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

		// 获取功能节点下的所有消息并转换为ICDFunctionNodeMsg对象
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
	 * 将FunctionCell对象转换为ICDFunctionCell对象
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
		// 获取功能单元下的所有消息并转换为ICDFunctionCellMsg对象
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
	 * 将解析出来的FunctionSubDomainMsg对象转换为ICDFunctionSubDomainMsg对象，
	 * 并添加到ICDFunctionSubDomain对象中。
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
				// 功能单元消息
				if (FUNCTION_CELL_MSG.equals(spteMsg.getMsg().getModelType())) {
					ICDFunctionCellMsg cellMsg = new ICDFunctionCellMsg(spteMsg, icdSubDomain);
					cellMsg.setAttributes(spteMsg.getICDMsg().getAttributes());
					icdDomainMsg.addCellMsg(cellMsg);
				} else if (FUNCTION_NODE_MSG.equals(spteMsg.getMsg().getModelType())) {// 功能节点消息
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
	 * 获取域的所有消息并转换为ICDFunctionDomainMsg，然后将转换都的对象添加到ICDFunctionDomain对象中。
	 * 
	 * @param icdManager
	 * @param icdDomain
	 * @param functionDomain
	 */
	private static void convertFromFunctionDomain(ClazzManager icdManager, ICDFunctionDomain icdDomain, FunctionDomain functionDomain) {
		// 获取域的所有消息并转换为ICDFunctionDomainMsg
		for (Entity logicChild : functionDomain.getLogicChildren()) {
			if (logicChild instanceof FunctionDomainMsg) {
				FunctionDomainMsg domainMsg = (FunctionDomainMsg) logicChild;
				try {
					List<SPTEMsg> spteMsgs = getSPTEMsgsOfFunctionMsg(icdManager, TemplateEngine.getEngine().getUnitManager(), domainMsg, new ArrayList<Entity>(0), false);
					ICDFunctionDomainMsg icdDomainMsg = new ICDFunctionDomainMsg(icdDomain);
					icdDomain.addDomainMsg(icdDomainMsg);
					icdDomainMsg.setAttributes(domainMsg.getAttributes());
					for (SPTEMsg spteMsg : spteMsgs) {
						// 功能单元消息
						if (FUNCTION_CELL_MSG.equals(spteMsg.getMsg().getModelType())) {
							ICDFunctionCellMsg cellMsg = new ICDFunctionCellMsg(spteMsg, icdDomain);
							cellMsg.setAttributes(spteMsg.getICDMsg().getAttributes());
							icdDomainMsg.addCellMsg(cellMsg);
						} else if (FUNCTION_NODE_MSG.equals(spteMsg.getMsg().getModelType())) {// 功能节点消息
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
	 * 获取所有的功能域
	 * 
	 * @param icdManager
	 * @return </br>
	 */
	public static List<ICDFunctionDomain> getAllFunctionDomains(ClazzManager icdManager) {
		List<ICDFunctionDomain> icdDomains = new ArrayList<ICDFunctionDomain>();
		// 获取ICD文件中的所有域
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
			// 获取域的所有消息并转换为ICDFunctionDomainMsg
			convertFromFunctionDomain(icdManager, icdDomain, functionDomain);

			// 获取域下的所有子域并将其转换为ICDFunctionSubDomain对象
			for (Entity subDomain : getAllFunctionSubDomainsOfFunctionbDomain(entity)) {
				convertFromFunctionSubDomain(icdManager, (FunctionSubDomain) subDomain, icdDomain);
			}

		}

		return icdDomains;
	}

	/**
	 * 将FunctionSubDomain转换为ICDFunctionSubDomain对象
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
		// 获取子域的所有消息并转换为ICDFunctionSubDomainMsg
		for (Entity logicChild : subDomain.getLogicChildren()) {
			if (logicChild instanceof FunctionSubDomainMsg) {
				convertFromFunctionSubDomainMsg(icdManager, icdSubDomain, (FunctionSubDomainMsg) logicChild);
			}
		}

		// 获取子域下的所有功能单元并转换为ICDFunctionCell对象
		List<FunctionCell> functionCells = getAllFunctionCellsOfFunctionSubDomain((FunctionSubDomain) subDomain);
		for (FunctionCell cell : functionCells) {
			convertFromFunctionCell(icdSubDomain, icdManager, cell);
		}
		// 获取子域下的所有功能节点并转换为ICDFunctionNode对象
		for (FunctionNode node : getAllFunctionNodesOfFunctionSubDomain((FunctionSubDomain) subDomain)) {
			convertFromFunctionNode(icdManager, icdSubDomain, node);
		}
	}

	/**
	 * 获取icd文件绝对路径
	 * 
	 * @param entity
	 *            测试用例
	 * @return 当无法找到匹配的节点时，会返回null</br>
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
	 * 获取机型下的层级信息
	 * 
	 * @param entity
	 *            机型 </br>
	 */
	public static List<Entity> getAllLayersOfFigther(Entity entity) {
		List<Entity> entities = new ArrayList<Entity>();
		if (entity == null) {
			logger.warning("传入的参数为空。");
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
			logger.warning("期望传入的参数类型为Fighter，却传入" + entity.getClass().getName());
		}

		return entities;
	}

	/**
	 * 获取机型下的层级的 名称
	 * 
	 * @param entity
	 *            机型 </br>
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
	 * 获取机型下的层级的编码
	 * 
	 * @param entity
	 *            机型
	 * @param layer
	 *            机型 </br>
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
	 * 获取机型下的所有功能域
	 * 
	 * @param entity
	 *            机型
	 */
	public static List<Entity> getAllFunctionDomainOfFighter(Entity entity) {
		List<Entity> entities = new ArrayList<Entity>();
		if (entity == null) {
			logger.warning("传入的参数为空。");
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
			logger.warning("期望传入的参数类型为Fighter，却传入" + entity.getClass().getName());
		}

		return entities;
	}

	/**
	 * 获取机型下的所有功能子域
	 * 
	 * @param entity
	 *            机型
	 */
	public static List<Entity> getAllFunctionSubDomainOfFighter(Entity entity) {
		List<Entity> entities = new ArrayList<Entity>();
		if (entity == null) {
			logger.warning("传入的参数为空。");
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
			logger.warning("期望传入的参数类型为Fighter，却传入" + entity.getClass().getName());
		}
		return entities;
	}

	/**
	 * 获取机型下的所有功能节点
	 * 
	 * @param entity
	 *            机型
	 */
	public static List<Entity> getAllFunctionNodeOfFighter(Entity entity) {
		List<Entity> entities = new ArrayList<Entity>();
		if (entity == null) {
			logger.warning("传入的参数为空。");
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
			logger.warning("期望传入的参数类型为Fighter，却传入" + entity.getClass().getName());
		}
		return entities;
	}

	/**
	 * 获取机型下的所有功能单元
	 * 
	 * @param entity
	 *            机型
	 */
	public static List<Entity> getAllFunctionCellOfFighter(Entity entity) {
		List<Entity> entities = new ArrayList<Entity>();
		if (entity == null) {
			logger.warning("传入的参数为空。");
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
			logger.warning("期望传入的参数类型为Fighter，却传入" + entity.getClass().getName());
		}
		return entities;
	}

	/**
	 * 获取机型下选择的类型的所有节点
	 * 
	 * @param entity
	 *            机型
	 */
	public static List<Entity> getSelectTypeofFighter(Entity entity, String type) {
		List<Entity> entities = new ArrayList<Entity>();
		if (null == type || null == entity) {
			logger.warning("传入的参数为空。");
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
	 * 返回该机型中对应的测试对象列表
	 * 
	 * @param fighter
	 *            icd中的机型
	 * @param testObjects
	 *            测试用例文件中的TestedObjects
	 */
	public static List<Entity> getTestedObjectsOfICD(Entity fighter, Entity testObjects) {
		List<Entity> entities = new ArrayList<Entity>();
		if (null == fighter || null == testObjects) {
			logger.warning("传入的参数为空。");
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
				logger.warning("期望传入的参数类型为TestedObjects，却传入" + testObjects.getClass().getName());
			}

		} else {
			logger.warning("期望传入的参数类型为Fighter，却传入" + fighter.getClass().getName());
		}
		return entities;
	}

	/**
	 * 获取机型下的版本信息
	 * 
	 * @param entity
	 *            机型
	 */
	public static String getVersionIDOfFighter(Entity entity) {
		String ID = null;
		if (entity == null) {
			logger.warning("传入的参数为空。");
			return ID;
		}
		if (entity instanceof Fighter) {
			for (Entity en : entity.getChildren()) {
				if (en instanceof Version) {
					ID = ((Version) en).getVersionCode();
				}
			}
		} else {
			logger.warning("期望传入的参数类型为Fighter，却传入" + entity.getClass().getName());
		}
		return ID;
	}

	/**
	 * 获取ICD文件的版本号
	 * 
	 * @param icdManager
	 * @return
	 */
	public static String getVerion(ClazzManager icdManager) {
		Entity entity = icdManager.getFighter();
		return getVersionIDOfFighter(entity);
	}

	/**
	 * 获取功能子域下的所有功能节点
	 * 
	 * @param entity
	 *            功能子域
	 * @return
	 */
	public static List<FunctionNode> getAllFunctionNodesOfFunctionSubDomain(FunctionSubDomain entity) {
		List<FunctionNode> entities = new ArrayList<FunctionNode>();
		if (entity == null) {
			logger.warning("传入的参数为空。");
			return entities;
		}
		logger.fine("子域的内容:\n" + entity.toString());
		List<Entity> logicChildren = entity.getLogicChildren();
		logger.fine("逻辑子节点的个数为:" + logicChildren.size());
		for (Entity logicChild : logicChildren) {
			if (logicChild instanceof FunctionNode) {
				logger.fine("功能节点的内容为:" + logicChild.toString());
				entities.add((FunctionNode) logicChild);
			}
		}

		return entities;
	}

	/**
	 * 获取功能子域下的所有功能单元
	 * 
	 * @param entity
	 *            功能子域
	 * @return
	 */
	public static List<FunctionCell> getAllFunctionCellsOfFunctionSubDomain(FunctionSubDomain entity) {
		List<FunctionCell> entities = new ArrayList<FunctionCell>();
		if (entity == null) {
			logger.warning("传入一个空的功能子域对象");
			return entities;
		}
		logger.fine("子域的内容:\n" + entity.toString());
		List<Entity> logicChildren = entity.getLogicChildren();
		for (Entity logicChild : logicChildren) {
			if (logicChild instanceof FunctionCell) {
				logger.fine("功能单元的内容为:" + logicChild.toString());
				entities.add((FunctionCell) logicChild);
			}
		}
		logger.fine("获取功能子域下的所有功能单元的个数为:" + entities.size());
		return entities;
	}

	/**
	 * 获取功能域消息的所有目的ID
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
			logger.warning("在功能域消息下找不到目的节点");
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
	 * 获取功能节点消息下的所有目的功能ID
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
			logger.warning("在功能节点消息下找不到目的节点");
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
	 * 获取功能单元消息下的所有目的功能ID
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
			logger.warning("在功能单元消息下找不到目的节点");
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
	 * 获取功能子域消息下的所有目的功能ID
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
			logger.warning("在功能子域消息下找不到目的节点");
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
	 * 获取功能节点消息下的所有目的功能ID
	 * 
	 * @param clazzManager
	 * @param msg
	 * @return </br>
	 */
	private static List<Destnation> getDestFunctionNodes(ClazzManager clazzManager, /* FunctionNodeMsg */Entity msg) {
		// 目的ID集合
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
			logger.warning("在功能节点消息下找不到目的节点");
			return destList;
		}

		children = dests.getChildren();
		for (Entity dest : children) {
			Destnation destnation = (Destnation) dest;
			destList.add(destnation);
		}

		logger.fine("功能目的IDs的个数为：" + destList.size());

		return destList;
	}

	/**
	 * 获取功能节点消息下的所有目的功能ID。如果是发送消息则需要过滤掉测试工具，如果是接收消息则需要过滤掉被测对象。
	 * 
	 * @param clazzManager
	 * @param msg
	 * @param testedObjects
	 * @return </br>
	 */
	private static List<Destnation> getDestFunctionNodes(ClazzManager clazzManager, FunctionNodeMsg msg, List<Entity> testedObjects) {
		// 目的ID集合
		List<Destnation> destList = getDestFunctionNodes(clazzManager, msg);

		// 判断是发送消息还是接收消息，如果是发送消息则需要过滤掉测试工具，如果是接收消息则需要过滤掉被测对象
		if (testedObjects.size() > 0) {
			try {
				boolean send = isSendMsg(msg, testedObjects);
				boolean recv = isRecvMsg(msg, testedObjects);
				if (recv) {// 接收消息
					// 过滤掉被测对象集合中的功能节点
					for (Entity entity : testedObjects) {
						if (entity instanceof FunctionCell) {
							logger.warning("用户将功能单元设置为被测对象。");
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
				} else if (send) {// 发送消息
					// 过滤掉测试工具集合中的功能节点
					for (Entity entity : testedObjects) {
						if (entity instanceof FunctionCell) {
							logger.warning("用户将功能单元设置为被测对象。");
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

		logger.fine("功能目的IDs的个数为：" + destList.size());

		return destList;
	}

	/**
	 * 获取功能单元消息下的所有目的功能ID
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
			logger.warning("在功能单元消息下找不到目的节点");
			return destList;
		}
		children = dests.getChildren();
		for (Entity dest : children) {
			Destnation destnation = (Destnation) dest;
			destList.add(destnation);
		}
		// 判断是发送消息还是接收消息，如果是发送消息则需要过滤掉测试工具，如果是接收消息则需要过滤掉被测对象
		if (testedObjects.size() > 0) {
			boolean send = isSendMsg(msg, testedObjects);
			boolean recv = isRecvMsg(msg, testedObjects);
			if (recv) {// 接收消息
				// 过滤掉被测对象集合中的功能单元
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
			} else if (send) {// 发送消息
				// 过滤掉测试工具集合中的功能单元
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
		logger.config("功能目的IDs的个数为：" + destList.size());

		return destList;
	}

	/**
	 * 获取功能子域消息下的所有目的功能ID
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
			logger.warning("在功能子域消息下找不到目的节点");
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
	 * 获取功能子域消息下的所有目的功能ID。如果消息为发送消息，则需要过滤掉测试工具，如果为接收消息则需要过滤掉被测对象。
	 * 
	 * @param clazzManager
	 * @param msg
	 * @param testedObjects
	 *            被测对象集合
	 * @return </br>
	 */
	private static List<Destnation> getDestFunctionSubDomains(ClazzManager clazzManager, FunctionSubDomainMsg msg, List<Entity> testedObjects) {
		List<Destnation> destList = getDestFunctionSubDomains(clazzManager, msg);

		// 判断是发送消息还是接收消息，如果是发送消息则需要过滤掉测试工具，如果是接收消息则需要过滤掉被测对象
		if (testedObjects.size() > 0) {
			try {
				boolean send = isSendMsg(msg, testedObjects);
				boolean recv = isRecvMsg(msg, testedObjects);
				if (recv) {// 接收消息
					// 过滤掉被测对象集合中的功能子域
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
				} else if (send) {// 发送消息
					// 过滤掉测试工具集合中的功能子域
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

		logger.fine("功能目的IDs的个数为：" + destList.size());

		return destList;
	}

	/**
	 * 获取功能域消息下的所有目的功能ID
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
			logger.warning("在功能域消息下找不到目的节点");
			return destList;
		}
		children = dests.getChildren();
		for (Entity dest : children) {
			Destnation destnation = (Destnation) dest;
			destList.add(destnation);
		}
		// 判断是发送消息还是接收消息，如果是发送消息则需要过滤掉测试工具，如果是接收消息则需要过滤掉被测对象
		if (testedObjects.size() > 0) {
			try {
				boolean send = isSendMsg(msg, testedObjects);
				boolean recv = isRecvMsg(msg, testedObjects);
				if (recv) {// 接收消息
					// 过滤掉被测对象集合中的功能域
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
				} else if (send) {// 发送消息
					// 过滤掉测试工具集合中的功能域
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

		logger.config("功能目的IDs的个数为：" + destList.size());

		return destList;
	}

	/**
	 * 判断功能消息是否为发送消息。如果消息为广播消息，则应该当作发送消息处理
	 * 
	 * @param functionMsg
	 *            功能消息
	 * @param testedObjects
	 *            被测对象集合
	 * @return 如果是发送消息返回true，否则返回false</br>
	 * @throws Exception
	 *             当无法判断消息是否为发送消息时会抛出此异常
	 */
	private static boolean isSendMsg(Entity functionMsg, List<Entity> testedObjects) {
		if (testedObjects == null || testedObjects.size() == 0) {

			return false;
		}

		if (functionMsg instanceof FunctionDomainMsg) {// 功能域消息

			return isSendMsg((FunctionDomainMsg) functionMsg, testedObjects);

		} else if (functionMsg instanceof FunctionSubDomainMsg) {// 功能子域消息

			return isSendMsg((FunctionSubDomainMsg) functionMsg, testedObjects);

		} else if (functionMsg instanceof FunctionCellMsg) {// 功能单元消息

			return isSendMsg((FunctionCellMsg) functionMsg, testedObjects);

		} else if (functionMsg instanceof FunctionNodeMsg) {// 功能节点消息

			return isSendMsg((FunctionNodeMsg) functionMsg, testedObjects);

		}
		logger.warning("无法确认是发送消息还是接收消息");

		return false;
	}

	/**
	 * 判断功能消息是否为接收消息。如果消息为广播消息，则应该当作发送消息处理。
	 * 
	 * @param functionMsg
	 *            功能消息
	 * @param testedObjects
	 *            被测对象集合
	 * @return 如果是发送消息返回true，否则返回false</br>
	 * @throws Exception
	 *             当无法判断消息是否为发送消息时会抛出此异常
	 */
	private static boolean isRecvMsg(Entity functionMsg, List<Entity> testedObjects) {
		if (testedObjects == null || testedObjects.size() == 0) {

			return false;
		}

		if (functionMsg instanceof FunctionDomainMsg) {// 功能域消息

			return isRecvMsg((FunctionDomainMsg) functionMsg, testedObjects);

		} else if (functionMsg instanceof FunctionSubDomainMsg) {// 功能子域消息

			return isRecvMsg((FunctionSubDomainMsg) functionMsg, testedObjects);

		} else if (functionMsg instanceof FunctionCellMsg) {// 功能单元消息

			return isRecvMsg((FunctionCellMsg) functionMsg, testedObjects);

		} else if (functionMsg instanceof FunctionNodeMsg) {// 功能节点消息

			return isRecvMsg((FunctionNodeMsg) functionMsg, testedObjects);

		}
		logger.warning("无法确认消息是接收消息");

		return false;
	}

	/**
	 * 判断功能域消息是否为发送消息
	 * 
	 * @param domainMsg
	 * @param testedObjects
	 * @return
	 */
	private static boolean isSendMsg(FunctionDomainMsg domainMsg, List<Entity> testedObjects) {
		/*
		 * 需要判断是发送消息还是接收消息。 当消息的源ID与被测试对象的ID相等时，则为接收消息，当消息的功能目的ID与
		 * 被测试对象的ID相等时，则为发送消息。
		 */
		// 判断是否为接收消息
		FunctionDomainMsg msg = domainMsg;
		// 判断是否为发送消息
		// 获取消息的发送目的IDs
		List<Destnation> destList = getDestnations(msg);
		for (Destnation dest : destList) {
			// 发送目的ID
			Integer destID = dest.getDestID();
			for (Entity testedObject : testedObjects) {
				if (destID.equals(testedObject.getFieldValue("ID")))
					return true;
			}
		}
		return false;
	}

	/**
	 * 判断功能域消息是否为接收消息
	 * 
	 * @param domainMsg
	 * @param testedObjects
	 * @return
	 */
	private static boolean isRecvMsg(FunctionDomainMsg domainMsg, List<Entity> testedObjects) {
		/*
		 * 需要判断是发送消息还是接收消息。 当消息的源ID与被测试对象的ID相等时，则为接收消息，当消息的功能目的ID与
		 * 被测试对象的ID相等时，则为发送消息。
		 */
		// 判断是否为接收消息
		FunctionDomainMsg msg = domainMsg;
		Integer srcID = msg.getSourceFunctionID();// 源ID
		for (Entity function : testedObjects) {
			if (function instanceof FunctionDomain) {
				FunctionDomain domain = (FunctionDomain) function;
				if (srcID.equals(domain.getID())) {
					return true;// 接收消息
				}
			} else if (function instanceof FunctionSubDomain) {
				FunctionSubDomain domain = (FunctionSubDomain) function;
				if (srcID.equals(domain.getID())) {
					return true;// 接收消息
				}
			} else if (function instanceof FunctionCell) {
				FunctionCell domain = (FunctionCell) function;
				if (srcID.equals(domain.getID())) {
					return true;// 接收消息
				}
			} else if (function instanceof FunctionNode) {
				FunctionNode domain = (FunctionNode) function;
				if (srcID.equals(domain.getID())) {
					return true;// 接收消息
				}
			}
		}

		return false;
	}

	/**
	 * 判断功能子域消息是否为发送消息
	 * 
	 * @param subMsg
	 * @param testedObjects
	 * @return
	 */
	private static boolean isSendMsg(FunctionSubDomainMsg subMsg, List<Entity> testedObjects) {
		/*
		 * 需要判断是发送消息还是接收消息。 当消息的源ID与被测试对象的ID相等时，则为接收消息，当消息的功能目的ID与
		 * 被测试对象的ID相等时，则被发送对象。
		 */
		FunctionSubDomainMsg msg = subMsg;
		// 判断是否为发送消息
		// 获取消息的发送目的IDs
		List<Destnation> destList = getDestnations(msg);
		for (Destnation dest : destList) {
			// 发送目的ID
			Integer destID = dest.getDestID();
			for (Entity testedObject : testedObjects) {
				if (destID.equals(testedObject.getFieldValue("ID")))
					return true;
			}
		}
		return false;
	}

	/**
	 * 判断功能子域消息是否为接收消息
	 * 
	 * @param subMsg
	 * @param testedObjects
	 * @return
	 */
	private static boolean isRecvMsg(FunctionSubDomainMsg subMsg, List<Entity> testedObjects) {
		/*
		 * 需要判断是发送消息还是接收消息。 当消息的源ID与被测试对象的ID相等时，则为接收消息，当消息的功能目的ID与
		 * 被测试对象的ID相等时，则被发送对象。
		 */
		// 判断是否为接收消息
		FunctionSubDomainMsg msg = subMsg;
		Integer srcID = msg.getSourceFunctionID();// 源ID
		for (Entity function : testedObjects) {
			if (function instanceof FunctionSubDomain) {
				FunctionSubDomain domain = (FunctionSubDomain) function;
				if (srcID.equals(domain.getID())) {
					return true;// 接收消息
				}
			}
		}
		return false;
	}

	/**
	 * 判断功能单元消息是否为发送消息
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
		 * 需要判断是发送消息还是接收消息。 当消息的源ID与被测试对象的ID相等时，则为接收消息，当消息的功能目的ID与
		 * 被测试对象的ID相等时，则被发送对象。
		 */
		// 判断是否为接收消息
		FunctionCellMsg msg = cellMsg;
		// 判断是否为发送消息
		// 获取消息的发送目的IDs

		List<Destnation> destList = getDestnations(msg);

		for (Destnation dest : destList) {
			// 发送目的ID
			Integer destID = dest.getDestID();
			for (Entity testedObject : testedObjects) {
				if (destID.equals(testedObject.getFieldValue("ID")))
					return true;
			}
		}

		return false;
	}

	/**
	 * 判断功能单元消息是否为接收消息
	 * 
	 * @param cellMsg
	 * @param testedObjects
	 * @return
	 */
	private static boolean isRecvMsg(FunctionCellMsg cellMsg, List<Entity> testedObjects) {
		/*
		 * 需要判断是发送消息还是接收消息。 当消息的源ID与被测试对象的ID相等时，则为接收消息，当消息的功能目的ID与
		 * 被测试对象的ID相等时，则被发送对象。
		 */
		// 判断是否为接收消息
		FunctionCellMsg msg = cellMsg;
		Integer srcID = msg.getSourceFunctionID();// 源ID
		for (Entity function : testedObjects) {
			if (function instanceof FunctionCell) {
				FunctionCell domain = (FunctionCell) function;
				if (srcID.equals(domain.getID())) {
					return true;// 接收消息
				}
			}
		}

		return false;
	}

	/**
	 * 判断功能节点消息是否为发送消息
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
		 * 需要判断是发送消息还是接收消息。 当消息的源ID与被测试对象的ID相等时，则为接收消息，当消息的功能目的ID与
		 * 被测试对象的ID相等时，则被发送对象。
		 */
		// 判断是否为发送消息
		// 获取消息的发送目的IDs
		List<Destnation> destList = getDestnations(nodeMsg);
		// 判断是否为广播消息，如果是，则判断消息的发送源是否为被测对象，如果不是，则为发送消息
		for (Destnation dest : destList) {
			// 发送目的ID
			Integer destID = dest.getDestID();
			for (Entity testedObject : testedObjects) {
				if (destID.equals(testedObject.getFieldValue("ID")))
					return true;
			}
		}

		return false;
	}

	/**
	 * 判断功能节点消息是否为接收消息
	 * 
	 * @param nodeMsg
	 * @param testedObjects
	 * @return
	 */
	public static boolean isRecvMsg(FunctionNodeMsg nodeMsg, List<Entity> testedObjects) {
		/*
		 * 需要判断是发送消息还是接收消息。 当消息的源ID与被测试对象的ID相等时，则为接收消息，当消息的功能目的ID与
		 * 被测试对象的ID相等时，则被发送对象。
		 */
		// 判断是否为接收消息
		FunctionNodeMsg msg = nodeMsg;
		Integer srcID = msg.getSourceFunctionID();// 源ID
		for (Entity function : testedObjects) {
			if (function instanceof FunctionNode) {
				FunctionNode node = (FunctionNode) function;
				if (srcID.equals(node.getID())) {
					return true;// 接收消息
				}
			}
		}

		return false;
	}

	/**
	 * 获取功能域下的所有功能节点
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
	 * 获取功能域下的所有功能单元
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
	 * 获取目的功能域对象集合
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
	 * 获取目的功能子域对象集合
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
	 * 获取目的功能节点对象集合
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
	 * 获取目的功能节点对象集合
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
	 * 获取功能域消息的所有功能子域消息
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
	 * 获取功能子域消息的所有功能节点消息
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
	 * 获取功能子域消息的所有功能单元消息
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
	 * 获取SPTEMsg对象
	 * 
	 * @param clazzManager
	 * @param message
	 *            测试用例消息
	 * @return
	 */
	public static SPTEMsg getSPTEMsg(ClazzManager clazzManager, Message message) {
		Integer topicId = Integer.valueOf(message.getTopicId());
		Integer srcId = Integer.valueOf(message.getSrcId());
		Entity nodeMsg = clazzManager.getFunctionNodeMsg(message.getId());
		// 对功能单元进行查找
		if (nodeMsg == null) {
			nodeMsg = clazzManager.getFunctionCellMsg(message.getId());
		}
		try {
			List<SPTEMsg> msgs = getSPTEMsgsOfFunctionMsg(clazzManager, TemplateEngine.getEngine().getUnitManager(), nodeMsg, new ArrayList<Entity>(), false);
			if (msgs.size() > 0) {
				msgs.get(0).setMsg(message);
				return msgs.get(0);
			}
			logger.warning("根据用户传入的topicId=" + topicId + " srcId=" + srcId + " 找不到SPTEMsg对象。");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 获取SPTEMsg对象
	 * 
	 * @param clazzManager
	 * @param topicId
	 *            主题ID
	 * @param srcId
	 *            消息源ID
	 * @return
	 */
	public static SPTEMsg getSPTEMsg(ClazzManager clazzManager, Integer topicId, Integer srcId, String level) {

		return getSPTEMsg(clazzManager, TemplateEngine.getEngine().getUnitManager(), topicId, srcId, level);
	}

	/**
	 * 获取SPTEMsg对象。
	 * 
	 * @param clazzManager
	 *            icd类管理器
	 * @param unitManager
	 *            单位类管理器
	 * @param topicId
	 *            主题ID
	 * @param srcId
	 *            消息的源ID
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
			logger.warning("根据用户传入的topicId=" + topicId + " srcId" + srcId + " 找不到对应的SPTEMsg对象。");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 获取消息对应父节点的层级
	 * 
	 * @param msg
	 *            消息
	 * @return 消息对应父节点的层级
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
	 * 获取测试用例消息集合
	 * 
	 * @param clazzManager
	 *            icd对象管理器
	 * @param msg
	 *            消息
	 * @param testedObjects
	 *            被测试对象集合
	 * @param ignoreCell
	 *            是否忽略功能单元消息
	 * @return
	 * @throws Exception
	 *             当无法判断消息是否为发送消息时会抛出此异常
	 */
	public static List<SPTEMsg> filter(ClazzManager clazzManager, Object msg, List<Entity> testedObjects, boolean ignoreCell) throws Exception {
		if (msg instanceof ICDFunctionDomainMsg) {
			ICDFunctionDomainMsg icdDomainMsg = (ICDFunctionDomainMsg) msg;
			List<FunctionDomainMsg> msgs = clazzManager.getAllFunctionDomainMsgs();
			for (FunctionDomainMsg domainMsg : msgs) {
				if (domainMsg.getMsgID().equals(icdDomainMsg.getAttribute("msgID").getValue().toString()))
					return getSPTEMsgsOfFunctionMsg(clazzManager, TemplateEngine.getEngine().getUnitManager(), domainMsg, testedObjects, ignoreCell);
			}
			logger.warning("找不到msgID=" + icdDomainMsg.getAttribute("msgID").getValue() + "功能域消息。");
		} else if (msg instanceof ICDFunctionSubDomainMsg) {
			ICDFunctionSubDomainMsg icdSubMsg = (ICDFunctionSubDomainMsg) msg;
			List<FunctionSubDomainMsg> msgs = clazzManager.getAllFunctionSubDomainMsgs();
			for (FunctionSubDomainMsg subMsg : msgs) {
				if (subMsg.getMsgID().equals(icdSubMsg.getAttribute("msgID").getValue().toString()))
					return getSPTEMsgsOfFunctionMsg(clazzManager, TemplateEngine.getEngine().getUnitManager(), subMsg, testedObjects, ignoreCell);
			}
			logger.warning("找不到msgID=" + icdSubMsg.getAttribute("msgID").getValue() + "功能子域消息。");
		} else if (msg instanceof ICDFunctionNodeMsg) {
			ICDFunctionNodeMsg nodeMsg = (ICDFunctionNodeMsg) msg;
			List<FunctionNodeMsg> msgs = clazzManager.getAllFunctionNodeMsgs();
			for (FunctionNodeMsg subMsg : msgs) {
				if (subMsg.getMsgID().equals(nodeMsg.getAttribute("msgID").getValue().toString()))
					return getSPTEMsgsOfFunctionMsg(clazzManager, TemplateEngine.getEngine().getUnitManager(), subMsg, testedObjects, ignoreCell);
			}
			logger.warning("找不到msgID=" + nodeMsg.getAttribute("msgID").getValue() + "功能节点消息。");
		} else if (msg instanceof ICDFunctionCellMsg) {
			ICDFunctionCellMsg cellMsg = (ICDFunctionCellMsg) msg;
			List<FunctionCellMsg> msgs = clazzManager.getAllFunctionCellMsgs();
			for (FunctionCellMsg subMsg : msgs) {
				if (subMsg.getMsgID().equals(cellMsg.getAttribute("msgID").getValue().toString()))
					return getSPTEMsgsOfFunctionMsg(clazzManager, TemplateEngine.getEngine().getUnitManager(), subMsg, testedObjects, ignoreCell);
			}
			logger.warning("找不到msgID=" + cellMsg.getAttribute("msgID").getValue() + "功能单元消息。");
		}

		logger.warning("传入的参数类型不正确。");

		return new ArrayList<SPTEMsg>(0);
	}

	/**
	 * 获取功能对象消息所对应的测试用例消息集合。
	 * 
	 * @param icdManager
	 *            ICD对象管理器
	 * @param functionMsg
	 *            功能消息
	 * @param testedObjects
	 *            被测对象
	 * @param ignoreCell
	 *            是否忽略功能单元
	 * @return
	 * @throws Exception
	 *             当无法判断消息是否为发送消息时会抛出此异常
	 */

	private static List<SPTEMsg> getSPTEMsgsOfFunctionMsg(ClazzManager icdManager, ClazzManager unitManager, Entity functionMsg, List<Entity> testedObjects, boolean ignoreCell) throws Exception {
		List<SPTEMsg> beans = new ArrayList<SPTEMsg>();
		Set<Entity> msgs = new HashSet<Entity>();
		List<Destnation> destList = null;// 功能目的IDs
		boolean isSend = isSendMsg(functionMsg, testedObjects);
		boolean isRecv = isRecvMsg(functionMsg, testedObjects);
		if (functionMsg == null)
			return beans;
		if (functionMsg instanceof FunctionDomainMsg) {// 功能域消息
			FunctionDomainMsg domainMsg = (FunctionDomainMsg) functionMsg;
			destList = getDestFunctionDomains(icdManager, domainMsg, testedObjects);

		} else if (functionMsg instanceof FunctionSubDomainMsg) {// 功能子域消息
			FunctionSubDomainMsg subMsg = (FunctionSubDomainMsg) functionMsg;
			destList = getDestFunctionSubDomains(icdManager, subMsg, testedObjects);

		} else if (functionMsg instanceof FunctionCellMsg) {// 功能单元消息
			FunctionCellMsg cellMsg = (FunctionCellMsg) functionMsg;
			destList = getDestFunctionCells(icdManager, cellMsg, testedObjects);

		} else if (functionMsg instanceof FunctionNodeMsg) {// 功能节点消息
			FunctionNodeMsg nodeMsg = (FunctionNodeMsg) functionMsg;
			destList = getDestFunctionNodes(icdManager, nodeMsg, testedObjects);

		} else {
			logger.warning("无法判断出消息的类型。class=" + functionMsg.getClass().getName());
			return beans;
		}

		if (functionMsg instanceof FunctionDomainMsg) {// 域消息
			FunctionDomainMsg domainMsg = (FunctionDomainMsg) functionMsg;
			// 获取域消息的所有子域消息
			List<FunctionSubDomainMsg> subDomainMsgs = getAllFunctionSubDomainMsgs(icdManager, domainMsg);
			// 获取功能子域下的所有功能节点消息
			List<FunctionNodeMsg> nodeMsgs = new ArrayList<FunctionNodeMsg>();
			for (FunctionSubDomainMsg subDomainMsg : subDomainMsgs) {
				nodeMsgs.addAll(getAllFunctionNodeMsgs(icdManager, subDomainMsg));
			}

			/*
			 * 获取目的功能域对象下的所有功能节点与功能单元IDs
			 */
			List<FunctionDomain> destDomains = getDestFunctionDomains(icdManager, destList);// 目的功能对象集合
			List<FunctionNode> nodes = new ArrayList<FunctionNode>();
			List<Integer> nodeIDs = new ArrayList<Integer>();// 目的域的所有功能节点IDs
			for (FunctionDomain destDomain : destDomains) {
				nodes.addAll(getAllFunctionNodes(destDomain));
			}
			for (FunctionNode node : nodes) {
				nodeIDs.add(node.getID());
			}

			// 获取功能节点消息的目的集合
			for (FunctionNodeMsg msg : nodeMsgs) {
				List<Destnation> nodeDestList = getDestnations(msg);// 源功能节点消息的目的IDs
				if (msg.getBrocast().equals("BROADCAST")) {
					msgs.add(msg);
				} else {
					// 消息过滤
					for (Destnation dest : nodeDestList) {
						for (Integer nodeID : nodeIDs) {
							if (nodeID.equals(dest.getDestID()))
								msgs.add(msg);
						}
					}
				}

			}
			// 如果未设置忽略功能单元消息，则获取功能单元消息的目的集合
			if (!ignoreCell) {
				// 获取功能子域下的所有功能单元消息
				List<FunctionCellMsg> cellMsgs = new ArrayList<FunctionCellMsg>();
				for (FunctionSubDomainMsg subDomainMsg : subDomainMsgs) {
					cellMsgs.addAll(getAllFunctionCellMsgs(icdManager, subDomainMsg));
				}
				List<FunctionCell> cells = new ArrayList<FunctionCell>();
				nodeIDs = new ArrayList<Integer>();// 目的功能单元IDs
				for (FunctionDomain destDomain : destDomains) {
					cells.addAll(getAllFunctionCells(destDomain));
				}

				for (FunctionCell cell : cells) {
					nodeIDs.add(cell.getID());
				}

				for (FunctionCellMsg msg : cellMsgs) {
					List<Destnation> nodeDestList = getDestnations(msg);// 源功能单元消息的目的IDs
					if (msg.getBrocast().equals("BROADCAST")) {
						msgs.add(msg);
					} else {
						// 消息过滤
						for (Destnation dest : nodeDestList) {
							for (Integer nodeID : nodeIDs) {
								if (nodeID.equals(dest.getDestID()))
									msgs.add(msg);
							}
						}
					}
				}
			}
		} else if (functionMsg instanceof FunctionSubDomainMsg) {// 功能子域消息
			// 获取目的功能对象s下的所有功能节点与功能单元
			FunctionSubDomainMsg subMsg = (FunctionSubDomainMsg) functionMsg;
			List<FunctionSubDomain> destDomains = getDestFunctionSubDomains(icdManager, destList);// 目的功能对象集合
			// 遍历出集合destDomains中的所有功能节点与功能单元的IDs
			List<FunctionNode> nodes = new ArrayList<FunctionNode>();
			List<Integer> nodeIDs = new ArrayList<Integer>();// 目的子域的所有功能节点IDs
			for (FunctionSubDomain destDomain : destDomains) {
				nodes.addAll(getAllFunctionNodesOfFunctionSubDomain(destDomain));
			}
			for (FunctionNode node : nodes) {
				nodeIDs.add(node.getID());
			}
			// 获取功能子域下的所有功能节点消息
			List<FunctionNodeMsg> nodeMsgs = getAllFunctionNodeMsgs(icdManager, subMsg);

			for (FunctionNodeMsg msg : nodeMsgs) {
				List<Destnation> nodeDestList = getDestnations(msg);// 源功能节点消息的目的IDs
				if (msg.getBrocast().equals("BROADCAST")) {
					msgs.add(msg);
				} else {
					for (Destnation dest : nodeDestList) {
						for (Integer nodeID : nodeIDs) {
							if (nodeID.equals(dest.getDestID())) {
								// 避免组播消息中有多个目的节点号，造成消息重复添加
								msgs.add(msg);
							}

						}
					}
				}

			}

			if (!ignoreCell) {
				// 获取功能子域下的所有功能单元消息
				List<FunctionCellMsg> cellMsgs = getAllFunctionCellMsgs(icdManager, subMsg);
				List<FunctionCell> cells = new ArrayList<FunctionCell>();
				nodeIDs = new ArrayList<Integer>();// 目的功能单元IDs
				for (FunctionSubDomain destDomain : destDomains) {
					cells.addAll(getAllFunctionCellsOfFunctionSubDomain(destDomain));
				}

				for (FunctionCell cell : cells) {
					nodeIDs.add(cell.getID());
				}

				for (FunctionCellMsg msg : cellMsgs) {
					List<Destnation> nodeDestList = getDestnations(msg);// 源功能单元消息的目的IDs
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
		} else if (functionMsg instanceof FunctionNodeMsg) {// 功能节点消息
			FunctionNodeMsg nodeMsg = (FunctionNodeMsg) functionMsg;
			// 获取目的功能节点集合
			List<FunctionNode> destNodes = getDestFunctionNodes(icdManager, destList);
			List<FunctionNode> filteredNodes = new ArrayList<FunctionNode>();
			if (testedObjects.size() > 0) {
				if (isSend) {// 如果是发送消息，则应该过滤掉发送目的为测试工具的消息
					for (FunctionNode node : destNodes) {
						if (testedObjects.indexOf(node) >= 0) {
							filteredNodes.add(node);
						}
					}
				} else if (isRecv) {// 如果是接收消息，则应该过滤掉发送目的为被测工具的消息
					for (FunctionNode node : destNodes) {
						if (testedObjects.indexOf(node) < 0) {
							filteredNodes.add(node);
						}
					}
				}

				/*
				 * 当消息是发送消息时，如果发送目的全为测试工具，则此消息不满足条件，不应该被转换为SPTEMsg消息 ；
				 * 当消息是接收消息时，如果发送目的全为被测试对象，则此消息也不满足条件，不应该被转换为SPTEMsg消息；
				 * 当消息为广播消息时，则满足条件
				 */
				if (filteredNodes.size() == 0 && !nodeMsg.getBrocast().equals("BROADCAST"))
					return beans;
			}

			msgs.add(nodeMsg);

		} else if (functionMsg instanceof FunctionCellMsg && !ignoreCell) {// 功能单元消息
			FunctionCellMsg cellMsg = (FunctionCellMsg) functionMsg;
			// 获取目的功能节点集合
			List<FunctionCell> destNodes = getDestFunctionCells(icdManager, destList, testedObjects);
			List<FunctionCell> filteredNodes = new ArrayList<FunctionCell>();
			if (testedObjects.size() > 0) {
				if (isSend) {// 如果是发送消息，则应该过滤掉发送目的为测试工具的消息
					// 过滤掉被作为被测对象的单元
					for (FunctionCell node : destNodes) {
						if (testedObjects.indexOf(node) >= 0) {
							filteredNodes.add(node);
						}
					}
				} else if (isRecv) {// 如果是接受消息，则应该过滤掉发送目的为被测工具的消息
					// 过滤掉被作为被测对象的单元
					for (FunctionCell node : destNodes) {
						if (testedObjects.indexOf(node) < 0) {
							filteredNodes.add(node);
						}
					}
				}

				/*
				 * 当消息是发送消息时，如果发送目的全为测试工具，则此消息不满足条件，不应该被转换为SPTEMsg消息 ；
				 * 当消息是接收消息时，如果发送目的全为被测试对象，则此消息也不满足条件，不应该被转换为SPTEMsg消息；
				 * 当消息为广播消息时，则满足条件
				 */
				if (filteredNodes.size() == 0 && !cellMsg.getBrocast().equals("BROADCAST"))
					return beans;
			}

			msgs.add(cellMsg);
		}

		return setMessageDirection(msgs, functionMsg, icdManager, unitManager, testedObjects);
	}

	/**
	 * 设置SPTEMsg对象的发送方向以及是否为周期消息
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
	 *            ICD对象管理器
	 * @param functionMsg
	 *            功能消息
	 * @param testedObjects
	 *            被测对象
	 * @param send
	 *            是否是发送消息
	 * @param ignoreCell
	 *            是否忽略功能单元
	 * @throws Exception
	 *             当无法判断消息是否为发送消息时会抛出此异常
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
	 * 获取消息列表中的发送或者接收消息
	 * 
	 * @param msgs
	 * @param isSend
	 * @return
	 */
	public static List<SPTEMsg> filterSpteMsgOfSend(List<SPTEMsg> msgs, boolean isSend) {
		List<SPTEMsg> spteMsg = new ArrayList<SPTEMsg>();

		if (isSend) {// 筛选出发送消息
			for (SPTEMsg msg : msgs) {
				if (msg.getMsg().isSend()) {
					spteMsg.add(msg);
				}
			}
		} else {// 筛选出接收消息
			for (SPTEMsg msg : msgs) {
				if (msg.getMsg().isRecv()) {
					spteMsg.add(msg);
				}
			}
		}

		return spteMsg;
	}

	/**
	 * 获取消息列表中的发送或者接收消息
	 * 
	 * @param msgs
	 * @param isPeriod
	 * @return
	 */
	public static List<SPTEMsg> filterSpteMsgOfPeriod(List<SPTEMsg> msgs, boolean isPeriod) {
		List<SPTEMsg> spteMsg = new ArrayList<SPTEMsg>();

		try {
			if (isPeriod) {// 筛选出发送消息
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
	 * 获取机型下所以的功能节点消息
	 * 
	 * @param entity
	 *            机型
	 * @return
	 */
	public static List<Entity> getAllFunctionNodeMsgOfFighter(Entity entity) {
		List<Entity> entities = new ArrayList<Entity>();
		if (entity == null) {
			logger.warning("传入的参数为空。");
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
			logger.warning("期望传入的参数类型为Fighter，却传入" + entity.getClass().getName());
		}
		return entities;
	}

	/**
	 * 获取功能域下的所有功能子域
	 * 
	 * @param entity
	 *            功能域
	 * @return
	 */
	public static List<Entity> getAllFunctionSubDomainsOfFunctionbDomain(Entity entity) {
		List<Entity> entities = new ArrayList<Entity>();
		if (entity == null) {
			logger.warning("传入的参数为空");
			return entities;
		}
		if (entity instanceof FunctionDomain) {
			logger.fine("功能域的内容:\n" + entity.toString());
			List<Entity> logicChildren = entity.getLogicChildren();
			for (Entity logicChild : logicChildren) {
				if (logicChild instanceof FunctionSubDomain) {
					logger.fine("功能子域的内容:\n" + logicChild.toString());
					entities.add(logicChild);
				}
			}
		} else {
			logger.warning("期望传入的参数类型为FunctionSubDomain，却传入" + entity.getClass().getName());
		}
		logger.fine("获取功能域下的所有功能子域个数为:" + entities.size());
		return entities;
	}

	/**
	 * 判断一个消息是否为周期消息
	 * 
	 * @param entity
	 *            功能消息对象
	 * @return
	 */
	public static boolean isPeriod(Entity entity) {
		if (entity == null) {
			logger.warning("传入的参数为空");
			return false;
		}

		// 周期消息的判断依据为，消息的"消息传输类型"如果为period则为周期消息
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
			logger.warning("参数的类型为:" + entity.getClass().getName());
		}
		return false;
	}

	/**
	 * 从功能节点消息转换为测试用例消息
	 * 
	 * @param msg
	 *            功能节点消息
	 * @return
	 * @throws Exception
	 *             当找不到对应的主题时会出现此异常
	 */
	private static SPTEMsg convertFromFunctionNodeMsg(FunctionNodeMsg msg, ClazzManager clazzManager, ClazzManager unitManager) throws Exception {
		if (msg == null) {
			logger.warning("传入的功能节点参数为空");
			return null;
		}
		FunctionNodeMsg nodeMsg = (FunctionNodeMsg) msg;
		Message message = new Message();
		message.setSrcId(nodeMsg.getSourceFunctionID().toString());// 发送源
		message.setUuid(UUID.randomUUID().toString());
		message.setId(nodeMsg.getMsgID());// 消息ID
		Entity entityTopic = clazzManager.getTopic(nodeMsg.getMsgTopicSymbol());
		if (entityTopic != null) {
			Topic topic = (Topic) entityTopic;
			message.setTopicId(topic.getTopicID().toString());
			message.setWidth(topic.getDataLength());
		} else {
			logger.severe("根据topicSymbol=" + nodeMsg.getMsgTopicSymbol() + " 找不到对应的主题。");
			return null;
		}
		message.setSendDuration(nodeMsg.getMsgTransPeriod());// 消息间隔
		message.setModelType(FUNCTION_NODE_MSG);// 设置消息为功能节点类型
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
		// 获取主题中的所有属性，将这些属性写入ICDMsg对象中
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
		// 遍历主题的所有引用信号，将这些信号生成对应的ICDField对象
		for (Entity entity : entyties) {
			if (entity instanceof com.coretek.spte.ContainedSignals) {// 主题包含信号
				List<Entity> children = entity.getChildren();
				for (Entity child : children) {
					if (child instanceof com.coretek.spte.ContainedSignal) {
						com.coretek.spte.ContainedSignal containedSignal = (com.coretek.spte.ContainedSignal) child;
						Signal signal = clazzManager.getSingal(containedSignal.getSignalSymbol());
						ICDField icdField = getSignal(signal, clazzManager, unitManager);
						icdMsg.addField(icdField);
						addAttribute(containedSignal, icdField);
						List<Entity> containedSingals = child.getChildren();// 被包含的信号
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
		if (!transType.equals(PERIOD_MSG)) {// 非周期消息
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
		} else {// 周期消息
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

		logger.fine("生成的测试用例消息内容是:\n" + message.toXML());

		return spteMsg;
	}

	/**
	 * 将ICDField对象转换为测试用例中的Field对象
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
	 * 解析出ICDField对象所包含的子ICDField对象。
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
	 * 为字段添加单位对象
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
					logger.warning("根据signalSymbol=" + csl.getSignalSymbol() + " 不能够获取对应的信号。");
					continue;
				}
				child.addICDField(getSignal(signal2, clazzManager, unitManager));

				ICDField child2 = new ICDField();
				addAttribute(entity, child2);
			} else if (entity instanceof EnumValueAndKey) {// 枚举值对
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
	 * 从功能单元消息转换为SPTEMsg对象
	 * 
	 * @param msg
	 * @param clazzManager
	 * @param unitManager
	 * @return
	 */
	private static SPTEMsg convertFromFunctionCellMsg(FunctionCellMsg msg, ClazzManager clazzManager, ClazzManager unitManager) {
		if (msg == null) {
			logger.warning("传入的参数为空");
			return null;
		}
		FunctionCellMsg cellMsg = (FunctionCellMsg) msg;
		Message message = new Message();
		message.setSrcId(cellMsg.getSourceFunctionID().toString());// 发送源
		message.setUuid(UUID.randomUUID().toString());
		message.setId(cellMsg.getMsgID());// 主题ID
		Entity entityTopic = clazzManager.getTopic(cellMsg.getMsgTopicSymbol());
		if (entityTopic != null) {
			Topic topic = (Topic) entityTopic;
			message.setTopicId(topic.getTopicID().toString());
		} else {
			logger.severe("根据topicSymbol=" + cellMsg.getMsgTopicSymbol() + " 找不到对应的主题。");
			return null;
		}
		message.setSendDuration(cellMsg.getMsgTransPeriod());// 消息间隔
		message.setModelType(FUNCTION_CELL_MSG);// 设置消息为功能节点类型
		message.setName(cellMsg.getMsgName());

		Topic topic = msg.getTopic();
		// 创建ICD主题对象供方便其他人使用
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
			if (entity instanceof com.coretek.spte.ContainedSignals) {// 主题包含信号
				List<Entity> children = entity.getChildren();
				for (Entity child : children) {
					if (child instanceof com.coretek.spte.ContainedSignal) {
						com.coretek.spte.ContainedSignal containedSignal = (com.coretek.spte.ContainedSignal) child;
						Signal signal = clazzManager.getSingal(containedSignal.getSignalSymbol());
						ICDField icdField = getSignal(signal, clazzManager, unitManager);
						icdMsg.addField(icdField);
						addAttribute(containedSignal, icdField);
						List<Entity> containedSingals = child.getChildren();// 被包含的信号
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
		if (!transType.equals(PERIOD_MSG)) {// 非周期消息
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
		} else {// 周期消息
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

		logger.config("生成的测试用例消息内容是:\n" + message.toXML());

		return spteMsg;
	}

	/**
	 * 获取单位对象
	 * 
	 * @param id
	 *            单位的ID
	 * @param clazzManager
	 *            类管理器
	 * @return 当无法找到匹配的单位时，会返回null
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
		logger.warning("未找到ID=" + id + "的单位对象。");
		return null;
	}

}