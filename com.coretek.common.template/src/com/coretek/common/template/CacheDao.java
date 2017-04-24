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
 * 用于保存和查询ICD的解析结果。每个CacheDao实例对应一个ICD解析结果数据库。
 * 
 * @author 孙大巍
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
	 * 初始化数据库，在调用其它方法之前必须先调用此方法
	 * 
	 * @return 初始化失败则返回false，成功则返回true
	 * @throws Exception
	 */
	public boolean init()
	{
		try
		{
			// 连接SQLite的JDBC
			Class.forName("org.sqlite.JDBC");
			// 建立数据库的连接
			connection = DriverManager.getConnection(new StringBuilder("jdbc:sqlite:").append(this.dbPath).toString());
		} catch (Exception e)
		{
			LoggingPlugin.logException(logger, e);
			logger.warning("打开数据库连接失败！");
			return false;
		}
		return true;
	}

	/**
	 * 缓存解析结果到数据库中
	 * 
	 * @param path ICD文件绝对路径
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
				logger.warning("数据库连接未打开！");
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
				logger.config("保存ICD解析结果到缓存数据库成功！");
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
	 * 获取缓存的ICD解析结果
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
				logger.warning("数据库连接未打开！");
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
				break;// 数据表中只会存在一条记录
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
	 * 获取缓存信息：文件绝对路径、MD5值
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
				logger.warning("数据库连接未打开！");
				return new String[0];
			}
			PreparedStatement ps = connection.prepareStatement(QUERY_INFO);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				infos = new String[2];
				infos[0] = rs.getString("filename");
				infos[1] = rs.getString("md5");
				break;// 数据库中只会存在一条记录
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
	 * 关闭数据库连接
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
				logger.config("关闭数据库连接成功！");
			} catch (SQLException e)
			{
				e.printStackTrace();
				LoggingPlugin.logException(logger, e);
			}
		} else
		{
			logger.config("数据库连接并未建立，无需关闭！");
		}

		return result;
	}

}
