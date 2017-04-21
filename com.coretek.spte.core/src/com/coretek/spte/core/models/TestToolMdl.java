package com.coretek.spte.core.models;

import java.util.HashSet;
import java.util.Set;

/**
 * 测试工具
 * 
 * @author 孙大巍
 * @date 2011-2-19
 */
public class TestToolMdl extends TestMdl
{

	private static final long	serialVersionUID	= -775609236153205081L;

	private Set<String>			ids					= new HashSet<String>();	// 存放所有的测试工具节点

	public Set<String> getIds()
	{
		return ids;
	}

	/**
	 * 添加测试工具的节点号
	 * 
	 * @param id
	 */
	public void addId(String id)
	{
		this.ids.add(id);
		this.firePropertyChange("prop_id_set_changed", null);
	}

	public void removeId(String id)
	{
		this.ids.remove(id);
		this.firePropertyChange("prop_id_set_changed", null);
	}
}