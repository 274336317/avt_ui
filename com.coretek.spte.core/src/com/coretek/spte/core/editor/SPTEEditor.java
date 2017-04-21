package com.coretek.spte.core.editor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.stackview.CommandStackInspectorPage;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.INavigationLocation;
import org.eclipse.ui.INavigationLocationProvider;
import org.eclipse.ui.PartInitException;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.template.ClazzManager;
import com.coretek.common.template.TemplateEngine;
import com.coretek.common.template.TemplateUtils;
import com.coretek.common.template.XMLBean;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.common.utils.EclipseUtils;
import com.coretek.common.utils.ImageAndColor;
import com.coretek.spte.core.SPTEPlugin;
import com.coretek.spte.core.debug.DebugCmdHanlder;
import com.coretek.spte.core.debug.DebugKeysHandler;
import com.coretek.spte.core.dnd.MsgDropListener;
import com.coretek.spte.core.editor.actions.AddRefMsgsAtn;
import com.coretek.spte.core.editor.actions.CopyMsgAtn;
import com.coretek.spte.core.editor.actions.CutAndPasteMsgAtn;
import com.coretek.spte.core.editor.actions.DelAllAtn;
import com.coretek.spte.core.editor.actions.DelMsgAtn;
import com.coretek.spte.core.editor.actions.EditorContextMenuProvider;
import com.coretek.spte.core.editor.actions.PasteConnAtn;
import com.coretek.spte.core.figures.ContainerFgr;
import com.coretek.spte.core.figures.TestNodeFgr;
import com.coretek.spte.core.figures.TestToolFgr;
import com.coretek.spte.core.figures.TestedObjectFgr;
import com.coretek.spte.core.models.AbtElement;
import com.coretek.spte.core.models.ContainerMdl;
import com.coretek.spte.core.models.PostilChildMdl;
import com.coretek.spte.core.models.PostilMdl;
import com.coretek.spte.core.models.RootContainerMdl;
import com.coretek.spte.core.models.TestMdl;
import com.coretek.spte.core.models.TestNodeMdl;
import com.coretek.spte.core.models.TestToolMdl;
import com.coretek.spte.core.models.TestedObjectMdl;
import com.coretek.spte.core.parts.BackgroundChildMsgPart;
import com.coretek.spte.core.parts.BackgroundMsgPart;
import com.coretek.spte.core.parts.ContainerPart;
import com.coretek.spte.core.parts.RootContainerPart;
import com.coretek.spte.core.parts.SPTEEditPartFactory;
import com.coretek.spte.core.parts.TestNodeContainerPart;
import com.coretek.spte.core.parts.TestNodePart;
import com.coretek.spte.core.parts.TestPart;
import com.coretek.spte.core.parts.TestToolPart;
import com.coretek.spte.core.parts.TestedObjectPart;
import com.coretek.spte.core.tools.PaletteFactory;
import com.coretek.spte.core.util.ICDFileManager;
import com.coretek.spte.core.util.TestCaseStatus;
import com.coretek.spte.core.util.Utils;
import com.coretek.spte.core.views.MessageView;
import com.coretek.spte.core.xml.parser.TestCaseParser;
import com.coretek.spte.testcase.MessageBlock;
import com.coretek.spte.testcase.Postil;
import com.coretek.spte.testcase.PostilChild;
import com.coretek.spte.testcase.Postils;
import com.coretek.spte.testcase.SimuObject;
import com.coretek.spte.testcase.SimuObjects;
import com.coretek.spte.testcase.TestCase;

/**
 * ͼ�α༭��������չʾ���༭������ͼ��
 * 
 * @author ���Ρ
 * @date 2010-9-1
 */
@SuppressWarnings("deprecation")
public class SPTEEditor extends GraphicalEditorWithFlyoutPalette implements INavigationLocationProvider {

	private RootContainerMdl rooContainerMdl;

	private PaletteRoot paletteRoot;

	public static String path;

	private static SPTEEditor practiceEditor;

	private TestCase testCaseBean;

	private String toolTipText = "";

