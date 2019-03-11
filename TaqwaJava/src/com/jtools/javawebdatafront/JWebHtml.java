package com.jtools.javawebdatafront;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import java.awt.*;
import java.util.*;
import com.jtools.javawebutils.JWebConstants;
import com.jtools.javawebutils.JWebEnviornment;
import com.jtools.javawebutils.JWebUtils;
public class JWebHtml implements JWebConstants {
  public static final int BACKCOLOR = 0;
  public static final int TEXT_COLOR = 1;
  public static final int TEXTSIZE = 2;
  public static final int FONT = 3;
  public static final int END_FONT = 4;
  public static final int ALIGN = 5;
  public static final int ANCHOR = 6;
  public static final int END_ANCHOR = 7;
  public static final int SPAN = 8;
  public static final int END_SPAN = 9;
  public static final int FONT_STYLE = 9;
  public static final int STYLE = 10;
  public static final int FONT_SIZE = 11;
  public static final int BACKGROUND = 12;
  public static final int FONT_COLOR = 13;
  public static final int TEXT_ALIGN = 14;
  public static final int TEXT_STYLE = 15;
  public static final int TEXT_WEIGHT = 16;
  public static final int FONT_FAMILY = 17;
  public static final String CENTER = "center";
  public static final String LEFT = "left";
  public static final String RIGHT = "right";
  public static final String TOP = "top";
  public static final String BOTTOM = "bottom";
  public static final int INPUT_SUBMIT = 18;
  public static final int INPUT_TEXT = 19;
  public static final int SELECT = 20;
  public static final int END_SELECT = 21;
  public static final int OPTION = 22;
  public static final int OPTION_SELECTED = 23;
  public static final int CHECKBOX = 24;
  public static final int INPUT_IMAGE = 25;
  public static final int BACKGROUND_IMAGE = 26;
  public static final int STYLE_ATTRIBUTES = 27;
  public static final int WIDTH = 28;
  public static final int FOREGROUND_IMAGE = 29;
  public static final int VALIGN = 30;
  public static final int STYLE_VALIGN = 31;
  public static final int STYLE_ALIGN = 32;
  public static final int STYLE_BORDER = 33;
  public static final int SPACER = 34;
  public static final int IMAGE = 35;
  public static final int SPACE = 36;
  public static final int TITLE = 37;
  public static final int ALT = 38;
  public static final int DIV = 39;
  public static final int END_DIV = 40;
  public static final int POSITION_ABSOLUTE = 41;
  public static final int POSITION_RELATIVE = 42;
  public static final int RECTANGLE = 43;
  public static final int STYLE_EXPR = 44;
  public static final int OPTION_2 = 45;
  public static final int OPTION_SELECTED_2 = 46;
  public static final int SPAN_STYLE = 47;
  public static final int BACKGROUND_TRANSPARENT = 48;
  public static final int HEIGHT = 49;
  public static final int OVERFLOW = 50;
  public static final int CURSOR = 51;
  public static final int INPUT_LABEL = 52;
  public static final int BACKGROUND_IMAGE_STYLE = 53;
  public static final int SELECT_LIST = 54;
  public static final int TEXT_AREA = 55;
  public static final int INPUT_BUTTON = 56;
  public static final int INPUT_VAR = 57;
  public static final int LINEBREAK = 58;
  public static final int INPUT_TAB = 59;
  public static final int MARGIN_LEFT = 60;
  public static final int INPUT_PASSWORD = 61;
  public static final int INPUT_FILE = 62;
  public static final int INPUT_BUTTON_UPLOAD = 63;
  public static final int LABEL = 64;
  public static final int RECTANGLE_EXPR = 65;
  public static final short HREF = 66;
  public static final short BOUNDS = 67;
  public static final short FIT_TO_SIZE = 68;
  public static final short ID = 69;
  public static final short DRAG_IMAGE = 70;
  public static final int ANCHOR_NEW = 71;
  public static final int HREF_NEW = 72;
  public static final int CELL_ID = 73;
  // JQuery
  public static final int INPUT_SUBMIT_JQ = 201;
  public static final int INPUT_TEXT_JQ = 202;
  public static final int SELECT_LIST_JQ = 203;
  public static final int INPUT_LABEL_JQ = 204;
  public static final int TEXT_AREA_JQ = 205;
  public static final int INPUT_BUTTON_JQ = 206;
  public static final int INPUT_VAR_JQ = 207;
  public static final int LINEBREAK_JQ = 208;
  public static final int INPUT_TAB_JQ = 209;
  public static final int MARGIN_LEFT_JQ = 210;
  public static final int INPUT_PASSWORD_JQ = 211;
  public static final int INPUT_FILE_JQ = 212;
  public static final int INPUT_BUTTON_UPLOAD_JQ = 213;
  // Events
  public static final int EVENT = 100;
  public static final int EVENT_ON_CHANGE_SUBMIT = 101;
  public static final int EVENT_CLICK_ON_CHANGE = 102;
  public static final int EVENT_ONCLICK_SUBMIT = 103;
  public static final int EVENT_ONCLICK_POPUP = 104;
  public static final int EVENT_ONCLICK_MOVE_ITEMS = 105;
  // ============================== Mahmood Code Begin 12-July-2006
  // =======================================
  public static final int EVENT_ONCLICK_MOVE_ITEMS_SORTED = 113;
  // ============================== Mahmood Code end 12-July-2006
  // =======================================
  public static final int EVENT_ONCLICK_CLOSE = 106;
  public static final int EVENT_ON_ROLLOVER_CHANGE_BACKGROUND = 107;
  public static final int EVENT_ON_ROLLOVER_CHANGE_UNDERLINE = 108;
  public static final int EVENT_DRAG_AND_RESIZE = 109;
  public static final int EVENT_DRAG = 110;
  public static final int EVENT_RESIZE = 111;
  public static final int EVENT_DRAG_AND_DROP = 112;
  public static final String SQUARE_BRACKET_START = "_SQBRKXXTSTRT_";
  public static final String SQUARE_BRACKET_END = "_SQRBRXXKTEND_";
  public static final String AUTO = "auto";
  public static final String HIDDEN = "hidden";
  public static final String CURSOR_auto = "auto";
  public static final String CURSOR_crosshairs = "auto";
  public static final String CURSOR_default = "default";
  public static final String CURSOR_E_Resize = "e-resize";
  public static final String CURSOR_hand = "hand";
  public static final String CURSOR_help = "help";
  public static final String CURSOR_move = "move";
  public static final String CURSOR_N_Resize = "n-resize";
  public static final String CURSOR_NE_neResize = "ne-resize";
  public static final String CURSOR_NW_Resize = "nw-resize";
  public static final String CURSOR_pointer = "pointer";
  public static final String VAR_X = "VAR_X";
  public static final String VAR_Y = "VAR_Y";
  public static final String VAR_WIDTH = "VAR_WIDTH";
  public static final String VAR_HEIGHT = "VAR_HEIGHT";
  public static final String CLICKED_X = "CLICKED_X";
  public static final String CLICKED_Y = "CLICKED_Y";
  public static final String CLICKED = "CLICKED";
  public static final String PIXEL = "px";
  public static final short JSP_GET_ELEMENT_HEIGHT = 0;
  public static final short JSP_GET_ELEMENT_WIDTH = 1;
  public static final short JSP_EXPRESSION = 2;
  public static final short JQ_GET_BUTTON_CLICK=1;
  public static final String DRAG_SOURCE_VAR = "DRAG_SOURCE_VAR";
  public static final String DRAG_TARGET_VAR = "DRAG_TARGET_VAR";
  public static final String VAR_CELL_ID = "CELL_ID_";
  public static final String SCRIPT_START="<script type=\"text/javascript\">";
  public static final String SCRIPT_END="</script>";
  public static final String FUNCTION_JQ_START="$(function() { ";
  public static final String FUNCTION_JQ_END = " });";
  public static final String VAR_JQ_START = "$('#";
  public static final String VAR_JQ_END = "')";
  public static final String VAR_JQ_VAL = ".val()";
  public static final String VAR_JQ_VAL_START = ".val(";
  public static final String VAR_JQ_VAL_END = ")";

