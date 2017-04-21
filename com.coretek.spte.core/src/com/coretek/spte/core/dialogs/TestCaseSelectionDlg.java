/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.core.dialogs;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;

import com.coretek.common.template.ClazzManager;
import com.coretek.common.template.TemplateEngine;
import com.coretek.common.template.TemplateUtils;
import com.coretek.common.template.XMLBean;
import com.coretek.common.utils.EclipseUtils;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.core.SPTEPlugin;
import com.coretek.spte.core.editor.SPTEEditor;
import com.coretek.spte.core.models.TestNodeMdl;
import com.coretek.spte.core.util.ICDFileManager;
import com.coretek.spte.core.util.Utils;
import com.coretek.spte.testcase.TestCase;

/**
 * 满足引用的规则： 1.所引用的ICD文件必须一致，路径可以不同，但是ICD文件的MD5值必须一样； 2.被测对象必须一致； 3.MD5值必须一致
 * 
 * @author SunDawei 2012-5-19
 */
public class TestCaseSelectionDlg extends TitleAreaDialog
{

	private CheckboxTreeViewer	viewer;

	private Image				testCaseImage	= SPTEPlugin.getImageDescriptor("icons/obj16/test_case_normal.ico").createImage();

	private Image				folderImage		= SPTEPlugin.getImageDescriptor("icons/obj16/test_suite.gif").createImage();

	private Image				projectImage	= SPTEPlugin.getImageDescriptor("icons/obj16/prj_obj.gif").createImage();

	private List<XMLBean>		targetMsgs		= new ArrayList<XMLBean>(0);

	/**
	 * @param parentShell </br> <b>Author</b> SunDawei </br> <b>Date</b>
	 *            2012-5-19
	 */
	public TestCaseSelectionDlg(Shell parentShell, TestNodeMdl testNodeMdl)
	{
		super(parentShell);
		this.setTitle("引用测试用例");
	}

	/**
	 * Get the target messages and timeSpans
	 * 
	 * @return </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-5-22
	 */
	public List<XMLBean> getTargetMsgs()
	{
		return this.targetMsgs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.TitleAreaDialog#createDialogArea(org.eclipse
	 * .swt.widgets.Composite) <br/> <b>Author</b> SunDawei </br> <b>Date</b>
	 * 2012-5-19
	 */
	@Override
	protected Control createDialogArea(Composite parent)
	{
		Composite panel = new Composite(parent, SWT.BORDER);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 500;
		panel.setLayoutData(gridData);
		panel.setLayout(new FillLayout());

		viewer = new CheckboxTreeViewer(panel);
		viewer.setContentProvider(new ContainerContentProvider());
		viewer.setLabelProvider(new LabelProvider());
		viewer.setInput(ResourcesPlugin.getWorkspace());
		viewer.getTree().setSize(300, 500);
		viewer.addCheckStateListener(new ICheckStateListener()
		{

			public void checkStateChanged(CheckStateChangedEvent event)
			{
				if (!event.getChecked())
				{
					setErrorMessage(null);
					return;
				}
				viewer.setAllChecked(false);
				viewer.setChecked(event.getElement(), true);
				Object obj = event.getElement();
				if (obj instanceof IFile)
				{
					// 被引用者
					IFile file = (IFile) obj;
					String icdPath = Utils.getICDFilePath(file);
					if (StringUtils.isNull(icdPath))
					{
						setErrorMessage("被选中的测试用例没有使用ICD文件！");
						getButton(IDialogConstants.OK_ID).setEnabled(false);
					} else
					{
						IEditorPart part = EclipseUtils.getActiveEditor();
						if (part instanceof SPTEEditor)
						{
							SPTEEditor editor = (SPTEEditor) part;
							IEditorInput input = editor.getEditorInput();
							// 引用者
							IFile referer = (IFile) ((IFileEditorInput) input).getFile();
							String icdPath2 = Utils.getICDFilePath(referer);
							if (!icdPath.equals(icdPath2))
							{
								setErrorMessage("被选中的测试用例所使用的ICD文件与引用者不一致！");
								getButton(IDialogConstants.OK_ID).setEnabled(false);
							} else
							{
								// 检查引用者与被引用者的MD5值
								if (ICDFileManager.getInstance().checkMD5Digest(file))
								{
									if (ICDFileManager.getInstance().checkMD5Digest(referer))
									{
										// 比较被测对象集合是否一致
										ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());
										ParseCaseFileJob job = new ParseCaseFileJob(file);
										try
										{
											dialog.run(true, false, job);
										} catch (InvocationTargetException e)
										{
											e.printStackTrace();
											return;
										} catch (InterruptedException e)
										{
											e.printStackTrace();
											return;
										}
										// 被引用的测试用例
										ClazzManager referedManager = job.getCaseManager();
										ClazzManager refererManager = TemplateEngine.getEngine().parseCase(referer.getLocation().toFile());
										TestCase referedTestCase = (TestCase) referedManager.getTestCase();
										List<String> referedIds = Utils.getTestedObjects(referedTestCase);
										TestCase refererTestCase = (TestCase) refererManager.getTestCase();
										List<String> refererIds = Utils.getTestedObjects(refererTestCase);
										if (refererIds.size() != referedIds.size())
										{
											setErrorMessage("引用者与被引用测试用例的被测对象不匹配！");
											getButton(IDialogConstants.OK_ID).setEnabled(false);
										} else
										{
											for (String referedId : referedIds)
											{
												for (String refererId : refererIds)
												{
													if (!refererId.equals(referedId))
													{
														setErrorMessage("引用者与被引用测试用例的被测对象不匹配！");
														getButton(IDialogConstants.OK_ID).setEnabled(false);
														return;
													}
												}
											}

											// 添加引用
											getButton(IDialogConstants.OK_ID).setEnabled(true);
											List<XMLBean> list = TemplateUtils.getAllMsgs(referedTestCase);
											// 克隆一份目标测试用例中的所有消息
											targetMsgs = cloneReferedMsgs(list);
										}

									} else
									{
										setErrorMessage("用例所使用的ICD内容已经发生改变！");
										getButton(IDialogConstants.OK_ID).setEnabled(false);
									}
								} else
								{
									setErrorMessage("被选中的测试用例所使用的ICD内容已经发生改变！");
									getButton(IDialogConstants.OK_ID).setEnabled(false);
								}
							}
						}
					}
				}

			}

		});
		viewer.expandAll();

