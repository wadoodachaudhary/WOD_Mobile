package taqwa;

import com.amazonaws.services.s3.model.*;
import com.jtools.javawebutils.JWebUtils;
import com.jtools.javawebutils.JavaWebGroup;
//import com.sun.xml.internal.bind.v2.model.core.ID;
import org.jqurantree.analysis.AnalysisTable;
import org.jqurantree.arabic.*;
import org.jqurantree.arabic.encoding.EncodingType;
import org.jqurantree.orthography.Chapter;
import org.jqurantree.orthography.Document;
import org.jqurantree.orthography.Token;
import org.jqurantree.orthography.Verse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.util.*;

import com.jtools.javawebdatafront.*;
import org.jqurantree.search.TokenSearch;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.BatchPutAttributesRequest;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.DeleteAttributesRequest;
import com.amazonaws.services.simpledb.model.DeleteDomainRequest;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.ReplaceableItem;
import com.amazonaws.services.simpledb.model.SelectRequest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
//import org.apache.commons.io.*;

import java.io.UnsupportedEncodingException;
import java.io.Writer;

import java.text.Normalizer;
import java.io.IOException;
import java.net.URL;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import java.net.*;
import java.io.*;

public class ApplicationQuranTree {

    private static String nl = System.getProperty("line.separator");
    private Connection con = null;
    private final String ID = "id";
    private final String WORD_SIMPLE = "word_simple";
    private final String WORD = "Word";

    private final String ALPHABET = "Alphabet";
    private final String SEGMENT = "Segment";
    private final String DEFINITION = "Definition";
    private final String TOKEN = "Token";
    private final String FORM = "Feature";
    private final String CHAPTER = "Chapter";
    private final String VERSE = "Verse";
    private final String ROOT = "Root";
    private final String ROOT_ID = "root_id";
    private final String ROOT_MEANING = "RootMeaning";

    private final String LETTER_1 = "letter1";
    private final String LETTER_2 = "letter2";
    private final String LETTER_3 = "letter3";
    private final String LETTER_1_TRANSLITERATION = "Letter1Transliteration";
    private final String LETTER_2_TRANSLITERATION = "Letter2Transliteration";
    private final String LETTER_3_TRANSLITERATION = "Letter3Transliteration";

    private final String DICTIONARY = "Dictionary";
    private final String LEMMA = "Lemma";
    private final String LEMMA_ID = "lemma_id";

    private final String WORD_ID = "word_id";
    private final String AUDIO_FILE = "Audiofile";
    private final String ORIGIN = "Origin";
    private final String TRANSLITERATION = "Transliteration";
    private final String TRANSLATION = "Translation";
    private final String TAG = "Tag";
    private final String FEATURES = "Features";
    private final String TEXT = "Text";

    private final String MEANING = "Meaning";
    private final String MEANING_SIMPLE = "meaning_simple";
    private final String MEANING_SIMPLE_XML = "MeaningSimple";

    private final String LANGUAGE = "Language";
    private final String LANGUAGE_ID = "language_id";
    private final String NAME = "Name";
    private final String MEANING_ID = "meaning_id";

    private final String VERSE_NO = "verse_no";
    private final String CHAPTER_NO = "chapter_no";
    private final String TOKEN_NO = "token_no";
    private final String SEGMENT_NO = "segment_no";
    private final String TOKEN_SIMPLE = "token_simple";

    private final String EXAMPLE = "Example";
    private final String REFERENCE = "Reference";

    private final String EMAIL_PLAN = "EmailPlan";
    private final String PLAN_DATE = "PlanDate";
    private final String STATUS = "Status";
    private final String STATUS_NEW = "NEW";
    private final String STATUS_DONE = "DONE";

    private final String PARTS_OF_SPEECH = "PartsOfSpeech";
    private final String CLASS = "Class";
    private final String WHITE_SPACE = "WhiteSpace";
    private final String UNKNOWN_DIACRITIC = "UnknownDiacritic";
    private final String TOKEN_PREFIX = "Token(";
    private String os = "";
    AmazonSimpleDB dbCloud;
    private String tab = "\t";
    private boolean update_mode = true;
    public String path = "", dbPath = "", docPath = "", webDocPath = "";

    JWebDataWindowLite dwVerse, dwSegment, dwMeaning, dwExample, dwSearch, dwEmailPlan, dwWord, dwRoot;

