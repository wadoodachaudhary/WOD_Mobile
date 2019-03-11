//
//  RootWord.h
//  Tabster
//
//  Created by Wadood Chaudhary on 5/19/12.
//  Copyright (c) 2012 Ahmadiyya Muslim Community - Taqwa Group. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "WODTableViewConroller.h"
#import "DictionaryDataController.h"

@interface RootWord : UITableViewController<WODTableViewConroller> {
@protected
    NSString* root;
    NSMutableString* allwords;
    NSMutableArray* pages,*pagesLocs;
    NSString* translation;
    DictionaryDataController* dataController;
    int chapter;
    Word* selectedWord;
    NSMutableArray* words;
    int rowcount,verseCount; 
}
    //- (void) event:(enum EVENTS)evt Sender:(id)sender;
- (id)initWithRootWord:(NSString*) rootWord controller:(DictionaryDataController*) _dataController NIB:(NSString*) nibName;
    //- (void)dataChanged;
    //- (void)valueChangedSwitch:(UISwitch*) aSwitch;
    //- (void)valueChangedSlider:(UISlider*) aSlider;

@end
