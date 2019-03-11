//
//  DictionaryDataController.m
//  Word of Day
//
//  Created by Wadood Chaudhary on 3/18/12.
//  Copyright (c) 2012 Ahmadiyya Muslim Community. All rights reserved.
//

#import "DictionaryDataController.h"
#import "Dictionary.h"
#import "AppDelegate.h"
#import <CoreData/CoreData.h>
#import "Example.h"
#import "Word.h"
#import "Definition.h"
#import "Example.h"
#import "AmazonCloudDatabase.h"
#import "Constants.h"
#import "Language.h"
#import "Verse.h"
#import "Utils.h"
#import "Reachability.h"



@class Example;
@class Definition;
@class Word;

enum UPLOAD_TARGETS {NONE = 0, CLOUD_ONLY=1, CLOUD_AND_LOCAL = 2, LOCAL_ONLY=3};

@interface DictionaryDataController () {
    @private
    NSString* deviceName;
}
- (NSMutableArray*) getRelatedWords:(NSString*) word root:(NSString*) rootId;
- (int) getVersesCount:(NSString*) word;
- (NSString*) getRelatedWordsAsText:(NSString*) word root:(NSString*) rootId;
- (void) getVerseTuple:(NSMutableDictionary*)verses Chapter:(int)chapter Verse:(int)verse Text:(NSString*) text Translation:(NSString*)translation;
- (NSString *) getVerse:(int)chapterToQry VerseToQry:(int) verseToQry;
- (NSMutableArray*) getRoot :(NSString*) word;
- (NSString*) getLemma :(NSString*) word;
- (NSDictionary*) retrieveData:(NSString*) querySQL;
- (void) initializeWordList;
- (void) loadDataFromFile;
- (void) createTable:(NSString*)sql;
- (void) executeSQL:(NSString*)sql;
- (void) openDatabase;
- (void) executeSQLFromFile:(NSString *)fileName FileType:(NSString*)fileType KeywordDelimiter:(NSString*)keywordDelimiter;
- (NSArray*) fetchData:(NSString *) entityDescription SortKey:(NSString*) sortKey ManagedObjectContext:(NSManagedObjectContext*) thisManagedObjectContext Predicate:(NSPredicate*) predicate;
- (NSArray*) fetchData:(NSString *) entityDescription SortKey:(NSString*) sortKey;
- (void) initializeEntitiesFromDatabase;
- (NSMutableArray*) retrieve:(NSString*) querySQL;
- (NSMutableArray*) retrieveDataWithoutKey:(NSString*) querySQL;
- (void) addExamples:(Definition*)entityDefinition DefinitionID:(NSString*)definitionid;
- (void) addDefinitions:(Word*)entityWord WordID:(NSString*) wordid;
- (void) addLanguage:(Definition*)entityDefinition LanguageID:(NSString*)languageid;
- (void) addWords;
- (void) addVersesToWord:(Word*) entityWord;
- (void) printWords;
- (void) printDefinitions:(Word*) entityWord;
- (void) printExamples:(Definition*) entityDefinition;
- (void) uploadToCloud:(NSString*) tableName;
- (NSArray*) getColumnList:(NSString*) tableName;
- (NSArray*) getColumnListFromResult:(sqlite3_stmt*) statement;
- (void) refreshWordOfDay;
- (Word*) addWord:(NSMutableArray*)wordInfo Favorites:(NSDictionary*)favorites;
- (int) getNewId:(NSString*) tableName;
- (NSString*) getColValue:(NSString*) sql;
- (NSDictionary*) refreshTableFromCloud:(NSString*)tableName QrySQL_cloud:(NSString*)qrySQL_cloud DeleteSQL_local:(NSString*)deleteSQL_local;
- (NSDictionary*) refreshTableFromCloud:(NSString*) tableName;
- (void) importFromCloud;
- (void) exportToCloud;

@end


@implementation DictionaryDataController
@synthesize managedObjectContext;
@synthesize dictionaryArray,recentsArray,favoriteArray,dailyArray,word_exampleArray,examplesArray,wordOfDay;


//NSMutableArray *wordList;
NSDateFormatter* dateFormatter;
AmazonCloudDatabase* dbCloud;
NSMutableArray* controllers;
NSMutableArray* wodTables;// = [NSMutableArray arrayWithObjects:EXAMPLE,EMAIL_PLAN,MEANING,nil];
NSMutableArray* dictTables;
NSError *error;

- (id)init {
    if (self = [super init]) {
        //[self cleanSplitWord:@"(with bracket)"];
        //[self getDeviceName];
        //exit(0);
               Reachability *reach = [Reachability reachabilityWithHostName:@"apple.com"];
                if([reach currentReachabilityStatus] == NotReachable) {
                    [Utils showMessage:@"Network" Message:@"No network is available. Word of the day will not be updated."];
                }
                else {
                    //NSLog(@"Online");
                }
        wodTables = [NSMutableArray arrayWithObjects:EMAIL_PLAN,MEANING,EXAMPLE,nil];
        dictTables = [NSMutableArray arrayWithObjects:WORD,SEGMENT,nil];
        [self openDatabase];
        [self managedObjectContext];
        //[self insertBismillahInSegment];
        //exit(0);
        
        //[Utils displayAllFonts];
       dbCloud = [[AmazonCloudDatabase alloc] init];
       if (dbCloud.online) [self getNewItemsFromCloud];
       //         }
       //         else {
       //             NSLog(@" Cloud not available.");
       //         }
//        //[self tester];
        //[dbCloud retrieve:@"Select * from language"];
        //[self importFromCloud];
        //[self uploadToCloud:[EXAMPLE lowercaseString]];
        //[self refreshTableFromCloud:[EXAMPLE lowercaseString]];
        //[self addNewWord:@"dailywod":@"plist":LOCAL_ONLY];
        //NSString* tableName = @"example";
        //[self uploadToCloud:tableName];
        //
        //    [self removeWord:@"قاعدة بيانات"];
        //      //[self refreshTableFromCloud:@"meaning":@"Select * from meaning where WordId='8'":@"Delete from meaning where WordId=8"];               //    [self refreshWordFromCloud];
        //[self uploadToCloudInBatch:@"root"];
        //[self addWOD:@"dailywod":@"plist":LOCAL_ONLY];
        //[self refreshWordOfDay];
        //
        //NSString* sql = @"ALTER TABLE Verse ADD size VARCHAR(255)";
        //NSLog(@"Favorite SQL:%@",sql);
        //[self executeSQL:sql];
        //NSString* sql = @"UPDATE Verse Set size2='0'";
        //NSLog(@"Favorite SQL:%@",sql);
        //[self executeSQL:sql];
        
        //[self test];
        
        //
        //[self refreshDefaultWordOfDay];
        [self refreshWordOfDay];
        [self downloadUserDataFromCloud];
        
        
        //[Utils downloadFont:@"Al Bayan"];
        //[Utils downloadFont:@"Baghdad"];
        
        
        
        //[self initializeEntitiesFromDatabase];
        //[self uploadUserDataToCloud];
        //exit(0);
        
        //[self updateSplitWordInVerse];
        //
        //[self resetApplicationModel];
        //[self initializeWordList];
        //[self loadDataFromFile];
        //[self initializeDataFromDatabase];
        dateFormatter = [[NSDateFormatter alloc] init];
        [dateFormatter setDateFormat:@"MM/dd/yy"];
    }
    return self;
}

-(void) test {
    if (false) {
        NSString* substr =  [Utils substr:@"Allah is Great" from:6 length:2];
        NSLog(@"Substr :%@",substr);
        NSString* settings = @"032005681011001308130400002031";
        //0320 = width (0,4)
        //0568 = height (4,4)
        //1    = view (8,1)
        //1  - showArabic (9,1)
        //1  - showTranslation (10,1)
        //1  - showTransliteration (11,1)
        //0  - showSplitWord (12,1)
        //0  - orientation (13,1)
        //16 - textFontPts (14,2)
        //08 - transFontPts (16,2)
        //18 - fontToSizePts (18,2)
        //02 - arabicFontIndex (20,2)
        //06 - englishIndex (22,2)
        //002 - chapter (24,3)
        //021 - verse (27,3)
        NSValue* rect =[NSValue valueWithCGRect:CGRectMake(0,0,[[Utils substr:settings from:0 length:4] intValue],[[Utils substr:settings from:4 length:4] intValue])];
        NSValue* viewSideBySide = [NSNumber numberWithBool:([[Utils substr:settings from:8 length:1] intValue]==0)];
        NSValue* showArabic =[NSNumber numberWithBool:([[Utils substr:settings from:9 length:1] intValue]==0)];
        NSValue* showTranslation =[NSNumber numberWithBool:([[Utils substr:settings from:10 length:1] intValue]==0)];
        NSValue* showTransliteration =[NSNumber numberWithBool:([[Utils substr:settings from:11 length:1] intValue]==0)];
        NSValue* showSplitWord =[NSNumber numberWithBool:([[Utils substr:settings from:12 length:1] intValue]==0)];
        NSValue* orientation =[NSNumber numberWithBool:([[Utils substr:settings from:13 length:1] intValue]==0)];
        NSValue* textFontPts =[NSNumber numberWithInt:[[Utils substr:settings from:14 length:2] intValue]];
        NSValue* transFontPts =[NSNumber numberWithInt:[[Utils substr:settings from:16 length:2] intValue]];
        NSValue* fontToSizePts =[NSNumber numberWithInt:[[Utils substr:settings from:18 length:2] intValue]];
        NSValue* textFontIndex =[NSNumber numberWithInt:[[Utils substr:settings from:20 length:2] intValue]];
        NSValue* translationFontIndex =[NSNumber numberWithInt:[[Utils substr:settings from:22 length:2] intValue]];
        NSValue* chapterNo =[NSNumber numberWithInt:[[Utils substr:settings from:24 length:3] intValue]];
        NSValue* verseNo =[NSNumber numberWithInt:[[Utils substr:settings from:27 length:3] intValue]];
        
        
        
        
        //NSLog(@"CGRect :%f,%f",rect.size.width,rect.size.height);
        
        exit(0);
    }
    
    /*
     NSString* screenSizeID = [NSString stringWithFormat:@"%04d%04d",(int)rect.size.width,(int)rect.size.height];
     NSString* verseViewID = @(verseView==ViewSideBySide?"1":"0");
     NSString* contentViewID = [NSString stringWithFormat:@"%@%@%@%@",@(showArabic?"1":"0"),@(showTranslation?"1":"0"),@(showTransliteration?"1":"0"),@(showTranslationSplitWord?"1":"0")];
     NSString* deviceViewID = @(UIDeviceOrientationIsLandscape([UIDevice currentDevice].orientation)?"0":"1");
     
     */
}

-(NSMutableArray*) getFavorites {
    NSString* sql = [NSString stringWithFormat:@"Select word,w.id,f.id,0 as chapter_no,0 as verse_no,type from Word w, Favorite f where w.id=f.word_id and type='W'             union Select verse,v.id,f.id,chapter_no,verse_no,type from Verse v,Favorite f where v.id=f.verse_id and type='V' order by f.id desc"];
    NSLog(@" getFavorites SQL:%@",sql);
    NSMutableArray* favorites = [self retrieve:sql];
    if ([favorites count]>0) return favorites;
    else return nil;
}


- (void) downloadUserDataFromCloud {
    //|12069,12069|22,22|209,209	|0,03200568111100160818052021,14|1,1100010692,28
    NSString* user = [self getUser];
    if ([user length]==0) return;
    NSString* favColName = [FAVORITE lowercaseString];
    NSString* logColName = [LOG lowercaseString];
    
    NSString* sql = [NSString stringWithFormat:@"Select %@,%@ from User where itemName() = '%@' ",favColName,logColName,user];
    //NSLog(@"USER NoSQL:%@",sql);
    NSMutableArray* userFavs = [dbCloud retrieveInOrder:sql];
    if ([userFavs count]>0) {
        NSMutableDictionary* row=[userFavs objectAtIndex:0];
        [self refreshFavoriteTable:[[row objectForKey:favColName]componentsSeparatedByString:@"|"]];
        [self refreshLogTable:[[row objectForKey:logColName]componentsSeparatedByString:@"|"] ];
    }
}

-(void) refreshLogTable:(NSArray*) logData {
    NSString* deleteSQL_local = [NSString stringWithFormat:@"Delete from %@",LOG];
    [self executeSQL:deleteSQL_local];
    NSString* time_stamp=[Utils getTodaysDateInFormat:@"YYYY-MM-DD HH:MM:SS.SSS"];
    for (NSString* logRec in logData) {
        NSArray* log = [logRec componentsSeparatedByString:@","];
        NSString* screen_id = log[0];
        NSString* settings = log[1];
        NSString* hits = log[2];
        NSString* word_id=@"-1";
        NSString* chapter_no=@"-1";
        NSString* verse_no=@"-1";
        int index = (int)[settings length]-6 ;
        if ([screen_id isEqualToString:@"0"]) {
            chapter_no = [settings substringWithRange:NSMakeRange(index, 3)];
            verse_no =  [settings substringWithRange:NSMakeRange(index+3, 3)];
        }
        else if ([screen_id isEqualToString:@"1"]) {
            chapter_no = [settings substringWithRange:NSMakeRange(index, 6)];
        }
        NSString* id = [NSString stringWithFormat:@"%d",[self getNewId:LOG]];
        
        NSString* insertSQL_local = [NSString stringWithFormat:@"Insert into Log (id, screen_id, chapter_no, verse_no, word_id, time_stamp, settings, hits) values (%@,%@,%@,%@,%@,'%@','%@',%@)",id,screen_id,chapter_no,verse_no,word_id,time_stamp,settings,hits];
        [self executeSQL:insertSQL_local];
        
        //NSLog(@"LOG =(SQL=%@) word_id=%@,chapter_no=%@,verse_no=%@",insertSQL_local,word_id,chapter_no,verse_no);
        
    }
}


-(void) refreshFavoriteTable:(NSArray*) favData {
    NSString* deleteSQL_local = [NSString stringWithFormat:@"Delete from %@",FAVORITE];
    [self executeSQL:deleteSQL_local];
    
    for (NSString* favRec in favData) {
        //NSLog(@"FavRec:%@",favRec);
        NSArray* fav = [favRec componentsSeparatedByString:@","];
        //NSLog(@"FavRec:%@ (%zd)",favRec,[fav count]);
        if ([fav count] > 1) {
            NSString* word_id  = fav[0];
            NSString* verse_id = fav[1];
            NSString* type = fav[2];
            NSString* id = [NSString stringWithFormat:@"%d",[self getNewId:FAVORITE]];
            NSString* userid = [self getUser];
            NSString* insertSQL_local = [NSString stringWithFormat:@"Insert into Favorite (id, word_id, verse_id,type) values (%@,%@,%@,'%@')",id,word_id,verse_id,type];
            [self executeSQL:insertSQL_local];
            //NSLog(@"FAVORITE (%@)= word_id=%@,verse_id=%@",insertSQL_local,word_id,verse_id);
        }
    }
}


