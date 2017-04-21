package com.coretek.spte.core.xml.parser;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import org.eclipse.core.resources.IFile;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * 解析xml文件，并利用schema文件进行验证
 * 
 * @author 孙大巍
 * @date 2010-12-17
 */

public abstract class XMLParserWithSchemaValidation
{

	protected File					schemaFile;

	protected IFile					xmlFile;

	protected PropertyChangeSupport	listeners	= new PropertyChangeSupport(this);

	public void setSchemaFile(File schemaFile)
	{
		this.schemaFile = schemaFile;
	}

	public void setXmlFile(IFile xmlFile)
	{
		this.xmlFile = xmlFile;
	}

	public File getSchemaFile()
	{
		return schemaFile;
	}

	public IFile getXmlFile()
	{
		return xmlFile;
	}

	/**
	 * 初始化解析器，读入schema文件和xml文件，并对xml文件进行验证
	 * 
	 * @param schemaFile schema文件
	 * @param xmlFile 需要被验证的xmlFile
	 * @return document
	 */
	public Document init()
	{
		InputStream input = null;
		InputStream schemaInput = null;
		Document document = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(true);
		SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		try
		{
			if (xmlFile.getLocation().toFile().length() == 0)
			{
				return null;
			}
			input = new FileInputStream(xmlFile.getLocation().toFile());
			schemaInput = new FileInputStream(this.schemaFile);
			factory.setSchema(schemaFactory.newSchema(new Source[] { new StreamSource(schemaInput) }));
			DocumentBuilder domBuidler = factory.newDocumentBuilder();
			domBuidler.setErrorHandler(new SimpleErrorHandler());
			document = domBuidler.parse(input);
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
				} finally
				{
					try
					{
						if (schemaInput != null)
							schemaInput.close();
					} catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		return document;
	}

	/**
	 * 解析xml文件，生成所需的对象
	 * 
	 * @param input
	 * @return
	 */
	public abstract Object doParse();

	/**
	 * 添加监听器
	 * 
	 * @param listener
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener)
	{
		this.listeners.addPropertyChangeListener(listener);
	}

	/**
	 * 删除监听器
	 * 
	 * @param listener
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener)
	{
		this.listeners.removePropertyChangeListener(listener);
	}

	/**
	 * 发送属性改变事件
	 * 
	 * @param prop
	 * @param newValue
	 */
	public void firePropertyChange(String prop, Object newValue)
	{
		this.listeners.firePropertyChange(prop, null, newValue);
	}
}