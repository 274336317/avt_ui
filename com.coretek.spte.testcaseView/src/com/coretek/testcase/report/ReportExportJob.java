/************************************************************************
 * Copyright (C) 2000-2012 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.testcase.report;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.spte.dataCompare.CompareResult;
import com.coretek.spte.monitor.ui.preference.StatusInfo;

/**
 * @author ZHANG Yi 2012-5-24
 */
public class ReportExportJob extends Job
{

	private List<CompareResult>	compareResults;
	private String				filePath;

	/**
	 * @param name </br>
	 */
	public ReportExportJob(String name, String filePath, List<CompareResult> compareResults)
	{
		super(name);
		this.filePath = filePath;
		this.compareResults = compareResults;
	}

	public ReportExportJob(String name, List<CompareResult> compareResults)
	{
		super(name);
		this.compareResults = compareResults;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor)
	{
		monitor.beginTask(Messages.getString("ReportExportJob_STARTEXCUTE") + Messages.getString("ReportExportJob_TESTREPORT_EXPORT"), 100); //$NON-NLS-1$ //$NON-NLS-2$
		Map<String, Object> root = new HashMap<String, Object>();
		monitor.worked(10);
		monitor.subTask("正在准备数据");
		List<Map<String, String>> compareresulttable = getCompareResultTable();
		root.put("compareresulttable", compareresulttable);
		Map<String, Map<String, Object>> projectMaps = groupCompareResultByProject(); // 将CompareResutl按照工程进行分组
		List<Map<String, Object>> projectList = getGroupedProjectList(projectMaps); // 将分组后的工程数据转化为一个List供模板文件使用
		monitor.worked(10);
		monitor.subTask("开始生成报告");
		root.put("projectlist", projectList);
		root.put("title", "SPTE");
		IReportExporter exporter = new FreeMarkerReportExporter(root, "report");
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

	public void setCompareResults(List<CompareResult> compareResults)
	{
		this.compareResults = compareResults;
	}

	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}

	/**
	 * 将CompareResult转化为一个List供模板文件中的表格使用
	 * 
	 * @return
	 */
	private List<Map<String, String>> getCompareResultTable()
	{
		List<Map<String, String>> compareresulttable = new ArrayList<Map<String, String>>();
		for (CompareResult r : compareResults)
		{
			Map<String, String> resultmap = new HashMap<String, String>();
			resultmap.put("project", r.getTestCaseFile().getProject().getName());
			resultmap.put("case", r.getTestCaseFile().getProjectRelativePath().toOSString());
			resultmap.put("result", r.isStatus() ? "成功" : "失败");
			if (!r.isStatus())
			{
				resultmap.put("description", r.getErrorDesc());
			}
			compareresulttable.add(resultmap);
		}
		return compareresulttable;
	}

	/**
	 * 将CompareResutl按照工程进行分组
	 * 
	 * @return
	 */
	private Map<String, Map<String, Object>> groupCompareResultByProject()
	{
		Map<String, Map<String, Object>> projectMaps = new HashMap<String, Map<String, Object>>();
		for (CompareResult r : compareResults)
		{
			if (!projectMaps.containsKey(r.getTestCaseFile().getProject().getName()))
			{
				Map<String, Object> projectMap = new HashMap<String, Object>();
				projectMap.put("PROJECTNAME", r.getTestCaseFile().getProject().getName());
				List<Map<String, String>> errorList = new ArrayList<Map<String, String>>();
				projectMap.put("ERRORLIST", errorList);
				if (r.isStatus())
				{
					projectMap.put("CASESUCCESS", 1);
					projectMap.put("CASEFAIL", 0);
				} else
				{
					projectMap.put("CASESUCCESS", 0);
					projectMap.put("CASEFAIL", 1);
					Map<String, String> tempMap = new HashMap<String, String>();
					tempMap.put(r.getTestCaseFile().getProjectRelativePath().toOSString(), r.getErrorDesc());
					((List<Map<String, String>>) projectMap.get("ERRORLIST")).add(tempMap);
				}
				projectMap.put("CASEALL", ((Integer) projectMap.get("CASESUCCESS")).intValue() + ((Integer) projectMap.get("CASEFAIL")).intValue());
				projectMaps.put(r.getTestCaseFile().getProject().getName(), projectMap);
			} else
			{
				Map<String, Object> projectMap = projectMaps.get(r.getTestCaseFile().getProject().getName());
				if (r.isStatus())
				{
					projectMap.put("CASESUCCESS", ((Integer) projectMap.get("CASESUCCESS")).intValue() + 1);
				} else
				{
					projectMap.put("CASEFAIL", ((Integer) projectMap.get("CASEFAIL")).intValue() + 1);
					List<Map<String, String>> temp = (List<Map<String, String>>) projectMap.get("ERRORLIST");
					Map<String, String> tempMap = new HashMap<String, String>();
					tempMap.put(r.getTestCaseFile().getProjectRelativePath().toOSString(), r.getErrorDesc());
					((List<Map<String, String>>) projectMap.get("ERRORLIST")).add(tempMap);
				}
				projectMap.put("CASEALL", ((Integer) projectMap.get("CASESUCCESS")).intValue() + ((Integer) projectMap.get("CASEFAIL")).intValue());
			}
		}
		return projectMaps;
	}

	/**
	 * 将分组后的项目封装为一个List供模板文件使用
	 * 
	 * @param projectMaps
	 * @return
	 */
	private List<Map<String, Object>> getGroupedProjectList(Map<String, Map<String, Object>> projectMaps)
	{
		List<Map<String, Object>> projectList = new ArrayList<Map<String, Object>>();
		for (String key : projectMaps.keySet())
		{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("PROJECTNAME", key);
			map.put("CASEALL", projectMaps.get(key).get("CASEALL"));
			map.put("CASESUCCESS", projectMaps.get(key).get("CASESUCCESS"));
			map.put("CASEFAIL", projectMaps.get(key).get("CASEFAIL"));
			List<Map<String, String>> caseList = new ArrayList<Map<String, String>>();
			List<Map<String, String>> errorList = (List<Map<String, String>>) projectMaps.get(key).get("ERRORLIST");
			for (Map<String, String> errorMap : errorList)
			{
				for (String caseKey : errorMap.keySet())
				{
					Map<String, String> caseMap = new HashMap<String, String>();
					caseMap.put("CASENAME", caseKey);
					caseMap.put("CASEDESC", errorMap.get(caseKey));
					caseList.add(caseMap);
				}
			}
			map.put("CASELIST", caseList);
			projectList.add(map);
		}
		return projectList;
	}
}
