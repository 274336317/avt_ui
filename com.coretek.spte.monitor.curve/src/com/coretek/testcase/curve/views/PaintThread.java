/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/

package com.coretek.testcase.curve.views;

/**
 * ��ͼ�̣߳����ݵ�ǰ������ͼ����ʼʱ���������ʱ�������ǰʱ������Ƶ�ǰ֡������ͼ��
 * 
 * @author ���� 2012-3-14
 */
public class PaintThread extends Thread
{

	// �Ƿ�ȴ���ѯ�̵߳Ļ���֪ͨ
	private boolean		isWait;

	// ������ͼ
	private CurveView	viewPart;

	private static int	id	= 0;

	public PaintThread(String name, CurveView viewPart, boolean isWait)
	{
		super("�����߳�" + id);
		this.viewPart = viewPart;
		this.isWait = isWait;
		if (isWait)
			id++;
	}

	/**
	 * ��ѯ�Ƿ�ȴ���ѯ�̵߳Ļ���֪ͨ
	 * 
	 * @return �Ƿ�ȴ���ѯ�̵߳Ļ���֪ͨ </br>
	 */
	public boolean isWait()
	{
		return isWait;
	}

	/*
	 * (non-Javadoc) ���߳��е������߻�ͼ���������л�����ͼ
	 * 
	 * @see java.lang.Thread#run() <br/>
	 */
	@Override
	public void run()
	{
		PaintManager paintManager = new PaintManager(viewPart);

		if (viewPart.isTerminated())
		{
			paintManager.paint();
		} else
		{
			// ��ǰ������ͼ���ڼ��״̬����ѭ������ˢ�����߻�ͼ���档
			if (!viewPart.isTerminated())
			{
				if (!viewPart.isLockMonitor())
				{
					paintManager.paint();

					try
					{
						Thread.sleep(viewPart.getPaintTime());
					} catch (Exception e)
					{
						e.printStackTrace();
					}
					setTimeStamp();
				}
			}
		}
		return;
	}

	/**
	 * ����������ͼ�Ŀ�ʼʱ���������ʱ�������ǰʱ����� </br>
	 */
	private void setTimeStamp()
	{
		long current = viewPart.getCurrentTimeStamp();
		long start = viewPart.getStartTimeStamp();
		long end = viewPart.getEndTimeStamp();

		if (end - start > viewPart.getPreferenceManager().getTimestampLengthEachPage())
		{
			viewPart.setStartTimeStamp(current - viewPart.getPreferenceManager().getTimestampLengthEachPage());
			viewPart.setEndTimeStamp(current);
		} else if (end - start == viewPart.getPreferenceManager().getTimestampLengthEachPage())
		{
			viewPart.setStartTimeStamp(start + current - end);
			viewPart.setEndTimeStamp(current);
		} else if (end - start > 0)
		{
			viewPart.setEndTimeStamp(current);
		} else
		{
			if (current >= viewPart.getPreferenceManager().getTimestampLengthEachPage())
			{
				viewPart.setStartTimeStamp(current - viewPart.getPreferenceManager().getTimestampLengthEachPage());
				viewPart.setEndTimeStamp(current);
			} else
			{
				if (!viewPart.isTerminated())
				{
					viewPart.setEndTimeStamp(current);
				}
			}
		}
	}

	/**
	 * �����Ƿ�ȴ���ѯ�̵߳Ļ���֪ͨ
	 * 
	 * @param isWait �Ƿ�ȴ���ѯ�̵߳Ļ���֪ͨ </br>
	 */
	public void setWait(boolean isWait)
	{
		this.isWait = isWait;
	}
}
