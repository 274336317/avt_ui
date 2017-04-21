package com.coretek.spte.ui.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.coretek.common.template.SPTEMsg;

/**
 * SPTEMsg���󻺴棬���ڱ������Ϣ���ݿ���ת������SPTEMsg���ϡ� ע�⣬���಻���̰߳�ȫ�ģ�����Ҫ���ж��̲߳���ʱ�����Լ�ȷ���̰߳�ȫ��
 * 
 * @author Sim.Wang 2012-3-26
 */
public class SPTECache
{
	private List<SPTEMsg>	spteMsgs	= Collections.synchronizedList(new LinkedList<SPTEMsg>());

	// ��ʼʱ�䣬Ҳ����Сʱ��
	private volatile long	beginTime	= -1;

	// ����ʱ�䣬Ҳ�����ʱ��
	private volatile long	endTime		= -1;

	public SPTECache()
	{

	}

	public void addSPTEMsg(SPTEMsg spteMsg)
	{
		spteMsgs.add(spteMsg);
	}

	public List<SPTEMsg> getSPTEMsgs()
	{
		return spteMsgs;
	}

	public void setSPTEMsgs(List<SPTEMsg> spteMsgs)
	{
		this.spteMsgs = spteMsgs;
	}

	public long getBeginTime()
	{
		return beginTime;
	}

	public void setBeginTime(long fromTime)
	{
		this.beginTime = fromTime;
	}

	public long getEndTime()
	{
		return endTime;
	}

	public void setEndTime(long toTime)
	{
		this.endTime = toTime;
	}

	public void clear()
	{
		this.beginTime = -1;
		this.endTime = -1;
		this.spteMsgs.clear();
		this.spteMsgs = Collections.synchronizedList(new LinkedList<SPTEMsg>());
		System.gc();
	}
}
