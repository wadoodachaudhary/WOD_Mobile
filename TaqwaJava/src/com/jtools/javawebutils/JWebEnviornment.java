package com.jtools.javawebutils;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import java.util.*;
import java.io.*;
import java.sql.*;

public class JWebEnviornment {

	private static boolean mode = true;
	private static String absFilePath = "c:\\";
	private static String relFilePath = "";
	private static HashMap persistentFiles;
	private static boolean checkPersist = false;
	public static final int PERSIST_NEVER = 0;
	public static final int PERSIST_ALWAYS = 1;
	public static final int PERSIST_IF_LISTED = 2;
	public static final int PERSIST_IF_EXISTS = 3;
	private static int filePersistenceTolerance = PERSIST_NEVER;
	private static String nextURL = "";
	private static String baseURL = "";
	private static String tempDir = "images";

	public JWebEnviornment() {
	}

	public static boolean getGraphicMode() {
		return mode;
	}

	public static void setGraphicMode(boolean _mode) {
		mode = _mode;
	}

	public static String getAbsolutePath() {
		return absFilePath;
	}

	public static void setAbsolutePath(String aFilePath) {
		absFilePath = aFilePath;
	}

	public static String getNextURL() {
		return nextURL;
	}

	public static void setNextURL(String value) {
		nextURL = value;
	}

	public static String getBaseURL() {
		return baseURL;
	}

	public static void setBaseURL(String value) {
		baseURL = value;
	}

	public static String getRelativePath() {
		return relFilePath;
	}

	public static void setRelativePath(String rFilePath) {
		relFilePath = rFilePath;
	}

	public static void addPersistentImage(String fileName) {
		if (persistentFiles == null) {
			persistentFiles = new HashMap();
		}
		if (!persistentFiles.containsKey(fileName)) {
			persistentFiles.put(fileName, fileName);
		}
	}

	public static boolean isImagePersistent(String fileName) {
		if (!checkPersist)
			return false;
		if (getFilePersistenceTolerance() == PERSIST_ALWAYS)
			return true;
		if (getFilePersistenceTolerance() == PERSIST_NEVER)
			return false;
		if (getFilePersistenceTolerance() == PERSIST_IF_LISTED) {
			if (getFilePersistenceTolerance() == PERSIST_IF_LISTED) {
				if (persistentFiles == null)
					return false;
				String filePresent = (String) persistentFiles.get(fileName);
				if (filePresent == null) {
					return false;
				}
				String fullFileName = JWebEnviornment.getAbsolutePath() + JWebEnviornment.getRelativePath() + File.separator + fileName;
				File file = new File(fullFileName);
				if (file.exists())
					return true;
				else
					return false;
			}
		}
		if (getFilePersistenceTolerance() == PERSIST_IF_EXISTS) {
			String fullFileName = JWebEnviornment.getAbsolutePath() + JWebEnviornment.getRelativePath() + File.separator + fileName;
			File file = new File(fullFileName);
			if (file.exists())
				return true;
			else {
				// System.out.println("");
				return false;
			}
		}
		return false;
	}

	public static void checkForPersistence(boolean check) {
		checkPersist = check;
	}

	public static void setFilePersistenceTolerance(int tol) {
		filePersistenceTolerance = tol;
	}

	public static int getFilePersistenceTolerance() {
		return filePersistenceTolerance;
	}

	private static String cacheFile = null;
	private static String cacheDir = null;
	private static String cacheFileName = null;
	private static Connection cacheConnection = null;

	public static Connection getCacheConnection() {
		return cacheConnection;
	}

	public static void setCacheConnection(Connection cacheCon) {
		cacheConnection = cacheCon;
	}

	public static String getCacheFile() {
		return cacheFile;
	}

	public static String getCacheDir() {
		return cacheDir;
	}

	public static void setCache(String _cacheDir, String _cacheFile) throws Exception {
		cacheFile = _cacheFile;
		cacheDir = _cacheDir;
	}

}