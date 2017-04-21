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
	 * property�ڵ�����id���Ŀ¼����
	 */
	public static final String	FODLDER_TYPE						= "folderType";

	public static final String	FODLDER_TYPE_TEST_SUITE				= "testcase";

	/*
	 * project�ڵ�����id�����Ŀ����. ��չ֮��
	 */
	public static final String	PROPERTY_ATTR_ID_PROJECT_TYPE		= "projectType";

	public static final String	PROPERTY_ATTR_VALUE_PROJECT_TYPE	= "sequenceDiagramProject";

	/*
	 * xml(cas)�ļ���׺
	 */
	public static final String	XML_CAS_FILE_EXTENSION				= "cas";

	/*
	 * ���ڹ���Ŀ¼���Ե��ļ���������������Ŀ¼��ÿ��Ŀ¼���ɸ��ļ���¼��Ŀ¼�����ԡ�
	 */
	public static final String	FOLDER_PROPERTY						= ".folderProperty";

	/**
	 * ������
	 */
	public static final String	CASE_REQ_ID							= "requireNum";
	/**
	 * ���׼��regulation
	 */
	public static final String	CASE_REG							= "regulation";

	/**
	 *���Թ��
	 */
	public static final String	CASE_PRO							= "procedure";

	/**
	 * ������������
	 */
	public static final String	CASE_DES							= "des";

	/**
	 * �����Լ��
	 */
	public static final String	CASE_CON							= "constrain";

	/**
	 * ����
	 */
	public static final String	CASE_CONDITION						= "condition";

	/**
	 * �����ļ����ڵ�����
	 */
	private static final String	ROOT_NODE_NAME						= "properties";

	/*
	 * �����ļ�һ���ڵ�����
	 */
	private static final String	L1_NODE_NAME						= "property";

	/*
	 * property�ڵ����ԣ�id
	 */
	private static final String	PROPERTY_ATTR_ID					= "id";

	/*
	 * property�ڵ����ԣ�value
	 */
	private static final String	PROPERTY_ATTR_VALUE					= "value";

	/**
	 * ����ĿĿ¼�д����µ��ļ���
	 * 
	 * @param newProject �µ���Ŀ����
	 * @param dirName �´�����Ŀ¼����
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
	 * ��ȡ���� ������������Ϣ(0.�����ţ� 1.���׼��2.����������3.���Թ�� 4.�Ⱦ�������5.�����Լ��)
	 * 
	 * @param file ��������
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
	 * ��ָ��Ŀ¼�µ�Ŀ¼�����ļ���д��Ŀ¼����ֵ
	 * 
	 * @param folder ��Ŀ�е�Ŀ¼����
	 * @param propertyId ����ID
	 * @param proptyValue ����ֵ duys�޸ģ��ر��������ɾ���ļ�ʱ���쳣������
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
				// ����Ҫд���property XML����
				newPropertiesRoot = XMLMemento.createWriteRoot(ROOT_NODE_NAME);
				// ���ԭproperty�ļ�����������Ϣ
				if (oldPropertiesRoot != null)
				{
					// ��ȡ���ڵ��µ�����L1�ӽڵ㣬�ȴ�ԭ�е����Խڵ��в���
					boolean found = false;
					IMemento[] oldPropertyNodes = oldPropertiesRoot.getChildren(L1_NODE_NAME);
					for (IMemento oldPropertyNode : oldPropertyNodes)
					{
						String oldPropertyId = oldPropertyNode.getString(PROPERTY_ATTR_ID);
						if (oldPropertyId.equalsIgnoreCase(propertyId))
						{
							// �����½ڵ㣬���������µ�����ֵ
							IMemento newPropertyNode = newPropertiesRoot.createChild(L1_NODE_NAME);
							newPropertyNode.putString(PROPERTY_ATTR_ID, propertyId);
							newPropertyNode.putString(PROPERTY_ATTR_VALUE, propertyValue);
							found = true;
						} else
						{
							// ����ԭ�е�����ֵ
							newPropertiesRoot.copyChild(oldPropertyNode);
						}
					}
					if (!found)
					{
						// ԭproperty�ļ�����������Ϣ����δ�ҵ�ָ��������id
						IMemento newPropertyNode = newPropertiesRoot.createChild(L1_NODE_NAME);
						newPropertyNode.putString(PROPERTY_ATTR_ID, propertyId);
						newPropertyNode.putString(PROPERTY_ATTR_VALUE, propertyValue);
					}
				} else
				{
					// ԭproperty�ļ���û��������Ϣ
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
	 * ���ָ��Ŀ¼�£�ָ��Ŀ¼�������ID������ֵ��
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
	 * ���ڴ��е�DOM��д��XML�ļ�.
	 * 
	 * @param doc DOM����
	 * @param fileName ��д���XML�ļ�����·��
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
	 * �����������ļ��У�һ�߿���һ�߽������������������߽��������򲻽�������������ļ����߲���������������ļ�
	 * 
	 * @param srcDirectory Դ�ļ��о���·��
	 * @param dstDirectory Ŀ���ļ��о���·��
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
						// FIXME ��ӶԵ����icd�ļ��Ľ���
						// ParserFactory.getICDParserForIOFile(xml,
						// project).doParse();//����������Ŀ���ļ��е�icd�ļ�
					}
				}
				if (temp.isDirectory())
				{// ��������ļ���
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
	 * ��ȡshell
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
		 * ˢ����ͼ���Ա���ʾ�����̵�������Ϣ
		 */
		ProjectView view = (ProjectView) EclipseUtils.getActivePage().findView("com.coretek.tools.ide.ui.DiagramView");
		view.getViewer().refresh(true);
	}
}
