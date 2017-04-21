package com.coretek.spte.core.tools;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;

import com.coretek.common.template.ClazzManager;
import com.coretek.common.template.Helper;
import com.coretek.common.template.TemplateEngine;
import com.coretek.common.template.TemplateUtils;
import com.coretek.common.template.XMLBean;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.common.utils.EclipseUtils;
import com.coretek.spte.core.editor.SPTEEditor;
import com.coretek.spte.core.models.ContainerMdl;
import com.coretek.spte.core.models.TestMdl;
import com.coretek.spte.core.util.ICDFileManager;
import com.coretek.spte.core.util.Utils;
import com.coretek.spte.core.views.MessageView;

/**
 * 监听Editor之间的切换，获取当前活动的Editor，然后更新消息视图
 * 
 * @author 孙大巍
 * @date 2011-2-18
 */
public class PartListener implements IPartListener
{

	private MessageView view;

	public PartListener(MessageView view)
	{
		this.view = view;
	}

	/*
	 * __________________________________________________________________________________
	 * @Class PartListener
	 * @Function partActivated
	 * @Description Editor选择事件
	 * @Auther MENDY
	 * @param part
	 * @Date 2016-5-20 下午04:22:57
	 */
	public void partActivated(IWorkbenchPart part)
	{
		if ((part instanceof IEditorPart) && !(part instanceof SPTEEditor))
		{
			this.showView(part, IPageLayout.ID_OUTLINE);
		}
		else if (part instanceof SPTEEditor)
		{
			this.showView(part, MessageView.MESSAGE_VIEW_ID);
		}
		// else if(part instanceof ConsoleView)
		// {
		//			
		// }
	}

	private void showView(IWorkbenchPart part, String viewID)
	{
		try
		{
			part.getSite().getPage().showView(viewID, null, IWorkbenchPage.VIEW_VISIBLE);
			view.refresh();
		}
		catch (PartInitException e)
		{
			e.printStackTrace();
		}
	}

	public void partBroughtToTop(IWorkbenchPart part)
	{
		// 需要更新消息视图，消息视图中的消息需要根据图形编辑器中的被测对象的节点号来将消息过滤然后显示
		try
		{
			new ProgressMonitorDialog(Display.getCurrent().getActiveShell()).run(true, false, new UpdateMessageViewJob(part));
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public void partClosed(IWorkbenchPart part)
	{

	}

	public void partDeactivated(IWorkbenchPart part)
	{
		if (part instanceof SPTEEditor)
		{
			IEditorReference[] refs = EclipseUtils.getAllEditors();
			if (refs == null || refs.length == 0)
			{
				List<Helper> entityList = new ArrayList<Helper>();
				this.view.setInput(entityList);
			}
		}
	}

	public void partOpened(IWorkbenchPart part)
	{

		if ((part instanceof IEditorPart) && !(part instanceof SPTEEditor))
		{
			this.showView(part, IPageLayout.ID_OUTLINE);
		}
		else if (part instanceof SPTEEditor)
		{
			this.showView(part, MessageView.MESSAGE_VIEW_ID);
		}
	}

	private class UpdateMessageViewJob implements IRunnableWithProgress
	{

		private IWorkbenchPart part;

		public UpdateMessageViewJob(IWorkbenchPart part)
		{
			this.part = part;
		}

		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
		{
			monitor.beginTask("正在渲染消息视图，请耐心等待...", IProgressMonitor.UNKNOWN);
			// 需要更新消息视图，消息视图中的消息需要根据图形编辑器中的被测对象的节点号来将消息过滤然后显示
			if (part instanceof SPTEEditor)
			{
				SPTEEditor editor = (SPTEEditor) part;
				IFile file = EclipseUtils.getInputOfEditor((SPTEEditor) part);
				if (!file.exists() || !ICDFileManager.getInstance().icdFileExists(file))
				{
					return;
				}
				TestMdl mdl = Utils.getTested((ContainerMdl) editor.getRootContainerMdl().getChildren().get(0));
				Entity testCase = mdl.getEmulator().getParent();
				String proPath = null;
				String icdPath = null;
				List<Helper> targets = new ArrayList<Helper>();
				if (testCase instanceof XMLBean)
				{
					proPath = file.getProject().getLocation().toOSString();
					icdPath = TemplateUtils.getICDOfTestCase(testCase);
				}
				if (null != proPath && null != icdPath)
				{
					IPath path = Path.fromPortableString(icdPath);
					File icdFile = path.toFile();
					ClazzManager clazzManager = TemplateEngine.getEngine().parseICD(icdFile);
					targets.addAll(TemplateUtils.getAllFunctionDomains(clazzManager));
					Entity entity = clazzManager.getFighter();
					editor.setFighter(entity);
					editor.setFighterClazzManager(clazzManager);
				}

				view.setInput(targets);
			}

			monitor.done();
		}

	}
}