	private DebugCmdHanlder debugResposne;

	public final static String ID = "com.example.ui.PracticeEditor";

	/**
	 * ����������Ӧicd�Ĺ�����
	 */
	private ClazzManager fighterClazzManager;

	/**
	 * ���� �����Ĺ�����
	 */
	private ClazzManager testCaseClazzManager;

	private Entity fighter;

	public SPTEEditor() {
		practiceEditor = this;
		setEditDomain(new DefaultEditDomain(this));
	}

	public ClazzManager getTestCaseClazzManager() {
		return testCaseClazzManager;
	}

	public void setTestCaseClazzManager(ClazzManager testCaseClazzManager) {
		this.testCaseClazzManager = testCaseClazzManager;
	}

	/*
	 * __________________________________________________________________________________
	 * @Class SPTEEditor
	 * @Function createPartControl
	 * @Description ����������������
	 * @Auther MENDY
	 * @param parent
	 * @Date 2016-5-30 ����03:21:53
	 */
	@Override
	public void createPartControl(Composite parent) {
		// ����editor����Ľ���ɫ
		parent.getParent().getParent().setBackground(ImageAndColor.getDefault().getColor(1));
		// ���ó�ȥeditor������ɫ
		parent.getParent().getParent().getParent().setBackground(ImageAndColor.getDefault().getColor(1));
		// ���ñ߿���ɫ
		parent.getParent().getParent().getParent().getParent().getParent().setBackground(ImageAndColor.getDefault().getColor(1));
		super.createPartControl(parent);
	}

	public boolean isEditable() {
		return this.rooContainerMdl.getStatus() == TestCaseStatus.Editing;
	}

	/**
	 * ���ñ༭����״̬��״̬��Ϊ���֣��鿴������༭�����ԡ� ���༭�����ڷǱ༭״̬ʱ���༭���еĲ��������ǲ������༭�ġ�
	 * ������ʷ�����ͼ�в鿴����������ִ����ʷ���ʱ����ؽ��༭��״̬����Ϊ �鿴��ʷ��¼ ״̬��
	 * 
	 * @param status
	 */
	public void setStatus(TestCaseStatus status) {
		if (this.rooContainerMdl == null) {
			this.rooContainerMdl = new RootContainerMdl(status);
		} else {
			this.rooContainerMdl.setStatus(status);
		}
	}

	/**
	 * ��ȡ�뵱ǰ�༭����صĵ����������
	 * 
	 * @return </br> <b>����</b> ���Ρ </br> <b>����</b> 2011-7-28
	 */
	public DebugCmdHanlder getDebugResposne() {
		return debugResposne;
	}

	public Entity getFighter() {
		return fighter;
	}

	public void setFighter(Entity fighter) {
		this.fighter = fighter;
	}

	public ClazzManager getFighterClazzManager() {
		return fighterClazzManager;
	}

	public void setFighterClazzManager(ClazzManager clazzManager) {
		this.fighterClazzManager = clazzManager;
	}

	/**
	 * ���rootContainerΪnull�����ʼ������Ȼ�󷵻س�ʼ����Ķ���
	 * 
	 * @return instance of RootContainerMdl
	 */
	public RootContainerMdl getRootContainerMdl() {
		if (this.rooContainerMdl == null) {
			this.rooContainerMdl = new RootContainerMdl();
		}
		return rooContainerMdl;
	}

	public TestCase getTestCaseBean() {
		return this.testCaseBean;
	}

	/**
	 * ��ȡ�������,���������еĲ��Զ����б�
	 * 
	 * @return </br> <b>����</b> ��һɭ </br> <b>����</b> 2011-12-31
	 */
	public XMLBean getTestedObject() {
		return (XMLBean) this.testCaseBean.getTestedObjects();
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		this.getCommandStack().flush();
	}

	public static SPTEEditor getDefault() {
		return practiceEditor;
	}

