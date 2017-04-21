package com.coretek.testcase.testcaseview.actions.left;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;

import com.coretek.common.i18n.messages.Messages;
import com.coretek.common.utils.EclipseUtils;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.core.util.Utils;
import com.coretek.testcase.testcaseview.TestCase;
import com.coretek.testcase.testcaseview.TestCaseGroup;
import com.coretek.testcase.testcaseview.TestCaseViewPart;
import com.coretek.testcase.testcaseview.actions.AbstractAction;
import com.coretek.testcase.testcaseview.dialogs.ConfirmDialog;

public class SaveGroupAction extends AbstractAction
{
	private boolean	isOverride;

	private boolean	isAlwaysDoSo;

	public SaveGroupAction(TestCaseViewPart viewPart)
	{
		super(viewPart);
		this.setText(Messages.getString("I18N_SAVE_AS"));
	}

	@Override
	public void run()
	{
		DirectoryDialog dialog = new DirectoryDialog(Utils.getShell(), SWT.NONE);
		String savePath = dialog.open();
		if (StringUtils.isNull(savePath))
		{
			return;
		}

		CheckboxTreeViewer treeViewer = this.getViewPart().getTreeViewer();
		Object[] objs = treeViewer.getCheckedElements();
		for (Object obj : objs)
		{
			TestCaseGroup group = (TestCaseGroup) obj;
			if (group.getChildGroups() != null && group.getChildGroups().size() > 0)
			{
				continue;
			}
			File desFolder = new File(StringUtils.concat(savePath, File.separator, group.getName()));
			if (desFolder.exists())
			{// 文件夹已存在，提示用户是否覆盖
				if (!this.isAlwaysDoSo)
				{
					ConfirmDialog cd = new ConfirmDialog(Utils.getShell(), StringUtils.concat("文件夹 ", desFolder.getName(), " 已存在, 是否需要要覆盖此文件夹 ？"));
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
			if (!this.isOverride)
			{// 不覆盖
				continue;
			}
			// 覆盖之前，先删除目标文件夹下的所有文件和文件夹
			this.deleteFolder(desFolder);
			desFolder.mkdir();

			Set<TestCase> testCases = group.getTestCases();
			if (testCases == null || testCases.size() == 0)
			{
				continue;
			}
			for (TestCase testCase : testCases)
			{
				IProject project = EclipseUtils.getProject(testCase.getProjectName());
				if (project.exists())
				{
					IFile sourceFile = Utils.getCaseByName(testCase.getProjectName(), testCase.getSuitePath(), testCase.getCaseName());// folder.getFile(testCase.getCaseName());
					if (sourceFile.exists())
					{
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
			}
		}
		this.isAlwaysDoSo = false;
		this.isOverride = false;
	}

	public void deleteFolder(File folder)
	{
		File[] files = folder.listFiles();
		if (files == null || files.length == 0)
		{
			return;
		}
		for (File file : files)
		{
			if (file.isDirectory())
			{
				this.deleteFolder(file);
				file.delete();
			} else
			{
				file.delete();
			}
		}
	}
}
