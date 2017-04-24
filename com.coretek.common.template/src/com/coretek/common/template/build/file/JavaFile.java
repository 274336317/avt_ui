/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template.build.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.coretek.common.logging.LoggingPlugin;

/**
 * JavaԴ�����ļ�
 * 
 * @author ���Ρ 2011-12-23
 */
public class JavaFile
{

	private final static Logger	logger;

	static
	{
		logger = LoggingPlugin.getLogger(JavaFile.class);
		logger.setLevel(Level.CONFIG);
	}

	private Set<Attribute>		fields			= new HashSet<Attribute>();

	private String				packageInfo;																// ����Ϣ

	private String				fileName;																	// �ļ���

	private String				destnation;																	// �ļ����ɵ�Ŀ�ĵ�

	private String				xmlName;																	// xml�еĽڵ�����

	private String				displayField	= "";														// ��ʾ�ֶ�

	private boolean				dragable;																	// �Ƿ������ק

	private String				superClazz		= "com.coretek.common.template.build.codeTemplate.Entity";	// ����

	private List<Reference>		references		= new ArrayList<Reference>();								// Ӧ�ö���

	/**
	 * ��ȡ����
	 * 
	 * @return the superClazz <br/>
	 *         <b>����</b> ���Ρ </br> <b>����</b> 2011-12-29
	 */
	public String getSuperClazz()
	{
		return superClazz;
	}

	/**
	 * ���ó���
	 * 
	 * @param superClazz the superClazz to set <br/>
	 *            <b>����</b> ���Ρ </br> <b>����</b> 2011-12-29
	 */
	public void setSuperClazz(String superClazz)
	{
		if (superClazz != null && superClazz.trim().length() > 0)
			this.superClazz = superClazz;
	}

	/**
	 * �Ƿ�֧����ק
	 * 
	 * @return the dragable <br/>
	 *         <b>����</b> ���Ρ </br> <b>����</b> 2011-12-27
	 */
	public boolean isDragable()
	{
		return dragable;
	}

	/**
	 * @param dragable the dragable to set <br/>
	 *            <b>����</b> ���Ρ </br> <b>����</b> 2011-12-27
	 */
	public void setDragable(boolean dragable)
	{
		this.dragable = dragable;
	}

	/**
	 * ��ȡ��ʾ�ֶ�
	 * 
	 * @return the displayField <br/>
	 *         <b>����</b> ���Ρ </br> <b>����</b> 2011-12-27
	 */
	public String getDisplayField()
	{
		return displayField;
	}

	/**
	 * @param displayField the displayField to set <br/>
	 *            <b>����</b> ���Ρ </br> <b>����</b> 2011-12-27
	 */
	public void setDisplayField(String displayField)
	{
		this.displayField = displayField;
	}

	/**
	 * </br> <b>����</b> ���Ρ </br> <b>����</b> 2011-12-23
	 */
	public JavaFile(String destnation)
	{
		this.destnation = destnation;
	}

	/**
	 * �������
	 * 
	 * @param reference </br> <b>����</b> ���Ρ </br> <b>����</b> 2011-12-25
	 */
	public void addReference(Reference reference)
	{
		this.references.add(reference);
	}

	/**
	 * @return the references <br/>
	 *         <b>����</b> ���Ρ </br> <b>����</b> 2011-12-25
	 */
	public List<Reference> getReferences()
	{
		return references;
	}

	/**
	 * @param references the references to set <br/>
	 *            <b>����</b> ���Ρ </br> <b>����</b> 2011-12-25
	 */
	public void setReferences(List<Reference> references)
	{
		this.references = references;
	}

	/**
	 * @return the xmlName <br/>
	 *         <b>����</b> ���Ρ </br> <b>����</b> 2011-12-23
	 */
	public String getXmlName()
	{
		return xmlName;
	}

	/**
	 * @param xmlName the xmlName to set <br/>
	 *            <b>����</b> ���Ρ </br> <b>����</b> 2011-12-23
	 */
	public void setXmlName(String xmlName)
	{
		this.xmlName = xmlName;
	}

	/**
	 * @return the packageInfo <br/>
	 *         <b>����</b> ���Ρ </br> <b>����</b> 2011-12-23
	 */
	public String getPackageInfo()
	{
		return packageInfo;
	}

	/**
	 * @param packageInfo the packageInfo to set <br/>
	 *            <b>����</b> ���Ρ </br> <b>����</b> 2011-12-23
	 */
	public void setPackageInfo(String packageInfo)
	{
		this.packageInfo = packageInfo;
	}

	/**
	 * @return the fileName <br/>
	 *         <b>����</b> ���Ρ </br> <b>����</b> 2011-12-23
	 */
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * @param fileName the fileName to set <br/>
	 *            <b>����</b> ���Ρ </br> <b>����</b> 2011-12-23
	 */
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	/**
	 * @return the destnation <br/>
	 *         <b>����</b> ���Ρ </br> <b>����</b> 2011-12-23
	 */
	public String getDestnation()
	{
		return destnation;
	}

	/**
	 * @param destnation the destnation to set <br/>
	 *            <b>����</b> ���Ρ </br> <b>����</b> 2011-12-23
	 */
	public void setDestnation(String destnation)
	{
		this.destnation = destnation;
	}

	/**
	 * @return the fields <br/>
	 *         <b>����</b> ���Ρ </br> <b>����</b> 2011-12-23
	 */

	public Set<Attribute> getFields()
	{
		return fields;
	}

