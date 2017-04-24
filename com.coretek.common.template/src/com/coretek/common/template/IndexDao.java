package com.coretek.common.template;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import com.coretek.common.logging.LoggingPlugin;
import com.coretek.common.utils.EclipseUtils;
import com.coretek.spte.data.DataPlugin;

/**
 * ��������������ݿ⡣��ν�������ݿ��ǽ�ICD�ļ��ľ���·���Լ��������ݿ�Ĵ�ŵ�ַ���浽�������ݿ��У�
 * ÿ�ε�����Ҫ��ѯICD����ʱ�����Ȳ�ѯ�������ݿ⣬�������ݿ��еĴ洢��Ϣ��ȥ���һ������ݿ⡣
 * 
 * @author ���Ρ
 * 
 */
public class IndexDao
{
	private static final Logger	LOGGER	= LoggingPlugin.getLogger(IndexDao.class.getName());

	private static final String	INSERT	= "INSERT INTO icd_index (file_name, location) VALUES(?,?)";

	private static final String	QUERY	= "SELECT location FROM icd_index WHERE file_name=?";

	private static final String	UPDATE	= "UPDATE icd_index SET location=? WHERE file_name=?";

	private static Connection	CONNECTION;

	private static String		DBPATH;

	/**
	 * ��ʼ�����ݿ⣺���������������ݿ⡢�������ݿ�����
	 * 
	 * @throws Exception ���޷������ݿ⽨�����ӻ�ȡ�������ӳ���ʱ���׳����쳣
	 */
	public synchronized static void init() throws Exception
	{
		if (CONNECTION == null)
		{
			StringBuilder sb = new StringBuilder();
			// ��װ���ݿ�·�����������ݿ���ڵ�ǰ�����ռ��.icd_dbĿ¼��
			String path = EclipseUtils.getWorkspacePath().toOSString();
			sb.append(path).append(File.separator).append(".icd_db").append(File.separator).append("icd_index.db");
			File file = new File(sb.toString());
			if (file.exists())
			{
				LOGGER.config("�������ݿ��Ѿ��������贴����");
			} else
			{
				LOGGER.config("�������ݿⲻ���ڣ���Ҫ���´�����");
				copyDB("icd_index.db", "icd_index.db");
			}
			DBPATH = sb.toString();
			// �������ݿ�����
			openConnection();
		}

	}

	/**
	 * �ر����ݿ�����
	 * 
	 * @throws SQLException ���ر����ݿ����ӷ�������ʱ���׳����쳣
	 */
	public synchronized static void closeConnection() throws SQLException
	{
		if (CONNECTION != null)
		{
			CONNECTION.close();
			CONNECTION = null;
			LOGGER.config("�ر����ݿ����ӳɹ���");
		} else
		{
			LOGGER.config("δ�������ݿ����ӣ�����رգ�");
		}
	}

	/**
	 * �������ݿ��ļ�����ǰ�����ռ��.icd_cacheĿ¼��
	 * 
	 * @throws Exception
	 */
	private static String copyDB(String srcName, String destName) throws Exception
	{
		// Դ·��
		StringBuilder srcPath = new StringBuilder();
		srcPath.append(EclipseUtils.getPluginLoaction(DataPlugin.getDefault()).getAbsolutePath());
		srcPath.append(File.separator).append("db").append(File.separator).append(srcName);

		// Ŀ��·��
		StringBuilder destPath = new StringBuilder();
		String path = EclipseUtils.getWorkspacePath().toOSString();
		destPath.append(path).append(File.separator).append(".icd_db");
		destPath.append(File.separator).append(destName);
		FileUtils.copyFile(new File(srcPath.toString()), new File(destPath.toString()));

		return destPath.toString();

	}

	private static void openConnection() throws Exception
	{
		try
		{
			// ����SQLite��JDBC
			Class.forName("org.sqlite.JDBC");
			// �������ݿ������
			CONNECTION = DriverManager.getConnection(new StringBuilder("jdbc:sqlite:").append(DBPATH).toString());
			LOGGER.config("�����ݿ����ӳɹ���");
		} catch (Exception e)
		{
			LoggingPlugin.logException(LOGGER, e);
			LOGGER.warning("�����ݿ�����ʧ�ܣ�");

			throw e;
		}
	}

