package com.coretek.spte.core.commands;

import org.eclipse.gef.commands.Command;

import com.coretek.spte.core.models.MsgConnMdl;

/**
 * ������Ϣ���ߵĸ��࣬����ͳ�Ƴ����Թ����а����Ľڵ�Id
 * 
 * @author ���Ρ
 * 
 *         2011-3-29
 */
public abstract class ConnCommand extends Command
{

	/**
	 * ���²��Թ��ߵ�Id���ϣ��Է�����ʾ���Թ��ߵ�Id
	 * 
	 * @param id
	 * @param add ���Ϊtrue����Ϊ��Ӳ�����Ϊfalse����Ϊɾ������
	 */
	public void updateIdSet(MsgConnMdl model, String id, boolean add)
	{

	}
}