		return panel;
	}

	private List<XMLBean> cloneReferedMsgs(List<XMLBean> msgs)
	{
		List<XMLBean> list = new ArrayList<XMLBean>(msgs.size());
		for (XMLBean bean : msgs)
		{
			try
			{
				list.add((XMLBean) bean.clone());
			} catch (CloneNotSupportedException e)
			{
				e.printStackTrace();
				return new ArrayList<XMLBean>(0);
			}
		}

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets
	 * .Shell) <br/> <b>Author</b> SunDawei </br> <b>Date</b> 2012-5-19
	 */
	@Override
	protected void configureShell(Shell newShell)
	{
		super.configureShell(newShell);
		newShell.setText("选择引用测试用例");
	}

	public class LabelProvider implements ILabelProvider
	{

		public Image getImage(Object element)
		{
			if (element instanceof IProject)
			{
				return projectImage;
			} else if (element instanceof IFolder)
			{
				return folderImage;
			} else if (element instanceof IFile)
			{
				return testCaseImage;
			}
			return null;
		}

		public String getText(Object element)
		{
			if (element instanceof IResource)
			{
				IResource re = (IResource) element;
				return re.getName();
			}
			return null;
		}

		public void addListener(ILabelProviderListener listener)
		{

		}

		public void dispose()
		{
			if (projectImage != null)
			{
				projectImage.dispose();
				projectImage = null;
			}
			if (folderImage != null)
			{
				folderImage.dispose();
				folderImage = null;
			}
			if (testCaseImage != null)
			{
				testCaseImage.dispose();
				testCaseImage = null;
			}
		}

		public boolean isLabelProperty(Object element, String property)
		{
			return false;
		}

		public void removeListener(ILabelProviderListener listener)
		{

		}
	}

	/**
	 * Provides content for a tree viewer that shows only containers.
	 */
	public static class ContainerContentProvider implements ITreeContentProvider
	{

		/**
		 * Creates a new ContainerContentProvider.
		 */
		public ContainerContentProvider()
		{
		}

		/**
		 * The visual part that is using this content provider is about to be
		 * disposed. Deallocate all allocated SWT resources.
		 */
		public void dispose()
		{

		}

		/*
		 * @see
		 * org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang
		 * .Object)
		 */
		public Object[] getChildren(Object element)
		{
			if (element instanceof IWorkspace)
			{
				// check if closed projects should be shown
				IProject[] allProjects = ((IWorkspace) element).getRoot().getProjects();
				List<IProject> prjs = new ArrayList<IProject>();
				for (IProject prj : allProjects)
				{
					if (prj.isAccessible() && prj.isOpen())
					{
						if (Utils.isSoftwareTestingProject(prj))
						{
							prjs.add(prj);
						}
					}
				}
				return prjs.toArray();
			} else if (element instanceof IContainer)
			{
				IContainer container = (IContainer) element;
				if (container.isAccessible())
				{
					if (container instanceof IProject)
					{
						try
						{
							List<IResource> children = new ArrayList<IResource>();
							IResource[] members = container.members();
							for (int i = 0; i < members.length; i++)
							{
								if (members[i] instanceof IFolder)
								{
									IFolder folder = (IFolder) members[i];
									if (Utils.isTestSuite(folder))
									{
										children.add(members[i]);
									}
								}
							}
							return children.toArray();
						} catch (CoreException e)
						{
							e.printStackTrace();
						}
					} else if (container instanceof IFolder)
					{
						try
						{
							List<IResource> children = new ArrayList<IResource>();
							IResource[] members = container.members();
							for (int i = 0; i < members.length; i++)
							{
								if (members[i] instanceof IFile)
								{
									IFile file = (IFile) members[i];
									if (Utils.isCasFile(file))
									{
										IFileEditorInput input = (IFileEditorInput) EclipseUtils.getActiveEditor().getEditorInput();
										IFile caseFile = input.getFile();
										if (!caseFile.equals(file))
											children.add(members[i]);
									}
								}
							}
							return children.toArray();
						} catch (CoreException e)
						{
							e.printStackTrace();
						}
					}
				}
			}
			return new Object[0];
		}

		/*
		 * @see
		 * org.eclipse.jface.viewers.IStructuredContentProvider#getElements(
		 * java.lang.Object)
		 */
		public Object[] getElements(Object element)
		{
			return getChildren(element);
		}

		/*
		 * @see
		 * org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang
		 * .Object)
		 */
		public Object getParent(Object element)
		{
			if (element instanceof IResource)
			{
				return ((IResource) element).getParent();
			}
			return null;
		}

		/*
		 * @see
		 * org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang
		 * .Object)
		 */
		public boolean hasChildren(Object element)
		{
			return getChildren(element).length > 0;
		}

		/*
		 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged
		 */
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
		{

		}

	}
}