	/**
	 * ���������ݿ��л�ȡICD�������ݿ����
	 * 
	 * @param registry
	 * @return
	 */
	public synchronized static CacheDao getICDCacheDBPath(Registry registry)
	{
		CacheDao cache = null;
		if (CONNECTION != null)
		{
			try
			{
				PreparedStatement ps = CONNECTION.prepareStatement(QUERY);
				ps.setString(1, registry.getTargetXML());
				ResultSet rs = ps.executeQuery();
				if (rs.next())
				{
					cache = new CacheDao(rs.getString("location"));
					LOGGER.config("���ҵ��������ݿ��·��: " + rs.getString("location"));
				}
				ps.close();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		if (cache == null)
		{
			return cache;
		}
		return cache.init() ? cache : null;
	}

	/**
	 * ��ICD���������Ľ�����浽���ݿ��С�����ʱ����֮ǰ�Ƿ��Ѿ�������������������ѯ����ʱ
	 * ��MD5ֵ�����MD5ֵ��ƥ�������¿���һ���µĻ������ݿ⣬Ȼ�󱣴��������ٸ����������ݿ��ж�Ӧ��
	 * ��¼�еĻ������ݿ��ַ�����û�б�������򿽱�һ���µĻ������ݿ⣬Ȼ�󱣴������������������ݿ��� ����һ����Ϣ��
	 * 
	 * @param registry
	 * @param manager ICD�������
	 * @throws Exception
	 */
	public synchronized static boolean addIndex(Registry registry, ClazzManager manager) throws Exception
	{
		boolean result = false;
		if (CONNECTION != null)
		{
			StringBuilder destName = new StringBuilder();
			destName.append(new File(registry.getTargetXML()).getName());
			destName.append("_").append(System.currentTimeMillis()).append(".db");
			// ����һ���ɾ���icd_cache.db��.cache_dbĿ¼�£���������
			String cachePath = copyDB("icd_cache.db", destName.toString());
			// ��ICD�������д�����ݿ��л���
			CacheDao cache = new CacheDao(cachePath);
			if (cache.init())
			{
				if (cache.add(registry.getTargetXML(), registry.getMd5(), manager) && cache.closeConnection())
				{
					// ���ICD�������֮ǰ�Ƿ񱻻�������
					cache = getICDCacheDBPath(registry);
					if (cache != null)
					{// ֮ǰ�ͱ�����������
						LOGGER.config("�������֮ǰ�����������ˣ�������Ҫ�����������ݿ�����Ӧ����Ϣ��");
						result = update(registry.getTargetXML(), cachePath);
					} else
					{
						// ����һ��������Ϣ
						PreparedStatement ps = CONNECTION.prepareStatement(INSERT);
						ps.setString(1, registry.getTargetXML());
						ps.setString(2, cachePath);
						ps.execute();
						ps.close();
						result = true;
						LOGGER.config("δ���ֽ������֮ǰ��������������������ɹ���");
					}
				}
			}
		}

		return result;
	}

	/**
	 * ����������Ϣ����Ҫ����֮ǰ�Ѿ������ĳ��ICD�������������ICD���ݱ��ı�����Ҫ���¿���һ�����ݿ��Ի�������������Ҫ����������Ϣ��
	 * 
	 * @param path ICD�ļ�·��
	 * @param location ���ݿ�·��
	 * @return
	 */
	private static boolean update(String path, String location)
	{
		boolean result = false;
		if (CONNECTION != null)
		{
			PreparedStatement ps = null;
			try
			{
				ps = CONNECTION.prepareStatement(UPDATE);
				ps.setString(1, location);
				ps.setString(2, path);
				result = ps.execute();
			} catch (SQLException e)
			{
				e.printStackTrace();
			} finally
			{
				if (ps != null)
				{
					try
					{
						ps.close();
					} catch (SQLException e)
					{
						e.printStackTrace();
					}
				}
			}
		}

		return result;
	}
}