  public JWebHtml() {
  }

  public static String getTag(int tagName, Object parameter) {
    if (tagName == BACKCOLOR)
      return " BGCOLOR=\"" + JWebUtils.getHexForColor((Color)parameter) + "\"";
    else if (tagName == FONT_COLOR)
      return " FONT COLOR=\"" + JWebUtils.getHexForColor((Color)parameter) + "\"";
    else if (tagName == TEXTSIZE)
      return " FONT SIZE=" + (String)parameter;
    else if (tagName == FONT)
      return " <FONT COLOR=\"" + JWebUtils.getHexForColor((Color)((ArrayList)parameter).get(0)) + "\" FONT SIZE=" + (String)((ArrayList)parameter).get(1) + " >";
    else if (tagName == STYLE) {
      if (parameter == null)
        return " Style=\"";
      else
        return " Style=\"" + parameter + "\"";
    } else if (tagName == STYLE_ATTRIBUTES) {
      String style = " Style=\"";
      ArrayList params = (ArrayList)parameter;
      for (int i = 0; i < params.size(); i++) {
        style += " " + (String)params.get(i);
      }
      style += "\"";
      return style;
    } else if (tagName == FONT_SIZE)
      return "font-size: " + (String)parameter + "pt;";
    else if (tagName == BACKGROUND)
      return "background:" + JWebUtils.getHexForColor((Color)parameter) + ";";
    else if (tagName == BACKGROUND_TRANSPARENT)
      return "background:transparent;";
    else if (tagName == BACKGROUND_IMAGE_STYLE)
      return "background-image:url('" + parameter + "');";
    else if (tagName == WIDTH)
      return "width:" + (String)parameter + ";";
    else if (tagName == BACKGROUND_IMAGE) {
      return " background=\"" + ((String)parameter) + "\" ";
    } else if (tagName == LINEBREAK) {
      return "<BR>";
    } else if (tagName == TEXT_COLOR)
      return "color:" + JWebUtils.getHexForColor((Color)parameter) + ";";
    else if (tagName == CURSOR)
      return "cursor:" + parameter + ";";
    else if (tagName == MARGIN_LEFT)
      return "margin-left:" + parameter + ";";
    else if (tagName == SELECT) {
      String name = getHtmlVarName((String)((ArrayList)parameter).get(0));
      return "<SELECT NAME=\"" + name + "\" " + addElementStyle(parameter, 1) + addElementEvent(parameter, 2) + ">";
    } else if (tagName == SELECT_LIST) {
      String name = (String)((ArrayList)parameter).get(0);
      return "<SELECT NAME=\"" + name + "\" multiple=\"true\"" + addElementStyle(parameter, 1) + addElementEvent(parameter, 2) + ">";
    } else if (tagName == BOUNDS) {
      if (parameter == null)
        return "";
      Rectangle r = (Rectangle)parameter;
      if (r.width == 0 || r.height == 0)
        return "";
      // return "";
      return "width:" + r.width + ";" + " height:" + r.height + ";";
    } else if (tagName == FIT_TO_SIZE) {
      return "width:100%" + " height:100%";
    } else if (tagName == END_SELECT)
      return "</SELECT>";
    else if (tagName == INPUT_VAR) {
      String name = (String)parameter;
      return getHiddenVar(name, "null");
    } else if (tagName == OPTION)
      return "<OPTION> " + (String)parameter;
    else if (tagName == OPTION_SELECTED)
      return "<OPTION SELECTED> " + (String)parameter;
    else if (tagName == OPTION_2) {
      ArrayList parameters = (ArrayList)parameter;
      String data = "value=\"" + (String)parameters.get(0) + "\"";
      String disp = (String)parameters.get(1);
      String style = (parameters.size() > 2 ? (" " + (String)parameters.get(2) + " ") : "");
      return "<OPTION " + style + data + "> " + disp + "</OPTION>";
    } else if (tagName == OPTION_SELECTED_2) {
      ArrayList parameters = (ArrayList)parameter;
      String data = "value=\"" + (String)parameters.get(0) + "\"";
      String disp = (String)parameters.get(1);
      String style = (parameters.size() > 2 ? (" " + (String)parameters.get(2) + " ") : "");
      return "<OPTION selected=\"1\" " + style + data + "> " + disp + "</OPTION>";
    } else if (tagName == END_FONT)
      return "</FONT>";
    else if (tagName == TEXT_ALIGN)
      return " text-align:" + parameter + ";";
    else if (tagName == TEXT_WEIGHT)
      return " font-weight:" + parameter + ";";
    else if (tagName == TEXT_STYLE)
      return " font-style:" + parameter + ";";
    else if (tagName == FONT_FAMILY)
      return " font-family:" + parameter + ";";
    else if (tagName == ALIGN)
      return " align=\"" + parameter + "\"";
    else if (tagName == VALIGN)
      return " valign=\"" + parameter + "\"";
    else if (tagName == STYLE_ALIGN)
      return " align:" + parameter + ";";
    else if (tagName == STYLE_VALIGN)
      return " valign:" + parameter + ";";
    else if (tagName == ANCHOR)
      return "<A href= \"" + parameter + "\">";
    else if (tagName == ANCHOR_NEW)
      return "<A href= \"" + parameter + "\" target=_new>";
    else if (tagName == END_ANCHOR)
      return "</A>";
    else if (tagName == HREF)
      return " href= \"" + parameter + "\"";
    else if (tagName == HREF_NEW)
      return " href= \"" + parameter + "\" target=_new ";
    else if (tagName == SPAN) {
      if (parameter == null)
        return "<SPAN >";
      else
        return "<SPAN ID='" + parameter + "'>";
    } else if (tagName == END_SPAN)
      return "</SPAN>";
    else if (tagName == ID) {
      if (parameter == null)
        return "";
      else
        return "ID='" + parameter + "'";
    } else if (tagName == CELL_ID) {
      if (parameter == null)
        return "";
      else
        return "ID='" + VAR_CELL_ID + parameter + "'";
    } else if (tagName == FOREGROUND_IMAGE) {
      ArrayList parameters = (ArrayList)parameter;
      String src = ((String)parameters.get(0));
      String name = ((String)parameters.get(1));
      String events = addElementEvent(parameter, 3);
      String vars = "";
      if (JWebUtils.isNotNull(events)) {
        vars = getRectangleVar(name);
      }
      return vars + " <img " + "ID='" + name + "' name=\"" + name + "\" src=\"" + src + "\"" + addElementStyle(parameter, 2) + addElementEvent(parameter, 3) + " border=0>";
    } else if (tagName == DRAG_IMAGE) {
      ArrayList parameters = (ArrayList)parameter;
      String name = ((String)parameters.get(0));
      String src = ((String)parameters.get(1));
      // String events=addElementEvent(parameter,3);
      // String vars="";
      // if (JWebUtils.isNotNull(events)) {
      // vars = getRectangleVar(name);
      // }
      return " <img " + "ID='" + name + "' name=\"" + name + "\" src=\"" + src + "\"" + addElementStyle(parameter, 2) + addElementEvent(parameter, 3) + " border=0>";
    } else if (tagName == SPAN_STYLE) {
      return "<SPAN " + parameter + ">";
    } else if (tagName == IMAGE) {
      ArrayList parameters = (ArrayList)parameter;
      String name = getHtmlVarName((String)parameters.get(0));
      String tip = (String)parameters.get(1);
      String src = (String)parameters.get(2);
      String align = "Center";
      if (parameters.size() > 3) {
        align = (String)parameters.get(3);
      }
      return "<img " + "ID=\"" + name + "\"" + " name=\"" + name + "\" src=\"" + src + "\" align=\"" + align + "\" ALT=\"" + tip + "\">";
    } else if (tagName == INPUT_SUBMIT) {
      ArrayList parameters = (ArrayList)parameter;
      String name = getHtmlVarName((String)parameters.get(0));
      String text = (String)parameters.get(1);
      String tip = parameters.size() == 3 ? "ALT=\"" + (String)parameters.get(1) + "\"" : "";
      return "<input type=\"submit\" name=\"" + name + "\" " + addElementStyle(parameter, 2) + addElementEvent(parameter, 3) + " size=\"20\" value=\"" + text + "\"" + tip + ">";
    } else if (tagName == INPUT_SUBMIT_JQ) {
      //e.g.  <button id="create-user">Create new user 2</button>
      ArrayList parameters = (ArrayList)parameter;
      String name = getHtmlVarName((String)parameters.get(0));
      String text = (String)parameters.get(1);
      String style = (String)parameters.get(2);
      String script = parameters.size() == 4 ?  (String) parameters.get(3) : "";
      String scriptHtml="";
      if (JWebUtils.isNotNull(script)) {
        scriptHtml = SCRIPT_START+ getJQ(JQ_GET_BUTTON_CLICK,name,script) +SCRIPT_END;
      }
      String tip = parameters.size() == 4 ? "ALT=\"" + (String)parameters.get(1) + "\"" : "";
      return scriptHtml+JWebUtils.getNewLineChar()+" <button id=\"" + name + "\" > " + text + "</button>";
    } else if (tagName == INPUT_FILE) {
      ArrayList parameters = (ArrayList)parameter;
      String name = getHtmlVarName((String)parameters.get(0));
      String text = (String)parameters.get(1);
      String tip = parameters.size() == 3 ? "ALT=\"" + (String)parameters.get(1) + "\"" : "";
      return "<input type=\"file\" name=\"" + name + "\" " + addElementStyle(parameter, 2) + addElementEvent(parameter, 3) + " size=\"20\" value=\"" + text + "\"" + tip + ">";
    } else if (tagName == INPUT_BUTTON_UPLOAD) {
      ArrayList parameters = (ArrayList)parameter;
      String name = getHtmlVarName((String)parameters.get(0));
      String value = (String)parameters.get(1);
      String tip = parameters.size() == 3 ? "ALT=\"" + (String)parameters.get(1) + "\"" : "";
      String nextURL = "\"" + JWebEnviornment.getNextURL() + "\"";
      return "<INPUT TYPE=button name=\"" + name + "\" value=\"" + value + "\"" + " onClick='submitmulti(" + nextURL + ")'" + ">";
    } else if (tagName == INPUT_BUTTON) {
      ArrayList parameters = (ArrayList)parameter;
      String name = getHtmlVarName((String)parameters.get(0));
      String text = (String)parameters.get(1);
      String tip = parameters.size() == 3 ? "ALT=\"" + (String)parameters.get(1) + "\"" : "";
      return "<input type=\"button\" name=\"" + name + "\" " + addElementStyle(parameter, 2) + addElementEvent(parameter, 3) + " size=\"20\" value=\"" + text + "\"" + tip + ">";
    } else if (tagName == INPUT_TAB) {
      ArrayList parameters = (ArrayList)parameter;
      String name = getHtmlVarName((String)parameters.get(0));
      String text = (String)parameters.get(1);
      String tip = parameters.size() == 3 ? "ALT=\"" + (String)parameters.get(1) + "\"" : "";
      return getHiddenVar(name, text) + "<a name=\"" + name + "_1\" " + addElementStyle(parameter, 2) + addElementEvent(parameter, 3) + tip + " onClick='setLabelButtonValue(this," + name + ")'" + ">" + text + "</a>";
    } else if (tagName == LABEL) {
      ArrayList parameters = (ArrayList)parameter;
      String name = getHtmlVarName((String)parameters.get(0));
      String text = (String)parameters.get(1);
      String style = (String)parameters.get(2);
      String events = addElementEvent(parameter, 3);
      String vars = "";
      if (JWebUtils.isNotNull(events)) {
        vars = getRectangleVar(name);
      }
      return vars + " <a ID='" + name + "' name=\"" + name + "\" " + style + events + ">" + text + "</a>";
    } else if (tagName == INPUT_LABEL) {
      ArrayList parameters = (ArrayList)parameter;
      String name = getHtmlVarName((String)parameters.get(0));
      String text = (String)parameters.get(1);
      String tip = parameters.size() == 3 ? "ALT=\"" + (String)parameters.get(1) + "\"" : "";
      // JWebUtils.printCollection(parameters);
      return getHiddenVar(name, text) + "<a name=\"" + name + "_1\" " + addElementStyle(parameter, 2) + addElementEvent(parameter, 4) + tip + ">" + text + "</a>";
    } else if (tagName == TEXT_AREA) {
      ArrayList parameters = (ArrayList)parameter;
      String name = getHtmlVarName((String)parameters.get(0));
      String text = (String)parameters.get(1);
      String tip = parameters.size() == 3 ? "TITLE=\"" + (String)parameters.get(1) + "\"" : "";
      return "<TextArea name=\"" + name + "\" " + addElementStyle(parameter, 2) + addElementEvent(parameter, 3) + tip + ">" + text + "</TextArea>";
    } else if (tagName == INPUT_IMAGE) {
      ArrayList parameters = (ArrayList)parameter;
      String name = getHtmlVarName((String)((ArrayList)parameter).get(0));
      String text = (String)((ArrayList)parameter).get(1);
      String src = (String)((ArrayList)parameter).get(2);
      String align = (String)((ArrayList)parameter).get(3);
      return "<input type=\"image\" name=\"" + name + "\" " + addElementStyle(parameter, 4) + " src=\"" + src + "\" align=\"" + align + "\" ALT=\"" + text + "\">";
    } else if (tagName == CHECKBOX) {
      String name = getHtmlVarName((String)((ArrayList)parameter).get(0));
      String text = (String)((ArrayList)parameter).get(1);
      String state = (String)((ArrayList)parameter).get(2);
      return getHiddenVar(name, text) + "<input type=\"checkbox\" name=\"" + name + "_1\" " + addElementStyle(parameter, 3) + " value=\"" + text + "\" " + state + " onClick=\"setCheckboxValue(this," + name + ")\"" + ">";
    } else if (tagName == TITLE) {
      return "TITLE=\"" + (String)parameter + "\"";
    } else if (tagName == ALT) {
      return "ALT=\"" + (String)parameter + "\"";
    } else if (tagName == INPUT_TEXT) {
      ArrayList parameters = (ArrayList)parameter;
      String name = getHtmlVarName((String)parameters.get(0));
      String text = (String)parameters.get(1);
      String tip = parameters.size() == 4 ? getTag(TITLE, parameters.get(3)) : "";
      return "<input type=\"text\" name=\"" + name + "\" " + addElementStyle(parameter, 2) + " size=\"100\" value=\"" + text + "\"" + tip + ">";
    } else if (tagName == INPUT_TEXT_JQ) {
      ArrayList parameters = (ArrayList)parameter;
      String name = getHtmlVarName((String)parameters.get(0));
      String text = (String)parameters.get(1);
      String tip = parameters.size() == 4 ? getTag(TITLE, parameters.get(3)) : "";
      return "<input type=\"text\" name=\"" + name + "\" id=\"" + name + "\" " + " size=\"100\" value=\"" + text + "\"" + tip + ">";
    } else if (tagName == INPUT_PASSWORD) {
      ArrayList parameters = (ArrayList)parameter;
      String name = getHtmlVarName((String)parameters.get(0));
      String text = (String)parameters.get(1);
      String tip = parameters.size() == 4 ? getTag(TITLE, parameters.get(3)) : "";
      return "<input type=\"password\" name=\"" + name + "\" " + addElementStyle(parameter, 2) + " size=\"100\" value=\"" + text + "\"" + tip + ">";
    } else if (tagName == STYLE_BORDER) {
      String style = (String)((ArrayList)parameter).get(0);
      String width = (String)((ArrayList)parameter).get(1);
      String color = JWebUtils.getHexForColor((Color)((ArrayList)parameter).get(2));
      return "border-style:" + style + ";border-width:" + width + ";border-color:" + color + ";";
    } else if (tagName == SPACER) {
      String style = (String)((ArrayList)parameter).get(0);
      String height = (String)((ArrayList)parameter).get(1);
      String width = (String)((ArrayList)parameter).get(1);
      return "SPACER TYPE=\"" + style + "\" height=\"" + height + "\" width=" + width;
    } else if (tagName == SPACE) {
      int spaceCount = ((Integer)parameter).intValue();
      return JWebUtils.replicate("&nbsp;", spaceCount);
    } else if (tagName == EVENT) {
      return (String)parameter;
    } else if (tagName == DIV) {
      String objPos = (String)((ArrayList)parameter).get(0);
      String posRect = (String)((ArrayList)parameter).get(1);
      return "<DIV style=\"position:" + objPos + " " + posRect + "\">";
    } else if (tagName == END_DIV) {
      return "</DIV>";
    } else if (tagName == RECTANGLE) {
      Rectangle rect = (Rectangle)parameter;
      String posRect = " left:" + Integer.toString(rect.x) + "px;";
      posRect += " top:" + Integer.toString(rect.y) + "px;";
      posRect += " width:" + Integer.toString(rect.width) + "px;";
      posRect += " height:" + Integer.toString(rect.height) + "px;";
      return posRect;
    } else if (tagName == RECTANGLE_EXPR) {
      String[] dims = (String[])parameter;
      String posRect = " left:" + dims[0] + ";";
      posRect += " top:" + dims[1] + ";";
      posRect += " width:" + dims[2] + ";";
      posRect += " height:" + dims[3] + ";";
      return posRect;
    } else if (tagName == POSITION_ABSOLUTE) {
      return "position:absolute;";
    } else if (tagName == POSITION_RELATIVE) {
      return "position:relative;";
    } else if (tagName == HEIGHT) {
      return "height:" + parameter + ";";
    } else if (tagName == OVERFLOW) {
      return "overflow:" + parameter + ";";
    } else
      return "";
  }

