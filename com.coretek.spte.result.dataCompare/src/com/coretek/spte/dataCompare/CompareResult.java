package com.coretek.spte.dataCompare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;

import com.coretek.common.template.SPTEMsg;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.spte.cfg.ErrorTypesEnum;
import com.coretek.spte.testcase.Message;
import com.coretek.spte.testcase.TestCase;

/**
 * ��¼�ȽϵĽ��
 * 
 * @author Sim.Wang 2012-2-1
 */
public class CompareResult
{
	private Map<Long, String>		errorMsgs			= new HashMap<Long, String>();		// ֵΪ��Ԥ�ڵ���Ϣ�����б�
	private Map<Long, SPTEMsg>		expectedMsgs		= new HashMap<Long, SPTEMsg>();	// ���ڱ���ִ�н����Ϣ�Ķ�Ӧ��������Ϣ
	private Map<Long, String>		partLossPeriodMsg	= new HashMap<Long, String>();		// ���ֶ�ʧ��������Ϣ
	private Map<Long, String>		timeOutMsgs			= new HashMap<Long, String>();		// ��ʱ����Ϣ�����б�
	private Map<Long, String>		unexpectedMsgs		= new HashMap<Long, String>();		// ��Ԥ��(���)����Ϣ�����б�
	private Map<String, String>		lossMsgs			= new HashMap<String, String>();	// ��ʧ����Ϣ�����б�

	private String					dbPath;												// ���ݿ�·��
	private String					icdPath;												// ICD�ļ�·��
	private IFile					testCaseIfile;											// ��ʶ���ĸ������ıȽϽ��

	private List<Entity>			testCaseMsgList;										// ������Ϣ�б�
	private List<SPTEMsg>			dbSpteMsgList;											// ʵ����Ϣ�б�

	private TestCase				testcase;

	private boolean					iniStatus			= true;

	private int						loss				= 0;

	private int						timeOut				= 0;

	private int						errorValue			= 0;

	private int						unexpected			= 0;

	private Map<Message, Integer>	allLost				= new HashMap<Message, Integer>();

	private int						allLostCount		= 0;								// ���еĶ�ʧ��Ϣ��Ŀ

	private String					md5;													// ����������md5ֵ�������жϲ���������ִ��֮���Ƿ��޸Ĺ�

	private String					projectName;

	private String					casePath;

	private String					caseName;

	public CompareResult()
	{

	}

	public Map<Long, SPTEMsg> getExpectedMsgs()
	{
		return this.expectedMsgs;
	}

	/**
	 * @return the caseName <br/>
	 * 
	 */
	public String getCaseName()
	{
		return caseName;
	}

	/**
	 * @param caseName the caseName to set <br/>
	 * 
	 */
	public void setCaseName(String caseName)
	{
		this.caseName = caseName;
	}

	/**
	 * @return the casePath <br/>
	 * 
	 */
	public String getCasePath()
	{
		return casePath;
	}

	/**
	 * @param casePath the casePath to set <br/>
	 * 
	 */
	public void setCasePath(String casePath)
	{
		this.casePath = casePath;
	}

	/**
	 * @return the projectName <br/>
	 * 
	 */
	public String getProjectName()
	{
		return projectName;
	}

	/**
	 * @param projectName the projectName to set <br/>
	 * 
	 */
	public void setProjectName(String projectName)
	{
		this.projectName = projectName;
	}

	/**
	 * Get the md5 digest value of testCase file
	 * 
	 * @return </br>
	 */
	public String getMD5()
	{
		return this.md5;
	}

	/**
	 * Set the md5 digest value of testCase file
	 * 
	 * @param md5 </br>
	 */
	public void setMD5(String md5)
	{
		this.md5 = md5;
	}

	/**
	 * ���ض�ʧ����Ϣ����,�˷�������ʧ����Ϣ�а�����������Ϣ�����������Ϣ����һ����ʧ
	 * 
	 * @return
	 */
	public int getLoss()
	{
		if (loss == 0)
		{
			loss = lossMsgs.size();
		}
		return loss;
	}

	public void setLoss(int loss)
	{
		this.loss = loss;
	}

	/**
	 * ���س�ʱ����Ϣ����
	 * 
	 * @return
	 */
	public int getTimeOut()
	{
		if (timeOut == 0)
		{
			timeOut = timeOutMsgs.size();
		}
		return timeOut;
	}

