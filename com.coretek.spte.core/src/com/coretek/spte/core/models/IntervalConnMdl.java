package com.coretek.spte.core.models;

import com.coretek.spte.core.util.SPTEConstants;
import com.coretek.spte.testcase.TimeSpan;

/**
 * 时间间隔连线模型
 * 
 * @author 孙大巍
 * @date 2010-11-24
 * 
 */
public class IntervalConnMdl extends AbtConnMdl
{

	private static final long		serialVersionUID	= 7414191853444584854L;

	/**
	 * 时间间隔。用于保存时间间隔设置，在生成xml文件的时候，需要将 此对象加入到resultMessage中。
	 */
	protected transient TimeSpan	resultInterval;

	public IntervalConnMdl()
	{
	}

	public IntervalConnMdl(AbtNode source, AbtNode target)
	{
		super(source, target);
	}

	public TimeSpan getResultInterval()
	{
		return resultInterval;
	}

	public void setResultInterval(TimeSpan resultInterval)
	{
		this.resultInterval = resultInterval;
		this.firePropertyChange(SPTEConstants.EVENT_PROP_RESULT_INTERVAL_CHANGED, this.resultInterval);
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		IntervalConnMdl model = (IntervalConnMdl) super.clone();
		model.rootModel = this.rootModel;
		model.parent = this.parent;
		model.source = null;
		model.target = null;
		model.color = this.color;
		model.resultInterval = (TimeSpan) this.resultInterval.clone();

		return model;
	}

}