  private static String addElementStyle(Object parameter, int count) {
    ArrayList params = (ArrayList)parameter;
    if (params.size() <= count)
      return "";
    if (params.get(count) instanceof java.lang.String) {
      return (String)params.get(count);
    }
    return "";
  }

  public static String getHtmlVarName(String varName) {
    String ovarName = varName;
    if (JWebUtils.isNotNull(varName)) {
      varName = JWebUtils.globalReplace(varName, "[", SQUARE_BRACKET_START);
      varName = JWebUtils.globalReplace(varName, "]", SQUARE_BRACKET_END);
      // varName = JWebUtils.replaceStr(varName," ","");
    }
    // System.err.println("HtmlVar:"+ovarName+" changed "+varName);
    return varName;
  }

  public static String getRegularVarName(String varName) {
    if (JWebUtils.isNotNull(varName)) {
      varName = JWebUtils.globalReplace(varName, SQUARE_BRACKET_START, "[");
      varName = JWebUtils.globalReplace(varName, SQUARE_BRACKET_END, "]");
    }
    return varName;
  }

  public static boolean isBoundsVar(String varName) {
    if (JWebUtils.isNotNull(varName)) {
      return (varName.endsWith(VAR_X) || varName.endsWith(VAR_Y) || varName.endsWith(VAR_WIDTH) || varName.endsWith(VAR_HEIGHT));
    }
    return false;
  }

