/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.cfg;

import static com.coretek.spte.monitor.cfg.CfgDialog.MONITOR_FILE_SUFFIX;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

/**
 * @author SunDawei
 * 
 * @date 2012-10-8
 */
class MonitorConfigProcesser
{
	private CfgDialog	dlg;

	MonitorConfigProcesser(CfgDialog dlg)
	{
		this.dlg = dlg;
	}

	/**
	 * 保存监控配置到指定路径下
	 * 
	 * @param path 指定的监控配置保存路径文件
	 * @throws IOException
	 */
	public void write(String path) throws IOException
	{
		if (path == null || path.trim().length() == 0)
		{
			return;
		}
		File file = new File(path);
		if (!file.exists())
		{
			file.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(this.dlg.getEndianCombo().getText() != null && this.dlg.getEndianCombo().getSelectionIndex() == 0 ? "big" : "little");
		oos.writeObject(this.dlg.getICDFilesCombo().getText() != null && !this.dlg.getICDFilesCombo().getText().equals("") ? this.dlg.getICDFilesCombo().getText() : "");
		Object[] objs = this.dlg.getViewer().getCheckedElements();
		oos.writeObject(objs);
		Object[] objs2 = this.dlg.getTopicViewer().getCheckedElements();
		oos.writeObject(objs2);
		fos.close();
	}

	/**
	 * 加载监控配置文件
	 * 
	 * @param path 监控配置文件路径
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void read(String path)
	{
		if (path == null || path.trim().length() == 0)
		{
			return;
		}
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try
		{
			fis = new FileInputStream(path);
			ois = new ObjectInputStream(fis);
			String endian = (String) ois.readObject();
			if (endian.equals("little"))
			{
				this.dlg.getEndianCombo().select(1);
			} else
			{
				this.dlg.getEndianCombo().select(0);
			}
			this.dlg.setICDPath((String) ois.readObject());
			boolean contained = false; // 标示从监控配置文件中读取的ICD文件是否已经存在在ICD文件选择对话框中，
			for (int i = 0; i < this.dlg.getICDFilesCombo().getItemCount(); i++)
			{
				if (this.dlg.getICDPath().equals(this.dlg.getICDFilesCombo().getItem(i)))
				{
					this.dlg.getICDFilesCombo().select(i);
					contained = true;
					break;
				}
			}
			if (!contained)
			{ // 如果不存在需要将其添加到ICD文件选择对话框中
				this.dlg.getICDFilesCombo().add(this.dlg.getICDPath());
				this.dlg.getICDFilesCombo().select(this.dlg.getICDFilesCombo().getItemCount() - 1);
			}
			Object[] objs = (Object[]) ois.readObject();
			this.dlg.getViewer().setCheckedElements(objs);
			this.dlg.getTopicViewer().setCheckedElements((Object[]) ois.readObject());
		} catch (Exception e)
		{
			e.printStackTrace();
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "错误", "配置文件读取失败!");
		} finally
		{
			if (ois != null)
			{
				try
				{
					ois.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				} finally
				{
					if (fis != null)
					{
						try
						{
							fis.close();
						} catch (IOException e)
						{
							e.printStackTrace();
						}
					}
				}
			}
		}

	}

	/**
	 * 读取默认配置文件下的所有监控配置，并将文件后缀名截取掉用于呈现在配置文件的选择
	 * 
	 * @return
	 */
	public String[] listAllFile()
	{
		File dir = new File(this.dlg.getConfDir());
		List<String> list = new ArrayList<String>();
		if (dir.isDirectory())
		{
			String[] files = dir.list();
			for (String f : files)
			{
				String s = truncateExtentionName(f);
				if (s.length() > 0)
				{
					list.add(s);
				}
			}
		}
		return (String[]) (list.toArray(new String[list.size()]));
	}

	/**
	 * 截取文件的后后缀名
	 * 
	 * @param name
	 * @return
	 */
	private String truncateExtentionName(String name)
	{
		String temp = "";
		if (name == null || name.length() < 0)
		{
			return temp;
		}
		if (name.endsWith(MONITOR_FILE_SUFFIX))
		{
			temp = name.substring(0, name.lastIndexOf(MONITOR_FILE_SUFFIX));
		}
		return temp;
	}
}
