package com.coretek.spte.monitor;

import java.util.ArrayList;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.coretek.spte.testcase.Message;

public class LossInfoDialog extends MessageDialog
{
	private List					lossList;
	private Text					detailText;
	private java.util.List<Message>	messageList;

	private Map<Message, Integer>	messageMap;

	public LossInfoDialog(Shell parentShell, Map<Message, Integer> messageMap)
	{
		super(parentShell, "¶ªÊ§ÏûÏ¢", null, null, MessageDialog.NONE, new String[] { IDialogConstants.CLOSE_LABEL }, 0);
		this.messageMap = messageMap;
		messageList = new ArrayList<Message>();
		this.messageList.addAll(messageMap.keySet());
	}

	@Override
	protected Control createCustomArea(Composite parent)
	{
		Composite composite = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.widthHint = 830;
		gridData.heightHint = 480;
		composite.setLayoutData(gridData);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		composite.setLayout(layout);
		lossList = new List(composite, SWT.NONE);
		GridData gd1 = new GridData(SWT.FILL, SWT.FILL, false, false);
		gd1.heightHint = 100;
		lossList.setLayoutData(gd1);
		for (Message message : messageMap.keySet())
		{
			StringBuilder sb = new StringBuilder(message.getName());
			if (message.isPeriodMessage())
			{
				sb.append("(" + messageMap.get(message).intValue()).append("/").append(message.getPeriodCount()).append(")");
			}
			lossList.add(sb.toString());
		}
		lossList.select(0);
		detailText = new Text(composite, SWT.MULTI | SWT.READ_ONLY | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd2 = new GridData(GridData.FILL_BOTH);
		gd2.grabExcessVerticalSpace = true;
		detailText.setLayoutData(gd2);
		detailText.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		detailText.setText(messageList.get(0).toString());
		lossList.addSelectionListener(new SelectionListener()
		{

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				int i = lossList.getSelectionIndex();
				detailText.setText(messageList.get(i).toString());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{
				// TODO Auto-generated method stub

			}
		});
		return composite;
	}

}
