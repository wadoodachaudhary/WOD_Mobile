/*

 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.jtools.javawebutils;

import java.io.*;
import java.util.Vector;

public class JWebStringList {
	public static final char SET[] = { '\001', '\002', '\003', '\004', '\005', '\006', '\007', '\b', '\t', '\n', '\013', '\f', '\r', '\016', '\017', '\020', '\021', '\022', '\023', '\024', '\025',
			'\026', '\027', '\030', '\031', '\032', '\033', '\034', '\035', '\036', '\037', ' ' };
	private Vector vector;
	private Vector ObjectVector;
	private boolean duplicate;
	private boolean sorted;

	public JWebStringList() {
		vector = new Vector();
		ObjectVector = new Vector();
		duplicate = true;
		sorted = false;
	}

	public int add(String s) {
		return addObject(s, null);
	}

	public int addSorted(String s, Object o) {
		for (int i = 0; i < vector.size(); i++) {
			String string = (String) vector.get(i);
			if (s.compareTo(string) < 0) {
				vector.insertElementAt(s, i);
				ObjectVector.insertElementAt(o, i);
				return i;
			}
		}
		vector.add(s);
		ObjectVector.add(o);
		return vector.size() - 1;
	}

	public int addObject(String s, Object o) {
		if (!duplicate) {
			if (indexOf(s) == -1) {
				if (!sorted) {
					vector.add(s);
					ObjectVector.add(o);
					return vector.size() - 1;
				} else {
					return addSorted(s, o);
				}
			} else {
				return indexOf(s);
			}
		}
		if (!sorted) {
			vector.add(s);
			ObjectVector.add(o);
			return vector.size() - 1;
		} else {
			return addSorted(s, o);
		}
	}

	public int indexOfName(String s) {
		String name = "";
		for (int i = 0; i < vector.size(); i++) {
			String str;
			try {
				str = (String) vector.elementAt(i);
			} catch (ClassCastException e) {
				continue;
			}
			if (str.indexOf(s + "=") != -1) {
				name = str;
				return i;
			}
		}
		return -1;
	}

	public int indexOf(String string) {
		for (int i = 0; i < vector.size(); i++) {
			if (((String) vector.get(i)).equalsIgnoreCase(string)) {
				return i;
			}
		}
		JWebStringList s = new JWebStringList();
		for (int i = 0; i < vector.size(); i++) {
			s.add((String) vector.get(i));
		}
		return -1;
	}

	public String SetString(JWebPChar buffer, int len) {
		return buffer.getString(len);
	}

	public void set(int i, String s) {
		vector.setElementAt(s, i);
	}

	public void setObject(int i, Object o) {
		try {
			ObjectVector.setElementAt(o, i);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("There is no element with this index number.");
		}
	}

	public Object getObjects(int i) {
		return ObjectVector.elementAt(i);
	}

	public Object getObjects(String s) {
		return ObjectVector.elementAt(indexOf(s));
	}

	public String get(int i) {
		return (String) vector.elementAt(i);
	}

	public void clear() {
		vector.clear();
		ObjectVector.clear();
	}

	public int getCount() {
		return vector.size();
	}

	public void setSortted(boolean value) {
		if (value) {
			sorted = true;
			for (int i = 0; i < vector.size(); i++) {
				String s = (String) vector.elementAt(i);
				for (int j = i + 1; j < vector.size(); j++) {
					String ss = (String) vector.elementAt(j);
					if (ss.compareTo(s) < 0) {
						Object objs = ObjectVector.elementAt(i);
						Object objss = ObjectVector.elementAt(j);
						vector.setElementAt(ss, i);
						ObjectVector.setElementAt(objss, i);
						vector.setElementAt(s, j);
						ObjectVector.setElementAt(objs, j);
						s = ss;
					}
				}
			}
		} else {
			sorted = false;
		}
	}

	public void setIntSort() {
		for (int i = 0; i < vector.size(); i++) {
			int s = (new Integer((String) vector.elementAt(i))).intValue();
			for (int j = i + 1; j < vector.size(); j++) {
				int ss = (new Integer((String) vector.elementAt(j))).intValue();
				if (ss < s) {
					Object objs = ObjectVector.elementAt(i);
					Object objss = ObjectVector.elementAt(j);
					vector.setElementAt((new Integer(ss)).toString(), i);
					ObjectVector.setElementAt(objss, i);
					vector.setElementAt((new Integer(s)).toString(), j);
					ObjectVector.setElementAt(objs, j);
					s = ss;
				}
			}
		}
	}

	public void setDuplicate(boolean d) {
		if (!d && duplicate != d) {
			duplicate = d;
			for (int i = 0; i < vector.size(); i++) {
				String s = (String) vector.elementAt(i);
				for (int j = i + 1; j < vector.size(); j++) {
					String ss = (String) vector.elementAt(j);
					if (ss.equals(s)) {
						vector.removeElementAt(j);
						ObjectVector.removeElementAt(j);
					}
				}
			}
		} else if (d && duplicate != d) {
			duplicate = d;
		}
	}

	public boolean getDuplicate() {
		return duplicate;
	}

	public String SetString(String s, int len) {
		return s.substring(0, len);
	}

	public boolean SaveToFile(String FileName) {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(FileName)));
			for (int i = 0; i < vector.size(); i++) {
				out.println((String) vector.get(i));
			}
			out.flush();
			out.close();
			boolean flag = true;
			return flag;
		} catch (IOException e) {
			System.out.println("IOException occured while writing to the file(" + FileName + "): " + e);
		}
		return false;
	}

	public void LoadFromFile(String FileName) {
		try {
			BufferedReader in = new BufferedReader(new FileReader(FileName));
			for (String line = null; (line = in.readLine()) != null;) {
				vector.add(line);
			}
			in.close();
		} catch (FileNotFoundException e) {
			System.out.println("File(" + FileName + ") not found: " + e);
		} catch (IOException e) {
			System.out.println("IOException occured while reading file(" + FileName + "): " + e);
		}
	}

	public void removeString(int index) {
		vector.remove(index);
	}

	public String getCommaText() {
		String result = "";
		int count = vector.size();
		if (count == 0) {
			return "";
		}
		for (int i = 0; i < count; i++) {
			String s = get(i);
			int length = s.length();
			if (length == 0) {
				result = result + ",";
				continue;
			}
			int p = 0;
			try {
				while (!in(SET, s.charAt(p)) && s.charAt(p) != '"' && s.charAt(p) != ',') {
					p++;
				}
			} catch (Exception exception) {
			}
			if (p < length) {
				s = AnsiQuotedStr(s, '"');
			}
			result = result + (s + ",");
		}
		return result.substring(0, result.length() - 1);
	}

	public static String AnsiQuotedStr(String s, char c) {
		StringBuffer stringBuffer = new StringBuffer(s);
		int j = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == c) {
				stringBuffer.insert(i + j, c);
				j++;
			}
		}
		int k = 0;
		do {
			if (k >= s.length()) {
				break;
			}
			if (in(SET, s.charAt(k)) || s.charAt(k) == ',') {
				stringBuffer.insert(0, c);
				stringBuffer.append(c);
				break;
			}
			k++;
		} while (true);
		return stringBuffer.toString();
	}

	public void setCommaText(String value) {
		if (value.length() == 0 || value.equals("") || value == null) {
			return;
		}
		clear();
		int length = value.length();
		int p;
		for (p = 0; in(SET, value.charAt(p)); p++) {
		}
		do {
			if (p >= length) {
				break;
			}
			char ch = getChar(value, p);
			if (ch == '\0') {
				break;
			}
			String s;
			if (ch == '"') {
				JWebPChar P = new JWebPChar(value.substring(p));
				s = AnsiExtractQuotedStr(P, '"');
				p += P.getRealPos();
			} else {
				int p1 = p;
				ch = getChar(value, p);
				if (ch == '\0') {
					break;
				}
				for (; ch > ' ' && ch != ','; ch = getChar(value, p)) {
					p++;
				}
				s = SetString(value.substring(p1), p - p1);
			}
			add(s);
			ch = getChar(value, p);
			if (ch == '\0') {
				break;
			}
			while (in(SET, ch)) {
				p++;
			}
			ch = getChar(value, p);
			if (ch == '\0') {
				break;
			}
			if (ch == ',') {
				do {
					p++;
					ch = getChar(value, p);
				} while (ch != '\0' && in(SET, ch));
			}
		} while (true);
	}

	private static boolean in(char cArray[], char c) {
		for (int i = 0; i < cArray.length; i++) {
			if (cArray[i] == c) {
				return true;
			}
		}
		return false;
	}

	public String names(int i) {
		try {
			String s = (String) vector.elementAt(i);
			System.out.println("the value of name is: " + s);
			int index = s.indexOf('=');
			if (index != -1) {
				String s1 = s.substring(0, index);
				return s1;
			}
		} catch (ClassCastException classcastexception) {
		}
		return "";
	}

	public void setValue(String name, String value) {
		int i;
		if ((i = indexOfName(name)) != -1) {
			vector.set(i, name + "=" + value);
		} else {
			vector.add(name + "=" + value);
		}
	}

	public String value(String s) {
		String name = "";
		for (int i = 0; i < vector.size(); i++) {
			String str;
			try {
				str = (String) vector.elementAt(i);
			} catch (ClassCastException e) {
				continue;
			}
			if (str.indexOf(s + "=") == -1) {
				continue;
			}
			name = str;
			break;
		}
		String value = "";
		int index = name.indexOf('=');
		if (index != -1) {
			value = name.substring(index + 1);
		}
		return value.trim();
	}

	private char getChar(String s, int pos) {
		if (pos < s.length()) {
			return s.charAt(pos);
		} else {
			return '\0';
		}
	}

	public String AnsiExtractQuotedStr(JWebPChar Src, char Quote) {
		JWebPChar realSrc = Src;
		String Result = "";
		try {
			if (Src.getPos() == -1 || Src.getCurrentChar() != Quote) {
				String s1 = Result;
				return s1;
			}
			Src.inc();
			int DropCount = 1;
			JWebPChar P = (JWebPChar) Src.clone();
			Src = AnsiStrScan(Src, Quote);
			do {
				if (Src.getPos() == -1) {
					break;
				}
				Src.inc();
				if (Src.getCurrentChar() != Quote) {
					break;
				}
				Src.inc();
				DropCount++;
				Src = AnsiStrScan(Src, Quote);
			} while (true);
			if (Src.getPos() == -1) {
				Src = StrEnd(P);
			}
			if (Src.getRealPos() - P.getRealPos() <= 1) {
				String s2 = Result;
				return s2;
			}
			if (DropCount == 1) {
				Result = SetString(P, Src.getRealPos() - P.getRealPos() - 1);
			} else {
				StringBuffer s = new StringBuffer(Result);
				Result = s.toString();
				JWebPChar Dest = new JWebPChar(Result);
				Src = AnsiStrScan(P, Quote);
				do {
					if (Src.getPos() == -1) {
						break;
					}
					Src.inc();
					if (Src.getCurrentChar() != Quote) {
						break;
					}
					Move(P, Dest, Src.getRealPos() - P.getRealPos());
					Result = Result + Dest.getString();
					Dest.inc(Src.getRealPos() - P.getRealPos());
					Src.inc();
					P = (JWebPChar) Src.clone();
					Src = AnsiStrScan(Src, Quote);
				} while (true);
				if (Src.getPos() == -1) {
					Src = StrEnd(P);
				}
				Move(P, Dest, Src.getRealPos() - P.getRealPos() - 1);
				Result = Result + Dest.getString();
			}
		} catch (Exception e) {
			System.out.println("Exception in AnsiExtractQuotedStr: " + e.toString());
			e.printStackTrace();
		} finally {
			realSrc.realPos = Src.realPos;
		}
		return Result;
	}

	public JWebPChar AnsiStrScan(JWebPChar src, char ch) {
		JWebPChar temp = new JWebPChar();
		temp.setString(src.getString());
		temp.setPos(src.getString().indexOf(ch));
		temp.setRealPos(src.getRealPos());
		return temp;
	}

	public JWebPChar StrEnd(JWebPChar p) {
		JWebPChar temp = new JWebPChar();
		temp.setString(p.getFullString());
		if (p.getString().length() == 0 || p.getString().equals("")) {
			temp.setPos(-1);
			temp.setRealPos(p.getRealPos() + 1);
		} else {
			temp.setPos(p.getFullString().length() - 1);
		}
		return temp;
	}

	public void Move(JWebPChar Source, JWebPChar Dest, int Count) {
		int num1 = Source.getPos();
		int num2 = Dest.getPos();
		Dest.setString(Source.getString(num1, (num1 + Count) - 1), num2, (num2 + Count) - 1);
	}
}
