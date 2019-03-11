package com.jtools.javawebutils;

/**
 * Interface for classes that output log statements
 * 
 * @author Bruce Blackshaw
 * @version $Revision: 1.1 $
 */
public interface Appender {

	/**
	 * Revision control id
	 */
	public static String cvsId = "@(#)$Id: Appender.java,v 1.1 2004/05/01 16:55:41 bruceb Exp $";

	/**
	 * Close this appender
	 */
	public void close();

	/**
	 * Log a message
	 * 
	 * @param msg
	 *            message to log
	 */
	public void log(String msg);

	/**
	 * Log a stack trace
	 * 
	 * @param t
	 *            throwable object
	 */
	public void log(Throwable t);
}
