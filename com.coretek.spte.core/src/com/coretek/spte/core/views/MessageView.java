package com.coretek.spte.core.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.gef.dnd.DelegatingDragAdapter;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.TransferDragSourceListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.coretek.common.template.Helper;
import com.coretek.common.template.ICDFunctionCell;
import com.coretek.common.template.ICDFunctionCellMsg;
import com.coretek.common.template.ICDFunctionDomain;
import com.coretek.common.template.ICDFunctionDomainMsg;
import com.coretek.common.template.ICDFunctionNode;
import com.coretek.common.template.ICDFunctionNodeMsg;
import com.coretek.common.template.ICDFunctionSubDomain;
import com.coretek.common.template.ICDFunctionSubDomainMsg;
import com.coretek.common.template.TemplateUtils;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.common.utils.EclipseUtils;
import com.coretek.spte.core.editor.SPTEEditor;
import com.coretek.spte.core.image.IconFactory;
import com.coretek.spte.core.image.ImageConstants;
import com.coretek.spte.core.tools.PartListener;
import com.coretek.spte.core.util.ICDFileManager;
import com.coretek.spte.core.util.SPTEConstants;
import com.coretek.spte.core.views.messageviewimage.ImageViewsConstants;
import com.coretek.spte.core.views.messageviewimage.ImageViewsIconFactory;
import com.coretek.spte.testcase.TestedObject;
import com.coretek.spte.testcase.TestedObjects;

/**
 * 消息列表视图，负责将ICD文件中的消息以图形化的形式在树型视图中显示
 * 
 * @author 孙大巍 2011-7-5
 */
public class MessageView extends ViewPart implements PropertyChangeListener {

	public static final String MESSAGE_VIEW_ID = "com.coretek.tools.sequence.MessageView";

	public static final String FUNCTION_DOMAIN = "(功能域)";

	public static final String SUB_FUNCTION_DOMAIN = "(功能子域)";

	public static final String FUNCTION_NODE = "(功能节点)";

	public static final String FUNCTION_CELL = "(功能单元)";

	public static final String FUNCTION_DOMAIN_MSG = "(功能域消息)";

	public static final String SUB_FUNCTION_DOMAIN_MSG = "(功能子域消息)";

	public static final String FUNCTION_NODE_MSG = "(功能节点消息)";

	public static final String FUNCTION_CELL_MSG = "(功能单元消息)";

	private TreeViewer viewer;

	private static MessageView messageView;

	private ResourceChangeListener changeListener;

	private IPartListener partListener;

	private FilterMsgAction filterMsgAction;

	private ShowAllNodeAction showAllNodeAction;

	private String testLevel; // 用于判断测试对象为功能单元还是功能节点

	private static final String LEVEL_NODE = "22";

	private static final String LEVEL_CELL = "21";

	/**
	 * 是否需要过滤无关消息
	 */
	private volatile static boolean unFilterMsg;

	/**
	 * 被测对象ID的列表
	 */
	private/* static */List<String> testObjectsID = new ArrayList<String>();

	private class ViewContentProvider implements ITreeContentProvider {

