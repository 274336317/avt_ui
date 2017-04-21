package com.coretek.spte.dataCompare;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.coretek.common.logging.LoggingPlugin;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.common.utils.ExpResolver;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.testcase.Field;
import com.coretek.spte.testcase.Message;
import com.coretek.spte.testcase.Period;
import com.coretek.spte.testcase.TestCase;
import com.coretek.spte.testcase.TimeSpan;
import com.coretek.spte.ui.model.BaseDAO;

/**
 * ִ�н��������
 * 
 * @author SunDawei 2012-8-10
 */
public class ResultHandler
{
	public static final String	UUID			= "uuId";

	public static final String	TIMESTAMP		= "timeStamp";

	public static final String	DOLLAR_VALUE	= "$value";

	public static final String	DOLLAR_PREVIOUS	= "$previous";

	private static final Logger	logger			= LoggingPlugin.getLogger(ResultHandler.class.getName());

	/**
	 * �Ƚ����ݿ��е���Ϣ�������е���Ϣ�Ƿ�һ��
	 * 
	 * @param testcase �� ����
	 * @param dbPath �����ݿ�·��
	 * @param icdPath ��icd·��
	 * @return
	 */
	public static CompareResult compareTestcaseWithDB(TestCase testcase, String dbPath, String icdPath)
	{
		// ��ȡ�����е�������Ϣ
		List<Entity> testCaseMsgList = testcase.getAllMsgOfTestCase();
		// ��ȡDB��UUID��Ϊ�յ�������Ϣ
		BaseDAO db = new BaseDAO(dbPath, icdPath);
		List<SPTEMsg> dbSpteMsgList = db.getMsgUUIDNotEmpty();
		db.closeDBConnection();

		CompareResult compareRsl = caseListCompareDbList(testCaseMsgList, dbSpteMsgList, new StatuArguments());
		compareRsl.setDbPath(dbPath);
		compareRsl.setIcdPath(icdPath);
		compareRsl.setDbSpteMsgList(dbSpteMsgList);
		compareRsl.setTestCaseMsgList(testCaseMsgList);
		compareRsl.setTestcase(testcase);

		return compareRsl;
	}

	/**
	 * �Ƚϲ��������������ݿ��е���Ϣ�Ƿ�һ��
	 * 
	 * @param testcase ��������
	 * @param startUUID ������������ʼ��ϢUUID
	 * @param endUUID ���������н�����ϢUUID
	 * @param dbPath ���ݿ�·��
	 * @param icdPath ICD·��
	 * @return
	 */
	public static CompareResult compareTestcaseWithDB4Debug(TestCase testcase, String startUUID, String endUUID, String dbPath, String icdPath)
	{

		// ��ȡ�����е�ָ��UUID��Χ����Ϣ
		List<Entity> testCaseMsgList = testcase.getMsgOfTestCaseByRange(startUUID, endUUID);

		// ��ȡDB��UUID��Ϊ�յ�������Ϣ
		BaseDAO db = new BaseDAO(dbPath, icdPath);
		List<SPTEMsg> dbSpteMsgList = db.getMsgUUIDNotEmpty();
		db.closeDBConnection();

		CompareResult compareRsl = caseListCompareDbList(testCaseMsgList, dbSpteMsgList, new StatuArguments());

		compareRsl.setDbPath(dbPath);
		compareRsl.setIcdPath(icdPath);
		compareRsl.setDbSpteMsgList(dbSpteMsgList);
		compareRsl.setTestCaseMsgList(testCaseMsgList);
		compareRsl.setTestcase(testcase);

		return compareRsl;
	}

