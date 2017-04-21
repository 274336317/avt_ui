package com.coretek.spte.parser;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.coretek.common.template.ClazzManager;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.template.TemplateUtils;
import com.coretek.common.template.XMLBean;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.testcase.Field;
import com.coretek.spte.testcase.Message;
import com.coretek.spte.testcase.Period;

/**
 * �Զ������ֽ������н��룬����Ϊ��Ϣ����
 * 
 * @see: �������ֽڱ������Ϣ��ʽΪ�� topicId(4B),srcId(4B),time(8B),len(4B),data[]
 */
public class BinaryMessageDecoder implements IDecoder
{
	Map<String, Integer>	periodNo	= new HashMap<String, Integer>();

	private ClazzManager	icdManager	= null;
	private ClazzManager	caseManager	= null;

	private boolean			isLittle	= true;							// ���յ������Ƿ�ΪС��

	public BinaryMessageDecoder(boolean isLittle)
	{
		this.isLittle = isLittle;
	}

	@Override
	public void setIcdManager(ClazzManager icdClazzManager)
	{
		icdManager = icdClazzManager;
	}

	@Override
	public void setCaseManager(ClazzManager caseClazzManager)
	{
		caseManager = caseClazzManager;
	}

	@Override
	public Object decode(byte[] data)
	{
		//ִ�л����������š���ϢԴID��
		littleToBig(data, 0, 3); // int topicId
		littleToBig(data, 4, 7); // int srcId
		littleToBig(data, 8, 15); // long time
		littleToBig(data, 16, 19); // int dataLen
		
		// ���ݵ�ǰ20��¼��һЩ������Ϣ
		ByteBuffer buf = ByteBuffer.wrap(data);
		int topicId = buf.getInt(); // int topicId
		int srcId = buf.getInt(); // int srcId
		long time = buf.getLong(); // long time
		int dataLen = buf.getInt(); // int dataLen

		// ����36���uuId
		byte[] uuidbyte = new byte[36];
		buf.get(uuidbyte, 0, 36);
		String uuId = new String(uuidbyte);
		// �����Ƕ��������ݲ���
		byte[] msgBinContent = new byte[dataLen];
		buf.get(msgBinContent, 0, dataLen);

		//FIXME �ڶ�ڵ��£����ֻ�ȡ��Ϣ�ķ�ʽ�Ǵ���ģ�
		SPTEMsg spteMsg = TemplateUtils.getSPTEMsg(icdManager, topicId, srcId, caseManager.getTestObjectsLevel());
		spteMsg.setTimeStamp(time);
		Message message = spteMsg.getMsg();
		message.setUuid(uuId);
		List<Entity> children = message.getChildren();

		for (int i = 0; i < children.size(); i++)
		{
			Entity entry = children.get(i);
			if (entry instanceof Period)
			{
				List<Entity> periodChildren = entry.getChildren();
				for (int j = 0; j < periodChildren.size(); j++)
				{
					Entity fieldEntity = periodChildren.get(j);
					if (fieldEntity instanceof Field)
					{
						decodeField((Field) fieldEntity, msgBinContent);
					}
				}
			} else if (entry instanceof Field)
			{
				decodeField((Field) entry, msgBinContent);
			}
		}

		// ͨ��CASE�ļ�������һЩ����ֵ
		if (caseManager != null && StringUtils.isNotNull(uuId))
			addOtherInfo(message);

		return spteMsg;

	}

