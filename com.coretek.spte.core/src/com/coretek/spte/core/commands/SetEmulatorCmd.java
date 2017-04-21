package com.coretek.spte.core.commands;

import org.eclipse.gef.commands.Command;

import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.spte.core.models.TestMdl;

/**
 * 设置测试用具的Emulator属性
 * 
 * @author 孙大巍
 * @date 2011-1-13
 */
public class SetEmulatorCmd extends Command
{

	private Entity	oldEmulator;

	private Entity	emulator;

	private TestMdl	lifelineModel;

	public SetEmulatorCmd(TestMdl lifelineModel)
	{
		this.lifelineModel = lifelineModel;
	}

	public TestMdl getLifelineModel()
	{
		return lifelineModel;
	}

	public Entity getEmulator()
	{
		return emulator;
	}

	public void setEmulator(Entity emulator)
	{
		this.emulator = emulator;
	}

	public void setLifelineModel(TestMdl lifelineModel)
	{
		this.lifelineModel = lifelineModel;
	}

	@Override
	public void execute()
	{
		this.oldEmulator = this.lifelineModel.getEmulator();
		this.lifelineModel.setEmulator(this.emulator);

	}

	@Override
	public void redo()
	{
		this.execute();
	}

	@Override
	public void undo()
	{
		this.lifelineModel.setEmulator(this.oldEmulator);
	}
}