-(void) uploadUserDataToCloud{
    NSString* sql = @"Select word_id,verse_id,type from Favorite order by id";
    NSMutableArray* data = [self retrieve:sql];
    NSString * ids=@"";
    for (NSMutableArray* row in data) {
        NSString* item = [NSString stringWithFormat:@"%@,%@,%@",[row objectAtIndex:0],[row objectAtIndex:1],[row objectAtIndex:2]];
        ids = [ids length]==0?item:[NSString stringWithFormat:@"%@|%@",ids,item];
    }
    sql = @"Select screen_id,settings,hits from Log";
    data = [self retrieve:sql];
    NSString * hits=@"";
    for (NSMutableArray* row in data) {
        NSString* item = [NSString stringWithFormat:@"%@,%@,%@",[row objectAtIndex:0],[row objectAtIndex:1],[row objectAtIndex:2]];
        hits = [hits length]==0?item:[NSString stringWithFormat:@"%@|%@",hits,item];
    }
    data = [self getSubscription];
    NSString * subscription=@"";
    if (data!=nil) {
        NSMutableArray* row = data;
        subscription= [NSString stringWithFormat:@"%@|%@|",[row objectAtIndex:0],[row objectAtIndex:1]];
    }
    //NSLog(@"ids=%@ hits=%@ subscription=%@",ids,hits,subscription);
    //ids = @"69,69|220,220|2090,2090";
    NSString* user = [self getUser];
    NSMutableArray* row = [NSMutableArray arrayWithObjects:ids,hits,subscription,nil];
    NSMutableArray* colList = [NSMutableArray arrayWithObjects:[FAVORITE lowercaseString],[LOG lowercaseString],@"subscription",nil];
    
    [dbCloud addRow:USER Row:row Key:user ColList:colList];
    
    //[dbCloud removeRow:FAVORITE RowID:@"000001"];
}

- (void) updateSplitWordInVerse  {
    //NSNumber* chapterno = [NSNumber numberWithInt:[(NSString*)[row objectAtIndex:0] intValue]];
    NSString* sql = @"Select id,verse_count from Chapter order by id";
    NSMutableArray* chaptersData = [self retrieve:sql];
    int maxLength = NSIntegerMin;
    //NSLog(@" Chapters:%i",(int)[chaptersData count]);
    for (NSMutableArray* chapter in chaptersData) {
        int chapterNo=[(NSString*)[chapter objectAtIndex:0] intValue];
        sql = [NSString stringWithFormat:@"Select chapter_no,verse_no,verse,translation,transliteration,Size from Verse where chapter_no=%tu order by chapter_no,verse_no",chapterNo];
        //sql = [NSString stringWithFormat:@"Select chapter_no,verse_no,verse,translation,transliteration,Size from Verse where chapter_no=2 and verse_no=69 and chapter_no=%tu order by chapter_no,verse_no",chapterNo];
        
        NSLog(@"Chapter:%i",chapterNo);
        NSMutableArray* versesData = [self retrieve:sql];
        for (NSMutableArray* row in versesData) {
            int verseNo   = [(NSString*)[row objectAtIndex:1] intValue];
            NSString* splitWordTranslation = [self getSplitWordTranslationOfVerseNo:verseNo atChapterNo:chapterNo];
            int verseLength = (int)[splitWordTranslation length];
            maxLength = MAX(verseLength,maxLength);
            sql = [NSString stringWithFormat:@"Update Verse set splitword='%@' where chapter_no=%i and verse_no=%i",splitWordTranslation,chapterNo,verseNo];
            // NSLog(@"SQL=%@",sql);
            [self executeSQL:sql];
            // NSLog(@"        VerseNo %i (%i) ",verseNo,verseLength);
        }
    }
    //NSLog(@"Max Length:%i",maxLength);
}
- (void) insertBismillahInVerse  {
    //NSNumber* chapterno = [NSNumber numberWithInt:[(NSString*)[row objectAtIndex:0] intValue]];
    NSString* sql = @"Select id,verse_count from Chapter where id>1 and id <> 9 order by id";
    NSMutableArray* chaptersData = [self retrieve:sql];
    sql = @"Select * from Verse where verse_no=1";
    NSMutableArray* bismilla = [self retrieve:sql];
    if ([bismilla count]>2) return;
    //NSLog(@" Chapters:%i",(int)[chaptersData count]);
    sql = [NSString stringWithFormat:@"Select * from Verse where chapter_no=1 and verse_no=1 order by chapter_no,verse_no"];
    NSMutableArray* versesData = [self retrieve:sql];
    
    for (NSMutableArray* chapter in chaptersData) {
        int chapterNo=[(NSString*)[chapter objectAtIndex:0] intValue];
        for (NSMutableArray* row in versesData) {
            int id =[self getNewId:@"Verse"];
            int verseNo   = [(NSString*)[row objectAtIndex:2] intValue];
            NSString* translation = (NSString*)[row objectAtIndex:3] ;
            NSString* verse = (NSString*)[row objectAtIndex:4] ;
            NSString* transliteration = (NSString*)[row objectAtIndex:5] ;
            NSString* size = (NSString*)[row objectAtIndex:6] ;
            NSString* splitword = (NSString*)[row objectAtIndex:7] ;
            
            sql = [NSString stringWithFormat:@"INSERT INTO Verse (id, chapter_no, verse_no, translation, verse, transliteration, size, splitword) VALUES (%i,%i,%i,'%@','%@','%@','%@','%@')",id,chapterNo,verseNo,translation,verse,transliteration,size,splitword];
                   //Insert into Verse set splitword='%@' where chapter_no=%i and verse_no=%i",splitWordTranslation,chapterNo,verseNo];
            NSLog(@"SQL=%@",sql);
            [self executeSQL:sql];
            // NSLog(@"        VerseNo %i (%i) ",verseNo,verseLength);
        }
    }
    //NSLog(@"Max Length:%i",maxLength);
}

- (void) insertBismillahInToken  {
    NSString* sql = @"Select id,verse_count from Chapter where id>1 and id <> 9 order by id";
    NSMutableArray* chaptersData = [self retrieve:sql];
    sql = @"Select * from Token where verse_no=1";
    NSMutableArray* bismilla = [self retrieve:sql];
    if ([bismilla count]>15) return;
    //NSLog(@" Chapters:%i",(int)[chaptersData count]);
    sql = [NSString stringWithFormat:@"Select * from Token where chapter_no=1 and verse_no=1 order by chapter_no,verse_no"];
    NSMutableArray* tokenData = [self retrieve:sql];
    
    for (NSMutableArray* chapter in chaptersData) {
        int chapterNo=[(NSString*)[chapter objectAtIndex:0] intValue];
        for (NSMutableArray* row in tokenData) {
            int id =[self getNewId:@"Token"];
            int verseNo   = [(NSString*)[row objectAtIndex:2] intValue];
            int tokenNo   = [(NSString*)[row objectAtIndex:3] intValue];
            NSString* token = (NSString*)[row objectAtIndex:4] ;
            NSString* transliteration = (NSString*)[row objectAtIndex:5] ;
            NSString* token_simple = (NSString*)[row objectAtIndex:6] ;
            NSString* meaning = (NSString*)[row objectAtIndex:7] ;
            
            sql = [NSString stringWithFormat:@"INSERT INTO Token (id, chapter_no, verse_no, token_no, token, transliteration, token_simple, meaning) VALUES (%i,%i,%i,%i,'%@','%@','%@','%@')",id,chapterNo,verseNo,tokenNo,token,transliteration,token_simple,meaning];
            //Insert into Verse set splitword='%@' where chapter_no=%i and verse_no=%i",splitWordTranslation,chapterNo,verseNo];
            NSLog(@"SQL=%@",sql);
            [self executeSQL:sql];
            // NSLog(@"        VerseNo %i (%i) ",verseNo,verseLength);
        }
    }
    //NSLog(@"Max Length:%i",maxLength);
}

- (void) insertBismillahInSegment  {
    NSString* sql = @"Select id,verse_count from Chapter where id>1 and id <> 9 order by id";
    NSMutableArray* chaptersData = [self retrieve:sql];
    sql = @"Select * from Segment where verse_no=1";
    NSMutableArray* bismilla = [self retrieve:sql];
    if ([bismilla count]>25) return;
    //NSLog(@" Chapters:%i",(int)[chaptersData count]);
    sql = [NSString stringWithFormat:@"Select * from Segment where chapter_no=1 and verse_no=1 order by chapter_no,verse_no"];
    NSMutableArray* segmentData = [self retrieve:sql];
    
    for (NSMutableArray* chapter in chaptersData) {
        int chapterNo=[(NSString*)[chapter objectAtIndex:0] intValue];
        for (NSMutableArray* row in segmentData) {
            int id =[self getNewId:@"Segment"];
            int verseNo   = [(NSString*)[row objectAtIndex:2] intValue];
            int tokenNo   = [(NSString*)[row objectAtIndex:3] intValue];
            int segmentNo   = [(NSString*)[row objectAtIndex:4] intValue];
            NSString* features = (NSString*)[row objectAtIndex:5] ;
            int wordID = [(NSString*)[row objectAtIndex:6] intValue];
            
            sql = [NSString stringWithFormat:@"INSERT INTO Segment (id, chapter_no, verse_no, token_no, segment_no, features, word_id) VALUES (%i,%i,%i,%i,%i,'%@',%i)",id,chapterNo,verseNo,tokenNo,segmentNo,features,wordID];
            //Insert into Verse set splitword='%@' where chapter_no=%i and verse_no=%i",splitWordTranslation,chapterNo,verseNo];
            NSLog(@"SQL=%@",sql);
            [self executeSQL:sql];
            // NSLog(@"        VerseNo %i (%i) ",verseNo,verseLength);
        }
    }
    //NSLog(@"Max Length:%i",maxLength);
}




- (void) registerController:(UITableViewController<WODTableViewConroller>*) controller {
    if (controllers==nil) controllers = [[NSMutableArray alloc] initWithCapacity:3];
    [controllers addObject:controller];
}


- (void) initializeEntitiesFromDatabase {
    //[self addWords];
    //
    //dictionaryArray= [NSArray arrayWithArray:[self fetchData:WORD:[TRANSLITERATION lowercaseString]]];
    //[self printWords];
}

- (void) initializeWordList {
    
}

-(NSString*) getRootMeaning:(NSString*) root{
    NSString* sql = [NSString stringWithFormat:@"Select meaning from Root where root='%@'",root];
    //NSLog(@"Root:%@",sql);
    NSMutableArray* rootResultset = [self retrieve:sql];
    if ([rootResultset count]>0) {
        NSMutableArray* rootRow =  [rootResultset objectAtIndex:0];
        return (NSString*)[rootRow objectAtIndex:0];
    }
    else return nil;
}

-(NSMutableArray*) getAllRootIndexes{
    NSString* sql = @"Select distinct letter1,letter2,letter3 from Root order by letter1,letter2,letter3";
    NSMutableArray* root = [self retrieve:sql];
    if ([root count]>0) return root;
    else return nil;
}


-(NSMutableArray*) getAllChapters{
    NSString* sql = @"Select * from Chapter order by id";
    return [self retrieve:sql];
}

-(NSMutableArray*) getFirstLettersOfAllRootWords{
    NSString* sql = @"Select distinct letter1 from Root order by letter1";
    NSMutableArray* root = [self retrieve:sql];
    if ([root count]>0) {
        NSMutableArray* letters = [NSMutableArray arrayWithCapacity:[root count]];
        for (NSMutableArray* row in root) {
            [letters addObject:(NSString*)[row objectAtIndex:0]];
        }
        return letters;
    }
    else return nil;
}


-(NSString*) getDeviceName{
    if ([deviceName length]==0) {
        deviceName = [Utils getDeviceName];
    }
    return deviceName;
}

-(NSMutableArray*) getRoot :(NSString*) word {
    NSString* sql = [NSString stringWithFormat:@"Select r.id,r.root from Word w, Root r where w.word='%@' and w.root_id=r.id LIMIT 1",word];
    NSMutableArray* root = [self retrieve:sql];
    if ([root count]>0) return (NSMutableArray*)[root objectAtIndex:0];
    else return nil;
}


-(NSMutableArray*) getSegment :(NSString*) wordPart {
    NSString* sql = [NSString stringWithFormat:@"select word,id from Word where substr(word_simple,1,%tu)='%@'",[wordPart length],wordPart];
    //NSLog(@"Segment SQL:%@",sql);
    NSMutableArray* segment = [self retrieve:sql];
    if ([segment count]>0) return segment;
    else return nil;
}

- (Word*)  getWord:(NSString*) wordId {
    NSString* sql = [NSString stringWithFormat:@"Select id,word,transliteration,tag,word_simple from Word where id=%@",wordId];
    NSMutableArray* words = [self retrieve:sql];
    NSDictionary* favorites = [self retrieveData:@"Select word_id from favorite"];
    for (NSMutableArray *row in words) {
        return [self addWord:row Favorites:favorites];
    }
    return nil;
}

- (Word*)  getWord:(int) chapter_no VerseNo:(int) verse_no TokenNo:(int) token_no {
    //NSString* sql = [NSString stringWithFormat:@"Select id,word,transliteration,tag,word_simple from Word where id=%@",token];
    //NSString* sql=[NSString stringWithFormat:@"Select w.id,w.word,w.transliteration,w.tag,w.word_simple from Word w, Token t where token_simple=word_simple and chapter_no=%i and verse_no=%i and token_no=%i",chapter_no,verse_no,token_no];
    NSString* sql=[NSString stringWithFormat:@"Select w.id,w.word,w.transliteration,w.tag,w.word_simple from Word w, Segment s where chapter_no=%i and verse_no=%i and token_no=%i and s.word_id =w.id order by length(w.word) desc",chapter_no,verse_no,token_no];
    NSMutableArray* words = [self retrieve:sql];
    //NSLog(@"GetWord SQL:%@",sql);
    NSDictionary* favorites = [self retrieveData:@"Select word_id from favorite"];
    for (NSMutableArray *row in words) {
        return [self addWord:row Favorites:favorites];
    }
    return nil;
}


-(NSMutableArray*) getRecentWords {
    NSString* sql = [NSString stringWithFormat:@"Select word,id,transliteration,tag,word_simple from Word where id in (Select word_id from EmailPlan) order by word_simple"];
    NSMutableArray* recentWords = [self retrieve:sql];
    if ([recentWords count]>0) return recentWords;
    else return nil;
}






