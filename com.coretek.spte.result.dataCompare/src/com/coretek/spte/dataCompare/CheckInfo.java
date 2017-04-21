package com.coretek.spte.dataCompare;

/**
 * 错误消息枚举
 * 
 * @author Sim.Wang 2012-2-2
 */
public enum CheckInfo
{
	MSG_LOSS(2001, "消息丢失"), MSG_TIMEOUT(2002, "消息超时,期望用时:{0}实际用时:{1}"), MSG_ERRORVALUE(2003, "期望值与实际值不匹配"), MSG_UNEXPECTED(2004, "非期望出现的消息"), PERIOD_PARTLOSS(2005, "周期消息部分消息丢失,期望收到{0}个周期,实际收到{1}个周期"), PERIOD_PARTEXCESS(2006, "周期消息部分消息多余,期望收到{0}个周期,实际收到{1}个周期");

	private int		code;	// 错误码
	private String	msg;	// 错误消息

	CheckInfo(int code, String msg)
	{
		this.code = code;
		this.msg = msg;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(this.code).append(";").append(this.msg);
		return sb.toString();
	}

	public int getCode()
	{
		return code;
	}

	public String getMsg()
	{
		return msg;
	}
}