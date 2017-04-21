package com.coretek.testcase.unit;

import com.coretek.common.template.ClazzManager;
import com.coretek.common.template.TemplateEngine;

/**
 * 单位管理器
 * 
 * @author duyisen 2012-01-13
 * 
 */
public class UnitManager
{

	private static UnitManager	unitMan;

	private ClazzManager		unitClazzMan;

	private UnitManager()
	{
		setUnitClazzMan(TemplateEngine.getEngine().getUnitManager());
	}

	public static UnitManager getInstance()
	{
		if (null == unitMan)
			unitMan = new UnitManager();

		return unitMan;
	}

	public ClazzManager getUnitClazzMan()
	{
		return unitClazzMan;
	}

	public void setUnitClazzMan(ClazzManager unitClazzMan)
	{
		this.unitClazzMan = unitClazzMan;
	}

}
