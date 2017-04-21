package com.coretek.testcase.projectView.projectwizard.page;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * 消息监控工程创建的向导页
 * 
 * @author 孙大巍
 * @date 2010-11-26
 * 
 */
public class MessageMonitorProjectCreationPage extends AbstractNewProjectCreationPage
{
	/**
	 * 查看曲线绘图器
	 */
	private Button	checkbox;

	/**
	 * 查看曲线绘图器
	 */
	private Label	label;

	private boolean	selected	= false;

	public boolean isSelected()
	{
		return selected;
	}

	public MessageMonitorProjectCreationPage(String pageName, IStructuredSelection selection)
	{
		super(pageName, selection);
	}

	@Override
	protected void createUserEntryArea(Composite composite, boolean defaultEnabled)
	{
		super.createUserEntryArea(composite, defaultEnabled);

		Composite panel = new Composite(composite, SWT.NONE);
		GridLayout grid = new GridLayout();
		grid.numColumns = 3;
		panel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		panel.setLayout(grid);

		GridData gd = new GridData();
		checkbox = new Button(panel, SWT.CHECK);
		checkbox.setLayoutData(gd);
		checkbox.addSelectionListener(new SelectionListener()
		{
			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			public void widgetSelected(SelectionEvent e)
			{
				selected = !selected;
			}
		});
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		label = new Label(panel, SWT.NONE);
		label.setLayoutData(gd);
		label.setText("打开曲线视图");
	}

}