-(NSString*) getLemma :(NSString*) word {
    NSString* sql = [NSString stringWithFormat:@"Select r.id,r.root from Word w, Root r where w.word='%@' and w.root_id=r.id LIMIT 1",word];
    NSMutableArray* lemma = [self retrieve:sql];
    if ([lemma count]>0) return (NSString*)[(NSMutableArray*)[lemma objectAtIndex:0] objectAtIndex:1];
    else return @"No Lemma.";
}


- (NSMutableArray*) getAllChapterNames {
    NSString* sql = [NSString stringWithFormat:@"Select id,transliteration,translation from Chapter"];
    NSMutableArray* chapters = [self retrieve:sql];
    if ([chapters count]>0) return chapters;
    else return nil;
}

- (void) setProperty:(NSString*) key Value:(NSString*) value {
    // Hide the keyboard
    // [firstNameTextField resignFirstResponder];
    // Store the data
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    [defaults setObject:value forKey:key];
    [defaults synchronize];
    //NSLog(@"property saved");
}


- (NSString*) getProperty:(NSString*) key {
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    return [defaults objectForKey:key];
}


- (BOOL)addSkipBackupAttributeToItemAtURL:(NSURL *)URL {
    const char* filePath = [[URL path] fileSystemRepresentation];
    const char* attrName = "com.apple.MobileBackup";
    u_int8_t attrValue = 1;
    int result = setxattr(filePath, attrName, &attrValue, sizeof(attrValue), 0, 0);
    return result == 0;
}

//-(NSString*) getTranslation :(int) chapter:(int) verse{
//    NSString* sql = [NSString stringWithFormat:@"Select Id,translation from translation where Chapter=%i and Verse=%i order by Chapter,Verse",chapter,verse];
//        //NSLog(@"Segment SQL:%@",sql);
//    NSMutableArray* verses = [self retrieve:sql];
//
//    if ([verses count]>0) return (NSString*)[(NSMutableArray*)[verses objectAtIndex:0] objectAtIndex:1];
//    else return @"No translation found.";
//}

-(Word*) getVerseAt:(NSUInteger) chapterNo VerseNo:(NSUInteger) verseNo {
    NSString* sql = [NSString stringWithFormat:@"Select chapter_no,verse_no,verse,translation,transliteration,Size from Verse where chapter_no=%tu and verse_no=%tu order by chapter_no,verse_no",chapterNo,verseNo];
    NSMutableArray* versesData = [self retrieve:sql];
    NSString* splitWordTranslation = [self getSplitWordTranslationOfVerseNo:verseNo atChapterNo:chapterNo];
    NSArray* splitWordGrammar = [self getSplitWordGrammarOfVerseNo:verseNo atChapterNo:chapterNo];
    
    //NSLog(@"SQL = %@ Count=%i",sql,[versesData count]);
    Word* entityWord= (Word *)[NSEntityDescription insertNewObjectForEntityForName:WORD inManagedObjectContext:managedObjectContext];
    entityWord.word =[NSString stringWithFormat:@"Verse(%tu,%tu)",chapterNo,verseNo];
    for (NSMutableArray* row in versesData) {
        //Select meaning from Token where chapter_no=1 and verse_no=2 order by chapter_no,verse_no;
        [row addObject:splitWordTranslation];
        [row addObject:splitWordGrammar];
        [self addVerseEntityIn:row toWordEntity:entityWord];
    }
    return entityWord;
}

-(Word*) getVerseAt:(NSUInteger) chapterNo {
    NSString* sql = [NSString stringWithFormat:@"Select chapter_no,verse_no,verse,translation,transliteration,Size from Verse where chapter_no=%tu order by chapter_no,verse_no",chapterNo];
    NSMutableArray* versesData = [self retrieve:sql];
    NSString* splitWordTranslation = @"";//[self getSplitWordTranslationOfVerseNo:verseNo atChapterNo:chapterNo];
    NSArray* splitWordGrammar = nil;//[self getSplitWordGrammarOfVerseNo:verseNo atChapterNo:chapterNo];
    
    //NSLog(@"SQL = %@ Count=%i",sql,[versesData count]);
    Word* entityWord= (Word *)[NSEntityDescription insertNewObjectForEntityForName:WORD inManagedObjectContext:managedObjectContext];
    entityWord.word =[NSString stringWithFormat:@"Verse(%tu,%i)",chapterNo,1];
    for (NSMutableArray* row in versesData) {
        //Select meaning from Token where chapter_no=1 and verse_no=2 order by chapter_no,verse_no;
        [row addObject:splitWordTranslation];
        [row addObject:splitWordGrammar];
        [self addVerseEntityIn:row toWordEntity:entityWord];
    }
    return entityWord;
}

-(int*) getVerseSizeFor: (Verse*) verse  WhenDiplayAttributesAre:(NSString*) displayAttributes{
    //return 100;
    //NSString* sql = [NSString stringWithFormat:@"Select size from Verse where chapter_no=%i and verse_no=%i",chapterNo,verseNo];
    NSString* sql = [NSString stringWithFormat:@"Select size from Verse where chapter_no=%i and verse_no=%i",[verse.chapterno intValue],[verse.verseno intValue]];
    //NSLog(@"[getVerseSizeFor] SQL=%@",sql);
    NSMutableArray* versesData = [self retrieve:sql];
    if ([versesData count]>0) {
        NSMutableArray* sizeRow =  [versesData objectAtIndex:0];
        int size = [(NSString*)[sizeRow objectAtIndex:0] intValue];
        //NSLog(@"getVerseSizeAt=%i",size);
        return size;
    }
    else {
        return 100;
    }
}

-(void) updateVerseSizeFor:(Verse*) verse  WhenDiplayAttributesAre:(NSString*) displayAttributes AndHeight:(int) height{
    //return 100;
    //NSString* sql = [NSString stringWithFormat:@"Select size from Verse where chapter_no=%i and verse_no=%i",chapterNo,verseNo];
    if ([Utils ifNull:verse.size]) verse.size = [NSString stringWithFormat:@"%@=%i",displayAttributes,height];
    else  [NSString stringWithFormat:@"%@=%i,%@",displayAttributes,height,verse.size];
    NSString* sql = [NSString stringWithFormat:@"Update Verse Set size='%@' where chapter_no=%i and verse_no=%i",verse.size,[verse.chapterno intValue],[verse.verseno intValue]];
    //NSLog(@"Update Verse Size For:%@",sql);
    [self executeSQL:sql];
    //[self executeSQL:@"commit"];
    
}




-(NSString*) cleanSplitWord:(NSString*) splitWord {
    //splitWord = @"they stand (still). Also! and one,two,three";
    NSError *error = nil;
    NSRegularExpression *regex = [NSRegularExpression regularExpressionWithPattern:@"\\(.+?\\)|\\\"|\\.|\\,|\\!|\\-|\\;" options:NSRegularExpressionCaseInsensitive error:&error];
    NSString *cleanWord = [regex stringByReplacingMatchesInString:splitWord options:0 range:NSMakeRange(0, [splitWord length]) withTemplate:@" "];
    //cleanWord = [cleanWord stringByReplacingOccurrencesOfString:@" " withString:@" "];
    cleanWord = [cleanWord stringByReplacingOccurrencesOfString:@"'" withString:@""];
    cleanWord = allTrim(cleanWord);
    //NSLog(@"[%@]", modifiedString);
    return cleanWord;
}

-(NSString*) getSplitWordTranslationOfVerseNo:(NSUInteger) verseNo atChapterNo:(NSUInteger) chapterNo {
    NSString* sql = [NSString stringWithFormat:@"Select meaning from Token where chapter_no=%tu and verse_no=%tu order by chapter_no,verse_no",chapterNo,verseNo];
    NSMutableArray* versesData = [self retrieveColumn:sql];
    NSMutableString* splitWordTranslation=nil;
    for (NSString* splitWord in versesData) {
        //if ([splitWord
        if (splitWordTranslation==nil) splitWordTranslation=[NSMutableString stringWithString:splitWord]; else [splitWordTranslation appendFormat:@"%@%@",SPLIT_WORD_DELIMITER,splitWord];
    }
    return [self cleanSplitWord:splitWordTranslation];
}


-(NSMutableArray*) getSplitWordGrammarOfVerseNo:(NSUInteger) verseNo atChapterNo:(NSUInteger) chapterNo {
    NSString* sql = [NSString stringWithFormat:@"Select word,token_no,segment_no,features from Segment S, Word W where chapter_no=%tu and verse_no=%tu and W.id = S.word_id order by chapter_no,verse_no,token_no,segment_no",chapterNo,verseNo];
    NSMutableArray* versesData = [self retrieve:sql];
    //NSLog(@"SQL = %@ Count=%i",sql,[versesData count]);
    
    return versesData;
}



-(void) getVerseTuple:(NSMutableDictionary*)verses Chapter:(int)chapter Verse:(int)verse Text:(NSString*)text Translation:(NSString*)translation {
    NSString* idField = [NSString stringWithFormat:@"%i:%i",chapter,verse];
    NSMutableArray* row = [NSMutableArray arrayWithObjects:[NSString stringWithFormat:@"%i",chapter],[NSString stringWithFormat:@"%i",verse],text,translation, nil];
    [verses setObject:row forKey:idField];
}

- (NSString *) getVerse:(int)chapterToQry VerseToQry:(int)verseToQry {
    NSString* sql = [NSString stringWithFormat:@"Select token_no,segment_no,w.word from Word w, Segment s where s.chapter_no=%i and s.verse_no=%i and w.id=s.word_id order by token_no,segment_no",chapterToQry,verseToQry];
    //NSLog(@"Segment SQL:%@",sql);
    NSMutableArray* segments = [self retrieve:sql];
    int _token=1;
    NSString* text = @"";
    for (NSMutableArray *row in segments) {
        NSString* token = (NSString*)[row objectAtIndex:0];
        NSString* segment = (NSString*)[row objectAtIndex:1];
        NSString* form = (NSString*)[row objectAtIndex:2];
        if (_token != [token intValue]) {
            _token = [token intValue];
            text = [text stringByAppendingString:@" "];
        }
        text = [text stringByAppendingString:form];
    }
    return text;
}



- (int) getVersesCount:(NSString*) word {
    NSString* sql = [NSString stringWithFormat:@"Select count(*) as VerseCount from Word w, Segment s where w.word='%@' and w.id=s.word_id Limit 1",word];
    //NSLog(@"Verse SQL:%@",sql);
    NSMutableArray* segments = [self retrieve:sql];
    if ([segments count]==0) return 0;
    NSMutableArray *row = (NSMutableArray*)[segments objectAtIndex:0];
    return[(NSString*)[row objectAtIndex:0] intValue];
}

- (NSDictionary*) getAllVerses:(NSString*) sql {
    //NSLog(@"Verse SQL:%@",sql);
    NSMutableDictionary *verses = [[NSMutableDictionary alloc] init];
    NSMutableArray* segments = [self retrieve:sql];
    
    for (NSMutableArray *row in segments) {
        NSString * id = (NSString*)[row objectAtIndex:0];
        int chapter = [(NSString*)[row objectAtIndex:1] intValue];
        int verse   = [(NSString*)[row objectAtIndex:2] intValue];
        //[self getVerseTuple:verses:chapter:verse:[self getVerse:chapter:verse]:[self getTranslation:chapter:verse]];
    }
    return verses;
}


- (NSMutableArray*) getWordsInRootWord:(NSString*) root{
    NSString* sql=[NSString stringWithFormat:@"Select id from Root where root='%@'",root];
    NSString* rootId= [self getColValue:sql];
    sql = [NSString stringWithFormat:@"Select distinct v.chapter_no,v.verse_no,token_no,W.word from Segment s, Word w, Verse v  where s.word_id=w.id and w.root_id=%@  and s.chapter_no=v.chapter_no and s.verse_no=v.verse_no order by v.chapter_no,v.verse_no",rootId];
    //NSLog(@"Words for Root Word SQL:%@",sql);
    //sql = [NSString stringWithFormat:@"Select chapter_no,verse_no,verse,translation,transliteration,size,splitword from Verse where chapter_no=%i order by chapter_no,verse_no",1];
    NSMutableArray* wordsData = [self retrieve:sql];
    return wordsData;
}

- (NSMutableDictionary*) getVersesInRootWord:(NSString*) root{
    NSString* sql=[NSString stringWithFormat:@"Select id from Root where root='%@'",root];
    NSString* rootId= [self getColValue:sql];
    sql = [NSString stringWithFormat:@"Select distinct v.chapter_no,v.verse_no,v.verse,v.translation,v.transliteration,v.size,v.splitword from Segment s, Word w, Verse v  where s.word_id=w.id and w.root_id=%@  and s.chapter_no=v.chapter_no and s.verse_no=v.verse_no order by v.chapter_no,v.verse_no",rootId];
    //NSLog(@"Root SQL:%@",sql);
    //sql = [NSString stringWithFormat:@"Select chapter_no,verse_no,verse,translation,transliteration,size,splitword from Verse where chapter_no=%i order by chapter_no,verse_no",1];
    NSMutableArray* versesData = [self retrieve:sql];
    return [self packageVersesInWord:versesData chapter:1];
}




- (NSMutableDictionary*) getVersesInTranslatedWord:(NSString*) transWord{
    NSString* sql = [NSString stringWithFormat:@"Select chapter_no,verse_no,verse,translation,transliteration,size,splitword from Verse where translation like '%%%@%%' order by chapter_no,verse_no",transWord];
    NSMutableArray* versesData = [self retrieve:sql];
    return [self packageVersesInWord:versesData chapter:1];
    
}

