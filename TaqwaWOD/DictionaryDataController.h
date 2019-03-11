//
//  DictionaryDataController.h
//  Tabster
//
//  Created by Wadood Chaudhary on 3/18/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#include <sqlite3.h>
#include <sys/xattr.h>
#import <CoreData/CoreData.h>
#import "Word.h"
#import "Verse.h"
#import "WODTableViewConroller.h"


@interface DictionaryDataController : NSObject {
    NSString *databasePath;
    sqlite3  *wordDB;
    NSMutableArray *wordList;
    NSManagedObjectContext *managedObjectContext;	
    NSPersistentStoreCoordinator *persistentStoreCoordinator;
    NSManagedObjectModel *managedObjectModel;

}

@property (nonatomic, assign) Word* wordOfDay;
@property (nonatomic, retain, readonly) NSManagedObjectModel *managedObjectModel;
@property (nonatomic, retain, readonly) NSManagedObjectContext *managedObjectContext;
@property (nonatomic, retain, readonly) NSPersistentStoreCoordinator *persistentStoreCoordinator;
@property (nonatomic, readonly) NSString *applicationDocumentsDirectory;
- (BOOL) resetApplicationModel;
- (void) updateFavorite:(Word*) word;
- (void) updateFavorite:(bool) add chapterNo:(int) chapterNo verse:(int) verseNo;

- (void) registerController:(UITableViewController<WODTableViewConroller> *) controller;
- (void) updateSubscription:(bool)subscription email:(NSString*)email;
- (NSDictionary*) getAllVerses:(NSString*) sql;
- (Word*) addVersesToChapter:(NSUInteger) chapterNo chapterName:(NSString*) chapterName;
- (NSMutableArray*) getAllRootIndexes;
- (NSMutableArray*) getFirstLettersOfAllRootWords;
- (NSMutableArray*) getRootWordsFor:(NSString*) rootWord;
- (NSMutableArray*) getAllChapterNames;
- (NSMutableArray*) getVerseNosInChapter:(int) chapterNo;
- (NSMutableDictionary*) getVersesInTranslatedWord:(NSString*) transWord;
- (NSMutableArray*) getVerseNosForRoot:(NSString*) root;
- (NSMutableArray*) getVerseNosForWord:(Word*) word;
- (Word*) getVerseAt:(NSUInteger) chapterNo VerseNo:(NSUInteger) verseNo;
- (NSMutableDictionary*) getVersesInChapter:(int) chapterNo;
- (int*) getVerseSizeFor: (Verse*) verse  WhenDiplayAttributesAre:(NSString*) displayAttributes;
- (void) updateVerseSizeFor:(Verse*) verse  WhenDiplayAttributesAre:(NSString*) displayAttributes AndHeight:(int) height;
- (NSMutableArray*) getWordsForRoot:(NSString*) root;
- (int) getRootWordCountFor:(NSString*) rootSearchWord;
- (NSString*) getRootMeaning:(NSString*) root;
- (NSMutableArray*) getSubscription;
- (int) getVerseCountForRoot:(NSString*) root;
- (NSMutableArray*) getAllChapters;
- (void) setProperty:(NSString*) key Value:(NSString*) value;
- (NSString*) getProperty:(NSString*) key;
- (NSMutableArray*) getSegment :(NSString*) wordPart;
- (NSMutableArray*) getRecentWords;
- (Word*) getWord :(NSString*) wordId;
- (Word*) getWord:(int) chapter_no VerseNo:(int) verse_no TokenNo:(int) token_no;
- (void) addLog:(NSUInteger) screenid Settings:(NSString*) settings ChapterNo:(NSInteger) chapterNo VerseNo:(NSInteger) verseNo WordID:(NSInteger) wordid  Place:(NSString*) place;
- (NSMutableArray*) getLastScreenFor:(NSInteger) screenID;
- (NSString*) getWordID:(Word*) word;
- (NSMutableArray*) getFavorites;
- (void) uploadUserDataToCloud;
- (NSString*) getUser;
- (NSMutableDictionary*) getVersesInRootWord:(NSString*) root;
- (NSMutableArray*) getWordsInRootWord:(NSString*) root;
- (void) getNewItemsFromCloud;
- (void) refreshWordOfDay;
- (void) downloadUserDataFromCloud;



@property (nonatomic, retain) NSArray *dictionaryArray;
@property (nonatomic, retain) NSArray *recentsArray;
@property (nonatomic, retain) NSArray *favoriteArray;
@property (nonatomic, retain) NSArray *dailyArray;
@property (nonatomic, retain) NSArray *word_exampleArray;
@property (nonatomic, retain) NSArray *examplesArray;

@end








