package com.coretek.testcase.testResult;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.progress.IWorkbenchSiteProgressService;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.utils.EclipseUtils;
import com.coretek.spte.cfg.CfgPlugin;
import com.coretek.spte.cfg.ReportPreferencePage;
import com.coretek.spte.core.TestResultManager;
import com.coretek.spte.dataCompare.CompareResult;
import com.coretek.testcase.report.DescriptionExportJob;
import com.coretek.testcase.report.ReportExportJob;
import com.coretek.testcase.testResult.actions.MainActionGroup;
import com.coretek.testcase.testcaseview.TestCaseViewPlugin;

/**
 * ���Խ����ͼ
 * 
 * @author ���Ρ
 * @date 2010-12-3
 */
public class TestResultViewPart extends ViewPart
{

	public static final String					ID		= "com.coretek.tools.testResult.view";

	private CheckboxTableViewer					tableViewer;

	private Combo								recordsTypeCombo;

	private Combo								testRecordsCombo;

	private Map<String, List<CompareResult>>	testRecordsMap;

	private MainActionGroup						actionGroup;

	private Image								runningImage;

	private Image								reportImage1;									// ���Ա���ͼ��
	private Image								reportImage2;									// ����˵��ͼ��

	private Image								refreshImage;

	private Image								failedImage;

	private Image								pastImage;

	private Image								upImage;

	private Image								downImage;

	private Image								removeImage;

	private List<Image>							images	= new ArrayList<Image>();

	private String[]							items	= new String[] { "ִ�н��", "���Խ��" };

	private ToolItem							upButton;

	private ToolItem							downButton;

	private ToolItem							removeItem;

	private ToolBar								toolBar;

	private DropdownSelectionListener			listenerRun;

	public TestResultViewPart()
	{

	}

	public Map<String, List<CompareResult>> getTestRecordsMap()
	{
		return testRecordsMap;
	}

	/**
	 * ���ò��Խ����ͼ����
	 * 
	 * @param testRecordsMap
	 */
	public void setTestRecordsMap(Map<String, List<CompareResult>> testRecordsMap)
	{
		this.testRecordsMap = testRecordsMap;
		this.testRecordsCombo.setItems(getSortedRecordTime(this.testRecordsMap));
		if (this.testRecordsCombo.getItems().length != 0)
		{
			this.testRecordsCombo.select(0);
			List<CompareResult> input = this.testRecordsMap.get(this.testRecordsCombo.getItem(0));
			getTableViewer().setInput(input);
		} else
		{
			getTableViewer().setInput(null);
		}

		getTableViewer().refresh();
	}

	public void refresh(String type)
	{
		TestResultViewPart viewPart = (TestResultViewPart) EclipseUtils.getActivePage().findView(TestResultViewPart.ID);
		// �ӹ����ռ��ж�ȡ������Ŀ���Խ��
		Map<String, List<CompareResult>> testRecordsMap = null;

		if (type.equals("ִ�н��"))
		{
			recordsTypeCombo.select(0);
			testRecordsMap = TestResultManager.getInstance().findAllResultInWorkSpace(TestResultManager.RESULT_FILE_NAME);
		} else if (type.equals("���Խ��"))
		{
			recordsTypeCombo.select(1);
			testRecordsMap = TestResultManager.getInstance().findAllResultInWorkSpace(TestResultManager.DEBUG_FILE_NAME);
		}

		// �����Խ�����õ����Խ����ͼ
		viewPart.setTestRecordsMap(testRecordsMap);
	}

	public CheckboxTableViewer getTableViewer()
	{
		return tableViewer;
	}

	private void upObjOfList(List<CompareResult> list, int i)
	{
		CompareResult compareResult1 = list.get(i);
		CompareResult compareResult0 = list.get(i - 1);
		list.set(i, compareResult0);
		list.set(i - 1, compareResult1);
	}

	private void downObjOfList(List<CompareResult> list, int i)
	{
		CompareResult compareResult0 = list.get(i);
		CompareResult compareResult1 = list.get(i + 1);
		list.set(i, compareResult1);
		list.set(i + 1, compareResult0);
	}

