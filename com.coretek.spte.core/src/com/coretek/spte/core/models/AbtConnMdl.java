package com.coretek.spte.core.models;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.graphics.Color;

import com.coretek.common.utils.StringUtils;
import com.coretek.spte.core.InstanceUtils;
import com.coretek.spte.core.util.SPTEConstants;
import com.coretek.spte.core.util.TestCaseStatus;

/**
 * 连接模型的基类
 * 
 * @author 孙大巍
 * @date 2010-8-21
 * 
 */
public abstract class AbtConnMdl extends AbtElement
{

	private static final long			serialVersionUID	= 2514693171655648233L;
	/**
	 * 颜色属性
	 */
	public final static String			PROP_COLOR			= "prop_color";
	/**
	 * 连线的颜色
	 */
	transient protected Color			color;
	/**
	 * 连接源
	 */
	protected AbtNode					source				= null;
	/**
	 * 连接目标
	 */
	protected AbtNode					target				= null;
	/**
	 * 图形的缺省显示颜色
	 */
	protected transient Color			defaultColor;

	/**
	 * 标识测试消息是否执行成功，如果成功则为false， 否则为true;
	 */
	protected boolean					failed;
	/**
	 * 消息状态
	 */
	protected transient TestCaseStatus	status				= TestCaseStatus.Editing;

	public boolean isFailed()
	{
		return failed;
	}

	public void setFailed(boolean failed)
	{
		if (this.failed != failed)
		{
			this.failed = failed;
			this.setColor(this.getDefaultColor());
		}
	}

	public TestCaseStatus getStatus()
	{
		return status;
	}

	public void setStatus(TestCaseStatus status)
	{
		if (this.status != status)
		{
			this.status = status;
			this.firePropertyChange(SPTEConstants.EVENT_STATUS_CHANGED, this.status);
		}
	}

	public Color getDefaultColor()
	{
		if (this.failed)
		{
			return ColorConstants.red;
		}
		if (this.defaultColor != null)
		{
			return this.defaultColor;
		}
		return ColorConstants.darkGreen;
	}

	public void setDefaultColor(Color color)
	{
		this.defaultColor = color;
	}

	public Color getColor()
	{
		if (this.status == TestCaseStatus.Debug)
		{
			return SPTEConstants.COLOR_DEBUG;
		}
		if (this.color == null)
		{
			this.color = ColorConstants.darkGreen;
			if (this.getName().contains("普通") || this.getName().contains("背景") )
			{
				this.color = InstanceUtils.getInstance().getMesOrBackgroudDefaultColor();
			}
			if (this.getName().contains("周期") || this.getName().contains("并行") )
			{
				this.color = InstanceUtils.getInstance().getPeriodOrParallelDefaultColor();
			}
			if (StringUtils.isNumber(this.getName()))
			{
				this.color = InstanceUtils.getInstance().getIntervalColor(); // 时间间隔消息
			}
		}
		return color;
	}

	public void setColor(Color color)
	{
		if (!this.color.equals(color))
		{
			this.color = color;
			this.firePropertyChange(PROP_COLOR, color);
		}
	}

	public void setSource(AbtNode source)
	{
		this.source = source;
	}

	public void setTarget(AbtNode target)
	{
		this.target = target;
	}

	public AbtNode getTarget()
	{
		return this.target;
	}

	public AbtNode getSource()
	{
		return this.source;
	}

	public AbtConnMdl()
	{
//		this.color = ColorConstants.darkGreen;
//		this.defaultColor = ColorConstants.darkGreen;
	}

	public AbtConnMdl(AbtNode source, AbtNode target)
	{
		super();
		Color m_TesmpColor = new Color(null, 102, 187, 0);
		if (source.getMesType().contains("普通") || source.getMesType().contains("背景"))
		{
			m_TesmpColor = InstanceUtils.getInstance().getMesOrBackgroudDefaultColor();
		}
		if (source.getMesType().contains("周期") || source.getMesType().contains("平行"))
		{
			m_TesmpColor = InstanceUtils.getInstance().getPeriodOrParallelDefaultColor();
		}
		if (source.getMesType().contains("时间间隔"))
		{
			m_TesmpColor = InstanceUtils.getInstance().getIntervalColor();
		}
		this.color = m_TesmpColor; 
		this.defaultColor = m_TesmpColor;
		this.source = source;
		this.target = target;

		this.source.addOutput(this);
		this.target.addInput(this);
	}

	public void reConnect()
	{
		if (this.source != null && this.target != null)
		{
			source.removeOutput(this);
			source.addOutput(this);
			target.removeInput(this);
			target.addInput(this);
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}
}