- (NSMutableDictionary*) getVersesInChapter:(int) chapterNo{
    //NSString* sql = [NSString stringWithFormat:@"Select chapter_no,verse_no,verse,translation,transliteration,size,splitword from Verse where chapter_no=2 and (verse_no>=4 and verse_no<=4) order by chapter_no,verse_no"];
    NSString* sql = [NSString stringWithFormat:@"Select chapter_no,verse_no,verse,translation,transliteration,size,splitword from Verse where chapter_no=%i order by chapter_no,verse_no",chapterNo];
    
    NSMutableArray* versesData = [self retrieve:sql];
    return [self packageVersesInWord:versesData chapter:chapterNo];
    
}
- (NSMutableDictionary*) packageVersesInWord:(NSMutableArray*) versesData chapter:(int) chapterNo{
    Word* entityWord= (Word *)[NSEntityDescription insertNewObjectForEntityForName:WORD inManagedObjectContext:managedObjectContext];
    entityWord.word =[NSString stringWithFormat:@"Verse(%tu,%i)",chapterNo,1];
    //NSMutableArray *verses = [[NSMutableArray alloc] initWithCapacity:[versesData count]];
    NSMutableDictionary* verses = [[NSMutableDictionary alloc] initWithCapacity:[versesData count]+1];
    NSMutableArray* allVerses = [NSMutableArray arrayWithCapacity:[versesData count]];
    //NSLog(@"SQL = %@ Count=%i",sql,[versesData count]);
    for (NSMutableArray* row in versesData) {
        //Select meaning from Token where chapter_no=1 and verse_no=2 order by chapter_no,verse_no;
        NSNumber* chapterno = [NSNumber numberWithInt:[(NSString*)[row objectAtIndex:0] intValue]];
        NSNumber* verseno   = [NSNumber numberWithInt:[(NSString*)[row objectAtIndex:1] intValue]];
        NSString* verseId = [NSString stringWithFormat:@"%i:%i",[chapterno intValue],[verseno intValue]];
        if ([verses objectForKey:verseId]==nil) {
            NSString* text = [row objectAtIndex:2];
            NSString* translation = [row objectAtIndex:3];
            NSString* transliteration = [row objectAtIndex:4];
            NSString* size = [row objectAtIndex:5];
            NSString* translation_split_word = [row objectAtIndex:6];
            //NSMutableArray* grammar_split_word=[row count]==8?[row objectAtIndex:7]:nil;;
            //NSLog(@"translation_split_word=%@",translation_split_word);
            Verse* entityVerse = (Verse *)[NSEntityDescription insertNewObjectForEntityForName:VERSE inManagedObjectContext:managedObjectContext];
            entityVerse.has=entityWord;
            entityVerse.verse=text;
            entityVerse.chapterno=chapterno;
            entityVerse.verseno=verseno;
            entityVerse.translation=translation;
            entityVerse.transliteration=transliteration;
            entityVerse.translation_split_word=translation_split_word;
            entityVerse.verseMorphology=nil;
            entityVerse.size=size;
            [verses setValue:entityVerse forKey:verseId];
            //NSLog(@"[Package Verses] VerseId:%@",verseId);
            CGPoint verseIdPt= CGPointMake([chapterno intValue],[verseno intValue]);
            NSValue* value = [NSValue valueWithCGPoint:verseIdPt];
            [allVerses addObject:value];
        }
    }
    NSString* verseId = [NSString stringWithFormat:@"%i:%i",0,0];
    [verses setValue:allVerses forKey:verseId];
    return verses;
}


- (NSMutableArray*) getVerseNosInChapter:(int) chapterNo{
    NSString* sql = [NSString stringWithFormat:@"Select distinct chapter_no,verse_no from Segment where chapter_no=%tu order by chapter_no,verse_no",chapterNo];
    return [self retrieve:sql];
}

- (int) getVerseCountForRoot:(NSString*) root{
    NSString* sql=[NSString stringWithFormat:@"Select id from Root where root='%@'",root];
    NSString* rootId= [self getColValue:sql];
    sql = [NSString stringWithFormat:@"Select count(*) as VerseCount from Segment s, Word w where s.word_id=w.id and w.root_id=%@",rootId];
    return [[self getColValue:sql] intValue];
}

- (NSMutableArray*) getVerseNosForRoot:(NSString*) root{
    NSString* sql=[NSString stringWithFormat:@"Select id from Root where root='%@'",root];
    NSString* rootId= [self getColValue:sql];
    sql = [NSString stringWithFormat:@"Select distinct chapter_no,verse_no,word from Segment s, Word w where s.word_id=w.id and w.root_id=%@  order by chapter_no,verse_no",rootId];
    //NSLog(@"Verse SQL:%@",sql);
    return [self retrieve:sql];
}

- (NSMutableArray*) getVerseNosForWord:(Word*) word{
    //NSString* sql = [NSString stringWithFormat:@"Select distinct chapter_no,verse_no,'' as text,'' as translation,'' as translation_split_word,'' as transliteration from Segment S, Word w where S.word_id=W.id and W.word='%@' order by chapter_no,verse_no",word.word];
    NSString* sql = [NSString stringWithFormat:@"Select distinct v.chapter_no,v.verse_no, v.verse as text,v.translation as translation,v.transliteration as transliteration,v.splitword from Segment S, Word w , Verse v where S.word_id=W.id and W.word='%@' and v.chapter_no=s.chapter_no and v.verse_no = s.verse_no order by v.chapter_no,v.verse_no",word.word];
    //NSLog(@"Verse SQL:%@",sql);
    return [self retrieve:sql];
}


- (NSMutableArray*) getVerseNosForTranslationWord:(NSString*) word{
    //NSString* sql = [NSString stringWithFormat:@"Select distinct chapter_no,verse_no,'' as text,'' as translation,'' as translation_split_word,'' as transliteration from Segment S, Word w where S.word_id=W.id and W.word='%@' order by chapter_no,verse_no",word.word];
    NSString* sql = [NSString stringWithFormat:@"Select distinct v.chapter_no,v.verse_no, v.verse as text,v.translation as translation,v.transliteration as transliteration,v.splitword from Verse v where v.translation like '%%%@%%' and order by v.chapter_no,v.verse_no",word];
    return [self retrieve:sql];
}


- (Word*) addVersesToChapter:(NSUInteger) chapterNo chapterName:(NSString*) chapterName {
    NSString* sql = [NSString stringWithFormat:@"Select id,chapter_no,verse_no from Segment where chapter_no=%tu order by chapter_no,verse_no",chapterNo];
    NSDictionary* versesData = [self getAllVerses:sql];
    Word* entityWord= (Word *)[NSEntityDescription insertNewObjectForEntityForName:WORD inManagedObjectContext:managedObjectContext];
    entityWord.word = chapterName;
    [self getVerses:versesData EntityWord:entityWord];
    return entityWord;
}

- (NSMutableArray*) getRelatedWords:(NSString*) word root:(NSString*) rootId {
    NSString* sql = [NSString stringWithFormat:@"Select id,word from Word where root_id=%@ and word <> '%@' LIMIT 20",rootId,word];
    return [self retrieve:sql];
    
}

- (NSMutableArray*) getWordsForRoot:(NSString*) root {
    NSString* sql=[NSString stringWithFormat:@"Select id from Root where root='%@'",root];
    NSString* rootId= [self getColValue:sql];
    sql = [NSString stringWithFormat:@"Select word,word_simple,meaning_simple from Word where root_id=%@",rootId];
    //NSLog(@"Verse SQL:%@",sql);
    return [self retrieve:sql];
    
}

- (NSString*) getRelatedWordsAsText:(NSString*) word root:(NSString*) rootId {
    NSString* sql = [NSString stringWithFormat:@"Select Distinct word_simple from Word where root_id=%@ and word <> '%@'",rootId,word];
    NSMutableString* relatedWords = [NSMutableString stringWithString:@""];
    NSMutableArray* relatedWordsData=  [self retrieve:sql];
    if ([relatedWordsData count]>0) {
        //NSMutableSet* relatedWordsSet = [[NSMutableSet alloc] init];
        for (NSMutableArray *row in relatedWordsData) {
            NSString* relatedWord  = (NSString*)[row objectAtIndex:0];
            [relatedWords appendFormat:@" %@",relatedWord];
            //[relatedWordsSet addObject:relatedWord];
        }
        //        for (NSString* relatedWord in relatedWordsSet) {
        //            [relatedWords appendFormat:@" %@",relatedWord];
        //        }
        
    }
    NSString* resultStr = [relatedWords stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
    //NSLog(@"[%@]",resultStr);
    return resultStr;
}


-(void) copyData:(NSString*)tableName Table:(NSDictionary*)table{
    //NSString* tableName= @"example";
    NSString* deleteSQL_local = [NSString stringWithFormat:@"Delete from %@",tableName];
    [self executeSQL:deleteSQL_local];
    int rowNo=0;
    NSString* colValue;
    NSMutableString* colNames = [NSMutableString string];
    NSArray* colList = [self getColumnList:tableName];
    for (int c=0; c<[colList count]; c++) {
        if (c > 0) [colNames appendString:@","];
        [colNames appendString:[colList objectAtIndex:c]];
        
    }
    for (NSString* rowKey in table) {
        NSArray* row = [table objectForKey:rowKey];
        NSMutableString* rowValues = [NSMutableString string];
        [rowValues appendString:rowKey];
        
        int colNo=1;
        for (NSString* storedValue in row) {
            colValue = [NSString stringWithFormat:@"'%@'",storedValue];
            [rowValues appendString:@","];
            [rowValues appendString:colValue];
            colNo++;
        }
        NSString* insertSQL_local=[NSString stringWithFormat:@"INSERT into %@ (%@) VALUES (%@)",tableName,colNames,rowValues];
        NSLog(@"%@: %@",[NSString stringWithFormat:@"%i",rowNo],insertSQL_local);
        [self executeSQL:insertSQL_local];
        //NSLog(@"%@: %@",[NSString stringWithFormat:@"%i",rowNo],insertSQL);
        //NSLog(@"%@ %@",[NSString stringWithFormat:@"%i",rowNo],rowValues);
    }
}


-(NSDictionary*) refreshTableFromCloud:(NSString *)tableName{
    return [self refreshTableFromCloud:tableName QrySQL_cloud:nil DeleteSQL_local:nil];
}




-(NSDictionary*) refreshTableFromCloud:(NSString *)tableName QrySQL_cloud:(NSString*)qrySQL_cloud DeleteSQL_local:(NSString*)deleteSQL_local {
    //NSString* tableName= @"example";
    if (qrySQL_cloud==nil) qrySQL_cloud = [NSString stringWithFormat:@"Select * from %@",tableName];
    if (deleteSQL_local==nil) deleteSQL_local = [NSString stringWithFormat:@"Delete from %@",tableName];
    
    NSDictionary* table = [dbCloud retrieve:qrySQL_cloud];
    [self executeSQL:deleteSQL_local];
    int rowNo=0;
    NSString* colValue;
    NSMutableString* colNames = [NSMutableString string];
    for (id rowKey in table) {
        NSDictionary* row = (NSDictionary*) [table objectForKey:rowKey];
        NSMutableString* rowValues = [NSMutableString string];
        int colNo=0;
        for (id colName in row) {
            if (rowNo==0) {
                if (colNo > 0) [colNames appendString:@","];
                [colNames appendString:(NSString*)colName];
            }
            NSString* storedValue =(NSString*) [row objectForKey:colName];
            if ([colName isEqualToString:ID]) colValue =storedValue;
            else {
                colValue = [NSString stringWithFormat:@"'%@'",storedValue];
            }
            if (colNo > 0) [rowValues appendString:@","];
            [rowValues appendString:colValue];
            colNo++;
            
        }
        //if (rowNo==0) NSLog(@"%@ %@",[NSString stringWithFormat:@"%i",rowNo],colNames);
        rowNo++;
        NSString* insertSQL_local=[NSString stringWithFormat:@"INSERT into %@ (%@) VALUES (%@)",tableName,colNames,rowValues];
        //NSLog(@"%@: %@",[NSString stringWithFormat:@"%i",rowNo],insertSQL_local);
        [self executeSQL:insertSQL_local];
        //NSLog(@"%@: %@",[NSString stringWithFormat:@"%i",rowNo],insertSQL);
        //NSLog(@"%@ %@",[NSString stringWithFormat:@"%i",rowNo],rowValues);
    }
    return table;
    
}

-(NSArray*) getColumnList:(NSString *)tableName {
    NSMutableArray* colList = [[NSMutableArray alloc] init];
    NSString* querySQL = [@"Select * from " stringByAppendingFormat:@"%@ Where 1<>1 ",tableName];
    //const char *dbpath = [databasePath UTF8String];
    sqlite3_stmt   *statement;
    int i=0;
    const char *query_stmt = [querySQL UTF8String];
    if (sqlite3_prepare_v2(wordDB, query_stmt, -1, &statement, NULL) == SQLITE_OK) {
        int colCount = sqlite3_column_count(statement);
        for (int c=0; c < colCount; c++) {
            NSString* colName = [[NSString alloc] initWithUTF8String:sqlite3_column_name(statement,c)];
            [colList addObject:[colName lowercaseString]];
        }
    }
    sqlite3_finalize(statement);
    return colList;
}


-(void) exportToCloud {
    for (NSString *table in dictTables) {
        [self uploadToCloudInBatch:table];
        //NSLog(@" Table:%@",tableName);
    }
}


-(void) exportAllToCloud {
    NSMutableArray* tables = [self retrieve:@"SELECT name FROM sqlite_master WHERE type='table'"];
    for (NSMutableArray *row in tables) {
        [self uploadToCloud:[row objectAtIndex:0]];
        //NSLog(@" Table:%@",tableName);
    }
    
    
}

-(void) importFromCloud {
    NSMutableArray* tables = [self retrieve:@"SELECT name FROM sqlite_master WHERE type='table'"];
    for (NSMutableArray *row in tables) {
        [self refreshTableFromCloud:[row objectAtIndex:0]];
        //NSLog(@" Table:%@",tableName);
    }
}


-(NSMutableArray*) getColumnListFromResult:(sqlite3_stmt*) statement {
    NSMutableArray* colList = [[NSMutableArray alloc] init];
    int colCount = sqlite3_column_count(statement);
    for (int c=0; c < colCount; c++) {
        NSString* colName = [[NSString alloc] initWithUTF8String:sqlite3_column_name(statement,c)];
        [colList addObject:colName];
    }
    return colList;
}


- (void) uploadToCloudInBatch:(NSString*) tableName{
    NSString* limit =@"";//LIMIT 100
    NSString* sql = [@"Select * from " stringByAppendingFormat:@"%@ %@",tableName,limit];
    NSMutableArray* tableData = [self retrieve:sql];
    NSArray* colList = [self getColumnList:tableName];
    [dbCloud createTable:tableName withData:tableData UsingLayout:colList];
}


- (void) uploadToCloud:(NSString*) tableName{
    NSString* querySQL = [@"Select * from " stringByAppendingString:tableName];
    NSMutableDictionary *dict = [[NSMutableDictionary alloc] init];
    //const char *dbpath = [databasePath UTF8String];
    sqlite3_stmt    *statement;
    int i=0;
    const char *query_stmt = [querySQL UTF8String];;
    NSArray* colList;
    if (sqlite3_prepare_v2(wordDB, query_stmt, -1, &statement, NULL) == SQLITE_OK) {
        colList = [self getColumnListFromResult:statement];
        while (sqlite3_step(statement) == SQLITE_ROW) {
            i++;
            //NSString* idField = [ NSString stringWithFormat: @"%i",i];
            NSString* idField = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(statement, 0)];
            NSMutableArray* row = [[NSMutableArray alloc] init ];
            for (int c=0; c < [colList count]; c++) {
                const char *fieldChars = (const char *) sqlite3_column_text(statement, c);
                NSString* field;
                if (fieldChars ==nil)
                    field = @" ";
                else
                    field = [[NSString alloc] initWithUTF8String:fieldChars];
                [row addObject:field];
            }
            [dict setObject:row forKey:idField];
        }
    }
    sqlite3_finalize(statement);
    [dbCloud createTable:tableName TableData:dict ColList:colList];
}

