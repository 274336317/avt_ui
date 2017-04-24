/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.coretek.common.logging.LoggingPlugin;
import com.coretek.common.template.ClazzManager;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.common.template.build.codeTemplate.EntityRules;
import com.coretek.common.template.build.codeTemplate.FieldRules;
import com.coretek.common.template.build.codeTemplate.ReferenceRules;
import com.coretek.common.template.build.file.FieldTypes;

/**
 * ����Ŀ���ļ�
 * 
 * @author ���Ρ 2011-12-23
 */
public class TargetParser implements IParser
{
	private static final Logger	logger	= LoggingPlugin.getLogger(TargetParser.class.getName());

	private File				rulesFile;															// �����ļ�

	private File				targetFile;														// Ŀ���ļ�

	private Element				rulesRoot;

	private ClazzManager		clazzManager;

	/**
	 * ����һ��Ŀ���ļ�������
	 * 
	 * @param targetFile ICD�ļ�
	 * @param rulesFile �����ļ�
	 */
	public TargetParser(File targetFile, File rulesFile, ClazzManager clazzManager)
	{
		this.rulesFile = rulesFile;
		this.targetFile = targetFile;
		this.clazzManager = clazzManager;

	}

	@Override
	public Object parse(File file)
	{
		if (file == null || !file.exists())
		{
			logger.warning("The file does not exis! Application exit.");
			throw new IllegalArgumentException("The file does not exist!");
		}
		logger.config("Path:" + file.getAbsolutePath());
		this.targetFile = file;
		// �����ļ��ĸ��ڵ�
		this.rulesRoot = this.getRoot(this.rulesFile);
		NodeList children = this.rulesRoot.getChildNodes();
		for (int i = 0; i < children.getLength(); i++)
		{
			Node child = children.item(i);
			if (child.getNodeType() != Node.ELEMENT_NODE)
			{
				continue;
			}
			// Entities�ڵ�
			if ("Entities".equals(child.getNodeName()))
			{
				NodeList nodeList = child.getChildNodes();
				for (int j = 0; j < nodeList.getLength(); j++)
				{
					Node node = nodeList.item(j);
					if (node.getNodeType() != Node.ELEMENT_NODE)
					{
						continue;
					}
					// Entity�ڵ�
					if ("Entity".equals(node.getNodeName()))
					{
						try
						{
							this.parseEntity(node, null, "");

						} catch (DOMException e)
						{

							e.printStackTrace();
						} catch (SecurityException e)
						{

							e.printStackTrace();
						} catch (IllegalArgumentException e)
						{

							e.printStackTrace();
						} catch (ClassNotFoundException e)
						{

							e.printStackTrace();
						} catch (InstantiationException e)
						{

							e.printStackTrace();
						} catch (IllegalAccessException e)
						{

							e.printStackTrace();
						} catch (NoSuchMethodException e)
						{

							e.printStackTrace();
						} catch (InvocationTargetException e)
						{

							e.printStackTrace();
						}
					}
				}
			}
		}

		this.makup();
		this.makupLogic();
		return null;
	}