	/**
	 * @param fields the fields to set <br/>
	 *            <b>����</b> ���Ρ </br> <b>����</b> 2011-12-23
	 */
	public void setFields(Set<Attribute> fields)
	{
		this.fields = fields;
	}

	/**
	 * �������
	 * 
	 * @param field </br> <b>����</b> ���Ρ </br> <b>����</b> 2011-12-23
	 */
	public void addField(Attribute field)
	{
		this.fields.add(field);
	}

	/**
	 * ����Java�ļ�
	 * 
	 * @return </br> <b>����</b> ���Ρ </br> <b>����</b> 2011-12-23
	 */
	public boolean toFile()
	{
		File file = new File(this.destnation);
		if (!file.exists())
		{
			file.mkdirs();
		}
		String folders[] = packageInfo.split("\\.");
		StringBuilder path = new StringBuilder(file.getAbsolutePath());
		path = path.append("/src");
		for (int i = 0; i < folders.length; i++)
		{
			path = path.append(File.separator).append(folders[i]);
		}
		File packageFile = new File(path.toString());
		if (!packageFile.exists())
		{
			if (!packageFile.mkdirs())
			{
				logger.warning("����" + packageFile.getAbsolutePath() + "ʧ�ܣ�");
				return false;
			}
		}
		path = path.append("/").append(this.fileName).append(".java");
		File javaFile = new File(path.toString());
		try
		{
			if (javaFile.exists())
			{
				if (!javaFile.delete())
				{

					return false;
				}
			}
			javaFile.createNewFile();
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}
		try
		{
			FileOutputStream fos = new FileOutputStream(javaFile);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			PrintWriter writer = new PrintWriter(osw);

			writer.println("/************************************************************************");
			writer.println(" *				�����������ɼ������޹�˾ ��Ȩ����");
			writer.println(" *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.");
			writer.println(" ***********************************************************************/");
			writer.println();
			writer.println("package " + this.packageInfo + ";");
			writer.println();
			writer.println("import java.util.List;");
			writer.println("import com.coretek.common.template.build.codeTemplate.Entity;");
			writer.println("import com.coretek.common.template.build.codeTemplate.EntityRules;");
			writer.println("import com.coretek.common.template.build.codeTemplate.FieldRules;");
			writer.println("import com.coretek.common.template.build.codeTemplate.ReferenceRules;");
			writer.println();
			writer.println("/**");
			writer.println("* �����Ӧ��xml�ϵ� \"" + this.xmlName + "\" �ڵ�.");
			writer.println("* ���������רҵ��ʿ���벻Ҫ�޸Ĵ��ļ�.");
			writer.println("* @author GENERATED BY SPTE_CODE_ROBOT");
			writer.println("*/");
			writer.println("@EntityRules(xpath=\"" + this.xmlName + "\", xmlName=\"" + this.xmlName + "\", displayField=\"" + this.displayField + "\", dragable=" + this.dragable + ", superClazz=\"" + this.superClazz + "\")");
			writer.println("public class " + this.fileName + " extends " + this.superClazz + " { ");
			for (Attribute field : this.fields)
			{
				writer.println("    //�����Զ�Ӧ��\"" + field.getXmlName() + "\"");
				writer.println("    @FieldRules(xmlName=\"" + field.getXmlName() + "\", display=" + field.isDisplay() + ", type=\"" + field.getType().getCode() + "\", node=" + field.isNode() + ", editable=" + field.isEditable() + ",textNode=" + field.isTextNode() + ")");
				writer.println("    private " + field.getType().getCode() + " " + field.getName() + ";");
			}
			for (Reference ref : this.references)
			{
				writer.println("    //��������");
				writer.println("    @ReferenceRules(condition=\"" + ref.getCondition() + "\", type=\"" + ref.getType() + "\")");
				writer.println("    private " + ref.getType() + " " + ref.getName() + ";");
			}
			writer.println();
			// ��ӡget/set
			for (Attribute field : this.fields)
			{
				// get
				writer.println("    public " + field.getType().getCode() + " get" + this.methodStyle(field.getName()) + "(){");
				writer.println("        return this." + field.getName() + ";");
				writer.println("    }");
				// set
				writer.println("    public void set" + this.methodStyle(field.getName()) + "(" + field.getType().getCode() + " " + field.getName() + "){");
				writer.println("        this." + field.getName() + " = " + field.getName() + ";");
				writer.println("    }");
			}
			for (Reference ref : this.references)
			{
				// get
				writer.println("    public " + ref.getType() + " get" + this.methodStyle(ref.getName()) + "(){");
				writer.println("        return this." + ref.getName() + ";");
				writer.println("    }");
				// set
				writer.println("    public void set" + this.methodStyle(ref.getName()) + "(" + ref.getType() + " " + ref.getName() + "){");
				if ("logicParent".equalsIgnoreCase(ref.getName()))
					writer.println("        super.setLogicParent(logicParent);");
				writer.println("        this." + ref.getName() + " = " + ref.getName() + ";");
				writer.println("    }");
			}
			writer.println("}");
			writer.flush();
			writer.close();
			osw.close();
			fos.close();
		} catch (FileNotFoundException e)
		{

			e.printStackTrace();
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{

			e.printStackTrace();
		}
		return false;
	}

	/**
	 * ��name��ɵ�һ����ĸ��д
	 * 
	 * @param name
	 * @return </br> <b>����</b> ���Ρ </br> <b>����</b> 2011-12-23
	 */
	private String methodStyle(String name)
	{
		String str = name.substring(0, 1);
		str = str.toUpperCase();
		name = name.substring(1);
		return str + name;
	}
}
