package de.visparu.spacerobots.gui.subscribers;

public interface MousePressedSubscriber extends MouseActionSubscriber
{
	void mousePressed(int x, int y, int keyCode);
}
