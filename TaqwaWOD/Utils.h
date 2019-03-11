    //
    //  Utils.h
    //  Tabster
    //
    //  Created by Wadood Chaudhary on 5/11/12.
    //  Copyright (c) 2012 Ahmadiyya Muslim Community - Taqwa Group. All rights reserved.
    //

#import <Foundation/Foundation.h>
#import "DefinitionCells.h"
#import "VerseDrawLabel.h"
#import "Word.h"
#import <mach/mach.h>
#import <mach/mach_host.h>


@interface Utils : NSObject
+ (NSMutableArray*) getTokens:(NSString*)text SearchString:(NSString*)searchString;
+ (NSRange)   findRange:(NSString*)text SearchString:(NSString*) searchString;
+(void) createImageOn:(DefinitionCells*) cellDefinition withimage:(NSString*) imageName atLocation : (CGRect) rect ;
+(void) createLabel:(DefinitionCells*)cellDefinition Text:(NSString*)text Rect:(CGRect)rect Font:(UIFont*)font TextAlignment:(NSTextAlignment)textAlignment TextColor:(UIColor*)textColor TextDirection:(enum TEXT_DIRECTION)textDirection WordToHighlight:(NSString*) wordToHighlight;
+(void) createLabel:(DefinitionCells*)cellDefinition Text:(NSString*) text TranslationSplitWord:(NSString*)translation_split_word Rect:(CGRect)rect Font:(UIFont*)font SplitWordFont:(UIFont*)splitWordFont TextAlignment:(NSTextAlignment)textAlignment TextColor:(UIColor*)textColor TextDirection:(enum TEXT_DIRECTION) textDirection WordToHighlight:(NSString*) wordToHighlight;
+ (void) start;
+ (NSString*) end;
+ (NSString*) getTodaysDateInFormat:(NSString*) format;
+ (void) addBorderTo:(UILabel*) lbl;
+ (natural_t) get_free_memory;
+ (void) showMessage:(NSString*)title Message:(NSString*)message;
+ (NSString*) capitalizeFirstLetterOf:(NSString*) _text;
+ (CGSize) getTextSize:(float) heightCushion forText:(NSString*)  text fontToSize:(UIFont*) font rectSize:(CGSize)rectSize;
+ (void) drawAtPoint:(NSString*) text atPoint:(CGPoint) point withFont:(UIFont*) font;
+ (void) drawAtPoint:(NSString*) text atPoint:(CGPoint) point insideRect:(CGRect) drawRect withFont:(UIFont*) font usingTextAttributes: (NSDictionary*) textAttributes inContext:(NSStringDrawingContext*) drawingContext;
+ (void) drawInRect:(NSString*) text insideRect:(CGRect) rect withFont:(UIFont*) font inColor:(UIColor*) color;
+ (CGRect) getScreenSize;
+ (BOOL) ifNull:(NSString*) str;
+ (void) clearArray:(NSMutableArray*) array;
+ (UIColor *)lighterColorForColor:(UIColor *)c ;
+ (UIColor *)darkerColorForColor:(UIColor *)c;
+ (UITabBarController*) getTabController;
+ (bool) isTabController:(UIViewController*) callingController;
+ (void) moveTab:(UIViewController*) callingController by:(int) incr;
+ (BOOL) isNumber:(NSString*) numberStr;
+ (DictionaryDataController*) getDataModel;
+ (NSString*) getDeviceName;
+ (void) downloadFont:(NSString*) fontName;
+ (void) displayAllFonts;
+(NSString*) substr:(NSString*) str from:(int) fromPos length:(int) lt;
+(void) printRect:(NSString*) title ForRect:(CGRect) rect;
+(void) printSize:(NSString*) title size:(CGSize) size;
+ (UIColor*) getGreen;
+(bool) isiPad;
+(bool) isRetina;
+(NSString*) trim:(NSString*) string;

@end