	/**
	 * ��������Ϣ��ʵ����Ϣ���бȽ� �Ǵ�ʵ����Ϣ�б�λ������һ����ʼ
	 * 
	 * @param testCaseMsgList : ������Ϣ�б�
	 * @param dbSpteMsgList : ʵ����Ϣ�б�
	 * @param staArgs : ��ʶ��������
	 * @return
	 * 
	 *         �д����ƻ����ֵķ�֧���� һ����δ�Ա�����Ϣ������
	 *         �����Ǳ���������Ϣ�������ʵ����Ϣ�б���δ�ҵ������ҵ�������С��Ԥ��ֵ�����Ϊ��ʧ��Ϣ
	 *         �����Ǳ���������Ϣ�������ʵ����Ϣ�б����ҵ�����������Ԥ��ֵ�����Ϊ��Ԥ����Ϣ
	 */
	public static CompareResult caseListCompareDbList(List<Entity> testCaseMsgList, List<SPTEMsg> dbSpteMsgList, StatuArguments staArgs)
	{

		CompareResult compareRsl = new CompareResult(); // ���ؽ������

		/** ���˵�ʵ����Ϣ�б��е����з�����Ϣ */
//		List<SPTEMsg> needCheckTestMsg = filterBackgroundMsg(dbSpteMsgList);
		List<SPTEMsg> needCheckTestMsg = filterSenddMsg(dbSpteMsgList);

		for (Entity msg : testCaseMsgList)
		{

			if (msg instanceof Message)
			{// ��Ϣ
				// ����Ƿ�����Ϣ���������
				try
				{
					if (((Message) msg).isSend())
						continue;
				} catch (Exception e)
				{
					LoggingPlugin.logException(logger, e);
					continue;
				}

				if (((Message) msg).isPeriodMessage())
				{
					// ������Ϣ
					handlePeriod((Message) msg, needCheckTestMsg, staArgs, compareRsl);
				} else
				{
					// ��ͨ��Ϣ
					handleMessage((Message) msg, needCheckTestMsg, staArgs, compareRsl);
				}

			}

			// ʱ����
			if (msg instanceof TimeSpan)
			{
				// ��¼��ʱ����ֵ
				staArgs.timespan = ((TimeSpan) msg).getValue();
				// ������ϢҪ���г�ʱ�ж�
				staArgs.needTimeoutCheck = true;
			} else
			{
				staArgs.needTimeoutCheck = false;
			}
		}

		return compareRsl;
	}
	
