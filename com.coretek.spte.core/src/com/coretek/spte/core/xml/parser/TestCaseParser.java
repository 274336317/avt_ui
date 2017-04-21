package com.coretek.spte.core.xml.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.coretek.common.template.ClazzManager;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.template.TemplateEngine;
import com.coretek.common.template.TemplateUtils;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.spte.core.models.BackgroundMsgMdl;
import com.coretek.spte.core.models.MsgConnMdl;
import com.coretek.spte.core.models.ParallelMsgMdl;
import com.coretek.spte.core.models.PeriodParentMsgMdl;
import com.coretek.spte.core.models.RootContainerMdl;
import com.coretek.spte.core.util.Painter;
import com.coretek.spte.core.util.SPTEConstants;
import com.coretek.spte.testcase.Message;
import com.coretek.spte.testcase.TestCase;
import com.coretek.spte.testcase.TimeSpan;

/**
 * ��schema�ļ���xml�ļ������﷨��飬Ȼ�������xml������
 * 
 * @author ���Ρ
 * @date 2010-12-20
 */
public class TestCaseParser extends XMLParserWithSchemaValidation
{

	private static TestCaseParser	parser;

	private static Painter			painter;

	private String					versionId;	// icd�ļ��İ汾��

	private static boolean			canChanged;

	private TestCaseParser()
	{

	}

	/**
	 * ��ȡ������ʵ��
	 * 
	 * @param schemaFile ������֤xml��schema�ļ�
	 * @param xmlFile ��Ҫ��������xml�ļ�
	 * @return
	 */
	public static TestCaseParser getInstance(File schemaFile, IFile xmlFile, RootContainerMdl transmodel)
	{
		if (parser == null)
		{
			parser = new TestCaseParser();
		}
		parser.listeners.removePropertyChangeListener(painter);
		painter = Painter.getInstance(transmodel);
		parser.addPropertyChangeListener(painter);
		parser.setSchemaFile(schemaFile);
		parser.setXmlFile(xmlFile);
		canChanged = true;
		return parser;
	}

	/**
	 * ������Ϊ�˽���cas�ļ�������ͼʱ����
	 * 
	 * @param schemaFile
	 * @param xmlFile
	 * @return </br> <b>����</b> duyisen </br> <b>����</b> 2011-12-26
	 */
	public static TestCaseParser getInstance(File schemaFile, IFile xmlFile)
	{
		if (parser == null)
		{
			parser = new TestCaseParser();
		}
		canChanged = false;
		parser.setSchemaFile(schemaFile);
		parser.setXmlFile(xmlFile);
		return parser;
	}

	@Override
	public Object doParse()
	{
		IProject project = this.xmlFile.getProject();
		Entity testCase = null;
		ClazzManager testCaseClazzManger = TemplateEngine.getEngine().parseCase(this.xmlFile.getLocation().toFile());
		if (null != testCaseClazzManger)
		{
			testCase = testCaseClazzManger.getTestCase();
		} else
		{
			xmlFile = project.getFile(xmlFile.getProjectRelativePath());
			testCaseClazzManger = TemplateEngine.getEngine().parseCase(this.xmlFile.getLocation().toFile());
			testCase = testCaseClazzManger.getTestCase();
		}

		if (null != testCase)
		{
			if (testCase instanceof TestCase)
			{
				String icdPath = TemplateUtils.getICDOfTestCase(testCase);

				IPath path = Path.fromPortableString(icdPath);
				File icdFile = path.toFile();
				ClazzManager icdClazzManger = TemplateEngine.getEngine().parseICD(icdFile);
				// ���ñ�������б�
				Entity testedObjects = ((TestCase) testCase).getTestedObjects();
				if (canChanged)
					this.firePropertyChange(SPTEConstants.PARSER_EVENT_TESTED_MODULE, testedObjects);

				// ��ȡ��Ϣ
				List<Entity> msgList = ((TestCase) testCase).getAllMsgOfTestCase();
				for (Entity msg : msgList)
				{
					if (msg instanceof Message)
					{// ��Ϣ
						if (((Message) msg).isPeriodMessage())
						{// ������Ϣ
							if (((Message) msg).isBackground())
							{// ����������Ϣ
								BackgroundMsgMdl model = new BackgroundMsgMdl();
								SPTEMsg spteMsg = TemplateUtils.getSPTEMsg(icdClazzManger, (Message) msg);
								model.setTcMsg((spteMsg));
								model.setName(((Message) msg).getName());
								List<Object> list = new ArrayList<Object>();
								list.add(project);
								list.add(model);
								if (canChanged)
									this.firePropertyChange(SPTEConstants.PARSER_EVENT_CYCLE, list.toArray());
							} else
							{// �Ǳ���������Ϣ
								PeriodParentMsgMdl model = new PeriodParentMsgMdl();
								SPTEMsg spteMsg = TemplateUtils.getSPTEMsg(icdClazzManger, (Message) msg);
								model.setTcMsg((spteMsg));
								model.setName(((Message) msg).getName());
								List<Object> list = new ArrayList<Object>();
								list.add(project);
								list.add(model);
								if (canChanged)
									this.firePropertyChange(SPTEConstants.PARSER_EVENT_CYCLE, list.toArray());
							}
						} else
						{// ��������Ϣ
							MsgConnMdl model = null;
							if (((Message) msg).isParallel())
							{
								model = new ParallelMsgMdl();
							} else
							{
								model = new MsgConnMdl();
							}
							SPTEMsg spteMsg = TemplateUtils.getSPTEMsg(icdClazzManger, (Message) msg);
							model.setTcMsg((spteMsg));
							model.setName(((Message) msg).getName());
							List<Object> list = new ArrayList<Object>();
							list.add(project);
							list.add(model);
							if (canChanged)
								this.firePropertyChange(SPTEConstants.PARSER_EVENT_MESSAGE, list.toArray());

						}
					} else if (msg instanceof TimeSpan)
					{// ʱ����

					}
				}

			}

		} else
		{
			return testCase;
		}
		List<Object> list = new ArrayList<Object>();
		list.add(project);
		list.add(((TestCase) testCase));
		if (canChanged)
			// ���������нڵ�֮���ٻ�����Ϣ
			this.firePropertyChange(SPTEConstants.PARSER_EVENT_INTERVAL, list.toArray());
		return testCase;

	}
}