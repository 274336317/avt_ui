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
 * ��ȡ���ݿ�
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

	protected String			icdPath;																// ICD�ļ�·��

	protected ClazzManager		clazzManager;															// �����ClazzManager����

	protected String			daPath;																// ��¼���ݿ�·��

	public BaseDAO(String dataPath, String icdPath)
	{
		daPath = new StringBuilder("jdbc:sqlite:").append(dataPath).toString();
		this.icdPath = icdPath;
		this.openDBConnection();
	}

	/**
	 * ��ȡ���ݿ��е������Ϣ
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
			// ����SQLite��JDBC
			Class.forName("org.sqlite.JDBC");

			// �������ݿ������
			connection = DriverManager.getConnection(daPath);
		} catch (Exception e)
		{
			LoggingPlugin.logException(logger, e);
			logger.warning("There was exception while creating database connection");
			return false;
		}

		/*
		 * �ں��潫������װΪSPTEMsg����ʱ�������õ�ClazzManager����
		 * ���Խ�ͨ��ICD�ļ��õ�ClazzManager���󣬲����䱣����ȫ�ֱ�����
		 */
		return createGlazzManager();
	}

	/**
	 * ��ȡ�����ICD�ļ�·��
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
					// ICD�ļ�·��
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
	 * ͨ��ICD�ļ��õ�ClazzManager���� �����ICD�ļ�·��û�и�������ȥ��ѯ���ȡ��
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
					// ICD�ļ�·����
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

		// ͨ��ICD�ļ��õ�ClazzManager����
		if (StringUtils.isNotNull(icdPath))
		{
			File icdFile = new File(icdPath);
			clazzManager = TemplateEngine.getEngine().parseICD(icdFile);
		}

		return clazzManager != null;
	}

	/**
	 * �����ݿ��ж�ȡָ��ʱ������������
	 * 
	 * @param time
	 * @param length
	 * @return </br>
	 */
	public List<SPTEMsg> read(long time, long length)
	{
		// ������ѯ���
		StringBuffer mySql = new StringBuffer("SELECT * FROM message WHERE ");
		mySql.append(TIMESTAMP).append(" BETWEEN ").append(time).append(" AND ").append(time + length);
		mySql.append(" ORDER BY ").append(TIMESTAMP);

		return executeSqlString(mySql.toString());
	}

	/**
	 * �����ݿ��ѯUUID��Ϊ�յ�����
	 * 
	 * @return
	 */
	public List<SPTEMsg> getMsgUUIDNotEmpty()
	{
		// ������ѯ���
		StringBuffer mySql = new StringBuffer();
		mySql.append("SELECT * FROM message WHERE ");
		mySql.append(UUID).append(" != ''");
		mySql.append(" ORDER BY ").append(TIMESTAMP);

		return executeSqlString(mySql.toString());
	}

	/**
	 * ��ȡ���ݿ����������Ϣ������
	 * 
	 * @return </br>
	 */
	public SPTEMsg[] getAllKindsOfSPTEMsgs()
	{
		List<SPTEMsg> list = executeSqlString("SELECT * FROM message GROUP BY topicId");
		return list.toArray(new SPTEMsg[list.size()]);
	}

	/**
	 * ִ�в�ѯ��䣬�������ת��ΪSPTEMsg���󷵻�
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
			// ��ѯ����
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
	 * �ر����ݿ�����
	 */
	public void closeDBConnection()
	{
		logger.info("�ر����ݿ�����.");

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
	 * ͨ���̳߳����������� �������ݿ��в�ѯ����������װ��SPTEMsg����
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
		 * �ȷ����л�ΪMessage���� ����װΪSPTEMsg����
		 * 
		 * @param timestamp
		 * @param content
		 * @return
		 */
		private SPTEMsg createSpteMsg(long timestamp, byte[] content)
		{
			SPTEMsg spteMsg = null;

			// �����л�,�õ�һ��Message����
			ByteArrayInputStream byteStream = new ByteArrayInputStream(content);
			try
			{
				ObjectInputStream objStream = new ObjectInputStream(byteStream);
				Message message = (Message) objStream.readObject();
				objStream.close();
				// ��װΪһ��SPTEMsg����
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