	private void makeActions()
	{
		this.actionGroup = new MainActionGroup(this);
	}

	private void fillContextMenu(IMenuManager menu)
	{
		IStructuredSelection selection = (IStructuredSelection) this.tableViewer.getSelection();
		this.actionGroup.setContext(new ActionContext(selection));
		this.actionGroup.fillContextMenu(menu);
	}

	protected void initContextMenu()
	{
		MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener()
		{
			public void menuAboutToShow(IMenuManager manager)
			{
				TestResultViewPart.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(this.tableViewer.getTable());
		this.tableViewer.getTable().setMenu(menu);
		getSite().registerContextMenu(menuMgr, this.tableViewer);
	}

	@Override
	public void dispose()
	{
		for (Image image : this.images)
		{
			if (image != null)
			{
				image.dispose();
			}
		}
		super.dispose();
	}

	@Override
	public void createPartControl(Composite parent)
	{
		this.failedImage = TestCaseViewPlugin.getImageDescriptor("icons/failed.gif").createImage();
		this.images.add(this.failedImage);
		this.pastImage = TestCaseViewPlugin.getImageDescriptor("icons/past.gif").createImage();
		this.images.add(this.pastImage);

		GridLayout grid = new GridLayout();
		grid.numColumns = 1;
		parent.setLayout(grid);

		Composite topPanel = new Composite(parent, SWT.BORDER);
		grid = new GridLayout();
		grid.numColumns = 7;
		topPanel.setLayout(grid);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 30;
		topPanel.setLayoutData(gd);

		Label label = new Label(topPanel, SWT.NONE | SWT.RIGHT);
		label.setText("����:");
		gd = new GridData();
		gd.widthHint = 30;
		label.setLayoutData(gd);

		recordsTypeCombo = new Combo(topPanel, SWT.DROP_DOWN | SWT.READ_ONLY);
		recordsTypeCombo.setItems(items);
		recordsTypeCombo.select(0);
		recordsTypeCombo.addSelectionListener(new SelectionListener()
		{

			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			public void widgetSelected(SelectionEvent e)
			{
				if (e.widget instanceof Combo)
				{
					String type = ((Combo) e.widget).getText();
					refresh(type);
				}
			}

		});

		label = new Label(topPanel, SWT.NONE | SWT.RIGHT);
		label.setText("����ִ����ʷ:");
		gd = new GridData();
		gd.widthHint = 90;
		label.setLayoutData(gd);

		testRecordsCombo = new Combo(topPanel, SWT.DROP_DOWN | SWT.READ_ONLY);

		testRecordsCombo.addSelectionListener(new SelectionListener()
		{

			public void widgetSelected(SelectionEvent e)
			{
				String record = testRecordsCombo.getItem(testRecordsCombo.getSelectionIndex());
				List<CompareResult> inputBeans = getTestRecordsMap().get(record);
				getTableViewer().setInput(inputBeans);
			}

			public void widgetDefaultSelected(SelectionEvent e)
			{
			}
		});
		gd = new GridData();
		gd.widthHint = 200;
		testRecordsCombo.setLayoutData(gd);

		toolBar = new ToolBar(topPanel, SWT.HORIZONTAL);
		ToolItem item = new ToolItem(toolBar, SWT.DROP_DOWN | SWT.BORDER);
		item.setToolTipText("����");
		this.runningImage = TestCaseViewPlugin.getImageDescriptor("icons/101_caseStart.gif").createImage();
		this.images.add(this.runningImage);
		item.setImage(this.runningImage);

		listenerRun = new DropdownSelectionListener(item);
		item.addSelectionListener(listenerRun);
		listenerRun.add("��������ͨ������");
		listenerRun.add("��������δͨ������");
		listenerRun.add("������ѡ�е�����");
		listenerRun.add("���н������������");

		item = new ToolItem(toolBar, SWT.PUSH);
		item.setToolTipText("���ɲ��Ա���");
		this.reportImage1 = TestCaseViewPlugin.getImageDescriptor("icons/report.png").createImage();
		this.images.add(this.reportImage1);
		item.setImage(this.reportImage1);
		item.addSelectionListener(new SelectionListener()
		{

			public void widgetSelected(SelectionEvent e)
			{

				Object[] objs = tableViewer.getCheckedElements();
				if (objs.length == 0)
				{
					MessageDialog.openInformation(tableViewer.getControl().getShell(), "��ʾ��Ϣ", "��ѡ��Ҫ���ɵĲ��Ա���");
				} else
				{
					List<CompareResult> list = new ArrayList<CompareResult>(objs.length);
					for (Object o : objs)
					{
						if (o instanceof CompareResult)
						{
							list.add((CompareResult) o);
						}
					}
					String filePath = CfgPlugin.getDefault().getPreferenceStore().getString(ReportPreferencePage.SAVE);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); //$NON-NLS-1$
					String fileName = "SPTE���Ա���" + sdf.format(new Date()) + ".doc";
					final File file = new File(filePath, fileName);
					ReportExportJob job = new ReportExportJob("������������", list);
					job.addJobChangeListener(new JobChangeAdapter()
					{

						@Override
						public void done(IJobChangeEvent event)
						{
							if (event.getResult().getSeverity() == IStatus.OK)
							{
								Display.getDefault().syncExec(new Runnable()
								{

									public void run()
									{
										MessageDialog.openInformation(Display.getDefault().getActiveShell(), "��ʾ��Ϣ", "���Ա������ɳɹ�\n�ļ�·����" + file.getAbsolutePath());
									}
								});
							} else
							{
								final String message = event.getResult().getMessage();
								Display.getDefault().syncExec(new Runnable()
								{

									public void run()
									{
										MessageDialog.openError(Display.getDefault().getActiveShell(), "������Ϣ", "���Ա�������ʧ��\n" + message);
									}
								});
							}
						}

					});
					job.setFilePath(file.getAbsolutePath());
					job.setPriority(Job.LONG);
					IWorkbenchSiteProgressService service = getProgressService();
					if (service != null)
					{
						service.schedule(job, 0, true);
					} else
					{
						job.schedule();
					}
				}

			}

			public void widgetDefaultSelected(SelectionEvent e)
			{

			}
		});

		item = new ToolItem(toolBar, SWT.PUSH);
		item.setToolTipText("���ɲ���˵��");
		this.reportImage2 = TestCaseViewPlugin.getImageDescriptor("icons/report.gif").createImage();
		this.images.add(this.reportImage2);
		item.setImage(this.reportImage2);
		item.addSelectionListener(new SelectionListener()
		{

			public void widgetSelected(SelectionEvent e)
			{
				Object[] objs = tableViewer.getCheckedElements();
				if (objs.length == 0)
				{
					MessageDialog.openInformation(tableViewer.getControl().getShell(), "��ʾ��Ϣ", "��ѡ��Ҫ���ɵĲ��Ա���");
				} else
				{
					List<CompareResult> list = new ArrayList<CompareResult>(objs.length);
					for (Object o : objs)
					{
						if (o instanceof CompareResult)
						{
							list.add((CompareResult) o);
						}
					}
					String filePath = CfgPlugin.getDefault().getPreferenceStore().getString(ReportPreferencePage.SAVE);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); //$NON-NLS-1$
					String fileName = "SPTE����˵��" + sdf.format(new Date()) + ".doc";
					final File file = new File(filePath, fileName);
					DescriptionExportJob job = new DescriptionExportJob("˵����������", list);
					job.addJobChangeListener(new JobChangeAdapter()
					{

						@Override
						public void done(IJobChangeEvent event)
						{
							if (event.getResult().getSeverity() == IStatus.OK)
							{
								Display.getDefault().syncExec(new Runnable()
								{

									public void run()
									{
										MessageDialog.openInformation(Display.getDefault().getActiveShell(), "��ʾ��Ϣ", "����˵�����ɳɹ�\n�ļ�·����" + file.getAbsolutePath());
									}
								});
							} else
							{
								final String message = event.getResult().getMessage();
								Display.getDefault().syncExec(new Runnable()
								{

									public void run()
									{
										MessageDialog.openError(Display.getDefault().getActiveShell(), "������Ϣ", "����˵������ʧ��\n" + message);
									}
								});
							}
						}

					});
					job.setFilePath(file.getAbsolutePath());
					job.setPriority(Job.LONG);
					IWorkbenchSiteProgressService service = getProgressService();
					if (service != null)
					{
						service.schedule(job, 0, true);
					} else
					{
						job.schedule();
					}
				}

			}

			public void widgetDefaultSelected(SelectionEvent e)
			{
				// TODO Auto-generated method stub

			}
		});

		item = new ToolItem(toolBar, SWT.PUSH);
		item.setToolTipText(Messages.getString("I18N_TESTRESULT_VIEW"));
		this.refreshImage = TestCaseViewPlugin.getImageDescriptor("icons/504_fresh.gif").createImage();
		this.images.add(this.refreshImage);
		item.setImage(this.refreshImage);
		item.addSelectionListener(new SelectionListener()
		{

			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			public void widgetSelected(SelectionEvent e)
			{
				refresh(recordsTypeCombo.getText());
			}
		});

		upButton = new ToolItem(toolBar, SWT.PUSH);
		this.upImage = TestCaseViewPlugin.getImageDescriptor("icons/505_moveUp.gif").createImage();
		this.images.add(this.upImage);
		upButton.setImage(this.upImage);
		this.upButton.setToolTipText("����");
		upButton.addSelectionListener(new SelectionListener()
		{

			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			public void widgetSelected(SelectionEvent e)
			{
				List<CompareResult> inputBeans = (List<CompareResult>) getTableViewer().getInput();
				if (null != inputBeans && inputBeans.size() != 0)
				{
					int index = getTableViewer().getTable().getSelectionIndex();
					if (index != -1 && index != 0)
					{
						upObjOfList(inputBeans, index);
						getTableViewer().setInput(inputBeans);
						getTableViewer().refresh();
					}
				}

			}

		});

		downButton = new ToolItem(toolBar, SWT.PUSH);
		this.downImage = TestCaseViewPlugin.getImageDescriptor("icons/506_moveDown.gif").createImage();
		this.images.add(this.downImage);
		downButton.setImage(this.downImage);
		downButton.setToolTipText("����");
		downButton.addSelectionListener(new SelectionListener()
		{

			public void widgetDefaultSelected(SelectionEvent e)
			{

			}

			public void widgetSelected(SelectionEvent e)
			{
				List<CompareResult> inputBeans = (List<CompareResult>) getTableViewer().getInput();
				if (null != inputBeans && inputBeans.size() != 0)
				{
					int index = getTableViewer().getTable().getSelectionIndex();
					if (index != -1 && index != inputBeans.size() - 1)
					{
						downObjOfList(inputBeans, index);
						getTableViewer().setInput(inputBeans);
						getTableViewer().refresh();
					}
				}

			}

		});
		this.removeItem = new ToolItem(toolBar, SWT.PUSH);
		this.removeImage = TestCaseViewPlugin.getImageDescriptor("icons/507_del.gif").createImage();
		this.images.add(this.removeImage);
		this.removeItem.setImage(this.removeImage);
		this.removeItem.setToolTipText("ɾ��ִ�н��");
		removeItem.addSelectionListener(new SelectionAdapter()
		{

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				if (testRecordsCombo.getText() == null || testRecordsCombo.getText().trim().equals(""))
				{
					return;
				}
				boolean sure = MessageDialog.openConfirm(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "ȷ����ʾ", "ȷ��Ҫɾ��" + testRecordsCombo.getText() + "��" + recordsTypeCombo.getText());
				if (sure)
				{
					String type = null;
					if (recordsTypeCombo.getText().equals("���Խ��"))
					{
						type = ".debug";
					} else
					// ����Ϊִ�н��
					{
						type = ".result";
					}
					TestResultManager.getInstance().deleteTestResult(type, testRecordsCombo.getText());
					refresh(recordsTypeCombo.getText());
				}
			}
		});

