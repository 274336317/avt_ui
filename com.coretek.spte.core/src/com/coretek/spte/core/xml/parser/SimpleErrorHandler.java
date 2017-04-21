package com.coretek.spte.core.xml.parser;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * 
 * 解析xml时的错误处理器
 * 
 * @author 孙大巍
 * @date 2010-12-17
 */
public class SimpleErrorHandler implements ErrorHandler
{

	public void error(SAXParseException exception) throws SAXException
	{
		exception.printStackTrace();
	}

	public void fatalError(SAXParseException exception) throws SAXException
	{
		exception.printStackTrace();
	}

	public void warning(SAXParseException exception) throws SAXException
	{
		exception.printStackTrace();
	}
}