package com.coretek.spte.parser;

import java.io.File;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import com.coretek.common.logging.LoggingPlugin;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.template.TemplateEngine;
import com.coretek.common.utils.StringUtils;

/**
 * ��Ϣ������������Ϣ�Ӷ��������ݸ�����Ϣ��������ļ�(ICD)�����д����Ϣ���� ��ʹ�ø���ʱ��Ҫ���������롢��������Լ���Ϣ��������ļ������ù�������:
 * setInDataStream(istream); setOutDataStream(ostream); start();
 * 
 * @author Administrator
 * 
 */
public class MessageParser implements IMessageParser
{

	/** ��Ϣ��������еĳ���, ��Ϣ�ڽ���ǰ�Թ̶����ȵ��ֽ������д��� */
	public static final int							MSG_LEN			= 2 * 1024;

	private static final Logger						logger			= LoggingPlugin.getLogger(MessageParser.class.getName());

	private IStreamReader							reader			= null;

	private IMessageWriter							writer			= null;

	private IDecoder								decoder			= null;

	private Observer								observer;

	private volatile boolean						inputFinished	= false;													// ��ʶ�Ƿ����ݽ������

	private volatile LinkedBlockingQueue<byte[]>	blockingQueue	= new LinkedBlockingQueue<byte[]>();

	/**
	 * @param isLittle </br> true:С�� / false:���
	 * @param observer ���յ���һ����Ϣ�󣬻�֪ͨ�۲���
	 */
	public MessageParser(boolean isLittle, Observer observer)
	{
		decoder = new BinaryMessageDecoder(isLittle);
		this.observer = observer;
	}

	@Override
	public boolean start()
	{
		if (reader == null || writer == null)
		{
			return false;
		}

		// ����һ���̣߳���ȡ���յ�������
		new Thread(new BinaryReader(), "Reading thread").start();
		logger.info("������ȡ�߳�...");

		BinaryDecoder decoder = new BinaryDecoder();
		decoder.addObserver(this.observer);
		// ����һ���̣߳�������ȡ��������
		new Thread(decoder, "Decoding thread").start();
		logger.info("�������ݽ����߳�...");

		return true;
	}

	@Override
	public void setInDataStream(IStreamReader istream)
	{
		reader = istream;
	}

	@Override
	public void setOutDataStream(IMessageWriter ostream)
	{
		writer = ostream;
	}

	@Override
	public boolean isFinished()
	{
		return inputFinished && this.blockingQueue.isEmpty();
	}

	public void setIcdPath(String icdPath)
	{
		decoder.setIcdManager(TemplateEngine.getEngine().parseICD(new File(icdPath)));
	}

	public void setCasePath(String casePath)
	{
		if (StringUtils.isNotNull(casePath))
			decoder.setCaseManager(TemplateEngine.getEngine().parseCase(new File(casePath)));
	}

	/**
	 * �����ƶ�ȡ��
	 * 
	 * @author ���Ρ 2012-3-20
	 */
	private class BinaryReader implements Runnable
	{

		@Override
		public void run()
		{

			while (!inputFinished)
			{
				// �����Ϣ���������ɶ�, ��ȴ�ֱ����ɶ�״̬
				if (!reader.canRead())
				{
					try
					{
						logger.info("�������л�û�����ݣ�BinaryReader�̵߳ȴ�...");
						Thread.sleep(500);
						continue;
					} catch (InterruptedException e)
					{
						LoggingPlugin.logException(logger, e);
						logger.warning("The sleeping status of BinaryReader was interrupted, the BinaryReader will exit.");
						return;
					}
				}
				// ���������ж�ȡ��Ϣ����
				byte[] data = new byte[MSG_LEN];
				int readlen = reader.read(data, MSG_LEN);

				// ���أ�1��ʾ���Ѿ�����
				if (readlen == -1)
				{
					inputFinished = true;
					logger.info("���յ���ִ������������ص� -1��");
					reader.shutDown();
					return;
				}

				blockingQueue.add(data);
				// �ж��ǲ��ǽ�����
				if (data[0] == 0 && data[1] == 0 && data[2] == 0 && data[3] == 0)
				{
					inputFinished = true;
					logger.info("��ִ����������н��յ��Ƴ����");
					reader.shutDown();
				}
			}
		}
	}

	/**
	 * �����ƽ�����
	 * 
	 * @author ���Ρ 2012-3-20
	 */
	private class BinaryDecoder extends Observable implements Runnable
	{
		// ���ڱ�ʶ�Ƿ�Ϊ���յ��ĵ�һ����Ϣ
		private volatile boolean	isFirstMsg	= true;

		@Override
		public void run()
		{
			while (!isFinished())
			{
				try
				{
					byte[] contents = blockingQueue.take();
					// �ж��ǲ��ǽ�����
					if (contents[0] == 0 && contents[1] == 0 && contents[2] == 0 && contents[3] == 0)
					{
						SPTEMsg msg = new SPTEMsg();
						msg.setTimeStamp(-1);
						writer.write(msg);
						logger.info("BinaryDecoder�߳̽��յ��Ƴ�����, �߳��˳���");
						break;
					}
					// ���ý����������д���������
					SPTEMsg msg = (SPTEMsg) decoder.decode(contents);
					writer.write(msg);
					if (this.isFirstMsg)
					{
						logger.info("��ִ�������յ���һ����Ϣ��");
						// ֪ͨ�۲��ߣ��Ѿ��յ���һ����Ϣ
						this.setChanged();
						this.notifyObservers();
						this.isFirstMsg = false;
						this.clearChanged();
						this.deleteObserver(observer);
					}
					logger.config(new StringBuilder("������������Ϣ:\n").append(msg.getMsg()).toString());
				} catch (InterruptedException e)
				{
					LoggingPlugin.logException(logger, e);
					logger.warning("The BlockingQueue was interruptted, the BinaryDecoder thread will exit.");
					break;
				}
			}

		}
	}
}