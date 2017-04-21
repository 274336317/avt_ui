package com.coretek.spte.ui.model;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import org.eclipse.jface.preference.IPreferenceStore;

import com.coretek.common.logging.LoggingPlugin;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.template.TemplateUtils;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.cfg.CfgPlugin;
import com.coretek.spte.cfg.MonitorCacheControlPage;

/**
 * ���������ݿ�Ľ���
 * 
 * @author Sim.Wang 2012-3-13
 */
public class MessageDAO extends BaseDAO
{
	private static final Logger				logger			= LoggingPlugin.getLogger(MessageDAO.class.getName());

	private static final String				addToMessage	= "INSERT INTO message(timestamp, msgId, topicId, srcId, desId, direction, uuId, content) VALUES(?,?,?,?,?,?,?,?)";

	private static final String				addToSystemInfo	= "INSERT INTO systemInfo(icdName, icdVersion, toolVersion, targetEdian) VALUES(?,?,?,?)";

	// �������ݿ����Ϣ����
	private LinkedBlockingQueue<SPTEMsg>	insertQueue		= new LinkedBlockingQueue<SPTEMsg>();

	public MessageDAO(String dataPath, String icdPath)
	{
		super(dataPath, icdPath);
		Persistence thread = new Persistence();
		thread.setName("PERSISTENCE");
		thread.start();
	}

	/**
	 * �鿴��ǰ��д�����ݿ�����Ƿ�Ϊ��
	 * 
	 * @return </br>
	 */
	public boolean isFinished()
	{
		if (this.insertQueue.isEmpty())
		{
			logger.info("Found the needInsertDbMsgs is empty.");
			return true;
		}

		return false;
	}

	/**
	 * ����Ϣ��ӵ�д�����ݿ�Ķ�����
	 * 
	 * @param msg </br>
	 */
	public void addToInsertQueue(SPTEMsg msg)
	{
		this.insertQueue.add(msg);
	}

	/**
	 * ����Ϣд��ϵͳ��Ϣ��
	 * 
	 * @param endian ָʾ��С��</br>
	 */
	public void updateSystemInfo(int endian)
	{
		try
		{
			PreparedStatement ps = connection.prepareStatement(addToSystemInfo);
			ps.setString(1, this.icdPath);
			String version = TemplateUtils.getVerion(this.clazzManager);
			if (version == null)
				version = StringUtils.EMPTY_STRING;
			ps.setString(2, version);
			ps.setInt(3, 0);
			ps.setInt(4, 0);
			ps.execute();
			ps.close();
		} catch (SQLException e)
		{
			LoggingPlugin.logException(logger, e);
		}
	}

	private class Persistence extends Thread
	{
		private int				oneTimeNo;
		private List<SPTEMsg>	spteMsgList;

		private Connection		connection;

		public Persistence()
		{
			IPreferenceStore store = CfgPlugin.getDefault().getPreferenceStore();
			oneTimeNo = store.getInt(MonitorCacheControlPage.ONETIME_INSERT_KEY) > 0 ? store.getInt(MonitorCacheControlPage.ONETIME_INSERT_KEY) : MonitorCacheControlPage.ONETIME_INSERT_DB;

			spteMsgList = new ArrayList<SPTEMsg>(oneTimeNo);
		}

		@Override
		public void run()
		{
			try
			{
				// ����SQLite��JDBC
				Class.forName("org.sqlite.JDBC");
				// �������ݿ������
				connection = DriverManager.getConnection(daPath);
				while (true)
				{
					try
					{
						SPTEMsg msg = insertQueue.take();
						if (msg.getTimeStamp() < 0)
						{
							logger.info("Got the exit command, Persistence thread will exit.");
							insert(spteMsgList);
							logger.info(new StringBuilder("A batch of data was written into database��the size is:").append(spteMsgList.size()).toString());
							spteMsgList.clear();
							insertQueue.clear();
							break;
						} else
						{
							spteMsgList.add(msg);
							if (spteMsgList.size() == oneTimeNo)
							{
								logger.info(new StringBuilder("A batch of data was written into database��the size is:").append(oneTimeNo).toString());
								insert(spteMsgList);
								spteMsgList.clear();
							}
						}

					} catch (InterruptedException e)
					{
						LoggingPlugin.logException(logger, e);
						logger.severe("The writing thread has thrown exception, the left messages can not be written into database.");
					}
				}
			} catch (Exception e)
			{
				LoggingPlugin.logException(logger, e);
				logger.warning("There was exception while creating database connection");
			} finally
			{
				if (this.connection != null)
					try
					{
						this.connection.close();
					} catch (SQLException e)
					{
						e.printStackTrace();
					}
			}
		}

		private void insert(List<SPTEMsg> sptmsgs)
		{

			PreparedStatement preStat;
			try
			{
				preStat = connection.prepareStatement(addToMessage);

				for (SPTEMsg sptmsg : sptmsgs)
				{
					// ��message�������л�����
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					ObjectOutputStream oos = new ObjectOutputStream(out);
					oos.writeObject(sptmsg.getMsg());
					byte[] objBytes = out.toByteArray();
					oos.close();
					out.close();

					preStat.setString(1, Long.toString(sptmsg.getTimeStamp()));
					preStat.setString(2, sptmsg.getMsg().getId());
					preStat.setString(3, sptmsg.getMsg().getTopicId());
					preStat.setString(4, sptmsg.getMsg().getSrcId());
					preStat.setString(5, sptmsg.getMsg().getDesId());
					preStat.setString(6, sptmsg.getMsg().getDirection());
					preStat.setString(7, sptmsg.getMsg().getUuid());
					preStat.setBytes(8, objBytes);

					preStat.addBatch();
				}
				connection.setAutoCommit(false);
				preStat.executeBatch();
				connection.commit();
				connection.setAutoCommit(true);

				preStat.close();
			} catch (Exception e)
			{
				LoggingPlugin.logException(logger, e);
			}
		}
	}
}