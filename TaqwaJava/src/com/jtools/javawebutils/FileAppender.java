package com.jtools.javawebutils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Appends log statements to a file
 * 
 * @author Bruce Blackshaw
 * @version $Revision: 1.6 $
 */
public class FileAppender implements Appender {

	/**
	 * Revision control id
	 */
	public final static String cvsId = "@(#)$Id: FileAppender.java,v 1.6 2007/04/26 04:21:47 hans Exp $";

	/**
	 * Destination
	 */
	private PrintWriter log;

	/**
	 * Path of logging file.
	 */
	private String file;

	/**
	 * Constructor
	 * 
	 * @param file
	 *            file to log to
	 * @throws IOException
	 */
	public FileAppender(String file) throws IOException {
		log = new PrintWriter(new FileWriter(file, true), true);
		this.file = file;
	}

	/**
	 * Log a message
	 * 
	 * @param msg
	 *            message to log
	 */
	public synchronized void log(String msg) {
		log.println(msg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enterprisedt.util.debug.Appender#log(java.lang.Throwable)
	 */
	public synchronized void log(Throwable t) {
		t.printStackTrace(log);
		log.println();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enterprisedt.util.debug.Appender#close()
	 */
	public synchronized void close() {
		log.flush();
		log.close();
	}

	/**
	 * Returns the path of the logging file.
	 * 
	 * @return the path of the logging file.
	 */
	public String getFile() {
		return file;
	}
}
