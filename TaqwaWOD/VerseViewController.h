//
//  VerseViewController.h
//  TaqwaWOD
//
//  Created by Wadood Chaudhary on 6/4/12.
//  Copyright (c) 2012 Instinet. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <QuartzCore/QuartzCore.h>
#import "DictionaryDataController.h"
#import "DefinitionCells.h"
#import "Verse.h"


@interface VerseViewController : UIViewController<UITableViewDelegate,UITableViewDataSource,WODTableViewConroller,UIGestureRecognizerDelegate>{
    IBOutlet UITableView* tableView; 
    IBOutlet UIScrollView* scrollView;
    IBOutlet UIView* popup;
    IBOutlet UIView* header;
    IBOutlet UIView* footer;
    IBOutlet UIButton* popupBack;
    IBOutlet UIButton* popupSplitWord;
    IBOutlet UIButton* popupMorphology;
    IBOutlet UIButton* popupSyntacticTree;
    IBOutlet UILabel* popupVerseHeader;
    IBOutlet UIView *leftSwipe, *rightSwipe;
    
    
    
}
- (id)initWithRootWord:(NSString*) rootWord controller:(DictionaryDataController*) rootDataController NIB:(NSString*) nibName;
- (id)initWithChapter:(int) chapterno verse:(int) verseno verseDisplayFormat:(NSMutableArray*) displayFormat controller:(DictionaryDataController*) rootDataController NIB:(NSString*) nibName;
- (id)initWithWord:(Word*) selectedWord controller:(DictionaryDataController*) wordDataController NIB:(NSString*) nibName;
- (void) event:(enum EVENTS)evt Sender:(id)sender;
- (void) setCallingController:(UIViewController*) viewController;
- (void) setFonts:(NSString*) arabicFont andEnglishFont:(NSString*) englishFont;
-(void)dismiss;

@property (strong,nonatomic) IBOutlet UISwitch* switchTransliteration;
@property (strong,nonatomic) IBOutlet UISwitch* switchTranslation;
@property (strong,nonatomic) IBOutlet UISwitch* switchArabic;
@property (strong,nonatomic) IBOutlet UILabel *sliderVerseIndex;
@property (strong,nonatomic) IBOutlet UILabel *sliderMinVerseIndex;
@property (strong,nonatomic) IBOutlet UILabel *sliderMaxVerseIndex;
@property (strong,nonatomic) IBOutlet UISlider* sliderVerses;


- (IBAction)handleSwipeFrom:(UISwipeGestureRecognizer *)recognizer;
- (IBAction) popupSelected:(id) sender;
- (IBAction) popupButtonClicked:(id) sender;
- (IBAction) valueChangedSwitch: (UISwitch*) aSwitch;
- (IBAction) valueChangedSlider: (UISlider *) verseSlider;
- (IBAction) valueChangedStepper: (UIStepper *) fontStepper;
- (IBAction) sliderTouchUp:(UISlider*) slider;




 @property (strong,nonatomic) IBOutlet UITableView *tableView;
@end
