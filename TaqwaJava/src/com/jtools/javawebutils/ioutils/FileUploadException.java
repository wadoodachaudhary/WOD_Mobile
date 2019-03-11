package com.jtools.javawebutils.ioutils;

/**
 * Exception for errors encountered while processing the request.
 * 
 * @author <a href="mailto:jmcnally@collab.net">John McNally</a>
 * @version $Id: FileUploadException.java,v 1.7 2003/04/27 17:30:06 martinc Exp $
 */
public class FileUploadException extends Exception {

	/**
	 * Constructs a new <code>FileUploadException</code> without message.
	 */
	public FileUploadException() {
	}

	/**
	 * Constructs a new <code>FileUploadException</code> with specified detail
	 * message.
	 * 
	 * @param msg
	 *            the error message.
	 */
	public FileUploadException(String msg) {
		super(msg);
	}
}