		this.tableViewer = CheckboxTableViewer.newCheckList(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.SINGLE);
		this.listenerRun.setTableViewer(this.tableViewer);
		TableColumn column = new TableColumn(this.tableViewer.getTable(), SWT.NONE);
		column.setText("");
		column.setWidth(50);

		column = new TableColumn(this.tableViewer.getTable(), SWT.NONE);
		column.setText("���");
		column.setAlignment(SWT.CENTER);
		column.setWidth(100);

		column = new TableColumn(this.tableViewer.getTable(), SWT.NONE);
		column.setText("������ʶ");
		column.setWidth(100);

		column = new TableColumn(this.tableViewer.getTable(), SWT.NONE);
		column.setText("��Ŀ");
		column.setWidth(100);

		column = new TableColumn(this.tableViewer.getTable(), SWT.NONE);
		column.setText("������Ϣ");
		column.setWidth(300);

		this.tableViewer.setContentProvider(new TableContentProvider());
		this.tableViewer.setLabelProvider(new TableLabelProvider(this.pastImage, this.failedImage));
		this.tableViewer.getTable().setHeaderVisible(true);
		gd = new GridData(GridData.FILL_BOTH);
		this.tableViewer.getTable().setLayoutData(gd);
		this.tableViewer.getTable().setLinesVisible(true);
		this.tableViewer.addDoubleClickListener(new IDoubleClickListener()
		{

			public void doubleClick(DoubleClickEvent event)
			{
				Object object = ((IStructuredSelection) event.getSelection()).getFirstElement();
				if (object instanceof CompareResult)
				{
					CompareResult compareResult = (CompareResult) object;
					if (compareResult.getTestCaseFile() == null || !compareResult.getTestCaseFile().exists())
					{
						MessageDialog.openError(Display.getCurrent().getActiveShell(), "����", "���������ļ������ڣ�");
						return;
					}
					String timeString = testRecordsCombo.getItem(testRecordsCombo.getSelectionIndex());
					boolean debug = false;
					if (recordsTypeCombo.getSelectionIndex() == 1)
					{
						debug = true;
					}
					OpenResultJob job = new OpenResultJob("��" + recordsTypeCombo.getText(), timeString, compareResult, debug);
					job.setPriority(Job.LONG);
					job.setUser(true);
					IWorkbenchSiteProgressService service = getProgressService();
					if (service != null)
					{
						service.schedule(job, 0, true);
					} else
					{
						job.schedule();
					}
				}
			}
		});

