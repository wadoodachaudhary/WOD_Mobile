package com.jtools.javawebdbms;
import com.jtools.javawebutils.JWebUtils;
import java.util.Stack;
import com.jtools.javawebutils.JWebConstants;
public class JWebExprFunctions implements JWebConstants{
  boolean trace=false;
  public JWebExprFunctions() {
    super();
  }
  
  public String makeParamToken(int startToken, int endToken, String s, String params, int paramNo) {
    String param = s.substring(startToken, endToken);
    String[] specialNumbers = new String[] { "bp", "bn", "mn","bps" };
    String[] operators = new String[] {"<>","<=",">=",">","<","-","+"};
    for (int i = 0; i < specialNumbers.length; i++) {
			String sn = specialNumbers[i];
			if (param.endsWith(sn)) {
				//System.out.println("Param:"+param);
				String no = param.substring(0,param.length()-sn.length());
				char c = no.charAt(0);
		    if (c=='-') c = no.charAt(1);
		    if ((c >= '0' && c <= '9'))  {
		        return JWebUtils.ifConcat(params, "'" + param + "'", ",");
		    }
		    
			}
			
		}
    if (JWebUtils.ifTextExists(operators, param)) {
    	//System.out.println("Param:"+param);
    	return JWebUtils.ifConcat(params, "'" + param + "'", ",");
    }
    return param.indexOf(SINGLE_QUOTE) >= 0 ? JWebUtils.ifConcat(params, "('" + param + "')", ",") : JWebUtils.ifConcat(params,"("+param+")", ",");
  }

  public double transformNumber(String no) {
    String[] units = new String[] { "bp", "bn", "mn","k","bps" };
    char c = no.charAt(0);
    if (c=='-') c = no.charAt(1);
    if (c >= '0' && c <= '9') {
      if (JWebUtils.ifTextExists(units, no)) {
        for (int i = 0; i < units.length; i++) {
          String numberPart = no.substring(0, no.length() - units[i].length());
          String unitPart = no.substring(no.length() - units[i].length());
          if (JWebUtils.isSame(unitPart, units[i])) {
            if (units[i].equalsIgnoreCase("bn")) {
              return Double.parseDouble(numberPart) * 1000000000;
            } else if (units[i].equalsIgnoreCase("mn")) {
              return Double.parseDouble(numberPart) * 1000000;
            } else if (units[i].equalsIgnoreCase("k")) {
              return Double.parseDouble(numberPart) * 1000;
            }
            if (units[i].equalsIgnoreCase("bp") || units[i].equalsIgnoreCase("bps")) {
              return Double.parseDouble(numberPart) / 10000;
            }
          }
        }
      }
    }
    return Double.MIN_VALUE;
  }

  public String transformName(String fncName) {
    if (JWebUtils.isSame(fncName, "Max"))
      return "Maximum";
    if (JWebUtils.isSame(fncName, "Min"))
      return "Minimum";
    return fncName;
  }

  public String removeQuotesFromParams(String fnc) {
    String s = fnc;
    String fncName = JWebUtils.getToken(fnc, "(").trim();
    s = JWebUtils.replaceStr(s, "'", SINGLE_QUOTE);
    String main = "";
    int stackNo = 0;
    int startPos = 0, endPos = 0;
    int startToken = 1, endToken = 0;
    boolean firstParen = false;
    Stack<Character> stack = new Stack<Character>();
    String params = "";
    int paramNo = 0;
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (c == COMMA) {
        if (stackNo == 1) {
          endToken = i;
          //System.out.println("Token at:" + startToken + "," + endToken);
          params = makeParamToken(startToken, endToken, s, params, ++paramNo);
          startToken = i + 1;
        }
      } else if (c == L_PAREN) {
        stack.push(L_PAREN);
        stackNo = stackNo + 1;
        startPos = i;
        if (stackNo == 1)
          startToken = i + 1;
      } else if (c == R_PAREN) {
        if (stack.isEmpty()) {
          System.err.println("Error: no matching Left parenthesis for " + s);
        } else if (stack.pop() != L_PAREN) {
          System.err.println("Error: no matching Left parenthesis for " + s);
        }
        endPos = i;
        if (stackNo > 1) {
          if (trace)
            System.out.println("Immediate Token:" + startPos + "," + endPos);
        }
        stackNo = stackNo - 1;
        if (stackNo == 0) {
          endToken = i;
          //System.out.println("Token at:" + startToken + "," + endToken);
          params = makeParamToken(startToken, endToken, s, params, ++paramNo);
        }
      }
      // ignore all other characters
    }
    //System.out.println("Stack : " + stack.isEmpty() + " Fnc ="+fncName + "("+params+")");
    return transformName(fncName) + "(" + params + ")";
  }
}
