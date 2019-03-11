package com.jtools.javawebdbms;
import java.awt.Point;
import java.io.*;
import java.util.ArrayList;
import java.io.*;
import java.util.Enumeration;
import java.util.ArrayList;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.StringTokenizer;
import java.io.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import javax.xml.parsers.*;
import com.jtools.javawebdatafront.JWebDataWindowLite;
import com.jtools.javawebutils.JWebConstants;
import com.jtools.javawebutils.JWebKeyword;
import com.jtools.javawebutils.JWebUtils;
public class JWebSQLEngine implements Serializable, JWebConstants {
  private String appDir = System.getProperty("user.dir");
  private static final String BRACKET_OPEN = "__START__";
  private static final String BRACKET_CLOSE = "__END__";
  private boolean trace=false;
  private JWebExprFunctions exprFuncs = new JWebExprFunctions();
  private JWebDataWindowLite dwConvert, dwData;


  
  public JWebDataWindowLite getDwConvert() {
		return dwConvert;
	}

	public void setDwConvert(JWebDataWindowLite dwConvert) {
		this.dwConvert = dwConvert;
	}

	public JWebSQLEngine() {
    addCustomFunctions();
  }
	
	public JWebSQLEngine(String appDir) {
    this.appDir = appDir;
    addCustomFunctions();
  }


  


  private String fixName(String oldName) {
    oldName = JWebUtils.globalReplace(oldName, BRACKET_OPEN, "[");
    return JWebUtils.globalReplace(oldName, BRACKET_CLOSE, "]");
  }



  boolean txtfile(String s) {
    File f = new File(s + ".txt");
    if (f.exists()) {
      System.out.println("exists");
      return true;
    }
    return false;
  }




  void CreateTable(String table_name, ArrayList v) throws IOException, SQLException {
  }

  void AlterTableAddCol(String table_name, ArrayList v) throws IOException, SQLException {
  }

  void AlterTableDropCol(String table_name, ArrayList v) throws IOException, SQLException {
  }

  void AlterTableRenameCol(String table_name, String oldColname, String newColname) throws IOException, SQLException {
    throw new SQLException("ALTER TABLE RENAME '" + oldColname + " TO " + newColname + "' is not supported");
  }

  void DropTable(String table_name) throws SQLException {
  }


  public void setSQLParser(InputStream inputstream) {
    parser = null;
    initParser(inputstream);
  }

  public void setSQLParser() {
    parser = null;
  }

  public void initParser(String sql) {
    String valSql = JWebUtils.globalReplace(sql, "[", BRACKET_OPEN);
    valSql = JWebUtils.globalReplace(valSql, "]", BRACKET_CLOSE);
    initParser(new ByteArrayInputStream(valSql.getBytes()));
  }

  public void initParser(InputStream inputstream) {
    if (parser == null)
      parser = new SQLJJParser(inputstream);
    else
      parser.ReInit(inputstream);
  }

  public SQLStatement readStatement() throws ParseException {
    if (parser == null)
      throw new ParseException("Parse is null;");
    else
      return parser.SQLStatement();
  }

  public ArrayList readStatements() throws ParseException {
    if (parser == null)
      throw new ParseException("Parser is null;");
    else
      return parser.SQLStatements();
  }

  public SQLExp readExpression() throws ParseException {
    if (parser == null)
      throw new ParseException("Parser not initialized: use initParser(InputStream);");
    else
      return parser.SQLExpression();
  }
  SQLJJParser parser;

  public class SQLUpdate implements SQLStatement {

    public SQLUpdate(String s) {
      where_ = null;
      columns_ = null;
      table_ = new String(s);
    }

    public String getTable() {
      return table_;
    }

    public void addSet(Hashtable hashtable) {
      set_ = hashtable;
    }

    public Hashtable getSet() {
      return set_;
    }

    public void addColumnUpdate(String s, SQLExp zexp) {
      if (set_ == null)
        set_ = new Hashtable();
      set_.put(s, zexp);
      if (columns_ == null)
        columns_ = new ArrayList();
      columns_.add(s);
    }

    public SQLExp getColumnUpdate(String s) {
      return (SQLExp)set_.get(s);
    }

    public SQLExp getColumnUpdate(int i) {
      if (--i < 0)
        return null;
      if (columns_ == null || i >= columns_.size()) {
        return null;
      } else {
        String s = (String)columns_.get(i);
        return (SQLExp)set_.get(s);
      }
    }

    public String getColumnUpdateName(int i) {
      if (--i < 0)
        return null;
      if (columns_ == null || i >= columns_.size())
        return null;
      else
        return (String)columns_.get(i);
    }

    public int getColumnUpdateCount() {
      if (set_ == null)
        return 0;
      else
        return set_.size();
    }

    public void addWhere(SQLExp zexp) {
      where_ = zexp;
    }

    public SQLExp getWhere() {
      return where_;
    }

    public String toString() {
      StringBuffer stringbuffer = new StringBuffer("update " + table_);
      stringbuffer.append(" set ");
      Iterator enumeration=null;
      if (columns_ != null)
        enumeration = columns_.iterator();
      //else enumeration = set_.keys();
      for (boolean flag = true; enumeration.hasNext(); flag = false) {
        String s = enumeration.next().toString();
        if (!flag)
          stringbuffer.append(", ");
        stringbuffer.append(s + "=" + set_.get(s).toString());
      }
      if (where_ != null)
        stringbuffer.append(" where " + where_.toString());
      return stringbuffer.toString();
    }
    String table_;
    Hashtable set_;
    SQLExp where_;
    ArrayList columns_;
  }

  public class SQLJJParser {

    class JJCalls {
      int gen;
      Token first;
      int arg;
      JJCalls next;

      JJCalls() {
      }
    }

