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
 * 记录比较的结果
 * 
 * @author Sim.Wang 2012-2-1
 */
public class CompareResult
{
	private Map<Long, String>		errorMsgs			= new HashMap<Long, String>();		// 值为非预期的消息描述列表
	private Map<Long, SPTEMsg>		expectedMsgs		= new HashMap<Long, SPTEMsg>();	// 用于保存执行结果消息的对应的期望消息
	private Map<Long, String>		partLossPeriodMsg	= new HashMap<Long, String>();		// 部分丢失的周期消息
	private Map<Long, String>		timeOutMsgs			= new HashMap<Long, String>();		// 超时的消息描述列表
	private Map<Long, String>		unexpectedMsgs		= new HashMap<Long, String>();		// 非预期(多出)的消息描述列表
	private Map<String, String>		lossMsgs			= new HashMap<String, String>();	// 丢失的消息描述列表

	private String					dbPath;												// 数据库路径
	private String					icdPath;												// ICD文件路径
	private IFile					testCaseIfile;											// 标识是哪个用例的比较结果

	private List<Entity>			testCaseMsgList;										// 用例消息列表
	private List<SPTEMsg>			dbSpteMsgList;											// 实际消息列表

	private TestCase				testcase;

	private boolean					iniStatus			= true;

	private int						loss				= 0;

	private int						timeOut				= 0;

	private int						errorValue			= 0;

	private int						unexpected			= 0;

	private Map<Message, Integer>	allLost				= new HashMap<Message, Integer>();

	private int						allLostCount		= 0;								// 所有的丢失消息数目

	private String					md5;													// 测试用例的md5值，用于判断测试用例在执行之后是否被修改过

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
	 * 返回丢失的消息数量,此方法若丢失的消息中包括有周期消息，则此周期消息算作一条丢失
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
	 * 返回超时的消息数量
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
	 * 返回预期值与实际值不匹配的消息数量
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
	 * 返回未预期消息的数量
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
	 * 返回丢失的周期消息
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
	 * 获取所有的丢失消息，Key为丢失的消息，value为该消息的丢失数目
	 * 
	 * @return
	 */
	public Map<Message, Integer> getAllLost()
	{
		return allLost;
	}

	/**
	 * 返回比较结果描述 比较通过时返回字符串："true" 比较未通过时，返回："false@"+错误描述
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
			return "成功";
		} else
		{
			StringBuilder sb = new StringBuilder("失败");
			sb.append("@");
			sb.append("值错误消息条数：").append(errorSize).append("; ");
			sb.append("丢失消息条数：").append(lossSize).append("; ");
			sb.append("超时消息条数：").append(timeOutSize).append("; ");
			sb.append("非预期消息条数：").append(unecpSize);
			return sb.toString();
		}
	}

	/**
	 * 结合比较结果，将实际消息列表封装成Result列表 ！！！ 注意：返回的结果中，未能包含用例中丢失的消息的信息
	 * 如果要得到丢失的消息结果，请调用getLossMsgs()函数
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
	 * 结合比较结果，将SPTEMsg对象封装成Result对象
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
	 * 比较结果是否一致
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
	 * 获取所有的丢失消息数目，周期消息分别计算
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
	 * 错误描述信息
	 */
	public String getErrorDesc()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("丢失个数:");
		sb.append(getAllLostCount());
		sb.append(",超时个数:");
		sb.append(getTimeOut());
		sb.append(",错误值个数:");
		sb.append(getErrorValue());
		sb.append(",未期望个数:");
		sb.append(getUnexpected());
		return sb.toString();
	}

}