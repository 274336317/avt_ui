package com.coretek.spte.core.editor;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.INavigationLocation;
import org.eclipse.ui.NavigationLocation;

public class SPTENavigationLocation extends NavigationLocation
{

	protected SPTENavigationLocation(IEditorPart editorPart)
	{
		super(editorPart);

	}

	public boolean mergeInto(INavigationLocation location)
	{
		if (location == null)
			return false;

		if (getClass() != location.getClass())
			return false;

		return true;
	}

	public void restoreLocation()
	{
		// IEditorPart part= getEditorPart();
		// if(part instanceof SPTEEditor) {
		// //TODO
		// }
	}

	public void restoreState(IMemento memento)
	{

	}

	public void saveState(IMemento memento)
	{

	}

	public void update()
	{

	}

}
