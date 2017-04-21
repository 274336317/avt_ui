package com.coretek.spte.core.views.messageviewimage;

import java.io.InputStream;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Widget;

import com.coretek.spte.core.util.ShowMessage;


public class ImageViewsIconFactory
{
	public static ImageDescriptor getImageDescriptor(String file_name)
	{
		return ImageDescriptor.createFromFile(ImageViewsIconFactory.class, file_name);
	}
	
	public static Image getImage(Widget widget, String file_name)
	{
		InputStream input = ImageViewsIconFactory.class.getResourceAsStream(file_name);
		Image image = null;

		try
		{
			image = new Image(widget.getDisplay(), input);
		} catch (org.eclipse.swt.SWTException e)
		{
			ShowMessage.printError("[icons] File not found: " + file_name);
			return new Image(widget.getDisplay(), 16, 16);
		}

		return image;
	}
}
