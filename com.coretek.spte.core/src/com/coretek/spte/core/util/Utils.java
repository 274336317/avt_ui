package com.coretek.spte.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.OpenStrategy;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.XMLMemento;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.WorkbenchWindow;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.coretek.common.template.Attribute;
import com.coretek.common.template.ICDMsg;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.template.TemplateEngine;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.common.utils.EclipseUtils;
import com.coretek.common.utils.ExpResolver;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.cfg.CfgPlugin;
import com.coretek.spte.cfg.ExecutionPreferencePage;
import com.coretek.spte.core.editor.SPTEEditor;
import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.AbtElement;
import com.coretek.spte.core.models.BackgroundChildMsgMdl;
import com.coretek.spte.core.models.BackgroundMsgMdl;
import com.coretek.spte.core.models.ContainerMdl;
import com.coretek.spte.core.models.IntervalConnMdl;
import com.coretek.spte.core.models.MsgConnMdl;
import com.coretek.spte.core.models.PeriodChildMsgMdl;
import com.coretek.spte.core.models.PeriodParentMsgMdl;
import com.coretek.spte.core.models.RootContainerMdl;
import com.coretek.spte.core.models.TestMdl;
import com.coretek.spte.core.models.TestNodeContainerMdl;
import com.coretek.spte.core.models.TestNodeMdl;
import com.coretek.spte.core.models.TestToolMdl;
import com.coretek.spte.core.models.TestedObjectMdl;
import com.coretek.spte.data.DataPlugin;
import com.coretek.spte.testcase.Message;
import com.coretek.spte.testcase.TestCase;
import com.coretek.spte.testcase.TestedObjects;
import com.coretek.spte.testcase.TimeSpan;

/**
 * �ṩһЩ���÷���
 * 
 * @author ���Ρ
 * @date 2010-9-15
 * 
 */
public class Utils
{
	/**
	 * ������
	 */
	public static final String	CASE_REQ_ID		= "requireNum";
	/**
	 * ���׼��regulation
	 */
	public static final String	CASE_REG		= "regulation";

	/**
	 *���Թ��
	 */
	public static final String	CASE_PRO		= "procedure";

	/**
	 * ������������
	 */
	public static final String	CASE_DES		= "des";

	/**
	 * �����Լ��
	 */
	public static final String	CASE_CON		= "constrain";

	/**
	 * ����
	 */
	public static final String	CASE_CONDITION	= "condition";

	public static final double	��				= 3.1415926;

	/**
	 * ��ȡ��������ids
	 * 
	 * @param testCase
	 * @return </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-5-19
	 */
	public static List<String> getTestedObjects(TestCase testCase)
	{
		TestedObjects testedObjs = (TestedObjects) testCase.getTestedObjects();
		List<String> referedIds = new ArrayList<String>();
		List<Entity> entities = testedObjs.getChildren();
		for (Entity entity : entities)
		{
			referedIds.add(entity.getFieldValue("id").toString());
		}

		return referedIds;
	}

	public static String getICDFilePath(File caseFile)
	{
		try
		{
			InputStream input = new FileInputStream(caseFile);
			return getICDFilePath(input);
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		return null;

	}

	public static String getICDFilePath(IFile caseFile)
	{
		return getICDFilePath(caseFile.getLocation().toFile());
	}

	/**
	 * ��ȡ�������������õ�ICD�ļ�
	 * 
	 * @param caseFile
	 * @return </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-7-27
	 */
	public static IFile getICDFile(IFile caseFile)
	{
		IFile icdFile = null;
		String icdFilePath = Utils.getICDFilePath(caseFile);
		if (StringUtils.isNotNull(icdFilePath))
		{
			IPath path = new Path(icdFilePath);
			icdFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
		}

		return icdFile;
	}

	/**
	 * Getting the relative path of icd file that was referred by the given
	 * TestCase
	 * 
	 * @param input the TestCase's contents
	 * @return </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-5-2
	 */
	public static String getICDFilePath(InputStream input)
	{
		String path = null;
		String xpath = "/testCase/ICD/file";
		XPathExpression xpathExpr = null;
		try
		{
			xpathExpr = XPathFactory.newInstance().newXPath().compile(xpath);
			NodeList nodes = (NodeList) xpathExpr.evaluate(new InputSource(input), XPathConstants.NODESET);
			input.close();
			if (nodes == null || nodes.getLength() == 0)
			{
				return path;
			} else
			{
				outer: for (int i = 0, length = nodes.getLength(); i < length; i++)
				{
					Node node = nodes.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE)
					{
						NodeList children = node.getChildNodes();
						for (int j = 0, length2 = children.getLength(); j < length2; j++)
						{
							Node child = children.item(j);
							if (child.getNodeType() == Node.TEXT_NODE)
							{
								path = child.getTextContent();
								break outer;
							}
						}
					}
				}
			}
		} catch (XPathExpressionException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return path;
	}

	/**
	 * Get all of icd files in the given folder.
	 * 
	 * @param folder
	 * @return </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-6-11
	 */
	public static List<IFile> getAllICDFilesInFolder(IContainer folder)
	{
		List<IFile> files = new ArrayList<IFile>();
		IResource[] resources;
		try
		{
			resources = folder.members();
			for (IResource resource : resources)
			{
				if (resource instanceof IFile)
				{
					IFile file = (IFile) resource;
					if (Utils.isICDFile(file))
					{
						files.add(file);
					}
				} else if (resource instanceof IContainer)
				{
					List<IFile> list = getAllICDFilesInFolder((IContainer) resource);
					files.addAll(list);
				}
			}
		} catch (CoreException e)
		{
			e.printStackTrace();
		}

		return files;
	}

	/**
	 * Getting the md5 of some TestCase object.
	 * 
	 * @param input
	 * @return </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-5-2
	 */
	public static String getMD5StoredInCaseFile(InputStream input)
	{
		String md5 = null;
		String xpath = "/testCase/ICD/md5";
		XPathExpression xpathExpr = null;
		try
		{
			xpathExpr = XPathFactory.newInstance().newXPath().compile(xpath);
			NodeList nodes = (NodeList) xpathExpr.evaluate(new InputSource(input), XPathConstants.NODESET);
			if (nodes == null || nodes.getLength() == 0)
			{
				return md5;
			} else
			{
				outer: for (int i = 0, length = nodes.getLength(); i < length; i++)
				{
					Node node = nodes.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE)
					{
						NodeList children = node.getChildNodes();
						for (int j = 0, length2 = children.getLength(); j < length2; j++)
						{
							Node child = children.item(j);
							if (child.getNodeType() == Node.TEXT_NODE)
							{
								md5 = child.getTextContent();
								break outer;
							}
						}
					}
				}
			}
		} catch (XPathExpressionException e)
		{
			e.printStackTrace();
		}
		return md5;
	}

