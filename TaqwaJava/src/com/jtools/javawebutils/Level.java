package com.jtools.javawebutils;

/**
 * Simple debug level class. Uses the same interface (but not implementation) as
 * log4j, so that the debug classes could be easily replaced by log4j (by
 * changing imports - too dangerous to rely on class path and using the same
 * package names)
 * 
 * @author Bruce Blackshaw
 * @version $Revision: 1.7 $
 */
public class Level {

	/**
	 * Revision control id
	 */
	final public static String cvsId = "@(#)$Id: Level.java,v 1.7 2006/10/11 08:43:54 hans Exp $";

	final static int OFF_INT = -1;

	final private static String OFF_STR = "OFF";

	final static int FATAL_INT = 0;

	final private static String FATAL_STR = "FATAL";

	final static int ERROR_INT = 1;

	final private static String ERROR_STR = "ERROR";

	final static int WARN_INT = 2;

	final private static String WARN_STR = "WARN";

	final static int INFO_INT = 3;

	final private static String INFO_STR = "INFO";

	final static int DEBUG_INT = 4;

	final private static String DEBUG_STR = "DEBUG";

	final static int ALL_INT = 10;

	final private static String ALL_STR = "ALL";

	final static int LEVEL_COUNT = 5;

	/**
	 * Off level
	 */
	final public static Level OFF = new Level(OFF_INT, OFF_STR);

	/**
	 * Fatal level
	 */
	final public static Level FATAL = new Level(FATAL_INT, FATAL_STR);

	/**
	 * OFF level
	 */
	final public static Level ERROR = new Level(ERROR_INT, ERROR_STR);

	/**
	 * Warn level
	 */
	final public static Level WARN = new Level(WARN_INT, WARN_STR);

	/**
	 * Info level
	 */
	final public static Level INFO = new Level(INFO_INT, INFO_STR);

	/**
	 * Debug level
	 */
	final public static Level DEBUG = new Level(DEBUG_INT, DEBUG_STR);

	/**
	 * All level
	 */
	final public static Level ALL = new Level(ALL_INT, ALL_STR);

	/**
	 * The level's integer value
	 */
	private int level = OFF_INT;

	/**
	 * The level's string representation
	 */
	private String string;

	/**
	 * Private constructor so no-one outside the class can create any more
	 * instances
	 * 
	 * @param level
	 *            level to set this instance at
	 * @param string
	 *            string representation
	 */
	private Level(int level, String string) {
		this.level = level;
		this.string = string;
	}

	/**
	 * Get integer log level
	 * 
	 * @return log level
	 */
	int getLevel() {
		return level;
	}

	/**
	 * Is this level greater or equal to the supplied level
	 * 
	 * @param l
	 *            level to test against
	 * @return true if greater or equal to, false if less than
	 */
	boolean isGreaterOrEqual(Level l) {
		if (this.level >= l.level)
			return true;
		return false;
	}

	/**
	 * Get level from supplied string
	 * 
	 * @param level
	 *            level as a string
	 * @return level object or null if not found
	 */
	public static Level getLevel(String level) {
		if (OFF.toString().equalsIgnoreCase(level))
			return OFF;
		if (FATAL.toString().equalsIgnoreCase(level))
			return FATAL;
		if (ERROR.toString().equalsIgnoreCase(level))
			return ERROR;
		if (WARN.toString().equalsIgnoreCase(level))
			return WARN;
		if (INFO.toString().equalsIgnoreCase(level))
			return INFO;
		if (DEBUG.toString().equalsIgnoreCase(level))
			return DEBUG;
		if (ALL.toString().equalsIgnoreCase(level))
			return ALL;
		return null;
	}

	/**
	 * String representation
	 * 
	 * @return string
	 */
	public String toString() {
		return string;
	}

}
