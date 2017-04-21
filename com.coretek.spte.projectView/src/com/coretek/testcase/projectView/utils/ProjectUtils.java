package com.coretek.testcase.projectView.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.XMLMemento;
import org.w3c.dom.Document;

import com.coretek.common.utils.EclipseUtils;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.core.util.Utils;
import com.coretek.testcase.projectView.views.ProjectView;

public class ProjectUtils
{

	/*
	 * property节点属性id类别：目录类型
	 */
	public static final String	FODLDER_TYPE						= "folderType";

	public static final String	FODLDER_TYPE_TEST_SUITE				= "testcase";

	/*
	 * project节点属性id类别：项目类型. 扩展之用
	 */
	public static final String	PROPERTY_ATTR_ID_PROJECT_TYPE		= "projectType";

	public static final String	PROPERTY_ATTR_VALUE_PROJECT_TYPE	= "sequenceDiagramProject";

	/*
	 * xml(cas)文件后缀
	 */
	public static final String	XML_CAS_FILE_EXTENSION				= "cas";

	/*
	 * 用于管理目录属性的文件，测试用例管理目录下每个目录均由该文件记录该目录的属性。
	 */
	public static final String	FOLDER_PROPERTY						= ".folderProperty";

	/**
	 * 需求编号
	 */
	public static final String	CASE_REQ_ID							= "requireNum";
	/**
	 * 结果准则regulation
	 */
	public static final String	CASE_REG							= "regulation";

	/**
	 *测试规程
	 */
	public static final String	CASE_PRO							= "procedure";

	/**
	 * 测试用例描述
	 */
	public static final String	CASE_DES							= "des";

	/**
	 * 假设和约束
	 */
	public static final String	CASE_CON							= "constrain";

	/**
	 * 条件
	 */
	public static final String	CASE_CONDITION						= "condition";

	/**
	 * 属性文件根节点名称
	 */
	private static final String	ROOT_NODE_NAME						= "properties";

	/*
	 * 属性文件一级节点名称
	 */
	private static final String	L1_NODE_NAME						= "property";

	/*
	 * property节点属性：id
	 */
	private static final String	PROPERTY_ATTR_ID					= "id";

	/*
	 * property节点属性：value
	 */
	private static final String	PROPERTY_ATTR_VALUE					= "value";

	/**
	 * 在项目目录中创建新的文件夹
	 * 
	 * @param newProject 新的项目引用
	 * @param dirName 新创建的目录名称
	 * @return
	 * @throws CoreException
	 */
	public static IPath createFolder(IProject newProject, String dirName) throws CoreException
	{
		// Create or get the handle for the directory
		IFolder folder = newProject.getFolder(dirName);
		if (!folder.exists())
		{
			try
			{
				File suiteFolder = new File(folder.getLocation().toOSString());
				suiteFolder.mkdir();

			} catch (Exception e)
			{
				// RuntimePlugin.logException(e);
				e.printStackTrace();
			}
		}
		return folder.getFullPath();
	}

	/**
	 * 获取测试 用例的描述信息(0.需求编号； 1.结果准则；2.用例描述；3.测试规程 4.先决条件；5.假设和约束)
	 * 
	 * @param file 测试用例
	 * @return
	 */
	public static String[] getCaseDes(IFile file)
	{
		String[] caseInfo = new String[] { " ", " ", " ", " ", " ", " " };
		FileReader reader = null;
		if (file.exists())
		{
			File fl = file.getLocation().toFile();
			try
			{
				reader = new FileReader(fl);
				XMLMemento oldPropertiesRoot = null;
				oldPropertiesRoot = XMLMemento.createReadRoot(reader);
				if ("testCase".equals(oldPropertiesRoot.getType()))
				{
					String str = "";
					for (int i = 0; i < caseInfo.length; i++)
					{
						switch (i)
						{
							case 0:
								caseInfo[i] = StringUtils.isNotNull(str = oldPropertiesRoot.getString(CASE_REQ_ID)) ? str : "";
								break;
							case 1:
								caseInfo[i] = StringUtils.isNotNull(str = oldPropertiesRoot.getString(CASE_REG)) ? str : "";
								break;
							case 2:
								caseInfo[i] = StringUtils.isNotNull(str = oldPropertiesRoot.getString(CASE_DES)) ? str : "";
								break;
							case 3:
								caseInfo[i] = StringUtils.isNotNull(str = oldPropertiesRoot.getString(CASE_PRO)) ? str : "";
								break;
							case 4:
								caseInfo[i] = StringUtils.isNotNull(str = oldPropertiesRoot.getString(CASE_CONDITION)) ? str : "";
								break;
							case 5:
								caseInfo[i] = StringUtils.isNotNull(str = oldPropertiesRoot.getString(CASE_CON)) ? str : "";
								break;
							default:
								break;
						}
					}
				}

			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
			} catch (WorkbenchException e)
			{
				e.printStackTrace();
			}

			return caseInfo;
		} else
		{
			return null;
		}

	}