- (void) printWords {
    for (Word *entityWord in dictionaryArray) {
        NSString* word = entityWord.word;
        NSString* transliteration = entityWord.transliteration;
        //NSString* meaning = entityWord.;
        NSLog(@"Word: %@ Transliteration:%@",word,transliteration);
        NSLog(@"*********************");
        [self printDefinitions:entityWord];
        NSLog(@"*********************");
    }
    
}

- (void) printDefinitions:(Word*) entityWord {
    NSSet* definitions = entityWord.definitions;
    int i=0;
    for (Definition *entityDefinition in definitions) {
        i++;
        NSString* definition = entityDefinition.meaning;
        NSLog(@"Definition(%i) %@",i,definition);
        NSLog(@"-------------------");
        [self printExamples:entityDefinition];
        NSLog(@"-------------------");
    }
    
}

- (void) printExamples:(Definition*) entityDefinition {
    NSSet* examples = entityDefinition.examples;
    int i=0;
    for (Example *entityExample in examples) {
        i++;
        NSString* example = entityExample.example;
        NSString* reference = entityExample.reference;
        NSLog(@"Example (%i) %@ [%@]",i,example,reference);
    }
    
}

- (void) addNewItemsToCloud {
    for (int i=0; i < [wodTables count]; i++) {
        NSString* table = [wodTables objectAtIndex:i];
        [self uploadToCloudInBatch:[table lowercaseString]];
    }
}


- (void) getNewItemsFromCloud {
    if (dbCloud.online==NO) return;
    NSString* todaysDate=[Utils getTodaysDateInFormat:@"yyyyMMdd"];
    NSString* maxPlanDate= [self getColValue:@"Select max(plandate) as plandate from EmailPlan"];
    maxPlanDate = allTrim(maxPlanDate);
    if (maxPlanDate.length==0) maxPlanDate=todaysDate;
    int emailplanid_max = [self getNewId:EMAIL_PLAN];
    NSString* emailplanItemName = [dbCloud getPaddedValue:[NSString stringWithFormat:@"%i",emailplanid_max-1]];
    NSString* sql = [NSString stringWithFormat:@"Select id,status,plandate,word_id from EmailPlan where itemName() > '%@' order by itemName() asc ",emailplanItemName];
    //NSLog(@"email NoSQL:%@",sql);
    //NSString* sql = [NSString stringWithFormat:@"Select id,status,plandate,word_id from EmailPlan where plandate > '%@' order by plandate asc ",maxPlanDate];
    //NSLog(@"SQL:%@",sql);
    NSMutableArray* emailplan = [dbCloud retrieveInOrder:sql];
    if ([emailplan count]==0) {
        //NSLog(@"No new records found");
        [UIApplication sharedApplication].applicationIconBadgeNumber = 0;
        return;
    }
    else  [UIApplication sharedApplication].applicationIconBadgeNumber = [emailplan count];
    NSMutableArray* inserts= [[NSMutableArray alloc]  init];
    NSMutableArray* wordid_List=[[NSMutableArray alloc]  init];
    
    for (NSMutableDictionary *row in emailplan) {
        NSString* emailplanid = [row objectForKey:[ID lowercaseString]];
        NSString* plandate = [row objectForKey:[PLAN_DATE lowercaseString]];
        NSString* status = [row objectForKey:[STATUS lowercaseString]];
        NSString* wordid = [row objectForKey:[WORD_ID lowercaseString]];
        //NSString* _status1 = [plandate compare:todaysDate]==NSOrderedDescending?DONE:NEW;
        NSString* status2 = [plandate compare:todaysDate]==NSOrderedAscending?DONE:NEW;
        //NSLog(@" Status 1:%@ Status 2:%@",_status1,_status2);
        NSString* emailPlanSQL = [NSString stringWithFormat:@"Insert into EmailPlan (id,plandate,status,word_id) values (%@,'%@','%@',%@)",emailplanid,plandate,status2,wordid];
        NSLog(@"Id:%@, WordID:%@, PlanDate:%@ TodaysDate:%@ Status:%@",emailplanid,wordid,plandate,todaysDate,status);
        //[self executeSQL:emailPlanSQL];
        [inserts addObject:emailPlanSQL];
        [wordid_List addObject:wordid];
    }
    //NSLog(@"Meaning NoSQL:%@",sql);
    
    for (NSString* wordid in wordid_List) {
        //Update Word
        sql = [NSString stringWithFormat:@"Select meaning_simple,root_id,transliteration from Word where id='%@'",wordid];
        //NSLog(@"Cloud SQL:%@",sql);
        NSMutableArray* word = [dbCloud retrieveInOrder:sql];
        if ([word count]==0) continue;
        NSMutableDictionary* wordEntry = [word objectAtIndex:0];
        NSString* rootid = [wordEntry objectForKey:[ROOT_ID lowercaseString]];
        NSString* meaningSimple = [wordEntry objectForKey:[MEANING_SIMPLE lowercaseString]];
        NSString* transliteration = [wordEntry objectForKey:[TRANSLITERATION lowercaseString]];
        NSString* updateWordSQL = [NSString stringWithFormat:@"Update Word set meaning_simple='%@',transliteration='%@' where id=%@",meaningSimple,transliteration,wordid];
        //NSLog(@"updateWordSQL:%@",updateWordSQL);
        
        [inserts addObject:updateWordSQL];
        // Root
        sql = [NSString stringWithFormat:@"Select meaning from Root where id='%@'",rootid];
        NSMutableArray* root = [dbCloud retrieveInOrder:sql];
        if ([root count]>0) {
            NSMutableDictionary* rootEntry = [root objectAtIndex:0];
            NSString* meaningRoot = [rootEntry objectForKey:[MEANING lowercaseString]];
            NSString* updateRootSQL = [NSString stringWithFormat:@"Update Root set meaning='%@' where id=%@",meaningRoot,rootid];
            [inserts addObject:updateRootSQL];
        }
            //Meaning
        sql = [NSString stringWithFormat:@"Select id,language_id,meaning,word_id from Meaning where word_id='%@'",wordid];
        NSMutableArray* meaning = [dbCloud retrieveInOrder:sql];
        for (NSMutableDictionary* meaningEntry in meaning) {
            NSString* meaningid = [meaningEntry objectForKey:[ID lowercaseString]];
            NSString* languageid =   [meaningEntry objectForKey:[LANGUAGE_ID lowercaseString]];
            NSString* meaning = [meaningEntry objectForKey:[MEANING lowercaseString]];
            NSString* wordid = [meaningEntry objectForKey:[WORD_ID lowercaseString]];
            
            NSString* meaningSQL = [NSString stringWithFormat:@"Insert into Meaning (id,word_id,meaning,language_id) values (%@,%@,'%@',%@)",meaningid,wordid,meaning,languageid];
            [inserts addObject:meaningSQL];
            sql = [NSString stringWithFormat:@"Select id,meaning_id,example, reference from Example where meaning_id='%@'",meaningid];
            //NSLog(@"Example NoSQL:%@",sql);
            NSMutableArray* example = [dbCloud retrieveInOrder:sql];
            for (NSMutableDictionary* exampleEntry in example) {
                NSString* example = [exampleEntry objectForKey:[EXAMPLE lowercaseString]];
                NSString* exampleid = [exampleEntry objectForKey:[ID lowercaseString]];
                NSString* meaningid = [exampleEntry objectForKey:[MEANING_ID lowercaseString]];
                NSString* reference = [exampleEntry objectForKey:[REFERENCE lowercaseString]];
                NSString* exampleSQL = [NSString stringWithFormat:@"Insert into Example (id,meaning_id,example,reference) values (%@,%@,'%@','%@')",exampleid,meaningid,example,reference];
                [inserts addObject:exampleSQL];
            }
        }
        [self removeWOD:wordid];
    }
    
    for (NSString* sql in inserts) {
        //NSLog(@" INSERT SQL=%@",sql);
        [self executeSQL:sql];
    }
    
}

- (void) removeWOD:(NSString*) wordid {
    NSString* deleteEmailPlanSQL= [NSString stringWithFormat:@"Delete from EmailPlan where word_id=%@",wordid];
    NSString* deleteMeaningSQL= [NSString stringWithFormat:@"Delete from Meaning where word_id=%@",wordid];
    NSString* deleteFavoriteSQL= [NSString stringWithFormat:@"Delete from Favorite where word_id=%@",wordid];
    NSString* deleteExampleSQL= [NSString stringWithFormat:@"Delete from Example where meaning_id in (Select id from Meaning where word_id=%@)",wordid];
    //order is important
    [self executeSQL:deleteFavoriteSQL];
    [self executeSQL:deleteEmailPlanSQL];
    [self executeSQL:deleteExampleSQL];
    [self executeSQL:deleteMeaningSQL];
}





- (void) refreshDefaultWordOfDay {
    NSString* todaysDate=[Utils getTodaysDateInFormat:@"yyyyMMdd"];
    // 3 is Allah.
    NSString* sql = [NSString stringWithFormat:@"Select id,word,transliteration,tag,word_simple from Word where id = 6225"];
    NSMutableArray* words = [self retrieve:sql];
    NSDictionary* favorites = [self retrieveData:@"Select word_id from favorite"];
    NSString* wordid = (NSString*)[[words objectAtIndex:0] objectAtIndex:0];
    
    self.wordOfDay=[self addWord:[words objectAtIndex:0] Favorites:favorites];
    
}

- (void) refreshWordOfDay {
    NSString* todaysDate=[Utils getTodaysDateInFormat:@"yyyyMMdd"];
    NSString* sql = [NSString stringWithFormat:@"Select id,word,transliteration,tag,word_simple from Word where id in (Select word_id from emailplan where plandate='%@')",todaysDate];
    NSMutableArray* words = [self retrieve:sql];
    if ([words count] == 0) {
        sql = @"Select id,word,transliteration,tag,word_simple from Word where id in (Select word_id from EmailPlan where status='NEW' and plandate >= (Select Min(plandate) from EmailPlan Where status='NEW'))";
        words = [self retrieve:sql];
        if ([words count] == 0) {
            int max = [self getNewId:EMAIL_PLAN]-1;
            int min = 1;
            int randomId = (arc4random() % max) + min;
            sql = [NSString stringWithFormat:@"Select id,word,transliteration,tag,word_simple from Word where id =(Select word_id from emailplan where id = %i)",randomId];
            words = [self retrieve:sql];
        }
    }
    
    if ([words count]==0) {
        [self refreshDefaultWordOfDay];
        return;
    }
    NSDictionary* favorites = [self retrieveData:@"Select word_id from favorite"];
    NSString* wordid = (NSString*)[[words objectAtIndex:0] objectAtIndex:0];
    
    self.wordOfDay=[self addWord:[words objectAtIndex:0] Favorites:favorites];
    NSString* updateSql = [NSString stringWithFormat:@"Update EmailPlan set status='DONE' where word_id=%@",wordid];
    [self executeSQL:updateSql];
    //
    //Make it done in emailplan
}

- (NSString*) getColValue:(NSString*) sql {
    NSMutableArray* table = [self retrieve:sql];
    if ([table count] >0) {
        NSMutableArray* row = [table objectAtIndex:0];
        return (NSString*)[row objectAtIndex:0];
    }
    else return @"";
    
}

-(NSString*) getUser {
    //NSString* deviceName = [Utils getDeviceName];
    NSString* user=@"";
    NSMutableArray* userInfo = [self retrieve:@"Select email from User"];
    if ([userInfo count]>0) {
        user =[(NSArray*) [userInfo objectAtIndex:0] objectAtIndex:0];
    }
    //NSLog(@"User=%@",user);
    return user;
}

- (void) updateSubscription:(bool)subscription email:(NSString*)email {
    //NSLog(@"Update Subscription:%@ email:%@",subscription,email);
    [self executeSQL:@"Delete from User"];
    NSString* id = [NSString stringWithFormat:@"%d",[self getNewId:USER]];
    NSString* sql =[NSString stringWithFormat:@"Insert into User (id,email,subscription) values (%@,'%@','%i')",id,email,subscription];
    //NSLog(@"SQL:%@",sql);
    [self executeSQL:sql];
    //NSLog(@"User:%@",[self getUser]);
    
    [self downloadUserDataFromCloud];
}

- (NSMutableArray*) getSubscription{
    //NSLog(@"Update Subscription:%@ email:%@",subscription,email);
    NSMutableArray* subscriberInfo = [self retrieve:@"Select email,subscription from User"];
    if ([subscriberInfo count]>0) {
        return (NSMutableArray*) [subscriberInfo objectAtIndex:0];
    }
    return NULL;
}



-(void) updateFavorite:(Word*) word{
    NSString* sql = [NSString stringWithFormat:@"Select id from Word where word_simple='%@'",word.wordsimple];
    NSString* wordid = allTrim([self getColValue:sql]);
    NSString* verseid= @"1";
    NSString* type =@"W";
    //wordid = allTrim(wordid);
    if ([wordid length] > 0) {
        if (wordid!=nil) [self executeSQL:[@"Delete from Favorite where word_id =" stringByAppendingString:wordid]];
        if ([word.favorite isEqualToString:_YES]) {
            NSString* id = [NSString stringWithFormat:@"%d",[self getNewId:FAVORITE]];
            // NSString* userid = [self getUser];
            sql = [NSString stringWithFormat:@"Insert into Favorite (id,word_id,verse_id,type) values (%@,%@,%@,'%@')",id,wordid,verseid,type];
            //NSLog(@"Favorite SQL:%@",sql);
            [self executeSQL:sql];
        }
        for (UITableViewController<WODTableViewConroller>* controller in controllers) {
            [controller dataChanged];
        }
    }
}

-(void) updateFavorite:(bool) add chapterNo:(int) chapterNo verse:(int) verseNo {
    NSString* sql = [NSString stringWithFormat:@"Select id from Verse where chapter_no=%i and verse_no=%i",chapterNo,verseNo];
    NSLog(@"Favorite SQL1:%@",sql);
    NSString* verseid = allTrim([self getColValue:sql]);
    //wordid = allTrim(wordid);
    NSString* type =@"V";
    NSString* wordid= @"1";
    if ([verseid length] > 0) {
        [self executeSQL:[@"Delete from Favorite where verse_id =" stringByAppendingString:verseid]];
        if (add) {
            NSString* id = [NSString stringWithFormat:@"%d",[self getNewId:FAVORITE]];
            //NSString* userid = [self getUser];
            sql = [NSString stringWithFormat:@"Insert into Favorite (id,word_id,verse_id,type) values (%@,%@,%@,'%@')",id,wordid,verseid,type];
            //NSLog(@"Favorite SQL2:%@",sql);
            [self executeSQL:sql];
        }
    }
}




