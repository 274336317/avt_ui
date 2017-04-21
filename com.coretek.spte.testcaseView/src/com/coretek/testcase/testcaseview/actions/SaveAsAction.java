package com.coretek.testcase.testcaseview.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.utils.EclipseUtils;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.core.util.Utils;
import com.coretek.testcase.testcaseview.TestCase;
import com.coretek.testcase.testcaseview.TestCaseViewPart;
import com.coretek.testcase.testcaseview.dialogs.ConfirmDialog;

/**
 * 
 * 导出测试用例
 * 
 * @author 孙大巍
 * @date 2010-12-15
 */

public class SaveAsAction extends AbstractAction
{

	private boolean	isOverride;

	private boolean	isAlwaysDoSo;

	public SaveAsAction(TestCaseViewPart viewPart)
	{
		super(viewPart);
		this.setText(Messages.getString("I18N_SAVE_AS"));
	}

	@Override
	public void run()
	{
		super.run();

		DirectoryDialog dialog = new DirectoryDialog(Utils.getShell(), SWT.NONE);
		String savePath = dialog.open();
		if (StringUtils.isNull(savePath))
		{
			return;
		}

		Object[] objs = this.viewPart.getTableViewer().getCheckedElements();

		for (Object obj : objs)
		{
			TestCase tc = (TestCase) obj;
			IProject project = EclipseUtils.getProject(tc.getProjectName());
			if (project.exists())
			{
				IFile sourceFile = Utils.getCaseByName(tc.getProjectName(), tc.getSuitePath(), tc.getCaseName());// folder.getFile(tc.getCaseName());
				if (sourceFile.exists())
				{
					File desFolder = new File(savePath);
					if (desFolder.exists() && desFolder.isDirectory())
					{
						File[] files = desFolder.listFiles();
						for (File f : files)
						{
							if (f.getName().equals(sourceFile.getName()))
							{
								if (!this.isAlwaysDoSo)
								{
									ConfirmDialog cd = new ConfirmDialog(Utils.getShell(), StringUtils.concat("文件 ", f.getName(), " 已存在, 是否需要要覆盖此文件 ？"));
									cd.open();
									this.isAlwaysDoSo = cd.isAlwaysDoSo();
									this.isOverride = cd.isOverride();
								} else
								{
									this.isOverride = true;
								}
							} else
							{
								this.isOverride = true;
							}
						}
						if (!this.isOverride)
						{// 不覆盖
							continue;
						}
						try
						{
							BufferedReader reader = new BufferedReader(new FileReader(sourceFile.getLocation().toFile()));
							File desFile = new File(StringUtils.concat(desFolder, File.separator, sourceFile.getName()));
							if (desFile.createNewFile())
							{
								PrintWriter writer = new PrintWriter(desFile);
								String str = null;
								while ((str = reader.readLine()) != null)
								{
									writer.println(str);
								}
								writer.flush();
								writer.close();
								reader.close();
							}
						} catch (FileNotFoundException e)
						{
							e.printStackTrace();
						} catch (IOException e)
						{
							e.printStackTrace();
						}
					}
				}
				// }
			}
		}
		this.isAlwaysDoSo = false;
		this.isOverride = false;
	}
}
