/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
package com.jtools.javawebdatafront;
import java.io.*;
import java.util.*;
import java.sql.*;

import com.jtools.javawebutils.JWebConstants;
import com.jtools.javawebutils.JWebUtils;


import java.util.Scanner;
public class JWebDataWindowLite implements DataWindow, JWebConstants, Serializable {
  private String sqlQuery = "";
  private String url;
  private String jdbcDriverClassName = JDBC_DRIVER_CLASS_ORACLE;
  private JWebCellLite parentCell;
  protected boolean allowUpdates = true;
  private DataWindowColumnLite key;
  protected boolean keyColumnDefined = false;
  private ArrayList tableColumns = new ArrayList();
  private Object object;
  private int objectType = DATAWINDOW_LITE;
  private ArrayList rows = new ArrayList();
  private String updateTable = "";
  private ArrayList deletedRows;
  private TreeMap colMap = new TreeMap();
  private String currentSQL;
  private String DEFAULT_NAME = "dw_NoNameAssigned";
  private String dwName = DEFAULT_NAME;
  private Connection con = null;
  private boolean metaDataPresent = true;
  private int dateParenthesis = QUOTES;
  private int driver = JDBC_ORACLE;
  private Object[] retrievalArguments;
  private boolean updateFlag = false;
  private int v1, v2, v3;
  private static boolean debugMode = false;
  private ResultSet resultSet = null;
  private Statement statement = null;
  private boolean serverSide = false;
    public static final short AGGREGATE_NONE = 0;
    public static final short AGGREGATE_SUM = 1;
    public static final short AGGREGATE_COUNT = 2;
    public static final short AGGREGATE_MIN = 3;
    public static final short AGGREGATE_MAX = 4;
    public static final short AGGREGATE_AVG = 5;
    public static final short AGGREGATE_UDF = 6;
    public static final int CROSSTAB = 1;
    public static final int CHART = 2;
    public static final int TREE = 3;
    public static final int RULE = 4;
    public static final int MAP = 5;
    public static final int TABBED = 6;
    public static final int FORM = 7;
    public static final int FLOWCHART = 8;
    public static final int MAIL = 9;
    public static final int DECKED = 10;
    public static final int POPUP = 11;
    public static final int STATUS_NEW = 0;
    public static final int STATUS_NOTMODIFIED = 1;
    public static final int STATUS_DATAMODIFIED = 2;
    public static final int STATUS_NEWMODIFIED = 3;
    public static final int STATUS_DELETED = 4;
    public static final int BUFFER_ORIGINAL = 1;
    public static final int BUFFER_CURRENT = 0;
    public static final int BUFFER_DELETE = 2;
    public static final int BUFFER_SCREEN = 2;
    public static final int TRIGGER_PRE_DELETE = 1;
    public static final int TRIGGER_POST_DELETE = 2;
    public static final int TRIGGER_PRE_INSERT = 3;
    public static final int TRIGGER_POST_INSERT = 4;
    public static final int TRIGGER_PRE_UPDATE = 5;
    public static final int TRIGGER_POST_UPDATE = 6;
    public static final int MY_DATABASE = 0;
    public static final int EXTERNAL_DATABASE = 1;
    public static String TYPE_DECIMAL = "DECIMAL";
    public static String TYPE_DOUBLE = "DOUBLE";
    public static String TYPE_INTEGER = "INTEGER";
    public static String TYPE_SMALLINT = "SMALL_INT";
    public static String TYPE_LONG_INTEGER = "LONG_INTEGER";
    public static String TYPE_DATETIME = "DATE_TIME";
    public static String TYPE_TEXT = "STRING";
    public static final int TEMPLATE_PLAIN = 1;

    //private int colCount = 0;

  public JWebDataWindowLite() {
  }

  public JWebDataWindowLite(Connection con) {
    setConnection(con);
  }

  public JWebDataWindowLite(int rowCount, int colCount) {
    for (int i = 0; i < colCount; i++) {
      addColumn("col_" + i, "dbtable", TYPE_CHARACTER, Types.VARCHAR);
    }
    for (int i = 0; i < rowCount; i++) {
      addRow();
    }
  }

  public JWebDataWindowLite(String[] columns) {
    this.setColumns(columns);
  }

  public void setColumnCount(int newColumnCount) {
    int colCount = tableColumns.size();
    if (newColumnCount > colCount) {
      for (int i = colCount; i < newColumnCount; i++) {
        addColumn("col_" + i, "dbtable", TYPE_CHARACTER, Types.VARCHAR);
      }
    } else if (newColumnCount < colCount) {
      for (int i = colCount - 1; i >= newColumnCount; i--) {
        tableColumns.remove(i);
      }
    }
  }

  public void setRowCount(int newRowCount) {
    int rowCount = getRowCount();
    if (newRowCount > getRowCount()) {
      for (int i = rowCount; i < newRowCount; i++) {
        addRow();
      }
    } else if (newRowCount < getRowCount()) {
      for (int i = rowCount - 1; i >= newRowCount; i--) {
        deleteRow(i);
      }
    }
  }





  public static void setDebugMode(boolean inDebugMode) {
    debugMode = inDebugMode;
  }

  /**
   * @return
   */
  public static boolean inDebug() {
    return debugMode;
  }

  public JWebDataWindowLite(JWebDataWindowLite dw) {
    // this.tableColumns = tableColumns;
    cloneColumnStructureLite(dw.tableColumns);
    this.rows = new ArrayList();
    rowsCopyLite(this.rows, dw.rows, 0, dw.rows.size());
    if (dw.deletedRows != null && dw.deletedRows.size() > 0) {
      this.deletedRows = new ArrayList();
      rowsCopyLite(deletedRows, dw.deletedRows, 0, dw.deletedRows.size());
    }
  }

  public JWebDataWindowLite(JWebDataWindowLite dw, int fromRow, int toRow) {
    cloneColumnStructureLite(dw.tableColumns);
    this.rows = new ArrayList();
    rowsCopyLite(this.rows, dw.rows, fromRow, toRow);
  }

  public void setServerSide(boolean serverSide) {
    this.serverSide = serverSide;
  }

  public boolean isServerSide() {
    return serverSide;
  }



  protected class DataWindowColumnLite implements Serializable {
    String dbColumn;
    String dbTable, label;
    char dbType;
    int columnType;
    boolean key, updatable;
    protected Object identifier;
    protected Object object;
    private String name;
    private int index, length;
    boolean nullable, dateTime;
  }

  private void buildColumnIndex() {
    for (int i = 0; i < getColumnCount(); i++) {
      DataWindowColumnLite col = (DataWindowColumnLite)tableColumns.get(i);
      colMap.put(col.dbColumn, new Integer(i));
    }
  }

  protected DataWindowColumnLite getColumn(int colIndex) {
    return (DataWindowColumnLite)tableColumns.get(colIndex);
  }

  protected class DataWindowRowLite implements Serializable {
    ArrayList dataItems = new ArrayList();
    int rowStatus;
    int index;
    String userStamp, timeStamp;
  }


  public void rowsCopyLite(ArrayList rows, ArrayList dwRows, int fromRowNum, int toRowNum) {
    if (dwRows == null || dwRows.size() == 0)
      return;
    if (rows == null)
      rows = new ArrayList();
    DataWindowRowLite toRow;
    DataWindowRowLite fromRow;
    int colCount, iCol;
    JWebCellLite diFrom;
    JWebCellLite diTo;
    colCount = tableColumns.size();
    for (int i = fromRowNum; i < toRowNum; i++) {
      fromRow = (DataWindowRowLite)dwRows.get(i);
      toRow = new DataWindowRowLite();
      rows.add(toRow);
      toRow.rowStatus = fromRow.rowStatus;
      toRow.timeStamp = fromRow.timeStamp;
      toRow.userStamp = fromRow.userStamp;
      for (iCol = 0; iCol < colCount; iCol++) {
        diTo = new JWebCellLite();
        toRow.dataItems.add(diTo);
        diFrom = (JWebCellLite)fromRow.dataItems.get(iCol);
        diTo.currentValue = diFrom.currentValue;
        diTo.originalValue = diTo.currentValue;
        diTo.itemStatus = diFrom.itemStatus;
        diTo.font = diFrom.font;
        diTo.color = diFrom.color;
        diTo.textColor = diFrom.textColor;
        diTo.textAlignment = diFrom.textAlignment;
        diTo.format = diFrom.format;
      }
    }
  }


  public void cloneColumnStructureLite(ArrayList dwTableColumns) {
    tableColumns = new ArrayList();
    String colName;
    for (int i = 0; i < dwTableColumns.size(); i++) {
      DataWindowColumnLite col = new DataWindowColumnLite();
      DataWindowColumnLite colFrom = (DataWindowColumnLite)dwTableColumns.get(i);
      col.dbColumn = colFrom.dbColumn;
      col.dbTable = colFrom.dbTable;
      col.dbType = colFrom.dbType;
      col.columnType = colFrom.columnType;
      col.object = colFrom.object;
      col.key = colFrom.key;
      col.columnType = colFrom.columnType;
      col.updatable = colFrom.updatable;
      col.nullable = colFrom.nullable;
      col.name = colFrom.name;
      colName = col.name == null ? col.dbColumn : col.name;
      colName = colName == null ? "Column_" + i : colName;
      colMap.put(colName.trim(), new Integer(i));
      tableColumns.add(col);
    }
  }



  public Object getRowAt(int row) {
    return rows.get(row);
  }

  public void rowsCopy(JWebDataWindowLite dw, int fromRowNum, int toRowNum) {
    DataWindowRowLite fromRow;
    DataWindowRowLite toRow;
    int colCount, iCol;
    JWebCellLite diTo;
    JWebCellLite diFrom;
    colCount = getColumnCount();
    for (int i = fromRowNum; i < toRowNum; i++) {
      int row = dw.addRow();
      toRow = (DataWindowRowLite)dw.getRowAt(row);
      fromRow = (DataWindowRowLite)getRowAt(i);
      toRow.rowStatus = fromRow.rowStatus;
      toRow.timeStamp = fromRow.timeStamp;
      toRow.userStamp = fromRow.userStamp;
      for (iCol = 0; iCol < colCount; iCol++) {
        diFrom = (JWebCellLite)fromRow.dataItems.get(iCol);
        diTo = dw.getCell(row, getColumnName(iCol));
        diTo.currentValue = diFrom.currentValue;
        diTo.originalValue = diTo.currentValue;
        diTo.itemStatus = diFrom.itemStatus;
      }
    }
  }


  public float getItemFloat(int atRow, int atCol) {
    String valueStr = getItem(atRow, atCol);
    return Float.parseFloat(JWebUtils.replaceStr(valueStr, ",", ""));
  }

  public float getItemFloat(int atRow, String atColName) {
    int atCol = getColumnIndex(atColName);
    if (atCol == -1)
      return 0f;
    return getItemFloat(atRow, atCol);
  }
  // atRow
  // atCol

  public int getItemInt(int atRow, int atCol) {
    String valueStr = getItem(atRow, atCol);
    if (JWebUtils.isNull(valueStr))
      return 0;
    try {
      int decimalPointIndex = valueStr.indexOf(".");
      if (decimalPointIndex >= 0) {
        String orig = valueStr;
        valueStr = valueStr.substring(0, decimalPointIndex);
        // System.err.println(orig+" valueStr:"+valueStr);
      }
      int result = Integer.parseInt(valueStr);
      return result;
    } catch (Exception ex) {
      // System.err.println("FIRST EXCEPTION ["+valueStr+"]");
      valueStr = JWebUtils.replaceStr(valueStr, " ", "");
    }
    return 0;
  }
  // atRow
  // atColName

  public int getItemInt(int atRow, String atColName) {
    int atCol = getColumnIndex(atColName);
    if (atCol == -1)
      return 0;
    return getItemInt(atRow, atCol);
  }

  public double getItemDouble(int atRow, int atCol) {
    String valueStr = getItem(atRow, atCol);
    if (JWebUtils.isNull(valueStr))
      return 0;
    try {
      double result = Double.parseDouble(valueStr);
      return result;
    } catch (Exception ex) {
      // System.err.println("FIRST EXCEPTION ["+valueStr+"]");
      valueStr = JWebUtils.replaceStr(valueStr, " ", "");
    }
    return 0.0;
  }
  // atRow
  // atColName

  public double getItemDouble(int atRow, String atColName) {
    int atCol = getColumnIndex(atColName);
    if (atCol == -1)
      return 0;
    return getItemDouble(atRow, atCol);
  }

  public boolean getItemBoolean(int atRow, int atCol) {
    String valueStr = getItem(atRow, atCol);
    try {
      return JWebUtils.convertStringToBoolean(valueStr);
    } catch (Exception ex) {
      // ex.printStackTrace();
      return false;
    }
  }
  // atRow
  // atColName

  public boolean getItemBoolean(int atRow, String atColName) {
    int atCol = getColumnIndex(atColName);
    if (atCol == -1)
      return false;
    return getItemBoolean(atRow, atCol);
  }

  private void moveCursorToRow(int rowIndex) {
    try {
      int currentRow = resultSet.getRow() - 1;
      if (rowIndex == currentRow)
        return;
      if (rowIndex < currentRow) {
        for (int i = 0; i < (currentRow - rowIndex); i++) {
          resultSet.previous();
        }
      } else {
        for (int i = 0; i < (rowIndex - currentRow); i++) {
          resultSet.next();
        }
      }
      DataWindowRowLite row = (DataWindowRowLite)rows.get(0);
      populateRow(row);
    } catch (SQLException ex) {
      ex.printStackTrace();
    } finally {
    }
  }

