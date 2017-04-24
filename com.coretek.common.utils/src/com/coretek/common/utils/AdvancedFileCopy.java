/************************************************************************
 *				北京科银京成技术有限公司 版权所有
 *    Copyright (C) 2000-2011 CoreTek Systems Inc. All Rights Reserved.
 ***********************************************************************/
package com.coretek.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 此类提供文件拷贝功能。在拷贝的过程当中，用户可以查询文件的拷贝进度信息， 如当前文件的拷贝进度、拷贝文件的总数、已经被拷贝的文件总数、当前正在
 * 被拷贝的源文件和目标文件的绝对路径。此函数为单线程运行模式。此类所使用 的IO为老式IO，未使用NIO，在拷贝时需要占用虚拟机堆内存，如果拷贝的文件过大
 * 的话，会导致虚拟机执行垃圾回收。
 * 
 * @author 孙大巍 2011-12-5
 */
public class AdvancedFileCopy implements Runnable
{

	private int				sum;			// 文件个数

	private String			cDestFile;		// 当前正在被拷贝的目标文件

	private String			cSourceFile;	// 当前正在被拷贝的源文件

	private File			destFile;		// 目标文件

	private File			sourceFile;		// 源文件

	private long			fileSize;		// 正在被拷贝的文件大小

	private long			leftSize;		// 剩余的文件大小

	private int				leftSum;		// 剩余的文件数量

	private boolean			finished;		// 是否完成整个拷贝过程

	private boolean			canceled;		// 是否被取消

	private ProgressInfo	progressInfo;	// 进度信息

	/**
	 * @param destFile 可以为文件或者目录，但是它应该和{@code sourceFile}的类型匹配
	 * @param sourceFile 可以为文件或者目录，但是它应该和{@code destFile}的类型匹配 <br/>
	 *            <b>作者</b> 孙大巍 日期 2011-6-17
	 * @throws Exception 当任何一个传入的文件不存在时会抛出此异常
	 */
	public AdvancedFileCopy(File sourceFile, File destFile) throws Exception
	{
		if (!sourceFile.exists())
		{
			throw new Exception("源文件不存在");
		}
		if (sourceFile.isFile())
		{
			if (destFile.isDirectory())
			{
				throw new IllegalArgumentException("源文件的类型和目标文件的类型不匹配");
			}
		} else if (sourceFile.isDirectory())
		{
			if (destFile.isFile())
			{
				throw new IllegalArgumentException("源文件的类型和目标文件的类型不匹配");
			}
		} else
		{
			throw new IllegalArgumentException("源文件的类型和目标文件的类型不匹配");
		}
		this.destFile = destFile;
		this.sourceFile = sourceFile;
	}

	/**
	 * 获取拷贝进度信息
	 * 
	 * @return 进度信息 <br/>
	 *         <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-6-22
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
	 * 文件拷贝操作是否被取消
	 * 
	 * @return </br> <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-6-22
	 */
	private synchronized boolean isCanceled()
	{
		return canceled;
	}

	/**
	 * 取消整个拷贝过程，但是已经拷贝的数据不会被删除 <br/>
	 * <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-6-22
	 */
	public synchronized void cancel()
	{
		this.canceled = true;
		this.finished = true;
	}

	/**
	 * 查询是否完成了整个拷贝过程<br/>
	 * 
	 * @return 如果整个拷贝过程已经结束，则返回true否则返回false <b>作者</b> 孙大巍 </br> <b>日期</b>
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
			// 计算出有多少文件需要被拷贝
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
	 * 拷贝文件
	 * 
	 * @param sourceFile 拷贝的源文件或文件夹
	 * @param destFile 目标
	 * @throws IOException <br/>
	 *             <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-7-14
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
			{// 10MB以下
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
						file2.mkdirs();// TODO 处理错误情况
					}
					this.copy(subFile, file2);
				}
			}
		}
	}

	/**
	 * 拷贝进度信息
	 * 
	 * @author 孙大巍 2011-6-20
	 */
	public class ProgressInfo implements Cloneable
	{

		private int		sum;			// 文件个数

		private String	cDestFile;		// 当前正在被拷贝的目标文件

		private String	cSourceFile;	// 当前正在被拷贝的源文件

		private long	fileSize;		// 正在被拷贝的文件大小

		private long	leftSize;		// 剩余的文件大小

		private int		leftSum;		// 剩余的文件数量

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
		 * 设置进度信息
		 * 
		 * <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-7-14
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
			sb.append("拷贝:" + this.cSourceFile).append("\n");
			sb.append("文件大小:").append(this.fileSize).append("\n");
			sb.append("剩余大小:").append(this.leftSize).append("\n");
			sb.append("文件数量:").append(this.sum).append("\n");
			sb.append("剩余文件数量:").append(this.leftSum).append("\n");
			return sb.toString();
		}

		@Override
		protected Object clone() throws CloneNotSupportedException
		{
			return super.clone();
		}

		/**
		 * 获取在拷贝过程当中还有多少个文件未拷贝<br/>
		 * 
		 * @return 大于等于零的整数 <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-7-14
		 */
		public int getLeftSum()
		{
			return leftSum;
		}

		/**
		 * 获取需要被拷贝文件的数量，未包含文件夹的数量<br/>
		 * <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-7-14
		 */
		public int getSum()
		{
			return sum;
		}

		/**
		 * 获取正在被拷贝的目标文件<br/>
		 * <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-7-14
		 */
		public String getcDestFile()
		{
			return cDestFile;
		}

		/**
		 * 获取正在被拷贝的源文件<br/>
		 * <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-7-14
		 */
		public String getcSourceFile()
		{
			return cSourceFile;
		}

		/**
		 * 获取正在被拷贝的文件的容量，单位为字节<br/>
		 * <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-7-14
		 */
		public long getFileSize()
		{
			return fileSize;
		}

		/**
		 * 获取正在被拷贝的文件的剩余容量，单位为字节<br/>
		 * <b>作者</b> 孙大巍 </br> <b>日期</b> 2011-7-14
		 */
		public long getLeftSize()
		{
			return leftSize;
		}
	}

	public static void main(String[] args)
	{
		// TODO 请在这里测试
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
