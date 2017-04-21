package com.coretek.spte.core.debug;

/**
 * µ÷ÊÔÃüÁî
 * 
 * @author Ëï´óÎ¡
 * 
 */
public enum DebugCmd
{
	StepInto("F5", "StepInto"), StepOver("F6", "StepOver"), StepReturn("F7", "StepReturn"), Terminate("F8", "Terminate"), Begin("F11", "Begin");

	DebugCmd(String key, String cmd)
	{
		this.key = key;
		this.cmd = cmd;
	}

	public String getKey()
	{
		return key;
	}

	public String getCmd()
	{
		return cmd;
	}

	private String	key;

	private String	cmd;

	public static DebugCmd getCmd(String key)
	{
		for (DebugCmd cmd : DebugCmd.values())
		{
			if (cmd.getKey().equalsIgnoreCase(key))
			{
				return cmd;
			}
		}

		return null;
	}
}