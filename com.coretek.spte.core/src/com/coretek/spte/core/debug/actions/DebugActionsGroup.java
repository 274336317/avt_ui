package com.coretek.spte.core.debug.actions;

import org.eclipse.jface.action.IToolBarManager;

import com.coretek.spte.core.debug.DebugCmd;
import com.coretek.spte.core.debug.DebugCmdHanlder;
import com.coretek.spte.core.editor.SPTEEditor;
import com.coretek.spte.core.util.TestCaseStatus;

/**
 * 需要注册到DebugAction.class实例中，以监听调试功能的开启/关闭事件
 * 
 * @author 孙大巍
 * 
 */
public class DebugActionsGroup
{
	private StepIntoAction			stepIntoAction;
	private StepOverAction			stepOverAction;
	private TerminateAction			terminateAction;
	private StepReturnAction		stepOutAction;
	private DebugAction				debugAction;
	private SPTEEditor				editor;
	private TerminateCycleAction	terminateCycleAction;

	public DebugActionsGroup()
	{

	}

	public SPTEEditor getEditor()
	{
		return editor;
	}

	public StepIntoAction getStepIntoAction()
	{
		return this.stepIntoAction;
	}

	public StepOverAction getStepOverAction()
	{
		return this.stepOverAction;
	}

	public TerminateAction getTerminateAction()
	{
		return this.terminateAction;
	}

	public StepReturnAction getStepReturnAction()
	{
		return this.stepOutAction;
	}

	public DebugAction getDebugAction()
	{
		return this.debugAction;
	}

	public TerminateCycleAction getTerminateCycleAction()
	{
		return this.terminateCycleAction;
	}

	public void fireTerminateDebugCmd()
	{
		if (this.terminateAction != null)
		{
			this.terminateAction.run();
		}
	}

	public void setEditor(SPTEEditor editor)
	{
		this.editor = editor;
		if (this.editor != null)
		{
			Object objs[] = this.stepIntoAction.getAllListeners();
			for (Object obj : objs)
			{
				if (obj instanceof DebugCmdHanlder)
				{
					this.stepIntoAction.removePropertyChangeListener((DebugCmdHanlder) obj);
				}
			}
			objs = this.stepOverAction.getAllListeners();
			for (Object obj : objs)
			{
				// 确保当在任何一个编辑器中点击结束调试状态按钮之后，所有编辑器的调试状态都将结束
				if (obj instanceof DebugCmdHanlder)
				{
					this.stepOverAction.removePropertyChangeListener((DebugCmdHanlder) obj);
				}
			}

			objs = this.stepOutAction.getAllListeners();
			for (Object obj : objs)
			{
				// 确保当在任何一个编辑器中点击结束调试状态按钮之后，所有编辑器的调试状态都将结束
				if (obj instanceof DebugCmdHanlder)
				{
					this.stepOutAction.removePropertyChangeListener((DebugCmdHanlder) obj);
				}
			}

			this.stepIntoAction.addPropertyChangeListener(editor.getDebugResposne());
			this.stepOverAction.addPropertyChangeListener(editor.getDebugResposne());
			this.stepOutAction.addPropertyChangeListener(editor.getDebugResposne());

			if (!this.terminateAction.contains(editor.getDebugResposne()))
			{
				this.terminateAction.addPropertyChangeListener(editor.getDebugResposne());
			}

			if (this.editor.getRootContainerMdl().getStatus() == TestCaseStatus.Debug)
			{
				this.setEnable(true);
			} else
			{
				this.setEnable(false);
			}
		}
	}

	public void contributeToToolBar(IToolBarManager toolBarManager)
	{
		// 单步进入调试Action
		this.stepIntoAction = new StepIntoAction(this);
		//单步调试Action
		this.stepOverAction = new StepOverAction(this);
		//跳出单步调试Action
		this.stepOutAction = new StepReturnAction(this);
		//开始执行单步调试Action
		this.debugAction = new DebugAction(this);
		//终止背景周期消息Action
		this.terminateCycleAction = new TerminateCycleAction(this);
		// 全部执行Action
		this.terminateAction = new TerminateAction(this);  

		toolBarManager.add(this.debugAction);
		toolBarManager.add(this.terminateAction);
		toolBarManager.add(this.stepIntoAction);
		toolBarManager.add(this.stepOverAction);
		toolBarManager.add(this.stepOutAction);
		toolBarManager.add(this.terminateCycleAction);
	}

	/**
	 * 设置debug相关按钮的enable
	 * 
	 * @param event </br> <b>作者</b> Sim.Wang </br> <b>日期</b> 2011-7-20
	 */
	public void setBtnEnable(DebugCmd event)
	{
		if (event.equals(DebugCmd.Begin))
		{
			this.setEnable(true);
			this.debugAction.setEnabled(false);
		} else if (event.equals(DebugCmd.Terminate))
		{
			this.setEnable(false);
			this.debugAction.setEnabled(true);
		} else
		{
			this.setEnable(false);
			this.debugAction.setEnabled(false);
			this.terminateAction.setEnabled(true);
		}
	}

	public void setEnable(boolean status)
	{
		this.stepIntoAction.setEnabled(status);
		this.stepOverAction.setEnabled(status);
		this.terminateAction.setEnabled(status);
		this.stepOutAction.setEnabled(status);
		this.terminateCycleAction.setEnabled(status);
		this.debugAction.setEnabled(!status);
	}
}
