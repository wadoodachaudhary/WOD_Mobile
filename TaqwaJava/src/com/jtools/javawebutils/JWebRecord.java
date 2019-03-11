package com.jtools.javawebutils;

import java.util.ArrayList;

/**
 * Title: Description: Copyright: Copyright (c) 2002 Company:
 * 
 * @author
 * @version 1.0
 */

public class JWebRecord {
	public String f1, f2, f3, f4, f5;
	public int i1, i2, i3, i4, i5;

	public JWebRecord(String f1, String f2) {
		this.f1 = f1;
		this.f2 = f2;
	}

	public JWebRecord(String f1, String f2, String f3) {
		this.f1 = f1;
		this.f2 = f2;
		this.f3 = f3;
	}

	public JWebRecord(String f1, String f2, String f3, String f4) {
		this.f1 = f1;
		this.f2 = f2;
		this.f3 = f3;
		this.f4 = f4;
	}

	public JWebRecord(String f1, String f2, int i1, int i2) {
		this.f1 = f1;
		this.f2 = f2;
		this.i1 = i1;
		this.i2 = i2;
	}

}