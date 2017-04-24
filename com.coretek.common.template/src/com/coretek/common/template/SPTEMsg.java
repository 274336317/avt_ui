/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template;

import java.io.Serializable;

import com.coretek.spte.testcase.Message;

/**
 * ��������������ԣ�һ���ǲ���������Ϣ��һ����ICD���⡣ ���ڱ༭����ʾ��Ϣʱ����ʾ����ϢӦ����ICD�����л�ȡ������������Ϣ�����ڱ��档
 * 
 * @author ���Ρ
 * @date 2012-1-3
 */
public class SPTEMsg implements Serializable
{
	private static final long	serialVersionUID	= 1526758349914379931L;

	private Message				msg;										// ����������Ϣ

	private ICDMsg				topic;										// ����+��Ϣ

	// ���ʱ��
	private long				timeStamp;

	/**
	 * @return the timeStamp <br/>
	 * 
	 */
	public long getTimeStamp()
	{
		return timeStamp;
	}

	/**
	 * @param timeStamp the timeStamp to set <br/>
	 * 
	 */
	public void setTimeStamp(long timeStamp)
	{
		this.timeStamp = timeStamp;
	}

	public SPTEMsg()
	{

	}

	/**
	 * ����һ��SPTEMsg����
	 * 
	 * @param msg ����������Ϣ
	 * @param icdMsg ������������</br>
	 */
	public SPTEMsg(Message msg, ICDMsg icdMsg)
	{
		this.msg = msg;
		this.topic = icdMsg;
	}

	/**
	 * ��ȡ����������Ϣ
	 * 
	 * @return the msg <br/>
	 * 
	 */
	public Message getMsg()
	{
		return msg;
	}

	/**
	 * @param msg the msg to set <br/>
	 * 
	 */
	public void setMsg(Message msg)
	{
		this.msg = msg;
	}

	/**
	 * ��ȡICD����
	 * 
	 * @return the topic <br/>
	 * 
	 */
	public ICDMsg getICDMsg()
	{
		return topic;
	}

	public void setICDMsg(ICDMsg icdMsg)
	{
		this.topic = icdMsg;
	}

	/**
	 * ��дequals�����������ڽ�����Ϣ�༭ʱ�ж��Ƿ�༭��Ϊ�Ѿ��������Ϣ
	 * 
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		if (this == obj)
		{
			return true;
		}
		if (obj instanceof SPTEMsg)
		{
			SPTEMsg oth = (SPTEMsg) obj;
			return oth.getICDMsg().getAttribute(Constants.ICD_MSG_ID).equals(this.getICDMsg().getAttribute(Constants.ICD_MSG_ID));
		}
		return false;
	}
}