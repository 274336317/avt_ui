package com.coretek.spte.core.dialogs;

import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.utils.EclipseUtils;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.core.commands.CfgIntervalCmd;
import com.coretek.spte.core.commands.RenameCmd;
import com.coretek.spte.core.editor.SPTEEditor;
import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.IntervalConnMdl;
import com.coretek.spte.core.request.ChangeLineNameRequest;
import com.coretek.spte.core.util.SPTEConstants;
import com.coretek.spte.testcase.TimeSpan;

/**
 * 时间间隔对话框
 * 
 * @author lifs
 * @date 2010-08-20
 * 
 */
public class IntervalDlg extends MessageDialog 
{

	private IntervalConnMdl				connection;

	private AbstractConnectionEditPart	part;

	private Text						text;
	
	private Label                       errorMessage;

	/**
	 * 时间间隔对象
	 */
	private TimeSpan					interval;

	public IntervalDlg(Shell shell, AbtConnMdl element, AbstractConnectionEditPart part)
	{

		super(shell, Messages.getString("I18N_DLG_TITLE_EDIT_INTERVAL"), null, null, MessageDialog.NONE, new String[] { IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL }, 0);
		setShellStyle(SWT.RESIZE | getShellStyle());
		this.setBlockOnOpen(true);
		this.setShellStyle(SWT.APPLICATION_MODAL | SWT.TITLE | SWT.CLOSE);
		this.connection = (IntervalConnMdl) element;
		this.part = part;
	}

	protected Control createCustomArea(Composite parent)
	{
		Composite area = new Composite(parent, SWT.BORDER);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 10;
		layout.marginTop = 30;
		layout.marginBottom = 70;

		area.setLayout(layout);
		area.setLayoutData(new GridData(GridData.FILL_BOTH));
		createTimeIntervalItems(area);

		return area;
	}

	/**
	 * 
	 * @param area
	 */
	private void createTimeIntervalItems(Composite area)
	{
		this.interval = this.connection.getResultInterval();

		Composite body = new Composite(area, SWT.NULL);
		body.setFont(area.getFont());
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		body.setLayout(gridLayout);
		body.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		GridData data = new GridData();
		data.widthHint = 80;
		Label label = new Label(body, SWT.RIGHT);
		label.setText(Messages.getString("I18N_INTERVAL") + ":");
		label.setLayoutData(data);

		this.text = new Text(body, SWT.BORDER);
		data = new GridData();
		data.widthHint = 150;
		this.text.setLayoutData(data);
		text.setFocus();
		
		data = new GridData();
		data.widthHint = 80;
		label = new Label(body, SWT.LEFT);
		label.setText("毫秒");
		label.setLayoutData(data);
		
		this.errorMessage = new Label(body, SWT.NULL | SWT.RIGHT);
		data = new GridData();
		data.widthHint = 200;
		data.horizontalSpan = 3;
		this.errorMessage.setLayoutData(data);
		this.errorMessage.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		
		// 检查输入是否符合标准
		this.text.addModifyListener(new ModifyListener()
		{

			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				Text input = (Text) e.getSource();
				if(getButtonBar()==null) return;
				if(!StringUtils.isNull(input.getText()))
				{
					if(!StringUtils.isPositiveInteger(input.getText()))  //非正整数
					{
						if(StringUtils.isPeriodValueRight(input.getText()))  //符合$value格式
						{
							errorMessage.setText(" ");
							if(null != getButton(IDialogConstants.OK_ID))
							{
								getButton(IDialogConstants.OK_ID).setEnabled(true);
							}
						} else                                           
						{
							errorMessage.setText(Messages.getString("I18N_INPUT_ERR_TIMESPAN"));
							if (null != getButton(IDialogConstants.OK_ID))
							{
								getButton(IDialogConstants.OK_ID).setEnabled(false);
							}
						}
					} else
					{
						errorMessage.setText(" ");
						if(null != getButton(IDialogConstants.OK_ID))
						{
							getButton(IDialogConstants.OK_ID).setEnabled(true);
						}
					}
				} else
				{
					errorMessage.setText(Messages.getString("I18N_TIMESPAN_MUST_NOT_NULL"));
					if (null != getButton(IDialogConstants.OK_ID))
					{
						getButton(IDialogConstants.OK_ID).setEnabled(false);
					}
				}
			}
			
		});
		if (this.connection.getResultInterval() != null)
		{
			this.text.setText(this.connection.getResultInterval().getValue());
		}
	}

	/**
	 * 设置消息名
	 */
	public void setName()
	{
		ChangeLineNameRequest request = new ChangeLineNameRequest(SPTEConstants.CHANGE_MESSAGE_REQUEST_TYPE, (AbtConnMdl) this.part.getModel(), this.interval.getValue());
		RenameCmd command = (RenameCmd) this.part.getCommand(request);
		SPTEEditor eidtor = (SPTEEditor) EclipseUtils.getActiveEditor();
		eidtor.getEditDomain().getCommandStack().execute(command);
	}
	
	@Override
	protected void buttonPressed(int buttonId)
	{
		if (buttonId != 0)
		{
			super.buttonPressed(buttonId);
			return;
		}

		this.interval = this.connection.getResultInterval();

		if (interval == null)
		{
			interval = new TimeSpan();
			interval.setUuid(StringUtils.getUUID());
		}
		this.interval.setValue(this.text.getText());
		CfgIntervalCmd command = new CfgIntervalCmd();
		command.setInterval(this.interval);
		command.setModel(this.connection);
		SPTEEditor eidtor = (SPTEEditor) EclipseUtils.getActiveEditor();
		eidtor.getEditDomain().getCommandStack().execute(command);
		super.buttonPressed(buttonId);
	}
}