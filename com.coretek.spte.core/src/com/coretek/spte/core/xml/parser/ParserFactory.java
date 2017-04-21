package com.coretek.spte.core.xml.parser;

import java.io.File;

import org.eclipse.core.resources.IFile;

/**
 * ��ȡ���ֽ�������ʵ��
 * 
 * @author ���Ρ
 * @date 2011-1-11
 */
public class ParserFactory
{

	private static ImportedTestCaseParser	importedParser;

	public static ImportedTestCaseParser getImportedParser(File schemaFile, IFile file)
	{
		if (importedParser == null)
		{
			importedParser = ImportedTestCaseParser.getInstance();
		}
		importedParser.setSchemaFile(schemaFile);
		importedParser.setXmlFile(file);
		return importedParser;
	}
}