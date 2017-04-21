package com.coretek.spte.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.coretek.common.utils.FileUtils;
import com.coretek.spte.core.reference.ReferingCase;
import com.coretek.spte.core.util.Utils;
import com.coretek.spte.dataCompare.CompareResult;

/**
 * ���Խ��������
 * 
 * @author linxy
 * @date 2011-1-28
 * 
 */
public class TestResultManager
{
	private static TestResultManager	instance;

	public static final String			RESULT_FILE_NAME	= ".result";

	public static final String			RESULT				= "result:";

	public static final String			PROJECT_NAME		= "projectName:";

	public static final String			CASE_NAME			= "casePath:";

	public static final String			TIME_OUT			= "timeout:";

	public static final String			ERROR_VALUE			= "errorValue:";

	public static final String			LOSS				= "loss:";

	public static final String			UNEXPECTED			= "unexpected:";

	public static final String			ICDPATH				= "icd:";

	public static final String			MD5					= "md5:";

	public static final String			DEBUG_FILE_NAME		= ".debugResult";

	private IProject					handleProject;

	private TestResultManager()
	{

	}

	public static TestResultManager getInstance()
	{
		if (null == instance)
		{
			instance = new TestResultManager();
		}
		return instance;
	}

	public IProject getHandleProject()
	{
		return handleProject;
	}

	public void setHandleProject(IProject handleProject)
	{
		this.handleProject = handleProject;
	}

	/**
	 * ������Ե����������ļ�
	 * 
	 * @param testCaseFile
	 * @param timeString
	 * @param kind ����ļ���·����һ���֣�������ʶ��Ϣ��Դ��
	 *            ��"result"��ʾ���Խ����"debugResult"��ʾdebug�Ľ���ȡ�
	 * 
	 *            1/ corrector : Sim.Wang @ 2011-08-02
	 *            (����[kind]�ӿڡ��������ڱ���debug���Խ��ʱ��Щ��������á�)
	 * 
	 */
	public void saveTestCases(ReferingCase testCaseFile, String timeString, String kind)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(File.separator);
		sb.append(kind);
		sb.append(File.separator);
		sb.append(timeString);
		// �������Լ�¼�ļ���
		IPath recordFolderPath = testCaseFile.getProject().getLocation().append(sb.toString());
		String projectName = testCaseFile.getProject().getName();
		IProject pro = testCaseFile.getProject();
		// ����������
		String testCaseGroup = testCaseFile.getTestCase().getFullPath().toOSString().replace("\\", "/");
		testCaseGroup = testCaseGroup.substring(0, testCaseGroup.lastIndexOf(".cas"));
		testCaseGroup = testCaseGroup.substring(testCaseGroup.indexOf(new StringBuilder(File.separator).append(projectName).append(File.separator).toString()) + projectName.length() + 2);

		File recordFolder = recordFolderPath.append(testCaseGroup).toFile();
		if (!recordFolder.exists())
		{
			recordFolder.mkdirs();
		}

		String newTestCasePath = recordFolder.getPath();
		try
		{

			FileUtils.copyFile(testCaseFile.getTestCase().getLocation().toString(), new StringBuilder().append(newTestCasePath).append(File.separator).append(testCaseFile.getTestCase().getName()).toString());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		// ˢ��result�ļ���
		IFolder refreshFolder = pro.getFolder(kind);
		refreshFolder(refreshFolder);

	}

