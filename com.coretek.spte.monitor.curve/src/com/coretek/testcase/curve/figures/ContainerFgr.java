/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.testcase.curve.figures;

import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.Polyline;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.coretek.common.utils.StringUtils;
import com.coretek.spte.common.service.IPaintService;
import com.coretek.spte.monitor.SequenceViewPart;
import com.coretek.spte.monitor.manager.Event;
import com.coretek.spte.monitor.ui.MonitorPlugin;
import com.coretek.testcase.curve.internal.model.CurveConst;
import com.coretek.testcase.curve.internal.model.FieldElement;
import com.coretek.testcase.curve.internal.model.FieldElementSet;
import com.coretek.testcase.curve.views.CurveView;

/**
 * 画曲线视图内的线
 * 
 * @author 尹军 2012-3-14
 */

public class ContainerFgr extends Shape
{

	// 有效的绘图区域
	private Rectangle	paintRect;

	// 当前时间戳线
	private Polyline	pline	= null;

	// 曲线视图
	private CurveView	viewPart;

	/**
	 * @param viewPart </br>
	 */
	public ContainerFgr(CurveView viewPart)
	{
		this.viewPart = viewPart;

	}

	public boolean isSamePoint(int value, long time, FieldElementSet field, Point point)
	{

		if (value > field.getMaxValue())
		{
			value = field.getMaxValue();
		}

		if (value < field.getMinValue())
		{
			value = field.getMinValue();
		}
		// 将各个域的值时间戳转换成图形上的X轴坐标值
		// int realX = Double.valueOf((time - viewPart.getStartTimeStamp()) /
		// (Double.valueOf(viewPart.getPreferenceManager().getTimestampLengthEachPage())
		// / Double.valueOf(paintRect.width))).intValue();
		int realX = changeTimeStamp2XAxis(time);
		// 将各个域的值转换成图形上的Y轴坐标值
		// int realY = Double.valueOf(Double.valueOf(field.getMaxValue() -
		// value) / (Double.valueOf(field.getMaxValue() - field.getMinValue()) /
		// Double.valueOf(paintRect.height))).intValue();
		int realY = changeValue2YAxis(field, value);
		List<FieldElementSet> fields = viewPart.getManager().getAllFields();
		int length = fields.size();

		int x = 0;
		int y = 0;
		if (realY == 0)
		{
			y = CurveConst.Y_AXIS_V_TOP_OFFSET + realY;
		} else
		{
			y = CurveConst.Y_AXIS_V_TOP_OFFSET + realY - 1;
		}
		// 将各个域的值加上偏移值
		if (realX < 0)
		{
			x = (length + 1) / 2 * viewPart.getPreferenceManager().getColWidth();
		} else
		{
			x = (length + 1) / 2 * viewPart.getPreferenceManager().getColWidth() + realX;
		}

		if (point.x == x && point.y == y)
		{
			return true;
		}
		return false;
	}

