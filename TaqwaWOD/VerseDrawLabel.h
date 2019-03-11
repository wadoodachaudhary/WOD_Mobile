//
//  VerseDrawLabel.h
//  Tabster
//
//  Created by Wadood Chaudhary on 5/12/12.
//  Copyright (c) 2012 Ahmadiyya Muslim Community - Taqwa Group. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Foundation/Foundation.h>
#import "Word.h" 
#import "Constants.h"

@interface VerseDrawLabel : UILabel
- (id)initWithWord:(CGRect)frame SelectedWord:(NSString*)word;
@property (atomic,assign) NSString* word ;
@property (atomic,assign) NSString* transliteration;
@property (atomic,assign) NSString* translation;
@property (atomic,assign) NSString* translation_split_word;
@property (atomic,assign) enum TEXT_DIRECTION textDirection;
@property (atomic,assign) UIFont* translationFont;
@property (atomic,assign) UIFont* transliterationFont;
@property (atomic,assign) UIFont* fontToSize;
@property (atomic,assign) UIFont* translationSplitWordFont;
@property (atomic,assign) UIColor* mainTextColor;
@property (atomic,assign) UITextAlignment mainTextAlignment;
@property (atomic,assign) NSMutableArray* locWords;
@property (atomic,assign) NSMutableArray* grammarWords;

@end
