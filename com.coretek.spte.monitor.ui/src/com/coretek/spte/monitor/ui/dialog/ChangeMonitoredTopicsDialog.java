/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.ui.dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.logging.LoggingPlugin;
import com.coretek.common.template.ClazzManager;
import com.coretek.common.template.Constants;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.monitor.manager.ExecutorSession;
import com.coretek.spte.monitor.manager.TopicVisualComponent;

/**
 * 更改被监控主题对话框
 * 
 * @author 孙大巍 2012-4-10
 */
public class ChangeMonitoredTopicsDialog extends MessageDialog
{

	private final static Logger		logger	= LoggingPlugin.getLogger(ChangeMonitoredTopicsDialog.class);

	private TopicVisualComponent	topicVisualComponent;

	private ClazzManager			icdManager;

	/**
	 * 
	 * @param parentShell
	 */
	public ChangeMonitoredTopicsDialog(Shell parentShell, ClazzManager icdManager)
	{
		super(parentShell, Messages.getString("ChangeMonitoredTopicsAction_Change_Monitor_Topics"), null, null, MessageDialog.NONE, new String[] { IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL }, 0);
		this.icdManager = icdManager;
	}

	/**
	 * 获取被选中的主题
	 * 
	 * @return
	 */
	public List<SPTEMsg> getSelectedTopics()
	{

		return this.topicVisualComponent.getSelectedTopics();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.MessageDialog#createCustomArea(org.eclipse.
	 * swt.widgets.Composite)
	 */
	@Override
	protected Control createCustomArea(Composite parent)
	{
		Composite panel = new Composite(parent, SWT.BORDER);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		panel.setLayout(layout);

		GridData gridData = new GridData(GridData.FILL_BOTH);
		panel.setLayoutData(gridData);

		this.topicVisualComponent = new TopicVisualComponent(panel);
		this.topicVisualComponent.showContents(this.icdManager.getAllTopics());
		List<Entity> topics = new ArrayList<Entity>();
		ExecutorSession session = ExecutorSession.getInstance();
		SPTEMsg[] spteMsgs = session.getCfgSPTEMsgs();
		for (SPTEMsg msg : spteMsgs)
		{
			String topicSymbol = msg.getICDMsg().getAttribute(Constants.ICD_TOPIC_SYMBOL).getValue().toString();
			Entity entity = this.icdManager.getTopic(topicSymbol);
			if (entity != null)
			{
				topics.add(entity);
			} else
			{
				logger.warning(StringUtils.concat("根据topicSymbol=", topicSymbol, " 找不到对应的主题。"));
			}
		}
		this.topicVisualComponent.check(topics);

		return panel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.MessageDialog#buttonPressed(int) <br/>
	 */
	@Override
	protected void buttonPressed(int buttonId)
	{
		super.buttonPressed(buttonId);
	}

}