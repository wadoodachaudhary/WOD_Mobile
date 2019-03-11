package com.jtools.javawebdatafront;

import com.jtools.javawebutils.JWebConstants;
import com.jtools.javawebutils.JWebEnviornment;
import com.jtools.javawebutils.JWebUtils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.FileUtils;
import org.jqurantree.arabic.ArabicText;
import taqwa.ApplicationQuranTree;

public class JWebDataWindowServer extends HttpServlet implements Serializable {

    private static String nl = System.getProperty("line.separator");
    private final static String USER_NAME = "User";
    private final String PIVOT = "Pivot";
    private final String PATH = "/Users/Wadood/Downloads/tomcat/";
    private final static String DOC_PATH = "";
    private final String SERVER_URL = "Analyzer";
    private final String BASE_URL = null;
    private final String CONTENT_TYPE = "text/html";
    private String cacheDir, cacheFile;
    private final int interval = 7200;
    public static String USER_CFG = "/cfg/Users.cfg";
    public static String EXCEL_DIR = "excel";
    private HttpSession session = null;
    private String sessionId = null;
    private final String name = "MainAnalyzer";
    private PrintWriter out;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private String wordDefinition;
    private String tokenStr;
    private String word;
    private String[] wordList;
    private ArrayList<JWebDataWindowLite> tokenDws = new ArrayList<JWebDataWindowLite>();
    private JWebDataWindowLite dwSegments;
    private final taqwa.ApplicationQuranTree qt = new taqwa.ApplicationQuranTree(true);
    private String rows = "10", cols = "100";
    private String ext = ".txt";
    String fileName = "";
    private final String MEANING_START = "{";
    private final String MEANING_END = "}";
    private final String EXAMPLE_START = "<";
    private final String EXAMPLE_END = ">";
    private final String WORD_ID = "WordID";
    private Page currentPage = new Page(Stages.ENTRY, "",false);
    private ArrayList<Page> pages = new ArrayList<Page>();
    private ArrayList<String> wordDefinitionLines= null;
            

    class Page {

        Stages stage;
        String contents;
        boolean verseSelections = false;

        Page(Stages stage, String contents, boolean verseSelections) {
            this.stage = stage;
            this.contents = contents;
            this.verseSelections = verseSelections;
        }

        public boolean equals(Object o) {
            Page p = (Page) o;
            if (p.stage == stage) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "[" + this.stage + "] " + this.contents;
        }
    }

    class Range {

        int start;
        int end;

        Range(int x, int y) {
            start = x;
            end = y;
        }
    }

    private enum Stages {

