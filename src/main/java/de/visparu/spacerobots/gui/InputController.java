package de.visparu.spacerobots.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import de.visparu.spacerobots.gui.subscribers.KeyActionSubscriber;
import de.visparu.spacerobots.gui.subscribers.KeyPressedSubscriber;
import de.visparu.spacerobots.gui.subscribers.KeyReleasedSubscriber;
import de.visparu.spacerobots.gui.subscribers.MouseActionSubscriber;
import de.visparu.spacerobots.gui.subscribers.MouseMovedSubscriber;
import de.visparu.spacerobots.gui.subscribers.MousePressedSubscriber;
import de.visparu.spacerobots.gui.subscribers.MouseReleasedSubscriber;

public final class InputController
{
	private MouseInputController mouseInputController;
	private KeyInputController   keyInputController;
	
	private final List<MousePressedSubscriber>  mousePressedSubscribers;
	private final List<MouseReleasedSubscriber> mouseReleasedSubscribers;
	private final List<MouseMovedSubscriber>    mouseMovedSubscribers;
	private final List<KeyPressedSubscriber>    keyPressedSubscribers;
	private final List<KeyReleasedSubscriber>   keyReleasedSubscribers;
	
	private final Set<Integer> keys;
	private final Set<Integer> buttons;
	
	private int lastMouseX;
	private int lastMouseY;
	
	public InputController()
	{
		this.mouseInputController = new MouseInputController();
		this.keyInputController   = new KeyInputController();
		
		this.mousePressedSubscribers  = new ArrayList<>();
		this.mouseReleasedSubscribers = new ArrayList<>();
		this.mouseMovedSubscribers    = new ArrayList<>();
		this.keyPressedSubscribers    = new ArrayList<>();
		this.keyReleasedSubscribers   = new ArrayList<>();
		
		this.keys    = new TreeSet<>();
		this.buttons = new TreeSet<>();
		
		this.lastMouseX = 0;
		this.lastMouseY = 0;
	}
	
	public boolean isKeyDown(final int key)
	{
		return this.keys.contains(key);
	}
	
	public boolean isButtonDown(final int button)
	{
		return this.buttons.contains(button);
	}
	
	public void registerMouseActionSubscriber(final MouseActionSubscriber sub)
	{
		if(sub instanceof MouseMovedSubscriber mms)
		{
			this.mouseMovedSubscribers.add(mms);
		}
		if(sub instanceof MousePressedSubscriber mps)
		{
			this.mousePressedSubscribers.add(mps);
		}
		if(sub instanceof MouseReleasedSubscriber mrs)
		{
			this.mouseReleasedSubscribers.add(mrs);
		}
	}
	
	public void registerKeyActionSubscriber(final KeyActionSubscriber sub)
	{
		if(sub instanceof KeyPressedSubscriber kps)
		{
			this.keyPressedSubscribers.add(kps);			
		}
		if(sub instanceof KeyReleasedSubscriber krs)
		{
			this.keyReleasedSubscribers.add(krs);
		}
	}
	
	public void deregisterMousePressedSubscriber(final MouseActionSubscriber sub)
	{
		if(sub instanceof MouseMovedSubscriber mms)
		{
			this.mouseMovedSubscribers.remove(mms);
		}
		if(sub instanceof MousePressedSubscriber mps)
		{
			this.mousePressedSubscribers.remove(mps);
		}
		if(sub instanceof MouseReleasedSubscriber mrs)
		{
			this.mouseReleasedSubscribers.remove(mrs);
		}
	}
	
	public void deregisterKeyPressedSubscriber(final KeyActionSubscriber sub)
	{
		if(sub instanceof KeyPressedSubscriber kps)
		{
			this.keyPressedSubscribers.remove(kps);			
		}
		if(sub instanceof KeyReleasedSubscriber krs)
		{
			this.keyReleasedSubscribers.remove(krs);
		}
	}
	
	public MouseInputController getMouseInputController()
	{
		return this.mouseInputController;
	}
	
	public KeyInputController getKeyInputController()
	{
		return this.keyInputController;
	}
	
	public class MouseInputController extends MouseAdapter
	{
		@Override
		public void mousePressed(MouseEvent e)
		{
			InputController.this.buttons.add(e.getButton());
			InputController.this.mousePressedSubscribers.forEach(sub -> sub.mousePressed(e.getX(), e.getY(), e.getButton()));
		}
		
		@Override
		public void mouseReleased(MouseEvent e)
		{
			InputController.this.buttons.remove(e.getButton());
			InputController.this.mouseReleasedSubscribers.forEach(sub -> sub.mouseReleased(e.getX(), e.getY(), e.getButton()));
		}
		
		@Override
		public void mouseMoved(MouseEvent e)
		{
			int lastX = InputController.this.lastMouseX;
			int lastY = InputController.this.lastMouseY;
			int nextX = e.getX();
			int nextY = e.getY();
			
			InputController.this.mouseMovedSubscribers.forEach(sub -> sub.mouseMoved(lastX, lastY, nextX, nextY));
			
			InputController.this.lastMouseX = nextX;
			InputController.this.lastMouseY = nextY;
		}
	}
	
	public class KeyInputController extends KeyAdapter
	{
		@Override
		public void keyPressed(KeyEvent e)
		{
			InputController.this.keys.add(e.getKeyCode());
		}
		
		@Override
		public void keyReleased(KeyEvent e)
		{
			InputController.this.keys.add(e.getKeyCode());
		}
	}
}
