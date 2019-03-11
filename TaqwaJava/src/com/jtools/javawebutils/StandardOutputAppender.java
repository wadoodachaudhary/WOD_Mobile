package com.jtools.javawebutils;

import java.io.PrintWriter;

/**
 * Appends log statements to standard output
 * 
 * @author Bruce Blackshaw
 * @version $Revision: 1.4 $
 */
public class StandardOutputAppender implements Appender {

	/**
	 * Revision control id
	 */
	public static String cvsId = "@(#)$Id: StandardOutputAppender.java,v 1.4 2006/10/12 12:38:58 bruceb Exp $";

	/**
	 * Destination
	 */
	private PrintWriter log = new PrintWriter(System.out);

	/**
	 * Constructor
	 */
	public StandardOutputAppender() {
	}

	/**
	 * Log a message
	 * 
	 * @param msg
	 *            message to log
	 */
	public synchronized void log(String msg) {
		log.println(msg);
		log.flush();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enterprisedt.util.debug.Appender#log(java.lang.Throwable)
	 */
	public synchronized void log(Throwable t) {
		t.printStackTrace(log);
		log.flush();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enterprisedt.util.debug.Appender#close()
	 */
	public synchronized void close() {
		log.flush();
	}
}