        ENTRY, ANALYZE, SUBSTITUTE, PRE_XML, XML, SAVE, UPLOAD
    };
    private Stages stage, prevStage;
    private String wordID;
    private String prevWordDefinition;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

    }

    class PageComp implements Comparator<Page> {

        @Override
        public int compare(Page page1, Page page2) {
            return 0;
        }

    }

    //ResourceBundle rb = ResourceBundle.getBundle("LocalStrings");
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//        for (Page pg : pages) {
//           System.out.println("    Page: "+pg);
//        }

        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("UTF-8");
        this.request = request;
        this.response = response;
        this.out = response.getWriter();
        session = request.getSession(true);
        out.println(" ");
        out.println("<br>");
        String valAnalyze = request.getParameter("Analyze");
        String valSubstitute = request.getParameter("Substitute");
        String valUpload = request.getParameter("Upload");
        String valStage = request.getParameter("Stage");
        String valXML = request.getParameter("XML");
        String valSave = request.getParameter("Save");
        String valStartOver = request.getParameter("StartOver");
        String back = request.getParameter("Back");
        setWordID((String) session.getAttribute("WordID"));
        String wordBoxText = request.getParameter("word");
        if (JWebUtils.isNotNull(valStartOver)) {
            goPage(Stages.ENTRY, wordBoxText, 1);
        } else if (JWebUtils.isNotNull(valAnalyze)) {
            goPage(Stages.ANALYZE, wordBoxText, 1);
        } else if (JWebUtils.isNotNull(valSubstitute)) {
            goPage(Stages.SUBSTITUTE, wordBoxText, 1);
        } else if (JWebUtils.isNotNull(valStage)) {
            goPage(Stages.PRE_XML, wordBoxText, 1);
        } else if (JWebUtils.isNotNull(valXML)) {
            goPage(Stages.XML, wordBoxText, 1);
        } else if (JWebUtils.isNotNull(valUpload)) {
            goPage(Stages.UPLOAD, wordBoxText, 1);
        } else if (JWebUtils.isNotNull(valSave)) {
            goPage(Stages.ENTRY, wordBoxText, 1);
        } else if (JWebUtils.isNotNull(back)) {
            int currentPageIndex = pages.indexOf(currentPage);
            if (currentPageIndex > 0) {
                Page lastPage = pages.get(currentPageIndex - 1);
                goPage(lastPage.stage, lastPage.contents, 0);
            } else {
                goPage(Stages.ENTRY, "", 1);
            }
            //generateTree();
        } else {
            goPage(Stages.ENTRY, "", 1);
        }
    }

    private void goPage(Stages stage, String word, int type) {
        int currentPageIndex = pages.indexOf(currentPage);
        if (currentPageIndex >= 0) {
            pages.get(currentPageIndex).contents = word;
        }
        currentPage.stage = stage;
        if (stage == Stages.ENTRY) {
            rawWord(word);
        } else if (stage == Stages.ANALYZE) {
            analyzeWord(type, word);
        } else if (stage == Stages.SUBSTITUTE) {
            updateVerseNos(type, word);
        } else if (stage == Stages.PRE_XML) {
            preXMLWord(type, word);
        } else if (stage == Stages.XML) {
            XMLWord(type, word);
        } else if (stage == Stages.UPLOAD) {
            uploadWord(type, word);
        } else if (stage == Stages.SAVE) {
            saveWord(type, word);
        }
    }

    private void generateWordContents(String wordStr, Stages stage) {
        currentPage.contents = wordDefinition;
        boolean verseSelections = JWebUtils.isNotNull(wordStr) ? true : false;
        int pgNo = pages.indexOf(currentPage);
        if (pgNo < 0) {
            pages.add(new Page(currentPage.stage, currentPage.contents, verseSelections));
        } else {
            pages.get(pgNo).contents = wordDefinition;
            pages.get(pgNo).verseSelections = verseSelections;
        }
        //System.out.println(pgNo+": Page: "+prevPage);
        addHeaderBar();
        //Buttons
        out.println("<table>");
        out.println("<tr><td>");
        out.println("<div id='Buttons' style='vertical-align: left'>");
        if (stage == Stages.ENTRY) {
            out.println("<button name='Analyze' value='1' type='submit'>Analyze</button>");
        } else if (stage == Stages.ANALYZE) {
            out.println("<button name='Substitute' value='1' type='submit'>Update Verse Nos</button>");
        } else if (stage == Stages.SUBSTITUTE) {
            out.println("<div id='Buttons-Sub' style='vertical-align: left'>");
            out.println("<table><tr>");
            out.println("<td><button name='Stage' value='1' type='submit'>Pre-XML</button></td>");
            //out.println("<td><button name='Substitute' value='1' type='submit'>Update Verse Nos</button></td>");
            out.println("</td></table>");
            out.println("</div>");
        } else if (stage == Stages.PRE_XML) {
            out.println("<button name='XML' value='1' type='submit'>XML</button>");
            out.println("<button name='Save' value='1' type='submit'>Save</button>");
        } else if (stage == Stages.XML) {
            out.println("<button name='Upload' value='1' type='submit'>Upload</button>");
            out.println("<button name='Save' value='1' type='submit'>Save</button>");
        } else if (stage == Stages.UPLOAD) {
            out.println("<button name='StartOver' value='1' type='submit'>Start over</button>");
        }
        out.println("</div>");
        out.println("</td></tr>");
        out.println("</table>");
//
        out.println("<table>");
        out.println("<tr>");
        out.println("<td>");
        out.println("<textarea rows='" + rows + "' cols='" + cols + "'  name=word> " + wordDefinition + "</textarea>");
        out.println("</td>");
        out.println("</tr>");
        if (JWebUtils.isNotNull(wordStr)) {
            out.println("<tr>");
            out.println("<td>");
            out.println("<div id='Verses' style='vertical-align: center'>");
            out.println(wordStr);
            out.println("</div>");
            out.println("</td>");
            out.println("</tr>");
        }
        out.println("</table>");
        if (JWebUtils.isNotNull(wordID)) {
            session.setAttribute(WORD_ID, wordID);
        } else {
            session.removeAttribute(WORD_ID);
        }
    }

    private void save() {
        try {
            if (JWebUtils.isNotNull(wordID)) {
                fileName = "w_" + wordID + ext;
                //File f = new File(fileName);
                //FileUtils.write(f, wordDefinition, "UTF-8");
                OutputStream os = new FileOutputStream(qt.docPath + fileName);
                os.write(239);
                os.write(187);
                os.write(191);
                PrintWriter w = new PrintWriter(new OutputStreamWriter(os, "UTF-8"));

                w.print(wordDefinition);
                w.flush();
                w.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doGet(request, response);
    }

    private String[] verseList;

    public String getVerseLookup(String raw) throws Exception {
        String verseTable = "";
        try {
            wordList = null;
            verseList = null;
            verseList = JWebUtils.getTokenList(raw, "Token(", ")");
            String verseRows = "";
            for (String str : verseList) {
                verseRows = verseRows + "<tr><td> Verse Tokens:(" + str + ")</td></tr>";
                verseRows = verseRows + "<tr><td>";
                JWebDataWindowLite dw = qt.getVerseTokens(str);
                tokenDws.add(dw);
                verseRows = verseRows + " " + JWebUtils.getHtml(dw);
                verseRows = verseRows + "</td></tr>";
            }
            //System.out.println("Verese rows:" + verseRows);
            dwSegments = null;
            String wordTitle = "";
            wordList = JWebUtils.getTokenList(raw, "Word(", ")");
            if (tokenDws.size() > 0 && (wordList == null || wordList.length == 0)) {
                wordList = new String[]{verseList[0]};
            }
            if (wordList != null && wordList.length > 0) {
                dwSegments = qt.getTokenSegments(wordList[0]);
                wordTitle = wordList[0];
            }
            String wordAnalyzeRow = "";
            if (dwSegments != null) {
                wordAnalyzeRow = "<tr><td> Word Tokens:(" + wordTitle + ")</td></tr>";
                wordAnalyzeRow = wordAnalyzeRow + "<tr><td>";
                wordAnalyzeRow = wordAnalyzeRow + " " + JWebUtils.getHtml(dwSegments);
                wordAnalyzeRow = wordAnalyzeRow + "</td></tr>";
            }
            verseTable = "<table>" + wordAnalyzeRow + verseRows + "</table>";
            // transpose
            //System.out.println("Stage:" + stage);
            //stage = "ABC";
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //con.close();
            return verseTable;

        }

    }
    private String codes = "Ð$ÏïìØÁ¾¿ÅÂÃÉÆÍÊÑÎÕÓÒÙÝÜÚÛàáäãåâçæèéøöôõ»ü$][÷^ßÖ×#ÔºÞî<>{}";

    private String removeGlyphCodesFrom(String raw) {
        raw = raw.replaceAll("\n", " ");
        for (int i = 0; i < codes.length(); i++) {
            raw = JWebUtils.replaceStr(raw, codes.substring(i, i + 1), "");
        }
        return raw;
    }

    private void analyzeWord(int type, String rawWord) {
        try {
            rawWord = removeGlyphCodesFrom(rawWord);
            if (rawWord.indexOf("Token(") < 0) {
                //insert tokens
                String[] tokenList = JWebUtils.getTokenList(rawWord, "(", ")");
                for (String token : tokenList) {
                    //System.out.println(" Token:"+ token+ " ");
                    if (token.contains(":")) {
                        String tokenNumbers[] = token.split(":");
                        if (tokenNumbers != null && tokenNumbers.length == 2 && JWebUtils.isNumber(tokenNumbers[0]) && JWebUtils.isNumber(tokenNumbers[1])) {
                            String tokenStr = "(" + token + ")";
                            int endPos = rawWord.indexOf(tokenStr);
                            if (endPos < 0) {
                                continue;
                            }
                            int startPos = rawWord.substring(0, endPos).lastIndexOf(".");
                            if (startPos < 0) {
                                continue;
                            }
                            rawWord = rawWord.substring(0, startPos + 1) + " Token" + tokenStr + rawWord.substring(startPos + 1);
                        }
                    }
                }
            }
            String verseTable = getVerseLookup(rawWord);
            String wordAnalyze = "";
            if (rawWord.indexOf("Word(") < 0 && (wordList != null && wordList.length > 0)) {
                wordAnalyze = wordList == null ? "Word(1)" : "Word(" + wordList[0] + ")\n";
            }
            if (rawWord.indexOf(MEANING_START) < 0) {
                rawWord = MEANING_START + rawWord + MEANING_END;
            }
            if (rawWord.indexOf(EXAMPLE_START) < 0) {
                int startPos = rawWord.indexOf("Token(");
                int endPos = rawWord.indexOf(".", startPos + 1);
                while (startPos > 0 && endPos > 0) {
                    rawWord = rawWord.substring(0, startPos - 1) + EXAMPLE_START + rawWord.substring(startPos, endPos + 1) + EXAMPLE_END + rawWord.substring(endPos + 1);
                    startPos = rawWord.indexOf("Token(", endPos + 1);
                    endPos = rawWord.indexOf(".", startPos + 1);
                }
            }
            //System.out.println("RawWord:" + rawWord);
            wordDefinition = wordAnalyze + rawWord;
            generateWordContents(verseTable, Stages.ANALYZE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //addInstinetButtonBar();
    }

    private void setWordID(String wID) {
        if (JWebUtils.isNumber(wID)) {
            wordID = wID;
        } else {
            wordID = "";
        }

    }

    private ArrayList<Range> selectionVerses;
    private String selectedWord;
    private void updateVerseNos(int type, String word) {
        wordDefinition = word;
        substitute();
        tokenStr = applySelections();
        //save();
        generateWordContents(tokenStr, Stages.SUBSTITUTE);
    }
    private void substitute() {
        if (wordList != null && wordList.length > 0) {
            String[] rows = request.getParameterValues("Word:" + wordList[0]);
            String tokens = "";
            if (rows != null && rows.length == 1) {
                tokens = rows[0];
                selectedWord = rows[0];
                String[] currentVal = JWebUtils.getTokenList(wordDefinition, "Word(", ")");
                //setWordID(tokens);
                if (currentVal != null && currentVal.length > 0) {
                    wordDefinition = JWebUtils.replaceStr(wordDefinition, "Word(" + currentVal[0] + ")", "Word(" + tokens + ")");
                }
            }
        }
        if (verseList != null) {
            selectionVerses = new ArrayList<Range>(verseList.length);
            for (int v = 0; v < verseList.length; v++) {
                String[] rows = request.getParameterValues(verseList[v]);
                String tokens = "";
                if (rows != null && rows.length == 2) {
                    tokens = ":" + rows[1] + ":" + rows[0];
                    selectionVerses.add(new Range(Integer.parseInt(rows[0]), Integer.parseInt(rows[1])));
                    String[] currentVal = JWebUtils.getTokenList(wordDefinition, "Token(" + verseList[v], ")");
                    if (currentVal != null && currentVal.length > 0) {
                        wordDefinition = JWebUtils.replaceStr(wordDefinition, "Token(" + verseList[v] + currentVal[0] + ")", "Token(" + verseList[v] + tokens + ")");
                    }
                }
            }
        }
    }

    public String applySelections() {
        String stage = "";
        stage = "<table>";
        if (dwSegments != null) {
            for (int c = 0; c < dwSegments.getColumnCount(); c++) {
                JWebCellLite cell = dwSegments.getCell(1, c);
                cell.setVar("Checked", JWebUtils.isSame(dwSegments.getItem(1, c), selectedWord) ? "1" : "0");
            }
            stage = stage + "<tr><td> Word Tokens:(" + wordList[0] + ")</td></tr>";
            stage = stage + "<tr><td>";
            stage = stage + " " + JWebUtils.getHtml(dwSegments);
            stage = stage + "</td></tr>";
        }
        if (verseList != null) {
            for (int i = 0; i < verseList.length; i++) {
                String str = verseList[i];
                stage = stage + "<tr><td> Verse Tokens:(" + str + ")</td></tr>";
                stage = stage + "<tr><td>";
                JWebDataWindowLite dw = tokenDws.get(i);
                if (dw != null && selectionVerses.size() > i) {
                    Range selection = selectionVerses.get(i);
                    for (int c = dw.getColumnCount() - 1; c >= 0; c--) {
                        JWebCellLite cell = dw.getCell(1, c);
                        int v = dw.getItemInt(1, c);
                        cell.setVar("Checked", v == selection.start || v == selection.end ? "1" : "0");
                    }
                }
                stage = stage + " " + JWebUtils.getHtml(dw);
                stage = stage + "</td></tr>";
            }
        }
        stage = stage + "</table>";
        return stage;
    }

    private void preXMLWord(int type, String def) {
        try {
            // Get Word
            wordDefinition = def;
            substitute();
            def=wordDefinition;
            wordDefinitionLines= new ArrayList<String>();
            String[] currentVal = JWebUtils.getTokenList(def, "Word(", ")");
            if (currentVal != null && currentVal.length > 0) {
                setWordID(currentVal[0]);
                JWebDataWindowLite dw = qt.getWordInfo(wordID);
                wordDefinition = "Word:" + dw.getItem(0,"Text") + "\n";
                wordDefinitionLines.add("Word:" + dw.getItem(0,"Text"));
                wordDefinition = wordDefinition + "Root:" + dw.getItem(0,"Root") + "\n";
                wordDefinitionLines.add("Root:" + dw.getItem(0,"Root"));
                wordDefinition = wordDefinition + "Transliteration:" + dw.getItem(0,"Transliteration") + "\n";
                wordDefinitionLines.add("Transliteration:" + dw.getItem(0,"Transliteration"));
                //
                wordDefinition = wordDefinition + "MeaningSimple:" + JWebUtils.isNull(dw.getItem(0,"MeaningSimple"), "") + "\n";
                wordDefinitionLines.add("MeaningSimple:" + JWebUtils.isNull(dw.getItem(0,"MeaningSimple"), ""));
                //
                String[] meanings = JWebUtils.getTokenList(def, MEANING_START, MEANING_END);
                for (String meaning : meanings) {
                    String examples[] = JWebUtils.getTokenList(meaning, EXAMPLE_START, EXAMPLE_END);
                    if (examples != null && examples.length > 0) {
                        meaning = JWebUtils.getTokenBefore(meaning, EXAMPLE_START);
                        wordDefinition = wordDefinition + "Meaning:" + meaning + "\n";
                        wordDefinitionLines.add("Meaning:" + meaning);
                        for (String example : examples) {
                            wordDefinition = wordDefinition + "Example:" + example + "\n";
                            wordDefinitionLines.add("Example:" + example);
                        }
                    }
                }
            }
            String whereExpr = "";
            for (String locStr : verseList) {
                String[] verseLoc = JWebUtils.getStringList(locStr, ":");
                String chapterNo = verseLoc[0];
                String verseNo = verseLoc[0] == "9" ? verseLoc[1] : Integer.toString((Integer.parseInt(verseLoc[1]) - 1));
                whereExpr = JWebUtils.ifConcat(whereExpr, " (chapter_no=" + chapterNo + " AND verse_no=" + verseNo + ")", "OR");
            }
            JWebDataWindowLite dwTransliteration = qt.getTransliterationForExpr(whereExpr);
            //save();
            rows = "30";
            generateWordContents(JWebUtils.getHtml(dwTransliteration), Stages.PRE_XML);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //addInstinetButtonBar();
    }

    private void XMLWord(int type, String stageWord) {
        try {
            wordDefinition = stageWord;
            save();
            rows = "30";
            String[] wodList = JWebUtils.getStringList(wordDefinition,nl);
            ArrayList<String> wordDefinitionLines2  = JWebUtils.getStringArrayAsArrayList(wodList);

            wordDefinition = JWebUtils.getArrayAsString(qt.addWordToXML(qt.docPath, wordDefinitionLines2));
            ext = ".xml";
            save();
            generateWordContents("", Stages.XML);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //addInstinetButtonBar();
    }

    private void uploadWord(int type, String xmlWord) {
        try {
            wordDefinition = xmlWord;
            save();
            qt.addWord(wordDefinition, true);
            generateWordContents("", Stages.UPLOAD);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //addInstinetButtonBar();
    }

    private void saveWord(int type, String rawWord) {
        try {
            generateWordContents("", Stages.SAVE);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //addInstinetButtonBar();
    }

    private void addHeaderBar() {
        String title = "(Taqwa - Word conversion from Raw to XML)";
        out.println("<html>");
        out.println("<head><div>");
        out.println("<p style='float: left;'><img src='resources/logo.png' height=60 width=60></p>");
        out.println("<p><title>" + title + "</title></p>");
        out.println("</div></head>");
        out.println("<script src='/WEB-INF/js/jquery.js' language='javascript' type='text/javascript'></script>");
        out.println("<script src='/WEB-INF/js/jstree.min.js' language='javascript' type='text/javascript'></script>");
        out.println("<script>");
        out.println("$(function () {");
        out.println("  $('#jstree').jstree();");
        //out.println("  $('#jstree').on('changed.jstree', function (e, data) {");
        //out.println("   console.log(data.selected);");
        //out.println("  });");
        //out.println("   $('button').on('click', function () {");
        //out.println("       $('#jstree').jstree(true).select_node('child_node_1');");
        //out.println("       $('#jstree').jstree('select_node', 'child_node_1');");
        //out.println("       $.jstree.reference('#jstree').select_node('child_node_1');");
        //out.println("  });");
        out.println(" });");
        out.println(" </script>");

        out.println("<body bgcolor=\"white\">");
        out.println("<h3>" + title + "</h3>");
        out.println("<P>");
        out.print("<form action=\"");
        out.print(response.encodeURL("word"));
        out.print("\" ");
        out.println("method=POST>");
        out.println("<div id='buttonsBuild' style='vertical-align: left'>");
        out.println("<button name='Back' value='Back' type='submit'>Go Back</button>");
        out.println("<a href=https://www.dropbox.com/s/5pmzy1ae33kzxxw/src_cs634_project.zip>Download Source Code</a>");
        out.println("</div>");
        out.println("<br>");
    }

    private void rawWord(String word) {
        wordID = "";
        if (JWebUtils.isNull(word)) {
            wordDefinition = getSampleWord();
        } else {
            wordDefinition = word;
        }
        generateWordContents("", Stages.ENTRY);
    }

    private String getSampleWord() {
        //return "Upoun face. Upside down. Throw headlong.  كَبَّ[aor.  يَكُبُّ  inf. Noun  كَبٌّ ].  كَبَّهٗ  or  كَبَّهٗ عَلٰى وَجْهِهٖ   or كَبْكَبَهٗ  He turned it upside down; he threw him down upon his face; he prostrated him.  They will be thrown down headlong or upon their faces (27:91).  They shall be thrown into it headlong (26:95).  Going headlong or upon his face (67:23).";
        return "كَثُرَ      [aor.  يَكْثُرُ and  يَكْثِرُ inf. noun كَثْرَةٌ  ] : It was or became much,copious, many, numerous; it multiplied. كَثَرُوْا عَلَيْهِ فَغَلَبُوْهُ  : They multiplied against him and overcame him. ÚôÛ$^ Îø Ø$Úôß» äö ]ø æ» Òø %öø : Whether it be little or much (4:8). كَثَّرَهٗ اَوْ اَكْثَرَهٗ  : He made it much, many or numerous; he multiplied it.  فَكَثَّرَكُمْ   : So He multiplied you (7:87). Êø ^ø Ò» %øö æ»] Êô n»ãø ^ ]Ö» Ëøø ^ø : And they wrought much  corruption therein (89:13). تَكَاثَرُوْا  : They contended one with another for superiority in number. اَلتَّكَاثُرُ  : Signifies the contending together for superiority in amount or number of property and children and men (102:2). اِسْتَكْثَرَ مِنَ الشَّىْءِ  : He desired or wished or sought for much of a thing; he reckoned it much or many. ø » jø Ó» %ø »lö Úôàø ]Ö» íø n»ô : I should have secured abundance of good (7:189). كَثْرَةٌ  : Abundance or numerousness (5:101;9:25).  كَثِيْرٌ  : Many or much. (2:110;4;83). اَكْثَرَ  :  Comparative degree of كَثِيْرٌ (4:60;17:7). كَوْثَرٌ  : A lord or master; abounding in good; a man possessing much good and who gives much (108:2).  ";
    }

    private String getInstinetSampleLabel() {
        return "Buy,DAY,XPAR,FRE,Added";
    }

    //Tree algorithm
    private void generateTree() {
        out.println("<div id='jstree'>");
        out.println("<ul>");
        out.println("<li>Root node 1");
        out.println(" <ul>");
        out.println("<li id='child_node_1'>Child node 1</li>");
        out.println("<li>Child node 2</li>");
        out.println(" </ul>");
        out.println("</li>");
        out.println("<li>Root node 2</li>");
        out.println("</ul>");
        out.println("</div>");
    }

    private void initApp(String path, String defPath) throws Exception {
        String sep = File.separator;
        JWebEnviornment.setAbsolutePath(path + sep);
        //JWebEnviornment.setRelativePath("../vista01/images1");
        JWebEnviornment.setRelativePath("images");
        JWebEnviornment.checkForPersistence(true);
        JWebEnviornment.setFilePersistenceTolerance(JWebEnviornment.PERSIST_IF_EXISTS);
        JWebEnviornment.setBaseURL(BASE_URL);
        JWebEnviornment.setNextURL(SERVER_URL);
        cacheFile = "cubecache";
        cacheDir = defPath + sep + "cache" + sep + "cache";
        JWebEnviornment.setCache(cacheDir, cacheFile);
        //cleanup(true, null);
    }

    private JWebDataWindowLite getMain(HttpSession session, HttpServletRequest request) throws Exception {
        //    JWebUtils.setErr("c://test.txt");
        JWebUtils.setErr(PATH + "\\test.txt");

        //JWebUtils.setErr("c://analyzerlogs.txt");
        return new JWebDataWindowLite();
    }

    private HttpSession getSession() {
        return session;
    }

    private void setSession(HttpSession session) {
        this.session = session;
    }

    private String getSessionId() {
        return sessionId;
    }

    private void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    private void cleanup(boolean removeAll, String session_id) {
        File excelDir = new File(PATH + File.separator + EXCEL_DIR);
        File[] files = excelDir.listFiles();
        for (File file : files) {
            if (removeAll) {
                file.delete();
            } else if (file.getName().endsWith(session_id + ".xls")) {
                //System.out.println(files[i].getName()+" removed.");
                file.delete();
            }
        }
        if (session_id != null) {
            String dirName = JWebEnviornment.getAbsolutePath() + File.separator + JWebEnviornment.getRelativePath();
            File imageDir = new File(dirName);
            files = imageDir.listFiles();
            for (File file : files) {
                //System.out.println(" "+files[i].getName());
                if (file.getName().endsWith(session_id + "." + JWebConstants.EXT_JPEG)) {
                    //System.out.println(files[i].getName()+" removed.");
                    file.delete();
                } else if (file.getName().endsWith(session_id + "." + JWebConstants.EXT_GIF)) {
                    //System.out.println(files[i].getName()+" removed.");
                    file.delete();
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public JWebDataWindowServer() {
        super();
        try {

            System.setErr(new PrintStream(new File(qt.path + "error.txt")));
            System.setOut(new PrintStream(new File(qt.path + "log.txt")));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JWebDataWindowServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
