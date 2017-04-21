package com.coretek.spte.core.models;

import java.util.HashSet;
import java.util.Set;

/**
 * ���Թ���
 * 
 * @author ���Ρ
 * @date 2011-2-19
 */
public class TestToolMdl extends TestMdl
{

	private static final long	serialVersionUID	= -775609236153205081L;

	private Set<String>			ids					= new HashSet<String>();	// ������еĲ��Թ��߽ڵ�

	public Set<String> getIds()
	{
		return ids;
	}

	/**
	 * ��Ӳ��Թ��ߵĽڵ��
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