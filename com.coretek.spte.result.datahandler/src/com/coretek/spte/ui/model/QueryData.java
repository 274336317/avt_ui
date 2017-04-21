/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
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
 * ���ݿ��ѯ
 * 
 * @author Sim.Wang 2012-1-15 <br/>
 *         ���Ρ 2012-02-06�޸� <br/>
 *         ���������ݿ������޸�Ϊÿ�η������ݿ�ʱ�¿�һ�����ӡ�
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
			// ����SQLite��JDBC
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

	private String				connectionString;					// ��¼���ݿ�·��
	private String				icdPath;							// ��¼ICD�ļ�·��
	private ClazzManager		clazzManager;						// �����ClazzManager����

	private SPTEMsgCache		page		= new SPTEMsgCache();	// �������ݷ��ڴ˶�����

	public QueryData(String dataPath, String IcdPath)
	{
		connectionString = new StringBuilder("jdbc:sqlite:").append(dataPath).toString();
		icdPath = IcdPath;
		clazzManager = TemplateEngine.getEngine().parseICD(new File(IcdPath));

	}

	/**
	 * @return the clazzManager <br/>
	 *         <b>����</b> ���Ρ </br> <b>����</b> 2012-2-3
	 */
	public ClazzManager getClazzManager()
	{
		return clazzManager;
	}

	/**
	 * ����Ϣд��ϵͳ��Ϣ��
	 * 
	 * @param endian ָʾ��С��</br> <b>����</b> ���Ρ </br> <b>����</b> 2012-2-4
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
	 * ͨ��ICD�ļ��õ�ClazzManager���� �����ICD�ļ�·��û�и�������ȥ��ѯ���ȡ��
	 * 
	 * @return
	 */
	private boolean createGlazzManager(Connection connection)
	{
		// ���ICD�ļ�·��û�б���Ϊ�������룬��ȥ���ݿ��ѯ
		if (StringUtils.isNull(icdPath))
		{
			try
			{
				Statement statement = connection.createStatement();
				ResultSet resultset = statement.executeQuery("select * from systemInfo");
				while (resultset.next())
				{
					// ICD�ļ�·���ɣ������ռ�·��+�������·��
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

		// ͨ��ICD�ļ��õ�ClazzManager����
		if (StringUtils.isNotNull(icdPath))
		{
			File icdFile = new File(icdPath);
			clazzManager = TemplateEngine.getEngine().parseICD(icdFile);
		}

		return clazzManager != null;
	}

	/**
	 * ��ȡ���� ʱ��� ��ָ��ʱ�䷶Χ�ڵ�����
	 * 
	 * @param time ����ʼʱ��
	 * @param length ��ʱ����
	 * @return
	 */
	public SPTEMsg[] getMessage(long time, long length)
	{
		// �ӻ����ж�ȡ
		return getMsgFromBufMem(time, length, null, null);
	}

	/**
	 * ��ȡ���� ʱ��� ��ָ��ʱ�䷶Χ�� ���� topicId Ҳ��ָ����Χ�ڵ�����
	 * 
	 * @param time : ��ʼʱ��
	 * @param length ��ʱ����
	 * @param range : topicId�ֶε�ȡֵ��Χ
	 * @return
	 */
	public SPTEMsg[] getMsgLimitByTpcId(long time, int length, String[] range)
	{
		// �ӻ����ж�ȡ
		return getMsgFromBufMem(time, length, TOPICID, range);
	}

	/**
	 * ��ȡ���� ʱ��� ��ָ��ʱ�䷶Χ�� ���� msgId Ҳ��ָ����Χ�ڵ�����
	 * 
	 * @param time : ��ʼʱ��
	 * @param length ��ʱ����
	 * @param range : msgId�ֶε�ȡֵ��Χ
	 * @return
	 */
	public SPTEMsg[] getMsgLimitByMsgId(long time, int length, String[] range)
	{
		// �ӻ����ж�ȡ
		return getMsgFromBufMem(time, length, MSGID, range);
	}

	/**
	 * ��ȡ���ݿ����������Ϣ������
	 * 
	 * @return </br> <b>����</b> ���Ρ </br> <b>����</b> 2012-3-16
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
	 * �������ж�ȡ���� ��������е����ݲ���������Ҫ����ͨ�����ݿ���»��� �ٵ������ж�ȡ����
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
		// �˷������Է��ѳ�4����С�ķ���
		List<SPTEMsg> spteMsgs = new ArrayList<SPTEMsg>();

		if (page.getBeginTime() <= time && page.getEndTime() >= (time + length))
		{
			/* ȡֵ�����ڻ����� */

			// �ӻ�����ȡ����
			return getMsgInBufMem(time, length, field, range);

		} else if (time < page.getEndTime() && page.getEndTime() < (time + length))
		{
			/* ȡֵ�����Ҷ˳������� */

			// �õ�֮ǰ�Ļ�������
			spteMsgs = page.getSPTEMsgs();
			// �����ݿ�����ӳ�����������
			spteMsgs.addAll(getMsgFromDB((page.getEndTime() + 1), (time + length - page.getEndTime() - 1)));

			// ɾ�����������
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

			// ���뻺��
			page.setSPTEMsgs(spteMsgs);
			page.setBeginTime(time);
			page.setEndTime(time + length);

			// �ӻ����ȡ����
			return getMsgInBufMem(time, length, field, range);

		} else if (time < page.getBeginTime() && page.getBeginTime() < (time + length))
		{
			/* ȡֵ������˳������� */

			// �����ݿ�ȡ�ó�����������
			spteMsgs = getMsgFromDB(time, page.getBeginTime() - time - 1);
			// �ٺϲ������е�����
			spteMsgs.addAll(page.getSPTEMsgs());

			// ɾ�����������
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

			// ���뻺��
			page.setSPTEMsgs(spteMsgs);
			page.setBeginTime(time);
			page.setEndTime(time + length);

			// �ӻ����ȡ����
			return getMsgInBufMem(time, length, field, range);

		} else
		{
			/** ȡֵ������ȫû�а����ڻ����� */

			// ������仺��
			spteMsgs = getMsgFromDB(time, length);

			page.setSPTEMsgs(spteMsgs);
			page.setBeginTime(time);
			page.setEndTime(time + length);

			// �ӻ����ȡ����
			return getMsgInBufMem(time, length, field, range);
		}
	}

	/**
	 * �ڻ����ж�ȡ����Ҫ�������
	 * 
	 * @param time
	 * @param length
	 * @param field
	 * @param range
	 * @return </br> <b>����</b> Sim.Wang </br> <b>����</b> 2012-2-22
	 */
	private SPTEMsg[] getMsgInBufMem(long time, long length, String field, String[] range)
	{
		List<SPTEMsg> spteMsgs = new ArrayList<SPTEMsg>();

		for (SPTEMsg sptemsg : page.getSPTEMsgs())
		{
			// ʱ�䷶Χ�ж�
			if (sptemsg.getTimeStamp() >= time && sptemsg.getTimeStamp() <= (time + length))
			{
				// ָ���ֶ��ж�
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
				// ��ȡ����Ҫ�����ݣ�����ѭ��
				break;
			}
		}

		return spteMsgs.toArray(new SPTEMsg[spteMsgs.size()]);
	}

	/**
	 * �����ݿ��ѯ���� ʱ��� ��ָ��ʱ�䷶Χ�� ���Ҵ������һ���ֶ�Ҳ�ڸ�����Χ�ڵ�����
	 * 
	 * @param time ����ʼʱ��
	 * @param length ��ʱ����
	 * @return
	 */
	private List<SPTEMsg> getMsgFromDB(long time, long length)
	{
		Connection connection = null;
		List<SPTEMsg> msgs = new ArrayList<SPTEMsg>();
		try
		{
			connection = DriverManager.getConnection(connectionString);
			// ������ѯ���
			StringBuffer mySql = new StringBuffer("select * from message where ");
			mySql.append(TIMESTAMP).append(" between ").append(time).append(" and ").append(time + length);
			Statement statement = connection.createStatement();
			// ��ѯ����
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
	 * �����ݿ��ѯָ��UUID������
	 * 
	 * @param uuid ��ָ����UUID
	 * @return
	 */
	public List<SPTEMsg> getMsgFromDbByUuid(String uuid)
	{
		Connection connection = null;
		List<SPTEMsg> spteMsgs = new ArrayList<SPTEMsg>();

		try
		{
			connection = DriverManager.getConnection(connectionString);
			// ������ѯ���
			StringBuffer mySql = new StringBuffer();
			mySql.append("select * from message where ");
			mySql.append(UUID).append(" = \"").append(uuid).append("\"");
			Statement statement = connection.createStatement();
			// ��ѯ����
			ResultSet resultset = statement.executeQuery(mySql.toString());

			// ȡ��ÿһ�����ݣ�����������װ��SPTEMsg����
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
	 * �����ݿ��ѯUUID��Ϊ�յ�����
	 * 
	 * @return <b>����</b> Sim.Wang </br> <b>����</b> 2012-2-1
	 */
	public List<SPTEMsg> getMsgFromDbUuidNotEmpty()
	{
		Connection connection = null;
		List<SPTEMsg> spteMsgs = new ArrayList<SPTEMsg>();

		try
		{
			connection = DriverManager.getConnection(connectionString);
			// ������ѯ���
			StringBuffer mySql = new StringBuffer();
			mySql.append("select * from message where ");
			mySql.append(UUID).append(" != ''");
			Statement statement = connection.createStatement();
			// ��ѯ����
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
	 * ͨ���̳߳����������� �������ݿ��в�ѯ����������װ��SPTEMsg����
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

	/**
	 * SPTEMsg���󻺴棬���ڱ������Ϣ���ݿ���ת������SPTEMsg���ϡ�
	 * 
	 * @author Sim.Wang
	 * @date 2012-2-23
	 */
	private static class SPTEMsgCache
	{
		private List<SPTEMsg>	spteMsgs	= new ArrayList<SPTEMsg>();

		// ��ʼʱ�䣬Ҳ����Сʱ��
		private long			beginTime	= -1;

		// ����ʱ�䣬Ҳ�����ʱ��
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