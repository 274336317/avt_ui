/************************************************************************
 *				�����������ɼ������޹�˾ ��Ȩ����
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * �����ṩ�ļ��������ܡ��ڿ����Ĺ��̵��У��û����Բ�ѯ�ļ��Ŀ���������Ϣ�� �統ǰ�ļ��Ŀ������ȡ������ļ����������Ѿ����������ļ���������ǰ����
 * ��������Դ�ļ���Ŀ���ļ��ľ���·�����˺���Ϊ���߳�����ģʽ��������ʹ�� ��IOΪ��ʽIO��δʹ��NIO���ڿ���ʱ��Ҫռ����������ڴ棬����������ļ�����
 * �Ļ����ᵼ�������ִ���������ա�
 * 
 * @author ���Ρ 2011-12-5
 */
public class AdvancedFileCopy implements Runnable
{

	private int				sum;			// �ļ�����

	private String			cDestFile;		// ��ǰ���ڱ�������Ŀ���ļ�

	private String			cSourceFile;	// ��ǰ���ڱ�������Դ�ļ�

	private File			destFile;		// Ŀ���ļ�

	private File			sourceFile;		// Դ�ļ�

	private long			fileSize;		// ���ڱ��������ļ���С

	private long			leftSize;		// ʣ����ļ���С

	private int				leftSum;		// ʣ����ļ�����

	private boolean			finished;		// �Ƿ����������������

	private boolean			canceled;		// �Ƿ�ȡ��

	private ProgressInfo	progressInfo;	// ������Ϣ

	/**
	 * @param destFile ����Ϊ�ļ�����Ŀ¼��������Ӧ�ú�{@code sourceFile}������ƥ��
	 * @param sourceFile ����Ϊ�ļ�����Ŀ¼��������Ӧ�ú�{@code destFile}������ƥ�� <br/>
	 *            <b>����</b> ���Ρ ���� 2011-6-17
	 * @throws Exception ���κ�һ��������ļ�������ʱ���׳����쳣
	 */
	public AdvancedFileCopy(File sourceFile, File destFile) throws Exception
	{
		if (!sourceFile.exists())
		{
			throw new Exception("Դ�ļ�������");
		}
		if (sourceFile.isFile())
		{
			if (destFile.isDirectory())
			{
				throw new IllegalArgumentException("Դ�ļ������ͺ�Ŀ���ļ������Ͳ�ƥ��");
			}
		} else if (sourceFile.isDirectory())
		{
			if (destFile.isFile())
			{
				throw new IllegalArgumentException("Դ�ļ������ͺ�Ŀ���ļ������Ͳ�ƥ��");
			}
		} else
		{
			throw new IllegalArgumentException("Դ�ļ������ͺ�Ŀ���ļ������Ͳ�ƥ��");
		}
		this.destFile = destFile;
		this.sourceFile = sourceFile;
	}

