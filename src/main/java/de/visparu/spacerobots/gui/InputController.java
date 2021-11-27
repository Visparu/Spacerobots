package de.visparu.spacerobots.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

public final class InputController implements MouseListener, MouseMotionListener, KeyListener
{
	private final List<MouseSubscriber> mouseSubscribers;
	
	private final boolean[] keys;
	private final boolean[] mouseKeys;
	
	public InputController()
	{
		final int byteMax = Byte.MAX_VALUE;

		this.mouseSubscribers = new ArrayList<>();
		this.keys             = new boolean[byteMax];
		this.mouseKeys        = new boolean[byteMax];
	}
	
	public boolean isKeyDown(final int key)
	{
		return this.keys[key];
	}
	
	public boolean isMouseButtonDown(final int button)
	{
		return this.mouseKeys[button];
	}

	@Override
	public void keyPressed(final KeyEvent e)
	{
		this.keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(final KeyEvent e)
	{
		this.keys[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(final KeyEvent e)
	{}

	@Override
	public void mouseClicked(final MouseEvent e)
	{}

	@Override
	public void mouseDragged(final MouseEvent e)
	{}

	@Override
	public void mouseEntered(final MouseEvent e)
	{}

	@Override
	public void mouseExited(final MouseEvent e)
	{}

	@Override
	public void mouseMoved(final MouseEvent e)
	{
		for (final MouseSubscriber element : this.mouseSubscribers)
		{
			element.mouseMoved(e);
		}
	}

	@Override
	public void mousePressed(final MouseEvent e)
	{
		this.mouseKeys[e.getButton()] = true;
		for (final MouseSubscriber element : this.mouseSubscribers)
		{
			element.mousePressed(e);
		}
	}
	
	@Override
	public void mouseReleased(final MouseEvent e)
	{
		this.mouseKeys[e.getButton()] = false;
		for (final MouseSubscriber element : this.mouseSubscribers)
		{
			element.mouseReleased(e);
		}
	}
	
	public void register(final MouseSubscriber sub)
	{
		this.mouseSubscribers.add(sub);
	}
}
