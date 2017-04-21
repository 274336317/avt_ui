/****************************************
 * 北京科银京成技术有限公司版权所有
 * www.coretek.com.cn
 ***************************************/
package com.coretek.testcase.projectView.action;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorMatchingStrategy;
import org.eclipse.ui.IEditorReference;

import com.coretek.spte.core.editor.SPTEEditor;
import com.coretek.testcase.projectView.ProjectPlugin;

/**
 * @author 孙大巍
 * @date 2011-7-29
 */
public class SPTEEditorDescriptor implements IEditorDescriptor
{

	public IEditorMatchingStrategy getEditorMatchingStrategy()
	{
		return new IEditorMatchingStrategy()
		{

			public boolean matches(IEditorReference editorRef, IEditorInput input)
			{
				return false;
			}
		};
	}

	public String getId()
	{
		return SPTEEditor.ID;
	}

	public ImageDescriptor getImageDescriptor()
	{
		return ProjectPlugin.getImageDescriptor("/icons/coretek.gif");
	}

	public String getLabel()
	{
		return Messages.getString("OpenFileAction_text");
	}

	public boolean isInternal()
	{
		return false;
	}

	public boolean isOpenExternal()
	{
		return false;
	}

	public boolean isOpenInPlace()
	{
		return false;
	}
}
