package com.coretek.common.template;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.coretek.common.logging.LoggingPlugin;

/**
 * 二级缓存，将解析后的ICD文件保存在硬盘上。在保存之前查询索引数据库检查之前是否已经保存了解析结果，
 * 如果之前已经保存过则检查旧的MD5值与现在的MD5值是否匹配，如果不匹配，则拷贝一个新的缓存数据库用来存储解析结果，
 * 然后再更新索引数据库中对应的记录中的缓存数据库存放地址。如果之前未保存过解析结果，则拷贝一个干净的缓存数据库并将
 * 解析结果存放到其中，然后在索引数据库中插入一条记录信息。
 * 
 * @author 孙大巍
 * 
 */
class SecondCache
{
	private static final Logger	logger	= LoggingPlugin.getLogger(SecondCache.class.getName());

	/**
	 * 获取缓存的ClazzManager。首先匹配MD5值，如果MD5值匹配则从缓存中将对象还原。
	 * 
	 * @param registry
	 * @return 当查找不到时返回false
	 */
	public ClazzManager getFromCache(Registry registry)
	{
		ClazzManager clazzManager = null;
		try
		{
			IndexDao.init();
			CacheDao cache = IndexDao.getICDCacheDBPath(registry);
			if (cache != null)
			{
				String[] infos = cache.getInfo();
				if (infos.length == 2)
				{
					String md5 = infos[1];
					if (md5.equals(registry.getMd5()))
					{
						clazzManager = cache.getContents();
						cache.closeConnection();
						logger.config("从二级缓存当中查找到了对应的缓存结果！");
					} else
					{
						logger.config("MD5值不相等！");
					}

				} else
				{
					logger.config("以前保存的信息格式不正确！无法从数据库中查找到匹配的缓存信息！");
				}
				cache.closeConnection();

			} else
			{
				logger.config("无法从数据库中查找到匹配的缓存信息！");
			}
		} catch (IOException e)
		{
			LoggingPlugin.logException(logger, e);
		} catch (ClassNotFoundException e)
		{
			LoggingPlugin.logException(logger, e);
		} catch (Exception e)
		{
			LoggingPlugin.logException(logger, e);
		} finally
		{
			try
			{
				IndexDao.closeConnection();
			} catch (SQLException e)
			{
				LoggingPlugin.logException(logger, e);
			}
		}

		return clazzManager;
	}

	/**
	 * 将解析出来的ICD文件内容保存到硬盘上
	 * 
	 * @param registry
	 * @param manager
	 */
	public boolean cache(Registry registry, ClazzManager manager)
	{
		boolean result = false;
		try
		{
			IndexDao.init();
			IndexDao.addIndex(registry, manager);
			logger.config("ICD解析结果已经被保存到二级缓存当中！");
			result = true;
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.config("保存ICD解析结果时发生错误！msg=" + e.getMessage());
		} finally
		{
			try
			{
				IndexDao.closeConnection();
			} catch (SQLException e)
			{
				LoggingPlugin.logException(logger, e);
			}
		}

		return result;
	}

}
