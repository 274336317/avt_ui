package com.coretek.common.template.test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import com.coretek.common.template.ICDFunctionDomain;
import com.coretek.common.template.TemplateEngine;
import com.coretek.common.template.TemplateEnginePlugin;
import com.coretek.common.template.TemplateUtils;

public class TestViewActionDelegate implements IViewActionDelegate
{

	private IViewPart	view;

	@Override
	public void init(IViewPart view)
	{
		this.view = view;

	}

	@Override
	public void run(IAction action)
	{

		FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
		dialog.open();
		String path = dialog.getFilterPath();
		path = path + "\\" + dialog.getFileName();
		File targetFile = new File(path);
		//		
		List<ICDFunctionDomain> domains = TemplateUtils.getAllFunctionDomains(TemplateEngine.getEngine().parseICD(targetFile));

		System.out.println(domains);
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection)
	{

	}

}