	/**
	 * ����������Ϣ�Ĵ���
	 * 
	 * @param msg
	 * @param needCheckTestMsg
	 * @param staArgs
	 * @param compareRsl 2
	 */
	private static void handleBackgroundPeriod(Message msg, List<SPTEMsg> needCheckTestMsg, StatuArguments staArgs,
			CompareResult compareRsl) {
		String uuid = ((Message) msg).getUuid();
		
		// ͳ���յ��Ĵ�������Ϣ������
		int periodCount = countPeriodTime(uuid, needCheckTestMsg, staArgs.dbSpteLocCursor + 1);
		
		if (periodCount == 0)
		{
			/** ��ʵ���б���δ���ҵ��˱���������Ϣ */
			// ����UUID���뵽��ʧ�б���
			compareRsl.getLossMsgs().put(uuid, CheckInfo.MSG_LOSS.getMsg());

			staArgs.beforFound = false;
			compareRsl.getAllLost().put(msg, msg.getPeriodCount()); // ������Ϣ��ӵ���ʧ��ϢMAP��

		} else
		{
			// ������¼��һ�����ڵ�ʵ��ֵ(�������б��ʽʱ������ǰ���ֵ�Ƶ�)
			Period previousPeriod = new Period();
			
			int duration = ((Message) msg).getPeriodDuration();                          //��ǰʱ��
			int amendValue = Integer.parseInt(((Message) msg).getAmendValue());          //����ÿ����Ϣ�ļ��
			
			for (int i = 0; i < periodCount; i++)
			{
				// ��������Ϣ�л�ȡ��i�����ڶ���
				Period period = getAppointedPeriod(((Message) msg), i + 1, previousPeriod);

				// ��ʵ���б��еõ���ǰ���ڶ���
				staArgs.dbSpteCurCursor = indexByUuidFirst(uuid, needCheckTestMsg, staArgs.dbSpteLocCursor + 1);
				Period dbPeriod = (Period) needCheckTestMsg.get(staArgs.dbSpteCurCursor).getMsg().getChildren().get(0);

				// ����ǰ�����ڿ�������
				try
				{
					previousPeriod = (Period) dbPeriod.clone();
				} catch (CloneNotSupportedException e)
				{
					e.printStackTrace();
				}

				// 1 ����ʱ�ж�
				// ��һ������������Ϣͨ��ʱ�����жϣ������������Ϣͨ������+�ӳ�ʱ���ж�
				Long usetime = needCheckTestMsg.get(staArgs.dbSpteCurCursor).getTimeStamp() - staArgs.timestamp;
				if(i == 0 && (staArgs.needTimeoutCheck && staArgs.beforFound))
				{
					if(staArgs.timespan.contains(DOLLAR_VALUE))
					{
						String replace = staArgs.timespan.replaceAll("\\" + DOLLAR_VALUE, usetime.toString());
						String result = (String)ExpResolver.getExpResolver().evaluate(replace);  //��������ִ�н��
						if(!result.equals("1.0"))  //ʵ��ֵ����ʽ��ƥ��
						{
							String info = MessageFormat.format(CheckInfo.MSG_TIMEOUT.getMsg(), staArgs.timespan, usetime);
							compareRsl.getTimeOutMsgs().put(needCheckTestMsg.get(staArgs.dbSpteCurCursor).getTimeStamp(), info);
						}
					}else{
						if(usetime.longValue() > Long.valueOf(staArgs.timespan))  //ʵ��ֵ����Ԥ��ֵ
						{
							String info = MessageFormat.format(CheckInfo.MSG_TIMEOUT.getMsg(), staArgs.timespan, usetime);
							compareRsl.getTimeOutMsgs().put(needCheckTestMsg.get(staArgs.dbSpteCurCursor).getTimeStamp(), info);
						}
					}
					
				}else{
					long limiteTime = duration + amendValue;   
					if (usetime.longValue() > limiteTime)
					{
						String info = MessageFormat.format(CheckInfo.MSG_TIMEOUT.getMsg(), limiteTime, usetime);
						compareRsl.getTimeOutMsgs().put(needCheckTestMsg.get(staArgs.dbSpteCurCursor).getTimeStamp(), info);
					}
				}
				
				// 2��ֵ�ıȽ�
				// ʵ�����ڵ�ֵ�������еĲ�һ��
				if (!entityCompare(period, dbPeriod))
				{
					// ������Ϣ���뵽ֵΪ��Ԥ����Ϣ���б�
					compareRsl.getErrorMsgs().put(needCheckTestMsg.get(staArgs.dbSpteCurCursor).getTimeStamp(), CheckInfo.MSG_ERRORVALUE.getMsg());
				}

//				SPTEMsg spteMsg = new SPTEMsg();
//				try
//				{
//					Message cloned = (Message) msg.clone();
//					cloned.getChildren().clear();
//					cloned.getChildren().add(period);
//					spteMsg.setMsg(cloned);
//					spteMsg.setICDMsg(needCheckTestMsg.get(staArgs.dbSpteCurCursor).getICDMsg());
//					compareRsl.getExpectedMsgs().put(needCheckTestMsg.get(staArgs.dbSpteCurCursor).getTimeStamp(), spteMsg);
//					// �����ǰ����֮ǰ��λ�Ĺ�� �����ƶ��ز�ֻһ����λ
//					// ���е���Ϣ��Ϊ�Ƕ�����ķ�Ԥ����Ϣ
//					for (int j = staArgs.dbSpteLocCursor + 1; j < staArgs.dbSpteCurCursor; j++)
//					{
//						compareRsl.getUnexpectedMsgs().put(needCheckTestMsg.get(j).getTimeStamp(), CheckInfo.MSG_UNEXPECTED.getMsg());
//					}
//
//					staArgs.timestamp = needCheckTestMsg.get(staArgs.dbSpteCurCursor).getTimeStamp();
//					// ������Ƶ���ǰλ��
//					staArgs.dbSpteLocCursor = staArgs.dbSpteCurCursor;
//				} catch (CloneNotSupportedException e)
//				{
//					e.printStackTrace();
//				}
			}

			staArgs.beforFound = true;
		}
		
	}

