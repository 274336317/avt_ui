package com.coretek.spte.monitor.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.monitor.MiddleSequence;
import com.coretek.spte.monitor.SequenceViewPart;
import com.coretek.spte.monitor.SequenceWithTime;

/**
 * 配置用例监控视图
 * 
 * @author 孙大巍
 * @date 2012-2-2
 */
public class CfgActionDelegate implements IViewActionDelegate
{

	private SequenceViewPart	viewPart;

	@Override
	public void init(IViewPart view)
	{
		this.viewPart = (SequenceViewPart) view;

	}

	@Override
	public void run(IAction action)
	{
		if (this.viewPart.getSequence() == null)
			return;

		CfgDialog dialog = new CfgDialog((SequenceWithTime) this.viewPart.getSequence());
		if (MessageDialog.OK == dialog.open())
			this.viewPart.updateCfg(dialog.getValue(), dialog.getBounds());
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection)
	{

	}

}

/**
 * 配置时序图的刻度值与时间范围
 * 
 * @author SunDawei 2012-5-29
 */
class CfgDialog extends MessageDialog
{

	private Text				txtValue;

	private Text				txtBounds;

	private int					value;		// 刻度值

	private int					bounds;	// 范围

	private SequenceWithTime	viewPart;

	public CfgDialog(SequenceWithTime viewPart)
	{
		super(Display.getDefault().getActiveShell(), Messages.getString("setCfg"), null, null, MessageDialog.NONE, new String[] { IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL }, 0);
		this.viewPart = viewPart;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.MessageDialog#createCustomArea(org.eclipse.
	 * swt.widgets.Composite) <br/> <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-2
	 */
	@Override
	protected Control createCustomArea(Composite parent)
	{
		Composite panel = new Composite(parent, SWT.BORDER);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		panel.setLayout(layout);

		Label label = new Label(panel, SWT.RIGHT);
		label.setText(Messages.getString("timeScale"));
		GridData gridData = new GridData();
		gridData.widthHint = 80;
		gridData.heightHint = 15;
		label.setLayoutData(gridData);

		this.txtValue = new Text(panel, SWT.BORDER);
		this.txtValue.setText(String.valueOf(this.viewPart.getScale()));
		gridData = new GridData();
		gridData.widthHint = 100;
		gridData.heightHint = 15;
		this.txtValue.setLayoutData(gridData);
		if (viewPart instanceof MiddleSequence)
		{
			MiddleSequence middle = (MiddleSequence) viewPart;
			label = new Label(panel, SWT.RIGHT);
			label.setText(Messages.getString("timeBound"));
			gridData = new GridData();
			gridData.widthHint = 80;
			gridData.heightHint = 15;
			label.setLayoutData(gridData);

			txtBounds = new Text(panel, SWT.BORDER);
			this.txtBounds.setText(String.valueOf(middle.getTimeBound()));
			gridData = new GridData();
			gridData.widthHint = 100;
			gridData.heightHint = 15;
			txtBounds.setLayoutData(gridData);
		}

		return panel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.MessageDialog#buttonPressed(int) <br/>
	 * <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-2
	 */
	@Override
	protected void buttonPressed(int buttonId)
	{
		if (MessageDialog.OK == buttonId)
		{
			if (StringUtils.isNull(this.txtValue.getText()) || !StringUtils.isNumber(this.txtValue.getText()))
			{
				return;
			}
			if (this.viewPart instanceof MiddleSequence)
				if (StringUtils.isNull(this.txtBounds.getText()) || !StringUtils.isNumber(this.txtBounds.getText()))
				{
					return;
				}

			this.value = Integer.valueOf(this.txtValue.getText());
			if (value <= 0)
				return;
		}

		super.buttonPressed(buttonId);
	}

	/**
	 * @return the value <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-2
	 */
	public int getValue()
	{
		return value;
	}

	/**
	 * @return the bounds <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2012-2-2
	 */
	public int getBounds()
	{
		return bounds;
	}

}