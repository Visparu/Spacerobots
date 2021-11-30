package de.visparu.spacerobots.gui.subscribers;

public interface MouseReleasedSubscriber extends MouseActionSubscriber
{
	void mouseReleased(int x, int y, int keyCode);
}
