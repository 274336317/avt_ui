package com.coretek.spte.core.util;

import java.util.Hashtable;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.coretek.spte.core.SPTEPlugin;

/**
 * 系统中的图片管理
 * 
 * @author 孙大巍
 * 
 */
public class ImageManager
{

	private static Hashtable<String, Image>				images		= new Hashtable<String, Image>();

	private static Hashtable<String, ImageDescriptor>	imageDesc	= new Hashtable<String, ImageDescriptor>();

	public synchronized static Image getImage(String path)
	{
		Image image = images.get(path);
		if (image == null)
		{
			if (imageDesc.get(path) != null)
			{
				image = imageDesc.get(path).createImage();
			} else
			{
				image = SPTEPlugin.getImageDescriptor(path).createImage();
			}
			images.put(path, image);
		}
		if (image == null)
		{
			image = SPTEPlugin.getImageDescriptor(path).createImage();
			images.put(path, image);
		}
		return image;
	}
	
	public synchronized static ImageDescriptor getImageDescriptor(String path)
	{
		ImageDescriptor des = imageDesc.get(path);
		if (des == null)
		{
			des = SPTEPlugin.getImageDescriptor(path);
			imageDesc.put(path, des);
		}
		return des;
	}


	public synchronized static boolean remove(String path)
	{
		return images.remove(path) != null;
	}

	public synchronized static boolean removeDescriptor(String path)
	{
		return imageDesc.remove(path) != null;
	}

	public synchronized static boolean remove(Image image)
	{
		String key = null;
		for (Map.Entry<String, Image> entry : images.entrySet())
		{
			if (image == entry.getValue())
			{
				key = entry.getKey();
			}
		}
		return images.remove(key) == null;
	}

	public synchronized static void dispose()
	{
		for (Map.Entry<String, Image> entry : images.entrySet())
		{
			entry.getValue().dispose();
		}
		images.clear();
		imageDesc.clear();
	}
}
