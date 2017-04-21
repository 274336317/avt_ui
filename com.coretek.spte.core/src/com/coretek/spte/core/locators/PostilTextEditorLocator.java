package com.coretek.spte.core.locators;

import org.eclipse.draw2d.TextUtilities;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Text;

import com.coretek.spte.core.models.PostilChildMdl;
import com.coretek.spte.core.util.Utils;

/**
 * 定位器，用于定位TextEditor的位置
 * 
 * @author duyisen 2012-3-15
 */
public class PostilTextEditorLocator implements CellEditorLocator
{

	private PostilChildMdl	postilMdl;

	public PostilTextEditorLocator(PostilChildMdl postilMdl)
	{
		super();
		this.postilMdl = postilMdl;
	}

	public void relocate(CellEditor celleditor)
	{
		Text text = (Text) celleditor.getControl();
		Rectangle constraints = postilMdl.getConstraints();

		constraints = Utils.convertAbToRe(constraints);

		text.setBounds(constraints.x, constraints.y, constraints.width, constraints.height);

		text.addModifyListener(new ModifyListener()
		{

			public void modifyText(ModifyEvent e)
			{
				if (e.getSource() instanceof Text)
				{
					Text txt = ((Text) e.getSource());
					Dimension dimension = TextUtilities.INSTANCE.getTextExtents(txt.getText(), txt.getFont());
					txt.setBounds(txt.getBounds().x, txt.getBounds().y, dimension.width, dimension.height);
					Rectangle rectangle = new Rectangle(txt.getBounds().x, txt.getBounds().y, txt.getBounds().width, txt.getBounds().height);
					rectangle = Utils.convertReToAb(rectangle);
					postilMdl.setConstraints(rectangle);
				}

			}
		});

	}

}
