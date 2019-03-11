package com.jtools.javawebdatafront;

import java.util.*;
import java.io.*;

public class DataWindowRows implements Serializable {
	protected ArrayList rows = new ArrayList();

	public int size() {
		return rows.size();
	}

	public Object get(int index) {
		return rows.get(index);
	}

	public ArrayList getRows() {
		return rows;
	}

	public void clear() {
		rows.clear();
	}

	public void add(Object item) {
		rows.add(item);
	}

	public void remove(int index) {
		rows.remove(index);
	}

}