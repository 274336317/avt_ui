/**
 * 
 */
package com.coretek.spte.rcp.utils;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;

import com.coretek.spte.rcp.SPTEPlugin;
import com.coretek.spte.rcp.image.IconFactory;
import com.coretek.spte.rcp.image.ImageConstants;

/**
 * __________________________________________________________________________________
 * 
 * @Class LookAndFeel.java
 * @Description
 * @Auther MENDY
 * @Date 2016-5-27 ÉÏÎç10:27:52
 */
public class LookAndFeel {

	private static LookAndFeel instance;
	public static final String TYPE_BLUE = "blue";
	public static final String TYPE_PURPLE = "purple";
	public static String CURRENT_TYPE = "blue";

	public LookAndFeel() {
		IDialogSettings settings = SPTEPlugin.getDefault().getDialogSettings();
		IDialogSettings rcp_settings = settings.getSection(IPreferencePageConstants.RCP_SETTING);
		if (rcp_settings != null) {
			if (rcp_settings.get(IPreferencePageConstants.SKIN_TYPE) != null && !"".equals(rcp_settings.get(IPreferencePageConstants.SKIN_TYPE))) {
				CURRENT_TYPE = rcp_settings.get(IPreferencePageConstants.SKIN_TYPE);
			}
		}
	}

	synchronized public static LookAndFeel getDefault() {
		if (instance == null) {
			instance = new LookAndFeel();
		}
		return instance;
	}

	public Image getToolBarBGImage() {
//		if (LookAndFeel.TYPE_BLUE.equals(LookAndFeel.CURRENT_TYPE)) {
//			return IconFactory.getImageDescriptor(ImageConstants.BG_BULE).createImage();
//		} else if (LookAndFeel.TYPE_PURPLE.equals(LookAndFeel.CURRENT_TYPE)) {
//			return IconFactory.getImageDescriptor(ImageConstants.BG_WHITE2).createImage();
//		} else {
//			return IconFactory.getImageDescriptor(ImageConstants.BG_WHITE).createImage();
//		}
		return IconFactory.getImageDescriptor(ImageConstants.BG_BG).createImage();
	}
	
	public Color getToolBarBGColor() {
		if (LookAndFeel.TYPE_BLUE.equals(LookAndFeel.CURRENT_TYPE)) {
			return new Color(null, 219, 226, 232);
		} else if (LookAndFeel.TYPE_PURPLE.equals(LookAndFeel.CURRENT_TYPE)) {
			return new Color(null, 255, 255, 255);
		} else {
			return new Color(null, 219, 226, 232);
		}
	}

	public Image getMenuBGImage() {
		if (LookAndFeel.TYPE_BLUE.equals(LookAndFeel.CURRENT_TYPE)) {
			return IconFactory.getImageDescriptor(ImageConstants.BG_WHITE).createImage();
		} else if (LookAndFeel.TYPE_PURPLE.equals(LookAndFeel.CURRENT_TYPE)) {
			return IconFactory.getImageDescriptor(ImageConstants.BG_WHITE2).createImage();
		} else {
			return IconFactory.getImageDescriptor(ImageConstants.BG_WHITE).createImage();
		}
	}
	
	public Color getMenuBGColor() {
		return new Color(null, 255,255,255);
	}

	public Image getContentBGImage() {
		if (LookAndFeel.TYPE_BLUE.equals(LookAndFeel.CURRENT_TYPE)) {
			return IconFactory.getImageDescriptor(ImageConstants.BG_BULE).createImage();
		} else if (LookAndFeel.TYPE_PURPLE.equals(LookAndFeel.CURRENT_TYPE)) {
			return IconFactory.getImageDescriptor(ImageConstants.BG_WHITE2).createImage();
		} else {
			return IconFactory.getImageDescriptor(ImageConstants.BG_WHITE).createImage();
		}
	}
	
	public Image getFastViewBarBGImage() {
		return IconFactory.getImageDescriptor(ImageConstants.BG_BULE).createImage();
	}
	
	public Color getFastViewBarBGColor() {
		RGB rgb = new RGB(219, 226, 232);
		return SimpleColorManager.get(rgb.toString(), rgb);
	}
	
	public Image getTrimCommonUIHandleBGImage() {
		return IconFactory.getImageDescriptor(ImageConstants.BG_BG).createImage();
	}
	
	public Color getTrimCommonUIHandleBGColor() {
		RGB rgb = new RGB(219, 226, 232);
		return SimpleColorManager.get(rgb.toString(), rgb);
	}
	