	private boolean decodeField(Field fieldEntity, byte[] data)
	{
		List<Entity> fieldChildren = fieldEntity.getChildren();
		int size = fieldChildren.size();
		int dataLen = data.length;

		if (size == 0)
		{
			// ��field��Ӧֵ���ֽ����е���ʼ�±�
			int wordOffset = fieldEntity.getOffsetword();
			int byteIndex = ((wordOffset * 4) < 0 ? 0 : (wordOffset * 4));

			int width = fieldEntity.getWidth();
			int byteLen = ((width / 8) == 0 ? 1 : (width / 8));

			int wordCount = byteLen / 4;
			wordCount = (wordCount == 0 ? 1 : wordCount);

			// Field��Ӧ��ֵ ����������ʾ
			StringBuilder valueStr = new StringBuilder();
			for (int wordIndex = 0; wordIndex < wordCount; wordIndex++)
			{
				String wordBinary = "";
				for (int j = 0; j < 4; j++)
				{
					// ����Field�����ֵ ����Ӧ��byte�ֽ��ҳ�
					int index = byteIndex + (wordIndex * 4) + j;
					if (index >= dataLen)
					{
						return false;
					}

					if (isLittle)
					{
						wordBinary = byte2String(data[index]) + wordBinary;
					} else
					{
						wordBinary += byte2String(data[index]);
					}
				}

				valueStr.append(wordBinary);
			}

			long valueLong = Long.valueOf(valueStr.toString(), 2);

			// ����value
			fieldEntity.setValue(Long.toString(valueLong));
		} else
		{
			// Ƕ�׵����
			for (int j = 0; j < fieldChildren.size(); j++)
			{
				decodeField((Field) fieldChildren.get(j), data);
			}
		}
		return true;
	}

	/**
	 * ��һ��byteת��Ϊ�������ַ���
	 * 
	 * @param b ��ת����byte������
	 * @return ת����Ķ������ַ���
	 */
	public String byte2String(byte b)
	{
		String string = Integer.toBinaryString((int) b);
		if (string != null)
		{
			if (string.length() >= 8)
			{
				// byte������ֻ��Ҫ���8λ
				string = string.substring(string.length() - 8, string.length());
			} else
			{
				// �������8λ�����ڸ�λ���0
				StringBuffer buf = new StringBuffer();
				for (int i = 0; i < 8 - string.length(); i++)
				{
					buf.append("0");
				}
				buf.append(string);
				string = buf.toString();
			}
		}

		return string;
	}

	/**
	 * ��С��ת��Ϊ���
	 * 
	 * @param data
	 */
	public void littleToBig(byte[] data, int start, int end)
	{
		if (data == null || end > (data.length - 1) || end < start || (end + 1 - start) % 4 != 0)
			return;

		// FIXME ��ʹ��ѭ����������
		for (int i = 0, j = (end + 1 - start) / 2; i < j; i++)
		{
			byte temp;
			temp = data[start + i];
			data[start + i] = data[end - i];
			data[end - i] = temp;
		}
	}

	/**
	 * ��������������Ϣ����һЩ����ֵ
	 * 
	 * @param message
	 */
	private void addOtherInfo(Message message)
	{
		// �ж��Ƿ��ͻ��ǽ�����Ϣ
		boolean isSend = false;
		boolean isRecv = false;
		try
		{
			isSend = ((Message) caseManager.getByUUID(message.getUuid())).isSend();
			isRecv = ((Message) caseManager.getByUUID(message.getUuid())).isRecv();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		if (isSend)
			message.setDirection(XMLBean.SEND_MSG);
		else if (isRecv)
			message.setDirection(XMLBean.RECV_MSG);
		else
			throw new RuntimeException("�޷��жϳ���Ϣ�Ƿ�����Ϣ���ǽ�����Ϣ");

		// �����������Ϣ
		if (message.getChildren().size() > 0 && (message.getChildren().get(0) instanceof Period))
		{
			// PeriodCount
			int periodCount = ((Message) caseManager.getByUUID(message.getUuid())).getPeriodCount();
			message.setPeriodCount(periodCount);

			// ��ʶ����ǰ�ǵڼ�������
			int count = (periodNo.get(message.getUuid()) != null) ? periodNo.get(message.getUuid()) + 1 : 1;
			periodNo.put(message.getUuid(), count);
			((Period) message.getChildren().get(0)).setValue(count);

			// ��������
			int periodDuration = ((Message) caseManager.getByUUID(message.getUuid())).getPeriodDuration();
			message.setPeriodDuration(periodDuration);

			// ����ֵ����
			String amendValue = ((Message) caseManager.getByUUID(message.getUuid())).getAmendValue();
			message.setAmendValue(amendValue);

			// �Ƿ�Ϊ������Ϣ����
			message.setBackground(((Message) caseManager.getByUUID(message.getUuid())).isBackground());
		}
	}
}