	public void setTimeOut(int timeOut)
	{
		this.timeOut = timeOut;
	}

	/**
	 * ����Ԥ��ֵ��ʵ��ֵ��ƥ�����Ϣ����
	 * 
	 * @return
	 */
	public int getErrorValue()
	{
		if (errorValue == 0)
		{
			errorValue = errorMsgs.size();
		}
		return errorValue;
	}

	public void setErrorValue(int errorValue)
	{
		this.errorValue = errorValue;
	}

	/**
	 * ����δԤ����Ϣ������
	 * 
	 * @return
	 */
	public int getUnexpected()
	{
		if (unexpected == 0)
		{
			unexpected = unexpectedMsgs.size();
		}
		return unexpected;
	}

	public void setUnexpected(int unexpected)
	{
		this.unexpected = unexpected;
	}

	public String getDBPath()
	{
		return dbPath;
	}

	public String getIcdPath()
	{
		return icdPath;
	}

	public void setIcdPath(String icdPath)
	{
		this.icdPath = icdPath;
	}

	public void setDbPath(String dbPath)
	{
		this.dbPath = dbPath;
	}

	public IFile getTestCaseFile()
	{
		return testCaseIfile;
	}

	public String getTestCaseFielName()
	{
		return getTestCaseFile().getName();
	}

	public void setTestCaseFile(IFile testCaseIfile)
	{
		this.testCaseIfile = testCaseIfile;
	}

	/**
	 * ���ض�ʧ��������Ϣ
	 * 
	 * @return
	 */
	protected Map<Long, String> getPartLossPeriodMsg()
	{
		return partLossPeriodMsg;
	}

	protected void setPartLossPeriodMsg(Map<Long, String> partLossPeriodMsg)
	{
		this.partLossPeriodMsg = partLossPeriodMsg;
	}

	public List<Entity> getTestCaseMsgList()
	{
		return testCaseMsgList;
	}

	protected void setTestCaseMsgList(List<Entity> testCaseMsgList)
	{
		this.testCaseMsgList = testCaseMsgList;
	}

	public List<SPTEMsg> getDbSpteMsgList()
	{
		return dbSpteMsgList;
	}

	public void setDbSpteMsgList(List<SPTEMsg> dbSpteMsgList)
	{
		this.dbSpteMsgList = dbSpteMsgList;
	}

	protected Map<Long, String> getErrorMsgs()
	{
		return errorMsgs;
	}

	public Map<String, String> getLossMsgs()
	{
		return lossMsgs;
	}

	protected Map<Long, String> getTimeOutMsgs()
	{
		return timeOutMsgs;
	}

	protected Map<Long, String> getUnexpectedMsgs()
	{
		return unexpectedMsgs;
	}

	/**
	 * ��ȡ���еĶ�ʧ��Ϣ��KeyΪ��ʧ����Ϣ��valueΪ����Ϣ�Ķ�ʧ��Ŀ
	 * 
	 * @return
	 */
	public Map<Message, Integer> getAllLost()
	{
		return allLost;
	}

	/**
	 * ���رȽϽ������ �Ƚ�ͨ��ʱ�����ַ�����"true" �Ƚ�δͨ��ʱ�����أ�"false@"+��������
	 * 
	 * @return
	 */
	public String getCompareResultDescribe()
	{
		boolean status;

		int errorSize = errorMsgs.size();
		int lossSize = lossMsgs.size();
		int timeOutSize = timeOutMsgs.size();
		int unecpSize = unexpectedMsgs.size();

		status = ((errorSize + lossSize + timeOutSize + unecpSize) == 0);
		if (status)
		{
			return "�ɹ�";
		} else
		{
			StringBuilder sb = new StringBuilder("ʧ��");
			sb.append("@");
			sb.append("ֵ������Ϣ������").append(errorSize).append("; ");
			sb.append("��ʧ��Ϣ������").append(lossSize).append("; ");
			sb.append("��ʱ��Ϣ������").append(timeOutSize).append("; ");
			sb.append("��Ԥ����Ϣ������").append(unecpSize);
			return sb.toString();
		}
	}

