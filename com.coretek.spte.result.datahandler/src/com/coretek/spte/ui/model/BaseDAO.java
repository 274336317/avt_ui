package com.coretek.spte.ui.model;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import com.coretek.common.logging.LoggingPlugin;
import com.coretek.common.template.ClazzManager;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.template.TemplateEngine;
import com.coretek.common.template.TemplateUtils;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.testcase.Message;

/**
 * 读取数据库
 * 
 * @author Sim.Wang 2012-3-13
 */
public class BaseDAO
{
	private static final Logger	logger			= LoggingPlugin.getLogger(BaseDAO.class.getName());

	public static final String	TIMESTAMP		= "timestamp";
	public static final String	TOPICID			= "topicId";
	public static final String	MSGID			= "msgId";
	public static final String	UUID			= "uuId";
	public static final String	CONTENT			= "content";

	private static final String	queryMaxTime	= "SELECT MAX(timestamp) AS timestamp FROM message";

	protected Connection		connection;

	protected String			icdPath;																// ICD文件路径

	protected ClazzManager		clazzManager;															// 保存的ClazzManager对象

	protected String			daPath;																// 记录数据库路径

	public BaseDAO(String dataPath, String icdPath)
	{
		daPath = new StringBuilder("jdbc:sqlite:").append(dataPath).toString();
		this.icdPath = icdPath;
		this.openDBConnection();
	}

	/**
	 * 获取数据库中的最大消息
	 * 
	 * @return </br>
	 */
	public long getMaxTimeStamp()
	{
		long time = 0L;
		try
		{
			Statement stat = this.connection.createStatement();
			ResultSet rs = stat.executeQuery(queryMaxTime);
			while (rs.next())
			{
				time = rs.getLong("timestamp");
				break;
			}
		} catch (SQLException e)
		{
			LoggingPlugin.logException(logger, e);
		}
		return time;
	}

	private boolean openDBConnection()
	{
		logger.info(new StringBuilder("Starting database connection, the database path is: ").append(daPath).toString());

		try
		{
			// 连接SQLite的JDBC
			Class.forName("org.sqlite.JDBC");

			// 建立数据库的连接
			connection = DriverManager.getConnection(daPath);
		} catch (Exception e)
		{
			LoggingPlugin.logException(logger, e);
			logger.warning("There was exception while creating database connection");
			return false;
		}

		/*
		 * 在后面将数据组装为SPTEMsg对象时，均会用到ClazzManager对象
		 * 所以将通过ICD文件得到ClazzManager对象，并将其保存在全局变量中
		 */
		return createGlazzManager();
	}

	/**
	 * 获取保存的ICD文件路径
	 * 
	 * @return
	 */
	public String getIcdPath()
	{
		if (StringUtils.isNull(icdPath))
		{
			try
			{
				Statement statement = this.connection.createStatement();
				ResultSet resultset = this.connection.createStatement().executeQuery("select * from systemInfo");
				while (resultset.next())
				{
					// ICD文件路径
					icdPath = resultset.getString("icdName");
					break;
				}
				statement.close();
			} catch (SQLException e)
			{
				LoggingPlugin.logException(logger, e);
			}
		}

		return this.icdPath;
	}

	/**
	 * 通过ICD文件得到ClazzManager对象 （如果ICD文件路径没有给出，则去查询表获取）
	 * 
	 * @return
	 */
	private boolean createGlazzManager()
	{
		if (StringUtils.isNull(icdPath))
		{
			try
			{
				Statement statement = this.connection.createStatement();
				ResultSet resultset = this.connection.createStatement().executeQuery("select * from systemInfo");
				while (resultset.next())
				{
					// ICD文件路径由
					icdPath = resultset.getString("icdName");
					break;
				}
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
	 * 从数据库中读取指定时间戳区间的数据
	 * 
	 * @param time
	 * @param length
	 * @return </br>
	 */
	public List<SPTEMsg> read(long time, long length)
	{
		// 创建查询语句
		StringBuffer mySql = new StringBuffer("SELECT * FROM message WHERE ");
		mySql.append(TIMESTAMP).append(" BETWEEN ").append(time).append(" AND ").append(time + length);
		mySql.append(" ORDER BY ").append(TIMESTAMP);

		return executeSqlString(mySql.toString());
	}

	/**
	 * 从数据库查询UUID不为空的数据
	 * 
	 * @return
	 */
	public List<SPTEMsg> getMsgUUIDNotEmpty()
	{
		// 创建查询语句
		StringBuffer mySql = new StringBuffer();
		mySql.append("SELECT * FROM message WHERE ");
		mySql.append(UUID).append(" != ''");
		mySql.append(" ORDER BY ").append(TIMESTAMP);

		return executeSqlString(mySql.toString());
	}

	/**
	 * 获取数据库里的所有消息的种类
	 * 
	 * @return </br>
	 */
	public SPTEMsg[] getAllKindsOfSPTEMsgs()
	{
		List<SPTEMsg> list = executeSqlString("SELECT * FROM message GROUP BY topicId");
		return list.toArray(new SPTEMsg[list.size()]);
	}

	/**
	 * 执行查询语句，将结果返转化为SPTEMsg对象返回
	 * 
	 * @param sqlString
	 * @return
	 */
	public List<SPTEMsg> executeSqlString(String sqlString)
	{
		List<SPTEMsg> spteMsgs = new ArrayList<SPTEMsg>();

		try
		{
			Statement statement = this.connection.createStatement();
			// 查询数据
			ResultSet resultset = statement.executeQuery(sqlString);
			spteMsgs = getSPTEMsgs(resultset);
			statement.close();
		} catch (SQLException e)
		{
			LoggingPlugin.logException(logger, e);
		}

		return spteMsgs;
	}

	/**
	 * 关闭数据库连接
	 */
	public void closeDBConnection()
	{
		logger.info("关闭数据库连接.");

		try
		{
			if (connection != null)
			{
				connection.close();
				connection = null;
			}

		} catch (SQLException e)
		{
			LoggingPlugin.logException(logger, e);
		}
	}

	/**
	 * 通过线程池来处理数据 将从数据库中查询出的数据组装成SPTEMsg对象
	 * 
	 * @param resultSet
	 * @return
	 */
	private List<SPTEMsg> getSPTEMsgs(ResultSet resultSet) throws SQLException
	{
		int processNo = Runtime.getRuntime().availableProcessors();
		ExecutorService pool = Executors.newFixedThreadPool(processNo);
		List<Handler> handlers = new ArrayList<Handler>();
		while (resultSet.next())
		{
			long timestamp = resultSet.getLong(TIMESTAMP);
			byte[] content = resultSet.getBytes(CONTENT);

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
	}
}