	/**
	 * ������Ϣ�Ĵ���
	 * 
	 * @param msg
	 * @param needCheckTestMsg
	 * @param staArgs
	 * @param compareRsl 2
	 */
	private static void handlePeriod(Message msg, List<SPTEMsg> needCheckTestMsg, StatuArguments staArgs, CompareResult compareRsl)
	{
		String uuid = ((Message) msg).getUuid();

		// ������Ϣ
		if (((Message) msg).isBackground())
		{// ����������Ϣ
			/** ������Ϣ���� */
			handleBackgroundPeriod(msg, needCheckTestMsg, staArgs, compareRsl);

		} else
		{// �Ǳ���������Ϣ
			// ͳ���յ��Ĵ�������Ϣ������
			int periodCount = countPeriodTime(uuid, needCheckTestMsg, staArgs.dbSpteLocCursor + 1);

			if (periodCount == 0)
			{
				/** ��ʵ���б���δ���ҵ���������Ϣ */
				// ����UUID���뵽��ʧ�б���
				compareRsl.getLossMsgs().put(uuid, CheckInfo.MSG_LOSS.getMsg());

				staArgs.beforFound = false;
				compareRsl.getAllLost().put(msg, msg.getPeriodCount()); // ������Ϣ��ӵ���ʧ��ϢMAP��

			} else if (periodCount < ((Message) msg).getPeriodCount())
			{
				/** ��ʵ���б��е�������Ϣ�������� */
				// ����UUID���뵽��ʧ�б���
				String info = MessageFormat.format(CheckInfo.PERIOD_PARTLOSS.getMsg(), ((Message) msg).getPeriodCount(), periodCount);
				compareRsl.getLossMsgs().put(uuid, info);
				compareRsl.getAllLost().put(msg, msg.getPeriodCount() - periodCount); // ������Ϣ��ӵ���ʧ��ϢMAP��
				// ��ʵ���б�Ĺ���ƶ������һ��������Ϣ��
				staArgs.dbSpteCurCursor = indexByUuidLast(uuid, needCheckTestMsg, staArgs.dbSpteLocCursor + 1);
				for (int i = staArgs.dbSpteLocCursor + 1; i <= staArgs.dbSpteCurCursor; i++)
				{
					// ���Ϊ��ǰ������Ϣ
					if (needCheckTestMsg.get(i).getMsg().getUuid().equals(uuid))
					{
						// ��¼�����ֶ�ʧ��������Ϣ�б���
						compareRsl.getPartLossPeriodMsg().put(needCheckTestMsg.get(i).getTimeStamp(), info);
					} else
					{
						// ��¼����Ԥ����Ϣ�б���
						compareRsl.getUnexpectedMsgs().put(needCheckTestMsg.get(i).getTimeStamp(), CheckInfo.MSG_UNEXPECTED.getMsg());
					}
				}

				staArgs.dbSpteLocCursor = staArgs.dbSpteCurCursor;
				staArgs.beforFound = false;

			} else if (periodCount > ((Message) msg).getPeriodCount())
			{
				/** ��ʵ���б��н��յ��˶����������Ϣ */
				// ����UUID���뵽��Ԥ�ڵ�UUID�б���
				String info = MessageFormat.format(CheckInfo.PERIOD_PARTEXCESS.getMsg(), ((Message) msg).getPeriodCount(), periodCount);

				// ��ʵ���б�Ĺ���ƶ������һ��������Ϣ��
				staArgs.dbSpteCurCursor = indexByUuidLast(uuid, needCheckTestMsg, staArgs.dbSpteLocCursor + 1);
				for (int i = staArgs.dbSpteLocCursor + 1; i <= staArgs.dbSpteCurCursor; i++)
				{
					compareRsl.getUnexpectedMsgs().put(needCheckTestMsg.get(i).getTimeStamp(), info);
				}

				staArgs.dbSpteLocCursor = staArgs.dbSpteCurCursor;
				staArgs.beforFound = false;

			} else if (periodCount == ((Message) msg).getPeriodCount())
			{
				/** ������Ϣ������ȷ */
				// ��ÿ��������Ϣ����ȷ��

				// ������¼��һ�����ڵ�ʵ��ֵ(�������б��ʽʱ������ǰ���ֵ�Ƶ�)
				Period previousPeriod = new Period();

				int duration = ((Message) msg).getPeriodDuration();
				int amendValue = Integer.parseInt(((Message) msg).getAmendValue());

				for (int i = 0; i < periodCount; i++)
				{
					// ��������Ϣ�л�ȡ��i�����ڶ���
					Period period = getAppointedPeriod(((Message) msg), i + 1, previousPeriod);

					// ��ʵ���б��еõ���ǰ���ڶ���
					staArgs.dbSpteCurCursor = indexByUuidFirst(uuid, needCheckTestMsg, staArgs.dbSpteLocCursor + 1);
					Period dbPeriod = (Period) needCheckTestMsg.get(staArgs.dbSpteCurCursor).getMsg().getChildren().get(0);

					// ����ǰ�����ڿ�������
					try
					{
						previousPeriod = (Period) dbPeriod.clone();
					} catch (CloneNotSupportedException e)
					{
						e.printStackTrace();
					}

					// 1 ����ʱ�ж�
					// ��һ��������Ϣͨ��ʱ�����жϣ������������Ϣͨ������+�ӳ�ʱ���ж�
					/**
					 * ���Ӷ�$value��ʶ��,���û��ڷ�����Ϣ����������������Ϣ���Ҽ�����ʱ����ʱ���޷���ȷ�жϵ�һ����Ը���Ƿ�����Ϣ�����ԡ�
					 */
					Long usetime = needCheckTestMsg.get(staArgs.dbSpteCurCursor).getTimeStamp() - staArgs.timestamp;
					if(i == 0 && (staArgs.needTimeoutCheck && staArgs.beforFound))
					{
						if(staArgs.timespan.contains(DOLLAR_VALUE))
						{
							String replace = staArgs.timespan.replaceAll("\\" + DOLLAR_VALUE, usetime.toString());
							String result = (String)ExpResolver.getExpResolver().evaluate(replace);  //��������ִ�н��
							if(!result.equals("1.0"))  //ʵ��ֵ����ʽ��ƥ��
							{
								String info = MessageFormat.format(CheckInfo.MSG_TIMEOUT.getMsg(), staArgs.timespan, usetime);
								compareRsl.getTimeOutMsgs().put(needCheckTestMsg.get(staArgs.dbSpteCurCursor).getTimeStamp(), info);
							}
						}else{
							if(usetime.longValue() > Long.valueOf(staArgs.timespan))  //ʵ��ֵ����Ԥ��ֵ
							{
								String info = MessageFormat.format(CheckInfo.MSG_TIMEOUT.getMsg(), staArgs.timespan, usetime);
								compareRsl.getTimeOutMsgs().put(needCheckTestMsg.get(staArgs.dbSpteCurCursor).getTimeStamp(), info);
							}
						}
						
					}else{
						long limiteTime = duration + amendValue;   
						if (usetime.longValue() > limiteTime)
						{
							String info = MessageFormat.format(CheckInfo.MSG_TIMEOUT.getMsg(), limiteTime, usetime);
							compareRsl.getTimeOutMsgs().put(needCheckTestMsg.get(staArgs.dbSpteCurCursor).getTimeStamp(), info);
						}
					}
					
					// 2��ֵ�ıȽ�
					// ʵ�����ڵ�ֵ�������еĲ�һ��
					if (!entityCompare(period, dbPeriod))
					{
						// ������Ϣ���뵽ֵΪ��Ԥ����Ϣ���б�
						compareRsl.getErrorMsgs().put(needCheckTestMsg.get(staArgs.dbSpteCurCursor).getTimeStamp(), CheckInfo.MSG_ERRORVALUE.getMsg());
					}

					SPTEMsg spteMsg = new SPTEMsg();
					try
					{
						Message cloned = (Message) msg.clone();
						cloned.getChildren().clear();
						cloned.getChildren().add(period);
						spteMsg.setMsg(cloned);
						spteMsg.setICDMsg(needCheckTestMsg.get(staArgs.dbSpteCurCursor).getICDMsg());
						compareRsl.getExpectedMsgs().put(needCheckTestMsg.get(staArgs.dbSpteCurCursor).getTimeStamp(), spteMsg);
						// �����ǰ����֮ǰ��λ�Ĺ�� �����ƶ��ز�ֻһ����λ
						// ���е���Ϣ��Ϊ�Ƕ�����ķ�Ԥ����Ϣ
						for (int j = staArgs.dbSpteLocCursor + 1; j < staArgs.dbSpteCurCursor; j++)
						{
							compareRsl.getUnexpectedMsgs().put(needCheckTestMsg.get(j).getTimeStamp(), CheckInfo.MSG_UNEXPECTED.getMsg());
						}

						staArgs.timestamp = needCheckTestMsg.get(staArgs.dbSpteCurCursor).getTimeStamp();
						// ������Ƶ���ǰλ��
						staArgs.dbSpteLocCursor = staArgs.dbSpteCurCursor;
					} catch (CloneNotSupportedException e)
					{
						e.printStackTrace();
					}
				}

				staArgs.beforFound = true;
			}
		}
	}

