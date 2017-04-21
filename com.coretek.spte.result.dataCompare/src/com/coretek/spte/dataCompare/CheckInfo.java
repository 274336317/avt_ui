package com.coretek.spte.dataCompare;

/**
 * ������Ϣö��
 * 
 * @author Sim.Wang 2012-2-2
 */
public enum CheckInfo
{
	MSG_LOSS(2001, "��Ϣ��ʧ"), MSG_TIMEOUT(2002, "��Ϣ��ʱ,������ʱ:{0}ʵ����ʱ:{1}"), MSG_ERRORVALUE(2003, "����ֵ��ʵ��ֵ��ƥ��"), MSG_UNEXPECTED(2004, "���������ֵ���Ϣ"), PERIOD_PARTLOSS(2005, "������Ϣ������Ϣ��ʧ,�����յ�{0}������,ʵ���յ�{1}������"), PERIOD_PARTEXCESS(2006, "������Ϣ������Ϣ����,�����յ�{0}������,ʵ���յ�{1}������");

	private int		code;	// ������
	private String	msg;	// ������Ϣ

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