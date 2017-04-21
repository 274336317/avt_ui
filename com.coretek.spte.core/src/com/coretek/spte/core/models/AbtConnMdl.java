package com.coretek.spte.core.models;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.graphics.Color;

import com.coretek.common.utils.StringUtils;
import com.coretek.spte.core.InstanceUtils;
import com.coretek.spte.core.util.SPTEConstants;
import com.coretek.spte.core.util.TestCaseStatus;

/**
 * ����ģ�͵Ļ���
 * 
 * @author ���Ρ
 * @date 2010-8-21
 * 
 */
public abstract class AbtConnMdl extends AbtElement
{

	private static final long			serialVersionUID	= 2514693171655648233L;
	/**
	 * ��ɫ����
	 */
	public final static String			PROP_COLOR			= "prop_color";
	/**
	 * ���ߵ���ɫ
	 */
	transient protected Color			color;
	/**
	 * ����Դ
	 */
	protected AbtNode					source				= null;
	/**
	 * ����Ŀ��
	 */
	protected AbtNode					target				= null;
	/**
	 * ͼ�ε�ȱʡ��ʾ��ɫ
	 */
	protected transient Color			defaultColor;

	/**
	 * ��ʶ������Ϣ�Ƿ�ִ�гɹ�������ɹ���Ϊfalse�� ����Ϊtrue;
	 */
	protected boolean					failed;
	/**
	 * ��Ϣ״̬
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
			if (this.getName().contains("��ͨ") || this.getName().contains("����") )
			{
				this.color = InstanceUtils.getInstance().getMesOrBackgroudDefaultColor();
			}
			if (this.getName().contains("����") || this.getName().contains("����") )
			{
				this.color = InstanceUtils.getInstance().getPeriodOrParallelDefaultColor();
			}
			if (StringUtils.isNumber(this.getName()))
			{
				this.color = InstanceUtils.getInstance().getIntervalColor(); // ʱ������Ϣ
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
		if (source.getMesType().contains("��ͨ") || source.getMesType().contains("����"))
		{
			m_TesmpColor = InstanceUtils.getInstance().getMesOrBackgroudDefaultColor();
		}
		if (source.getMesType().contains("����") || source.getMesType().contains("ƽ��"))
		{
			m_TesmpColor = InstanceUtils.getInstance().getPeriodOrParallelDefaultColor();
		}
		if (source.getMesType().contains("ʱ����"))
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