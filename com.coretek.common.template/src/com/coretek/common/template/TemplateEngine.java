/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
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
 * ģ�����档
 * 
 * @author ���Ρ 2011-12-27
 */
public class TemplateEngine
{

	private static final Logger			logger		= LoggingPlugin.getLogger(TemplateEngine.class.getName());

	private static TemplateEngine		engine;

	// һ�����棬�����󱣴����ڴ浱��
	private Map<Registry, ClazzManager>	firstCache	= new HashMap<Registry, ClazzManager>();

	// �������棬������������浽Ӳ�̵���
	private SecondCache					secondCache	= new SecondCache();

	private TemplateEngine()
	{

	}

	/**
	 * ��ȡ��λ������
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
	 * ��ȡģ���������
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
	 * ����ICD�ļ�
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
			logger.warning("ICD�ļ�������!");
			throw new IllegalArgumentException("ICD�ļ�������!");

		}
		if (rulesXML == null || !rulesXML.exists())
		{
			logger.warning("�����ļ������ڣ�");
			throw new IllegalArgumentException("�����ļ������ڣ�");

		}
		Registry registry = new Registry(targetXML, rulesXML);
		// ��ȡ����Ľ������
		ClazzManager cm = this.firstCache.get(registry);

		if (cm == null || cm.getAllEntities().size() == 0)
		{
			logger.config("δ��һ�������в��ҵ�ICD���󣬽�����������в��ҡ�");
			cm = this.secondCache.getFromCache(registry);
			if (cm == null)
			{
				logger.config("δ�Ӷ��������в��ҵ�ICD���󣬽��н���������");
				cm = new ClazzManager();
				// ��Ҫ����
				TargetParser parser = new TargetParser(targetXML, rulesXML, cm);
				parser.parse(targetXML);
				// ����������
				this.secondCache.cache(registry, cm);
			}

			this.firstCache.put(registry, cm);

		} else
		{
			logger.config("��һ�������в��ҵ�ICD����");
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

			// �жϵ�ǰ��ICD�ļ��Ƿ��ѱ��ı䣬����ı������½�������ɾ��֮ǰ��ICD�������
			if (registry.canParsing())
			{
				cm.clear();// �����ǰ����������
				logger.config("MD5ֵ��ƥ�䣡ICD�ļ��Ѿ����޸ģ���Ҫ�������½�����");
				cm = new ClazzManager();
				// ����Ŀ���ļ������ɶ�Ӧ�Ķ��󼯺�
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
	 * �������������ļ�
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
	 * ����Ŀ���ļ�
	 * 
	 * @param targetXML ��Ҫ��������Ŀ���ļ�
	 * @param rulesXML �����������ļ�
	 */
	public ClazzManager parse(File targetXML, File rulesXML)
	{
		if (targetXML == null || !targetXML.exists())
		{
			logger.warning("Ŀ�� �ļ�������!");
			throw new IllegalArgumentException("Ŀ�� �ļ�������!!");

		}
		if (rulesXML == null || !rulesXML.exists())
		{
			logger.warning("�����ļ������ڣ�");
			throw new IllegalArgumentException("�����ļ������ڣ�");

		}
		Registry m = new Registry(targetXML, rulesXML);
		// ��ȡ����Ľ������
		ClazzManager cm = this.firstCache.get(m);

		if (cm == null || cm.getAllEntities().size() == 0)
		{
			cm = new ClazzManager();
			// ��Ҫ����
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

			// �жϵ�ǰ��ICD�ļ��Ƿ��ѱ��ı䣬����ı������½�������ɾ��֮ǰ��ICD�������
			if (m.canParsing())
			{
				cm.clear();// �����ǰ����������
				cm = new ClazzManager();
				// ����Ŀ���ļ������ɶ�Ӧ�Ķ��󼯺�
				TargetParser parser = new TargetParser(targetXML, rulesXML, cm);
				parser.parse(targetXML);
				this.firstCache.put(m, cm);
			}

		}

		return cm;
	}

	/**
	 * ����������Ѿ������������Ķ��� </br>
	 */
	public void clear()
	{
		this.firstCache.clear();
	}
}