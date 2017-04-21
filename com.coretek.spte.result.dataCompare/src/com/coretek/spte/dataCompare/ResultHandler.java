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
 * 执行结果处理器
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
	 * 比较数据库中的消息与用例中的消息是否一致
	 * 
	 * @param testcase ： 用例
	 * @param dbPath ：数据库路径
	 * @param icdPath ：icd路径
	 * @return
	 */
	public static CompareResult compareTestcaseWithDB(TestCase testcase, String dbPath, String icdPath)
	{
		// 获取用例中的所有消息
		List<Entity> testCaseMsgList = testcase.getAllMsgOfTestCase();
		// 获取DB中UUID不为空的所有消息
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
	 * 比较测试用例中与数据库中的消息是否一致
	 * 
	 * @param testcase 测试用例
	 * @param startUUID 测试用例中起始消息UUID
	 * @param endUUID 测试用例中结束消息UUID
	 * @param dbPath 数据库路径
	 * @param icdPath ICD路径
	 * @return
	 */
	public static CompareResult compareTestcaseWithDB4Debug(TestCase testcase, String startUUID, String endUUID, String dbPath, String icdPath)
	{

		// 获取用例中的指定UUID范围的消息
		List<Entity> testCaseMsgList = testcase.getMsgOfTestCaseByRange(startUUID, endUUID);

		// 获取DB中UUID不为空的所有消息
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
	 * 将用例消息与实际消息进行比较 是从实际消息列表定位光标的下一条开始
	 * 
	 * @param testCaseMsgList : 用例消息列表
	 * @param dbSpteMsgList : 实际消息列表
	 * @param staArgs : 标识变量对象
	 * @return
	 * 
	 *         有待完善或商讨的分支处理： 一、暂未对背景消息作处理
	 *         二、非背景周期消息，如果在实际消息列表中未找到或者找到的条数小于预期值，标记为丢失消息
	 *         三、非背景周期消息，如果在实际消息列表中找到的条数大于预期值，标记为非预期消息
	 */
	public static CompareResult caseListCompareDbList(List<Entity> testCaseMsgList, List<SPTEMsg> dbSpteMsgList, StatuArguments staArgs)
	{

		CompareResult compareRsl = new CompareResult(); // 返回结果对象

		/** 过滤掉实际消息列表中的所有发送消息 */
//		List<SPTEMsg> needCheckTestMsg = filterBackgroundMsg(dbSpteMsgList);
		List<SPTEMsg> needCheckTestMsg = filterSenddMsg(dbSpteMsgList);

		for (Entity msg : testCaseMsgList)
		{

			if (msg instanceof Message)
			{// 消息
				// 如果是发送消息，则不做检查
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
					// 周期消息
					handlePeriod((Message) msg, needCheckTestMsg, staArgs, compareRsl);
				} else
				{
					// 普通消息
					handleMessage((Message) msg, needCheckTestMsg, staArgs, compareRsl);
				}

			}

			// 时间间隔
			if (msg instanceof TimeSpan)
			{
				// 记录下时间间隔值
				staArgs.timespan = ((TimeSpan) msg).getValue();
				// 下条消息要进行超时判断
				staArgs.needTimeoutCheck = true;
			} else
			{
				staArgs.needTimeoutCheck = false;
			}
		}

		return compareRsl;
	}
	
	/**
	 * 背景周期消息的处理
	 * 
	 * @param msg
	 * @param needCheckTestMsg
	 * @param staArgs
	 * @param compareRsl 2
	 */
	private static void handleBackgroundPeriod(Message msg, List<SPTEMsg> needCheckTestMsg, StatuArguments staArgs,
			CompareResult compareRsl) {
		String uuid = ((Message) msg).getUuid();
		
		// 统计收到的此周期消息的条数
		int periodCount = countPeriodTime(uuid, needCheckTestMsg, staArgs.dbSpteLocCursor + 1);
		
		if (periodCount == 0)
		{
			/** 在实际列表中未能找到此背景周期消息 */
			// 将此UUID加入到丢失列表中
			compareRsl.getLossMsgs().put(uuid, CheckInfo.MSG_LOSS.getMsg());

			staArgs.beforFound = false;
			compareRsl.getAllLost().put(msg, msg.getPeriodCount()); // 将此消息添加到丢失消息MAP中

		} else
		{
			// 用来记录上一个周期的实际值(周期中有表达式时，根据前面的值推导)
			Period previousPeriod = new Period();
			
			int duration = ((Message) msg).getPeriodDuration();                          //当前时间
			int amendValue = Integer.parseInt(((Message) msg).getAmendValue());          //发送每条消息的间隔
			
			for (int i = 0; i < periodCount; i++)
			{
				// 从周期消息中获取第i个周期对象
				Period period = getAppointedPeriod(((Message) msg), i + 1, previousPeriod);

				// 从实际列表中得到当前周期对象
				staArgs.dbSpteCurCursor = indexByUuidFirst(uuid, needCheckTestMsg, staArgs.dbSpteLocCursor + 1);
				Period dbPeriod = (Period) needCheckTestMsg.get(staArgs.dbSpteCurCursor).getMsg().getChildren().get(0);

				// 将当前的周期拷贝保存
				try
				{
					previousPeriod = (Period) dbPeriod.clone();
				} catch (CloneNotSupportedException e)
				{
					e.printStackTrace();
				}

				// 1 、超时判断
				// 第一条背景周期消息通过时间间隔判断，后面的周期消息通过周期+延长时间判断
				Long usetime = needCheckTestMsg.get(staArgs.dbSpteCurCursor).getTimeStamp() - staArgs.timestamp;
				if(i == 0 && (staArgs.needTimeoutCheck && staArgs.beforFound))
				{
					if(staArgs.timespan.contains(DOLLAR_VALUE))
					{
						String replace = staArgs.timespan.replaceAll("\\" + DOLLAR_VALUE, usetime.toString());
						String result = (String)ExpResolver.getExpResolver().evaluate(replace);  //用来接收执行结果
						if(!result.equals("1.0"))  //实际值与表达式不匹配
						{
							String info = MessageFormat.format(CheckInfo.MSG_TIMEOUT.getMsg(), staArgs.timespan, usetime);
							compareRsl.getTimeOutMsgs().put(needCheckTestMsg.get(staArgs.dbSpteCurCursor).getTimeStamp(), info);
						}
					}else{
						if(usetime.longValue() > Long.valueOf(staArgs.timespan))  //实际值大于预期值
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
				
				// 2、值的比较
				// 实际周期的值与用例中的不一致
				if (!entityCompare(period, dbPeriod))
				{
					// 将此消息加入到值为非预期消息的列表
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
//					// 如果当前光标比之前定位的光标 向下移动地不只一个单位
//					// 当中的消息认为是多出来的非预期消息
//					for (int j = staArgs.dbSpteLocCursor + 1; j < staArgs.dbSpteCurCursor; j++)
//					{
//						compareRsl.getUnexpectedMsgs().put(needCheckTestMsg.get(j).getTimeStamp(), CheckInfo.MSG_UNEXPECTED.getMsg());
//					}
//
//					staArgs.timestamp = needCheckTestMsg.get(staArgs.dbSpteCurCursor).getTimeStamp();
//					// 将光标移到当前位置
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
	 * 周期消息的处理
	 * 
	 * @param msg
	 * @param needCheckTestMsg
	 * @param staArgs
	 * @param compareRsl 2
	 */
	private static void handlePeriod(Message msg, List<SPTEMsg> needCheckTestMsg, StatuArguments staArgs, CompareResult compareRsl)
	{
		String uuid = ((Message) msg).getUuid();

		// 周期消息
		if (((Message) msg).isBackground())
		{// 背景周期消息
			/** 背景消息处理 */
			handleBackgroundPeriod(msg, needCheckTestMsg, staArgs, compareRsl);

		} else
		{// 非背景周期消息
			// 统计收到的此周期消息的条数
			int periodCount = countPeriodTime(uuid, needCheckTestMsg, staArgs.dbSpteLocCursor + 1);

			if (periodCount == 0)
			{
				/** 在实际列表中未能找到此周期消息 */
				// 将此UUID加入到丢失列表中
				compareRsl.getLossMsgs().put(uuid, CheckInfo.MSG_LOSS.getMsg());

				staArgs.beforFound = false;
				compareRsl.getAllLost().put(msg, msg.getPeriodCount()); // 将此消息添加到丢失消息MAP中

			} else if (periodCount < ((Message) msg).getPeriodCount())
			{
				/** 在实际列表中的周期消息条数不足 */
				// 将此UUID加入到丢失列表中
				String info = MessageFormat.format(CheckInfo.PERIOD_PARTLOSS.getMsg(), ((Message) msg).getPeriodCount(), periodCount);
				compareRsl.getLossMsgs().put(uuid, info);
				compareRsl.getAllLost().put(msg, msg.getPeriodCount() - periodCount); // 将此消息添加到丢失消息MAP中
				// 将实际列表的光标移动到最后一条周期消息处
				staArgs.dbSpteCurCursor = indexByUuidLast(uuid, needCheckTestMsg, staArgs.dbSpteLocCursor + 1);
				for (int i = staArgs.dbSpteLocCursor + 1; i <= staArgs.dbSpteCurCursor; i++)
				{
					// 如果为当前周期消息
					if (needCheckTestMsg.get(i).getMsg().getUuid().equals(uuid))
					{
						// 记录到部分丢失的周期消息列表中
						compareRsl.getPartLossPeriodMsg().put(needCheckTestMsg.get(i).getTimeStamp(), info);
					} else
					{
						// 记录到非预期消息列表中
						compareRsl.getUnexpectedMsgs().put(needCheckTestMsg.get(i).getTimeStamp(), CheckInfo.MSG_UNEXPECTED.getMsg());
					}
				}

				staArgs.dbSpteLocCursor = staArgs.dbSpteCurCursor;
				staArgs.beforFound = false;

			} else if (periodCount > ((Message) msg).getPeriodCount())
			{
				/** 在实际列表中接收到了多余的周期消息 */
				// 将此UUID加入到非预期的UUID列表中
				String info = MessageFormat.format(CheckInfo.PERIOD_PARTEXCESS.getMsg(), ((Message) msg).getPeriodCount(), periodCount);

				// 将实际列表的光标移动到最后一条周期消息处
				staArgs.dbSpteCurCursor = indexByUuidLast(uuid, needCheckTestMsg, staArgs.dbSpteLocCursor + 1);
				for (int i = staArgs.dbSpteLocCursor + 1; i <= staArgs.dbSpteCurCursor; i++)
				{
					compareRsl.getUnexpectedMsgs().put(needCheckTestMsg.get(i).getTimeStamp(), info);
				}

				staArgs.dbSpteLocCursor = staArgs.dbSpteCurCursor;
				staArgs.beforFound = false;

			} else if (periodCount == ((Message) msg).getPeriodCount())
			{
				/** 周期消息条数正确 */
				// 对每条周期消息进行确认

				// 用来记录上一个周期的实际值(周期中有表达式时，根据前面的值推导)
				Period previousPeriod = new Period();

				int duration = ((Message) msg).getPeriodDuration();
				int amendValue = Integer.parseInt(((Message) msg).getAmendValue());

				for (int i = 0; i < periodCount; i++)
				{
					// 从周期消息中获取第i个周期对象
					Period period = getAppointedPeriod(((Message) msg), i + 1, previousPeriod);

					// 从实际列表中得到当前周期对象
					staArgs.dbSpteCurCursor = indexByUuidFirst(uuid, needCheckTestMsg, staArgs.dbSpteLocCursor + 1);
					Period dbPeriod = (Period) needCheckTestMsg.get(staArgs.dbSpteCurCursor).getMsg().getChildren().get(0);

					// 将当前的周期拷贝保存
					try
					{
						previousPeriod = (Period) dbPeriod.clone();
					} catch (CloneNotSupportedException e)
					{
						e.printStackTrace();
					}

					// 1 、超时判断
					// 第一条周期消息通过时间间隔判断，后面的周期消息通过周期+延长时间判断
					/**
					 * 增加对$value的识别,当用户在发送消息后期望的是周期消息，且加入了时间间隔时将无法正确判断第一条，愿意是发送消息被忽略。
					 */
					Long usetime = needCheckTestMsg.get(staArgs.dbSpteCurCursor).getTimeStamp() - staArgs.timestamp;
					if(i == 0 && (staArgs.needTimeoutCheck && staArgs.beforFound))
					{
						if(staArgs.timespan.contains(DOLLAR_VALUE))
						{
							String replace = staArgs.timespan.replaceAll("\\" + DOLLAR_VALUE, usetime.toString());
							String result = (String)ExpResolver.getExpResolver().evaluate(replace);  //用来接收执行结果
							if(!result.equals("1.0"))  //实际值与表达式不匹配
							{
								String info = MessageFormat.format(CheckInfo.MSG_TIMEOUT.getMsg(), staArgs.timespan, usetime);
								compareRsl.getTimeOutMsgs().put(needCheckTestMsg.get(staArgs.dbSpteCurCursor).getTimeStamp(), info);
							}
						}else{
							if(usetime.longValue() > Long.valueOf(staArgs.timespan))  //实际值大于预期值
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
					
					// 2、值的比较
					// 实际周期的值与用例中的不一致
					if (!entityCompare(period, dbPeriod))
					{
						// 将此消息加入到值为非预期消息的列表
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
						// 如果当前光标比之前定位的光标 向下移动地不只一个单位
						// 当中的消息认为是多出来的非预期消息
						for (int j = staArgs.dbSpteLocCursor + 1; j < staArgs.dbSpteCurCursor; j++)
						{
							compareRsl.getUnexpectedMsgs().put(needCheckTestMsg.get(j).getTimeStamp(), CheckInfo.MSG_UNEXPECTED.getMsg());
						}

						staArgs.timestamp = needCheckTestMsg.get(staArgs.dbSpteCurCursor).getTimeStamp();
						// 将光标移到当前位置
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
	 * 非周期消息的处理
	 * 
	 * @param msg
	 * @param needCheckTestMsg
	 * @param staArgs
	 * @param compareRsl 2
	 */
	private static void handleMessage(Message msg, List<SPTEMsg> needCheckTestMsg, StatuArguments staArgs, CompareResult compareRsl)
	{
		String uuid = ((Message) msg).getUuid();// 非周期消息

		// 从之前光标定位的位置开始，遍历实际消息列表
		staArgs.dbSpteCurCursor = indexByUuidFirst(uuid, needCheckTestMsg, staArgs.dbSpteLocCursor + 1);
		if (staArgs.dbSpteCurCursor < 0)
		{
			/** 在实际消息列表中未能找到此消息 */
			// 记录消息到丢失列表中
			compareRsl.getLossMsgs().put(uuid, CheckInfo.MSG_LOSS.getMsg());
			compareRsl.getAllLost().put(msg, 1); // 将此消息添加到丢失消息MAP中，1为丢失数量
			staArgs.beforFound = false;
		} else
		{
			/** 实际消息列表中找到此消息 */

			// 1、超时判断
			/*
			 * 进行超时判断的前提： 前一条消息是时间间隔 前一条非时间间隔消息在实际消息列表中正确找到
			 */
			if (staArgs.needTimeoutCheck && staArgs.beforFound)
			{
				Long usetime = needCheckTestMsg.get(staArgs.dbSpteCurCursor).getTimeStamp() - staArgs.timestamp;
				
				/**
				 * 增加对$value的识别
				 */
				
				if(staArgs.timespan.contains(DOLLAR_VALUE))  
				{
					String replace = staArgs.timespan.replaceAll("\\" + DOLLAR_VALUE, usetime.toString());
					String result = (String)ExpResolver.getExpResolver().evaluate(replace);  //用来接收执行结果
					
					if(!result.equals("1.0"))  //实际值与表达式不匹配
					{
						String info = MessageFormat.format(CheckInfo.MSG_TIMEOUT.getMsg(), staArgs.timespan, usetime);
						compareRsl.getTimeOutMsgs().put(needCheckTestMsg.get(staArgs.dbSpteCurCursor).getTimeStamp(), info);
					}
				} else
				{
					if(usetime.longValue() > Long.valueOf(staArgs.timespan))  //实际值大于预期值
					{
						String info = MessageFormat.format(CheckInfo.MSG_TIMEOUT.getMsg(), staArgs.timespan, usetime);
						compareRsl.getTimeOutMsgs().put(needCheckTestMsg.get(staArgs.dbSpteCurCursor).getTimeStamp(), info);
					}
				}
				
			}

			// 2、用例消息与实际消息进行值的比较
			Message damsg = needCheckTestMsg.get(staArgs.dbSpteCurCursor).getMsg();
			// 消息值不一致
			if (!entityCompare(msg, damsg))
			{
				// 添加到值错误列表
				compareRsl.getErrorMsgs().put(needCheckTestMsg.get(staArgs.dbSpteCurCursor).getTimeStamp(), CheckInfo.MSG_ERRORVALUE.getMsg());
			}

			// 如果当前光标比之前定位的光标 向下移动地不只一个单位
			// 当中的消息认为是多出来的非预期消息
			for (int j = staArgs.dbSpteLocCursor + 1; j < staArgs.dbSpteCurCursor; j++)
			{
				compareRsl.getUnexpectedMsgs().put(needCheckTestMsg.get(j).getTimeStamp(), CheckInfo.MSG_UNEXPECTED.getMsg());
			}

			staArgs.beforFound = true;
			staArgs.timestamp = needCheckTestMsg.get(staArgs.dbSpteCurCursor).getTimeStamp();

			// 将光标移到当前位置
			staArgs.dbSpteLocCursor = staArgs.dbSpteCurCursor;
		}
	}
	
	/**
	 * 两个消息的域进行比较
	 * 
	 * @param entity1 ：可能是Period对象，也可能是Message对象
	 * @param entity2
	 * @return
	 */
	public static boolean entityCompare(Entity modEntity, Entity chkEntity)
	{
		boolean compareResutl = true; // 比较结果

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
	 * 检查field中的value值
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
				// 两个实际值比较
				result = modField.getValue().equals(chkField.getValue());

			} else if (!modField.getValue().contains(DOLLAR_VALUE))
			{
				// 不带变量的表达式
				ExpResolver ex = ExpResolver.getExpResolver();
				String value = (String) ex.evaluate(modField.getValue());
				value = String.valueOf(Double.valueOf(value).intValue());

				result = value.equals(chkField.getValue());

			} else
			{
				// 表达式中有变量"$value"
				ExpResolver ex = ExpResolver.getExpResolver();
				String value = (String) ex.evaluate(modField.getValue().replaceAll("\\" + DOLLAR_VALUE, chkField.getValue()));

				result = value.equals("1.0");
			}
		}

		return result;
	}

	/**
	 * 过滤掉所有背景消息
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
	 * 过滤掉所有发送消息(非预期消息不过滤)
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
	 * 在列表中，从指定的起始位置开始，向后查找指定UUID的消息 返回查找到的第一条消息的位置
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
	 * 在列表中，从指定的起始位置开始，向后查找指定UUID的消息 返回查找到的最后一条消息的位置
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
	 * 在列表中，从指定的起始位置开始，向后查找指定UUID的消息 统计指定UUID消息出现的次数，主要用于周期消息返回次数正确的判断
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
	 * 从周期消息中获取指定的周期的对象 （用上一个周期的实际值替换掉表达式中的"$previous"）
	 * 
	 * @param message ：周期消息
	 * @param periodCounter ：要取的第几个周期
	 * @param previousPeriod ：上一个周期
	 * @return
	 */
	public static Period getAppointedPeriod(Message message, int periodCounter, Period previousPeriod)
	{
		if (!message.isPeriodMessage() || message.getPeriodCount() < periodCounter)
		{
			return null;
		}

		// 是第一条周期消息，则不会有计算表达式
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
	 * 在用例和DB消息进行比较时，会用到很多变量进行标识一些状态 如果直接把这些变量作为接口参数传递的话，会很难维护
	 * 所以定义这么一个对象来统一管理所有的标识变量
	 */
	private static class StatuArguments
	{
		int		dbSpteLocCursor		= -1;		// 定位SPTE消息列表的光标，记录之前定位到的位置
		int		dbSpteCurCursor		= -1;		// 定位SPTE消息列表的光标，记录当前移动的位置

		boolean	needTimeoutCheck	= false;	// 是否需要进行超时检查，取决于有无时间间隔消息
//		boolean	beforFound			= false;	// 标识上一条消息是否找到(周期消息的话，要求完全正确)
		boolean	beforFound			= true;	// 标识上一条消息是否找到(周期消息的话，要求完全正确)
//		long	timespan			= 0l;		// 记录时间间隔
		String  timespan            = "";       // 记录时间间隔
		long	timestamp			= 0l;		// 记录上一条消息的时间戳
	}
}