  public static boolean isClickedVar(String varName) {
    if (JWebUtils.isNotNull(varName)) {
      return (varName.endsWith(CLICKED_X) || varName.endsWith(CLICKED_Y));
    }
    return false;
  }

  public static boolean isClickedValue(String value) {
    if (JWebUtils.isNotNull(value)) {
      return (value.equals(CLICKED));
    }
    return false;
  }

  public static String getBoundsName(String varName) {
    if (JWebUtils.isNotNull(varName)) {
      if (varName.endsWith(VAR_X)) {
        return varName.substring(0, varName.length() - VAR_X.length());
      } else if (varName.endsWith(VAR_Y)) {
        return varName.substring(0, varName.length() - VAR_Y.length());
      } else if (varName.endsWith(VAR_WIDTH)) {
        return varName.substring(0, varName.length() - VAR_WIDTH.length());
      } else if (varName.endsWith(VAR_HEIGHT)) {
        return varName.substring(0, varName.length() - VAR_HEIGHT.length());
      }
    }
    return varName;
  }

  public static String getClickedVarName(String varName) {
    if (JWebUtils.isNotNull(varName)) {
      if (varName.endsWith(CLICKED_X)) {
        return varName.substring(0, varName.length() - CLICKED_X.length()) + ".x";
      } else if (varName.endsWith(CLICKED_Y)) {
        return varName.substring(0, varName.length() - CLICKED_Y.length()) + ".y";
      }
    }
    return varName;
  }

