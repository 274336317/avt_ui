package com.coretek.spte.core.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.swt.widgets.Text;

import com.coretek.spte.core.commands.DirectPostilCommand;
import com.coretek.spte.core.models.PostilChildMdl;

public class PostilDirectEditPolicy extends DirectEditPolicy
{

	@Override
	protected Command getDirectEditCommand(DirectEditRequest request)
	{
		Text text = (Text) request.getCellEditor().getControl();

		DirectPostilCommand command = new DirectPostilCommand((PostilChildMdl) getHost().getModel(), text.getText().trim());

		return command;

	}

	@Override
	protected void showCurrentEditValue(DirectEditRequest request)
	{

	}

}