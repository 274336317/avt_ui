package com.coretek.common.template;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.coretek.common.logging.LoggingPlugin;

/**
 * �������棬���������ICD�ļ�������Ӳ���ϡ��ڱ���֮ǰ��ѯ�������ݿ���֮ǰ�Ƿ��Ѿ������˽��������
 * ���֮ǰ�Ѿ����������ɵ�MD5ֵ�����ڵ�MD5ֵ�Ƿ�ƥ�䣬�����ƥ�䣬�򿽱�һ���µĻ������ݿ������洢���������
 * Ȼ���ٸ����������ݿ��ж�Ӧ�ļ�¼�еĻ������ݿ��ŵ�ַ�����֮ǰδ���������������򿽱�һ���ɾ��Ļ������ݿⲢ��
 * ���������ŵ����У�Ȼ�����������ݿ��в���һ����¼��Ϣ��
 * 
 * @author ���Ρ
 * 
 */
class SecondCache
{
	private static final Logger	logger	= LoggingPlugin.getLogger(SecondCache.class.getName());

	/**
	 * ��ȡ�����ClazzManager������ƥ��MD5ֵ�����MD5ֵƥ����ӻ����н�����ԭ��
	 * 
	 * @param registry
	 * @return �����Ҳ���ʱ����false
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
						logger.config("�Ӷ������浱�в��ҵ��˶�Ӧ�Ļ�������");
					} else
					{
						logger.config("MD5ֵ����ȣ�");
					}

				} else
				{
					logger.config("��ǰ�������Ϣ��ʽ����ȷ���޷������ݿ��в��ҵ�ƥ��Ļ�����Ϣ��");
				}
				cache.closeConnection();

			} else
			{
				logger.config("�޷������ݿ��в��ҵ�ƥ��Ļ�����Ϣ��");
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
	 * ������������ICD�ļ����ݱ��浽Ӳ����
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
			logger.config("ICD��������Ѿ������浽�������浱�У�");
			result = true;
		} catch (Exception e)
		{
			e.printStackTrace();
			logger.config("����ICD�������ʱ��������msg=" + e.getMessage());
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
