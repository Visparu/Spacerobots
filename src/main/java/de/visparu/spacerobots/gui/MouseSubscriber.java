package de.visparu.spacerobots.gui;

import java.awt.event.MouseEvent;

public interface MouseSubscriber
{
	void mouseMoved(MouseEvent e);
	
	void mousePressed(MouseEvent e);
	
	void mouseReleased(MouseEvent e);
}