	/**
	 * ��װ����֮������ù�ϵ
	 */
	private void makup()
	{
		// ��װ����֮������ù�ϵ
		List<Entity> entities = this.clazzManager.getAllEntities();
		for (Entity entity : entities)
		{
			Class<? extends Entity> clazz = (Class<? extends Entity>) entity.getClass();
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields)
			{
				ReferenceRules ref = field.getAnnotation(ReferenceRules.class);
				if (ref != null)
				{
					// ��ȡ�����ö��������
					String refedClazz = ref.type();
					for (Entity refedEntity : entities)
					{
						if (refedEntity.getClass().getName().equals(refedClazz))
						{
							String condition = ref.condition();
							// �����ߵ��ֶ���
							String refer = (condition.split("="))[0];
							// �������ߵ��ֶ���
							String refed = (condition.split("="))[1];
							Object referValue = null;
							for (Field refField : fields)
							{
								if (refField.getName().equals(refer))
								{
									refField.setAccessible(true);
									try
									{
										referValue = refField.get(entity);
									} catch (IllegalArgumentException e)
									{

										e.printStackTrace();
									} catch (IllegalAccessException e)
									{

										e.printStackTrace();
									}
									break;
								}
							}
							// �����ö���������ֶ�
							Field[] refedFields = refedEntity.getClass().getDeclaredFields();
							// �������ߵ��ֶε�ֵ
							Object refedValue = null;
							// ���ұ������ֶε�ֵ
							for (Field refedField : refedFields)
							{
								if (refedField.getName().equals(refed))
								{
									refedField.setAccessible(true);
									try
									{
										refedValue = refedField.get(refedEntity);
									} catch (IllegalArgumentException e)
									{

										e.printStackTrace();
									} catch (IllegalAccessException e)
									{

										e.printStackTrace();
									}
									break;
								}
							}

							if (referValue != null && referValue.equals(refedValue))
							{
								// ���������ֶ�����Ӧ��set����
								field.setAccessible(true);
								try
								{
									field.set(entity, refedEntity);
								} catch (IllegalArgumentException e)
								{

									e.printStackTrace();
								} catch (IllegalAccessException e)
								{

									e.printStackTrace();
								}
								break;
							}
						}
					}
				}
			}
		}

	}

	private void makupLogic()
	{
		// ��װ����֮����߼����ù�ϵ
		List<Entity> entities = this.clazzManager.getAllEntities();
		for (Entity entity : entities)
		{
			Class<? extends Entity> clazz = (Class<? extends Entity>) entity.getClass();
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields)
			{
				if ("logicParent".equals(field.getName()))
				{
					ReferenceRules rule = field.getAnnotation(ReferenceRules.class);
					if (rule != null)
					{
						String[] condition = rule.condition().split("=");
						String referField = condition[0];// �����ߵ��ֶ�
						String refedField = condition[1];// �����õ��ֶ�
						String refedClazz = rule.type();// �����õ�clazz

						for (Field referField1 : fields)
						{
							if (referField1.getName().equals(referField))
							{
								for (Entity refedEntity : entities)
								{
									if (!refedEntity.getClass().getName().equals(refedClazz))
									{
										continue;
									}
									Field[] refedFields = refedEntity.getClass().getDeclaredFields();
									for (Field refedField1 : refedFields)
									{
										if (refedField1.getName().equals(refedField))
										{
											try
											{
												refedField1.setAccessible(true);
												Object refedValue = refedField1.get(refedEntity);
												referField1.setAccessible(true);
												Object referValue = referField1.get(entity);
												if (refedValue != null && refedValue.equals(referValue))
												{
													entity.setLogicParent(refedEntity);
													break;
												}
											} catch (IllegalArgumentException e)
											{

												e.printStackTrace();
											} catch (IllegalAccessException e)
											{

												e.printStackTrace();
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * ���������ڵ�
	 * 
	 * @param file
	 * @return </br>
	 */
	private Element getRoot(File file)
	{
		InputStream input = null;
		try
		{
			input = new FileInputStream(file);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setNamespaceAware(true);
			DocumentBuilder domBuidler = factory.newDocumentBuilder();
			Document document = domBuidler.parse(input);
			Element root = document.getDocumentElement();
			return root;
		} catch (FileNotFoundException e)
		{

			e.printStackTrace();
		} catch (ParserConfigurationException e)
		{

			e.printStackTrace();
		} catch (SAXException e)
		{

			e.printStackTrace();
		} catch (IOException e)
		{

			e.printStackTrace();
		} finally
		{
			if (input != null)
				try
				{
					input.close();
				} catch (IOException e)
				{

					e.printStackTrace();
				}
		}

		return null;

	}

	private NodeList runXPath(String xpath)
	{
		XPathExpression xpathExpr = null;
		try
		{
			xpathExpr = XPathFactory.newInstance().newXPath().compile(xpath);
		} catch (XPathExpressionException e)
		{
			e.printStackTrace();
		}
		InputStream input = null;
		NodeList nodes = null;
		try
		{
			input = new FileInputStream(this.targetFile.getAbsoluteFile());
			Object result = xpathExpr.evaluate(new InputSource(input), XPathConstants.NODESET);
			nodes = (NodeList) result;
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (XPathExpressionException e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				if (input != null)
					input.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		return nodes;
	}

	/**
	 * ����ʵ��
	 * 
	 * @param entityNode �����ļ��еĽڵ�
	 * @param parent ���ڵ����
	 * @return
	 * @throws ClassNotFoundException
	 * @throws DOMException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	private void parseEntity(Node entityNode, Entity parent, String xpath) throws DOMException, ClassNotFoundException, InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException
	{
		if (!"Entity".equals(entityNode.getNodeName()))
		{
			return;
		}
		String xpath2 = (parent == null ? "" : parent.getXPath()) + "/" + entityNode.getAttributes().getNamedItem("xmlName").getNodeValue();
		NodeList targetNodes = this.runXPath(xpath2);
		int counter = 1;
		for (int m = 0; targetNodes != null && m < targetNodes.getLength(); m++)
		{
			// Ŀ���ļ��еĽڵ�
			Node targetNode = targetNodes.item(m);
			NamedNodeMap map = entityNode.getAttributes();
			Class<Entity> clsEntity = (Class<Entity>) Class.forName(map.getNamedItem("class").getNodeValue());
			EntityRules er = clsEntity.getAnnotation(EntityRules.class);
			Entity entity = clsEntity.newInstance();
			entity.setXmlName(er.xmlName());
			entity.setXpath(er.xmlName() + "[" + counter + "]");
			counter++;
			xpath = xpath + "/" + er.xmlName();
			entity.setParent(parent);

			Field[] fields = clsEntity.getDeclaredFields();
			// ��������
			for (Field field : fields)
			{
				if (field.getAnnotation(FieldRules.class) != null)
				{
					// ����FieldRulesע��
					FieldRules fr = field.getAnnotation(FieldRules.class);
					if (fr.node())
					{// ���ֶ�Ϊ�ڵ�
						NodeList list = entityNode.getChildNodes();
						for (int i = 0; i < list.getLength(); i++)
						{
							Node attsNode = list.item(i);
							if ("Attributes".equals(attsNode.getNodeName()))
							{
								NodeList attNodes = attsNode.getChildNodes();
								for (int k = 0; k < attNodes.getLength(); k++)
								{
									// ����Attribute
									Node attNode = attNodes.item(k);

									if (attNode.getAttributes() != null && attNode.getAttributes().getNamedItem("xmlName") != null)
									{
										Object value = this.parseField(targetNode, fr);// �ҵ�Ŀ���ļ�������Ӧ�ڵ��ֵ

										if (value != null && attNode.getAttributes() != null && attNode.getAttributes().getNamedItem("fieldType") != null)
										{
											// �ҵ����ԣ�������д��
											String fieldName = field.getName();
											String setMethod = "set" + toMethodStyle(fieldName);
											FieldTypes fieldType = FieldTypes.getFieldType(fr.type());
											Method method = clsEntity.getMethod(setMethod, fieldType.getType());
											method.invoke(entity, FieldTypes.toValue(FieldTypes.getFieldType(fr.type()), value));
										}
									}
								}
								break;
							}
						}
					} else
					{// ���ֶ�ΪxmlԪ������
						NodeList list = entityNode.getChildNodes();
						for (int i = 0; i < list.getLength(); i++)
						{
							Node attsNode = list.item(i);
							if ("Attributes".equals(attsNode.getNodeName()))
							{

								// Attribute�ڵ�
								NodeList attNodes = attsNode.getChildNodes();
								for (int k = 0; k < attNodes.getLength(); k++)
								{
									Node attNode = attNodes.item(k);
									if (attNode.getAttributes() != null && attNode.getAttributes().getNamedItem("xmlName") != null)
									{
										Object value = this.parseField(targetNode, fr);// �ҵ�Ŀ���ļ�������Ӧ�ڵ��ֵ
										if (value != null)
										{
											// �ҵ����ԣ�������д��
											String fieldName = field.getName();
											String setMethod = "set" + toMethodStyle(fieldName);
											FieldTypes fieldType = FieldTypes.getFieldType(fr.type());
											Method method = clsEntity.getMethod(setMethod, fieldType.getType());
											method.invoke(entity, FieldTypes.toValue(FieldTypes.getFieldType(fr.type()), value));
										}
									}
								}
								break;// �˳�Attribute����
							}
						}

					}

				}
			}

			// ע�ᱻ���������Ķ���
			this.clazzManager.register(entity);

			// �����ӽڵ�
			NodeList list = entityNode.getChildNodes();
			for (int i = 0; i < list.getLength(); i++)
			{
				Node child = list.item(i);
				if (child.getNodeType() != Node.ELEMENT_NODE)
					continue;
				if ("Children".equals(child.getNodeName()))
				{
					NodeList children = child.getChildNodes();
					for (int j = 0; j < children.getLength(); j++)
					{
						child = children.item(j);
						if (child.getNodeType() != Node.ELEMENT_NODE)
							continue;
						if ("Entity".equals(child.getNodeName()))
						{
							// �����ڵ��class����ֵ���ӽڵ��class����ֵ���ʱ������������������������԰���������
							String childClazz = child.getAttributes().getNamedItem("class").getNodeValue();
							if (childClazz.equals(entity.getClass().getName()))
							{
								String[] strs = xpath.split("/");
								xpath = xpath + "/" + strs[strs.length - 1];
								// ����Entity
								// this.parseEntity(child, entity, xpath);
								xpath = parent.getXPath() + "/" + entity.getXpath();

								this.parseSelfContained(entityNode, entity, xpath);

							} else
							{
								// ����Entity
								this.parseEntity(child, entity, xpath);
							}

						}
					}
				}
			}
		}

	}

	/**
	 * �����԰���
	 * 
	 * @param node
	 * @param entity
	 * @param xpath 
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	private void parseSelfContained(Node entityNode, Entity parent, String xpath) throws SecurityException, NoSuchMethodException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		if (!"Entity".equals(entityNode.getNodeName()))
		{
			return;
		}

		String xpath2 = parent.getXPath() + "/" + entityNode.getAttributes().getNamedItem("xmlName").getNodeValue();
		NodeList targetNodes = this.runXPath(xpath2);
		int counter = 1;
		for (int m = 0; m < targetNodes.getLength(); m++)
		{
			logger.fine("��⵽���Ұ�����xpath=" + xpath2);
			// Ŀ���ļ��еĽڵ�
			Node targetNode = targetNodes.item(m);
			NamedNodeMap map = entityNode.getAttributes();
			Class<Entity> clsEntity = (Class<Entity>) Class.forName(map.getNamedItem("class").getNodeValue());
			EntityRules er = clsEntity.getAnnotation(EntityRules.class);
			Entity entity = clsEntity.newInstance();
			entity.setXmlName(er.xmlName());
			entity.setXpath(er.xmlName() + "[" + counter + "]");
			counter++;
			entity.setParent(parent);
			// ע�ᱻ���������Ķ���
			this.clazzManager.register(entity);
			Field[] fields = clsEntity.getDeclaredFields();
			// ��������
			for (Field field : fields)
			{
				if (field.getAnnotation(FieldRules.class) != null)
				{
					// ����FieldRulesע��
					FieldRules fr = field.getAnnotation(FieldRules.class);
					if (fr.node())
					{// ���ֶ�Ϊ�ڵ�
						NodeList list = entityNode.getChildNodes();
						for (int i = 0; i < list.getLength(); i++)
						{
							Node attsNode = list.item(i);
							if ("Attributes".equals(attsNode.getNodeName()))
							{
								NodeList attNodes = attsNode.getChildNodes();
								for (int k = 0; k < attNodes.getLength(); k++)
								{
									// Attribute
									Node attNode = attNodes.item(k);
									if (attNode.getAttributes() != null && attNode.getAttributes().getNamedItem("xmlName") != null)
									{
										Object value = this.parseField(targetNode, fr);// �ҵ�Ŀ���ļ�������Ӧ�ڵ��ֵ

										if (value != null && attNode.getAttributes() != null && attNode.getAttributes().getNamedItem("fieldType") != null)
										{
											// �ҵ����ԣ�������д��
											String fieldName = field.getName();
											String setMethod = "set" + toMethodStyle(fieldName);
											FieldTypes fieldType = FieldTypes.getFieldType(fr.type());
											Method method = clsEntity.getMethod(setMethod, fieldType.getType());
											method.invoke(entity, FieldTypes.toValue(FieldTypes.getFieldType(fr.type()), value));
										}
									}
								}
								break;
							}
						}
					} else
					{// ���ֶ�ΪxmlԪ������
						NodeList list = entityNode.getChildNodes();
						for (int i = 0; i < list.getLength(); i++)
						{
							Node attsNode = list.item(i);
							if ("Attributes".equals(attsNode.getNodeName()))
							{
								// Attribute�ڵ�
								NodeList attNodes = attsNode.getChildNodes();
								for (int k = 0; k < attNodes.getLength(); k++)
								{
									Node attNode = attNodes.item(k);
									if (attNode.getAttributes() != null && attNode.getAttributes().getNamedItem("xmlName") != null)
									{
										Object value = this.parseField(targetNode, fr);// �ҵ�Ŀ���ļ�������Ӧ�ڵ��ֵ
										if (value != null)
										{
											// �ҵ����ԣ�������д��
											String fieldName = field.getName();
											String setMethod = "set" + toMethodStyle(fieldName);
											FieldTypes fieldType = FieldTypes.getFieldType(fr.type());
											Method method = clsEntity.getMethod(setMethod, fieldType.getType());
											method.invoke(entity, FieldTypes.toValue(FieldTypes.getFieldType(fr.type()), value));
										}
									}
								}
								break;// �˳�Attribute����
							}
						}
					}
				}
			}

			if (targetNode.getChildNodes().getLength() != 0)
			{
				NodeList children = targetNode.getChildNodes();
				for (int i = 0; i < children.getLength(); i++)
				{
					Node child = children.item(i);
					if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals(targetNode.getNodeName()))
					{
						// ������Ҫͨ�����Ұ����������ӽڵ�
						this.parseSelfContained(entityNode, entity, xpath2);
					}
				}
			}
		}

	}

	/**
	 * ��ȡ������������ֵ
	 * 
	 * @param node
	 * @return
	 */
	private Object parseField(Node targetNode, FieldRules fr)
	{
		NodeList nodes = targetNode.getChildNodes();

		for (int i = 0; i < nodes.getLength(); i++)
		{
			Node node = nodes.item(i);
			if (fr.textNode())
			{
				return node.getNodeValue().trim();
			}
			if (node.getNodeType() != Node.ELEMENT_NODE)
				continue;
			if (fr.node())
			{// ����Ϊ�ڵ�
				if (!fr.textNode() && fr.xmlName().equals(node.getNodeName()))
				{
					NodeList list = node.getChildNodes();
					for (int k = 0; k < list.getLength(); k++)
					{
						Node textNode = list.item(k);
						if (textNode.getNodeType() == Node.TEXT_NODE)
						{
							return textNode.getNodeValue().trim();
						}
					}
				} else if (fr.textNode())
				{
					NodeList list = node.getChildNodes();
					for (int k = 0; k < list.getLength(); k++)
					{
						Node textNode = list.item(k);
						if (textNode.getNodeType() == Node.TEXT_NODE)
						{
							return textNode.getNodeValue().trim();
						}
					}
				}
			}
		}
		if (!fr.node() && targetNode.getAttributes() != null)
		{ // xml�еĽڵ�����
			if (targetNode.getAttributes().getNamedItem(fr.xmlName()) != null)
				return targetNode.getAttributes().getNamedItem(fr.xmlName()).getNodeValue();
		}
		return null;
	}

	/**
	 * ��name��ɵ�һ����ĸ��д
	 * 
	 * @param name
	 * @return
	 */
	private String toMethodStyle(String name)
	{
		String str = name.substring(0, 1);
		str = str.toUpperCase();
		name = name.substring(1);
		return str + name;
	}

}