	/**
	 * ��ȡ����������Ϣ
	 * 
	 * @return ������Ϣ <br/>
	 *         <b>����</b> ���Ρ </br> <b>����</b> 2011-6-22
	 */
	public synchronized ProgressInfo getProgressInfo()
	{
		try
		{
			if (this.progressInfo == null)
			{
				return null;
			}
			return (ProgressInfo) this.progressInfo.clone();
		} catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * �ļ����������Ƿ�ȡ��
	 * 
	 * @return </br> <b>����</b> ���Ρ </br> <b>����</b> 2011-6-22
	 */
	private synchronized boolean isCanceled()
	{
		return canceled;
	}

	/**
	 * ȡ�������������̣������Ѿ����������ݲ��ᱻɾ�� <br/>
	 * <b>����</b> ���Ρ </br> <b>����</b> 2011-6-22
	 */
	public synchronized void cancel()
	{
		this.canceled = true;
		this.finished = true;
	}

	/**
	 * ��ѯ�Ƿ������������������<br/>
	 * 
	 * @return ����������������Ѿ��������򷵻�true���򷵻�false <b>����</b> ���Ρ </br> <b>����</b>
	 *         2011-6-22
	 */
	public synchronized boolean isFinished()
	{
		return finished;
	}

	private synchronized void setFinished(boolean finished)
	{
		this.finished = finished;
	}

	private void setLeftSum(int leftSum)
	{
		this.leftSum = leftSum;
	}

	private void setSum(int sum)
	{
		this.sum = sum;
	}

	private void setcDestFile(String cDestFile)
	{
		this.cDestFile = cDestFile;
	}

	private void setcSourceFile(String cSourceFile)
	{
		this.cSourceFile = cSourceFile;
	}

	private void setFileSize(long fileSize)
	{
		this.fileSize = fileSize;
	}

	private void setLeftSize(long leftSize)
	{
		this.leftSize = leftSize;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		if (sourceFile.isFile())
		{
			this.setSum(1);
		} else if (sourceFile.isDirectory())
		{
			// ������ж����ļ���Ҫ������
			this.setSum(FileUtils.getSumOfSubFiles(sourceFile, false));
			this.setLeftSum(this.sum);
		}
		this.progressInfo = new ProgressInfo();
		try
		{
			if (!this.destFile.exists())
			{
				if (this.sourceFile.isDirectory())
				{
					this.destFile.mkdirs();
				} else
				{
					this.destFile.createNewFile();
				}
			}
			this.copy(this.sourceFile, this.destFile);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		this.setFinished(true);
	}

	/**
	 * �����ļ�
	 * 
	 * @param sourceFile ������Դ�ļ����ļ���
	 * @param destFile Ŀ��
	 * @throws IOException <br/>
	 *             <b>����</b> ���Ρ </br> <b>����</b> 2011-7-14
	 */
	private void copy(File sourceFile, File destFile) throws IOException
	{
		if (this.isCanceled())
		{
			return;
		}
		if (sourceFile.isFile())
		{
			this.setFileSize(sourceFile.length());
			this.setLeftSize(sourceFile.length());
			this.setcDestFile(destFile.getAbsolutePath());
			this.setcSourceFile(sourceFile.getAbsolutePath());
			int counter = 0;
			byte[] contents = null;
			if (this.fileSize > 1024 * 1024 * 100)
			{// 100MB
				contents = new byte[1024 * 1024];
			} else if (this.fileSize > 1024 * 1024 * 10)
			{// 10MB
				contents = new byte[1024 * 100];
			} else
			{// 10MB����
				contents = new byte[1024 * 10];
			}
			FileInputStream fis = new FileInputStream(sourceFile);
			FileOutputStream fos = new FileOutputStream(destFile);
			while ((counter = fis.read(contents)) != -1)
			{
				if (this.isCanceled())
				{
					break;
				}
				fos.write(contents, 0, counter);
				this.setLeftSize(this.leftSize - counter);
				synchronized (this)
				{
					this.progressInfo.setInfo();
				}
			}
			fos.flush();
			fos.close();
			fis.close();
			this.setLeftSum(this.leftSum - 1);
		} else if (sourceFile.isDirectory())
		{
			File[] subFiles = sourceFile.listFiles();
			for (File subFile : subFiles)
			{
				if (subFile.isFile())
				{
					File file2 = new File(destFile.getAbsoluteFile() + File.separator + subFile.getName());
					if (file2.exists())
					{
						file2.delete();
					}
					file2.createNewFile();// TODO
					this.copy(subFile, file2);
				}
			}
			for (File subFile : subFiles)
			{
				if (subFile.isDirectory())
				{
					File file2 = new File(destFile.getAbsoluteFile() + File.separator + subFile.getName());
					if (!file2.exists())
					{
						file2.mkdirs();// TODO ����������
					}
					this.copy(subFile, file2);
				}
			}
		}
	}

	/**
	 * ����������Ϣ
	 * 
	 * @author ���Ρ 2011-6-20
	 */
	public class ProgressInfo implements Cloneable
	{

		private int		sum;			// �ļ�����

		private String	cDestFile;		// ��ǰ���ڱ�������Ŀ���ļ�

		private String	cSourceFile;	// ��ǰ���ڱ�������Դ�ļ�

		private long	fileSize;		// ���ڱ��������ļ���С

		private long	leftSize;		// ʣ����ļ���С

		private int		leftSum;		// ʣ����ļ�����

		public ProgressInfo()
		{
			this.sum = AdvancedFileCopy.this.sum;
			this.cDestFile = AdvancedFileCopy.this.cDestFile;
			this.cSourceFile = AdvancedFileCopy.this.cSourceFile;
			this.fileSize = AdvancedFileCopy.this.fileSize;
			this.leftSize = AdvancedFileCopy.this.leftSize;
			this.leftSum = AdvancedFileCopy.this.leftSum;
		}

		/**
		 * ���ý�����Ϣ
		 * 
		 * <b>����</b> ���Ρ </br> <b>����</b> 2011-7-14
		 */
		private void setInfo()
		{
			this.cDestFile = AdvancedFileCopy.this.cDestFile;
			this.cSourceFile = AdvancedFileCopy.this.cSourceFile;
			this.fileSize = AdvancedFileCopy.this.fileSize;
			this.leftSize = AdvancedFileCopy.this.leftSize;
			this.leftSum = AdvancedFileCopy.this.leftSum;
		}

		@Override
		public String toString()
		{
			StringBuilder sb = new StringBuilder();
			sb.append("����:" + this.cSourceFile).append("\n");
			sb.append("�ļ���С:").append(this.fileSize).append("\n");
			sb.append("ʣ���С:").append(this.leftSize).append("\n");
			sb.append("�ļ�����:").append(this.sum).append("\n");
			sb.append("ʣ���ļ�����:").append(this.leftSum).append("\n");
			return sb.toString();
		}

		@Override
		protected Object clone() throws CloneNotSupportedException
		{
			return super.clone();
		}

		/**
		 * ��ȡ�ڿ������̵��л��ж��ٸ��ļ�δ����<br/>
		 * 
		 * @return ���ڵ���������� <b>����</b> ���Ρ </br> <b>����</b> 2011-7-14
		 */
		public int getLeftSum()
		{
			return leftSum;
		}

		/**
		 * ��ȡ��Ҫ�������ļ���������δ�����ļ��е�����<br/>
		 * <b>����</b> ���Ρ </br> <b>����</b> 2011-7-14
		 */
		public int getSum()
		{
			return sum;
		}

		/**
		 * ��ȡ���ڱ�������Ŀ���ļ�<br/>
		 * <b>����</b> ���Ρ </br> <b>����</b> 2011-7-14
		 */
		public String getcDestFile()
		{
			return cDestFile;
		}

		/**
		 * ��ȡ���ڱ�������Դ�ļ�<br/>
		 * <b>����</b> ���Ρ </br> <b>����</b> 2011-7-14
		 */
		public String getcSourceFile()
		{
			return cSourceFile;
		}

		/**
		 * ��ȡ���ڱ��������ļ�����������λΪ�ֽ�<br/>
		 * <b>����</b> ���Ρ </br> <b>����</b> 2011-7-14
		 */
		public long getFileSize()
		{
			return fileSize;
		}

		/**
		 * ��ȡ���ڱ��������ļ���ʣ����������λΪ�ֽ�<br/>
		 * <b>����</b> ���Ρ </br> <b>����</b> 2011-7-14
		 */
		public long getLeftSize()
		{
			return leftSize;
		}
	}

	public static void main(String[] args)
	{
		// TODO �����������
		// File source = new File("D:/Warcraft");
		// File dest = new File("D:/AAAAAAA");
		// try {
		// AdvancedFileCopy afc = new AdvancedFileCopy(source, dest);
		// new Thread(afc).start();
		// while(true) {
		// Thread.sleep(1000);
		// ProgressInfo info = afc.getProgressInfo();
		// System.out.println(info != null? info.toString():"");
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

	}
}
