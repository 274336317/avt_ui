/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.spte.monitor.ui.preference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.coretek.spte.monitor.ui.MonitorPlugin;

@SuppressWarnings("unchecked")
public abstract class AbstractPreferencePage extends PreferencePage implements IWorkbenchPreferencePage
{

	protected Map						fCheckBoxes				= new HashMap();

	private SelectionListener			fCheckBoxListener		= new SelectionListener()
																{
																	public void widgetDefaultSelected(SelectionEvent e)
																	{
																	}

																	public void widgetSelected(SelectionEvent e)
																	{
																		Button button = (Button) e.widget;
																		fOverlayStore.setValue((String) fCheckBoxes.get(button), button.getSelection());
																	}
																};

	protected Map						fColorButtons			= new HashMap();

	private ArrayList					fMasterSlaveListeners	= new ArrayList();

	private ModifyListener				fNumberFieldListener	= new ModifyListener()
																{
																	public void modifyText(ModifyEvent e)
																	{
																		numberFieldChanged((Text) e.widget);
																	}
																};
	protected ArrayList					fNumberFields			= new ArrayList();

	protected OverlayPreferenceStore	fOverlayStore;
	private ModifyListener				fTextFieldListener		= new ModifyListener()
																{
																	public void modifyText(ModifyEvent e)
																	{
																		Text text = (Text) e.widget;
																		fOverlayStore.setValue((String) fTextFields.get(text), text.getText());
																	}
																};

	protected Map						fTextFields				= new HashMap();

	public AbstractPreferencePage()
	{
		super();
		setPreferenceStore(getPreferenceStore());
		fOverlayStore = new OverlayPreferenceStore(getPreferenceStore(), createOverlayStoreKeys());
	}

	protected Button addCheckBox(Composite parent, String label, String key, int indentation)
	{
		Button checkBox = new Button(parent, SWT.CHECK);
		checkBox.setText(label);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalIndent = indentation;
		gd.horizontalSpan = 2;
		checkBox.setLayoutData(gd);
		checkBox.addSelectionListener(fCheckBoxListener);

		fCheckBoxes.put(checkBox, key);

		return checkBox;
	}

	protected void addFiller(Composite composite)
	{
		PixelConverter pixelConverter = new PixelConverter(composite);
		Label filler = new Label(composite, SWT.LEFT);
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalSpan = 2;
		gd.heightHint = pixelConverter.convertHeightInCharsToPixels(1) / 2;
		filler.setLayoutData(gd);
	}

	protected Group addGroupBox(Composite parent, String label, int nColumns)
	{
		Group group = new Group(parent, SWT.NONE);
		group.setText(label);
		GridLayout layout = new GridLayout();
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		layout.numColumns = nColumns;
		group.setLayout(layout);
		group.setLayoutData(gd);
		return group;
	}

	protected Button addRadioButton(Composite parent, String label, String key, int indentation)
	{
		Button radioButton = new Button(parent, SWT.RADIO);
		radioButton.setText(label);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalIndent = indentation;
		gd.horizontalSpan = 2;
		radioButton.setLayoutData(gd);
		radioButton.addSelectionListener(fCheckBoxListener);

		fCheckBoxes.put(radioButton, key);

		return radioButton;
	}

	protected Control addTextField(Composite composite, String label, String key, int textLimit, int indentation, boolean isNumber)
	{

		Label labelControl = new Label(composite, SWT.NONE);
		labelControl.setText(label);
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalIndent = indentation;
		labelControl.setLayoutData(gd);

		Text textControl = new Text(composite, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.widthHint = convertWidthInCharsToPixels(textLimit + 1);
		textControl.setLayoutData(gd);
		textControl.setTextLimit(textLimit);
		fTextFields.put(textControl, key);
		if (isNumber)
		{
			fNumberFields.add(textControl);
			textControl.addModifyListener(fNumberFieldListener);
		} else
		{
			textControl.addModifyListener(fTextFieldListener);
		}

		return textControl;
	}

	protected void createDependency(final Button master, String masterKey, final Control slave)
	{
		indent(slave);
		boolean masterState = fOverlayStore.getBoolean(masterKey);
		slave.setEnabled(masterState);
		SelectionListener listener = new SelectionListener()
		{
			public void widgetDefaultSelected(SelectionEvent e)
			{
			}

			public void widgetSelected(SelectionEvent e)
			{
				slave.setEnabled(master.getSelection());
			}
		};
		master.addSelectionListener(listener);
		fMasterSlaveListeners.add(listener);
	}

	private IStatus validatePositiveNumber(String number)
	{
		StatusInfo status = new StatusInfo();
		if (number.length() == 0)
		{
			status.setError("输入为空"); //$NON-NLS-1$
		} else
		{
			try
			{
				int value = Integer.parseInt(number);
				if (value < 0)
					status.setError("非法输入"); //$NON-NLS-1$
			} catch (NumberFormatException e)
			{
				status.setError("非法输入"); //$NON-NLS-1$
			}
		}
		return status;
	}

	protected void updateStatus(IStatus status)
	{
		if (!status.matches(IStatus.ERROR))
		{
			for (int i = 0; i < fNumberFields.size(); i++)
			{
				Text text = (Text) fNumberFields.get(i);
				IStatus s = validatePositiveNumber(text.getText());
				status = StatusUtil.getMoreSevere(s, status);
			}
		}
		setValid(!status.matches(IStatus.ERROR));
		StatusUtil.applyToStatusLine(this, status);
	}

	protected abstract OverlayPreferenceStore.OverlayKey[] createOverlayStoreKeys();

	/*
	 * @see DialogPage#dispose()
	 */
	public void dispose()
	{
		if (fOverlayStore != null)
		{
			fOverlayStore.stop();
			fOverlayStore = null;
		}
		super.dispose();
	}

	public IPreferenceStore getPreferenceStore()
	{
		return MonitorPlugin.getDefault().getPreferenceStore();
	}

	protected void indent(Control control)
	{
		GridData gridData = new GridData();
		gridData.horizontalIndent = 20;
		control.setLayoutData(gridData);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench)
	{
	}

	protected void initializeFields()
	{

		Iterator e = fCheckBoxes.keySet().iterator();
		while (e.hasNext())
		{
			Button b = (Button) e.next();
			String key = (String) fCheckBoxes.get(b);
			b.setSelection(fOverlayStore.getBoolean(key));
		}

		e = fTextFields.keySet().iterator();
		while (e.hasNext())
		{
			Text t = (Text) e.next();
			String key = (String) fTextFields.get(t);
			t.setText(fOverlayStore.getString(key));
		}
	}

	protected void numberFieldChanged(Text textControl)
	{
		String number = textControl.getText();
		IStatus status = validatePositiveNumber(number);
		if (!status.matches(IStatus.ERROR))
			fOverlayStore.setValue((String) fTextFields.get(textControl), number);
		updateStatus(status);
	}

	/*
	 * @see PreferencePage#performDefaults()
	 */
	protected void performDefaults()
	{
		fOverlayStore.loadDefaults();
		initializeFields();
		super.performDefaults();
	}

	@SuppressWarnings("deprecation")
	public boolean performOk()
	{
		fOverlayStore.propagate();
		MonitorPlugin.getDefault().savePluginPreferences();
		return true;
	}

}
