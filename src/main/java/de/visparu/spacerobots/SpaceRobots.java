package de.visparu.spacerobots;

import de.visparu.spacerobots.framework.Framework;
import de.visparu.spacerobots.game.InternalArena;
import de.visparu.spacerobots.gui.InputController;
import de.visparu.spacerobots.gui.MainMenu;
import de.visparu.spacerobots.gui.Window;
import de.visparu.spacerobots.settings.GraphicsInfo;
import de.visparu.spacerobots.util.ClassFinder;

public final class SpaceRobots
{
	public static void main(final String[] args)
	{
		final var fw = new Framework();
		final var w  = new Window();
		final var ic = new InputController();
		final var a  = new InternalArena();
		final var mm = new MainMenu();
		
		a.init(ClassFinder.find("de.visparu.spacerobots.game.entities.external.robots"), -GraphicsInfo.ARENA_STARTTEXT_TIME, (int) (Math.random() * Integer.MAX_VALUE));
		w.init(ic);
		mm.init(a);
		fw.init(w, mm, ic);
		
		fw.run();
	}
}