	@Override
	public void dispose() {
		ScalableFreeformRootEditPart rootPart = (ScalableFreeformRootEditPart) this.getGraphicalViewer().getRootEditPart();
		if (rootPart.getChildren() != null && rootPart.getChildren().size() != 0) {
			RootContainerPart trans = (RootContainerPart) rootPart.getChildren().get(0);
			ContainerPart sub = (ContainerPart) trans.getChildren().get(0);
			ContainerFgr figure = (ContainerFgr) sub.getFigure();
			figure.dispose();
			for (Object obj : sub.getChildren()) {
				if (obj instanceof TestPart) {
					TestPart part = (TestPart) obj;
					TestMdl model = (TestMdl) part.getModel();
					TestNodeContainerPart itemPart = null;
					if (part instanceof TestedObjectPart) {
						TestedObjectFgr fgr = (TestedObjectFgr) part.getFigure();
						fgr.dispose();
						itemPart = (TestNodeContainerPart) part.getChildren().get(0);
					} else if (part instanceof TestToolPart) {
						TestToolFgr fgr = (TestToolFgr) part.getFigure();
						fgr.dispose();
						itemPart = (TestNodeContainerPart) part.getChildren().get(0);
					}

					if (model instanceof TestedObjectMdl) {
						for (Object subObj : itemPart.getChildren()) {
							TestNodePart itemPart1 = (TestNodePart) subObj;
							TestNodeFgr itemFgr1 = (TestNodeFgr) itemPart1.getFigure();
							itemFgr1.dispose();
						}
					}
				}

			}
		}

		super.dispose();
	}

	@Override
	public DefaultEditDomain getEditDomain() {
		return super.getEditDomain();
	}

	@Override
	protected void configureGraphicalViewer() {
		double zoomLevels[];
		List<String> zoomContributions = new ArrayList<String>();
		super.configureGraphicalViewer();
		ScalableFreeformRootEditPart root = new ScalableFreeformRootEditPart();
		ZoomManager manager = root.getZoomManager();
		getActionRegistry().registerAction(new ZoomInAction(manager));
		getActionRegistry().registerAction(new ZoomOutAction(manager));
		zoomLevels = new double[] { 0.25, 0.5, 0.75, 1.0, 1.5, 2.0, 2.5, 3.0, 4.0, 5.0 };
		manager.setZoomLevels(zoomLevels);
		zoomContributions.add(ZoomManager.FIT_ALL);
		zoomContributions.add(ZoomManager.FIT_HEIGHT);
		zoomContributions.add(ZoomManager.FIT_WIDTH);
		manager.setZoomLevelContributions(zoomContributions);

		getGraphicalViewer().setRootEditPart(root);
		getGraphicalViewer().setEditPartFactory(new SPTEEditPartFactory());
		ContextMenuProvider provider = new EditorContextMenuProvider(getGraphicalViewer(), getActionRegistry(), this);
		getGraphicalViewer().setContextMenu(provider);
	}

	@Override
	protected void initializeGraphicalViewer() {
		getGraphicalViewer().setContents(this.rooContainerMdl);
		getGraphicalViewer().addDropTargetListener(new MsgDropListener(getGraphicalViewer()));
		DebugKeysHandler keysHandler = new DebugKeysHandler(this.getGraphicalViewer(), this.getRootContainerMdl(), this);
		this.debugResposne = new DebugCmdHanlder(((IFileEditorInput) this.getEditorInput()).getFile(), this);
		this.getGraphicalViewer().setKeyHandler(keysHandler);
	}

	/**
	 * ���û����õ���Ϣ��Ϣ���浽һ����ʱ��ͼ������ͬ��xml�ļ���
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		// �༭�����ڲ��ɱ༭״̬����������
		if (!this.isEditable()) {
			firePropertyChange(IEditorPart.PROP_DIRTY);
			return;
		}
		this.showBusy(true);
		try {
			getCommandStack().markSaveLocation();
			/*
			 * ����cas�ļ��� ʵ��˼·�� ����ÿһ��item����ֻ��һ����������������Ϣ��������ͬʱӵ��������
			 * ������Ϣ��������Ϣ��ɣ��������ʱ���ܽ�������Ϣ�е������������ ֻ����������ߣ�Ҳ���ǵ�һ�����ߡ�
			 * ����ÿ��item����ʱ��������������ʱ��Ҳ���ǰ�����incoming���У� ���ܹ������
			 * ���Թ��߲�������ʱ������ֻ�ܽ�ʱ���������ڱ�������ϡ�
			 */
			TestCase testCase = new TestCase();
			IFile file = ((IFileEditorInput) this.getEditorInput()).getFile();
			List<AbtElement> children = this.rooContainerMdl.getChildren();// ��ȡFSubTransModel

