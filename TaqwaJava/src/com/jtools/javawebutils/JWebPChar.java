package com.jtools.javawebutils;

public class JWebPChar implements Cloneable {
	String string = null;
	int pos = -1;
	int realPos = 0;

	// -------------------------------------------------------------------PChar
	public JWebPChar() {
	}

	// -------------------------------------------------------------------PChar
	public JWebPChar(String s) {
		if (s != null) {
			string = s;
			pos = 0;
		}
	}

	// ---------------------------------------------------------------------clone
	public Object clone() throws CloneNotSupportedException {
		JWebPChar temp = new JWebPChar();
		temp.string = this.string;
		temp.pos = this.pos;
		temp.realPos = this.realPos;
		return temp;
	}

	// ------------------------------------------------------------getCurrentChar
	public char getCurrentChar() {
		char ch = 0;
		if (pos < string.length())
			ch = string.charAt(pos);
		return ch;
	}

	// -----------------------------------------------------------------------inc
	public void inc() {
		pos++;
		string = string.substring(pos);
		if (realPos < pos)
			realPos = pos;
		else
			realPos++;
		pos = 0;
	}

	// -----------------------------------------------------------------------inc
	public void inc(int num) {
		pos += num;
		string = string.substring(pos);
		if (realPos < pos)
			realPos = pos;
		else
			realPos++;

		pos = 0;
	}

	// --------------------------------------------------------------------setPos
	public void setPos(int i) {

		pos = i;
		realPos = pos;
		if (pos == -1)
			string = "";
		else {
			string = string.substring(pos);
			pos = 0;
		}
	}

	// --------------------------------------------------------------------getPos
	public int getPos() {
		if (pos > string.length())
			return -1;
		return pos;
	}

	// ----------------------------------------------------------------getRealPos
	public int getRealPos() {
		return realPos;
	}

	// ----------------------------------------------------------------setRealPos
	public void setRealPos(int i) {
		realPos += i;
	}

	// -----------------------------------------------------------------getString
	public String getString(int pos) {
		return string.substring(this.pos, pos);
	}

	// -------------------------------------------------------------getFullString
	public String getFullString() {
		return string;
	}

	// -----------------------------------------------------------------getString
	public String getString() {
		String s = "";
		if (pos != -1 && pos < string.length())
			s = string.substring(pos);
		return s;
	}

	// -----------------------------------------------------------------setString
	public void setString(String s) {
		string = s;
	}

	// -----------------------------------------------------------------setString
	public void setString(String s, int beginIndex, int endIndex) {
		string = s.substring(beginIndex, endIndex + 1);
	}

	// -----------------------------------------------------------------getString
	public String getString(int beginIndex, int endIndex) {
		return string.substring(beginIndex, endIndex + 1);
	}

}