  public String getItem(int atRow, int atCol) {
    if (serverSide) {
      int currentRow;
      String currentValue = "";
      moveCursorToRow(atRow);
      if (atRow >= getRowCount())
        return "";
      if (atCol >= tableColumns.size())
        return "";
      JWebCellLite cell = (JWebCellLite)((DataWindowRowLite)rows.get(0)).dataItems.get(atCol);
      if (cell != null) {
        return cell.currentValue;
      }
      //        if (((DataWindowColumnLite)tableColumns.get(atCol)).dbType == TYPE_LONG) {
      //          currentValue = resultSet.getString(atCol + 1);
      //        } else if (((DataWindowColumnLite)tableColumns.get(atCol)).dbType == TYPE_DATE) {
      //          String s = resultSet.getString(atCol + 1);
      //          if (JWebUtils.isNull(s))
      //            currentValue = s;
      //          else {
      //            java.util.Date d = JWebUtils.convertStringToDate2(s);
      //            currentValue = JWebUtils.convertDateToString2(d);
      //          }
      //        } else
      //          currentValue = resultSet.getString(atCol + 1);
      return currentValue;
    } else {
      if (atRow >= rows.size())
        return "";
      if (atCol >= tableColumns.size())
        return "";
      JWebCellLite cell = (JWebCellLite)((DataWindowRowLite)rows.get(atRow)).dataItems.get(atCol);
      if (cell != null) {
        return cell.currentValue;
      }
    }
    return "";
  }

  public String getItem(int atRow, String atColName) {
    int atCol = getColumnIndex(atColName);
    if (atCol == -1)
      return null;
    return getItem(atRow, atCol);
  }

  public int getColumnIndex(String colName) {
    Integer colIndex = (Integer)colMap.get(colName.trim());
    if (colIndex != null) {
      return colIndex.intValue();
    }
    for (int i = 0; i < tableColumns.size(); i++) {
      DataWindowColumnLite col = (DataWindowColumnLite)tableColumns.get(i);
      if (col.name.equalsIgnoreCase(colName)) {
        return i;
      }
    }
    return -1;
  }

  public int addRow() {
    if (rows == null)
      rows = new ArrayList();
    DataWindowRowLite newRow = new DataWindowRowLite();
    newRow.rowStatus = STATUS_NEW;
    rows.add(newRow);
    initRow(newRow, "");
    newRow.index = rows.size() - 1;
    return newRow.index;
  }

  public int addRow(int status) {
    DataWindowRowLite newRow = new DataWindowRowLite();
    rows.add(newRow);
    initRow(newRow, "");
    newRow.rowStatus = status;
    int rowIndex = rows.size() - 1;
    for (int i = 0; i < tableColumns.size(); i++) {
      JWebCellLite dataItem = (JWebCellLite)newRow.dataItems.get(i);
      dataItem.itemStatus = status;
    }
    newRow.index = rowIndex;
    return newRow.index;
  }
  // public DataWindowRowLite addRow(int row) {
  // return addRow();
  // }

  public int getRowCount() {
    if (serverSide)
      return this.getResultSetRowCount();
    else
      return rows.size();
  }

  public int getColumnCount() {
    return tableColumns.size();
  }

  private void initRow(DataWindowRowLite row, String value) {
    JWebCellLite di;
    for (int iCol = 0; iCol < getColumnCount(); iCol++) {
      di = new JWebCellLite();
      di.currentValue = value;
      di.originalValue = value;
      di.itemStatus = STATUS_NEW;
      row.dataItems.add(di);
    }
  }
  private Connection conUpdate;

  public void setUpdateConnection(Connection conUpdate) {
    this.conUpdate = conUpdate;
  }

  public Connection getUpdateConnection() {
    if (this.conUpdate == null)
      return getDBCon();
    return this.conUpdate;
  }
  private Connection conCube;

  public void setCubeConnection(Connection conCube) {
    this.conCube = conCube;
  }

  public Connection getCubeConnection() {
    if (this.conCube == null)
      return getDBCon();
    return this.conCube;
  }

  public void setName(String dwName) {
    this.dwName = dwName;
  }

  /**
   * @return
   */
  public String getName() {
    return dwName;
  }
  // a

  public void setConnection(java.sql.Connection a) {
    setConnection(a, true);
  }

  public void setConnection(java.sql.Connection a, boolean refreshMetaInfo) {
    con = a;
    if (refreshMetaInfo)
      setMetaInfo();
  }

  public void setConnection(String driver, String url, String userid, String pwd) throws Exception {
    java.sql.Driver d = (java.sql.Driver)Class.forName(driver).newInstance();
    con = java.sql.DriverManager.getConnection(url, userid, pwd);
    try {
      con.setAutoCommit(false);
    } catch (Exception ex) {
    }
    setMetaInfo();
  }

  public void setConnection(int driver, String db) throws Exception {
    setConnection(driver, db, null, null);
  }

  public void setConnection(int driver, String db, String userid, String pwd) throws Exception {
    setDriver(driver);
    if (driver == JDBC_ORACLE) {
    } else if (driver == JDBC_ODBC) {
      this.setConnection(getODBC_Connection(db));
    }
  }

  private Connection getODBC_Connection(String db) throws Exception {
    Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
    return DriverManager.getConnection("jdbc:odbc:" + db);
  }


  private void setMetaInfo() {
    try {
      if (!this.metaDataPresent)
        return;
      databaseMetaData = getDBCon().getMetaData();
      if (databaseMetaData != null) {
        // System.err.println("" +
        // databaseMetaData.getDatabaseProductName());
        if (databaseMetaData.getDatabaseProductName().indexOf("Visual FoxPro") >= 0) {
          setDateParenthesis(CURLY);
          setDriver(JDBC_FOXPRO);
        } else if (databaseMetaData.getDatabaseProductName().indexOf("ACCESS") >= 0) {
          // setDateParenthesis(CURLY);
          // setTextParenthesis(BRACKET);
          setDriver(JDBC_ACCESS);
        } else if (databaseMetaData.getDatabaseProductName().indexOf("Microsoft SQL Server") >= 0) {
          // setDateParenthesis(QUOTES);
          setDriver(JDBC_MSQL);
        } else if (databaseMetaData.getDatabaseProductName().indexOf("Cubedb") >= 0) {
          setDateParenthesis(QUOTES);
          setDriver(JDBC_CUBE);
        } else {
          setDateParenthesis(QUOTES);
          setDriver(JDBC_ORACLE);
        }
      }
    } catch (Exception ex) {
      // ex.printStackTrace();
      databaseMetaData = null;
      this.setMetaDataPresent(false);
    }
  }

  /**
   * @return
   */
  public java.sql.Connection getDBCon() {
    return con;
  }

  public void connectToDB(String jdbcDriverClassName, String urlparm, String id, String pwd, boolean checkForMetaData) throws SQLException {
    this.jdbcDriverClassName = jdbcDriverClassName;
    this.setMetaDataPresent(checkForMetaData);
    connectToDB(urlparm, id, pwd);
  }

