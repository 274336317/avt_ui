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
 * ����xml�ļ���������schema�ļ�������֤
 * 
 * @author ���Ρ
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
	 * ��ʼ��������������schema�ļ���xml�ļ�������xml�ļ�������֤
	 * 
	 * @param schemaFile schema�ļ�
	 * @param xmlFile ��Ҫ����֤��xmlFile
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
	 * ����xml�ļ�����������Ķ���
	 * 
	 * @param input
	 * @return
	 */
	public abstract Object doParse();

	/**
	 * ��Ӽ�����
	 * 
	 * @param listener
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener)
	{
		this.listeners.addPropertyChangeListener(listener);
	}

	/**
	 * ɾ��������
	 * 
	 * @param listener
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener)
	{
		this.listeners.removePropertyChangeListener(listener);
	}

	/**
	 * �������Ըı��¼�
	 * 
	 * @param prop
	 * @param newValue
	 */
	public void firePropertyChange(String prop, Object newValue)
	{
		this.listeners.firePropertyChange(prop, null, newValue);
	}
}