	/**
	 * 向指定目录下的目录属性文件中写入目录属性值
	 * 
	 * @param folder 项目中的目录对象
	 * @param propertyId 属性ID
	 * @param proptyValue 属性值 duys修改：关闭流，解决删除文件时报异常的问题
	 */
	public static void setFolderProperty(IFolder folder, String propertyId, String propertyValue)
	{
		FileWriter writer = null;
		FileReader reader = null;
		try
		{
			File file = folder.getFile(FOLDER_PROPERTY).getLocation().toFile();
			if (!file.exists())
				file.createNewFile();
			reader = new FileReader(file);

			XMLMemento oldPropertiesRoot = null;
			XMLMemento newPropertiesRoot = null;

			try
			{
				oldPropertiesRoot = XMLMemento.createReadRoot(reader);
			} catch (Exception e)
			{
				oldPropertiesRoot = null;
			}

			try
			{
				// 创建要写入的property XML对象
				newPropertiesRoot = XMLMemento.createWriteRoot(ROOT_NODE_NAME);
				// 如果原property文件中有属性信息
				if (oldPropertiesRoot != null)
				{
					// 获取根节点下的所有L1子节点，先从原有的属性节点中查找
					boolean found = false;
					IMemento[] oldPropertyNodes = oldPropertiesRoot.getChildren(L1_NODE_NAME);
					for (IMemento oldPropertyNode : oldPropertyNodes)
					{
						String oldPropertyId = oldPropertyNode.getString(PROPERTY_ATTR_ID);
						if (oldPropertyId.equalsIgnoreCase(propertyId))
						{
							// 创建新节点，重新设置新的属性值
							IMemento newPropertyNode = newPropertiesRoot.createChild(L1_NODE_NAME);
							newPropertyNode.putString(PROPERTY_ATTR_ID, propertyId);
							newPropertyNode.putString(PROPERTY_ATTR_VALUE, propertyValue);
							found = true;
						} else
						{
							// 保存原有的属性值
							newPropertiesRoot.copyChild(oldPropertyNode);
						}
					}
					if (!found)
					{
						// 原property文件中有属性信息，但未找到指定的属性id
						IMemento newPropertyNode = newPropertiesRoot.createChild(L1_NODE_NAME);
						newPropertyNode.putString(PROPERTY_ATTR_ID, propertyId);
						newPropertyNode.putString(PROPERTY_ATTR_VALUE, propertyValue);
					}
				} else
				{
					// 原property文件中没有属性信息
					IMemento newPropertyNode = newPropertiesRoot.createChild(L1_NODE_NAME);
					newPropertyNode.putString(PROPERTY_ATTR_ID, propertyId);
					newPropertyNode.putString(PROPERTY_ATTR_VALUE, propertyValue);
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}

			writer = new FileWriter(file);
			newPropertiesRoot.save(writer);
			folder.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (CoreException e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				reader.close();
				writer.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}

		}

	}

	/**
	 * 获得指定目录下，指定目录属性类别ID的属性值。
	 * 
	 * @param folder
	 * @param propertyId
	 * @return
	 */
	public static String getFolderProperty(IFolder folder, String propertyId)
	{
		String propertyValue = "";
		try
		{
			if(!folder.getFile(FOLDER_PROPERTY).exists())
			{
				return propertyValue;
			}
			File file = folder.getFile(FOLDER_PROPERTY).getLocation().toFile();
			if (!file.exists())
				return propertyValue;
			FileReader reader = new FileReader(file);
			XMLMemento propertiesRoot = XMLMemento.createReadRoot(reader);
			IMemento[] propertyNodes = propertiesRoot.getChildren(L1_NODE_NAME);
			for (IMemento propertyNode : propertyNodes)
			{
				String oldPropertyId = propertyNode.getString(PROPERTY_ATTR_ID);
				if (oldPropertyId.equalsIgnoreCase(propertyId))
				{
					propertyValue = propertyNode.getString(PROPERTY_ATTR_VALUE);
					return propertyValue;
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			return propertyValue;
		}

		return propertyValue;
	}

	/**
	 * 将内存中的DOM树写入XML文件.
	 * 
	 * @param doc DOM引用
	 * @param fileName 被写入的XML文件绝对路径
	 * @return
	 * @throws Exception
	 */
	public static boolean writeDomToFile(Document doc, String fileName) throws Exception
	{
		boolean isOver = false;
		DOMSource doms = new DOMSource(doc);
		File f = new File(fileName);
		StreamResult sr = new StreamResult(f);
		try
		{
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			Properties properties = t.getOutputProperties();
			properties.setProperty(OutputKeys.ENCODING, "UTF-8");
			t.setOutputProperties(properties);
			t.transform(doms, sr);
			isOver = true;
		} catch (TransformerConfigurationException tce)
		{
			tce.printStackTrace();
		} catch (TransformerException te)
		{
			te.printStackTrace();
		}
		return isOver;
	}

	/**
	 * 拷贝并解析文件夹，一边拷贝一边解析，如果拷贝出错或者解析出错，则不解析拷贝出错的文件或者不拷贝解析出错的文件
	 * 
	 * @param srcDirectory 源文件夹绝对路径
	 * @param dstDirectory 目标文件夹绝对路径
	 */
	public static void copyAndParseFile(String srcDirectory, String dstDirectory, IProject project)
	{
		try
		{
			File dir = new File(srcDirectory);
			String[] file = dir.list();
			File temp = null;
			for (int i = 0; i < file.length; i++)
			{
				if (srcDirectory.endsWith(File.separator))
					temp = new File(srcDirectory + file[i]);
				else
					temp = new File(srcDirectory + File.separator + file[i]);

				if (temp.isFile())
				{
					FileInputStream input = new FileInputStream(temp);
					String destFilePath = dstDirectory + File.separator + (temp.getName()).toString();
					FileOutputStream output = new FileOutputStream(destFilePath);
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1)
					{
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
					File xml = new File(destFilePath);
					if (xml.exists())
					{
						// FIXME 添加对导入的icd文件的解析
						// ParserFactory.getICDParserForIOFile(xml,
						// project).doParse();//解析拷贝的目标文件夹的icd文件
					}
				}
				if (temp.isDirectory())
				{// 如果是子文件夹
					String destFilePath = dstDirectory + File.separator + (temp.getName()).toString();
					File desFolder = new File(destFilePath);
					if (!desFolder.exists())
					{
						desFolder.mkdir();
					}
					copyAndParseFile(srcDirectory + File.separator + file[i], dstDirectory + File.separator + file[i], project);
				}
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 获取shell
	 * 
	 * @return shell
	 */
	public static Shell getShell()
	{
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}

	public static void refreshView()
	{
		/*
		 * 刷新视图，以便显示出工程的描述信息
		 */
		ProjectView view = (ProjectView) EclipseUtils.getActivePage().findView("com.coretek.tools.ide.ui.DiagramView");
		view.getViewer().refresh(true);
	}
}
