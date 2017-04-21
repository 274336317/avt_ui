package com.coretek.spte.parser;

/**
 * 流读取器，负责从特定的流中读取二进制数据
 * 
 * @author 孙大巍 2012-3-20
 */
public interface IStreamReader
{

	public final static byte[]	ENDTAG	= new byte[2 * 1024];

	/**
	 * 读取数据。
	 * 
	 * @param buffer 存放从流中读取的数据
	 * @param size 存放数据的缓冲字节大小
	 * @return 返回实际读取大小，当到达流末尾时返回-1。
	 */
	int read(byte[] buffer, int size);

	/**
	 * 判断流是否可读。
	 * 
	 * @return 返回true表示可读，false表示不可读
	 */
	boolean canRead();

	/**
	 * 停止读取操作
	 */
	void shutDown();
}