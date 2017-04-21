/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.coretek.common.template.SPTEMsg;

/**
 * ������ù����������𱣴桢���ؼ�����á�
 * 
 * @author ���Ρ
 * @date 2012-1-10
 */
public class CfgManager
{
	private String			icdPath;								// ����ص�icd�ļ�

	private String			monitorNodeId;							// ��ؽڵ�ı��

	private Set<SPTEMsg>	spteMsgs	= new HashSet<SPTEMsg>();

	/**
	 * ��ȡ��ؽڵ���
	 * 
	 * @return
	 */
	public String getMonitorNodeId()
	{
		return monitorNodeId;
	}

	/**
	 * ���ü�ؽڵ�ı��
	 * 
	 * @param monitorNodeId
	 */
	public void setMonitorNodeId(String monitorNodeId)
	{
		this.monitorNodeId = monitorNodeId;
	}

	/**
	 * ��ӱ���ص���Ϣ
	 * 
	 * @param spteMsg
	 */
	public void addSPTEMsg(SPTEMsg spteMsg)
	{
		this.spteMsgs.add(spteMsg);
	}

	/**
	 * ��ӱ���ص���Ϣ
	 * 
	 * @param spteMsgs
	 */
	public void addSPTEMsgs(Collection<SPTEMsg> spteMsgs)
	{
		this.spteMsgs.addAll(spteMsgs);
	}

	/**
	 * ������еļ�����ýڵ���Ϣ
	 * 
	 */
	public void clear()
	{
		this.spteMsgs.clear();
	}

	public String getIcdPath()
	{
		return icdPath;
	}

	public void setIcdPath(String icdPath)
	{
		this.icdPath = icdPath;
	}

	public List<SPTEMsg> getSpteMsgs()
	{
		return new ArrayList<SPTEMsg>(this.spteMsgs);
	}
}