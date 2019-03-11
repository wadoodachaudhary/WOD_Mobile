//
//  Settings.h
//  Tabster
//
//  Created by Wadood Chaudhary on 5/1/12.
//  Copyright (c) 2012 Ahmadiyya Muslim Community - Taqwa Group. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DictionaryDataController.h"

@interface Subscribe : UIViewController <UITextViewDelegate>

@property (strong,nonatomic) IBOutlet UITextField* email;
@property (strong,nonatomic) IBOutlet UITextField* firstname;
@property (strong,nonatomic) IBOutlet UITextField* lastname;

@property (strong,nonatomic) IBOutlet UISwitch* subscribe;

-(IBAction) buttonTouchDown:(id)sender;
- (IBAction)dismissKeyboard: (id)sender;
- (id)initSignup:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil;
@property (strong, nonatomic) DictionaryDataController *dataController;
@property (weak, nonatomic) IBOutlet UIButton *buttonSave;
@property (weak, nonatomic) IBOutlet UIView *displayFrame;


@end
