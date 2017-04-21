/**
 * 
 */
package com.coretek.spte.rcp.utils;

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

/**
 * __________________________________________________________________________________
 * 
 * @Class SimpleColorManager.java
 * @Description
 * @Auther MENDY
 * @Date 2016-5-27 ÉÏÎç10:21:14
 */
public class SimpleColorManager {

	public static ColorRegistry registry;

	static {
		registry = new ColorRegistry();
	}

	public static void put(String symbolicName, RGB colorData) {
		registry.put(symbolicName, colorData);
	}
	
	public static Color get(String symbolicName) {
		return registry.get(symbolicName);
	}
	
	public static Color get(String symbolicName, RGB colorData) {
		if (registry.get(symbolicName) == null) {
			registry.put(symbolicName, colorData);
			return registry.get(symbolicName);
		}
		return registry.get(symbolicName);
	}

}
