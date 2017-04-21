package com.coretek.spte.rcp.image;

import java.io.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;


/**
 * __________________________________________________________________________________
 * @Class IconFactory.java
 * @Description 
 * @Auther MENDY
 * @Date 2016-5-26 ÉÏÎç10:00:39
 */
public class IconFactory {
	/**
	 * This static function returns an ImageDescriptor for a given file
	 */
	public static ImageDescriptor getImageDescriptor(String file_name) {
		return ImageDescriptor.createFromFile(IconFactory.class, file_name);
	}

	/**
	 * This function returns an Image loaded from the given image file
	 */
	public static Image getImage(Widget widget, String file_name) {
		InputStream input = IconFactory.class.getResourceAsStream(file_name);
		Image image = null;
		try {
			image = new Image(widget.getDisplay(), input);
		} catch (org.eclipse.swt.SWTException e) {
			// ShowMessage.printError("[icons] File not found: " + file_name);
			return new Image(widget.getDisplay(), 16, 16);
		}
		return image;
	}
}
