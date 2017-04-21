package com.coretek.spte.parser;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import org.eclipse.jface.preference.IPreferenceStore;

import com.coretek.common.logging.LoggingPlugin;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.cfg.CfgPlugin;
import com.coretek.spte.cfg.ExecutionPreferencePage;
import com.coretek.spte.ui.model.IMonitorCache;
import com.coretek.spte.ui.model.MonitorCache;

public class ExecutionContext extends Observable implements IExecutorContext, Observer
{
	private final static Logger	logger	= LoggingPlugin.getLogger(ExecutionContext.class);

	protected IMonitorCache		messageCache;

	// 消息解析器
	private MessageParser		messageParser;
	// 服务器
	private IServer				tcpServer;

	private Observer			observer;

	private boolean				needCache;

	public ExecutionContext(Observer observer, boolean needCache)
	{
		this.observer = observer;
		this.needCache = needCache;
	}

	@Override
	public void init(String icdPath, String msgDBPath)
	{
		messageCache = new MonitorCache(msgDBPath, icdPath, needCache);

		IPreferenceStore store = CfgPlugin.getDefault().getPreferenceStore();
		int port = store.getInt(ExecutionPreferencePage.PORT_NAME);
		int cacheSize = store.getInt(ExecutionPreferencePage.CACHE_SIZE_NAME);
		String endian = store.getString(ExecutionPreferencePage.ENDIAN_NAME);
		if (StringUtils.isNull(endian))
		{
			endian = ExecutionPreferencePage.ENDIAN_DEFAULT_VALUE;
		}
		((MonitorCache) messageCache).updateSystemInfo("little".equals(endian) ? 1 : 0);
		if (port <= 0)
		{
			port = ExecutionPreferencePage.PORT_DEFAULT_VALUE;
		}
		if (cacheSize <= 0)
		{
			cacheSize = ExecutionPreferencePage.CACHE_SIZE_DEFAULT_VALUE;
		}
		logger.info(new StringBuilder("\nport=").append(port).append("\ncacheSize=").append(cacheSize).append("\nendian=").append(endian).toString());

		this.messageParser = new MessageParser("little".equals(endian) ? true : false, this);
		this.messageParser.setIcdPath(icdPath);
		this.tcpServer = new TcpServer();
		this.tcpServer.start(port, cacheSize);
		this.messageParser.setInDataStream(this.tcpServer);
		this.messageParser.setOutDataStream((IMessageWriter) messageCache);
		this.addObserver(this.observer);
	}

	@Override
	public void shutDown()
	{
		logger.info("The method ParserContext.shutDown() was invoked.");
		try
		{
			this.tcpServer.shutDown();
		} catch (Exception e)
		{
			LoggingPlugin.logException(logger, e);
		}
	}

	@Override
	public boolean startParsing()
	{
		return this.messageParser.start();
	}

	@Override
	public boolean isFinished()
	{
		boolean result = this.messageParser.isFinished();
		if (!result)
		{
			return false;
		}
		boolean result2 = ((MonitorCache) messageCache).isFinished();
		logger.info(new StringBuilder("查询解析器能够关闭的结果是： ").append((result && result2)).toString());
		if ((result && result2))
		{
			((MonitorCache) messageCache).closeDBConnection();
		}
		return (result && result2);
	}

	@Override
	public SPTEMsg[] querySPTEMsgs(long startTime, long length)
	{
		return messageCache.getMessage(startTime, length);
	}

	@Override
	public SPTEMsg[] getMsgLimitByMsgId(long time, int length, String[] range)
	{
		return messageCache.getMsgLimitByMsgId(time, length, range);
	}

	@Override
	public void clearCache()
	{
		this.messageCache.clearCache();

	}

	/**
	 * 获取消息解析器
	 * 
	 * @return
	 */
	public MessageParser getMessageParser()
	{
		return this.messageParser;
	}

	@Override
	public void update(Observable o, Object arg)
	{
		logger.info("接收到从执行器发送的第一条消息。");
		// 接收到了第一条消息，向上广播
		this.setChanged();
		this.notifyObservers();
		this.deleteObserver(this.observer);
	}

}