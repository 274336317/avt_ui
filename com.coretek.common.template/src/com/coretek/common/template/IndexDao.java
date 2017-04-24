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
 * 负责访问索引数据库。所谓索引数据库是将ICD文件的绝对路径以及缓存数据库的存放地址保存到索引数据库中，
 * 每次调用者要查询ICD缓存时，首先查询索引数据库，根据数据库中的存储信息再去查找缓存数据库。
 * 
 * @author 孙大巍
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
	 * 初始化数据库：包括拷贝索引数据库、创建数据库连接
	 * 
	 * @throws Exception 当无法和数据库建立连接获取建立连接出错时会抛出此异常
	 */
	public synchronized static void init() throws Exception
	{
		if (CONNECTION == null)
		{
			StringBuilder sb = new StringBuilder();
			// 组装数据库路径，索引数据库放在当前工作空间的.icd_db目录下
			String path = EclipseUtils.getWorkspacePath().toOSString();
			sb.append(path).append(File.separator).append(".icd_db").append(File.separator).append("icd_index.db");
			File file = new File(sb.toString());
			if (file.exists())
			{
				LOGGER.config("索引数据库已经存在无需创建。");
			} else
			{
				LOGGER.config("索引数据库不存在，需要重新创建。");
				copyDB("icd_index.db", "icd_index.db");
			}
			DBPATH = sb.toString();
			// 建立数据库连接
			openConnection();
		}

	}

	/**
	 * 关闭数据库连接
	 * 
	 * @throws SQLException 当关闭数据库连接发生错误时会抛出此异常
	 */
	public synchronized static void closeConnection() throws SQLException
	{
		if (CONNECTION != null)
		{
			CONNECTION.close();
			CONNECTION = null;
			LOGGER.config("关闭数据库连接成功！");
		} else
		{
			LOGGER.config("未建立数据库连接，无需关闭！");
		}
	}

	/**
	 * 拷贝数据库文件到当前工作空间的.icd_cache目录下
	 * 
	 * @throws Exception
	 */
	private static String copyDB(String srcName, String destName) throws Exception
	{
		// 源路径
		StringBuilder srcPath = new StringBuilder();
		srcPath.append(EclipseUtils.getPluginLoaction(DataPlugin.getDefault()).getAbsolutePath());
		srcPath.append(File.separator).append("db").append(File.separator).append(srcName);

		// 目的路径
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
			// 连接SQLite的JDBC
			Class.forName("org.sqlite.JDBC");
			// 建立数据库的连接
			CONNECTION = DriverManager.getConnection(new StringBuilder("jdbc:sqlite:").append(DBPATH).toString());
			LOGGER.config("打开数据库连接成功！");
		} catch (Exception e)
		{
			LoggingPlugin.logException(LOGGER, e);
			LOGGER.warning("打开数据库连接失败！");

			throw e;
		}
	}

	/**
	 * 从索引数据库中获取ICD缓存数据库对象
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
					LOGGER.config("查找到缓存数据库的路径: " + rs.getString("location"));
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
	 * 将ICD解析出来的结果保存到数据库中。保存时会检查之前是否已经保存过，如果保存过则查询保存时
	 * 的MD5值，如果MD5值不匹配则重新拷贝一个新的缓存数据库，然后保存解析结果再更新索引数据库中对应的
	 * 记录中的缓存数据库地址；如果没有保存过，则拷贝一个新的缓存数据库，然后保存解析结果再在索引数据库中 插入一条信息。
	 * 
	 * @param registry
	 * @param manager ICD解析结果
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
			// 拷贝一个干净的icd_cache.db到.cache_db目录下，并重命名
			String cachePath = copyDB("icd_cache.db", destName.toString());
			// 将ICD解析结果写入数据库中缓存
			CacheDao cache = new CacheDao(cachePath);
			if (cache.init())
			{
				if (cache.add(registry.getTargetXML(), registry.getMd5(), manager) && cache.closeConnection())
				{
					// 检查ICD解析结果之前是否被缓存起来
					cache = getICDCacheDBPath(registry);
					if (cache != null)
					{// 之前就被缓存起来了
						LOGGER.config("解析结果之前被缓存起来了，现在需要更新索引数据库中相应的信息！");
						result = update(registry.getTargetXML(), cachePath);
					} else
					{
						// 插入一条索引信息
						PreparedStatement ps = CONNECTION.prepareStatement(INSERT);
						ps.setString(1, registry.getTargetXML());
						ps.setString(2, cachePath);
						ps.execute();
						ps.close();
						result = true;
						LOGGER.config("未发现解析结果之前被缓存过，保存解析结果成功！");
					}
				}
			}
		}

		return result;
	}

	/**
	 * 更新索引信息，主要用于之前已经缓存过某个ICD解析结果，现在ICD内容被改变了需要重新拷贝一个数据库以缓存结果，所以需要更新索引信息。
	 * 
	 * @param path ICD文件路径
	 * @param location 数据库路径
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