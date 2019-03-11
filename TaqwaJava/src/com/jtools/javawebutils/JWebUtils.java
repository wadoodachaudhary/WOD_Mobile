/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
package com.jtools.javawebutils;

//import com.chilkatsoft.CkZip;
import com.jtools.javawebdatafront.JWebCellLite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;
import java.awt.Container;
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.Dialog;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import java.awt.*;
import java.sql.*;
import java.util.Vector;
import java.io.*;
import java.util.zip.*;
import java.lang.reflect.*;

import java.awt.image.*;
import java.math.*;
import java.awt.Rectangle;
import java.io.PrintStream;

import com.jtools.javawebdatafront.JWebDataWindowLite;

public class JWebUtils {

    private static String[] monthNames;
    static Frame parentFrame;
    static Frame mainFrame = null;
    private static String requestDateClause;
    private static String selectedList;

    public static String currentSQL;
    private static java.sql.Connection dbCon;
    private static int clientId;
    private static int ctr = 0;
    private static String lastName;
    private static int userId;
    private static ArrayList images;
    private static String userName;
    private static Timestamp time;
    private static String computerName;
    private static String region = "";
    private static String date;
    private static String seqNo;
    private static Connection con = null;
    private static int screenHeight = 0;
    private static int screenWidth = 0;
    private static Panel tab = null;
    private static String fieldName;
    private static Component anyComponent;
    //private static String userNameField = "guest";
    //private static String passwordField = "trustworthy";
    //private static String serverField   = "jdbc:borland:dslocal:d:\\jbuilder3\\myprojects\\jtman\\LocalDB\\DBwdlenzi";
    //private static String driverField   = "com.borland.JWebDataWindow.jdbc.JWebDataWindowDriver";
    private static String serverField = "jdbc:odbc:TMAN_ACCESS";
    //private static String serverField = "jdbc:odbc:TMAN_PARADOX";
    private static String driverField = "sun.jdbc.odbc.JdbcOdbcDriver";
    private static String userNameField = "";
    private static String passwordField = "";
    //private static String serverField   = "jdbc:odbc:TMANText";
    //private static String driverField   = "sun.jdbc.odbc.JdbcOdbcDriver";
    private static Vector index = new Vector();

    private static Set values = null;
    private static String iniFileName;
    private static Vector nameValue = new Vector();
    private static ArrayList fieldTypes = null;
    private static int conId = -1;

    public JWebUtils() {
    }

    public static boolean isOdd(int i) {
        return (i & 0x00000001) != 0;
    }

    private static boolean debugFlag = false;

    public static boolean inDebug() {
        return debugFlag;
    }

    public static void setDubugFlag(boolean _debugFlag) {
        debugFlag = _debugFlag;
    }

    public static void centerFrameOnScreen(Window frame) {
        positionFrameOnScreen(frame, 0.5, 0.5);
    }

    public static String getStr(double d, int lt) {
        lt = Math.min(Double.toString(d).length(), lt);
        return Double.toString(d).substring(0, lt);
    }

    /**
     * Positions the specified frame at a relative position in the screen, where
     * 50% is considered to be the center of the screen.
     *
     * @param frame the frame.
     * @param horizontalPercent the relative horizontal position of the frame
     * (0.0 to 1.0, where 0.5 is the center of the screen).
     * @param verticalPercent the relative vertical position of the frame (0.0
     * to 1.0, where 0.5 is the center of the screen).
     */
    public static void positionFrameOnScreen(Window frame, double horizontalPercent, double verticalPercent) {
        Dimension s = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension f = frame.getSize();
        int w = Math.max(s.width - f.width, 0);
        int h = Math.max(s.height - f.height, 0);
        int x = (int) (horizontalPercent * w);
        int y = (int) (verticalPercent * h);
        frame.setBounds(x, y, f.width, f.height);
    }

    /**
     * Positions the specified frame at a random location on the screen while
     * ensuring that the entire frame is visible (provided that the frame is
     * smaller than the screen).
     *
     * @param frame the frame.
     */
    public static void positionFrameRandomly(Window frame) {
        positionFrameOnScreen(frame, Math.random(), Math.random());
    }

    /**
     * Positions the specified dialog within its parent.
     *
     * @param dialog the dialog to be positioned on the screen.
     */
    public static void centerDialogInParent(Dialog dialog) {
        positionDialogRelativeToParent(dialog, 0.5, 0.5);
    }

    /**
     * Positions the specified dialog at a position relative to its parent.
     *
     * @param dialog the dialog to be positioned.
     * @param horizontalPercent the relative location.
     * @param verticalPercent the relative location.
     */
    public static void positionDialogRelativeToParent(Dialog dialog, double horizontalPercent, double verticalPercent) {
        Dimension d = dialog.getSize();
        Container parent = dialog.getParent();
        Dimension p = parent.getSize();
        int baseX = parent.getX() - d.width;
        int baseY = parent.getY() - d.height;
        int w = d.width + p.width;
        int h = d.height + p.height;
        int x = baseX + (int) (horizontalPercent * w);
        int y = baseY + (int) (verticalPercent * h);
        // make sure the dialog fits completely on the screen...
        Dimension s = Toolkit.getDefaultToolkit().getScreenSize();
        x = Math.min(x, (s.width - d.width));
        x = Math.max(x, 0);
        y = Math.min(y, (s.height - d.height));
        y = Math.max(y, 0);
        dialog.setBounds(x, y, d.width, d.height);
    }

    /**
     * Creates a panel that contains a table based on the specified table model.
     *
     * @param model the table model to use when constructing the table.
     *
     * @return the panel.
     */
    /**
     * Creates a label with a specific font.
     *
     * @param text the text for the label.
     * @param font the font.
     *
     * @return the label.
     */
    /**
     * Creates a label with a specific font and color.
     *
     * @param text the text for the label.
     * @param font the font.
     * @param color the color.
     *
     * @return the label.
     */
    /**
     * Creates a JButton.
     *
     * @param label the label.
     * @param font the font.
     *
     * @return the button.
     */

    /**
     * A utility method for drawing rotated text.
     * <p/>
     * A common rotation is -Math.PI/2 which draws text 'vertically' (with the
     * top of the characters on the left).
     *
     * @param text the text.
     * @param g2 the graphics device.
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     * @param rotation the clockwise rotation (in radians).
     */
    public static void drawRotatedString(String text, Graphics2D g2, float x, float y, double rotation) {
        if ((text == null) || (text.equals(""))) {
            return;
        }
        AffineTransform saved = g2.getTransform();
        // apply the rotation...
        AffineTransform rotate = AffineTransform.getRotateInstance(rotation, x, y);
        g2.transform(rotate);
        // workaround for JDC bug ID 4312117 and others...
        //TextLayout tl = new TextLayout(text, g2.getFont(), g2.getFontRenderContext());
        //tl.draw(g2, x, y);
        // replaces this code...
        g2.drawString(text, x, y);
        g2.setTransform(saved);
    }

    /**
     * Returns a point based on (x, y) but constrained to be within the bounds
     * of a given rectangle.
     *
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     * @param area the constraining rectangle.
     * @return a point within the rectangle.
     */
    public static Point2D getPointInRectangle(double x, double y, Rectangle2D area) {
        x = Math.max(area.getMinX(), Math.min(x, area.getMaxX()));
        y = Math.max(area.getMinY(), Math.min(y, area.getMaxY()));
        return new Point2D.Double(x, y);
    }

    public static NumberCheck numberCheck = new NumberCheck();
    //private static DataBrokerService dataBroker;

    static class NumberCheck extends java.awt.event.KeyAdapter {

        public NumberCheck() {
        }

        public void keyReleased(KeyEvent e) { //testField.setHorizontalAlignment(SwingConstants.RIGHT);
            TextField testField = (TextField) e.getComponent(); //getSource();
            boolean isRed = false;
            if (!testField.getText().equals("")) {
                try { //Double.parseDouble(testField.getText());
                    Integer.parseInt(testField.getText());
                } catch (Exception E) {
                    isRed = true;
                }
            }
            if (isRed) {
                testField.setBackground(Color.red);
            } else {
                testField.setBackground(Color.white);
            }
        }
    }

    static class NameValuePair {

        String name, value;

        public NameValuePair(String _name, String _value) {
            name = _name;
            value = _value;
        }
    }

    static class ControlValue {

        String name, value;

        public ControlValue(String _name, String _value) {
            name = _name;
            value = _value;
        }
    }
    /* public static void setDataBroker(DataBrokerService _dataBroker) {
     dataBroker = _dataBroker;
     }
     public static DataBrokerService getDataBroker() {
     return dataBroker;
     }*/

    public static void setRemoteConId(int _conId) {
        conId = _conId;
    }

    public static int getRemoteConId() {
        return conId;
    }

    static class ColValue {

        String name, value;

        public ColValue(String _name, String _value) {
            name = _name;
            value = _value;
        }
    }

    public static void setSelectedList(String _selectedList) {
        selectedList = _selectedList;
    }

    public static String getSelectedList() {
        return selectedList;
    }

    static class IndexName {

        String name;
        Vector data;

        public IndexName(String _name, Vector _data) {
            name = _name;
            data = _data;
        }
    }