  public static int getBoundsValue(String value) {
    if (value.endsWith(PIXEL)) {
      return Integer.parseInt(value.substring(0, value.length() - PIXEL.length()));
    } else
      return Integer.parseInt(value);
  }

  public static void setBoundsValue(String varName, String value, Rectangle rect) {
    int val = -1;
    // System.err.println(" "+varName+" "+value+ " "+rect);
    if (varName.endsWith(VAR_X)) {
      val = getBoundsValue(value);
      if (val != -1)
        rect.x = val;
    } else if (varName.endsWith(VAR_Y)) {
      val = getBoundsValue(value);
      if (val != -1)
        rect.y = val;
    } else if (varName.endsWith(VAR_WIDTH)) {
      val = getBoundsValue(value);
      if (val != -1)
        rect.width = val;
    } else if (varName.endsWith(VAR_HEIGHT)) {
      val = getBoundsValue(value);
      if (val != -1)
        rect.height = val;
    }
  }

  private static String addElementEvent(Object parameter, int count) {
    ArrayList params = (ArrayList)parameter;
    if (params.size() > count) {
      return getTag(EVENT, params.get(count));
    }
    return "";
  }

  public static String getEventCode(int eventID, String[] params) {
    if (eventID == EVENT_ON_CHANGE_SUBMIT) {
      return " onChange=\"submit()\";";
    } else if (eventID == EVENT_ONCLICK_SUBMIT) {
      return " onClick=\"submit()\";";
    } else if (eventID == EVENT_ONCLICK_POPUP) {
      String url = params[0] + (params.length > 1 ? "?" + params[1] : "");
      String wname = (params.length > 2 ? params[2] : "");
      String fsize = (params.length > 3 ? params[3] : "");
      return " onClick=\"javascript:actionPopup('" + url + "','" + wname + "','" + fsize + "')\";";
    } else if (eventID == EVENT_ONCLICK_MOVE_ITEMS) {
      String to = params[0];
      String from = params[1];
      return " onClick=\"javascript:moveSelectedOptions(" + to + "," + from + "," + to + VAR + "," + from + VAR + ")\";";
    }
    // ============================== Mahmood Code Begin 12-July-2006
    // =======================================
    else if (eventID == EVENT_ONCLICK_MOVE_ITEMS_SORTED) {
      String to = params[0];
      String from = params[1];
      return " onClick=\"javascript:moveSelectedOptionsSorted(" + to + "," + from + "," + to + VAR + "," + from + VAR + ")\";";
    }
    // ============================== Mahmood Code end 12-July-2006
    // =======================================
    else if (eventID == EVENT_ONCLICK_CLOSE) {
      return " onClick=\"javascript:window.close()\";";
    } else if (eventID == EVENT_ON_ROLLOVER_CHANGE_BACKGROUND) {
      return " onMouseOut=\"this.background='" + params[0] + "'\" onMouseOver=\"this.background='" + params[1] + "'\"";
    } else if (eventID == EVENT_ON_ROLLOVER_CHANGE_UNDERLINE) {
      return " onMouseOut=\"this.style.textDecoration='none'\" onMouseOver=\"this.style.textDecoration='underline'\"";
    } else if (eventID == EVENT_DRAG_AND_RESIZE) {
      String bounds = params[1] + VAR_X + "," + params[1] + VAR_Y + "," + params[1] + VAR_WIDTH + "," + params[1] + VAR_HEIGHT;
      return " onmouseup=\"doUp()\" onmousemove=\"doMove()\" onmousedown=\"doDown(" + params[0] + "," + params[1] + "," + bounds + ")\"";
    } else if (eventID == EVENT_DRAG_AND_DROP) {
      String bounds = params[1] + VAR_X + "," + params[1] + VAR_Y + "," + params[1] + VAR_WIDTH + "," + params[1] + VAR_HEIGHT;
      return "";
      // return " onmouseup=\"doDragUp()\" onmousemove=\"doDragMove()\"
      // onmousedown=\"doDragDown("+params[0]+","+params[1]+","+bounds+")\"";
    }
    return "";
  }

