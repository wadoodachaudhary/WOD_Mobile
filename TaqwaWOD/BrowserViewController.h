//
//  BrowserViewController.h
//  TaqwaWOD
//
//  Created by Wadood Chaudhary on 6/20/12.
//  Copyright (c) 2012 Instinet. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DictionaryDataController.h"
#import "VerseViewController.h"

@interface BrowserViewController : UIViewController<UIPickerViewDelegate, UIPickerViewDataSource> {
    
    NSMutableArray* chapters;
    int chapterNo,verseNo;
    IBOutlet UIButton *buttonBrowse1;
    IBOutlet UIView *buttonFrame;
    @private
    UIButton *goButton,*goToLastButton;
    UIBarButtonItem* returnToLastButton;
    @private
    DictionaryDataController* dataController;
}

@property (strong, nonatomic) IBOutlet UIView *buttonFrame;
@property (strong,atomic) IBOutlet UIPickerView *versePickerView;
@property (strong,atomic) IBOutlet UILabel *browseToVerse;
@property (strong,nonatomic) IBOutlet UISwitch* switchTransliteration;
@property (strong,nonatomic) IBOutlet UISwitch* switchTranslation;
@property (strong,nonatomic) IBOutlet UISwitch* switchArabic;
@property (strong,nonatomic) IBOutlet UISwitch* switchSplitWord;

-(IBAction) buttonTouchDown:(id)sender;


@end
