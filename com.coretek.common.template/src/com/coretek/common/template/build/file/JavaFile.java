/************************************************************************
 *				北京科银京成技术有限公司 版权所有
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
 * Java源代码文件
 * 
 * @author 孙大巍 2011-12-23
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

	private String				packageInfo;																// 包信息

	private String				fileName;																	// 文件名

	private String				destnation;																	// 文件生成的目的地

	private String				xmlName;																	// xml中的节点名字

	private String				displayField	= "";														// 显示字段

	private boolean				dragable;																	// 是否可以拖拽

	private String				superClazz		= "com.coretek.common.template.build.codeTemplate.Entity";	// 父类

	private List<Reference>		references		= new ArrayList<Reference>();								// 应用对象

	/**
	 * 获取父类
	 * 
	 * @return the superClazz <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-29
	 */
	public String getSuperClazz()
	{
		return superClazz;
	}

	/**
	 * 设置超类
	 * 
	 * @param superClazz the superClazz to set <br/>
	 *            <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-29
	 */
	public void setSuperClazz(String superClazz)
	{
		if (superClazz != null && superClazz.trim().length() > 0)
			this.superClazz = superClazz;
	}

	/**
	 * 是否支持拖拽
	 * 
	 * @return the dragable <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-27
	 */
	public boolean isDragable()
	{
		return dragable;
	}

	/**
	 * @param dragable the dragable to set <br/>
	 *            <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-27
	 */
	public void setDragable(boolean dragable)
	{
		this.dragable = dragable;
	}

	/**
	 * 获取显示字段
	 * 
	 * @return the displayField <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-27
	 */
	public String getDisplayField()
	{
		return displayField;
	}

	/**
	 * @param displayField the displayField to set <br/>
	 *            <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-27
	 */
	public void setDisplayField(String displayField)
	{
		this.displayField = displayField;
	}

	/**
	 * </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-23
	 */
	public JavaFile(String destnation)
	{
		this.destnation = destnation;
	}

	/**
	 * 添加引用
	 * 
	 * @param reference </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-25
	 */
	public void addReference(Reference reference)
	{
		this.references.add(reference);
	}

	/**
	 * @return the references <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-25
	 */
	public List<Reference> getReferences()
	{
		return references;
	}

	/**
	 * @param references the references to set <br/>
	 *            <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-25
	 */
	public void setReferences(List<Reference> references)
	{
		this.references = references;
	}

	/**
	 * @return the xmlName <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-23
	 */
	public String getXmlName()
	{
		return xmlName;
	}

	/**
	 * @param xmlName the xmlName to set <br/>
	 *            <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-23
	 */
	public void setXmlName(String xmlName)
	{
		this.xmlName = xmlName;
	}

	/**
	 * @return the packageInfo <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-23
	 */
	public String getPackageInfo()
	{
		return packageInfo;
	}

	/**
	 * @param packageInfo the packageInfo to set <br/>
	 *            <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-23
	 */
	public void setPackageInfo(String packageInfo)
	{
		this.packageInfo = packageInfo;
	}

	/**
	 * @return the fileName <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-23
	 */
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * @param fileName the fileName to set <br/>
	 *            <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-23
	 */
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	/**
	 * @return the destnation <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-23
	 */
	public String getDestnation()
	{
		return destnation;
	}

	/**
	 * @param destnation the destnation to set <br/>
	 *            <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-23
	 */
	public void setDestnation(String destnation)
	{
		this.destnation = destnation;
	}

	/**
	 * @return the fields <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-23
	 */

	public Set<Attribute> getFields()
	{
		return fields;
	}

	/**
	 * @param fields the fields to set <br/>
	 *            <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-23
	 */
	public void setFields(Set<Attribute> fields)
	{
		this.fields = fields;
	}

	/**
	 * 添加属性
	 * 
	 * @param field </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-23
	 */
	public void addField(Attribute field)
	{
		this.fields.add(field);
	}

	/**
	 * 生成Java文件
	 * 
	 * @return </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-23
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
				logger.warning("生成" + packageFile.getAbsolutePath() + "失败！");
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
			writer.println(" *				北京科银京成技术有限公司 版权所有");
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
			writer.println("* 此类对应于xml上的 \"" + this.xmlName + "\" 节点.");
			writer.println("* 如果您不是专业人士，请不要修改此文件.");
			writer.println("* @author GENERATED BY SPTE_CODE_ROBOT");
			writer.println("*/");
			writer.println("@EntityRules(xpath=\"" + this.xmlName + "\", xmlName=\"" + this.xmlName + "\", displayField=\"" + this.displayField + "\", dragable=" + this.dragable + ", superClazz=\"" + this.superClazz + "\")");
			writer.println("public class " + this.fileName + " extends " + this.superClazz + " { ");
			for (Attribute field : this.fields)
			{
				writer.println("    //此属性对应于\"" + field.getXmlName() + "\"");
				writer.println("    @FieldRules(xmlName=\"" + field.getXmlName() + "\", display=" + field.isDisplay() + ", type=\"" + field.getType().getCode() + "\", node=" + field.isNode() + ", editable=" + field.isEditable() + ",textNode=" + field.isTextNode() + ")");
				writer.println("    private " + field.getType().getCode() + " " + field.getName() + ";");
			}
			for (Reference ref : this.references)
			{
				writer.println("    //引用属性");
				writer.println("    @ReferenceRules(condition=\"" + ref.getCondition() + "\", type=\"" + ref.getType() + "\")");
				writer.println("    private " + ref.getType() + " " + ref.getName() + ";");
			}
			writer.println();
			// 打印get/set
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
	 * 将name变成第一个字母大写
	 * 
	 * @param name
	 * @return </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-12-23
	 */
	private String methodStyle(String name)
	{
		String str = name.substring(0, 1);
		str = str.toUpperCase();
		name = name.substring(1);
		return str + name;
	}
}