	/**
	 * To check whether the given project is icd project or not.
	 * 
	 * @param project
	 * @return </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-5-2
	 */
	public static boolean isICDProject(IProject project)
	{
		try
		{
			String[] natureIds = project.getDescription().getNatureIds();
			if (natureIds == null)
				return false;
			for (String id : natureIds)
			{
				if ("com.coretek.spte.projectView.icdProjectNature".equals(id))
					return true;
			}
		} catch (CoreException e1)
		{
			e1.printStackTrace();
		}
		return false;
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
			return new String[0];
		}

	}

	/**
	 * ���ݱ༭����ƫ�Ƽ���������ڻ����������е�λ��
	 * 
	 * @param rectangle
	 * @return </br> <b>����</b> duyisen </br> <b>����</b> 2012-3-13
	 */
	public static Rectangle convertReToAb(Rectangle rectangle)
	{
		SPTEEditor activeEditor = (SPTEEditor) EclipseUtils.getActiveEditor();
		FigureCanvas canvas = (FigureCanvas) activeEditor.getGraphicalViewer().getControl();
		int offsetY = canvas.getViewport().getViewLocation().y;
		int offsetX = canvas.getViewport().getViewLocation().x;
		return new Rectangle(rectangle.x + offsetX, rectangle.y + offsetY, rectangle.width, rectangle.height);
	}

	/**
	 * ���ݱ༭����ƫ�Ƽ���������ڵ�ǰ���ڵ�λ��
	 * 
	 * @param rectangle
	 * @return </br> <b>����</b> duyisen </br> <b>����</b> 2012-3-13
	 */
	public static Rectangle convertAbToRe(Rectangle rectangle)
	{
		SPTEEditor activeEditor = (SPTEEditor) EclipseUtils.getActiveEditor();
		FigureCanvas canvas = (FigureCanvas) activeEditor.getGraphicalViewer().getControl();
		int offsetY = canvas.getViewport().getViewLocation().y;
		int offsetX = canvas.getViewport().getViewLocation().x;
		return new Rectangle(rectangle.x - offsetX, rectangle.y - offsetY, rectangle.width, rectangle.height);
	}

	/**
	 * 
	 * @param v ��Ҫ���������������
	 * @param scale ָ�����������λ��
	 * @return </br> <b>����</b> duyisen </br> <b>����</b> 2011-7-13
	 */
	public static double div(double v, int scale)
	{
		if (scale < 0)
		{
			throw new IllegalArgumentException("����scale�����Ǵ��ڵ���0������");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * �ж�һ�������Ƿ�Ϊ������Թ���
	 * 
	 * @param project
	 * @return �����������Թ����򷵻�true</br> <b>����</b> ���Ρ </br> <b>����</b> 2011-7-15
	 * @throws CoreException
	 * @throws IOException
	 */
	public static boolean isSoftwareTestingProject(IProject project)
	{
		try
		{
			String[] natureIds = project.getDescription().getNatureIds();
			if (natureIds == null)
				return false;
			for (String id : natureIds)
			{
				if ("com.coretek.spte.projectView.testProjectNature".equals(id))
					return true;
			}
		} catch (CoreException e1)
		{
			e1.printStackTrace();
		}
		return false;
	}

	/**
	 * �ж�������Ϣ�ķ���ֵ��ʽ�Ƿ���ȷ
	 * 
	 * @param value
	 * @return </br> <b>����</b> duyisen </br> <b>����</b> 2012-2-9
	 */
	public static boolean isSendValueRight(String value)
	{
		ExpResolver ex = ExpResolver.getExpResolver();
		String result = "";
		if (value.contains("$previous"))
		{
			value = value.replaceAll("\\$previous", "1");
			if (!StringUtils.isDouble(value))
			{
				try
				{
					result = (String) ex.evaluate(value);
				} catch (Exception e)
				{
					return false;
				}
				if (StringUtils.isDouble(result))
				{
					return true;
				}
			}
		} else
		{
			if (StringUtils.isPositiveInteger(result))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * �ж�������Ϣ�ķ���ֵ��ʽ�Ƿ���ȷ
	 * 
	 * @param value
	 * @param index
	 * @return </br> <b>����</b> duyisen </br> <b>����</b> 2012-2-9
	 */
	public static boolean isSendValueRight(String value, int index)
	{
		if (index > 2)
		{
			ExpResolver ex = ExpResolver.getExpResolver();
			String result = "";
			if (value.contains("$previous"))
			{
				value = value.replaceAll("\\$previous", "1");
				if (!StringUtils.isDouble(value))
				{
					result = (String) ex.evaluate(value);
					if (StringUtils.isDouble(result))
					{
						return true;
					}
				}
			} else
			{
				if (StringUtils.isPositiveInteger(result))
				{
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * ��ȡ��������
	 * 
	 * @param projectName ������
	 * @param suitePath ���Լ���
	 * @param caseName ����������
	 * @return
	 */
	public static IFile getCaseByName(String projectName, String suitePath, String caseName)
	{
		IProject project = EclipseUtils.getProject(projectName);
		if (project == null)
			return null;

		IFolder folder = null;
		if (suitePath.contains("\\"))
		{
			String[] suiteNames = suitePath.split("\\\\");

			int i = 0;
			for (String name : suiteNames)
			{
				if (i == 0)
				{
					folder = project.getFolder(name);
				} else
				{
					folder = folder.getFolder(name);
				}
				i++;
			}
		} else
		{
			folder = project.getFolder(suitePath);
		}

		if (folder == null)
			return null;
		IFile file = folder.getFile(caseName);

		return file;
	}

	/**
	 * ͨ�������Լ����Լ�·����ȡ���Լ�IFolder����
	 * 
	 * @param pro
	 * @param suitePath
	 * @return </br> <b>����</b> duyisen </br> <b>����</b> 2012-2-6
	 */
	public static IFolder getSuiteByName(IProject pro, String suitePath)
	{

		IFolder folder = null;
		if (suitePath.contains("\\"))
		{
			String[] suiteNames = suitePath.split("\\\\");

			int i = 0;
			for (String name : suiteNames)
			{
				if (i == 0)
				{
					folder = pro.getFolder(name);
				} else
				{
					folder = folder.getFolder(name);
				}
				i++;
			}
		} else
		{
			folder = pro.getFolder(suitePath);
		}
		return folder;
	}

	/**
	 * ��ȡ������items�е�����ResultMessageBean(s)
	 * 
	 * @param parts ��ѡ�е�parts
	 * @return
	 */
	public static List<Entity> getAllResultMessage(List<TestNodeMdl> items)
	{
		List<Entity> results = new ArrayList<Entity>();
		for (TestNodeMdl item : items)
		{
			if (Utils.hasConnection(item))
			{
				MsgConnMdl connection = Utils.getMessageOfItem(item);
				if (connection.getTcMsg() != null)
				{
					Message result = null;
					if (!(connection instanceof PeriodChildMsgMdl))
					{
						result = connection.getTcMsg().getMsg();
						results.add(result);
						if (Utils.isSourceOfTimer(item))
						{
							IntervalConnMdl timer = (IntervalConnMdl) Utils.getTimerFromOutgoing(item);
							if (timer.getResultInterval() != null)
							{
								TimeSpan interval = (TimeSpan) timer.getResultInterval();
								results.add(interval);
							}
						}
					} else if (connection instanceof PeriodChildMsgMdl)
					{// ������Ϣ��������
						if (Utils.isSourceOfTimer(item))
						{
							IntervalConnMdl timer = (IntervalConnMdl) Utils.getTimerFromOutgoing(item);
							if (timer.getResultInterval() != null)
							{
								TimeSpan interval = (TimeSpan) timer.getResultInterval();
								results.add(interval);
							}
						}
					}
				}
			}
		}

		return results;
	}

	/**
	 * �ж�һ��item�Ƿ���ʱ�������ߵ�����Դ
	 * 
	 * @param item
	 * @return
	 */
	public static boolean isSourceOfTimer(TestNodeMdl item)
	{
		if (item == null)
		{
			return true;
		}
		if (item.getOutgoingConnections() != null && item.getOutgoingConnections().size() > 0)
		{
			for (AbtConnMdl model : item.getOutgoingConnections())
			{
				if (model instanceof IntervalConnMdl)
				{
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * �жϴ�source���ϻ����µĵ�һ��ӵ����Ϣ���ߵ�item�Ƿ���source��target֮��
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public static boolean hasMessageBetweenTargetAndSource(TestNodeMdl source, TestNodeMdl target)
	{
		int sourceIndex = source.getParent().getChildren().indexOf(source);
		int targetIndex = target.getParent().getChildren().indexOf(target);

		if (sourceIndex > targetIndex)
		{// ��source����
			for (int i = sourceIndex + 1; i >= targetIndex; i--)
			{
				TestNodeMdl item = (TestNodeMdl) source.getParent().getChildren().get(i);
				if (Utils.hasConnection(item))
				{
					return true;
				}
			}
		} else if (sourceIndex < targetIndex)
		{// ��source����
			for (int i = sourceIndex + 1; i <= targetIndex; i++)
			{
				TestNodeMdl item = (TestNodeMdl) source.getParent().getChildren().get(i);
				if (Utils.hasConnection(item))
				{
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * ��ȡĳ��item�е���Ϣ�������ǻ�ȡʱ����
	 * 
	 * @param item
	 * @return ��Ϣ����ģ��
	 */
	public static MsgConnMdl getMessageOfItem(TestNodeMdl item)
	{
		if (item == null)
		{
			return null;
		}
		if (hasConnection(item))
		{
			if (item.getOutgoingConnections() != null && item.getOutgoingConnections().size() > 0)
			{
				for (AbtConnMdl model : item.getOutgoingConnections())
				{
					if (model instanceof MsgConnMdl)
					{
						return (MsgConnMdl) model;
					}
				}
			}
			if (item.getIncomingConnections() != null && item.getIncomingConnections().size() > 0)
			{
				for (AbtConnMdl model : item.getIncomingConnections())
				{
					if (model instanceof MsgConnMdl)
					{
						return (MsgConnMdl) model;
					}
				}
			}
		}
		return null;
	}

	/**
	 * �ж�һ�������Ƿ�Ҫ������������ʱ�����������ӵ���Ϣ����֮�䣬 ����� �򷵻�true
	 * 
	 * @return
	 */
	public static boolean isBetweenTimeredConnections(TestNodeMdl source)
	{
		int index = source.getParent().getChildren().indexOf(source);
		TestNodeMdl item = (TestNodeMdl) getTestedObject(source).getChildren().get(0).getChildren().get(index);
		AbtConnMdl timer = null;
		/*
		 * �����ң��ҵ���һ��ӵ����Ϣ���ߵ�item��Ȼ���ж��Ƿ���ʱ����������item������ǵĻ������ж�ʱ������Ŀ��item�Ƿ���
		 * ��Ҫ�����ӵ�item֮��
		 */
		for (int i = index - 1; i >= 0; i--)
		{
			TestNodeMdl upper = (TestNodeMdl) item.getParent().getChildren().get(i);

			if (hasConnection(upper))
			{
				timer = getTimerFromOutgoing(upper);// getTimer(upper);
				if (timer != null)
				{
					TestNodeMdl target = (TestNodeMdl) timer.getTarget();
					int targetIndex = item.getParent().getChildren().indexOf(target);
					if (targetIndex >= index)
					{
						return true;
					}
				}
				break;
			}
		}

		return false;
	}

	/**
	 * ��item�е�Outgoing�л�ȡtimer
	 * 
	 * @param timerSource
	 * @return
	 */
	public static AbtConnMdl getTimerFromOutgoing(TestNodeMdl timerSource)
	{
		List<AbtConnMdl> outs = timerSource.getOutgoingConnections();
		if (outs != null && outs.size() > 0)
		{
			for (AbtConnMdl model : outs)
			{
				if (model instanceof IntervalConnMdl)
				{
					return model;
				}
			}
		}
		return null;
	}

	/**
	 * ��ȡ����ģ��
	 * 
	 * @param item
	 * @return
	 */
	public static TestedObjectMdl getTestedObject(TestNodeMdl item)
	{
		TestMdl lifeline = (TestMdl) item.getParent().getParent();
		if (lifeline instanceof TestedObjectMdl)
		{
			return (TestedObjectMdl) lifeline;
		}

		List<AbtElement> elements = item.getParent().getParent().getParent().getChildren();
		for (AbtElement element : elements)
		{
			if (element != lifeline)
			{
				return (TestedObjectMdl) element;
			}
		}
		return null;
	}

	/**
	 * ��ȡ����ģ��
	 * 
	 * @param mdl
	 * @return
	 */
	public static TestedObjectMdl getTestedObject(RootContainerMdl mdl)
	{
		return (TestedObjectMdl) mdl.getChildren().get(0).getChildren().get(1);
	}

	/**
	 * ��ȡ�������
	 * 
	 * @param conn
	 * @return
	 */
	public static TestedObjectMdl getTestedObject(MsgConnMdl conn)
	{
		TestNodeMdl item = (TestNodeMdl) conn.getSource();
		return getTestedObject(item);
	}

	/**
	 * ��ȡ���Թ���
	 * 
	 * @param item
	 * @return
	 */
	public static TestToolMdl getTestTool(TestNodeMdl item)
	{
		TestMdl lifeline = (TestMdl) item.getParent().getParent();
		if (lifeline instanceof TestToolMdl)
		{
			return (TestToolMdl) lifeline;
		}

		List<AbtElement> elements = item.getParent().getParent().getParent().getChildren();
		for (AbtElement element : elements)
		{
			if (element instanceof TestToolMdl)
			{
				return (TestToolMdl) element;
			}
		}
		return null;
	}

	/**
	 * ��ȡ��������е�item
	 * 
	 * @param model
	 * @return
	 */
	public static TestNodeMdl getTestedLineItem(AbtConnMdl model)
	{
		TestNodeMdl source = (TestNodeMdl) model.getSource();
		TestNodeMdl target = (TestNodeMdl) model.getTarget();

		if (Utils.isSendMessage(model))
		{// ������Ϣ
			return target;
		} else
		{// ������Ϣ
			return source;
		}
	}

	/**
	 * �����������ڱ༭���д�
	 * 
	 * @param file
	 * @return
	 * @throws PartInitException
	 */
	public static SPTEEditor openEditor(IFile file) throws PartInitException
	{
		boolean activate = OpenStrategy.activateOnOpen();
		IEditorPart editorPart = IDE.openEditor(EclipseUtils.getActivePage(), file, activate);
		return (SPTEEditor) editorPart;
	}

	/**
	 * �Ƿ�Ϊ.cas�ļ�
	 * 
	 * @param file
	 */
	public static boolean isCasFile(IFile file)
	{

		return SPTEConstants.RESULT_FILE_POST_FIX.equals("." + file.getFileExtension());
	}

	/**
	 * ��ĳ��item�����Ӽ������ҳ�������Ϣ�ĸ�����
	 * 
	 * @param models
	 * @return
	 */
	public static PeriodParentMsgMdl getFixedParentMessageModel(List<AbtConnMdl> models)
	{
		if (models == null)
		{
			return null;
		}
		for (AbtConnMdl model : models)
		{
			if (model instanceof PeriodParentMsgMdl)
			{
				return (PeriodParentMsgMdl) model;
			}
		}
		return null;
	}

	/**
	 * ��ĳ��item�����Ӽ������ҳ�������Ϣ��������
	 * 
	 * @param models
	 * @return
	 */
	public static PeriodChildMsgMdl getFixedChildMessageModel(List<AbtConnMdl> models)
	{
		if (models == null)
		{
			return null;
		}
		for (AbtConnMdl model : models)
		{
			if (model instanceof PeriodChildMsgMdl)
			{
				return (PeriodChildMsgMdl) model;
			}
		}
		return null;
	}

	/**
	 * �ж�ĳ��item�Ƿ�ӵ����Ϣ��ʱ���������������������򷵻�true�����򷵻�false
	 * 
	 * @param item
	 * @return
	 */
	public static boolean hasConnection(TestNodeMdl item)
	{
		if (item.getOutgoingConnections() != null && item.getOutgoingConnections().size() > 0)
		{
			return true;
		}
		if (item.getIncomingConnections() != null && item.getIncomingConnections().size() > 0)
		{
			return true;
		}
		return false;
	}

	/**
	 * �ж�ĳ��item�Ƿ�ӵ����Ϣ���ߣ�������򷵻�true�����򷵻�false�� ע�⣬���������������Ϣ������ʱ����
	 * 
	 * @param item
	 * @return
	 */
	public static boolean hasMessage(TestNodeMdl item)
	{
		if (item.getOutgoingConnections() != null && item.getOutgoingConnections().size() > 0)
		{
			for (AbtConnMdl model : item.getOutgoingConnections())
			{
				if (model instanceof MsgConnMdl)
				{
					return true;
				}
			}
		}
		if (item.getIncomingConnections() != null && item.getIncomingConnections().size() > 0)
		{
			for (AbtConnMdl model : item.getIncomingConnections())
			{
				if (model instanceof MsgConnMdl)
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * ��ȡĳ��item�����ڵ���һ��item
	 * 
	 * @param item
	 * @return
	 */
	public static TestNodeMdl getNextNode(TestNodeMdl item)
	{
		int length = item.getParent().getChildren().size();
		if (length > item.getParent().getChildren().indexOf(item) + 1)
		{
			return (TestNodeMdl) item.getParent().getChildren().get(item.getParent().getChildren().indexOf(item) + 1);
		}
		return null;
	}

	/**
	 * �ж�ĳ���ڵ��Ƿ���������Ϣ�ĸ�������������֮�䣬������򷵻�true�����򷵻�false��
	 * 
	 * @param item
	 * @return
	 */
	public static boolean isInsideCycleMessage(TestNodeMdl item)
	{
		TestNodeContainerMdl parentItem = (TestNodeContainerMdl) item.getParent();
		int index = parentItem.getChildren().indexOf(item);
		if (index == 0)
		{
			return false;
		}
		TestNodeMdl fixed = (TestNodeMdl) parentItem.getChildren().get(index - 1);
		if (fixed.getIncomingConnections() != null && fixed.getIncomingConnections().size() > 0)
		{
			List<AbtConnMdl> list = fixed.getIncomingConnections();
			for (AbtConnMdl model : list)
			{
				if (model instanceof PeriodParentMsgMdl)
				{
					return true;
				}
			}
		}
		if (fixed.getOutgoingConnections() != null && fixed.getOutgoingConnections().size() > 0)
		{
			List<AbtConnMdl> list = fixed.getOutgoingConnections();
			for (AbtConnMdl model : list)
			{
				if (model instanceof PeriodParentMsgMdl)
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * �ж�ĳ���ڵ��Ƿ���������Ϣ�������ߣ�������򷵻�true�����򷵻�false��
	 * 
	 * @param item
	 * @return
	 */
	public static boolean isCycleChildMessage(TestNodeMdl item)
	{
		if (item.getIncomingConnections() != null && item.getIncomingConnections().size() > 0)
		{
			List<AbtConnMdl> list = item.getIncomingConnections();
			for (AbtConnMdl model : list)
			{
				if (model instanceof PeriodChildMsgMdl)
				{
					return true;
				}
			}
		}
		if (item.getOutgoingConnections() != null && item.getOutgoingConnections().size() > 0)
		{
			List<AbtConnMdl> list = item.getOutgoingConnections();
			for (AbtConnMdl model : list)
			{
				if (model instanceof PeriodChildMsgMdl)
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * �ļ��Ƿ�Ϊicd�ļ�
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isIcdFile(File file)
	{

		return file.getName().endsWith(SPTEConstants.ICD_FILE_POST_FIX);
	}

	/**
	 * To verify that the given IFile whether is ICD File or not.
	 * 
	 * @param file
	 * @return </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-5-2
	 */
	public static boolean isICDFile(IFile file)
	{
		File jFile = file.getLocation().toFile();
		return isIcdFile(jFile);
	}

	/**
	 * �ж���Ϣ�Ƿ�Ϊ������Ϣ��ע�ⲻ�ܽ�ʱ������Ϊ��������
	 * 
	 * @param model
	 * @return
	 */
	public static boolean isSendMessage(AbtConnMdl model)
	{
		if (model instanceof IntervalConnMdl)
		{
			throw new IllegalArgumentException("You can not pass a timer as argument.");
		}
		TestNodeMdl item = (TestNodeMdl) model.getTarget();
		TestMdl linelife = (TestMdl) item.getParent().getParent();
		if (linelife instanceof TestedObjectMdl)
		{
			return true;

		} else
		{
			return false;
		}
	}

	/**
	 * �жϱ�ѡ�е��ļ����Ƿ��ǰ���icd�ļ���folder������Ƿ���true
	 * 
	 * @param folder
	 * @return
	 */
	public static boolean isIcdFolder(IFolder folder)
	{
		if (folder.getName().equals(SPTEConstants.ICD_FOLDER_NAME))
		{
			return true;
		}
		if (folder.getParent() instanceof IFolder)
		{
			IFolder parentFolder = (IFolder) folder.getParent();
			return isIcdFolder(parentFolder);
		}
		return false;
	}

	/**
	 * ��ȡfolder�µ�����icd�ļ�
	 * 
	 * @param folder
	 * @return
	 */
	public static void getIcdFiles(IFolder folder, List<IFile> files)
	{
		IResource[] subFiles = null;
		try
		{
			subFiles = folder.members();
		} catch (CoreException e)
		{
			e.printStackTrace();
		}
		if (subFiles == null)
		{
			return;
		}
		for (IResource subFile : subFiles)
		{
			if (subFile instanceof IFolder)
			{
				getIcdFiles((IFolder) subFile, files);
			} else
			{
				if (subFile instanceof IFile)
				{
					IFile icdFile = (IFile) subFile;
					if ("xml".equals(icdFile.getFileExtension()))
						files.add((IFile) subFile);
				}
			}
		}
	}

	/**
	 * ��ȡһ��lineItem��������������in timer
	 * 
	 * @param item
	 * @return
	 */
	public static AbtConnMdl getIncomingInterval(TestNodeMdl item)
	{
		if (item.getIncomingConnections() != null)
		{
			for (AbtConnMdl model : item.getIncomingConnections())
			{
				if (model instanceof IntervalConnMdl)
				{
					return model;
				}
			}
		}
		return null;
	}

	/**
	 * ��ȡһ��lineItem��������������out timer
	 * 
	 * @param item
	 * @return
	 */
	public static AbtConnMdl getOutcomingInterval(TestNodeMdl item)
	{
		if (item.getOutgoingConnections() != null)
		{
			for (AbtConnMdl model : item.getOutgoingConnections())
			{
				if (model instanceof IntervalConnMdl)
				{
					return model;
				}
			}
		}
		return null;
	}

	/**
	 * �ӱ�ѡ���LineItem��ʼ���²��ң�����ܲ��ҵ�ӵ����Ϣ���ߵ�LineItem�򷵻�true, ���򷵻�false
	 * 
	 * @return
	 */
	public static boolean hasMessageFromSelectedItem(TestNodeMdl item)
	{
		TestNodeContainerMdl parent = (TestNodeContainerMdl) item.getParent();
		for (int i = parent.getChildren().indexOf(item) + 1; i < parent.getChildren().size(); i++)
		{
			if (Utils.hasMessage((TestNodeMdl) parent.getChildren().get(i)))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * ��ȡ���̵������еĲ�������
	 * 
	 * @param project
	 * @return
	 */
	public static List<IFile> getAllCasesInProject(IProject project)
	{
		List<IFile> files = new ArrayList<IFile>();
		try
		{
			IResource resources[] = project.members();
			for (IResource resource : resources)
			{
				if (resource instanceof IFolder)
				{
					IFolder folder = (IFolder) resource;
					if (!Utils.isTestSuite(folder))
					{
						continue;
					}
					IResource temps[] = folder.members();
					for (IResource temp : temps)
					{
						if (temp instanceof IFile)
						{
							IFile file = (IFile) temp;
							if (file.getName().endsWith(SPTEConstants.RESULT_FILE_POST_FIX))
							{
								files.add(file);
							}
						} else if (temp instanceof IFolder)
						{
							IFolder fd = (IFolder) temp;
							files.addAll(getAllCasesInFolder(fd));
						}
					}
				}
			}
		} catch (CoreException e)
		{
			e.printStackTrace();
		}
		return files;
	}

	/**
	 * 
	 * ��ȡ���Լ��е�����.cas�ļ�
	 * 
	 * @param folder
	 * @return
	 */
	public static List<IFile> getAllCasesInFolder(IFolder folder)
	{
		List<IFile> files = new ArrayList<IFile>();
		if (!Utils.isTestSuite(folder))
		{
			return files;
		}
		IResource resources[] = null;
		try
		{
			resources = folder.members();
		} catch (CoreException e)
		{
			e.printStackTrace();
		}
		if (resources != null)
		{
			for (IResource temp : resources)
			{
				if (temp instanceof IFile)
				{
					IFile file = (IFile) temp;
					if (file.getName().endsWith(SPTEConstants.RESULT_FILE_POST_FIX))
					{
						files.add(file);
					}
				} else if (temp instanceof IFolder)
				{
					files.addAll(getAllCasesInFolder((IFolder) temp));
				}
			}
		}

		return files;
	}

	/**
	 * ��ȡ�����ռ��е����в�������
	 * 
	 * @return
	 */
	public static IFile[] getAllCasesInWorkspace()
	{
		List<IFile> files = new ArrayList<IFile>();
		IProject projects[] = EclipseUtils.getAllProjects();
		for (IProject project : projects)
		{
			files.addAll(Utils.getAllCasesInProject(project));
		}

		return files.toArray(new IFile[files.size()]);
	}

	/**
	 * ��֤����Ĳ�������
	 * 
	 * @param file ���������ļ�
	 * @return </br> <b>����</b> duyisen </br> <b>����</b> 2012-2-9
	 */
	public static boolean validateImportedTestCase(IFile file)
	{
		// String xpath = "/testCase/ICD";
		// XPathExpression xpathExpr = null;
		// try {
		// xpathExpr = XPathFactory
		// .newInstance()
		// .newXPath()
		// .compile(xpath);
		// } catch (XPathExpressionException e) {
		// LoggingPlugin.logException(logger, e);
		// }
		//		
		// InputStream input = null;
		// NodeList nodes = null;
		//		
		// try {
		// input = new FileInputStream(file.getLocation().toFile());
		// Object result = xpathExpr.evaluate(new InputSource(input),
		// XPathConstants.NODESET);
		// nodes = (NodeList)result;
		//			
		// } catch (FileNotFoundException e) {
		// LoggingPlugin.logException(logger, e);
		// return false;
		// } catch (XPathExpressionException e) {
		// LoggingPlugin.logException(logger, e);
		// return false;
		// } finally {
		// try {
		// if(input != null)
		// input.close();
		// } catch (IOException e) {
		// LoggingPlugin.logException(logger, e);
		// }
		// }
		//		
		// if(nodes == null || nodes.getLength() != 1) {
		// logger.warning("�޷����ļ����ҵ�ICD�ڵ㡣filePath=" +
		// file.getLocation().toFile().getAbsolutePath());
		// return false;
		// }
		//		
		// Node node = nodes.item(0);
		// NodeList children = node.getChildNodes();
		// if(children.getLength() < 2) {
		// logger.warning("ICD�ڵ���������Ӧ���������ӽڵ㡣children=" + children.getLength());
		// return false;
		// }
		// String icdPath = null;
		// String version = null;
		// for(int i = 0; i < children.getLength(); i ++) {
		// Node child = children.item(i);
		// if(child.getNodeType() != Node.ELEMENT_NODE) {
		// continue;
		// }
		// if(child.getNodeName().equals("file")) {
		// NodeList kids = child.getChildNodes();
		// for(int k = 0; k < kids.getLength(); k ++) {
		// Node kid = kids.item(k);
		// if(kid.getNodeType() == Node.TEXT_NODE) {
		// icdPath = kid.getTextContent();
		// break;
		// }
		// }
		// if(StringUtils.isNull(icdPath)) {
		// logger.warning("ICD�ڵ��µ�file�ڵ��в�δ�����κ����ݡ�");
		// return false;
		// }
		// } else if(child.getNodeName().equals("version")) {
		// NodeList kids = child.getChildNodes();
		// for(int k = 0; k < kids.getLength(); k ++) {
		// Node kid = kids.item(k);
		// if(kid.getNodeType() == Node.TEXT_NODE) {
		// version = kid.getTextContent();
		// break;
		// }
		// }
		// if(StringUtils.isNull(version)) {
		// logger.warning("ICD�ڵ��µ�version�ڵ��в�δ�����κ����ݡ�");
		// return false;
		// }
		// }
		// }
		//		
		// if(StringUtils.isNull(icdPath)) {
		// logger.warning("ICD�ڵ��µ�file�ڵ��в�δ�����κ����ݡ�");
		// return false;
		// }
		//		
		// if(StringUtils.isNull(version)) {
		// logger.warning("ICD�ڵ��µ�version�ڵ��в�δ�����κ����ݡ�");
		// return false;
		// }
		// icdPath = icdPath.trim();
		// version = version.trim();
		//		
		// // Pattern p = Pattern.compile("^(icd)/.+(\\.xml)$");
		// // Matcher m = p.matcher(icdPath);
		// // if(!m.matches()) {
		// // logger.warning("file�ڵ������������ݵĸ�ʽ����ȷ��icdPath=" + icdPath);
		// // return false;
		// // }
		// String[] strs = icdPath.split("/");
		// IProject prj = file.getProject();
		// IResource res = null;
		// IFolder folder = null;
		// for(int i = 0; i < strs.length; i ++) {
		// if(i == strs.length - 1) {
		// res = folder.findMember(strs[strs.length - 1]);
		// } else if(i == 1){
		// folder = prj.getFolder(strs[i]);
		// } else if(StringUtils.isNotNull(strs[i])){
		// folder = folder.getFolder(strs[i]);
		// }
		// }
		//		
		// IFile icdFile = (IFile)res;
		// ClazzManager clazzManager =
		// TemplateEngine.getEngine().parseICD(icdFile.getLocation().toFile());
		// if(clazzManager == null) {
		// logger.warning("�޷�������icd�ļ�" + strs[strs.length - 1]);
		// return false;
		// }
		// String version2 = TemplateUtils.getVerion(clazzManager);
		// if(!version.equals(version2)) {
		// logger.warning("�汾�Ų���ƥ�䡣case�ļ��б���İ汾��=" + version + "����icd�ļ��б���İ汾��="
		// + version2);
		// return false;
		// }

		return true;
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

	/**
	 * ��ȡ����ģ��/���Թ��ߵ��е�һ�����е�LineItem����ν���е�LineItem���Ǵ��������Ҳ����κα�ʹ�õ�LineItem
	 * 
	 * @param lineModel
	 * @return </br> <b>����</b> ���Ρ </br> <b>����</b> 2011-7-22
	 */
	public static TestNodeMdl getFirstIdleItem(TestMdl lineModel)
	{
		TestNodeContainerMdl parent = (TestNodeContainerMdl) lineModel.getChildren().get(0);
		List<AbtElement> elements = parent.getChildren();
		for (int i = elements.size() - 1; i >= 0; i--)
		{
			TestNodeMdl item = (TestNodeMdl) elements.get(i);
			if (!Utils.isIdleNode(item) && i < elements.size() - 1)
			{
				return Utils.getNextNode(item);
			}
			if (item == elements.get(0) && Utils.isIdleNode(item))
			{
				return item;
			}
		}

		return null;
	}

	/**
	 * ��ȡ��tcMsgBean��ص�node
	 * 
	 * @param lineModel
	 * @param message
	 * @return
	 */
	public static TestNodeMdl getNode(TestMdl lineModel, Message message)
	{
		TestNodeContainerMdl parent = (TestNodeContainerMdl) lineModel.getChildren().get(0);
		List<AbtElement> elements = parent.getChildren();
		for (AbtElement element : elements)
		{
			TestNodeMdl item = (TestNodeMdl) element;
			if (item.getOutgoingConnections() != null && item.getOutgoingConnections().size() > 0)
			{
				for (AbtConnMdl model : item.getOutgoingConnections())
				{
					if (model instanceof MsgConnMdl)
					{
						MsgConnMdl mc = (MsgConnMdl) model;
						if (mc.getTcMsg() != null && mc.getTcMsg().getMsg() != null && mc.getTcMsg().getMsg().equals(message))
						{
							if (message.isPeriodMessage())
							{
								item = Utils.getNextNode(Utils.getNextNode(item));
							}
							return item;
						}
					}
				}
			}

			if (item.getIncomingConnections() != null && item.getIncomingConnections().size() > 0)
			{
				for (AbtConnMdl model : item.getIncomingConnections())
				{
					if (model instanceof MsgConnMdl)
					{
						MsgConnMdl mc = (MsgConnMdl) model;
						if (mc.getTcMsg() != null && mc.getTcMsg().getMsg() != null && mc.getTcMsg().getMsg().equals(message))
						{
							if (message.isPeriodMessage())
							{
								item = Utils.getNextNode(Utils.getNextNode(item));
							}
							return item;
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * �ж�һ��LineItem�Ƿ�Ϊ����
	 * 
	 * @param item
	 * @return
	 */
	public static boolean isIdleNode(TestNodeMdl item)
	{
		if (item.getIncomingConnections().size() == 0 && item.getOutgoingConnections().size() == 0)
		{
			return true;
		}
		return false;
	}

	/**
	 * �ж�һ������ģ�ʹ�����Ƿ�����Ϣ���ǽ�����Ϣ�� ����Ƿ�����Ϣ���Ӳ��Թ��ߵ�������󣩣��򷵻�true
	 * 
	 * @param model
	 * @return
	 */
	public static boolean isSendMessage(MsgConnMdl model)
	{
		if (model == null)
		{
			return false;
		}

		if (model.getTcMsg() != null)
		{
			if (model.getTcMsg().getMsg() != null)
			{
				if (model.getTcMsg().getMsg().isSend())
					return true;
			}
		} else
		{
			TestMdl line = (TestMdl) model.getSource().getParent().getParent();
			if (line instanceof TestToolMdl)
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * �ж�һ����Ϣ�Ƿ�Ϊ������Ϣ
	 * 
	 * @param model
	 * @return
	 */
	public static boolean isRecvMessage(MsgConnMdl model)
	{
		if (model == null)
		{
			return false;
		}
		if (model.getTcMsg() != null)
		{
			if (model.getTcMsg().getMsg() != null)
			{
				if (model.getTcMsg().getMsg().isRecv())
					return true;
			}
		} else
		{
			TestMdl line = (TestMdl) model.getSource().getParent().getParent();
			if (line instanceof TestedObjectMdl)
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * ��ȡ���Թ���
	 * 
	 * @param transModel
	 * @return
	 */
	public static TestToolMdl getTester(ContainerMdl transModel)
	{
		List<AbtElement> elements = transModel.getChildren();
		for (AbtElement element : elements)
		{
			TestMdl model = (TestMdl) element;
			if (model instanceof TestToolMdl)
			{
				return (TestToolMdl) model;
			}
		}
		return null;
	}

	/**
	 * ��ȡ�������
	 * 
	 * @param transModel
	 * @return
	 */
	public static TestedObjectMdl getTested(ContainerMdl transModel)
	{
		List<AbtElement> elements = transModel.getChildren();
		for (AbtElement element : elements)
		{
			TestMdl model = (TestMdl) element;
			if (!(model instanceof TestToolMdl))
			{
				return (TestedObjectMdl) model;
			}
		}
		return null;
	}

	/**
	 * ��ȡһ�������µ����в��Լ� �޸ģ���������ı䣬���Լ��»����в��Լ���������Ҫ�����÷���
	 * 
	 * @param project
	 * @return
	 */
	public static List<IFolder> getTestSuite(IProject project)
	{
		List<IFolder> suites = new ArrayList<IFolder>();
		try
		{
			IResource resources[] = project.members();
			for (IResource resource : resources)
			{
				getSuite(resource, suites);
			}
		} catch (CoreException e)
		{
			e.printStackTrace();
		}
		return suites;
	}

	/**
	 * ��ȡ��Դ�����еĲ��Լ�
	 * 
	 * @param resource ��Դ
	 * @param suiteList ���Լ�����</br> <b>����</b> duyisen </br> <b>����</b> 2011-12-12
	 */
	public static void getSuite(IResource resource, List<IFolder> suiteList)
	{
		if (resource instanceof IFolder && Utils.isTestSuite((IFolder) resource))
		{
			suiteList.add((IFolder) resource);
			try
			{
				for (IResource re : ((IFolder) resource).members())
				{
					getSuite(re, suiteList);
				}
			} catch (CoreException e)
			{

				e.printStackTrace();
			}
		}

	}

	/**
	 * �ж��ļ����Ƿ�Ϊ���Լ���
	 * 
	 * @param folder
	 * @return
	 */
	public static boolean isTestSuite(IFolder folder)
	{
		IFile file = folder.getFile(SPTEConstants.TEST_SUITE_PROPERTY_FILE_NAME);
		if (file.exists())
		{
			return true;
		}
		return false;
	}

	/**
	 * 
	 * ��ȡ��Ŀ�е�����icd�ļ�
	 * 
	 * @return
	 */
	public static List<IFile> getAllICDFilesInProject(IProject project)
	{
		List<IFile> files = new ArrayList<IFile>();
		IFolder folder = project.getFolder(SPTEConstants.ICD_FOLDER_NAME);
		if (folder.exists())
		{
			Utils.getIcdFiles(folder, files);
		}

		return files;
	}

	/**
	 * 
	 * �жϲ��������������ڲ��Լ��ϵ����Ƿ�Ψһ������Ƿ���true
	 * 
	 * @param testCaseName
	 * @return
	 */
	public static boolean isUniqueCaseInFolder(String testCaseName, IFolder folder)
	{
		IFile file = folder.getFile(testCaseName);
		if (file.exists())
		{
			return false;
		}
		return true;
	}

	/**
	 * �жϲ��Թ��ߺͲ��Զ����Ƿ��Ѿ������ã�����Ѿ��������ˣ��򷵻�true�� ���򷵻�false
	 * 
	 * @return
	 */
	public static boolean isSystemReady(TestNodeMdl lineItem)
	{
		ContainerMdl transmodel = (ContainerMdl) lineItem.getParent().getParent().getParent();
		for (AbtElement element : transmodel.getChildren())
		{
			if (element instanceof TestMdl)
			{
				TestMdl model = (TestMdl) element;
				if (model instanceof TestedObjectMdl && model.getEmulator() == null)
				{
					MessageDialog.openError(Utils.getShell(), "������ʾ", "�༭��Ϣ֮ǰ�����ñ������");
					return false;
				}
			}

		}
		return true;
	}

	/**
	 * ����ICD�����µ�����ICD�ļ�
	 * 
	 * @param project </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-4-26
	 */
	public static boolean parseAllICDFilesInProject2(IProject project)
	{
		try
		{
			IResource[] resources = project.members();
			for (IResource resource : resources)
			{
				if (resource instanceof IFolder)
				{
					if (!parseAllICDFilesInFolder((IFolder) resource))
					{
						return false;
					}
				} else if (resource instanceof IFile)
				{
					if (Utils.isIcdFile(resource.getLocation().toFile()))
					{
						File targetFile = resource.getLocation().toFile();
						TemplateEngine.getEngine().parseICD(targetFile);
					}

				}
			}
		} catch (CoreException e)
		{
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * �����ļ����µ�����ICD�ļ�
	 * 
	 * @param folder
	 * @return </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-4-26
	 */
	public static boolean parseAllICDFilesInFolder(IFolder folder)
	{
		try
		{
			IResource[] resources = folder.members();
			for (IResource resource : resources)
			{
				if (resource instanceof IFolder)
				{
					if (!parseAllICDFilesInFolder((IFolder) resource))
					{
						return false;
					}
				} else if (resource instanceof IFile)
				{
					if (Utils.isIcdFile(resource.getLocation().toFile()))
					{
						File targetFile = resource.getLocation().toFile();
						TemplateEngine.getEngine().parseICD(targetFile);
					}
				}
			}
		} catch (CoreException e)
		{
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * �жϵ�ǰ�ı༭���Ƿ��ڱ༭״̬
	 * 
	 * @return
	 */
	public static boolean isEditingStatus()
	{
		SPTEEditor editor = (SPTEEditor) EclipseUtils.getActiveEditor();
		if (editor == null)
		{
			return false;
		}
		return editor.getRootContainerMdl().getStatus() == TestCaseStatus.Editing;
	}

	/**
	 * ��������Ϣ��ӵ�status line����ʾ
	 * 
	 * @param msg
	 */
	public static void addErrorMsgToStatusLine(String msg)
	{
		WorkbenchWindow workbenchWindow = (WorkbenchWindow) PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IStatusLineManager lineManager = workbenchWindow.getStatusLineManager();
		lineManager.setErrorMessage(null);
		lineManager.setErrorMessage(ImageManager.getImage("icons/error.gif"), msg);
		lineManager.update(true);
	}

	/**
	 * ����Ϣ��ӵ�status line����ʾ
	 * 
	 * @param msg
	 */
	public static void addMsgToStatusLine(String msg)
	{
		WorkbenchWindow workbenchWindow = (WorkbenchWindow) PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IStatusLineManager lineManager = workbenchWindow.getStatusLineManager();
		lineManager.setErrorMessage(null);
		lineManager.setMessage(ImageManager.getImage("icons/info_green.gif"), msg);
		lineManager.update(true);
	}

	/**
	 * ��������Ϣ��ӵ�status line����ʾ
	 * 
	 * @param msg
	 */
	public static void addWarningToStatusLine(String msg)
	{
		WorkbenchWindow workbenchWindow = (WorkbenchWindow) PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IStatusLineManager lineManager = workbenchWindow.getStatusLineManager();
		lineManager.setErrorMessage(null);
		lineManager.setMessage(null);
		lineManager.setMessage(ImageManager.getImage("icons/warning.gif"), msg);
		lineManager.update(true);
	}

	/**
	 * ���status line�ϵ���Ϣ
	 * 
	 * 
	 * @author ���Ρ 2011-5-9
	 */
	public static void clearStatusLine()
	{
		WorkbenchWindow workbenchWindow = (WorkbenchWindow) PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IStatusLineManager lineManager = workbenchWindow.getStatusLineManager();
		lineManager.setErrorMessage(null);
		lineManager.setMessage(null);
	}

	/**
	 * ��ȡ��һ����Ϣģ��
	 * 
	 * @param msg
	 * @param root
	 * @return
	 */
	public static MsgConnMdl getNextMsg(MsgConnMdl msg, RootContainerMdl root, boolean needSelectNode)
	{
		ContainerMdl container = (ContainerMdl) root.getChildren().get(0);
		// ��ȡ���Թ����еĲ��Խڵ�
		List<AbtElement> list = container.getChildren().get(0).getChildren().get(0).getChildren();
		if (msg == null)
		{// ��ȡ���������еĵ�һ����Ϣ
			for (AbtElement element : list)
			{
				TestNodeMdl node = (TestNodeMdl) element;
				if (node.getOutgoingConnections() != null && node.getOutgoingConnections().size() != 0)
				{
					MsgConnMdl targetMsg = (MsgConnMdl) node.getOutgoingConnections().get(0);
					if (needSelectNode)
						node.setStatus(TestCaseStatus.Debug);
					return targetMsg;
				} else if (node.getIncomingConnections() != null && node.getIncomingConnections().size() != 0)
				{
					MsgConnMdl targetMsg = (MsgConnMdl) node.getIncomingConnections().get(0);
					if (needSelectNode)
						node.setStatus(TestCaseStatus.Debug);
					return targetMsg;
				}
			}
			MessageDialog.openError(Utils.getShell(), "����", "δ�ҵ�������Ϣ��");
			return null;
		} else
		{
			List<MsgConnMdl> msgs = Utils.getAllMsgs(list);
			int index = msgs.indexOf(msg);
			if (index == msgs.size() - 1)
			{// ���������
				return null;
			}
			return msgs.get(index + 1);
		}
	}

	/**
	 * ��ȡ���е���Ϣ
	 * 
	 * @param list
	 * @return
	 */
	public static List<MsgConnMdl> getAllMsgs(List<AbtElement> list)
	{
		List<MsgConnMdl> msgs = new ArrayList<MsgConnMdl>();
		for (AbtElement element : list)
		{
			TestNodeMdl node = (TestNodeMdl) element;
			if (node.getAllIncomings().size() != 0)
			{
				for (AbtConnMdl mdl : node.getAllIncomings())
				{
					if (mdl instanceof MsgConnMdl && !(mdl instanceof PeriodChildMsgMdl))
					{
						msgs.add((MsgConnMdl) mdl);
					}
				}
			} else if (node.getAllOutgoings().size() != 0)
			{
				for (AbtConnMdl mdl : node.getAllOutgoings())
				{
					if (mdl instanceof MsgConnMdl && !(mdl instanceof PeriodChildMsgMdl))
					{
						msgs.add((MsgConnMdl) mdl);
					}
				}
			}
		}
		return msgs;
	}

	public static List<MsgConnMdl> getAllMsgs(RootContainerMdl root)
	{
		return getAllMsgs(root.getChildren().get(0).getChildren().get(0).getChildren().get(0).getChildren());
	}

	/**
	 * ��ȡ������Ϣ�䣨������������Ϣ����������Ϣ
	 * 
	 * @param staMsg
	 * @param endMsg
	 * @param root
	 * @return
	 */
	public static List<MsgConnMdl> getIntervalMsgs(MsgConnMdl staMsg, MsgConnMdl endMsg, RootContainerMdl root)
	{
		List<MsgConnMdl> list = Utils.getAllMsgs(root.getChildren().get(0).getChildren().get(0).getChildren().get(0).getChildren());
		int index1 = list.indexOf(staMsg);
		int index2 = list.indexOf(endMsg);

		if (index2 >= index1)
		{
			return list.subList(index1, index2 + 1); // �ҿ����䣬�� + 1
		}

		return null;
	}

	/**
	 * ��ȡִ��������������
	 * 
	 * @return </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-4-11
	 */
	public static String getExecutorCommand()
	{
		String executorCmd = CfgPlugin.getDefault().getPreferenceStore().getString(ExecutionPreferencePage.EXCUTOR_COMMAND);
		if (StringUtils.isNull(executorCmd))
		{
			String executorPath = EclipseUtils.getPluginLoaction(DataPlugin.getDefault()).getAbsolutePath();
			StringBuilder sb = new StringBuilder("cmd /c start /b ");
			sb.append(executorPath);
			sb.append(File.separator);
			sb.append("executor");
			sb.append(File.separator);
			sb.append("testParser.exe");
			executorCmd = sb.toString();
		}
		return executorCmd;
	}

	public static Set<String> getSimuObjectsIDs(TestMdl testMdl)
	{
		List<AbtElement> children = testMdl.getChildren();
		TestNodeContainerMdl tnc = (TestNodeContainerMdl) children.get(0);
		children = tnc.getChildren();
		Set<String> set = new HashSet<String>();
		for (AbtElement element : children)
		{
			TestNodeMdl node = (TestNodeMdl) element;
			List<AbtConnMdl> list = node.getOutgoingConnections();
			if (list != null && list.size() == 1)
			{
				AbtConnMdl conn = list.get(0);
				if (conn instanceof MsgConnMdl)
				{
					MsgConnMdl msgConn = (MsgConnMdl) conn;
					SPTEMsg spteMsg = msgConn.getTcMsg();
					if (spteMsg != null)
					{
						ICDMsg icdMsg = spteMsg.getICDMsg();
						Attribute att = icdMsg.getAttribute("sourceFunctionID");
						if (att != null)
						{
							set.add(att.getValue().toString());
						}
					}
				}

			}

			list = node.getIncomingConnections();
			if (list != null && list.size() == 1)
			{
				AbtConnMdl conn = list.get(0);
				if (conn instanceof MsgConnMdl)
				{
					MsgConnMdl msgConn = (MsgConnMdl) conn;
					SPTEMsg spteMsg = msgConn.getTcMsg();
					if (spteMsg != null)
					{
						ICDMsg icdMsg = spteMsg.getICDMsg();
						List<Integer> destIDs = icdMsg.getDestIDs();
						for (Integer destID : destIDs)
						{
							set.add(destID.toString());
						}
					}
				}

			}
		}

		return set;
	}
}