  public static boolean isSpace(String value) {
    return value.equals("\u00a0");
  }

  public static String getZoomed(String format, double zoomFactor) {
    if (format == null || format.indexOf("px") < 0)
      return format;
    String[] list = JWebUtils.getStringList(format, "px");
    if (list != null) {
      // System.err.println(""+list[0]);
      return Integer.toString((int)((Double.parseDouble(list[0]) * zoomFactor)));
    }
    return format;
  }

  public static String convertSpaces(String value) {
    value = JWebUtils.replaceStr(value, "\u00a0", "");
    return JWebUtils.replaceStr(value, "&nbsp;", "");
  }
  private static final String[] asHtmlCharEntities = {
    // only some of them can be either upper- or lowercase (not mixed), others
    // are case sensitive; we record this behaviour directly here, so we
    // can
    // always compare case sensitive
    // Special characters
    "<lt;", // < //Less than sign
    "<LT;", // < //Less than sign
    ">GT;", // > //Greater than sign
    ">gt;", // > //Greater than sign
    "&amp;", // & //Ampersand
    "&AMP;", // & //Ampersand
    "\"quot;", // " Double quote sign
    "\"QUOT;", // " Double quote sign
    // Character entity references
    "\u00a0nbsp;", // Nonbreaking space Non breaking space
    "\u00a9copy;", //  Copyright
    "\u00a9COPY;", //  Copyright
    "\u00aereg;", //  Registered TradeMark
    "\u00aeREG;", //  Registered TradeMark
    "\u00c0Agrave;", //  Capital A, grave accent
    "\u00c1Aacute;", //  Capital A, acute accent
    "\u00c2Acirc;", //  Capital A, circumflex accent
    "\u00c3Atilde;", //  Capital A, tilde
    "\u00c4Auml;", //  Capital A, dieresis or umlaut mark
    "\u00c5Aring;", //  Capital A, ring
    "\u00c6AElig;", //  Capital AE dipthong (ligature)
    "\u00c7Ccedil;", //  Capital C, cedilla
    "\u00c8Egrave;", //  Capital E, grave accent
    "\u00c9Eacute;", //  Capital E, acute accent
    "\u00caEcirc;", //  Capital E, circumflex accent
    "\u00cbEuml;", //  Capital E, dieresis or umlaut mark
    "\u00ccIgrave;", //  Capital I, grave accent
    "\u00cdIacute;", //  Capital I, acute accent
    "\u00ceIcirc;", //  Capital I, circumflex accent
    "\u00cfIuml;", //  Capital I, dieresis or umlaut mark
    "\u00d0ETH;", //  Capital Eth, Icelandic
    "\u00d1Ntilde;", //  Capital N, tilde
    "\u00d2Ograve;", //  Capital O, grave accent
    "\u00d3Oacute;", //  Capital O, acute accent
    "\u00d4Ocirc;", //  Capital O, circumflex accent
    "\u00d5Otilde;", //  Capital O, tilde
    "\u00d6Ouml;", //  Capital O, dieresis or umlaut mark
    "\u00d8Oslash;", //  Capital O, slash
    "\u00d9Ugrave;", //  Capital U, grave accent
    "\u00daUacute;", //  Capital U, acute accent
    "\u00dbUcirc;", //  Capital U, circumflex accent
    "\u00dcUuml;", //  Capital U, dieresis or umlaut mark;
    "\u00ddYacute;", //  Capital Y, acute accent
    "\u00deTHORN;", //  Capital THORN, Icelandic
    "\u00dfszlig;", //  Small sharp s, German (sz ligature)
    "\u00e0agrave;", //  Small a, grave accent
    "\u00e1aacute;", //  Small a, acute accent
    "\u00e2acirc;", //  Small a, circumflex accent
    "\u00e3atilde;", //  Small a, tilde
    "\u00e4auml;", //  Small a, dieresis or umlaut mark
    "\u00e5aring;", //  Small a, ring
    "\u00e6aelig;", //  Small ae dipthong (ligature
    "\u00e7ccedil;", //  Small c, cedilla
    "\u00e8egrave;", //  Small e, grave accent
    "\u00e9eacute;", //  Small e, acute accent
    "\u00eaecirc;", //  Small e, circumflex accent
    "\u00ebeuml;", //  Small e, dieresis or umlaut mark
    "\u00ecigrave;", //  Small i, grave accent
    "\u00ediacute;", //  Small i, acute accent
    "\u00eeicirc;", //  Small i, circumflex accent
    "\u00efiuml;", //  Small i, dieresis or umlaut mark
    "\u00f0eth;", //  Small eth, Icelandic
    "\u00f1ntilde;", //  Small n, tilde
    "\u00f2ograve;", //  Small o, grave accent
    "\u00f3oacute;", //  Small o, acute accent
    "\u00f4ocirc;", //  Small o, circumflex accent
    "\u00f5otilde;", //  Small o, tilde
    "\u00f6ouml;", //  Small o, dieresis or umlaut mark
    "\u00f8oslash;", //  Small o, slash
    "\u00f9ugrave;", //  Small u, grave accent
    "\u00fauacute;", //  Small u, acute accent
    "\u00fbucirc;", //  Small u, circumflex accent
    "\u00fcuuml;", //  Small u, dieresis or umlaut mark
    "\u00fdyacute;", //  Small y, acute accent
    "\u00fethorn;", //  Small thorn, Icelandic
    "\u00ffyuml;", //  Small y, dieresis or umlaut mark
    "\u2122trade;", //  TradeMark
    "\u2122TRADE;", //  TradeMark
    } ;

