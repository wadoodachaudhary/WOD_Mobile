package com.jtools.javawebutils;

/**
 * Title: Description: Copyright: Copyright (c) 2002 Company:
 * 
 * @author
 * @version 1.0
 */

public class JWebErrorLog {

	public JWebErrorLog() {
	}

	public static void printErr(Object obj, String message) {
		System.err.println("Error: (" + JWebUtils.getTodaysTime() + ") CLASS:" + obj.getClass().getName() + "[" + message + "]");
	}
}