/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.utils.widget;

import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.coretek.common.utils.plugin.UtilsPlugin;

/**
 * 具有动画效果的浮动小窗体
 * 
 * @author 孙大巍 2012-4-5
 */
public class FloatWindow extends Window
{
	private Image				imgMouseOut	= UtilsPlugin.getImageDescriptor("icons/close_mouse_out.png").createImage();

	private Image				imgMouseOn	= UtilsPlugin.getImageDescriptor("icons/close_mouse_on.png").createImage();

	private IWorkbenchWindow	ww			= PlatformUI.getWorkbench().getWorkbenchWindows()[0];

	private Color				color		= new Color(null, 255, 255, 255);

	private ControlListener		controlListener;

	private Timer				timer;

	private Timer				descreaseTimer;

	protected FloatWindow(Shell parentShell)
	{
		super(parentShell);
	}

	@Override
	public void setParentShell(Shell newParentShell)
	{

		super.setParentShell(newParentShell);
	}

	@Override
	public void setShellStyle(int newShellStyle)
	{

		super.setShellStyle(newShellStyle);
	}

	@Override
	public boolean close()
	{
		this.imgMouseOn.dispose();
		this.imgMouseOut.dispose();
		this.color.dispose();
		this.ww.getShell().removeControlListener(this.controlListener);
		return super.close();
	}

	@Override
	protected Control createContents(Composite parent)
	{
		parent.setBackground(this.color);
		Composite menuBarPanel = new Composite(parent, SWT.NONE);
		menuBarPanel.setBackground(this.color);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.verticalSpacing = 0;
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 18;
		menuBarPanel.setLayoutData(gridData);
		menuBarPanel.setLayout(layout);

		Label label = new Label(menuBarPanel, SWT.RIGHT);
		label.setBackground(this.color);
		label.setText("");
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 13;
		label.setLayoutData(gridData);

		label = new Label(menuBarPanel, SWT.RIGHT);

		label.setImage(this.imgMouseOut);
		gridData = new GridData(GridData.FILL_VERTICAL);
		gridData.widthHint = 13;
		label.setLayoutData(gridData);
		label.addMouseTrackListener(new MouseTrackListener()
		{
			@Override
			public void mouseEnter(MouseEvent e)
			{
				Label label = (Label) e.getSource();
				label.setImage(imgMouseOn);
			}

			@Override
			public void mouseExit(MouseEvent e)
			{
				Label label = (Label) e.getSource();
				label.setImage(imgMouseOut);

			}

			@Override
			public void mouseHover(MouseEvent e)
			{
				Label label = (Label) e.getSource();
				label.setImage(imgMouseOn);

			}

		});
		label.addMouseListener(new MouseListener()
		{

			@Override
			public void mouseDoubleClick(MouseEvent e)
			{

			}

			@Override
			public void mouseDown(MouseEvent e)
			{
				descreaseTimer = new Timer();
				descreaseTimer.schedule(new DecreaseJob(FloatWindow.this.getShell()), 100, 40);

			}

			@Override
			public void mouseUp(MouseEvent e)
			{

			}

		});

		Composite mainPanel = new Composite(parent, SWT.NONE);
		mainPanel.setBackground(this.color);
		gridData = new GridData(GridData.FILL_BOTH);
		mainPanel.setLayoutData(gridData);
		timer = new Timer();
		timer.schedule(new IncreaseJob(this.getShell()), 100, 40);
		return mainPanel;
	}

	/**
	 * 渐出任务
	 * 
	 * @author 孙大巍 2012-4-5
	 */
	private class DecreaseJob extends TimerTask
	{
		private Shell	shell;

		public DecreaseJob(Shell shell)
		{
			this.shell = shell;
		}

		@Override
		public void run()
		{
			Display.getDefault().syncExec(new Runnable()
			{

				@Override
				public void run()
				{
					Rectangle rect = shell.getBounds();
					if (rect.height <= 10)
					{
						cancel();
						descreaseTimer.cancel();
						FloatWindow.this.close();
						return;
					}
					rect.height = rect.height - 10;

					rect.y = rect.y + 10;
					shell.setBounds(rect);
					shell.redraw();
				}

			});
		}

	}

	/**
	 * 渐进任务
	 * 
	 * @author 孙大巍 2012-4-5
	 */
	private class IncreaseJob extends TimerTask
	{

		private Shell	shell;

		public IncreaseJob(Shell shell)
		{
			this.shell = shell;
		}

		@Override
		public void run()
		{

			Display.getDefault().syncExec(new Runnable()
			{

				@Override
				public void run()
				{
					Rectangle rect = shell.getBounds();
					if (rect.height >= 120)
					{
						cancel();
						timer.cancel();
						return;
					}
					rect.height = rect.height + 10;
					rect.y = rect.y - 10;
					shell.setBounds(rect);
					shell.redraw();

				}

			});
		}

	}

	@Override
	protected void initializeBounds()
	{
		super.initializeBounds();
		Shell shell = this.getShell();
		Rectangle rect = shell.getBounds();
		rect.width = 150;
		rect.height = 10;
		rect.x = ww.getShell().getBounds().x + ww.getShell().getBounds().width - 155;
		rect.y = ww.getShell().getBounds().y + ww.getShell().getBounds().height - 10;
		shell.setBounds(rect);
		this.controlListener = new MyControlListener(this.getShell(), ww);
		ww.getShell().addControlListener(this.controlListener);
	}

	private static class MyControlListener implements ControlListener
	{

		private Shell				shell;

		private IWorkbenchWindow	window;

		public MyControlListener(Shell shell, IWorkbenchWindow window)
		{
			this.shell = shell;
			this.window = window;
		}

		@Override
		public void controlMoved(ControlEvent e)
		{
			this.updateBounds();
		}

		@Override
		public void controlResized(ControlEvent e)
		{
			this.updateBounds();
		}

		private void updateBounds()
		{
			Rectangle rect = shell.getBounds();
			rect.x = window.getShell().getBounds().width + window.getShell().getBounds().x - 155;
			rect.y = window.getShell().getBounds().height + window.getShell().getBounds().y - 125;
			shell.setBounds(rect);
			shell.redraw();
		}
	}
}