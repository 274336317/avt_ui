package com.coretek.spte.core.policies;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.editpolicies.ResizableEditPolicy;

public class NoHandlePolicy extends ResizableEditPolicy
{

	@Override
	protected List createSelectionHandles()
	{

		return new ArrayList();
	}

}
