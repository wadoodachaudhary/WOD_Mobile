package com.jtools.javawebutils;

public class JWebSize {

	private static int[] emptyArray = new int[0];
	private int a[];

	public JWebSize() {
		a = emptyArray;
	}

	public JWebSize(int numEntries) {
		this(numEntries, 0);
	}

	public JWebSize(int numEntries, int value) {
		this();
		insertEntries(0, numEntries, value);
	}

	public JWebSize(int[] sizes) {
		this();
		setSizes(sizes);
	}

	public void setSizes(int[] sizes) {
		if (a.length != sizes.length) {
			a = new int[sizes.length];
		}
		setSizes(0, a.length, sizes);
	}

	private int setSizes(int from, int to, int[] sizes) {
		if (to <= from) {
			return 0;
		}
		int m = (from + to) / 2;
		a[m] = sizes[m] + setSizes(from, m, sizes);
		return a[m] + setSizes(m + 1, to, sizes);
	}

	/**
	 * Returns the size of all entries.
	 * 
	 * @return a new array containing the sizes in this object
	 */
	public int[] getSizes() {
		int n = a.length;
		int[] sizes = new int[n];
		getSizes(0, n, sizes);
		return sizes;
	}

	private int getSizes(int from, int to, int[] sizes) {
		if (to <= from) {
			return 0;
		}
		int m = (from + to) / 2;
		sizes[m] = a[m] - getSizes(from, m, sizes);
		return a[m] + getSizes(m + 1, to, sizes);
	}

	/**
	 * Returns the start position for the specified entry. For example,
	 * <code>getPosition(0)</code> returns 0, <code>getPosition(1)</code> is
	 * equal to <code>getSize(0)</code>, <code>getPosition(2)</code> is
	 * equal to <code>getSize(0)</code> + <code>getSize(1)</code>, and so
	 * on.
	 * 
	 * @param index
	 *            the index of the entry whose position is desired
	 * @return the starting position of the specified entry
	 */
	public int getPosition(int index) {
		return getPosition(0, a.length, index);
	}

	private int getPosition(int from, int to, int index) {
		if (to <= from) {
			return 0;
		}
		int m = (from + to) / 2;
		if (index <= m) {
			return getPosition(from, m, index);
		} else {
			return a[m] + getPosition(m + 1, to, index);
		}
	}

	/**
	 * Returns the index of the entry that corresponds to the specified
	 * position. For example, <code>getIndex(0)</code> is 0, since the first
	 * entry always starts at position 0.
	 * 
	 * @param position
	 *            the position of the entry
	 * @return the index of the entry that occupies the specified position
	 */
	public int getIndex(int position) {
		return getIndex(0, a.length, position);
	}

	private int getIndex(int from, int to, int position) {
		if (to <= from) {
			return from;
		}
		int m = (from + to) / 2;
		int pivot = a[m];
		if (position < pivot) {
			return getIndex(from, m, position);
		} else {
			return getIndex(m + 1, to, position - pivot);
		}
	}

	/**
	 * Returns the size of the specified entry.
	 * 
	 * @param index
	 *            the index corresponding to the entry
	 * @return the size of the entry
	 */
	public int getSize(int index) {
		return getPosition(index + 1) - getPosition(index);
	}

	/**
	 * Sets the size of the specified entry.
	 * 
	 * @param index
	 *            the index corresponding to the entry
	 * @param size
	 *            the size of the entry
	 */
	public void setSize(int index, int size) {
		changeSize(0, a.length, index, size - getSize(index));
	}

	private void changeSize(int from, int to, int index, int delta) {
		if (to <= from) {
			return;
		}
		int m = (from + to) / 2;
		if (index <= m) {
			a[m] += delta;
			changeSize(from, m, index, delta);
		} else {
			changeSize(m + 1, to, index, delta);
		}
	}

	/**
	 * Adds a contiguous group of entries to this <code>SizeSequence</code>.
	 * 
	 * @param start
	 *            the index to be assigned to the first entry in the group
	 * @param length
	 *            the number of entries in the group
	 * @param value
	 *            the size to be assigned to each new entry
	 */
	public void insertEntries(int start, int length, int value) {
		int sizes[] = getSizes();
		int end = start + length;
		int n = a.length + length;
		a = new int[n];
		for (int i = 0; i < start; i++) {
			a[i] = sizes[i];
		}
		for (int i = start; i < end; i++) {
			a[i] = value;
		}
		for (int i = end; i < n; i++) {
			a[i] = sizes[i - length];
		}
		setSizes(a);
	}

	/**
	 * Removes a contiguous group of entries from this <code>SizeSequence</code>.
	 * 
	 * @param start
	 *            the index of the first entry to be removed
	 * @param length
	 *            the number of entries to be removed
	 */
	public void removeEntries(int start, int length) {
		int sizes[] = getSizes();
		int end = start + length;
		int n = a.length - length;
		a = new int[n];
		for (int i = 0; i < start; i++) {
			a[i] = sizes[i];
		}
		for (int i = start; i < n; i++) {
			a[i] = sizes[i + length];
		}
		setSizes(a);
	}
}
