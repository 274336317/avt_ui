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
 * 用schema文件对xml文件进行语法检查，然后解析出xml的内容
 * 
 * @author 孙大巍
 * @date 2010-12-20
 */
public class TestCaseParser extends XMLParserWithSchemaValidation
{

	private static TestCaseParser	parser;

	private static Painter			painter;

	private String					versionId;	// icd文件的版本号

	private static boolean			canChanged;

	private TestCaseParser()
	{

	}

	/**
	 * 获取解析器实例
	 * 
	 * @param schemaFile 用来验证xml的schema文件
	 * @param xmlFile 需要被解析的xml文件
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
	 * 仅仅是为了解析cas文件；不画图时调用
	 * 
	 * @param schemaFile
	 * @param xmlFile
	 * @return </br> <b>作者</b> duyisen </br> <b>日期</b> 2011-12-26
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
				// 设置被测对象列表
				Entity testedObjects = ((TestCase) testCase).getTestedObjects();
				if (canChanged)
					this.firePropertyChange(SPTEConstants.PARSER_EVENT_TESTED_MODULE, testedObjects);

				// 获取消息
				List<Entity> msgList = ((TestCase) testCase).getAllMsgOfTestCase();
				for (Entity msg : msgList)
				{
					if (msg instanceof Message)
					{// 消息
						if (((Message) msg).isPeriodMessage())
						{// 周期消息
							if (((Message) msg).isBackground())
							{// 背景周期消息
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
							{// 非背景周期消息
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
						{// 非周期消息
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
					{// 时间间隔

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
			// 解析完所有节点之后，再画出消息
			this.firePropertyChange(SPTEConstants.PARSER_EVENT_INTERVAL, list.toArray());
		return testCase;

	}
}