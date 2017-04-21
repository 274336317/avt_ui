/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.cfg;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.coretek.common.template.ClazzManager;
import com.coretek.common.template.ICDFunctionDomain;
import com.coretek.common.template.ICDFunctionNodeMsg;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.template.TemplateEngine;
import com.coretek.common.template.TemplateUtils;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.monitor.manager.ExecutorSession;
import com.coretek.spte.monitor.manager.ICDFunctionNodeMsgComparer;
import com.coretek.spte.monitor.manager.MonitorManagerPlugin;
import com.coretek.spte.monitor.manager.TopicComparer;

/**
 * ������öԻ���
 * 
 * @author ���Ρ
 * @date 2012-1-10
 */
public class CfgDialog extends MessageDialog
{
	private CheckboxTreeViewer		viewer;

	private CheckboxTreeViewer		topicViewer;

	private Combo					filterText;

	private Button					button;											// �����ť

	private Combo					cmbICDFiles;									// ICD�ļ�������

	private String					icdPath;										// �û�ѡ���ICD�ļ�·��

	private Combo					endianCombo;									// ��С��������

	private String					endian;											// ��С��
																					// :big��little

	private List<SPTEMsg>			selectedMsgs		= new ArrayList<SPTEMsg>();

	private Group					endianPanel;									// ��С��������

	private static CfgDialog		instance			= new CfgDialog();

	private MonitorConfigProcesser	processer;										// �Լ�����ý��д���Ĵ����������ڶ�ȡ������ã�����������

	private Combo					configCombo;									// ��������ļ�������

	private Button					saveButton;									// ��������ļ����水ť

	private Text					nodeText;										// ��ؽڵ������

	public static final String		MONITOR_CONF_DIR	= "config";				// ��������ļ�����Ŀ¼
	public static final String		MONITOR_FILE_SUFFIX	= ".cfg";					// ��������ļ���׺��
	private String					confDir;

	/**
	 * @param parentShell
	 */
	public CfgDialog(Shell parentShell)
	{
		super(parentShell, "�������", null, null, MessageDialog.NONE, new String[] { IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL }, 0);
	}

	private CfgDialog()
	{
		this(Display.getCurrent().getActiveShell());
		processer = new MonitorConfigProcesser(this);
		confDir = MonitorManagerPlugin.getDefault().getStateLocation().append(MONITOR_CONF_DIR).toOSString();
		File file = new File(confDir);
		if (!file.isDirectory())
		{
			file.mkdirs();
		}
	}

	Combo getEndianCombo()
	{
		return this.endianCombo;
	}

	Combo getICDFilesCombo()
	{
		return this.cmbICDFiles;
	}

	CheckboxTreeViewer getViewer()
	{
		return this.viewer;
	}

	CheckboxTreeViewer getTopicViewer()
	{
		return this.topicViewer;
	}

	public String getConfDir()
	{
		return confDir;
	}

	public void setConfDir(String confDir)
	{
		this.confDir = confDir;
	}

	/**
	 * ������ȡ������öԻ���
	 * 
	 * @return
	 */
	public static CfgDialog getInstance()
	{
		return instance;
	}

	/**
	 * ��ȡ�û����õ�ICD�ļ�·��
	 * 
	 * @return
	 */
	public String getIcdPath()
	{
		return icdPath;
	}

	/**
	 * ��ȡ�û�ѡ��ļ����Ϣ���󼯺�
	 * 
	 * @return
	 */
	public List<SPTEMsg> getSelectedMsgs()
	{
		return selectedMsgs;
	}

