/************************************************************************
 *				北京科银京成技术有限公司 版权所有
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
 * 监控配置管理器。负责保存、加载监控配置。
 * 
 * @author 孙大巍
 * @date 2012-1-10
 */
public class CfgManager
{
	private String			icdPath;								// 被监控的icd文件

	private String			monitorNodeId;							// 监控节点的编号

	private Set<SPTEMsg>	spteMsgs	= new HashSet<SPTEMsg>();

	/**
	 * 获取监控节点编号
	 * 
	 * @return
	 */
	public String getMonitorNodeId()
	{
		return monitorNodeId;
	}

	/**
	 * 设置监控节点的编号
	 * 
	 * @param monitorNodeId
	 */
	public void setMonitorNodeId(String monitorNodeId)
	{
		this.monitorNodeId = monitorNodeId;
	}

	/**
	 * 添加被监控的消息
	 * 
	 * @param spteMsg
	 */
	public void addSPTEMsg(SPTEMsg spteMsg)
	{
		this.spteMsgs.add(spteMsg);
	}

	/**
	 * 添加被监控的消息
	 * 
	 * @param spteMsgs
	 */
	public void addSPTEMsgs(Collection<SPTEMsg> spteMsgs)
	{
		this.spteMsgs.addAll(spteMsgs);
	}

	/**
	 * 清除所有的监控配置节点消息
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