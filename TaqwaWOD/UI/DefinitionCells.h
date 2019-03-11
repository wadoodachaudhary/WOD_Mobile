//
//  DefinitionCells.h
//  Tabster
//
//  Created by Wadood Chaudhary on 4/19/12.
//  Copyright (c) 2012 Ahmadiyya Muslim Community - Taqwa Group. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Word.h"
#import "DictionaryDataController.h"
#import "WordViewController.h"
#import "WODTableViewConroller.h"

@interface DefinitionCells : UITableViewCell
@property (strong,nonatomic) IBOutlet UILabel  *definition;
@property (strong,nonatomic) IBOutlet UILabel  *refcount;
@property (strong,nonatomic) IBOutlet UILabel  *msgdisplaycount;
@property (strong,nonatomic) IBOutlet UILabel  *root;
@property (strong,nonatomic) IBOutlet UILabel  *relatedWord1;
@property (strong,nonatomic) IBOutlet UILabel  *relatedWord2;
@property (strong,nonatomic) IBOutlet UILabel  *relatedWord3;
@property (strong,nonatomic) IBOutlet UILabel  *relatedWord4;
@property (strong,nonatomic) IBOutlet UILabel  *relatedWord5;

@property (strong,nonatomic) IBOutlet UILabel  *rootword;
@property (strong,nonatomic) IBOutlet UILabel  *rootwordMeaning;
@property (strong,nonatomic) IBOutlet UILabel  *relatedWord;
@property (strong,nonatomic) IBOutlet UILabel  *relatedWordMeaning;
@property (strong,nonatomic) IBOutlet UILabel  *childrenwords;


@property (strong,nonatomic) IBOutlet UILabel *partsofspeech;
@property (strong,nonatomic) IBOutlet UILabel *word_entry;
@property (strong,nonatomic) IBOutlet UILabel *transliteration;
@property (strong,nonatomic) IBOutlet UILabel *example;
@property (strong,nonatomic) IBOutlet UILabel *hdrExample;
@property (strong,nonatomic) IBOutlet UILabel *hdrDef;
@property (strong,nonatomic) IBOutlet UILabel *hdrRef;

@property (strong,nonatomic) IBOutlet UILabel *verseIndex;
@property (strong,nonatomic) IBOutlet UILabel *minVerseIndex;
@property (strong,nonatomic) IBOutlet UILabel *maxVerseIndex;




@property (strong,nonatomic) IBOutlet UILabel *verseId;
@property (strong,nonatomic) IBOutlet UILabel *reference;

@property (strong,nonatomic) IBOutlet UIButton *favorite;
@property (strong,nonatomic) IBOutlet UIButton *audio;
@property (strong,nonatomic) IBOutlet UIButton *showall;
@property (strong,nonatomic) IBOutlet UIButton *btnRoot;

@property (strong,nonatomic) IBOutlet UISwitch* switchTranslationSplitWord;
@property (strong,nonatomic) IBOutlet UISwitch* switchTranslation;
@property (strong,nonatomic) IBOutlet UISwitch* switchArabic;

@property (strong,nonatomic) IBOutlet UISlider* sliderVerses;


- (IBAction) valueChangedSwitch: (id) aSwitch;
- (IBAction) valueChangedSlider: (UISlider *) verseSlider;

@property (strong,nonatomic) IBOutlet UITableView *verses;
@property (atomic,assign) int selectedBox;
@property (atomic,assign) Word* selectedWord;
@property (atomic,assign) UIViewController<WODTableViewConroller> *controller;
@property (strong, nonatomic) DictionaryDataController *dataController;



-(IBAction) buttonTouchDown:(id)sender;




@end