    public ApplicationQuranTree() {
        try {
            //System.out.println("Here");
            initWeb();
            addWord(new File(webDocPath + "w_8420.xml"), true);
            //---------

            //testML();
            //analyze("{is used to denote uncertainty or large number.Token(2:4) And many a Prophet or how many a Prophet Token(3:147)]}.");
            //testDW();
            //testExpr();
            //insertTokens();
            //getAllTokenMeaning();
            //insertClass();
            //testCloudAuthentication();
            //insertChapters();
            //insertSegments();
            //addWordToXML();
            //addWord();
            //removeWordFromCloud("58");
            //removeWordFromCloud("78");

            //insertTransliteration();
            //convertToUnicode();
            //insertRootMeaning2();
            //insertRootMeaning();
            //insertTranslation();
            //test();
            //insertSimpleMeaning();
            //insertDictionary();
            //makeDictionary();
            //removeWord("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //test();
        //testMorphology();

    }
    
    public void testML() {
        String wordDefinition = "a"+"\n"+"b"+"\n"+"c";
        String[] wodList = JWebUtils.getStringList(wordDefinition,nl);
        ArrayList<String> wordDefinitionLines  = JWebUtils.getStringArrayAsArrayList(wodList);
        for (String word:wordDefinitionLines) {
            System.out.println(word);
        }
    }
    
    public void test0() {
        String wordDefinition = "a"+"\n"+"b"+"\n"+"c";
        String[] wodList = JWebUtils.getStringList(wordDefinition,nl);
        ArrayList<String> wordDefinitionLines  = JWebUtils.getStringArrayAsArrayList(wodList);
        for (String word:wordDefinitionLines) {
            System.out.println(word);
        }
    }


    public ApplicationQuranTree(boolean fromWeb) {
        try {
            initWeb();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //test();
        //testMorphology();

    }

    private static final char L_PAREN = '(';
    private static final char R_PAREN = ')';
    private static final char COMMA = ',';

    private static String bucketName = "wadood";
    private static String objectKey = "video_1.html";

    private void test2() {
        String wordDefinition = "Word(3:125)\n {is used to denote uncertainty or large number Token(2:4).  Token(3:147) And many a Prophet or how many a Prophet (3:147)]}";
        String rawWord = "Upon face. Upside down. Throw headlong.  كَبَّ[aor.  يَكُبُّ  inf. Noun  كَبٌّ ].  كَبَّهٗ  or  كَبَّهٗ عَلٰى وَجْهِهٖ   or كَبْكَبَهٗ  He turned it upside down; he threw him down upon his face; he prostrated him.  They will be thrown down headlong or upon their faces (27:91).  They shall be thrown into it headlong (26:95).  Going headlong or upon his face (67:23).";
        //rawWord= "ABC de. All is great. All is not fine.";
        if (rawWord.indexOf("Token(") < 0) {
            //insert tokens
            String[] tokenList = JWebUtils.getTokenList(rawWord, "(", ")");
            for (String token : tokenList) {
                //System.out.println(" Token:"+ token+ " ");
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
        System.out.println("RawWord:" + rawWord);
    }

    private void listObjects(AmazonS3 s3client) {
        List<Bucket> bkts = s3client.listBuckets();
        for (int i = 0; i < bkts.size(); i++) {
            Bucket bkt = bkts.get(i);
            String bucket = bkt.getName();
            System.out.println("Bucket:" + bkt.getName());
            ObjectListing files = s3client.listObjects(bucket);

            List<S3ObjectSummary> list = files.getObjectSummaries();
            for (S3ObjectSummary file : list) {
                //S3Object obj = s3Client.getObject(bucket, image.getKey());
                //writeToFile(obj.getObjectContent());
                System.out.println("File:" + file.getKey());
            }
        }
    }

    public String convertRaw(String raw) {
        raw = "{is used to denote uncertainty or large number. [Token(3:147) And many a Prophet or how many a Prophet (3:147)]}.";

        String stage = raw;
        return stage;
    }

    public String analyze(String raw) throws Exception {
        initWeb();
        String stage = "";
        Connection con = getConnection();
        try {

            //raw = "{is used to denote uncertainty or large number. And many a Prophet or how many a Prophet (3:147)]}.";
            //raw.sp
            //String[] list = JWebUtils.getTagList(raw,"(",")");
            //System.err.println("Raw:"+raw);
            String[] list = JWebUtils.getTokenList(raw, "Token(", ")");
            stage = "<table>";
            for (String str : list) {
                //System.out.println("Str:"+str);
                stage = stage + "<tr><td>";
                JWebDataWindowLite dw = getVerseTokens(str);
                stage = stage + " " + JWebUtils.getHtml(dw);
                stage = stage + "</td></tr>";
            }
            stage = stage + "</table>";
            // transpose
            //System.err.println("Stage:"+stage);
            //stage = "ABC";
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.close();
            return stage;

        }

    }

    public JWebDataWindowLite getTokenSegments(String locStr) throws Exception {
        JWebDataWindowLite dwVerseFlattened = null;
        try {
            con = getConnection(true);
            //System.out.println("locStr:" + locStr);
            JWebDataWindowLite dwVerse = new JWebDataWindowLite(con);
            JWebUtils.setDBCon(con);

            String[] verseLoc = JWebUtils.getStringList(locStr, ":");
            String chapterNo = verseLoc[0];
            //String verseNo = verseLoc[0] == "9" ? verseLoc[1] : Integer.toString((Integer.parseInt(verseLoc[1]) - 1));
            String verseNo = verseLoc[1];
            //String tokenNo = verseLoc[2];
            //dwVerse.resetRows();
            String sql = "Select W.id as word_id,w.word as word from Segment S, Word w  \n"
                    + "where chapter_no=:1 and verse_no=:2 and S.word_id=W.id \n"
                    + "order by chapter_no,verse_no,token_no";
            //System.out.println("SQL=" + sql);
            dwVerse.setQuery(sql);
            dwVerse.retrieve(new Object[]{chapterNo, verseNo});
            int rowCount = 2;
            dwVerseFlattened = new JWebDataWindowLite(rowCount, dwVerse.getRowCount());
            String name = "Word:" + locStr;
            dwVerseFlattened.setName(name);
            //for (int c = 0; c < dwVerse.getColumnCount(); c++) {
            int rFlat = 0;
            for (int r = dwVerse.getRowCount() - 1; r >= 0; r--) {
                dwVerseFlattened.setColumnName(rFlat, dwVerse.getItem(r, "word"));
                dwVerseFlattened.setItem(0, rFlat, dwVerse.getItem(r, "word_id"));
                JWebCellLite cell = dwVerseFlattened.setItem(rowCount - 1, rFlat, dwVerse.getItem(r, "word_id"));
                cell.setVar("Type", "Checkbox");
                cell.setVar("Checked", "0");
                cell.setName(name);
                rFlat++;
                //dwVerseFlattened.setItem(c,0,dwVerse.getItem(r,"word"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
            return dwVerseFlattened;
        }

    }

    public JWebDataWindowLite getTransliterationForExpr(String whereExpr) throws Exception {
        JWebDataWindowLite dwVerseFlattened = null;
        try {
            con = getConnection(true);
            JWebDataWindowLite dwVerse = new JWebDataWindowLite(con);
            JWebUtils.setDBCon(con);
            String sql = "Select transliteration from Verse where " + whereExpr + " Order by chapter_no,verse_no";
            //System.out.println("Transliteration SQL:"+sql);
            dwVerse.runQuery(sql);
            int rowCount = 1;
            dwVerseFlattened = new JWebDataWindowLite(rowCount, dwVerse.getRowCount());
            dwVerseFlattened.setName(whereExpr);
            int rFlat = 0;
            for (int r = dwVerse.getRowCount() - 1; r >= 0; r--) {
                dwVerseFlattened.setColumnName(rFlat, Integer.toString(r));
                dwVerseFlattened.setItem(0, rFlat, dwVerse.getItem(r, "transliteration"));
                rFlat++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
            return dwVerseFlattened;
        }
    }

    public JWebDataWindowLite getVerseTokens(String locStr) throws Exception {
        JWebDataWindowLite dwVerseFlattened = null;
        try {
            con = getConnection(true);
            //System.out.println("locStr:" + locStr);
            JWebDataWindowLite dwVerse = new JWebDataWindowLite(con);
            JWebUtils.setDBCon(con);
            String[] verseLoc = JWebUtils.getStringList(locStr, ":");
            if (verseLoc != null && verseLoc.length == 2) {
                String chapterNo = verseLoc[0];
                //String verseNo = verseLoc[0] == "9" ? verseLoc[1] : Integer.toString((Integer.parseInt(verseLoc[1]) - 1));
                String verseNo = verseLoc[1];
                String sql = "Select chapter_no,verse_no,Token_no,token,token_simple from Token \n"
                        + "where chapter_no=:1 and verse_no=:2 \n"
                        + "Order by chapter_no,verse_no,token_no";
                dwVerse.setQuery(sql);
                dwVerse.retrieve(new Object[]{chapterNo, verseNo});
                int rowCount = 2;
                dwVerseFlattened = new JWebDataWindowLite(rowCount, dwVerse.getRowCount());
                dwVerseFlattened.setName(locStr);
                int rFlat = 0;
                for (int r = dwVerse.getRowCount() - 1; r >= 0; r--) {
                    dwVerseFlattened.setColumnName(rFlat, dwVerse.getItem(r, "token"));
                    dwVerseFlattened.setItem(0, rFlat, dwVerse.getItem(r, "token_no"));
                    JWebCellLite cell = dwVerseFlattened.setItem(rowCount - 1, rFlat, r + 1);
                    cell.setVar("Type", "Checkbox");
                    cell.setVar("Checked", "0");
                    cell.setName(locStr);
                    rFlat++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
            return dwVerseFlattened;
        }
    }

    private JWebDataWindowLite getVerseSegment(Connection con, String locStr) throws Exception {
        JWebDataWindowLite dwVerse = new JWebDataWindowLite(con);
        JWebUtils.setDBCon(con);

        String[] verseLoc = JWebUtils.getStringList(locStr, ":");
        String chapterNo = verseLoc[0];
        //String verseNo = verseLoc[0] == "9" ? verseLoc[1] : Integer.toString((Integer.parseInt(verseLoc[1]) - 1));
        String verseNo = verseLoc[1];
        dwVerse.resetRows();
        dwVerse.setQuery("Select T.chapter_no,T.verse_no,T.Token_no,S.segment_no,w.id,w.word,w.word_simple,token,token_simple from Token T,Segment S, Word w \n"
                + "where S.chapter_no=:1 and S.verse_no=:2 and S.chapter_no=T.chapter_no and T.verse_no=S.verse_no and S.token_no=T.token_no and W.id=S.word_id \n"
                + "Order by T.chapter_no,T.verse_no,T.token_no,segment_no");
        dwVerse.retrieve(new Object[]{chapterNo, verseNo});
        int rowCount = 1;
        JWebDataWindowLite dwVerseFlattened = new JWebDataWindowLite(rowCount, dwVerse.getRowCount());
        //for (int c = 0; c < dwVerse.getColumnCount(); c++) {
        for (int r = 0; r < dwVerse.getRowCount(); r++) {
            dwVerseFlattened.setColumnName(r, dwVerse.getItem(r, "token"));
            dwVerseFlattened.setItem(0, r, dwVerse.getItem(r, "token_no"));
            //dwVerseFlattened.setItem(c,0,dwVerse.getItem(r,"word"));
        }
        //}
        return dwVerseFlattened;

    }

    public void testCloudAuthentication() throws Exception {
        AWSCredentials myCredentials = new BasicAWSCredentials("AKIAJVAC4UHMY2USJNZQ", "qxUVyak4fjZRywGSnnRv+dvRDPDQ/yXHMcjeJbkb");
        AmazonS3 s3client = new AmazonS3Client(myCredentials);

        //System.out.println("Bucket:" + s3client.listBuckets().get(0));
        //AmazonS3 s3client = new AmazonS3Client(new PropertiesCredentials(ApplicationQuranTree.class.getResourceAsStream("AwsCredentialscs708.properties")));
        try {
            //System.out.println("Generating pre-signed URL.");
            java.util.Date expiration = new java.util.Date();
            long milliSeconds = expiration.getTime();
            milliSeconds += 1000 * 60 * 60; // Add 1 hour.
            expiration.setTime(milliSeconds);
            Hashtable fileTable = new Hashtable();
            ObjectListing files = s3client.listObjects(bucketName);
            List<S3ObjectSummary> list = files.getObjectSummaries();
            for (S3ObjectSummary file : list) {
                objectKey = file.getKey();
                GeneratePresignedUrlRequest generatePresignedUrlRequest
                        = new GeneratePresignedUrlRequest(bucketName, objectKey);
                generatePresignedUrlRequest.setMethod(HttpMethod.GET);
                generatePresignedUrlRequest.setExpiration(expiration);

                URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);
                fileTable.put("bucketName/" + objectKey, url.toString());
                //System.out.println("Pre-Signed URL = " + url.toString());
                Set set = fileTable.keySet(); // get set-view of keys
                Iterator itr = set.iterator();
                while (itr.hasNext()) {
                    String fileName = (String) itr.next();
                    System.out.println(fileTable.get(fileName) + " " + fileName);
                }

            }

        } catch (AmazonServiceException exception) {
            System.out.println("Caught an AmazonServiceException, "
                    + "which means your request made it "
                    + "to Amazon S3, but was rejected with an error response "
                    + "for some reason.");
            System.out.println("Error Message: " + exception.getMessage());
            System.out.println("HTTP  Code: " + exception.getStatusCode());
            System.out.println("AWS Error Code:" + exception.getErrorCode());
            System.out.println("Error Type:    " + exception.getErrorType());
            System.out.println("Request ID:    " + exception.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, "
                    + "which means the client encountered "
                    + "an internal error while trying to communicate"
                    + " with S3, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }

    }

    public void testDW() throws Exception {
        dbPath = "//Users/wadood/Google Drive/TaqwaWOD/";
        Connection con = getConnection();
        dwEmailPlan = new JWebDataWindowLite(con);
        //dwEmailPlan.runQuery("Select * from EmailPlan");
        dwEmailPlan.setQuery("Select * from EmailPlan");
        dwEmailPlan.retrieve(new Object[]{}, 0, 3);
        //System.err.println("RowCount: "+dwEmailPlan.getRowCount());
        dwEmailPlan.printAsString("RowCount: " + dwEmailPlan.getRowCount());
        //
        dwEmailPlan.resetRows();
        dwEmailPlan.retrieve(new Object[]{}, 4, 6);
        //System.err.println("RowCount: "+dwEmailPlan.getRowCount());
        dwEmailPlan.printAsString("RowCount: " + dwEmailPlan.getRowCount());
        //
        dwEmailPlan.resetRows();
        dwEmailPlan.retrieve(new Object[]{}, 7, 10);
        //System.err.println("RowCount: "+dwEmailPlan.getRowCount());
        dwEmailPlan.printAsString("RowCount: " + dwEmailPlan.getRowCount());

    }

    public void testExpr() {
        String s = "(A in ('1','2'),A,B)";
        String main = "";
        int stackNo = 0;
        int startPos = 0, endPos = 0;
        int startToken = 1, endToken = 0;
        Stack<Character> stack = new Stack<Character>();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == COMMA) {
                if (stackNo == 1) {
                    endToken = i - 1;
                    System.out.println("Token at:" + startToken + "," + endToken);
                    startToken = i + 1;
                }
            } else if (s.charAt(i) == L_PAREN) {
                stack.push(L_PAREN);
                stackNo = stackNo + 1;
                startPos = i;
            } else if (s.charAt(i) == R_PAREN) {
                if (stack.isEmpty()) {
                    System.out.println("Problem");
                } else if (stack.pop() != L_PAREN) {
                    System.out.println("Problem");
                }
                endPos = i;
                if (stackNo > 1) {
                    System.out.println("Immediate Token:" + startPos + "," + endPos);
                }
                stackNo = stackNo - 1;
                if (stackNo == 0) {
                    endToken = i - 1;
                    System.out.println("Token at:" + startToken + "," + endToken);

                }
            }

            // ignore all other characters
        }
        System.out.println("Stack : " + stack.isEmpty());
    }

    public void println(String original) {
        try {
            byte[] utf8Bytes = original.getBytes("UTF8");
            byte[] defaultBytes = original.getBytes();

            String roundTrip = new String(utf8Bytes, "UTF8");
            System.out.println("roundTrip = " + roundTrip);
            System.out.println();
            printBytes(utf8Bytes, "utf8Bytes");
            System.out.println();
            printBytes(defaultBytes, "defaultBytes");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public void printBytes(byte[] array, String name) {
        for (int k = 0; k < array.length; k++) {
            System.out.println(name + "[" + k + "] = " + "0x" + byteToHex(array[k]));
        }
    }

    public String byteToHex(byte b) {
        char hexDigit[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] array = {hexDigit[(b >> 4) & 0x0f], hexDigit[b & 0x0f]};
        return new String(array);
    }

    public String charToHex(char c) {
        byte hi = (byte) (c >>> 8);
        byte lo = (byte) (c & 0xff);
        return byteToHex(hi) + byteToHex(lo);
    }

    private class EncodedCharacter {

        // start stepping through the array from the beginning
        public String arabic, codes, shape;

        public EncodedCharacter(String arabic, String shape, String code) {
            this.arabic = arabic;
            this.codes = code;
            this.shape = shape;
        }

        public String toString() {
            return arabic + "->" + codes;
        }

        public boolean isFound(char code) {
            if (this.codes.indexOf(code) >= 0) {
                return true;
            } else {
                return false;
            }
        }

        public String toUnicode(char code) {
            if (this.codes.indexOf(code) >= 0) {
                return this.arabic;
            } else {
                return "";
            }
        }

    }

    public boolean isDiacritic(String c) {
        String diacritic = "<UnknownDiacritic><Fatha><Damma><Kasra><Fathatan><Dammatan><Kasratan><Shadda><Sukun><Maddah><HamzaAbove><HamzaBelow><HamzatWasl><AlifKhanjareeya>";
        return diacritic.indexOf("<" + c + ">") >= 0 ? true : false;
    }

    public String getArabic(ArrayList<EncodedCharacter> dictionary, char ch) {
        for (int row = 0; row < dictionary.size(); row++) {
            EncodedCharacter map = dictionary.get(row);
            if (map.isFound(ch)) {
                return map.arabic;
            }
        }
        System.err.println("(" + ch + ") : not found");
        return "";
    }

    public void addToBuilder(ArabicTextBuilder builder, ArrayList<EncodedCharacter> dictionary, char letter) {
        String[] character = JWebUtils.getStringList(getArabic(dictionary, letter), "+");
        if (character.length > 1) {
            System.out.println("More than one character:" + letter);
        }
        for (int i = 0; i < character.length; i++) {
            System.out.println(character[i]);
            if (isDiacritic(character[i])) {
                builder.add(DiacriticType.valueOf(character[i]));
            } else if (JWebUtils.isSame(character[i], WHITE_SPACE)) {
                builder.addWhitespace();
            } else {
                builder.add(CharacterType.valueOf(character[i]));
            }
        }
    }

    public boolean isKnownToEncoding(ArrayList<EncodedCharacter> dictionary, char ch) {
        for (int row = 0; row < dictionary.size(); row++) {
            EncodedCharacter map = dictionary.get(row);
            if (map.isFound(ch)) {
                return true;
            }
        }
        return false;
    }

    //Üºn»Öô]ø
    //hº]4øÂø
    //Ü»ãöÖø
    public String getEncoded(ArrayList<EncodedCharacter> dictionary, String raw_lines, boolean directionRTL) {
        String[] lines = JWebUtils.getStringList(raw_lines, "\n");
        String encoded = "";
        //boolean directionRTL=false;
        for (int l = lines.length - 1; l >= 0; l--) {
            String line = lines[l].trim();
            System.err.println(l + ":" + line);
            encoded = encoded + (directionRTL ? getEncodedRTL(line, dictionary) : getEncodedLTR(";" + line, dictionary));
            directionRTL = !directionRTL;
        }
        return encoded;

    }

    public String getEncodedRTL(String line, ArrayList<EncodedCharacter> dictionary) {
        String encoded = "";
        char data[] = line.toCharArray();
        int c = data.length - 1;
        ArabicTextBuilder builder = new ArabicTextBuilder();
        String letter = null, diacritic1 = null, diacritic2 = null;
        while (c >= 0) {
            if (isKnownToEncoding(dictionary, data[c])) {
                String[] character = JWebUtils.getStringList(getArabic(dictionary, data[c]), "+");
                if (character.length > 1) {
                    System.out.println("More than one character:" + character[0] + "," + character[1]);
                }
                for (int i = 0; i < character.length; i++) {
                    if (isDiacritic(character[i])) {
                        if (diacritic1 == null) {
                            diacritic1 = character[i];
                        } else {
                            diacritic2 = character[i];
                        }
                    } else if (JWebUtils.isSame(character[i], WHITE_SPACE)) {
                        encoded = encoded + "|" + WHITE_SPACE;
                    } else {
                        if (i == 0) {
                            letter = character[0];
                        } else {
                            letter = letter + "|" + character[1];
                        }
                    }
                }
            } else {
                System.out.println("Not found (" + data[c] + ")");
            }
            if (letter != null) {
                if (encoded == null) {
                    encoded = letter;
                } else {
                    encoded = encoded + "|" + letter;
                }
                if (diacritic1 != null) {
                    encoded = encoded + "|" + diacritic1;
                }
                if (diacritic2 != null) {
                    encoded = encoded + "|" + diacritic2;
                }
                //if (diacritic1!=null) letter=null;
                letter = null;
                diacritic1 = null;
                diacritic2 = null;
            }
            c = c - 1;
        }
        return encoded;
    }

    public String getEncodedLTR(String line, ArrayList<EncodedCharacter> dictionary) {
        String encoded = "";
        char data[] = line.toCharArray();
        int c = 0;
        String letter = null, diacritic = null;
        while (c < data.length) {
            if (isKnownToEncoding(dictionary, data[c])) {
                String[] character = JWebUtils.getStringList(getArabic(dictionary, data[c]), "+");
                if (character.length > 1) {
                    System.out.println("More than one character:" + character[0] + "," + character[1]);
                }
                for (int i = 0; i < character.length; i++) {
                    if (isDiacritic(character[i])) {
                        if (diacritic == null) {
                            diacritic = character[i];
                        } else {
                            diacritic = diacritic + "|" + character[i];
                        }
                    } else if (JWebUtils.isSame(character[i], WHITE_SPACE)) {
                        encoded = encoded + "|" + WHITE_SPACE;
                    } else {
                        if (letter == null) {
                            letter = character[i];
                        } else {
                            letter = letter + "|" + character[i];
                        }
                    }
                }
            } else {
                System.out.println("Not found (" + data[c] + ")");
            }
            if (diacritic != null) {
                if (encoded == null) {
                    encoded = letter;
                } else {
                    encoded = encoded + "|" + letter;
                }
                if (diacritic != null) {
                    encoded = encoded + "|" + diacritic;
                }
                letter = null;
                diacritic = null;
            }
            c = c + 1;
        }
        return encoded;
    }

    public ArabicText getArabicText(String encodedStr) {
        ArabicTextBuilder builder = new ArabicTextBuilder();
        String[] encoded = JWebUtils.getStringList(encodedStr, "|");

        for (int k = 0; k < encoded.length; k++) {

            if (isDiacritic(encoded[k])) {
                if (JWebUtils.isNotSame(encoded[k], UNKNOWN_DIACRITIC)) {
                    builder.add(DiacriticType.valueOf(encoded[k]));
                }
            } else if (JWebUtils.isSame(encoded[k], WHITE_SPACE)) {
                builder.addWhitespace();
            } else {
                builder.add(CharacterType.valueOf(encoded[k]));
            }
        }
        return builder.toText();
    }

    public void emit2(String foo) throws IOException {
        System.out.println("Literal: " + foo);
        System.out.print("Hex: ");
        for (char ch : foo.toCharArray()) {
            System.out.print(Integer.toHexString(ch & 0xFFFF) + " ");
        }
        System.out.println();
    }

    public void convertToUnicode() throws Exception {
        String sRTL = "RTL";
        boolean test = false;
        if (test) {
            System.out.println(getArabicText("Alif|Fatha|Lam|Lam|AlifKhanjareeya|Ha|Damma"));
            Token token = Document.getToken(87, 2, 3);
            System.out.println("<" + token.toUnicode() + "  " + token.toSimpleEncoding() + ">");
            ArabicTextBuilder builder = new ArabicTextBuilder();
            // Alif Fatha | Lam| Lam  Shadda |Ha Damma
            builder.add(CharacterType.Alif, DiacriticType.Fatha);
            builder.add(CharacterType.Lam);
            builder.add(CharacterType.Lam, DiacriticType.Shadda, DiacriticType.AlifKhanjareeya);
            builder.add(CharacterType.Ha, DiacriticType.Damma);
            //builder.add(CharacterType.AlifMaksura);
            //builder.add(DiacriticType.AlifKhanjareeya);
            System.out.println("<" + builder.toText() + ">");
            return;
        }
        String raw = JWebUtils.readTextFromFile(docPath + "wod_raw.txt");
        raw = raw.replaceAll("\n", " ");
        ArrayList<String> dictionaryFile = JWebUtils.readTextFromFileAsLines(docPath + "dict-map.txt");
        ArrayList<EncodedCharacter> dictionary = new ArrayList<EncodedCharacter>();
        String tab = "\t";
        ArrayList<ArabicText> word = new ArrayList<ArabicText>();
        for (int row = 0; row < dictionaryFile.size(); row++) {
            String line[] = JWebUtils.getStringList(dictionaryFile.get(row), "=");
            //System.out.println(row + ":" + dictionaryFile.get(row));
            EncodedCharacter toMap = new EncodedCharacter(line[0], line[1], line[2]);
            dictionary.add(toMap);
        }
        String[] encodedTokens = JWebUtils.getTokenList(raw, "{", "}");
        for (int i = 0; i < encodedTokens.length; i++) {
            String raw_lines = encodedTokens[i].trim();
            boolean directionRTL = JWebUtils.isSame(raw_lines.substring(0, 3), sRTL) ? true : false;
            raw_lines = raw_lines.substring(3);
            // dictWord = "áõ¡øÊöo .FÂø äøÖô]ø";test=true;
//            String raw_lines = "äö.# \n" +
//                    "]øÖ \n" +
//                    "^mø";
            //dictWord = "äüã ø$Ö ø]";
            test = true;
            String encodedStr = getEncoded(dictionary, raw_lines, directionRTL);
            System.out.println("|" + raw_lines + "| " + encodedStr);

            String[] encoded = JWebUtils.getStringList(encodedStr, "|");
            word.add(getArabicText(encodedStr));
            if (test) {
                break;
            }
        }
        //System.out.println("**");
        System.out.println("");
        for (ArabicText s : word) {
            String ucode = s.toUnicode();
            System.out.print(ucode);
            //printToFile(ucode);
            //println(s.toUnicode());
        }
        System.out.println("");
        //System.out.println("**");
    }

    public void printToFile(String contents) throws Exception {
        File f = new File(docPath + "test.txt");
        //FileUtils.write(f, contents, "UTF-8");
    }

    public void init() throws Exception {
        os = System.getProperty("os.name");
        System.out.println("OS:" + os);
        if (os.startsWith("Windows")) {
            dbPath = "C:\\Users\\Wadood\\Documents\\My Dropbox\\TaqwaWOD\\";
            docPath = "C:\\Users\\Wadood\\Documents\\My Dropbox\\QuranTree\\Documents\\";
            path = "C:\\Users\\Wadood\\Documents\\My Dropbox\\QuranTree\\";
            webDocPath = "//Users/Wadood/Downloads/tomcat/webapps/wod/WEB-INF/docs/";
        } else {
            dbPath = "//Users/wadood/Dropbox/QuranTree/";
            docPath = "//Users/wadood/Dropbox/QuranTree/words/";
            path = "//Users/wadood/Dropbox/QuranTree/";
            webDocPath = "//Users/Wadood/Downloads/tomcat/webapps/wod/WEB-INF/docs/";
        }
        con = getConnection();
        JWebUtils.setDBCon(con);
        initCloud();
        //dw.commit();

        //dbCloud = new AmazonSimpleDBClient(accessKeyId,secretAccessKey);
        //dbCloud = new AmazonSimpleDBClient(new PropertiesCredentials(ApplicationQuranTree.class.getResourceAsStream("AwsCredentials.properties")));
        //dbCloud = new AmazonSimpleDBClient(new PropertiesCredentials(ApplicationQuranTree.class.getResourceAsStream("AwsCredentials.properties")));
    }

    public void initWeb() throws Exception {
        os = System.getProperty("os.name");
        System.out.println("OS:" + os);
        if (os.startsWith("Windows")) {
            //dbPath = "d:\\tomcat\\webapps\\wod\\WEB-INF\\db\\";
            dbPath = "d:\\Users\\Wadood\\Dropbox\\TaqwaWOD\\";
            docPath = "d:\\tomcat\\webapps\\wod\\WEB-INF\\docs\\";
            path = "d:\\tomcat\\webapps\\wod\\WEB-INF\\";
            webDocPath = "d:\\tomcat\\webapps\\wod\\WEB-INF\\docs\\";
        } else {
            dbPath = "/Users/Wadood/Dropbox/TaqwaWOD/";
            docPath = "/Users/Wadood/Downloads/tomcat/webapps/wod/WEB-INF/docs/";
            path = "/Users/Wadood/Downloads/tomcat/webapps/wod/WEB-INF/";
            webDocPath = "//Users/Wadood/Downloads/tomcat/webapps/wod/WEB-INF/docs/";

        }
        initCloud();

    }

    public void initDatabases(Connection con) throws Exception {
        JWebUtils.setDBCon(con);
        dwVerse = new JWebDataWindowLite(con);
        dwSegment = new JWebDataWindowLite(con);
        //dw = new JWebDataWindowLite(con);
        dwWord = new JWebDataWindowLite(con);
        dwRoot = new JWebDataWindowLite(con);
        dwMeaning = new JWebDataWindowLite(con);
        dwExample = new JWebDataWindowLite(con);
        dwSearch = new JWebDataWindowLite(con);
        dwEmailPlan = new JWebDataWindowLite(con);
        dwEmailPlan.runQuery("Select * from EmailPlan where 1 <> 1");

        dwVerse.setQuery("Select * from Segment where chapter_no=:1 and verse_no=:2 Order by token_no,segment_no");
        dwSegment.setQuery("Select * from Segment where Id=:1");
        dwMeaning.runQuery("Select * from Meaning where 1<>1");
        dwExample.runQuery("Select * from Example where 1<>1");

        dwWord.runQuery("Select * from Word where 1<>1");
        dwRoot.runQuery("Select * from Root where 1<>1");

        dwWord.setKey(ID, true);
        dwRoot.setKey(ID, true);
        dwMeaning.setKey(ID, true);
        dwExample.setKey(ID, true);
    }

    public void initCloud() throws Exception {
        String accessKeyId = "0D30PAP4SFJT5E6086G2";
        String secretAccessKey = "v7ZlhrN3LZ1ZMICUykxq+H/19GFwaweLdFrsonoG";
        AWSCredentials myCredentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
        dbCloud = new AmazonSimpleDBClient(myCredentials);
    }

    public static void main(String[] args) {
        try {
            ApplicationQuranTree qt = new ApplicationQuranTree();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Connection getConnection(boolean newConnection) throws Exception {
        if (newConnection) {
            closeConnection();
        }
        return getConnection();

    }

    public Connection getConnection() throws Exception {
        if (con == null) {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:" + dbPath + "words.db");
            con.setAutoCommit(false);
        }
        return con;

    }

    public void closeConnection() {
        try {
            if (con != null) {
                con.close();
            }

        } catch (Exception e) {
        } finally {
            con = null;
        }
    }

    private void testMorphology() {
        ArrayList<String> morphology = JWebUtils.readTextFromFileAsLines("quranic-corpus-morphology.txt");
        System.out.println(morphology.size());
        String tab = "\t";
        for (int n = 0; n < 10; n++) {
            String line = morphology.get(n);
            System.out.println(line);
            String[] location = JWebUtils.getTokenList(line, "(", ")", ":");
            String items = line.substring(line.indexOf(tab)).trim();
            String form = JWebUtils.getToken(items, tab);
            items = items.substring(form.length()).trim();
            String tag = JWebUtils.getToken(items, tab);
            items = items.substring(tag.length()).trim();
            String feature = JWebUtils.getToken(items, tab);
            String[] features = JWebUtils.getTagList(feature, "|", "|");
            System.out.print("(" + location[0] + "," + location[1] + "," + location[2] + "," + location[3] + ")");
            System.out.print(tab + form);
            System.out.print(tab + tag);
            System.out.println(tab + feature);
            for (int f = 0; f < features.length; f++) {
                System.out.print(features[f] + "|");
            }
            System.out.println("");
        }

    }

    private String getVerse(String locStr) throws Exception {
        return getVerse(locStr, false);
    }

    private String getVerse(String locStr, boolean adjustForBismilla) throws Exception {
        String[] verseLoc = JWebUtils.getStringList(locStr, ":");
        String chapterNo = verseLoc[0];
        //String verseNo = (adjustForBismilla && verseLoc[0] != "9") ? Integer.toString((Integer.parseInt(verseLoc[1]) - 1)) : verseLoc[1];
        String verseNo = verseLoc[1];
        dwVerse.resetRows();
        dwVerse.retrieve(new Object[]{chapterNo, verseNo});
        String verse = "";
        //dwVerse.printAsString(chapterNo + "," + verseNo);
        int token, _token = -1;
        for (int v = 0; v < dwVerse.getRowCount(); v++) {
            token = dwVerse.getItemInt(v, TOKEN);
            //segment = dwVerse.getItemInt(v,SEGMENT);
            String form = dwVerse.getItem(v, FORM);
            if (_token == -1) {
                _token = token;
            }
            if (_token != token) {
                _token = token;
                verse = verse + " " + form;
            } else {
                verse = verse + form;
            }

        }
        return verse;
    }

    private void removeWord(String wordid) throws Exception {
        JWebUtils.executeUpdate("Delete from favorite where wordid=" + wordid);
        JWebUtils.executeUpdate("Delete from emailplan where wordid=" + wordid);
        JWebUtils.executeUpdate("Delete from example where meaningid in (Select m.id from word w, meaning m where w.id=" + wordid + " and m.wordid=w.id)");
        JWebUtils.executeUpdate("Delete from meaning where wordid=" + wordid);
        JWebUtils.commit();

    }

    private String replacePart(String text, boolean adjustForBismilla) throws Exception {
        if (text.indexOf(VERSE) >= 0) {
            String verseLocStr = JWebUtils.getToken(text, VERSE + "(", ")");
            String verseOriginalStr = VERSE + "(" + verseLocStr + ")";
            if (JWebUtils.isNotNull(verseLocStr)) {
                String verse = getVerse(verseLocStr, adjustForBismilla);
                text = JWebUtils.replaceStr(text, verseOriginalStr, verse);
            }

        }
        if (text.indexOf(TOKEN) >= 0) {
            String tokenLocStr = JWebUtils.getToken(text, TOKEN + "(", ")");
            String tokenOriginalStr = TOKEN + "(" + tokenLocStr + ")";
            if (JWebUtils.isNotNull(tokenLocStr)) {
                String verse = "\u200E" + getToken(tokenLocStr, adjustForBismilla) + "\u200E";
                text = JWebUtils.replaceStr(text, tokenOriginalStr, verse);
            }

        }

        return text;

    }

    private String getToken(String locStr) throws Exception {
        return getToken(locStr, false);
    }

    private String getToken(String locStr, boolean adjustForBismilla) throws Exception {
        //System.err.println("LocStr:"+locStr);
        String[] tokenLoc = JWebUtils.getStringList(locStr, ":");
        String chapterNo = tokenLoc[0];
        //String verseNo = (adjustForBismilla && tokenLoc[0] != "9") ? Integer.toString((Integer.parseInt(tokenLoc[1]) - 1)) : tokenLoc[1];
        String verseNo = tokenLoc[1];
        int tokenStart = Integer.parseInt(tokenLoc[2]);
        int tokenEnd = Integer.parseInt(tokenLoc[3]);
        dwVerse = new JWebDataWindowLite(con);
        dwVerse.setQuery("Select * from Segment where chapter_no=:1 and verse_no=:2 Order by verse_no,token_no,segment_no");
        dwVerse.resetRows();
        dwVerse.retrieve(new Object[]{chapterNo, verseNo});
        String versePart = "";

        int token, _token = -1;
        for (int v = 0; v < dwVerse.getRowCount(); v++) {
            token = dwVerse.getItemInt(v, TOKEN_NO);
            if (token >= tokenStart && token <= tokenEnd) {
                //segment = dwVerse.getItemInt(v,SEGMENT);
                String form = JWebUtils.getColumnValue("Select word from Word where id='" + dwVerse.getItem(v, WORD_ID) + "'", WORD);
                if (JWebUtils.isNotNull(form)) {
                    if (_token == -1) {
                        _token = token;
                    }
                    if (_token != token) {
                        _token = token;
                        versePart = versePart + " " + form;
                    } else {
                        versePart = versePart + form;
                    }
                }
            }
        }
        return versePart;
    }

    private class WOD {

        // start stepping through the array from the beginning
        public String xmlWord, wordid;
        public String rootid, meaning_simple, status;

        public WOD(String xmlWord, String wordid, String rootid, String status) {
            this.xmlWord = xmlWord;
            this.wordid = wordid;
            this.rootid = rootid;
            this.status = status;
            //this.meaning_simple=meaning_simple;
        }
    }

    public ArrayList<String> addWordToXML() throws Exception {
        ArrayList<String> wod = addWordToXML(docPath, "wod_stage.txt");
        JWebUtils.writeTextToFile(docPath + "wod_stage.xml", JWebUtils.getArrayAsString(wod), true);
        return wod;
    }

    public ArrayList<String> addWordToXML(String docPath, String fileName) throws Exception {
      ArrayList<String> wodFiletext = JWebUtils.readTextFromFileAsLines(docPath + fileName);
      return addWordToXML(docPath,wodFiletext);
    }
    public ArrayList<String> addWordToXML(String docPath, ArrayList<String> wodFiletext ) throws Exception {
        String word, meaning, example, root, meaning_simple, transliteration;
        ArrayList<String> wod = new ArrayList<String>();
        //ArrayList<String> wodFiletext = JWebUtils.readTextFromFileAsLines(docPath + fileName);
        boolean isFirstMeaning = true;
        for (int row = 0; row < wodFiletext.size(); row++) {
            String line = wodFiletext.get(row);
            //System.out.println(line);
            word = JWebUtils.getTokenAfter(line, WORD + ":");
            if (JWebUtils.isNotNull(word)) {
                wod.add("<Word>");
                wod.add("<Text>" + word.trim() + "</Text>");
                wod.add("<Status>NEW</Status>");
                continue;
            }
            root = JWebUtils.getTokenAfter(line, ROOT + ":");
            if (JWebUtils.isNotNull(root)) {
                wod.add("<Root>" + root.trim() + "</Root>");
                continue;
            }
            transliteration = JWebUtils.getTokenAfter(line, TRANSLITERATION + ":");
            if (JWebUtils.isNotNull(transliteration)) {
                wod.add("<Transliteration>" + transliteration.trim() + "</Transliteration>");
                continue;
            }
            meaning_simple = JWebUtils.getTokenAfter(line, MEANING_SIMPLE_XML + ":");
            if (JWebUtils.isNotNull(meaning_simple)) {
                wod.add("<MeaningSimple>" + meaning_simple.trim() + "</MeaningSimple>");
                continue;
            }
            meaning = JWebUtils.getTokenAfter(line, MEANING + ":");
            if (JWebUtils.isNotNull(meaning)) {
                if (isFirstMeaning) {
                    isFirstMeaning = false;
                } else {
                    wod.add(tab + "</Meaning>");
                }
                wod.add(tab + "<Meaning>");
                wod.add(tab + tab + "<Definition>" + meaning.trim() + "</Definition>");
                wod.add(tab + tab + "<Reference>Ghulam Farid</Reference>");
                continue;
            }
            example = JWebUtils.getTokenAfter(line, EXAMPLE + ":");
            if (JWebUtils.isNotNull(example)) {
                example = JWebUtils.replaceStr(example, "[", "Token(");
                example = JWebUtils.replaceStr(example, "]", ")");
                wod.add(tab + tab + "<Example>" + example.trim() + "</Example>");
            }
        }
        wod.add(tab + "</Meaning>");
        wod.add("</Word>");
        return wod;
    }

    public void addWord() throws Exception {
        addWord(docPath + "DictionaryXML.txt", false);
    }

    public void addWord(File file, boolean adjustForBismilla) throws Exception {
         String wodFiletext = JWebUtils.readTextFromFile(file);
         addWord(wodFiletext,adjustForBismilla);  
    }
    public void addWord(String wodFiletext, boolean adjustForBismilla) throws Exception {
        try {
            con = getConnection(true);
            initDatabases(con);
            JWebDataWindowLite dwWordId = new JWebDataWindowLite(con);
            String wordids = "", rootids = "";
            String[] words = JWebUtils.getXMLTokenList(wodFiletext, WORD);
            String dateStr = JWebUtils.getTodaysDateISO();
            //dateStr = "20130620";
            String dateFormat = "yyyyMMdd";
            ArrayList<WOD> wods = new ArrayList<WOD>();
            for (int w = 0; w < words.length; w++) {
                String xmlWord = words[w];
                //String word = JWebUtils.getXMLToken(wod, WORD);
                String text = JWebUtils.getXMLToken(xmlWord, TEXT);
                String status = JWebUtils.getXMLToken(xmlWord, STATUS);
                String root = JWebUtils.getXMLToken(xmlWord, ROOT);
                String meaning_simple = JWebUtils.getXMLToken(xmlWord, MEANING_SIMPLE);
                String transliteration = JWebUtils.getXMLToken(xmlWord, TRANSLITERATION);

                if (JWebUtils.isSame(status, STATUS_DONE)) {
                    continue;
                }
                String wordSQL = "Select id,root_id,meaning_simple from Word where word_simple='" + text + "'";
                //System.out.println("SQL: " + wordSQL);
                dwWordId.clear();
                dwWordId.runQuery(wordSQL);
                String wordid = dwWordId.getItem(0, 0);
                String rootid = dwWordId.getItem(0, 1);
                removeWordFromCloud(wordid);
                WOD wod = new WOD(xmlWord, wordid, rootid, status);
                wods.add(wod);

            }
            //int meaningid = getIdFromCloud(MEANING);
            //int exampleid = getIdFromCloud(EXAMPLE);
            //int emailplanid = getIdFromCloud(EMAIL_PLAN)+2;
            int meaningid = getMaxId(MEANING);
            int exampleid = getMaxId(EXAMPLE);
            int emailplanid = getMaxId(EMAIL_PLAN);

//if (emailplanid > 0) dateStr = getMaxColValueFromCloud(EMAIL_PLAN, PLAN_DATE.toLowerCase());
            //System.out.println("email:" + emailplanid + " example:" + exampleid + " meaning:" + meaningid);
            //if (1==1) return;
            for (WOD wod : wods) {
                String xmlWord = wod.xmlWord;
                String status = wod.status;
                if (JWebUtils.isSame(status, STATUS_DONE)) {
                    continue;
                }
                String wordid = wod.wordid;
                String rootid = wod.rootid;
                String text = JWebUtils.getXMLToken(xmlWord, TEXT);
                String root_meaning = JWebUtils.getXMLToken(xmlWord, ROOT);
                String meaning_simple = JWebUtils.getXMLToken(xmlWord, MEANING_SIMPLE_XML);
                String transliteration = JWebUtils.getXMLToken(xmlWord, TRANSLITERATION);
               // Single quotes must be handled.
                //
                if (JWebUtils.isNotNull(meaning_simple)) {
                    String updateSQL = "Update Word set meaning_simple='" + meaning_simple + "' where id=" + wordid;
                    //System.out.println("Meaning Simple Update:" + updateSQL);
                    JWebUtils.executeUpdate("Update Word set meaning_simple='" + meaning_simple + "' where id=" + wordid);
                }
                if (JWebUtils.isNotNull(root_meaning)) {
                    String updateSQL = "Update Root set meaning='" + root_meaning + "' where id=" + rootid;
                    System.out.println("Root Update:"+updateSQL);
                    JWebUtils.executeUpdate("Update Root set meaning='" + root_meaning + "' where id=" + rootid);

                }
                if (JWebUtils.isNotNull(transliteration)) {
                    //String updateSQL = "Update Word set transliteration='" + transliteration + "' where id=" + wordid;
                    //System.out.println("Meaning Simple Update:"+updateSQL);
                    JWebUtils.executeUpdate("Update Word set transliteration='" + transliteration + "' where id=" + wordid);
                }
                wordids = JWebUtils.isNull(wordids) ? wordid : wordids + "," + wordid;
                rootids = JWebUtils.isNull(rootids) ? rootid : rootids + "," + rootid;
                //
                java.util.Date date = JWebUtils.getDate(dateStr, dateFormat);
                java.util.Date plandate = JWebUtils.addDays(date, 1);
                String planDateStr = JWebUtils.getStringFromDate(plandate, dateFormat);
                dateStr = planDateStr;
                //Add to EMAIL_PLAN
                int row;
                row = dwEmailPlan.addRow();
                emailplanid++;
                dwEmailPlan.setItem(row, ID, emailplanid);
                dwEmailPlan.setItem(row, WORD_ID, wordid);
                dwEmailPlan.setItem(row, PLAN_DATE, planDateStr);
                dwEmailPlan.setItem(row, STATUS, STATUS_NEW);
                // Add to Meaning
                String[] meanings = JWebUtils.getXMLTokenList(xmlWord, MEANING);
                for (int m = 0; m < meanings.length; m++) {
                    meaningid++;
                    String definition = JWebUtils.getXMLToken(meanings[m], DEFINITION);
                    String reference = JWebUtils.getXMLToken(meanings[m], REFERENCE);

                    row = dwMeaning.addRow();
                    dwMeaning.setItem(row, ID, meaningid);
                    dwMeaning.setItem(row, WORD_ID, wordid);
                    dwMeaning.setItem(row, MEANING, JWebUtils.removeQuotes(definition));
                    dwMeaning.setItem(row, LANGUAGE_ID, 1);
                    // Add to Example
                    String[] examples = JWebUtils.getXMLTokenList(meanings[m], EXAMPLE);
                    for (int e = 0; e < examples.length; e++) {
                        exampleid++;
                        examples[e] = replacePart(examples[e], adjustForBismilla);
                        row = dwExample.addRow();
                        dwExample.setItem(row, ID, exampleid);
                        dwExample.setItem(row, MEANING_ID, meaningid);
                        dwExample.setItem(row, EXAMPLE, JWebUtils.removeQuotes(examples[e]));
                        dwExample.setItem(row, REFERENCE, reference);
                    }
                }
            }
            dwWord = new JWebDataWindowLite(con);
            dwWord.runQuery("Select * from Word where id in (" + wordids + ")");
            dwRoot = new JWebDataWindowLite(con);
            dwRoot.runQuery("Select * from Root where id in (" + rootids + ")");
            String delExampleSQL = "Delete from Example \n"
                    + "where meaning_id in (Select m.id from Word w, Meaning m Where w.id=m.word_id and w.id in (" + wordids + "))";
            //System.out.println(delExampleSQL);
            JWebUtils.executeUpdate(con, delExampleSQL);
            String delMeaningSQL = "Delete from Meaning where word_id in (" + wordids + ")";
            JWebUtils.executeUpdate(con, delMeaningSQL);
             
            
            dwWord.printAsString(WORD);
            //dwEmailPlan.printAsString(EMAIL_PLAN);
            //dwMeaning.printAsString(WORD);
            //dwExample.printAsString(ROOT);
            updateWOD();
            updateToCloud();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Message:" + e.getMessage());
        } finally {
            closeConnection();

        }
    }

    private void updateToCloud() throws Exception {
        JWebUtils.commit();
        dwMeaning.prepareForUpdate(MEANING.toLowerCase());
        dwExample.prepareForUpdate(EXAMPLE.toLowerCase());
        dwEmailPlan.prepareForUpdate(EMAIL_PLAN.toLowerCase());
        dwRoot.prepareForUpdate(ROOT.toLowerCase());
        dwWord.prepareForUpdate(WORD.toLowerCase());
        convertDataForCloud(dwMeaning, MEANING);
        convertDataForCloud(dwExample, EXAMPLE);
        convertDataForCloud(dwEmailPlan, EMAIL_PLAN);
        if (dwRoot.getRowCount() > 0) {
            convertDataForCloud(dwRoot, ROOT);
        }
        convertDataForCloud(dwWord, WORD);

    }

    private void removeItemsFromCloud(String tableName, String colName, String colValue) {
        String selectExpression = "select * from " + tableName + " where " + colName.toLowerCase() + "='" + colValue + "'";
        //System.out.println("Select Expression:"+selectExpression);
        SelectRequest selectRequest = new SelectRequest(selectExpression);
        for (Item item : dbCloud.select(selectRequest).getItems()) {
            dbCloud.deleteAttributes(new DeleteAttributesRequest(tableName, item.getName()));
        }
    }

    private void removeItemsFromCloud(String tableName, String colName, String colValue, String childTableName, String childColName) {
        String selectExpression = "select * from " + tableName + " where " + colName.toLowerCase() + "='" + colValue + "'";
        SelectRequest selectRequest = new SelectRequest(selectExpression);
        for (Item item : dbCloud.select(selectRequest).getItems()) {
            for (Attribute attribute : item.getAttributes()) {
                if (JWebUtils.isSame(attribute.getName(), ID)) ;
                removeItemsFromCloud(childTableName, childColName, attribute.getValue());
            }
            dbCloud.deleteAttributes(new DeleteAttributesRequest(tableName, item.getName()));
        }
    }

    private int getIdFromCloud(String tableName) {
        try {
            String selectExpression = "select * from " + tableName + " where itemName() > '000000' order by itemName() desc limit 1";
            //System.out.println("NoSQL: "+selectExpression);
            SelectRequest selectRequest = new SelectRequest(selectExpression);
            for (Item item : dbCloud.select(selectRequest).getItems()) {
                for (Attribute attribute : item.getAttributes()) {
                    if (JWebUtils.isSame(attribute.getName(), ID)) {
                        return Integer.parseInt(attribute.getValue());
                    }
                }
            }
        } catch (Exception ex) {
            return 0;
        }
        return 0;
    }

    private int getMaxId(String tableName) {
        return Integer.parseInt(JWebUtils.getColumnValue("Select max(id) as id from " + tableName, "id"));
    }

    private String getMaxColValueFromCloud(String tableName, String colName) {
        String selectExpression = "select " + colName.toLowerCase() + " from " + tableName + " where " + colName.toLowerCase() + " is not null order by " + colName.toLowerCase() + " desc limit 1";
        SelectRequest selectRequest = new SelectRequest(selectExpression);
        for (Item item : dbCloud.select(selectRequest).getItems()) {
            for (Attribute attribute : item.getAttributes()) {
                if (JWebUtils.isSame(attribute.getName(), colName)) {
                    return attribute.getValue();
                }
            }
        }
        return "";
    }

    private String getColValueFromCloud(String tableName, String colName, String keyValue) {
        String selectExpression = "select " + colName.toLowerCase() + " from " + tableName.toLowerCase() + " where itemName()='" + keyValue + "' limit 1";
        SelectRequest selectRequest = new SelectRequest(selectExpression);
        for (Item item : dbCloud.select(selectRequest).getItems()) {
            for (Attribute attribute : item.getAttributes()) {
                if (JWebUtils.isSame(attribute.getName(), colName)) {
                    return attribute.getValue();
                }
            }
        }
        return "";
    }

    private void removeWordFromCloud(String wordid) throws Exception {
        // Email Plan
        removeItemsFromCloud(EMAIL_PLAN, WORD_ID, wordid);
        // Meaning and Example
        removeItemsFromCloud(MEANING, WORD_ID, wordid, EXAMPLE, MEANING_ID);

    }

    private void updateWOD() throws Exception {
        dwMeaning.prepareForUpdate(MEANING.toLowerCase());
        dwExample.prepareForUpdate(EXAMPLE.toLowerCase());
        dwEmailPlan.prepareForUpdate(EMAIL_PLAN.toLowerCase());
        dwEmailPlan.update();
        dwMeaning.update();
        dwExample.update();
        dwExample.commit();
    }

    private void insertTranslation() throws Exception {
        Connection conn = getConnection();
        JWebDataWindowLite dw = new JWebDataWindowLite(conn);
        String table = VERSE.toLowerCase();
        dw.setQuery("Select * from " + table + " where 1 <> 1");
        dw.retrieve();
        dw.executeImmediate("delete from " + table);
        dw.commit();
        int maxLength = 0;
        String longVerseLoc = "";
        ArrayList<String> translationFile = JWebUtils.readTextFromFileAsLines("quran-english.txt");
        ArrayList<String> transliterationFile = JWebUtils.readTextFromFileAsLines("quran-english-transliteration.txt");

        //System.out.println(morphology.size());
        String tab = "\t";
        int r = 0;
        String _chapterNo = "0";
        String _text = "Start Value";
        for (int row = 0; row < Math.min(7, translationFile.size()); row++) {
            if (row % 1000 == 0) {
                System.out.println("Processing:" + row);
                updateAndClear(dw, table);
            }
            r = dw.addRow();
            String line = translationFile.get(row).trim();

            if (JWebUtils.isEmpty(line)) {
                continue;
            }
            int tabPos = line.indexOf(tab);
            String text = line.substring(tabPos).trim();
            String[] verseLoc = JWebUtils.getStringList(line.substring(0, tabPos), ":");
            String chapterNo = verseLoc[0];
            String verseNo = verseLoc[1];
            //System.out.println(chapterNo+":"+verseNo);
            if (JWebUtils.isNull(chapterNo)) {
                System.out.println("Null Chapter nos at:" + _chapterNo);
                _chapterNo = chapterNo;
            }
            _chapterNo = chapterNo + ":" + verseNo;
            Verse verse = Document.getVerse(Integer.parseInt(chapterNo), Integer.parseInt(verseNo));

            //System.out.println("Chapter No:"+chapterNo+ " verseNo:"+verseNo);
            dw.setItem(r, ID, String.valueOf(row + 1));
            dw.setItem(r, CHAPTER_NO, chapterNo);
            dw.setItem(r, VERSE_NO, verseNo);
            dw.setItem(r, TRANSLATION, text);
            dw.setItem(r, VERSE, verse.toUnicode());
            if (text.length() > maxLength) {
                maxLength = text.length();
                longVerseLoc = chapterNo + "," + verseNo;
            }
            _text = text;

        }
        updateAndClear(dw, table);
        //System.out.println("Max Length:" + maxLength + " Loc:" + longVerseLoc);
    }

    public void insertTransliteration() throws Exception {
        Connection conn = getConnection();
        ArrayList<String> transliterationFile = JWebUtils.readTextFromFileAsLines("quran-english-transliteration.txt");

        //System.out.println(morphology.size());
        String tab = "|";
        int r = 0;
        String _chapterNo = "0";
        String _text = "Start Value";
        for (int row = 0; row < Math.max(7, transliterationFile.size()); row++) {
            if (row % 1000 == 0) {
                System.out.println("Processing:" + row);
            }
            String line = transliterationFile.get(row).trim();

            if (JWebUtils.isEmpty(line)) {
                continue;
            }
            String[] verseLoc = JWebUtils.getStringList(line, tab);
            String chapterNo = verseLoc[0];
            String verseNo = verseLoc[1];
            String transliteration = verseLoc[2];
            System.out.println(chapterNo + ":" + verseNo + ":" + transliteration);
            if (JWebUtils.isNull(chapterNo)) {
                System.out.println("Null Chapter nos at:" + _chapterNo);
                _chapterNo = chapterNo;
            }
            String sql = " UPDATE Verse SET transliteration = '" + transliteration + "' Where chapter_no = " + chapterNo + " and verse_no=" + verseNo;
            JWebUtils.executeUpdate(sql);

        }
        JWebUtils.commit();
        //System.out.println("Max Length:" + maxLength + " Loc:" + longVerseLoc);
    }

    private JWebDataWindowLite initialize(String table) throws Exception {
        Connection con = getConnection();
        JWebDataWindowLite dw = new JWebDataWindowLite(con);
        dw.setQuery("Select * from " + table + " where 1 <> 1");
        dw.retrieve();
        if (update_mode) {
            dw.executeImmediate("delete from " + table);
            dw.commit();
        }
        dw.setKey(ID, true);
        dw.prepareForUpdate(table);
        return dw;

    }

    private int getId(String transliteration, JWebDataWindowLite dw) throws Exception {
        int id = dw.find(TRANSLITERATION, transliteration);
        if (id >= 0) {
            return dw.getItemInt(id, ID);
        }
        dwSearch.clear();
        if (transliteration.indexOf("'") >= 0) {
            dwSearch.setQuery("Select Id from " + dw.getUpdateTable() + " where Transliteration=\":1\"");
        } else {
            dwSearch.setQuery("Select Id from " + dw.getUpdateTable() + " where Transliteration=':1'");
        }
        dwSearch.retrieve(new Object[]{transliteration});
        if (dwSearch.getRowCount() > 0) {
            id = dwSearch.getItemInt(0, ID);
        }
        return id;
    }

    private void insertSimpleMeaning() throws Exception {
        String table = MEANING_SIMPLE.toLowerCase();
        JWebDataWindowLite dwMeaningSimple = initialize(table);
        JWebDataWindowLite dwDictionary = new JWebDataWindowLite(con);
        dwDictionary.runQuery("Select Word,WordSimple,Meaning from Dictionary");
        JWebDataWindowLite dwWord = new JWebDataWindowLite(con);
        dwWord.runQuery("Select Id,WordSimple,MeaningSimple from Word");
        int meaningSimpleKey = 0;

        for (int row = 0; row < Math.max(10, dwWord.getRowCount()); row++) {
            if (row % 500 == 0 && row > 0) {
                System.out.println("Processing:" + row);
                updateAndClear(dwMeaningSimple, table);
            }
            //dwWord.setRowStatus(row,JWebDataWindowLite.STATUS_NOTMODIFIED);
            String meaning = null;
            String wordsimple = dwWord.getItem(row, WORD_SIMPLE);
            for (int w = 0; w < dwDictionary.getRowCount(); w++) {
                String word = dwDictionary.getItem(w, 1);
                if (JWebUtils.isNotNull(word) && JWebUtils.isNotNull(wordsimple)) {
                    if (JWebUtils.isSame(wordsimple, word)) {
                        meaning = dwDictionary.getItem(w, MEANING);
                        break;
                    }
                    if (wordsimple.length() > 1 && word.indexOf(wordsimple) >= 0 && (word.length() - wordsimple.length() < 3)) {
                        meaning = dwDictionary.getItem(w, MEANING);
                        break;
                    }
                }
            }
            if (JWebUtils.isNotNull(meaning)) {
                meaningSimpleKey++;
                int rMeaningSimple = dwMeaningSimple.addRow();
                dwMeaningSimple.setItem(rMeaningSimple, ID, meaningSimpleKey);
                dwMeaningSimple.setItem(rMeaningSimple, WORD_ID, dwWord.getItem(row, ID));
                dwMeaningSimple.setItem(rMeaningSimple, MEANING_SIMPLE, meaning);
                //System.out.println(row+"XX: "+wordsimple+"("+meaning+")");
            } else {
                dwWord.setRowStatus(row, JWebDataWindowLite.STATUS_NOTMODIFIED);
                //System.out.println(row+"  : "+wordsimple+"(******)");
            }
        }
        //dwWord.setKey(MEANING_SIMPLE,true);
        //dwWord.setKey(WORD_SIMPLE,true);
        updateAndClear(dwMeaningSimple, table);

    }

    private static int countOccurrences(String arg1, String arg2) {
        int count = 0;
        int index = 0;
        while ((index = arg1.indexOf(arg2, index)) != -1) {
            ++index;
            ++count;
        }
        return count;
    }

    private boolean IsAMeaningLine(String line) {
        String[] nm = new String[]{"pronoun.", "masculine", "3rd.", "sing.", "Lane", "Lexicon", "Volume", "page", "impf.", "pass.", "(", ")", " perf.", "barakah", "n.f.", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "vb.", "n.", ".", ",", ":", "LL", "V1", "p:", "perf.", "act.", "n.m."};
        int occurences = 0;
        for (String s : nm) {
            occurences = occurences + (s.length() * countOccurrences(line, s));
        }
        if (occurences > line.length() / 2) {
            return false;
        }
        return true;
    }

    private void insertAlphabet() throws Exception {
        JWebDataWindowLite dwAlphabet = initialize(ALPHABET);

        ArrayList<String> alphabetFile = JWebUtils.readTextFromFileAsLines("alphabets.txt");
        for (int row = 0; row < alphabetFile.size(); row++) {

            String line = alphabetFile.get(row);
            String fields[] = JWebUtils.getStringList(line, ":");
            int r = dwAlphabet.addRow();
            dwAlphabet.setItem(r, ID, row + 1);
            dwAlphabet.setItem(r, TRANSLITERATION, fields[0]);
            dwAlphabet.setItem(r, ALPHABET, fields[1]);
        }
        updateAndClear(dwAlphabet, ALPHABET);

    }

    private void insertClass() throws Exception {
        JWebDataWindowLite dwPartsOfSpeech = initialize(CLASS);

        ArrayList<String> partsOfSpeechFile = JWebUtils.readTextFromFileAsLines("PartsOfSpeech.txt");
        for (int row = 1; row < partsOfSpeechFile.size(); row++) {

            String line = partsOfSpeechFile.get(row);
            System.out.println(line);
            String fields[] = JWebUtils.getStringList(line, "\t");
            int r = dwPartsOfSpeech.addRow();
            dwPartsOfSpeech.setItem(r, ID, row);
            dwPartsOfSpeech.setItem(r, CLASS, fields[0]);
            dwPartsOfSpeech.setItem(r, TAG, fields[1]);
            dwPartsOfSpeech.setItem(r, NAME, fields[2]);
            dwPartsOfSpeech.setItem(r, TRANSLATION, fields[3]);
        }
        updateAndClear(dwPartsOfSpeech, CLASS);

    }

    private void insertRootDictionary(JWebDataWindowLite dwRoot, ArrayList<String> roots) throws Exception {
        for (int row = 0; row < roots.size(); row++) {
            String line = roots.get(row);
            String fields[] = JWebUtils.getStringList(line, tab);
            String meaning = fields[2];
            String rootLetters = fields[3] + fields[4] + fields[5];
            int findRow = dwRoot.find(ROOT, rootLetters);
            if (findRow >= 0) {
                dwRoot.setItem(findRow, MEANING, meaning);
            }
        }
    }

    public JWebDataWindowLite getWordInfo(String wordID) throws Exception {
        JWebDataWindowLite dw = null;
        Connection con = null;
        try {
            con = getConnection();
            dw = new JWebDataWindowLite(con);
            dw.runQuery("Select W.word_simple as Text,r.meaning as Root,W.meaning_simple as MeaningSimple,W.transliteration as Transliteration\n"
                    + " from Word w LEFT OUTER JOIN Root r on w.root_id = r.id \n"
                    + " where W.id=" + wordID);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.close();
            con = null;
            return dw;
        }

    }

    private void insertRootMeaning() throws Exception {
        ArrayList<String> rootDictionary = JWebUtils.readTextFromFileAsLines("RootMeaning.txt");
        JWebDataWindowLite dwRoot = new JWebDataWindowLite(con);
        dwRoot.runQuery("Select * from Root");
        insertRootDictionary(dwRoot, rootDictionary);
        initialize(ROOT);
        updateAndClear(dwRoot, ROOT);
    }

    private void insertRootMeaningOld() throws Exception {
        JWebDataWindowLite dwAlphabet = new JWebDataWindowLite(con);
        dwAlphabet.runQuery("Select * from Alphabet");
        String table = ROOT_MEANING;
        JWebDataWindowLite dwRootMeaning = initialize(table);
        ArrayList<String> alphabet = new ArrayList<String>();
        ArrayList<String> roots = new ArrayList<String>();
        int rootMeaningKey = 0;
        ArrayList<String> rootMeaning = JWebUtils.readTextFromFileAsLines("root.txt");
        int rRootMeaning = 0;
        String equals = "=";
        String root, meaning;
        boolean meaningOpen = false;
        int row = 0;
        while (row < rootMeaning.size()) {
            String line = rootMeaning.get(row);
            meaning = "";
            row++;
            if (line.indexOf(equals) >= 0) {
                String fields[] = JWebUtils.getStringList(line, equals);
                root = null;
                if (fields.length == 3) {
                    root = fields[fields.length - 2];
                    meaning = fields[fields.length - 1];
                    //System.out.println(line);
                } else if (fields.length == 2) {
                    root = fields[0];
                    meaning = fields[1];
                    if (meaning.trim().length() < 6) {
                        //alphabet.add(root + "=" + meaning);
                        root = null;
                    } else if (fields[1].trim().length() < 15 && countOccurrences(fields[1], "-") >= 2) {
                        root = fields[1];
                        meaning = "";
                    }
                } else if (fields.length == 1) {
                    root = fields[0];
                }
                boolean isMeaningContd = true;
                while (row < rootMeaning.size() && isMeaningContd) {
                    line = rootMeaning.get(row);
                    if (JWebUtils.isNotEmpty(line)) {
                        if (line.indexOf(equals) >= 0) {
                            isMeaningContd = false;
                        } else if (IsAMeaningLine(line)) {
                            meaning = JWebUtils.isNull(meaning) ? line : meaning + ".    " + line;
                            row++;
                        } else {
                            isMeaningContd = false;
                            //System.out.println("   " + line);
                        }
                    } else {
                        row++;
                    }
                }
                if (JWebUtils.isNotNull(root) && JWebUtils.isNotNull(meaning)) {
                    //System.out.println("{ " + root + "="+meaning + " }");
                    String letters[] = JWebUtils.getStringList(root, "-");
                    if (letters.length == 3) {
                        String[] arabicLetter = new String[3];
                        for (int a = 0; a < 3; a++) {
                            int pos = dwAlphabet.find(TRANSLITERATION, letters[a].trim());
                            if (pos >= 0) {
                                arabicLetter[a] = dwAlphabet.getItem(pos, ALPHABET);
                            } else {
                                System.out.println(" Mismatch { " + root + "=" + meaning + " }");
                            }
                        }

                        roots.add("{ " + root + "=" + meaning + " }");
                        rootMeaningKey++;
                        rRootMeaning = dwRootMeaning.addRow();
                        dwRootMeaning.setItem(rRootMeaning, ID, rootMeaningKey);
                        dwRootMeaning.setItem(rRootMeaning, ROOT, root);
                        dwRootMeaning.setItem(rRootMeaning, MEANING, meaning);
                        dwRootMeaning.setItem(rRootMeaning, LETTER_1, arabicLetter[0]);
                        dwRootMeaning.setItem(rRootMeaning, LETTER_2, arabicLetter[1]);
                        dwRootMeaning.setItem(rRootMeaning, LETTER_3, arabicLetter[2]);
                        dwRootMeaning.setItem(rRootMeaning, LETTER_1_TRANSLITERATION, letters[0]);
                        dwRootMeaning.setItem(rRootMeaning, LETTER_2_TRANSLITERATION, letters[1]);
                        dwRootMeaning.setItem(rRootMeaning, LETTER_3_TRANSLITERATION, letters[2]);

                    } else {
                        //System.out.println("{ Not a trilateral root " + root + "=" + meaning + " }");
                    }

                }

            }
        }
        updateAndClear(dwRootMeaning, table);

        System.out.println("*******************  ");
        for (int i = 0; i < alphabet.size(); i++) {
            int pos = dwAlphabet.find(TRANSLITERATION, alphabet.get(i).trim());
            //System.out.println((i+1)+" "+alphabet.get(i) + "  :  "+ (pos>=0?"Found":"Not found"));
        }
//        for (String letter : alphabet) {
//            System.out.println(letter);
//        }
        System.out.println("*******************  ");
        for (String line : roots) {
            //System.out.println(line);
        }
    }

    private StringBuilder getURLContent(URL url) throws IOException {
        URLConnection conn = url.openConnection();
        String encoding = conn.getContentEncoding();
        if (encoding == null) {
            encoding = "ISO-8859-1";
        }
        //URLEncoder.encode(urlString.toString(), "utf-8");
        encoding = "utf-8";
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));
        StringBuilder sb = new StringBuilder(16384);
        try {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
        } finally {
            br.close();
        }
        return sb;
    }

    //            "<td><span\\s+class=\"flagicon\"[^>]*>"
//            + ".*?</span><a href=\""
//            + "([^\"]+)"      // first piece of data goes up to quote
//            + "\"[^>]*>"      // end quote, then skip to end of tag
//            + "([^<]+)"       // name is data up to next tag
//            + "</a>.*?</td>"; // end a tag, then skip to the td close tag
    private void updateAndClear(ArrayList<String> tokens, String fileName) throws Exception {
        if (!update_mode) {
            return;
        }
        StringBuffer lines = null;
        for (String line : tokens) {
            if (lines == null) {
                lines = new StringBuffer(line);
            } else {
                lines.append(JWebUtils.getNewLineChar() + line);
            }
        }
        JWebUtils.writeTextToFile(fileName, lines.toString(), false);
        tokens.clear();
    }

    public void insertTokens() throws Exception {
        String table = TOKEN;
        JWebDataWindowLite dwToken = initialize(table);
        int dictionaryKey = 0;
        int wordid = -1;
        ArabicText arabicText;
        ArrayList<String> dictionary = JWebUtils.readTextFromFileAsLines("token_meaning.txt");
        int rDictionary = 0;

        for (int row = 1; row < Math.max(100, dictionary.size()); row++) {
            if (row % 500 == 0) {
                System.out.println("Processing:" + row);
                updateAndClear(dwToken, table);
            }
            String line = dictionary.get(row);
            String fields[] = JWebUtils.getStringList(line, tab);
            String[] verseLoc = JWebUtils.getStringList(fields[0], ":");
            int chapter_no = Integer.parseInt(verseLoc[0]);
            int verse_no = Integer.parseInt(verseLoc[1]);
            int token_no = Integer.parseInt(verseLoc[2]);
            String meaningsimple = "";
            if (fields.length >= 3) {
                meaningsimple = fields[2];
            } else {
                System.out.println("No meaning found for: " + chapter_no + ":" + verse_no + ":" + token_no);
            }
            Token token = Document.getToken(chapter_no, verse_no, token_no);
            int rToken = dwToken.addRow();
            dwToken.setItem(rToken, ID, row + 1);
            dwToken.setItem(rToken, TOKEN, token.toUnicode());
            dwToken.setItem(rToken, TOKEN_SIMPLE, token.removeDiacritics().toUnicode());
            dwToken.setItem(rToken, TRANSLITERATION, token.toBuckwalter());
            dwToken.setItem(rToken, MEANING, meaningsimple);
            //dwToken.setItem(rToken, , fields[3]);
            dwToken.setItem(rToken, CHAPTER_NO, chapter_no);
            dwToken.setItem(rToken, VERSE_NO, verse_no);
            dwToken.setItem(rToken, TOKEN_NO, token_no);

        }
        updateAndClear(dwToken, table);

    }

    private void getAllTokenMeaning() throws Exception {
        int row = 0;
        ArrayList<String> tokens = new ArrayList<String>();
        int chapter_no, verse_no, token_no;
        int startChapter = 71;
        int endChapter = 114;
        int startVerse = -1;
        String tokenMeaning;
        String fileName = "token_meaning.txt";
        JWebDataWindowLite dwSearch = new JWebDataWindowLite(con);
        dwSearch.runQuery("Select distinct token_simple,meaning from Token");

        //JWebUtils.writeTextToFile(fileName,"Location"+tab+"Transliteration"+tab+"Translation",true);
        for (Chapter chapter : Document.getChapters()) {
            java.util.Iterator<Verse> verseItr = chapter.iterator();
            chapter_no = chapter.getChapterNumber();
            if (chapter_no < startChapter) {
                continue;
            }
            while (verseItr.hasNext()) {
                Verse verse = verseItr.next();
                verse_no = verse.getVerseNumber();
                if (chapter_no == startChapter && verse_no < startVerse) {
                    continue;
                }
                for (Token token : verse.getTokens()) {
                    token_no = token.getTokenNumber();
                    int pos = dwSearch.find(TOKEN_SIMPLE, token.removeDiacritics().toUnicode());
                    if (pos >= 0) {
                        tokenMeaning = dwSearch.getItem(pos, MEANING);
                    } else {
                        tokenMeaning = getTokenMeaning(token, chapter_no, verse_no, token_no);
                        dwSearch.addRow(token.removeDiacritics().toUnicode(), tokenMeaning);
                    }
                    tokens.add(chapter_no + ":" + verse_no + ":" + token_no + tab + token.toBuckwalter() + tab + tokenMeaning);
                    if (row % 250 == 0 && row > 0) {
                        System.out.println("Processing:" + row + " at " + chapter_no + ":" + verse_no);
                        updateAndClear(tokens, fileName);
                    }
                    row++;
                }

            }
            if (chapter_no >= endChapter) {
                break;
            }
        }
        updateAndClear(tokens, fileName);

    }

    private String getTokenMeaning(Token token, int chapter_no, int verse_no, int token_no) throws Exception {
        URL url = new URL("http://corpus.quran.com/wordmorphology.jsp?location=(" + chapter_no + ":" + verse_no + ":" + token_no + ")");
        StringBuilder page = getURLContent(url);
        String s = page.toString();
        //byte[] utf8Bytes = page.;
        //String roundTrip = new String(utf8Bytes, "UTF8");
        //String s =  new String(byte[],"UTF8");
        //yalʿanuhumu</a><br/>curses them</td>
        //Token token = Document.getToken(chapter_no, verse_no, token_no);
        //int pos = s.indexOf(token.toBuckwalter());
        String sToken = new String(token.toBuckwalter().toCharArray());
        //System.out.println(sToken+":"+token.toBuckwalter() + " , " + token.toUnicode() + " , " + token.toSimpleEncoding());
        //System.out.println(s);
        String start = "<span class=\"location\">(" + chapter_no + ":" + verse_no + ":" + token_no + ")</span><br />";
        String end = "able";
        String container = JWebUtils.getToken(s, start, end);
        //System.out.println(container);
        start = "</a><br />";
        end = "<t";
        String container2 = JWebUtils.getToken(container, start, end).trim();
        end = "<br />";
        int pos = container2.indexOf(end);
        if (pos > 0) {
            start = "<br />";
            end = "<t";
            String container3 = JWebUtils.getToken(container2, start, end).trim();
            return container3;
        } else {
            return container2.trim();
        }

    }

    private void makeDictionary() throws Exception {
        String table = DICTIONARY.toLowerCase();
        JWebDataWindowLite dwDictionary = initialize(table);
        JWebDataWindowLite dwWord = new JWebDataWindowLite(con);
        dwWord.runQuery("Select Id,Chapter,Verse,Token,Segment,WordId,WordSimple from Word W, Segment S Where S.word_id=W.word_id Order by chapter_no,verse_no,token_no,segment_no");

        int dictionaryKey = 0;
        int wordid = -1;
        ArabicText arabicText;
        ArrayList<String> dictionary = JWebUtils.readTextFromFileAsLines("quran-dict-test.txt");
        int rDictionary = 0;

        for (int row = 1; row < Math.max(100, dictionary.size()); row++) {
            if (row % 500 == 0) {
                System.out.println("Processing:" + row);
                updateAndClear(dwDictionary, table);
            }
            String line = dictionary.get(row);
            String fields[] = JWebUtils.getStringList(line, tab);
            String wordsimple = fields[3];
            //System.out.println(row + " [" + wordsimple + "]");
            wordid = -1;
            int findRow = dwWord.find(1, wordsimple);
            if (findRow >= 0) {
                wordid = dwWord.getItemInt(findRow, 0);
            } else {
                for (int w = 0; w < dwWord.getRowCount(); w++) {
                    String word = dwWord.getItem(w, 1);
                    if (JWebUtils.isNotNull(word) && word.length() > 2) {
                        if (wordsimple.indexOf(word) >= 0) {
                            wordid = dwWord.getItemInt(w, 0);
                            break;
                        }
                    }
                }
            }
            if (wordid >= 0) {
                dictionaryKey++;
                rDictionary = dwDictionary.addRow();
                dwDictionary.setItem(rDictionary, ID, dictionaryKey);
                dwDictionary.setItem(rDictionary, WORD, fields[1]);
                dwDictionary.setItem(rDictionary, MEANING, fields[2]);
                dwDictionary.setItem(rDictionary, WORD_SIMPLE, fields[3]);
                dwDictionary.setItem(rDictionary, WORD_ID, wordid);
            }
        }
        updateAndClear(dwDictionary, table);

    }

    private void insertDictionary() throws Exception {
        String table = DICTIONARY.toLowerCase();
        JWebDataWindowLite dwDictionary = initialize(table);
        JWebDataWindowLite dwWord = new JWebDataWindowLite(con);
        dwWord.runQuery("Select Id,WordSimple from Word");
        JWebDataWindowLite dwSegment = new JWebDataWindowLite(con);
        dwSegment.runQuery("Select Id,Chapter,Verse,Token,Segment,WordId from segment Order by Chapter,Verse,Token,Segment");

        int dictionaryKey = 0;
        int wordid = -1;
        ArabicText arabicText;
        ArrayList<String> dictionary = JWebUtils.readTextFromFileAsLines("quran-dict.txt");
        int rDictionary = 0;

        for (int row = 1; row < Math.max(100, dictionary.size()); row++) {
            if (row % 500 == 0) {
                System.out.println("Processing:" + row);
                updateAndClear(dwDictionary, table);
            }
            String line = dictionary.get(row);
            String fields[] = JWebUtils.getStringList(line, tab);
            String wordsimple = fields[3];
            //System.out.println(row + " [" + wordsimple + "]");
            wordid = -1;
            int findRow = dwWord.find(1, wordsimple);
            if (findRow >= 0) {
                wordid = dwWord.getItemInt(findRow, 0);
            }
            if (wordid >= 0) {
                dictionaryKey++;
                rDictionary = dwDictionary.addRow();
                dwDictionary.setItem(rDictionary, ID, dictionaryKey);
                dwDictionary.setItem(rDictionary, WORD, fields[1]);
                dwDictionary.setItem(rDictionary, MEANING, fields[2]);
                dwDictionary.setItem(rDictionary, WORD_SIMPLE, fields[3]);
                dwDictionary.setItem(rDictionary, WORD_ID, wordid);
            }
        }
        updateAndClear(dwDictionary, table);

    }

    private void insertDictionary(JWebDataWindowLite dwWord, ArrayList<String> dictionary) throws Exception {
        for (int row = 1; row < dictionary.size(); row++) {
            String line = dictionary.get(row);
            String fields[] = JWebUtils.getStringList(line, tab);
            String wordsimple = fields[3];
            int findRow = dwWord.find(WORD_SIMPLE, wordsimple);
            if (findRow >= 0) {
                //System.out.println(wordsimple);
                String meaning_simple = JWebUtils.removeQuotes(fields[2]);
                dwWord.setItem(findRow, MEANING_SIMPLE, meaning_simple);
            }
        }

    }

    private String extractMeaning(String meaning_simple) {
        meaning_simple = meaning_simple.trim();
        String remove = JWebUtils.getToken(meaning_simple, "(", ")");
        if (JWebUtils.isNotNull(remove)) {
            meaning_simple = JWebUtils.replaceStr(meaning_simple, "(" + remove + ")", "");
        }
        meaning_simple = JWebUtils.removeQuotes(meaning_simple);
        remove = JWebUtils.getToken(meaning_simple, "(", ")");
        if (JWebUtils.isNotNull(remove)) {
            meaning_simple = JWebUtils.replaceStr(meaning_simple, "(" + remove + ")", "");
        }

        meaning_simple = meaning_simple.trim();
        return meaning_simple;
    }

    private void insertDictionary(JWebDataWindowLite dwWord) throws Exception {
        for (int row = 0; row < dwWord.getRowCount(); row++) {
            String word_simple = dwWord.getItem(row, WORD_SIMPLE);
            String meaning_simple = JWebUtils.getColumnValue("Select meaning from Token where token_simple='" + word_simple + "'", MEANING);
            if (JWebUtils.isNotNull(meaning_simple)) {
                meaning_simple = extractMeaning(meaning_simple);
                String current_meaning_simple = dwWord.getItem(row, MEANING_SIMPLE);
                if (JWebUtils.isNull(current_meaning_simple)) {
                    dwWord.setItem(row, MEANING_SIMPLE, meaning_simple);
                }
            }
        }

    }

    private void insertChapters() throws Exception {
        JWebDataWindowLite dwChapter = initialize(CHAPTER);
        int chapterKey = 0;
        ArabicText arabicText;
        ArrayList<String> chapters = JWebUtils.readTextFromFileAsLines("quran-chapters.txt");
        //1.	Al-Fatihah (The Opening)
        int rChapter = 0;
        for (int row = 0; row < Math.max(100, chapters.size()); row++) {
            String line = chapters.get(row);
            //System.out.println(line);
            String chapterid = JWebUtils.getToken(line, ".").trim();
            String englishName = JWebUtils.getToken(line, "(", ")").trim();
            String chapter = JWebUtils.getToken(line, ".", "(").trim();
            chapterKey++;
            rChapter = dwChapter.addRow();
            dwChapter.setItem(rChapter, ID, chapterid);
            dwChapter.setItem(rChapter, TRANSLITERATION, chapter);
            dwChapter.setItem(rChapter, TRANSLATION, englishName);
        }
        updateAndClear(dwChapter, CHAPTER);

    }

    private void insertSegments() throws Exception {
        update_mode = true;
        JWebDataWindowLite dwWord = initialize(WORD);
        JWebDataWindowLite dwSegment = initialize(SEGMENT);
        JWebDataWindowLite dwRoot = initialize(ROOT);
        JWebDataWindowLite dwLemma = initialize(LEMMA);

        int wordkey = 0, rootkey = 0, lemmakey = 0;
        ArabicText arabicText;
        ArrayList<String> dictionary = JWebUtils.readTextFromFileAsLines("quran-dict.txt");
        ArrayList<String> morphology = JWebUtils.readTextFromFileAsLines("quranic-corpus-morphology.txt");
        ArrayList<String> rootDictionary = JWebUtils.readTextFromFileAsLines("RootMeaning.txt");

        //System.out.println(morphology.size());
        String tab = "\t";
        int r = 0;
        int rWord = 0;
        for (int row = 0; row < Math.max(800, morphology.size()); row++) {
            if (row % 1000 == 0 && row > 0) {
                System.out.println("Processing:" + row);
                insertDictionary(dwWord);
                insertDictionary(dwWord, dictionary);
                insertRootDictionary(dwRoot, rootDictionary);
                updateAndClear(dwSegment, SEGMENT);
                updateAndClear(dwWord, WORD);
                updateAndClear(dwRoot, ROOT);
                updateAndClear(dwLemma, LEMMA);
            }
            String line = morphology.get(row);
            //System.out.println(line);
            String[] location = JWebUtils.getTokenList(line, "(", ")", ":");
            String items = line.substring(line.indexOf(tab)).trim();
            String transliteration = JWebUtils.getToken(items, tab);
            arabicText = ArabicText.fromBuckwalter(transliteration);
            String form = arabicText.toUnicode();
            String simpleForm = arabicText.removeDiacritics().toUnicode();

            items = items.substring(form.length()).trim();
            String tag = JWebUtils.getToken(items, tab);
            items = items.substring(tag.length()).trim();
            String feature = JWebUtils.getToken(items, tab);
            String[] features = JWebUtils.getTagList(feature, "|", "|");
            String lemma = null, root = null;
            for (int f = 0; f < features.length; f++) {
                if (features[f].length() >= 4 && JWebUtils.isSame(features[f].substring(0, 4), "LEM:")) {
                    lemma = features[f].substring(4);
                } else if (features[f].length() >= 5 && JWebUtils.isSame(features[f].substring(0, 5), "ROOT:")) {
                    root = features[f].substring(5);
                }
            }

            int rootid = -1;
            if (JWebUtils.isNotNull(root)) {
                rootid = getId(root, dwRoot);
                if (rootid < 0) {
                    rootkey++;
                    rootid = rootkey;
                    int rRoot = dwRoot.addRow();
                    dwRoot.setItem(rRoot, ID, rootid);
                    dwRoot.setItem(rRoot, ROOT, (ArabicText.fromBuckwalter(root)).toUnicode());
                    dwRoot.setItem(rRoot, LETTER_1, ArabicText.fromBuckwalter(root).removeDiacritics().getCharacter(0).toUnicode());
                    dwRoot.setItem(rRoot, LETTER_2, ArabicText.fromBuckwalter(root).removeDiacritics().getCharacter(1).toUnicode());
                    dwRoot.setItem(rRoot, LETTER_3, ArabicText.fromBuckwalter(root).removeDiacritics().getCharacter(2).toUnicode());
                    dwRoot.setItem(rRoot, TRANSLITERATION, root);
                    //dwRoot.setItem(rRoot, TRANSLATION, root);

                }
            }
            int lemmaid = -1;
            if (JWebUtils.isNotNull(lemma)) {
                lemmaid = getId(lemma, dwLemma);
                if (lemmaid < 0) {
                    lemmakey++;
                    lemmaid = lemmakey;
                    int rLemma = dwLemma.addRow();
                    dwLemma.setItem(rLemma, ID, lemmaid);
                    dwLemma.setItem(rLemma, LEMMA, (ArabicText.fromBuckwalter(lemma)).toUnicode());
                    dwLemma.setItem(rLemma, TRANSLITERATION, lemma);
                }
            }

//            dwWord.setItem(rWord, "Related", (ArabicText.fromBuckwalter(lem)).toUnicode());
//            dwWord.setItem(r, "RelatedTransliteration", lem);
            int wordid = getId(transliteration, dwWord);
            if (wordid < 0) {
                wordkey++;
                wordid = wordkey;
                rWord = dwWord.addRow();
                dwWord.setItem(rWord, ID, wordid);
                dwWord.setItem(rWord, WORD, form);
                dwWord.setItem(rWord, WORD_SIMPLE, simpleForm);
                dwWord.setItem(rWord, TRANSLITERATION, transliteration);
                dwWord.setItem(rWord, TAG, tag);
                dwWord.setItem(rWord, LEMMA_ID, lemmaid);
                dwWord.setItem(rWord, ROOT_ID, rootid);

            }
            r = dwSegment.addRow();
            dwSegment.setItem(r, ID, row + 1);
            dwSegment.setItem(r, CHAPTER_NO, location[0]);
            dwSegment.setItem(r, VERSE_NO, location[1]);
            dwSegment.setItem(r, TOKEN_NO, location[2]);
            dwSegment.setItem(r, SEGMENT_NO, location[3]);
            dwSegment.setItem(r, FEATURES, feature);
            dwSegment.setItem(r, WORD_ID, wordid);

            //System.out.println("");
        }
        insertDictionary(dwWord);
        insertDictionary(dwWord, dictionary);
        insertRootDictionary(dwRoot, rootDictionary);
        updateAndClear(dwSegment, SEGMENT);
        updateAndClear(dwWord, WORD);
        updateAndClear(dwRoot, ROOT);
        updateAndClear(dwLemma, LEMMA);
        if (!update_mode) {
            dwWord.printAsString(WORD);
            dwSegment.printAsString(SEGMENT);
            dwRoot.printAsString(ROOT);
        }

    }

    private void updateAndClear(JWebDataWindowLite dw, String table) throws Exception {
        updateAndClear(dw, table, JWebDataWindowLite.STATUS_NEWMODIFIED);
    }

    private void updateAndClear(JWebDataWindowLite dw, String table, int status) throws Exception {
        if (!update_mode) {
            return;
        }
        dw.setKey(ID, true);
        dw.prepareForUpdate(table, status);
        dw.update();
        dw.commit();
        dw.resetRows();
        dw.resetUpdate();
    }
    
    
    public void test() {
        CharacterType[] allCharacters = CharacterType.values();
        System.out.println("Characters ");
        for (int i = 0; i < allCharacters.length; i++) {
            ArabicTextBuilder builder = new ArabicTextBuilder();
            builder.add(allCharacters[i]);
            System.out.println(allCharacters[i].name() + "=" + builder.toText() + "=");

        }
        DiacriticType[] allDiacritics = DiacriticType.values();
        String diacritics = "";
        for (int i = 0; i < allDiacritics.length; i++) {
            ArabicTextBuilder builder = new ArabicTextBuilder();
            builder.add(allCharacters[0], allDiacritics[i]);
            diacritics = diacritics + "," + allDiacritics[i].name();
            System.out.println(allDiacritics[i].name() + "=" + builder.toText() + "=");

        }
        System.out.println(diacritics);
        //        سَوَّىٰ

        if (1 == 1) {
            return;
        }

        ArabicTextBuilder builder = new ArabicTextBuilder();

        // Noon + Sukun
        builder.add(CharacterType.Noon, DiacriticType.Sukun);
        builder.add(DiacriticType.AlifKhanjareeya);
        // Alif + Fatha + HamzaAbove
        builder.add(CharacterType.Alif, DiacriticType.Fatha, DiacriticType.HamzaAbove);
        // Ain + Fatha
        builder.add(CharacterType.Ain, DiacriticType.Fatha);

        // Meem + Sukun
        builder.add(CharacterType.Meem, DiacriticType.Sukun);
        System.out.println(builder.toText());

        CharacterType[] values = {CharacterType.AlifMaksura, CharacterType.TaMarbuta, CharacterType.Tatweel, CharacterType.SmallHighSeen};//
//                SmallHighRoundedZero, SmallHighUprightRectangularZero,
//                SmallHighMeemIsolatedForm, SmallLowSeen, SmallWaw, SmallYa,
//                SmallHighNoon, EmptyCentreLowStop, EmptyCentreHighStop,
//                RoundedHighStopWithFilledCentre, SmallLowMeem};

        TokenSearch search = new TokenSearch(EncodingType.Unicode);
        search.findSubstring("");
        search.findToken("");
        // Display the results.
        AnalysisTable table = search.getResults();
        System.out.println(table);
        System.out.println("Matches: " + table.getRowCount() + "\r\n");

        Verse _verse = Document.getVerse(2, 2);
        System.out.println("---------- Verse No:" + _verse.getVerseNumber());
        for (Token tokenVerse : _verse.getTokens()) {
            System.out.println(tokenVerse.removeDiacritics().toString());
            java.util.Iterator<ArabicCharacter> charItr = tokenVerse.iterator();
            while (charItr.hasNext()) {
                ArabicCharacter aChar = charItr.next();
                //aChar.
                //System.out.print("  ["+ aChar.toUnicode()+"]  ");
            }
            //tokenVerse.
        }
        System.out.println("...");
        if (1 == 1) {
            return;
        }
        int v = 0;
        String dictVerse = "ø 9ø # (ø ø eø     #ø føqø";
        for (int i = 0; i < dictVerse.length(); i++) {
            if (dictVerse.charAt(i) != ' ') {
                System.out.print(v + "=(" + dictVerse.charAt(i) + ")");
                v++;
            }
        }

        System.out.println(" ");

        Token[] tokens = new Token[6];
        tokens[0] = Document.getToken(88, 17, 1);
        tokens[1] = Document.getToken(88, 17, 2);
        tokens[2] = Document.getToken(88, 17, 3);
        tokens[3] = Document.getToken(88, 17, 4);
        tokens[4] = Document.getToken(88, 17, 5);
        tokens[5] = Document.getToken(88, 17, 6);
        int k = 0;
        for (int i = 0; i < tokens.length; i++) {
            for (int j = 0; j < tokens[i].getLetterCount(); j++) {
                String ac = tokens[i].getCharacter(j).toUnicode();
                for (int m = 0; m < ac.length(); m++) {
                    if (ac.charAt(m) != ' ') {
                        System.out.print((k) + "=(" + ac.charAt(m) + ")");
                        k++;
                    }
                }

            }
        }

        System.out.println(" ");
//        TokenSearch search = new TokenSearch(EncodingType.Unicode);
//        search.findSubstring("آخ�?رَةٌ");
//        search.findToken("آخ�?رَةٌ");
//        // Display the results.
//        AnalysisTable table = search.getResults();
//        System.out.println(table);
//        System.out.println("Matches: " + table.getRowCount() + "\r\n");

        Token token = Document.getToken(1, 1, 1);

        Verse verse = Document.getVerse(1, 1);

        // Display the token using simple encoding.
        System.out.println(" Simple encoding: " + token.toSimpleEncoding() + nl
                + " Token Itself: " + token.toString() + nl
                + " Verse: " + token.getVerse().toUnicode() + nl
                + " Token: " + token.toString(EncodingType.Buckwalter) + nl
                + " Char: " + token.getCharacter(0) + nl
                + " Char Unicode: " + token.getCharacter(0).toUnicode() + nl
                + " Verse: " + verse.toUnicode() + nl
                + " Token: " + token.toString(EncodingType.Buckwalter) + nl
        );

        String text1 = "fa";
        String text2 = "jaEalo";
        String text3 = "na";
        String text4 = "humo";
        ArabicText arabicText1 = ArabicText.fromBuckwalter(text1);
        ArabicText arabicText2 = ArabicText.fromBuckwalter(text2);
        ArabicText arabicText3 = ArabicText.fromBuckwalter(text3);
        ArabicText arabicText4 = ArabicText.fromBuckwalter(text4);

        //ArabicText arabicText = ArabicText.fromUnicode(ar);
        String testDiacritics = arabicText2.removeDiacritics().toUnicode();
        System.out.println("Unicode combined:[" + arabicText1.toUnicode() + arabicText2.toUnicode() + arabicText3.toUnicode() + arabicText4.toUnicode() + "]");
        System.out.println("Without diacritics:[" + arabicText1.removeDiacritics() + arabicText2.removeDiacritics() + arabicText3.removeDiacritics() + arabicText4.removeDiacritics() + "]");
        System.out.println("No letter:[" + arabicText1.removeNonLetters() + arabicText2.removeNonLetters() + arabicText3.removeNonLetters() + arabicText4.removeNonLetters() + "]");
        System.out.println("Test Diacritics:[" + testDiacritics + "]");

        System.out.println("[***************");
        for (int c = 0; c < arabicText2.getLetterCount(); c++) {
            System.out.println("[" + arabicText2.removeDiacritics().getCharacter(c).toUnicode() + "]");
        }
        System.out.println("[***************");

        for (Chapter chapter : Document.getChapters()) {
            System.out.println("Chapter No:" + chapter.getChapterNumber());
            java.util.Iterator<Verse> verseItr = chapter.iterator();
            while (verseItr.hasNext()) {
                verse = verseItr.next();
                System.out.println("---------- Verse No:" + verse.getVerseNumber());
                for (Token tokenVerse : verse.getTokens()) {
                    System.out.println(tokenVerse.toString());
                    java.util.Iterator<ArabicCharacter> charItr = tokenVerse.iterator();
                    while (charItr.hasNext()) {
                        ArabicCharacter aChar = charItr.next();
                        //aChar.
                        //System.out.print("  ["+ aChar.toUnicode()+"]  ");
                    }
                    //tokenVerse.
                }
                System.out.println("...");
            }
            break;
        }
    }

    private void getWords() throws Exception {
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite://Users/wadood/words.db");
        JWebDataWindowLite dw = new JWebDataWindowLite(conn);
        dw.setQuery("Select * from word");
        dw.retrieve();
        dw.printAsString();
    }

    private void convertDataForCloud(JWebDataWindowLite dw, String tableName) {
        List<ReplaceableItem> cldData = new ArrayList<ReplaceableItem>();
        for (int r = 0; r < dw.getRowCount(); r++) {
            if (r > 0 && r % 25 == 0) {
                dbCloud.batchPutAttributes(new BatchPutAttributesRequest(tableName, cldData));
                cldData.clear();
            }
            ReplaceableItem item = new ReplaceableItem(JWebUtils.getPaddedValue(dw.getItem(r, ID), 6, "0"));
            List<ReplaceableAttribute> row = new ArrayList<ReplaceableAttribute>();
            for (int c = 0; c < dw.getColumnCount(); c++) {
                row.add(new ReplaceableAttribute(dw.getColumnName(c), dw.getItem(r, c), true));

            }
            item.setAttributes(row);
            cldData.add(item);
        }

        dbCloud.batchPutAttributes(new BatchPutAttributesRequest(tableName, cldData));
    }

    private List<ReplaceableItem> createSampleData() {
        List<ReplaceableItem> sampleData = new ArrayList<ReplaceableItem>();

        sampleData.add(new ReplaceableItem("Item_01").withAttributes(
                new ReplaceableAttribute("Category", "Clothes", true),
                new ReplaceableAttribute("Subcategory", "Sweater", true),
                new ReplaceableAttribute("Name", "Cathair Sweater", true),
                new ReplaceableAttribute("Color", "Siamese", true),
                new ReplaceableAttribute("Size", "Small", true),
                new ReplaceableAttribute("Size", "Medium", true),
                new ReplaceableAttribute("Size", "Large", true)));

        sampleData.add(new ReplaceableItem("Item_02").withAttributes(
                new ReplaceableAttribute("Category", "Clothes", true),
                new ReplaceableAttribute("Subcategory", "Pants", true),
                new ReplaceableAttribute("Name", "Designer Jeans", true),
                new ReplaceableAttribute("Color", "Paisley Acid Wash", true),
                new ReplaceableAttribute("Size", "30x32", true),
                new ReplaceableAttribute("Size", "32x32", true),
                new ReplaceableAttribute("Size", "32x34", true)));

        sampleData.add(new ReplaceableItem("Item_03").withAttributes(
                new ReplaceableAttribute("Category", "Clothes", true),
                new ReplaceableAttribute("Subcategory", "Pants", true),
                new ReplaceableAttribute("Name", "Sweatpants", true),
                new ReplaceableAttribute("Color", "Blue", true),
                new ReplaceableAttribute("Color", "Yellow", true),
                new ReplaceableAttribute("Color", "Pink", true),
                new ReplaceableAttribute("Size", "Large", true),
                new ReplaceableAttribute("Year", "2006", true),
                new ReplaceableAttribute("Year", "2007", true)));

        sampleData.add(new ReplaceableItem("Item_04").withAttributes(
                new ReplaceableAttribute("Category", "Car Parts", true),
                new ReplaceableAttribute("Subcategory", "Engine", true),
                new ReplaceableAttribute("Name", "Turbos", true),
                new ReplaceableAttribute("Make", "Audi", true),
                new ReplaceableAttribute("Model", "S4", true),
                new ReplaceableAttribute("Year", "2000", true),
                new ReplaceableAttribute("Year", "2001", true),
                new ReplaceableAttribute("Year", "2002", true)));

        sampleData.add(new ReplaceableItem("Item_05").withAttributes(
                new ReplaceableAttribute("Category", "Car Parts", true),
                new ReplaceableAttribute("Subcategory", "Emissions", true),
                new ReplaceableAttribute("Name", "O2 Sensor", true),
                new ReplaceableAttribute("Make", "Audi", true),
                new ReplaceableAttribute("Model", "S4", true),
                new ReplaceableAttribute("Year", "2000", true),
                new ReplaceableAttribute("Year", "2001", true),
                new ReplaceableAttribute("Year", "2002", true)));

        return sampleData;
    }

    private void testCloud() throws Exception {
        //dbCloud = new AmazonSimpleDBClient(new PropertiesCredentials(ApplicationQuranTree.class.getResourceAsStream("AwsCredentials.properties")));

        AmazonSimpleDB sdb = new AmazonSimpleDBClient(new PropertiesCredentials(ApplicationQuranTree.class.getResourceAsStream("AwsCredentials.properties")));

        try {
            // Create a domain
            String myDomain = "MyStore";
            System.out.println("Creating domain called " + myDomain + ".\n");
            //sdb.createDomain(new CreateDomainRequest(myDomain));

            // List domains
            System.out.println("Listing all domains in your account:\n");
            for (String domainName : sdb.listDomains().getDomainNames()) {
                System.out.println("  " + domainName);
            }
            System.out.println();
            if (1 == 1) {
                return;
            }
            // Put data into a domain
            System.out.println("Putting data into " + myDomain + " domain.\n");
            sdb.batchPutAttributes(new BatchPutAttributesRequest(myDomain, createSampleData()));

            // Select data from a domain
            // Notice the use of backticks around the domain name in our select expression.
            String selectExpression = "select * from `" + myDomain + "` where Category = 'Books'";
            //System.out.println("Selecting: " + selectExpression + "\n");
            SelectRequest selectRequest = new SelectRequest(selectExpression);
            for (Item item : sdb.select(selectRequest).getItems()) {
                System.out.println("  Item");
                System.out.println("    Name: " + item.getName());
                for (Attribute attribute : item.getAttributes()) {
                    System.out.println("      Attribute");
                    System.out.println("        Name:  " + attribute.getName());
                    System.out.println("        Value: " + attribute.getValue());
                }
            }
            System.out.println();

            // Delete values from an attribute
            System.out.println("Deleting Blue attributes in Item_O3.\n");
            Attribute deleteValueAttribute = new Attribute("Color", "Blue");
            sdb.deleteAttributes(new DeleteAttributesRequest(myDomain, "Item_03")
                    .withAttributes(deleteValueAttribute));

            // Delete an attribute and all of its values
            System.out.println("Deleting attribute Year in Item_O3.\n");
            sdb.deleteAttributes(new DeleteAttributesRequest(myDomain, "Item_03")
                    .withAttributes(new Attribute().withName("Year")));

            // Replace an attribute
            System.out.println("Replacing Size of Item_03 with Medium.\n");
            List<ReplaceableAttribute> replaceableAttributes = new ArrayList<ReplaceableAttribute>();
            replaceableAttributes.add(new ReplaceableAttribute("Size", "Medium", true));
            sdb.putAttributes(new PutAttributesRequest(myDomain, "Item_03", replaceableAttributes));

            // Delete an item and all of its attributes
            System.out.println("Deleting Item_03.\n");
            sdb.deleteAttributes(new DeleteAttributesRequest(myDomain, "Item_03"));

            // Delete a domain
            System.out.println("Deleting " + myDomain + " domain.\n");
            sdb.deleteDomain(new DeleteDomainRequest(myDomain));
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it "
                    + "to Amazon SimpleDB, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with SimpleDB, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }

}