			TestCase oldTestCase = (TestCase) getTestCaseClazzManager().getTestCase();
			testCase.setName(file.getName());
			if (oldTestCase == null) {
				// ���������Ĺ�����
				ClazzManager clazzMag = TemplateEngine.getEngine().parseCase(file.getLocation().toFile());
				setTestCaseClazzManager(clazzMag);
				oldTestCase = (TestCase) clazzMag.getTestCase();
			}
			testCase.setVersion(oldTestCase.getVersion());
			List<Entity> caseInfo = oldTestCase.getChildren();
			for (Entity info : caseInfo) {// �����������й̶�����Ϣд�뵽����������
				if (info instanceof MessageBlock || info instanceof Postils || info instanceof SimuObjects) {
					continue;
				} else {
					testCase.addChild(info);
				}
			}
			MessageBlock messageBlock = new MessageBlock();

			Postils postils = new Postils();
			testCase.addChild(postils);

			for (AbtElement element : children) {
				if (element instanceof ContainerMdl) {
					ContainerMdl model = (ContainerMdl) element;
					List<AbtElement> lifelines = model.getChildren();// ��ȡLifelineModel
					for (AbtElement node : lifelines) {
						if (node instanceof PostilMdl) {
							PostilMdl postilMdl = (PostilMdl) node;
							Rectangle rectangle = postilMdl.getConstraints();
							Postil postil = new Postil();
							postils.addChild(postil);
							postil.setHigh(rectangle.height);
							postil.setWidth(rectangle.width);
							postil.setX(rectangle.x);
							postil.setY(rectangle.y);
							Rectangle childRectangle = null;
							if (postilMdl.getLeftChildrenMdl() != null) {
								PostilChildMdl postilLeftChildMdl = postilMdl.getLeftChildrenMdl();
								childRectangle = postilLeftChildMdl.getConstraints();
								PostilChild postilLeftChild = new PostilChild();
								postilLeftChild.setText(postilLeftChildMdl.getText());
								postilLeftChild.setX(childRectangle.x);
								postilLeftChild.setY(childRectangle.y);
								postilLeftChild.setHigh(childRectangle.height);
								postilLeftChild.setWidth(childRectangle.width);
								postil.addChild(postilLeftChild);
							}
							if (postilMdl.getRightChildrenMdl() != null) {
								PostilChildMdl postilRightChildMdl = postilMdl.getRightChildrenMdl();
								childRectangle = postilRightChildMdl.getConstraints();
								PostilChild postilRightChild = new PostilChild();
								postilRightChild.setText(postilRightChildMdl.getText());
								postilRightChild.setX(childRectangle.x);
								postilRightChild.setY(childRectangle.y);
								postilRightChild.setHigh(childRectangle.height);
								postilRightChild.setWidth(childRectangle.width);
								postil.addChild(postilRightChild);
							}
						} else if (node instanceof TestMdl) {
							TestMdl lifeline = (TestMdl) node;
							if (lifeline instanceof TestToolMdl) {// ���Թ���
								// ����ģ��ڵ㼯��
								Set<String> ids = Utils.getSimuObjectsIDs(lifeline);
								List<Entity> list = new ArrayList<Entity>(ids.size());
								IFile icdFile = Utils.getICDFile(file);
								ClazzManager clazz = TemplateEngine.getEngine().parseICD(icdFile.getLocation().toFile());
								for (String id : ids) {
									String name = clazz.getFunctionNodeName(id);
									SimuObject so = new SimuObject();
									so.setId(id);
									so.setName(name);
									list.add(so);
								}
								SimuObjects simu = new SimuObjects();
								simu.setChildren(list);
								testCase.addChild(simu);
							} else if (lifeline instanceof TestedObjectMdl) {
								// �ڱ��⹤���ϼ������Ҫ����Щ����Ϊ���Թ����ϲ�������ʱ����
								List<AbtElement> items = lifeline.getChildren();// ��ȡLineItem��parent
								List<TestNodeMdl> itemModels = new ArrayList<TestNodeMdl>();
								for (AbtElement item : items) {
									List<AbtElement> childItems = item.getChildren();// ��ȡ���߽ڵ�
									for (AbtElement childItem : childItems) {
										TestNodeMdl lineItem = (TestNodeMdl) childItem;
										itemModels.add(lineItem);
									}
									List<Entity> list = Utils.getAllResultMessage(itemModels);
									for (Entity obj : list) {
										messageBlock.addChild(obj);
									}
								}
							}

						}

					}
				}
			}
			testCase.addChild(messageBlock);
			// ��resultMessageBeanת��Ϊxml��ʽ���ı���Ϣ��Ȼ��д�뵽�ļ�����
			IFile editorIFile = EclipseUtils.getInputOfActiveEditor();
			File editorFile = editorIFile.getLocation().toFile();
			OutputStream os = new FileOutputStream(editorFile);
			OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
			PrintWriter writer = new PrintWriter(osw);
			StringBuilder sb = new StringBuilder();
			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			sb.append(testCase.toString());
			writer.write(sb.toString());
			writer.close();

