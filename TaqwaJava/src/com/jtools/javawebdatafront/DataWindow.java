package com.jtools.javawebdatafront;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public interface DataWindow {
	public void setColumnCount(int newColumnCount);

	public void setRowCount(int newRowCount);

	public void setColumnName(int colNo, String colName);

	public void setColumnHeader(int colNo, String colName);

	public String getItem(int atRow, int atCol);

	public String getItem(int atRow, String atColName);

	public int getColumnIndex(String colName);

	public String getColumnName(int colNum);

	public int getObjectType();

	public int getColumnCount();

	public int getRowCount();

	public void setCellItem(int atRow, int atCol, String itemValue);
}