	public void saveDB(String dbPath, String timeString, String kind, IFile testCase)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(testCase.getProject().getLocation().toString());
		sb.append(File.separator);
		sb.append(kind);
		sb.append(File.separator);
		sb.append(timeString);
		String caseName = testCase.getName();
		StringBuilder sb2 = new StringBuilder();
		sb2.append(testCase.getParent().getName()).append(File.separator).append(caseName.substring(0, caseName.length() - testCase.getFileExtension().length() - 1));
		String dbName = dbPath.substring(dbPath.lastIndexOf(File.separator));
		String desFile = sb.append(File.separator).append(sb2.toString()).append(dbName).toString();
		try
		{
			FileUtils.copyFile(dbPath, desFile);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * �����Խ����Ϣ���浽�ļ���
	 * 
	 * @param timeString ����ִ�е�ʱ��
	 * @param kind
	 * @param compareResult �ȽϽ��
	 * @param md5 ���������ļ���md5ֵ
	 */
	public void saveResultFile(String timeString, String kind, CompareResult compareResult, String md5)
	{
		IFile testCaseIFile = compareResult.getTestCaseFile();
		StringBuilder sb = new StringBuilder();
		sb.append(testCaseIFile.getProject().getLocation().toString());
		sb.append(File.separator);
		sb.append(kind);
		sb.append(File.separator);
		sb.append(timeString);
		String projectName = testCaseIFile.getProject().getName();
		String testCaseGroupString = testCaseIFile.getFullPath().toOSString().replace("\\", "/");
		testCaseGroupString = testCaseGroupString.substring(0, testCaseGroupString.lastIndexOf(".cas"));
		testCaseGroupString = testCaseGroupString.substring(testCaseGroupString.indexOf(File.separator + projectName + File.separator) + projectName.length() + 2);
		sb.append(File.separator).append(testCaseGroupString).append(File.separator).append(RESULT_FILE_NAME);

		File resultFile = new File(sb.toString());

		FileWriter fileWriter;

		try
		{
			fileWriter = new FileWriter(resultFile);
			sb = new StringBuilder();
			sb.append(RESULT);
			if (compareResult.isStatus())
			{
				sb.append("true");
			} else
			{
				sb.append("false");
			}
			sb.append("\n");

			sb.append(TIME_OUT);
			sb.append(compareResult.getTimeOut());
			sb.append("\n");

			sb.append(ERROR_VALUE);
			sb.append(compareResult.getErrorValue());
			sb.append("\n");

			sb.append(LOSS);
			sb.append(compareResult.getAllLostCount());
			sb.append("\n");

			sb.append(UNEXPECTED);
			sb.append(compareResult.getUnexpected());
			sb.append("\n");

			sb.append(PROJECT_NAME);
			// �����Ŀ����
			sb.append(projectName);
			sb.append("\n");
			sb.append(CASE_NAME);
			// ��Ӳ������������Ŀ��·��
			sb.append(testCaseIFile.getProjectRelativePath().toOSString());
			sb.append("\n");
			sb.append(ICDPATH);
			// ���ICD�ļ�·����·��
			sb.append(compareResult.getIcdPath());
			sb.append("\n");

			// ������������ļ���md5ֵ
			sb.append(MD5);
			sb.append(md5);
			sb.append("\n");

			fileWriter.write(sb.toString());
			fileWriter.close();

		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * ���ҹ����ռ������в��Լ�¼
	 * 
	 * @return
	 */
	public Map<String, List<CompareResult>> findAllResultInWorkSpace(String type)
	{
		Map<String, List<CompareResult>> allRecords = new HashMap<String, List<CompareResult>>();
		// ��ù����ռ�
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		// ������Ŀ
		IProject[] projects = workspace.getRoot().getProjects();
		// ɨ��������Ŀ�µ�result�ļ��У���ȡ���Լ�¼
		for (IProject project : projects)
		{
			// ���˵�ICD�����̣���Ϊֻ�в��Թ��̲Ż��в��Խ����¼
			if (Utils.isSoftwareTestingProject(project))
			{
				IPath resultPath = project.getLocation().append("/" + type);
				setHandleProject(project);
				// �����Ŀ�´���result�ļ��У�����ȡ���в��Խ��
				if (resultPath.toFile().exists())
				{
					Map<String, List<CompareResult>> records = getTestResultRecords(resultPath.toFile());
					allRecords = combineTestResultRecords(allRecords, records);
				}
			}

		}
		return allRecords;
	}

	private Map<String, List<CompareResult>> getTestResultRecords(File file)
	{
		HashMap<String, List<CompareResult>> testRecords = new HashMap<String, List<CompareResult>>();
		File[] dataFiles = file.listFiles();
		for (File timeFile : dataFiles)
		{
			if (timeFile.isDirectory())
			{
				String time = timeFile.getName();
				File[] files = timeFile.listFiles();
				// ���Խ������
				List<CompareResult> compareResults = new ArrayList<CompareResult>();
				for (File fl : files)
				{
					compareResults.addAll(getTestResultRecordsFormFolder(fl));
				}
				testRecords.put(time, compareResults);
			}
		}
		return testRecords;
	}

	private List<CompareResult> getTestResultRecordsFormFolder(File file)
	{
		List<CompareResult> testRecordList = new ArrayList<CompareResult>();
		if (file.isDirectory())
		{
			if (isResultFolder(file))
			{// ����ǲ��Խ���ļ��У���ȡCompareResult����ӵ�testRecordList��
				testRecordList.add(getCompareResult(file));
			} else
			{// ������ǲ��Խ���ļ��У������ݹ���á�
				File[] files = file.listFiles();
				for (File fl : files)
				{
					testRecordList.addAll(getTestResultRecordsFormFolder(fl));
				}
			}
		}
		return testRecordList;
	}

	private CompareResult getCompareResult(File file)
	{
		CompareResult compareResult = new CompareResult();
		File[] files = file.listFiles();
		for (File fl : files)
		{
			if (!fl.isDirectory())
			{
				String name = fl.getName();
				if (name.endsWith(".db"))
				{
					String dbPath = fl.getPath();
					compareResult.setDbPath(dbPath);
				} else if (name.equals(RESULT_FILE_NAME))
				{// ���Խ���ļ�
					try
					{
						FileReader fr = new FileReader(fl);
						BufferedReader br = new BufferedReader(fr);
						String txt = br.readLine();
						while (txt != null)
						{
							if (txt.startsWith(RESULT))
							{// �ȽϽ��
								if (txt.endsWith("true"))
								{
									compareResult.setStatus(true);
								} else
								{
									compareResult.setStatus(false);
								}
							} else if (txt.startsWith(PROJECT_NAME))
							{
								String proName = txt.substring(txt.indexOf(":") + 1);
								compareResult.setProjectName(proName);
								if (getHandleProject().getName().equals(proName))
								{
									txt = br.readLine();
									if (txt.startsWith(CASE_NAME))
									{
										String casePath = txt.substring(txt.indexOf(":") + 1);
										compareResult.setCasePath(casePath);
										IFile testCase = getHandleProject().getFile(casePath);
										if (testCase.exists())
										{
											compareResult.setTestCaseFile(testCase);
										}
										compareResult.setCaseName(testCase.getName());
									}
								}
							} else if (txt.startsWith(TIME_OUT))
							{
								String size = txt.substring(txt.indexOf(":") + 1);
								compareResult.setTimeOut(Integer.parseInt(size));
							} else if (txt.startsWith(LOSS))
							{
								String size = txt.substring(txt.indexOf(":") + 1);
								compareResult.setAllLostCount(Integer.parseInt(size));
							} else if (txt.startsWith(ERROR_VALUE))
							{
								String size = txt.substring(txt.indexOf(":") + 1);
								compareResult.setErrorValue(Integer.parseInt(size));
							} else if (txt.startsWith(UNEXPECTED))
							{
								String size = txt.substring(txt.indexOf(":") + 1);
								compareResult.setUnexpected(Integer.parseInt(size));
							} else if (txt.startsWith(ICDPATH))
							{
								compareResult.setIcdPath(txt.substring(txt.indexOf(":") + 1));
							} else if (txt.startsWith(MD5))
							{
								compareResult.setMD5(txt.substring(txt.indexOf(":") + 1));
							}
							txt = br.readLine();
						}
						br.close();
					} catch (FileNotFoundException e)
					{

						e.printStackTrace();
					} catch (IOException e)
					{

						e.printStackTrace();
					}
				}
			}
		}

		return compareResult;
	}

	private boolean isResultFolder(File file)
	{
		boolean hasDB = false;
		boolean hasCase = false;
		boolean hasResult = false;
		if (file.isDirectory())
		{
			File[] files = file.listFiles();
			for (File fl : files)
			{
				if (fl.isDirectory())
				{
					return false;
				} else
				{
					String name = fl.getName();
					if (name.equals(RESULT_FILE_NAME))
					{
						hasResult = true;
					} else if (name.endsWith(".cas"))
					{
						hasCase = true;
					} else if (name.endsWith(".db"))
					{
						hasDB = true;
					}
					if (hasDB && hasCase && hasResult)
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * �ϲ�MAP����part�е����ݺϲ���all�У�
	 * 
	 * @param all
	 * @param part
	 * @return all
	 */
	private Map<String, List<CompareResult>> combineTestResultRecords(Map<String, List<CompareResult>> all, Map<String, List<CompareResult>> part)
	{
		// ��part�л��key�ĵ�����
		Iterator<String> iterator = part.keySet().iterator();
		while (iterator.hasNext())
		{
			// ��all�в����Ƿ�����ͬkey�ļ�¼�������кϲ�
			String record = iterator.next();
			List<CompareResult> allBeans = all.get(record);

			if (null == allBeans)
			{
				all.put(record, part.get(record));
			} else
			{
				all.get(record).addAll(part.get(record));
			}

		}
		return all;
	}

	/**
	 * ˢ��ָ���ļ����µ�����
	 * 
	 * @param folder ָ�����ļ���
	 */
	private void refreshFolder(IFolder folder)
	{
		String folderPath = folder.getLocation().toOSString();
		File fileA = new File(folderPath + "/.refresh");
		File fileB = new File(folderPath + "/.refresh_force");

		try
		{
			if (fileA.exists() && fileB.exists())
			{
				fileA.delete();
			} else if (fileA.exists())
			{
				fileA.renameTo(fileB);
			} else if (fileB.exists())
			{
				fileB.renameTo(fileA);
			} else
			{
				fileA.createNewFile();
			}

			folder.refreshLocal(IResource.DEPTH_ONE, null);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void deleteTestResult(String type, String time)
	{
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IProject[] projects = workspace.getRoot().getProjects();
		for (IProject project : projects)
		{
			if (Utils.isSoftwareTestingProject(project))
			{
				IPath resultPath = project.getLocation().append("/" + type).append("/" + time);
				File file = resultPath.toFile();
				if (file.exists())
				{
					deleteFile(file);
				}
			}

		}
	}

	/**
	 * �ݹ�ɾ���ļ�
	 * 
	 * @param file
	 */
	private void deleteFile(File file)
	{
		if (!FileUtils.delete(file))
		{
			MessageDialog.openError(Display.getDefault().getActiveShell(), "����", "ɾ���ļ�" + file.getName() + "ʧ�ܣ�");
		}
	}

}