/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.template;

import java.io.Serializable;

import com.coretek.spte.testcase.Message;

/**
 * 此类包含两个属性，一个是测试用例消息另一个是ICD主题。 当在编辑、显示消息时，显示的信息应当从ICD主题中获取。测试用例消息仅用于保存。
 * 
 * @author 孙大巍
 * @date 2012-1-3
 */
public class SPTEMsg implements Serializable
{
	private static final long	serialVersionUID	= 1526758349914379931L;

	private Message				msg;										// 测试用例消息

	private ICDMsg				topic;										// 主题+消息

	// 监控时间
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
	 * 构建一个SPTEMsg对象
	 * 
	 * @param msg 测试用例消息
	 * @param icdMsg 测试用例主题</br>
	 */
	public SPTEMsg(Message msg, ICDMsg icdMsg)
	{
		this.msg = msg;
		this.topic = icdMsg;
	}

	/**
	 * 获取测试用例消息
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
	 * 获取ICD主题
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
	 * 重写equals方法，用于在进行消息编辑时判断是否编辑的为已经保存的消息
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