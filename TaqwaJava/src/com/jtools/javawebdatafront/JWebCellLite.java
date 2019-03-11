/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.jtools.javawebdatafront;

import java.io.*;
import com.jtools.javawebutils.JWebConstants;

import java.util.*;
import java.awt.*;

public class JWebCellLite implements Serializable, JWebConstants {
	public String originalValue;
	public String currentValue;
	public String userEnteredValue, programEnteredValue;
	public int itemStatus;
	//private JWebExpression expr;
	public Object object;
	public int col, row;
	public Font font;
	public Color textColor, color;
	public String format;
	public int textAlignment = CENTER;
	private JWebDataWindowLite cellRenderer;
	private String name = null;
	private TreeMap<Object,Object> vars = null;

	public JWebCellLite() {
	}

	public void setName(String value) {
		this.name = value;
	}

	public String getName() {
		return name;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public Object getObject() {
		return object;
	}

	public Object getVar(Object key) {
		if (vars == null)
			return null;
		return vars.get(key);
	}


	public void removeVar(Object key) {
		if (vars == null)
			return;
		vars.remove(key);
	}

	public void setVar(Object key, boolean value) {
		setBooleanVar(key, value);
	}

	public void setVar(Object key, int value) {
		setIntVar(key, value);
	}

	public void setVar(Object key, Object value) {
		if (vars == null)
			vars = new TreeMap();
		if (vars.containsKey(key))
			vars.remove(key);
		vars.put(key, value);
	}

	/**
	 * @param key
	 * @return
	 */
	public String getTextVar(Object key) {
		if (vars == null)
			return null;
		return (String) vars.get(key);
	}

	/**
	 * @param key
	 * @param value
	 */
	public void setTextVar(Object key, String value) {
		if (vars == null)
			vars = new TreeMap();
		if (vars.containsKey(key))
			vars.remove(key);
		vars.put(key, value);
	}

	/**
	 * @param key
	 * @return
	 */
	public double getDoubleVar(Object key) {
		if (vars == null)
			return Double.NaN;
		return ((Double) vars.get(key)).doubleValue();
	}

	/**
	 * @param key
	 * @param value
	 */
	public void setDoubleVar(Object key, double value) {
		if (vars == null)
			vars = new TreeMap();
		if (vars.containsKey(key))
			vars.remove(key);
		vars.put(key, new Double(value));
	}

	/**
	 * @param key
	 * @return
	 */
	public int getIntVar(Object key) {
		if (vars == null)
			return Integer.MIN_VALUE;
		Integer varValue = (Integer) vars.get(key);
		if (varValue == null)
			return Integer.MIN_VALUE;
		return varValue.intValue();
	}

	/**
	 * @param key
	 * @param value
	 */
	public void setIntVar(Object key, int value) {
		if (vars == null)
			vars = new TreeMap();
		if (vars.containsKey(key))
			vars.remove(key);
		vars.put(key, new Integer(value));
	}

	/**
	 * @param key
	 * @return
	 */
	public boolean getBooleanVar(Object key) {
		if (vars == null)
			return false;
		Boolean varValue = (Boolean) vars.get(key);
		if (varValue == null)
			return false;
		return varValue.booleanValue();
	}

	/**
	 * @param key
	 * @param value
	 */
	public void setBooleanVar(Object key, boolean value) {
		if (vars == null)
			vars = new TreeMap();
		if (vars.containsKey(key))
			vars.remove(key);
		vars.put(key, new Boolean(value));
	}

	public void setValue(String value) {
		currentValue = value;
	}

	public String getValue() {
		return currentValue;
	}

	public void setCellRenderer(JWebDataWindowLite cellRenderer) {
		this.cellRenderer = cellRenderer;
	}

	public JWebDataWindowLite getCellRenderer() {
		return cellRenderer;
	}

    public TreeMap<Object,Object> getVars() {
        return vars;
    }



}
