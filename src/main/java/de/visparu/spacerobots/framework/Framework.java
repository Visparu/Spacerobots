package de.visparu.spacerobots.framework;

import java.awt.Graphics2D;

import de.visparu.spacerobots.gui.InputController;
import de.visparu.spacerobots.gui.MainMenu;
import de.visparu.spacerobots.gui.Window;
import de.visparu.spacerobots.settings.GraphicsInfo;

public final class Framework
{
	private static final double  TPS   = 30.0;
	private static final double  FPS   = 200.0;
	private static final double  IDL   = 1.5;
	private static final boolean DEBUG = true;
	
	private boolean init;
	
	private boolean running;
	
	private Window w;
	
	private MainMenu mm;
	
	public Framework()
	{
		this.init = false;
	}
	
	public void info(final int ticks, final int frames, final long cycles)
	{
		System.out.println(String.format("Ticks: %d, Frames: %d, Cycles: %d", ticks, frames, cycles));
	}
	
	public void init(final Window w, final MainMenu mm, final InputController ic)
	{
		if (this.init)
		{
			return;
		}
		
		this.running = false;
		
		this.w = w;
		
		this.mm = mm;
		ic.registerMouseActionSubscriber(mm);
		
		this.init = true;
	}
	
	public void render()
	{
		final var bs           = this.w.getCanvasBufferStrategy();
		final var g2d          = (Graphics2D) bs.getDrawGraphics();
		final var canvasBounds = GraphicsInfo.CANVAS_DIMENSION_BOUNDS;
		
		g2d.setColor(GraphicsInfo.CANVAS_COLOR_BACKGROUND);
		g2d.fillRect(0, 0, (int) canvasBounds.getWidth(), (int) canvasBounds.getHeight());

		this.mm.render(g2d);
		
		g2d.dispose();
		bs.show();
	}
	
	public void run()
	{
		if (!this.init || this.running)
		{
			return;
		}
		
		this.running = true;
		
		final var startTime = System.nanoTime();
		var       lastTick  = startTime;
		var       lastFrame = startTime;
		var       lastInfo  = startTime;
		
		final var deltaTick  = 1000000000 / Framework.TPS;
		final var deltaFrame = 1000000000 / Framework.FPS;
		final var deltaInfo  = 1000000000 * Framework.IDL;
		
		var ticks  = 0;
		var frames = 0;
		var cycles = 0L;
		
		while (this.running)
		{
			final var now = System.nanoTime();
			
			if (now > (lastTick + deltaTick))
			{
				this.tick();
				lastTick = now;
				ticks++;
			}
			
			if (now > (lastFrame + deltaFrame))
			{
				this.render();
				lastFrame = now;
				frames++;
			}
			
			if (now > (lastInfo + deltaInfo))
			{
				final var lastTPS = (int) (ticks / Framework.IDL);
				final var lastFPS = (int) (frames / Framework.IDL);
				if (Framework.DEBUG)
				{
					this.info(lastTPS, lastFPS, cycles);
				}
				lastInfo = now;
				ticks    = 0;
				frames   = 0;
			}
			
			cycles++;
		}
	}
	
	public void tick()
	{
		this.mm.update();
	}
}