	/**
	 * ��������Ϣ�Ĵ���
	 * 
	 * @param msg
	 * @param needCheckTestMsg
	 * @param staArgs
	 * @param compareRsl 2
	 */
	private static void handleMessage(Message msg, List<SPTEMsg> needCheckTestMsg, StatuArguments staArgs, CompareResult compareRsl)
	{
		String uuid = ((Message) msg).getUuid();// ��������Ϣ

		// ��֮ǰ��궨λ��λ�ÿ�ʼ������ʵ����Ϣ�б�
		staArgs.dbSpteCurCursor = indexByUuidFirst(uuid, needCheckTestMsg, staArgs.dbSpteLocCursor + 1);
		if (staArgs.dbSpteCurCursor < 0)
		{
			/** ��ʵ����Ϣ�б���δ���ҵ�����Ϣ */
			// ��¼��Ϣ����ʧ�б���
			compareRsl.getLossMsgs().put(uuid, CheckInfo.MSG_LOSS.getMsg());
			compareRsl.getAllLost().put(msg, 1); // ������Ϣ��ӵ���ʧ��ϢMAP�У�1Ϊ��ʧ����
			staArgs.beforFound = false;
		} else
		{
			/** ʵ����Ϣ�б����ҵ�����Ϣ */

			// 1����ʱ�ж�
			/*
			 * ���г�ʱ�жϵ�ǰ�᣺ ǰһ����Ϣ��ʱ���� ǰһ����ʱ������Ϣ��ʵ����Ϣ�б�����ȷ�ҵ�
			 */
			if (staArgs.needTimeoutCheck && staArgs.beforFound)
			{
				Long usetime = needCheckTestMsg.get(staArgs.dbSpteCurCursor).getTimeStamp() - staArgs.timestamp;
				
				/**
				 * ���Ӷ�$value��ʶ��
				 */
				
				if(staArgs.timespan.contains(DOLLAR_VALUE))  
				{
					String replace = staArgs.timespan.replaceAll("\\" + DOLLAR_VALUE, usetime.toString());
					String result = (String)ExpResolver.getExpResolver().evaluate(replace);  //��������ִ�н��
					
					if(!result.equals("1.0"))  //ʵ��ֵ����ʽ��ƥ��
					{
						String info = MessageFormat.format(CheckInfo.MSG_TIMEOUT.getMsg(), staArgs.timespan, usetime);
						compareRsl.getTimeOutMsgs().put(needCheckTestMsg.get(staArgs.dbSpteCurCursor).getTimeStamp(), info);
					}
				} else
				{
					if(usetime.longValue() > Long.valueOf(staArgs.timespan))  //ʵ��ֵ����Ԥ��ֵ
					{
						String info = MessageFormat.format(CheckInfo.MSG_TIMEOUT.getMsg(), staArgs.timespan, usetime);
						compareRsl.getTimeOutMsgs().put(needCheckTestMsg.get(staArgs.dbSpteCurCursor).getTimeStamp(), info);
					}
				}
				
			}

			// 2��������Ϣ��ʵ����Ϣ����ֵ�ıȽ�
			Message damsg = needCheckTestMsg.get(staArgs.dbSpteCurCursor).getMsg();
			// ��Ϣֵ��һ��
			if (!entityCompare(msg, damsg))
			{
				// ��ӵ�ֵ�����б�
				compareRsl.getErrorMsgs().put(needCheckTestMsg.get(staArgs.dbSpteCurCursor).getTimeStamp(), CheckInfo.MSG_ERRORVALUE.getMsg());
			}

			// �����ǰ����֮ǰ��λ�Ĺ�� �����ƶ��ز�ֻһ����λ
			// ���е���Ϣ��Ϊ�Ƕ�����ķ�Ԥ����Ϣ
			for (int j = staArgs.dbSpteLocCursor + 1; j < staArgs.dbSpteCurCursor; j++)
			{
				compareRsl.getUnexpectedMsgs().put(needCheckTestMsg.get(j).getTimeStamp(), CheckInfo.MSG_UNEXPECTED.getMsg());
			}

			staArgs.beforFound = true;
			staArgs.timestamp = needCheckTestMsg.get(staArgs.dbSpteCurCursor).getTimeStamp();

			// ������Ƶ���ǰλ��
			staArgs.dbSpteLocCursor = staArgs.dbSpteCurCursor;
		}
	}
	
