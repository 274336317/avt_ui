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
 * 图形编辑器，负责展示、编辑、保存图形
 * 
 * @author 孙大巍
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
	 * 测试用例对应icd的管理器
	 */
	private ClazzManager fighterClazzManager;

	/**
	 * 测试 用例的管理器
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
	 * @Description 新增创建布局容器
	 * @Auther MENDY
	 * @param parent
	 * @Date 2016-5-30 下午03:21:53
	 */
	@Override
	public void createPartControl(Composite parent) {
		// 设置editor面板四角颜色
		parent.getParent().getParent().setBackground(ImageAndColor.getDefault().getColor(1));
		// 设置除去editor面板角颜色
		parent.getParent().getParent().getParent().setBackground(ImageAndColor.getDefault().getColor(1));
		// 设置边框颜色
		parent.getParent().getParent().getParent().getParent().getParent().setBackground(ImageAndColor.getDefault().getColor(1));
		super.createPartControl(parent);
	}

	public boolean isEditable() {
		return this.rooContainerMdl.getStatus() == TestCaseStatus.Editing;
	}

	/**
	 * 设置编辑器的状态，状态份为三种：查看结果、编辑、调试。 当编辑器处在非编辑状态时，编辑器中的测试用例是不允许被编辑的。
	 * 当从历史结果视图中查看测试用例的执行历史结果时，务必将编辑器状态设置为 查看历史记录 状态。
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
	 * 获取与当前编辑器相关的调试命令处理器
	 * 
	 * @return </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-7-28
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
	 * 如果rootContainer为null，则初始化它，然后返回初始化后的对象
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
	 * 获取被测对象,测试用例中的测试对象列表
	 * 
	 * @return </br> <b>作者</b> 杜一森 </br> <b>日期</b> 2011-12-31
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
	 * 将用户设置的消息信息保存到一个与时序图名字相同的xml文件中
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		// 编辑器处于不可编辑状态，则不允许保存
		if (!this.isEditable()) {
			firePropertyChange(IEditorPart.PROP_DIRTY);
			return;
		}
		this.showBusy(true);
		try {
			getCommandStack().markSaveLocation();
			/*
			 * 生成cas文件。 实现思路： 对于每一个item，它只有一个输出或者输入的消息，不可能同时拥有两个。
			 * 周期消息由两条消息组成，在输出的时候不能将周期消息中的子连线输出， 只能输出父连线，也就是第一条连线。
			 * 对于每个item，当时间间隔是它的输入时，也就是包含在incoming当中， 才能够输出。
			 * 测试工具不能设置时间间隔，只能将时间间隔设置在被测对象上。
			 */
			TestCase testCase = new TestCase();
			IFile file = ((IFileEditorInput) this.getEditorInput()).getFile();
			List<AbtElement> children = this.rooContainerMdl.getChildren();// 获取FSubTransModel

			TestCase oldTestCase = (TestCase) getTestCaseClazzManager().getTestCase();
			testCase.setName(file.getName());
			if (oldTestCase == null) {
				// 测试用例的管理器
				ClazzManager clazzMag = TemplateEngine.getEngine().parseCase(file.getLocation().toFile());
				setTestCaseClazzManager(clazzMag);
				oldTestCase = (TestCase) clazzMag.getTestCase();
			}
			testCase.setVersion(oldTestCase.getVersion());
			List<Entity> caseInfo = oldTestCase.getChildren();
			for (Entity info : caseInfo) {// 将测试用例中固定的信息写入到测试用例中
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
					List<AbtElement> lifelines = model.getChildren();// 获取LifelineModel
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
							if (lifeline instanceof TestToolMdl) {// 测试工具
								// 保存模拟节点集合
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
								// 在被测工具上计算输出要方便些，因为测试工具上不能设置时间间隔
								List<AbtElement> items = lifeline.getChildren();// 获取LineItem的parent
								List<TestNodeMdl> itemModels = new ArrayList<TestNodeMdl>();
								for (AbtElement item : items) {
									List<AbtElement> childItems = item.getChildren();// 获取连线节点
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
			// 将resultMessageBean转变为xml格式的文本信息，然后写入到文件当中
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
	 * @Description 另存为
	 * 
	 * @Auther MENDY
	 * 
	 * @Date 2016-5-20 下午04:58:29
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
	 * @Description 判断是否被修改过
	 * 
	 * @Auther MENDY
	 * 
	 * @return
	 * 
	 * @Date 2016-5-20 下午05:00:00
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
	 * @Description 是否允许保存
	 * 
	 * @Auther MENDY
	 * 
	 * @return
	 * 
	 * @Date 2016-5-20 下午05:00:19
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	/**
	 * 设置是否需要隐藏背景消息
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
				// 被测对象
				List<AbstractEditPart> parentNodeParts = editPart.getChildren();
				List<AbstractEditPart> nodeParts = parentNodeParts.get(0).getChildren();
				for (AbstractEditPart nodePart : nodeParts) {
					TestNodePart testNodePart = (TestNodePart) nodePart;
					List<ConnectionEditPart> tarConnParts = testNodePart.getTargetConnections();
					List<ConnectionEditPart> srcConnParts = testNodePart.getSourceConnections();
					for (ConnectionEditPart connPart : tarConnParts) {
						// 找出背景消息的figure，然后设置背景消息的figure的visible属性
						if (connPart instanceof BackgroundChildMsgPart || connPart instanceof BackgroundMsgPart) {
							connPart.getFigure().setVisible(flag);
						}
					}
					for (ConnectionEditPart connPart : srcConnParts) {
						// 找出背景消息的figure，然后设置背景消息的figure的visible属性
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

			// 设置palette初始宽度
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
	 * @Description 打开editor编辑器
	 * @Auther MENDY
	 * @Date 2016-5-18 上午09:41:29
	 */
	private class EditorOpenJob implements IRunnableWithProgress {

		private IEditorInput input;

		public EditorOpenJob(IEditorInput input) {
			this.input = input;
		}

		public void run(IProgressMonitor monitor) {
			monitor.beginTask("正在打开编辑器", 100);
			try {
				IFile file = ((IFileEditorInput) input).getFile();
				// 检查测试用例文件所依赖的ICD文件是否存在
				if (!file.exists() || !ICDFileManager.getInstance().icdFileExists(file)) {
					monitor.done();
					return;
				}

				// 检查file是否是保存的历史记录，
				// 如果file不是被保存的历史记录，则它的父文件夹则应该包含一个名叫.folderProperty的文件,此文件夹为测试集,
				// 并且其父文件夹的父节点应该是工程节点。
				IContainer container = file.getParent();
				if (container instanceof IFolder) {
					IFolder folder = (IFolder) container;
					if (Utils.isTestSuite(folder)) {
					} else {
						// 将编辑器状态设置为不可编辑的状态
						setStatus(TestCaseStatus.ViewResult);
					}
				} else {
					// 将编辑器状态设置为不可编辑的状态
					setStatus(TestCaseStatus.ViewResult);
				}
				// 设置编辑器名字
				toolTipText = file.getFullPath().toOSString().substring(1);
				setPartName(toolTipText);

				ContainerMdl subModel = new ContainerMdl();
				if (getStatus() != null) {
					subModel.setName(getStatus().getText());
				}
				getRootContainerMdl().addChild(subModel);
				TestToolMdl tester = new TestToolMdl();
				tester.initModel();
				tester.setName(Messages.getString("I18N_TEST_TOOL")); // 测试工具
				tester.setLocation(new Point(200, 10)); // 设置测试工具离左边和上边多少距离，x=125，y=10
				tester.setParent(subModel);
				subModel.addChild(tester);
				TestedObjectMdl tested = new TestedObjectMdl();
				tested.initModel();
				tested.setName(Messages.getString("I18N_TESTED_OBJECT"));// 被测对象
				tested.setLocation(new Point(450, 10));// 设置被测对象离左边和上边多少距离
				subModel.addChild(tested);
				monitor.worked(20);
				testCaseBean = (TestCase) TestCaseParser.getInstance(TemplateUtils.getTestCaseSchemaFile(), ((IFileEditorInput) input).getFile(), rooContainerMdl).doParse();
				monitor.worked(20);
				monitor.subTask("解析测试用例...");
				// 测试用例的管理器
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