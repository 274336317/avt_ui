/************************************************************************
 * Copyright (C) 2000-2012 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.testcase.report;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.template.TemplateUtils;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.core.xml.parser.TestCaseParser;
import com.coretek.spte.dataCompare.CompareResult;
import com.coretek.spte.monitor.ui.preference.StatusInfo;
import com.coretek.spte.testcase.TestCase;

/**
 * @author ZHANG Yi 2012-5-30
 */
public class DescriptionExportJob extends Job
{
	private String				filePath;
	private List<CompareResult>	compareResults;

	/**
	 * @param name </br> <b>Author</b> ZHANG Yi </br> <b>Date</b> 2012-5-30
	 */
	public DescriptionExportJob(String name, String filePath, List<CompareResult> compareResults)
	{
		super(name);
		this.filePath = filePath;
		this.compareResults = compareResults;
	}

	public DescriptionExportJob(String name, List<CompareResult> compareResults)
	{
		super(name);
		this.compareResults = compareResults;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.
	 * IProgressMonitor) <br/> <b>Author</b> ZHANG Yi </br> <b>Date</b>
	 * 2012-5-30
	 */
	@Override
	protected IStatus run(IProgressMonitor monitor)
	{
		monitor.beginTask(Messages.getString("ReportExportJob_STARTEXCUTE") + Messages.getString("ReportExportJob_TESTREPORT_EXPORT"), 100); //$NON-NLS-1$ //$NON-NLS-2$
		Map<String, Object> root = new HashMap<String, Object>();
		monitor.worked(10);
		monitor.subTask("正在准备数据");
		Map<String, List<IFile>> projectMaps = groupCompareResultByProject(); // 将CompareResult按照工程进行分组
		List<Map<String, Object>> projectList = getGroupedProjectList(projectMaps); // 将分组后的工程数据转化为一个List供模板文件使用
		monitor.worked(10);
		monitor.subTask("开始生成测试说明");
		root.put("projectlist", projectList);
		root.put("title", "SPTE");
		IReportExporter exporter = new FreeMarkerReportExporter(root, "description");
		exporter.setFile(new File(filePath));
		try
		{
			exporter.exportReport();
		} catch (ExportReportException e)
		{
			e.printStackTrace();
			monitor.done();
			return new StatusInfo(IStatus.ERROR, e.getMessage());
		}

		monitor.done();
		return Status.OK_STATUS;
	}

	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}

	public void setCompareResults(List<CompareResult> compareResults)
	{
		this.compareResults = compareResults;
	}

	/**
	 * 将CompareResult按照工程进行分组
	 * 
	 * @return </br> <b>Author</b> ZHANG Yi </br> <b>Date</b> 2012-5-31
	 */
	private Map<String, List<IFile>> groupCompareResultByProject()
	{
		Map<String, List<IFile>> projectMaps = new HashMap<String, List<IFile>>();
		for (CompareResult r : compareResults)
		{
			if (!projectMaps.containsKey(r.getTestCaseFile().getProject().getName()))
			{
				List<IFile> testCaseFiles = new ArrayList<IFile>();
				testCaseFiles.add(r.getTestCaseFile());
				projectMaps.put(r.getTestCaseFile().getProject().getName(), testCaseFiles);
			} else
			{
				projectMaps.get(r.getTestCaseFile().getProject().getName()).add(r.getTestCaseFile());
			}
		}
		return projectMaps;
	}

	/**
	 * 将分组后的项目封装为一个List供模板文件使用
	 * 
	 * @param projectMaps
	 * @return </br> <b>Author</b> ZHANG Yi </br> <b>Date</b> 2012-5-31
	 */
	private List<Map<String, Object>> getGroupedProjectList(Map<String, List<IFile>> projectMaps)
	{
		List<Map<String, Object>> projectList = new ArrayList<Map<String, Object>>();
		for (String key : projectMaps.keySet())
		{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("PROJECTNAME", key);
			List<Map<String, String>> caseList = new ArrayList<Map<String, String>>();
			for (IFile file : projectMaps.get(key))
			{
				TestCase testcase = (TestCase) TestCaseParser.getInstance(TemplateUtils.getTestCaseSchemaFile(), file).doParse();
				Map<String, String> caseMap = new HashMap<String, String>();
				String str = "";
				caseMap.put("REQ", StringUtils.isNotNull(str = testcase.getCaseDes().getREQ()) ? str : "");
				caseMap.put("condition", StringUtils.isNotNull(str = testcase.getCaseDes().getCondition()) ? str : "");
				caseMap.put("criterion", StringUtils.isNotNull(str = testcase.getCaseDes().getCriterion()) ? str : "");
				caseMap.put("procedure", StringUtils.isNotNull(str = testcase.getCaseDes().getProcedure()) ? str : "");
				caseMap.put("restriction", StringUtils.isNotNull(str = testcase.getCaseDes().getRestriction()) ? str : "");
				caseMap.put("expected", StringUtils.isNotNull(str = testcase.getCaseDes().getExpected()) ? str : "");
				caseMap.put("input", StringUtils.isNotNull(str = testcase.getCaseDes().getInput()) ? str : "");
				caseMap.put("CASENAME", testcase.getName());
				caseList.add(caseMap);
			}
			map.put("CASELIST", caseList);
			projectList.add(map);
		}
		return projectList;
	}

}