	/**
	 * ������Ϣ������бȽ�
	 * 
	 * @param entity1 ��������Period����Ҳ������Message����
	 * @param entity2
	 * @return
	 */
	public static boolean entityCompare(Entity modEntity, Entity chkEntity)
	{
		boolean compareResutl = true; // �ȽϽ��

		if (modEntity == null || chkEntity == null)
		{
			compareResutl = false;

		} else if (modEntity.getChildren().size() != chkEntity.getChildren().size())
		{
			compareResutl = false;

		} else
		{
			for (int i = 0; i < modEntity.getChildren().size(); i++)
			{
				if (!fieldValueCheck((Field) modEntity.getChildren().get(i), (Field) chkEntity.getChildren().get(i)))
				{
					compareResutl = false;
					break;
				}
			}
		}

		return compareResutl;
	}

	/**
	 * ���field�е�valueֵ
	 * 
	 * @param modField
	 * @param chkField
	 * @return
	 */
	public static boolean fieldValueCheck(Field modField, Field chkField)
	{
		boolean result = true;

		if (modField == null || chkField == null)
		{
			result = false;

		} else if (modField.getChildren().size() != chkField.getChildren().size())
		{
			result = false;

		} else if (modField.getChildren().size() > 0)
		{
			for (int i = 0; i < modField.getChildren().size(); i++)
			{
				if (!fieldValueCheck((Field) modField.getChildren().get(i), (Field) chkField.getChildren().get(i)))
				{
					result = false;
					break;
				}
			}

		} else if (modField.getChildren().size() == 0)
		{
			if (StringUtils.isDouble(modField.getValue()))
			{
				// ����ʵ��ֵ�Ƚ�
				result = modField.getValue().equals(chkField.getValue());

			} else if (!modField.getValue().contains(DOLLAR_VALUE))
			{
				// ���������ı��ʽ
				ExpResolver ex = ExpResolver.getExpResolver();
				String value = (String) ex.evaluate(modField.getValue());
				value = String.valueOf(Double.valueOf(value).intValue());

				result = value.equals(chkField.getValue());

			} else
			{
				// ���ʽ���б���"$value"
				ExpResolver ex = ExpResolver.getExpResolver();
				String value = (String) ex.evaluate(modField.getValue().replaceAll("\\" + DOLLAR_VALUE, chkField.getValue()));

				result = value.equals("1.0");
			}
		}

		return result;
	}

