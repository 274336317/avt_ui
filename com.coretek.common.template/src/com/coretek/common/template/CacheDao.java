package com.coretek.common.template;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.coretek.common.logging.LoggingPlugin;

/**
 * ���ڱ���Ͳ�ѯICD�Ľ��������ÿ��CacheDaoʵ����Ӧһ��ICD����������ݿ⡣
 * 
 * @author ���Ρ
 * 
 */
class CacheDao
{
	private static final Logger	logger			= LoggingPlugin.getLogger(IndexDao.class.getName());

	private static final String	INSERT			= "INSERT INTO icd_cache (filename, md5, contents) VALUES(?,?,?)";

	private static final String	QUERY_INFO		= "SELECT filename, md5 FROM icd_cache";

	private static final String	QUERY_CONTENTS	= "SELECT contents FROM icd_cache";

	private String				dbPath;

	private Connection			connection;

	public CacheDao(String dbPath)
	{
		this.dbPath = dbPath;
	}

	/**
	 * ��ʼ�����ݿ⣬�ڵ�����������֮ǰ�����ȵ��ô˷���
	 * 
	 * @return ��ʼ��ʧ���򷵻�false���ɹ��򷵻�true
	 * @throws Exception
	 */
	public boolean init()
	{
		try
		{
			// ����SQLite��JDBC
			Class.forName("org.sqlite.JDBC");
			// �������ݿ������
			connection = DriverManager.getConnection(new StringBuilder("jdbc:sqlite:").append(this.dbPath).toString());
		} catch (Exception e)
		{
			LoggingPlugin.logException(logger, e);
			logger.warning("�����ݿ�����ʧ�ܣ�");
			return false;
		}
		return true;
	}

	/**
	 * ���������������ݿ���
	 * 
	 * @param path ICD�ļ�����·��
	 * @param md5
	 * @param manager
	 */
	public boolean add(String path, String md5, ClazzManager manager)
	{
		boolean result = false;
		try
		{
			if (connection == null)
			{
				logger.warning("���ݿ�����δ�򿪣�");
			} else
			{
				PreparedStatement ps = connection.prepareStatement(INSERT);
				ps.setString(1, path);
				ps.setString(2, md5);
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(out);
				oos.writeObject(manager);
				byte[] contents = out.toByteArray();
				ps.setBytes(3, contents);

				result = ps.execute();
				ps.close();
				oos.close();
				out.close();
				result = true;
				logger.config("����ICD����������������ݿ�ɹ���");
			}

		} catch (SQLException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * ��ȡ�����ICD�������
	 * 
	 * @return
	 */
	public ClazzManager getContents()
	{
		ClazzManager manager = null;
		try
		{
			if (connection == null)
			{
				logger.warning("���ݿ�����δ�򿪣�");
				return null;
			}
			PreparedStatement ps = connection.prepareStatement(QUERY_CONTENTS);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				byte[] bytes = rs.getBytes("contents");
				ByteArrayInputStream input = new ByteArrayInputStream(bytes);
				ObjectInputStream ois = new ObjectInputStream(input);
				manager = (ClazzManager) ois.readObject();
				ois.close();
				input.close();
				break;// ���ݱ���ֻ�����һ����¼
			}
			rs.close();
			ps.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}

		return manager;
	}

	/**
	 * ��ȡ������Ϣ���ļ�����·����MD5ֵ
	 * 
	 * @return
	 */
	public String[] getInfo()
	{
		String[] infos = null;
		try
		{
			if (connection == null)
			{
				logger.warning("���ݿ�����δ�򿪣�");
				return new String[0];
			}
			PreparedStatement ps = connection.prepareStatement(QUERY_INFO);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				infos = new String[2];
				infos[0] = rs.getString("filename");
				infos[1] = rs.getString("md5");
				break;// ���ݿ���ֻ�����һ����¼
			}
			rs.close();
			ps.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
			infos = new String[0];
		}
		return infos;
	}

	/**
	 * �ر����ݿ�����
	 * 
	 * @return
	 */
	public boolean closeConnection()
	{
		boolean result = false;
		if (this.connection != null)
		{
			try
			{
				this.connection.close();
				result = true;
				logger.config("�ر����ݿ����ӳɹ���");
			} catch (SQLException e)
			{
				e.printStackTrace();
				LoggingPlugin.logException(logger, e);
			}
		} else
		{
			logger.config("���ݿ����Ӳ�δ����������رգ�");
		}

		return result;
	}

}
