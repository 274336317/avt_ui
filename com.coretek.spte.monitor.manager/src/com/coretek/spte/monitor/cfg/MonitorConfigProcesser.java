/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
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
	 * ���������õ�ָ��·����
	 * 
	 * @param path ָ���ļ�����ñ���·���ļ�
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
	 * ���ؼ�������ļ�
	 * 
	 * @param path ��������ļ�·��
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
			boolean contained = false; // ��ʾ�Ӽ�������ļ��ж�ȡ��ICD�ļ��Ƿ��Ѿ�������ICD�ļ�ѡ��Ի����У�
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
			{ // �����������Ҫ������ӵ�ICD�ļ�ѡ��Ի�����
				this.dlg.getICDFilesCombo().add(this.dlg.getICDPath());
				this.dlg.getICDFilesCombo().select(this.dlg.getICDFilesCombo().getItemCount() - 1);
			}
			Object[] objs = (Object[]) ois.readObject();
			this.dlg.getViewer().setCheckedElements(objs);
			this.dlg.getTopicViewer().setCheckedElements((Object[]) ois.readObject());
		} catch (Exception e)
		{
			e.printStackTrace();
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "����", "�����ļ���ȡʧ��!");
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
	 * ��ȡĬ�������ļ��µ����м�����ã������ļ���׺����ȡ�����ڳ����������ļ���ѡ��
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
	 * ��ȡ�ļ��ĺ��׺��
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
