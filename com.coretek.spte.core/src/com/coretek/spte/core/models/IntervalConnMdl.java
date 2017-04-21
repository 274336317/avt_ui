package com.coretek.spte.core.models;

import com.coretek.spte.core.util.SPTEConstants;
import com.coretek.spte.testcase.TimeSpan;

/**
 * ʱ��������ģ��
 * 
 * @author ���Ρ
 * @date 2010-11-24
 * 
 */
public class IntervalConnMdl extends AbtConnMdl
{

	private static final long		serialVersionUID	= 7414191853444584854L;

	/**
	 * ʱ���������ڱ���ʱ�������ã�������xml�ļ���ʱ����Ҫ�� �˶�����뵽resultMessage�С�
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
