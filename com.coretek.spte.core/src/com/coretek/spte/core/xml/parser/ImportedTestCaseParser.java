package com.coretek.spte.core.xml.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.coretek.spte.core.util.RefCaseBean;

/**
 * 解析导入的测试用例文件，只解析出消息和引用的测试用例名字
 * 
 * @author 孙大巍
 * @date 2011-1-11
 */
public class ImportedTestCaseParser extends XMLParserWithSchemaValidation
{

	private static ImportedTestCaseParser	parser;

	/**
	 * 被解析的.cas文件
	 */
	private IFile							file;

	public static ImportedTestCaseParser getInstance(File schemaFile, IFile file)
	{
		if (parser == null)
		{
			parser = new ImportedTestCaseParser();
		}
		parser.setSchemaFile(schemaFile);
		parser.setXmlFile(file);
		parser.file = file;
		return parser;
	}

	public static ImportedTestCaseParser getInstance()
	{
		if (parser == null)
		{
			parser = new ImportedTestCaseParser();
		}
		return parser;
	}

	@Override
	public Object doParse()
	{
		assert this.xmlFile != null;
		assert this.xmlFile.exists();
		if (this.xmlFile.getLocation().toFile().length() == 0)
		{
			return null;
		}

		List<Object> objects = new ArrayList<Object>();
		InputStream input = null;
		Document doc = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try
		{
			input = new FileInputStream(this.xmlFile.getLocation().toFile());
			DocumentBuilder domBuilder = factory.newDocumentBuilder();
			doc = domBuilder.parse(input);
		} catch (ParserConfigurationException e)
		{
			e.printStackTrace();
			return null;
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return null;
		} catch (SAXException e)
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
		Element root = doc.getDocumentElement();
		NodeList children = root.getChildNodes();

		for (int i = 0; i < children.getLength(); i++)
		{
			Node node = children.item(i);
			if (children.item(i).getNodeType() != Node.ELEMENT_NODE)
			{
				continue;
			}
			NamedNodeMap nnm = node.getAttributes();
			if (node.getNodeName().equals("refCase"))
			{
				String ref = nnm.getNamedItem("ref").getNodeValue();
				String strs[] = ref.split("/");
				RefCaseBean rc = new RefCaseBean();
				rc.setProjectName(strs[0]);
				rc.setUnitName(strs[1]);
				rc.setCaseName(strs[2]);
				objects.add(rc);

			} else if (node.getNodeName().equals("message"))
			{
				String messageName = nnm.getNamedItem("name").getNodeValue();
				objects.add(messageName);
			}
		}
		return objects;
	}
}