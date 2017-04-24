/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template.build;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.coretek.common.logging.LoggingPlugin;
import com.coretek.common.template.build.file.Attribute;
import com.coretek.common.template.build.file.FieldTypes;
import com.coretek.common.template.build.file.JavaFile;
import com.coretek.common.template.build.file.Reference;
import com.coretek.common.template.parser.IParser;

/**
 * 根据XML文件生成Java代码
 * 
 * @author 孙大巍 2011-12-23
 */
public class JavaCodeGenerator implements IParser
{
	private static final Logger	logger	= LoggingPlugin.getLogger(JavaCodeGenerator.class.getName());

	private String				path;																	// 代码生成的路径

	public JavaCodeGenerator(String path)
	{
		this.path = path;
	}

	/**
	 * @return the path <br/>
	 * 
	 */
	public String getPath()
	{
		return path;
	}

	/**
	 * @param path the path to set <br/>
	 * 
	 */
	public void setPath(String path)
	{
		this.path = path;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coretek.common.template.parser.IParser#parse(java.io.File) <br/>
	 */
	@Override
	public Object parse(File file)
	{
		if (file == null || !file.exists())
		{
			logger.warning("The file does not exis! Application exit.");
			throw new IllegalArgumentException("The file does not exist!");
		}
		logger.config("Path:" + file.getAbsolutePath());
		try
		{
			InputStream input = new FileInputStream(file);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setNamespaceAware(true);
			try
			{
				DocumentBuilder domBuidler = factory.newDocumentBuilder();
				Document document = domBuidler.parse(input);
				Element root = document.getDocumentElement();
				NodeList children = root.getChildNodes();
				for (int i = 0; i < children.getLength(); i++)
				{
					Node child = children.item(i);
					if (child.getNodeType() != Node.ELEMENT_NODE)
					{
						continue;
					}
					// Entities节点
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
							// Entity节点
							if ("Entity".equals(node.getNodeName()))
							{
								JavaFile jf = this.parseEntity(node);
								jf.toFile();
							}
						}
					}
				}

			} catch (SAXException e)
			{
				e.printStackTrace();
				return null;
			} catch (ParserConfigurationException e)
			{
				e.printStackTrace();
				return null;
			} catch (IOException e)
			{
				e.printStackTrace();
				return null;
			} finally
			{
				if (input != null)
				{
					try
					{
						input.close();
					} catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		} catch (FileNotFoundException e)
		{
			logger.severe("Throws FileNotFoundException.");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析实体
	 * 
	 * @param entityNode
	 * @return </br>
	 */
	private JavaFile parseEntity(Node entityNode)
	{
		if (!"Entity".equals(entityNode.getNodeName()))
		{
			return null;
		}
		JavaFile jf = new JavaFile(this.path);
		NamedNodeMap map = entityNode.getAttributes();
		String clazz = map.getNamedItem("class").getNodeValue();
		// 检测自我包含
		if (entityNode.getParentNode() != null)
		{
			Node grandPa = entityNode.getParentNode();
			grandPa = grandPa.getParentNode();
			if (grandPa != null && grandPa.getAttributes().getNamedItem("class") != null)
			{
				String gClazz = grandPa.getAttributes().getNamedItem("class").getNodeValue();
				if (gClazz.equals(clazz))
				{
					logger.config("检测到自我包含。本次无需执行解析实体操作.class=" + clazz);
					return null;
				}
			}

		}
		String[] strs = clazz.split("\\.");
		String str = "";
		for (int i = 0; i < strs.length; i++)
		{
			if (i == strs.length - 1)
				break;
			str = str + strs[i];
		}
		jf.setFileName(strs[strs.length - 1]);
		jf.setPackageInfo(clazz.replaceAll("\\." + jf.getFileName(), ""));
		String xmlName = map.getNamedItem("xmlName").getNodeValue();
		jf.setXmlName(xmlName);
		// 在消息视图上显示的字段
		if (map.getNamedItem("displayField") != null)
		{
			jf.setDisplayField(map.getNamedItem("displayField").getNodeValue());
		}
		// 获取父类
		if (map.getNamedItem("superClazz") != null)
		{
			jf.setSuperClazz(map.getNamedItem("superClazz").getNodeValue());
		}
		if (map.getNamedItem("dragable") != null)
			jf.setDragable(Boolean.valueOf(map.getNamedItem("dragable").getNodeValue()));
		NodeList children = entityNode.getChildNodes();
		for (int i = 0; i < children.getLength(); i++)
		{
			Node child = children.item(i);
			if ("Attributes".equals(child.getNodeName()))
				jf.setFields(this.parseFields(child));
			else if ("Children".equals(child.getNodeName()))
			{
				NodeList nodes = child.getChildNodes();
				for (int j = 0; j < nodes.getLength(); j++)
				{
					Node node = nodes.item(j);
					if (node.getNodeType() != Node.ELEMENT_NODE)
					{
						continue;
					}
					if ("Entity".equals(node.getNodeName()))
					{
						JavaFile javaFile = this.parseEntity(node);
						if (javaFile != null)
						{
							javaFile.toFile();
						}
					}
				}

			} else if ("References".equals(child.getNodeName()))
			{
				NodeList list = child.getChildNodes();
				for (int k = 0; k < list.getLength(); k++)
				{
					Node refNode = list.item(k);
					if (refNode.getNodeType() != Node.ELEMENT_NODE)
						continue;
					if ("Reference".equals(refNode.getNodeName()))
						this.parseReference(refNode, jf);
				}
			}
		}

		return jf;
	}

	/**
	 * 解析Reference
	 * 
	 * @param node
	 */
	private void parseReference(Node node, JavaFile jf)
	{
		Reference ref = new Reference();

		ref.setCondition(node.getAttributes().getNamedItem("condition").getNodeValue());
		ref.setName(node.getAttributes().getNamedItem("name").getNodeValue());
		ref.setType(node.getAttributes().getNamedItem("type").getNodeValue());
		jf.addReference(ref);
	}

	/**
	 * 解析属性列表
	 * 
	 * @param node
	 * @return </br>
	 */
	private Set<Attribute> parseFields(Node node)
	{

		Set<Attribute> fields = new HashSet<Attribute>();
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++)
		{
			Node child = children.item(i);
			if (child.getNodeType() != Node.ELEMENT_NODE)
			{
				continue;
			}
			fields.add(this.parseField(child));
		}
		return fields;
	}

	/**
	 * 解析属性
	 * 
	 * @param node
	 * @return </br>
	 */
	private Attribute parseField(Node node)
	{

		Attribute field = new Attribute();
		if ("Attribute".equals(node.getNodeName()))
		{
			field.setXmlName(node.getAttributes().getNamedItem("xmlName").getNodeValue());
			field.setName(node.getAttributes().getNamedItem("name").getNodeValue());
			if (node.getAttributes().getNamedItem("fieldType") == null)
			{
				field.setType(FieldTypes.STRING);
			} else
				field.setType(FieldTypes.getFieldType(node.getAttributes().getNamedItem("fieldType").getNodeValue()));
			if (node.getAttributes().getNamedItem("display") != null)
			{
				String value = node.getAttributes().getNamedItem("display").getNodeValue();
				if ("true".equals(value))
				{
					field.setDisplay(true);
				}
			}
			if (node.getAttributes().getNamedItem("editable") != null)
			{
				String value = node.getAttributes().getNamedItem("editable").getNodeValue();
				if ("true".equals(value))
				{
					field.setEditable(true);
				}
			}

			String value = node.getAttributes().getNamedItem("type").getNodeValue();
			if ("node".equals(value))
			{
				field.setNode(true);
			} else if ("text_node".equals(value))
			{
				field.setNode(true);
				field.setTextNode(true);
			}
		}
		return field;
	}

}
