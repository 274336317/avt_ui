/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.figures;

import java.util.List;

import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;

import com.coretek.common.template.ClazzManager;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.common.utils.StringUtils;
import com.coretek.spte.cfg.ErrorTypesEnum;
import com.coretek.spte.core.locators.MidpointOffsetLocator;
import com.coretek.spte.dataCompare.CompareResult;
import com.coretek.spte.dataCompare.Result;
import com.coretek.spte.monitor.ResultDialogHandler;
import com.coretek.spte.testcase.Message;
import com.coretek.spte.testcase.Period;

/**
 * 消息连线
 * 
 * @author 孙大巍
 * @date 2012-1-5
 */
public class ConnFgr extends PolylineConnection
{
	private Color			foregroundColor;

	private Color			arrowColor;

	private Result			result;

	private ClazzManager	icdManager;

	private ClazzManager	caseManager;

	@Override
	protected void outlineShape(Graphics g)
	{
		if (result.getHandlerType().size() > 0)
		{
			if (result.getHandlerType().get(0) != null)
			{
				this.foregroundColor = result.getHandlerType().get(0).getColor();
				this.arrowColor = result.getHandlerType().get(0).getColor();
			}

		} else if (result.getErrorTypes().indexOf(ErrorTypesEnum.WRONGVALUE) >= 0)
		{
			this.foregroundColor = ColorConstants.red;
			this.arrowColor = ColorConstants.red;
		}
		g.setForegroundColor(foregroundColor);
		g.setLineStyle(Graphics.LINE_SOLID);
		g.setLineWidth(1);
		super.outlineShape(g);
	}

	public ConnFgr(Color foregroundColor, Color arrowColor, Result result, ClazzManager caseManager, final List<Entity> testedObjects, ClazzManager icdManager, final CompareResult compareResult)
	{
		this.foregroundColor = foregroundColor;
		this.arrowColor = arrowColor;
		this.result = result;
		this.caseManager = caseManager;
		String name = null;
		this.icdManager = icdManager;
		if (result.getSpteMsg().getMsg().isPeriodMessage())
		{
			Period period = (Period) result.getSpteMsg().getMsg().getChildren().get(0);
			name = StringUtils.concat(result.getSpteMsg().getMsg().getName(), "  (", period.getValue(), "/", result.getSpteMsg().getMsg().getPeriodCount(), ")");
		} else
		{
			name = result.getSpteMsg().getMsg().getName();
		}
		this.add(new Label(name), new MidpointOffsetLocator(this, 0));
		this.setFigureAttr();
		this.addMouseListener(new MouseListener.Stub()
		{

			@Override
			public void mouseDoubleClicked(MouseEvent me)
			{
				Message msg = (Message) ConnFgr.this.caseManager.getByUUID(ConnFgr.this.result.getSpteMsg().getMsg().getUuid());
				SPTEMsg expectedMsg = null;
				if (msg.isPeriodMessage() && msg.isRecv())
				{
					expectedMsg = ConnFgr.this.result.getExpectedMsg();
				} else
				{
					expectedMsg = new SPTEMsg(msg, ConnFgr.this.result.getSpteMsg().getICDMsg());
				}
				ResultDialogHandler.open(Display.getCurrent().getActiveShell(), ConnFgr.this.result, expectedMsg, ConnFgr.this.icdManager, testedObjects, compareResult);
			}

		});

		this.addMouseMotionListener(new MouseMotionListener.Stub()
		{

			private Cursor	defaultCursor;

			@Override
			public void mouseExited(MouseEvent me)
			{
				ConnFgr fgr = (ConnFgr) me.getSource();
				if (fgr.getCursor() != null)
					fgr.getCursor().dispose();
				fgr.setCursor(defaultCursor);
			}

			@Override
			public void mouseHover(MouseEvent me)
			{
				ConnFgr fgr = (ConnFgr) me.getSource();
				defaultCursor = fgr.getCursor();
				fgr.setCursor(new Cursor(null, SWT.CURSOR_HAND));
			}

		});
	}

	protected void setFigureAttr()
	{
		PolygonDecoration poly = new PolygonDecoration();
		poly.setForegroundColor(arrowColor);
		poly.setScale(5, 5);
		this.setTargetDecoration(poly);
		this.setConnectionRouter(new BendpointConnectionRouter());
	}

	public Result getResult()
	{
		return result;
	}

	public void setResult(Result result)
	{
		this.result = result;
	}

}