		private List<Object> typeObjectList = new ArrayList<Object>();

		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof Helper) {
				if (parentElement instanceof ICDFunctionDomain) {// 功能域
					List<Helper> helpers = new ArrayList<Helper>();
					if (unFilterMsg) {// 不需要过滤
						helpers.addAll(((ICDFunctionDomain) parentElement).getDomainMsg());
						helpers.addAll(((ICDFunctionDomain) parentElement).getSubDomains());
					} else {// 需要过滤
						List<ICDFunctionSubDomain> icdFunctionSubDomains = ((ICDFunctionDomain) parentElement).getSubDomains();
						for (ICDFunctionSubDomain icdFunctionSubDomain : icdFunctionSubDomains) {
							if (hasNodeMsgToTestObjectOfSubDomain(icdFunctionSubDomain) && LEVEL_NODE.equals(testLevel)) {
								helpers.add(icdFunctionSubDomain);
							}
							if (hasCellMsgToTestObjectOfSubDomain(icdFunctionSubDomain) && LEVEL_CELL.equals(testLevel)) {
								helpers.add(icdFunctionSubDomain);
							}
						}
					}
					return helpers.toArray();
				} else if (parentElement instanceof ICDFunctionSubDomain) {// 功能子域
					List<Object> typeObject = new ArrayList<Object>();
					if (((ICDFunctionSubDomain) parentElement).getSubDomainMsgs().size() != 0) {
						if (unFilterMsg)
							typeObject.add(((ICDFunctionSubDomain) parentElement).getSubDomainMsgs());
					}
					if (((ICDFunctionSubDomain) parentElement).getCells().size() != 0)
						if (unFilterMsg) {
							typeObject.add(((ICDFunctionSubDomain) parentElement).getCells());
						}
						// 获取功能单元消息
						else {
							List<ICDFunctionCell> icdFunctionCells = ((ICDFunctionSubDomain) parentElement).getCells();
							for (ICDFunctionCell icdFunctionCell : icdFunctionCells) {
								if (hasCellMsgToTestObjectOfCell(icdFunctionCell) && LEVEL_CELL.equals(testLevel)) {
									typeObject.add(icdFunctionCell);
								}
							}
						}
					// 获取功能单元消息结束
					if (((ICDFunctionSubDomain) parentElement).getNodes().size() != 0) {
						if (unFilterMsg) {
							typeObject.add(((ICDFunctionSubDomain) parentElement).getNodes());
						} else {
							List<ICDFunctionNode> icdFunctionNodes = ((ICDFunctionSubDomain) parentElement).getNodes();
							for (ICDFunctionNode icdFunctionNode : icdFunctionNodes) {
								if (hasNodeMsgToTestObjectOfNode(icdFunctionNode) && LEVEL_NODE.equals(testLevel)) {
									typeObject.add(icdFunctionNode);
								}
							}
						}
					}
					typeObjectList = typeObject;
					return typeObject.toArray();
				} else if (parentElement instanceof ICDFunctionNode) {// 功能节点
					List<Helper> helpers = new ArrayList<Helper>();
					List<ICDFunctionNodeMsg> nodeMsgs = ((ICDFunctionNode) parentElement).getNodeMsgs();
					if (!unFilterMsg) {
						for (ICDFunctionNodeMsg nodeMsg : nodeMsgs) {
							if (hasNodeMsgToTestObject(nodeMsg) && LEVEL_NODE.equals(testLevel)) {
								helpers.add(nodeMsg);
							}
						}
					} else {
						helpers.addAll(nodeMsgs);
					}
					return helpers.toArray();
				} else if (parentElement instanceof ICDFunctionCell) {// 功能单元
					List<Helper> helpers = new ArrayList<Helper>();
					List<ICDFunctionCellMsg> cellMsgs = ((ICDFunctionCell) parentElement).getCellMsgs();
					if (unFilterMsg)
						helpers.addAll(((ICDFunctionCell) parentElement).getCellMsgs());
					else {
						for (ICDFunctionCellMsg cellMsg : cellMsgs) {
							if (hasCellMsgToTestObject(cellMsg) && LEVEL_CELL.equals(testLevel)) {
								helpers.add(cellMsg);
							}
						}
					}
					return helpers.toArray();
				}
			} else if (parentElement instanceof List<?>) {
				if (((List<?>) parentElement).size() != 0) {
					return ((List<?>) parentElement).toArray();
				}
			}
			return new Object[0];

		}

		public Object getParent(Object element) {
			if (element instanceof Helper) {
				if (element instanceof ICDFunctionDomainMsg) {
					return ((ICDFunctionDomainMsg) element).getParent();
				} else if (element instanceof ICDFunctionSubDomain) {
					return ((ICDFunctionSubDomain) element).getParent();
				} else if (element instanceof ICDFunctionSubDomainMsg) {
					for (Object obj : typeObjectList) {
						if (obj instanceof List<?>) {
							if (((List<?>) obj).size() != 0) {
								if (((List<?>) obj).get(0) instanceof ICDFunctionSubDomainMsg) {
									return obj;
								}
							}
						}
					}
				} else if (element instanceof ICDFunctionNode) {
					for (Object obj : typeObjectList) {
						if (obj instanceof List<?>) {
							if (((List<?>) obj).size() != 0) {
								if (((List<?>) obj).get(0) instanceof ICDFunctionNode) {
									return obj;
								}
							}
						}
					}
				} else if (element instanceof ICDFunctionCell) {
					for (Object obj : typeObjectList) {
						if (obj instanceof List<?>) {
							if (((List<?>) obj).size() != 0) {
								if (((List<?>) obj).get(0) instanceof ICDFunctionCell) {
									return obj;
								}
							}
						}
					}
				} else if (element instanceof ICDFunctionCellMsg) {
					return ((ICDFunctionCellMsg) element).getParentCell();
				} else if (element instanceof ICDFunctionNodeMsg) {
					return ((ICDFunctionNodeMsg) element).getParentNode();
				}
			} else if (element instanceof List<?>) {
				if (((List<?>) element).size() != 0) {
					if (((List<?>) element).get(0) instanceof ICDFunctionSubDomainMsg) {
						((ICDFunctionSubDomainMsg) ((List<?>) element).get(0)).getParent();
					} else if (((List<?>) element).get(0) instanceof ICDFunctionCell) {
						((ICDFunctionCell) ((List<?>) element).get(0)).getParent();
					} else if (((List<?>) element).get(0) instanceof ICDFunctionNode) {
						((ICDFunctionNode) ((List<?>) element).get(0)).getParent();
					}
				}
			}
			return null;
		}

		public boolean hasChildren(Object element) {
			if (element instanceof Helper) {
				if (element instanceof ICDFunctionDomain) {// 功能域
					return (!((ICDFunctionDomain) element).getDomainMsg().isEmpty()) || (!((ICDFunctionDomain) element).getSubDomains().isEmpty());
				} else if (element instanceof ICDFunctionSubDomain) {// 功能子域
					return (!((ICDFunctionSubDomain) element).getCells().isEmpty()) || (!((ICDFunctionSubDomain) element).getNodes().isEmpty()) || (!((ICDFunctionSubDomain) element).getSubDomainMsgs().isEmpty());
				} else if (element instanceof ICDFunctionNode) {// 功能节点
					return !((ICDFunctionNode) element).getNodeMsgs().isEmpty();
				} else if (element instanceof ICDFunctionCell) {// 功能单元
					// if (unFilterMsg)
					// return !((ICDFunctionCell)
					// element).getCellMsgs().isEmpty();
					// else
					// return false;
					return !((ICDFunctionCell) element).getCellMsgs().isEmpty();
				}
			} else if (element instanceof List<?>) {
				if (((List<?>) element).size() != 0) {
					return true;
				}
			}
			return false;
		}

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List<?>) {
				List<?> list = (List<?>) inputElement;

				if (list.size() != 0) {
					if (unFilterMsg) {
						if (list.get(0) instanceof ICDFunctionSubDomainMsg || list.get(0) instanceof ICDFunctionCell || list.get(0) instanceof ICDFunctionNode) {
							List<Object> objList = new ArrayList<Object>();
							objList.add(inputElement);
							return objList.toArray();
						} else {
							return list.toArray();
						}
					} else {
						List<Object> objects = new ArrayList<Object>();
						if (list.get(0) instanceof ICDFunctionDomain) {
							for (Object obj : list) {
								if (obj instanceof ICDFunctionDomain) {
									if (hasNodeMsgToTestObjectOfDomain((ICDFunctionDomain) obj) && LEVEL_NODE.equals(testLevel)) {
										objects.add(obj);
									}
									if (hasCellMsgToTestObjectOfDomain((ICDFunctionDomain) obj) && LEVEL_CELL.equals(testLevel)) {
										objects.add(obj);
									}
								}
							}
							return objects.toArray();
						}
						return list.toArray();
					}

				}
			}
			return new Object[0];
		}

		/**
		 * 判断一个节点消息是否和被测对象相关
		 * 第一，判断消息的源节点是否是被测对象，如果是，则还需要判断消息是否为被测对象内部的消息，如果是，则返回false；
		 * 第二，判断消息的源节点是否为测试工具，如果是，则还需要判断消息是否为测试工具内部的消息，如果是，则返回false。
		 * 
		 * @param nodeMsg
		 * @return
		 */
		private boolean hasNodeMsgToTestObject(ICDFunctionNodeMsg nodeMsg) {
			// 是否为广播消息
			if (TemplateUtils.isBroadcast(nodeMsg))
				return true;
			// 是否为被测对象内部的消息
			if (TemplateUtils.isInternalMsgOfTestedObjects(nodeMsg, testObjectsID)) {
				return false;
			}
			// 是否为测试工具内部的消息
			if (TemplateUtils.isInternalMsgOfToolObjects(nodeMsg, testObjectsID)) {
				return false;
			}
			return true;
		}

		private boolean hasCellMsgToTestObject(ICDFunctionCellMsg cellMsg) {
			// 是否为广播消息
			if (TemplateUtils.isBroadcast(cellMsg))
				return true;
			// 是否为被测对象内部的消息
			if (TemplateUtils.isInternalMsgOfTestedObjects(cellMsg, testObjectsID)) {
				return false;
			}
			// 是否为测试工具内部的消息
			if (TemplateUtils.isInternalMsgOfToolObjects(cellMsg, testObjectsID)) {
				return false;
			}
			return true;
		}

		/**
		 * 判断功能节点下的节点消息是否存在与被测对象相关消息。 存在返回true
		 * 
		 * @param node
		 * @return
		 */
		private boolean hasNodeMsgToTestObjectOfNode(ICDFunctionNode node) {
			List<ICDFunctionNodeMsg> icdFunctionNodeMsgs = node.getNodeMsgs();
			for (ICDFunctionNodeMsg icdFunctionNodeMsg : icdFunctionNodeMsgs) {
				if (hasNodeMsgToTestObject(icdFunctionNodeMsg)) {
					return true;
				}
			}
			return false;
		}

		private boolean hasCellMsgToTestObjectOfCell(ICDFunctionCell cell) {
			List<ICDFunctionCellMsg> icdFunctionCellMsgs = cell.getCellMsgs();
			for (ICDFunctionCellMsg icdFunctionCellMsg : icdFunctionCellMsgs) {
				if (hasCellMsgToTestObject(icdFunctionCellMsg)) {
					return true;
				}
			}
			return false;
		}

		/**
		 * 判断功能子域下的是否存在包含节点消息域被测对象相关的功能节点
		 * 
		 * @param icdFunctionSubDomain
		 * @return
		 */
		private boolean hasNodeMsgToTestObjectOfSubDomain(ICDFunctionSubDomain icdFunctionSubDomain) {
			List<ICDFunctionNode> icdFunctionNodes = icdFunctionSubDomain.getNodes();
			for (ICDFunctionNode icdFunctionNode : icdFunctionNodes) {
				if (hasNodeMsgToTestObjectOfNode(icdFunctionNode)) {
					return true;
				}
			}
			return false;
		}

		private boolean hasCellMsgToTestObjectOfSubDomain(ICDFunctionSubDomain icdFunctionSubDomain) {
			List<ICDFunctionCell> icdFunctionCells = icdFunctionSubDomain.getCells();
			for (ICDFunctionCell icdFunctionCell : icdFunctionCells) {
				if (hasCellMsgToTestObjectOfCell(icdFunctionCell)) {
					return true;
				}
			}
			return false;
		}

		/**
		 * 判断功能域下是否存在与被测对象相关的节点消息
		 * 
		 * @param icdFunctionDomain
		 * @return
		 */
		private boolean hasNodeMsgToTestObjectOfDomain(ICDFunctionDomain icdFunctionDomain) {
			List<ICDFunctionSubDomain> icdeFunctionSubDomains = icdFunctionDomain.getSubDomains();
			for (ICDFunctionSubDomain icdeFunctionSubDomain : icdeFunctionSubDomains) {
				if (hasNodeMsgToTestObjectOfSubDomain(icdeFunctionSubDomain)) {
					return true;
				}
			}
			return false;
		}

		private boolean hasCellMsgToTestObjectOfDomain(ICDFunctionDomain icdFunctionDomain) {
			List<ICDFunctionSubDomain> icdeFunctionSubDomains = icdFunctionDomain.getSubDomains();
			for (ICDFunctionSubDomain icdeFunctionSubDomain : icdeFunctionSubDomains) {
				if (hasCellMsgToTestObjectOfSubDomain(icdeFunctionSubDomain)) {
					return true;
				}
			}
			return false;
		}

		public void dispose() {

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}
	}

	private static class ViewLabelProvider implements ILabelProvider {

		public Image getImage(Object obj) {

			return (Image) getInfo(obj, "");
			// return
			// PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}

		/**
		 * __________________________________________________________________________________
		 * 
		 * @Class ViewLabelProvider
		 * @Function getInfo
		 * @Description TODO(处理消息视图树图片)
		 * @Auther MENDY
		 * @param m_model
		 * @param type
		 * @return Object
		 * @Date 2016-5-10 上午10:03:29__________________________________________________________________________________
		 */
		private Object getInfo(Object m_model, String type) {
			//
			// 设置解析ICDB文件消息树图片显示，通过对象model来判断
			//
			Object m_result = null;
			if (m_model instanceof ICDFunctionDomain) {
				ICDFunctionDomain m_FunDomain = (ICDFunctionDomain) m_model;
				if ("text".equals(type)) {
					m_result = m_FunDomain.getAttribute("name").getValue() + FUNCTION_DOMAIN;
				} else {
					m_result = ImageViewsIconFactory.getImageDescriptor(ImageViewsConstants.FUNCTION_DOMAIN).createImage();
				}
			}
			if (m_model instanceof ICDFunctionSubDomain) {
				ICDFunctionSubDomain m_FunSubDomain = (ICDFunctionSubDomain) m_model;
				if ("text".equals(type)) {
					m_result = m_FunSubDomain.getAttribute("name").getValue() + SUB_FUNCTION_DOMAIN;
				} else {
					m_result = ImageViewsIconFactory.getImageDescriptor(ImageViewsConstants.SUB_FUNCTION_DOMAIN).createImage();
				}
			}
			if (m_model instanceof ICDFunctionNode) {
				ICDFunctionNode m_FunNode = (ICDFunctionNode) m_model;
				if ("text".equals(type)) {
					m_result = m_FunNode.getAttribute("name").getValue() + FUNCTION_NODE;
				} else {
					m_result = ImageViewsIconFactory.getImageDescriptor(ImageViewsConstants.FUNCTION_NODE).createImage();
				}
			}
			if (m_model instanceof ICDFunctionNodeMsg) {
				ICDFunctionNodeMsg m_FunNodeMsg = (ICDFunctionNodeMsg) m_model;
				// System.out.println(m_FunNodeMsg.getSpteMsg().getMsg().getDirection());
				if ("text".equals(type)) {
					m_result = m_FunNodeMsg.getAttribute("name").getValue() + FUNCTION_NODE_MSG;
				} else {
					m_result = ImageViewsIconFactory.getImageDescriptor(ImageViewsConstants.FUNCTION_NODE_MSG).createImage();
				}
			}

			return m_result;
		}

		/**
		 * TODO (获取所有消息是功能节点)
		 */
		public String getText(Object element) {
			if (element instanceof Helper) {
				if (element instanceof ICDFunctionDomain) {// 功能域
					return ((ICDFunctionDomain) element).getAttribute("name").getValue() + FUNCTION_DOMAIN;
				} else if (element instanceof ICDFunctionSubDomain) {// 功能子域
					return ((ICDFunctionSubDomain) element).getAttribute("name").getValue() + SUB_FUNCTION_DOMAIN;
				} else if (element instanceof ICDFunctionNode) {// 功能节点
					return ((ICDFunctionNode) element).getAttribute("name").getValue() + FUNCTION_NODE;
				} else if (element instanceof ICDFunctionCell) {// 功能单元
					return ((ICDFunctionCell) element).getAttribute("name").getValue() + FUNCTION_CELL;
				} else if (element instanceof ICDFunctionDomainMsg) {// 功能域消息
					return ((ICDFunctionDomainMsg) element).getAttribute("msgName").getValue() + FUNCTION_DOMAIN_MSG;
				} else if (element instanceof ICDFunctionSubDomainMsg) {// 功能子域消息
					return ((ICDFunctionSubDomainMsg) element).getAttribute("msgName").getValue() + SUB_FUNCTION_DOMAIN_MSG;
				} else if (element instanceof ICDFunctionNodeMsg) {// 功能节点消息
					return ((ICDFunctionNodeMsg) element).getAttribute("msgName").getValue() + FUNCTION_NODE_MSG;
				} else if (element instanceof ICDFunctionCellMsg) {// 功能单元消息
					return ((ICDFunctionCellMsg) element).getAttribute("msgName").getValue() + FUNCTION_CELL_MSG;
				}
			} else if (element instanceof List<?>) {
				if (((List<?>) element).size() != 0) {
					if (((List<?>) element).get(0) instanceof ICDFunctionSubDomainMsg) {
						return "功能子域消息";
					} else if (((List<?>) element).get(0) instanceof ICDFunctionCell) {
						return "功能单元";
					} else if (((List<?>) element).get(0) instanceof ICDFunctionNode) {
						return "功能节点";
					}
				}

			}
			return "";
		}

		public void addListener(ILabelProviderListener listener) {

		}

		public void dispose() {

		}

		public boolean isLabelProperty(Object element, String property) {

			return false;
		}

		public void removeListener(ILabelProviderListener listener) {

		}
	}

	/**
	 * The constructor.
	 */
	public MessageView() {
		if (messageView == null) {
			messageView = this;
		}
		unFilterMsg = false;
		initEditorObject();
		this.changeListener = new ResourceChangeListener();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this.changeListener);
	}

	public static MessageView getDefault() {
		if (messageView == null) {
			messageView = new MessageView();
		}
		return messageView;
	}

	private void makeAction() {
		ImageDescriptor filterMsgImage = IconFactory.getImageDescriptor(ImageConstants.SPTE_FILTER);
		ImageDescriptor allMsgImage = IconFactory.getImageDescriptor(ImageConstants.SPTE_ALL_NODE);
		filterMsgAction = new FilterMsgAction(this, "过滤无关消息", filterMsgImage);
		showAllNodeAction = new ShowAllNodeAction(this, "展示所有消息", allMsgImage);

	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(filterMsgAction);
		manager.add(showAllNodeAction);
	}

	private void ContributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalToolBar(bars.getToolBarManager());
	}

	public static boolean isFilterMsg() {
		return unFilterMsg;
	}

	public static void setFilterMsg(boolean filterMsg) {
		MessageView.unFilterMsg = filterMsg;
	}

	/**
	 * 设置视图的输入
	 */
	public void setInput(final List<Helper> targets) {
		if (this.viewer != null) {
			initEditorObject();
			Display.getDefault().asyncExec(new Runnable() {

				public void run() {
					viewer.setInput(targets);
					viewer.expandToLevel(3);
					viewer.refresh();
				}

			});

		}
	}

	/**
	 * 刷新视图
	 */
	public void refresh() {
		if (this.viewer != null) {
			initEditorObject();
			Display.getDefault().asyncExec(new Runnable() {

				public void run() {
					viewer.expandToLevel(3);
					viewer.refresh();
				}

			});

		}
	}

	/**
	 * 初始化测试对象ID列表
	 */
	private void initEditorObject() {
		IEditorPart editor = EclipseUtils.getActiveEditor();
		if (editor instanceof SPTEEditor) {
			SPTEEditor spteEditor = (SPTEEditor) editor;

			IFileEditorInput input = (IFileEditorInput) spteEditor.getEditorInput();
			IFile file = input.getFile();
			if (file.exists() && ICDFileManager.getInstance().icdFileExists(file)) {
				for (Entity entity : ((SPTEEditor) editor).getTestedObject().getChildren()) {
					testObjectsID.clear();
					if (entity instanceof TestedObject) {
						testObjectsID.add(((TestedObject) entity).getId());
					}
				}
				// 用于判断测试对象为功能单元还是功能节点
				testLevel = ((TestedObjects) ((SPTEEditor) editor).getTestedObject()).getLevel();

			}
		}
	}

	public void createPartControl(Composite parent) {

		parent.setLayoutData(new GridLayout());

		Composite mainPanel = new Composite(parent, SWT.BORDER);
		GridLayout grid = new GridLayout();
		grid.numColumns = 1;
		mainPanel.setLayout(grid);
		GridData data = new GridData(GridData.FILL_BOTH);
		mainPanel.setLayoutData(data);

		viewer = new TreeViewer(mainPanel, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		data = new GridData(GridData.FILL_BOTH);
		grid = new GridLayout();
		grid.numColumns = 1;
		viewer.getTree().setLayoutData(data);
		viewer.getTree().setLayout(grid);
		viewer.getTree().addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {

			}

			public void widgetSelected(SelectionEvent e) {
				if (e.getSource() instanceof Tree) {
					TreeSelection selection = (TreeSelection) viewer.getSelection();
					Object obj = selection.getFirstElement();

					// 打开消息属性视图，并展示当前结果
					IWorkbenchPage page = EclipseUtils.getActivePage();
					IViewPart part = page.findView("com.coretek.tools.sequence.MessagePropertyView");
					if (part instanceof MessagePropertyView) {

						try {
							page.showView("com.coretek.tools.sequence.MessagePropertyView");
						} catch (PartInitException e1) {
							e1.printStackTrace();
						}
						if (null != obj && obj instanceof Helper) {
							((MessagePropertyView) part).setInput((Helper) obj);
						} else {
							((MessagePropertyView) part).setInput(null);
						}

					}
				}

			}
		});

		addDragSupport(viewer);
		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "view.viewer");
		this.partListener = new PartListener(this);
		this.getViewSite().getWorkbenchWindow().getPartService().addPartListener(this.partListener);

		makeAction();
		ContributeToActionBars();

	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	@Override
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this.changeListener);
		this.getViewSite().getWorkbenchWindow().getPartService().removePartListener(this.partListener);
		super.dispose();
	}

	private static class ResourceChangeListener implements IResourceChangeListener {

		public void resourceChanged(final IResourceChangeEvent event) {
			if (event.getType() == IResourceChangeEvent.PRE_DELETE) {
				IResource resource = event.getResource();
				if (resource instanceof IProject) {
					IProject project = (IProject) resource;
					// MessageManager.removeByProject(project);
					// TODO:删除项目时；暂时不改变项目视图的显示
					// MessageView.getDefault().setInput(MessageManager.getMessagesArray());
				}
			}
		}
	}

	public void propertyChange(final PropertyChangeEvent evt) {
		if (SPTEConstants.PROP_MESSAGE_CHANGED.equals(evt.getPropertyName())) {
			if (!(EclipseUtils.getActiveEditor() instanceof SPTEEditor) || viewer == null) {
				return;
			}
			SPTEEditor editor = (SPTEEditor) EclipseUtils.getActiveEditor();
			if (editor == null) {
				viewer.setInput(null);
			} else {
				List<Helper> list = new ArrayList<Helper>();
				if (list.size() > 0) {
					viewer.setInput(list);
					viewer.expandAll();
				} else {
					viewer.setInput(null);
				}
			}
		}

	}

	/**
	 * 添加拖放支持，在拖拽的时候必须检查被拖拽的消息的源或目标是否为被测试节点
	 * 
	 * @param viewer
	 */
	private void addDragSupport(final TreeViewer viewer) {
		DelegatingDragAdapter dragAdapter = new DelegatingDragAdapter();
		dragAdapter.addDragSourceListener(new TransferDragSourceListener() {

			public void dragStart(DragSourceEvent event) {
				TreeSelection selection = (TreeSelection) viewer.getSelection();
				Object obj = selection.getFirstElement();
				if (obj != null && obj instanceof Helper) {
					IEditorPart part = EclipseUtils.getActiveEditor();
					if (part instanceof SPTEEditor) {// 禁止拖动到其它类型的Editor中
						event.doit = true;
					} else {
						event.doit = false;
					}

				} else {
					event.doit = false;
				}
			}

			public void dragSetData(DragSourceEvent event) {
				TreeSelection selection = (TreeSelection) viewer.getSelection();
				TreePath[] paths = selection.getPaths();
				Helper children = (Helper) paths[0].getLastSegment();
				event.data = children;
			}

			public void dragFinished(DragSourceEvent event) {

			}

			public Transfer getTransfer() {
				TemplateTransfer transfer = TemplateTransfer.getInstance();
				TreeSelection selection = (TreeSelection) viewer.getSelection();
				TreePath[] paths = selection.getPaths();
				Helper children = (Helper) paths[0].getLastSegment();
				transfer.setTemplate(children);
				return transfer;
			}
		});
		viewer.addDragSupport(DND.DROP_COPY, new Transfer[] { TemplateTransfer.getInstance() }, dragAdapter);
	}
}