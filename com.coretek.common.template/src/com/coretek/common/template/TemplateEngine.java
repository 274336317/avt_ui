/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.coretek.common.logging.LoggingPlugin;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.common.template.parser.TargetParser;
import com.coretek.common.utils.EclipseUtils;
import com.coretek.spte.testcase.MessageBlock;

/**
 * 模板引擎。
 * 
 * @author 孙大巍 2011-12-27
 */
public class TemplateEngine
{

	private static final Logger			logger		= LoggingPlugin.getLogger(TemplateEngine.class.getName());

	private static TemplateEngine		engine;

	// 一级缓存，将对象保存在内存当中
	private Map<Registry, ClazzManager>	firstCache	= new HashMap<Registry, ClazzManager>();

	// 二级缓存，将解析结果保存到硬盘当中
	private SecondCache					secondCache	= new SecondCache();

	private TemplateEngine()
	{

	}

	/**
	 * 获取单位管理器
	 * 
	 * @return
	 */
	public ClazzManager getUnitManager()
	{
		File file = EclipseUtils.getPluginLoaction(TemplateEnginePlugin.getDefault());
		File unitRule = new File(new StringBuilder(file.getAbsolutePath()).append(File.separator).append("rules").append(File.separator).append("unitRules.xml").toString());
		File unitFile = new File(new StringBuilder(file.getAbsolutePath()).append(File.separator).append("rules").append(File.separator).append("unit.xml").toString());

		return this.parse(unitFile, unitRule);
	}

	/**
	 * 获取模板引擎对象
	 * 
	 * @return
	 */
	public synchronized static TemplateEngine getEngine()
	{
		if (engine == null)
			engine = new TemplateEngine();

		return engine;
	}

	/**
	 * 解析ICD文件
	 * 
	 * @param ICDfile
	 * @return
	 */
	public ClazzManager parseICD(File targetXML)
	{
		File file = EclipseUtils.getPluginLoaction(TemplateEnginePlugin.getDefault());
		StringBuilder sb = new StringBuilder(file.getAbsolutePath());
		sb.append(File.separator);
		sb.append("rules");
		sb.append(File.separator);
		sb.append("icdRules.xml");
		File rulesXML = new File(sb.toString());
		if (targetXML == null || !targetXML.exists())
		{
			logger.warning("ICD文件不存在!");
			throw new IllegalArgumentException("ICD文件不存在!");

		}
		if (rulesXML == null || !rulesXML.exists())
		{
			logger.warning("规则文件不存在！");
			throw new IllegalArgumentException("规则文件不存在！");

		}
		Registry registry = new Registry(targetXML, rulesXML);
		// 获取缓存的解析结果
		ClazzManager cm = this.firstCache.get(registry);

		if (cm == null || cm.getAllEntities().size() == 0)
		{
			logger.config("未从一级缓存中查找到ICD对象，进入二级缓存中查找。");
			cm = this.secondCache.getFromCache(registry);
			if (cm == null)
			{
				logger.config("未从二级缓存中查找到ICD对象，进行解析操作。");
				cm = new ClazzManager();
				// 需要解析
				TargetParser parser = new TargetParser(targetXML, rulesXML, cm);
				parser.parse(targetXML);
				// 缓存解析结果
				this.secondCache.cache(registry, cm);
			}

			this.firstCache.put(registry, cm);

		} else
		{
			logger.config("从一级缓存中查找到ICD对象。");
			for (Object obj : this.firstCache.keySet().toArray())
			{
				if (obj instanceof Registry)
				{
					if (registry.equals(obj))
					{
						registry = (Registry) obj;
					}
				}
			}

			// 判断当前的ICD文件是否已被改变，如果改变则重新解析，并删除之前的ICD解析结果
			if (registry.canParsing())
			{
				cm.clear();// 清空以前解析的内容
				logger.config("MD5值不匹配！ICD文件已经被修改，需要进行重新解析！");
				cm = new ClazzManager();
				// 解析目标文件并生成对应的对象集合
				TargetParser parser = new TargetParser(targetXML, rulesXML, cm);
				parser.parse(targetXML);
				Registry newReg = new Registry(registry);
				this.firstCache.put(newReg, cm);
				this.secondCache.cache(newReg, cm);
			}

		}

		return cm;
	}