    public static void commit() {
        try {
            if (conId == -1) {
                getDBCon().commit(); //else dataBroker.commit(conId)*/;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void rollback() {
        try {
            if (conId == -1) {
                getDBCon().rollback(); //*else dataBroker.rollback(conId);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void maxamizeFrame(Frame frame) {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        d.height = d.height - 25;
        frame.setSize(d);
        frame.setLocation(0, 0);
    }

    public static String getUserType() {
        return "REP";
    }

    public static void openIniFile(String fileName) {
        try {
            iniFileName = fileName;
            BufferedReader in = new BufferedReader(new FileReader(new File(fileName)));
            String inputLine = "";
            while ((inputLine = in.readLine()) != null) {
                String name, value;
                if (inputLine.trim().equalsIgnoreCase("")) {
                    continue;
                }
                int posEqualSign = inputLine.indexOf("=");
                name = inputLine.substring(0, posEqualSign);
                value = inputLine.substring(posEqualSign + 1);
                NameValuePair nameValuePair = new NameValuePair(name.trim(), value.trim());
                nameValue.addElement(nameValuePair);
            }
            in.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public static void saveIniFile() {
        try {
            String newLine = System.getProperty("line.separator");
            FileWriter out = new FileWriter(iniFileName);
            for (int i = 0; i < nameValue.size(); i++) {
                NameValuePair pair = (NameValuePair) nameValue.elementAt(i);
                out.write(pair.name + "=" + pair.value + newLine);
            }
            out.flush();
            out.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        ;
    }

    public static String getDefaultValue(String name, String defaultValue) {
        for (int i = 0; i < nameValue.size(); i++) {
            NameValuePair pair = (NameValuePair) nameValue.elementAt(i);
            if (pair.name.equalsIgnoreCase(name)) {
                return pair.value;
            }
        }
        return defaultValue;
    }

    public static void setDefaultValue(String name, String value) {
        for (int i = 0; i < nameValue.size(); i++) {
            NameValuePair pair = (NameValuePair) nameValue.elementAt(i);
            if (pair.name.equalsIgnoreCase(name)) {
                pair.value = value;
                return;
            }
        }
        NameValuePair nameValuePair = new NameValuePair(name, value);
        nameValue.addElement(nameValuePair);
    }

    public static boolean isNull(String s) {
        if (s == null) {
            return true;
        }
        s = s.trim();
        if (s.length() == 0 || s.equalsIgnoreCase("") || s.equalsIgnoreCase("<NONE>") || s.equalsIgnoreCase("null")) {
            return true;
        }
        return false;
    }

    public static String isNull(String s, String result) {
        if (s == null) {
            return result;
        }
        s = s.trim();
        if (s.length() == 0 || s.equalsIgnoreCase("") || s.equalsIgnoreCase("<NONE>") || s.equalsIgnoreCase("null")) {
            return result;
        }
        return s;
    }

    public static boolean isNull(StringBuffer s) {
        if (s == null) {
            return true;
        }
        return isNull(s.toString());
    }

    public static boolean isEmpty(String s) {
        if (s == null) {
            return true;
        }
        s = s.trim();
        if (s.length() == 0 || s.equalsIgnoreCase("") || s.equalsIgnoreCase("<NONE>") || s.equalsIgnoreCase("null")) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(StringBuffer s) {
        if (s == null) {
            return true;
        }
        return isNull(s.toString());
    }

    public static boolean isNotNull(String s) {
        return !isNull(s);
    }

    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }

    public static java.sql.Connection getDBCon() {
        return dbCon;
    }

    public static String getFieldName() {
        return fieldName;
    }

    public static void setFieldName(String _fieldName) {
        if (_fieldName.equalsIgnoreCase("TARIFF_TYPE")) {
            _fieldName = "TARIFF TYPE";
        }
        fieldName = _fieldName;
    }

    public static String getUserName() {
        return userName;
        //return "wadood";
    }

    public static void setUserName(String _userName) {
        userName = _userName;
    }

    public static String getRequestDateClause() {
        return requestDateClause;
    }

    public static void setRequestDateClause(String _requestDateClause) {
        requestDateClause = _requestDateClause;
    }

    public static String getColumnValue(String q, String col) {
        String result = null;
        //System.out.println(q);
        try {
            JWebDataWindowLite ds = new JWebDataWindowLite(dbCon);
            ds.setQuery(q);
            ds.retrieve();
            if (ds.getRowCount() > 0) {
                result = ds.getItem(0, col.toLowerCase());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void executeUpdate(String q) {
        java.sql.Connection con = getDBCon();
        java.sql.Statement s = null;
        try {
            s = con.createStatement();
            s.executeUpdate(q);
        } catch (Exception e) {
            e.printStackTrace();
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

    public static void executeUpdate(java.sql.Connection con, String q) {
        java.sql.Statement s = null;
        try {
            s = con.createStatement();
            s.executeUpdate(q);
        } catch (Exception e) {
            e.printStackTrace();
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

    public static void setDBCon(java.sql.Connection a) {
        dbCon = a;
    }

    public static java.sql.Connection connectToDB(String url, String id, String pwd, String driver) throws SQLException, Exception {
        java.sql.Connection con = null;
        Class.forName(driver);
        con = DriverManager.getConnection(url, id, pwd);
        return con;
    }

    public static void setConnection(java.sql.Connection _dbCon) {
        dbCon = _dbCon;
    }

    public static void openDBCon(String serverField, String userNameField, String passwordField, String driverField) throws Exception {
        dbCon = connectToDB(serverField, userNameField, passwordField, driverField);
    }

    public static void closeDBCon() {
        try {
            dbCon.close();
        } catch (Exception ex) {
            System.err.println("Cannot disconnect to this database.");
        }
    }

    public static java.util.Date getDate(String dateStr) {
        return convertStringToDate2(dateStr);
    }

    public static java.util.Date getDate(String dateStr, String formatStr) {
        try {
            java.text.SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
            sdf.setLenient(true);
            return sdf.parse(dateStr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new java.util.Date();
    }

    public static void setDate(String value) {
        date = value;
    }

    private static void populateMonthNames() {
        Calendar cal = Calendar.getInstance();
        monthNames = new String[cal.getMaximum(Calendar.MONTH) - cal.getMinimum(Calendar.MONTH) + 1];
        for (int i = 0; i < monthNames.length; i++) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM");
            cal.set(1999, i, 1);
            monthNames[i] = sdf.format(cal.getTime());
        }
    }

    public static int getDecimalPlaces(double d) {
        String str0 = JWebUtils.replicate("0", 10);
        NumberFormat rateFormatter = new DecimalFormat("#." + str0 + ";(#." + str0 + ")");
        String dbl = rateFormatter.format(d);
        int decimalPos = dbl.indexOf(".");
        int trailingZeroCt = 0;
        for (int i = dbl.length() - 1; i >= decimalPos; i--) {
            if (dbl.substring(i, i + 1).equalsIgnoreCase("0")) {
                trailingZeroCt++;
            } else if (dbl.substring(i, i + 1).equalsIgnoreCase(")")) {
                trailingZeroCt++;
            } else {
                break;
            }
        }
        int decimalPoints = dbl.substring(decimalPos).length() - trailingZeroCt - 1;
        return decimalPoints;
        //    int decimals=0;
        //    while ((int)d % 10 ==0){
        //      d*=10;
        //      decimals++;
        //    }
        //    return decimals+1;
    }

    public static void execStoreProcedure(Connection con, String sp, String[] in, Object out) throws Exception {
        CallableStatement cstmt = null;
        String params = "(";
        for (int i = 0; i < in.length; i++) {
            if (i > 0) {
                params = params + ",?";
            } else {
                params = params + "?";
            }
        }
        params = params + ")";
        System.err.println(params);
        cstmt = con.prepareCall("{call " + sp + params + "}");
        for (int i = 0; i < in.length; i++) {
            cstmt.setString(i + 1, in[i]);
        }
        //cstmt.registerOutParameter(3, Types.VARCHAR);
        cstmt.execute(); // Execute the callable statement
        con.commit();
        cstmt.close();
        return;
        //String dbNames = cstmt.getString(3);
        //return dbNames;
    }

    public static void execStoreProcedure(Connection con, String sp, int[] in, Object out) throws Exception {
        CallableStatement cstmt = null;
        String params = "(";
        for (int i = 0; i < in.length; i++) {
            if (i > 0) {
                params = params + ",?";
            } else {
                params = params + "?";
            }
        }
        params = params + ")";
        //System.err.println(params);
        cstmt = con.prepareCall("{call " + sp + params + "}");
        for (int i = 0; i < in.length; i++) {
            cstmt.setInt(i + 1, in[i]);
        }
        //cstmt.registerOutParameter(3, Types.VARCHAR);
        cstmt.execute(); // Execute the callable statement
        con.commit();
        cstmt.close();
        return;
        //String dbNames = cstmt.getString(3);
        //return dbNames;
    }

    public static String addToCommaList(String textList, String delim, String toAdd) {
        if (isNull(textList)) {
            return toAdd;
        }
        String[] result = getStringList(textList, delim);
        for (int i = 0; i < result.length; i++) {
            if (result[i].trim().equalsIgnoreCase(toAdd)) {
                return textList;
            }
        }
        if (textList.equals("")) {
            textList = toAdd;
        } else {
            textList = textList + "," + toAdd;
        }
        return textList;
    }

    public static String[] getStringList(String textList, String delim) {
        return getStringList(textList, delim, true);
    }

    public static String[] getStringList(String textList) {
        return getStringList(textList, ",", true);
    }

    public static String[] getStringList(String textList, String delim, boolean trimmed) {
        if (isNull(textList)) {
            return null;
        }
        StringTokenizer parser = new StringTokenizer(textList, delim);
        String[] result = new String[parser.countTokens()];
        int i = 0;
        while (parser.hasMoreTokens()) {
            String text = parser.nextToken();
            if (trimmed) {
                result[i] = text.trim();
            } else {
                result[i] = text;
            }
            i++;
        }
        return result;
    }

    public static String[] getStringListWithSpacesRemoved(String textList) {
        if (isNull(textList)) {
            return null;
        }
        StringTokenizer parser = new StringTokenizer(textList, " ");
        ArrayList list = new ArrayList();
        while (parser.hasMoreTokens()) {
            String text = parser.nextToken();
            if (JWebUtils.isNotNull(text)) {
                list.add(text);
            }
        }
        return getArrayListAsStringArray(list);
    }

    public static int[] getIntList(String textList, String delim) {
        if (textList == null) {
            return null;
        }
        StringTokenizer parser = new StringTokenizer(textList, delim);
        int[] result = new int[parser.countTokens()];
        int i = 0;
        while (parser.hasMoreTokens()) {
            String text = parser.nextToken();
            result[i] = Integer.parseInt(text);
            i++;
        }
        return result;
    }

    public static int[] getIntList(ArrayList list) {
        if (list == null) {
            return null;
        }
        int[] result = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = ((Integer) list.get(i)).intValue();
        }
        return result;
    }

    public static String getStringFromRectangle(Rectangle rect) {
        return rect.x + "," + rect.x + "," + rect.width + "," + rect.height;
    }

    public static Rectangle getRectangleFromString(String textList, String delim) {
        if (textList == null) {
            return null;
        }
        int[] resultStr = getIntList(textList, delim);
        if (resultStr.length != 4) {
            return null;
        }
        Rectangle result = new Rectangle(resultStr[0], resultStr[1], resultStr[2], resultStr[3]);
        return result;
    }

    public static String ifConcat(String source, String toAppend, String seprator) {
        if (isNull(source)) {
            source = toAppend;
        } else {
            source = source + seprator + toAppend;
        }
        return source;
    }

    public static StringBuffer ifConcat(StringBuffer source, String toAppend, String seprator) {
        if (isNull(source)) {
            if (source == null) {
                source = new StringBuffer(toAppend);
            } else {
                source.append(toAppend);
            }
        } else {
            source = source.append(seprator).append(toAppend);
        }
        return source;
    }

    public static String getToken(String text, String delimEnd) {
        if (text == null) {
            return null;
        }
        String result = null;
        int startPos = 0;
        int endPos = text.indexOf(delimEnd);
        if (startPos >= 0) {
            if (endPos >= 0 && endPos > startPos) {
                result = text.substring(startPos, endPos);
            } else {
                result = text.substring(startPos);
            }
        }
        return result;
    }

    public static String[] getTokenList(String text, String delimStart, String delimEnd, String delimResult) {
        if (text == null) {
            return null;
        }
        String result = null;
        int startPos = text.indexOf(delimStart);
        if (startPos >= 0) {
            int endPos = text.indexOf(delimEnd, startPos);
            if (endPos >= 0 && endPos > startPos) {
                result = text.substring(startPos + 1, endPos);
            } else {
                result = text.substring(startPos + 1);
            }
        }
        return getStringList(result, delimResult);

    }

    public static String getToken(String text, String delimStart, String delimEnd) {
        if (text == null) {
            return null;
        }
        String result = text;
        int startPos = text.indexOf(delimStart);
        if (startPos >= 0) {
            int endPos = text.indexOf(delimEnd, startPos);
            if (endPos >= 0 && endPos > startPos) {
                result = text.substring(startPos + delimStart.length(), endPos);
            } else {
                result = text.substring(startPos + delimStart.length());
            }
        }
        return result;
    }

    public static String getTokenAfter(String text, String delim) {
        if (text == null) {
            return null;
        }
        String result = null;
        int startPos = text.indexOf(delim);
        if (startPos >= 0) {
            result = text.substring(startPos + delim.length());
        }
        return result;
    }

     public static String getTokenBefore(String text, String delim) {
        if (text == null) {
            return null;
        }
        String result = null;
        int endPos = text.indexOf(delim);
        if (endPos >= 0) {
            result = text.substring(0,endPos);
        }
        return result;
    }

    
    public static String[] getTokenList(String text, String delimStart, String delimEnd) {
        if (text == null || !(text.contains(delimStart) && text.contains(delimEnd))) {
            return null;
        }
        
        int startPos;
        startPos = text.indexOf(delimStart);
        
        ArrayList<String> result = new ArrayList<String>();
          
        while (startPos >= 0) {
            int endPos = text.indexOf(delimEnd,startPos);
            if (endPos >= 0 && endPos > startPos) {
                result.add(text.substring(startPos + delimStart.length(), endPos));
            } else {
                result.add(text.substring(startPos + delimStart.length()));
            }
            startPos = text.indexOf(delimStart, endPos + 1);
            //System.out.println(text+"  StartPos:"+startPos);
        }
        return getArrayListAsStringArray(result);
    }

    public static String[] getXMLTokenList(String text, String tag) {
        if (text == null) {
            return null;
        }
        String delimStart = "<" + tag + ">";
        String delimEnd = "</" + tag + ">";
        ArrayList<String> result = new ArrayList<String>();

        int startPos = text.indexOf(delimStart);
        while (startPos >= 0) {
            int endPos = text.indexOf(delimEnd, startPos);
            if (endPos >= 0 && endPos > startPos) {
                result.add(text.substring(startPos + delimStart.length(), endPos));
            } else {
                result.add(text.substring(startPos + delimStart.length()));
            }
            startPos = text.indexOf(delimStart, endPos + 1);
        }
        return getArrayListAsStringArray(result);
    }

    public static String getXMLToken(String text, String tag) {
        if (text == null) {
            return null;
        }
        String delimStart = "<" + tag + ">";
        String delimEnd = "</" + tag + ">";
        String result = null;
        int startPos = text.indexOf(delimStart);
        if (startPos >= 0) {
            int endPos = text.indexOf(delimEnd, startPos);
            if (endPos >= 0 && endPos > startPos) {
                result = text.substring(startPos + delimStart.length(), endPos);
            } else {
                result = text.substring(startPos + delimStart.length());
            }
        }
        return result;
    }

    public static String[] getTagList(String textList, String delimStart, String delimEnd) {
        if (textList == null) {
            return null;
        }
        StringTokenizer parser = new StringTokenizer(textList, delimStart + delimEnd, false);
        if (parser.countTokens() == 1) {
            return null;
        }
        String[] result = new String[parser.countTokens()];
        int i = 0;
        while (parser.hasMoreTokens()) {
            String text = parser.nextToken();
            result[i] = text;
            i++;
        }
        return result;
    }

    public static String getTags(String textList, String delimStart, String delimEnd, String seprator) {
        if (textList == null) {
            return null;
        }
        StringTokenizer parser = new StringTokenizer(textList, delimStart + delimEnd, false);
        if (parser.countTokens() == 1) {
            return "";
        }
        //System.err.println("text=" + textList);
        String result = "";
        int i = 0;
        String text;
        while (parser.hasMoreTokens()) {
            parser.nextToken();
            if (parser.hasMoreTokens()) {
                text = parser.nextToken();
                if (result.equals("")) {
                    result = text;
                } else {
                    result = result + seprator + text;
                }
            }
        }
        return result;
    }

    public static String getTextFromList(String[] textList, String delimiter) {
        if (textList == null) {
            return null;
        }
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < textList.length; i++) {
            if (i == 0) {
                result.append(textList[i]);
            } else {
                result.append(delimiter + textList[i]);
            }
        }
        return result.toString();
    }

    public static String getCommaText(String[] textList) {
        if (textList == null) {
            return null;
        }
        String result = "";
        for (int i = 0; i < textList.length; i++) {
            if (result.equals("")) {
                result = textList[i];
            } else {
                result = result + "," + textList[i];
            }
        }
        return result;
    }

    public static String getCommaText(Vector textList) {
        if (textList == null) {
            return null;
        }
        String result = "";
        for (int i = 0; i < textList.size(); i++) {
            if (result.equals("")) {
                result = textList.get(i).toString();
            } else {
                result = result + "," + textList.get(i).toString();
            }
        }
        return result;
    }

    public static Vector getData(String fieldName) {
        //if (index.size()==0) create();
        for (int i = 0; i < index.size(); i++) {
            IndexName col = (IndexName) index.elementAt(i);
            //System.out.println(col.name+":"+fieldName);
            if (col.name.trim().equalsIgnoreCase(fieldName.trim())) {
                return col.data;
            }
        }
        return null;
    }

    static void createData(Vector list) {
        Vector values = new Vector();
        ColValue col = (ColValue) list.elementAt(0);
        String fieldName = col.name;
        for (int i = 0; i < list.size(); i++) {
            col = (ColValue) list.elementAt(i);
            if (col.name.equalsIgnoreCase(fieldName)) {
                values.add(col.value);
            } else {
                IndexName idx = new IndexName(fieldName, values);
                index.addElement(idx);
                values = new Vector();
                fieldName = col.name;
                values.add(col.value);
            }
        }
        IndexName idx = new IndexName(fieldName, values);
        index.addElement(idx);
    }

    public static String replaceStr(String source, String searchFor, String replaceWith) {
        boolean stop = false;
        String retStr = source.substring(0);
        int start = retStr.indexOf(searchFor);
        int searchForLength = searchFor.length();
        int replaceWithLength = replaceWith.length();
        while (start >= 0) {
            retStr = retStr.substring(0, start) + replaceWith + retStr.substring(start + searchForLength);
            start = retStr.indexOf(searchFor, start + replaceWithLength);
        }
        //showMessage(retStr);
        return retStr;
    }

    public static String globalReplace(String source, String searchFor, String replaceWith) {
        return replaceStr(source, searchFor, replaceWith);
    }

    public static String getControlValue(String controlName) {
        if (values == null) {
            return "";
        }
        Iterator iter = values.iterator();
        while (iter.hasNext()) {
            ControlValue cv = (ControlValue) iter.next();
            if (cv.name.equalsIgnoreCase(controlName)) {
                return cv.value;
            }
        }
        return "";
    }

    public static void setControlValue(String controlName, String value) {
        if (values == null) {
            values = new HashSet();
        }
        Iterator iter = values.iterator();
        while (iter.hasNext()) {
            ControlValue cv = (ControlValue) iter.next();
            if (cv.name.equalsIgnoreCase(controlName)) {
                cv.value = value;
                return;
            }
        }
        ControlValue cv = new ControlValue(controlName, value);
        values.add(cv);
    }

    public static String getStringFromDate(java.util.Date date) {
        DateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MMM-dd");
        //date = (java.util.Date)formatter.parse(date);
        String s = formatter.format(date);
        return s;
    }

    public static String getStringFromDate(java.util.Date date, String format) {
        DateFormat formatter;
        formatter = new SimpleDateFormat(format);
        //date = (java.util.Date)formatter.parse(date);
        String s = formatter.format(date);
        return s;
    }

    public static java.util.Date addDays(java.util.Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, +days); //minus number would decrement the days
        return cal.getTime();
    }

    public static String makeDateStr(Calendar cal, String format) {
        int[] fieldValues = {Calendar.ERA, Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.DAY_OF_WEEK, Calendar.DAY_OF_WEEK_IN_MONTH, Calendar.DAY_OF_YEAR, Calendar.WEEK_OF_MONTH, Calendar.WEEK_OF_YEAR, Calendar.HOUR, Calendar.HOUR_OF_DAY, Calendar.AM_PM};
        String[] fieldNames = {"ERA", "YEAR", "MONTH", "DAY_OF_MONTH", "DAY_OF_WEEK", "DAY_OF_WEEK_IN_MONTH", "DAY_OF_YEAR", "WEEK_OF_MONTH", "WEEK_OF_YEAR", "HOUR", "HOUR_OF_DAY", "AM_PM"};
        SimpleDateFormat formatter = new SimpleDateFormat("MMM");
        String month = formatter.format(cal.getTime());
        //new Date(cal.get(fieldValues[1]),cal.get(fieldValues[2]),cal.get(fieldValues[3]));
        String ds = Integer.toString(cal.get(fieldValues[3]));
        if (cal.get(fieldValues[3]) < 10) {
            ds = "0" + ds;
        }
        return ds + "-" + month + "-" + cal.get(fieldValues[1]);
    }

    public static String makeDateStr(Calendar cal) {
        int[] fieldValues = {Calendar.ERA, Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.DAY_OF_WEEK, Calendar.DAY_OF_WEEK_IN_MONTH, Calendar.DAY_OF_YEAR, Calendar.WEEK_OF_MONTH, Calendar.WEEK_OF_YEAR, Calendar.HOUR, Calendar.HOUR_OF_DAY, Calendar.AM_PM};
        String ds = Integer.toString(cal.get(fieldValues[3]));
        if (cal.get(fieldValues[3]) < 10) {
            ds = "0" + ds;
        }
        String month = Integer.toString(cal.get(fieldValues[2]) + 1);
        //System.out.println("Month="+month);
        if (month.trim().length() == 1) {
            month = "0" + month;
        }
        return month + "/" + ds + "/" + cal.get(fieldValues[1]);
    }

    public static String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        String month = Integer.toString(cal.get(Calendar.MONTH) + 1);
        String day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
        if (month.trim().length() == 1) {
            month = "0" + month;
        }
        if (day.trim().length() == 1) {
            day = "0" + day;
        }
        //if (cal.get(Calendar.DAY_OF_MONTH) < 10) day = "0"+day;
        return month + "/" + day + "/" + cal.get(Calendar.YEAR);
    }

    public static String getTodaysDateISO() {
        Calendar cal = Calendar.getInstance();
        String month = Integer.toString(cal.get(Calendar.MONTH) + 1);
        String day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
        if (month.trim().length() == 1) {
            month = "0" + month;
        }
        if (day.trim().length() == 1) {
            day = "0" + day;
        }
        //if (cal.get(Calendar.DAY_OF_MONTH) < 10) day = "0"+day;
        return cal.get(Calendar.YEAR) + month + day;
    }

    public static String getTodaysTime() {
        Calendar cal = Calendar.getInstance();
        int shift = 0;
        if (cal.get(Calendar.AM_PM) == 1) {
            shift = 12;
        }
        String sHour = Integer.toString(cal.get(Calendar.HOUR) + shift);
        String sMins = Integer.toString(cal.get(Calendar.MINUTE));
        String sSecs = Integer.toString(cal.get(Calendar.SECOND));
        if (sHour.length() == 1) {
            sHour = "0" + sHour;
        }
        if (sMins.length() == 1) {
            sMins = "0" + sMins;
        }
        if (sSecs.length() == 1) {
            sSecs = "0" + sSecs;
        }
        return sHour + ":" + sMins + ":" + sSecs;
    }

    public static boolean validateDate(String date) {
        boolean retValue = true;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        try {
            sdf.setLenient(false);
            sdf.parse(date);
        } catch (Exception e) {
            retValue = false;
        }
        return retValue;
    }

    public static String getAbsoluteDate(String relativeDate) {
        try {
            if (relativeDate.trim().length() == 0) {
                return relativeDate;
            }
            int ymd[] = get_YMD(relativeDate, "MM/DD/YYYY");
            int y = ymd[0], m = ymd[1], d = ymd[2];
            String day, month, year;
            day = (d < 10) ? "0" + Integer.toString(d) : Integer.toString(d);
            month = (m < 10) ? "0" + Integer.toString(m) : Integer.toString(m);
            year = Integer.toString(y);
            int lt = year.trim().length();
            if (lt < 4) {
                if (lt == 2) {
                    if (y < 50) {
                        year = "20" + year;
                    } else {
                        year = "19" + year;
                    }
                    //System.out.println("Absolute date=y="+Integer.toString(y)+"="+year+"}");
                }
                ;
                if (lt == 1) {
                    year = "200" + year;
                }
                ;
                if (lt == 3) {
                    year = "1900";
                }
            }
            return year + "/" + month + "/" + day;
        } catch (Exception e) {
            return "01/01/1900";
        }
    }

    public static String getMilitaryDate(String absDate) {
        if (absDate.trim().length() < 8) {
            return absDate;
        }
        int ymd[] = get_YMD(absDate, "YYYY/MM/DD");
        int y = ymd[0], m = ymd[1], d = ymd[2];
        String day, month, year;
        day = (d < 10) ? "0" + Integer.toString(d) : Integer.toString(d);
        month = (m < 10) ? "0" + Integer.toString(m) : Integer.toString(m);
        year = Integer.toString(y);
        return month + "/" + day + "/" + year;
    }

    public static String getRelativeDate(String absoluteDate) { //if (relativeDate.trim().length() < 11) return absoluteDate;
        if (absoluteDate.indexOf("/") < 4) {
            return absoluteDate;
        }
        int ymd[] = get_YMD(absoluteDate, "YYYY/MM/DD");
        int y = ymd[0], m = ymd[1] - 1, d = ymd[2];
        Calendar cal = Calendar.getInstance();
        cal.set(y, m, d);
        return makeDateStr(cal);
    }

    private static int[] get_YMD(String dateStr, String format) {
        int[] ymd = new int[3];
        int y = 2000, m = 1, d = 1;
        if (format.equalsIgnoreCase("DD-MMM-YYYY")) {
            y = Integer.parseInt(dateStr.substring(7));
            d = Integer.parseInt(dateStr.substring(0, 2));
            for (int i = 0; i < monthNames.length; i++) {
                if (monthNames[i].equalsIgnoreCase(dateStr.substring(3, 6))) {
                    m = i;
                }
            }
        } else if (format.equalsIgnoreCase("DD-MMM-YY")) {
            y = Integer.parseInt(dateStr.substring(7));
            d = Integer.parseInt(dateStr.substring(0, 2));
            for (int i = 0; i < monthNames.length; i++) {
                if (monthNames[i].equalsIgnoreCase(dateStr.substring(3, 6))) {
                    m = i;
                }
            }
        } else if (format.equalsIgnoreCase("MM/DD/YYYY")) {
            String dtxt = dateStr;
            //System.out.println("In YMD="+dtxt);
            StringTokenizer dateParser = new StringTokenizer(dtxt, "/");
            dateParser.hasMoreTokens();
            m = Integer.parseInt(dateParser.nextToken());
            dateParser.hasMoreTokens();
            d = Integer.parseInt(dateParser.nextToken());
            dateParser.hasMoreTokens();
            y = Integer.parseInt(dateParser.nextToken());
        } else if (format.equalsIgnoreCase("YYYY/MM/DD")) {
            String dtxt = dateStr;
            StringTokenizer dateParser = new StringTokenizer(dtxt, "/");
            dateParser.hasMoreTokens();
            y = Integer.parseInt(dateParser.nextToken());
            dateParser.hasMoreTokens();
            m = Integer.parseInt(dateParser.nextToken());
            dateParser.hasMoreTokens();
            d = Integer.parseInt(dateParser.nextToken());
        }
        ymd[0] = y;
        ymd[1] = m;
        ymd[2] = d;
        return ymd;
    }

    public static String round(double value) {
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(value);
    }

    public static String addDigit(String digitStr) {
        String newDigitStr = "";
        int digitPos = digitStr.indexOf(".");
        if (digitPos > 0) {
            newDigitStr = digitStr.substring(digitPos + 1);
            newDigitStr = newDigitStr + "00";
            newDigitStr = digitStr.substring(0, digitPos) + "." + newDigitStr.substring(0, 2);
        } else {
            newDigitStr = digitStr + ".00";
        }
        //System.out.println(digitStr + " -> " + newDigitStr);
        return newDigitStr;
    }

    public static String[] getTocks(String s, char separator) {
        if ((s == null) || (s.length() == 0)) {
            return null;
        }
        int iBeg = 0;
        int len = s.length();
        Vector vv = new Vector(10);
        for (int i = 0; i < len; i++) {
            if (s.charAt(i) == separator) {
                if (iBeg == i) {
                    vv.addElement(new String(""));
                } else {
                    vv.addElement(s.substring(iBeg, i));
                }
                iBeg = i + 1;
            }
            ;
        }
        ;
        vv.addElement(s.substring(iBeg));
        String[] result = new String[vv.size()];
        for (int i = 0; i < vv.size(); i++) {
            result[i] = (String) vv.elementAt(i);
        }
        return result;
    }

    public static Component getComponent() {
        return anyComponent;
    }

    public static void setComponent(Component _anyComponent) {
        anyComponent = _anyComponent;
    }

    public static boolean isFieldEmpty(TextField textField) {
        if (textField.getText().trim().length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean writeLog(String message) { //getTodaysDate() + getTodaysTime();
        File file = new File("tmanLog.txt");
        if (!file.exists()) {
            boolean isCreated = false;
            try {
                isCreated = file.createNewFile();
            } catch (Exception e) {
            }
            ;
            if (!isCreated) {
                return false;
            }
        }
        ;
        if (!file.canWrite()) {
            return false;
        }
        int size = (int) file.length();
        if (size > 250000) {
            String s = getTodaysDate();
            s = "_" + globalReplace(s, "/", "_");
            File newFile = new File("tmanLog" + s + ".txt");
            file.renameTo(newFile);
            file.delete();
            return writeLog(message);
        } else {
            try {
                FileOutputStream fs = new FileOutputStream(file.getPath(), true);
                if ((message == null) || (message.length() == 0)) {
                    message = "\r\n";
                } else {
                    message
                            = /*getTodaysDate() + " " + */ getTodaysTime() + " -- " + message + "\r\n";
                }
                int len = message.length();
                byte[] b = message.getBytes();
                fs.write(b);
                fs.close();
            } catch (Exception e) {
                return false;
            }
            ;
        }
        ;
        return true;
    }

    public static boolean writeTextToFile(String fileName, String message) {
        return writeTextToFile(fileName, message, false);
    }

    public static boolean writeTextToFile(String fileName, String message, boolean createNew) {
        //getTodaysDate() + getTodaysTime();
        File file = new File(fileName);
        if (file.exists() && createNew) {
            try {
                file.delete();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (!file.exists()) {
            boolean isCreated = false;
            try {
                isCreated = file.createNewFile();
            } catch (Exception e) {
            }
            if (!isCreated) {
                return false;
            }
        }
        if (!file.canWrite()) {
            return false;
        }
        int size = (int) file.length();
        try {
            FileOutputStream fs = new FileOutputStream(file.getPath(), true);
            if ((message == null) || (message.length() == 0)) {
                message = "\r\n";
            } else {
                message = message + "\r\n";
            }
            /*getTodaysDate() + " " + */
            //getTodaysTime() + " -- " + message + "\r\n";
            int len = message.length();
            byte[] b = message.getBytes();
            fs.write(b);
            fs.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static void zipDir(String dir, String toZipFile) {
        zipDir(dir, toZipFile, false);
    }

    public static void zipDir(String dir, String toZipFile, boolean addPathInfo) {
        try { //create a ZipOutputStream to zip the data to
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(toZipFile));
            zipDir(dir, zos, addPathInfo);
            zos.close();
        } catch (Exception e) {
            //handle exception
        }
    }

    public static void zipWithPwd(String srcFile, String zipFile, String pwd) {
        try {
            //System.loadLibrary("chilkat");
            System.load("C:\\tomcat\\common\\lib\\chilkat.dll");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load." + e);
            System.exit(1);
        }
//	  CkZip zip = new CkZip();
//	  boolean success;
//	  //  Any string unlocks the component for the 1st 30-days.
//	  success = zip.UnlockComponent("Anything for 30-day trial");
//	  if (success != true) {
//	    System.out.println(zip.lastErrorText());
//	    return ;
//	  }
//	  success = zip.NewZip(zipFile);
//	  if (success != true) {
//	    System.out.println(zip.lastErrorText());
//	    return ;
//	  }
//	  zip.put_Encryption(4);
//	  zip.put_EncryptKeyLength(128);
//	  zip.SetPassword(pwd);
//
//	  boolean recurse;
//	  recurse = true;
//	  zip.AppendFiles(srcFile,recurse);
//	  //  Write "AesEncrypted.zip"
//	  success = zip.WriteZipAndClose();
//	  if (success != true) {
//	    System.out.println(zip.lastErrorText());
//	  }
    }

    private static void zipDir(String dir2zip, ZipOutputStream zos, boolean addPathInfo) {
        try {
            File zipDir = new File(dir2zip);
            String[] dirList = zipDir.list();
            byte[] readBuffer = new byte[2156];
            int bytesIn = 0;
            //loop through dirList, and zip the files
            for (int i = 0; i < dirList.length; i++) {
                File f = new File(zipDir, dirList[i]);
                if (f.isDirectory()) {
                    String filePath = f.getPath();
                    zipDir(filePath, zos, addPathInfo);
                    //loop again
                    continue;
                }
                FileInputStream fis = new FileInputStream(f);
                ZipEntry anEntry;
                if (addPathInfo) {
                    anEntry = new ZipEntry(f.getPath());
                } else {
                    anEntry = new ZipEntry(f.getName());
                }
                zos.putNextEntry(anEntry);
                while ((bytesIn = fis.read(readBuffer)) != -1) {
                    zos.write(readBuffer, 0, bytesIn);
                }
                fis.close();
            }
        } catch (Exception e) {
            //handle exception
        }
    }

    public static byte[] compress(byte[] bIn) {
        byte[] input = "some some bytes to compress".getBytes();
        // Create the compressor with highest level of compression
        Deflater compressor = new Deflater();
        compressor.setLevel(Deflater.BEST_COMPRESSION);
        // Give the compressor the data to compress
        compressor.setInput(input);
        compressor.finish();
        // Create an expandable byte array to hold the compressed data.
        // You cannot use an array that's the same size as the orginal because
        // there is no guarantee that the compressed data will be smaller than
        // the uncompressed data.
        ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);
        // Compress the data
        byte[] buf = new byte[1024];
        while (!compressor.finished()) {
            int count = compressor.deflate(buf);
            bos.write(buf, 0, count);
        }
        try {
            bos.close();
        } catch (IOException e) {
        }
        // Get the compressed data
        //byte[] compressedData = bos.toByteArray();
        return null;
    }
    //

    public static ArrayList unZip(String fileName, String newPath) {
        if (isNull(newPath)) {
            newPath = "";
        }
        ArrayList files = new ArrayList();
        int BUFFER = 2048;
        try {
            BufferedOutputStream dest = null;
            FileInputStream fis = new FileInputStream(fileName);
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                //System.out.println("Extracting: " +entry);
                int count;
                byte data[] = new byte[BUFFER];
                // write the files to the disk
                String newFileName = newPath + entry.getName();
                //System.err.println(" "+newFileName);
                FileOutputStream fos = new FileOutputStream(newFileName);
                dest = new BufferedOutputStream(fos, BUFFER);
                while ((count = zis.read(data, 0, BUFFER)) != -1) {
                    dest.write(data, 0, count);
                }
                dest.flush();
                dest.close();
                files.add(newFileName);
            }
            zis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return files;
    }

    public static boolean makeBakup(String fileName) {
        try {
            File fromFile = new File(fileName);
            if (!fromFile.exists() || !fromFile.canRead()) {
                return false;
            }
            String s = getTodaysDate();
            s = "_" + globalReplace(s, "/", "_");
            File toFile = new File(stripPathAndExtension(fileName) + s + ".zip");
            if (toFile.exists()) {
                return false;
            } else {
                boolean isCreated = false;
                try {
                    isCreated = toFile.createNewFile();
                } catch (Exception e) {
                }
                if (!isCreated || !toFile.canWrite()) {
                    return false;
                }
            }
            FileInputStream fsFrom = new FileInputStream(fromFile);
            FileOutputStream fsTo = new FileOutputStream(toFile);
            ZipOutputStream defStream = new ZipOutputStream(fsTo);
            defStream.putNextEntry(new ZipEntry(stripPath(fileName)));
            byte[] bToComp = new byte[(int) fromFile.length()];
            int n = 0;
            int nRead = 0;
            int nBytes = 25000;
            byte[] b = new byte[nBytes];
            while (n != -1) {
                n = fsFrom.read(b, 0, nBytes);
                if (n != -1) {
                    System.arraycopy(b, 0, bToComp, nRead, n);
                    defStream.write(b, 0, n);
                    nRead += n;
                }
            }
            ;
            defStream.closeEntry();
            defStream.close();
            /*
             if (nRead != 0)
             { byte[] bOut = compress(bToComp);
             if (bOut != null)
             fsTo.write(bOut);
             };
             */
            fsFrom.close();
            fsTo.close();
        } catch (Exception e) {
        }
        ;
        return true;
    }

    public static boolean makeZip(String fileName) {
        try {
            File fromFile = new File(fileName);
            if (!fromFile.exists() || !fromFile.canRead()) {
                return false;
            }
            File toFile = new File(stripPathAndExtension(fileName) + ".zip");
            if (toFile.exists()) {
                return false;
            } else {
                boolean isCreated = false;
                try {
                    isCreated = toFile.createNewFile();
                } catch (Exception e) {
                }
                ;
                if (!isCreated || !toFile.canWrite()) {
                    return false;
                }
            }
            ;
            FileInputStream fsFrom = new FileInputStream(fromFile);
            FileOutputStream fsTo = new FileOutputStream(toFile);
            ZipOutputStream defStream = new ZipOutputStream(fsTo);
            defStream.putNextEntry(new ZipEntry(stripPath(fileName)));
            byte[] bToComp = new byte[(int) fromFile.length()];
            int n = 0;
            int nRead = 0;
            int nBytes = 25000;
            byte[] b = new byte[nBytes];
            while (n != -1) {
                n = fsFrom.read(b, 0, nBytes);
                if (n != -1) {
                    System.arraycopy(b, 0, bToComp, nRead, n);
                    defStream.write(b, 0, n);
                    nRead += n;
                }
            }
            defStream.closeEntry();
            defStream.close();
            fsFrom.close();
            fsTo.close();
        } catch (Exception e) {
        }
        return true;
    }

    public static String spaces(int length) {
        return replicate(' ', length);
    }

    public static String replicate(char chr, int length) {
        String result = "";
        for (int i = 0; i < length; i++) {
            result = result + chr;
        }
        return result;
    }

    public static String replicate(String str, int length) {
        String result = "";
        for (int i = 0; i < length; i++) {
            result = result + str;
        }
        return result;
    }

    public static String replicateCommaList(String str, int length) {
        String result = str;
        for (int i = 1; i < length; i++) {
            result = result + "," + str;
        }
        return result;
    }

    public static String getHtml(JWebDataWindowLite dw) {
        if (dw==null) return "";
        String name = dw.getName();
        String html = "  <table border='1'>";
        html = html + "    <tr>\n ";
        for (int c = 0; c < dw.getColumnCount(); c++) {
            html = html + "<th>" + dw.getColumnName(c) + "</th>";
        }
        html = html + "    </tr>\n ";

        for (int r = 0; r < dw.getRowCount(); r++) {
            html = html + "    <tr>\n ";

            for (int c = 0; c < dw.getColumnCount(); c++) {
                JWebCellLite cell = dw.getCell(r, c);
                String type = (String)cell.getVar("Type");
                if (isNotNull(type) && type.equalsIgnoreCase("Checkbox")) {
                    cell.setVar("Type", "Checkbox");
                    int isChecked = Integer.parseInt((String)cell.getVar("Checked"));
                    cell.setName(name);
                    html = html + "<td>" + "<input type = 'checkbox' name = '"+cell.getName()+"' value = "+dw.getItem(r, c)+" "+(isChecked==1?"checked":"")+"> " + "</td>";
                }
                else {        // <input type = "checkbox" name = "s1_t1" value = "s1_t2_view"   >View  <br>
                    html = html + "<td>" + dw.getItem(r, c) + "</td>";
                }

            }
            html = html + "    </tr>\n ";
        }
        html = html + " </table> ";
        return html;
    }

    public static void invokeMethod(Class mainClass, Object main, String method) {
        try {
            Method m = mainClass.getMethod(method, (Class<?>[]) null);
            System.out.println(m.toString() + " " + main.toString());
            m.invoke(main, (Class<?>) null);
        } catch (NoSuchMethodException e1) {
            System.out.println("NoSuchMethodException " + e1.getMessage());
            e1.printStackTrace();
        } catch (InvocationTargetException e2) {
            System.out.println("InvocationTargetException " + e2.getMessage());
            e2.printStackTrace();
        } catch (IllegalAccessException e3) {
            System.out.println("IllegalAccessException " + e3.getMessage() + System.getProperty("new line"));
            e3.printStackTrace();
        }
    }

    public static String removeEnds(String source) {
        return source.substring(1, source.length() - 1);
    }

    public static boolean copyFile(String destName, String srcName, boolean toAppend) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            byte[] buf = new byte[3000];
            bis = new BufferedInputStream(new FileInputStream(srcName));
            bos = new BufferedOutputStream(new FileOutputStream(destName, toAppend));
            int size;
            while ((size = bis.read(buf)) > -1) {
                bos.write(buf, 0, size);
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return true;
    }

    public static String DoubleToStr(double d, int k) {
        k = Math.max(0, k);
        if (k == 0 && d < 1000D && d > -1000D) {
            return Math.round(d) + "";
        }
        long l = (long) Math.pow(10D, k);
        long l1 = Math.round(d * (double) l);
        long l2 = Math.abs(l1) % l + l;
        String s = "";
        if (k > 0) {
            s = "." + (l2 + "").substring(1);
        }
        l1 /= l;
        if (l1 == 0L) {
            if (d < 0.0D) {
                s = "-0" + s;
            } else {
                s = "0" + s;
            }
            return s;
        }
        if (l1 > 0L) {
            for (; l1 >= 1L; l1 /= 1000L) {
                long l3 = l1 % 1000L + 1000L;
                if (l3 >= 1000L && l1 >= 1000L) {
                    s = " " + (l3 + "").substring(1) + s;
                } else {
                    s = l1 % 1000L + s;
                }
            }
        } else if (l1 < 0L) {
            for (; l1 <= -1L; l1 /= 1000L) {
                long l4 = l1 % 1000L - 1000L;
                if (l4 <= -1000L && l1 <= -1000L) {
                    s = " " + (l4 + "").substring(2) + s;
                } else {
                    s = l1 % 1000L + s;
                }
            }
        }
        return s;
    }

    public static String FormatStr(String s, int k, int l) {
        String s1 = "";
        int i2 = s.length();
        if (k < 0) {
            k = i2;
        }
        switch (l) {
            case 1: // '\001'
                if (i2 > k) {
                    s1 = s.substring((i2 - k) / 2, k);
                    break;
                }
                int j2 = (k - i2) / 2;
                for (int i1 = 0; i1 < j2; i1++) {
                    s1 = s1 + " ";
                }
                s1 = s1 + s;
                for (int j1 = 0; j1 < k - i2 - j2; j1++) {
                    s1 = s1 + " ";
                }
                break;
            case 2: // '\002'
                if (i2 > k) {
                    s1 = s.substring(i2 - k, k);
                    break;
                }
                for (int k1 = i2; k1 < k; k1++) {
                    s1 = s1 + " ";
                }
                s1 = s1 + s;
                break;
            default:
                if (i2 > k) {
                    s1 = s.substring(0, k);
                    break;
                }
                s1 = s;
                for (int l1 = i2; l1 < k; l1++) {
                    s1 = s1 + " ";
                }
                break;
        }
        return s1;
    }

    public static String getHexForColor(Color color) {
        if (color == null) {
            color = Color.white;
        }
        int red = color.getRed();
        int blue = color.getBlue();
        int green = color.getGreen();
        return "#" + getHex(red) + getHex(green) + getHex(blue);
    }
    /*
     * Return the hex value for an int, including the starting 0
     * @param value the integer to retrieve in Hex
     */

    public static String getHex(int value) {
        String hex = Integer.toHexString(value);
        if (hex.length() < 2) {
            hex = "0" + hex;
        }
        return hex;
    }

    /**
     * Print out an array. For debugging purposes This works on 1 and 2d arrays
     *
     * @param array the array to print out
     */
    public static String getStringForArray(Object array) {
        StringBuffer buffer = new StringBuffer();
        if (array == null) {
            return "null array";
        }
        if (array.getClass().isArray()) {
            int arrayLength = java.lang.reflect.Array.getLength(array);
            int array2Length;
            Object array2;
            for (int x = 0; x < arrayLength; x++) {
                array2 = java.lang.reflect.Array.get(array, x);
                if (array2.getClass().isArray()) {
                    //It is an array so traverse the contained arrays
                    array2Length = java.lang.reflect.Array.getLength(array2);
                    for (int y = 0; y < array2Length; y++) {
                        buffer.append(java.lang.reflect.Array.get(array2, y).toString());
                        buffer.append(" | ");
                    }
                    buffer.append("\n");
                } else {
                    buffer.append(array2.toString());
                    buffer.append(" | ");
                }
            }
        } else {
            throw new Error("The object is not an array");
        }
        return buffer.toString();
    }

    public static final boolean isRectangleContainingRectangle(Rectangle a, Rectangle b) {
        if (b.x >= a.x && (b.x + b.width) <= (a.x + a.width) && b.y >= a.y && (b.y + b.height) <= (a.y + a.height)) {
            return true;
        }
        return false;
    }

    /**
     * Return the rectangle (0,0,bounds.width,bounds.height) for the component
     * <code>aComponent</code>
     */
    public static Rectangle getLocalBounds(Component aComponent) {
        Rectangle b = new Rectangle(aComponent.getBounds());
        b.x = b.y = 0;
        return b;
    }

    public static Rectangle computeIntersection(int x, int y, int width, int height, Rectangle dest) {
        int x1 = (x > dest.x) ? x : dest.x;
        int x2 = ((x + width) < (dest.x + dest.width)) ? (x + width) : (dest.x + dest.width);
        int y1 = (y > dest.y) ? y : dest.y;
        int y2 = ((y + height) < (dest.y + dest.height) ? (y + height) : (dest.y + dest.height));
        dest.x = x1;
        dest.y = y1;
        dest.width = x2 - x1;
        dest.height = y2 - y1;
        // If rectangles don't intersect, return zero'd intersection.
        if (dest.width < 0 || dest.height < 0) {
            dest.x = dest.y = dest.width = dest.height = 0;
        }
        return dest;
    }

    /**
     * Convenience to calculate the union of two rectangles without allocating a
     * new rectangle Return dest
     */
    public static Rectangle computeUnion(int x, int y, int width, int height, Rectangle dest) {
        int x1 = (x < dest.x) ? x : dest.x;
        int x2 = ((x + width) > (dest.x + dest.width)) ? (x + width) : (dest.x + dest.width);
        int y1 = (y < dest.y) ? y : dest.y;
        int y2 = ((y + height) > (dest.y + dest.height)) ? (y + height) : (dest.y + dest.height);
        dest.x = x1;
        dest.y = y1;
        dest.width = (x2 - x1);
        dest.height = (y2 - y1);
        return dest;
    }

    /**
     * Convenience returning an array of rect representing the regions within
     * <code>rectA</code> that do not overlap with <code>rectB</code>. If the
     * two Rects do not overlap, returns an empty array
     */
    public static Rectangle[] computeDifference(Rectangle rectA, Rectangle rectB) {
        if (rectB == null || !rectA.intersects(rectB) || isRectangleContainingRectangle(rectB, rectA)) {
            return new Rectangle[0];
        }
        Rectangle t = new Rectangle();
        Rectangle a = null, b = null, c = null, d = null;
        Rectangle result[];
        int rectCount = 0;
        /* rectA contains rectB */
        if (isRectangleContainingRectangle(rectA, rectB)) {
            t.x = rectA.x;
            t.y = rectA.y;
            t.width = rectB.x - rectA.x;
            t.height = rectA.height;
            if (t.width > 0 && t.height > 0) {
                a = new Rectangle(t);
                rectCount++;
            }
            t.x = rectB.x;
            t.y = rectA.y;
            t.width = rectB.width;
            t.height = rectB.y - rectA.y;
            if (t.width > 0 && t.height > 0) {
                b = new Rectangle(t);
                rectCount++;
            }
            t.x = rectB.x;
            t.y = rectB.y + rectB.height;
            t.width = rectB.width;
            t.height = rectA.y + rectA.height - (rectB.y + rectB.height);
            if (t.width > 0 && t.height > 0) {
                c = new Rectangle(t);
                rectCount++;
            }
            t.x = rectB.x + rectB.width;
            t.y = rectA.y;
            t.width = rectA.x + rectA.width - (rectB.x + rectB.width);
            t.height = rectA.height;
            if (t.width > 0 && t.height > 0) {
                d = new Rectangle(t);
                rectCount++;
            }
        } else {
            /* 1 */
            if (rectB.x <= rectA.x && rectB.y <= rectA.y) {
                if ((rectB.x + rectB.width) > (rectA.x + rectA.width)) {
                    t.x = rectA.x;
                    t.y = rectB.y + rectB.height;
                    t.width = rectA.width;
                    t.height = rectA.y + rectA.height - (rectB.y + rectB.height);
                    if (t.width > 0 && t.height > 0) {
                        a = t;
                        rectCount++;
                    }
                } else if ((rectB.y + rectB.height) > (rectA.y + rectA.height)) {
                    t.setBounds((rectB.x + rectB.width), rectA.y, (rectA.x + rectA.width) - (rectB.x + rectB.width), rectA.height);
                    if (t.width > 0 && t.height > 0) {
                        a = t;
                        rectCount++;
                    }
                } else {
                    t.setBounds((rectB.x + rectB.width), rectA.y, (rectA.x + rectA.width) - (rectB.x + rectB.width), (rectB.y + rectB.height) - rectA.y);
                    if (t.width > 0 && t.height > 0) {
                        a = new Rectangle(t);
                        rectCount++;
                    }
                    t.setBounds(rectA.x, (rectB.y + rectB.height), rectA.width, (rectA.y + rectA.height) - (rectB.y + rectB.height));
                    if (t.width > 0 && t.height > 0) {
                        b = new Rectangle(t);
                        rectCount++;
                    }
                }
            } else if (rectB.x <= rectA.x && (rectB.y + rectB.height) >= (rectA.y + rectA.height)) {
                if ((rectB.x + rectB.width) > (rectA.x + rectA.width)) {
                    t.setBounds(rectA.x, rectA.y, rectA.width, rectB.y - rectA.y);
                    if (t.width > 0 && t.height > 0) {
                        a = t;
                        rectCount++;
                    }
                } else {
                    t.setBounds(rectA.x, rectA.y, rectA.width, rectB.y - rectA.y);
                    if (t.width > 0 && t.height > 0) {
                        a = new Rectangle(t);
                        rectCount++;
                    }
                    t.setBounds((rectB.x + rectB.width), rectB.y, (rectA.x + rectA.width) - (rectB.x + rectB.width), (rectA.y + rectA.height) - rectB.y);
                    if (t.width > 0 && t.height > 0) {
                        b = new Rectangle(t);
                        rectCount++;
                    }
                }
            } else if (rectB.x <= rectA.x) {
                if ((rectB.x + rectB.width) >= (rectA.x + rectA.width)) {
                    t.setBounds(rectA.x, rectA.y, rectA.width, rectB.y - rectA.y);
                    if (t.width > 0 && t.height > 0) {
                        a = new Rectangle(t);
                        rectCount++;
                    }
                    t.setBounds(rectA.x, (rectB.y + rectB.height), rectA.width, (rectA.y + rectA.height) - (rectB.y + rectB.height));
                    if (t.width > 0 && t.height > 0) {
                        b = new Rectangle(t);
                        rectCount++;
                    }
                } else {
                    t.setBounds(rectA.x, rectA.y, rectA.width, rectB.y - rectA.y);
                    if (t.width > 0 && t.height > 0) {
                        a = new Rectangle(t);
                        rectCount++;
                    }
                    t.setBounds((rectB.x + rectB.width), rectB.y, (rectA.x + rectA.width) - (rectB.x + rectB.width), rectB.height);
                    if (t.width > 0 && t.height > 0) {
                        b = new Rectangle(t);
                        rectCount++;
                    }
                    t.setBounds(rectA.x, (rectB.y + rectB.height), rectA.width, (rectA.y + rectA.height) - (rectB.y + rectB.height));
                    if (t.width > 0 && t.height > 0) {
                        c = new Rectangle(t);
                        rectCount++;
                    }
                }
            } else if (rectB.x <= (rectA.x + rectA.width) && (rectB.x + rectB.width) > (rectA.x + rectA.width)) {
                if (rectB.y <= rectA.y && (rectB.y + rectB.height) > (rectA.y + rectA.height)) {
                    t.setBounds(rectA.x, rectA.y, rectB.x - rectA.x, rectA.height);
                    if (t.width > 0 && t.height > 0) {
                        a = t;
                        rectCount++;
                    }
                } else if (rectB.y <= rectA.y) {
                    t.setBounds(rectA.x, rectA.y, rectB.x - rectA.x, (rectB.y + rectB.height) - rectA.y);
                    if (t.width > 0 && t.height > 0) {
                        a = new Rectangle(t);
                        rectCount++;
                    }
                    t.setBounds(rectA.x, (rectB.y + rectB.height), rectA.width, (rectA.y + rectA.height) - (rectB.y + rectB.height));
                    if (t.width > 0 && t.height > 0) {
                        b = new Rectangle(t);
                        rectCount++;
                    }
                } else if ((rectB.y + rectB.height) > (rectA.y + rectA.height)) {
                    t.setBounds(rectA.x, rectA.y, rectA.width, rectB.y - rectA.y);
                    if (t.width > 0 && t.height > 0) {
                        a = new Rectangle(t);
                        rectCount++;
                    }
                    t.setBounds(rectA.x, rectB.y, rectB.x - rectA.x, (rectA.y + rectA.height) - rectB.y);
                    if (t.width > 0 && t.height > 0) {
                        b = new Rectangle(t);
                        rectCount++;
                    }
                } else {
                    t.setBounds(rectA.x, rectA.y, rectA.width, rectB.y - rectA.y);
                    if (t.width > 0 && t.height > 0) {
                        a = new Rectangle(t);
                        rectCount++;
                    }
                    t.setBounds(rectA.x, rectB.y, rectB.x - rectA.x, rectB.height);
                    if (t.width > 0 && t.height > 0) {
                        b = new Rectangle(t);
                        rectCount++;
                    }
                    t.setBounds(rectA.x, (rectB.y + rectB.height), rectA.width, (rectA.y + rectA.height) - (rectB.y + rectB.height));
                    if (t.width > 0 && t.height > 0) {
                        c = new Rectangle(t);
                        rectCount++;
                    }
                }
            } else if (rectB.x >= rectA.x && (rectB.x + rectB.width) <= (rectA.x + rectA.width)) {
                if (rectB.y <= rectA.y && (rectB.y + rectB.height) > (rectA.y + rectA.height)) {
                    t.setBounds(rectA.x, rectA.y, rectB.x - rectA.x, rectA.height);
                    if (t.width > 0 && t.height > 0) {
                        a = new Rectangle(t);
                        rectCount++;
                    }
                    t.setBounds((rectB.x + rectB.width), rectA.y, (rectA.x + rectA.width) - (rectB.x + rectB.width), rectA.height);
                    if (t.width > 0 && t.height > 0) {
                        b = new Rectangle(t);
                        rectCount++;
                    }
                } else if (rectB.y <= rectA.y) {
                    t.setBounds(rectA.x, rectA.y, rectB.x - rectA.x, rectA.height);
                    if (t.width > 0 && t.height > 0) {
                        a = new Rectangle(t);
                        rectCount++;
                    }
                    t.setBounds(rectB.x, (rectB.y + rectB.height), rectB.width, (rectA.y + rectA.height) - (rectB.y + rectB.height));
                    if (t.width > 0 && t.height > 0) {
                        b = new Rectangle(t);
                        rectCount++;
                    }
                    t.setBounds((rectB.x + rectB.width), rectA.y, (rectA.x + rectA.width) - (rectB.x + rectB.width), rectA.height);
                    if (t.width > 0 && t.height > 0) {
                        c = new Rectangle(t);
                        rectCount++;
                    }
                } else {
                    t.setBounds(rectA.x, rectA.y, rectB.x - rectA.x, rectA.height);
                    if (t.width > 0 && t.height > 0) {
                        a = new Rectangle(t);
                        rectCount++;
                    }
                    t.setBounds(rectB.x, rectA.y, rectB.width, rectB.y - rectA.y);
                    if (t.width > 0 && t.height > 0) {
                        b = new Rectangle(t);
                        rectCount++;
                    }
                    t.setBounds((rectB.x + rectB.width), rectA.y, (rectA.x + rectA.width) - (rectB.x + rectB.width), rectA.height);
                    if (t.width > 0 && t.height > 0) {
                        c = new Rectangle(t);
                        rectCount++;
                    }
                }
            }
        }
        result = new Rectangle[rectCount];
        rectCount = 0;
        if (a != null) {
            result[rectCount++] = a;
        }
        if (b != null) {
            result[rectCount++] = b;
        }
        if (c != null) {
            result[rectCount++] = c;
        }
        if (d != null) {
            result[rectCount++] = d;
        }
        return result;
    }

    /**
     * Returns true if the mouse event specifies the left mouse button.
     *
     * @param anEvent a MouseEvent object
     * @return true if the left mouse button was active
     */
    public static boolean isLeftMouseButton(MouseEvent anEvent) {
        return ((anEvent.getModifiers() & InputEvent.BUTTON1_MASK) != 0);
    }

    /**
     * Returns true if the mouse event specifies the middle mouse button.
     *
     * @param anEvent a MouseEvent object
     * @return true if the middle mouse button was active
     */
    public static boolean isMiddleMouseButton(MouseEvent anEvent) {
        return ((anEvent.getModifiers() & InputEvent.BUTTON2_MASK) == InputEvent.BUTTON2_MASK);
    }

    /**
     * Returns true if the mouse event specifies the right mouse button.
     *
     * @param anEvent a MouseEvent object
     * @return true if the right mouse button was active
     */
    public static boolean isRightMouseButton(MouseEvent anEvent) {
        return ((anEvent.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK);
    }

    public static int find(String[] s, String val) {
        for (int i = 0; i < s.length; i++) {
            if (isSame(s[i], val)) {
                return i;
            }
        }
        return -1;
    }

    public static boolean ifFunctionExists(String[] s, String val) {
        val = val.toLowerCase().trim();
        for (int i = 0; i < s.length; i++) {
            if (s[i] == null) {
                return false;
            }
            int fncPos = val.indexOf(s[i].toLowerCase().trim());
            if (fncPos >= 0) {
                fncPos = fncPos + s[i].length();
                while (fncPos < val.length() - 1) {
                    String valStr = val.substring(fncPos, fncPos + 1);
                    if (valStr.equals("(")) {
                        return true;
                    }
                    if (!valStr.equals(" ")) {
                        return false;
                    }
                    fncPos++;
                }
                return false;
            }
        }
        return false;
    }

    public static boolean ifTextExists(String[] s, String val) {
        val = val.toLowerCase().trim();
        for (int i = 0; i < s.length; i++) {
            if (s[i] == null) {
                return false;
            }
            if (val.indexOf(s[i].toLowerCase().trim()) >= 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean ifTextExists(String val, String[] s) {
        val = val.toLowerCase().trim();
        for (int i = 0; i < s.length; i++) {
            if (s[i] == null) {
                return false;
            }
            if (s[i].toLowerCase().trim().indexOf(val) >= 0) {
                return true;
            }
        }
        return false;
    }

    public static int exists(String[] s, String val) {
        for (int i = 0; i < s.length; i++) {
            if (s[i] == null) {
                return -1;
            }
            if (isSame(s[i], val)) {
                return i;
            }
        }
        return -1;
    }

    public static boolean in(String val, String[] s) {
        if (isNull(val) && s == null) {
            return true;
        }
        if (s == null) {
            return false;
        }
        for (int i = 0; i < s.length; i++) {
            if (s[i] == null) {
                return false;
            }
            if (isSame(s[i], val)) {
                return true;
            }
        }
        return false;
    }

    public static double getIncDouble(double num, float inc) {
        return num + (num * (inc / 100));
    }

    public static double getDecDouble(double num, float inc) {
        return num - (num * (inc / 100));
    }

    public static float getDecFloat(float num, float inc) {
        return num - (num * (inc / 100));
    }

    public static int getIncInt(int num, float inc) {
        return (int) (num + (num * (inc / 100)));
    }

    public static int getInc(int num, float inc) {
        //System.err.print((double)(inc/100));
        //System.err.println((num * (inc/100)));
        return (int) (num * (inc / 100));
    }

    static final String encode = "Cp1252"; // dBase encoding

    /**
     * Converts a long to a little-endian four-byte array
     */
    public static final byte[] intToLittleEndian(int val) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (val % 256);
            val = val / 256;
        }
        return b;
    }

    /**
     * Converts a long to a little-endian two-byte array
     */
    public static final byte[] shortToLittleEndian(short val) {
        byte[] b = new byte[2];
        for (int i = 0; i < 2; i++) {
            b[i] = (byte) (val % 256);
            val = (short) (val / 256);
        }
        return b;
    }

    /**
     * Converts a little-endian four-byte array to a long, represented as a
     * double, since long is signed.
     * <p/>
     * I don't know why Java doesn't supply this. It could be that it's there
     * somewhere, but I looked and couldn't find it.
     */
    public static final double vax_to_long(byte[] b) {
        //existing code that has been commented out
        //return fixByte(b[0]) + ( fixByte(b[1]) * 256) +
        //( fixByte(b[2]) * (256^2)) + ( fixByte(b[3]) * (256^3));
        // Fix courtesy Preetha Suri <Preetha.Suri@sisl.co.in>
        //
        long lngTmp = (long) (0x0ffL & b[0]) | ((0x0ffL & (long) b[1]) << 8) | ((0x0ffL & (long) b[2]) << 16) | ((0x0ffL & (long) b[3]) << 24);
        return ((double) lngTmp);
    }

    /**
     * Converts a little-endian four-byte array to a short, represented as an
     * int, since short is signed.
     * <p/>
     * I don't know why Java doesn't supply this. It could be that it's there
     * somewhere, but I looked and couldn't find it.
     */
    public static final int vax_to_short(byte[] b) {
        return (int) (fixByte(b[0]) + (fixByte(b[1]) * 256));
    }
    /*
     *
     * bytes are signed; let's fix them...
     *
     */

    public static final short fixByte(byte b) {
        if (b < 0) {
            return (short) (b + 256);
        }
        return b;
    }

    public static void clearList(Collection list) {
        if (list != null) {
            list.clear();
            list = null;
        }
    }

    public static String pad(String s, int len, char padChar) {
        StringBuffer buf = new StringBuffer();
        int slen = s.length();
        if (len > slen) {
            int diff = len - s.length();
            for (int i = 0; i < diff; i++) {
                buf.append(padChar);
            }
        }
        buf.append(s);
        return buf.toString();
    }

    public static final String getPaddedValue(String str, int size, String padChar) {
        if (str != null && str.length() == size) {
            return str;
        }
        StringBuffer tmp;
        if (str == null) {
            tmp = new StringBuffer(size);
        } else {
            tmp = new StringBuffer(str);
        }
        if (tmp.length() > size) {
            return tmp.toString().substring(0, size); // do cutting
        } else {
            // or add some padding at the beginning of the string
            StringBuffer pad = new StringBuffer(size);
            int numBlanks = size - tmp.length();
            for (int p = 0; p < numBlanks; p++) {
                pad.append(padChar);
            }
            return pad.append(tmp).toString();
        }
    }

    /**
     * Cut or padd the string to the given size
     *
     * @param size the wanted length
     * @param padChar char to use for padding (must be of length()==1!)
     * @return the string with correct lenght, padded with pad if necessary
     */
    public static final String forceToSize(String str, int size, String padChar) {
        if (str != null && str.length() == size) {
            return str;
        }
        StringBuffer tmp;
        if (str == null) {
            tmp = new StringBuffer(size);
        } else {
            tmp = new StringBuffer(str);
        }
        if (tmp.length() > size) {
            return tmp.toString().substring(0, size); // do cutting
        } else {
            // or add some padding to the end of the string
            StringBuffer pad = new StringBuffer(size);
            int numBlanks = size - tmp.length();
            for (int p = 0; p < numBlanks; p++) {
                pad.append(padChar);
            }
            return tmp.append(pad).toString();
        }
    }

    /**
     * Cut or padd the string to the given size
     *
     * @param size the wanted length
     * @param padByte char to use for padding
     * @return the string with correct lenght, padded with pad if necessary
     */
    public static final byte[] forceToSize(String str, int size, byte padByte) throws java.io.UnsupportedEncodingException {
        if (str != null && str.length() == size) {
            return str.getBytes(encode);
        }
        byte[] result = new byte[size];
        if (str == null) {
            for (int ii = 0; ii < size; ii++) {
                result[ii] = padByte;
            }
            return result;
        }
        if (str.length() > size) {
            return str.substring(0, size).getBytes(encode); // do cutting
        }        // do padding
        byte[] tmp = str.getBytes(encode);
        for (int jj = 0; jj < tmp.length; jj++) {
            result[jj] = tmp[jj];
        }
        for (int kk = tmp.length; kk < size; kk++) {
            result[kk] = padByte;
        }
        return result;
    }
    /*
     * Delete a file in the data directory
     */

    public static final void delFile(String fname) throws NullPointerException, IOException {
        File f = new File(fname);
        // only delete a file that exists
        //
        if (f.exists()) {
            // try the delete. If it fails, complain
            //
            if (!f.delete()) {
                throw new IOException("Could not delete file: " + fname + ".");
            }
        }
    }

    public static final void delFile(String dataDir, String fname) throws NullPointerException, IOException {
        File f = new File(dataDir + File.separator + fname);
        // only delete a file that exists
        //
        if (f.exists()) {
            // try the delete. If it fails, complain
            //
            if (!f.delete()) {
                throw new IOException("Could not delete file: " + dataDir + "/" + fname + ".");
            }
        }
    }

    /**
     * rename a file
     *
     * @return true if succeeded
     */
    public static final boolean renameFile(String oldName, String newName) {
        File f_old = new File(oldName);
        File f_new = new File(newName);
        boolean ret = f_old.renameTo(f_new);
        return ret;
    }

    /**
     * Strip the path and suffix of a file name
     *
     * @param file "/usr/local/dbase/test.DBF"
     * @return "test"
     */
    public static final String stripPathAndExtension(final String file) {
        String sep = File.separator;
        int begin = file.lastIndexOf(sep);
        if (begin < 0) {
            begin = 0;
        } else {
            begin++;
        }
        int end = file.lastIndexOf(".");
        if (end < 0) {
            end = file.length();
        }
        return file.substring(begin, end);
    }

    public static final String getFilePath(final String file) {
        String sep = File.separator;
        String[] otherSeprators = new String[]{"//", "\\", "/"};
        int begin = file.lastIndexOf(sep);
        if (begin < 0) {
            begin = 0;
            for (int i = 0; i < otherSeprators.length; i++) {
                begin = file.lastIndexOf(otherSeprators[i]);
                if (begin < 0) {
                    begin = 0;
                } else {
                    sep = otherSeprators[i];
                    break;
                }
            }
        }
        //begin++;
        return file.substring(0, begin + sep.length());
    }

    public static final String stripAllPaths(final String file) {
        String[] otherSeprators = new String[]{"//", "\\", "/", "\\\\"};
        String sep = File.separator;
        //System.err.println(" Strip all paths "+file+" ["+sep+"]");
        int begin = file.lastIndexOf(sep);
        if (begin < 0) {
            begin = -1;
            for (int i = 0; i < otherSeprators.length; i++) {
                begin = file.lastIndexOf(otherSeprators[i]);
                if (begin >= 0) {
                    sep = otherSeprators[i];
                    break;
                }
            }
        }
        //System.err.println("<stripAllPaths> "+file +" ["+sep+"] "+begin);
        if (begin >= 0) {
            begin = begin + (sep.length());
            return file.substring(begin);
        } else {
            return file;
        }
    }

    /**
     * Strip the path
     *
     * @param file "/usr/local/dbase/test.DBF"
     * @return "test"
     */
    public static final String stripPath(final String file) {
        String sep = File.separator;
        int begin = file.lastIndexOf(sep);
        if (begin < 0) {
            begin = 0;
        } else {
            begin = begin + (sep.length() - 1);
        }
        return file.substring(begin);
    }

    /**
     * Returns extension
     *
     * @param file "/usr/local/dbase/test.DBF"
     * @return "test"
     */
    public static final String getFileExtension(final String file) {
        int end = file.lastIndexOf(".");
        if (end < 0) {
            return "";
        }
        String str = file.substring(end + 1);
        return str;
    }

    static class MyDir extends javax.swing.filechooser.FileSystemView {

        public File createNewFolder(File containingDir) {
            return null;
        }

        public File[] getRoots() {
            return null;
        }

        public boolean isHiddenFile(File f) {
            return false;
        }

        public boolean isRoot(File f) {
            return false;
        }
    }

    public static final Vector getAllFiles(final String path, final String suffix) {
        MyDir view = new MyDir();
        Vector vec = new Vector(20);
        File dir = view.createFileObject(path);
        File[] ff = view.getFiles(dir, false);
        String upperSuffix = null;
        if (suffix != null) {
            upperSuffix = suffix.toUpperCase();
        }
        for (int ii = 0; ii < ff.length; ii++) {
            String file = ff[ii].toString().toUpperCase();
            if (upperSuffix == null || file.endsWith(upperSuffix)) {
                vec.addElement(ff[ii]);
            }
        }
        return vec;
    }

    public static boolean debug = false;

    public static void log(String id, String str) {
        if (debug) {
            log(id + ": " + str);
        }
    }

    public static void log(String str) {
        if (debug) {
            System.out.println(str);
        }
    }

    private static String _months[] = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
    private static int calendarMonths[] = {Calendar.JANUARY, Calendar.FEBRUARY, Calendar.MARCH, Calendar.APRIL, Calendar.MAY, Calendar.JUNE, Calendar.JULY, Calendar.AUGUST, Calendar.SEPTEMBER, Calendar.OCTOBER, Calendar.NOVEMBER, Calendar.DECEMBER};

    public static int getMonth(String month) {
        for (int i = 0; i < _months.length; i++) {
            if (JWebUtils.same(month, _months[i])) {
                return calendarMonths[i];
            }
        }
        return -1;
    }

    public static String getMonth(int month) {
        return _months[month].substring(0, 1).toUpperCase() + _months[month].substring(1).toLowerCase();
    }

    public static java.util.Date convertStringToDate2(String s) throws RuntimeException {
        java.util.Date d;
        String formats[] = new String[]{"M/dd/yy", "M/dd/yyyy", "mm/dd/yyyy H:m:s", "yy-M-d H:m:s", "yyyy-M-d H:m:s", "yyyy-M-d", "yy-M-d H:m:s", "dd/mm/yyyy"};
        String message = "";
        for (int i = 0; i < formats.length; i++) {
            SimpleDateFormat sdf = new SimpleDateFormat(formats[i]);
            try {
                sdf.setLenient(false);
                d = sdf.parse(s);
                return d;
            } catch (ParseException e) {
                message += "{" + e.getMessage() + " " + sdf.toPattern() + "," + e.getErrorOffset() + "}";
            }
        }
        throw new RuntimeException("No valid format was found " + message);
    }

    public static String urlEncode(String s) {
        if (isNull(s)) {
            return "";
        }
        return globalReplace(globalReplace(globalReplace(globalReplace(s, " ", "%20"), "&", "%26"), "!", "%21"), "#", "%23");
    }

    public static String stripPhone(String s) {
        String sLegal = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            if (sLegal.indexOf(s.charAt(i)) >= 0) {
                sb.append(s.charAt(i));
            }
        }
        return sb.toString().toLowerCase();
    }

    public static boolean isSame(String s1, String s2) {
        return same(s1, s2, true);
    }

    public static boolean isSame(String s1, String s2, boolean ignoreCase) {
        return same(s1, s2, ignoreCase);
    }

    public static boolean isNotSame(String s1, String s2) {
        return notSame(s1, s2);
    }

    public static boolean notSame(String s1, String s2) {
        return !same(s1, s2);
    }

    public static boolean same(String s1, String s2) {
        return same(s1, s2, true);
    }

    public static boolean same(String s1, String s2, boolean ignoreCase) {
        if (s1 != null && s2 != null && s1.equals(s2)) {
            return true;
        }
        boolean isNull_s1 = JWebUtils.isNull(s1), isNull_s2 = JWebUtils.isNull(s2);
        boolean s1_null = (s1 == null ? true : false), s2_null = (s2 == null ? true : false);
        if (isNull_s1 && isNull_s2) {
            return true;
        }
        if (isNull_s1 && s2.equals(JWebKeyword.NVL)) {
            return true;
        }
        if (isNull_s2 && s1.equals(JWebKeyword.NVL)) {
            return true;
        }
        if ((isNull_s1 && !isNull_s2) || (!isNull_s1 && isNull_s2)) {
            return false;
        }
        if (ignoreCase && s1.trim().equalsIgnoreCase(s2.trim())) {
            return true;
        }
        try {
            double d1 = Double.parseDouble(s1), d2 = Double.parseDouble(s2);
            if (d1 == d2) {
                return true;
            }
            //if (Math.abs(d1-d2)< .0000001) return true;
            return false;
        } catch (Exception ex) {
            //ex.printStackTrace();
            return false;
        }
    }

    public static java.util.Date convertStringToDate(String s) throws ParseException {
        java.util.Date d = null;
        SimpleDateFormat sdf1 = new SimpleDateFormat("M/d/yy H:m:s");
        SimpleDateFormat sdf2 = null;
        boolean bError = false;
        try {
            sdf1.setLenient(false);
            d = sdf1.parse(s);
            //o(s + " worked with M/d/yy H:m:s");
        } catch (ParseException e) {
            bError = true;
        }
        if (bError) {
            sdf2 = new SimpleDateFormat("M/d/yy");
            try {
                sdf2.setLenient(false);
                d = sdf2.parse(s);
                //o(s + " worked with M/d/yy");
            } catch (ParseException e) {
                throw new ParseException(e.getMessage() + "; valid format is " + sdf1.toPattern() + " or " + sdf2.toPattern(), e.getErrorOffset());
            }
        }
        return d;
    }

    public static String convertDateToString2(java.util.Date d) {
        return convertDateToString2(d, false);
    }

    public static String convertDateToString2(java.util.Date d, boolean stripTime) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(d);
        if (stripTime || (gc.get(gc.HOUR_OF_DAY) + gc.get(gc.MINUTE) + gc.get(gc.SECOND) == 0)) {
            return (gc.get(gc.MONTH) + 1) + "/" + gc.get(gc.DATE) + "/" + gc.get(gc.YEAR);
        }
        return (gc.get(gc.MONTH) + 1) + "/" + gc.get(gc.DATE) + "/" + gc.get(gc.YEAR) + " " + gc.get(gc.HOUR_OF_DAY) + ":" + gc.get(gc.MINUTE) + ":" + gc.get(gc.SECOND);
    }

    public static String getCurrentDateTime() {
        GregorianCalendar gc = new GregorianCalendar();
        java.util.Date d = gc.getTime();
        return convertDateToString2(d);
    }

    public static String toClassCase(String s) {
        s = s.toLowerCase();
        StringTokenizer st = new StringTokenizer(s, "_");
        String ss = new String("");
        for (; st.hasMoreElements();) {
            s = (String) st.nextElement();
            ss = ss + s.substring(0, 1).toUpperCase() + s.substring(1);
        }
        return ss;
    }

    public static boolean isNumberFormat(String s) {
        int len = s.length();
        if (len == 0) {
            return false;
        }
        int decimalCount = 0;
        for (int i = 0; i < len; i++) {
            if (!Character.isDigit(s.charAt(i))) {
                if (s.charAt(i) == '.') {
                    decimalCount++;
                }
                if (decimalCount > 1) {
                    return false;
                }
                if (s.charAt(i) != '.') {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isNumber(String s) {
        if (s == null) {
            return false;
        }

        boolean b = true;
        try {
            float f = Float.parseFloat(s);
        } catch (NumberFormatException nfe) {
            b = false;
        }
        return b;
    }

    public static String convertBooleanToString(boolean value) {
        return (value ? "true" : "false");
    }

    public static boolean stringToBoolean(String s) {
        return (!isNull(s) && s.equals("Y") ? true : false);
    }

    public static boolean convertStringToBoolean(String s) {
        if (isNull(s)) {
            return false;
        }
        s = s.trim().toUpperCase();
        return (!isNull(s) && (s.equals("YES") || s.equals("Y") || s.equals("TRUE") || s.equals("T") || s.equals("1")) ? true : false);
    }

    public static void o(String s) {
        if (inDebug()) {
            System.out.println("STRINGUTILS: " + s);
        }
    }

    public static File findFileOnClassPath(String name) {
        String classpath = System.getProperty("java.class.path");
        String pathSeparator = System.getProperty("path.separator");
        String fileSeparator = System.getProperty("file.separator");
        StringTokenizer tokenizer = new StringTokenizer(classpath, pathSeparator);
        while (tokenizer.hasMoreTokens()) {
            String pathElement = tokenizer.nextToken();
            File directoryOrJar = new File(pathElement);
            File absoluteDirectoryOrJar = directoryOrJar.getAbsoluteFile();
            if (absoluteDirectoryOrJar.isFile()) {
                File target = new File(absoluteDirectoryOrJar.getParent() + fileSeparator + name);
                if (target.exists()) {
                    return target;
                }
            } else {
                File target = new File(pathElement + fileSeparator + name);
                if (target.exists()) {
                    return target;
                }
            }
        }
        return null;
    }

    public static boolean isImplemented(Class x, String interfaceName) {
        Class[] xImplementedInterfaces = x.getClass().getInterfaces();
        boolean xImplements = false;
        for (int i = 0; i < xImplementedInterfaces.length && !xImplements; i++) {
            //System.err.println(xImplementedInterfaces[i].getName());
            xImplements = xImplementedInterfaces[i].getName().equalsIgnoreCase(interfaceName);
        }
        return xImplements;
    }

    public static double mod(double numerator, double denominator) {
        double result;
        result = Math.IEEEremainder(numerator, denominator);
        if (result < 0) {
            result += denominator;
        }
        return result;
    } // end mod()

    /**
     * Rounds towards zero.
     *
     * @param value Value to round.
     * @return Value rounded towards zero (returned as double).
     */
    public static double frac(double value) {
        double result;
        result = value - trunc(value);
        if (result < 0.0) {
            result += 1.0;
        }
        return result;
    } // end frac()

    public static double round(double d, int scale, int mode) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        return (bd.setScale(scale, mode)).doubleValue();
    }

    public static double round(double d, int scale) {
        return round(d, scale, BigDecimal.ROUND_HALF_UP);
    }

    public static int trunc(double value) {
        int result;
        result = (int) Math.floor(Math.abs(value));
        if (value < 0.0) {
            result *= -result;
        }
        return result;
    } // end trunc()

    public static boolean isNull(float f) {
        if (f != f) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNull(double d) {
        if (d != d) {
            return true;
        } else {
            return false;
        }
    }

    private static void deleteDir(File file) throws IOException {

        if (file.isDirectory()) {
            //directory is empty, then delete it
            if (file.list().length == 0) {

                file.delete();
                System.out.println("Directory is deleted : " + file.getAbsolutePath());

            } else {

                //list all the directory contents
                String files[] = file.list();
                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File(file, temp);
                    //recursive delete
                    deleteDir(fileDelete);
                }
                //check the directory again, if empty then delete it
                if (file.list().length == 0) {
                    file.delete();
                    //System.out.println("Directory is deleted : "+ file.getAbsolutePath());
                }
            }

        } else {
            //if file, then delete it
            file.delete();
            //System.out.println("File is deleted : " + file.getAbsolutePath());
        }
    }

    public static void deleteDirectory(String folder) {
        File directory = new File(folder);
        //make sure directory exists
        if (!directory.exists()) {
            System.out.println("Directory does not exist.");
            //System.exit(0);
        } else {
            try {
                deleteDir(directory);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }

    public static Calendar parseDate(String s) {
        int defaultCenturyStart = 0;
        String wtb[] = {"am", "pm", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday", "january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december", "gmt", "ut", "utc", "est", "edt", "cst", "cdt", "mst", "mdt", "pst", "pdt"};
        // this time zone table needs to be expanded
        int ttb[] = {14, 1, 0, 0, 0, 0, 0, 0, 0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 10000 + 0, 10000 + 0, 10000 + 0, // GMT/UT/UTC
            10000 + 5 * 60, 10000 + 4 * 60, // EST/EDT
            10000 + 6 * 60, 10000 + 5 * 60, 10000 + 7 * 60, 10000 + 6 * 60, 10000 + 8 * 60, 10000 + 7 * 60};
        int year = Integer.MIN_VALUE;
        int mon = -1;
        int mday = -1;
        int hour = -1;
        int min = -1;
        int sec = -1;
        int millis = -1;
        int c = -1;
        int i = 0;
        int n = -1;
        int wst = -1;
        int tzoffset = -1;
        int prevc = 0;
        syntax:
        {
            if (s == null) {
                break syntax;
            }
            int limit = s.length();
            while (i < limit) {
                c = s.charAt(i);
                i++;
                if (c <= ' ' || c == ',') {
                    continue;
                }
                if (c == '(') { // skip comments
                    int depth = 1;
                    while (i < limit) {
                        c = s.charAt(i);
                        i++;
                        if (c == '(') {
                            depth++;
                        } else if (c == ')') {
                            if (--depth <= 0) {
                                break;
                            }
                        }
                    }
                    continue;
                }
                if ('0' <= c && c <= '9') {
                    n = c - '0';
                    while (i < limit && '0' <= (c = s.charAt(i)) && c <= '9') {
                        n = n * 10 + c - '0';
                        i++;
                    }
                    if (prevc == '+' || prevc == '-' && year != Integer.MIN_VALUE) {
                        // timezone offset
                        if (n < 24) {
                            n = n * 60; // EG. "GMT-3"
                        } else {
                            n = n % 100 + n / 100 * 60; // eg "GMT-0430"
                        }
                        if (prevc == '+') // plus means east of GMT
                        {
                            n = -n;
                        }
                        if (tzoffset != 0 && tzoffset != -1) {
                            break syntax;
                        }
                        tzoffset = n;
                    } else if (n >= 70) {
                        if (year != Integer.MIN_VALUE) {
                            break syntax;
                        } else if (c <= ' ' || c == ',' || c == '/' || i >= limit) // year = n < 1900 ? n : n - 1900;
                        {
                            year = n;
                        } else {
                            break syntax;
                        }
                    } else if (c == ':') {
                        if (hour < 0) {
                            hour = (byte) n;
                        } else if (min < 0) {
                            min = (byte) n;
                        } else {
                            break syntax;
                        }
                    } else if (c == '/') {
                        if (mon < 0) {
                            mon = (byte) (n - 1);
                        } else if (mday < 0) {
                            mday = (byte) n;
                        } else {
                            break syntax;
                        }
                    } else if (i < limit && c != ',' && c > ' ' && c != '-') {
                        break syntax;
                    } else if (hour >= 0 && min < 0) {
                        min = (byte) n;
                    } else if (min >= 0 && sec < 0) {
                        sec = (byte) n;
                    } else if (mday < 0) {
                        mday = (byte) n;
                    } // Handle two-digit years < 70 (70-99 handled above).
                    else if (year == Integer.MIN_VALUE && mon >= 0 && mday >= 0) {
                        year = n;
                    } else {
                        break syntax;
                    }
                    prevc = 0;
                } else if (c == '/' || c == ':' || c == '+' || c == '-') {
                    prevc = c;
                } else {
                    int st = i - 1;
                    while (i < limit) {
                        c = s.charAt(i);
                        if (!('A' <= c && c <= 'Z' || 'a' <= c && c <= 'z')) {
                            break;
                        }
                        i++;
                    }
                    if (i <= st + 1) {
                        break syntax;
                    }
                    int k;
                    for (k = wtb.length; --k >= 0;) {
                        if (wtb[k].regionMatches(true, 0, s, st, i - st)) {
                            int action = ttb[k];
                            if (action != 0) {
                                if (action == 1) { // pm
                                    if (hour > 12 || hour < 1) {
                                        break syntax;
                                    } else if (hour < 12) {
                                        hour += 12;
                                    }
                                } else if (action == 14) { // am
                                    if (hour > 12 || hour < 1) {
                                        break syntax;
                                    } else if (hour == 12) {
                                        hour = 0;
                                    }
                                } else if (action <= 13) { // month!
                                    if (mon < 0) {
                                        mon = (byte) (action - 2);
                                    } else {
                                        break syntax;
                                    }
                                } else {
                                    tzoffset = action - 10000;
                                }
                            }
                            break;
                        }
                    }
                    if (k < 0) {
                        break syntax;
                    }
                    prevc = 0;
                }
            }
            if (year == Integer.MIN_VALUE || mon < 0 || mday < 0) {
                break syntax;
            }
            // Parse 2-digit years within the correct default century.
            if (year < 100) {
                year += (defaultCenturyStart / 100) * 100;
                if (year < defaultCenturyStart) {
                    year += 100;
                }
            }
            year -= 1900;
            if (sec < 0) {
                sec = 0;
            }
            if (min < 0) {
                min = 0;
            }
            if (hour < 0) {
                hour = 0;
            }
            Calendar cal = Calendar.getInstance();
            cal.set(year, mon, mday, hour, min, sec);
            return cal;
            //return new java.util.Date ().getTime();
            //return UTC(year, mon, mday, hour, min, sec) + tzoffset * (60 * 1000);
        }
        // syntax error
        throw new IllegalArgumentException();
    }

    public static float getTimeDiffInHours(java.util.Date date1, java.util.Date date2) {
        int MILLIS_PER_HOUR = 60 * 60 * 1000;
        return Math.abs((date1.getTime() - date2.getTime()) / MILLIS_PER_HOUR);
    }

    public static float getTimeDiffInMinutes(java.util.Date date1, java.util.Date date2) {
        int MILLIS_PER_MINUTE = 60 * 1000;
        return Math.abs((date1.getTime() - date2.getTime()) / MILLIS_PER_MINUTE);
    }

    public static BufferedImage getBufferedImage(int w, int h) {
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.createGraphics();
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        return bi;
    }

    public static String getNewLineChar() {
        return System.getProperties().getProperty("line.separator");
    }

    public static String getColorName(Color color) {
        String rgb = Integer.toString(color.getRed());
        rgb += "_" + Integer.toString(color.getBlue());
        rgb += "_" + Integer.toString(color.getGreen());
        return rgb;
    }

    public static String unFormat(String s, String format) {
        try {
            DecimalFormat formatter = new DecimalFormat(format);
            Number number = formatter.parse(s);
            return number.toString();
        } catch (Exception ex) {
            //ex.printStackTrace();
            //System.err.println(ex.getMessage()+" "+s+" "+format);
        }
        //    try {
        //      SimpleDateFormat formatter = new SimpleDateFormat(format);
        //      java.util.Date d = formatter.parse(s);
        //      return d.toString();
        //    }
        //    catch (Exception ex) {
        //      //System.err.println(ex.getMessage()+" "+s+" "+format);
        //    }
        return s;
    }

    public static String removeQuotes(String s) {
        s = JWebUtils.replaceStr(s, "'", "");
        s = JWebUtils.replaceStr(s, "\"", "");
        return s;
    }

    public static String removeEndQuotes(String s) {
        s = s.trim();
        if ((s.startsWith("\"") && s.endsWith("\"")) || (s.startsWith("'") && s.endsWith("'"))) {
            s = s.substring(1, s.length() - 1);
        }
        //    if ((s.substring(0,1).equals("\"") && s.substring(s.length()-1,s.length()).equals("\"")) || ((s.substring(0,1).equals("'") && s.substring(s.length()-1,s.length()).equals("'")))) {
        //      s=s.substring(1,s.length()-1);
        //    }
        return s;
    }

    public static Rectangle2D.Float getFloatRect(Rectangle r) {
        if (r == null) {
            return null;
        }
        return new Rectangle2D.Float(r.x, r.y, r.width, r.height);
    }

    public static Rectangle getZoomRect(Rectangle r, double zoomFactor) {
        if (r == null) {
            return null;
        }
        return new Rectangle((int) (zoomFactor * r.x), (int) (zoomFactor * r.y), (int) (zoomFactor * r.width), (int) (zoomFactor * r.height));
    }

    private static String getValidVarName(String name) {
        name = JWebUtils.globalReplace(name, " ", "_");
        name = JWebUtils.globalReplace(name, "&", "_");
        name = JWebUtils.globalReplace(name, ";", "_");
        name = JWebUtils.globalReplace(name, ".", "_");
        name = name.replace('+', '_');
        name = name.replace('(', '_');
        name = name.replace('"', '_');
        name = name.replace(')', '_');
        name = name.replace('/', '_');
        return name;
    }

    public static String getValidName(String name) {
        name = name.replace(' ', '_');
        name = name.replace(',', '_');
        name = name.replace('?', '_');
        name = name.replace('.', '_');
        name = name.replace('&', '_');
        name = name.replace(';', '_');
        name = name.replace('-', '_');
        name = name.replace('+', '_');
        name = name.replace('(', '_');
        name = name.replace('"', '_');
        name = name.replace(')', '_');
        name = name.replace('/', '_');
        name = name.replace('\\', '_');
        name = name.replace(':', '_');
        return name;
    }

    public static boolean isExpr(String var) {
        if (var.trim().indexOf(' ') >= 0) {
            return true;
        }
        if (var.trim().indexOf(',') >= 0) {
            return true;
        }
        if (var.trim().indexOf('?') >= 0) {
            return true;
        }
        if (var.trim().indexOf('.') >= 0) {
            return true;
        }
        if (var.trim().indexOf('&') >= 0) {
            return true;
        }
        if (var.trim().indexOf(';') >= 0) {
            return true;
        }
        if (var.trim().indexOf('-') >= 0) {
            return true;
        }
        if (var.trim().indexOf('+') >= 0) {
            return true;
        }
        if (var.trim().indexOf('(') >= 0) {
            return true;
        }
        if (var.trim().indexOf('"') >= 0) {
            return true;
        }
        if (var.trim().indexOf(')') >= 0) {
            return true;
        }
        if (var.trim().indexOf('/') >= 0) {
            return true;
        }
        if (var.trim().indexOf('\\') >= 0) {
            return true;
        }
        if (var.trim().indexOf(':') >= 0) {
            return true;
        }
        if (var.trim().indexOf('[') >= 0) {
            return true;
        }
        if (var.trim().indexOf(']') >= 0) {
            return true;
        }
        return false;
    }
    //start SHAKEEL 21 July 2006

    public static boolean isCalculated(String var) {
        if (var.trim().indexOf('@') >= 0) {
            return true;
        }
        return false;
    }
    //end SHAKEEL 21 July 2006

    public static String getPlatform() {
        String platform = System.getProperty("os.name");
        if (contains(platform, "Windows") > -1) {
            return "Windows";
        } else if (contains(platform, "Unix") > -1) {
            return "Unix";
        } else if (contains(platform, "Linux") > -1) {
            return "Linux";
        } else if (contains(platform, "SunOS") > -1) {
            return "Unix";
        }
        return "Windows";
    }

    public static String getBrowserPathSeparator() {
        return "/";
    }

    public static String getSeparator() {
        String WindowsSeparator = "\\";
        String UnixLinuxSeparator = "/";
        String separator = "";
        if (getPlatform().equals("Windows")) {
            separator = WindowsSeparator + WindowsSeparator;
        } else {
            separator = UnixLinuxSeparator;
        }
        return separator;
    }

    public static int contains(String st, String stringToSearchFor) {
        return st.indexOf(stringToSearchFor);
    }

    public static void printCollection(java.util.List list) {
        System.err.println("**********    " + list.size() + "      ********");
        for (int i = 0; i < list.size(); i++) {
            System.err.print("(" + i + ")" + list.get(i).toString());
            if (i + 1 % 6 == 0) {
                System.err.println("");
            }
        }
        System.err.println("");
    }

    public static double sqr(double x) {
        return (x) * (x);
    }

    public static String getTimeStamp() {
        Calendar cal = Calendar.getInstance();
        String time = "" + cal.get(Calendar.YEAR);
        time += "_" + cal.get(Calendar.MONTH + 1);
        time += "_" + cal.get(Calendar.DAY_OF_WEEK + 1);
        time += "_" + cal.get(Calendar.HOUR);
        time += "_" + cal.get(Calendar.MINUTE);
        time += "_" + cal.get(Calendar.SECOND);
        return time;
    }

    public static String[] getChunks(String strValue, int chunkSize) {
        return getChunks(strValue, chunkSize, 0, strValue.length());
    }

    public static String[] getChunks(String strValue, int chunkSize, int startPos, int endPos) {
        String chunk = "";
        int chunkPos = startPos;
        ArrayList clause = new ArrayList();
        while (chunkPos < endPos) {
            chunk = strValue.substring(chunkPos, Math.min(endPos, chunkPos + chunkSize));
            clause.add(chunk);
            chunkPos = chunkPos + chunkSize;
        }
        String result[] = new String[clause.size()];
        for (int i = 0; i < clause.size(); i++) {
            result[i] = (String) clause.get(i);
        }
        return result;
    }

    public static void makeDir(String path) {
        File newFile = new File(path);
        if (newFile.exists()) {
            return;
        } else {
            newFile.mkdirs();
        }
    }

    public static String getRandomStr() {
        return getRandomStr(999999);
    }

    public static String getRandomStr(int upperBound) {
        return Integer.toString((int) (Math.random() * upperBound));
    }

    public static Color getColor(int rgb) {
        //extracting color from a packed pixel
        int r = (rgb & 0xff0000) >> 16;
        int g = (rgb & 0xff00) >> 8;
        int b = (rgb & 0xff);
        return new Color(r, g, b);
    }

    public static String getNewId() {
        return (Double.toString(Math.random() * 100000000) + Long.toString(System.currentTimeMillis())).replace('.', '_');
    }

    public static ArrayList getFontSizeList() {
        ArrayList fontList = new ArrayList();
        fontList.add("6");
        fontList.add("8");
        fontList.add("10");
        fontList.add("12");
        fontList.add("14");
        fontList.add("16");
        fontList.add("20");
        fontList.add("24");
        fontList.add("32");
        return fontList;
    }

    public static ArrayList getFontAttrList() {
        ArrayList fontList = new ArrayList();
        fontList.add("Bold");
        fontList.add("Italic");
        fontList.add("Plain");
        return fontList;
    }

    public static ArrayList getFontList() {
        ArrayList fontList = new ArrayList();
        Toolkit tk = Toolkit.getDefaultToolkit();
        String[] fontnames = tk.getFontList();
        for (int i = 0; i < fontnames.length; i++) {
            fontList.add(fontnames[i]);
        }
        return fontList;
    }

    public static int getStringWidth(Font font, String text) {
        return Toolkit.getDefaultToolkit().getFontMetrics(font).stringWidth(text);
    }

    public static int getStringHeight(Font font) {
        FontMetrics fm = Toolkit.getDefaultToolkit().getFontMetrics(font);
        return fm.getAscent() + fm.getDescent();
    }

    public static Dimension getStringBounds(Font font, String text) {
        if (JWebUtils.isNull(text)) {
            return new Dimension(1, 1);
        }
        FontMetrics fm = Toolkit.getDefaultToolkit().getFontMetrics(font);
        Dimension result = new Dimension();
        result.height = fm.getAscent() + fm.getDescent();
        result.width = fm.stringWidth(text);
        return result;
    }

    public static boolean isBetween(int source, int start, int end) {
        return (source >= start && source <= end);
    }

    public static String readTextFromFile(String fileName) {
       return readTextFromFile(new File(fileName));
    }
    public static String readTextFromFile(File file) {
        String text = null;
        try {
            iniFileName = file.getName();
            BufferedReader in = new BufferedReader(new FileReader(file));
            String inputLine = "";
            String newLine = JWebUtils.getNewLineChar();
            while ((inputLine = in.readLine()) != null) {
                if (text == null) {
                    text = inputLine;
                } else {
                    text = text + newLine + inputLine;
                }
            }
            in.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return text;
    }

    public static ArrayList<String> readTextFromFileAsLines(String fileName) {
        ArrayList<String> text = new ArrayList();
        try {
            iniFileName = fileName;
            BufferedReader in = new BufferedReader(new FileReader(new File(fileName)));
            String inputLine = "";
            String newLine = JWebUtils.getNewLineChar();
            while ((inputLine = in.readLine()) != null) {
                text.add(inputLine);
            }
            in.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return text;
    }

    public static boolean leadingzeros = false;

    public static final String format(double d) {
        return format("" + d, 2);
    }

    public static final String format(int i) {
        return format("" + i, 0);
    }

    public static final String format(int i, int len) {
        return format("" + i, 0, len);
    }

    public static final String format(String in) {
        return format(in, 2);
    }

    public static final String format(String in, int dp, int len) {
        StringBuffer sb = new StringBuffer();
        String s = format(in, dp).trim();
        int diff = len - s.length();
        for (int i = 0; i < diff; i++) {
            sb.append(" ");
        }
        sb.append(s);
        return sb.toString();
    }

    public static final String format(String in, int dp) {
        int e1 = in.indexOf('e');
        int e2 = in.indexOf('E');
        int e = Math.max(e1, e2);
        if (e > -1) {
            in = expand(in, e);
        }
        int i = in.lastIndexOf('.');
        if (i != -1) {
            String dec = "";
            String num = in.substring(0, i);
            if (dp > 0) {
                if ((i + dp + 1) < in.length()) {
                    dec = in.substring(i, i + dp + 1);
                } else {
                    dec = in.substring(i);
                }
            } else {
                dec = "";
            }
            while (dec.length() < dp + 1) {
                dec += "0";
            }
            if (dp == 0) {
                dec = "";
            }
            if (!leadingzeros) {
                char[] tmp = num.toCharArray();
                for (i = 0; i < tmp.length - 1; i++) {
                    if (tmp[i] != '0' && tmp[i] != ' ') {
                        break;
                    }
                    if (tmp[i] == '0') {
                        tmp[i] = ' ';
                    }
                    if (tmp[i + 1] == '.' && tmp[i] == ' ') {
                        tmp[i] = '0';
                    }
                }
                num = new String(tmp);
            }
            return (num + dec);
        } else {
            String dec = ".";
            while (dec.length() < dp + 1) {
                dec += "0";
            }
            if (dp == 0) {
                dec = "";
            }
            if (!leadingzeros) {
                char[] tmp = in.toCharArray();
                for (i = 0; i < tmp.length - 1; i++) {
                    if (tmp[i] != '0' && tmp[i] != ' ') {
                        break;
                    }
                    if (tmp[i] == '0') {
                        tmp[i] = ' ';
                    }
                    if (tmp[i + 1] == '.' && tmp[i] == ' ') {
                        tmp[i] = '0';
                    }
                }
                in = new String(tmp);
            }
            return (in + dec);
        }
    }

    private static final String expand(String s, int e) {
        String last = s.substring(e + 1);
        String start = s.substring(0, e);
        int pow = Integer.parseInt(last);
        //System.out.println(start +" e "+last+" "+pow);
        int i = start.indexOf('.');
        if (i > 0) {
            int d = start.length() - i - 1;
            String a = start.substring(0, i);
            start = a + start.substring(i + 1);
            pow -= d;
        }
        for (i = 0; i < pow; i++) {
            start += "0";
        }
        for (i = pow; i < 0; i++) {
            start = "0" + start;
        }
        if (pow < 0) {
            int lp = start.length() + pow;
            //System.out.println(start+" x "+lp+" "+pow);
            start = start.substring(0, lp) + "." + start.substring(lp);
        }
        //System.out.println("->"+start);
        return start.trim();
    }

    public static Connection getCubeConnection(String cubePath) {
        Connection con = null;
        //System.err.println(" CUBE PATH :"+cubePath);
        if (cubePath == null) {
            return null;
        }
        try {
            Class.forName("com.jtools.javawebcube.cubedb.CubeDriver");
            con = DriverManager.getConnection("jdbc:cube:" + cubePath, "sa", "");
        } catch (Exception e) {
            System.err.println("No Cube Connection classes");
            e.printStackTrace();
        }
        return con;
    }

    private static ArrayList getArrayList(int itemCount, Object obj) {
        ArrayList aList = new ArrayList(itemCount);
        for (int i = 0; i < itemCount; i++) {
            aList.add(obj);
        }
        return aList;
    }

    public static char getDecimalPointChar() {
        if (DECIMAL != 0) {
            return DECIMAL;
        } else {
            return DECIMAL = (new Double(5.5D)).toString().charAt(1);
        }
    }

    public static final double degToRad(double d) {
        return d * 0.017453292519943295D;
    }

    public static final double radToDeg(double d) {
        return d * 57.295779513082323D;
    }

    public static boolean validUnit(int i) {
        return i == 1 || i == 2 || i == 3;
    }

    public static double boundAngle(int i, double d) {
        boolean flag = d < 0.0D;
        double d1 = 0.0D;
        switch (i) {
            case 1: // '\001'
                d1 = 360D;
                break;
            case 3: // '\003'
                d1 = 400D;
                break;
            default:
                d1 = 6.2831853071795862D;
                break;
        }
        double d2 = fmod(Math.abs(d), d1);
        if (flag && d2 != 0.0D) {
            d2 = -d2;
        }
        return d2;
    }

    public static double log10(double d) {
        double d1 = Math.log(d);
        return d1 / 2.3025850929940002D;
    }

    public static int intLog10(double d) {
        return (int) precCorrect(1, log10(d));
    }

    public static int nicePrecision(double d) {
        if (d <= 0.0D) {
            return 0;
        } else {
            int i = (int) Math.floor(log10(d));
            double d1 = d / pow10(i);
            int j = d1 >= 3D ? 0 : 1;
            return (int) (-Math.floor(log10(d)) + (double) j);
        }
    }

    public static int tickBasedPrecision(double d) {
        if (d <= 0.0D) {
            return 0;
        }
        String s = (new Double(d)).toString();
        int i = s.indexOf('.');
        if (i >= 0) {
            int j = s.length() - i - 1;
            for (int l = s.length() - 1; l > i; l--) {
                if (s.charAt(l) == '0') {
                    j--;
                } else {
                    l = i;
                }
            }
            if (j > 0) {
                return j;
            }
        } else {
            i = s.length();
        }
        int k = 0;
        for (int i1 = i - 1; i1 >= 0; i1--) {
            if (s.charAt(i1) == '0') {
                k++;
            } else {
                i1 = -1;
            }
        }
        return -k;
    }

    public static double clamp(double d, double d1, double d2) {
        return d <= d2 ? d >= d1 ? d : d1 : d2;
    }

    public static int clamp(int i, int j, int k) {
        return i <= k ? i >= j ? i : j : k;
    }

    public static long clamp(long l, long l1, long l2) {
        return l <= l2 ? l >= l1 ? l : l1 : l2;
    }

    public static double precFloor(int i, double d) {
        double d1 = d;
        if (d1 < 0.0D) {
            d1 = -precCeil(i, -d1);
        } else if (i >= 0) {
            String s = internalFormat(d1, i + 1);
            int j = s.length();
            if (j >= 1) {
                StringBuffer stringbuffer = new StringBuffer(s);
                stringbuffer.setCharAt(j - 1, '0');
                s = new String(stringbuffer);
            }
            double d3 = d1;
            d1 = Double.valueOf(s).doubleValue();
            if (d1 > d3) {
                d1 -= pow10(-i);
            }
        } else {
            double d2 = pow10(-i);
            d1 = d2 * Math.floor(d1 / d2);
        }
        return d1;
    }

    public static double precCeil(int i, double d) {
        double d1 = d;
        if (d1 < 0.0D) {
            d1 = -precFloor(i, -d1);
        } else if (i >= 0) {
            char ac[] = internalFormat(d1, i + 1).toCharArray();
            int j = ac.length - 1;
            if (ac[j] != '0') {
                ac[j] = '0';
                for (j--; j >= 0; j--) {
                    if (ac[j] == getDecimalPointChar()) {
                        continue;
                    }
                    if (ac[j] != '9') {
                        ac[j]++;
                        break;
                    }
                    ac[j] = '0';
                }
                if (j < 0) {
                    char ac1[] = ac;
                    ac = new char[ac1.length + 1];
                    for (int k = ac1.length; k > 0; k--) {
                        ac[k] = ac1[k - 1];
                    }
                    ac[0] = '1';
                }
            }
            double d3 = d1;
            d1 = Double.valueOf(new String(ac)).doubleValue();
            if (d1 < d3) {
                d1 += pow10(-i);
            }
        } else {
            double d2 = pow10(-i);
            d1 = d2 * Math.ceil(d1 / d2);
        }
        return d1;
    }

    public static String logFormat(int i) {
        double d = pow10(i);
        return format(d, -i);
    }

    public static String format(double d, int i) {
        double d1 = precCorrect(i, d);
        String s = String.valueOf(d1);
        if (s.indexOf('e') != -1 || s.indexOf('E') != -1) {
            return s;
        }
        int j = findDecimal(s);
        if (i > 0) {
            if (j < 0) {
                StringBuffer stringbuffer = new StringBuffer(s);
                stringbuffer.append(getDecimalPointChar());
                for (int i1 = 0; i1 < i; i1++) {
                    stringbuffer.append('0');
                }
                s = new String(stringbuffer);
            } else {
                int k = s.length() - j - 1;
                int j1 = k - i;
                if (j1 < 0) {
                    StringBuffer stringbuffer1 = new StringBuffer(s);
                    for (int l1 = 0; l1 > j1; l1--) {
                        stringbuffer1.append('0');
                    }
                    s = new String(stringbuffer1);
                } else if (j1 > 0) {
                    s = s.substring(0, s.length() - j1);
                }
            }
        } else if (i < 0) {
            int l = j >= 0 ? j : s.length();
            int k1 = l + i;
            if (k1 >= 0) {
                StringBuffer stringbuffer2 = new StringBuffer(s.substring(0, k1));
                for (int i2 = 0; i2 < l - k1; i2++) {
                    stringbuffer2.append('0');
                }
                s = new String(stringbuffer2);
            } else {
                s = new String("0");
            }
        } else if (j >= 0) {
            s = s.substring(0, j);
        }
        return s;
    }

    public static String internalFormat(double d, int i) {
        String s = format(d, i);
        if (s.indexOf('e') != -1 || s.indexOf('E') != -1) {
            s = convertFromSN(s);
        }
        return s;
    }

    public static String convertFromSN(String s) {
        int i = s.indexOf('e');
        if (i < 0) {
            i = s.indexOf('E');
        }
        if (i < 0) {
            return s;
        }
        int j = 0;
        if (i + 1 < s.length()) {
            String s1 = s.substring(i + 1);
            if (s1.charAt(0) == '+') {
                j = 0;
                if (s1.length() > 1) {
                    j = Integer.valueOf(s1.substring(1)).intValue();
                }
            } else {
                j = Integer.valueOf(s1).intValue();
            }
        }
        String s2 = s.substring(0, i);
        if (j == 0) {
            return s2;
        }
        StringBuffer stringbuffer = new StringBuffer();
        char c = s2.charAt(0);
        switch (c) {
            case 45: // '-'
                stringbuffer.append('-');
            // fall through
            case 43: // '+'
                s2 = s2.substring(1);
                break;
        }
        if (j < 0) {
            stringbuffer.append("0.");
            for (int k = -1; k > j; k--) {
                stringbuffer.append('0');
            }
            stringbuffer.append(s2.charAt(0));
            for (int i1 = 2; i1 < s2.length(); i1++) {
                stringbuffer.append(s2.charAt(i1));
            }
        } else {
            stringbuffer.append(s2.charAt(0));
            int l;
            for (l = 0; l < j; l++) {
                if (l + 2 < s2.length()) {
                    stringbuffer.append(s2.charAt(l + 2));
                } else {
                    stringbuffer.append('0');
                }
            }
            if (l + 2 < s2.length()) {
                stringbuffer.append(getDecimalPointChar());
                for (; l + 2 < s2.length(); l++) {
                    stringbuffer.append(s2.charAt(l + 2));
                }
            }
        }
        return new String(stringbuffer);
    }

    private static int findDecimal(String s) {
        if (s == null) {
            return -1;
        } else {
            return s.indexOf(getDecimalPointChar());
        }
    }

    public static double convertAngle(int i, int j, double d) {
        double d1 = d;
        switch (j) {
            case 1: // '\001'
                switch (i) {
                    case 2: // '\002'
                        d1 = (d * 180D) / 3.1415926535897931D;
                        break;
                    case 3: // '\003'
                        d1 = d * 0.90000000000000002D;
                        break;
                    default:
                        d1 = d;
                        break;
                }
                break;
            case 3: // '\003'
                switch (i) {
                    case 2: // '\002'
                        d1 = (d * 200D) / 3.1415926535897931D;
                        break;
                    case 1: // '\001'
                        d1 = d / 0.90000000000000002D;
                        break;
                    default:
                        d1 = d;
                        break;
                }
                break;
            default:
                switch (i) {
                    case 1: // '\001'
                        d1 = (d * 3.1415926535897931D) / 180D;
                        break;
                    case 3: // '\003'
                        d1 = (d * 3.1415926535897931D) / 200D;
                        break;
                    default:
                        d1 = d;
                        break;
                }
                break;
        }
        return d1;
    }

    public static double pow10(int i) {
        int j = 12 + i;
        if (j >= 0 && j < tens_val.length) {
            return tens_val[j];
        } else {
            return Math.pow(10D, i);
        }
    }

    public static double precCorrect(int i, double d) {
        if (d == (-1.0D / 0.0D)) {
            return 0.0D;
        }
        if (i >= 0) {
            double d1 = pow10(i);
            return Math.floor(d * d1 + 0.5D) / d1;
        } else {
            double d2 = pow10(-i);
            return Math.floor(d / d2 + 0.5D) * d2;
        }
    }

    public static double max(double d, double d1) {
        return d <= d1 ? d1 : d;
    }

    public static double min(double d, double d1) {
        return d >= d1 ? d1 : d;
    }

    public static double abs(double d) {
        return d <= 0.0D ? -d : d;
    }

    public static int abs(int i) {
        return i <= 0 ? -i : i;
    }

    public static double fmod(double d, double d1) {
        double d2 = 2147483647D;
        if (d1 != 0.0D) {
            int i = ifloor(d / d1);
            d2 = d - d1 * (double) i;
        }
        return d2;
    }

    public static void trace() {
        try {
            throw new ArrayIndexOutOfBoundsException("");
        } catch (Exception exception) {
            exception.printStackTrace(System.out);
        }
    }

    public static int distance(int i, int j, int k, int l) {
        int i1 = k - i;
        int j1 = l - j;
        return (int) Math.sqrt(i1 * i1 + j1 * j1);
    }

    public static int distance(int i, int j, Rectangle rectangle) {
        int k = distance(i, rectangle.x, rectangle.width);
        int l = distance(j, rectangle.y, rectangle.height);
        int i1 = k * k + l * l;
        if (i1 <= 0) {
            return 0;
        } else {
            return (int) Math.sqrt(i1);
        }
    }

    public static int distance(int i, int j, int k) {
        if (i < j) {
            return j - i;
        }
        if (i > j + k) {
            return i - (j + k);
        } else {
            return 0;
        }
    }

    public static long distTrap(int i, int j, int k, double d, double d1, double d2, double d3) {
        long l;
        if (d == d1) {
            l = 32000L;
        } else {
            l = (long) (d2 + ((double) i - d) * ((d2 - d3) / (d - d1)));
        }
        long l1;
        if (d <= (double) i && (double) i <= d1 || d1 <= (double) i && (double) i <= d) {
            l1 = 1L;
        } else if ((double) i <= d && d <= d1 || (double) i >= d && d >= d1) {
            l1 = 2L;
        } else {
            l1 = 3L;
        }
        long l2;
        if (l <= (long) j && j <= k || k <= j && (long) j <= l) {
            l2 = 10L;
        } else if (l <= (long) k && k <= j || j <= k && (long) k <= l) {
            l2 = 12L;
        } else {
            l2 = 11L;
        }
        double d4 = 0.0D;
        switch ((int) l2) {
            default:
                break;
            case 10: // '\n'
                switch ((int) l1) {
                    case 1: // '\001'
                        d4 = 0.0D;
                        break;
                    case 2: // '\002'
                        if (k <= j && (double) j <= d2 || d2 <= (double) j && j <= k) {
                            d4 = Math.abs(i - (int) d);
                        } else {
                            d4 = ((double) j - d2) * ((double) j - d2) + ((double) i - d) * ((double) i - d);
                            d4 = Math.sqrt(d4);
                        }
                        break;
                    case 3: // '\003'
                        if (k <= j && (double) j <= d3 || d3 <= (double) j && j <= k) {
                            d4 = Math.abs(i - (int) d1);
                        } else {
                            d4 = ((double) j - d3) * ((double) j - d3) + ((double) i - d1) * ((double) i - d1);
                            d4 = Math.sqrt(d4);
                        }
                        break;
                }
                break;
            case 11: // '\013'
                switch ((int) l1) {
                    case 1: // '\001'
                        d4 = Math.abs(j - (int) l);
                        break;
                    case 2: // '\002'
                        d4 = ((double) j - d2) * ((double) j - d2) + ((double) i - d) * ((double) i - d);
                        d4 = Math.sqrt(d4);
                        break;
                    case 3: // '\003'
                        d4 = ((double) j - d3) * ((double) j - d3) + ((double) i - d1) * ((double) i - d1);
                        d4 = Math.sqrt(d4);
                        break;
                }
                break;
            case 12: // '\f'
                switch ((int) l1) {
                    case 1: // '\001'
                        d4 = Math.abs(j - k);
                        break;
                    case 2: // '\002'
                        d4 = (double) ((j - k) * (j - k)) + ((double) i - d) * ((double) i - d);
                        d4 = Math.sqrt(d4);
                        break;
                    case 3: // '\003'
                        d4 = (double) ((j - k) * (j - k)) + ((double) i - d1) * ((double) i - d1);
                        d4 = Math.sqrt(d4);
                        break;
                }
                break;
        }
        return (long) d4;
    }

    public static Polygon pointsToPolygon(Point[] apoint) {
        Polygon polygon = new Polygon();
        for (int i = 0; i < apoint.length; i++) {
            if (i == 0) {
                polygon.addPoint(apoint[i].x, apoint[i].y);
            } else {
                polygon.addPoint(polygon.xpoints[i - 1] + apoint[i].x, polygon.ypoints[i - 1] + apoint[i].y);
            }
        }
        return polygon;
    }

    public static Color brighter(Color color) {
        if (color.equals(Color.white)) {
            return WHITE_BRIGHTER;
        }
        if (color.equals(Color.black)) {
            return BLACK_BRIGHTER;
        } else {
            int i = color.getRed();
            i += (int) ((double) (255 - i) * 0.5D);
            int j = color.getBlue();
            j += (int) ((double) (255 - j) * 0.5D);
            int k = color.getGreen();
            k += (int) ((double) (255 - k) * 0.5D);
            return new Color(Math.min(i, 255), Math.min(k, 255), Math.min(j, 255));
        }
    }

    public static Color darker(Color color) {
        if (color.equals(Color.white)) {
            return WHITE_DARKER;
        }
        if (color.equals(Color.black)) {
            return BLACK_DARKER;
        } else {
            return color.darker();
        }
    }

    public static boolean colorsNear(Color color, Color color1) {
        if (color == null || color1 == null) {
            return false;
        }
        return Math.abs(color.getRed() - color1.getRed()) <= 15 && Math.abs(color.getGreen() - color1.getGreen()) <= 15 && Math.abs(color.getBlue() - color1.getBlue()) <= 15;
    }

    public static int iceil(double d) {
        int i = 0;
        if (d < 0.0D) {
            i = -ifloor(-d);
        } else {
            i = (int) Math.ceil(d);
        }
        return i;
    }

    public static int ifloor(double d) {
        int i = 0;
        if (d == (-1.0D / 0.0D)) {
            return i;
        }
        if (d < 0.0D) {
            i = -iceil(-d);
        } else {
            i = (int) d;
        }
        return i;
    }

    public static double calcError(int i) {
        return 0.5D * pow10(-i - 1);
    }

    public static int locateX(double d, double d1, double d2) {
        int i = (d <= d2 ? 0 : 1) << 1;
        int j = (d >= d1 ? 0 : 1) << 0;
        return i | j;
    }

    public static int locateY(double d, double d1, double d2) {
        int i = (d <= d2 ? 0 : 1) << 3;
        int j = (d >= d1 ? 0 : 1) << 2;
        return i | j;
    }

    public static int locateXY(double d, double d1, double d2, double d3, double d4, double d5) {
        return locateY(d1, d4, d5) | locateX(d, d2, d3);
    }

    /**
     * @deprecated Method isHTML is deprecated
     */
    public static boolean isHTML(String s) {
        return false;
    }

    public static int countInString(String s, String s1) {
        int i = 0;
        int j = 0;
        for (int k = 0; (k = s1.indexOf(s, j)) >= 0;) {
            i++;
            j = k + 1;
        }
        return i;
    }

    public static void fudgeItalicLabel(Font font, Dimension dimension) {
        int i = font.getStyle();
        if ((i | 2) > 0) {
            int j = font.getSize();
            dimension.width += Math.max(1, j / 6);
        }
    }

    public static final double M_PI = 3.1415926535897931D;
    public static final double M_PI_2 = 1.5707963267948966D;
    public static final double M_2PI = 6.2831853071795862D;
    public static final double M_SQRT2 = 1.4142135623730951D;
    public static final int DEGREES = 1;
    public static final int RADIANS = 2;
    public static final int GRADS = 3;
    public static final double DEFAULT_FLOAT = 1.7976931348623157E+308D;
    public static final double LOG_TEN_BASE_E = 2.3025850929940002D;
    public static char DECIMAL = '0';
    static final String tens_str[] = {"0", "00", "000", "0000", "00000", "000000", "0000000", "00000000", "000000000", "0000000000", "00000000000", "000000000000"};
    static double tens_val[] = {9.9999999999999998E-013D, 9.9999999999999994E-012D, 1E-010D, 1.0000000000000001E-009D, 1E-008D, 9.9999999999999995E-008D, 9.9999999999999995E-007D, 1.0000000000000001E-005D, 0.0001D, 0.001D, 0.01D, 0.10000000000000001D, 1.0D, 10D, 100D, 1000D, 10000D, 100000D, 1000000D, 10000000D, 100000000D, 1000000000D, 10000000000D, 100000000000D, 1000000000000D};
    public static final int XINSIDE = 1;
    public static final int XBYX1 = 2;
    public static final int XBYX2 = 3;
    public static final int YINSIDE = 10;
    public static final int YBYTOP = 11;
    public static final int YBYORIGIN = 12;
    private static final Color WHITE_BRIGHTER = new Color(200, 200, 200);
    private static final Color WHITE_DARKER = new Color(140, 140, 140);
    private static final Color BLACK_BRIGHTER = new Color(125, 125, 125);
    private static final Color BLACK_DARKER = new Color(75, 75, 75);
    private static final double FACTOR = 0.5D;

    public static boolean backfacing(int[] ai, int[] ai1) {
        long l = 0L;
        int i = Math.min(ai.length, ai1.length);
        for (int j = 0; j < i; j++) {
            int k = j >= i - 1 ? 0 : j + 1;
            l += (long) ai[j] * (long) ai1[k] - (long) ai1[j] * (long) ai[k];
        }
        return l < 0L;
    }

    public static void booleanfill(boolean[] aflag, boolean flag) {
        int i = aflag.length;
        if (i > 0) {
            aflag[0] = flag;
        }
        for (int j = 1; j < i; j += j) {
            System.arraycopy(aflag, 0, aflag, j, i - j >= j ? j : i - j);
        }
    }

    public static int min4(int i, int j, int k, int l) {
        int i1 = Math.min(i, j);
        int j1 = Math.min(k, l);
        return Math.min(i1, j1);
    }

    public static int max4(int i, int j, int k, int l) {
        int i1 = Math.max(i, j);
        int j1 = Math.max(k, l);
        return Math.max(i1, j1);
    }

    public static double floor(double d) {
        return Math.floor(d + 1E-013D);
    }

    public static double ceil(double d) {
        return Math.ceil(d - 1E-013D);
    }

    public static int nDigits(double d) {
        if (d == 0.0D) {
            return 0x3fffffff;
        } else {
            return -(int) floor(log10(abs(d)));
        }
    }

    public static double niceNum(double d, boolean flag) {
        if (d == 0.0D) {
            return 0.0D;
        }
        if (d < 0.0D) {
            d = -d;
        }
        int i = (int) floor(log10(d));
        double d1 = pow10(i);
        double d2 = d / d1;
        double d3 = 0.0D;
        if (flag) {
            if (d2 < 1.5D) {
                d3 = 1.0D;
            } else if (d2 < 3D) {
                d3 = 2D;
            } else if (d2 < 7D) {
                d3 = 5D;
            } else {
                d3 = 10D;
            }
        } else if (d2 <= 1.0D) {
            d3 = 1.0D;
        } else if (d2 <= 2D) {
            d3 = 2D;
        } else if (d2 <= 5D) {
            d3 = 5D;
        } else {
            d3 = 10D;
        }
        return d3 * d1;
    }

    public static Rectangle computeBoundingRectangle(Rectangle rectangle, Rectangle rectangle1) {
        if (rectangle == null) {
            return rectangle1;
        }
        if (rectangle1 == null) {
            return rectangle;
        } else {
            Rectangle rectangle2 = new Rectangle();
            rectangle2.x = Math.min(rectangle.x, rectangle1.x);
            rectangle2.y = Math.min(rectangle.y, rectangle1.y);
            rectangle2.width = Math.max(rectangle.x + rectangle.width, rectangle1.x + rectangle1.width) - rectangle2.x;
            rectangle2.height = Math.max(rectangle.y + rectangle.height, rectangle1.y + rectangle1.height) - rectangle2.y;
            return rectangle2;
        }
    }

    public static final double roundOffEpsilon = 1E-013D;

    public static String getArrayAsString(ArrayList aList) {
        String newLine = getNewLineChar();
        StringBuffer result = new StringBuffer();
        for (int k = 0; k < aList.size(); k++) {
            result.append(aList.get(k).toString() + newLine);
        }
        return result.toString();
    }

    public static String[] getArrayListAsStringArray(ArrayList aList) {
        String newLine = getNewLineChar();
        String[] result = new String[aList.size()];
        for (int k = 0; k < aList.size(); k++) {
            result[k] = (String) aList.get(k);
        }
        return result;
    }

    public static ArrayList<String> getStringArrayAsArrayList(String[] aList) {
        ArrayList<String> ret = new ArrayList<String>(aList.length);
        for (int k = 0; k < aList.length; k++) {
            ret.add(aList[k]);
        }
        return ret;
    }

    public static String[] getObjectArrayAsStringArray(Object[] aList) {
        String[] result = new String[aList.length];
        for (int k = 0; k < aList.length; k++) {
            result[k] = (String) aList[k];
        }
        return result;
    }

    public static String getArrayAsString(ArrayList aList, String deLimiter, String quotes) {
        return getArrayAsString(aList, deLimiter, quotes, null);
    }

    public static String getArrayAsString(ArrayList aList, String deLimiter, String quotes, String blankChar) {
        if (aList.size() == 0) {
            return "";
        }
        StringBuffer result = new StringBuffer();
        for (int k = 0; k < aList.size(); k++) {
            if (blankChar != null) {
                if (aList.get(k).toString().length() == 0) {
                    result.append(quotes + blankChar + quotes + deLimiter);
                } else {
                    result.append(quotes + aList.get(k).toString() + quotes + deLimiter);
                }
            } else {
                result.append(quotes + aList.get(k).toString() + quotes + deLimiter);
            }
        }
        String resultStr = result.toString();
        return resultStr.substring(0, resultStr.length() - deLimiter.length());
    }

    public static String getArrayAsString(String[] aList, String deLimiter) {
        if (aList == null || aList.length == 0) {
            return "";
        }
        StringBuffer result = new StringBuffer();
        for (int k = 0; k < aList.length; k++) {
            result.append(aList[k] + deLimiter);
        }
        String resultStr = result.toString();
        return resultStr.substring(0, resultStr.length() - deLimiter.length());
    }

    public static String getArrayAsString(Object[] aList, String deLimiter) {
        if (aList == null || aList.length == 0) {
            return "";
        }
        StringBuffer result = new StringBuffer();
        for (int k = 0; k < aList.length; k++) {
            result.append(aList[k].toString() + deLimiter);
        }
        String resultStr = result.toString();
        return resultStr.substring(0, resultStr.length() - deLimiter.length());
    }

    public static String getArrayAsString(int[] aList, String deLimiter) {
        if (aList == null || aList.length == 0) {
            return "";
        }
        StringBuffer result = new StringBuffer();
        for (int k = 0; k < aList.length; k++) {
            result.append(Integer.toString(aList[k]) + deLimiter);
        }
        String resultStr = result.toString();
        return resultStr.substring(0, resultStr.length() - deLimiter.length());
    }

    public static String getArrayAsString(double[] aList, String deLimiter) {
        if (aList == null || aList.length == 0) {
            return "";
        }
        StringBuffer result = new StringBuffer();
        for (int k = 0; k < aList.length; k++) {
            result.append(Double.toString(aList[k]) + deLimiter);
        }
        String resultStr = result.toString();
        return resultStr.substring(0, resultStr.length() - deLimiter.length());
    }

    public static String generateId(String prefix) {
        return getValidName(prefix + "_" + Double.toString(Math.random() * 100000000) + Long.toString(System.currentTimeMillis()));
    }

    public static void setErr(String fileName) {
        try {
            System.setErr(new PrintStream(new FileOutputStream(fileName)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getFormatted(String text, String format) {
        try {
            if (isNull(text) || isNull(format)) {
                return text;
            }
            if (isNumber(text)) {
                DecimalFormat formatter = new DecimalFormat(format);
                double number = Double.parseDouble(text);
                String formattedString = formatter.format(number);
                return formattedString;
            } else {
                SimpleDateFormat formatter = new SimpleDateFormat(format);
                return formatter.format(parseDate(text).getTime());
            }
        } catch (Exception ex) {
            return text;
        }
        //return  text;
    }

    private static StringBuffer replace1(StringBuffer buf, String token, String val) {
        boolean replaced = false;
        while (!replaced) {
            int start = buf.toString().indexOf(token);
            switch (start) {
                case -1:
                    replaced = true;
                    break;
                default:
                    int end = start + token.length();
                    buf.replace(start, end, val);
                    break;
            }
        }
        return buf;
    }

    private static StringBuffer replace2(String srcStr, String token, String val) {
        StringBuffer newBuf = new StringBuffer();
        int tokenLength = token.length();
        int lastEnd = 0;
        int start;
        while ((start = srcStr.indexOf(token, lastEnd)) != -1) {
            if (start != 0) {
                newBuf.append(srcStr.substring(lastEnd, start));
            }
            newBuf.append(val);
            lastEnd = start + tokenLength;
        }
        newBuf.append(srcStr.substring(lastEnd));
        return newBuf;
    }

    public static Connection getNewConnection(String fileName, String path) {
        try {
            return getNewConnection(new FileInputStream(fileName.trim()), path.trim());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Connection getNewConnection(Object obj, String fileName, String path) {
        return getNewConnection(obj.getClass().getResourceAsStream(fileName.trim()), path.trim());
    }

    public static Connection getNewConnection(InputStream in, String path) {
        try {
            String driver, url, user, pwd;
            try {
                java.util.Properties m_JDBCProperties = new java.util.Properties();
                m_JDBCProperties.load(in);
                driver = m_JDBCProperties.getProperty("driver").trim();
                url = m_JDBCProperties.getProperty("url").trim();
                url = JWebUtils.replaceStr(url, "&filePath:", path + File.separator);
                url = JWebUtils.replaceStr(url, JWebConstants.SEPARATOR_PATTERN, File.separator);
                user = m_JDBCProperties.getProperty("user");
                pwd = m_JDBCProperties.getProperty("password");
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception ex) {
                    }
                }
            }
            Connection con = getNewConnection(driver, url, user, pwd);
            return con;
        } catch (Exception ex) {
            ex.printStackTrace();
            //System.err.println(" JWebUtils.getNewConnection(): "+path+" "+fileName);
        }
        return null;
    }

    public static Connection getNewConnection(String driver, String db, String userid, String pwd) {
        try {
            //System.err.println(" new Connection JWebUtils");
            Class.forName(driver).newInstance();
            Connection con = DriverManager.getConnection(db, userid, pwd);
            return con;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String[] substring(String[] src, int startPos, int length) {
        String[] result = new String[length];
        //System.err.println("["+getArrayAsString(src,",")+"]"+startPos+","+length);
        for (int i = startPos; i < startPos + length; i++) {
            result[i - startPos] = src[i];
        }
        return result;
    }

    public static void substring(String[] src, int startPos, int length, String[] result) {
        //System.err.println("["+getArrayAsString(src,",")+"]"+startPos+","+length);
        for (int i = startPos; i < startPos + length; i++) {
            result[i - startPos] = src[i];
        }
    }

    public static void initArray(int[] intArray) {
        initArray(intArray, 0);
    }

    public static void initArray(int[] intArray, int value) {
        for (int i = 0; i < intArray.length; i++) {
            intArray[i] = value;
        }
    }

    public static void initArray(String[] textArray, String value) {
        for (int i = 0; i < textArray.length; i++) {
            textArray[i] = value;
        }
    }

    public static String[] getWords(String text) {
        StringTokenizer st = new StringTokenizer(text);
        String[] words = new String[st.countTokens()];
        int w = 0;
        while (st.hasMoreTokens()) {
            words[w] = st.nextToken();
            w++;
        }
        return words;
    }
    //=========================== Mahmood Code begin 14/Feb/2005 =============================

    public static double getMinStep(double[] ad, int i, boolean dataOK) {
        double d = 1.7976931348623157E+308D;
        if (!dataOK || ad == null || i < 1) {
            return d;
        }
        if (i == 1) {
            d = ad[0];
            if (d + 1.0D == 1.0D) {
                d = 1.0D;
            }
        } else {
            d = ad[1] - ad[0];
            for (int j = 1; j < i - 1; j++) {
                double d1 = ad[j + 1] - ad[j];
                if (d > d1) {
                    d = d1;
                }
            }
        }
        return d;
    }

    public static int getPointIndex(double d, double[] ad, int i) {
        if (i <= 1) {
            return 0;
        }
        int j = 0;
        for (int l = i - 1; j <= l;) {
            int k = (j + l) / 2;
            if (ad[k] < d) {
                j = k + 1;
            } else if (ad[k] > d) {
                l = k - 1;
            } else {
                return k;
            }
        }
        if (j == i || j > 0 && ad[j] > d) {
            j--;
        }
        j = Math.max(Math.min(j, i - 1), 0);
        return j;
    }

    public static String[] getNameValue(String nameValueStr, String delim) {
        int posEqualSign = nameValueStr.indexOf(delim);
        String name = nameValueStr.substring(0, posEqualSign);
        String value = nameValueStr.substring(posEqualSign + 1);
        return new String[]{name, value};
    }

    public static String[] getNameValue(String nameValueStr) {
        return getNameValue(nameValueStr, "=");
    }

    public static String[] getTokensByPattern(String str, String delim, String pattern) {
        ArrayList patternList = new ArrayList();
        String[] textList = getStringList(str, delim);
        for (int i = 0; i < textList.length; i++) {
            if (textList[i].indexOf(pattern) >= 0) {
                patternList.add(textList[i]);
            }
        }
        return patternList.size() > 0 ? getArrayListAsStringArray(patternList) : null;
    }

    public static String listToStr(java.util.List s, String delim, String lastDelimeter) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.size(); i++) {
            sb.append(s.get(i));
            if (i == s.size() - 2) {
                sb.append(lastDelimeter);
            } else if (i < s.size() - 2) {
                sb.append(delim);
            }
        }
        return sb.toString();
    }

    public static void addIfNew(ArrayList<String> a, String[] s) {
        for (int i = 0; i < s.length; i++) {
            if (!a.contains(s[i])) {
                a.add(s[i]);
            }
        }
    }

}