    public void BasicDataTypeDeclaration() throws ParseException {
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 15: // '\017'
        case 27: // '\033'
        case 34: // '"'
        case 44: // ','
        case 48: // '0'
        case 56: // '8'
        case 70: // 'F'
        case 71: // 'G'
          switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
            case 15: // '\017'
              jj_consume_token(15);
              break;
            case 71: // 'G'
              jj_consume_token(71);
              break;
            case 70: // 'F'
              jj_consume_token(70);
              break;
            case 34: // '"'
              jj_consume_token(34);
              break;
            case 48: // '0'
              jj_consume_token(48);
              break;
            case 44: // ','
              jj_consume_token(44);
              break;
            case 56: // '8'
              jj_consume_token(56);
              break;
            case 27: // '\033'
              jj_consume_token(27);
              break;
            default:
              jj_la1[0] = jj_gen;
              jj_consume_token(-1);
              throw new ParseException();
          }
          switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
            case 88: // 'X'
              jj_consume_token(88);
              jj_consume_token(76);
              switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
                case 89: // 'Y'
                  jj_consume_token(89);
                  jj_consume_token(76);
                  break;
                default:
                  jj_la1[1] = jj_gen;
                  break;
              }
              jj_consume_token(90);
              break;
            default:
              jj_la1[2] = jj_gen;
              break;
          }
          break;
        case 20: // '\024'
          jj_consume_token(20);
          break;
        case 12: // '\f'
          jj_consume_token(12);
          break;
        case 13: // '\r'
          jj_consume_token(13);
          break;
        default:
          jj_la1[3] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
      }
    }

    public ArrayList SQLStatements() throws ParseException {
      ArrayList aList = new ArrayList();
      label0:
      do {
        SQLStatement zstatement = SQLStatement();
        if (zstatement == null)
          return aList;
        aList.add(zstatement);
        switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
          case 17: // '\021'
          case 21: // '\025'
          case 26: // '\032'
          case 33: // '!'
          case 39: // '\''
          case 54: // '6'
          case 57: // '9'
          case 59: // ';'
          case 60: // '<'
          case 68: // 'D'
            break;
          default:
            jj_la1[4] = jj_gen;
            break label0;
        }
      } while (true);
      return aList;
    }

    public SQLStatement SQLStatement() throws ParseException {
      Object obj = null;
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 17: // '\021'
          SQLTransactStmt ztransactstmt = CommitStatement();
          return ztransactstmt;
        case 21: // '\025'
          SQLDelete zdelete = DeleteStatement();
          return zdelete;
        case 33: // '!'
          SQLInsert zinsert = InsertStatement();
          return zinsert;
        case 39: // '\''
          SQLLockTable zlocktable = LockTableStatement();
          return zlocktable;
        case 57: // '9'
          SQLTransactStmt ztransactstmt1 = RollbackStatement();
          return ztransactstmt1;
        case 59: // ';'
          SQLQuery zquery = QueryStatement();
          return zquery;
        case 60: // '<'
          SQLTransactStmt ztransactstmt2 = SetTransactionStatement();
          return ztransactstmt2;
        case 68: // 'D'
          SQLUpdate zupdate = UpdateStatement();
          return zupdate;
        case 26: // '\032'
        case 54: // '6'
          switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
            case 26: // '\032'
              jj_consume_token(26);
              break;
            case 54: // '6'
              jj_consume_token(54);
              break;
            default:
              jj_la1[5] = jj_gen;
              jj_consume_token(-1);
              throw new ParseException();
          }
          jj_consume_token(91);
          return null;
      }
      jj_la1[6] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }

    public SQLTransactStmt CommitStatement() throws ParseException {
      SQLTransactStmt ztransactstmt = new SQLTransactStmt("COMMIT");
      jj_consume_token(17);
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 74: // 'J'
          jj_consume_token(74);
          break;
        default:
          jj_la1[7] = jj_gen;
          break;
      }
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 16: // '\020'
          jj_consume_token(16);
          Token token1 = jj_consume_token(86);
          ztransactstmt.setComment(token1.toString());
          break;
        default:
          jj_la1[8] = jj_gen;
          break;
      }
      jj_consume_token(91);
      return ztransactstmt;
    }

    public SQLLockTable LockTableStatement() throws ParseException {
      SQLLockTable zlocktable = new SQLLockTable();
      ArrayList aList = new ArrayList();
      jj_consume_token(39);
      jj_consume_token(65);
      String s = TableReference();
      aList.add(s);
      label0:
      do
        switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
          default:
            jj_la1[9] = jj_gen;
            break label0;
          case 89: // 'Y'
            jj_consume_token(89);
            s = TableReference();
            aList.add(s);
            break;
        }
      while (true);
      jj_consume_token(32);
      s = LockMode();
      zlocktable.setLockMode(s);
      jj_consume_token(43);
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 46: // '.'
          jj_consume_token(46);
          zlocktable.nowait_ = true;
          break;
        default:
          jj_la1[10] = jj_gen;
          break;
      }
      jj_consume_token(91);
      zlocktable.addTables(aList);
      return zlocktable;
    }

    public SQLTransactStmt RollbackStatement() throws ParseException {
      SQLTransactStmt ztransactstmt = new SQLTransactStmt("ROLLBACK");
      jj_consume_token(57);
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 74: // 'J'
          jj_consume_token(74);
          break;
        default:
          jj_la1[11] = jj_gen;
          break;
      }
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 16: // '\020'
          jj_consume_token(16);
          Token token1 = jj_consume_token(86);
          ztransactstmt.setComment(token1.toString());
          break;
        default:
          jj_la1[12] = jj_gen;
          break;
      }
      jj_consume_token(91);
      return ztransactstmt;
    }

    public SQLTransactStmt SetTransactionStatement() throws ParseException {
      SQLTransactStmt ztransactstmt = new SQLTransactStmt("SET TRANSACTION");
      boolean flag = false;
      jj_consume_token(60);
      jj_consume_token(66);
      jj_consume_token(55);
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 50: // '2'
          jj_consume_token(50);
          flag = true;
          break;
        case 75: // 'K'
          jj_consume_token(75);
          break;
        default:
          jj_la1[13] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
      }
      jj_consume_token(91);
      ztransactstmt.readOnly_ = flag;
      return ztransactstmt;
    }

    public String LockMode() throws ParseException {
      StringBuffer stringbuffer = new StringBuffer();
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 58: // ':'
          jj_consume_token(58);
          stringbuffer.append("ROW ");
          switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
            case 61: // '='
              jj_consume_token(61);
              stringbuffer.append("SHARE");
              break;
            case 24: // '\030'
              jj_consume_token(24);
              stringbuffer.append("EXCLUSIVE");
              break;
            default:
              jj_la1[14] = jj_gen;
              jj_consume_token(-1);
              throw new ParseException();
          }
          return stringbuffer.toString();
        case 61: // '='
          jj_consume_token(61);
          stringbuffer.append("SHARE");
          switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
            case 58: // ':'
            case 68: // 'D'
              switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
                case 68: // 'D'
                  jj_consume_token(68);
                  stringbuffer.append(" UPDATE");
                  break;
                case 58: // ':'
                  jj_consume_token(58);
                  jj_consume_token(24);
                  stringbuffer.append(" ROW EXCLUSIVE");
                  break;
                default:
                  jj_la1[15] = jj_gen;
                  jj_consume_token(-1);
                  throw new ParseException();
              }
              break;
            default:
              jj_la1[16] = jj_gen;
              break;
          }
          return stringbuffer.toString();
        case 24: // '\030'
          jj_consume_token(24);
          return new String("EXCLUSIVE");
      }
      jj_la1[17] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }

    public SQLUpdate UpdateStatement() throws ParseException {
      jj_consume_token(68);
      String s = TableReference();
      SQLUpdate zupdate = new SQLUpdate(s);
      jj_consume_token(60);
      ColumnValues(zupdate);
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 72: // 'H'
          jj_consume_token(72);
          SQLExp zexp = SQLExpression();
          zupdate.addWhere(zexp);
          break;
        default:
          jj_la1[18] = jj_gen;
          break;
      }
      jj_consume_token(91);
      return zupdate;
    }

    public void ColumnValues(SQLUpdate zupdate) throws ParseException {
      String s = TableColumn();
      jj_consume_token(92);
      SQLExp zexp = UpdatedValue();
      zupdate.addColumnUpdate(s, zexp);
      label0:
      do
        switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
          default:
            jj_la1[19] = jj_gen;
            break label0;
          case 89: // 'Y'
            jj_consume_token(89);
            String s1 = TableColumn();
            jj_consume_token(92);
            SQLExp zexp1 = UpdatedValue();
            zupdate.addColumnUpdate(s1, zexp1);
            break;
        }
      while (true);
    }

    public SQLExp UpdatedValue() throws ParseException {
      if (jj_2_1(0x7fffffff)) {
        jj_consume_token(88);
        SQLQuery zquery = SelectStatement();
        jj_consume_token(90);
        return zquery;
      }
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 10: // '\n'
        case 19: // '\023'
        case 25: // '\031'
        case 40: // '('
        case 41: // ')'
        case 45: // '-'
        case 47: // '/'
        case 53: // '5'
        case 64: // '@'
        case 76: // 'L'
        case 82: // 'R'
        case 85: // 'U'
        case 86: // 'V'
        case 87: // 'W'
        case 88: // 'X'
        case 101: // 'e'
        case 102: // 'f'
          SQLExp zexp = SQLExpression();
          return zexp;
        case 105: // 'i'
          SQLExp zexp1 = PreparedCol();
          return zexp1;
      }
      jj_la1[20] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }

    public SQLInsert InsertStatement() throws ParseException {
      jj_consume_token(33);
      jj_consume_token(36);
      String s = TableReference();
      SQLInsert zinsert = new SQLInsert(s);
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 88: // 'X'
          jj_consume_token(88);
          String s1 = TableColumn();
          ArrayList aList = new ArrayList();
          aList.add(s1);
          label0:
          do
            switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
              default:
                jj_la1[21] = jj_gen;
                break label0;
              case 89: // 'Y'
                jj_consume_token(89);
                String s2 = TableColumn();
                aList.add(s2);
                break;
            }
          while (true);
          // fhzeya before consume token process where
          // zinsert.addWhere();
          //
          jj_consume_token(90);
          zinsert.addColumns(aList);
          break;
        default:
          jj_la1[22] = jj_gen;
          break;
      }
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 69: // 'E'
          jj_consume_token(69);
          jj_consume_token(88);
          ArrayList vector1 = SQLExpressionList();
          jj_consume_token(90);
          SQLExpression zexpression = new SQLExpression(",");
          zexpression.setOperands(vector1);
          zinsert.addValueSpec(zexpression);
          break;
        case 59: // ';'
          SQLQuery zquery = SelectStatement();
          zinsert.addValueSpec(zquery);
          break;
        default:
          jj_la1[23] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
      }
      // process where
      int i = 1;
      ArrayList wh = new ArrayList(20);
      Token t = getNextTokenFromManager(); // where
      while (t.toString().equals(";") != true) {
        t = getNextTokenFromManager(); // where.
        // System.out.println(t.toString());
        wh.add(t.toString());
      }
      /*
			 * SQLExpression f,sec,exp; ArrayList a=new ArrayList(),b=new
			 * ArrayList(),c=new ArrayList(),d=new ArrayList(); StringBuffer sb=new
			 * StringBuffer(); Token t=getNextTokenFromManager();//where
			 * System.out.println(t.toString()); t=getNextTokenFromManager();
			 * //sheet a.add(0,t); System.out.println(t.toString());
			 * t=getNextTokenFromManager(); //= f=new
			 * SQLExpression(t.toString()); t=getNextTokenFromManager();
			 * //sheetname b.add(0,t); f.addOperand((SQLExp)a.get(0));
			 * f.addOperand((SQLExp)b.get(0));
			 * t=getNextTokenFromManager(); //and t=getNextTokenFromManager();
			 * //cell c.add(t.toString()); t=getNextTokenFromManager();
			 * //= sec=new SQLExpression(t.toString());
			 * t=getNextTokenFromManager(); //cellname
			 * d.add(t.toString()); sec.addOperand((SQLExp)c);
			 * sec.addOperand((SQLExp)d); exp=new SQLExpression("and",f,sec);
			 */
      // jj_consume_token(91);
      zinsert.addWhere(wh);
      return zinsert;
    }

    public SQLDelete DeleteStatement() throws ParseException {
      jj_consume_token(21);
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 29: // '\035'
          jj_consume_token(29);
          break;
        default:
          jj_la1[24] = jj_gen;
          break;
      }
      String s = TableReference();
      SQLDelete zdelete = new SQLDelete(s);
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 72: // 'H'
          jj_consume_token(72);
          SQLExp zexp = SQLExpression();
          zdelete.addWhere(zexp);
          break;
        default:
          jj_la1[25] = jj_gen;
          break;
      }
      jj_consume_token(91);
      return zdelete;
    }

    public SQLQuery QueryStatement() throws ParseException {
      SQLQuery zquery = SelectStatement();
      jj_consume_token(91);
      return zquery;
    }

    public String TableColumn() throws ParseException {
      StringBuffer stringbuffer = new StringBuffer();
      String s = OracleObjectName();
      stringbuffer.append(s);
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 93: // ']'
          jj_consume_token(93);
          String s1 = OracleObjectName();
          stringbuffer.append("." + s1);
          switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
            case 93: // ']'
              jj_consume_token(93);
              String s2 = OracleObjectName();
              stringbuffer.append("." + s2);
              break;
            default:
              jj_la1[26] = jj_gen;
              break;
          }
          break;
        default:
          jj_la1[27] = jj_gen;
          break;
      }
      return stringbuffer.toString();
    }

    public String OracleObjectName() throws ParseException {
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 82: // 'R'
          Token token1 = jj_consume_token(82);
          return token1.toString();
        case 87: // 'W'
          Token token2 = jj_consume_token(87);
          return token2.toString();
      }
      jj_la1[28] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }

    public String Relop() throws ParseException {
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 92: // '\\'
          Token token1 = jj_consume_token(92);
          return token1.toString();
        case 94: // '^'
          Token token2 = jj_consume_token(94);
          return token2.toString();
        case 95: // '_'
          Token token3 = jj_consume_token(95);
          return token3.toString();
        case 96: // '`'
          Token token4 = jj_consume_token(96);
          return token4.toString();
        case 97: // 'a'
          Token token5 = jj_consume_token(97);
          return token5.toString();
        case 98: // 'b'
          Token token6 = jj_consume_token(98);
          return token6.toString();
        case 99: // 'c'
          Token token7 = jj_consume_token(99);
          return token7.toString();
        case 100: // 'd'
          Token token8 = jj_consume_token(100);
          return token8.toString();
        case 93: // ']'
        default:
          jj_la1[29] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
      }
    }

    public String TableReference() throws ParseException {
      StringBuffer stringbuffer = new StringBuffer();
      String s = OracleObjectName();
      stringbuffer.append(s);
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 93: // ']'
          jj_consume_token(93);
          String s1 = OracleObjectName();
          stringbuffer.append("." + s1);
          break;
        default:
          jj_la1[30] = jj_gen;
          break;
      }
      return stringbuffer.toString();
    }

    public void NumOrID() throws ParseException {
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 82: // 'R'
          jj_consume_token(82);
          break;
        case 76: // 'L'
        case 101: // 'e'
        case 102: // 'f'
          switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
            case 101: // 'e'
            case 102: // 'f'
              switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
                case 101: // 'e'
                  jj_consume_token(101);
                  break;
                case 102: // 'f'
                  jj_consume_token(102);
                  break;
                default:
                  jj_la1[31] = jj_gen;
                  jj_consume_token(-1);
                  throw new ParseException();
              }
              break;
            default:
              jj_la1[32] = jj_gen;
              break;
          }
          jj_consume_token(76);
          break;
        default:
          jj_la1[33] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
      }
    }

    public SQLQuery SelectStatement() throws ParseException {
      SQLQuery zquery = SelectWithoutOrder();
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 52: // '4'
          ArrayList aList = OrderByClause();
          zquery.addOrderBy(aList);
          break;
        default:
          jj_la1[34] = jj_gen;
          break;
      }
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 28: // '\034'
          ForUpdateClause();
          zquery.forupdate_ = true;
          break;
        default:
          jj_la1[35] = jj_gen;
          break;
      }
      return zquery;
    }

    public SQLQuery SelectWithoutOrder() throws ParseException {
      SQLQuery zquery = new SQLQuery();
      SQLExp zexp = null;
      SQLGroupBy zgroupby = null;
      SQLExpression zexpression = null;
      jj_consume_token(59);
      // fhz
      // Token t=getNextTokenFromManager();
      // System.out.println(t.toString());
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 5: // '\005'
        case 23: // '\027'
          switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
            case 5: // '\005'
              jj_consume_token(5);
              break;
            case 23: // '\027'
              jj_consume_token(23);
              zquery.distinct_ = true;
              break;
            default:
              jj_la1[36] = jj_gen;
              jj_consume_token(-1);
              throw new ParseException();
          }
          break;
        default:
          jj_la1[37] = jj_gen;
          break;
      }
      ArrayList aList = SelectList();
      ArrayList vector1 = FromClause();
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 72: // 'H'
          zexp = WhereClause();
          break;
        default:
          jj_la1[38] = jj_gen;
          break;
      }
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 18: // '\022'
        case 63: // '?'
          ConnectClause();
          break;
        default:
          jj_la1[39] = jj_gen;
          break;
      }
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 30: // '\036'
          zgroupby = GroupByClause();
          break;
        default:
          jj_la1[40] = jj_gen;
          break;
      }
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 35: // '#'
        case 42: // '*'
        case 67: // 'C'
          zexpression = SetClause();
          break;
        default:
          jj_la1[41] = jj_gen;
          break;
      }
      zquery.addSelect(aList);
      zquery.addFrom(vector1);
      zquery.addWhere(zexp);
      zquery.addGroupBy(zgroupby);
      zquery.addSet(zexpression);
      return zquery;
    }

    public ArrayList SelectList() throws ParseException {
      ArrayList aList = new ArrayList(8);
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 103: // 'g'
          jj_consume_token(103);
          aList.add(new SQLSelectItem("*"));
          return aList;
        case 10: // '\n'
        case 19: // '\023'
        case 40: // '('
        case 41: // ')'
        case 47: // '/'
        case 64: // '@'
        case 76: // 'L'
        case 82: // 'R'
        case 85: // 'U'
        case 86: // 'V'
        case 87: // 'W'
        case 88: // 'X'
        case 101: // 'e'
        case 102: // 'f'
          SQLSelectItem zselectitem = SelectItem();
          aList.add(zselectitem);
          label0:
          do
            switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
              default:
                jj_la1[42] = jj_gen;
                break label0;
              case 89: // 'Y'
                jj_consume_token(89);
                SQLSelectItem zselectitem1 = SelectItem();
                aList.add(zselectitem1);
                break;
            }
          while (true);
          return aList;
      }
      jj_la1[43] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }

    public SQLSelectItem SelectItem() throws ParseException {
      if (jj_2_2(0x7fffffff)) {
        String s = SelectStar();
        return new SQLSelectItem(s);
      }
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 10: // '\n'
        case 19: // '\023'
        case 40: // '('
        case 41: // ')'
        case 47: // '/'
        case 64: // '@'
        case 76: // 'L'
        case 82: // 'R'
        case 85: // 'U'
        case 86: // 'V'
        case 87: // 'W'
        case 88: // 'X'
        case 101: // 'e'
        case 102: // 'f'
          SQLExp zexp = SQLSimpleExpression();
          SQLSelectItem zselectitem = new SQLSelectItem(zexp.toString());
          zselectitem.setExpression(zexp);
          switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
            case 8: // '\b'
            case 82: // 'R'
              String s1 = SelectAlias();
              zselectitem.setAlias(s1);
              break;
            default:
              jj_la1[44] = jj_gen;
              break;
          }
          return zselectitem;
      }
      jj_la1[45] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }

    public String SelectAlias() throws ParseException {
      StringBuffer stringbuffer = new StringBuffer();
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 8: // '\b'
          jj_consume_token(8);
          break;
        default:
          jj_la1[46] = jj_gen;
          break;
      }
      label0:
      do {
        Token token1 = jj_consume_token(82);
        stringbuffer.append(token1.toString().trim() + " ");
        switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
          case 82: // 'R'
            break;
          default:
            jj_la1[47] = jj_gen;
            break label0;
        }
      } while (true);
      return stringbuffer.toString().trim();
    }

    public String SelectStar() throws ParseException {
      if (jj_2_3(2)) {
        String s = OracleObjectName();
        jj_consume_token(104);
        return new String(s + ".*");
      }
      if (jj_2_4(4)) {
        String s1 = OracleObjectName();
        jj_consume_token(93);
        String s2 = OracleObjectName();
        jj_consume_token(104);
        return new String(s1 + "." + s2 + ".*");
      } else {
        jj_consume_token(-1);
        throw new ParseException();
      }
    }

    public ArrayList FromClause() throws ParseException {
      ArrayList aList = new ArrayList(8);
      jj_consume_token(29);
      SQLFromItem zfromitem = FromItem();
      aList.add(zfromitem);
      label0:
      do
        switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
          default:
            jj_la1[48] = jj_gen;
            break label0;
          case 89: // 'Y'
            jj_consume_token(89);
            SQLFromItem zfromitem1 = FromItem();
            aList.add(zfromitem1);
            break;
        }
      while (true);
      return aList;
    }

    public SQLFromItem FromItem() throws ParseException {
      String s = TableReference();
      SQLFromItem zfromitem = new SQLFromItem(s);
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 82: // 'R'
          Token token1 = jj_consume_token(82);
          zfromitem.setAlias(token1.toString());
          break;
        default:
          jj_la1[49] = jj_gen;
          break;
      }
      return zfromitem;
    }

    public SQLExp WhereClause() throws ParseException {
      jj_consume_token(72);
      SQLExp zexp = SQLExpression();
      return zexp;
    }

    public void ConnectClause() throws ParseException {
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 63: // '?'
          jj_consume_token(63);
          jj_consume_token(73);
          SQLExpression();
          break;
        default:
          jj_la1[50] = jj_gen;
          break;
      }
      jj_consume_token(18);
      jj_consume_token(14);
      SQLExpression();
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 63: // '?'
          jj_consume_token(63);
          jj_consume_token(73);
          SQLExpression();
          break;
        default:
          jj_la1[51] = jj_gen;
          break;
      }
    }

    public SQLGroupBy GroupByClause() throws ParseException {
      SQLGroupBy zgroupby = null;
      jj_consume_token(30);
      jj_consume_token(14);
      ArrayList aList = SQLExpressionList();
      zgroupby = new SQLGroupBy(aList);
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 31: // '\037'
          jj_consume_token(31);
          SQLExp zexp = SQLExpression();
          zgroupby.setHaving(zexp);
          break;
        default:
          jj_la1[52] = jj_gen;
          break;
      }
      return zgroupby;
    }

    public SQLExpression SetClause() throws ParseException {
      Token token1;
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 67: // 'C'
          token1 = jj_consume_token(67);
          switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
            case 5: // '\005'
              jj_consume_token(5);
              break;
            default:
              jj_la1[53] = jj_gen;
              break;
          }
          break;
        case 35: // '#'
          token1 = jj_consume_token(35);
          break;
        case 42: // '*'
          token1 = jj_consume_token(42);
          break;
        default:
          jj_la1[54] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
      }
      SQLExpression zexpression = new SQLExpression(token1.toString());
      if (jj_2_5(0x7fffffff)) {
        jj_consume_token(88);
        SQLQuery zquery = SelectWithoutOrder();
        zexpression.addOperand(zquery);
        jj_consume_token(90);
      } else {
        switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
          case 59: // ';'
            SQLQuery zquery1 = SelectWithoutOrder();
            zexpression.addOperand(zquery1);
            break;
          default:
            jj_la1[55] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
        }
      }
      return zexpression;
    }

    public ArrayList OrderByClause() throws ParseException {
      ArrayList aList = new ArrayList();
      jj_consume_token(52);
      jj_consume_token(14);
      SQLExp zexp = SQLSimpleExpression();
      SQLOrderBy zorderby = new SQLOrderBy(zexp);
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 9: // '\t'
        case 22: // '\026'
          switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
            case 9: // '\t'
              jj_consume_token(9);
              break;
            case 22: // '\026'
              jj_consume_token(22);
              zorderby.setAscOrder(false);
              break;
            default:
              jj_la1[56] = jj_gen;
              jj_consume_token(-1);
              throw new ParseException();
          }
          break;
        default:
          jj_la1[57] = jj_gen;
          break;
      }
      aList.add(zorderby);
      label0:
      do
        switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
          default:
            jj_la1[58] = jj_gen;
            break label0;
          case 89: // 'Y'
            jj_consume_token(89);
            SQLExp zexp1 = SQLSimpleExpression();
            SQLOrderBy zorderby1 = new SQLOrderBy(zexp1);
            switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
              case 9: // '\t'
              case 22: // '\026'
                switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
                  case 9: // '\t'
                    jj_consume_token(9);
                    break;
                  case 22: // '\026'
                    jj_consume_token(22);
                    zorderby1.setAscOrder(false);
                    break;
                  default:
                    jj_la1[59] = jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
                }
                break;
              default:
                jj_la1[60] = jj_gen;
                break;
            }
            aList.add(zorderby1);
            break;
        }
      while (true);
      return aList;
    }

    public void ForUpdateClause() throws ParseException {
      jj_consume_token(28);
      jj_consume_token(68);
      label0:
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 49: // '1'
          jj_consume_token(49);
          TableColumn();
          do
            switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
              default:
                jj_la1[61] = jj_gen;
                break label0;
              case 89: // 'Y'
                jj_consume_token(89);
                TableColumn();
                break;
            }
          while (true);
        default:
          jj_la1[62] = jj_gen;
          break;
      }
    }

    public SQLExp SQLExpression() throws ParseException {
      SQLExpression zexpression = null;
      boolean flag = true;
      SQLExp zexp = SQLAndExpression();
      label0:
      do
        switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
          default:
            jj_la1[63] = jj_gen;
            break label0;
          case 51: // '3'
            jj_consume_token(51);
            SQLExp zexp1 = SQLAndExpression();
            if (flag)
              zexpression = new SQLExpression("OR", zexp);
            flag = false;
            zexpression.addOperand(zexp1);
            break;
        }
      while (true);
      return ((SQLExp)(flag ? zexp : zexpression));
    }

    public SQLExp SQLAndExpression() throws ParseException {
      SQLExpression zexpression = null;
      boolean flag = true;
      SQLExp zexp = SQLUnaryLogicalExpression();
      label0:
      do
        switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
          default:
            jj_la1[64] = jj_gen;
            break label0;
          case 6: // '\006'
            jj_consume_token(6);
            SQLExp zexp1 = SQLUnaryLogicalExpression();
            if (flag)
              zexpression = new SQLExpression("AND", zexp);
            flag = false;
            zexpression.addOperand(zexp1);
            break;
        }
      while (true);
      return ((SQLExp)(flag ? zexp : zexpression));
    }

    public SQLExp SQLUnaryLogicalExpression() throws ParseException {
      boolean flag = false;
      if (jj_2_6(2)) {
        SQLExpression zexpression = ExistsClause();
        return zexpression;
      }
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 10: // '\n'
        case 19: // '\023'
        case 40: // '('
        case 41: // ')'
        case 45: // '-'
        case 47: // '/'
        case 53: // '5'
        case 64: // '@'
        case 76: // 'L'
        case 82: // 'R'
        case 85: // 'U'
        case 86: // 'V'
        case 87: // 'W'
        case 88: // 'X'
        case 101: // 'e'
        case 102: // 'f'
          switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
            case 45: // '-'
              jj_consume_token(45);
              flag = true;
              break;
            default:
              jj_la1[65] = jj_gen;
              break;
          }
          SQLExp zexp = SQLRelationalExpression();
          Object obj;
          if (flag)
            obj = new SQLExpression("NOT", zexp);
          else
            obj = zexp;
          return ((SQLExp)(obj));
      }
      jj_la1[66] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }

    public SQLExpression ExistsClause() throws ParseException {
      boolean flag = false;
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 45: // '-'
          jj_consume_token(45);
          flag = true;
          break;
        default:
          jj_la1[67] = jj_gen;
          break;
      }
      jj_consume_token(25);
      jj_consume_token(88);
      SQLQuery zquery = SubQuery();
      jj_consume_token(90);
      SQLExpression zexpression1 = new SQLExpression("EXISTS", zquery);
      SQLExpression zexpression;
      if (flag)
        zexpression = new SQLExpression("NOT", zexpression1);
      else
        zexpression = zexpression1;
      return zexpression;
    }

    public SQLExp SQLRelationalExpression() throws ParseException {
      SQLExpression zexpression = null;
      boolean flag = false;
      Object obj;
      if (jj_2_7(0x7fffffff)) {
        jj_consume_token(88);
        ArrayList aList = SQLExpressionList();
        jj_consume_token(90);
        obj = new SQLExpression(",");
        ((SQLExpression)obj).setOperands(aList);
      } else {
        switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
          case 10: // '\n'
          case 19: // '\023'
          case 40: // '('
          case 41: // ')'
          case 47: // '/'
          case 53: // '5'
          case 64: // '@'
          case 76: // 'L'
          case 82: // 'R'
          case 85: // 'U'
          case 86: // 'V'
          case 87: // 'W'
          case 88: // 'X'
          case 101: // 'e'
          case 102: // 'f'
            switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
              case 53: // '5'
                jj_consume_token(53);
                flag = true;
                break;
              default:
                jj_la1[68] = jj_gen;
                break;
            }
            SQLExp zexp = SQLSimpleExpression();
            if (flag)
              obj = new SQLExpression("PRIOR", zexp);
            else
              obj = zexp;
            break;
          default:
            jj_la1[69] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
        }
      }
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 11: // '\013'
        case 32: // ' '
        case 37: // '%'
        case 38: // '&'
        case 45: // '-'
        case 92: // '\\'
        case 94: // '^'
        case 95: // '_'
        case 96: // '`'
        case 97: // 'a'
        case 98: // 'b'
        case 99: // 'c'
        case 100: // 'd'
          switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
            case 92: // '\\'
            case 94: // '^'
            case 95: // '_'
            case 96: // '`'
            case 97: // 'a'
            case 98: // 'b'
            case 99: // 'c'
            case 100: // 'd'
              zexpression = SQLRelationalOperatorExpression();
              break;
            case 93: // ']'
            default:
              jj_la1[70] = jj_gen;
              if (jj_2_8(2))
                zexpression = SQLInClause();
              else if (jj_2_9(2))
                zexpression = SQLBetweenClause();
              else if (jj_2_10(2))
                zexpression = SQLLikeClause();
              else
                switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
                  case 37: // '%'
                    zexpression = IsNullClause();
                    break;
                  default:
                    jj_la1[71] = jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
                }
              break;
          }
          break;
        default:
          jj_la1[72] = jj_gen;
          break;
      }
      if (zexpression == null)
        return ((SQLExp)(obj));
      ArrayList vector1 = zexpression.getOperands();
      if (vector1 == null)
        vector1 = new ArrayList();
      vector1.add(0,obj);
      zexpression.setOperands(vector1);
      return zexpression;
    }

    public ArrayList SQLExpressionList() throws ParseException {
      ArrayList aList = new ArrayList(8);
      SQLExp zexp = SQLSimpleExpressionOrPreparedCol();
      aList.add(zexp);
      label0:
      do
        switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
          default:
            jj_la1[73] = jj_gen;
            break label0;
          case 89: // 'Y'
            jj_consume_token(89);
            SQLExp zexp1 = SQLSimpleExpressionOrPreparedCol();
            aList.add(zexp1);
            break;
        }
      while (true);
      return aList;
    }

    public SQLExpression SQLRelationalOperatorExpression() throws ParseException {
      String s1 = null;
      String s = Relop();
      SQLExpression zexpression = new SQLExpression(s);
      Object obj;
      if (jj_2_11(0x7fffffff)) {
        switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
          case 5: // '\005'
          case 7: // '\007'
            switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
              case 5: // '\005'
                jj_consume_token(5);
                s1 = "ALL";
                break;
              case 7: // '\007'
                jj_consume_token(7);
                s1 = "ANY";
                break;
              default:
                jj_la1[74] = jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
            }
            break;
          default:
            jj_la1[75] = jj_gen;
            break;
        }
        jj_consume_token(88);
        SQLQuery zquery = SubQuery();
        jj_consume_token(90);
        if (s1 == null)
          obj = zquery;
        else
          obj = new SQLExpression(s1, zquery);
      } else {
        switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
          case 10: // '\n'
          case 19: // '\023'
          case 40: // '('
          case 41: // ')'
          case 47: // '/'
          case 53: // '5'
          case 64: // '@'
          case 76: // 'L'
          case 82: // 'R'
          case 85: // 'U'
          case 86: // 'V'
          case 87: // 'W'
          case 88: // 'X'
          case 101: // 'e'
          case 102: // 'f'
          case 105: // 'i'
            switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
              case 53: // '5'
                jj_consume_token(53);
                s1 = "PRIOR";
                break;
              default:
                jj_la1[76] = jj_gen;
                break;
            }
            SQLExp zexp = SQLSimpleExpressionOrPreparedCol();
            if (s1 == null)
              obj = zexp;
            else
              obj = new SQLExpression(s1, zexp);
            break;
          default:
            jj_la1[77] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
        }
      }
      zexpression.addOperand(((SQLExp)(obj)));
      return zexpression;
    }

    public SQLExp SQLSimpleExpressionOrPreparedCol() throws ParseException {
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 10: // '\n'
        case 19: // '\023'
        case 40: // '('
        case 41: // ')'
        case 47: // '/'
        case 64: // '@'
        case 76: // 'L'
        case 82: // 'R'
        case 85: // 'U'
        case 86: // 'V'
        case 87: // 'W'
        case 88: // 'X'
        case 101: // 'e'
        case 102: // 'f'
          SQLExp zexp = SQLSimpleExpression();
          return zexp;
        case 105: // 'i'
          SQLExp zexp1 = PreparedCol();
          return zexp1;
      }
      jj_la1[78] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }

    public SQLExp PreparedCol() throws ParseException {
      jj_consume_token(105);
      return new SQLExpression("?");
    }

    public SQLExpression SQLInClause() throws ParseException {
      boolean flag = false;
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 45: // '-'
          jj_consume_token(45);
          flag = true;
          break;
        default:
          jj_la1[79] = jj_gen;
          break;
      }
      jj_consume_token(32);
      SQLExpression zexpression = new SQLExpression(flag ? "NOT IN" : "IN");
      jj_consume_token(88);
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 10: // '\n'
        case 19: // '\023'
        case 40: // '('
        case 41: // ')'
        case 47: // '/'
        case 64: // '@'
        case 76: // 'L'
        case 82: // 'R'
        case 85: // 'U'
        case 86: // 'V'
        case 87: // 'W'
        case 88: // 'X'
        case 101: // 'e'
        case 102: // 'f'
        case 105: // 'i'
          ArrayList aList = SQLExpressionList();
          zexpression.setOperands(aList);
          break;
        case 59: // ';'
          SQLQuery zquery = SubQuery();
          zexpression.addOperand(zquery);
          break;
        default:
          jj_la1[80] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
      }
      jj_consume_token(90);
      return zexpression;
    }

    public SQLExpression substringClause() throws ParseException {
      boolean flag = false;
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 45: // '-'
          jj_consume_token(45);
          flag = true;
          break;
        default:
          jj_la1[79] = jj_gen;
          break;
      }
      jj_consume_token(32);
      SQLExpression zexpression = new SQLExpression(flag ? "NOT IN" : "IN");
      jj_consume_token(88);
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 10: // '\n'
        case 19: // '\023'
        case 40: // '('
        case 41: // ')'
        case 47: // '/'
        case 64: // '@'
        case 76: // 'L'
        case 82: // 'R'
        case 85: // 'U'
        case 86: // 'V'
        case 87: // 'W'
        case 88: // 'X'
        case 101: // 'e'
        case 102: // 'f'
        case 105: // 'i'
          ArrayList aList = SQLExpressionList();
          zexpression.setOperands(aList);
          break;
        case 59: // ';'
          SQLQuery zquery = SubQuery();
          zexpression.addOperand(zquery);
          break;
        default:
          jj_la1[80] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
      }
      jj_consume_token(90);
      return zexpression;
    }

    public SQLExpression SQLBetweenClause() throws ParseException {
      boolean flag = false;
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 45: // '-'
          jj_consume_token(45);
          flag = true;
          break;
        default:
          jj_la1[81] = jj_gen;
          break;
      }
      jj_consume_token(11);
      SQLExp zexp = SQLSimpleExpression();
      jj_consume_token(6);
      SQLExp zexp1 = SQLSimpleExpression();
      SQLExpression zexpression;
      if (flag)
        zexpression = new SQLExpression("NOT BETWEEN", zexp, zexp1);
      else
        zexpression = new SQLExpression("BETWEEN", zexp, zexp1);
      return zexpression;
    }

    public SQLExpression SQLLikeClause() throws ParseException {
      boolean flag = false;
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 45: // '-'
          jj_consume_token(45);
          flag = true;
          break;
        default:
          jj_la1[82] = jj_gen;
          break;
      }
      jj_consume_token(38);
      SQLExp zexp = SQLSimpleExpression();
      SQLExpression zexpression;
      if (flag)
        zexpression = new SQLExpression("NOT LIKE", zexp);
      else
        zexpression = new SQLExpression("LIKE", zexp);
      return zexpression;
    }

    public SQLExpression IsNullClause() throws ParseException {
      boolean flag = false;
      jj_consume_token(37);
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 45: // '-'
          jj_consume_token(45);
          flag = true;
          break;
        default:
          jj_la1[83] = jj_gen;
          break;
      }
      jj_consume_token(47);
      return flag ? new SQLExpression("IS NOT NULL") : new SQLExpression("IS NULL");
    }

    public SQLExp SQLSimpleExpression() throws ParseException {
      Object obj1 = null;
      Object obj = SQLMultiplicativeExpression();
      label0:
      do
        switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
          default:
            jj_la1[84] = jj_gen;
            break label0;
          case 101: // 'e'
          case 102: // 'f'
          case 106: // 'j'
            Token token1;
            switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
              case 101: // 'e'
                token1 = jj_consume_token(101);
                break;
              case 102: // 'f'
                token1 = jj_consume_token(102);
                break;
              case 106: // 'j'
                token1 = jj_consume_token(106);
                break;
              default:
                jj_la1[85] = jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
            }
            SQLExp zexp = SQLMultiplicativeExpression();
            SQLExpression zexpression = new SQLExpression(token1.toString(), ((SQLExp)(obj)));
            zexpression.addOperand(zexp);
            obj = zexpression;
            break;
        }
      while (true);
      return ((SQLExp)(obj));
    }

    public SQLExp SQLMultiplicativeExpression() throws ParseException {
      Object obj1 = null;
      Object obj = SQLExpotentExpression();
      label0:
      do
        switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
          default:
            jj_la1[86] = jj_gen;
            break label0;
          case 103: // 'g'
          case 107: // 'k'
            Token token1;
            switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
              case 103: // 'g'
                token1 = jj_consume_token(103);
                break;
              case 107: // 'k'
                token1 = jj_consume_token(107);
                break;
              default:
                jj_la1[87] = jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
            }
            SQLExp zexp = SQLExpotentExpression();
            SQLExpression zexpression = new SQLExpression(token1.toString(), ((SQLExp)(obj)));
            zexpression.addOperand(zexp);
            obj = zexpression;
            break;
        }
      while (true);
      return ((SQLExp)(obj));
    }

    public SQLExp SQLExpotentExpression() throws ParseException {
      SQLExpression zexpression = null;
      boolean flag = true;
      SQLExp zexp = SQLUnaryExpression();
      label0:
      do
        switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
          default:
            jj_la1[88] = jj_gen;
            break label0;
          case 108: // 'l'
            Token token1 = jj_consume_token(108);
            SQLExp zexp1 = SQLUnaryExpression();
            if (flag)
              zexpression = new SQLExpression(token1.toString(), zexp);
            flag = false;
            zexpression.addOperand(zexp1);
            break;
        }
      while (true);
      return ((SQLExp)(flag ? zexp : zexpression));
    }

    public SQLExp SQLUnaryExpression() throws ParseException {
      Token token1 = null;
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 101: // 'e'
        case 102: // 'f'
          switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
            case 101: // 'e'
              token1 = jj_consume_token(101);
              break;
            case 102: // 'f'
              token1 = jj_consume_token(102);
              break;
            default:
              jj_la1[89] = jj_gen;
              jj_consume_token(-1);
              throw new ParseException();
          }
          break;
        default:
          jj_la1[90] = jj_gen;
          break;
      }
      SQLExp zexp = SQLPrimaryExpression();
      Object obj;
      if (token1 == null)
        obj = zexp;
      else
        obj = new SQLExpression(token1.toString(), zexp);
      return ((SQLExp)(obj));
    }

    public SQLExp SQLPrimaryExpression() throws ParseException {
      String s4 = "";
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 47: // '/'
          jj_consume_token(47);
          return new SQLConstant("NULL", 1);
      }
      jj_la1[93] = jj_gen;
      if (jj_2_12(0x7fffffff)) {
        OuterJoinExpression();
        return new SQLExpression("_NOT_SUPPORTED");
      }
      if (jj_2_13(3)) {
        jj_consume_token(19);
        jj_consume_token(88);
        jj_consume_token(103);
        jj_consume_token(90);
        return new SQLExpression("COUNT", new SQLConstant("*", 0));
      }
      if (jj_2_14(2)) {
        String s = AggregateFunc();
        jj_consume_token(88);
        switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
          case 5: // '\005'
          case 23: // '\027'
            switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
              case 5: // '\005'
                jj_consume_token(5);
                s4 = "all ";
                break;
              case 23: // '\027'
                jj_consume_token(23);
                s4 = "distinct ";
                break;
              default:
                jj_la1[91] = jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
            }
            break;
          default:
            jj_la1[92] = jj_gen;
            break;
        }
        String s3 = TableColumn();
        jj_consume_token(90);
        return new SQLExpression(s, new SQLConstant(s4 + s3, 0));
      }
      if (jj_2_15(0x7fffffff)) {
        SQLExpression zexpression = FunctionCall();
        return zexpression;
      }
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 82: // 'R'
        case 87: // 'W'
          String s1 = TableColumn();
          return new SQLConstant(s1, 0);
        case 76: // 'L'
          Token token1 = jj_consume_token(76);
          return new SQLConstant(token1.toString(), 2);
        case 86: // 'V'
          Token token2 = jj_consume_token(86);
          String s2 = token2.toString();
          if (s2.startsWith("'"))
            s2 = s2.substring(1);
          if (s2.endsWith("'"))
            s2 = s2.substring(0, s2.length() - 1);
          return new SQLConstant(s2, 3);
        case 85: // 'U'
          Token token3 = jj_consume_token(85);
          return new SQLConstant(token3.toString(), 3);
        case 88: // 'X'
          jj_consume_token(88);
          SQLExp zexp = SQLExpression();
          jj_consume_token(90);
          return zexp;
        case 77: // 'M'
        case 78: // 'N'
        case 79: // 'O'
        case 80: // 'P'
        case 81: // 'Q'
        case 83: // 'S'
        case 84: // 'T'
        default:
          jj_la1[94] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
      }
    }

    public String AggregateFunc() throws ParseException {
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 64: // '@'
          Token token1 = jj_consume_token(64);
          return token1.toString();
        case 10: // '\n'
          Token token2 = jj_consume_token(10);
          return token2.toString();
        case 40: // '('
          Token token3 = jj_consume_token(40);
          return token3.toString();
        case 41: // ')'
          Token token4 = jj_consume_token(41);
          return token4.toString();
        case 19: // '\023'
          Token token5 = jj_consume_token(19);
          return token5.toString();
      }
      jj_la1[95] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }

    public SQLExpression FunctionCall() throws ParseException {
      Token token1 = jj_consume_token(82);
      jj_consume_token(88);
      ArrayList aList = SQLExpressionList();
      jj_consume_token(90);
      int i = isCustomFunction(token1.toString());
      if (i <= 0)
        throw new ParseException("Undefined function: " + token1.toString());
      if (i < 99 && aList.size() != i) {
        throw new ParseException("Function " + token1.toString() + " should have " + i + " parameter(s)");
      } else {
        SQLExpression zexpression = new SQLExpression(token1.toString());
        zexpression.setOperands(aList);
        return zexpression;
      }
    }

    public void OuterJoinExpression() throws ParseException {
      OracleObjectName();
      switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
        case 93: // ']'
          jj_consume_token(93);
          OracleObjectName();
          switch (jj_ntk != -1 ? jj_ntk : jj_ntk()) {
            case 93: // ']'
              jj_consume_token(93);
              OracleObjectName();
              break;
            default:
              jj_la1[96] = jj_gen;
              break;
          }
          break;
        default:
          jj_la1[97] = jj_gen;
          break;
      }
      jj_consume_token(88);
      jj_consume_token(101);
      jj_consume_token(90);
    }

    public SQLQuery SubQuery() throws ParseException {
      SQLQuery zquery = SelectWithoutOrder();
      return zquery;
    }

    private boolean jj_2_1(int i) throws ParseException{
      jj_la = i;
      jj_lastpos = jj_scanpos = token;
      boolean flag = !jj_3_1();
      jj_save(0, i);
      return flag;
    }

    private boolean jj_2_2(int i) throws ParseException{
      jj_la = i;
      jj_lastpos = jj_scanpos = token;
      boolean flag = !jj_3_2();
      jj_save(1, i);
      return flag;
    }

    private boolean jj_2_3(int i) throws ParseException{
      jj_la = i;
      jj_lastpos = jj_scanpos = token;
      boolean flag = !jj_3_3();
      jj_save(2, i);
      return flag;
    }

    private boolean jj_2_4(int i) throws ParseException{
      jj_la = i;
      jj_lastpos = jj_scanpos = token;
      boolean flag = !jj_3_4();
      jj_save(3, i);
      return flag;
    }

    private boolean jj_2_5(int i) throws ParseException{
      jj_la = i;
      jj_lastpos = jj_scanpos = token;
      boolean flag = !jj_3_5();
      jj_save(4, i);
      return flag;
    }

    private boolean jj_2_6(int i) throws ParseException{
      jj_la = i;
      jj_lastpos = jj_scanpos = token;
      boolean flag = !jj_3_6();
      jj_save(5, i);
      return flag;
    }

    private boolean jj_2_7(int i) throws ParseException{
      jj_la = i;
      jj_lastpos = jj_scanpos = token;
      boolean flag = !jj_3_7();
      jj_save(6, i);
      return flag;
    }

    private boolean jj_2_8(int i) throws ParseException{
      jj_la = i;
      jj_lastpos = jj_scanpos = token;
      boolean flag = !jj_3_8();
      jj_save(7, i);
      return flag;
    }

    private boolean jj_2_9(int i) throws ParseException{
      jj_la = i;
      jj_lastpos = jj_scanpos = token;
      boolean flag = !jj_3_9();
      jj_save(8, i);
      return flag;
    }

    private boolean jj_2_10(int i) throws ParseException{
      jj_la = i;
      jj_lastpos = jj_scanpos = token;
      boolean flag = !jj_3_10();
      jj_save(9, i);
      return flag;
    }

    private boolean jj_2_11(int i) throws ParseException{
      jj_la = i;
      jj_lastpos = jj_scanpos = token;
      boolean flag = !jj_3_11();
      jj_save(10, i);
      return flag;
    }

    private boolean jj_2_12(int i) throws ParseException{
      jj_la = i;
      jj_lastpos = jj_scanpos = token;
      boolean flag = !jj_3_12();
      jj_save(11, i);
      return flag;
    }

    private boolean jj_2_13(int i) throws ParseException{
      jj_la = i;
      jj_lastpos = jj_scanpos = token;
      boolean flag = !jj_3_13();
      jj_save(12, i);
      return flag;
    }

    private boolean jj_2_14(int i) throws ParseException{
      jj_la = i;
      jj_lastpos = jj_scanpos = token;
      boolean flag = !jj_3_14();
      jj_save(13, i);
      return flag;
    }

    private boolean jj_2_15(int i) throws ParseException{
      jj_la = i;
      jj_lastpos = jj_scanpos = token;
      boolean flag = !jj_3_15();
      jj_save(14, i);
      return flag;
    }

    private boolean jj_3_7() throws ParseException{
      if (jj_scan_token(88))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_20())
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_scan_token(89))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_91() throws ParseException{
      if (jj_scan_token(53))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_88() throws ParseException{
      Token token1 = jj_scanpos;
      if (jj_3R_91())
        jj_scanpos = token1;
      else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_20())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_87() throws ParseException {
      if (jj_scan_token(88))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_72())
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_scan_token(90))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_85() throws ParseException {
      Token token1 = jj_scanpos;
      if (jj_3R_87()) {
        jj_scanpos = token1;
        if (jj_3R_88())
          return true;
        if (jj_la == 0 && jj_scanpos == jj_lastpos)
          return false;
      } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      token1 = jj_scanpos;
      if (jj_3R_89())
        jj_scanpos = token1;
      else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      return false;
    }

    private boolean jj_3R_16() throws ParseException {
      if (jj_scan_token(88))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3_1() throws ParseException{
      if (jj_3R_16())
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      do {
        Token token1 = jj_scanpos;
        if (jj_3R_16()) {
          jj_scanpos = token1;
          break;
        }
        if (jj_la == 0 && jj_scanpos == jj_lastpos)
          return false;
      } while (true);
      if (jj_scan_token(59))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_31() throws ParseException{
      if (jj_scan_token(45))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_19() throws ParseException{
      Token token1 = jj_scanpos;
      if (jj_3R_31())
        jj_scanpos = token1;
      else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_scan_token(25))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_scan_token(88))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_86())
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_scan_token(90))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_84() throws ParseException{
      if (jj_scan_token(45))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_82() throws ParseException{
      Token token1 = jj_scanpos;
      if (jj_3R_84())
        jj_scanpos = token1;
      else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_85())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3_6() throws ParseException{
      if (jj_3R_19())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_78() throws ParseException{
      Token token1 = jj_scanpos;
      if (jj_3_6()) {
        jj_scanpos = token1;
        if (jj_3R_82())
          return true;
        if (jj_la == 0 && jj_scanpos == jj_lastpos)
          return false;
      } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      return false;
    }

    private boolean jj_3R_48() throws ParseException{
      if (jj_scan_token(93))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_18())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_79() throws ParseException{
      if (jj_scan_token(6))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_78())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_73() throws ParseException{
      if (jj_3R_78())
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      do {
        Token token1 = jj_scanpos;
        if (jj_3R_79()) {
          jj_scanpos = token1;
          break;
        }
        if (jj_la == 0 && jj_scanpos == jj_lastpos)
          return false;
      } while (true);
      return false;
    }

    private boolean jj_3R_128() throws ParseException{
      if (jj_scan_token(42))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_37() throws ParseException{
      if (jj_scan_token(93))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_18())
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      Token token1 = jj_scanpos;
      if (jj_3R_48())
        jj_scanpos = token1;
      else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      return false;
    }

    private boolean jj_3R_74() throws ParseException{
      if (jj_scan_token(51))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_73())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_68() throws ParseException{
      if (jj_3R_73())
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      do {
        Token token1 = jj_scanpos;
        if (jj_3R_74()) {
          jj_scanpos = token1;
          break;
        }
        if (jj_la == 0 && jj_scanpos == jj_lastpos)
          return false;
      } while (true);
      return false;
    }

    private boolean jj_3R_86() throws ParseException{
      if (jj_3R_90())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_127() throws ParseException{
      if (jj_scan_token(35))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_27() throws ParseException{
      if (jj_3R_18())
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      Token token1 = jj_scanpos;
      if (jj_3R_37())
        jj_scanpos = token1;
      else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_scan_token(88))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_scan_token(101))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_scan_token(90))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_144() throws ParseException{
      if (jj_scan_token(5))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3_5() throws ParseException{
      if (jj_scan_token(88))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_130() throws ParseException{
      if (jj_3R_90())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_129() throws ParseException{
      if (jj_scan_token(88))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_90())
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_scan_token(90))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_67() throws ParseException{
      if (jj_scan_token(82))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_scan_token(88))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_72())
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_scan_token(90))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_126() throws ParseException{
      if (jj_scan_token(67))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      Token token1 = jj_scanpos;
      if (jj_3R_144())
        jj_scanpos = token1;
      else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      return false;
    }

    private boolean jj_3R_112() throws ParseException{
      Token token1 = jj_scanpos;
      if (jj_3R_126()) {
        jj_scanpos = token1;
        if (jj_3R_127()) {
          jj_scanpos = token1;
          if (jj_3R_128())
            return true;
          if (jj_la == 0 && jj_scanpos == jj_lastpos)
            return false;
        } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
          return false;
      } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      token1 = jj_scanpos;
      if (jj_3R_129()) {
        jj_scanpos = token1;
        if (jj_3R_130())
          return true;
        if (jj_la == 0 && jj_scanpos == jj_lastpos)
          return false;
      } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      return false;
    }

    private boolean jj_3R_70() throws ParseException{
      if (jj_scan_token(23))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_42() throws ParseException{
      if (jj_scan_token(19))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_41() throws ParseException{
      if (jj_scan_token(41))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_40() throws ParseException{
      if (jj_scan_token(40))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_39() throws ParseException {
      if (jj_scan_token(10))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_125() throws ParseException {
      if (jj_scan_token(31))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_68())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_38() throws ParseException {
      if (jj_scan_token(64))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_28() throws ParseException {
      Token token1 = jj_scanpos;
      if (jj_3R_38()) {
        jj_scanpos = token1;
        if (jj_3R_39()) {
          jj_scanpos = token1;
          if (jj_3R_40()) {
            jj_scanpos = token1;
            if (jj_3R_41()) {
              jj_scanpos = token1;
              if (jj_3R_42())
                return true;
              if (jj_la == 0 && jj_scanpos == jj_lastpos)
                return false;
            } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
              return false;
          } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
            return false;
        } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
          return false;
      } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      return false;
    }

    private boolean jj_3R_111()  throws ParseException {
      if (jj_scan_token(30))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_scan_token(14))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_72())
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      Token token1 = jj_scanpos;
      if (jj_3R_125())
        jj_scanpos = token1;
      else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      return false;
    }

    private boolean jj_3_15() throws ParseException {
      if (jj_scan_token(82))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_scan_token(88))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_124() throws ParseException {
      if (jj_scan_token(63))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_scan_token(73))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_68())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_64() throws ParseException {
      if (jj_scan_token(88))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_68())
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_scan_token(90))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_123() throws ParseException {
      if (jj_scan_token(63))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_scan_token(73))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_68())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_63() throws ParseException {
      if (jj_scan_token(85))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_110() throws ParseException {
      Token token1 = jj_scanpos;
      if (jj_3R_123())
        jj_scanpos = token1;
      else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_scan_token(18))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_scan_token(14))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_68())
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      token1 = jj_scanpos;
      if (jj_3R_124())
        jj_scanpos = token1;
      else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      return false;
    }

    private boolean jj_3R_62() throws ParseException {
      if (jj_scan_token(86))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_61() throws ParseException {
      if (jj_scan_token(76))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_60() throws ParseException {
      if (jj_3R_66())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_69() throws ParseException {
      if (jj_scan_token(5))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_65() throws ParseException {
      Token token1 = jj_scanpos;
      if (jj_3R_69()) {
        jj_scanpos = token1;
        if (jj_3R_70())
          return true;
        if (jj_la == 0 && jj_scanpos == jj_lastpos)
          return false;
      } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      return false;
    }

    private boolean jj_3R_59() throws ParseException {
      if (jj_3R_67())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3_12() throws ParseException {
      if (jj_3R_27())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_109() throws ParseException {
      if (jj_scan_token(72))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_68())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3_14() throws ParseException {
      if (jj_3R_28())
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_scan_token(88))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      Token token1 = jj_scanpos;
      if (jj_3R_65())
        jj_scanpos = token1;
      else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_66())
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_scan_token(90))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3_13() throws ParseException {
      if (jj_scan_token(19))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_scan_token(88))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_scan_token(103))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_scan_token(90))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_58() throws ParseException {
      if (jj_3R_27())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_122() throws ParseException {
      if (jj_scan_token(82))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_57() throws ParseException {
      if (jj_scan_token(47))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_54() throws ParseException {
      Token token1 = jj_scanpos;
      if (jj_3R_57()) {
        jj_scanpos = token1;
        if (jj_3R_58()) {
          jj_scanpos = token1;
          if (jj_3_13()) {
            jj_scanpos = token1;
            if (jj_3_14()) {
              jj_scanpos = token1;
              if (jj_3R_59()) {
                jj_scanpos = token1;
                if (jj_3R_60()) {
                  jj_scanpos = token1;
                  if (jj_3R_61()) {
                    jj_scanpos = token1;
                    if (jj_3R_62()) {
                      jj_scanpos = token1;
                      if (jj_3R_63()) {
                        jj_scanpos = token1;
                        if (jj_3R_64())
                          return true;
                        if (jj_la == 0 && jj_scanpos == jj_lastpos)
                          return false;
                      } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
                        return false;
                    } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
                      return false;
                  } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
                    return false;
                } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
                  return false;
              } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
                return false;
            } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
              return false;
          } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
            return false;
        } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
          return false;
      } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      return false;
    }

    private boolean jj_3R_107() throws ParseException {
      if (jj_3R_121())
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      Token token1 = jj_scanpos;
      if (jj_3R_122())
        jj_scanpos = token1;
      else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      return false;
    }

    private boolean jj_3R_108() throws ParseException {
      if (jj_scan_token(89))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_107())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_56() throws ParseException {
      if (jj_scan_token(102))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_96() throws ParseException {
      if (jj_scan_token(29))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_107())
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      do {
        Token token1 = jj_scanpos;
        if (jj_3R_108()) {
          jj_scanpos = token1;
          break;
        }
        if (jj_la == 0 && jj_scanpos == jj_lastpos)
          return false;
      } while (true);
      return false;
    }

    private boolean jj_3R_55() throws ParseException {
      if (jj_scan_token(101))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_53() throws ParseException {
      Token token1 = jj_scanpos;
      if (jj_3R_55()) {
        jj_scanpos = token1;
        if (jj_3R_56())
          return true;
        if (jj_la == 0 && jj_scanpos == jj_lastpos)
          return false;
      } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      return false;
    }

    private boolean jj_3R_49() throws ParseException {
      Token token1 = jj_scanpos;
      if (jj_3R_53())
        jj_scanpos = token1;
      else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_54())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3_4() throws ParseException{
      if (jj_3R_18())
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_scan_token(93))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_18())
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_scan_token(104))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_17() throws ParseException {
      Token token1 = jj_scanpos;
      if (jj_3_3()) {
        jj_scanpos = token1;
        if (jj_3_4())
          return true;
        if (jj_la == 0 && jj_scanpos == jj_lastpos)
          return false;
      } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      return false;
    }

    private boolean jj_3_3() throws ParseException {
      if (jj_3R_18())
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_scan_token(104))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_150() throws ParseException {
      if (jj_scan_token(82))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_50() throws ParseException {
      if (jj_scan_token(108))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_49())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_149() throws ParseException {
      if (jj_scan_token(8))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_43() throws ParseException {
      if (jj_3R_49())
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      do {
        Token token1 = jj_scanpos;
        if (jj_3R_50()) {
          jj_scanpos = token1;
          break;
        }
        if (jj_la == 0 && jj_scanpos == jj_lastpos)
          return false;
      } while (true);
      return false;
    }

    private boolean jj_3R_148() throws ParseException {
      Token token1 = jj_scanpos;
      if (jj_3R_149())
        jj_scanpos = token1;
      else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_150())
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      do {
        Token token2 = jj_scanpos;
        if (jj_3R_150()) {
          jj_scanpos = token2;
          break;
        }
        if (jj_la == 0 && jj_scanpos == jj_lastpos)
          return false;
      } while (true);
      return false;
    }

    private boolean jj_3R_52() throws ParseException {
      if (jj_scan_token(107))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_147() throws ParseException {
      if (jj_3R_148())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3_2() throws ParseException{
      if (jj_3R_17())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_47()  throws ParseException{
      if (jj_scan_token(106))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_51()  throws ParseException {
      if (jj_scan_token(103))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_44()  throws ParseException{
      Token token1 = jj_scanpos;
      if (jj_3R_51()) {
        jj_scanpos = token1;
        if (jj_3R_52())
          return true;
        if (jj_la == 0 && jj_scanpos == jj_lastpos)
          return false;
      } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_43())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_142() throws ParseException {
      if (jj_3R_20())
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      Token token1 = jj_scanpos;
      if (jj_3R_147())
        jj_scanpos = token1;
      else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      return false;
    }

    private boolean jj_3R_32() throws ParseException {
      if (jj_3R_43())
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      do {
        Token token1 = jj_scanpos;
        if (jj_3R_44()) {
          jj_scanpos = token1;
          break;
        }
        if (jj_la == 0 && jj_scanpos == jj_lastpos)
          return false;
      } while (true);
      return false;
    }

    private boolean jj_3R_119() throws ParseException {
      Token token1 = jj_scanpos;
      if (jj_3R_141()) {
        jj_scanpos = token1;
        if (jj_3R_142())
          return true;
        if (jj_la == 0 && jj_scanpos == jj_lastpos)
          return false;
      } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      return false;
    }

    private boolean jj_3R_141() throws ParseException {
      if (jj_3R_17())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_46() throws ParseException {
      if (jj_scan_token(102))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_45() throws ParseException {
      if (jj_scan_token(101))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_120() throws ParseException {
      if (jj_scan_token(89))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_119())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_33() throws ParseException {
      Token token1 = jj_scanpos;
      if (jj_3R_45()) {
        jj_scanpos = token1;
        if (jj_3R_46()) {
          jj_scanpos = token1;
          if (jj_3R_47())
            return true;
          if (jj_la == 0 && jj_scanpos == jj_lastpos)
            return false;
        } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
          return false;
      } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_32())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_106() throws ParseException {
      if (jj_3R_119())
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      do {
        Token token1 = jj_scanpos;
        if (jj_3R_120()) {
          jj_scanpos = token1;
          break;
        }
        if (jj_la == 0 && jj_scanpos == jj_lastpos)
          return false;
      } while (true);
      return false;
    }

    private boolean jj_3R_20() throws ParseException {
      if (jj_3R_32())
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      do {
        Token token1 = jj_scanpos;
        if (jj_3R_33()) {
          jj_scanpos = token1;
          break;
        }
        if (jj_la == 0 && jj_scanpos == jj_lastpos)
          return false;
      } while (true);
      return false;
    }

    private boolean jj_3R_95()  throws ParseException {
      Token token1 = jj_scanpos;
      if (jj_3R_105()) {
        jj_scanpos = token1;
        if (jj_3R_106())
          return true;
        if (jj_la == 0 && jj_scanpos == jj_lastpos)
          return false;
      } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      return false;
    }

    private boolean jj_3R_105()  throws ParseException {
      if (jj_scan_token(103))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_118() throws ParseException {
      if (jj_scan_token(45))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_104() throws ParseException {
      if (jj_scan_token(23))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_102() throws ParseException {
      if (jj_scan_token(37))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      Token token1 = jj_scanpos;
      if (jj_3R_118())
        jj_scanpos = token1;
      else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_scan_token(47))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_94() throws ParseException {
      Token token1 = jj_scanpos;
      if (jj_3R_103()) {
        jj_scanpos = token1;
        if (jj_3R_104())
          return true;
        if (jj_la == 0 && jj_scanpos == jj_lastpos)
          return false;
      } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      return false;
    }

    private boolean jj_3R_103() throws ParseException {
      if (jj_scan_token(5))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_100() throws ParseException {
      if (jj_3R_112())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_99() throws ParseException {
      if (jj_3R_111())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_98() throws ParseException {
      if (jj_3R_110())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_97() throws ParseException {
      if (jj_3R_109())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_36()throws ParseException {
      if (jj_scan_token(45))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_23() throws ParseException {
      Token token1 = jj_scanpos;
      if (jj_3R_36())
        jj_scanpos = token1;
      else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_scan_token(38))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_20())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_90() throws ParseException {
      if (jj_scan_token(59))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      Token token1 = jj_scanpos;
      if (jj_3R_94())
        jj_scanpos = token1;
      else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_95())
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_96())
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      token1 = jj_scanpos;
      if (jj_3R_97())
        jj_scanpos = token1;
      else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      token1 = jj_scanpos;
      if (jj_3R_98())
        jj_scanpos = token1;
      else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      token1 = jj_scanpos;
      if (jj_3R_99())
        jj_scanpos = token1;
      else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      token1 = jj_scanpos;
      if (jj_3R_100())
        jj_scanpos = token1;
      else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      return false;
    }

    private boolean jj_3R_35()  throws ParseException {
      if (jj_scan_token(45))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_22() throws ParseException {
      Token token1 = jj_scanpos;
      if (jj_3R_35())
        jj_scanpos = token1;
      else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_scan_token(11))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_20())
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_scan_token(6))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_20())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_30() throws ParseException {
      if (jj_scan_token(87))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_116() throws ParseException {
      if (jj_3R_72())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_117() throws ParseException {
      if (jj_3R_86())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_34() throws ParseException {
      if (jj_scan_token(45))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_21() throws ParseException {
      Token token1 = jj_scanpos;
      if (jj_3R_34())
        jj_scanpos = token1;
      else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_scan_token(32))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_scan_token(88))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      token1 = jj_scanpos;
      if (jj_3R_116()) {
        jj_scanpos = token1;
        if (jj_3R_117())
          return true;
        if (jj_la == 0 && jj_scanpos == jj_lastpos)
          return false;
      } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_scan_token(90))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_146() throws ParseException {
      if (jj_scan_token(7))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_143() throws ParseException {
      if (jj_scan_token(93))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_18())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_26() throws ParseException {
      if (jj_scan_token(88))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_scan_token(59))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_121() throws ParseException {
      if (jj_3R_18())
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      Token token1 = jj_scanpos;
      if (jj_3R_143())
        jj_scanpos = token1;
      else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      return false;
    }

    private boolean jj_3R_83() throws ParseException {
      if (jj_scan_token(105))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_25() throws ParseException {
      if (jj_scan_token(5))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_81() throws ParseException {
      if (jj_3R_83())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_76() throws ParseException {
      Token token1 = jj_scanpos;
      if (jj_3R_80()) {
        jj_scanpos = token1;
        if (jj_3R_81())
          return true;
        if (jj_la == 0 && jj_scanpos == jj_lastpos)
          return false;
      } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      return false;
    }

    private boolean jj_3R_80() throws ParseException {
      if (jj_3R_20())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_138() throws ParseException {
      if (jj_scan_token(100))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_137() throws ParseException {
      if (jj_scan_token(99))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_136() throws ParseException{
      if (jj_scan_token(98))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_135() throws ParseException {
      if (jj_scan_token(97))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_134() throws ParseException {
      if (jj_scan_token(96))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3_11() throws ParseException{
      Token token1 = jj_scanpos;
      if (jj_3R_24()) {
        jj_scanpos = token1;
        if (jj_3R_25()) {
          jj_scanpos = token1;
          if (jj_3R_26())
            return true;
          if (jj_la == 0 && jj_scanpos == jj_lastpos)
            return false;
        } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
          return false;
      } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      return false;
    }

    private boolean jj_3R_24() throws ParseException {
      if (jj_scan_token(7))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_133() throws ParseException{
      if (jj_scan_token(95))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_140() throws ParseException {
      if (jj_scan_token(53))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_132() throws ParseException {
      if (jj_scan_token(94))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_115() throws ParseException {
      Token token1 = jj_scanpos;
      if (jj_3R_140())
        jj_scanpos = token1;
      else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_76())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_113() throws ParseException {
      Token token1 = jj_scanpos;
      if (jj_3R_131()) {
        jj_scanpos = token1;
        if (jj_3R_132()) {
          jj_scanpos = token1;
          if (jj_3R_133()) {
            jj_scanpos = token1;
            if (jj_3R_134()) {
              jj_scanpos = token1;
              if (jj_3R_135()) {
                jj_scanpos = token1;
                if (jj_3R_136()) {
                  jj_scanpos = token1;
                  if (jj_3R_137()) {
                    jj_scanpos = token1;
                    if (jj_3R_138())
                      return true;
                    if (jj_la == 0 && jj_scanpos == jj_lastpos)
                      return false;
                  } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
                    return false;
                } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
                  return false;
              } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
                return false;
            } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
              return false;
          } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
            return false;
        } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
          return false;
      } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      return false;
    }

    private boolean jj_3R_131() throws ParseException {
      if (jj_scan_token(92))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_145() throws ParseException {
      if (jj_scan_token(5))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_139() throws ParseException {
      Token token1 = jj_scanpos;
      if (jj_3R_145()) {
        jj_scanpos = token1;
        if (jj_3R_146())
          return true;
        if (jj_la == 0 && jj_scanpos == jj_lastpos)
          return false;
      } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      return false;
    }

    private boolean jj_3R_18() throws ParseException {
      Token token1 = jj_scanpos;
      if (jj_3R_29()) {
        jj_scanpos = token1;
        if (jj_3R_30())
          return true;
        if (jj_la == 0 && jj_scanpos == jj_lastpos)
          return false;
      } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      return false;
    }

    private boolean jj_3R_29() throws ParseException {
      if (jj_scan_token(82))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_114() throws ParseException {
      Token token1 = jj_scanpos;
      if (jj_3R_139())
        jj_scanpos = token1;
      else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_scan_token(88))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_86())
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_scan_token(90))
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_75() throws ParseException {
      if (jj_scan_token(93))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_18())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_71() throws ParseException{
      if (jj_scan_token(93))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_18())
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      Token token1 = jj_scanpos;
      if (jj_3R_75())
        jj_scanpos = token1;
      else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      return false;
    }

    private boolean jj_3R_101() throws ParseException {
      if (jj_3R_113())
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      Token token1 = jj_scanpos;
      if (jj_3R_114()) {
        jj_scanpos = token1;
        if (jj_3R_115())
          return true;
        if (jj_la == 0 && jj_scanpos == jj_lastpos)
          return false;
      } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      return false;
    }

    private boolean jj_3R_66() throws ParseException {
      if (jj_3R_18())
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      Token token1 = jj_scanpos;
      if (jj_3R_71())
        jj_scanpos = token1;
      else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      return false;
    }

    private boolean jj_3R_77() throws ParseException{
      if (jj_scan_token(89))
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      if (jj_3R_76())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_72() throws ParseException{
      if (jj_3R_76())
        return true;
      if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      do {
        Token token1 = jj_scanpos;
        if (jj_3R_77()) {
          jj_scanpos = token1;
          break;
        }
        if (jj_la == 0 && jj_scanpos == jj_lastpos)
          return false;
      } while (true);
      return false;
    }

    private boolean jj_3R_93() throws ParseException{
      if (jj_3R_102())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3_10() throws ParseException{
      if (jj_3R_23())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3_9() throws ParseException{
      if (jj_3R_22())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3_8() throws ParseException{
      if (jj_3R_21())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    private boolean jj_3R_89() throws ParseException{
      Token token1 = jj_scanpos;
      if (jj_3R_92()) {
        jj_scanpos = token1;
        if (jj_3_8()) {
          jj_scanpos = token1;
          if (jj_3_9()) {
            jj_scanpos = token1;
            if (jj_3_10()) {
              jj_scanpos = token1;
              if (jj_3R_93())
                return true;
              if (jj_la == 0 && jj_scanpos == jj_lastpos)
                return false;
            } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
              return false;
          } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
            return false;
        } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
          return false;
      } else if (jj_la == 0 && jj_scanpos == jj_lastpos)
        return false;
      return false;
    }

    private boolean jj_3R_92() throws ParseException{
      if (jj_3R_101())
        return true;
      return jj_la != 0 || jj_scanpos != jj_lastpos ? false : false;
    }

    public SQLJJParser(InputStream inputstream) {
      lookingAhead = false;
      jj_la1 = new int[98];
      jj_2_rtns = new JJCalls[15];
      jj_rescan = false;
      jj_gc = 0;
      jj_expentries = new ArrayList();
      jj_kind = -1;
      jj_lasttokens = new int[100];
      jj_input_stream = new RuleCharStream(inputstream, 1, 1);
      setSQLJJParserTokenManager(jj_input_stream);
      token = new Token();
      jj_ntk = -1;
      jj_gen = 0;
      for (int i = 0; i < 98; i++)
        jj_la1[i] = -1;
      for (int j = 0; j < jj_2_rtns.length; j++)
        jj_2_rtns[j] = new JJCalls();
    }

    public void ReInit(InputStream inputstream) {
      jj_input_stream.ReInit(inputstream, 1, 1);
      ReInit(jj_input_stream);
      token = new Token();
      jj_ntk = -1;
      jj_gen = 0;
      for (int i = 0; i < 98; i++)
        jj_la1[i] = -1;
      for (int j = 0; j < jj_2_rtns.length; j++)
        jj_2_rtns[j] = new JJCalls();
    }

    public SQLJJParser(Reader reader) {
      lookingAhead = false;
      jj_la1 = new int[98];
      jj_2_rtns = new JJCalls[15];
      jj_rescan = false;
      jj_gc = 0;
      jj_expentries = new ArrayList();
      jj_kind = -1;
      jj_lasttokens = new int[100];
      jj_input_stream = new RuleCharStream(reader, 1, 1);
      setSQLJJParserTokenManager(jj_input_stream);
      token = new Token();
      jj_ntk = -1;
      jj_gen = 0;
      for (int i = 0; i < 98; i++)
        jj_la1[i] = -1;
      for (int j = 0; j < jj_2_rtns.length; j++)
        jj_2_rtns[j] = new JJCalls();
    }

    public void ReInit(Reader reader) {
      jj_input_stream.ReInit(reader, 1, 1);
      ReInit(jj_input_stream);
      token = new Token();
      jj_ntk = -1;
      jj_gen = 0;
      for (int i = 0; i < 98; i++)
        jj_la1[i] = -1;
      for (int j = 0; j < jj_2_rtns.length; j++)
        jj_2_rtns[j] = new JJCalls();
    }

    public SQLJJParser() {
      lookingAhead = false;
      jj_la1 = new int[98];
      jj_2_rtns = new JJCalls[15];
      jj_rescan = false;
      jj_gc = 0;
      jj_expentries = new ArrayList();
      jj_kind = -1;
      jj_lasttokens = new int[100];
      token = new Token();
      jj_ntk = -1;
      jj_gen = 0;
      for (int i = 0; i < 98; i++)
        jj_la1[i] = -1;
      for (int j = 0; j < jj_2_rtns.length; j++)
        jj_2_rtns[j] = new JJCalls();
    }

    public void ReInit() {
      token = new Token();
      jj_ntk = -1;
      jj_gen = 0;
      for (int i = 0; i < 98; i++)
        jj_la1[i] = -1;
      for (int j = 0; j < jj_2_rtns.length; j++)
        jj_2_rtns[j] = new JJCalls();
    }

    private Token jj_consume_token(int i) throws ParseException {
      Token token1;
      if ((token1 = token).next != null)
        token = token.next;
      else
        token = token.next = getNextTokenFromManager();
      jj_ntk = -1;
      if (token.kind == i) {
        jj_gen++;
        if (++jj_gc > 100) {
          jj_gc = 0;
          for (int j = 0; j < jj_2_rtns.length; j++) {
            for (JJCalls jjcalls = jj_2_rtns[j]; jjcalls != null; jjcalls = jjcalls.next)
              if (jjcalls.gen < jj_gen)
                jjcalls.first = null;
          }
        }
        return token;
      } else {
        token = token1;
        jj_kind = i;
        throw generateParseException();
      }
    }

    private boolean jj_scan_token(int i) throws ParseException {
      if (jj_scanpos == jj_lastpos) {
        jj_la--;
        if (jj_scanpos.next == null)
          jj_lastpos = jj_scanpos = jj_scanpos.next = getNextTokenFromManager();
        else
          jj_lastpos = jj_scanpos = jj_scanpos.next;
      } else {
        jj_scanpos = jj_scanpos.next;
      }
      if (jj_rescan) {
        int j = 0;
        Token token1;
        for (token1 = token; token1 != null && token1 != jj_scanpos; token1 = token1.next)
          j++;
        if (token1 != null)
          jj_add_error_token(i, j);
      }
      return jj_scanpos.kind != i;
    }

    public Token getNextToken() throws ParseException {
      if (token.next != null)
        token = token.next;
      else
        token = token.next = getNextTokenFromManager();
      jj_ntk = -1;
      jj_gen++;
      return token;
    }

    public Token getToken(int i) throws ParseException {
      Token token1 = lookingAhead ? jj_scanpos : token;
      for (int j = 0; j < i; j++)
        if (token1.next != null)
          token1 = token1.next;
        else
          token1 = token1.next = getNextTokenFromManager();
      return token1;
    }

    private int jj_ntk() throws ParseException {
      if ((jj_nt = token.next) == null)
        return jj_ntk = (token.next = getNextTokenFromManager()).kind;
      else
        return jj_ntk = jj_nt.kind;
    }

    private void jj_add_error_token(int i, int j) {
      if (j >= 100)
        return;
      if (j == jj_endpos + 1)
        jj_lasttokens[jj_endpos++] = i;
      else if (jj_endpos != 0) {
        jj_expentry = new int[jj_endpos];
        for (int k = 0; k < jj_endpos; k++)
          jj_expentry[k] = jj_lasttokens[k];
        boolean flag = false;
        Iterator enumeration = jj_expentries.iterator();
        while (enumeration.hasNext()) {
          int ai[] = (int[])enumeration.next();
          if (ai.length != jj_expentry.length)
            continue;
          flag = true;
          for (int l = 0; l < jj_expentry.length; l++) {
            if (ai[l] == jj_expentry[l])
              continue;
            flag = false;
            break;
          }
          if (flag)
            break;
        }
        if (!flag)
          jj_expentries.add(jj_expentry);
        if (j != 0)
          jj_lasttokens[(jj_endpos = j) - 1] = i;
      }
    }

    public ParseException generateParseException() throws ParseException{
      jj_expentries.clear();
      boolean aflag[] = new boolean[109];
      for (int i = 0; i < 109; i++)
        aflag[i] = false;
      if (jj_kind >= 0) {
        aflag[jj_kind] = true;
        jj_kind = -1;
      }
      for (int j = 0; j < 98; j++)
        if (jj_la1[j] == jj_gen) {
          for (int k = 0; k < 32; k++) {
            if ((jj_la1_0[j] & 1 << k) != 0)
              aflag[k] = true;
            if ((jj_la1_1[j] & 1 << k) != 0)
              aflag[32 + k] = true;
            if ((jj_la1_2[j] & 1 << k) != 0)
              aflag[64 + k] = true;
            if ((jj_la1_3[j] & 1 << k) != 0)
              aflag[96 + k] = true;
          }
        }
      for (int l = 0; l < 109; l++)
        if (aflag[l]) {
          jj_expentry = new int[1];
          jj_expentry[0] = l;
          jj_expentries.add(jj_expentry);
        }
      jj_endpos = 0;
      jj_rescan_token();
      jj_add_error_token(0, 0);
      int ai[][] = new int[jj_expentries.size()][];
      for (int i1 = 0; i1 < jj_expentries.size(); i1++)
        ai[i1] = (int[])jj_expentries.get(i1);
      return new ParseException(token, ai, tokenImage);
    }

    public void enable_tracing() {
    }

    public void disable_tracing() {
    }

    private void jj_rescan_token() throws ParseException{
      jj_rescan = true;
      for (int i = 0; i < 15; i++) {
        JJCalls jjcalls = jj_2_rtns[i];
        do {
          if (jjcalls.gen > jj_gen) {
            jj_la = jjcalls.arg;
            jj_lastpos = jj_scanpos = jjcalls.first;
            switch (i) {
              case 0: // '\0'
                jj_3_1();
                break;
              case 1: // '\001'
                jj_3_2();
                break;
              case 2: // '\002'
                jj_3_3();
                break;
              case 3: // '\003'
                jj_3_4();
                break;
              case 4: // '\004'
                jj_3_5();
                break;
              case 5: // '\005'
                jj_3_6();
                break;
              case 6: // '\006'
                jj_3_7();
                break;
              case 7: // '\007'
                jj_3_8();
                break;
              case 8: // '\b'
                jj_3_9();
                break;
              case 9: // '\t'
                jj_3_10();
                break;
              case 10: // '\n'
                jj_3_11();
                break;
              case 11: // '\013'
                jj_3_12();
                break;
              case 12: // '\f'
                jj_3_13();
                break;
              case 13: // '\r'
                jj_3_14();
                break;
              case 14: // '\016'
                jj_3_15();
                break;
            }
          }
          jjcalls = jjcalls.next;
        } while (jjcalls != null);
      }
      jj_rescan = false;
    }

    private void jj_save(int i, int j) {
      JJCalls jjcalls;
      for (jjcalls = jj_2_rtns[i]; jjcalls.gen > jj_gen; jjcalls = jjcalls.next) {
        if (jjcalls.next != null)
          continue;
        jjcalls = jjcalls.next = new JJCalls();
        break;
      }
      jjcalls.gen = (jj_gen + j) - jj_la;
      jjcalls.first = token;
      jjcalls.arg = j;
    }
    // public SQLJJParserTokenManager token_source;
    RuleCharStream jj_input_stream;
    public Token token;
    public Token jj_nt;
    private int jj_ntk;
    private Token jj_scanpos;
    private Token jj_lastpos;
    private int jj_la;
    public boolean lookingAhead;
    private boolean jj_semLA;
    private int jj_gen;
    private int jj_la1[];
    private int jj_la1_0[] = { 0x8008000, 0, 0, 0x810b000, 0x4220000, 0x4000000, 0x4220000, 0, 0x10000, 0, 0, 0, 0x10000, 0, 0x1000000, 0, 0, 0x1000000, 0, 0, 0x2080400, 0, 0, 0, 0x20000000, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0x10000000, 0x800020, 0x800020, 0, 0x40000, 0x40000000, 0, 0, 0x80400, 256, 0x80400, 256, 0, 0, 0, 0, 0, 0x80000000, 32, 0, 0, 0x400200, 0x400200, 0, 0x400200, 0x400200, 0, 0, 0, 64, 0, 0x80400, 0, 0, 0x80400, 0, 0, 2048, 0, 160, 160, 0, 0x80400, 0x80400, 0, 0x80400, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0x800020, 0x800020, 0, 0, 0x80400, 0, 0 };
    private int jj_la1_1[] = { 0x1011004, 0, 0, 0x1011004, 0x1a400082, 0x400000, 0x1a400082, 0, 0, 0, 16384, 0, 0, 0x40000, 0x20000000, 0x4000000, 0x4000000, 0x24000000, 0, 0, 0x20a300, 0, 0, 0x8000000, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0x100000, 0, 0, 0, 0, 0x80000000, 0, 1032, 0, 33536, 0, 33536, 0, 0, 0, 0, 0x80000000, 0x80000000, 0, 0, 1032, 0x8000000, 0, 0, 0, 0, 0, 0, 0x20000, 0x80000, 0, 8192, 0x20a300, 8192, 0x200000, 0x208300, 0, 32, 8289, 0, 0, 0, 0x200000, 0x208300, 33536, 8192, 0x8008300, 8192, 8192, 8192, 0, 0, 0, 0, 0, 0, 0, 0, 0, 32768, 0, 768, 0, 0 };
    private int jj_la1_2[] = { 192, 0x2000000, 0x1000000, 192, 16, 0, 16, 1024, 0, 0x2000000, 0, 1024, 0, 2048, 0, 16, 16, 0, 256, 0x2000000, 0x1e41001, 0x2000000, 0x1000000, 32, 0, 256, 0x20000000, 0x20000000, 0x840000, 0xd0000000, 0x20000000, 0, 0, 0x41000, 0, 0, 0, 0, 256, 0, 0, 8, 0x2000000, 0x1e41001, 0x40000, 0x1e41001, 0, 0x40000, 0x2000000, 0x40000, 0, 0, 0, 0, 8, 0, 0, 0, 0x2000000, 0, 0, 0x2000000, 0, 0, 0, 0, 0x1e41001, 0, 0, 0x1e41001, 0xd0000000, 0, 0xd0000000, 0x2000000, 0, 0, 0, 0x1e41001, 0x1e41001, 0, 0x1e41001, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0x1e41000, 1, 0x20000000, 0x20000000 };
    private int jj_la1_3[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 608, 0, 0, 0, 0, 0, 0, 0, 0, 31, 0, 96, 96, 96, 0, 0, 0, 0, 0, 0, 0, 0, 0, 224, 0, 96, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 96, 0, 0, 96, 31, 0, 31, 0, 0, 0, 0, 608, 608, 0, 608, 0, 0, 0, 1120, 1120, 2176, 2176, 4096, 96, 96, 0, 0, 0, 0, 0, 0, 0 };
    private JJCalls jj_2_rtns[];
    private boolean jj_rescan;
    private int jj_gc;
    private ArrayList jj_expentries;
    private int jj_expentry[];
    private int jj_kind;
    private int jj_lasttokens[];
    private int jj_endpos;

    public void setDebugStream(PrintStream printstream) {
      debugStream = printstream;
    }

    private int jjStopStringLiteralDfa_0(int i, long l, long l1) {
      switch (i) {
        case 0: // '\0'
          if ((l1 & 0x4000000000L) != 0L)
            return 0;
          if ((l1 & 0x10020000000L) != 0L)
            return 47;
          if ((l1 & 0x80000000000L) != 0L)
            return 3;
          if ((l & -32L) != 0L || (l1 & 4095L) != 0L) {
            jjmatchedKind = 82;
            return 48;
          } else {
            return -1;
          }
        case 1: // '\001'
          if ((l & 0x1a003f00004300L) != 0L)
            return 48;
          if ((l & 0xffe5ffc0ffffbce0L) != 0L || (l1 & 4095L) != 0L) {
            if (jjmatchedPos != 1) {
              jjmatchedKind = 82;
              jjmatchedPos = 1;
            }
            return 48;
          } else {
            return -1;
          }
        case 2: // '\002'
          if ((l & 0xebf5d8deefffb800L) != 0L || (l1 & 4094L) != 0L) {
            if (jjmatchedPos != 2) {
              jjmatchedKind = 82;
              jjmatchedPos = 2;
            }
            return 48;
          }
          return (l & 0x14002700100006e0L) == 0L && (l1 & 1L) == 0L ? -1 : 48;
        case 3: // '\003'
          if ((l & 0x1c488d024508000L) != 0L || (l1 & 1536L) != 0L)
            return 48;
          if ((l & 0xea31540ecbaf3800L) != 0L || (l1 & 2558L) != 0L) {
            jjmatchedKind = 82;
            jjmatchedPos = 3;
            return 48;
          } else {
            return -1;
          }
        case 4: // '\004'
          if ((l & 0xa030040048080000L) != 0L || (l1 & 2314L) != 0L)
            return 48;
          if ((l & 0x4a01500e83a73800L) != 0L || (l1 & 244L) != 0L) {
            jjmatchedKind = 82;
            jjmatchedPos = 4;
            return 48;
          } else {
            return -1;
          }
        case 5: // '\005'
          if ((l & 0x4200100c01853800L) != 0L || (l1 & 196L) != 0L) {
            jjmatchedKind = 82;
            jjmatchedPos = 5;
            return 48;
          }
          return (l & 0x801400282220000L) == 0L && (l1 & 48L) == 0L ? -1 : 48;
        case 6: // '\006'
          if ((l & 4096L) != 0L) {
            if (jjmatchedPos != 6) {
              jjmatchedKind = 82;
              jjmatchedPos = 6;
            }
            return 11;
          }
          if ((l & 0x100400052800L) != 0L || (l1 & 192L) != 0L)
            return 48;
          if ((l & 0x4200000801800000L) != 0L || (l1 & 4L) != 0L) {
            if (jjmatchedPos != 6) {
              jjmatchedKind = 82;
              jjmatchedPos = 6;
            }
            return 48;
          } else {
            return -1;
          }
        case 7: // '\007'
          if ((l & 0x4200000000800000L) != 0L)
            return 48;
          if ((l & 4096L) != 0L) {
            jjmatchedKind = 82;
            jjmatchedPos = 7;
            return 11;
          }
          if ((l1 & 64L) != 0L)
            return 11;
          if ((l & 0x801000000L) != 0L || (l1 & 4L) != 0L) {
            jjmatchedKind = 82;
            jjmatchedPos = 7;
            return 48;
          } else {
            return -1;
          }
        case 8: // '\b'
          if ((l1 & 4L) != 0L) {
            jjmatchedKind = 82;
            jjmatchedPos = 8;
            return 48;
          }
          if ((l & 0x801000000L) != 0L)
            return 48;
          if ((l & 4096L) != 0L) {
            jjmatchedKind = 82;
            jjmatchedPos = 8;
            return 11;
          } else {
            return -1;
          }
        case 9: // '\t'
          if ((l1 & 4L) != 0L) {
            jjmatchedKind = 82;
            jjmatchedPos = 9;
            return 48;
          }
          if ((l & 4096L) != 0L) {
            jjmatchedKind = 82;
            jjmatchedPos = 9;
            return 11;
          } else {
            return -1;
          }
        case 10: // '\n'
          if ((l1 & 4L) != 0L)
            return 48;
          if ((l & 4096L) != 0L) {
            jjmatchedKind = 82;
            jjmatchedPos = 10;
            return 11;
          } else {
            return -1;
          }
        case 11: // '\013'
          if ((l & 4096L) != 0L) {
            jjmatchedKind = 82;
            jjmatchedPos = 11;
            return 11;
          } else {
            return -1;
          }
        case 12: // '\f'
          if ((l & 4096L) != 0L) {
            jjmatchedKind = 82;
            jjmatchedPos = 12;
            return 11;
          } else {
            return -1;
          }
      }
      return -1;
    }

    private int jjStartNfa_0(int i, long l, long l1) {
      return jjMoveNfa_0(jjStopStringLiteralDfa_0(i, l, l1), i + 1);
    }

    private int jjStopAtPos(int i, int j) {
      jjmatchedKind = j;
      jjmatchedPos = i;
      return i + 1;
    }

    private int jjStartNfaWithStates_0(int i, int j, int k) {
      jjmatchedKind = j;
      jjmatchedPos = i;
      try {
        curChar = input_stream.readChar();
      } catch (IOException ioexception) {
        return i + 1;
      }
      return jjMoveNfa_0(k, i + 1);
    }

    private int jjMoveStringLiteralDfa0_0() {
      switch (curChar) {
        case 33: // '!'
          return jjMoveStringLiteralDfa1_0(0L, 0x40000000L);
        case 35: // '#'
          return jjStopAtPos(0, 95);
        case 40: // '('
          return jjStopAtPos(0, 88);
        case 41: // ')'
          return jjStopAtPos(0, 90);
        case 42: // '*'
          jjmatchedKind = 103;
          return jjMoveStringLiteralDfa1_0(0L, 0x100000000000L);
        case 43: // '+'
          return jjStopAtPos(0, 101);
        case 44: // ','
          return jjStopAtPos(0, 89);
        case 45: // '-'
          return jjStartNfaWithStates_0(0, 102, 0);
        case 46: // '.'
          jjmatchedKind = 93;
          return jjMoveStringLiteralDfa1_0(0L, 0x10000000000L);
        case 47: // '/'
          return jjStartNfaWithStates_0(0, 107, 3);
        case 59: // ';'
          return jjStopAtPos(0, 91);
        case 60: // '<'
          jjmatchedKind = 99;
          return jjMoveStringLiteralDfa1_0(0L, 0x1100000000L);
        case 61: // '='
          return jjStopAtPos(0, 92);
        case 62: // '>'
          jjmatchedKind = 97;
          return jjMoveStringLiteralDfa1_0(0L, 0x400000000L);
        case 63: // '?'
          return jjStopAtPos(0, 105);
        case 65: // 'A'
        case 97: // 'a'
          return jjMoveStringLiteralDfa1_0(2016L, 0L);
        case 66: // 'B'
        case 98: // 'b'
          return jjMoveStringLiteralDfa1_0(30720L, 0L);
        case 67: // 'C'
        case 99: // 'c'
          return jjMoveStringLiteralDfa1_0(0xf8000L, 0L);
        case 68: // 'D'
        case 100: // 'd'
          return jjMoveStringLiteralDfa1_0(0xf00000L, 0L);
        case 69: // 'E'
        case 101: // 'e'
          return jjMoveStringLiteralDfa1_0(0x7000000L, 0L);
        case 70: // 'F'
        case 102: // 'f'
          return jjMoveStringLiteralDfa1_0(0x38000000L, 0L);
        case 71: // 'G'
        case 103: // 'g'
          return jjMoveStringLiteralDfa1_0(0x40000000L, 0L);
        case 72: // 'H'
        case 104: // 'h'
          return jjMoveStringLiteralDfa1_0(0x80000000L, 0L);
        case 73: // 'I'
        case 105: // 'i'
          return jjMoveStringLiteralDfa1_0(0x3f00000000L, 0L);
        case 76: // 'L'
        case 108: // 'l'
          return jjMoveStringLiteralDfa1_0(0xc000000000L, 0L);
        case 77: // 'M'
        case 109: // 'm'
          return jjMoveStringLiteralDfa1_0(0xf0000000000L, 0L);
        case 78: // 'N'
        case 110: // 'n'
          return jjMoveStringLiteralDfa1_0(0x1f00000000000L, 0L);
        case 79: // 'O'
        case 111: // 'o'
          return jjMoveStringLiteralDfa1_0(0x1e000000000000L, 0L);
        case 80: // 'P'
        case 112: // 'p'
          return jjMoveStringLiteralDfa1_0(0x20000000000000L, 0L);
        case 81: // 'Q'
        case 113: // 'q'
          return jjMoveStringLiteralDfa1_0(0x40000000000000L, 0L);
        case 82: // 'R'
        case 114: // 'r'
          return jjMoveStringLiteralDfa1_0(0x780000000000000L, 0L);
        case 83: // 'S'
        case 115: // 's'
          return jjMoveStringLiteralDfa1_0(0xf800000000000000L, 1L);
        case 84: // 'T'
        case 116: // 't'
          return jjMoveStringLiteralDfa1_0(0L, 6L);
        case 85: // 'U'
        case 117: // 'u'
          return jjMoveStringLiteralDfa1_0(0L, 24L);
        case 86: // 'V'
        case 118: // 'v'
          return jjMoveStringLiteralDfa1_0(0L, 224L);
        case 87: // 'W'
        case 119: // 'w'
          return jjMoveStringLiteralDfa1_0(0L, 3840L);
        case 124: // '|'
          return jjMoveStringLiteralDfa1_0(0L, 0x40000000000L);
        case 91: // '[' //
          return jjMoveStringLiteralDfa1_0(0xf00000L, 0L);
        case 93: // ']'
          return jjMoveStringLiteralDfa1_0(0xf00000L, 0L);
        case 34: // '"'
        case 36: // '$'
        case 37: // '%'
        case 38: // '&'
        case 39: // '\''
        case 48: // '0'
        case 49: // '1'
        case 50: // '2'
        case 51: // '3'
        case 52: // '4'
        case 53: // '5'
        case 54: // '6'
        case 55: // '7'
        case 56: // '8'
        case 57: // '9'
        case 58: // ':'
        case 64: // '@'
        case 74: // 'J'
        case 75: // 'K'
        case 88: // 'X'
        case 89: // 'Y'
        case 90: // 'SQL'
          // case 91: // '['
        case 92: // '\\'
          // case 93: // ']'
        case 94: // '^'
        case 95: // '_'
        case 96: // '`'
        case 106: // 'j'
        case 107: // 'k'
        case 120: // 'x'
        case 121: // 'y'
        case 122: // 'z'
        case 123: // '{'
          // possible pla
        default:
          return jjMoveNfa_0(2, 0);
      }
    }

    private int jjMoveStringLiteralDfa1_0(long l, long l1) {
      try {
        curChar = input_stream.readChar();
      } catch (IOException ioexception) {
        jjStopStringLiteralDfa_0(0, l, l1);
        return 1;
      }
      switch (curChar) {
        case 43: // '+'
        case 44: // ','
        case 45: // '-'
        case 46: // '.'
        case 47: // '/'
        case 48: // '0'
        case 49: // '1'
        case 50: // '2'
        case 51: // '3'
        case 52: // '4'
        case 53: // '5'
        case 54: // '6'
        case 55: // '7'
        case 56: // '8'
        case 57: // '9'
        case 58: // ':'
        case 59: // ';'
        case 60: // '<'
        case 63: // '?'
        case 64: // '@'
        case 66: // 'B'
        case 67: // 'C'
        case 68: // 'D'
        case 71: // 'G'
        case 74: // 'J'
        case 75: // 'K'
        case 81: // 'Q'
        case 87: // 'W'
        case 90: // 'SQL'
          // case 91: // '['
        case 92: // '\\'
          // case 93: // ']'
        case 94: // '^'
        case 95: // '_'
        case 96: // '`'
        case 98: // 'b'
        case 99: // 'c'
        case 100: // 'd'
        case 103: // 'g'
        case 106: // 'j'
        case 107: // 'k'
        case 113: // 'q'
        case 119: // 'w'
        case 122: // 'z'
        case 123: // '{'
        default:
          break;
        case 91:
        case 42: // '*'
          if ((l1 & 0x10000000000L) != 0L)
            return jjStopAtPos(1, 104);
          if ((l1 & 0x100000000000L) != 0L)
            return jjStopAtPos(1, 108);
          break;
        case 61: // '='
          if ((l1 & 0x40000000L) != 0L)
            return jjStopAtPos(1, 94);
          if ((l1 & 0x400000000L) != 0L)
            return jjStopAtPos(1, 98);
          if ((l1 & 0x1000000000L) != 0L)
            return jjStopAtPos(1, 100);
          break;
        case 62: // '>'
          if ((l1 & 0x100000000L) != 0L)
            return jjStopAtPos(1, 96);
          break;
        case 65: // 'A'
        case 97: // 'a'
          return jjMoveStringLiteralDfa2_0(l, 0x110080100000L, l1, 226L);
        case 69: // 'E'
        case 101: // 'e'
          return jjMoveStringLiteralDfa2_0(l, 0x1980000000600800L, l1, 0L);
        case 70: // 'F'
        case 102: // 'f'
          if ((l & 0x2000000000000L) != 0L)
            return jjStartNfaWithStates_0(1, 49, 48);
          break;
        case 72: // 'H'
        case 104: // 'h'
          return jjMoveStringLiteralDfa2_0(l, 0x2000000000008000L, l1, 256L);
        case 73: // 'I'
        case 105: // 'i'
          return jjMoveStringLiteralDfa2_0(l, 0x64000801000L, l1, 512L);
        case 76: // 'L'
        case 108: // 'l'
          return jjMoveStringLiteralDfa2_0(l, 0x8000020L, l1, 0L);
        case 77: // 'M'
        case 109: // 'm'
          return jjMoveStringLiteralDfa2_0(l, 0x4000000000000000L, l1, 0L);
        case 78: // 'N'
        case 110: // 'n'
          if ((l & 0x100000000L) != 0L) {
            jjmatchedKind = 32;
            jjmatchedPos = 1;
          }
          return jjMoveStringLiteralDfa2_0(l, 0x4001e000000c0L, l1, 8L);
        case 79: // 'O'
        case 111: // 'o'
          return jjMoveStringLiteralDfa2_0(l, 0x6006880100f2000L, l1, 1024L);
        case 80: // 'P'
        case 112: // 'p'
          return jjMoveStringLiteralDfa2_0(l, 0L, l1, 16L);
        case 82: // 'R'
        case 114: // 'r'
          if ((l & 0x8000000000000L) != 0L) {
            jjmatchedKind = 51;
            jjmatchedPos = 1;
          }
          return jjMoveStringLiteralDfa2_0(l, 0x30000060000000L, l1, 2052L);
        case 83: // 'S'
        case 115: // 's'
          if ((l & 256L) != 0L) {
            jjmatchedKind = 8;
            jjmatchedPos = 1;
          } else if ((l & 0x2000000000L) != 0L)
            return jjStartNfaWithStates_0(1, 37, 48);
          return jjMoveStringLiteralDfa2_0(l, 512L, l1, 0L);
        case 84: // 'T'
        case 116: // 't'
          return jjMoveStringLiteralDfa2_0(l, 0x8000000000000000L, l1, 0L);
        case 85: // 'U'
        case 117: // 'u'
          return jjMoveStringLiteralDfa2_0(l, 0x41800000000000L, l1, 1L);
        case 86: // 'V'
        case 118: // 'v'
          return jjMoveStringLiteralDfa2_0(l, 1024L, l1, 0L);
        case 88: // 'X'
        case 120: // 'x'
          return jjMoveStringLiteralDfa2_0(l, 0x7000000L, l1, 0L);
        case 89: // 'Y'
        case 121: // 'y'
          if ((l & 16384L) != 0L)
            return jjStartNfaWithStates_0(1, 14, 48);
          break;
        case 124: // '|'
          if ((l1 & 0x40000000000L) != 0L)
            return jjStopAtPos(1, 106);
          break;
      }
      return jjStartNfa_0(0, l, l1);
    }

    private int jjMoveStringLiteralDfa2_0(long l, long l1, long l2, long l3) {
      if (((l1 &= l) | (l3 &= l2)) == 0L)
        return jjStartNfa_0(0, l, l2);
      try {
        curChar = input_stream.readChar();
      } catch (IOException ioexception) {
        jjStopStringLiteralDfa_0(1, l1, l3);
        return 2;
      }
      switch (curChar) {
        case 70: // 'F'
        case 72: // 'H'
        case 74: // 'J'
        case 80: // 'P'
        case 81: // 'Q'
        case 90: // 'SQL'
        case 91: // '['
        case 92: // '\\'
        case 93: // ']'
        case 94: // '^'
        case 95: // '_'
        case 96: // '`'
        case 102: // 'f'
        case 104: // 'h'
        case 106: // 'j'
        case 112: // 'p'
        case 113: // 'q'
        default:
          break;
        case 65: // 'A'
        case 97: // 'a'
          return jjMoveStringLiteralDfa3_0(l1, 0xe180000000008000L, l3, 4L);
        case 66: // 'B'
        case 98: // 'b'
          return jjMoveStringLiteralDfa3_0(l1, 0L, l3, 2L);
        case 67: // 'C'
        case 99: // 'c'
          if ((l1 & 512L) != 0L)
            return jjStartNfaWithStates_0(2, 9, 48);
          else
            return jjMoveStringLiteralDfa3_0(l1, 0x8001000000L, l3, 0L);
        case 68: // 'D'
        case 100: // 'd'
          if ((l1 & 64L) != 0L)
            return jjStartNfaWithStates_0(2, 6, 48);
          else
            return jjMoveStringLiteralDfa3_0(l1, 0x10080000000000L, l3, 16L);
        case 69: // 'E'
        case 101: // 'e'
          return jjMoveStringLiteralDfa3_0(l1, 0L, l3, 256L);
        case 71: // 'G'
        case 103: // 'g'
          if ((l1 & 1024L) != 0L)
            return jjStartNfaWithStates_0(2, 10, 48);
          break;
        case 73: // 'I'
        case 105: // 'i'
          return jjMoveStringLiteralDfa3_0(l1, 0x60000006000000L, l3, 2056L);
        case 75: // 'K'
        case 107: // 'k'
          return jjMoveStringLiteralDfa3_0(l1, 0x4000000000L, l3, 0L);
        case 76: // 'L'
        case 108: // 'l'
          if ((l1 & 32L) != 0L)
            return jjStartNfaWithStates_0(2, 5, 48);
          else
            return jjMoveStringLiteralDfa3_0(l1, 0xa04800000200000L, l3, 32L);
        case 77: // 'M'
        case 109: // 'm'
          if ((l3 & 1L) != 0L)
            return jjStartNfaWithStates_0(2, 64, 48);
          else
            return jjMoveStringLiteralDfa3_0(l1, 0x1000000030000L, l3, 0L);
        case 78: // 'N'
        case 110: // 'n'
          if ((l1 & 0x20000000000L) != 0L) {
            jjmatchedKind = 41;
            jjmatchedPos = 2;
          }
          return jjMoveStringLiteralDfa3_0(l1, 0x40000041000L, l3, 0L);
        case 79: // 'O'
        case 111: // 'o'
          return jjMoveStringLiteralDfa3_0(l1, 0x68002000L, l3, 0L);
        case 82: // 'R'
        case 114: // 'r'
          if ((l1 & 0x10000000L) != 0L)
            return jjStartNfaWithStates_0(2, 28, 48);
          else
            return jjMoveStringLiteralDfa3_0(l1, 0L, l3, 1216L);
        case 83: // 'S'
        case 115: // 's'
          return jjMoveStringLiteralDfa3_0(l1, 0x200c00000L, l3, 0L);
        case 84: // 'T'
        case 116: // 't'
          if ((l1 & 0x200000000000L) != 0L)
            return jjStartNfaWithStates_0(2, 45, 48);
          if ((l1 & 0x1000000000000000L) != 0L)
            return jjStartNfaWithStates_0(2, 60, 48);
          else
            return jjMoveStringLiteralDfa3_0(l1, 0x101c00100800L, l3, 512L);
        case 85: // 'U'
        case 117: // 'u'
          return jjMoveStringLiteralDfa3_0(l1, 0x80000L, l3, 0L);
        case 86: // 'V'
        case 118: // 'v'
          return jjMoveStringLiteralDfa3_0(l1, 0x80000000L, l3, 0L);
        case 87: // 'W'
        case 119: // 'w'
          if ((l1 & 0x400000000000000L) != 0L)
            return jjStartNfaWithStates_0(2, 58, 48);
          else
            return jjMoveStringLiteralDfa3_0(l1, 0x400000000000L, l3, 0L);
        case 88: // 'X'
        case 120: // 'x'
          if ((l1 & 0x10000000000L) != 0L)
            return jjStartNfaWithStates_0(2, 40, 48);
          break;
        case 89: // 'Y'
        case 121: // 'y'
          if ((l1 & 128L) != 0L)
            return jjStartNfaWithStates_0(2, 7, 48);
          break;
      }
      return jjStartNfa_0(1, l1, l3);
    }

    private int jjMoveStringLiteralDfa3_0(long l, long l1, long l2, long l3) {
      if (((l1 &= l) | (l3 &= l2)) == 0L)
        return jjStartNfa_0(1, l, l2);
      try {
        curChar = input_stream.readChar();
      } catch (IOException ioexception) {
        jjStopStringLiteralDfa_0(2, l1, l3);
        return 3;
      }
      switch (curChar) {
        case 70: // 'F'
        case 71: // 'G'
        case 74: // 'J'
        case 80: // 'P'
        case 81: // 'Q'
        case 86: // 'V'
        case 88: // 'X'
        case 90: // 'SQL'
        case 91: // '['
        case 92: // '\\'
        case 93: // ']'
        case 94: // '^'
        case 95: // '_'
        case 96: // '`'
        case 102: // 'f'
        case 103: // 'g'
        case 106: // 'j'
        case 112: // 'p'
        case 113: // 'q'
        case 118: // 'v'
        case 120: // 'x'
        default:
          break;
        case 65: // 'A'
        case 97: // 'a'
          return jjMoveStringLiteralDfa4_0(l1, 0x400008001000L, l3, 16L);
        case 66: // 'B'
        case 98: // 'b'
          return jjMoveStringLiteralDfa4_0(l1, 0x1000000000000L, l3, 0L);
        case 67: // 'C'
        case 99: // 'c'
          if ((l1 & 0x400000L) != 0L)
            return jjStartNfaWithStates_0(3, 22, 48);
          else
            return jjMoveStringLiteralDfa4_0(l1, 0L, l3, 192L);
        case 68: // 'D'
        case 100: // 'd'
          if ((l1 & 0x80000000000000L) != 0L)
            return jjStartNfaWithStates_0(3, 55, 48);
          break;
        case 69: // 'E'
        case 101: // 'e'
          if ((l1 & 0x100000L) != 0L)
            return jjStartNfaWithStates_0(3, 20, 48);
          if ((l1 & 0x4000000000L) != 0L)
            return jjStartNfaWithStates_0(3, 38, 48);
          if ((l1 & 0x80000000000L) != 0L)
            return jjStartNfaWithStates_0(3, 43, 48);
          else
            return jjMoveStringLiteralDfa4_0(l1, 0x810000e00200000L, l3, 0L);
        case 72: // 'H'
        case 104: // 'h'
          if ((l3 & 512L) != 0L)
            return jjStartNfaWithStates_0(3, 73, 48);
          break;
        case 73: // 'I'
        case 105: // 'i'
          return jjMoveStringLiteralDfa4_0(l1, 0x80000000L, l3, 0L);
        case 75: // 'K'
        case 107: // 'k'
          if ((l1 & 0x8000000000L) != 0L)
            return jjStartNfaWithStates_0(3, 39, 48);
          if ((l3 & 1024L) != 0L)
            return jjStartNfaWithStates_0(3, 74, 48);
          break;
        case 76: // 'L'
        case 108: // 'l'
          if ((l1 & 0x800000000000L) != 0L)
            return jjStartNfaWithStates_0(3, 47, 48);
          if ((l1 & 0x100000000000000L) != 0L)
            return jjStartNfaWithStates_0(3, 56, 48);
          else
            return jjMoveStringLiteralDfa4_0(l1, 0x4200000001002000L, l3, 2L);
        case 77: // 'M'
        case 109: // 'm'
          if ((l1 & 0x20000000L) != 0L)
            return jjStartNfaWithStates_0(3, 29, 48);
          else
            return jjMoveStringLiteralDfa4_0(l1, 0x30000L, l3, 0L);
        case 78: // 'N'
        case 110: // 'n'
          return jjMoveStringLiteralDfa4_0(l1, 0xc0000L, l3, 4L);
        case 79: // 'O'
        case 111: // 'o'
          if ((l1 & 0x1000000000L) != 0L)
            return jjStartNfaWithStates_0(3, 36, 48);
          else
            return jjMoveStringLiteralDfa4_0(l1, 0x20000000000000L, l3, 8L);
        case 82: // 'R'
        case 114: // 'r'
          if ((l1 & 32768L) != 0L)
            return jjStartNfaWithStates_0(3, 15, 48);
          else
            return jjMoveStringLiteralDfa4_0(l1, 0xa000000000000000L, l3, 256L);
        case 83: // 'S'
        case 115: // 's'
          return jjMoveStringLiteralDfa4_0(l1, 0x2000000L, l3, 0L);
        case 84: // 'T'
        case 116: // 't'
          if ((l1 & 0x4000000L) != 0L)
            return jjStartNfaWithStates_0(3, 26, 48);
          if ((l1 & 0x40000000000000L) != 0L)
            return jjStartNfaWithStates_0(3, 54, 48);
          else
            return jjMoveStringLiteralDfa4_0(l1, 0x800000L, l3, 2048L);
        case 85: // 'U'
        case 117: // 'u'
          return jjMoveStringLiteralDfa4_0(l1, 0x140040000000L, l3, 32L);
        case 87: // 'W'
        case 119: // 'w'
          return jjMoveStringLiteralDfa4_0(l1, 2048L, l3, 0L);
        case 89: // 'Y'
        case 121: // 'y'
          if ((l1 & 0x4000000000000L) != 0L)
            return jjStartNfaWithStates_0(3, 50, 48);
          break;
      }
      return jjStartNfa_0(2, l1, l3);
    }

    private int jjMoveStringLiteralDfa4_0(long l, long l1, long l2, long l3) {
      if (((l1 &= l) | (l3 &= l2)) == 0L)
        return jjStartNfa_0(2, l, l2);
      try {
        curChar = input_stream.readChar();
      } catch (IOException ioexception) {
        jjStopStringLiteralDfa_0(3, l1, l3);
        return 4;
      }
      switch (curChar) {
        case 68: // 'D'
        case 70: // 'F'
        case 74: // 'J'
        case 75: // 'K'
        case 77: // 'M'
        case 79: // 'O'
        case 81: // 'Q'
        case 86: // 'V'
        case 87: // 'W'
        case 88: // 'X'
        case 89: // 'Y'
        case 90: // 'SQL'
        case 91: // '['
        case 92: // '\\'
        case 93: // ']'
        case 94: // '^'
        case 95: // '_'
        case 96: // '`'
        case 97: // 'a'
        case 100: // 'd'
        case 102: // 'f'
        case 106: // 'j'
        case 107: // 'k'
        case 109: // 'm'
        case 111: // 'o'
        case 113: // 'q'
        default:
          break;
        case 66: // 'B'
        case 98: // 'b'
          return jjMoveStringLiteralDfa5_0(l1, 0x200000000000000L, l3, 0L);
        case 67: // 'C'
        case 99: // 'c'
          return jjMoveStringLiteralDfa5_0(l1, 0x800000000000000L, l3, 0L);
        case 69: // 'E'
        case 101: // 'e'
          if ((l1 & 0x2000000000000000L) != 0L)
            return jjStartNfaWithStates_0(4, 61, 48);
          if ((l3 & 2L) != 0L)
            return jjStartNfaWithStates_0(4, 65, 48);
          if ((l3 & 256L) != 0L)
            return jjStartNfaWithStates_0(4, 72, 48);
          if ((l3 & 2048L) != 0L)
            return jjStartNfaWithStates_0(4, 75, 48);
          else
            return jjMoveStringLiteralDfa5_0(l1, 0x1000000052800L, l3, 32L);
        case 71: // 'G'
        case 103: // 'g'
          return jjMoveStringLiteralDfa5_0(l1, 0x400000000L, l3, 0L);
        case 72: // 'H'
        case 104: // 'h'
          return jjMoveStringLiteralDfa5_0(l1, 0L, l3, 192L);
        case 73: // 'I'
        case 105: // 'i'
          return jjMoveStringLiteralDfa5_0(l1, 0x400000820000L, l3, 0L);
        case 76: // 'L'
        case 108: // 'l'
          return jjMoveStringLiteralDfa5_0(l1, 0x4000000000000000L, l3, 0L);
        case 78: // 'N'
        case 110: // 'n'
          if ((l3 & 8L) != 0L)
            return jjStartNfaWithStates_0(4, 67, 48);
          else
            return jjMoveStringLiteralDfa5_0(l1, 0x80000000L, l3, 0L);
        case 80: // 'P'
        case 112: // 'p'
          if ((l1 & 0x40000000L) != 0L)
            return jjStartNfaWithStates_0(4, 30, 48);
          break;
        case 82: // 'R'
        case 114: // 'r'
          if ((l1 & 0x10000000000000L) != 0L)
            return jjStartNfaWithStates_0(4, 52, 48);
          if ((l1 & 0x20000000000000L) != 0L)
            return jjStartNfaWithStates_0(4, 53, 48);
          else
            return jjMoveStringLiteralDfa5_0(l1, 0x100a00001000L, l3, 0L);
        case 83: // 'S'
        case 115: // 's'
          if ((l1 & 0x40000000000L) != 0L)
            return jjStartNfaWithStates_0(4, 42, 48);
          else
            return jjMoveStringLiteralDfa5_0(l1, 0L, l3, 4L);
        case 84: // 'T'
        case 116: // 't'
          if ((l1 & 0x80000L) != 0L)
            return jjStartNfaWithStates_0(4, 19, 48);
          if ((l1 & 0x8000000L) != 0L)
            return jjStartNfaWithStates_0(4, 27, 48);
          if ((l1 & 0x8000000000000000L) != 0L)
            return jjStartNfaWithStates_0(4, 63, 48);
          else
            return jjMoveStringLiteralDfa5_0(l1, 0x2200000L, l3, 16L);
        case 85: // 'U'
        case 117: // 'u'
          return jjMoveStringLiteralDfa5_0(l1, 0x1000000L, l3, 0L);
      }
      return jjStartNfa_0(3, l1, l3);
    }

    private int jjMoveStringLiteralDfa5_0(long l, long l1, long l2, long l3) {
      if (((l1 &= l) | (l3 &= l2)) == 0L)
        return jjStartNfa_0(3, l, l2);
      try {
        curChar = input_stream.readChar();
      } catch (IOException ioexception) {
        jjStopStringLiteralDfa_0(4, l1, l3);
        return 5;
      }
      switch (curChar) {
        case 66: // 'B'
        case 68: // 'D'
        case 70: // 'F'
        case 72: // 'H'
        case 74: // 'J'
        case 75: // 'K'
        case 76: // 'L'
        case 77: // 'M'
        case 79: // 'O'
        case 80: // 'P'
        case 81: // 'Q'
        case 85: // 'U'
        case 86: // 'V'
        case 87: // 'W'
        case 88: // 'X'
        case 90: // 'SQL'
        case 91: // '['
        case 92: // '\\'
        case 93: // ']'
        case 94: // '^'
        case 95: // '_'
        case 96: // '`'
        case 98: // 'b'
        case 100: // 'd'
        case 102: // 'f'
        case 104: // 'h'
        case 106: // 'j'
        case 107: // 'k'
        case 108: // 'l'
        case 109: // 'm'
        case 111: // 'o'
        case 112: // 'p'
        case 113: // 'q'
        case 117: // 'u'
        case 118: // 'v'
        case 119: // 'w'
        case 120: // 'x'
        default:
          break;
        case 65: // 'A'
        case 97: // 'a'
          return jjMoveStringLiteralDfa6_0(l1, 0x200100000002000L, l3, 196L);
        case 67: // 'C'
        case 99: // 'c'
          return jjMoveStringLiteralDfa6_0(l1, 0x40000L, l3, 0L);
        case 69: // 'E'
        case 101: // 'e'
          if ((l1 & 0x200000L) != 0L)
            return jjStartNfaWithStates_0(5, 21, 48);
          if ((l3 & 16L) != 0L)
            return jjStartNfaWithStates_0(5, 68, 48);
          else
            return jjMoveStringLiteralDfa6_0(l1, 0x400000800L, l3, 0L);
        case 71: // 'G'
        case 103: // 'g'
          if ((l1 & 0x80000000L) != 0L)
            return jjStartNfaWithStates_0(5, 31, 48);
          break;
        case 73: // 'I'
        case 105: // 'i'
          return jjMoveStringLiteralDfa6_0(l1, 0x4000000000000000L, l3, 0L);
        case 78: // 'N'
        case 110: // 'n'
          return jjMoveStringLiteralDfa6_0(l1, 0x810000L, l3, 0L);
        case 82: // 'R'
        case 114: // 'r'
          if ((l1 & 0x1000000000000L) != 0L)
            return jjStartNfaWithStates_0(5, 48, 48);
          break;
        case 83: // 'S'
        case 115: // 's'
          if ((l1 & 0x2000000L) != 0L)
            return jjStartNfaWithStates_0(5, 25, 48);
          if ((l3 & 32L) != 0L)
            return jjStartNfaWithStates_0(5, 69, 48);
          else
            return jjMoveStringLiteralDfa6_0(l1, 0x801000000L, l3, 0L);
        case 84: // 'T'
        case 116: // 't'
          if ((l1 & 0x20000L) != 0L)
            return jjStartNfaWithStates_0(5, 17, 48);
          if ((l1 & 0x200000000L) != 0L)
            return jjStartNfaWithStates_0(5, 33, 48);
          if ((l1 & 0x400000000000L) != 0L)
            return jjStartNfaWithStates_0(5, 46, 48);
          if ((l1 & 0x800000000000000L) != 0L)
            return jjStartNfaWithStates_0(5, 59, 48);
          break;
        case 89: // 'Y'
        case 121: // 'y'
          return jjMoveStringLiteralDfa6_0(l1, 4096L, l3, 0L);
      }
      return jjStartNfa_0(4, l1, l3);
    }

    private int jjMoveStringLiteralDfa6_0(long l, long l1, long l2, long l3) {
      if (((l1 &= l) | (l3 &= l2)) == 0L)
        return jjStartNfa_0(4, l, l2);
      try {
        curChar = input_stream.readChar();
      } catch (IOException ioexception) {
        jjStopStringLiteralDfa_0(5, l1, l3);
        return 6;
      }
      switch (curChar) {
        case 68: // 'D'
        case 70: // 'F'
        case 71: // 'G'
        case 72: // 'H'
        case 74: // 'J'
        case 75: // 'K'
        case 77: // 'M'
        case 79: // 'O'
        case 80: // 'P'
        case 81: // 'Q'
        case 83: // 'S'
        case 85: // 'U'
        case 86: // 'V'
        case 87: // 'W'
        case 88: // 'X'
        case 89: // 'Y'
        case 90: // 'SQL'
        case 91: // '['
        case 92: // '\\'
        case 93: // ']'
        case 94: // '^'
        case 96: // '`'
        case 97: // 'a'
        case 98: // 'b'
        case 100: // 'd'
        case 102: // 'f'
        case 103: // 'g'
        case 104: // 'h'
        case 106: // 'j'
        case 107: // 'k'
        case 109: // 'm'
        case 111: // 'o'
        case 112: // 'p'
        case 113: // 'q'
        case 115: // 's'
        default:
          break;
        case 95: // '_'
          return jjMoveStringLiteralDfa7_0(l1, 4096L, l3, 0L);
        case 67: // 'C'
        case 99: // 'c'
          return jjMoveStringLiteralDfa7_0(l1, 0x200000000800000L, l3, 4L);
        case 69: // 'E'
        case 101: // 'e'
          return jjMoveStringLiteralDfa7_0(l1, 0x800000000L, l3, 0L);
        case 73: // 'I'
        case 105: // 'i'
          return jjMoveStringLiteralDfa7_0(l1, 0x1000000L, l3, 0L);
        case 76: // 'L'
        case 108: // 'l'
          if ((l1 & 0x100000000000L) != 0L)
            return jjStartNfaWithStates_0(6, 44, 48);
          break;
        case 78: // 'N'
        case 110: // 'n'
          if ((l1 & 2048L) != 0L)
            return jjStartNfaWithStates_0(6, 11, 48);
          if ((l1 & 8192L) != 0L)
            return jjStartNfaWithStates_0(6, 13, 48);
          else
            return jjMoveStringLiteralDfa7_0(l1, 0x4000000000000000L, l3, 0L);
        case 82: // 'R'
        case 114: // 'r'
          if ((l1 & 0x400000000L) != 0L)
            return jjStartNfaWithStates_0(6, 34, 48);
          if ((l3 & 128L) != 0L) {
            jjmatchedKind = 71;
            jjmatchedPos = 6;
          }
          return jjMoveStringLiteralDfa7_0(l1, 0L, l3, 64L);
        case 84: // 'T'
        case 116: // 't'
          if ((l1 & 0x10000L) != 0L)
            return jjStartNfaWithStates_0(6, 16, 48);
          if ((l1 & 0x40000L) != 0L)
            return jjStartNfaWithStates_0(6, 18, 48);
          break;
      }
      return jjStartNfa_0(5, l1, l3);
    }

    private int jjMoveStringLiteralDfa7_0(long l, long l1, long l2, long l3) {
      if (((l1 &= l) | (l3 &= l2)) == 0L)
        return jjStartNfa_0(5, l, l2);
      try {
        curChar = input_stream.readChar();
      } catch (IOException ioexception) {
        jjStopStringLiteralDfa_0(6, l1, l3);
        return 7;
      }
      switch (curChar) {
        default:
          break;
        case 50: // '2'
          if ((l3 & 64L) != 0L)
            return jjStartNfaWithStates_0(7, 70, 11);
          break;
        case 67: // 'C'
        case 99: // 'c'
          return jjMoveStringLiteralDfa8_0(l1, 0x800000000L, l3, 0L);
        case 73: // 'I'
        case 105: // 'i'
          return jjMoveStringLiteralDfa8_0(l1, 4096L, l3, 0L);
        case 75: // 'K'
        case 107: // 'k'
          if ((l1 & 0x200000000000000L) != 0L)
            return jjStartNfaWithStates_0(7, 57, 48);
          break;
        case 84: // 'T'
        case 116: // 't'
          if ((l1 & 0x800000L) != 0L)
            return jjStartNfaWithStates_0(7, 23, 48);
          if ((l1 & 0x4000000000000000L) != 0L)
            return jjStartNfaWithStates_0(7, 62, 48);
          else
            return jjMoveStringLiteralDfa8_0(l1, 0L, l3, 4L);
        case 86: // 'V'
        case 118: // 'v'
          return jjMoveStringLiteralDfa8_0(l1, 0x1000000L, l3, 0L);
      }
      return jjStartNfa_0(6, l1, l3);
    }

    private int jjMoveStringLiteralDfa8_0(long l, long l1, long l2, long l3) {
      if (((l1 &= l) | (l3 &= l2)) == 0L)
        return jjStartNfa_0(6, l, l2);
      try {
        curChar = input_stream.readChar();
      } catch (IOException ioexception) {
        jjStopStringLiteralDfa_0(7, l1, l3);
        return 8;
      }
      switch (curChar) {
        default:
          break;
        case 69: // 'E'
        case 101: // 'e'
          if ((l1 & 0x1000000L) != 0L)
            return jjStartNfaWithStates_0(8, 24, 48);
          break;
        case 73: // 'I'
        case 105: // 'i'
          return jjMoveStringLiteralDfa9_0(l1, 0L, l3, 4L);
        case 78: // 'N'
        case 110: // 'n'
          return jjMoveStringLiteralDfa9_0(l1, 4096L, l3, 0L);
        case 84: // 'T'
        case 116: // 't'
          if ((l1 & 0x800000000L) != 0L)
            return jjStartNfaWithStates_0(8, 35, 48);
          break;
      }
      return jjStartNfa_0(7, l1, l3);
    }

    private int jjMoveStringLiteralDfa9_0(long l, long l1, long l2, long l3) {
      if (((l1 &= l) | (l3 &= l2)) == 0L)
        return jjStartNfa_0(7, l, l2);
      try {
        curChar = input_stream.readChar();
      } catch (IOException ioexception) {
        jjStopStringLiteralDfa_0(8, l1, l3);
        return 9;
      }
      switch (curChar) {
        case 79: // 'O'
        case 111: // 'o'
          return jjMoveStringLiteralDfa10_0(l1, 0L, l3, 4L);
        case 84: // 'T'
        case 116: // 't'
          return jjMoveStringLiteralDfa10_0(l1, 4096L, l3, 0L);
      }
      return jjStartNfa_0(8, l1, l3);
    }

    private int jjMoveStringLiteralDfa10_0(long l, long l1, long l2, long l3) {
      if (((l1 &= l) | (l3 &= l2)) == 0L)
        return jjStartNfa_0(8, l, l2);
      try {
        curChar = input_stream.readChar();
      } catch (IOException ioexception) {
        jjStopStringLiteralDfa_0(9, l1, l3);
        return 10;
      }
      switch (curChar) {
        case 69: // 'E'
        case 101: // 'e'
          return jjMoveStringLiteralDfa11_0(l1, 4096L, l3, 0L);
        case 78: // 'N'
        case 110: // 'n'
          if ((l3 & 4L) != 0L)
            return jjStartNfaWithStates_0(10, 66, 48);
          break;
      }
      return jjStartNfa_0(9, l1, l3);
    }

    private int jjMoveStringLiteralDfa11_0(long l, long l1, long l2, long l3) {
      if (((l1 &= l) | (l3 &= l2)) == 0L)
        return jjStartNfa_0(9, l, l2);
      try {
        curChar = input_stream.readChar();
      } catch (IOException ioexception) {
        jjStopStringLiteralDfa_0(10, l1, 0L);
        return 11;
      }
      switch (curChar) {
        case 71: // 'G'
        case 103: // 'g'
          return jjMoveStringLiteralDfa12_0(l1, 4096L);
      }
      return jjStartNfa_0(10, l1, 0L);
    }

    private int jjMoveStringLiteralDfa12_0(long l, long l1) {
      if ((l1 &= l) == 0L)
        return jjStartNfa_0(10, l, 0L);
      try {
        curChar = input_stream.readChar();
      } catch (IOException ioexception) {
        jjStopStringLiteralDfa_0(11, l1, 0L);
        return 12;
      }
      switch (curChar) {
        case 69: // 'E'
        case 101: // 'e'
          return jjMoveStringLiteralDfa13_0(l1, 4096L);
      }
      return jjStartNfa_0(11, l1, 0L);
    }

    private int jjMoveStringLiteralDfa13_0(long l, long l1) {
      if ((l1 &= l) == 0L)
        return jjStartNfa_0(11, l, 0L);
      try {
        curChar = input_stream.readChar();
      } catch (IOException ioexception) {
        jjStopStringLiteralDfa_0(12, l1, 0L);
        return 13;
      }
      switch (curChar) {
        case 82: // 'R'
        case 114: // 'r'
          if ((l1 & 4096L) != 0L)
            return jjStartNfaWithStates_0(13, 12, 11);
          break;
      }
      return jjStartNfa_0(12, l1, 0L);
    }

    private void jjCheckNAdd(int i) {
      if (jjrounds[i] != jjround) {
        jjstateSet[jjnewStateCnt++] = i;
        jjrounds[i] = jjround;
      }
    }

    private void jjAddStates(int i, int j) {
      do
        jjstateSet[jjnewStateCnt++] = jjnextStates[i];
      while (i++ != j);
    }

    private void jjCheckNAddTwoStates(int i, int j) {
      jjCheckNAdd(i);
      jjCheckNAdd(j);
    }

    private void jjCheckNAddStates(int i, int j) {
      do
        jjCheckNAdd(jjnextStates[i]);
      while (i++ != j);
    }

    private void jjCheckNAddStates(int i) {
      jjCheckNAdd(jjnextStates[i]);
      jjCheckNAdd(jjnextStates[i + 1]);
    }

    private int jjMoveNfa_0(int i, int j) {
      int k = 0;
      jjnewStateCnt = 47;
      int l = 1;
      jjstateSet[0] = i;
      int i1 = 0x7fffffff;
      do {
        if (++jjround == 0x7fffffff)
          ReInitRounds();
        if (curChar < '@') {
          long l1 = 1L << curChar;
          do
            switch (jjstateSet[--l]) {
              case 2: // '\002'
                if ((0x3ff000000000000L & l1) != 0L) {
                  if (i1 > 76)
                    i1 = 76;
                  jjCheckNAddStates(0, 6);
                } else if (curChar == '.')
                  jjCheckNAddTwoStates(27, 37);
                else if (curChar == '"')
                  jjCheckNAddTwoStates(24, 25);
                else if (curChar == '\'')
                  jjCheckNAddTwoStates(19, 20);
                else if (curChar == ':')
                  jjstateSet[jjnewStateCnt++] = 13;
                else if (curChar == '/')
                  jjstateSet[jjnewStateCnt++] = 3;
                else if (curChar == '-')
                  jjstateSet[jjnewStateCnt++] = 0;
                break;
              case 11: // '\013'
              case 48: // '0'
                if ((0x3ff001000000000L & l1) != 0L) {
                  if (i1 > 82)
                    i1 = 82;
                  jjCheckNAdd(11);
                }
                break;
              case 47: // '/'
                if ((0x3ff000000000000L & l1) != 0L) {
                  if (i1 > 76)
                    i1 = 76;
                  jjCheckNAdd(37);
                }
                if ((0x3ff000000000000L & l1) != 0L) {
                  if (i1 > 76)
                    i1 = 76;
                  jjCheckNAddTwoStates(27, 28);
                }
                break;
              case 0: // '\0'
                if (curChar == '-') {
                  if (i1 > 80)
                    i1 = 80;
                  jjCheckNAdd(1);
                }
                break;
              case 1: // '\001'
                if ((-9217L & l1) != 0L) {
                  if (i1 > 80)
                    i1 = 80;
                  jjCheckNAdd(1);
                }
                break;
              case 3: // '\003'
                if (curChar == '*')
                  jjCheckNAddTwoStates(4, 5);
                break;
              case 4: // '\004'
                if ((0xfffffbffffffffffL & l1) != 0L)
                  jjCheckNAddTwoStates(4, 5);
                break;
              case 5: // '\005'
                if (curChar == '*')
                  jjCheckNAddStates(7, 9);
                break;
              case 6: // '\006'
                if ((0xffff7bffffffffffL & l1) != 0L)
                  jjCheckNAddTwoStates(7, 5);
                break;
              case 7: // '\007'
                if ((0xfffffbffffffffffL & l1) != 0L)
                  jjCheckNAddTwoStates(7, 5);
                break;
              case 8: // '\b'
                if (curChar == '/' && i1 > 81)
                  i1 = 81;
                break;
              case 9: // '\t'
                if (curChar == '/')
                  jjstateSet[jjnewStateCnt++] = 3;
                break;
              case 12: // '\f'
                if (curChar == ':')
                  jjstateSet[jjnewStateCnt++] = 13;
                break;
              case 14: // '\016'
                if ((0x3ff001000000000L & l1) != 0L) {
                  if (i1 > 85)
                    i1 = 85;
                  jjAddStates(10, 11);
                }
                break;
              case 15: // '\017'
                if (curChar == '.')
                  jjstateSet[jjnewStateCnt++] = 16;
                break;
              case 17: // '\021'
                if ((0x3ff001000000000L & l1) != 0L) {
                  if (i1 > 85)
                    i1 = 85;
                  jjstateSet[jjnewStateCnt++] = 17;
                }
                break;
              case 18: // '\022'
                if (curChar == '\'')
                  jjCheckNAddTwoStates(19, 20);
                break;
              case 19: // '\023'
                if ((0xffffff7fffffffffL & l1) != 0L)
                  jjCheckNAddTwoStates(19, 20);
                break;
              case 20: // '\024'
                if (curChar == '\'') {
                  if (i1 > 86)
                    i1 = 86;
                  jjstateSet[jjnewStateCnt++] = 21;
                }
                break;
              case 21: // '\025'
                if (curChar == '\'')
                  jjCheckNAddTwoStates(22, 20);
                break;
              case 22: // '\026'
                if ((0xffffff7fffffffffL & l1) != 0L)
                  jjCheckNAddTwoStates(22, 20);
                break;
              case 23: // '\027'
                if (curChar == '"')
                  jjCheckNAddTwoStates(24, 25);
                break;
              case 24: // '\030'
                if ((0xfffffffbffffdbffL & l1) != 0L)
                  jjCheckNAddTwoStates(24, 25);
                break;
              case 25: // '\031'
                if (curChar == '"' && i1 > 87)
                  i1 = 87;
                break;
              case 26: // '\032'
                if (curChar == '.')
                  jjCheckNAddTwoStates(27, 37);
                break;
              case 27: // '\033'
                if ((0x3ff000000000000L & l1) != 0L) {
                  if (i1 > 76)
                    i1 = 76;
                  jjCheckNAddTwoStates(27, 28);
                }
                break;
              case 29: // '\035'
                if ((0x280000000000L & l1) != 0L)
                  jjAddStates(12, 13);
                break;
              case 30: // '\036'
                if (curChar == '.')
                  jjCheckNAdd(31);
                break;
              case 31: // '\037'
                if ((0x3ff000000000000L & l1) != 0L) {
                  if (i1 > 76)
                    i1 = 76;
                  jjCheckNAdd(31);
                }
                break;
              case 32: // ' '
                if ((0x3ff000000000000L & l1) != 0L) {
                  if (i1 > 76)
                    i1 = 76;
                  jjCheckNAddStates(14, 16);
                }
                break;
              case 33: // '!'
                if ((0x3ff000000000000L & l1) != 0L) {
                  if (i1 > 76)
                    i1 = 76;
                  jjCheckNAdd(33);
                }
                break;
              case 34: // '"'
                if ((0x3ff000000000000L & l1) != 0L) {
                  if (i1 > 76)
                    i1 = 76;
                  jjCheckNAddTwoStates(34, 35);
                }
                break;
              case 35: // '#'
                if (curChar == '.')
                  jjCheckNAdd(36);
                break;
              case 36: // '$'
                if ((0x3ff000000000000L & l1) != 0L) {
                  if (i1 > 76)
                    i1 = 76;
                  jjCheckNAdd(36);
                }
                break;
              case 37: // '%'
                if ((0x3ff000000000000L & l1) != 0L) {
                  if (i1 > 76)
                    i1 = 76;
                  jjCheckNAdd(37);
                }
                break;
              case 38: // '&'
                if ((0x3ff000000000000L & l1) != 0L) {
                  if (i1 > 76)
                    i1 = 76;
                  jjCheckNAddStates(0, 6);
                }
                break;
              case 39: // '\''
                if ((0x3ff000000000000L & l1) != 0L) {
                  if (i1 > 76)
                    i1 = 76;
                  jjCheckNAddTwoStates(39, 28);
                }
                break;
              case 40: // '('
                if ((0x3ff000000000000L & l1) != 0L) {
                  if (i1 > 76)
                    i1 = 76;
                  jjCheckNAddStates(17, 19);
                }
                break;
              case 41: // ')'
                if (curChar == '.')
                  jjCheckNAdd(42);
                break;
              case 42: // '*'
                if ((0x3ff000000000000L & l1) != 0L) {
                  if (i1 > 76)
                    i1 = 76;
                  jjCheckNAddTwoStates(42, 28);
                }
                break;
              case 43: // '+'
                if ((0x3ff000000000000L & l1) != 0L) {
                  if (i1 > 76)
                    i1 = 76;
                  jjCheckNAddTwoStates(43, 44);
                }
                break;
              case 44: // ','
                if (curChar == '.')
                  jjCheckNAdd(45);
                break;
              case 45: // '-'
                if ((0x3ff000000000000L & l1) != 0L) {
                  if (i1 > 76)
                    i1 = 76;
                  jjCheckNAdd(45);
                }
                break;
              case 46: // '.'
                if ((0x3ff000000000000L & l1) != 0L) {
                  if (i1 > 76)
                    i1 = 76;
                  jjCheckNAdd(46);
                }
                break;
            }
          while (l != k);
        } else if (curChar < '\200') {
          long l2 = 1L << (curChar & 0x3f);
          do
            switch (jjstateSet[--l]) {
              case 2: // '\002'
              case 10: // '\n'
                if ((0x7fffffe07fffffeL & l2) != 0L) {
                  if (i1 > 82)
                    i1 = 82;
                  jjCheckNAddTwoStates(10, 11);
                }
                break;
              case 48: // '0'
                if ((0x7fffffe87fffffeL & l2) != 0L) {
                  if (i1 > 82)
                    i1 = 82;
                  jjCheckNAdd(11);
                }
                if ((0x7fffffe07fffffeL & l2) != 0L) {
                  if (i1 > 82)
                    i1 = 82;
                  jjCheckNAddTwoStates(10, 11);
                }
                break;
              case 1: // '\001'
                if (i1 > 80)
                  i1 = 80;
                jjstateSet[jjnewStateCnt++] = 1;
                break;
              case 4: // '\004'
                jjCheckNAddTwoStates(4, 5);
                break;
              case 6: // '\006'
              case 7: // '\007'
                jjCheckNAddTwoStates(7, 5);
                break;
              case 11: // '\013'
                if ((0x7fffffe87fffffeL & l2) != 0L) {
                  if (i1 > 82)
                    i1 = 82;
                  jjCheckNAdd(11);
                }
                break;
              case 13: // '\r'
                if ((0x7fffffe07fffffeL & l2) != 0L) {
                  if (i1 > 85)
                    i1 = 85;
                  jjCheckNAddStates(20, 22);
                }
                break;
              case 14: // '\016'
                if ((0x7fffffe87fffffeL & l2) != 0L) {
                  if (i1 > 85)
                    i1 = 85;
                  jjCheckNAddTwoStates(14, 15);
                }
                break;
              case 16: // '\020'
                if ((0x7fffffe07fffffeL & l2) != 0L) {
                  if (i1 > 85)
                    i1 = 85;
                  jjCheckNAddTwoStates(16, 17);
                }
                break;
              case 17: // '\021'
                if ((0x7fffffe87fffffeL & l2) != 0L) {
                  if (i1 > 85)
                    i1 = 85;
                  jjCheckNAdd(17);
                }
                break;
              case 19: // '\023'
                jjCheckNAddTwoStates(19, 20);
                break;
              case 22: // '\026'
                jjCheckNAddTwoStates(22, 20);
                break;
              case 24: // '\030'
                jjAddStates(23, 24);
                break;
              case 28: // '\034'
                if ((0x2000000020L & l2) != 0L)
                  jjAddStates(25, 27);
                break;
            }
          while (l != k);
        } else {
          int j1 = (curChar & 0xff) >> 6;
          long l3 = 1L << (curChar & 0x3f);
          do
            switch (jjstateSet[--l]) {
              case 1: // '\001'
                if ((jjbitVec0[j1] & l3) != 0L) {
                  if (i1 > 80)
                    i1 = 80;
                  jjstateSet[jjnewStateCnt++] = 1;
                }
                break;
              case 4: // '\004'
                if ((jjbitVec0[j1] & l3) != 0L)
                  jjCheckNAddTwoStates(4, 5);
                break;
              case 6: // '\006'
              case 7: // '\007'
                if ((jjbitVec0[j1] & l3) != 0L)
                  jjCheckNAddTwoStates(7, 5);
                break;
              case 19: // '\023'
                if ((jjbitVec0[j1] & l3) != 0L)
                  jjCheckNAddTwoStates(19, 20);
                break;
              case 22: // '\026'
                if ((jjbitVec0[j1] & l3) != 0L)
                  jjCheckNAddTwoStates(22, 20);
                break;
              case 24: // '\030'
                if ((jjbitVec0[j1] & l3) != 0L)
                  jjAddStates(23, 24);
                break;
            }
          while (l != k);
        }
        if (i1 != 0x7fffffff) {
          jjmatchedKind = i1;
          jjmatchedPos = j;
          i1 = 0x7fffffff;
        }
        j++;
        if ((l = jjnewStateCnt) == (k = 47 - (jjnewStateCnt = k)))
          return j;
        try {
          curChar = input_stream.readChar();
        } catch (IOException ioexception) {
          return j;
        }
      } while (true);
    }

    public void setSQLJJParserTokenManager(RuleCharStream RuleCharStream) {
      debugStream = System.out;
      jjrounds = new int[47];
      jjstateSet = new int[94];
      curLexState = 0;
      defaultLexState = 0;
      input_stream = RuleCharStream;
    }

    public void setSQLJJParserTokenManager(RuleCharStream RuleCharStream, int i) {
      setSQLJJParserTokenManager(RuleCharStream);
      SwitchTo(i);
    }

    public void ReInit(RuleCharStream RuleCharStream) {
      jjmatchedPos = jjnewStateCnt = 0;
      curLexState = defaultLexState;
      input_stream = RuleCharStream;
      ReInitRounds();
    }

    private void ReInitRounds() {
      jjround = 0x80000001;
      for (int i = 47; i-- > 0; )
        jjrounds[i] = 0x80000000;
    }

    public void ReInit(RuleCharStream RuleCharStream, int i) {
      ReInit(RuleCharStream);
      SwitchTo(i);
    }

    public void SwitchTo(int i) {
      if (i >= 1 || i < 0) {
        throw new TokenMgrError("Error: Ignoring invalid lexical state : " + i + ". State unchanged.", 2);
      } else {
        curLexState = i;
        return;
      }
    }

    private Token jjFillToken() {
      Token token = newToken(jjmatchedKind);
      token.kind = jjmatchedKind;
      String s = jjstrLiteralImages[jjmatchedKind];
      token.image = s != null ? s : input_stream.GetImage();
      token.beginLine = input_stream.getBeginLine();
      token.beginColumn = input_stream.getBeginColumn();
      token.endLine = input_stream.getEndLine();
      token.endColumn = input_stream.getEndColumn();
      return token;
    }

    public Token getNextTokenFromManager() throws ParseException {
      Token token = null;
      int i = 0;
      do {
        try {
          curChar = input_stream.BeginToken();
        } catch (IOException ioexception) {
          jjmatchedKind = 0;
          Token token1 = jjFillToken();
          token1.specialToken = token;
          return token1;
        }
        try {
          input_stream.backup(0);
          for (; curChar <= ' ' && (0x100002600L & 1L << curChar) != 0L; curChar = input_stream.BeginToken())
            ;
        } catch (IOException ioexception1) {
          continue;
        }
        jjmatchedKind = 0x7fffffff;
        jjmatchedPos = 0;
        i = jjMoveStringLiteralDfa0_0();
        if (jjmatchedKind == 0x7fffffff)
          break;
        if (jjmatchedPos + 1 < i)
          input_stream.backup(i - jjmatchedPos - 1);
        if ((jjtoToken[jjmatchedKind >> 6] & 1L << (jjmatchedKind & 0x3f)) != 0L) {
          Token token2 = jjFillToken();
          token2.specialToken = token;
          return token2;
        }
        if ((jjtoSpecial[jjmatchedKind >> 6] & 1L << (jjmatchedKind & 0x3f)) != 0L) {
          Token token3 = jjFillToken();
          if (token == null) {
            token = token3;
          } else {
            token3.specialToken = token;
            token = token.next = token3;
          }
        }
      } while (true);
      int j = input_stream.getEndLine();
      int k = input_stream.getEndColumn();
      String s = null;
      boolean flag = false;
      try {
        input_stream.readChar();
        input_stream.backup(1);
      } catch (IOException ioexception2) {
        flag = true;
        s = i > 1 ? input_stream.GetImage() : "";
        if (curChar == '\n' || curChar == '\r') {
          j++;
          k = 0;
        } else {
          k++;
        }
      }
      if (!flag) {
        input_stream.backup(1);
        s = i > 1 ? input_stream.GetImage() : "";
      }
      throw new ParseException(flag, curLexState, j, k, s, curChar, 0);
    }
    public PrintStream debugStream;
    long jjbitVec0[] = { 0L, 0L, -1L, -1L };
    int jjnextStates[] = { 39, 40, 41, 28, 43, 44, 46, 5, 6, 8, 14, 15, 30, 32, 33, 34, 35, 40, 41, 28, 13, 14, 15, 24, 25, 29, 30, 32 };
    public String jjstrLiteralImages[] = { "", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "(", ",", ")", ";", "=", ".", "!=", "#", "<>", ">", ">=", "<", "<=", "+", "-", "*", ".*", "?", "||", "/", "**" };
    public String lexStateNames[] = { "DEFAULT" };
    long jjtoToken[] = { -31L, 0x1fffffe41fffL };
    long jjtoSkip[] = { 30L, 0x30000L };
    long jjtoSpecial[] = { 0L, 0x30000L };
    private RuleCharStream input_stream;
    private int jjrounds[];
    private int jjstateSet[];
    protected char curChar;
    int curLexState;
    int defaultLexState;
    int jjnewStateCnt;
    int jjround;
    int jjmatchedPos;
    int jjmatchedKind;
  }

  public class ParseException extends Exception {
  	 protected boolean specialConstructor;
     public Token currentToken;
     public int expectedTokenSequences[][];
     public String tokenImage[];
     protected String eol; 
     static final int LEXICAL_ERROR = 0;
     static final int STATIC_LEXER_ERROR = 1;
     static final int INVALID_LEXICAL_STATE = 2;
     static final int LOOP_DETECTED = 3;
     private int lineNo, colNo;
    

    public ParseException(boolean flag, int i, int j, int k, String s, char c, int l) {
      // this(LexicalError(flag, i, j, k, s, c), l);
    	//private String LexicalError(boolean flag, int i, int j, int k, String s, char c) {
      //System.out.println("Lexical error at line " + j + ", column " + k + ".  Encountered: " + (flag ? "<EOF> " : "\"" + "\"" + " (" + (int)c + "), ") + "after : \"" + "\"");
      lineNo=i;
      colNo=j;
    }
   
    int errorCode;
 	
  	
  	
    public ParseException(Token token, int[][] ai, String[] as) {
      super("");
      eol = System.getProperty("line.separator", "\n");
      specialConstructor = true;
      currentToken = token;
      expectedTokenSequences = ai;
      tokenImage = as;
    }

    public ParseException() {
      eol = System.getProperty("line.separator", "\n");
      specialConstructor = false;
    }

    public ParseException(String s) {
      super(s);
      eol = System.getProperty("line.separator", "\n");
      specialConstructor = false;
    }

    public String getMessage() {
      if (!specialConstructor)
        return super.getMessage();
      String s = "";
      int i = 0;
      for (int j = 0; j < expectedTokenSequences.length; j++) {
        if (i < expectedTokenSequences[j].length)
          i = expectedTokenSequences[j].length;
        for (int k = 0; k < expectedTokenSequences[j].length; k++)
          s = s + tokenImage[expectedTokenSequences[j][k]] + " ";
        if (expectedTokenSequences[j][expectedTokenSequences[j].length - 1] != 0)
          s = s + "...";
        s = s + eol + "    ";
      }
      String s1 = "Encountered \"";
      Token token = currentToken.next;
      for (int l = 0; l < i; l++) {
        if (l != 0)
          s1 = s1 + " ";
        if (token.kind == 0) {
          s1 = s1 + tokenImage[0];
          break;
        }
        s1 = s1 + add_escapes(token.image);
        token = token.next;
      }
      s1 = s1 + "\" at line " + currentToken.next.beginLine + ", column " + currentToken.next.beginColumn;
      s1 = s1 + "." + eol;
      if (expectedTokenSequences.length == 1)
        s1 = s1 + "Was expecting:" + eol + "    ";
      else
        s1 = s1 + "Was expecting one of:" + eol + "    ";
      s1 = s1 + s;
      return s1;
    }
    
    
    public String getOneLineMessage() {
      if (!specialConstructor)
        return super.getMessage();
      String s = "";
      int i = 0;
      String s1 = "Encountered \"";
      Token token = currentToken.next;
      for (int l = 0; l < i; l++) {
        if (l != 0)
          s1 = s1 + " ";
        if (token.kind == 0) {
          s1 = s1 + tokenImage[0];
          break;
        }
        s1 = s1 + add_escapes(token.image);
        token = token.next;
      }
      s1 = s1 + " at " + currentToken.next.beginColumn + " position.";
      s1 = JWebUtils.replaceStr(s1, SINGLE_QUOTE,"'");
      s1 = JWebUtils.replaceStr(s1, NO_QUOTE,"");
      return s1;
    }

    protected String add_escapes(String s) {
      StringBuffer stringbuffer = new StringBuffer();
      for (int i = 0; i < s.length(); i++) {
        char c;
        switch (s.charAt(i)) {
          case 0: // '\0'
            break;
          case 8: // '\b'
            stringbuffer.append("\\b");
            break;
          case 9: // '\t'
            stringbuffer.append("\\t");
            break;
          case 10: // '\n'
            stringbuffer.append("\\n");
            break;
          case 12: // '\f'
            stringbuffer.append("\\f");
            break;
          case 13: // '\r'
            stringbuffer.append("\\r");
            break;
          case 34: // '"'
            stringbuffer.append("\\\"");
            break;
          case 39: // '\''
            stringbuffer.append("\\'");
            break;
          case 92: // '\\'
            stringbuffer.append("\\\\");
            break;
          default:
            if ((c = s.charAt(i)) < ' ' || c > '~') {
              String s1 = "0000" + Integer.toString(c, 16);
              stringbuffer.append("\\u" + s1.substring(s1.length() - 4, s1.length()));
            } else {
              stringbuffer.append(c);
            }
            break;
        }
      }
      return stringbuffer.toString();
    }
   
  }

  public class Token {

    public Token() {
    }

    public final String toString() {
      return image;
    }
    public int kind;
    public int beginLine;
    public int beginColumn;
    public int endLine;
    public int endColumn;
    public String image;
    public Token next;
    public Token specialToken;
  }

  public Token newToken(int i) {
    switch (i) {
      default:
        return new Token();
    }
  }

  public class TokenMgrError extends Error {

    protected String addEscapes(String s) {
      StringBuffer stringbuffer = new StringBuffer();
      for (int i = 0; i < s.length(); i++) {
        char c;
        switch (s.charAt(i)) {
          case 0: // '\0'
            break;
          case 8: // '\b'
            stringbuffer.append("\\b");
            break;
          case 9: // '\t'
            stringbuffer.append("\\t");
            break;
          case 10: // '\n'
            stringbuffer.append("\\n");
            break;
          case 12: // '\f'
            stringbuffer.append("\\f");
            break;
          case 13: // '\r'
            stringbuffer.append("\\r");
            break;
          case 34: // '"'
            stringbuffer.append("\\\"");
            break;
          case 39: // '\''
            stringbuffer.append("\\'");
            break;
          case 92: // '\\'
            stringbuffer.append("\\\\");
            break;
          default:
            if ((c = s.charAt(i)) < ' ' || c > '~') {
              String s1 = "0000" + Integer.toString(c, 16);
              stringbuffer.append("\\u" + s1.substring(s1.length() - 4, s1.length()));
            } else {
              stringbuffer.append(c);
            }
            break;
        }
      }
      return stringbuffer.toString();
    }

    private String LexicalError(boolean flag, int i, int j, int k, String s, char c) {
      return "Lexical error at line " + j + ", column " + k + ".  Encountered: " + (flag ? "<EOF> " : "\"" + addEscapes(String.valueOf(c)) + "\"" + " (" + (int)c + "), ") + "after : \"" + addEscapes(s) + "\"";
    }

    public String getMessage() {
      return super.getMessage();
    }

    public TokenMgrError() {
    }

    public TokenMgrError(String s, int i) {
      super(s);
      errorCode = i;
    }

    public TokenMgrError(boolean flag, int i, int j, int k, String s, char c, int l) {
      // this(LexicalError(flag, i, j, k, s, c), l);
    }
    static final int LEXICAL_ERROR = 0;
    static final int STATIC_LEXER_ERROR = 1;
    static final int INVALID_LEXICAL_STATE = 2;
    static final int LOOP_DETECTED = 3;
    int errorCode;
  }

  public final class RuleCharStream {

    private final void ExpandBuff(boolean flag) {
      char ac[] = new char[bufsize + 2048];
      int ai[] = new int[bufsize + 2048];
      int ai1[] = new int[bufsize + 2048];
      try {
        if (flag) {
          System.arraycopy(buffer, tokenBegin, ac, 0, bufsize - tokenBegin);
          System.arraycopy(buffer, 0, ac, bufsize - tokenBegin, bufpos);
          buffer = ac;
          System.arraycopy(bufline, tokenBegin, ai, 0, bufsize - tokenBegin);
          System.arraycopy(bufline, 0, ai, bufsize - tokenBegin, bufpos);
          bufline = ai;
          System.arraycopy(bufcolumn, tokenBegin, ai1, 0, bufsize - tokenBegin);
          System.arraycopy(bufcolumn, 0, ai1, bufsize - tokenBegin, bufpos);
          bufcolumn = ai1;
          maxNextCharInd = bufpos += bufsize - tokenBegin;
        } else {
          System.arraycopy(buffer, tokenBegin, ac, 0, bufsize - tokenBegin);
          buffer = ac;
          System.arraycopy(bufline, tokenBegin, ai, 0, bufsize - tokenBegin);
          bufline = ai;
          System.arraycopy(bufcolumn, tokenBegin, ai1, 0, bufsize - tokenBegin);
          bufcolumn = ai1;
          maxNextCharInd = bufpos -= tokenBegin;
        }
      } catch (Throwable throwable) {
        throw new Error(throwable.getMessage());
      }
      bufsize += 2048;
      available = bufsize;
      tokenBegin = 0;
    }

    private final void FillBuff() throws IOException {
      if (maxNextCharInd == available)
        if (available == bufsize) {
          if (tokenBegin > 2048) {
            bufpos = maxNextCharInd = 0;
            available = tokenBegin;
          } else if (tokenBegin < 0)
            bufpos = maxNextCharInd = 0;
          else
            ExpandBuff(false);
        } else if (available > tokenBegin)
          available = bufsize;
        else if (tokenBegin - available < 2048)
          ExpandBuff(true);
        else
          available = tokenBegin;
      int i;
      try {
        if ((i = inputStream.read(buffer, maxNextCharInd, available - maxNextCharInd)) == -1) {
          inputStream.close();
          throw new IOException();
        } else {
          maxNextCharInd += i;
          return;
        }
      } catch (IOException ioexception) {
        bufpos--;
        backup(0);
        if (tokenBegin == -1)
          tokenBegin = bufpos;
        throw ioexception;
      }
    }

    public final char BeginToken() throws IOException {
      tokenBegin = -1;
      char c = readChar();
      tokenBegin = bufpos;
      return c;
    }

    private final void UpdateLineColumn(char c) {
      column++;
      if (prevCharIsLF) {
        prevCharIsLF = false;
        line += column = 1;
      } else if (prevCharIsCR) {
        prevCharIsCR = false;
        if (c == '\n')
          prevCharIsLF = true;
        else
          line += column = 1;
      }
      switch (c) {
        case 13: // '\r'
          prevCharIsCR = true;
          break;
        case 10: // '\n'
          prevCharIsLF = true;
          break;
        case 9: // '\t'
          column--;
          column += 8 - (column & 7);
          break;
      }
      bufline[bufpos] = line;
      bufcolumn[bufpos] = column;
    }

    public final char readChar() throws IOException {
      if (inBuf > 0) {
        inBuf--;
        if (++bufpos == bufsize)
          bufpos = 0;
        return buffer[bufpos];
      }
      if (++bufpos >= maxNextCharInd)
        FillBuff();
      char c = buffer[bufpos];
      UpdateLineColumn(c);
      return c;
    }

    public final int getColumn() {
      return bufcolumn[bufpos];
    }

    public final int getLine() {
      return bufline[bufpos];
    }

    public final int getEndColumn() {
      return bufcolumn[bufpos];
    }

    public final int getEndLine() {
      return bufline[bufpos];
    }

    public final int getBeginColumn() {
      return bufcolumn[tokenBegin];
    }

    public final int getBeginLine() {
      return bufline[tokenBegin];
    }

    public final void backup(int i) {
      inBuf += i;
      if ((bufpos -= i) < 0)
        bufpos += bufsize;
    }

    public RuleCharStream(Reader reader, int i, int j, int k) {
      bufpos = -1;
      column = 0;
      line = 1;
      prevCharIsCR = false;
      prevCharIsLF = false;
      maxNextCharInd = 0;
      inBuf = 0;
      inputStream = reader;
      line = i;
      column = j - 1;
      available = bufsize = k;
      buffer = new char[k];
      bufline = new int[k];
      bufcolumn = new int[k];
    }

    public RuleCharStream(Reader reader, int i, int j) {
      this(reader, i, j, 4096);
    }

    public RuleCharStream(Reader reader) {
      this(reader, 1, 1, 4096);
    }

    public void ReInit(Reader reader, int i, int j, int k) {
      inputStream = reader;
      line = i;
      column = j - 1;
      if (buffer == null || k != buffer.length) {
        available = bufsize = k;
        buffer = new char[k];
        bufline = new int[k];
        bufcolumn = new int[k];
      }
      prevCharIsLF = prevCharIsCR = false;
      tokenBegin = inBuf = maxNextCharInd = 0;
      bufpos = -1;
    }

    public void ReInit(Reader reader, int i, int j) {
      ReInit(reader, i, j, 4096);
    }

    public void ReInit(Reader reader) {
      ReInit(reader, 1, 1, 4096);
    }

    public RuleCharStream(InputStream inputstream, int i, int j, int k) {
      this(((Reader)(new InputStreamReader(inputstream))), i, j, 4096);
    }

    public RuleCharStream(InputStream inputstream, int i, int j) {
      this(inputstream, i, j, 4096);
    }

    public RuleCharStream(InputStream inputstream) {
      this(inputstream, 1, 1, 4096);
    }

    public void ReInit(InputStream inputstream, int i, int j, int k) {
      ReInit(((Reader)(new InputStreamReader(inputstream))), i, j, 4096);
    }

    public void ReInit(InputStream inputstream) {
      ReInit(inputstream, 1, 1, 4096);
    }

    public void ReInit(InputStream inputstream, int i, int j) {
      ReInit(inputstream, i, j, 4096);
    }

    public final String GetImage() {
      if (bufpos >= tokenBegin)
        return new String(buffer, tokenBegin, (bufpos - tokenBegin) + 1);
      else
        return new String(buffer, tokenBegin, bufsize - tokenBegin) + new String(buffer, 0, bufpos + 1);
    }

    public final char[] GetSuffix(int i) {
      char ac[] = new char[i];
      if (bufpos + 1 >= i) {
        System.arraycopy(buffer, (bufpos - i) + 1, ac, 0, i);
      } else {
        System.arraycopy(buffer, bufsize - (i - bufpos - 1), ac, 0, i - bufpos - 1);
        System.arraycopy(buffer, 0, ac, i - bufpos - 1, bufpos + 1);
      }
      return ac;
    }

    public void Done() {
      buffer = null;
      bufline = null;
      bufcolumn = null;
    }

    public void adjustBeginLineColumn(int i, int j) {
      int k = tokenBegin;
      int l;
      if (bufpos >= tokenBegin)
        l = (bufpos - tokenBegin) + inBuf + 1;
      else
        l = (bufsize - tokenBegin) + bufpos + 1 + inBuf;
      int i1 = 0;
      int j1 = 0;
      boolean flag = false;
      boolean flag1 = false;
      int i2 = 0;
      int k1;
      for (; i1 < l && bufline[j1 = k % bufsize] == bufline[k1 = ++k % bufsize]; i1++) {
        bufline[j1] = i;
        int l1 = (i2 + bufcolumn[k1]) - bufcolumn[j1];
        bufcolumn[j1] = j + i2;
        i2 = l1;
      }
      if (i1 < l) {
        bufline[j1] = i++;
        bufcolumn[j1] = j + i2;
        while (i1++ < l)
          if (bufline[j1 = k % bufsize] != bufline[++k % bufsize])
            bufline[j1] = i++;
          else
            bufline[j1] = i;
      }
      line = bufline[j1];
      column = bufcolumn[j1];
    }
    public static final boolean staticFlag = false;
    int bufsize;
    int available;
    int tokenBegin;
    public int bufpos;
    private int bufline[];
    private int bufcolumn[];
    private int column;
    private int line;
    private boolean prevCharIsCR;
    private boolean prevCharIsLF;
    private Reader inputStream;
    private char buffer[];
    private int maxNextCharInd;
    private int inBuf;
  }

  public class SQLAliasedName implements Serializable {

    public SQLAliasedName() {
      strform_ = "";
      schema_ = null;
      table_ = null;
      column_ = null;
      alias_ = null;
      form_ = JWebKeyword.FORM_COLUMN;
    }

    public SQLAliasedName(String s, int i) {
      strform_ = "";
      schema_ = null;
      table_ = null;
      column_ = null;
      alias_ = null;
      form_ = JWebKeyword.FORM_COLUMN;
      form_ = i;
      strform_ = new String(s);
      StringTokenizer stringtokenizer = new StringTokenizer(s, ".");
      switch (stringtokenizer.countTokens()) {
        case 1: // '\001'
          if (i == JWebKeyword.FORM_TABLE)
            table_ = new String(stringtokenizer.nextToken());
          else
            column_ = new String(stringtokenizer.nextToken());
          break;
        case 2: // '\002'
          if (i == JWebKeyword.FORM_TABLE) {
            schema_ = new String(stringtokenizer.nextToken());
            table_ = new String(stringtokenizer.nextToken());
          } else {
            table_ = new String(stringtokenizer.nextToken());
            column_ = new String(stringtokenizer.nextToken());
          }
          break;
        case 3: // '\003'
        default:
          schema_ = new String(stringtokenizer.nextToken());
          table_ = new String(stringtokenizer.nextToken());
          column_ = new String(stringtokenizer.nextToken());
          break;
      }
    }

    public String toString() {
      if (alias_ == null)
        return strform_;
      else
        return strform_ + " " + alias_;
    }

    public String getSchema() {
      return schema_;
    }

    public String getTable() {
      return table_;
    }

    public String getColumn() {
      return column_;
    }

    public boolean isWildcard() {
      if (form_ == JWebKeyword.FORM_TABLE)
        return table_ != null && table_.equals("*");
      else
        return column_ != null && column_.indexOf('*') >= 0;
    }

    public String getAlias() {
      return alias_;
    }

    public void setAlias(String s) {
      alias_ = new String(fixName(s));
    }
    String strform_;
    String schema_;
    String table_;
    String column_;
    String alias_;
    int form_;
  }

  public interface SQLExp extends Serializable {
  }

  public class SQLConstant implements SQLExp {

    public SQLConstant(String s, int i) {
      type_ = -1;
      val_ = null;
      val_ = new String(fixName(s));
      //
      //
      type_ = i;
      if (type_ == 0) {
        varNames.add(val_);
      }
      // System.err.println("variable="+val_+" ="+type_);
    }

    public String getValue() {
      return val_;
    }

    public void setValue(String val) {
      val_ = val;
    }

    public int getType() {
      return type_;
    }

    public String toString() {
      if (type_ == 3)
        return '\'' + val_ + '\'';
      else
        return val_;
    }
    public static final int UNKNOWN = -1;
    public static final int COLUMNNAME = 0;
    public static final int NULL = 1;
    public static final int NUMBER = 2;
    public static final int STRING = 3;
    int type_;
    String val_;
  }

  public class SQLDelete implements SQLStatement {

    public SQLDelete(String s) {
      where_ = null;
      table_ = new String(s);
    }

    public void addWhere(SQLExp zexp) {
      where_ = zexp;
    }

    public String getTable() {
      return table_;
    }

    public SQLExp getWhere() {
      return where_;
    }

    public String toString() {
      StringBuffer stringbuffer = new StringBuffer("delete ");
      if (where_ != null)
        stringbuffer.append("from ");
      stringbuffer.append(table_);
      if (where_ != null)
        stringbuffer.append(" where " + where_.toString());
      return stringbuffer.toString();
    }
    String table_;
    SQLExp where_;
  }

  public class SQLEval {

    public SQLEval() {
    }

    public String[] getParams(SQLExpression zexpression) {
      String[] params = new String[zexpression.nbOperands()];
      for (int i = 0; i < params.length; i++) {
        String param = zexpression.getOperand(i).toString();
        param = JWebUtils.replaceStr(param, "'", "");
        param = JWebUtils.replaceStr(param, NO_QUOTE, "");
        param = JWebUtils.replaceStr(param, SINGLE_QUOTE, "'");
        params[i] = param;
        //System.out.println(zexpression.getOperand(i).toString());
      }
      return params;
    }

    public boolean eval(JWebSQLTuple ztuple, SQLExp zexp) throws SQLException {
      if (ztuple == null || zexp == null)
        throw new SQLException("SQLEval.eval(): null argument or operator");
      if (!(zexp instanceof SQLExpression))
        throw new SQLException("SQLEval.eval(): only expressions are supported");
      SQLExpression zexpression = (SQLExpression)zexp;
      String s = zexpression.getOperator();
      if (s.equals("AND")) {
        boolean flag = true;
        for (int i = 0; i < zexpression.nbOperands(); i++)
          flag &= eval(ztuple, zexpression.getOperand(i));
        return flag;
      }
      if (s.equals("OR")) {
        boolean flag1 = false;
        for (int j = 0; j < zexpression.nbOperands(); j++)
          flag1 |= eval(ztuple, zexpression.getOperand(j));
        return flag1;
      }
      if (s.equals("NOT"))
        return !eval(ztuple, zexpression.getOperand(0));
      if (s.equals("="))
        return evalCmp(ztuple, zexpression.getOperands()) == 0.0D;
      if (s.equals("!="))
        return evalCmp(ztuple, zexpression.getOperands()) != 0.0D;
      if (s.equals("<>"))
        return evalCmp(ztuple, zexpression.getOperands()) != 0.0D;
      if (s.equals("#"))
        throw new SQLException("SQLEval.eval(): Operator # not supported");
      if (s.equals(">"))
        return evalCmp(ztuple, zexpression.getOperands()) > 0.0D;
      if (s.equals(">="))
        return evalCmp(ztuple, zexpression.getOperands()) >= 0.0D;
      if (s.equals("<"))
        return evalCmp(ztuple, zexpression.getOperands()) < 0.0D;
      if (s.equals("<="))
        return evalCmp(ztuple, zexpression.getOperands()) <= 0.0D;
      if (s.equals("BETWEEN") || s.equals("NOT BETWEEN")) {
        SQLExpression zexpression1 = new SQLExpression("AND", new SQLExpression(">=", zexpression.getOperand(0), zexpression.getOperand(1)), new SQLExpression("<=", zexpression.getOperand(0), zexpression.getOperand(2)));
        if (s.equals("NOT BETWEEN"))
          return !eval(ztuple, ((SQLExp)(zexpression1)));
        else
          return eval(ztuple, ((SQLExp)(zexpression1)));
      }
      if (s.equals("LIKE")) {
      	Object expr1 = evalExpValue(ztuple, zexpression.getOperand(0));
      	Object expr2 = evalExpValue(ztuple, zexpression.getOperand(1));
      	String exprValue = expr1.toString().trim().toLowerCase(); 
      	String likeValue = expr2.toString().trim().toLowerCase();
      	//System.out.println("exprValue="+exprValue+" LikeValue="+likeValue);
      	if (likeValue.startsWith("%") && likeValue.endsWith("%")) {
      		likeValue = likeValue.substring(1, likeValue.length()-2);
      		if (exprValue.indexOf(likeValue)>=0) return true; else return false;
      	}
      	else if (likeValue.startsWith("%")) {
      		likeValue = likeValue.substring(1);
      		if (exprValue.startsWith(likeValue)) return true; else return false;
      	}
      	else if (likeValue.endsWith("%")) {
      		likeValue = likeValue.substring(0,likeValue.length()-1);
      		if (exprValue.endsWith(likeValue)) return true; else return false;
      	}
      	else {
      		if (JWebUtils.isSame(likeValue,exprValue)) return true; else return false;
      	}
      	//System.out.println(expr1+" LIKE "+expr2);
      	//System.out.println(" LIKE "+JWebUtils.removeEndQuotes(zexpression.getOperand(1).toString()));
      	//System.out.println(" LIKE being implemented. ");
        //return false;
      }
      if (s.equals("NOT LIKE")) {
      	//throw new SQLException("SQLEval.eval(): Operator (NOT) LIKE not supported");
      	System.out.println(" NOT LIKE being implemented. ");
      	return false;
      }
      if (s.equals("IN") || s.equals("NOT IN")) {
        SQLExpression zexpression2 = new SQLExpression("OR");
        for (int k = 1; k < zexpression.nbOperands(); k++)
          zexpression2.addOperand(new SQLExpression("=", zexpression.getOperand(0), zexpression.getOperand(k)));
        if (s.equals("NOT IN"))
          return !eval(ztuple, ((SQLExp)(zexpression2)));
        else
          return eval(ztuple, ((SQLExp)(zexpression2)));
      }
      if (s.equals("IS NULL")) {
        if (zexpression.nbOperands() <= 0 || zexpression.getOperand(0) == null)
          return true;
        SQLExp zexp1 = zexpression.getOperand(0);
        if (zexp1 instanceof SQLConstant)
          return ((SQLConstant)zexp1).getType() == 1;
        else
          throw new SQLException("SQLEval.eval(): can't eval IS (NOT) NULL");
      }
      if (s.equals("IS NOT NULL")) {
        SQLExpression zexpression3 = new SQLExpression("IS NULL");
        zexpression3.setOperands(zexpression.getOperands());
        return !eval(ztuple, ((SQLExp)(zexpression3)));
      } else {
        throw new SQLException("SQLEval.eval(): Unknown operator " + s);
      }
    }

    double evalCmp(JWebSQLTuple ztuple, ArrayList aList) throws SQLException {
      if (aList.size() < 2)
        throw new SQLException("SQLEval.evalCmp(): Trying to compare less than two values");
      if (aList.size() > 2)
        throw new SQLException("SQLEval.evalCmp(): Trying to compare more than two values");
      Object obj = null;
      Object obj1 = null;
      obj = evalExpValue(ztuple, (SQLExp)aList.get(0));
      obj1 = evalExpValue(ztuple, (SQLExp)aList.get(1));
      if ((obj instanceof Number) && (obj1 instanceof String)) {
        String obj1Value = JWebUtils.removeEndQuotes(obj1.toString());
        try {
          Double objDouble = new Double(JWebUtils.removeEndQuotes(obj1.toString()));
          return objDouble.doubleValue() - ((Number)obj).doubleValue();
        } catch (NumberFormatException numberformatexception) {
        }
        String objValue = obj.toString();
        return (double)(objValue.equals(obj1Value) ? 0 : -1);
      } else if ((obj1 instanceof Number) && (obj instanceof String)) {
        String objValue = JWebUtils.removeEndQuotes(obj.toString());
        try {
          Double objDouble = new Double(JWebUtils.removeEndQuotes(obj.toString()));
          return objDouble.doubleValue() - ((Number)obj1).doubleValue();
        } catch (NumberFormatException numberformatexception) {
        }
        String obj1Value = obj1.toString();
        return (double)(objValue.equals(obj1Value) ? 0 : -1);
      } else if ((obj instanceof String) || (obj1 instanceof String)) {
        // System.err.println("["+obj1+"]["+obj+"]");
        String obj1Value = JWebUtils.removeEndQuotes(obj1.toString());
        String objValue = JWebUtils.removeEndQuotes(obj.toString());
        return (double)(objValue.equals(obj1Value) ? 0 : -1);
      } else if ((obj instanceof Number) && (obj1 instanceof Number))
        return ((Number)obj).doubleValue() - ((Number)obj1).doubleValue();
      else
        throw new SQLException("SQLEval.evalCmp(): can't compare (" + obj.toString() + ") with (" + obj1.toString() + ")");
    }

    public String evalTextExp(JWebSQLTuple ztuple, SQLExpression zexpression) throws SQLException {
      if (ztuple == null || zexpression == null || zexpression.getOperator() == null)
        throw new SQLException("SQLEval.eval(): null argument or operator");
      String s = zexpression.getOperator();
      Object obj = evalExpValue(ztuple, zexpression.getOperand(0));
      String s1 = obj.toString();
      if (s == null) {
        return JWebUtils.removeQuotes(s1);
      }
      // System.err.println("["+getClass().getName()+"]"+" FUNCTION "+s);
      if (s.equals("concat")) {
        for (int i = 1; i < zexpression.nbOperands(); i++) {
          s1 += evalExpValue(ztuple, zexpression.getOperand(i)).toString();
        }
        return JWebUtils.removeQuotes(s1);
      }
      if (s.equals("IntToStr")) {
        // System.err.println(s1);
        if (JWebUtils.isNotNull(s1)) {
          return Integer.toString(Double.valueOf(s1).intValue());
          // return JWebUtils.removeQuotes(s1);
          // return s1;
        } else
          return "";
      }
      if (s.equals("+")) {
        for (int i = 1; i < zexpression.nbOperands(); i++) {
          s1 += evalExpValue(ztuple, zexpression.getOperand(i)).toString();
        }
        return JWebUtils.removeQuotes(s1);
      }
      if (s.equals("pattern")) {
        String pattern = JWebUtils.removeQuotes(((String)evalExpValue(ztuple, zexpression.getOperand(0))).trim());
        Object inputObj = evalExpValue(ztuple, zexpression.getOperand(1));
        String input = null;
        if ((inputObj instanceof String))
          input = (String)inputObj;
        else if ((inputObj instanceof Double))
          input = inputObj.toString();
        else if ((inputObj instanceof Integer))
          input = inputObj.toString();
        else
          input = inputObj.toString();
        // System.err.println("["+getClass().getName()+"]"+""+pattern+","+input);
        input = JWebUtils.removeQuotes(input.trim());
        return "false";
      }
      if (isUserDefinedFunction(s)) {
        Object[] parameters = new Object[zexpression.nbOperands()];
        for (int i = 0; i < zexpression.nbOperands(); i++) {
          parameters[i] = evalExpValue(ztuple, zexpression.getOperand(i));
        }
        //JWebRuleFunction fnc = getUserDefinedFunction(s);
        String s4 = "";//evalUserDefinedText(fnc, parameters);
        // System.err.println("["+getClass().getName()+"]"+"d4:"+d4);
        return s4;
      }
      return "";
    }


    public double getNumber(SQLExp exp, String param, JWebSQLTuple ztuple) throws SQLException {
      double d = exprFuncs.transformNumber(param);
      if (d != Double.MIN_VALUE) return d; 
      Object obj1 = evalExpValue(ztuple, exp);
      if (exp instanceof SQLConstant) {
        SQLConstant expConstant = (SQLConstant)exp;
        SQLExp expEval = new SQLConstant(param, expConstant.getType());
        obj1 = evalExpValue(ztuple, expEval);
      } else if (exp instanceof SQLExpression) {
        obj1 = evalExpValue(ztuple, exp);
      }
      if (JWebUtils.isNull(obj1.toString())) return 0.0;
      else d = Double.valueOf(obj1.toString());
      return d;
    }

    public double evalNumericExp(JWebSQLTuple ztuple, SQLExpression zexpression) throws SQLException {
      if ((ztuple == null) || zexpression == null || zexpression.getOperator() == null)
        throw new SQLException("SQLEval.eval(): null argument or operator");
      String s = zexpression.getOperator();
      if (s == null) {
        Object obj = evalExpValue(ztuple, zexpression.getOperand(0));
        if (!(obj instanceof Double)) {
          throw new SQLException("SQLEval.evalNumericExp(): expression [" + obj + "] not numeric");
        }
        Double double1 = (Double)obj;
        return double1.doubleValue();
      }
      if (s.equals("+")) {
        boolean numeric = true;
        for (int i = 0; i < zexpression.nbOperands(); i++) {
          Object obj1 = evalExpValue(ztuple, zexpression.getOperand(i));
          //System.err.println("["+obj1+"]");
          if (!(obj1 instanceof Double)) {
          	 numeric = false;
            throw new SQLException("SQLEval.evalNumericExp(): expression ["+obj1+"] not numeric");
           
          }
        }
        if (numeric) {
          Object obj = evalExpValue(ztuple, zexpression.getOperand(0));
          Double double1 = (Double)obj;
          double d = double1.doubleValue();
          for (int i = 1; i < zexpression.nbOperands(); i++) {
            Object obj1 = evalExpValue(ztuple, zexpression.getOperand(i));
            d += ((Number)obj1).doubleValue();
          }
          return d;
        } else {
          throw new SQLException("SQLEval.evalNumericExp(): ["+expr+"] expression not numeric");
        }
      }
      if (s.equals("-")) {
        Object obj = evalExpValue(ztuple, zexpression.getOperand(0));
        if (!(obj instanceof Double)) {
          throw new SQLException("SQLEval.evalNumericExp(): expression [" + obj + "] not numeric");
        }
        Double double1 = (Double)obj;
        double d1 = double1.doubleValue();
        if (zexpression.nbOperands() == 1)
          return -d1;
        for (int j = 1; j < zexpression.nbOperands(); j++) {
          Object obj2 = evalExpValue(ztuple, zexpression.getOperand(j));
          d1 -= ((Number)obj2).doubleValue();
        }
        return d1;
      }
      if (s.equals("*")) {
        Object obj = evalExpValue(ztuple, zexpression.getOperand(0));
        if (!(obj instanceof Double)) {
          throw new SQLException("SQLEval.evalNumericExp(): expression [" + obj + "] not numeric");
        }
        Double double1 = (Double)obj;
        double d2 = double1.doubleValue();
        for (int k = 1; k < zexpression.nbOperands(); k++) {
          Object obj3 = evalExpValue(ztuple, zexpression.getOperand(k));
          d2 *= ((Number)obj3).doubleValue();
        }
        return d2;
      }
      if (s.equals("/")) {
        Object obj = evalExpValue(ztuple, zexpression.getOperand(0));
        if (!(obj instanceof Double)) {
          throw new SQLException("SQLEval.evalNumericExp(): expression [" + obj + "] not numeric");
        }
        Double double1 = (Double)obj;
        double d3 = double1.doubleValue();
        for (int l = 1; l < zexpression.nbOperands(); l++) {
          Object obj4 = evalExpValue(ztuple, zexpression.getOperand(l));
          d3 /= ((Number)obj4).doubleValue();
        }
        return d3;
      }
      if (s.equals("**")) {
        Object obj = evalExpValue(ztuple, zexpression.getOperand(0));
        if (!(obj instanceof Double)) {
          throw new SQLException("SQLEval.evalNumericExp(): expression [" + obj + "] not numeric");
        }
        Double double1 = (Double)obj;
        double d4 = double1.doubleValue();
        for (int i1 = 1; i1 < zexpression.nbOperands(); i1++) {
          Object obj5 = evalExpValue(ztuple, zexpression.getOperand(i1));
          d4 = Math.pow(d4, ((Number)obj5).doubleValue());
        }
        return d4;
      }
      if (s.equals("round")) {
        Object obj = evalExpValue(ztuple, zexpression.getOperand(0));
        if (!(obj instanceof Double)) {
          throw new SQLException("SQLEval.evalNumericExp(): expression [" + obj + "] not numeric");
        }
        Double double1 = (Double)obj;
        double d4 = double1.doubleValue();
        obj = evalExpValue(ztuple, zexpression.getOperand(1));
        if (!(obj instanceof Double)) {
          throw new SQLException("SQLEval.evalNumericExp(): round 2nd expression not numeric");
        }
        Double d5 = (Double)obj;
        // System.err.println("ROUND --"+d4+","+d5.intValue());
        return JWebUtils.round(d4, d5.intValue());
      }
      if (s.equals("value")) {
        Object obj = evalExpValue(ztuple, zexpression.getOperand(0));
        if (!(obj instanceof Double)) {
          throw new SQLException("SQLEval.evalNumericExp(): expression [" + obj + "] not numeric");
        }
        Double double1 = (Double)obj;
        double d4 = double1.doubleValue();
        // System.err.println("["+getClass().getName()+"]"+"
        // value:"+d4);
        return d4;
      } else if (s.equals("trunc")) {
        Object obj = evalExpValue(ztuple, zexpression.getOperand(0));
        if (!(obj instanceof Double)) {
          throw new SQLException("SQLEval.evalNumericExp(): expression [" + obj + "] not numeric");
        }
        Double double1 = (Double)obj;
        double d4 = double1.doubleValue();
        return JWebUtils.trunc(d4);
      } else if (s.equals("length")) {
        Object obj = evalExpValue(ztuple, zexpression.getOperand(0));
        if (!(obj instanceof Double)) {
          throw new SQLException("SQLEval.evalNumericExp(): expression [" + obj + "] not numeric");
        }
        Double double1 = (Double)obj;
        double d4 = double1.doubleValue();
        return Double.toString(d4).length();
      } else if (s.equals("sum") && zexpression.nbOperands() > 1) {
        Object obj = evalExpValue(ztuple, zexpression.getOperand(0));
        double d = 0.0;
        if ((obj instanceof Number)) {
          d = ((Number)obj).doubleValue();
        }
        for (int i = 1; i < zexpression.nbOperands(); i++) {
          Object obj1 = evalExpValue(ztuple, zexpression.getOperand(i));
          if ((obj1 instanceof Double)) {
            d += ((Number)obj1).doubleValue();
          }
        }
        return d;
      } else if (s.equals("sum")) {
        Object obj = evalExpValue(ztuple, zexpression.getOperand(0));
        if (!(obj instanceof Double)) {
          throw new SQLException("SQLEval.evalNumericExp(): expression [" + obj + "] not numeric");
        }
        Double double1 = (Double)obj;
        double d4 = double1.doubleValue();
        return d4;
      } else if (s.equals("sumn")) {
        Object obj = evalExpValue(ztuple, zexpression.getOperand(0));
        double d = 0.0;
        if ((obj instanceof Number)) {
          d = ((Number)obj).doubleValue();
        }
        for (int i = 1; i < zexpression.nbOperands(); i++) {
          Object obj1 = evalExpValue(ztuple, zexpression.getOperand(i));
          if ((obj1 instanceof Double)) {
            d += ((Number)obj1).doubleValue();
          }
        }
        return d;
      }
      if (s.equalsIgnoreCase("SlidingScale")) {
        String[] params = getParams(zexpression);
        double totalConsideration = getNumber(zexpression.getOperand(0), params[0], ztuple);
        double startInterval = 1+getNumber(zexpression.getOperand(1), params[1], ztuple);
        double endInterval = getNumber(zexpression.getOperand(2), params[2], ztuple);
        double tierCharge = getNumber(zexpression.getOperand(3), params[3], ztuple);
        if (trace) System.out.println("Total Consideration:" + BigDecimal.valueOf(totalConsideration).toString() + " " + " Scale Start:" + BigDecimal.valueOf(startInterval).toString() + " Scale End:" + BigDecimal.valueOf(endInterval).toString() + " Scale Rate:" + tierCharge);
        if (totalConsideration >= startInterval) {
          double rate = ((Math.min(totalConsideration - startInterval, endInterval - startInterval) * tierCharge) / totalConsideration);
          //System.out.println(" Rate:" +((Math.min(totalConsideration - startInterval, endInterval - startInterval) * tierCharge)));
          return ((Math.min(totalConsideration - startInterval, endInterval - startInterval) * tierCharge));
        } else {
          return 0.0;
        }
      }
      if (s.equalsIgnoreCase("convert")) {
      	if (dwConvert==null) return 0.0;
        String[] params = getParams(zexpression);
        double  numberToConvert = getNumber(zexpression.getOperand(0), params[0], ztuple);
        String  currencyFrom = evalExpValue(ztuple, zexpression.getOperand(1)).toString();
        String  currencyTo = evalExpValue(ztuple, zexpression.getOperand(2)).toString();
        currencyFrom = JWebUtils.replaceStr(currencyFrom,  SINGLE_QUOTE,"").toUpperCase();
        currencyTo = JWebUtils.replaceStr(currencyTo,  SINGLE_QUOTE,"").toUpperCase();
        double currencyFromRate = dwConvert.getItemDouble(dwConvert.find(CURRENCY,currencyFrom),CURRENCY_RATE);
        double currencyToRate = dwConvert.getItemDouble(dwConvert.find(CURRENCY,currencyTo),CURRENCY_RATE);
        double n = numberToConvert;
      	numberToConvert = (numberToConvert / currencyFromRate)* currencyToRate;
        //System.out.println(n+" : "+numberToConvert+","+currencyFrom+","+currencyTo);
        return numberToConvert;
        
      }
      if (s.equals("Maximum") || s.equalsIgnoreCase("Floor")) {
        String[] params = getParams(zexpression);
        SQLExp expEval = null;
        double d = Double.MIN_VALUE, d1 = 0.0;
        for (int i = 0; i < zexpression.nbOperands(); i++) {
          SQLExp exp = zexpression.getOperand(i);
          Object obj1 = evalExpValue(ztuple, zexpression.getOperand(i));
          if (exp instanceof SQLConstant) {
            SQLConstant expConstant = (SQLConstant)exp;
            expEval = new SQLConstant(params[i], expConstant.getType());
            obj1 = evalExpValue(ztuple, expEval);
          } else if (exp instanceof SQLExpression) {
            obj1 = evalExpValue(ztuple, exp);
          }
          if ((obj1 instanceof Double)) {
            d1 = ((Number)obj1).doubleValue();
            d = d1 > d ? d1 : d;
          }
        }
        return d;
      }
      if (s.equals("Minimum") || s.equalsIgnoreCase("Cap")) {
        String[] params = getParams(zexpression);
        SQLExp expEval = null;
        double d = Double.MAX_VALUE, d1 = 0.0;
        for (int i = 0; i < zexpression.nbOperands(); i++) {
          SQLExp exp = zexpression.getOperand(i);
          Object obj1 = evalExpValue(ztuple, zexpression.getOperand(i));
          if (exp instanceof SQLConstant) {
            SQLConstant expConstant = (SQLConstant)exp;
            expEval = new SQLConstant(params[i], expConstant.getType());
            obj1 = evalExpValue(ztuple, expEval);
          } else if (exp instanceof SQLExpression) {
            obj1 = evalExpValue(ztuple, exp);
          }
          if ((obj1 instanceof Double)) {
            d1 = ((Number)obj1).doubleValue();
            d = d1 < d ? d1 : d;
          }
        }
        return d;
      }
      if (s.equals("if")) {
        String[] params = getParams(zexpression);
        String exprStr = params[0];
        boolean expr;
        try {
        	 expr = evaluateExpr(exprStr,false);
				} catch (Exception e) {
					// TODO: handle exception
					 expr = false;
				}
        
        double d = 0.0;
        int param = expr ? 1 : 2;
        if (trace)
          System.out.println("IF func(expr):" + exprStr+ " result="+expr);
//        if (exprStr.equalsIgnoreCase("fixed+variable>75") && expr) {
//          System.out.println("IF func(expr):" + exprStr+ " result="+expr);
//        }
        SQLExp exp = zexpression.getOperand(param);
        SQLExp expEval = null;
        Object obj1 = null;
        if (exp instanceof SQLConstant) {
          SQLConstant expConstant = (SQLConstant)exp;
          expEval = new SQLConstant(params[param], expConstant.getType());
          obj1 = evalExpValue(ztuple, expEval);
        } else if (exp instanceof SQLExpression) {
          obj1 = evalExpValue(ztuple, exp);
        }
        //if ((obj1 instanceof Number)) {
        try {
          d = Double.valueOf(obj1.toString());
        }
        catch (Exception e) {
        	d = evaluateNumeric(obj1.toString()+"+0.0");
        	//System.out.println(d);
        }
        return d;
      }
      if (s.equals("sum")) {
        Object obj = evalExpValue(ztuple, zexpression.getOperand(0));
        if (!(obj instanceof Double)) {
          throw new SQLException("SQLEval.evalNumericExp(): expression [" + obj + "] not numeric");
        }
        Double double1 = (Double)obj;
        double d4 = double1.doubleValue();
        return d4;
      } else if (s.equals("sumn")) {
        Object obj = evalExpValue(ztuple, zexpression.getOperand(0));
        double d = 0.0;
        if ((obj instanceof Number)) {
          d = ((Number)obj).doubleValue();
        }
        for (int i = 1; i < zexpression.nbOperands(); i++) {
          Object obj1 = evalExpValue(ztuple, zexpression.getOperand(i));
          if ((obj1 instanceof Double)) {
            d += ((Number)obj1).doubleValue();
          }
        }
        return d;
      }
      return 0.0;
    }

    public Object evalExpValue(JWebSQLTuple ztuple, SQLExp zexp) throws SQLException {
      Object obj = null;
      if (zexp instanceof SQLConstant) {
        SQLConstant zconstant = (SQLConstant)zexp;
        switch (zconstant.getType()) {
          case 0: // '\0'
            //System.err.println("zConstant:"+ztuple.getAttValue(zconstant.getValue()));
            Object obj1 = ztuple == null ? null : ztuple.getAttValue(zconstant.getValue());
            if (obj1 == null) {
            	obj1= ztuple == null ? null :ztuple.getAttValue(zconstant.getValue().toLowerCase());
            }
            if (obj1 == null) {
              if (obj1 == null) {
                throw new SQLException("SQLEval.evalExpValue(): unknown var [" + zconstant.getValue() + "]");
              } else {
                System.err.println("Object Value:" + obj1.toString());
              }
            }
            try {
              obj = new Double(JWebUtils.removeEndQuotes(obj1.toString()));
            } catch (NumberFormatException numberformatexception) {
              obj = obj1;
            }
            break;
          case 2: // '\002'
            obj = new Double(zconstant.getValue());
            break;
          case 1: // '\001'
          case 3: // '\003'
          default:
            obj = zconstant.getValue();
            break;
        }
      } else if (zexp instanceof SQLExpression) {
        SQLExpression zexpression = (SQLExpression)zexp;
        String s = zexpression.getOperator();
        if (isTextFunction(s)) {
          // System.err.println("["+getClass().getName()+"]"+"
          // evalText {");
          obj = new String(evalTextExp(ztuple, zexpression));
        } else {
          try {
            // System.err.println("["+getClass().getName()+"]"+"
            // Numeric "+s);
            if (s.equals("value")) {
              // System.err.println("["+getClass().getName()+"]"+"
              // Numeric "+s);
            }
            obj = new Double(evalNumericExp(ztuple, zexpression));
          } catch (Exception ex) {
            obj = new String(evalTextExp(ztuple, zexpression));
            // System.err.println("["+getClass().getName()+"]"+"
            // evalText }");
          }
        }
      }
      return obj;
    }
  }

  public class SQLExpression implements SQLExp  {

    public SQLExpression(String s) {
      op_ = null;
      operands_ = null;
      op_ = new String(s);
    }

    public SQLExpression(String s, SQLExp zexp) {
      op_ = null;
      operands_ = null;
      op_ = new String(s);
      addOperand(zexp);
    }

    public SQLExpression(String s, SQLExp zexp, SQLExp zexp1) {
      op_ = null;
      operands_ = null;
      op_ = new String(s);
      addOperand(zexp);
      addOperand(zexp1);
    }

    public String getOperator() {
      return op_;
    }

    public void setOperands(ArrayList aList) {
      operands_ = aList;
    }

    public ArrayList getOperands() {
      return operands_;
    }

    public void addOperand(SQLExp zexp) {
      if (operands_ == null)
        operands_ = new ArrayList();
      operands_.add(zexp);
    }

    public SQLExp getOperand(int i) {
      if (operands_ == null || i >= operands_.size())
        return null;
      else
        return (SQLExp)operands_.get(i);
    }

    public int nbOperands() {
      if (operands_ == null)
        return 0;
      else
        return operands_.size();
    }

    public String toReversePolish()  {
      StringBuffer stringbuffer = new StringBuffer("(");
      stringbuffer.append(op_);
      for (int i = 0; i < nbOperands(); i++) {
        SQLExp zexp = getOperand(i);
        if (zexp instanceof SQLExpression)
          stringbuffer.append(" " + ((SQLExpression)zexp).toReversePolish());
        else if (zexp instanceof SQLQuery)
          stringbuffer.append(" (" + zexp.toString() + ")");
        else
          stringbuffer.append(" " + zexp.toString());
      }
      stringbuffer.append(")");
      return stringbuffer.toString();
    }

    public String toString() {
      if (op_.equals("?"))
        return op_;
      if (isCustomFunction(op_) > 0)
        return formatFunction();
      StringBuffer stringbuffer = new StringBuffer();
      if (needPar(op_))
        stringbuffer.append("(");
      switch (nbOperands()) {
        case 1: // '\001'
          SQLExp zexp = getOperand(0);
          if (zexp instanceof SQLConstant) {
            if (isAggregate(op_))
              stringbuffer.append(op_ + "(" + zexp.toString() + ")");
            else
              stringbuffer.append(op_ + " " + zexp.toString());
          } else if (zexp instanceof SQLQuery)
            stringbuffer.append(op_ + " (" + zexp.toString() + ")");
          else
            stringbuffer.append(op_ + " " + zexp.toString());
          break;
        case 3: // '\003'
          if (op_.toUpperCase().endsWith("BETWEEN")) {
            stringbuffer.append(getOperand(0).toString() + " " + op_ + " " + getOperand(1).toString() + " AND " + getOperand(2).toString());
            break;
          }
          // fall through
        default:
          boolean flag = op_.equals("IN") || op_.equals("NOT IN");
          int i = nbOperands();
          for (int j = 0; j < i; j++) {
            if (flag && j == 1)
              stringbuffer.append(" " + op_ + " (");
            SQLExp zexp1 = getOperand(j);
            if ((zexp1 instanceof SQLQuery) && !flag)
              stringbuffer.append("(" + zexp1.toString() + ")");
            else
              stringbuffer.append(zexp1.toString());
            if (j < i - 1)
              if (op_.equals(",") || flag && j > 0)
                stringbuffer.append(", ");
              else if (!flag)
                stringbuffer.append(" " + op_ + " ");
          }
          if (flag)
            stringbuffer.append(")");
          break;
      }
      if (needPar(op_))
        stringbuffer.append(")");
      return stringbuffer.toString();
    }

    private boolean needPar(String s) {
      s = s.toUpperCase();
      return !s.equals("ANY") && !s.equals("ALL") && !s.equals("UNION") && !isAggregate(s);
    }

    private String formatFunction(){
      StringBuffer stringbuffer = new StringBuffer(op_ + "(");
      int i = nbOperands();
      for (int j = 0; j < i; j++)
        stringbuffer.append(getOperand(j).toString() + (j >= i - 1 ? "" : ","));
      stringbuffer.append(")");
      return stringbuffer.toString();
    }
    String op_;
    ArrayList operands_;
  }

  public class SQLFromItem extends SQLAliasedName {

    public SQLFromItem() {
    }

    public SQLFromItem(String s) {
      super(s, 1);
        /* FORM_TABLE */
    }
  }

  public class SQLGroupBy implements Serializable {

    public SQLGroupBy(ArrayList aList) {
      having_ = null;
      groupby_ = aList;
    }

    public void setHaving(SQLExp zexp) {
      having_ = zexp;
    }

    public ArrayList getGroupBy() {
      return groupby_;
    }

    public SQLExp getHaving() {
      return having_;
    }

    public String toString() {
      StringBuffer stringbuffer = new StringBuffer("group by ");
      stringbuffer.append(groupby_.get(0).toString());
      for (int i = 1; i < groupby_.size(); i++)
        stringbuffer.append(", " + groupby_.get(i).toString());
      if (having_ != null)
        stringbuffer.append(" having " + having_.toString());
      return stringbuffer.toString();
    }
    ArrayList groupby_;
    SQLExp having_;
  }

  public class SQLInsert implements SQLStatement {

    public SQLInsert(String s) {
      columns_ = null;
      valueSpec_ = null;
      table_ = new String(s);
      where = null;
    }

    public String getTable() {
      return table_;
    }

    public ArrayList getColumns() {
      return columns_;
    }

    public void addColumns(ArrayList aList) {
      columns_ = aList;
    }

    public void addWhere(ArrayList exp) {
      where = exp;
    }

    public ArrayList getWhere() {
      return where;
    }

    public void addValueSpec(SQLExp zexp) {
      valueSpec_ = zexp;
    }

    public ArrayList getValues() {
      if (!(valueSpec_ instanceof SQLExpression))
        return null;
      else
        return ((SQLExpression)valueSpec_).getOperands();
    }

    public SQLQuery getQuery() {
      if (!(valueSpec_ instanceof SQLQuery))
        return null;
      else
        return (SQLQuery)valueSpec_;
    }
    // fhzeya

    public String toString() {
      StringBuffer stringbuffer = new StringBuffer("insert into " + table_);
      if (columns_ != null && columns_.size() > 0) {
        stringbuffer.append("(" + columns_.get(0));
        for (int i = 1; i < columns_.size(); i++)
          stringbuffer.append("," + columns_.get(i));
        stringbuffer.append(")");
      }
      String s = valueSpec_.toString();
      stringbuffer.append(" ");
      if (getValues() != null)
        stringbuffer.append("values ");
      if (s.startsWith("("))
        stringbuffer.append(s);
      else
        stringbuffer.append(" (" + s + ")");
      return stringbuffer.toString();
    }
    String table_;
    ArrayList columns_;
    SQLExp valueSpec_;
    ArrayList where; // fhzeya.where
  }

  public class SQLLockTable implements SQLStatement {

    public SQLLockTable() {
      nowait_ = false;
      lockMode_ = null;
      tables_ = null;
    }

    public void addTables(ArrayList aList) {
      tables_ = aList;
    }

    public ArrayList getTables() {
      return tables_;
    }

    public void setLockMode(String s) {
      lockMode_ = new String(s);
    }

    public String getLockMode() {
      return lockMode_;
    }

    public boolean isNowait() {
      return nowait_;
    }
    boolean nowait_;
    String lockMode_;
    ArrayList tables_;
  }

  public class SQLOrderBy implements Serializable {

    public SQLOrderBy(SQLExp zexp) {
      asc_ = true;
      exp_ = zexp;
    }

    public void setAscOrder(boolean flag) {
      asc_ = flag;
    }

    public boolean getAscOrder() {
      return asc_;
    }

    public SQLExp getExpression() {
      return exp_;
    }

    public String toString() {
      return exp_.toString() + " " + (asc_ ? "ASC" : "DESC");
    }
    SQLExp exp_;
    boolean asc_;
  }

  public class SQLQuery implements SQLStatement, SQLExp {

    public SQLQuery() {
      distinct_ = false;
      where_ = null;
      groupby_ = null;
      setclause_ = null;
      orderby_ = null;
      forupdate_ = false;
    }

    public void addSelect(ArrayList aList) {
      select_ = aList;
    }

    public void addFrom(ArrayList aList) {
      from_ = aList;
    }

    public void addWhere(SQLExp zexp) {
      where_ = zexp;
    }

    public void addGroupBy(SQLGroupBy zgroupby) {
      groupby_ = zgroupby;
    }

    public void addSet(SQLExpression zexpression) {
      setclause_ = zexpression;
    }

    public void addOrderBy(ArrayList aList) {
      orderby_ = aList;
    }

    public ArrayList getSelect() {
      return select_;
    }

    public ArrayList getFrom() {
      return from_;
    }

    public SQLExp getWhere() {
      return where_;
    }

    public SQLGroupBy getGroupBy() {
      return groupby_;
    }

    public SQLExpression getSet() {
      return setclause_;
    }

    public ArrayList getOrderBy() {
      return orderby_;
    }

    public boolean isDistinct() {
      return distinct_;
    }

    public boolean isForUpdate() {
      return forupdate_;
    }

    public String toString() {
      StringBuffer stringbuffer = new StringBuffer("select ");
      if (distinct_)
        stringbuffer.append("distinct ");
      stringbuffer.append(select_.get(0).toString());
      for (int i = 1; i < select_.size(); i++)
        stringbuffer.append(", " + select_.get(i).toString());
      stringbuffer.append(" from ");
      stringbuffer.append(from_.get(0).toString());
      for (int j = 1; j < from_.size(); j++)
        stringbuffer.append(", " + from_.get(j).toString());
      if (where_ != null)
        stringbuffer.append(" where " + where_.toString());
      if (groupby_ != null)
        stringbuffer.append(" " + groupby_.toString());
      if (setclause_ != null)
        stringbuffer.append(" " + setclause_.toString());
      if (orderby_ != null) {
        stringbuffer.append(" order by ");
        stringbuffer.append(orderby_.get(0).toString());
        for (int k = 1; k < orderby_.size(); k++)
          stringbuffer.append(", " + orderby_.get(k).toString());
      }
      if (forupdate_)
        stringbuffer.append(" for update");
      return stringbuffer.toString();
    }
    ArrayList select_;
    boolean distinct_;
    ArrayList from_;
    SQLExp where_;
    SQLGroupBy groupby_;
    SQLExpression setclause_;
    ArrayList orderby_;
    boolean forupdate_;
  }

  public class SQLSelectItem extends SQLAliasedName {

    public SQLSelectItem() {
      expression_ = null;
      aggregate_ = null;
    }

    public SQLSelectItem(String s) {
      super(s, 2);
        /* FORM_COLUMN */
      expression_ = null;
      aggregate_ = null;
      setAggregate(getAggregateCall(s));
    }

    public SQLExp getExpression() {
      if (isExpression())
        return expression_;
      if (isWildcard())
        return null;
      else
        return new SQLConstant(getColumn(), 0);
    }

    public void setExpression(SQLExp zexp) {
      expression_ = zexp;
      strform_ = expression_.toString();
    }

    public boolean isExpression() {
      return expression_ != null;
    }

    public void setAggregate(String s) {
      aggregate_ = s;
    }

    public String getAggregate() {
      return aggregate_;
    }
    SQLExp expression_;
    String aggregate_;
  }

  public interface SQLStatement extends Serializable {
  }

  public class SQLTransactStmt implements SQLStatement {

    public SQLTransactStmt(String s) {
      comment_ = null;
      readOnly_ = false;
      statement_ = new String(s);
    }

    public void setComment(String s) {
      comment_ = new String(s);
    }

    public String getComment() {
      return comment_;
    }

    public boolean isReadOnly() {
      return readOnly_;
    }
    String statement_;
    String comment_;
    boolean readOnly_;
  }
  private Hashtable fcts_ = null;
  private HashMap fcts_type = null;
  private HashMap fcts_user = null;

  private void addCustomFunctions() {
    addCustomFunction("CubeValue", 4, this.TYPE_CHARACTER);
    addCustomFunction("CubeDimension", 3, this.TYPE_CHARACTER);
    addCustomFunction("if", 3, this.TYPE_CHARACTER);
    addCustomFunction("SlidingScale".toLowerCase(), 4, this.TYPE_CHARACTER);
    addCustomFunction("convert", 3, this.TYPE_CHARACTER);
    addCustomFunction("sumn", 99, this.TYPE_CHARACTER);
    addCustomFunction("maximum", 99, this.TYPE_CHARACTER);
    addCustomFunction("cap", 99, this.TYPE_CHARACTER);
    addCustomFunction("floor", 99, this.TYPE_CHARACTER);
  }

  public void addCustomFunction(String s, int i, char type) {
    if (fcts_ == null) {
      fcts_ = new Hashtable();
      fcts_type = new HashMap();
      fcts_user = new HashMap();
    }
    if (i <= 0)
      i = 1;
    fcts_.put(s.toUpperCase(), new Integer(i));
    fcts_type.put(s.toUpperCase(), new Character(type));
  }

  public boolean isTextFunction(String expr) {
    Character chrKey = (Character)fcts_type.get(expr.toUpperCase());
    if (chrKey == null)
      return false;
    char typeID = (chrKey).charValue();
    return (typeID == this.TYPE_CHARACTER);
  }

  private boolean isUserDefinedFunction(String name) {
    Object userFnc = fcts_user.get(name.toUpperCase());
    return !(userFnc == null);
  }


  public int isCustomFunction(String s) {
    Integer integer;
    if (s == null || s.length() < 1 || fcts_ == null || (integer = (Integer)fcts_.get(s.toUpperCase())) == null)
      return -1;
    else
      return integer.intValue();
  }

  public boolean isAggregate(String s) {
    s = s.toUpperCase().trim();
    return s.equals("SUM") || s.equals("AVG") || s.equals("MAX") || s.equals("MIN") || s.equals("COUNT") || fcts_ != null && fcts_.get(s) != null;
  }

  public String getAggregateCall(String s) {
    int i = s.indexOf('(');
    if (i <= 0)
      return null;
    String s1 = s.substring(0, i);
    if (isAggregate(s1))
      return s1.trim();
    else
      return null;
  }

  public void addVars(String vars, String values) {
    tuple = new JWebSQLTuple(vars);
    tuple.setRow(values);
  }


  public void setVars(JWebDataWindowLite dw) {
    tuple = new JWebSQLTuple(dw);
    // tuple.setRow(values);
  }

  public void setVarsRow(JWebDataWindowLite dw, int row) {
    if (tuple == null)
      tuple = new JWebSQLTuple(dw);
    tuple.setRow(dw, row);
    // tuple.setRow(values);
    //System.err.println(tuple.toString());
  }
  

  private SQLExpression getSQLExpression(String expr) throws ParseException {
      // addVars("Col1,Col2","300,40000.0");
      initParser(new ByteArrayInputStream(("Select * from Dual where " + expr + ";").getBytes()));
      SQLStatement st = readStatement();
      SQLQuery q = (SQLQuery)st;
      SQLExp e = q.getWhere();
      return (SQLExpression)q.getWhere();
  }

  private String expr; 
  public boolean evaluateExpr(String expr)throws ParseException {
    return evaluateExpr(expr,true);
  }
  public boolean evaluateExpr(String expr,boolean overrideExpr) throws ParseException {
    try {
    	this.expr=expr;
      SQLExpression sql_exp = getSQLExpression(expr);
      if (overrideExpr) current_sql_exp = sql_exp;
      SQLEval eval = new SQLEval();
      return eval.eval(tuple, sql_exp);
    } catch (Exception ex) {
      ex.printStackTrace();
      System.err.println("Parse Exception:"+this.expr);
    }
    return false;
  }
  public boolean evaluateExpr(SQLExpression sql_exp) throws ParseException,SQLException{
      SQLEval eval = new SQLEval();
      return eval.eval(tuple, sql_exp);
  }
  
  private SQLExpression parsedExpr = null;

  public boolean parseExpr(String expr) {
    try {
      parsedExpr = getSQLExpression(expr);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return false;
  }

  public String[] getOperands(String expr) throws ParseException {
    parsedExpr = getSQLExpression(expr);
    if (parsedExpr == null)
      return null;
    String[] operands = new String[parsedExpr.nbOperands()];
    for (int i = 0; i < parsedExpr.nbOperands(); i++) {
      SQLExp exp = (SQLExp)parsedExpr.getOperand(i);
      operands[i] = exp.toString();
      //System.err.println(operands[i]);
    }
    return operands;
  }

  public String evaluateText(String expr) throws ParseException{
    try {
      SQLExpression sql_exp = getSQLExpression(expr);
      SQLEval eval = new SQLEval();
      return eval.evalTextExp(tuple, sql_exp);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }
  
  public String normalizeFuncs(String fnc) {
    return  exprFuncs.removeQuotesFromParams(fnc);
  }
  private SQLExpression current_sql_exp;
  public SQLExpression getCurrentExpr() {
    return current_sql_exp;
  }

  public double evaluateNumericFunc(String fnc) {
    String fncExpr = exprFuncs.removeQuotesFromParams(fnc);
    if (trace) System.out.println("Function: " + fncExpr);
    return evaluateNumeric(fncExpr);
  }
  
  
  public double evaluateNumeric(String expr) {
    try {
      if (trace)
        System.out.println("Expression:" + expr);
      this.expr=expr;
      current_sql_exp = getSQLExpression(expr);
      SQLEval eval = new SQLEval();
      return eval.evalNumericExp(tuple, current_sql_exp);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return 0;
  }
  
  
  public double evaluateNumericFunc(SQLExpression sql_exp) throws Exception {
    return evaluateNumeric(sql_exp);
  }
  public double evaluateNumeric(SQLExpression sql_exp) throws Exception {
    SQLEval eval = new SQLEval();
    return eval.evalNumericExp(tuple, sql_exp);
  }
  
  
  private String tokenImage[] = { "<EOF>", "\" \"", "\"\\t\"", "\"\\r\"", "\"\\n\"", "\"ALL\"", "\"AND\"", "\"ANY\"", "\"AS\"", "\"ASC\"", "\"AVG\"", "\"BETWEEN\"", "\"BINARY_INTEGER\"", "\"BOOLEAN\"", "\"BY\"", "\"CHAR\"", "\"COMMENT\"", "\"COMMIT\"", "\"CONNECT\"", "\"COUNT\"", "\"DATE\"", "\"DELETE\"", "\"DESC\"", "\"DISTINCT\"", "\"EXCLUSIVE\"", "\"EXISTS\"", "\"EXIT\"", "\"FLOAT\"", "\"FOR\"", "\"FROM\"", "\"GROUP\"", "\"HAVING\"", "\"IN\"", "\"INSERT\"", "\"INTEGER\"", "\"INTERSECT\"", "\"INTO\"", "\"IS\"", "\"LIKE\"", "\"LOCK\"", "\"MAX\"", "\"MIN\"", "\"MINUS\"", "\"MODE\"", "\"NATURAL\"", "\"NOT\"", "\"NOWAIT\"", "\"NULL\"", "\"NUMBER\"", "\"OF\"", "\"ONLY\"", "\"OR\"", "\"ORDER\"", "\"PRIOR\"", "\"QUIT\"", "\"READ\"", "\"REAL\"", "\"ROLLBACK\"", "\"ROW\"", "\"SELECT\"", "\"SET\"", "\"SHARE\"", "\"SMALLINT\"", "\"START\"", "\"SUM\"", "\"TABLE\"", "\"TRANSACTION\"", "\"UNION\"", "\"UPDATE\"", "\"VALUES\"", "\"VARCHAR2\"", "\"VARCHAR\"", "\"WHERE\"", "\"WITH\"", "\"WORK\"", "\"WRITE\"", "<S_NUMBER>", "<FLOAT>", "<INTEGER>", "<DIGIT>", "<LINE_COMMENT>", "<MULTI_LINE_COMMENT>", "<S_IDENTIFIER>", "<LETTER>", "<SUBSTRING>", "<SPECIAL_CHARS>", "<S_BIND>", "<S_CHAR_LITERAL>", "<S_QUOTED_IDENTIFIER>", "\"(\"", "\",\"", "\")\"", "\";\"", "\"=\"", "\".\"", "\"!=\"", "\"#\"", "\"<>\"", "\">\"", "\">=\"", "\"<\"", "\"<=\"", "\"+\"", "\"-\"", "\"*\"", "\".*\"", "\"?\"", "\"||\"", "\"/\"", "\"**\"", "LOOP BY", "UNTIL" };
  private ArrayList varNames = new ArrayList();

  public void initVars() {
    varNames.clear();
  }

  public ArrayList getVars() {
    return varNames;
  }

  public JWebSQLTuple getTuple(String varNames) {
    return new JWebSQLTuple(varNames);
  }

  public JWebSQLTuple getTuple() {
    return new JWebSQLTuple();
  }

  public SQLEval getEvaluater() {
    return new SQLEval();
  }
  private JWebSQLTuple tuple;

  public void setDataSources(JWebDataWindowLite dwDataSource) {
    this.dwData = dwDataSource;
  }



  public void setTrace(boolean trace) {
    this.trace = trace;
  }

  public boolean isTrace() {
    return trace;
  }
  
  public void clear() {
    
  }
}