			EclipseUtils.getCurrentProject().refreshLocal(IResource.DEPTH_INFINITE, null);
			firePropertyChange(IEditorPart.PROP_DIRTY);
		} catch (CoreException ce) {
			ce.printStackTrace();
		} catch (Exception ioe) {
			ioe.printStackTrace();
		}
		this.showBusy(false);
	}

	/*
	 * __________________________________________________________________________________
	 * 
	 * @Class SPTEEditor
	 * 
	 * @Function doSaveAs
	 * 
	 * @Description ���Ϊ
	 * 
	 * @Auther MENDY
	 * 
	 * @Date 2016-5-20 ����04:58:29
	 */
	@Override
	public void doSaveAs() {
		// For now, do nothing
	}

	/*
	 * __________________________________________________________________________________
	 * 
	 * @Class SPTEEditor
	 * 
	 * @Function isDirty
	 * 
	 * @Description �ж��Ƿ��޸Ĺ�
	 * 
	 * @Auther MENDY
	 * 
	 * @return
	 * 
	 * @Date 2016-5-20 ����05:00:00
	 */
	@Override
	public boolean isDirty() {
		return getCommandStack().isDirty();
	}

	/*
	 * __________________________________________________________________________________
	 * 
	 * @Class SPTEEditor
	 * 
	 * @Function isSaveAsAllowed
	 * 
	 * @Description �Ƿ�������
	 * 
	 * @Auther MENDY
	 * 
	 * @return
	 * 
	 * @Date 2016-5-20 ����05:00:19
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	/**
	 * �����Ƿ���Ҫ���ر�����Ϣ
	 * 
	 * @param flag
	 */
	@SuppressWarnings("unchecked")
	public void setHidingBackgroundMsgs(boolean flag) {
		ScalableFreeformRootEditPart rootPart = (ScalableFreeformRootEditPart) this.getGraphicalViewer().getRootEditPart();
		RootContainerPart trans = (RootContainerPart) rootPart.getChildren().get(0);
		ContainerPart sub = (ContainerPart) trans.getChildren().get(0);
		List<AbstractEditPart> kids = (List<AbstractEditPart>) sub.getChildren();
		for (AbstractEditPart editPart : kids) {
			if (editPart.getModel() instanceof TestedObjectMdl) {
				// �������
				List<AbstractEditPart> parentNodeParts = editPart.getChildren();
				List<AbstractEditPart> nodeParts = parentNodeParts.get(0).getChildren();
				for (AbstractEditPart nodePart : nodeParts) {
					TestNodePart testNodePart = (TestNodePart) nodePart;
					List<ConnectionEditPart> tarConnParts = testNodePart.getTargetConnections();
					List<ConnectionEditPart> srcConnParts = testNodePart.getSourceConnections();
					for (ConnectionEditPart connPart : tarConnParts) {
						// �ҳ�������Ϣ��figure��Ȼ�����ñ�����Ϣ��figure��visible����
						if (connPart instanceof BackgroundChildMsgPart || connPart instanceof BackgroundMsgPart) {
							connPart.getFigure().setVisible(flag);
						}
					}
					for (ConnectionEditPart connPart : srcConnParts) {
						// �ҳ�������Ϣ��figure��Ȼ�����ñ�����Ϣ��figure��visible����
						if (connPart instanceof BackgroundChildMsgPart || connPart instanceof BackgroundMsgPart) {
							connPart.getFigure().setVisible(flag);
						}
					}
				}
			}
		}
	}

	@Override
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		EditorOpenJob job = new EditorOpenJob(input);
		try {
			Shell m_shell = Display.getCurrent().getActiveShell();
			new ProgressMonitorDialog(m_shell).run(true, false, job);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected PaletteRoot getPaletteRoot() {
		if (this.paletteRoot == null) {
			this.paletteRoot = PaletteFactory.createPalette();
		}
		return this.paletteRoot;
	}

	@Override
	protected PaletteViewerProvider createPaletteViewerProvider() {
		return new PaletteViewerProvider(getEditDomain()) {

			protected void configurePaletteViewer(PaletteViewer viewer) {
				super.configurePaletteViewer(viewer);
				viewer.addDragSourceListener(new TemplateTransferDragSourceListener(viewer));
			}
		};
	}

	@Override
	protected FlyoutPreferences getPalettePreferences() {
		return new FlyoutPreferences() {

			public int getDockLocation() {
				return PositionConstants.WEST;
			}

			public void setDockLocation(int location) {
			}

			public int getPaletteState() {
				return FlyoutPaletteComposite.STATE_PINNED_OPEN;
			}

			// ����palette��ʼ���
			public int getPaletteWidth() {
				return 80;
			}

			public void setPaletteState(int state) {
			}

			public void setPaletteWidth(int width) {
			}
		};
	}

	@SuppressWarnings( { "unchecked" })
	public Object getAdapter(Class type) {
		if (type == CommandStackInspectorPage.class)
			return new CommandStackInspectorPage(getCommandStack());
		if (type == ActionRegistry.class)
			return getActionRegistry();
		if (type == ZoomManager.class)
			return getGraphicalViewer().getProperty(ZoomManager.class.toString());

		return super.getAdapter(type);
	}

	@Override
	public void commandStackChanged(EventObject event) {
		super.commandStackChanged(event);
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void createActions() {
		super.createActions();
		ActionRegistry registry = getActionRegistry();
		IAction action = null;
		action = new CopyMsgAtn(this);
		registry.registerAction(action);
		this.getSelectionActions().add(action.getId());

		action = new PasteConnAtn(this);
		registry.registerAction(action);
		this.getSelectionActions().add(action.getId());

		action = new CutAndPasteMsgAtn(this);
		registry.registerAction(action);
		this.getSelectionActions().add(action.getId());

		action = new DelMsgAtn(this);
		registry.registerAction(action);
		this.getSelectionActions().add(action.getId());

		action = new DelAllAtn(this);
		registry.registerAction(action);
		this.getSelectionActions().add(action.getId());

		action = new AddRefMsgsAtn(this);
		registry.registerAction(action);
		this.getSelectionActions().add(action.getId());
	}

	@Override
	public String getTitleToolTip() {
		return toolTipText;
	}

	@Override
	public GraphicalViewer getGraphicalViewer() {
		return super.getGraphicalViewer();
	}

	public void scrollToY(int offset) {
		FigureCanvas canvas = (FigureCanvas) this.getGraphicalControl();
		canvas.scrollToY(offset);
	}

	public int getClientAreaHeight() {
		FigureCanvas canvas = (FigureCanvas) this.getGraphicalControl();
		return canvas.getClientArea().height;
	}

	public TestCaseStatus getStatus() {
		if (this.rooContainerMdl != null) {
			return this.rooContainerMdl.getStatus();
		} else {
			return null;
		}

	}

	public INavigationLocation createEmptyNavigationLocation() {
		return new SPTENavigationLocation(this);
	}

	public INavigationLocation createNavigationLocation() {
		return new SPTENavigationLocation(this);
	}

	/**
	 * __________________________________________________________________________________
	 * 
	 * @Class SPTEEditor.java
	 * @Description ��editor�༭��
	 * @Auther MENDY
	 * @Date 2016-5-18 ����09:41:29
	 */
	private class EditorOpenJob implements IRunnableWithProgress {

		private IEditorInput input;

		public EditorOpenJob(IEditorInput input) {
			this.input = input;
		}

		public void run(IProgressMonitor monitor) {
			monitor.beginTask("���ڴ򿪱༭��", 100);
			try {
				IFile file = ((IFileEditorInput) input).getFile();
				// �����������ļ���������ICD�ļ��Ƿ����
				if (!file.exists() || !ICDFileManager.getInstance().icdFileExists(file)) {
					monitor.done();
					return;
				}

				// ���file�Ƿ��Ǳ������ʷ��¼��
				// ���file���Ǳ��������ʷ��¼�������ĸ��ļ�����Ӧ�ð���һ������.folderProperty���ļ�,���ļ���Ϊ���Լ�,
				// �����丸�ļ��еĸ��ڵ�Ӧ���ǹ��̽ڵ㡣
				IContainer container = file.getParent();
				if (container instanceof IFolder) {
					IFolder folder = (IFolder) container;
					if (Utils.isTestSuite(folder)) {
					} else {
						// ���༭��״̬����Ϊ���ɱ༭��״̬
						setStatus(TestCaseStatus.ViewResult);
					}
				} else {
					// ���༭��״̬����Ϊ���ɱ༭��״̬
					setStatus(TestCaseStatus.ViewResult);
				}
				// ���ñ༭������
				toolTipText = file.getFullPath().toOSString().substring(1);
				setPartName(toolTipText);

				ContainerMdl subModel = new ContainerMdl();
				if (getStatus() != null) {
					subModel.setName(getStatus().getText());
				}
				getRootContainerMdl().addChild(subModel);
				TestToolMdl tester = new TestToolMdl();
				tester.initModel();
				tester.setName(Messages.getString("I18N_TEST_TOOL")); // ���Թ���
				tester.setLocation(new Point(200, 10)); // ���ò��Թ�������ߺ��ϱ߶��پ��룬x=125��y=10
				tester.setParent(subModel);
				subModel.addChild(tester);
				TestedObjectMdl tested = new TestedObjectMdl();
				tested.initModel();
				tested.setName(Messages.getString("I18N_TESTED_OBJECT"));// �������
				tested.setLocation(new Point(450, 10));// ���ñ����������ߺ��ϱ߶��پ���
				subModel.addChild(tested);
				monitor.worked(20);
				testCaseBean = (TestCase) TestCaseParser.getInstance(TemplateUtils.getTestCaseSchemaFile(), ((IFileEditorInput) input).getFile(), rooContainerMdl).doParse();
				monitor.worked(20);
				monitor.subTask("������������...");
				// ���������Ĺ�����
				ClazzManager clazzMag = TemplateEngine.getEngine().parseCase(((IFileEditorInput) input).getFile().getLocation().toFile());
				setTestCaseClazzManager(clazzMag);
				monitor.worked(40);
				// SPTEPlugin.getActiveWorkbenchShell().setBackground(ColorConstants.red);
				SPTEPlugin.getDefault().activeViewAndBringToTop(MessageView.MESSAGE_VIEW_ID);
				monitor.worked(20);
			} catch (Throwable e) {
				e.printStackTrace();
			} finally {
				System.gc();
			}
		}

	}
}