	/**
	 * ���˵����б�����Ϣ
	 * 
	 * @param SpteMsgList
	 * @return
	 */
	public static List<SPTEMsg> filterBackgroundMsg(List<SPTEMsg> SpteMsgList)
	{
		List<SPTEMsg> sptemsgLst = new ArrayList<SPTEMsg>();

		if (SpteMsgList == null)
			return sptemsgLst;

		for (SPTEMsg spte : SpteMsgList)
		{
			if (!spte.getMsg().isBackground())
				sptemsgLst.add(spte);
		}

		return sptemsgLst;
	}

	/**
	 * ���˵����з�����Ϣ(��Ԥ����Ϣ������)
	 * 
	 * @param SpteMsgList
	 * @return
	 */
	public static List<SPTEMsg> filterSenddMsg(List<SPTEMsg> SpteMsgList)
	{
		List<SPTEMsg> sptemsgLst = new ArrayList<SPTEMsg>();

		if (SpteMsgList == null)
			return sptemsgLst;

		for (SPTEMsg spte : SpteMsgList)
		{
			try
			{
				if (StringUtils.isNull(spte.getMsg().getUuid()) || !spte.getMsg().isSend())
					sptemsgLst.add(spte);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return sptemsgLst;
	}

	/**
	 * ���б��У���ָ������ʼλ�ÿ�ʼ��������ָ��UUID����Ϣ ���ز��ҵ��ĵ�һ����Ϣ��λ��
	 * 
	 * @param uuid
	 * @param spteMsgList
	 * @param startLoc
	 * @return
	 */
	public static int indexByUuidFirst(String uuid, List<SPTEMsg> spteMsgList, int startLoc)
	{
		int point = -1;

		if (spteMsgList == null || startLoc < 0)
			return point;

		for (int i = startLoc; i < spteMsgList.size(); i++)
		{
			if (spteMsgList.get(i).getMsg().getUuid().equals(uuid))
			{
				point = i;
				break;
			}
		}

		return point;
	}

	/**
	 * ���б��У���ָ������ʼλ�ÿ�ʼ��������ָ��UUID����Ϣ ���ز��ҵ������һ����Ϣ��λ��
	 * 
	 * @param uuid
	 * @param spteMsgList
	 * @param startLoc
	 * @return
	 */
	public static int indexByUuidLast(String uuid, List<SPTEMsg> spteMsgList, int startLoc)
	{
		int point = -1;

		if (spteMsgList == null || startLoc < 0)
			return point;

		for (int i = startLoc; i < spteMsgList.size(); i++)
		{
			if (spteMsgList.get(i).getMsg().getUuid().equals(uuid))
			{
				point = i;
			}
		}

		return point;
	}

	/**
	 * ���б��У���ָ������ʼλ�ÿ�ʼ��������ָ��UUID����Ϣ ͳ��ָ��UUID��Ϣ���ֵĴ�������Ҫ����������Ϣ���ش�����ȷ���ж�
	 * 
	 * @param uuid
	 * @param spteMsgList
	 * @param startLoc
	 * @return
	 */
	public static int countPeriodTime(String uuid, List<SPTEMsg> spteMsgList, int startLoc)
	{
		int time = 0;

		if (spteMsgList == null || startLoc < 0)
			return time;

		for (int i = startLoc; i < spteMsgList.size(); i++)
		{
			if (spteMsgList.get(i).getMsg().getUuid().equals(uuid))
			{
				time++;
			}
		}

		return time;
	}

	/**
	 * ��������Ϣ�л�ȡָ�������ڵĶ��� ������һ�����ڵ�ʵ��ֵ�滻�����ʽ�е�"$previous"��
	 * 
	 * @param message ��������Ϣ
	 * @param periodCounter ��Ҫȡ�ĵڼ�������
	 * @param previousPeriod ����һ������
	 * @return
	 */
	public static Period getAppointedPeriod(Message message, int periodCounter, Period previousPeriod)
	{
		if (!message.isPeriodMessage() || message.getPeriodCount() < periodCounter)
		{
			return null;
		}

		// �ǵ�һ��������Ϣ���򲻻��м�����ʽ
		if (periodCounter == 1)
		{
			try
			{
				return (Period) ((Period) (message.getChildren().get(0))).clone();
			} catch (CloneNotSupportedException e)
			{
				e.printStackTrace();
				return null;
			}
		}

		Period period = new Period();
		int size = message.getChildren().size();
		for (int t = 0; t < size; t++)
		{
			if (t == (size - 1) || ((Period) message.getChildren().get(t + 1)).getValue() > periodCounter)
			{
				try
				{
					period = (Period) ((Period) (message.getChildren().get(t))).clone();
				} catch (CloneNotSupportedException e)
				{
					e.printStackTrace();
				}
				break;
			}
		}

		for (int i = 0; i < period.getChildren().size(); i++)
		{
			Field field = (Field) period.getChildren().get(i);
			String value = field.getValue();

			if (value.contains(DOLLAR_PREVIOUS))
			{
				String preValue = ((Field) previousPeriod.getChildren().get(i)).getValue();
				field.setValue(String.valueOf(Double.valueOf(ExpResolver.getExpResolver().evaluate(value.replaceAll("\\" + DOLLAR_PREVIOUS, preValue)).toString()).intValue()));
			}
		}

		return period;
	}

	/**
	 * ��������DB��Ϣ���бȽ�ʱ�����õ��ܶ�������б�ʶһЩ״̬ ���ֱ�Ӱ���Щ������Ϊ�ӿڲ������ݵĻ��������ά��
	 * ���Զ�����ôһ��������ͳһ�������еı�ʶ����
	 */
	private static class StatuArguments
	{
		int		dbSpteLocCursor		= -1;		// ��λSPTE��Ϣ�б�Ĺ�꣬��¼֮ǰ��λ����λ��
		int		dbSpteCurCursor		= -1;		// ��λSPTE��Ϣ�б�Ĺ�꣬��¼��ǰ�ƶ���λ��

		boolean	needTimeoutCheck	= false;	// �Ƿ���Ҫ���г�ʱ��飬ȡ��������ʱ������Ϣ
//		boolean	beforFound			= false;	// ��ʶ��һ����Ϣ�Ƿ��ҵ�(������Ϣ�Ļ���Ҫ����ȫ��ȷ)
		boolean	beforFound			= true;	// ��ʶ��һ����Ϣ�Ƿ��ҵ�(������Ϣ�Ļ���Ҫ����ȫ��ȷ)
//		long	timespan			= 0l;		// ��¼ʱ����
		String  timespan            = "";       // ��¼ʱ����
		long	timestamp			= 0l;		// ��¼��һ����Ϣ��ʱ���
	}
}
