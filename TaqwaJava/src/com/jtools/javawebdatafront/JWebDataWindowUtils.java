package com.jtools.javawebdatafront;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */


import com.jtools.javawebutils.JWebConstants;

public class JWebDataWindowUtils implements JWebConstants {

	public JWebDataWindowUtils() {
	}

	public static DataWindow getTranspose(DataWindow dw, String firstCol) {
		DataWindow dwTranspose;
		dwTranspose = new JWebDataWindowLite();
		dwTranspose.setColumnCount(dw.getRowCount() + 1);
		dwTranspose.setRowCount(dw.getColumnCount() - 1);
		dwTranspose.setColumnName(0, firstCol);
		dwTranspose.setColumnHeader(0, firstCol);
		for (int r = 0; r < dw.getRowCount(); r++) {
			dwTranspose.setColumnName(r + 1, dw.getItem(r, 0));
			dwTranspose.setColumnHeader(r + 1, dw.getItem(r, 0));
		}
		for (int c = 1; c < dw.getColumnCount(); c++) {
			dwTranspose.setCellItem(c - 1, 0, dw.getColumnName(c));
		}
		for (int r = 0; r < dw.getRowCount(); r++) {
			for (int c = 1; c < dw.getColumnCount(); c++) {
				dwTranspose.setCellItem(c - 1, r + 1, dw.getItem(r, c));
			}
		}
		return dwTranspose;

	}
}