	public Color getCompositeBGColor() {
		RGB rgb = new RGB(255, 255, 255);
		return SimpleColorManager.get(rgb.toString(), rgb);
	}
	
	public Image getCompositeBGImage() {
		return IconFactory.getImageDescriptor(ImageConstants.BG_BULE).createImage();
	}

	public Color getShellColor() {
		if (LookAndFeel.TYPE_BLUE.equals(LookAndFeel.CURRENT_TYPE)) {
			RGB rgb = new RGB(151, 205, 255);
			return SimpleColorManager.get(rgb.toString(), rgb);
		} else if (LookAndFeel.TYPE_PURPLE.equals(LookAndFeel.CURRENT_TYPE)) {
			RGB rgb = new RGB(239, 213, 253);
			return SimpleColorManager.get(rgb.toString(), rgb);
		} else {
			RGB rgb = new RGB(151, 205, 255);
			return SimpleColorManager.get(rgb.toString(), rgb);
		}
	}

	public Color getTabFolderBGColor() {
		if (LookAndFeel.TYPE_BLUE.equals(LookAndFeel.CURRENT_TYPE)) {
			//RGB rgb = new RGB(210, 234, 255);
			RGB rgb = new RGB(255, 255, 255);
			return SimpleColorManager.get(rgb.toString(), rgb);
		} else if (LookAndFeel.TYPE_PURPLE.equals(LookAndFeel.CURRENT_TYPE)) {
			RGB rgb = new RGB(226, 206, 249);
			return SimpleColorManager.get(rgb.toString(), rgb);
		} else {
			RGB rgb = new RGB(210, 234, 255);
			return SimpleColorManager.get(rgb.toString(), rgb);
		}
	}

	public Font getControlFont() {
		IDialogSettings settings = SPTEPlugin.getDefault().getDialogSettings();
		IDialogSettings rcp_settings = settings.getSection(IPreferencePageConstants.RCP_SETTING);
		if (rcp_settings != null) {
			String fontname = rcp_settings.get(IPreferencePageConstants.SYSTEM_CONTROL_FONT_NAME);
			if (fontname != null && !"".equals(fontname)) {
				int fontheight = rcp_settings.getInt(IPreferencePageConstants.SYSTEM_CONTROL_FONT_HEIGHT);
				int fontstyle = rcp_settings.getInt(IPreferencePageConstants.SYSTEM_CONTROL_FONT_STYLE);
				FontData fontdata = new FontData(fontname, fontheight, fontstyle);
				return SimpleFontManager.get(fontdata.getName() + "," + fontdata.getStyle() + "," + fontdata.getHeight(), new FontData[] { fontdata });
			}
		}
		return null;
	}

	public Color getControlBGColor() {
		IDialogSettings settings = SPTEPlugin.getDefault().getDialogSettings();
		IDialogSettings rcp_settings = settings.getSection(IPreferencePageConstants.RCP_SETTING);
		if (rcp_settings != null) {
			String controlcolor = rcp_settings.get(IPreferencePageConstants.CONTROL_BG_CORLOR);
			return getColor(controlcolor);
		}
		RGB rgb = new RGB(255, 255, 255);
		return SimpleColorManager.get(rgb.toString(), rgb);
	}

	public Color getLabelBGColor() {
		IDialogSettings settings = SPTEPlugin.getDefault().getDialogSettings();
		IDialogSettings rcp_settings = settings.getSection(IPreferencePageConstants.RCP_SETTING);
		if (rcp_settings != null) {
			String labelcolor = rcp_settings.get(IPreferencePageConstants.LABEL_BG_CORLOR);
			return getColor(labelcolor);
		}
		return null;
	}

	private Color getColor(String scolor) {
		if (scolor.equals("")) {
			return null;
		}
		String[] colors = scolor.split(",");
		int red = Integer.parseInt(colors[0]);
		int green = Integer.parseInt(colors[1]);
		int blue = Integer.parseInt(colors[2]);
		RGB rgb = new RGB(red, green, blue);
		return SimpleColorManager.get(rgb.toString(), rgb);
	}

	public Color getFontColor() {
		IDialogSettings settings = SPTEPlugin.getDefault().getDialogSettings();
		IDialogSettings rcp_settings = settings.getSection(IPreferencePageConstants.RCP_SETTING);
		if (rcp_settings != null) {
			String fontcolor = rcp_settings.get(IPreferencePageConstants.SYSTEM_CONTROL_FONT_CORLOR);
			return getColor(fontcolor);
		}
		return null;
	}

}
