package de.visparu.spacerobots.gui;

import java.awt.Canvas;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import de.visparu.spacerobots.settings.GraphicsInfo;

public final class Window
{
	private JFrame frame;
	private Canvas canvas;
	
	private boolean init;
	
	public Window()
	{
		this.init = false;
	}
	
	public BufferStrategy getCanvasBufferStrategy()
	{
		final var buffers = GraphicsInfo.CANVAS_BUFFERS;

		var bs = this.canvas.getBufferStrategy();
		if (bs == null)
		{
			this.canvas.createBufferStrategy(buffers);
			bs = this.canvas.getBufferStrategy();
		}
		return bs;
	}
	
	public void init(final InputController ic)
	{
		if (this.init)
		{
			return;
		}
		
		this.initFrameInit();
		this.initFrameConfigure();
		this.initFrameControl(ic);
		this.initFrameConnect();
		this.initFramePresent();
		
		this.init = true;
	}
	
	private void initFrameConfigure()
	{
		final var exit         = WindowConstants.EXIT_ON_CLOSE;
		final var title        = GraphicsInfo.FRAME_TITLE;
		final var canvasBounds = GraphicsInfo.CANVAS_DIMENSION_BOUNDS;

		this.frame.setResizable(false);
		this.frame.setDefaultCloseOperation(exit);
		this.frame.setTitle(title);
		
		this.canvas.setSize(canvasBounds);
	}
	
	private void initFrameConnect()
	{
		this.frame.add(this.canvas);
	}
	
	private void initFrameControl(final InputController ic)
	{
		this.canvas.addMouseListener(ic.getMouseInputController());
		this.canvas.addMouseMotionListener(ic.getMouseInputController());
		this.canvas.addKeyListener(ic.getKeyInputController());
	}
	
	private void initFrameInit()
	{
		this.frame  = new JFrame();
		this.canvas = new Canvas();
	}
	
	private void initFramePresent()
	{
		this.frame.pack();
		this.frame.setLocationRelativeTo(null);
		this.frame.setVisible(true);
	}
}