		if (recordsTypeCombo.getItemCount() > 0)
		{
			if (recordsTypeCombo.getText().equals("ִ�н��"))
			{
				testRecordsMap = TestResultManager.getInstance().findAllResultInWorkSpace(TestResultManager.RESULT_FILE_NAME);
			} else if (recordsTypeCombo.getText().equals("���Խ��"))
			{
				testRecordsMap = TestResultManager.getInstance().findAllResultInWorkSpace(TestResultManager.DEBUG_FILE_NAME);
			}
			if (testRecordsMap != null && testRecordsMap.size() > 0)
			{
				testRecordsCombo.setItems(getSortedRecordTime(this.testRecordsMap));
				testRecordsCombo.select(0);
				this.tableViewer.setInput(this.testRecordsMap.get(this.testRecordsCombo.getItem(0)));
			}
		}
		this.initContextMenu();
		this.makeActions();

	}

	private IWorkbenchSiteProgressService getProgressService()
	{
		IWorkbenchPartSite site = this.getSite();
		if (site != null)
			return (IWorkbenchSiteProgressService) site.getAdapter(IWorkbenchSiteProgressService.class);

		return null;
	}

	@Override
	public void setFocus()
	{
		this.recordsTypeCombo.setFocus();
	}

	/**
	 * �Ӳ��Լ�¼Map�л�ò���ʱ�䣨�������У�
	 * 
	 * @param map
	 * @return
	 */
	private String[] getSortedRecordTime(Map<String, List<CompareResult>> map)
	{
		Set<String> set = map.keySet();
		String[] records = new String[set.size()];
		set.toArray(records);

		for (int i = 0; i < records.length - 1; i++)
		{
			if (records[i].compareTo(records[i + 1]) < 0)
			{
				String tempString = records[i];
				records[i] = records[i + 1];
				records[i + 1] = tempString;
				i = -1;
			}
		}
		return records;
	}
}