	/**
	 * 向信号集合中添加信号
	 * 
	 * @param value 信号值
	 * @param time 信号时间戳
	 * @param field 信号集合</br>
	 */
	public void addPoint(int value, long time, FieldElementSet field)
	{

		if (value > field.getMaxValue())
		{
			value = field.getMaxValue();
		}

		if (value < field.getMinValue())
		{
			value = field.getMinValue();
		}
		// 将各个域的值时间戳转换成图形上的X轴坐标值
		int realX = changeTimeStamp2XAxis(time);
		// 将各个域的值转换成图形上的Y轴坐标值
		int realY = changeValue2YAxis(field, value);
		List<FieldElementSet> fields = viewPart.getManager().getAllFields();
		int length = fields.size();

		int x = 0;
		int y = 0;
		if (realY == 0)
		{
			y = CurveConst.Y_AXIS_V_TOP_OFFSET + realY;
		} else
		{
			y = CurveConst.Y_AXIS_V_TOP_OFFSET + realY - 1;
		}
		// 将各个域的值加上偏移值
		if (realX < 0)
		{
			x = (length + 1) / 2 * viewPart.getPreferenceManager().getColWidth();
		} else
		{
			x = (length + 1) / 2 * viewPart.getPreferenceManager().getColWidth() + realX;
		}

		PointFgr pointFgr = this.getLastPointFgr(field);
		Point startPoint = null;

		if (pointFgr != null)
		{
			startPoint = pointFgr.getLocation();
		}

		PointFgr pf = new PointFgr();
		Rectangle rect = new Rectangle(x, y, 1, 1);

		pf.setBounds(rect);
		pf.setField(field);
		pf.setValue(value);
		pf.setTime(time);

		this.add(pf);

		if (field.getLineType() == 0)
		{ // 方波连线
			if (startPoint != null)
			{
				Polyline polyline = new Polyline();
				polyline.setLineStyle(SWT.LINE_SOLID);
				polyline.setLineWidth(1);
				polyline.setForegroundColor(new Color(null, field.getColor()));

				if (startPoint.x < (length + 1) / 2 * viewPart.getPreferenceManager().getColWidth())
				{
					startPoint.x = (length + 1) / 2 * viewPart.getPreferenceManager().getColWidth();
				}
				startPoint = new Point(startPoint.x, startPoint.y);
				Point endPoint = new Point(x, startPoint.y);
				polyline.setStart(startPoint);
				polyline.setEnd(endPoint);

				FlowPage fp = new FlowPage();
				TextFlow tf = new TextFlow();

				tf.setText(StringUtils.concat("value=", pointFgr.getValue(), ";time=", pointFgr.getTime()));
				fp.add(tf);
				polyline.setToolTip(fp);

				polyline.addMouseListener(new MouseListener()
				{
					public void mouseDoubleClicked(org.eclipse.draw2d.MouseEvent me)
					{

					}

					public void mousePressed(final org.eclipse.draw2d.MouseEvent me)
					{
						Polyline polyline = (Polyline) me.getSource();
						long start = viewPart.getStartTimeStamp();
						long end = viewPart.getEndTimeStamp();
						List<FieldElementSet> fields = viewPart.getManager().getAllFields();
						int fieldsSize = fields.size();
						for (int k = 0; k < fieldsSize; k++)
						{
							List<FieldElement> elements = fields.get(k).getElementsToShow(start, end);
							int elementsSize = elements.size();
							for (int j = 0; j < elementsSize; j++)
							{
								if (isSamePoint(elements.get(j).getValue(), elements.get(j).getTime(), fields.get(k), polyline.getStart()))
								{
									viewPart.setSelectFieldElementSet(fields.get(k));
									// 获得线的两端的X轴的坐标值
									int realXStart = polyline.getStart().x - (fieldsSize + 1) / 2 * viewPart.getPreferenceManager().getColWidth();

									int realXEnd = polyline.getEnd().x - (fieldsSize + 1) / 2 * viewPart.getPreferenceManager().getColWidth();

									// 将X轴的坐标值转换成时间戳
									long startStamp = changXAxis2TimeStamp(realXStart);
									long endStamp = changXAxis2TimeStamp(realXEnd);

									// 通过事件的方式通知其他观察者当前选择事件
									viewPart.getObservable().notifyObservers(new Event(Event.EVENT_TIME_SELECTED, (int) startStamp, (int) endStamp));
									Display.getDefault().asyncExec(new Runnable()
									{

										public void run()
										{
											IPaintService paintService = MonitorPlugin.getDefault().getPaintService();
											long time = ContainerFgr.this.changXAxis2TimeStamp(me.x);
											PaintJob pj = new PaintJob(paintService, time);
											pj.setUser(true);
											pj.schedule();
										}

									});

									return;
								}
							}
						}
						viewPart.setSelectFieldElementSet(null);
					}

					public void mouseReleased(org.eclipse.draw2d.MouseEvent me)
					{

					}
				});

				polyline.addMouseMotionListener(new PointMouseMotionListener());
				this.add(polyline);

				polyline = new Polyline();
				polyline.setLineStyle(SWT.LINE_SOLID);
				polyline.setLineWidth(1);
				polyline.setForegroundColor(new Color(null, field.getColor()));

				startPoint = new Point(x, startPoint.y);
				endPoint = new Point(x, y);
				polyline.setStart(startPoint);
				polyline.setEnd(endPoint);

				polyline.addMouseListener(new MouseListener()
				{
					public void mouseDoubleClicked(org.eclipse.draw2d.MouseEvent me)
					{
						Polyline polyline = (Polyline) me.getSource();
						List<FieldElementSet> fields = viewPart.getManager().getAllFields();
						int length = fields.size();

						// 获得线的两端的X轴的坐标值
						int realXStart = polyline.getStart().x - (length + 1) / 2 * viewPart.getPreferenceManager().getColWidth();

						int realXEnd = polyline.getEnd().x - (length + 1) / 2 * viewPart.getPreferenceManager().getColWidth();

						// 将X轴的坐标值转换成时间戳
						long startStamp = changXAxis2TimeStamp(realXStart);
						long endStamp = changXAxis2TimeStamp(realXEnd);

						viewPart.getObservable().notifyObservers(new Event(Event.EVENT_TIME_SELECTED, (int) startStamp, (int) endStamp));
					}

					public void mousePressed(org.eclipse.draw2d.MouseEvent me)
					{
						Polyline polyline = (Polyline) me.getSource();
						long start = viewPart.getStartTimeStamp();
						long end = viewPart.getEndTimeStamp();
						List<FieldElementSet> fields = viewPart.getManager().getAllFields();
						int fieldsSize = fields.size();
						for (int k = 0; k < fieldsSize; k++)
						{
							List<FieldElement> elements = fields.get(k).getElementsToShow(start, end);
							int elementsSize = elements.size();
							for (int j = 0; j < elementsSize; j++)
							{
								if (isSamePoint(elements.get(j).getValue(), elements.get(j).getTime(), fields.get(k), polyline.getEnd()))
								{
									viewPart.setSelectFieldElementSet(fields.get(k));
									return;
								}
							}
						}
						viewPart.setSelectFieldElementSet(null);
					}

					public void mouseReleased(org.eclipse.draw2d.MouseEvent me)
					{

					}
				});

				polyline.addMouseMotionListener(new PointMouseMotionListener());

				this.add(polyline);
			}
		} else
		{ // 直连线
			if (startPoint != null)
			{
				Polyline polyline = new Polyline();
				polyline.setLineStyle(SWT.LINE_SOLID);
				polyline.setLineWidth(1);
				polyline.setForegroundColor(new Color(null, field.getColor()));

				startPoint = new Point(startPoint.x, startPoint.y);
				Point endPoint = new Point(x, y);
				if (paintRect != null && paintRect.contains(startPoint) && paintRect.contains(endPoint))
				{
					polyline.setStart(startPoint);
					polyline.setEnd(endPoint);
				} else
				{
					return;
				}

				FlowPage fp = new FlowPage();
				TextFlow tf = new TextFlow();
				tf.setText("value=" + pointFgr.getValue() + ";time=" + pointFgr.getTime());
				fp.add(tf);
				polyline.setToolTip(fp);

				this.add(polyline);
			}
		}
	}