  public static String getCellId(String param) {
    if (param.startsWith(VAR_CELL_ID)) {
      param = param.substring(VAR_CELL_ID.length());
    }
    // ====================== Mahmood Code begin 19-March-2006
    // ================================
    if (param.endsWith("_LABEL"))
      param = param.substring(0, param.indexOf("_LABEL"));
    // ====================== Mahmood Code end 19-March-2006
    // ================================
    return param;
  }

  public static String getHiddenVar(String name, String text) {
    return getHiddenVar(name, name, text);
  }

  public static String getHiddenVar(String id, String name, String text) {
    return "<input type=\"hidden\" " + "ID='" + id + "' name=\"" + name + "\" value=\"" + text + "\">";
  }

  public static boolean isDraggedVar(String fullVarName) {
    return fullVarName.startsWith(DRAG_SOURCE_VAR) ? true : false;
  }

  public static String getDragTarget(String value) {
    if (JWebUtils.isNull(value))
      return "";
    int lt = DRAG_TARGET_VAR.length();
    if (lt >= value.length())
      return "";
    else
      return getCellId(value.substring(lt + 1));
  }

  public static String getDragSource(String value) {
    if (JWebUtils.isNull(value))
      return "";
    int lt = DRAG_SOURCE_VAR.length();
    if (lt >= value.length())
      return "";
    else
      return getCellId(value.substring(lt + 1));
  }

