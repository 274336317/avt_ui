/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.ui.model;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.resources.ResourcesPlugin;

import com.coretek.common.logging.LoggingPlugin;
import com.coretek.common.template.ClazzManager;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.template.TemplateEngine;
import com.coretek.common.template.TemplateUtils;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.testcase.Message;

/**
 * 数据库查询
 * 
 * @author Sim.Wang 2012-1-15 <br/>
 *         孙大巍 2012-02-06修改 <br/>
 *         将公用数据库连接修改为每次访问数据库时新开一个连接。
 * 
 */
@Deprecated
public class QueryData
{
	private static final Logger	logger;

	static
	{
		logger = LoggingPlugin.getLogger(QueryData.class.getName());
		logger.setLevel(Level.CONFIG);
		try
		{
			// 连接SQLite的JDBC
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e)
		{
			LoggingPlugin.logException(logger, e);
		}
	}

	public static final String	TIMESTAMP	= "timestamp";
	public static final String	TOPICID		= "topicId";
	public static final String	MSGID		= "msgId";
	public static final String	UUID		= "uuId";
	public static final String	CONTENT		= "content";

	private String				connectionString;					// 记录数据库路径
	private String				icdPath;							// 记录ICD文件路径
	private ClazzManager		clazzManager;						// 保存的ClazzManager对象

	private SPTEMsgCache		page		= new SPTEMsgCache();	// 缓存数据放在此对象中

	public QueryData(String dataPath, String IcdPath)
	{
		connectionString = new StringBuilder("jdbc:sqlite:").append(dataPath).toString();
		icdPath = IcdPath;
		clazzManager = TemplateEngine.getEngine().parseICD(new File(IcdPath));

	}

	/**
	 * @return the clazzManager <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-3
	 */
	public ClazzManager getClazzManager()
	{
		return clazzManager;
	}

