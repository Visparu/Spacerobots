package de.visparu.spacerobots.gui.subscribers;

public interface MouseMovedSubscriber extends MouseActionSubscriber
{
	void mouseMoved(int prevX, int prevY, int newX, int newY);
}