	/**
	 * 解析测试用例文件
	 * 
	 * @param caseFile
	 * @return
	 */
	public ClazzManager parseCase(File caseFile)
	{
		File file = EclipseUtils.getPluginLoaction(TemplateEnginePlugin.getDefault());
		File icdRule = new File(file.getAbsoluteFile() + File.separator + "rules" + File.separator + "testCaseRules.xml");
		ClazzManager clazzManager = this.parse(caseFile, icdRule);
		List<Entity> list = new ArrayList<Entity>();
		String xpath = "//@uuid";
		XPathExpression xpathExpr = null;
		try
		{
			xpathExpr = XPathFactory.newInstance().newXPath().compile(xpath);
		} catch (XPathExpressionException e)
		{
			e.printStackTrace();
			return null;
		}
		InputStream input = null;
		NodeList nodes = null;
		try
		{
			input = new FileInputStream(caseFile.getAbsoluteFile());
			Object result = xpathExpr.evaluate(new InputSource(input), XPathConstants.NODESET);
			nodes = (NodeList) result;
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return null;
		} catch (XPathExpressionException e)
		{
			e.printStackTrace();
			return null;
		} finally
		{
			try
			{
				if (input != null)
					input.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		for (int i = 0; i < nodes.getLength(); i++)
		{
			Node node = nodes.item(i);
			String uuid = node.getNodeValue();
			XMLBean bean = clazzManager.getByUUID(uuid);
			if (bean != null)
			{
				list.add(bean);
			}

		}
		for (Entity en : clazzManager.getAllEntities())
		{
			if (en instanceof MessageBlock)
			{
				en.setChildren(list);
			}
		}
		return clazzManager;
	}

	/**
	 * 解析目标文件
	 * 
	 * @param targetXML 需要被解析的目标文件
	 * @param rulesXML 解析规则定义文件
	 */
	public ClazzManager parse(File targetXML, File rulesXML)
	{
		if (targetXML == null || !targetXML.exists())
		{
			logger.warning("目标 文件不存在!");
			throw new IllegalArgumentException("目标 文件不存在!!");

		}
		if (rulesXML == null || !rulesXML.exists())
		{
			logger.warning("规则文件不存在！");
			throw new IllegalArgumentException("规则文件不存在！");

		}
		Registry m = new Registry(targetXML, rulesXML);
		// 获取缓存的解析结果
		ClazzManager cm = this.firstCache.get(m);

		if (cm == null || cm.getAllEntities().size() == 0)
		{
			cm = new ClazzManager();
			// 需要解析
			TargetParser parser = new TargetParser(targetXML, rulesXML, cm);
			parser.parse(targetXML);
			this.firstCache.put(m, cm);

		} else
		{
			for (Object obj : this.firstCache.keySet().toArray())
			{
				if (obj instanceof Registry)
				{
					if (m.equals(obj))
					{
						m = (Registry) obj;
					}
				}
			}

			// 判断当前的ICD文件是否已被改变，如果改变则重新解析，并删除之前的ICD解析结果
			if (m.canParsing())
			{
				cm.clear();// 清空以前解析的内容
				cm = new ClazzManager();
				// 解析目标文件并生成对应的对象集合
				TargetParser parser = new TargetParser(targetXML, rulesXML, cm);
				parser.parse(targetXML);
				this.firstCache.put(m, cm);
			}

		}

		return cm;
	}

	/**
	 * 清除掉所有已经被解析出来的对象 </br>
	 */
	public void clear()
	{
		this.firstCache.clear();
	}
}