	/**
	 * 将信息写入系统信息表
	 * 
	 * @param endian 指示大小端</br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-4
	 */
	public void updateSystemInfo(int endian)
	{
		Connection connection = null;
		try
		{
			connection = DriverManager.getConnection(connectionString);
			this.createGlazzManager(connection);
			PreparedStatement ps = connection.prepareStatement("INSERT INTO systemInfo(icdName, icdVersion, toolVersion, targetEdian) VALUES(?,?,?,?)");
			ps.setString(1, this.icdPath);
			ps.setString(2, TemplateUtils.getVerion(this.clazzManager));
			ps.setInt(3, 0);
			ps.setInt(4, 0);
			ps.execute();
			ps.close();
		} catch (SQLException e)
		{
			LoggingPlugin.logException(logger, e);
		} finally
		{
			if (connection != null)
			{
				try
				{
					connection.close();
				} catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 通过ICD文件得到ClazzManager对象 （如果ICD文件路径没有给出，则去查询表获取）
	 * 
	 * @return
	 */
	private boolean createGlazzManager(Connection connection)
	{
		// 如果ICD文件路径没有被作为参数传入，则去数据库查询
		if (StringUtils.isNull(icdPath))
		{
			try
			{
				Statement statement = connection.createStatement();
				ResultSet resultset = statement.executeQuery("select * from systemInfo");
				while (resultset.next())
				{
					// ICD文件路径由：工作空间路径+表中相对路径
					icdPath = new StringBuilder(ResourcesPlugin.getWorkspace().getRoot().getLocation().toPortableString()).append(resultset.getString("icdName")).toString();
					break;
				}
				resultset.close();
				statement.close();
			} catch (SQLException e)
			{
				LoggingPlugin.logException(logger, e);
				return false;
			}
		}

		// 通过ICD文件得到ClazzManager对象
		if (StringUtils.isNotNull(icdPath))
		{
			File icdFile = new File(icdPath);
			clazzManager = TemplateEngine.getEngine().parseICD(icdFile);
		}

		return clazzManager != null;
	}

	/**
	 * 获取满足 时间戳 在指定时间范围内的数据
	 * 
	 * @param time ：起始时间
	 * @param length ：时间跨度
	 * @return
	 */
	public SPTEMsg[] getMessage(long time, long length)
	{
		// 从缓存中读取
		return getMsgFromBufMem(time, length, null, null);
	}

	/**
	 * 获取满足 时间戳 在指定时间范围内 并且 topicId 也在指定范围内的数据
	 * 
	 * @param time : 起始时间
	 * @param length ：时间跨度
	 * @param range : topicId字段的取值范围
	 * @return
	 */
	public SPTEMsg[] getMsgLimitByTpcId(long time, int length, String[] range)
	{
		// 从缓存中读取
		return getMsgFromBufMem(time, length, TOPICID, range);
	}

	/**
	 * 获取满足 时间戳 在指定时间范围内 并且 msgId 也在指定范围内的数据
	 * 
	 * @param time : 起始时间
	 * @param length ：时间跨度
	 * @param range : msgId字段的取值范围
	 * @return
	 */
	public SPTEMsg[] getMsgLimitByMsgId(long time, int length, String[] range)
	{
		// 从缓存中读取
		return getMsgFromBufMem(time, length, MSGID, range);
	}

	/**
	 * 获取数据库里的所有消息的种类
	 * 
	 * @return </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-3-16
	 */
	public SPTEMsg[] getAllKindsOfSPTEMsgs()
	{
		List<SPTEMsg> list = new ArrayList<SPTEMsg>();
		Connection connection = null;
		try
		{
			connection = DriverManager.getConnection(connectionString);
			this.createGlazzManager(connection);
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM message GROUP BY topicId");
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				SPTEMsg spteMsg = this.createSpteMsg(rs.getLong("timestamp"), rs.getBytes("content"));
				list.add(spteMsg);
			}
			ps.close();
		} catch (SQLException e)
		{
			LoggingPlugin.logException(logger, e);
		} finally
		{
			if (connection != null)
			{
				try
				{
					connection.close();
				} catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
		}

		return list.toArray(new SPTEMsg[list.size()]);
	}

	/**
	 * 到缓存中读取数据 如果缓存中的数据不能满足需要，则通过数据库更新缓存 再到缓存中读取数据
	 * 
	 * @param time
	 * @param length
	 * @param field
	 * @param range
	 * @return
	 */
	private SPTEMsg[] getMsgFromBufMem(long time, long length, String field, String[] range)
	{
		// FIXME ADD BY DAVID
		// 此方法可以分裂成4个更小的方法
		List<SPTEMsg> spteMsgs = new ArrayList<SPTEMsg>();

		if (page.getBeginTime() <= time && page.getEndTime() >= (time + length))
		{
			/* 取值区间在缓存中 */

			// 从缓存中取数据
			return getMsgInBufMem(time, length, field, range);

		} else if (time < page.getEndTime() && page.getEndTime() < (time + length))
		{
			/* 取值区间右端超出缓存 */

			// 得到之前的缓存数据
			spteMsgs = page.getSPTEMsgs();
			// 从数据库中添加超出部分数据
			spteMsgs.addAll(getMsgFromDB((page.getEndTime() + 1), (time + length - page.getEndTime() - 1)));

			// 删除多余的数据
			Iterator<SPTEMsg> it = spteMsgs.iterator();
			while (it.hasNext())
			{
				SPTEMsg msg = it.next();
				if (msg.getTimeStamp() < time)
				{
					it.remove();
				} else
				{
					break;
				}
			}

			// 放入缓存
			page.setSPTEMsgs(spteMsgs);
			page.setBeginTime(time);
			page.setEndTime(time + length);

			// 从缓存读取数据
			return getMsgInBufMem(time, length, field, range);

		} else if (time < page.getBeginTime() && page.getBeginTime() < (time + length))
		{
			/* 取值区间左端超出缓存 */

			// 从数据库取得超出部分数据
			spteMsgs = getMsgFromDB(time, page.getBeginTime() - time - 1);
			// 再合并缓存中的数据
			spteMsgs.addAll(page.getSPTEMsgs());

			// 删除多余的数据
			List<SPTEMsg> needDeleteMsgs = new ArrayList<SPTEMsg>();
			for (SPTEMsg sptemsg : spteMsgs)
			{
				if (sptemsg.getTimeStamp() > (time + length))
				{
					needDeleteMsgs.add(sptemsg);
				}
			}
			for (SPTEMsg deletMsg : needDeleteMsgs)
			{
				spteMsgs.remove(deletMsg);
			}

			// 放入缓存
			page.setSPTEMsgs(spteMsgs);
			page.setBeginTime(time);
			page.setEndTime(time + length);

			// 从缓存读取数据
			return getMsgInBufMem(time, length, field, range);

		} else
		{
			/** 取值区间完全没有包含在缓存内 */

			// 重新填充缓存
			spteMsgs = getMsgFromDB(time, length);

			page.setSPTEMsgs(spteMsgs);
			page.setBeginTime(time);
			page.setEndTime(time + length);

			// 从缓存读取数据
			return getMsgInBufMem(time, length, field, range);
		}
	}

	/**
	 * 在缓存中读取满足要求的数据
	 * 
	 * @param time
	 * @param length
	 * @param field
	 * @param range
	 * @return </br> <b>作者</b> Sim.Wang </br> <b>日期</b> 2012-2-22
	 */
	private SPTEMsg[] getMsgInBufMem(long time, long length, String field, String[] range)
	{
		List<SPTEMsg> spteMsgs = new ArrayList<SPTEMsg>();

		for (SPTEMsg sptemsg : page.getSPTEMsgs())
		{
			// 时间范围判断
			if (sptemsg.getTimeStamp() >= time && sptemsg.getTimeStamp() <= (time + length))
			{
				// 指定字段判断
				if (field != null && range != null)
				{
					for (String rg : range)
					{
						if ((field.equals(TOPICID) && rg.equals(sptemsg.getMsg().getTopicId())) || (field.equals(MSGID) && rg.equals(sptemsg.getMsg().getId())))
						{
							spteMsgs.add(sptemsg);
							break;
						}
					}
				} else
				{
					spteMsgs.add(sptemsg);
				}

			} else if (sptemsg.getTimeStamp() > (time + length))
			{
				// 已取完需要的数据，跳出循环
				break;
			}
		}

		return spteMsgs.toArray(new SPTEMsg[spteMsgs.size()]);
	}

	/**
	 * 从数据库查询满足 时间戳 在指定时间范围内 并且传入的另一个字段也在给出范围内的数据
	 * 
	 * @param time ：起始时间
	 * @param length ：时间跨度
	 * @return
	 */
	private List<SPTEMsg> getMsgFromDB(long time, long length)
	{
		Connection connection = null;
		List<SPTEMsg> msgs = new ArrayList<SPTEMsg>();
		try
		{
			connection = DriverManager.getConnection(connectionString);
			// 创建查询语句
			StringBuffer mySql = new StringBuffer("select * from message where ");
			mySql.append(TIMESTAMP).append(" between ").append(time).append(" and ").append(time + length);
			Statement statement = connection.createStatement();
			// 查询数据
			ResultSet resultset = statement.executeQuery(mySql.toString());
			msgs = getSPTEMsgs(resultset);
			statement.close();
		} catch (SQLException e)
		{
			LoggingPlugin.logException(logger, e);
		} finally
		{
			if (connection != null)
			{
				try
				{
					connection.close();
				} catch (SQLException e)
				{
					LoggingPlugin.logException(logger, e);
				}
			}
		}

		return msgs;
	}

	/**
	 * 从数据库查询指定UUID的数据
	 * 
	 * @param uuid ：指定的UUID
	 * @return
	 */
	public List<SPTEMsg> getMsgFromDbByUuid(String uuid)
	{
		Connection connection = null;
		List<SPTEMsg> spteMsgs = new ArrayList<SPTEMsg>();

		try
		{
			connection = DriverManager.getConnection(connectionString);
			// 创建查询语句
			StringBuffer mySql = new StringBuffer();
			mySql.append("select * from message where ");
			mySql.append(UUID).append(" = \"").append(uuid).append("\"");
			Statement statement = connection.createStatement();
			// 查询数据
			ResultSet resultset = statement.executeQuery(mySql.toString());

			// 取出每一条数据，并将它们组装成SPTEMsg对象
			while (resultset.next())
			{
				SPTEMsg stpeMsg = createSpteMsg(resultset.getLong(TIMESTAMP), resultset.getBytes(CONTENT));
				spteMsgs.add(stpeMsg);
			}
			resultset.close();
			statement.close();
		} catch (SQLException e)
		{
			LoggingPlugin.logException(logger, e);
		} finally
		{
			if (connection != null)
			{
				try
				{
					connection.close();
				} catch (SQLException e)
				{
					LoggingPlugin.logException(logger, e);
				}
			}
		}

		return spteMsgs;
	}

	/**
	 * 从数据库查询UUID不为空的数据
	 * 
	 * @return <b>作者</b> Sim.Wang </br> <b>日期</b> 2012-2-1
	 */
	public List<SPTEMsg> getMsgFromDbUuidNotEmpty()
	{
		Connection connection = null;
		List<SPTEMsg> spteMsgs = new ArrayList<SPTEMsg>();

		try
		{
			connection = DriverManager.getConnection(connectionString);
			// 创建查询语句
			StringBuffer mySql = new StringBuffer();
			mySql.append("select * from message where ");
			mySql.append(UUID).append(" != ''");
			Statement statement = connection.createStatement();
			// 查询数据
			ResultSet resultset = statement.executeQuery(mySql.toString());
			spteMsgs = getSPTEMsgs(resultset);

			resultset.close();
			statement.close();
		} catch (SQLException e)
		{
			LoggingPlugin.logException(logger, e);
		} finally
		{
			if (connection != null)
			{
				try
				{
					connection.close();
				} catch (SQLException e)
				{
					LoggingPlugin.logException(logger, e);
				}
			}
		}

		return spteMsgs;
	}

	/**
	 * 通过线程池来处理数据 将从数据库中查询出的数据组装成SPTEMsg对象
	 * 
	 * @param resut
	 * @return
	 */
	private List<SPTEMsg> getSPTEMsgs(ResultSet resut) throws SQLException
	{
		int processNo = Runtime.getRuntime().availableProcessors();
		ExecutorService pool = Executors.newFixedThreadPool(processNo);
		List<Handler> handlers = new ArrayList<Handler>();
		while (resut.next())
		{
			long timestamp = resut.getLong(TIMESTAMP);
			byte[] content = resut.getBytes(CONTENT);

			handlers.add(new Handler(timestamp, content));
		}

		List<Future<SPTEMsg>> futures = null;
		try
		{
			futures = pool.invokeAll(handlers);
		} catch (InterruptedException e)
		{
			LoggingPlugin.logException(logger, e);
		}

		List<SPTEMsg> spteMsgs = new ArrayList<SPTEMsg>();
		if (futures != null)
		{
			for (Future<SPTEMsg> f : futures)
			{
				try
				{
					SPTEMsg s = (SPTEMsg) f.get();
					spteMsgs.add(s);
				} catch (Exception e)
				{
					LoggingPlugin.logException(logger, e);
				}

			}
		}

		pool.shutdown();

		return spteMsgs;
	}

	/**
	 * 先反序列化为Message对象 再组装为SPTEMsg对象
	 * 
	 * @param timestamp
	 * @param content
	 * @return
	 */
	private SPTEMsg createSpteMsg(long timestamp, byte[] content)
	{
		SPTEMsg spteMsg = null;

		// 反序列化,得到一个Message对象
		ByteArrayInputStream byteStream = new ByteArrayInputStream(content);
		try
		{
			ObjectInputStream objStream = new ObjectInputStream(byteStream);
			Message message = (Message) objStream.readObject();
			objStream.close();
			// 组装为一个SPTEMsg对象
			if (message != null)
			{
				spteMsg = TemplateUtils.getSPTEMsg(clazzManager, message);
				spteMsg.setTimeStamp(timestamp);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return spteMsg;
	}

	/**
	 * SPTEMsg对象缓存，用于保存从消息数据库中转换来的SPTEMsg集合。
	 * 
	 * @author Sim.Wang
	 * @date 2012-2-23
	 */
	private static class SPTEMsgCache
	{
		private List<SPTEMsg>	spteMsgs	= new ArrayList<SPTEMsg>();

		// 开始时间，也是最小时间
		private long			beginTime	= -1;

		// 结束时间，也是最大时间
		private long			endTime		= -1;

		public List<SPTEMsg> getSPTEMsgs()
		{
			return spteMsgs;
		}

		public void setSPTEMsgs(List<SPTEMsg> spteMsgs)
		{
			this.spteMsgs = spteMsgs;
		}

		public long getBeginTime()
		{
			return beginTime;
		}

		public void setBeginTime(long fromTime)
		{
			this.beginTime = fromTime;
		}

		public long getEndTime()
		{
			return endTime;
		}

		public void setEndTime(long toTime)
		{
			this.endTime = toTime;
		}
	}

	private class Handler implements Callable<SPTEMsg>
	{
		long	timestamp;
		byte[]	content;

		public Handler(long timestamp, byte[] content)
		{
			this.timestamp = timestamp;
			this.content = content;
		}

		@Override
		public SPTEMsg call() throws Exception
		{
			return createSpteMsg(timestamp, content);
		}

	}
}