- (NSArray*) makeExamples:(Definition*)entityDefinition WordID:(NSString*) wordid {
    NSMutableArray* tokens = [[NSMutableArray alloc] init];
    NSMutableArray* tokens_meaning = [[NSMutableArray alloc] init];
    
    NSString* sql = [NSString stringWithFormat:@"Select T.chapter_no,T.verse_no,T.token,T.meaning from Segment S, Token T where word_id=%@ and S.chapter_no=T.chapter_no and S.verse_no=T.verse_no and S.token_no=T.token_no limit 10",wordid];
    NSMutableArray* occurences = [self retrieve:sql];
    if ([occurences count]>0) {
        for (int r=0; r< [occurences count]; r++) {
            NSMutableArray *row = (NSMutableArray *)[occurences objectAtIndex:r];
            NSString* chapter_no = (NSString*)[row objectAtIndex:0];
            NSString* verse_no = (NSString*)[row objectAtIndex:1];
            NSString* reference=[NSString stringWithFormat:@"(%@:%@)",chapter_no,verse_no];
            NSString* token = (NSString*)[row objectAtIndex:2];
            NSString* token_meaning = (NSString*)[row objectAtIndex:3];
            NSUInteger index = [tokens indexOfObject:token];
            if (index == NSNotFound) {
                [tokens addObject:token];
                [tokens_meaning addObject:token_meaning];
                NSString* example =[NSString stringWithFormat:@"%@  %@ %@",token,token_meaning,reference];
                Example*  entityExample = (Example *)[NSEntityDescription insertNewObjectForEntityForName:EXAMPLE inManagedObjectContext:managedObjectContext];
                entityExample.exampleOfDefinition=entityDefinition;
                entityExample.example=example;
                entityExample.reference=reference;
                [entityDefinition addExamplesObject:entityExample];
            }
            
        }
    }
    return tokens_meaning;
}


- (void) addExamples:(Definition*)entityDefinition DefinitionID:(NSString*)definitionid {
    //NSString* sql = [NSString stringWithFormat:@"Select e.id,example,reference from example e, definitionexample de where de.exampleid=e.id and de.meaningid=%@",definitionid];
    NSString* sql = [NSString stringWithFormat:@"Select id,example,reference from Example where meaning_id=%@ order by id",definitionid];
    NSMutableArray* examples = [self retrieve:sql];
    Example* entityExample;
    for (NSMutableArray *row in examples) {
        NSString* id = (NSString*)[row objectAtIndex:0];
        NSString* example = (NSString*)[row objectAtIndex:1];
        NSString* reference = (NSString*)[row objectAtIndex:2];
        entityExample = (Example *)[NSEntityDescription insertNewObjectForEntityForName:EXAMPLE inManagedObjectContext:managedObjectContext];
        entityExample.exampleOfDefinition=entityDefinition;
        entityExample.example=example;
        entityExample.reference=reference;
        [entityDefinition addExamplesObject:entityExample];
    }
}

- (void) addLanguage:(Definition*)entityDefinition LanguageID:(NSString*)languageid  {
    NSString* sql = [NSString stringWithFormat:@"Select id,name from Language where id=%@ ",languageid];
    NSMutableArray* languages = [self retrieve:sql];
    Language* entityLanguage;
    NSMutableArray* row = [languages objectAtIndex:0];
    NSString* id = (NSString*)[row objectAtIndex:0];
    NSString* name = (NSString*)[row objectAtIndex:1];
    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"name='%@'", name];
    NSArray* languages_ = [self fetchData:LANGUAGE SortKey:[NAME lowercaseString] ManagedObjectContext:managedObjectContext Predicate:predicate];
    if ([languages_ count] > 0) {
        entityLanguage = (Language *) [languages_ objectAtIndex:0];
    }
    else {
        entityLanguage = (Language *)[NSEntityDescription insertNewObjectForEntityForName:LANGUAGE inManagedObjectContext:managedObjectContext];
    }
    entityLanguage.name=name;
    //NSLog(@"LanguageId=%@ name=%@",id,name);
    entityDefinition.inLanguage=entityLanguage;
}




- (void) addVersesToWord:(Word*) entityWord {
    NSString* sql = [NSString stringWithFormat:@"Select s.id,chapter_no,verse_no from Word w, Segment s where w.word='%@' and w.id=s.word_id order by chapter_no,verse_no LIMIT 50",entityWord.word];
    NSDictionary* versesData = [self getAllVerses:sql];
    [self getVerses:versesData EntityWord:entityWord];
}


- (void) addVerseNosToWord:(Word*) entityWord {
    NSMutableArray* verseNosData=[self getVerseNosForWord:entityWord];
    for (NSMutableArray* row in verseNosData) {
        [self addVerseEntityIn:row toWordEntity:entityWord];
    }
    
}



- (void) addVerseEntityIn:(NSMutableArray*) row toWordEntity:(Word*) entityWord{
    Verse* entityVerse;
    NSNumber* chapterno = [NSNumber numberWithInt:[(NSString*)[row objectAtIndex:0] intValue]];
    NSNumber* verseno   = [NSNumber numberWithInt:[(NSString*)[row objectAtIndex:1] intValue]];
    NSString* text = [row objectAtIndex:2];
    NSString* translation = [row objectAtIndex:3];
    NSString* transliteration = [row objectAtIndex:4];
    //NSString* size = [row objectAtIndex:5];
    NSString* translation_split_word = [row objectAtIndex:5];
    NSMutableArray* grammar_split_word=[row count]==7?[row objectAtIndex:6]:nil;;
    //NSLog(@"translation_split_word=%@",translation_split_word);
    entityVerse = (Verse *)[NSEntityDescription insertNewObjectForEntityForName:VERSE inManagedObjectContext:managedObjectContext];
    entityVerse.has=entityWord;
    entityVerse.verse=text;
    entityVerse.chapterno=chapterno;
    entityVerse.verseno=verseno;
    entityVerse.translation=translation;
    entityVerse.transliteration=transliteration;
    entityVerse.translation_split_word=translation_split_word;
    entityVerse.verseMorphology=grammar_split_word;
    //entityVerse.size=size;
    //NSLog(@" Verse added to Word %i,%i",[chapterno intValue],[verseno intValue]);
    
}

- (void) getVerses:(NSDictionary*)verses EntityWord:(Word*)entityWord{
    
    for (NSString* key in verses) {
        NSMutableArray* row = (NSMutableArray*)[verses objectForKey:key];
        [self addVerseEntityIn:row toWordEntity:entityWord];
    }
    
}

- (void) addRelatedWords:(Word*)entityWord RootID:(NSString*)rootId {
    NSMutableArray* relatedWords = [self getRelatedWords:entityWord.word root:rootId];
    Word* entityRelatedWord;
    NSMutableSet* allRelatedWords = [[NSMutableSet alloc] init];
    for (int r=0; r< [relatedWords count]; r++) {
        NSMutableArray *row = (NSMutableArray *)[relatedWords  objectAtIndex:r];
        NSString* id = (NSString*)[row objectAtIndex:0];
        NSString* relatedWord  = (NSString*)[row objectAtIndex:1];
        NSString* sql = [NSString stringWithFormat:@"Select id,word,transliteration,tag,word_simple from Word where word='%@' Limit 1",relatedWord];
        NSMutableArray* words = [self retrieve:sql];
        if ([words count] > 0) {
            NSMutableArray *row = (NSMutableArray*) [words objectAtIndex:0];
            entityRelatedWord= [self addWord:row Favorites:nil];
            [allRelatedWords addObject:entityRelatedWord];
            
        }
    }
    entityWord.relatedWords=allRelatedWords;
    
}

- (NSString*) makeDefinition: (NSString*) wordid {
    NSString* sql = [NSString stringWithFormat:@"Select meaning_simple from Word where id=%@ order by id",wordid];
    NSString* meaning=[NSMutableString stringWithString:[self getColValue:sql]];
    return meaning;
    //return @"Meaning";
}

-(NSString*) getModeWord:(NSArray*) meanings{
    NSMutableDictionary* dict = [[NSMutableDictionary alloc] init];
    for (NSString* line in meanings) {
        NSArray *lineTokens = [line componentsSeparatedByString:@" "];
        for (NSString* wordRaw in lineTokens) {
            NSString* word = [[wordRaw componentsSeparatedByCharactersInSet:[[NSCharacterSet letterCharacterSet] invertedSet]] componentsJoinedByString:@""];
            NSNumber* number = [dict objectForKey:word];
            if (number==nil)
                number = [NSNumber numberWithInt:1];
            else {
                int value = [number intValue];
                number = [NSNumber numberWithInt:value + 1];
            }
            [dict setValue:number forKey:word];
        }
    }
    int maxValue=0;
    NSString* mode=@"";
    for (NSString* word in dict) {
        NSNumber* number = [dict objectForKey:word];
        int value = [number intValue];
        if (value > maxValue) {
            mode=word;
            maxValue=value;
        }
        else if (value==maxValue) {
            if ([word length]>[mode length]) mode=word;
        }
    }
    return mode;
}

- (void) addDefinitions:(Word*)entityWord WordID:(NSString*)wordid  {
    NSString* sql = [NSString stringWithFormat:@"Select id,meaning,language_id from Meaning where word_id=%@ order by id",wordid];
    NSMutableArray* meanings = [self retrieve:sql];
    Definition* entityDefinition;
    if ([meanings count]>0) {
        for (int r=0; r< [meanings count]; r++) {
            NSMutableArray *row = (NSMutableArray *)[meanings objectAtIndex:r];
            NSString* id = (NSString*)[row objectAtIndex:0];
            NSString* meaning = (NSString*)[row objectAtIndex:1];
            NSString* languageid = (NSString*)[row objectAtIndex:2];
            //NSLog(@"meaningid=%@ languageid=%@",id,languageid);
            entityDefinition = (Definition *)[NSEntityDescription insertNewObjectForEntityForName:DEFINITION inManagedObjectContext:managedObjectContext];
            entityDefinition.definesWord=entityWord;
            entityDefinition.meaning=meaning;
            //entityDefinition
            [self addLanguage:entityDefinition LanguageID:languageid];
            [self addExamples:entityDefinition DefinitionID:id];
        }
    }
    else {
        NSString* meaning =[self makeDefinition:wordid];
        NSString* languageid = @"1";
        entityDefinition = (Definition *)[NSEntityDescription insertNewObjectForEntityForName:DEFINITION inManagedObjectContext:managedObjectContext];
        entityDefinition.definesWord=entityWord;
        
        //entityDefinition
        [self addLanguage:entityDefinition LanguageID:languageid];
        NSArray* meanings = [self makeExamples:entityDefinition WordID:wordid];
        meaning = [meaning stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet] ];
        if ([meaning length]==0) {
            NSString* modeWord = [self getModeWord:meanings];
            modeWord =[Utils capitalizeFirstLetterOf:modeWord];
            entityDefinition.meaning=modeWord;
        }
        else {
            entityDefinition.meaning=[Utils capitalizeFirstLetterOf:meaning];
        }
    }
}


- (Word*) addWord:(NSMutableArray*)wordInfo Favorites:(NSDictionary*)favorites {
    Word* entityWord;
    NSString* id = (NSString*)[wordInfo objectAtIndex:0];
    NSString* word = (NSString*)[wordInfo objectAtIndex:1];
    NSString* transliteration = (NSString*)[wordInfo objectAtIndex:2];
    NSString* tag = (NSString*)[wordInfo objectAtIndex:3];
    NSString* wordsimple = (NSString*)[wordInfo objectAtIndex:4];
    
    
    entityWord = (Word *)[NSEntityDescription insertNewObjectForEntityForName:WORD inManagedObjectContext:managedObjectContext];
    entityWord.word=[word stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
    entityWord.wordsimple=[wordsimple stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
    entityWord.transliteration=transliteration;
    entityWord.tag= tag;
    NSMutableArray* rootInfo = [self getRoot:entityWord.word];
    entityWord.root= (NSString*)[rootInfo objectAtIndex:1];
    NSString* rootId = (NSString*)[rootInfo objectAtIndex:0];
    entityWord.lemma= [self getLemma:entityWord.word];
    entityWord.versecount= [NSNumber numberWithInt:[self getVersesCount:entityWord.word]];
    entityWord.relatedword=[self getRelatedWordsAsText:word root:rootId];
    
    //check for favorite
    entityWord.favorite=_NO;
    if (favorites!=nil) {
        NSString* isFavorite = (NSString*)[favorites objectForKey:id];
        if (isFavorite!=Nil) entityWord.favorite=_YES;
    }
    [self addDefinitions:entityWord WordID:id];
    [self addVerseNosToWord:entityWord];
    //[self addRelatedWords:entityWord:rootId];
    
    
    return entityWord;
}

- (int) getRootWordCountFor:(NSString*) rootWord {
    NSString* sql = [NSString stringWithFormat:@"Select count(*) as RootWordCount from Word where root_id = (Select id from Root where root='%@')",rootWord];
    NSMutableArray* words = [self retrieve:sql];
    NSMutableArray *row = [words objectAtIndex:0];
    return [(NSString *)[row objectAtIndex:0] intValue];
}


- (NSMutableArray*) getRootWordsFor:(NSString*) rootWord {
    NSString* sql = [NSString stringWithFormat:@"Select id,word,transliteration,tag,word_simple from Word where root_id = (Select id from Root where root='%@') LIMIT 15",rootWord];
    NSMutableArray* words = [self retrieve:sql];
    NSMutableArray* rootWords = [[NSMutableArray alloc] initWithCapacity:[words count]];
    for (NSMutableArray *row in words) {
        [rootWords addObject:[self addWord:row Favorites:nil]];
    }
    return rootWords;
}

- (void) addWords {
    NSString* sql = [NSString stringWithFormat:@"Select id,word,transliteration,tag,word_simple from Word where word_simple <> '%@' and id in (Select word_id from EmailPlan where status='%@')",self.wordOfDay.wordsimple,DONE];
    NSMutableArray* words = [self retrieve:sql];
    NSDictionary* favorites = [self retrieveData:@"Select word_id from favorite"];
    for (NSMutableArray *row in words) {
        [self addWord:row Favorites:favorites];
    }
}


- (NSMutableArray*) retrieve:(NSString*) querySQL{
    NSMutableArray *table = [[NSMutableArray alloc] init];
    //const char *dbpath = [databasePath UTF8String];
    sqlite3_stmt    *statement;
    int i=0;
    const char *query_stmt = [querySQL UTF8String];
    if (sqlite3_prepare_v2(wordDB, query_stmt, -1, &statement, NULL) == SQLITE_OK) {
        NSArray* colList = [self getColumnListFromResult:statement];
        while (sqlite3_step(statement) == SQLITE_ROW) {
            i++;
            const char *IdFieldChars = (const char *) sqlite3_column_text(statement, 0);
            NSString* idField;
            if (IdFieldChars ==nil)
                idField = @" ";
            else
                idField = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(statement, 0)];
            //NSString* idField = [ NSString stringWithFormat: @"%i",i];
            NSMutableArray* row = [[NSMutableArray alloc] init ];
            [row addObject:idField];
            for (int c=1; c < [colList count]; c++) {
                const char *fieldChars = (const char *) sqlite3_column_text(statement, c);
                NSString* field;
                if (fieldChars ==nil)
                    field = @" ";
                else
                    field = [[NSString alloc] initWithUTF8String:fieldChars];
                [row addObject:field];
            }
            //[dict setObject:row forKey:idField];
            [table addObject:row];
        }
    }
    sqlite3_finalize(statement);
    return table;
}


- (NSMutableArray*) retrieveColumn:(NSString*) querySQL{
    NSMutableArray *table = [[NSMutableArray alloc] init];
    //const char *dbpath = [databasePath UTF8String];
    sqlite3_stmt    *statement;
    int i=0;
    const char *query_stmt = [querySQL UTF8String];
    if (sqlite3_prepare_v2(wordDB, query_stmt, -1, &statement, NULL) == SQLITE_OK) {
        NSArray* colList = [self getColumnListFromResult:statement];
        while (sqlite3_step(statement) == SQLITE_ROW) {
            i++;
            const char *IdFieldChars = (const char *) sqlite3_column_text(statement, 0);
            NSString* idField;
            if (IdFieldChars ==nil)
                idField = @" ";
            else
                idField = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(statement, 0)];
            //NSString* idField = [ NSString stringWithFormat: @"%i",i];
            [table addObject:idField];
        }
    }
    sqlite3_finalize(statement);
    return table;
}

