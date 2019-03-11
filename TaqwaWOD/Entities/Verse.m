//
//  Verse.m
//  Tabster
//
//  Created by Wadood Chaudhary on 5/13/12.
//  Copyright (c) 2012 Ahmadiyya Muslim Community - Taqwa Group. All rights reserved.
//

#import "Verse.h"
#import "Word.h"


@implementation Verse

@dynamic translation,transliteration;
@dynamic translation_split_word;
@dynamic verse;
@dynamic size;
@dynamic chapterno;
@dynamic verseno;
@dynamic has;


@synthesize verseDrawArabic,verseDrawTranslation,verseDrawTranslationSplitWord,verseMorphology,verseSyntacticTree,verseDrawTransliteration,rect,rectText,rectTranslation,rectTransliteration,rectTranslationSplitWord,rowHeight;

@end
