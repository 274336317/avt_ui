/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.testcase.curve.views;

import java.util.List;

import com.coretek.common.template.Constants;
import com.coretek.common.template.SPTEMsg;
import com.coretek.common.template.build.codeTemplate.Entity;
import com.coretek.spte.testcase.Field;
import com.coretek.spte.testcase.Period;
import com.coretek.testcase.curve.internal.model.FieldElement;
import com.coretek.testcase.curve.internal.model.FieldElementSet;

/**
 * �źŷ�����
 * 
 * @author ���� 2012-3-14
 */
public class FieldElementParser
{

	private CurveView	viewPart;

	public FieldElementParser(CurveView viewPart)
	{
		this.viewPart = viewPart;
	}

	/**
	 * ���������ݿ��õ���Ϣ��������Ϣ�����µ��źŵ�ֵ������ӵ��źż����С�
	 * 
	 * @param msgs �����ݿ��õ���Ϣ </br>
	 */
	public void parse(SPTEMsg msgs[])
	{
		if (msgs != null && msgs.length > 0)
		{
			for (SPTEMsg msg : msgs)
			{
				List<FieldElementSet> fields = viewPart.getManager().getAllFields();
				for (FieldElementSet field : fields)
				{
					SPTEMsg spteMsg = field.getMonitorMsgNode();
					if (spteMsg.getICDMsg().getAttribute(Constants.ICD_MSG_NAME).getValue().toString().equals(msg.getICDMsg().getAttribute(Constants.ICD_MSG_NAME).getValue().toString()))
					{
						field.addSPTEMsgElement(msg);
						String fieldName = field.getField().getAttribute(Constants.ICD_FIELD_NAME).getValue().toString();

						List<Entity> children = msg.getMsg().getChildren();

						for (int i = 0; i < children.size(); i++)
						{
							Entity entry = children.get(i);
							if (entry instanceof Period)
							{
								List<Entity> periodChildren = entry.getChildren();
								for (int j = 0; j < periodChildren.size(); j++)
								{
									Entity fieldEntity = periodChildren.get(j);
									if (fieldEntity instanceof Field)
									{
										try
										{
											if (((Field) fieldEntity).getName().equals(fieldName))
											{
												String value = (String) ((Field) fieldEntity).getValue();
												FieldElement element = new FieldElement(Integer.parseInt(value), msg.getTimeStamp());
												field.addElement(element);
											}
										} catch (IllegalArgumentException e)
										{
											e.printStackTrace();
										}
									}
								}
							} else if (entry instanceof Field)
							{
								try
								{
									if (((Field) entry).getName().equals(fieldName))
									{
										String value = (String) ((Field) entry).getValue();
										FieldElement element = new FieldElement(Integer.parseInt(value), msg.getTimeStamp());
										field.addElement(element);
									}
								} catch (IllegalArgumentException e)
								{
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
	}
}