	/**
	 * Add the referred icd file's path
	 * 
	 * @param path
	 */
	public void setICDFile(String path)
	{
		this.icdPath = path;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.MessageDialog#createCustomArea(org.eclipse.
	 * swt.widgets.Composite)
	 */
	@Override
	protected Control createCustomArea(final Composite parent)
	{
		GridData gridData = new GridData(GridData.FILL_BOTH);
		Composite panel = new Composite(parent, SWT.NONE);
		panel.setLayoutData(gridData);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		panel.setLayout(layout);

		makeLoldingPanel(panel);

		this.createEndPanel(panel);

		this.createNodePanel(panel);

		this.crateMsgPanel(panel);

		this.createTopicPanel(panel);

		viewer.addCheckStateListener(new CheckStateListener(this.viewer, this.topicViewer));
		topicViewer.addCheckStateListener(new TopicViewerCheckStateListener(topicViewer, viewer));

		button.addSelectionListener(new SelectionListener()
		{
			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{
			}

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				FileDialog dialog = new FileDialog(parent.getShell(), SWT.OPEN);
				String path = dialog.open();
				if (StringUtils.isNotNull(path))
				{
					cmbICDFiles.add(path);
					icdPath = path;
					cmbICDFiles.select(0);
				}
			}
		});

		return panel;
	}

	/**
	 * ��װ��С��ѡ�����
	 * 
	 * @param panel
	 */
	private void createEndPanel(Composite panel)
	{
		// ��С��ѡ�����
		endianPanel = new Group(panel, SWT.SHADOW_ETCHED_IN);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		endianPanel.setLayout(layout);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 2;
		endianPanel.setLayoutData(gridData);
		endianPanel.setText("��С��");

		Label label = new Label(endianPanel, SWT.RIGHT);
		label.setText("��С��:");

		endianCombo = new Combo(endianPanel, SWT.BORDER | SWT.READ_ONLY); // ��������Ϊֻ�������������û�����
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		endianCombo.setLayoutData(gridData);
		endianCombo.setItems(new String[] { "���", "С��" });
		// ��ExecutorSession�ж�ȡ��С�˲���
		if (ExecutorSession.getInstance() == null || ExecutorSession.getInstance().getEndian() == null)
		{
			endianCombo.select(1);
		} else
		{
			if (ExecutorSession.getInstance().getEndian().endsWith("little"))
			{
				endianCombo.select(1);
			} else
			{
				endianCombo.select(0);
			}
		}
		if (endianCombo.getText() != null && endianCombo.getSelectionIndex() == 0)
		{
			endian = "big";
		} else
		{
			endian = "little";
		}

	}

	/**
	 * ��װ��Ϣ���
	 * 
	 * @param panel
	 */
	private void crateMsgPanel(Composite panel)
	{
		// ICD�ļ�ѡ�����
		Group icdPanel = new Group(panel, SWT.SHADOW_ETCHED_IN);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		icdPanel.setLayout(layout);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 2;
		icdPanel.setLayoutData(gridData);
		icdPanel.setText("ICD�ļ�");

		Label label = new Label(icdPanel, SWT.RIGHT);
		label.setText("ICD�ļ�:");

		cmbICDFiles = new Combo(icdPanel, SWT.BORDER | SWT.READ_ONLY); // ��������Ϊֻ�������������û�����
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		cmbICDFiles.setLayoutData(gridData);
		if (icdPath != null)
		{
			this.cmbICDFiles.add(this.icdPath);
			this.cmbICDFiles.select(0);
		}
		cmbICDFiles.addModifyListener(new ICDFilesModifyListener(this, viewer, topicViewer));

		button = new Button(icdPanel, SWT.NONE);
		button.setText("���...");
		gridData = new GridData();
		gridData.widthHint = 70;
		gridData.heightHint = 25;
		button.setLayoutData(gridData);
		// ICD��Ϣ���
		Group group = new Group(panel, SWT.SHADOW_IN);
		group.setText("���ܶ�����Ϣ");
		gridData = new GridData();
		gridData.widthHint = 400;
		gridData.heightHint = 360;
		group.setLayoutData(gridData);

		layout = new GridLayout();
		layout.numColumns = 3;
		group.setLayout(layout);
		// ���������
		label = new Label(group, SWT.RIGHT);
		label.setText("��Ϣ����:");
		gridData = new GridData();
		gridData.widthHint = 55;
		gridData.heightHint = 15;
		label.setLayoutData(gridData);

		this.filterText = new Combo(group, SWT.BORDER);
		gridData = new GridData();
		gridData.heightHint = 15;
		gridData.widthHint = 235;
		this.filterText.setLayoutData(gridData);

		Button btn_ok = new Button(group, SWT.NONE);
		btn_ok.setText("ȷ��");
		gridData = new GridData();
		gridData.widthHint = 70;
		gridData.heightHint = 25;
		btn_ok.setLayoutData(gridData);

		viewer = new CheckboxTreeViewer(group, SWT.BORDER);
		gridData = new GridData();
		gridData.widthHint = 370;
		gridData.heightHint = 300;
		gridData.horizontalSpan = 3;
		viewer.getTree().setLayoutData(gridData);
		viewer.setContentProvider(new ContentProvider());
		viewer.setLabelProvider(new LabelProvider());

		if (icdPath != null)
		{
			ClazzManager icdManager = TemplateEngine.getEngine().parseICD(new File(this.icdPath));
			List<ICDFunctionDomain> domains = TemplateUtils.getAllFunctionDomains(icdManager);
			viewer.setInput(domains);
			viewer.expandAll();
		}
		viewer.setComparer(new ICDFunctionNodeMsgComparer());

	}

	/**
	 * ��װ�������
	 * 
	 * @param panel
	 */
	private void createTopicPanel(Composite panel)
	{
		// �������
		Group group1 = new Group(panel, SWT.SHADOW_IN);
		group1.setText("����");
		GridData gridData = new GridData();
		gridData.widthHint = 400;
		gridData.heightHint = 360;
		group1.setLayoutData(gridData);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		group1.setLayout(layout);

		Label label = new Label(group1, SWT.RIGHT);
		label.setText("��������:");
		gridData = new GridData();
		gridData.widthHint = 55;
		gridData.heightHint = 15;
		label.setLayoutData(gridData);

		Combo topicFilter = new Combo(group1, SWT.BORDER);
		gridData = new GridData();
		gridData.widthHint = 235;
		gridData.heightHint = 15;
		topicFilter.setLayoutData(gridData);

		Button btn_ok = new Button(group1, SWT.NONE);
		btn_ok.setText("ȷ��");
		gridData = new GridData();
		gridData.widthHint = 70;
		gridData.heightHint = 25;
		btn_ok.setLayoutData(gridData);

		topicViewer = new CheckboxTreeViewer(group1, SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalSpan = 3;
		gridData.widthHint = 370;
		gridData.heightHint = 300;
		topicViewer.getTree().setLayoutData(gridData);
		topicViewer.setContentProvider(new ContentProvider());
		topicViewer.setLabelProvider(new TopicTreeLabelProvider());
		// topicViewer.addCheckStateListener(new
		// TopicViewerCheckStateListener(topicViewer, viewer));
		if (icdPath != null)
		{
			ClazzManager icdManager = TemplateEngine.getEngine().parseICD(new File(this.icdPath));
			this.topicViewer.setInput(icdManager.getAllTopics());
			this.topicViewer.expandAll();
		}
		topicViewer.setComparer(new TopicComparer());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.MessageDialog#buttonPressed(int)
	 */
	@Override
	protected void buttonPressed(int buttonId)
	{
		if (buttonId == Window.CANCEL)
		{
			super.buttonPressed(buttonId);
			return;
		}
		if (saveButton.getSelection())
		{
			String fileName = configCombo.getText();
			if (fileName == null || fileName.trim().length() == 0)
			{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
				fileName = sdf.format(new Date());
			}
			if (!fileName.endsWith(MONITOR_FILE_SUFFIX))
			{
				fileName = fileName + MONITOR_FILE_SUFFIX;
			}
			try
			{
				processer.write(StringUtils.concat(confDir, File.separator, fileName));
			} catch (IOException e)
			{
				e.printStackTrace();
				MessageDialog.openError(saveButton.getShell(), "����", StringUtils.concat("����ļ�����ʧ��", e.getMessage()));
			}
		}
		/* ���û����õļ�ؽڵ���Ϣ���浽���ò������� */
		Object[] objs = this.viewer.getCheckedElements();
		selectedMsgs.clear();
		for (Object obj : objs)
		{
			if (obj instanceof ICDFunctionNodeMsg)
			{
				ICDFunctionNodeMsg nodeMsg = (ICDFunctionNodeMsg) obj;
				if (nodeMsg.getSpteMsg() != null)
					this.selectedMsgs.add(nodeMsg.getSpteMsg());
			}
		}

		if (endianCombo.getText() != null && endianCombo.getSelectionIndex() == 0)
		{
			endian = "big";
		} else
		{
			endian = "little";
		}
		if (cmbICDFiles.getText() != null && !cmbICDFiles.getText().equals(""))
		{
			icdPath = cmbICDFiles.getText();
		}
		super.buttonPressed(buttonId);
	}

	public String getEndian()
	{
		return endian;
	}

	/**
	 * ���Ƽ�������ļ�����
	 * 
	 * @param parent
	 */
	protected void makeLoldingPanel(Composite parent)
	{
		Composite loadingGroup = new Composite(parent, SWT.NONE);
		GridData gd = new GridData(GridData.FILL, GridData.FILL, true, true);
		gd.horizontalSpan = 2;
		loadingGroup.setLayoutData(gd);
		GridLayout layout = new GridLayout();
		layout.numColumns = 4;
		loadingGroup.setLayout(layout);
		Label label = new Label(loadingGroup, SWT.NONE);
		label.setText("�����ļ�:");
		configCombo = new Combo(loadingGroup, SWT.DROP_DOWN);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 15;
		configCombo.setLayoutData(gridData);
		configCombo.setItems(processer.listAllFile());
		configCombo.addModifyListener(new ConfigModifyListener(this, processer));
		saveButton = new Button(loadingGroup, SWT.CHECK);
		saveButton.setText("�Ƿ񱣴�");
		saveButton.setSelection(true);
		saveButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				updateOkButton();
			}
		});

		gridData = new GridData();
		gridData.widthHint = 70;
		gridData.heightHint = 25;
		saveButton.setLayoutData(gridData);
	}

	void updateOkButton()
	{
		Button b = getButton(Window.OK);
		if (saveButton.getSelection() && configCombo.getText().trim().length() == 0)
		{
			b.setEnabled(false);
		} else
		{
			b.setEnabled(true);
		}
	}

	void setICDPath(String icdPath)
	{
		this.icdPath = icdPath;
	}

	String getICDPath()
	{
		return this.icdPath;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent)
	{
		super.createButtonsForButtonBar(parent);
		updateOkButton();
	}

	/**
	 * ��ؽڵ����
	 * 
	 * @param parent
	 */
	protected void createNodePanel(Composite parent)
	{
		Group group = new Group(parent, SWT.SHADOW_ETCHED_IN);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		group.setLayout(layout);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 2;
		group.setLayoutData(gridData);
		group.setText("��ؽڵ�");
		Label label = new Label(group, SWT.NONE);
		label.setText("��ؽڵ�:");
		nodeText = new Text(group, SWT.BORDER);
		nodeText.setLayoutData(new GridData(GridData.FILL_BOTH));
		nodeText.addVerifyListener(new VerifyListener()
		{
			@Override
			public void verifyText(VerifyEvent e)
			{
				//ֻ���������֣�����ʱe.textΪ��       ����      2012-12-27
				e.doit = (e.text.length() == 0 || Character.isDigit(e.text.charAt(0)));
			}
		});
	}

	/**
	 * ��ȡ��ؽڵ��
	 * 
	 * @return
	 */
	public int getNode()
	{
		return Integer.parseInt(nodeText.getText());
	}

	/**
	 * �����޸ĸ��ڵ��״̬
	 * 
	 * @param obj
	 */
	static void updateParent(Object obj, CheckboxTreeViewer viewer)
	{
		ITreeContentProvider provider = (ITreeContentProvider) viewer.getContentProvider();
		Object parent = provider.getParent(obj);
		if (parent != null)
		{
			int checked = 0;
			Object[] children = provider.getChildren(parent);
			for (Object child : children)
			{
				if (viewer.getChecked(child))
				{
					checked++;
				}
			}
			if (checked == 0)
			{
				viewer.setGrayChecked(parent, false);
				viewer.setChecked(parent, false);
			} else if (checked < children.length)
			{
				viewer.setChecked(parent, false);
				viewer.setGrayChecked(parent, true);
			} else
			{
				viewer.setGrayChecked(parent, false);
				viewer.setChecked(parent, true);
			}
			updateParent(parent, viewer);
		}
	}

}