//
//  Word.h
//  Tabster
//
//  Created by Wadood Chaudhary on 4/20/12.
//  Copyright (c) 2012 Ahmadiyya Muslim Community - Taqwa Group. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class Meaning;

@interface Word : NSManagedObject

@property (nonatomic, retain) NSString * tag;
@property (nonatomic, retain) NSString * root;
@property (nonatomic, retain) NSString * lemma;
@property (nonatomic, retain) NSString * audiofile;
@property (nonatomic, retain) NSString * origin;
@property (nonatomic, retain) NSString * transliteration;
@property (nonatomic, retain) NSString * word;
@property (nonatomic, retain) NSString * wordsimple;
@property (nonatomic, retain) NSString * relatedword;
@property (nonatomic, retain) NSString * favorite;
@property (nonatomic, retain) NSNumber * versecount;
@property (nonatomic, retain) NSSet *definitions;
@property (nonatomic, retain) NSSet *verses;
@property (nonatomic, retain) NSSet *relatedWords;
@property (nonatomic, retain) NSDate * date;

@end

@interface Word (CoreDataGeneratedAccessors)

- (void)addDefinitionsObject:(Meaning *)value;
- (void)removeDefinitionsObject:(Meaning *)value;
- (void)addDefinitions:(NSSet *)values;
- (void)removeDefinitions:(NSSet *)values;
- (NSMutableSet*)primitiveDefinitions;
- (void)setPrimitiveDefinitions:(NSMutableSet*)value;
    //verses
- (void)addVersesObject:(Meaning *)value;
- (void)removeVersesObject:(Meaning *)value;
- (void)addVerses:(NSSet *)values;
- (void)removeVerses:(NSSet *)values;
- (NSMutableSet*)primitiveVerses;
- (void)setPrimitiveVerses:(NSMutableSet*)value; 
    //
    //relatedWords
- (void)addRelatedWordsObject:(Meaning *)value;
- (void)removeRelatedWordsObject:(Meaning *)value;
- (void)addRelatedWords:(NSSet *)values;
- (void)removeRelatedWords:(NSSet *)values;
- (NSMutableSet*)primitiveRelatedWords;
- (void)setPrimitiveRelatedWords:(NSMutableSet*)value; 


@end
