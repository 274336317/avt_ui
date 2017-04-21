/************************************************************************
 *    Copyright (C) 2000-2012 CoreTek Systems Inc. 
 *              All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.core.tools;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.ui.IFileEditorInput;

import com.coretek.common.template.Attribute;
import com.coretek.common.template.ClazzManager;
import com.coretek.common.template.ICDMsg;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.template.TemplateEngine;
import com.coretek.common.utils.EclipseUtils;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.core.editor.SPTEEditor;
import com.coretek.spte.core.models.AbtConnMdl;
import com.coretek.spte.core.models.AbtElement;
import com.coretek.spte.core.models.MsgConnMdl;
import com.coretek.spte.core.models.TestMdl;
import com.coretek.spte.core.models.TestNodeContainerMdl;
import com.coretek.spte.core.models.TestNodeMdl;
import com.coretek.spte.core.util.Utils;

/**
 * To display nodes' name in a textflow.
 * 
 * @author SunDawei 2012-6-8
 */
public class DisplayNodeNameListener implements MouseMotionListener
{

	private TestMdl		testMdl;

	private TextFlow	textFlow;

	private String		title;

	public DisplayNodeNameListener(TestMdl testMdl, TextFlow textFlow, String title)
	{
		this.testMdl = testMdl;
		this.textFlow = textFlow;
		this.title = title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.draw2d.MouseMotionListener#mouseDragged(org.eclipse.draw2d
	 * .MouseEvent) <br/> <b>Author</b> SunDawei </br> <b>Date</b> 2012-6-8
	 */
	public void mouseDragged(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.draw2d.MouseMotionListener#mouseEntered(org.eclipse.draw2d
	 * .MouseEvent) <br/> <b>Author</b> SunDawei </br> <b>Date</b> 2012-6-8
	 */
	public void mouseEntered(MouseEvent arg0)
	{
		StringBuilder sb = new StringBuilder();
		Set<String> set = Utils.getSimuObjectsIDs(testMdl);
		IFile file = this.getICDFilePath();
		if (file != null && file.exists())
		{
			sb.append(this.title);
			sb.append("\n");
			ClazzManager clazz = TemplateEngine.getEngine().parseICD(file.getLocation().toFile());
			for (String str : set)
			{
				sb.append(clazz.getFunctionNodeName(str));
				sb.append("\n");
			}
		} else
		{
			this.handleError(sb);
		}

		textFlow.setText(sb.toString());
	}

	/**
	 * 获取测试用例文件所依赖的ICD文件的绝对路径
	 * 
	 * @return </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-6-8
	 */
	private IFile getICDFilePath()
	{
		IFile caseFile = this.getCaseFile();

		String icdFilePath = Utils.getICDFilePath(caseFile);
		if (StringUtils.isNotNull(icdFilePath))
		{
			IPath path = new Path(icdFilePath);
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			if (file != null && file.exists())
			{
				return file;
			}
		}

		return null;
	}

	/**
	 * Get the current editor's testCase file.
	 * 
	 * @return </br> <b>Author</b> SunDawei </br> <b>Date</b> 2012-6-8
	 */
	private IFile getCaseFile()
	{
		SPTEEditor part = (SPTEEditor) EclipseUtils.getActiveEditor();
		if (part == null)
			return null;
		IFileEditorInput input = (IFileEditorInput) part.getEditorInput();
		return input.getFile();
	}

	public void handleError(StringBuilder sb)
	{
		sb.append("错误：无法找到ICD文件！");
		sb.append("\n");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.draw2d.MouseMotionListener#mouseExited(org.eclipse.draw2d
	 * .MouseEvent) <br/> <b>Author</b> SunDawei </br> <b>Date</b> 2012-6-8
	 */
	public void mouseExited(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.draw2d.MouseMotionListener#mouseHover(org.eclipse.draw2d.
	 * MouseEvent) <br/> <b>Author</b> SunDawei </br> <b>Date</b> 2012-6-8
	 */
	public void mouseHover(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.draw2d.MouseMotionListener#mouseMoved(org.eclipse.draw2d.
	 * MouseEvent) <br/> <b>Author</b> SunDawei </br> <b>Date</b> 2012-6-8
	 */
	public void mouseMoved(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

}