package com.cortek.spte.core.editmanager;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.coretek.spte.core.models.PostilChildMdl;

/**
 * 标签的直接编辑管理器
 * 
 * @author duyisen 2012-3-2
 */
public class PostilMdlDirectEditManager extends DirectEditManager
{

	public PostilMdlDirectEditManager(GraphicalEditPart source,

	Class editorType, CellEditorLocator locator)
	{

		super(source, editorType, locator);

	}

	@Override
	protected void initCellEditor()
	{

		CellEditor cellEditor = getCellEditor();
		cellEditor.setStyle(SWT.MULTI);
		Text text = (Text) cellEditor.getControl();

		PostilChildMdl model = (PostilChildMdl) getEditPart().getModel();

		text.setText(model.getText());

	}

	@Override
	protected CellEditor createCellEditorOn(Composite composite)
	{
		CellEditor editor = new TextCellEditor(composite, SWT.MULTI);

		return editor;
	}

}
