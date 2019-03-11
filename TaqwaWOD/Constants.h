//
//  Constants.h
//  Tabster
//
//  Created by Wadood Chaudhary on 4/24/12.
//  Copyright (c) 2012 Ahmadiyya Muslim Community - Taqwa Group. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Constants : NSObject

#define NUM_SECTIONS        7
#define RTL_CHAR @"\u200F"
#define LTR_CHAR @"\u200E"

#define NUM_SECTION_1_ITEMS 2
#define NUM_SECTION_2_ITEMS 14
#define NUM_SECTION_3_ITEMS 5

#define WORD        @"Word"
#define WORD_ID        @"word_id"
#define AUDIO_FILE  @"audiofile"
#define ORIGIN      @"origin"
#define TRANSLITERATION @"transliteration"
#define ROOT        @"Root"
#define LEMMA  @"Lemma"
#define TAG  @"Tag"


#define MEANING     @"Meaning"
#define MEANING_SIMPLE     @"Meaning_Simple"
#define ROOT_ID     @"Root_Id"


#define LANGUAGE    @"Language"
#define LANGUAGE_ID    @"language_id"
#define NAME          @"Name"
#define MEANING_ID     @"meaning_id"


#define EXAMPLE     @"Example"
#define REFERENCE   @"Reference"

#define SEGMENT @"Segment"

#define VERSE        @"Verse"
#define TRANSLATION  @"Translation"




#define SUBSCRIBER  @"Subscriber"


#define MEANINGS    @"Meanings"
#define EXAMPLES    @"Examples"
#define DEFINITION  @"Definition"
#define FAVORITE    @"Favorite"
#define USER    @"User"

#define LOG    @"Log"

#define _YES    @"YES"
#define _NO    @"NO"
#define ID    @"id"


#define PLAN_DATE    @"plandate"
#define EMAIL_PLAN    @"EmailPlan"
#define STATUS    @"Status"


#define NEW     @"NEW"
#define DONE    @"DONE"

#define IMG_FAV_ADD    @"bookmark_add_24.png"
#define IMG_FAV_RMV    @"tag_remove_24.png"
#define IMG_VIEW_TOP   @"chart_up_24.png"
#define IMG_VIEW_SIDE   @"chart_down_24.png"
#define IMG_FONT       @"font_24.png"
#define IMG_FONT_UP    @"font_up_24.png"
#define IMG_FONT_DOWN  @"font_down_24.png"

#define IMG_VIEW_COLS   @"viewcols.png"




#define SPLIT_WORD_DELIMITER @"||"


enum EVENTS {ShowVerse = 0, ShowRoot= 1, ShowRelatedWord=2,  SelectFavorite=3, PlayAudio=4, ValueChanged=5, ButtonTouch=6};
enum DISPLAY_BOX {EntryBox = 0, AttributesBox=2, DefinitionBox=3, ExampleBox = 4, VersesBox=5, RootWordBox=6, RelatedWordBox=7};
enum TEXT_DIRECTION {LTR = 0, RTL=1};
enum VIEW{ViewTop=0,ViewSideBySide=1};
enum VERSE_DISPLAY {VD_Chapter=0,VD_Verse=1,VD_Word=2,VD_TranslatedWord=3,VD_SplitWord=4,VD_Root=5};

#define allTrim( object ) [object stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
#define lower( object ) [object lowercaseString];
#define SYSTEM_VERSION_EQUAL_TO(v)                  ([[[UIDevice currentDevice] systemVersion] compare:v options:NSNumericSearch] == NSOrderedSame)
#define SYSTEM_VERSION_GREATER_THAN(v)              ([[[UIDevice currentDevice] systemVersion] compare:v options:NSNumericSearch] == NSOrderedDescending)
#define SYSTEM_VERSION_GREATER_THAN_OR_EQUAL_TO(v)  ([[[UIDevice currentDevice] systemVersion] compare:v options:NSNumericSearch] != NSOrderedAscending)
#define SYSTEM_VERSION_LESS_THAN(v)                 ([[[UIDevice currentDevice] systemVersion] compare:v options:NSNumericSearch] == NSOrderedAscending)
#define SYSTEM_VERSION_LESS_THAN_OR_EQUAL_TO(v)     ([[[UIDevice currentDevice] systemVersion] compare:v options:NSNumericSearch] != NSOrderedDescending)



















@end