  public void connectToDB(String urlparm, String id, String pwd) throws SQLException {
    try {
      url = urlparm;
      Class.forName(jdbcDriverClassName);
      con = DriverManager.getConnection(url, id, pwd);
      // if (con==null) System.err.println(" No coonection");
      // System.err.println(" Setting dbCon");
      this.setConnection(con);
      // System.err.println(" Connection "+con);
      if (this.metaDataPresent)
        con.setAutoCommit(false);
      // stmt = con.createStatement();
    } catch (java.lang.Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * @return
   */
  public String getCurrentSQL() {
    return currentSQL;
  }
  private DatabaseMetaData databaseMetaData = null;

  private void setMetaDataPresent(boolean metaDataPresent) {
    this.metaDataPresent = metaDataPresent;
  }

  public void setDateParenthesis(int dateParenthesis) {
    this.dateParenthesis = dateParenthesis;
  }

  public int getDateParenthesis() {
    return this.dateParenthesis;
  }

  public int getDriver() {
    return driver;
  }

  public void setDriver(int driver) {
    this.driver = driver;
  }

  public void runQuery(String sql) {
    try {
      setQuery(sql);
      retrieve();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void setArguments(Object[] args) {
    this.retrievalArguments = args;
  }

  public int retrieve(Object[] args) throws Exception, SQLException {
    return retrieve(args, 0, 10000000);
  }

  public void runQuery(String sql, Object[] args) {
    try {
      setQuery(sql);
      retrieve(args);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void setQuery(String a) {
    sqlQuery = a;
  }

  /**
   * @return
   */
  public String getQuery() {
    return sqlQuery;
  }

  public int retrieve() throws Exception, SQLException {
    return retrieve(new Object[0]);
  }
  private int resultSetRowCount = 0;

  public int getResultSetRowCount() {
    return resultSetRowCount;
  }

  public int retrieve(Object[] args, int iFromRow, int iToRow) throws Exception, SQLException {
    return retrieve(args, iFromRow, iToRow, true);
  }

  public int retrieve(Object[] args, int iFromRow, int iToRow, boolean refreshResultSet) throws Exception, SQLException {
    // System.err.println(" ColSize: "+tableColumns.size());
    if (JWebUtils.isNull(sqlQuery))
      throw new Exception("JWebDataWindow query is NULL");
    if (resultSet != null && this.databaseMetaData == null) {
      return retrieveWithoutMetaData(args, iFromRow, iToRow);
    }
    String q = sqlQuery;
    try {
      if (args != null) {
        retrievalArguments = new Object[args.length];
        System.arraycopy(args, 0, retrievalArguments, 0, args.length);
        boolean argProcessed = false;
        for (int i = 1; i <= args.length; i++) {
          argProcessed = false;
          String[] arg = JWebUtils.getStringList((String)args[i - 1], "=");
          if (arg != null && arg.length > 1) {
            if (q.indexOf(":" + arg[0]) >= 0) {
              q = JWebUtils.globalReplace(q, ":" + arg[0], arg[1]);
              argProcessed = true;
            }
          }
          if (!argProcessed) {
            q = JWebUtils.globalReplace(q, ":" + String.valueOf(i), String.valueOf(args[i - 1]));
          }
        }
      }
      boolean firstTime = true;
      int colCount, iCol, iRow = 0;
      if (con == null)
        throw new Exception("No Connection is available");
      long startTime = System.currentTimeMillis();
      DataWindowRowLite row;
      JWebCellLite dataItem;
      if (refreshResultSet || resultSet == null) {
        resultSet = null;
        statement = null;
        resultSetRowCount = -1;
        statement = con.createStatement();
        // System.err.println(getName()+": query created: ["+q+"]");
        //
        if (this.inDebug()) {
          System.err.println("Query:" + q);
        }
        resultSet = statement.executeQuery(q);
        if (this.inDebug()) {
          System.err.println("Database Query Execution Time:" + (System.currentTimeMillis() - startTime) + " ms");
        }
        ResultSetMetaData metaData = resultSet.getMetaData();
        try {
          resultSet.last();
          resultSetRowCount = resultSet.getRow();
          resultSet.beforeFirst();
        } catch (Exception e) {
          resultSetRowCount = 0;
        }
        colCount = metaData.getColumnCount();
        if (tableColumns.size() == 0) {
          // no DataWindowColumn has been manually added; create
          // tableColumns automatically; updates are NOT allowed
          for (iCol = 0; iCol < colCount; iCol++) {
            // System.err.println(metaData.getColumnLabel(iCol +
            // 1).toLowerCase()+" "+metaData.getColumnTypeName(iCol
            // + 1));
            try {
              addColumn(metaData.getColumnName(iCol + 1).toLowerCase(), metaData.getTableName(iCol + 1), getDataWindowColumnType(metaData.getColumnType(iCol + 1)));
            } catch (Exception e) {
              addColumn(metaData.getColumnName(iCol + 1).toLowerCase(), "", getDataWindowColumnType(metaData.getColumnType(iCol + 1)));
            }
            getColumn(iCol).columnType = metaData.getColumnType(iCol + 1);
          }
        } else if (colCount != tableColumns.size()) {
          throw new Exception("JWebDataWindow DataWindowColumn count does not match Query DataWindowColumn count");
        }
      } else {
        colCount = getColumnCount();
      }
      //System.err.println(resultSet.getRow());
      // System.err.println("Count="+count);
      if (serverSide)
        iToRow = 0;
      while (resultSet.next()) {
        iRow = addRow(1);
        // if (iRow%10000 == 0) System.err.println("..." + iRow);
        row = (DataWindowRowLite)rows.get(iRow);
        populateRow(row);
        iRow++;
        if (iRow >= iToRow)
          break;
      }

      return iRow;
    } catch (Exception e) {
      System.err.println("SQL [" + q + "]");
      throw e;
    } finally {
      if (!serverSide && refreshResultSet && resultSet != null) {
        resultSet.close();
      }
      if (!serverSide && refreshResultSet && statement != null) {
        statement.close();
      }
    }
  }

  private void populateRow(DataWindowRowLite row) throws SQLException {
    for (int iCol = 0; iCol < getColumnCount(); iCol++) {
      JWebCellLite dataItem = (JWebCellLite)row.dataItems.get(iCol);
      if (((DataWindowColumnLite)tableColumns.get(iCol)).dbType == TYPE_LONG) {
        dataItem.currentValue = resultSet.getString(iCol + 1);
      } else if (((DataWindowColumnLite)tableColumns.get(iCol)).dbType == TYPE_DATE) {
        String s = resultSet.getString(iCol + 1);
        if (JWebUtils.isNull(s))
          dataItem.currentValue = s;
        else {
          java.util.Date d = JWebUtils.convertStringToDate2(s);
          dataItem.currentValue = JWebUtils.convertDateToString2(d);
        }
      } else
        dataItem.currentValue = resultSet.getString(iCol + 1);
      dataItem.originalValue = dataItem.currentValue;
      dataItem.itemStatus = STATUS_NOTMODIFIED;
    }
  }


    public static char getDataWindowColumnType(int t) {
        if (t == Types.INTEGER || t == Types.SMALLINT || t == Types.NUMERIC || t == Types.BIGINT || t == Types.BIT || t == Types.DECIMAL || t == Types.DOUBLE || t == Types.FLOAT || t == Types.REAL || t == Types.SMALLINT) {
            return TYPE_NUMERIC;
        } else if (t == Types.TIME || t == Types.TIMESTAMP || t == Types.DATE) {
            return TYPE_DATE;
        } else {
            return TYPE_CHARACTER;
        }
    }

  public int BlockUpdate() throws Exception, SQLException {
    // System.err.println(" ColSize: "+tableColumns.size());
    String q = sqlQuery;
    int iFromRow = 0, iToRow = 0;
    try {
      boolean firstTime = true;
      int colCount, iCol, iRow = 0;
      if (con == null)
        throw new Exception("No Connection is available");
      long startTime = System.currentTimeMillis();
      DataWindowRowLite row;
      JWebCellLite dataItem;
      if (resultSet == null) {
        resultSet = null;
        statement = null;
        resultSetRowCount = -1;
        statement = con.createStatement();
        // System.err.println(getName()+": query created: ["+q+"]");
        //
        if (this.inDebug()) {
          System.err.println("Query:" + q);
        }
        resultSet = statement.executeQuery(q);
        if (this.inDebug()) {
          System.err.println("Database Query Execution Time:" + (System.currentTimeMillis() - startTime) + " ms");
        }
        ResultSetMetaData metaData = resultSet.getMetaData();
        try {
          resultSet.last();
          resultSetRowCount = resultSet.getRow();
          resultSet.beforeFirst();
        } catch (Exception e) {
          resultSetRowCount = 0;
        }
        colCount = metaData.getColumnCount();
        if (tableColumns.size() == 0) {
          // no DataWindowColumn has been manually added; create
          // tableColumns automatically; updates are NOT allowed
          for (iCol = 0; iCol < colCount; iCol++) {
            // System.err.println(metaData.getColumnLabel(iCol +
            // 1).toLowerCase()+" "+metaData.getColumnTypeName(iCol
            // + 1));
            try {
              addColumn(metaData.getColumnName(iCol + 1).toLowerCase(), metaData.getTableName(iCol + 1), getDataWindowColumnType(metaData.getColumnType(iCol + 1)));
            } catch (Exception e) {
              addColumn(metaData.getColumnName(iCol + 1).toLowerCase(), "", getDataWindowColumnType(metaData.getColumnType(iCol + 1)));
            }
            getColumn(iCol).columnType = metaData.getColumnType(iCol + 1);
          }
        } else if (colCount != tableColumns.size()) {
          throw new Exception("JWebDataWindow DataWindowColumn count does not match Query DataWindowColumn count");
        }
      } else {
        colCount = getColumnCount();
      }
      // System.err.println("Count="+count);
      while (resultSet.next()) {
        iRow = addRow(1);
        // if (iRow%10000 == 0) System.err.println("..." + iRow);
        row = (DataWindowRowLite)rows.get(iRow);
        for (iCol = 0; iCol < colCount; iCol++) {
          dataItem = (JWebCellLite)row.dataItems.get(iCol);
          if (((DataWindowColumnLite)tableColumns.get(iCol)).dbType == TYPE_LONG) {
            dataItem.currentValue = resultSet.getString(iCol + 1);
          } else if (((DataWindowColumnLite)tableColumns.get(iCol)).dbType == TYPE_DATE) {
            String s = resultSet.getString(iCol + 1);
            if (JWebUtils.isNull(s))
              dataItem.currentValue = s;
            else {
              java.util.Date d = JWebUtils.convertStringToDate2(s);
              dataItem.currentValue = JWebUtils.convertDateToString2(d);
            }
          } else
            dataItem.currentValue = resultSet.getString(iCol + 1);
          dataItem.originalValue = dataItem.currentValue;
          dataItem.itemStatus = STATUS_NOTMODIFIED;
        }
        iRow++;
        if (iRow > iToRow)
          break;
      }
      return iRow;
    } catch (Exception e) {
      System.err.println("SQL [" + q + "]");
      throw e;
    } finally {
    }
  }

  public DataWindowColumnLite addColumn(JWebDataWindowLite dw, int colIndex) {
    DataWindowColumnLite col = dw.getColumn(colIndex);
    return addColumn(col.name, col.dbColumn, col.dbTable, col.dbType, col.columnType);
  }

  public DataWindowColumnLite addColumn(String dbColumn, String dbTable, char dbType) {
    return addColumn(dbColumn, dbTable, dbType, Types.VARCHAR);
  }

  public DataWindowColumnLite addColumn(String dbColumn, String dbTable, char dbType, int sqlType) {
    return addColumn(dbColumn, dbColumn, dbTable, dbType, sqlType);
  }

  public DataWindowColumnLite addColumn(String colName, String dbColumn, String dbTable, char dbType, int sqlType) {
    DataWindowColumnLite col = new DataWindowColumnLite();
    col.dbTable = dbTable;
    col.dbType = dbType;
    col.dbColumn = dbColumn;
    col.name = colName;
    col.columnType = sqlType;
    tableColumns.add(col);
    colMap.put(col.dbColumn, new Integer(tableColumns.size() - 1));
    return col;
  }

  public void update(boolean old) throws Exception, SQLException {
    if (updateTable.trim().length() == 0)
      throw new Exception("Update failure: Update table not defined [" + this.getName() + "]");
    ArrayList sqlSubmits = new ArrayList();
    ArrayList sqlSubmitsRowNums = new ArrayList();
    int j, iRow, iCol, rowCount, colCount, rowStatus;
    String sqlstr, whereClause, whereKeyClause, insert1, insert2;
    DataWindowColumnLite col;
    JWebCellLite dataItem;
    int deletedStatementCount = 0;
    if (updateFlag) {
      try {
        con.setAutoCommit(false);
      } catch (Exception ex) {
      }
      rowCount = (deletedRows == null ? 0 : deletedRows.size());
      colCount = tableColumns.size();
      for (iRow = 0; iRow < rowCount; iRow++) {
        whereKeyClause = "";
        // WHERE CLAUSE BASED ON (ORIGINAL) KEY VALUES
        for (iCol = 0; iCol < colCount; iCol++) {
          col = getColumn(iCol);
          if (col.key)
            switch (col.dbType) {
            case TYPE_CHARACTER:
              whereKeyClause += getColumnName(iCol) + " = '" + getDeletedItem(iRow, iCol) + "' AND ";
              break;
            case TYPE_DATE:
              whereKeyClause += getColumnName(iCol) + " = " + getDateClause(getDeletedItem(iRow, iCol)) + " AND ";
              break;
            case TYPE_NUMERIC:
              whereKeyClause += getColumnName(iCol) + " = " + getDeletedItem(iRow, iCol) + " AND ";
              break;
            }
        }
        whereKeyClause = whereKeyClause.substring(0, whereKeyClause.length() - 5);
        sqlstr = "DELETE FROM " + updateTable + " WHERE " + whereKeyClause;
        sqlSubmits.add(sqlstr);
        sqlSubmitsRowNums.add(new Integer(iRow));
        deletedStatementCount++;
      }
      // UPDATES AND INSERTS
      rowCount = rows.size();
      for (iRow = 0; iRow < rowCount; iRow++) {
        whereClause = "";
        whereKeyClause = "";
        rowStatus = getRowStatus(iRow, BUFFER_CURRENT);
        if (rowStatus == STATUS_DATAMODIFIED) {
          sqlstr = "";
          for (iCol = 0; iCol < colCount; iCol++) {
            col = getColumn(iCol);
            if ((col.updatable) && (col.dbTable.trim().equalsIgnoreCase(updateTable.trim()))) {
              dataItem = getCell(iRow, iCol);
              // System.err.println(col.dbColumn+"= "+dataItem.originalValue+
              // " "+dataItem.currentValue);
              // if ((dataItem.itemStatus == STATUS_DATAMODIFIED)
              // && (col.key))
              // throw new
              // Exception("Update failure: Key DataWindowColumn cannot be modified in this version of  ["+updateTable+" "+col.dbColumn+"]");
              if (dataItem.itemStatus == STATUS_DATAMODIFIED) {
                switch (col.dbType) {
                case TYPE_CHARACTER:
                  if (JWebUtils.isNull(dataItem.currentValue)) {
                    if (col.nullable)
                      sqlstr += col.dbColumn + " = NULL, ";
                    else
                      sqlstr += col.dbColumn + " = '', ";
                  } else {
                    sqlstr += col.dbColumn + " = " + getCharClause(iRow, col, JWebUtils.globalReplace(dataItem.currentValue, "'", "''")) + ", ";
                  }
                  if (JWebUtils.isNull(dataItem.originalValue)) {
                    whereClause += col.dbColumn + " IS NULL AND ";
                  } else {
                    whereClause += col.dbColumn + " = " + getCharClause(iRow, col, JWebUtils.globalReplace(dataItem.originalValue, "'", "''")) + " AND ";
                  }
                  // System.err.println(col.dbColumn+" sqlstr: "+sqlstr);
                  break;
                case TYPE_DATE:
                  if (JWebUtils.isNull(dataItem.currentValue))
                    if (col.nullable)
                      sqlstr += col.dbColumn + " = NULL, ";
                    else
                      sqlstr += col.dbColumn + " = '', ";
                  else
                    sqlstr += col.dbColumn + " = " + getDateClause(dataItem.currentValue) + ", ";
                  if (JWebUtils.isNull(dataItem.originalValue))
                    whereClause += col.dbColumn + " IS NULL AND ";
                  else
                    whereClause += col.dbColumn + " = " + getDateClause(dataItem.originalValue) + " AND ";
                  break;
                case TYPE_NUMERIC:
                  if (JWebUtils.isNull(dataItem.currentValue))
                    if (col.nullable)
                      sqlstr += col.dbColumn + " = NULL, ";
                    else
                      sqlstr += col.dbColumn + " = 0, ";
                  else
                    sqlstr += col.dbColumn + " = " + dataItem.currentValue + ", ";
                  if (JWebUtils.isNull(dataItem.originalValue))
                    whereClause += col.dbColumn + " IS NULL AND ";
                  else
                    whereClause += col.dbColumn + " = " + dataItem.originalValue + " AND ";
                  break;
                }
              }
            }
          }
          // build where clause for the key
          for (iCol = 0; iCol < colCount; iCol++) {
            col = getColumn(iCol);
            if (col.key) {
              // System.err.println("key="+col.dbColumn);
              dataItem = getCell(iRow, iCol);
              switch (col.dbType) {
              case TYPE_CHARACTER:
                whereKeyClause += col.dbColumn + " = '" + JWebUtils.globalReplace(dataItem.originalValue, "'", "''") + "' AND ";
                break;
              case TYPE_DATE:
                whereKeyClause += col.dbColumn + " = " + getDateClause(dataItem.originalValue) + " AND ";
                break;
              case TYPE_NUMERIC:
                whereKeyClause += col.dbColumn + " = " + dataItem.originalValue + " AND ";
                break;
              }
            }
          }
          if (sqlstr.length() > 0) {
            sqlstr = sqlstr.substring(0, sqlstr.length() - 2);
            whereKeyClause = whereKeyClause.substring(0, whereKeyClause.length() - 5);
            // sqlSubmits.add("UPDATE " + updateTable + " SET " +
            // sqlstr + " WHERE " + whereClause + whereKeyClause);
            sqlSubmits.add("UPDATE " + updateTable + " SET " + sqlstr + " WHERE " + whereKeyClause);
            sqlSubmitsRowNums.add(new Integer(iRow));
          }
        } else if (rowStatus == STATUS_NEWMODIFIED) {
          insert1 = "";
          insert2 = "";
          for (iCol = 0; iCol < colCount; iCol++) {
            col = getColumn(iCol);
            if ((col.updatable) && (col.dbTable.trim().equalsIgnoreCase(updateTable.trim()))) {
              insert1 += col.dbColumn + ", ";
              dataItem = getCell(iRow, iCol);
              switch (col.dbType) {
              case TYPE_CHARACTER:
                if (JWebUtils.isNull(dataItem.currentValue)) {
                  if (col.nullable)
                    insert2 += "NULL, ";
                  else
                    insert2 += "'', ";
                } else {
                  insert2 += getCharClause(iRow, col, JWebUtils.globalReplace(dataItem.currentValue, "'", "''")) + ", ";
                }
                break;
              case TYPE_DATE:
                if (JWebUtils.isNull(dataItem.currentValue))
                  if (col.nullable)
                    insert2 += "NULL, ";
                  else
                    insert2 += "'1/1/1900',";
                else
                  insert2 += getDateClause(dataItem.currentValue) + ", ";
                break;
              case TYPE_NUMERIC:
                if (JWebUtils.isNull(dataItem.currentValue))
                  if (col.nullable)
                    insert2 += "NULL, ";
                  else
                    insert2 += "0,";
                else
                  insert2 += dataItem.currentValue + ", ";
                break;
              }
            }
          }
          insert1 = insert1.substring(0, insert1.length() - 2);
          insert2 = insert2.substring(0, insert2.length() - 2);
          sqlstr = "INSERT INTO " + updateTable + "(" + insert1 + ") VALUES (" + insert2 + ") ";
          sqlSubmits.add(sqlstr);
          sqlSubmitsRowNums.add(new Integer(iRow));
        }
      }
      int updateCtr = 0;
      for (int i = 0; i < sqlSubmits.size(); i++) {
        currentSQL = (String)sqlSubmits.get(i);
        // System.out.println(""+currentSQL);
        updateCtr = executeUpdate(currentSQL) ? updateCtr + 1 : updateCtr;
        currentSQL = "";
      }
      // System.err.println("sqlSubmits:"+sqlSubmits.size()+
      // " Records Updated:"+ updateCtr);
      // SUCCESS - modify the original buffer to primary buffer and reset
      // status of rows and tableColumns
      try {
        commit();
      } catch (Exception ex) {
        // ex.printStackTrace();
      }
      System.err.println("sqlSubmits:" + sqlSubmits.size() + " Records Updated:" + updateCtr);
      if (true) {
        DataWindowRowLite row;
        for (iRow = 0; iRow < rowCount; iRow++) {
          row = (DataWindowRowLite)rows.get(iRow);
          if ((row.rowStatus == STATUS_DATAMODIFIED) || (row.rowStatus == STATUS_NEWMODIFIED)) {
            row.rowStatus = STATUS_NOTMODIFIED;
            for (iCol = 0; iCol < colCount; iCol++) {
              col = getColumn(iCol);
              if ((col.updatable) && (col.dbTable.trim().equalsIgnoreCase(updateTable.trim()))) {
                dataItem = getCell(iRow, iCol);
                if ((dataItem.itemStatus == STATUS_DATAMODIFIED) || (dataItem.itemStatus == STATUS_NEWMODIFIED)) {
                  dataItem.itemStatus = STATUS_NOTMODIFIED;
                  dataItem.originalValue = dataItem.currentValue;
                }
              }
            }
          }
        }
        if (deletedRows != null)
          deletedRows.clear();
      }
    }
    updateFlag = false;
  }

  public String getColumnName(int atCol) {
    DataWindowColumnLite col = getColumn(atCol);
    if (col == null)
      return "";
    // if (col.name==null) return col.dbColumn;
    return col.name;
  }

  public void setColumnName(int atCol, String colName) {
    DataWindowColumnLite col = getColumn(atCol);
    if (col == null)
      return;
    col.name = colName;
  }

  public void setColumnHeader(int atCol, String colName) {
    setColumnName(atCol, colName);
  }
  private String[] columnHeaders;

  public void setColumnHeader(String[] columnHeaders) {
    this.columnHeaders = columnHeaders;
  }

  public JWebCellLite getCell(int row, int col) {
    if (serverSide) {
      moveCursorToRow(row);
      return (JWebCellLite)((DataWindowRowLite)rows.get(0)).dataItems.get(col);
    } else {
      return (JWebCellLite)((DataWindowRowLite)rows.get(row)).dataItems.get(col);
    }
  }

  public JWebCellLite getCell(int row, String colName) {
    int col = getColumnIndex(colName);
    return (JWebCellLite)((DataWindowRowLite)rows.get(row)).dataItems.get(col);
  }

  public void setCell(int row, int col, JWebCellLite cell) {
    ((DataWindowRowLite)rows.get(row)).dataItems.set(col, cell);
  }

  public String getDeletedItem(int atRow, int atCol) throws RuntimeException {
    if (atRow >= deletedRows.size())
      throw new RuntimeException(Integer.toString(atRow));
    if (atCol >= tableColumns.size())
      throw new RuntimeException(Integer.toString(atCol));
    DataWindowRowLite row = (DataWindowRowLite)deletedRows.get(atRow);
    JWebCellLite dataItem = (JWebCellLite)row.dataItems.get(atCol);
    return dataItem.currentValue;
  }

  public String getCharClause(int row, DataWindowColumnLite col, String strValue) {
    return "'" + strValue + "'";
  }

  public String getDateClause(String dateValue) {
    return getDateClause(dateValue, true);
  }

  public String getDateClause(String dateValue, boolean addQuotes) {
    String clause = addQuotes ? "'" + dateValue + "'" : dateValue;
    if (getDriver() == JDBC_ORACLE) {
      clause = "to_date('" + dateValue + "', 'mm/dd/yyyy hh24:mi:ss..')";
    } else if (getDriver() == JDBC_FOXPRO) {
      clause = "{" + dateValue + "}";
    }
    return clause;
  }

  public String getDateClause(Connection con, String dateValue) {
    String clause = "'" + dateValue + "'";
    // System.err.println(" In GET DATE CLAUSE");
    if (con == null)
      return clause;
    try {
      DatabaseMetaData databaseMetaData = con.getMetaData();
      if (databaseMetaData == null)
        return clause;
      // System.err.println("databaseMetaData.getDatabaseProductName() " +
      // databaseMetaData.getDatabaseProductName());
      if (databaseMetaData.getDatabaseProductName().indexOf("Visual FoxPro") >= 0) {
        clause = "{" + dateValue + "}";
      } else if (databaseMetaData.getDatabaseProductName().indexOf("ACCESS") >= 0) {
        clause = "#" + dateValue + "#";
      } else if (databaseMetaData.getDatabaseProductName().indexOf("ORACLE") >= 0) {
        clause = "to_date('" + dateValue + "', 'mm/dd/yyyy hh24:mi:ss..')";
      } else {
        return clause;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return clause;
  }

  public int getRowStatus(int atRow, int buffer) throws RuntimeException {
    if (buffer == BUFFER_DELETE) {
      DataWindowRowLite row = (DataWindowRowLite)deletedRows.get(atRow);
      return row.rowStatus;
    }
    if (atRow >= rows.size())
      throw new RuntimeException(Integer.toString(atRow));
    DataWindowRowLite row = (DataWindowRowLite)rows.get(atRow);
    return row.rowStatus;
  }

  public void copyConnection(JWebDataWindowLite dw) {
    this.setConnection(dw.getConnection());
    setDriver(dw.getDriver());
  }

  public boolean executeUpdate(String q) throws SQLException, Exception {
    // System.err.println("Execute Update:"+q);
    Statement s = null;
    boolean result = true;
    try {
      if (con == null)
        throw new Exception("No connection available in the pool");
      s = con.createStatement();
      s.executeUpdate(q);
      if (q.substring(0, 6).equals("UPDATE") && (s.getUpdateCount() == 0)) {
        result = false;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.err.println("Failed Query " + q);
      result = false;
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("Failed Query " + q);
      result = false;
    } finally {
      if (s != null) {
        s.close();
      }
    }
    return result;
  }

  public void commit() {
    try {
      // con.commit();
      if (!con.getAutoCommit())
        con.commit();
    } catch (SQLException ex) {
      System.err.println(ex.getMessage());
    }
  }
  private String sortExpr = "";
  private int[] sortList;
  // sortExpr

  public void setSort(String sortExpr) {
    // System.err.println(" sortExpr:"+sortExpr);
    this.sortExpr = sortExpr;
    String[] sortCols = JWebUtils.getStringList(sortExpr, ",");
    sortList = new int[sortCols.length];
    for (int i = 0; i < sortCols.length; i++) {
      String sCol = sortCols[i].trim();
      if (sCol.endsWith("ASC") || sCol.endsWith("DESC")) {
        sCol = sortCols[i].substring(0, sortCols[i].length() - 3);
        // System.err.println(" "+sCol);
      }
      // System.err.println(" "+sCol);
      sortList[i] = getColumnIndex(sCol);
      // System.err.println("SortCol:"+sCol+" "+sortList[i]);
    }
  }

  /**
   */
  public void sort() {
    Collections.sort(rows, new ColumnComparator(true));
    for (int r = 0; r < getRowCount(); r++) {
      DataWindowRowLite row = (DataWindowRowLite)rows.get(r);
      row.index = r;
      for (int c = 0; c < getColumnCount(); c++) {
        JWebCellLite cell = (JWebCellLite)row.dataItems.get(c);
        cell.row = r;
      }
    }
  }

  private class ColumnComparator implements Serializable, Comparator {
    protected boolean sortAsc;
    // boolean sortAsc

    public ColumnComparator(boolean sortAsc) {
      this.sortAsc = sortAsc;
    }

    /**
     * @param dataItem
     * @return
     */
    private String getCurrentValue(JWebCellLite dataItem) {
      return (JWebUtils.isNull(dataItem.currentValue) ? "" : dataItem.currentValue);
    }

    /**
     * @param o1
     * @param o2
     * @return
     */
    public int compare(Object o1, Object o2) {
      DataWindowRowLite r1 = (DataWindowRowLite)o1;
      DataWindowRowLite r2 = (DataWindowRowLite)o2;
      int result = 0;
      for (int i = 0; i < sortList.length; i++) {
        int sortCol = sortList[i];
        DataWindowColumnLite col = getColumn(sortCol);
        JWebCellLite dataItem1 = (JWebCellLite)r1.dataItems.get(sortCol);
        JWebCellLite dataItem2 = (JWebCellLite)r2.dataItems.get(sortCol);
        String col1 = getCurrentValue(dataItem1);
        String col2 = getCurrentValue(dataItem2);
        if (col.dateTime) {
          v1 = JWebUtils.getMonth(col1);
          v2 = JWebUtils.getMonth(col2);
          if (v1 == -1 || v2 == -1) {
            result = col1.compareTo(col2);
          } else {
            result = v1 - v2;
          }
          // System.out.println(col.name+ " result:"+result +
          // " v1:"+v1+" v2:"+v2);
        } else {
          // System.err.println(r1.index+" sortCol:"+sortCol);
          result = col1.compareTo(col2);
        }
        if (result != 0)
          break;
      }
      if (!sortAsc)
        result = -result;
      return result;
    }
    // obj

    public boolean equals(Object obj) {
      if (obj instanceof ColumnComparator) {
        ColumnComparator compObj = (ColumnComparator)obj;
        return (compObj.sortAsc == sortAsc);
      }
      return false;
    }
  }

  public Connection getConnection() {
    return con;
  }

  public void clear() {
    if (rows != null)
      rows.clear();
    if (tableColumns != null)
      tableColumns.clear();
    if (deletedRows != null)
      deletedRows.clear();
  }

  public void setColumns(int numColumns, String tableName, String[] colNames) {
    clear();
    if (tableColumns == null)
      tableColumns = new ArrayList();
    String colName = "";
    int colWidth = 30;
    for (int i = 0; i < numColumns; i++) {
      if (colNames == null) {
        colName = "Column " + i;
      } else {
        colName = colNames[i];
        colWidth = colName.length() * 10;
      }
      addColumn(colName, tableName, TYPE_CHARACTER);
    }
  }

  public void setColumnType(int atCol, char dbType) {
    if (atCol >= tableColumns.size())
      return;
    DataWindowColumnLite col = (DataWindowColumnLite)tableColumns.get(atCol);
    col.dbType = dbType;
  }

  public void setColumnSQLType(int atCol, int dbType) {
    if (atCol >= tableColumns.size())
      return;
    DataWindowColumnLite col = (DataWindowColumnLite)tableColumns.get(atCol);
    col.columnType = dbType;
  }

  public char getColumnType(int atCol) {
    if (atCol >= tableColumns.size())
      return '!';
    DataWindowColumnLite col = (DataWindowColumnLite)tableColumns.get(atCol);
    return col.dbType;
  }

  public int getColumnSQLType(int atCol) {
    if (atCol >= tableColumns.size())
      return '!';
    DataWindowColumnLite col = (DataWindowColumnLite)tableColumns.get(atCol);
    return col.columnType;
  }
  private ArrayList cubeQuery;

  private class SubCube implements Serializable {
    String query;
    Object[] args;
    String metrics;
    String[] metricList;
    int[] metricTarget;
    int metricDescriber;
    int metricCount;
    JWebDataWindowLite dwData;

    SubCube(String query, String metrics) {
      this.query = query;
      this.metrics = metrics;
    }
  }

  public void addQuery(String qry, Object[] args, String[] metricList, int[] metricTarget, int metricDescriber, int metricCount) {
    if (cubeQuery == null)
      cubeQuery = new ArrayList();
    SubCube cube = new SubCube(qry, "");
    cube.metricList = metricList;
    cube.metricTarget = metricTarget;
    cube.metricDescriber = metricDescriber;
    cube.metricCount = metricCount;
    // cube.metricIndex=metricIndex;
    cube.args = args;
    cubeQuery.add(cube);
  }

  public void buildCube(int dimCount, int interval) throws Exception {
    HashMap map = new HashMap();
    JWebDataWindowLite[] queryList = new JWebDataWindowLite[cubeQuery.size()];
    Thread[] queryThreads = new Thread[cubeQuery.size()];
    for (int i = 0; i < cubeQuery.size(); i++) {
      SubCube sCube = (SubCube)cubeQuery.get(i);
      JWebDataWindowLite dwQuery = new JWebDataWindowLite();
      dwQuery.setConnection(getDBCon());
      dwQuery.setName("Query:" + i);
      dwQuery.setQuery(sCube.query);
      queryThreads[i] = dwQuery.retrieveInBackground(sCube.args);
      queryList[i] = dwQuery;
      sCube.dwData = dwQuery;
    }
    // wait loop - check after evey 10 seconds
    // System.err.println(" Out of looop");
    for (int i = 0; i < queryThreads.length; i++) {
      queryThreads[i].start();
    }
    suspendDuringQueryExecution(queryList, interval);
    //
    for (int i = 0; i < cubeQuery.size(); i++) {
      SubCube sc = (SubCube)cubeQuery.get(i);
      // sc.dwData.saveAsExcel("c:\\q_"+i+".xls",true,true);
      this.joinDataWindows((SubCube)cubeQuery.get(i), dimCount, map);
    }
  }

  private void suspendDuringQueryExecution(final JWebDataWindowLite[] queryList, final int interval) {
    synchronized (this) {
      try {
        boolean queryInProcess = true;
        while (queryInProcess) {
          try {
            queryInProcess = false;
            for (int i = 0; i < queryList.length; i++) {
              JWebDataWindowLite dwQuery = queryList[i];
              // System.err.println("Suspend status for: " +
              // dwQuery.getName() + " " + dwQuery.isBusy());
              if (dwQuery.isBusy()) {
                queryInProcess = true;
                // break;
              }
            }
            // sleep();
            if (queryInProcess) {
              if (interval == 0) {
                wait(100);
              } else {
                wait(1000 * interval);
              }
            }
          } catch (InterruptedException ex) {
            break;
          }
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }
  private boolean inProcess = false;

  public Thread retrieveInBackground(final Object[] args) {
    Thread retriever = null;
    inProcess = true;
    retriever = new Thread() {

        public void run() {
          try {
            // System.err.println("Before retrieve "+" "+isBusy());
            retrieve(args);
            inProcess = false;
          } catch (Exception ex) {
            ex.printStackTrace();
            inProcess = false;
          }
        }
      };
    retriever.setPriority(Thread.MIN_PRIORITY);
    return retriever;
  }

  public boolean isBusy() {
    return inProcess;
  }

  private int joinDataWindows(SubCube cube, int joinedDimCount, HashMap rowKeys) throws Exception, SQLException {
    String sqlQuery;
    int dimCount = joinedDimCount;
    sqlQuery = cube.query;
    String metrics = cube.metrics;
    JWebDataWindowLite dwData = cube.dwData;
    try {
      boolean firstTime = true;
      int colCount, iCol, iRow = 0;
      colCount = dwData.getColumnCount();
      int cubeColumns[] = new int[colCount];
      String data[] = new String[colCount];
      for (iCol = 0; iCol < colCount; iCol++) {
        String columnName = dwData.getColumnName(iCol);
        // System.err.println(" dwData "+columnName);
        for (int i = 0; i < getColumnCount(); i++) {
          // System.err.println(" Data: "+columnName+ " Main:"
          // +getColumnName(i));
          if (JWebUtils.same(getColumnName(i), columnName)) {
            cubeColumns[iCol] = i;
            break;
          }
        }
      }
      // System.err.println("CubeColumns:" +
      // JWebUtils.getArrayAsString(cubeColumns, ","));
      int cubeColumn = 0;
      JWebCellLite dataItem;
      // Closed.4,In Progress.5
      String[] metricList = cube.metricList;
      int[] metricTarget = cube.metricTarget;
      int metricDescriber = cube.metricDescriber;
      int metricCount = cube.metricCount;
      // int metricIndex=cube.metricIndex;
      for (int r = 0; r < dwData.getRowCount(); r++) {
        String rowKey = "";
        for (iCol = 0; iCol < colCount; iCol++) {
          String value = dwData.getItem(r, iCol).trim();
          if (iCol < dimCount)
            rowKey = rowKey + "[" + value + "]";
          data[iCol] = value;
        }
        // data[iCol] = resultSet.getString(iCol + 1);
        int rowNum = 0;
        if (rowKeys.containsKey(rowKey)) {
          rowNum = ((Integer)rowKeys.get(rowKey)).intValue();
        } else {
          rowNum = addRow();
          rowKeys.put(rowKey, new Integer(rowNum));
        }
        DataWindowRowLite row = (DataWindowRowLite)rows.get(rowNum);
        for (iCol = 0; iCol < colCount - metricCount; iCol++) {
          dataItem = (JWebCellLite)row.dataItems.get(cubeColumns[iCol]);
          dataItem.currentValue = data[iCol];
          dataItem.originalValue = dataItem.currentValue;
          dataItem.itemStatus = STATUS_NOTMODIFIED;
        }
        int metricSrc = colCount - 1;
        if (metricList != null && metricList.length > 0) {
          // System.err.println(" METRIC LIST NOT NULL ");
          for (int i = 0; i < metricList.length; i++) {
            if (JWebUtils.same(metricList[i], data[metricDescriber])) {
              metricSrc = metricTarget[i];
              dataItem = (JWebCellLite)row.dataItems.get(metricSrc);
              dataItem.currentValue = data[colCount - 1];
              dataItem.originalValue = dataItem.currentValue;
              dataItem.itemStatus = STATUS_NOTMODIFIED;
              break;
            }
          }
        } else if (metricCount > 1) {
          for (int m = 0; m < metricTarget.length; m++) {
            metricSrc = metricTarget[m];
            dataItem = (JWebCellLite)row.dataItems.get(metricSrc);
            dataItem.currentValue = data[dimCount + m];
            dataItem.originalValue = dataItem.currentValue;
            dataItem.itemStatus = STATUS_NOTMODIFIED;
          }
        } else {
          metricSrc = metricTarget[0];
          dataItem = (JWebCellLite)row.dataItems.get(metricSrc);
          dataItem.currentValue = data[colCount - 1];
          dataItem.originalValue = dataItem.currentValue;
          dataItem.itemStatus = STATUS_NOTMODIFIED;
        }
        iRow++;
      }
      return iRow;
    } catch (Exception e) {
      System.err.println("SQL [" + sqlQuery + "]");
      throw e;
    } finally {
    }
  }

  public void deleteRow(int atRow) {
    if (atRow >= rows.size())
      return;
    if (deletedRows == null)
      deletedRows = new ArrayList();
    deletedRows.add(rows.get(atRow));
    rows.remove(atRow);
    updateFlag = true;
  }

  public String getRowAsText(int row) {
    return getRowAsText(row, 0, getColumnCount() - 1);
  }

  public String getRowAsText(int row, int colStart, int colEnd) {
    StringBuffer text = new StringBuffer("");
    String nl = JWebUtils.getNewLineChar();
    for (int i = colStart; i < Math.min(colEnd + 1, getColumnCount()); i++) {
      text.append("[" + getColumnName(i) + "=" + getItem(row, i) + "]");
    }
    return text.toString();
  }

  public String[] getRowAsStringArray(int row) {
    return getRowAsStringArray(row, 0, getColumnCount() - 1);
  }

  public String[] getRowAsStringArray(int row, int colStart, int colEnd) {
    String[] text = new String[colEnd - colStart + 1];
    for (int i = colStart; i < Math.min(colEnd + 1, getColumnCount()); i++) {
      text[i] = getItem(row, i);
    }
    return text;
  }

  private void setCellValue(DataWindowRowLite row, JWebCellLite dataItem, String value) {
    setCellValue(row, dataItem, value, BUFFER_CURRENT);
  }

  private void setCellValue(DataWindowRowLite row, JWebCellLite dataItem, String itemValue, int buffer) {
    dataItem.currentValue = itemValue;
    // if (JWebUtils.isNull(dataItem.originalValue))
    // dataItem.originalValue=itemValue;
    switch (buffer) {
    case BUFFER_CURRENT:
      dataItem.setValue(itemValue);
      if (JWebUtils.isNull(itemValue)) {
        if ((row.rowStatus == STATUS_NOTMODIFIED) && (!JWebUtils.isNull(dataItem.originalValue)))
          row.rowStatus = STATUS_DATAMODIFIED;
        if ((dataItem.itemStatus == STATUS_NOTMODIFIED) && (!JWebUtils.isNull(dataItem.originalValue)))
          dataItem.itemStatus = STATUS_DATAMODIFIED;
      } else {
        if (row.rowStatus == STATUS_NEW)
          row.rowStatus = STATUS_NEWMODIFIED;
        else if ((row.rowStatus == STATUS_NOTMODIFIED) && (!itemValue.equals(dataItem.originalValue)))
          row.rowStatus = STATUS_DATAMODIFIED;
        if (dataItem.itemStatus == STATUS_NEW)
          dataItem.itemStatus = STATUS_NEWMODIFIED;
        else if ((dataItem.itemStatus == STATUS_NOTMODIFIED) && (!itemValue.equals(dataItem.originalValue)))
          dataItem.itemStatus = STATUS_DATAMODIFIED;
        if (dataItem.originalValue == null) {
          dataItem.originalValue = dataItem.currentValue;
        } else {
          if (!dataItem.originalValue.equals(dataItem.currentValue)) {
            dataItem.itemStatus = STATUS_DATAMODIFIED;
          } else {
            dataItem.itemStatus = STATUS_NOTMODIFIED;
          }
        }
      }
      break;
    case BUFFER_ORIGINAL:
      dataItem.originalValue = itemValue;
      break;
    case BUFFER_SCREEN:
      dataItem.userEnteredValue = itemValue;
      break;
    default:
    }
    updateFlag = true;
  }

  public void setCellItem(int row, int col, String value) {
    setItem(row, col, value);
  }

  public JWebCellLite setItem(int row, int col, String value) {
    JWebCellLite cell = getCell(row, col);
    setCellValue((DataWindowRowLite)rows.get(row), cell, value);
    return cell;
  }

  public JWebCellLite setItem(int row, String col, int value) {
    return setItem(row, getColumnIndex(col), value);
  }

  public JWebCellLite setItem(int row, int col, int value) {
    JWebCellLite cell = getCell(row, col);
    setCellValue((DataWindowRowLite)rows.get(row), cell, value + "");
    return cell;
  }

  public JWebCellLite setItem(int row, String col, double value) {
    return setItem(row, getColumnIndex(col), value);
  }

  public JWebCellLite setItem(int row, int col, double value) {
    JWebCellLite cell = getCell(row, col);
    setCellValue((DataWindowRowLite)rows.get(row), cell, value + "");
    return cell;
  }

  public JWebCellLite setItem(int row, String colName, String value) {
    int col = getColumnIndex(colName);
    JWebCellLite cell = getCell(row, col);
    setCellValue((DataWindowRowLite)rows.get(row), cell, value);
    return cell;
  }

  public void printAsString() {
    printAsString(null);
  }

  public void printAsStringErr(String label) {
    if (label != null)
      System.err.println(label);
    for (int c = 0; c < getColumnCount(); c++) {
      System.err.print(getColumnName(c) + " ");
    }
    System.err.println("");
    for (int r = 0; r < getRowCount(); r++) {
      for (int c = 0; c < getColumnCount(); c++) {
        System.err.print(getItem(r, c) + " ");
      }
      System.err.println("");
    }
  }
  public void printAsString(String label) {
    if (label != null)
      System.out.println(label);
    for (int c = 0; c < getColumnCount(); c++) {
      System.out.print(getColumnName(c) + " ");
    }
    System.out.println("");
    for (int r = 0; r < getRowCount(); r++) {
      for (int c = 0; c < getColumnCount(); c++) {
        System.out.print(getItem(r, c) + " ");
      }
      System.out.println("");
    }
  }

  public void saveAs(String fileName) {
    StringBuffer contents = new StringBuffer();
    for (int c = 0; c < getColumnCount(); c++) {
      contents.append(getColumnName(c) + " ");
    }
    contents.append(JWebUtils.getNewLineChar());
    for (int r = 0; r < getRowCount(); r++) {
      for (int c = 0; c < getColumnCount(); c++) {
        contents.append(getItem(r, c) + " ");
      }
      contents.append(JWebUtils.getNewLineChar());
    }
    JWebUtils.writeTextToFile(fileName, contents.toString(), true);
  }

  public void setUpdateTable(String a) {
    updateTable = a;
  }

  /**
   * @return
   */
  public String getUpdateTable() {
    return updateTable;
  }

  public boolean isUpdatable(int colNum) throws Exception {
    DataWindowColumnLite col;
    col = getColumn(colNum);
    return col.updatable;
  }

  /**
   * @param colNum
   * @param value
   * @exception Exception
   */
  public void setUpdatable(int colNum, boolean value) throws Exception {
    DataWindowColumnLite col;
    col = getColumn(colNum);
    col.updatable = value;
  }

  public void prepareForUpdate(String tableName) throws Exception {
    prepareForUpdate(tableName, STATUS_NEWMODIFIED);
  }

  public void prepareForUpdate(String tableName, int status) throws Exception {
    this.setUpdateTable(tableName);
    for (int i = 0; i < getColumnCount(); i++) {
      DataWindowColumnLite col = getColumn(i);
      col.dbTable = tableName;
      col.updatable = true;
      col.dbColumn = col.name;
      col.nullable = true;
      // col.dbType=TYPE_CHARACTER;
    }
    if (!serverSide) {
      for (int i = 0; i < getRowCount(); i++) {
        this.setRowStatus(i, status);
      }
    }
    updateFlag = true;
    allowUpdates = true;
  }

  /**
   * @param colNum
   * @return
   * @exception Exception
   */
  public boolean isKey(int colNum) throws Exception {
    DataWindowColumnLite col;
    col = getColumn(colNum);
    return col.key;
  }

  /**
   * @param colName
   * @param value
   * @exception Exception
   */
  public void setKey(String colName, boolean value) throws Exception {
    int i = getColumnIndex(colName);
    if (i == -1)
      throw new Exception();
    setKey(i, value);
  }

  /**
   * @param colNum
   * @param value
   * @exception Exception
   */
  public void setKey(int colNum, boolean value) throws Exception {
    DataWindowColumnLite col;
    col = getColumn(colNum);
    col.key = value;
    key = col;
    keyColumnDefined = true;
  }

  public String getKey() {
    return key.name;
  }

  public void resetUpdate() {
    updateFlag = false;
    if (deletedRows != null)
      deletedRows.clear();
  }

  public void setRowStatus(int atRow, int status) throws Exception {
    if (atRow >= rows.size())
      throw new Exception(Integer.toString(atRow));
    DataWindowRowLite row = (DataWindowRowLite)rows.get(atRow);
    row.rowStatus = status;
    JWebCellLite dataItem;
    for (int col = 0; col < tableColumns.size(); col++) {
      dataItem = (JWebCellLite)row.dataItems.get(col);
      dataItem.itemStatus = status;
    }
    updateFlag = true;
  }

  public void save(String fileName) throws Exception {
    prepareForUpdate(fileName);
    createTable(fileName);
    update();
  }

  public boolean createTable(String tableName, boolean dropOldTable, String[] cols) throws Exception {
    return createTable(tableName, dropOldTable, cols, null, null);
  }

  public boolean createTable(String tableName, boolean dropOldTable, String[] cols, char[] types) throws Exception {
    return createTable(tableName, dropOldTable, cols, types, null);
  }

  public boolean createTable(String tableName, boolean dropOldTable, String[] cols, char[] types, int[] widths) throws Exception {
    char dwType;
    int dbType;
    int width;
    for (int i = 0; i < cols.length; i++) {
      if (types == null) {
        dwType = TYPE_CHARACTER;
        dbType = Types.VARCHAR;
      } else {
        dwType = types[i];
        if (dwType == TYPE_DATE)
          dbType = dbType = Types.DATE;
        else if (dwType == TYPE_NUMERIC)
          dbType = Types.NUMERIC;
        else
          dbType = Types.VARCHAR;
      }
      if (widths == null) {
        width = 250;
      } else {
        width = widths[i];
      }
      DataWindowColumnLite col = addColumn(cols[i], tableName, dwType, dbType);
      col.length = width;
    }
    return createTable(tableName, dropOldTable);
  }

  public boolean createTable(String tableName) throws Exception {
    return createTable(tableName, true);
  }

  public boolean createTable(String tableName, boolean dropOldTable) throws Exception {
    synchronized (this) {
      if (isTablePresent(tableName)) {
        if (dropOldTable) {
          executeUpdate("DROP TABLE " + tableName);
        } else {
          return false;
        }
      }
      StringBuffer createStr = new StringBuffer();
      for (int i = 0; i < getColumnCount(); i++) {
        DataWindowColumnLite col = getColumn(i);
        String colName = col.dbColumn;
        int sqlType = col.columnType;
        // System.err.println(tableName+"."+colName+" = "+sqlType);
        switch (sqlType) {
        case Types.DECIMAL:
        case Types.FLOAT:
        case Types.REAL:
          if (driver == this.JDBC_ACCESS) {
            createStr.append(colName + " Single ");
          } else {
            createStr.append(colName + " FLOAT ");
          }
          break;
        case Types.NUMERIC:
        case Types.BIT:
        case Types.INTEGER:
        case Types.SMALLINT:
        case Types.BIGINT:
          if (driver == this.JDBC_ACCESS) {
            createStr.append(colName + " INTEGER ");
          } else {
            createStr.append(colName + " INTEGER ");
          }
          break;
        default:
          if (driver == this.JDBC_ACCESS) {
            createStr.append(colName + " Text ");
          } else {
            createStr.append(colName + " VARCHAR(" + col.length + ")");
          }
        }
        if (i < getColumnCount() - 1) {
          createStr.append(",");
        }
      }
      String createSQL = "CREATE TABLE " + tableName + " ( ";
      createSQL = createSQL + " " + createStr.toString() + ")";
      // System.out.println("createSQL:" + createSQL);
      executeImmediate(createSQL);
    }
    return true;
  }

  public boolean isTablePresent(String tableName, Connection connection) throws Exception {
    if (connection == null)
      throw new Exception("Connection is null");
    DatabaseMetaData md = connection.getMetaData();
    if (md == null) {
      try {
        runQuery("select * from " + tableName + " Where 1 < 1");
      } catch (Exception ex) {
        return false;
      }
    }
    ResultSet rs = md.getTables(null, null, tableName, null);
    if (rs.next()) {
      return true;
    } else {
      if (getDriver() == JDBC_ORACLE) {
        Statement stmt = connection.createStatement();
        try {
          rs = stmt.executeQuery(" SELECT * FROM user_tables " + " WHERE table_name = '" + tableName.toUpperCase() + "' ");
          if (rs.next()) {
            return true;
          }
        } catch (Exception ex) {
          // ex.printStackTrace();
        }
      }
      return false;
    }
  }

  public boolean isTablePresent(String tableName) throws Exception {
    return isTablePresent(tableName, getConnection());
  }

  public int executeImmediate(String sql) throws SQLException {
    synchronized (this) {
      try {
        Statement stmt = con.createStatement();
        stmt.executeUpdate(sql);
        con.commit();
        return stmt.getUpdateCount();
      } catch (SQLException ex) {
        System.err.println(" IMMEDIATE: " + sql);
        throw (ex);
      }
    }
  }

  public void setColumnLength(int i, int length) {
    getColumn(i).length = length;
  }

  public void setColumnLength(int length) {
    for (int i = 0; i < getColumnCount(); i++) {
      setColumnLength(i, length);
    }
  }

  public void resetRows() {
    if (rows != null)
      rows.clear();
    if (deletedRows != null)
      deletedRows.clear();
  }

  public void createIndex(String onTable, String indexName, String colName) throws Exception {
    String sql = "Create Index " + indexName + " ON " + onTable + "(" + colName + ")";
    executeImmediate(sql);
  }

  public void createIndex(String onTable, int[] colList) throws Exception {
    for (int i = 0; i < colList.length; i++) {
      String colName = getColumnName(colList[i]);
      createIndex(onTable, "INDEX_" + colName, colName);
    }
  }

  public void setObject(int row, int col, Object object) {
    JWebCellLite cell = getCell(row, col);
    cell.object = object;
  }

  public Object getObject(int row, int col) {
    if (row >= rows.size())
      return null;
    if (col >= tableColumns.size())
      return null;
    JWebCellLite cell = getCell(row, col);
    return cell.object;
  }

  public void setObject(Object object) {
    this.object = object;
  }

  public Object getObject() {
    return object;
  }

  public JWebCellLite getParentCell() {
    return parentCell;
  }

  /**
   * @param cell
   */
  public void setParentCell(JWebCellLite cell) {
    this.parentCell = cell;
  }

  public void setUpdateFlag(boolean updateFlag) {
    this.updateFlag = updateFlag;
  }

  public boolean getUpdateFlag() {
    return this.updateFlag;
  }

  public JWebDataWindowLite getSheet(String sheetName) {
    JWebCellLite cell = null;
    for (int i = 0; i < getColumnCount(); i++) {
      // System.err.println(getColumnName(i)+"="+sheetName);
      cell = getCell(0, i);
      if (JWebUtils.isSame(cell.getName(), sheetName)) {
        break;
      }
      cell = null;
    }
    // System.err.println(" SHEET "+cell);
    if (cell != null) {
      return (JWebDataWindowLite)cell.getCellRenderer();
    }
    return null;
  }

  public JWebDataWindowLite getSheet(int sheetIndex) {
    DataWindowRowLite row = (DataWindowRowLite)rows.get(0);
    JWebCellLite cell = (JWebCellLite)row.dataItems.get(sheetIndex);
    return (JWebDataWindowLite)cell.getCellRenderer();
  }

  public static JWebDataWindowLite readFromDataWindow(String fileName) throws Exception {
    FileInputStream in = new FileInputStream(fileName);
    ObjectInputStream oin = new ObjectInputStream(in);
    JWebDataWindowLite dw = (JWebDataWindowLite)(oin.readObject());
    oin.close();
    return dw;
  }

  public void serialize(String fileName) throws Exception {
    ObjectOutputStream objectOut = new ObjectOutputStream(new FileOutputStream(fileName));
    if (this.getConnection() == null)
      objectOut.writeObject(this);
    else {
      con = null;
      objectOut.writeObject(this);
    }
    objectOut.flush();
    objectOut.close();
  }

  public int find(String value) {
    return find(0, value);
  }

  public int find(String colName, String value) {
    return find(getColumnIndex(colName), value);
  }

  public int find(int col, String value) {
    for (int i = 0; i < this.getRowCount(); i++) {
      // System.err.println("Comparing:["+value+"] ["+this.getItem(i,col)+"]");
      if (JWebUtils.same(value, this.getItem(i, col))) {
        // System.err.println("Comparison TRUE:["+value+"] ["+this.getItem(i,col)+"]");
        return i;
      } else {
        // System.err.println("Comparison FALSE:["+value+"] ["+this.getItem(i,col)+"]");
      }
    }
    return -1;
  }

  public String getRowValuesAsString(int col) {
    StringBuffer rowValues = new StringBuffer();
    for (int i = 0; i < getRowCount(); i++) {
      rowValues.append(" " + getItem(i, col));
    }
    return rowValues.toString();
  }

  public int[] getItemAsIntArray(int atRow, String atColName) {
    return getItemAsIntArray(atRow, atColName, ",");
  }

  public int[] getItemAsIntArray(int atRow, int atCol) {
    return getItemAsIntArray(atRow, atCol, ",");
  }

  public int[] getItemAsIntArray(int atRow, String atColName, String delim) {
    int atCol = getColumnIndex(atColName);
    if (atCol == -1)
      return null;
    return getItemAsIntArray(atRow, atCol, delim);
  }

  public int[] getItemAsIntArray(int atRow, int atCol, String delim) {
    return JWebUtils.getIntList(getItem(atRow, atCol), delim);
  }

  public String[] getItemAsStringArray(int atRow, String atColName) {
    return getItemAsStringArray(atRow, atColName, ",");
  }

  public String[] getItemAsStringArray(int atRow, int atCol) {
    return getItemAsStringArray(atRow, atCol, ",");
  }

  public String[] getItemAsStringArray(int atRow, String atColName, String delim) {
    int atCol = getColumnIndex(atColName);
    if (atCol == -1)
      return null;
    return getItemAsStringArray(atRow, atCol, delim);
  }

  public String[] getItemAsStringArray(int atRow, int atCol, String delim) {
    return JWebUtils.getStringList(getItem(atRow, atCol), delim);
  }

  public String getColumnList() {
    String colList = "";
    for (int i = 0; i < getColumnCount(); i++) {
      colList = JWebUtils.ifConcat(colList, getColumnName(i), ",");
    }
    return colList;
  }

  public void setColumnAsDate(int index, boolean asDate) {
    DataWindowColumnLite col = getColumn(index);
    col.dateTime = asDate;
  }

  public int addRow(String value) {
    int row = addRow(0);
    setItem(row, 0, value);
    return row;
  }

  public int addRow(String value1, String value2) {
    int row = addRow(0);
    setItem(row, 0, value1);
    setItem(row, 1, value2);
    return row;
  }

  public int addRow(String[] colValues) {
    int row = addRow(0);
    for (int i = 0; i < colValues.length; i++) {
      setItem(row, i, colValues[i]);
    }
    return row;
  }

  public int addRow(String value1, String value2, String value3) {
    int row = addRow(0);
    setItem(row, 0, value1);
    setItem(row, 1, value2);
    setItem(row, 2, value3);
    return row;
  }

  public int addRow(String value1, String value2, String value3, String value4) {
    int row = addRow(0);
    setItem(row, 0, value1);
    setItem(row, 1, value2);
    setItem(row, 2, value3);
    setItem(row, 3, value4);
    return row;
  }

  public int addRow(String value1, String value2, String value3, String value4, String value5) {
    int row = addRow(0);
    setItem(row, 0, value1);
    setItem(row, 1, value2);
    setItem(row, 2, value3);
    setItem(row, 3, value4);
    setItem(row, 4, value5);
    return row;
  }

  public int addRow(String value1, String value2, String value3, String value4, String value5, String value6) {
    int row = addRow(0);
    setItem(row, 0, value1);
    setItem(row, 1, value2);
    setItem(row, 2, value3);
    setItem(row, 3, value4);
    setItem(row, 4, value5);
    setItem(row, 5, value6);
    return row;
  }

  public int addRow(String value1, String value2, String value3, String value4, String value5, String value6, String value7) {
    int row = addRow(0);
    setItem(row, 0, value1);
    setItem(row, 1, value2);
    setItem(row, 2, value3);
    setItem(row, 3, value4);
    setItem(row, 4, value5);
    setItem(row, 5, value6);
    setItem(row, 6, value7);
    return row;
  }

  public void setColumns(String[] colNames) {
    setColumns(colNames.length, "TABLE_A", colNames);
  }

  public int getObjectType() {
    return objectType;
  }

  public JWebDataWindowLite getTranspose(String firstCol) {
    return (JWebDataWindowLite)JWebDataWindowUtils.getTranspose(this, firstCol);
  }

  public int retrieveUsingStoredProcedure(String[] args, int iFromRow, int iToRow) throws Exception, SQLException {
    // System.err.println(" ColSize: "+tableColumns.size());
    boolean findMinMax = false;
    if (JWebUtils.isNull(sqlQuery)) {
      throw new Exception("JWebDataWindow query is NULL");
    }
    resultSet = null;
    CallableStatement statement = null;
    String q = sqlQuery;
    try {
      boolean firstTime = true;
      int colCount;
      int iCol;
      int iRow = 0;
      // ResultSetMetaData metaData;
      if (con == null) {
        throw new Exception("No Connection is available");
      }
      statement = con.prepareCall(sqlQuery);
      if (args != null) {
        retrievalArguments = new Object[args.length];
        System.arraycopy(args, 0, retrievalArguments, 0, args.length);
        boolean argProcessed = false;
        for (int i = 0; i < args.length; i++) {
          statement.setString(i + 1, args[i]);
        }
      }
      long startTime = System.currentTimeMillis();
      if (this.inDebug()) {
        System.err.println("Query:" + q);
      }
      resultSet = statement.executeQuery();
      try {
        resultSet.last();
        resultSetRowCount = resultSet.getRow();
        resultSet.beforeFirst();
      } catch (Exception e) {
        resultSetRowCount = 0;
      }
      if (this.inDebug()) {
        System.err.println("Database Query Execution Time:" + (System.currentTimeMillis() - startTime) + " ms");
      }
      DataWindowRowLite row;
      JWebCellLite dataItem;
      ResultSetMetaData metaData = resultSet.getMetaData();
      colCount = metaData.getColumnCount();
      if (tableColumns.size() == 0) {
        // no DataWindowColumn has been manually added; create
        // tableColumns automatically; updates are NOT allowed
        allowUpdates = false;
        for (iCol = 0; iCol < colCount; iCol++) {
          // System.err.println(metaData.getColumnLabel(iCol +
          // 1).toLowerCase()+" "+metaData.getColumnTypeName(iCol +
          // 1));
          addColumn(metaData.getColumnName(iCol + 1).toLowerCase(), metaData.getTableName(iCol + 1), getDataWindowColumnType(metaData.getColumnType(iCol + 1)));
          getColumn(iCol).columnType = metaData.getColumnType(iCol + 1);
        }
      } else if (colCount != tableColumns.size()) {
        throw new Exception("JWebDataWindow DataWindowColumn count does not match Query DataWindowColumn count");
      }
      if (serverSide)
        iToRow = 0;
      while (resultSet.next()) {
        iRow = addRow(1);
        // if (iRow%10000 == 0) System.err.println("..." + iRow);
        row = (DataWindowRowLite)rows.get(iRow);
        populateRow(row);
        iRow++;
        if (iRow > iToRow) {
          break;
        }
      }
      updateFlag = false;
      return iRow;
    } catch (Exception e) {
      System.err.println("SQL [" + q + "]");
      throw e;
    } finally {
      if (!serverSide && resultSet != null) {
        resultSet.close();
      }
      if (!serverSide && statement != null) {
        statement.close();
      }
    }
  }

  public void saveAs(int fileType, String fileName) throws Exception {
    saveAs(fileType, fileName, false, true);
  }


  private void saveAsText(String fileName, boolean saveColumnNames, boolean createNew) throws Exception {
    // WritableWorkbook wwb;
    FileWriter file;
    String delimiter = getDelimiter();
    StringBuffer colStr = new StringBuffer();
    if (createNew) {
      file = new FileWriter(fileName);
    } else {
      file = new FileWriter(fileName, true);
      // colStr.append(JWebUtils.getNewLineChar());
    }
    for (int j = 0; j < getColumnCount(); j++) {
      String colName = getColumnName(j);
      if (this.columnHeaders != null) {
        colName = columnHeaders[j];
      }
      if (JWebUtils.isNull(colName))
        colName = "Column" + j;
      colName = JWebUtils.getToken(colName, ".", ".");
      colStr.append(colName);
      if (j + 1 < getColumnCount())
        colStr.append(delimiter);
    }
    colStr.append(JWebUtils.getNewLineChar());
    if (saveColumnNames)
      file.write(colStr.toString());
    for (int i = 0; i < getRowCount(); i++) {
      if (i == 0)
        colStr = new StringBuffer();
      else
        colStr = new StringBuffer(JWebUtils.getNewLineChar());
      for (int j = 0; j < getColumnCount(); j++) {
        String colValue = "";
        try {
          colValue = getItem(i, j);
        } catch (Exception ex) {
          System.err.println(i + "," + j + " " + this.getRowAsText(i));
          ex.printStackTrace();
          // System.exit(0);
        } finally {
        }
        colStr.append(JWebUtils.isNull(colValue) ? " " : colValue);
        if (j + 1 < getColumnCount())
          colStr.append(delimiter);
      }
      file.write(colStr.toString());
    }
    file.write(JWebUtils.getNewLineChar());
    file.close();
  }
  private String delimiter = ",";

  public String getDelimiter() {
    return delimiter;
  }

  public void setDelimiter(String delimiter) {
    this.delimiter = delimiter;
  }

  public void saveAs(int fileType, String fileName, boolean saveColumnNames, boolean createNew) throws Exception {
    if (fileType == DB_TABLE) {
      prepareForUpdate(fileName);
      createTable(fileName);
      update();
      commit();
    } else if (fileType == TEXT) {
      saveAsText(fileName, saveColumnNames, createNew);
    }
  }

  private void adjustColumnLength(int atCol, String itemValue) {
    if (JWebUtils.isNull(itemValue)) {
      return;
    }
    DataWindowColumnLite col = getColumn(atCol);
    if (col.length < itemValue.length()) {
      col.length = itemValue.length();
    }
  }

  public int retrieveAndUpdate(String table, String aFileName, int blockSize) throws Exception {
    this.setQuery("Select * from " + table + " Where 1 <> 1 ");
    this.retrieve();
    this.prepareForUpdate(table);
    int row = this.addRow();
    String colValue = "";
    File fFile = new File(aFileName);
    Scanner scanner = new Scanner(fFile);
    Scanner lineScanner;
    boolean rowSetCreated=false;
    int rowsUpdated=0;
    addRow();
    try {
      //first use a Scanner to get each line
      while (scanner.hasNextLine()) {
        String aLine = scanner.nextLine();
        
        //System.err.println(lineNo+ ": "+aLine);
        lineScanner = new Scanner(aLine);
        lineScanner.useDelimiter("\\|");
        for (int col = 0; col < getColumnCount(); col++) {
          if (lineScanner.hasNext()) {
            colValue = lineScanner.next();
            setItem(row, col, colValue);
          }
        }
        lineScanner.close();
        row++;
        rowsUpdated++;
        if (row == blockSize) {
          //this.printAsString("Tickets");
          rowSetCreated = true;
          if (scanner.hasNextLine()) this.update(0, row);
          row = 0;
        }
        if (!rowSetCreated) {
          addRow();
        }
      }
      this.update(0, row);
    } finally {
      //ensure the underlying stream is always closed
      scanner.close();
    }
    return rowsUpdated;
  }

  private int retrieveWithoutMetaData(Object[] args, int iFromRow, int iToRow) throws Exception, SQLException {
    if (JWebUtils.isNull(sqlQuery))
      throw new Exception("JWebDataWindow query is NULL");
    ResultSet resultSet = null;
    Statement statement = null;
    if (args == null)
      args = new Object[0];
    boolean userHasCreatedCols = false;
    try {
      String q;
      retrievalArguments = new Object[args.length];
      System.arraycopy(args, 0, retrievalArguments, 0, args.length);
      q = sqlQuery;
      boolean argProcessed = false;
      for (int i = 1; i <= args.length; i++) {
        argProcessed = false;
        String[] arg = JWebUtils.getStringList((String)args[i - 1], "=");
        if (arg != null && arg.length > 1) {
          if (q.indexOf(":" + arg[0]) >= 0) {
            q = JWebUtils.globalReplace(q, ":" + arg[0], arg[1]);
            argProcessed = true;
          }
        }
        if (!argProcessed) {
          q = JWebUtils.globalReplace(q, ":" + String.valueOf(i), String.valueOf(args[i - 1]));
        }
      }
      // this.args = args;
      // System.err.println(q);
      boolean firstTime = true;
      int colCount, iCol, iRow = 0;
      int[] colMap = null;
      ResultSetMetaData metaData;
      if (con == null)
        throw new Exception("No Connection is available");
      statement = con.createStatement();
      // System.err.println("Query No:"+getName()+" created. SQL= "+q.substring(0,100));
      long startTime = System.currentTimeMillis();
      if (this.inDebug()) {
        System.err.println("Query:" + q);
      }
      resultSet = statement.executeQuery(q);
      if (this.inDebug()) {
        System.err.println("Database Query Execution Time:" + (System.currentTimeMillis() - startTime) + " ms");
      }
      DataWindowRowLite row;
      JWebCellLite dataItem;
      metaData = resultSet.getMetaData();
      colCount = metaData.getColumnCount();
      if (tableColumns.size() == 0) {
        // no DataWindowColumn has been manually added; create
        // tableColumns automatically; updates are NOT allowed
        allowUpdates = false;
        for (iCol = 0; iCol < colCount; iCol++) {
          // System.err.println(iCol+":"+metaData.getColumnName(iCol +
          // 1)+" "+metaData.getColumnLabel(iCol +
          // 1).toLowerCase()+" "+metaData.getColumnTypeName(iCol +
          // 1));
          addColumn(metaData.getColumnName(iCol + 1).toLowerCase(), "thisTable", getDataWindowColumnType(metaData.getColumnType(iCol + 1)));
          getColumn(iCol).columnType = metaData.getColumnType(iCol + 1);
        }
      } else {
        userHasCreatedCols = true;
        colMap = new int[colCount];
        for (int i = 0; i < colMap.length; i++) {
          colMap[i] = -1;
        }
        // throw new
        // Exception("JWebDataWindow DataWindowColumn count does not match Query DataWindowColumn count");
      }
      while (resultSet.next()) {
        iRow = addRow(1);
        // if (iRow%10000 == 0) System.err.println("..." + iRow);
        row = (DataWindowRowLite)rows.get(iRow);
        for (iCol = 0; iCol < colCount; iCol++) {
          if (userHasCreatedCols) {
            if (colMap[iCol] == -1) {
              String colName = metaData.getColumnName(iCol + 1);
              colMap[iCol] = iCol;
            }
            dataItem = (JWebCellLite)row.dataItems.get(colMap[iCol]);
          } else {
            dataItem = (JWebCellLite)row.dataItems.get(iCol);
          }
          if (((DataWindowColumnLite)tableColumns.get(iCol)).dbType == TYPE_LONG) {
            dataItem.currentValue = resultSet.getString(iCol + 1);
          } else if (((DataWindowColumnLite)tableColumns.get(iCol)).dbType == TYPE_DATE) {
            String s = resultSet.getString(iCol + 1);
            if (JWebUtils.isNull(s))
              dataItem.currentValue = s;
            else {
              java.util.Date d = JWebUtils.convertStringToDate2(s);
              dataItem.currentValue = JWebUtils.convertDateToString2(d);
            }
          } else
            dataItem.currentValue = resultSet.getString(iCol + 1);
          dataItem.originalValue = dataItem.currentValue;
          dataItem.itemStatus = STATUS_NOTMODIFIED;
          adjustColumnLength(iCol, dataItem.currentValue);
        }
        iRow++;
        if (iRow > iToRow)
          break;
      }
      updateFlag = false;
      return iRow;
    } catch (Exception e) {
      System.err.println("SQL [" + sqlQuery + "]");
      throw e;
    } finally {
      if (resultSet != null) {
        resultSet.close();
      }
      if (statement != null) {
        statement.close();
      }
    }
  }
  private int startBlockRow = 1;
  private int endBlockRow = 0;

  public int retrieveInBlocks(String blockColName, int blockSize) throws Exception, SQLException {
    int rowsRetrieved = 0;
    startBlockRow = endBlockRow + 1;
    endBlockRow = endBlockRow + blockSize;
    rowsRetrieved = retrieve(new Object[0], startBlockRow, endBlockRow);
    if (rowsRetrieved < blockSize) {
      startBlockRow = 1;
      endBlockRow = 0;
    }
    return rowsRetrieved;
  }

  public int retrieveInBlocks(int blockSize) throws Exception, SQLException {
    int rowsRetrieved = 0;
    startBlockRow = endBlockRow + 1;
    endBlockRow = endBlockRow + blockSize;
    rowsRetrieved = retrieve(new Object[0], startBlockRow, endBlockRow);
    if (rowsRetrieved < blockSize) {
      startBlockRow = 1;
      endBlockRow = 0;
    }
    return rowsRetrieved;
  }

  public int retrieveUsingStoredProcedure(Object[] args, int iFromRow, int iToRow) throws Exception, SQLException {
    // System.err.println(" ColSize: "+tableColumns.size());
    if (iToRow < iFromRow)
      iToRow = Integer.MAX_VALUE;
    if (JWebUtils.isNull(sqlQuery)) {
      throw new Exception("JWebDataWindow query is NULL");
    }
    resultSet = null;
    CallableStatement statement = null;
    String q = sqlQuery;
    try {
      boolean firstTime = true;
      int colCount;
      int iCol;
      int iRow = 0;
      // ResultSetMetaData metaData;
      if (con == null) {
        throw new Exception("No Connection is available");
      }
      statement = con.prepareCall(sqlQuery);
      if (args != null) {
        retrievalArguments = new Object[args.length];
        System.arraycopy(args, 0, retrievalArguments, 0, args.length);
        boolean argProcessed = false;
        for (int i = 0; i < args.length; i++) {
          // System.err.println("ARG Classtype:"
          // +args[i].getClass().getName());
          if (JWebUtils.isSame(args[i].getClass().getName(), "java.lang.String")) {
            statement.setString(i + 1, args[i].toString());
          } else if (JWebUtils.isSame(args[i].getClass().getName(), "java.lang.Integer")) {
            statement.setInt(i + 1, Integer.parseInt(args[i].toString()));
          } else if (JWebUtils.isSame(args[i].getClass().getName(), "java.lang.Date")) {
            statement.setDate(i + 1, (java.sql.Date)(args[i]));
          } else {
            statement.setString(i + 1, args[i].toString());
          }
        }
      }
      long startTime = System.currentTimeMillis();
      resultSet = statement.executeQuery();
      try {
        resultSet.last();
        resultSetRowCount = resultSet.getRow();
        resultSet.beforeFirst();
      } catch (Exception e) {
        resultSetRowCount = 0;
      }
      if (this.inDebug()) {
        System.err.println("Database Query Execution Time:" + (System.currentTimeMillis() - startTime) + " ms");
      }
      DataWindowRowLite row;
      JWebCellLite dataItem;
      ResultSetMetaData metaData = resultSet.getMetaData();
      colCount = metaData.getColumnCount();
      if (tableColumns.size() == 0) {
        // no DataWindowColumn has been manually added; create
        // tableColumns automatically; updates are NOT allowed
        allowUpdates = false;
        for (iCol = 0; iCol < colCount; iCol++) {
          // System.err.println(metaData.getColumnLabel(iCol +
          // 1).toLowerCase()+" "+metaData.getColumnTypeName(iCol +
          // 1));
          addColumn(metaData.getColumnName(iCol + 1).toLowerCase(), metaData.getTableName(iCol + 1), getDataWindowColumnType(metaData.getColumnType(iCol + 1)));
          getColumn(iCol).columnType = metaData.getColumnType(iCol + 1);
        }
      } else if (colCount != tableColumns.size()) {
        throw new Exception("JWebDataWindow DataWindowColumn count does not match Query DataWindowColumn count");
      }
      if (serverSide)
        iToRow = 0;
      while (resultSet.next()) {
        iRow = addRow(1);
        // if (iRow%10000 == 0) System.err.println("..." + iRow);
        row = (DataWindowRowLite)rows.get(iRow);
        populateRow(row);
        iRow++;
        if (iRow > iToRow) {
          break;
        }
      }
      // fillNonDbColumns(iFromRow, iRow);
      updateFlag = false;
      return iRow;
    } catch (Exception e) {
      System.err.println("SQL [" + q + "]");
      throw e;
    } finally {
      if (resultSet != null && !serverSide) {
        resultSet.close();
      }
      if (!serverSide && statement != null) {
        statement.close();
      }
    }
  }

  private class DmlParm {
    Object parm;
    int type;

    DmlParm(Object parm, int type) {
      this.parm = parm;
      this.type = type;
    }
  }

  private class Dml {
    String dml;
    DmlParm[] parms;
    int dsRowNum;

    Dml(String dml, DmlParm[] parms, int dsRowNum) {
      this.dml = dml;
      this.parms = parms;
      this.dsRowNum = dsRowNum;
    }

    Object[] getParms() {
      Object[] ret = new Object[parms.length];
      for (int i = 0; i < parms.length; i++) {
        ret[i] = parms[i].parm;
      }
      return ret;
    }

    int[] getParmTypes() {
      int[] ret = new int[parms.length];
      for (int i = 0; i < parms.length; i++) {
        ret[i] = parms[i].type;
      }
      return ret;
    }
  }

  private int getDeletedRowsCount() {
    if (deletedRows != null)
      return deletedRows.size();
    else
      return 0;
  }

  private List<Dml> getDeleteDmls() throws Exception {
    int rowCount = getDeletedRowsCount();
    int colCount = getColumnCount();
    ArrayList<Dml> dmls = new ArrayList<Dml>();
    ArrayList<DmlParm> parms = new ArrayList<DmlParm>();
    ArrayList<String> conds = new ArrayList<String>();
    for (int iRow = 0; iRow < rowCount; iRow++) {
      for (int iCol = 0; iCol < colCount; iCol++) {
        DataWindowColumnLite col = getColumn(iCol);
        conds.add(getColumnName(iCol) + " = ?");
        if (col.key)
          switch (col.dbType) {
          case TYPE_CHARACTER:
            parms.add(new DmlParm(getDeletedItem(iRow, iCol), java.sql.Types.VARCHAR));
            break;
          case TYPE_DATE:
            parms.add(new DmlParm(getDateClause(getDeletedItem(iRow, iCol), false), Types.TIMESTAMP));
            break;
          case TYPE_NUMERIC:
            try {
              parms.add(new DmlParm(Long.valueOf(getDeletedItem(iRow, iCol)), java.sql.Types.BIGINT));
            } catch (NumberFormatException ex) {
              parms.add(new DmlParm(Float.valueOf(getDeletedItem(iRow, iCol)), java.sql.Types.FLOAT));
            }
            break;
          }
      }
      dmls.add(new Dml((new StringBuilder("DELETE FROM ").append(getUpdateTable()).append(" WHERE ").append(JWebUtils.listToStr(conds, " AND ", " AND "))).toString(), (DmlParm[])parms.toArray(new DmlParm[parms.size()]), iRow));
      parms = new ArrayList<DmlParm>();
      conds = new ArrayList<String>();
    }
    return dmls;
  }

  private Dml getUpdateDml(int iRow) throws Exception {
    int colCount = getColumnCount();
    List<String> conds = new ArrayList<String>();
    List<DmlParm> condsParms = new ArrayList<DmlParm>();
    List<String> mutes = new ArrayList<String>();
    List<DmlParm> mutesParms = new ArrayList<DmlParm>();
    for (int iCol = 0; iCol < colCount; iCol++) {
      DataWindowColumnLite col = getColumn(iCol);
      if ((col.updatable) && (col.dbTable.trim().equalsIgnoreCase(getUpdateTable().trim()))) {
        JWebCellLite dataItem = getCell(iRow, iCol);
        if (dataItem.itemStatus == STATUS_DATAMODIFIED) {
          switch (col.dbType) {
          case TYPE_CHARACTER:
            mutes.add(col.dbColumn + " = ?");
            if (JWebUtils.isNull(dataItem.currentValue))
              mutesParms.add(new DmlParm(null, Types.VARCHAR));
            else
              mutesParms.add(new DmlParm(dataItem.currentValue, Types.VARCHAR));
            if (JWebUtils.isNull(dataItem.originalValue))
              conds.add(col.dbColumn + " IS NULL");
            else {
              conds.add(col.dbColumn + " = ?");
              condsParms.add(new DmlParm(dataItem.originalValue, Types.VARCHAR));
            }
            break;
          case TYPE_DATE:
            if (JWebUtils.isNull(dataItem.currentValue)) {
              mutes.add(col.dbColumn + " = ?");
              mutesParms.add(new DmlParm(null, Types.DATE));
            } else {
              mutes.add(col.dbColumn + " = ?");
              mutesParms.add(new DmlParm(getDateClause(dataItem.currentValue, false), Types.TIMESTAMP));
            }
            break;
          case TYPE_NUMERIC:
            mutes.add(col.dbColumn + " = ?");
            if (JWebUtils.isNull(dataItem.currentValue))
              mutesParms.add(new DmlParm(null, Types.BIGINT));
            else {
              try {
                mutesParms.add(new DmlParm(Long.valueOf(dataItem.currentValue), Types.BIGINT));
              } catch (NumberFormatException ex) {
                mutesParms.add(new DmlParm(Float.valueOf(dataItem.currentValue), Types.FLOAT));
              }
            }
            if (JWebUtils.isNull(dataItem.originalValue))
              conds.add(col.dbColumn + " IS NULL");
            else {
              conds.add(col.dbColumn + " = ?");
              try {
                condsParms.add(new DmlParm(Long.valueOf(dataItem.originalValue), Types.BIGINT));
              } catch (NumberFormatException ex) {
                condsParms.add(new DmlParm(Float.valueOf(dataItem.originalValue), Types.FLOAT));
              }
            }
            break;
          }
        }
      }
    }
    // build where clause for the key
    for (int iCol = 0; iCol < colCount; iCol++) {
      DataWindowColumnLite col = getColumn(iCol);
      if (col.key) {
        conds.add(col.dbColumn + " = ?");
        JWebCellLite dataItem = getCell(iRow, iCol);
        switch (col.dbType) {
        case TYPE_CHARACTER:
          condsParms.add(new DmlParm(dataItem.originalValue, Types.VARCHAR));
          break;
        case TYPE_DATE:
          condsParms.add(new DmlParm(getDateClause(dataItem.originalValue), Types.TIMESTAMP));
          break;
        case TYPE_NUMERIC:
          try {
            condsParms.add(new DmlParm(Long.valueOf(dataItem.originalValue), Types.BIGINT));
          } catch (NumberFormatException ex) {
            condsParms.add(new DmlParm(Float.valueOf(dataItem.originalValue), Types.FLOAT));
          }
          break;
        }
      }
    }
    DmlParm[] allParms = new DmlParm[mutesParms.size() + condsParms.size()];
    for (int i = 0; i < mutesParms.size(); i++) {
      allParms[i] = mutesParms.get(i);
    }
    for (int i = 0; i < condsParms.size(); i++) {
      allParms[mutesParms.size() + i] = condsParms.get(i);
    }
    return new Dml((new StringBuilder("UPDATE ").append(getUpdateTable()).append(" SET ").append(JWebUtils.listToStr(mutes, ", ", ", ")).append(" WHERE ").append(JWebUtils.listToStr(conds, " AND ", " AND "))).toString(), allParms, iRow);
  }

  private Dml getInsertDml(int iRow) throws Exception {
    int colCount = getColumnCount();
    List<String> colnames = new ArrayList<String>();
    List<String> colvals = new ArrayList<String>();
    List<DmlParm> parms = new ArrayList<DmlParm>();
    for (int iCol = 0; iCol < colCount; iCol++) {
      DataWindowColumnLite col = getColumn(iCol);
      if ((col.updatable) && (col.dbTable.trim().equalsIgnoreCase(getUpdateTable().trim()))) {
        colnames.add(col.dbColumn);
        // System.out.println(">>>>" + col.dbColumn + " = " +
        // col.dbType);
        JWebCellLite dataItem = getCell(iRow, iCol);
        switch (col.dbType) {
        case TYPE_CHARACTER:
          colvals.add("?");
          if (JWebUtils.isNull(dataItem.currentValue))
            parms.add(new DmlParm(null, Types.VARCHAR));
          else
            parms.add(new DmlParm(dataItem.currentValue, Types.VARCHAR));
          break;
        case TYPE_DATE:
          colvals.add("?");
          if (JWebUtils.isNull(dataItem.currentValue))
            parms.add(new DmlParm(null, Types.DATE));
          else
            parms.add(new DmlParm(getDateClause(dataItem.currentValue, false), Types.TIMESTAMP));
          break;
        case TYPE_NUMERIC:
          colvals.add("?");
          if (JWebUtils.isNull(dataItem.currentValue))
            parms.add(new DmlParm(0, Types.BIGINT));
          else {
            try {
              parms.add(new DmlParm(Long.valueOf(dataItem.currentValue), Types.BIGINT));
            } catch (NumberFormatException ex) {
              //parms.add(new DmlParm(BigDecimal.valueOf(Double.valueOf(dataItem.currentValue)), Types.DECIMAL));
              parms.add(new DmlParm(Float.valueOf(dataItem.currentValue), Types.FLOAT));
            }
          }
          break;
        }
      }
    }
    return new Dml((new StringBuilder("INSERT INTO ").append(getUpdateTable()).append(" (").append(JWebUtils.listToStr(colnames, ", ", ", ")).append(") VALUES (").append(JWebUtils.listToStr(colvals, ", ", ", ") + ") ")).toString(), (DmlParm[])parms.toArray(new DmlParm[parms.size()]), iRow);
  }

  protected void executeUpdate(String q, Object[] args, int[] types) throws Exception {
    java.sql.PreparedStatement s = null;
    StringBuilder sb = new StringBuilder();
    try {
      s = con.prepareStatement(q);
      for (int i = 0; i < args.length; i++) {
        s.setObject(i + 1, args[i], types[i]);
        sb.append(args[i]).append(i < args.length - 1 ? ", " : "");
      }
      if (this.inDebug()) {
        System.err.println(q + " [" + sb.toString() + "]");
      }
      s.executeUpdate();
      if (q.substring(0, 6).equals("UPDATE") && (s.getUpdateCount() == 0)) {
        throw new Exception("Record Modified between retrieve and update");
      }
    } catch (Exception e) {
      throw new Exception(q + " [" + sb.toString() + "]", e);
    } finally {
      if (s != null) {
        try {
          s.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void update() throws Exception {
    update(0, getRowCount());
  }

  public void update(int blockSize) throws Exception {
    int rowsFrom = 0, rowsInBlock = 0;
    int rowCount = getRowCount();
    rowsInBlock = Math.min(blockSize, rowCount);
    update(0, rowsInBlock);
    int rowsUpdated = rowsInBlock;
    while (rowsUpdated < rowCount) {
      rowsFrom = rowsUpdated;
      rowsInBlock = (Math.min(rowCount, rowsUpdated + blockSize) - rowsUpdated);
      update(rowsFrom, rowsInBlock);
      //System.err.println(rowsFrom + "," + rowsInBlock);
      rowsUpdated = rowsUpdated + rowsInBlock;
    }
  }

  public void update(int startRow, int rowCount) throws Exception {
    if (getUpdateTable().trim().length() == 0)
      throw new Exception("Update failure: Update table not defined [" + getName() + "]");
    if (getUpdateFlag()) {
      List<Dml> dmls = getDeleteDmls();
      for (int iRow = startRow; iRow < startRow + rowCount; iRow++) {
        int rowStatus = serverSide ? STATUS_NEWMODIFIED : getRowStatus(iRow, BUFFER_CURRENT);
        if (rowStatus == STATUS_DATAMODIFIED)
          dmls.add(getUpdateDml(iRow));
        else if (rowStatus == STATUS_NEWMODIFIED)
          dmls.add(getInsertDml(iRow));
      }
      for (int i = 0; i < dmls.size(); i++) {
        Dml dml = dmls.get(i);
        executeUpdate(dml.dml, dml.getParms(), dml.getParmTypes());
      }
    }
  }
}