	/**
	 * ��ϱȽϽ������ʵ����Ϣ�б��װ��Result�б� ������ ע�⣺���صĽ���У�δ�ܰ��������ж�ʧ����Ϣ����Ϣ
	 * ���Ҫ�õ���ʧ����Ϣ����������getLossMsgs()����
	 * 
	 * @return
	 */
	public List<Result> getReslutList()
	{
		List<Result> resultList = new ArrayList<Result>();
		for (SPTEMsg spte : dbSpteMsgList)
		{
			resultList.add(getResultBySpteMsg(spte));
		}
		return resultList;
	}

	/**
	 * ��ϱȽϽ������SPTEMsg�����װ��Result����
	 * 
	 * @param spteMsg
	 * @return
	 */
	public Result getResultBySpteMsg(SPTEMsg spteMsg)
	{
		String describe = "";
		ErrorTypesEnum errorType = null;
		long timestamp = spteMsg.getTimeStamp();
		List<ErrorTypesEnum> errors = new ArrayList<ErrorTypesEnum>();
		if (errorMsgs.get(timestamp) != null)
		{
			describe = new StringBuilder(describe).append(errorMsgs.get(timestamp)).append(";").toString();
			errorType = ErrorTypesEnum.WRONGVALUE;
			errors.add(errorType);
		}
		if ((partLossPeriodMsg.get(timestamp)) != null)
		{
			describe = new StringBuilder(describe).append(partLossPeriodMsg.get(timestamp)).append(";").toString();
			errorType = ErrorTypesEnum.LOST;
			errors.add(errorType);
		}
		if ((timeOutMsgs.get(timestamp)) != null)
		{
			describe = new StringBuilder(describe).append(timeOutMsgs.get(timestamp)).append(";").toString();
			errorType = ErrorTypesEnum.TIMEOUT;
			errors.add(errorType);
		}
		if ((unexpectedMsgs.get(timestamp)) != null)
		{
			describe = new StringBuilder(describe).append(unexpectedMsgs.get(timestamp)).append(";").toString();
			errorType = ErrorTypesEnum.UNEXPECTED;
			errors.add(errorType);
		}
		if (spteMsg.getMsg().isPeriodMessage())
		{
			Result result = new Result(errors, null, spteMsg, describe);
			SPTEMsg msg = this.expectedMsgs.get(spteMsg.getTimeStamp());
			result.setExpectedMsg(msg);
			return result;
		}

		return new Result(errors, null, spteMsg, describe);
	}

	/**
	 * �ȽϽ���Ƿ�һ��
	 * 
	 * @return
	 */
	public boolean isStatus()
	{
		return (this.iniStatus && errorMsgs.size() == 0 && lossMsgs.size() == 0 && timeOutMsgs.size() == 0 && unexpectedMsgs.size() == 0);
	}

	/**
	 * @param iniStatus
	 */
	public void setStatus(boolean iniStatus)
	{
		this.iniStatus = iniStatus;
	}

	/**
	 * ��ȡ���еĶ�ʧ��Ϣ��Ŀ��������Ϣ�ֱ����
	 * 
	 * @return
	 */
	public int getAllLostCount()
	{
		Iterator<Integer> iterator = getAllLost().values().iterator();
		while (iterator.hasNext())
		{
			allLostCount += iterator.next().intValue();
		}
		return allLostCount;
	}

	public void setAllLostCount(int allLostCount)
	{
		this.allLostCount = allLostCount;
	}

	public void setTestcase(TestCase testcase)
	{
		this.testcase = testcase;
	}

	public TestCase getTestcase()
	{
		return testcase;
	}

	@Override
	public String toString()
	{
		return getCompareResultDescribe();
	}

	public String getTitle()
	{
		String temp = testCaseIfile.getName();
		return temp.substring(0, temp.indexOf("."));
	}

	public List<String> getLostMsgsID()
	{
		List<String> list = new ArrayList<String>();
		if (lossMsgs == null || lossMsgs.size() == 0)
			return list;
		list.addAll(lossMsgs.keySet());
		return list;
	}

	/**
	 * ����������Ϣ
	 */
	public String getErrorDesc()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("��ʧ����:");
		sb.append(getAllLostCount());
		sb.append(",��ʱ����:");
		sb.append(getTimeOut());
		sb.append(",����ֵ����:");
		sb.append(getErrorValue());
		sb.append(",δ��������:");
		sb.append(getUnexpected());
		return sb.toString();
	}

}