- (void) deleteAll: (NSArray*) mutableFetchResults {
    for (NSManagedObject *word in mutableFetchResults) {
        [managedObjectContext deleteObject:word];
    }
    NSError *error;
    if (![managedObjectContext save:&error]) {
        // Handle the error.
    }
    
}



-(NSArray*) fetchData:(NSString *) entityDescription SortKey:(NSString*) sortKey {
    return [self fetchData:entityDescription SortKey:sortKey ManagedObjectContext:managedObjectContext Predicate:nil];
}

-(NSArray*) fetchData:(NSString *) entityDescription SortKey:(NSString*) sortKey ManagedObjectContext: (NSManagedObjectContext*)thisManagedObjectContext Predicate: (NSPredicate*) predicate {
    NSFetchRequest *request = [[NSFetchRequest alloc] init];
    NSEntityDescription *entity = [NSEntityDescription entityForName:entityDescription inManagedObjectContext:thisManagedObjectContext];
    [request setEntity:entity];
    //Set Predicates
    if (predicate !=nil) [request setPredicate:predicate];
    
    //	// Order the events by creation date, most recent first.
    NSSortDescriptor *sortDescriptor = [[NSSortDescriptor alloc] initWithKey:sortKey ascending:NO];
    NSArray *sortDescriptors = [[NSArray alloc] initWithObjects:sortDescriptor, nil];
    [request setSortDescriptors:sortDescriptors];
    //[sortDescriptor release];
    
    // Execute the fetch -- create a mutable copy of the result.
    NSError *error = nil;
    NSMutableArray *mutableFetchResults = [[thisManagedObjectContext executeFetchRequest:request error:&error] mutableCopy];
    if (mutableFetchResults == nil) {
        // Handle the error.
    }
    return mutableFetchResults;
}

- (void)setWordList:(NSMutableArray *)newList {
    if (wordList != newList) {
        wordList = [newList mutableCopy];
    }
}

- (NSInteger)countOfList {
    return [wordList count];
}

/**
 Returns the managed object context for the application.
 If the context doesn't already exist, it is created and bound to the persistent store coordinator for the application.
 */
- (NSManagedObjectContext *) managedObjectContext {
    
    if (managedObjectContext != nil) {
        return managedObjectContext;
    }
    
    NSPersistentStoreCoordinator *coordinator = [self persistentStoreCoordinator];
    if (coordinator != nil) {
        managedObjectContext = [[NSManagedObjectContext alloc] init];
        [managedObjectContext setPersistentStoreCoordinator: coordinator];
    }
    
    
    return managedObjectContext;
}


/**
 Returns the managed object model for the application.
 If the model doesn't already exist, it is created by merging all of the models found in the application bundle.
 */
- (NSManagedObjectModel *)managedObjectModel {
    
    if (managedObjectModel != nil) {
        return managedObjectModel;
    }
    managedObjectModel = [NSManagedObjectModel mergedModelFromBundles:nil];
    
    
    return managedObjectModel;
}


- (BOOL) resetApplicationModel {
    
    // ----------------------
    // This method removes all traces of the Core Data store and then resets the application defaults
    // ----------------------
    
    //[[NSUserDefaults standardUserDefaults] setObject:[NSNumber numberWithBool:YES] forKey:kApplicationIsFirstTimeRunKey];
    
    NSError *_error = nil;
    NSURL *storeURL = [NSURL fileURLWithPath: [[self applicationDocumentsDirectory] stringByAppendingPathComponent: @"Tabster.sqlite"]];
    NSPersistentStore *_store = [persistentStoreCoordinator persistentStoreForURL:storeURL];
    
    //
    // Remove the SQL store and the file associated with it
    //
    if ([persistentStoreCoordinator removePersistentStore:_store error:&_error]) {
        [[NSFileManager defaultManager] removeItemAtPath:storeURL.path error:&_error];
    }
    
    if (_error) {
        NSLog(@"Failed to remove persistent store: %@", [_error localizedDescription]);
        NSArray *_detailedErrors = [[_error userInfo] objectForKey:NSDetailedErrorsKey];
        if (_detailedErrors != nil && [_detailedErrors count] > 0) {
            for (NSError *_detailedError in _detailedErrors) {
                NSLog(@" DetailedError: %@", [_detailedError userInfo]);
            }
        }
        else {
            NSLog(@" %@", [_error userInfo]);
        }
        return NO;
    }
    
    //[persistentStoreCoordinator release], persistentStoreCoordinator = nil;
    //[managedObjectContext release], managedObjectContext = nil;
    
    //
    // Rebuild the application's managed object context
    //
    //[self managedObjectContext];
    
    //
    // Repopulate Core Data defaults
    //
    //[self setupModelDefaults];
    
    return YES;
}


/**
 Returns the persistent store coordinator for the application.
 If the coordinator doesn't already exist, it is created and the application's store added to it.
 */
- (NSPersistentStoreCoordinator *)persistentStoreCoordinator {
    
    if (persistentStoreCoordinator != nil) {
        return persistentStoreCoordinator;
    }
    
    NSURL *storeUrl = [NSURL fileURLWithPath: [[self applicationDocumentsDirectory] stringByAppendingPathComponent: @"Tabster.sqlite"]];
    //NSLog(@"Absolute URL %@",[storeUrl absoluteString]);
    //[ storeURL];
    NSError *error;
    persistentStoreCoordinator = [[NSPersistentStoreCoordinator alloc] initWithManagedObjectModel: [self managedObjectModel]];
    if (![persistentStoreCoordinator addPersistentStoreWithType:NSSQLiteStoreType configuration:nil URL:storeUrl options:nil error:&error]) {
        // Handle the error.
    }
    
    return persistentStoreCoordinator;
}


#pragma mark -
#pragma mark Application's documents directory

/**
 Returns the path to the application's documents directory.
 */
- (NSString *)applicationDocumentsDirectory {
    
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *basePath = ([paths count] > 0) ? [paths objectAtIndex:0] : nil;
    return basePath;
}

- (NSString *)applicationCachesDirectory {
    
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES);
    NSString *basePath = ([paths count] > 0) ? [paths objectAtIndex:0] : nil;
    return basePath;
}


- (void) createTable: (NSString *) createSQL {
    char *errMsg;
    //const char *sql_stmt = "CREATE TABLE IF NOT EXISTS WORDS (ID INTEGER PRIMARY KEY AUTOINCREMENT, WORD TEXT, MEANING TEXT,REFERENCE TEXT)";
    const char *sql_stmt = [createSQL UTF8String];
    if (sqlite3_exec(wordDB, sql_stmt, NULL, NULL, &errMsg) == SQLITE_OK) {
        NSLog(@"Table created or found.");
    }
    else {
        if (errMsg==nil)  NSLog (@"Nil error") ;
        else {
            NSLog(@"SQL Error (Table not created) = %@", [NSString stringWithUTF8String:errMsg]);
        }
    }
}


-(BOOL) infoFound {
    BOOL success;
    NSError *error;
    
    NSFileManager *fileManager = [NSFileManager defaultManager];
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *filePath = [documentsDirectory stringByAppendingPathComponent:@"Info.plist"];
    
    success = [fileManager fileExistsAtPath:filePath];
    if (success) return YES; else return NO;
}

-(void) copyInfoPlist {
    BOOL success;
    NSError *error;
    
    NSFileManager *fileManager = [NSFileManager defaultManager];
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *filePath = [documentsDirectory stringByAppendingPathComponent:@"Info.plist"];
    NSString *path = [[NSBundle mainBundle] pathForResource:@"Info" ofType:@"plist"];
    [fileManager removeItemAtPath:filePath error:&error];
    success = [fileManager copyItemAtPath:path toPath:filePath error:&error];
    if (!success) {
        NSAssert1(0, @"Failed to copy Plist. Error %@", [error localizedDescription]);
    }
}

-(void) copyInfoPlist2 {
    NSError *error;
    BOOL success;
    NSFileManager *fileManager = [NSFileManager defaultManager];
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *filePath = [documentsDirectory stringByAppendingPathComponent:@"Info.plist"];
    
    success = [fileManager fileExistsAtPath:filePath];
    if (success) return;
    NSString* resourcePath = [[NSBundle mainBundle] pathForResource:@"info" ofType:@"plist"];
    NSString *path = [[[NSBundle mainBundle] resourcePath] stringByAppendingPathComponent:@"Info.plist"];
    success = [fileManager copyItemAtPath:path toPath:filePath error:&error];
    
    if (!success) {
        NSLog(@"Failed to copy Plist. Error %@", [error localizedDescription]);
    }
    else {
        NSLog(@"Successfully copied Plist.");
        
    }
}


- (void) openDatabase {
    bool forceCopy = NO;
    if ([self infoFound]==NO) {
        forceCopy=YES;
        [self copyInfoPlist];
    }
    else {
        NSString *docsDir;
        NSArray *dirPaths;
        dirPaths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
        docsDir = [dirPaths objectAtIndex:0];
        NSString* infoPath = [[NSString alloc] initWithString: [docsDir stringByAppendingPathComponent: @"Info.plist"]];
        
        NSFileManager* filemgr = [NSFileManager defaultManager];
        NSError *error;
        
        NSMutableDictionary *savedInfo = [[NSMutableDictionary alloc] initWithContentsOfFile: infoPath];
        float savedVersion = [(NSNumber*)[savedInfo objectForKey:@"Database version"] floatValue];
        
        NSDictionary* infoDict = [[NSBundle mainBundle] infoDictionary];
        float currentVersion =  [(NSNumber*)[infoDict objectForKey:@"Database version"]floatValue];
        
        if (savedVersion != currentVersion) {
            forceCopy= YES;
            [self copyInfoPlist];
            NSLog(@"*** Not same - Version No.:%f : Saved Version no %f",currentVersion,savedVersion);
        }
        else {
            //NSLog(@"*** Same Version No.:%@ : Saved Version no %@",currentVersion,savedVersion);
        }
    }
    //forceCopy=YES;
    const char *dbpath = [databasePath UTF8String];
    sqlite3_stmt    *statement;
    NSError *error;
    NSString *docsDir;
    NSArray *dirPaths;
    dirPaths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES);
    docsDir = [dirPaths objectAtIndex:0];
    //databasePath = [[NSString alloc] initWithString:@"/Users/wadood/words.db"];
    //NSLog(@"\n\nthe string %@",filePath);
    //databasePath = filePath;
    //NSString *docsDirectory = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
    //NSString *path = [docsDirectory stringByAppendingPathComponent:@"fileName.txt"];
    // Build the path to the database file
    databasePath = [[NSString alloc] initWithString: [docsDir stringByAppendingPathComponent: @"words.db"]];
    //NSLog(@"Path %@",databasePath);
    NSDictionary *favoriteTable,*userTable;
    NSFileManager* filemgr = [NSFileManager defaultManager];
    //[filemgr removeItemAtPath:databasePath error:&error];
    if (forceCopy || [filemgr fileExistsAtPath: databasePath ] == NO) {
        //Copy file from bundle
        if ([filemgr fileExistsAtPath: databasePath] == YES) {
            // copy user and favorite tables
            [self openDatabaseFile];
            favoriteTable= [self retrieveData:@"Select * from Favorite"];
            userTable= [self retrieveData:@"Select * from Subscriber"];
            //NSLog(@"Row Counts: %tu,%tu",[favoriteTable count],[userTable count]);
            [self closeDatabaseFile];
        }
        NSString* resourcePath = [[NSBundle mainBundle] pathForResource:@"words" ofType:@"db"];
        [filemgr removeItemAtPath:databasePath error:&error];
        if ([filemgr copyItemAtPath:resourcePath toPath:databasePath error:&error])  {
            NSLog(@"File successfully copied");
        }
        else {
            NSLog(@"Error description-%@ \n", [error localizedDescription]);
            NSLog(@"Error reason-%@", [error localizedFailureReason]);
        }
        
    }
    if ([filemgr fileExistsAtPath: databasePath ] == YES) {
        [self openDatabaseFile];
        [self addSkipBackupAttributeToItemAtURL:[NSURL fileURLWithPath:databasePath]];
        if (userTable!=nil) {
            [self copyData:@"Favorite" Table:favoriteTable];
            [self copyData:@"Subscriber" Table:userTable];
        }
    }
}

