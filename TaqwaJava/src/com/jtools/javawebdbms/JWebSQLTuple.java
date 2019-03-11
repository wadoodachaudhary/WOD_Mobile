package com.jtools.javawebdbms;

import java.io.*;
import java.util.Vector;
import java.io.*;
import java.util.Enumeration;
import java.util.Vector;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.StringTokenizer;
import java.io.*;
import java.sql.SQLException;
import java.util.*;
import javax.xml.parsers.*;

import com.jtools.javawebdatafront.JWebDataWindowLite;
import com.jtools.javawebutils.JWebKeyword;

public class JWebSQLTuple {

	public JWebSQLTuple() {
		attributes = new Vector();
		values = new Vector();
		searchTable = new Hashtable();
	}

	public JWebSQLTuple(String s) {
		this();
		for (StringTokenizer stringtokenizer = new StringTokenizer(s, ","); stringtokenizer.hasMoreTokens(); setAtt(stringtokenizer.nextToken().trim(), null))
			;
	}


  public JWebSQLTuple(JWebDataWindowLite dw) {
    this();
    for (int i = 0; i < dw.getColumnCount(); i++) {
      setAtt(dw.getColumnName(i).toLowerCase(), null);
    }
  }

	public void setRow(String s) {
		StringTokenizer stringtokenizer = new StringTokenizer(s, ",");
		for (int i = 0; stringtokenizer.hasMoreTokens(); i++) {
			String s1 = stringtokenizer.nextToken().trim();
			try {
				Double double1 = new Double(s1);
				setAtt(getAttName(i), double1);
			} catch (Exception exception) {
				setAtt(getAttName(i), s1);
			}
		}
	}


  public void setRow(JWebDataWindowLite dw, int row) throws RuntimeException {
    for (int i = 0; i < dw.getColumnCount(); i++) {
      String val = dw.getItem(row, dw.getColumnName(i));
      //System.err.println("Tuple:"+dw.getColumnName(i)+"="+val);
      setAtt(dw.getColumnName(i).toLowerCase(), com.jtools.javawebutils.JWebUtils.isNull(val,"").toLowerCase());
    }
  }


	public void setRow(String[] varNames, String[] varValues) throws Exception {
		for (int i = 0; i < varNames.length; i++) {
			setAtt(varNames[i], varValues[i]);
		}
	}

	public void setRow(Vector vector) {
		for (int i = 0; i < vector.size(); i++)
			setAtt(getAttName(i), vector.elementAt(i));
	}

	public void setAtt(String s, Object obj) {
		if (s != null) {
			boolean flag = searchTable.containsKey(s);
			if (flag) {
				int i = ((Integer) searchTable.get(s)).intValue();
				values.setElementAt(obj, i);
			} else {
				int j = attributes.size();
				attributes.addElement(s);
				values.addElement(obj);
				searchTable.put(s, new Integer(j));
			}
		}
	}

	public String getAttName(int i) {
		try {
			return (String) attributes.elementAt(i);
		} catch (ArrayIndexOutOfBoundsException arrayindexoutofboundsexception) {
			return null;
		}
	}

	public int getAttIndex(String s) {
		if (s == null)
			return -1;
		Integer integer = (Integer) searchTable.get(s);
		if (integer != null)
			return integer.intValue();
		else
			return -1;
	}

	public Object getAttValue(int i) {
		try {
			return values.elementAt(i);
		} catch (ArrayIndexOutOfBoundsException arrayindexoutofboundsexception) {
			return null;
		}
	}

	public Object getAttValue(String s) {
		boolean flag = false;
		if (s != null)
			flag = searchTable.containsKey(s);
		if (flag) {
			int i = ((Integer) searchTable.get(s)).intValue();
			return values.elementAt(i);
		} else {
			return null;
		}
	}

	public boolean isAttribute(String s) {
		if (s != null)
			return searchTable.containsKey(s);
		else
			return false;
	}

	public int getNumAtt() {
		return values.size();
	}

	public String toString() {
		StringBuffer stringbuffer = new StringBuffer();
		stringbuffer.append("[");
		if (attributes.size() > 0) {
			Object obj = attributes.elementAt(0);
			String s;
			if (obj == null)
				s = "(null)";
			else
				s = obj.toString();
			Object obj2 = values.elementAt(0);
			String s2;
			if (obj2 == null)
				s2 = "(null)";
			else
				s2 = obj2.toString();
			stringbuffer.append(s + " = " + s2);
		}
		for (int i = 1; i < attributes.size(); i++) {
			Object obj1 = attributes.elementAt(i);
			String s1;
			if (obj1 == null)
				s1 = "(null)";
			else
				s1 = obj1.toString();
			Object obj3 = values.elementAt(i);
			String s3;
			if (obj3 == null)
				s3 = "(null)";
			else
				s3 = obj3.toString();
			stringbuffer.append(", " + s1 + " = " + s3);
		}
		stringbuffer.append("]");
		return stringbuffer.toString();
	}

	private Vector attributes;
	private Vector values;
	private Hashtable searchTable;
}