/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.spte.monitor.cfg;

import static com.coretek.spte.monitor.cfg.CfgDialog.MONITOR_FILE_SUFFIX;

import java.io.File;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Combo;

import com.coretek.common.utils.StringUtils;

/**
 * @author SunDawei
 * 
 * @date 2012-10-8
 */
class ConfigModifyListener implements ModifyListener
{
	private CfgDialog				dlg;

	private MonitorConfigProcesser	processer;

	public ConfigModifyListener(CfgDialog dlg, MonitorConfigProcesser processer)
	{
		this.dlg = dlg;
		this.processer = processer;
	}

	@Override
	public void modifyText(ModifyEvent e)
	{
		Combo configCombo = (Combo) e.getSource();
		this.dlg.updateOkButton();
		if (configCombo.getSelectionIndex() < 0)
		{ // 如果没有选择任何配置文件则直接返回
			return;
		}
		if (configCombo.getItem(configCombo.getSelectionIndex()).equals(configCombo.getText()))
		{
			processer.read(StringUtils.concat(this.dlg.getConfDir(), File.separator, configCombo.getText(), MONITOR_FILE_SUFFIX));
		}
	}

}
