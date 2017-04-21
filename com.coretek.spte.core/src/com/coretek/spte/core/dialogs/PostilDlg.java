/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.core.dialogs;

import org.eclipse.draw2d.TextUtilities;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.coretek.common.utils.StringUtils;
import com.coretek.spte.core.models.PostilChildMdl;

/**
 * To show the comments and provide input interface
 * 
 * @author SunDawei 2012-5-18
 */
public class PostilDlg extends MessageDialog
{

	private PostilChildMdl	postilChildMdl;

	private Text			text;

	private String			contents;

	public PostilDlg(Shell parentShell, PostilChildMdl postilChildMdl)
	{
		super(Display.getDefault().getActiveShell(), "±à¼­×¢ÊÍ", null, null, MessageDialog.NONE, new String[] { IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL }, 0);
		this.postilChildMdl = postilChildMdl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.MessageDialog#createCustomArea(org.eclipse.
	 * swt.widgets.Composite) <br/> <b>Author</b> SunDawei </br> <b>Date</b>
	 * 2012-5-18
	 */
	@Override
	protected Control createCustomArea(Composite parent)
	{
		Composite panel = new Composite(parent, SWT.BORDER);
		GridData data = new GridData(GridData.FILL_BOTH);
		panel.setLayoutData(data);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		panel.setLayout(layout);

		text = new Text(panel, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		text.setText(postilChildMdl.getText());
		data = new GridData();
		data.widthHint = 400;
		data.heightHint = 300;
		text.setLayoutData(data);

		return panel;
	}

	public String getText()
	{
		return this.contents;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.MessageDialog#buttonPressed(int) <br/>
	 * <b>Author</b> SunDawei </br> <b>Date</b> 2012-5-18
	 */
	@Override
	protected void buttonPressed(int buttonId)
	{
		if (Window.CANCEL != buttonId)
		{
			contents = text.getText();
			if (StringUtils.isNull(contents))
			{
				contents = "±êÇ©";
			} else
			{
				contents = contents.trim();
			}
			Dimension dimension = TextUtilities.INSTANCE.getTextExtents(text.getText(), text.getFont());
			if (dimension.width > 300)
			{
				dimension.width = 300;
			}
			if (dimension.height > 200)
			{
				dimension.height = 200;
			}
			Rectangle rect = this.postilChildMdl.getConstraints();
			rect.setSize(dimension);
			this.postilChildMdl.setConstraints(rect);
		}

		super.buttonPressed(buttonId);
	}
}