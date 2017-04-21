package com.coretek.spte.parser;

/**
 * 执行器环境
 * 
 * @author 孙大巍 2012-3-27
 */
public interface IExecutorContext extends IContext
{

	/**
	 * 查看解析操作是否完成
	 * 
	 * @return 如果解析操作执行完毕，则返回true，否则返回false
	 */
	public boolean isFinished();

	/**
	 * 开始解析
	 * 
	 * @return 如果开启成功则返回true，否则返回false
	 */
	public boolean startParsing();

}
