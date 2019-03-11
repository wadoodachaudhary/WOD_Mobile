//
//  SearchByRoot.h
//  Tabster
//
//  Created by Wadood Chaudhary on 5/15/12.
//  Copyright (c) 2012 Ahmadiyya Muslim Community - Taqwa Group. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DictionaryDataController.h"

@interface SearchByRoot : UIViewController<UIPickerViewDelegate, UIPickerViewDataSource> {
    DictionaryDataController* dataController;
    NSMutableArray* letterIndex;
    NSMutableArray* letterIndex1;
    NSMutableArray* letterIndex2;
    NSMutableArray* letterIndex3;

    }
@property (strong,atomic) IBOutlet UIPickerView *rootPickerView;
@property (weak, nonatomic) IBOutlet UIView *displayView;
@property (strong,atomic) IBOutlet UILabel *rootSearchWord;
@property (weak, nonatomic) IBOutlet UIButton *buttonSearch;
- (IBAction)buttonTouchDown:(id)sender;

@end
