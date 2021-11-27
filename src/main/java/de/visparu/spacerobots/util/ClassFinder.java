package de.visparu.spacerobots.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.visparu.spacerobots.game.entities.internal.InternalRobot;

public final class ClassFinder
{
	private static final char PKG_SEPARATOR = '.';

	private static final char DIR_SEPARATOR = '/';

	private static final String CLASS_FILE_SUFFIX = ".class";

	private static final String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. Are you sure the package '%s' exists?";

	private ClassFinder()
	{
		
	}
	
	private static List<Class<?>> find(final File file, final String scannedPackage)
	{
		final List<Class<?>> classes  = new ArrayList<>();
		final var            resource = scannedPackage + ClassFinder.PKG_SEPARATOR + file.getName();
		if (file.isDirectory())
		{
			for (final File child : file.listFiles())
			{
				classes.addAll(ClassFinder.find(child, resource));
			}
		}
		else if (resource.endsWith(ClassFinder.CLASS_FILE_SUFFIX))
		{
			final var endIndex  = resource.length() - ClassFinder.CLASS_FILE_SUFFIX.length();
			final var className = resource.substring(0, endIndex);
			try
			{
				classes.add(Class.forName(className));
			}
			catch (final ClassNotFoundException e)
			{
				e.printStackTrace();
			}
		}
		return classes;
	}

	public static List<Class<?>> find(final String scannedPackage) throws IllegalArgumentException
	{
		final var scannedPath = scannedPackage.replace(ClassFinder.PKG_SEPARATOR, ClassFinder.DIR_SEPARATOR);
		final var scannedUrl  = Thread.currentThread().getContextClassLoader().getResource(scannedPath);
		if (scannedUrl == null)
		{
			throw new IllegalArgumentException(
				String.format(ClassFinder.BAD_PACKAGE_ERROR, scannedPath, scannedPackage));
		}
		final var            scannedDir = new File(scannedUrl.getFile());
		final List<Class<?>> classes    = new ArrayList<>();
		for (final File file : scannedDir.listFiles())
		{
			classes.addAll(ClassFinder.find(file, scannedPackage));
		}
		return classes;
	}

	public static List<Class<?>> getClassesFromList(final List<InternalRobot> iRobots)
	{
		final List<Class<?>> robotClasses = new ArrayList<>();
		for (var i = 0; i < iRobots.size(); i++)
		{
			final var      iRobot     = iRobots.get(i);
			final var      robot      = iRobot.getRobot();
			final Class<?> robotClass = robot.getClass();
			robotClasses.add(robotClass);
		}
		return robotClasses;
	}
}
