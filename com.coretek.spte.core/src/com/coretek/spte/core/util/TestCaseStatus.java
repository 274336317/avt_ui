package com.coretek.spte.core.util;

/**
 * ��������״̬
 * 
 * @author ���Ρ
 * 
 */
public enum TestCaseStatus
{

	Editing("Editing", "�༭״̬"), // �༭״̬
	ViewResult("ViewResult", "�鿴���н��"), // �鿴���н��
	Debug("Debug", "����״̬"); // ����

	private String	name;

	private String	text;

	TestCaseStatus(String name, String text)
	{
		this.name = name;
		this.text = text;
	}

	public String getName()
	{
		return name;
	}

	public String getText()
	{
		return text;
	}

	@Override
	public String toString()
	{
		return text;
	}
}