-(void) openDatabaseFile {
    const char *dbpath = [databasePath UTF8String];
    if (sqlite3_open(dbpath, &wordDB) != SQLITE_OK) {
        NSLog(@"Database not opened");
    }
    else {
        sqlite3_exec(wordDB, "PRAGMA foreign_keys = ON;", 0, 0, 0);
    }
    
}

-(void) closeDatabaseFile {
    if (sqlite3_close(wordDB) != SQLITE_OK) {
        NSLog(@"Database not closedopened");
    }
}



- (void) executeSQL: (NSString *) sql {
    char *errMsg;
    const char *sql_stmt = [sql UTF8String];
    if (sqlite3_exec(wordDB, sql_stmt, NULL, NULL, &errMsg) != SQLITE_OK) {
        if (errMsg==nil)  NSLog (@"Nil error"); else NSLog(@"SQL Error = %@", [NSString stringWithUTF8String:errMsg]);
    }
    else {
        //sqlite3_
    }
}


- (void) createTables {
    NSString *filePath = [[NSBundle mainBundle] pathForResource:@"Test" ofType:@"txt"];
    NSString *fileText = [NSString stringWithContentsOfFile:filePath encoding:NSUTF8StringEncoding error:nil];
    NSRange range = [fileText rangeOfString:@"CREATE"];
    NSMutableArray* ranges = [[NSMutableArray alloc] init];
    while (range.location != NSNotFound) {
        [ranges addObject:NSStringFromRange(range)];
        //NSRange aRange = NSRangeFromString((NSString*)[ranges lastObject]);
        //NSLog(@"Range is: %@ ", NSStringFromRange(aRange));
        range = NSMakeRange(range.location + range.length, [fileText length] - (range.location + range.length));
        range = [fileText rangeOfString:@"CREATE" options:0 range:range];
        
    }
    NSRange startRange, endRange;
    startRange = NSRangeFromString((NSString*) [ranges objectAtIndex:0]);
    for (int i=1; i < [ranges count]; i++) {
        endRange = NSRangeFromString((NSString*) [ranges objectAtIndex:i]);
        range = NSMakeRange(startRange.location, endRange.location-startRange.location);
        startRange = endRange;
        NSString* createSQL = [fileText substringWithRange:range];
        NSLog(@"Range is: %@ : %@", NSStringFromRange(range), createSQL);
        [self createTable:createSQL];
    }
    range = NSMakeRange(endRange.location, [fileText length] - endRange.location);
    NSString* createSQL = [fileText substringWithRange:range];
    NSLog(@"Range is: %@ : %@", NSStringFromRange(range), createSQL);
    [self createTable:createSQL];
    
}
-(int) getNewId:(NSString*) tableName {
    if ([[self getColValue:[@"Select Count(*) from "stringByAppendingString:tableName]] isEqualToString:@"0"]) return 1;
    NSMutableArray* row = (NSMutableArray*) [[self retrieve:[@"Select max(id)+1 as id from " stringByAppendingString:tableName]] objectAtIndex:0];
    return [(NSString*)[row objectAtIndex:0] intValue];
    
}

-( void) addWOD:(NSString*)fileName FileType:(NSString*)fileType UploadTargets:(int)uploadTargets {
    int wordid;
    int exampleid = [self getNewId:EXAMPLE];
    int definitionid = [self getNewId:MEANING];
    int emailplanid = [self getNewId:EMAIL_PLAN];
    
    
    
    //NSLog(@"%@,%@,%@",wordid,definitionid,exampleid);
    
    NSString *filePath = [[NSBundle mainBundle] pathForResource:fileName ofType:fileType];
    NSMutableDictionary* wod = [[NSMutableDictionary alloc] initWithContentsOfFile:filePath];
    NSString* word = [wod objectForKey:WORD];
    wordid = [[self getColValue:[NSString stringWithFormat:@"Select id from word where Word='%@'",word]] intValue];
    NSLog(@"%d,%d,%d",wordid,definitionid,exampleid);
    if (1==1) return;
    NSString* audiofile = [wod objectForKey:AUDIO_FILE];
    NSString* plandate = [wod objectForKey:PLAN_DATE];
    NSString* wordidstr = [NSString stringWithFormat:@"%d",wordid];
    NSString* emailplanidstr = [NSString stringWithFormat:@"%d",emailplanid];
    NSString* emailPlanSQL = [NSString stringWithFormat:@"Insert into emailplan (Id,PlanDate,WordId,Status) values (%@,'%@',%@,'%@')",emailplanidstr,plandate,wordidstr,NEW];
    if (uploadTargets>0) {
        if (uploadTargets<=2) {
            NSMutableArray* row = [NSMutableArray arrayWithObjects:emailplanidstr,plandate,wordidstr,NEW,nil];
            NSMutableArray* colList = [NSMutableArray arrayWithObjects:ID,PLAN_DATE,WORD_ID,STATUS,nil];
        }
        if (uploadTargets>=2) {
            [self executeSQL:emailPlanSQL];
        }
    }
    //NSLog(@"Word:%@",wordSQL);
    
    NSArray* meanings = [wod objectForKey:MEANINGS];
    for (NSDictionary* meaningEntry in meanings) {
        NSString* meaning = [meaningEntry objectForKey:MEANING];
        NSString* language = [meaningEntry objectForKey:LANGUAGE];
        NSString* languageid = [self getColValue:[NSString stringWithFormat:@"Select id from language where name='%@'",language]];
        NSString* definitionidstr = [NSString stringWithFormat:@"%d",definitionid];
        NSString* meaningSQL = [NSString stringWithFormat:@"Insert into meaning (Id,WordId,Meaning,LanguageId) values (%@,%@,'%@',%@)",definitionidstr,wordidstr,meaning,languageid];
        NSMutableArray* row = [NSMutableArray arrayWithObjects:definitionidstr,wordidstr,meaning,languageid,nil];
        NSMutableArray* colList = [NSMutableArray arrayWithObjects:ID,WORD_ID,MEANING,LANGUAGE_ID,nil];
        if (uploadTargets>0) {
            if (uploadTargets<=2) [dbCloud addRow:[MEANING lowercaseString] Row:row Key:definitionidstr ColList:colList];
            if (uploadTargets>=2) [self executeSQL:meaningSQL];
        }
        
        //NSLog(@"       ->:%@",meaningSQL);
        NSArray*  examples = (NSArray*)[meaningEntry objectForKey:EXAMPLES];
        for (NSDictionary* exampleEntry in examples) {
            NSString* example = [exampleEntry objectForKey:EXAMPLE];
            NSString* reference = [exampleEntry objectForKey:REFERENCE];
            NSString* exampleidstr = [NSString stringWithFormat:@"%d",exampleid];
            NSString* exampleSQL = [NSString stringWithFormat:@"Insert into example (Id,MeaningId,Example,Reference) values (%@,%@,'%@','%@')",exampleidstr,definitionidstr,example,reference];
            NSMutableArray* row = [NSMutableArray arrayWithObjects:exampleidstr,definitionidstr,example,reference,nil];
            NSMutableArray* colList = [NSMutableArray arrayWithObjects:ID,MEANING_ID,EXAMPLE,REFERENCE,nil];
            
            if (uploadTargets>0) {
                if (uploadTargets<=2) [dbCloud addRow:[EXAMPLE lowercaseString] Row:row Key:exampleidstr ColList:colList];
                if (uploadTargets>=2) [self executeSQL:exampleSQL];
            }
            //NSLog(@"      -------->%@",exampleSQL);
            exampleid++;
        }
        definitionid++;
    }
}






- (void) executeSQLFromFile:(NSString *)fileName FileType:(NSString*)fileType KeywordDelimiter:(NSString*)keywordDelimiter {
    NSString *filePath = [[NSBundle mainBundle] pathForResource:fileName ofType:fileType];
    NSString *fileText = [NSString stringWithContentsOfFile:filePath encoding:NSUTF8StringEncoding error:nil];
    NSRange range = [fileText rangeOfString:[NSString stringWithString:keywordDelimiter]];
    NSMutableArray* ranges = [[NSMutableArray alloc] init];
    while (range.location != NSNotFound) {
        [ranges addObject:NSStringFromRange(range)];
        //NSRange aRange = NSRangeFromString((NSString*)[ranges lastObject]);
        //NSLog(@"Range is: %@ ", NSStringFromRange(aRange));
        range = NSMakeRange(range.location + range.length, [fileText length] - (range.location + range.length));
        range = [fileText rangeOfString:[NSString stringWithString:keywordDelimiter] options:0 range:range];
        
    }
    NSRange startRange, endRange;
    startRange = NSRangeFromString((NSString*) [ranges objectAtIndex:0]);
    for (int i=1; i < [ranges count]; i++) {
        endRange = NSRangeFromString((NSString*) [ranges objectAtIndex:i]);
        range = NSMakeRange(startRange.location, endRange.location-startRange.location);
        startRange = endRange;
        NSString* sql = [fileText substringWithRange:range];
        NSLog(@"Range is: %@ : %@", NSStringFromRange(range), sql);
        [self executeSQL:sql];
    }
    range = NSMakeRange(endRange.location, [fileText length] - endRange.location);
    NSString* sql = [fileText substringWithRange:range];
    //NSLog(@"Range is: %@ : %@", NSStringFromRange(range), sql);
    [self executeSQL:sql];
    
}

- (NSDictionary*) retrieveData:(NSString*) querySQL{
    NSMutableDictionary *dict = [[NSMutableDictionary alloc] init];
    //const char *dbpath = [databasePath UTF8String];
    sqlite3_stmt    *statement;
    int i=0;
    const char *query_stmt = [querySQL UTF8String];
    if (sqlite3_prepare_v2(wordDB, query_stmt, -1, &statement, NULL) == SQLITE_OK) {
        NSArray* colList = [self getColumnListFromResult:statement];
        while (sqlite3_step(statement) == SQLITE_ROW) {
            i++;
            NSString* idField = [[NSString alloc] initWithUTF8String:(const char *) sqlite3_column_text(statement, 0)];
            
            NSMutableArray* row = [[NSMutableArray alloc] init ];
            for (int c=1; c < [colList count]; c++) {
                const char *fieldChars = (const char *) sqlite3_column_text(statement, c);
                NSString* field;
                if (fieldChars ==nil)
                    field = @" ";
                else
                    field = [[NSString alloc] initWithUTF8String:fieldChars];
                [row addObject:field];
            }
            [dict setObject:row forKey:idField];
        }
    }
    sqlite3_finalize(statement);
    return dict;
}

- (NSMutableArray*) retrieveDataWithoutKey:(NSString*) querySQL{
    NSMutableArray *table = [[NSMutableArray alloc] init];
    //const char *dbpath = [databasePath UTF8String];
    sqlite3_stmt    *statement;
    int i=0;
    const char *query_stmt = [querySQL UTF8String];
    if (sqlite3_prepare_v2(wordDB, query_stmt, -1, &statement, NULL) == SQLITE_OK) {
        NSArray* colList = [self getColumnListFromResult:statement];
        while (sqlite3_step(statement) == SQLITE_ROW) {
            i++;
            NSMutableArray* row = [[NSMutableArray alloc] init ];
            for (int c=0; c < [colList count]; c++) {
                const char *fieldChars = (const char *) sqlite3_column_text(statement, c);
                NSString* field;
                if (fieldChars ==nil)
                    field = @" ";
                else
                    field = [[NSString alloc] initWithUTF8String:fieldChars];
                [row addObject:field];
            }
            [table addObject:row];
        }
    }
    sqlite3_finalize(statement);
    return table;
}


-(BOOL)reachable {
    Reachability *r = [Reachability reachabilityWithHostName:@"enbr.co.cc"];
    NetworkStatus internetStatus = [r currentReachabilityStatus];
    if(internetStatus == NotReachable) {
        return NO;
    }
    return YES;
}


-(void) addLog:(NSUInteger) screenid  Settings:(NSString*) settings ChapterNo:(NSInteger) chapterNo VerseNo:(NSInteger) verseNo WordID:(NSInteger) wordid Place:(NSString*) place{
    NSString* timestamp=[Utils getTodaysDateInFormat:@"YYYY-MM-DD HH:MM:SS.SSS"];
    
    NSString* sql = [NSString stringWithFormat:@"Select * from Log Where screen_id=%zd Order by id desc LIMIT 1",screenid];
    //NSLog(@"(Add Log SQL=%@)",sql);
    NSMutableArray* rows = [self retrieve:sql];
    if ([rows count]>0) {
        NSString* logSQL = [NSString stringWithFormat:@" Update Log set hits=hits+%i,settings='%@',chapter_no=%tu, verse_no=%tu, word_id=%tu, time_stamp='%@' where screen_id=%zd",1, settings,chapterNo, verseNo, wordid, timestamp,screenid];
        //NSLog(@"Log Update=%@ (SQL=%@)",place,logSQL);
        [self executeSQL:logSQL];
    }
    else {
        int logid =[self getNewId:LOG];
        
        
        NSString* logSQL = [NSString stringWithFormat:@" INSERT INTO Log (id, screen_id, hits,settings,chapter_no, verse_no, word_id, time_stamp)  values (%i,%tu,%i,'%@',%tu,%tu,%tu,'%@') ",logid, screenid,1, settings,chapterNo, verseNo, wordid, timestamp];
        //NSLog(@"Place=%@ (SQL=%@)",place,logSQL);
        [self executeSQL:logSQL];
    }
    //[self executeSQL:@"commit"];
    
    
}


-(NSMutableArray*) getLastScreenFor:(NSInteger) screenID{
    //if (true) return nil;
    NSString* sql;
    if (screenID==-1) sql = [NSString stringWithFormat:@"Select * from Log Order by id desc LIMIT 1"];
    else sql = [NSString stringWithFormat:@"Select * from Log Where screen_id=%zd Order by id desc LIMIT 1",screenID];
    //NSLog(@"(Last Screen SQL=%@)",sql);
    NSMutableArray* rows = [self retrieve:sql];
    if (rows!=nil && [rows count]>0) {
        NSMutableArray* row = [rows objectAtIndex:0];
        return row;
    }
    else return nil;
}



-(NSString*) getWordID:(Word*) word{
    NSString* sql = [NSString stringWithFormat:@"Select id from Word where word_simple='%@'",word.wordsimple];
    NSString* wordid = allTrim([self getColValue:sql]);
    return wordid;
}








- (void) loadDataFromFile {
    [self openDatabase];
    [self executeSQLFromFile:@"data" FileType:@"txt" KeywordDelimiter:@"INSERT"];
    //[self executeSQLFromFile:@"Reset":@"txt":@"DELETE"];
    
    
    //NSString* createSQL = [fileText substringWithRange:range];
    //[ranges addObject:range];
    //NSLog(@"Range is: %@ : %@", NSStringFromRange(range), createSQL);
    
    
}


@end


