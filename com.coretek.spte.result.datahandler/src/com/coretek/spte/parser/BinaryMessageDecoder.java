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
 * 对二进制字节流进行解码，解码为消息对象
 * 
 * @see: 二进制字节编码的消息格式为： topicId(4B),srcId(4B),time(8B),len(4B),data[]
 */
public class BinaryMessageDecoder implements IDecoder
{
	Map<String, Integer>	periodNo	= new HashMap<String, Integer>();

	private ClazzManager	icdManager	= null;
	private ClazzManager	caseManager	= null;

	private boolean			isLittle	= true;							// 接收的数据是否为小端

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
		//执行环境会把主题号、消息源ID、
		littleToBig(data, 0, 3); // int topicId
		littleToBig(data, 4, 7); // int srcId
		littleToBig(data, 8, 15); // long time
		littleToBig(data, 16, 19); // int dataLen
		
		// 数据的前20记录了一些属性信息
		ByteBuffer buf = ByteBuffer.wrap(data);
		int topicId = buf.getInt(); // int topicId
		int srcId = buf.getInt(); // int srcId
		long time = buf.getLong(); // long time
		int dataLen = buf.getInt(); // int dataLen

		// 用了36存放uuId
		byte[] uuidbyte = new byte[36];
		buf.get(uuidbyte, 0, 36);
		String uuId = new String(uuidbyte);
		// 接着是二进制数据部分
		byte[] msgBinContent = new byte[dataLen];
		buf.get(msgBinContent, 0, dataLen);

		//FIXME 在多节点下，这种获取消息的方式是错误的！
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

		// 通过CASE文件，设置一些属性值
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
			// 该field对应值在字节流中的起始下标
			int wordOffset = fieldEntity.getOffsetword();
			int byteIndex = ((wordOffset * 4) < 0 ? 0 : (wordOffset * 4));

			int width = fieldEntity.getWidth();
			int byteLen = ((width / 8) == 0 ? 1 : (width / 8));

			int wordCount = byteLen / 4;
			wordCount = (wordCount == 0 ? 1 : wordCount);

			// Field对应的值 二进制流表示
			StringBuilder valueStr = new StringBuilder();
			for (int wordIndex = 0; wordIndex < wordCount; wordIndex++)
			{
				String wordBinary = "";
				for (int j = 0; j < 4; j++)
				{
					// 将该Field对象的值 所对应的byte字节找出
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

			// 设置value
			fieldEntity.setValue(Long.toString(valueLong));
		} else
		{
			// 嵌套的情况
			for (int j = 0; j < fieldChildren.size(); j++)
			{
				decodeField((Field) fieldChildren.get(j), data);
			}
		}
		return true;
	}

	/**
	 * 将一个byte转化为二进制字符串
	 * 
	 * @param b 待转化的byte型数据
	 * @return 转化后的二进制字符串
	 */
	public String byte2String(byte b)
	{
		String string = Integer.toBinaryString((int) b);
		if (string != null)
		{
			if (string.length() >= 8)
			{
				// byte型数据只需要最后8位
				string = string.substring(string.length() - 8, string.length());
			} else
			{
				// 如果不够8位，则在高位填充0
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
	 * 从小端转换为大端
	 * 
	 * @param data
	 */
	public void littleToBig(byte[] data, int start, int end)
	{
		if (data == null || end > (data.length - 1) || end < start || (end + 1 - start) % 4 != 0)
			return;

		// FIXME 蛋痛的循环条件计算
		for (int i = 0, j = (end + 1 - start) / 2; i < j; i++)
		{
			byte temp;
			temp = data[start + i];
			data[start + i] = data[end - i];
			data[end - i] = temp;
		}
	}

	/**
	 * 给解析出来的消息设置一些属性值
	 * 
	 * @param message
	 */
	private void addOtherInfo(Message message)
	{
		// 判断是发送还是接收消息
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
			throw new RuntimeException("无法判断出消息是发送消息还是接收消息");

		// 如果是周期消息
		if (message.getChildren().size() > 0 && (message.getChildren().get(0) instanceof Period))
		{
			// PeriodCount
			int periodCount = ((Message) caseManager.getByUUID(message.getUuid())).getPeriodCount();
			message.setPeriodCount(periodCount);

			// 标识出当前是第几个周期
			int count = (periodNo.get(message.getUuid()) != null) ? periodNo.get(message.getUuid()) + 1 : 1;
			periodNo.put(message.getUuid(), count);
			((Period) message.getChildren().get(0)).setValue(count);

			// 周期设置
			int periodDuration = ((Message) caseManager.getByUUID(message.getUuid())).getPeriodDuration();
			message.setPeriodDuration(periodDuration);

			// 修正值设置
			String amendValue = ((Message) caseManager.getByUUID(message.getUuid())).getAmendValue();
			message.setAmendValue(amendValue);

			// 是否为背景消息属性
			message.setBackground(((Message) caseManager.getByUUID(message.getUuid())).isBackground());
		}
	}
}