  private static String getRectangleVar(String name) {
    String vars = getHiddenVar(name + VAR_X, "-1");
    vars += " " + getHiddenVar(name + VAR_Y, "-1");
    vars += " " + getHiddenVar(name + VAR_WIDTH, "-1");
    vars += " " + getHiddenVar(name + VAR_HEIGHT, "-1");
    return vars;
  }
  
  public static String getJQVar(String varName) {
    return JWebHtml.VAR_JQ_START+varName+JWebHtml.VAR_JQ_END;
  }

  public static String getJQVarValue(String varName) {
    return getJQVar(varName) + JWebHtml.VAR_JQ_VAL;
  }

  private static String  addEndMarker(boolean isStatement) {
    return isStatement ? ";" : " ";
  }
  public static String getJQVarValue(String varName, String expr, boolean isStatement) {
    return getJQVar(varName)+JWebHtml.VAR_JQ_VAL_START+"'"+expr+"'"+JWebHtml.VAR_JQ_VAL_END+addEndMarker(isStatement);
  }

  public static String getJQVarAssign(String var1Name, String var2Name, boolean isStatement) {
    return getJQVar(var1Name) + JWebHtml.VAR_JQ_VAL_START + getJQVarValue(var2Name) + JWebHtml.VAR_JQ_VAL_END + addEndMarker(isStatement);
  }

  public static String getJSP(int functionCode, String[] params) {
    if (functionCode == JSP_EXPRESSION) {
      return ("expression(" + params[0] + ")");
    } else if (functionCode == JSP_GET_ELEMENT_WIDTH) {
      return ("getElementWidth('" + params[0] + "'," + params[1] + ")");
    } else if (functionCode == JSP_GET_ELEMENT_HEIGHT) {
      return ("getElementHeight('" + params[0] + "'," + params[1] + ")");
    }
    return "";
  }
    
  private static String getJQ(int template,String name, String script) {
    if (template == JQ_GET_BUTTON_CLICK) {
    return JWebUtils.getNewLineChar() +
           " $(function() { "+JWebUtils.getNewLineChar()+
                      "   $('#"+name+"') "+JWebUtils.getNewLineChar()+
                      "     .button() "+JWebUtils.getNewLineChar()+
                      "     .click(function() { "+JWebUtils.getNewLineChar()+
                              script  +JWebUtils.getNewLineChar()+
                      "    }); "+JWebUtils.getNewLineChar()+
                      " }); "+JWebUtils.getNewLineChar();
    }
    return "";
  }  
}