	/**
	 * 清屏 </br>
	 */
	public void clearSreen()
	{
		this.removeAll();
		viewPart.getCanvas().getDisplay().syncExec(new Runnable()
		{
			public void run()
			{
				viewPart.getTableViewer().setInput(null);
				viewPart.getTableTreeViewer().setInput(null);
				viewPart.getTableTreeViewer().refresh();
			}
		});
		super.repaint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Shape#fillShape(org.eclipse.draw2d.Graphics)
	 * <br/>
	 */
	@Override
	protected void fillShape(Graphics graphics)
	{

	}

	/**
	 * 获得信号集合中的最后一个点图形对象
	 * 
	 * @param field 信号集合
	 * @return 最后一个点图形对象 </br>
	 */
	@SuppressWarnings("unchecked")
	public PointFgr getLastPointFgr(FieldElementSet field)
	{
		PointFgr p = null;

		List<IFigure> children = (List<IFigure>) this.getChildren();
		for (int i = children.size() - 1; i >= 0; i--)
		{
			IFigure figure = (IFigure) children.get(i);
			if (figure instanceof PointFgr)
			{
				PointFgr fgr = (PointFgr) figure;
				if (fgr.getField() == field)
				{
					p = fgr;
					break;
				}
			}
		}
		return p;
	}

	/**
	 * 判断是否有点图形对象
	 * 
	 * @return 是否有点图形对象 </br>
	 */
	public boolean hasPointFgr()
	{
		boolean result = false;
		for (Object obj : this.getChildren())
		{
			if (obj instanceof PointFgr)
			{
				result = true;
				break;
			}
		}

		return result;
	}

	/*
	 * (non-Javadoc) 画图形对象的轮廓线
	 * 
	 * @see org.eclipse.draw2d.Shape#outlineShape(org.eclipse.draw2d.Graphics)
	 * <br/>
	 */
	@Override
	protected void outlineShape(Graphics graphics)
	{
		graphics.setForegroundColor(ColorConstants.red);

		org.eclipse.swt.graphics.Rectangle r = viewPart.getCanvas().getBounds();

		List<FieldElementSet> fields = viewPart.getManager().getAllFields();
		int length = fields.size();

		int x = r.x;
		int y = r.y + CurveConst.Y_AXIS_V_TOP_OFFSET;
		graphics.setForegroundColor(ColorConstants.red);

		// 画前X轴
		graphics.drawLine(x + (length + 1) / 2 * viewPart.getPreferenceManager().getColWidth(), y, x + r.width - length / 2 * viewPart.getPreferenceManager().getColWidth() - 10, y);

		// 画后X轴
		graphics.drawLine(x + (length + 1) / 2 * viewPart.getPreferenceManager().getColWidth(), y + r.height - (CurveConst.Y_AXIS_V_TOP_OFFSET + CurveConst.Y_AXIS_V_BOTTOM_OFFSET), x + r.width - length / 2 * viewPart.getPreferenceManager().getColWidth() - 10, y + r.height - (CurveConst.Y_AXIS_V_TOP_OFFSET + CurveConst.Y_AXIS_V_BOTTOM_OFFSET));

		// 画前Y轴
		graphics.drawLine(x + (length + 1) / 2 * viewPart.getPreferenceManager().getColWidth(), y, x + (length + 1) / 2 * viewPart.getPreferenceManager().getColWidth(), y + r.height - (CurveConst.Y_AXIS_V_TOP_OFFSET + CurveConst.Y_AXIS_V_BOTTOM_OFFSET));

		// 画后Y轴
		graphics.drawLine(x + r.width - length / 2 * viewPart.getPreferenceManager().getColWidth() - 10, y, x + r.width - length / 2 * viewPart.getPreferenceManager().getColWidth() - 10, y + r.height - (CurveConst.Y_AXIS_V_TOP_OFFSET + CurveConst.Y_AXIS_V_BOTTOM_OFFSET));

		// 设置可画图区域
		paintRect = new Rectangle(x + (length + 1) / 2 * viewPart.getPreferenceManager().getColWidth(), y, r.width - (length + 1) / 2 * viewPart.getPreferenceManager().getColWidth() - (length) / 2 * viewPart.getPreferenceManager().getColWidth() - 10, r.height - (CurveConst.Y_AXIS_V_TOP_OFFSET + CurveConst.Y_AXIS_V_BOTTOM_OFFSET));

		// 获得X轴的像素粒度
		double p = (r.width - (length + 1) / 2 * viewPart.getPreferenceManager().getColWidth() - length / 2 * viewPart.getPreferenceManager().getColWidth() - 10) / (double) viewPart.getPreferenceManager().getxIntervalNum();

		// 获得X轴的时间粒度
		double time = viewPart.getPreferenceManager().getTimestampLengthEachPage() / (double) CurveConst.getInstance().getScale() / (double) viewPart.getPreferenceManager().getxIntervalNum();

		// 画X轴的时间刻度、时间标签
		int previousPosition = 0;
		int width = graphics.getFontMetrics().getAverageCharWidth();
		for (int i = 0; i <= viewPart.getPreferenceManager().getxIntervalNum(); i++)
		{
			double position = i * p + r.x + (length + 1) / 2 * viewPart.getPreferenceManager().getColWidth();

			// 画X轴的时间刻度
			graphics.drawLine((int) Math.round(position), r.height - (CurveConst.Y_AXIS_V_TOP_OFFSET + CurveConst.Y_AXIS_V_BOTTOM_OFFSET) + y - 5, (int) Math.round(position), r.height - (CurveConst.Y_AXIS_V_TOP_OFFSET + CurveConst.Y_AXIS_V_BOTTOM_OFFSET) + y);

			double result = (int) Math.round(time * i + viewPart.getStartTimeStamp());
			if (result == (double) (int) result)
			{
				int stringLen = String.valueOf((int) Math.round(result)).length();
				int StringWidth = stringLen * width;
				if (i == 0)
				{
					graphics.drawLine((int) Math.round(position), r.height - (CurveConst.Y_AXIS_V_TOP_OFFSET + CurveConst.Y_AXIS_V_BOTTOM_OFFSET) + y - 10, (int) Math.round(position), r.height - (CurveConst.Y_AXIS_V_TOP_OFFSET + CurveConst.Y_AXIS_V_BOTTOM_OFFSET) + y);
					// 画X轴的时间标签
					graphics.drawString(String.valueOf((int) Math.round(result)), (int) Math.round(position), y + r.height - (CurveConst.Y_AXIS_V_TOP_OFFSET + CurveConst.Y_AXIS_V_BOTTOM_OFFSET) + 10);
					previousPosition = (int) Math.round(position);
				} else
				{
					if ((int) Math.round(position - previousPosition - StringWidth) > 15)
					{
						graphics.drawLine((int) Math.round(position), r.height - (CurveConst.Y_AXIS_V_TOP_OFFSET + CurveConst.Y_AXIS_V_BOTTOM_OFFSET) + y - 10, (int) Math.round(position), r.height - (CurveConst.Y_AXIS_V_TOP_OFFSET + CurveConst.Y_AXIS_V_BOTTOM_OFFSET) + y);
						// 画X轴的时间标签
						graphics.drawString(String.valueOf((int) Math.round(result)), (int) Math.round(position), y + r.height - (CurveConst.Y_AXIS_V_TOP_OFFSET + CurveConst.Y_AXIS_V_BOTTOM_OFFSET) + 10);
						previousPosition = (int) Math.round(position);
					}
				}
			} else
			{
				int stringLen = String.valueOf((int) Math.round(time * i + p * Double.valueOf(viewPart.getPreferenceManager().getTimestampLengthEachPage()) / Double.valueOf(paintRect.width))).length();
				int StringWidth = stringLen * width;
				if (i == 0)
				{
					graphics.drawLine((int) Math.round(position), r.height - (CurveConst.Y_AXIS_V_TOP_OFFSET + CurveConst.Y_AXIS_V_BOTTOM_OFFSET) + y - 10, (int) Math.round(position), r.height - (CurveConst.Y_AXIS_V_TOP_OFFSET + CurveConst.Y_AXIS_V_BOTTOM_OFFSET) + y);
					// 画X轴的时间标签
					graphics.drawString(String.valueOf((int) Math.round(result)), (int) Math.round(position), y + r.height - (CurveConst.Y_AXIS_V_TOP_OFFSET + CurveConst.Y_AXIS_V_BOTTOM_OFFSET) + 10);
					previousPosition = (int) Math.round(position);
				} else
				{
					if ((int) Math.round(position - previousPosition - StringWidth) > 15)
					{
						graphics.drawLine((int) Math.round(position), r.height - (CurveConst.Y_AXIS_V_TOP_OFFSET + CurveConst.Y_AXIS_V_BOTTOM_OFFSET) + y - 10, (int) Math.round(position), r.height - (CurveConst.Y_AXIS_V_TOP_OFFSET + CurveConst.Y_AXIS_V_BOTTOM_OFFSET) + y);
						// 画X轴的时间标签
						graphics.drawString(String.valueOf((int) Math.round(time * i + p * Double.valueOf(viewPart.getPreferenceManager().getTimestampLengthEachPage()) / Double.valueOf(paintRect.width))), (int) Math.round(position - 10), y + r.height - (CurveConst.Y_AXIS_V_TOP_OFFSET + CurveConst.Y_AXIS_V_BOTTOM_OFFSET) + 10);
						previousPosition = (int) Math.round(position);
					}
				}
			}
		}
		graphics.setForegroundColor(ColorConstants.red);

		// 画Y轴的刻度线
		p = (r.height - (CurveConst.Y_AXIS_V_TOP_OFFSET + CurveConst.Y_AXIS_V_BOTTOM_OFFSET)) / (double) viewPart.getPreferenceManager().getyIntervalNum();
		double position;
		for (int i = 0; i < viewPart.getPreferenceManager().getyIntervalNum() - 1; i++)
		{
			position = r.height - (CurveConst.Y_AXIS_V_TOP_OFFSET + CurveConst.Y_AXIS_V_BOTTOM_OFFSET) - (p + p * i);

			// 画Y轴的前刻度线
			graphics.drawLine((length + 1) / 2 * viewPart.getPreferenceManager().getColWidth(), (int) Math.round(position + CurveConst.Y_AXIS_V_TOP_OFFSET), (length + 1) / 2 * viewPart.getPreferenceManager().getColWidth() + 5, (int) Math.round(position + CurveConst.Y_AXIS_V_TOP_OFFSET));

			// 画Y轴的后刻度线
			graphics.drawLine(r.width - length / 2 * viewPart.getPreferenceManager().getColWidth() - 10 - 5, (int) Math.round(position + CurveConst.Y_AXIS_V_TOP_OFFSET), r.width - length / 2 * viewPart.getPreferenceManager().getColWidth() - 10, (int) Math.round(position + CurveConst.Y_AXIS_V_TOP_OFFSET));
		}

		// 画左边Y轴的值标签
		for (int j = 0; j < (length + 1) / 2; j++)
		{
			graphics.setForegroundColor(new Color(null, fields.get(2 * j).getColor()));
			for (int i = 0; i < 11; i++)
			{
				position = (r.height - (CurveConst.Y_AXIS_V_TOP_OFFSET + CurveConst.Y_AXIS_V_BOTTOM_OFFSET)) - p * i;
				if (i == 0)
				{
					graphics.drawString(String.valueOf(fields.get(2 * j).getMinValue()), x + (length + 1) / 2 * viewPart.getPreferenceManager().getColWidth() - (2 * j + 2) / 2 * viewPart.getPreferenceManager().getColWidth() + 5, (int) Math.round(position + 5));
				} else if (i == 10)
				{
					graphics.drawString(String.valueOf(fields.get(2 * j).getMaxValue()), x + (length + 1) / 2 * viewPart.getPreferenceManager().getColWidth() - (2 * j + 2) / 2 * viewPart.getPreferenceManager().getColWidth() + 5, (int) Math.round(position + 5));
				} else
				{
					graphics.drawString(String.valueOf((int) Math.round(fields.get(2 * j).getMaxValue() - position * (fields.get(2 * j).getMaxValue() - fields.get(2 * j).getMinValue()) / (r.height - (CurveConst.Y_AXIS_V_TOP_OFFSET + CurveConst.Y_AXIS_V_BOTTOM_OFFSET)))), x + (length + 1) / 2 * viewPart.getPreferenceManager().getColWidth() - (2 * j + 2) / 2 * viewPart.getPreferenceManager().getColWidth() + 5, (int) Math.round(position + 5));
				}
			}
		}

		// 画右边Y轴的值标签
		for (int j = 0; j < length / 2; j++)
		{
			graphics.setForegroundColor(new Color(null, fields.get(2 * j + 1).getColor()));
			for (int i = 0; i < 11; i++)
			{
				position = (r.height - (CurveConst.Y_AXIS_V_TOP_OFFSET + CurveConst.Y_AXIS_V_BOTTOM_OFFSET)) - p * i;
				if (i == 0)
				{
					graphics.drawString(String.valueOf(fields.get(2 * j + 1).getMinValue()), x + r.width - length / 2 * viewPart.getPreferenceManager().getColWidth() - 10 + (2 * j + 1) / 2 * viewPart.getPreferenceManager().getColWidth() + 5, (int) Math.round(position + 5));
				} else if (i == 10)
				{
					graphics.drawString(String.valueOf(fields.get(2 * j + 1).getMaxValue()), x + r.width - length / 2 * viewPart.getPreferenceManager().getColWidth() - 10 + (2 * j + 1) / 2 * viewPart.getPreferenceManager().getColWidth() + 5, (int) Math.round(position + 5));
				} else
				{
					graphics.drawString(String.valueOf((int) Math.round(fields.get(2 * j + 1).getMaxValue() - position * (fields.get(2 * j + 1).getMaxValue() - fields.get(2 * j + 1).getMinValue()) / (r.height - (CurveConst.Y_AXIS_V_TOP_OFFSET + CurveConst.Y_AXIS_V_BOTTOM_OFFSET)))), x + r.width - length / 2 * viewPart.getPreferenceManager().getColWidth() - 10 + (2 * j + 1) / 2 * viewPart.getPreferenceManager().getColWidth() + 5, (int) Math.round(position + 5));
				}
			}
		}
	}

	public void repaintScale()
	{
		long start = viewPart.getStartTimeStamp();
		long end = viewPart.getEndTimeStamp();
		List<FieldElementSet> fields = viewPart.getManager().getAllFields();
		int fieldsSize = fields.size();
		this.removeAll();

		// 按照时间戳大小的顺序添加对列表的队尾，只添加比队尾时间戳大的信号
		for (int k = 0; k < fieldsSize; k++)
		{
			long current = start;
			List<FieldElement> elements = fields.get(k).getElementsToShow(start, end);

			int elementsSize = elements.size();
			for (int j = 0; j < elementsSize; j++)
			{
				if (elements.get(j).getTime() > current)
				{ // 只添加比队尾时间戳大的信号
					if (j == 0)
					{
						addPoint(elements.get(j).getValue(), start, fields.get(k));
					}
					addPoint(elements.get(j).getValue(), elements.get(j).getTime(), fields.get(k));
					current = elements.get(j).getTime(); // 更新列表队尾的时间戳
				}
				if (j == elementsSize - 1)
				{
					if (elements.get(j).getTime() < end)
					{
						addPoint(elements.get(j).getValue(), end, fields.get(k));
					}
				}
			}
		}

		if (!viewPart.isTerminated())
		{

			// 画曲线的当前值轴
			org.eclipse.swt.graphics.Rectangle r = viewPart.getCanvas().getBounds();
			int x = r.x + (fieldsSize + 1) / 2 * viewPart.getPreferenceManager().getColWidth() + Double.valueOf((viewPart.getEndTimeStamp() - viewPart.getStartTimeStamp()) / (Double.valueOf(viewPart.getPreferenceManager().getTimestampLengthEachPage()) / Double.valueOf(paintRect.width))).intValue();
			int y = r.y + CurveConst.Y_AXIS_V_TOP_OFFSET;
			pline = new Polyline();
			pline.setLineStyle(SWT.LINE_SOLID);
			pline.setLineWidth(2); // 修改值的宽度
			pline.setForegroundColor(ColorConstants.lightBlue);

			Point startPoint = new Point(x, y);
			Point endPoint = new Point(x, y + r.height - (CurveConst.Y_AXIS_V_TOP_OFFSET + CurveConst.Y_AXIS_V_BOTTOM_OFFSET));

			pline.setStart(startPoint);
			pline.setEnd(endPoint);

			this.add(pline);
		} else
		{
			// 画曲线的当前值轴
			org.eclipse.swt.graphics.Rectangle r = viewPart.getCanvas().getBounds();
			int x = 0;
			if (paintRect != null)
			{
				x = r.x + (fieldsSize + 1) / 2 * viewPart.getPreferenceManager().getColWidth() + Double.valueOf((viewPart.getCurrentTimeStamp() - viewPart.getStartTimeStamp()) / (Double.valueOf(viewPart.getPreferenceManager().getTimestampLengthEachPage()) / Double.valueOf(paintRect.width))).intValue();
			}
			int y = r.y + CurveConst.Y_AXIS_V_TOP_OFFSET;

			pline = new Polyline();
			pline.setLineStyle(SWT.LINE_SOLID);
			pline.setLineWidth(2); // 修改值的宽度
			pline.setForegroundColor(ColorConstants.lightBlue);

			Point startPoint = new Point(x, y);
			Point endPoint = new Point(x, y + r.height - (CurveConst.Y_AXIS_V_TOP_OFFSET + CurveConst.Y_AXIS_V_BOTTOM_OFFSET));

			pline.setStart(startPoint);
			pline.setEnd(endPoint);

			this.add(pline);
		}

		if (fields != null && fields.size() > 0)
		{
			viewPart.getTableViewer().setInput(fields);
			viewPart.getTableViewer().refresh();

			viewPart.getTableTreeViewer().setInput(fields);
			viewPart.getTableTreeViewer().expandAll();
			viewPart.getTableTreeViewer().refresh();
		}

		super.repaint();
	}

	/**
	 * 将X的坐标值转化为实际的时间戳值
	 * 
	 * @param x
	 * @return
	 */
	private long changXAxis2TimeStamp(int x)
	{
		long startTimeStamp = viewPart.getStartTimeStamp();
		double cache = Double.valueOf(viewPart.getPreferenceManager().getTimestampLengthEachPage()) / Double.valueOf(paintRect.width);
		return startTimeStamp + Double.valueOf(x * cache).longValue();
	}

	private static class PointMouseMotionListener implements MouseMotionListener
	{

		// 缺省线宽
		public int	defaultLine;

		// 线宽
		public int	wideLine;

		public void mouseDragged(org.eclipse.draw2d.MouseEvent me)
		{

		}

		public void mouseEntered(org.eclipse.draw2d.MouseEvent me)
		{
			Polyline polyline = (Polyline) me.getSource();

			defaultLine = polyline.getLineWidth();
			wideLine = defaultLine + 2;

			polyline.setLineWidth(wideLine);
		}

		public void mouseExited(org.eclipse.draw2d.MouseEvent me)
		{
			Polyline polyline = (Polyline) me.getSource();
			polyline.setLineWidth(defaultLine);
		}

		public void mouseHover(org.eclipse.draw2d.MouseEvent me)
		{

		}

		public void mouseMoved(org.eclipse.draw2d.MouseEvent me)
		{

		}
	}

	/**
	 * 将时间戳转化为X坐标
	 * 
	 * @param time
	 * @return
	 */
	private int changeTimeStamp2XAxis(long time)
	{
		long distance = time - viewPart.getStartTimeStamp();
		double perLength = Double.valueOf(viewPart.getPreferenceManager().getTimestampLengthEachPage());
		double width = Double.valueOf(paintRect.width);
		return Double.valueOf(distance / (perLength / width)).intValue();
	}

	/**
	 * 将当前消息值转化未Y坐标
	 * 
	 * @param field
	 * @return
	 */
	private int changeValue2YAxis(FieldElementSet field, int value)
	{
		double max = Double.valueOf(field.getMaxValue() - value);
		double min = Double.valueOf(field.getMaxValue() - field.getMinValue());
		double height = Double.valueOf(paintRect.height);
		return Double.valueOf(max / (min / height)).intValue();
	}
}
