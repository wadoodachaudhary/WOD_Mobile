//
//  Verse.h
//  Tabster
//
//  Created by Wadood Chaudhary on 5/13/12.
//  Copyright (c) 2012 Ahmadiyya Muslim Community - Taqwa Group. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>
#import "VerseDrawLabel.h"

@class Word;

@interface Verse : NSManagedObject {
    CGRect rect,rectText,rectTranslation,rectTransliteration,rectTranslationSplitWord;
}

@property (nonatomic, retain) NSString * translation;
@property (nonatomic, retain) NSString * transliteration;
@property (nonatomic, retain) NSString * translation_split_word;
@property (nonatomic, retain) NSString * verse;
@property (nonatomic, retain) NSString * size;
@property (nonatomic, retain) NSNumber * chapterno;
@property (nonatomic, retain) NSNumber * verseno;

@property (nonatomic, retain) Word *has;
@property int rowHeight;
@property (nonatomic, assign) CGRect rect;
@property (nonatomic, assign) CGRect rectText;
@property (nonatomic, assign) CGRect rectTranslation;
@property (nonatomic, assign) CGRect rectTransliteration;
@property (nonatomic, assign) CGRect rectTranslationSplitWord;

    //Non database properties
@property (atomic,retain) NSMutableArray* verseDrawArabic;
@property (atomic,retain) NSMutableArray* verseDrawTranslation;
@property (atomic,retain) NSMutableArray* verseDrawTranslationSplitWord;
@property (atomic,retain) NSMutableArray* verseDrawTransliteration;
@property (atomic,retain) NSMutableArray* verseMorphology;
@property (atomic,retain) NSMutableArray* verseSyntacticTree;



@end
