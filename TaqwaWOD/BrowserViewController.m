//
//  BrowserViewController.m
//  TaqwaWOD
//
//  Created by Wadood Chaudhary on 6/20/12.
//  Copyright (c) 2012 Instinet. All rights reserved.
//

#import "BrowserViewController.h"
#import "AppDelegate.h"
#import "VerseViewController.h"
#import <QuartzCore/QuartzCore.h>
#import "FlatButton.h"
#import "Utils.h"


@interface BrowserViewController () {
@private
    UIActivityIndicatorView *spinner;
    NSString *nibName,*nibNameCurrent;
    UINib *nibLandscape;
    UINib *nibPortrait;
}
@end

@implementation  BrowserViewController
@synthesize  versePickerView;
@synthesize browseToVerse;
@synthesize switchArabic,switchSplitWord,switchTranslation,switchTransliteration,buttonFrame;


- (id)init {
    self = [super init];
    //NSLog(@" Browser View Controller Init");
    
    if (self) {
        
        [self populateChapters];
        // Custom initialization
           }
    return self;
}

- (void)awakeFromNib {
    //NSLog(@"[BrowserViewController] awakeFromNIB:%@",self.nibName);
    if ([nibName length]==0){
       nibName =self.nibName;
       nibNameCurrent=nibName;
    }
    //[self populateChapters];
    //[versePickerView removeFromSuperview];
    //[self addButton];
    //[self reset];
}


- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    //NSLog(@" Browser View Controller InitWithNibName");
    if (self) {
        // Custom initialization
        
    }
    return self;
}

-(void) addButtons {
      //NSLog(@"goButtonRect:%@ buttonFrame:%@ : picker%@", NSStringFromCGRect(frameRect),NSStringFromCGRect(buttonFrame.frame),NSStringFromCGRect(versePickerView.frame));
    if (SYSTEM_VERSION_GREATER_THAN(@"7.0")) {

    goButton = [[FlatButton alloc] initWithFrame:buttonBrowse1.frame withBackgroundColor:[Utils getGreen]];
    goButton.layer.cornerRadius = 10;
    [goButton setTitle:buttonBrowse1.titleLabel.text forState:UIControlStateNormal];
    goButton.titleLabel.font = buttonBrowse1.titleLabel.font;
    [goButton addTarget:self action:@selector(buttonTouchDown:) forControlEvents:UIControlEventTouchUpInside];
    [goButton setImage:buttonBrowse1.imageView.image forState:UIControlStateNormal];
        
    }
    else {
        goButton = [[FlatButton alloc] initWithFrame:CGRectMake(buttonBrowse1.frame.origin.x+7,buttonBrowse1.frame.origin.y+24,buttonBrowse1.frame.size.width,buttonBrowse1.frame.size.height) withBackgroundColor:[UIColor grayColor]];
        [goButton setTitle:buttonBrowse1.titleLabel.text forState:UIControlStateNormal];
        goButton.titleLabel.font = buttonBrowse1.titleLabel.font;
        [goButton addTarget:self action:@selector(buttonTouchDown:) forControlEvents:UIControlEventTouchUpInside];
        [goButton setImage:buttonBrowse1.imageView.image forState:UIControlStateNormal];
        CGRect r = versePickerView.frame;
        //[versePickerView setFrame:CGRectMake(r.origin.x,r.origin.y-100,r.size.width-30,r.size.height)];
    }
    [buttonFrame addSubview:goButton];
    buttonBrowse1.hidden=YES;
    returnToLastButton = [[UIBarButtonItem alloc] initWithTitle:@"Go" style:UIBarButtonItemStyleBordered target:self action:@selector(buttonTouchDown:)];
    
    
    
}

-(void) populateChapters {
    AppDelegate * theDelegate = (AppDelegate *) [UIApplication sharedApplication].delegate;
    //if (theDelegate==nil) NSLog(@"Delegate not found!");
    dataController = theDelegate.dataController;
    chapters = [dataController getAllChapters];
    
}

- (void)viewDidLoad {
    
    //NSLog(@"[BrowserViewController] ViewDidLoad");
    //[versePickerView removeFromSuperview];
    //[buttonFrame removeFromSuperview];
    [self addButtons];
    
    //[self reset];
    [self populateChapters];
    //[self.view addSubview:versePickerView];
    //[self.view addSubview:buttonFrame];
    
    [super viewDidLoad];
    [switchSplitWord setOn:NO];
    [switchArabic setOn:YES];
    [switchTranslation setOn:YES];
    [switchTransliteration setOn:NO];
    UISwipeGestureRecognizer *gestureLeft = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(didSwipe:)];
    gestureLeft.direction = UISwipeGestureRecognizerDirectionLeft;
    [self.view addGestureRecognizer:gestureLeft];
    
    //
    UISwipeGestureRecognizer *gestureRight = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(didSwipe:)];
    gestureRight.direction = UISwipeGestureRecognizerDirectionRight;
    [self.view addGestureRecognizer:gestureRight];
    
    //buttonBrowse1.layer.borderWidth=1.0f;
    //buttonBrowse1.layer.borderColor=[[UIColor grayColor] CGColor];
     buttonBrowse1.hidden=YES;
//    UIButton* flatButton = [[FlatButton alloc] initWithFrame:buttonBrowse1.frame
//                                         withBackgroundColor:[UIColor colorWithRed:0.521569
//                                                                             green:0.768627
//                                                                              blue:0.254902 alpha:1]];
    
    
    //switchArabic.onTintColor = green;
    //switchSplitWord.onTintColor = green;
    //switchTranslation.onTintColor = green;
    //switchTransliteration.onTintColor = green;
    //
    [self initDisplay];
    NSString* title = [NSString stringWithFormat:@"<= Return to (%i:%i)",chapterNo,verseNo];
    if (chapterNo==0) title=@"";
    CGSize textSize = [Utils getTextSize:1.0 forText:title fontToSize:[UIFont fontWithName:@"Avenir-Black" size:16] rectSize:versePickerView.frame.size];
//    frameRect = CGRectMake(5,5,textSize.width+50,goButton.frame.size.height);
//    //NSLog(@"goButtonRect:%@ buttonFrame:%@ : picker%@", NSStringFromCGRect(frameRect),NSStringFromCGRect(buttonFrame.frame),NSStringFromCGRect(versePickerView.frame));
//    goToLastButton = [[FlatButton alloc] initWithFrame:frameRect withBackgroundColor:green];
//    goToLastButton.layer.cornerRadius = 8;
//    [goToLastButton setTitle:title forState:UIControlStateNormal];
//    goToLastButton.titleLabel.font = [UIFont fontWithName:@"Avenir-Black" size:16.0f];
//    [goToLastButton addTarget:self action:@selector(buttonTouchDown:) forControlEvents:UIControlEventTouchUpInside];
//    goToLastButton.contentHorizontalAlignment = UIControlContentHorizontalAlignmentLeft;
//    //[self.view addSubview:goToLastButton];
//    
    //lastButton.title=@"Title";
    
    self.navigationItem.leftBarButtonItem = returnToLastButton;
    self.navigationItem.leftBarButtonItem.enabled = YES;
    
    self.title=@"Quran";
    //
    spinner = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleGray];
    spinner.center = CGPointMake(160, 240);
    spinner.tag = 12;
    spinner.hidesWhenStopped=YES;
    [self.view addSubview:spinner];
    //[self reset];
}


-(void) reset{
    
    UIDeviceOrientation deviceOrientation = [UIDevice currentDevice].orientation;
    NSString* landscape=[NSString stringWithFormat:@"%@Landscape",nibName];
    NSString* portrait=[NSString stringWithFormat:@"%@", nibName];
    //NSLog(@"%@,%@,%@",nibName,landscape,portrait);
    if (UIDeviceOrientationIsLandscape(deviceOrientation)) {
        if (![nibNameCurrent isEqual:landscape]) {
            if (nibLandscape==nil) {
                if([[NSBundle mainBundle] pathForResource:landscape ofType:@"nib"] != nil) {
                    nibLandscape = [UINib nibWithNibName:landscape  bundle:nil];
                    [nibLandscape instantiateWithOwner:self options:nil];
                    nibNameCurrent=landscape;
                }
            }
            
        }
    }
    else {
        if (![nibNameCurrent isEqual:portrait]) {
            if (nibPortrait==nil) {
                if([[NSBundle mainBundle] pathForResource:portrait ofType:@"nib"] != nil) {
                    nibLandscape = [UINib nibWithNibName:portrait  bundle:nil];
                    [nibLandscape instantiateWithOwner:self options:nil];
                    nibNameCurrent=portrait;
                }
            }
        }
    }
//    if (true) return;
//    CGRect rect = [Utils getScreenSize];
//    float y=50;
//    UIDeviceOrientation deviceOrientation = [UIDevice currentDevice].orientation;
//    float heightFluff=(rect.size.height-(versePickerView.frame.size.height+buttonFrame.frame.size.height-goButton.frame.size.height))/4;
//    float widthFluff=(rect.size.width-(versePickerView.frame.size.width+goButton.frame.size.width))/2;
//    if (UIDeviceOrientationIsLandscape(deviceOrientation)) {
//        [versePickerView setFrame:CGRectMake(0,0,versePickerView.frame.size.width,216)];
//        buttonFrame.frame = CGRectMake(versePickerView.frame.size.width,0,buttonFrame.frame.size.width, buttonFrame.frame.size.height);
//        goButton.frame=CGRectMake(versePickerView.frame.size.width+goButton.frame.size.width/2,versePickerView.frame.size.height,goButton.frame.size.width,goButton.frame.size.height);
//        
//    }
//    else {
//        
//        NSLog(@"(%f,%f)",widthFluff,heightFluff);
//        [versePickerView setFrame:CGRectMake(0,y+heightFluff,versePickerView.frame.size.width,216)];
//        //162.0, 180.0 and 216.0
//        buttonFrame.frame = CGRectMake(versePickerView.frame.size.width,y+heightFluff,buttonFrame.frame.size.width, buttonFrame.frame.size.height);
//        goButton.frame=CGRectMake(versePickerView.frame.size.width+buttonFrame.frame.size.width,versePickerView.frame.origin.y,goButton.frame.size.width,goButton.frame.size.height);
//        [Utils printRect:@"versePicker:" ForRect:versePickerView.frame];
//        [Utils printRect:@"buttonFrame:" ForRect:buttonFrame.frame];
//        [Utils printRect:@"goButton:" ForRect:goButton.frame];
//        
//    }
    //[self.view setContentMode:UIViewContentModeRedraw];
 
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation{
    return YES;//(interfaceOrientation == UIInterfaceOrientationPortrait);
}


- (void)didRotateFromInterfaceOrientation:(UIInterfaceOrientation)fromInterfaceOrientation {
    
    
}


- (void)viewWillAppear:(BOOL)animated {
    //[self initDisplay];
    [self refreshLastReadLocations];
    NSString* title = [NSString stringWithFormat:@"Return to (%i:%i) ",chapterNo,verseNo];
    [goToLastButton setTitle:title forState:UIControlStateNormal];
    [returnToLastButton setTitle:title];
    
    //NSLog(@"[BrowserViewController] ViewWillAppear");
    [[self navigationController] setNavigationBarHidden:NO animated:YES];
    [super viewWillAppear:true];
    if (spinner.isAnimating) [spinner stopAnimating];
    //[self reset];
}


- (void)viewDidUnload{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}


-(IBAction) buttonTouchDown:(id)sender{
    if (sender==goButton) {
        chapterNo = (int)[versePickerView selectedRowInComponent:0]+1;
        verseNo   = (int)[versePickerView selectedRowInComponent:1]+1;
        [self showQuran];
    }
    else {
        [self initDisplay];
        [self showQuran];

    }
}

-(void) refreshLastReadLocations{
    NSMutableArray* screen = [dataController getLastScreenFor:0];
    if (screen!=nil) {
        chapterNo = [[screen objectAtIndex:2] intValue];
        verseNo =   [[screen objectAtIndex:3] intValue];
        NSString* settings = [screen objectAtIndex:6];
        NSValue* _rect =[NSValue valueWithCGRect:CGRectMake(0,0,[[Utils substr:settings from:0 length:4] intValue],[[Utils substr:settings from:4 length:4] intValue])];
        switchArabic.on =[[Utils substr:settings from:10 length:1] boolValue];
        switchTranslation.on =[[Utils substr:settings from:11 length:1] boolValue];
        switchTransliteration.on =[[Utils substr:settings from:12 length:1] boolValue];
        switchSplitWord.on =[[Utils substr:settings from:13 length:1] boolValue];
        //
    
        
    }
}

-(void) initDisplay {
    switchArabic.on=true;
    switchTranslation.on=true;
    switchTransliteration.on=false;
    switchSplitWord.on=false;
    [self refreshLastReadLocations];
}

-(void) showQuran {
    //chapterNo=1;
    //verseNo=250;
    [self navigateToVerses];
    
}

-(NSMutableArray*) getDisplayFormat {
    NSValue* arabicOn = [NSNumber numberWithBool:switchArabic.isOn];
    NSValue* translationOn = [NSNumber numberWithBool:switchTranslation.isOn];
    NSValue* transliterationOn = [NSNumber numberWithBool:switchTransliteration.isOn];
    NSValue* translationSplitWordOn = [NSNumber numberWithBool:switchSplitWord.isOn];
    int bv = [(NSNumber*)arabicOn boolValue]+[(NSNumber*)translationOn boolValue]+[(NSNumber*)translationSplitWordOn boolValue]+[(NSNumber*)transliterationOn boolValue];
    if (bv==0) {
        switchArabic.on=YES;
        switchTranslation.on=YES;
        arabicOn     = [NSNumber numberWithBool:YES];
        translationOn = [NSNumber numberWithBool:YES];
    }
    NSMutableArray* displayFormat = [NSMutableArray arrayWithObjects:arabicOn,translationOn,translationSplitWordOn,transliterationOn, nil];
    
    return displayFormat;
}

-(void) navigateToVerses{
     [spinner startAnimating];
    //NSLog(@"[BrowserViewController] %i:%i",chapterNo,verseNo);
    NSMutableArray* displayFormat = [self getDisplayFormat];
   
    VerseViewController* verseViewController=[[VerseViewController alloc] initWithChapter:chapterNo verse:verseNo verseDisplayFormat:displayFormat controller:dataController NIB:@"VerseViewController"];
    verseViewController.hidesBottomBarWhenPushed = YES;
    verseViewController.title=@"Quran";
    [verseViewController setCallingController:nil];
    //[[Utils getTabController] presentViewController:verseViewController animated:YES completion:nil];
    [self.navigationController pushViewController:verseViewController animated:YES];

    //[self presentViewController:verseViewController animated:YES completion:nil];
    //if (verseViewController != nil) [self presentViewController:verseViewController animated:YES completion:nil];
}



-(void)didSwipe:(UISwipeGestureRecognizer *)gestureRecognizer {
    if (gestureRecognizer.state == UIGestureRecognizerStateEnded) {
        if (gestureRecognizer.direction==UISwipeGestureRecognizerDirectionLeft) {
            //NSLog(@"[BrowserViewController].didSwipe Left");
            //[self.navigationController pushViewController:verseViewController animated:YES];
            //[self navigateToVerses];
            //[[Utils getTabController] setSelectedIndex:[[Utils getTabController] ]
            [Utils moveTab:self by:1];
    }
        else if (gestureRecognizer.direction==UISwipeGestureRecognizerDirectionRight) {
            //NSLog(@"[BrowserViewController].didSwipe Right");
            [Utils moveTab:self by:-1];
        }
    }
}



-(NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView{
    //One column
    return 2;
}

-(NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component{
    //set number of rows
    //NSLog(@"[BrowserViewController] numberOfRowsInComponent:%zd",component);
    
    if (component==0) {
        return [chapters count];
    }
    else if (component==1) {
        int chapterIndex = (int)[pickerView selectedRowInComponent:0];
        NSMutableArray* row = [chapters objectAtIndex:chapterIndex];
        int verseCount = [[row objectAtIndex:4] intValue];
        //NSLog(@"row:%i count:%i",chapterIndex,verseCount);
        return verseCount;
    }
    return 0;
}


-(NSString*) getSelectionFromPicker:(UIPickerView *)pickerView {
    chapterNo = (int)[pickerView selectedRowInComponent:0]+1;
    verseNo   = (int)[pickerView selectedRowInComponent:1];
}


-(NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component{
    //NSLog(@"[BrowserViewController] titleForRow:%zd Component:%zd",row,component);
    
    if (component==0) {
        NSMutableArray* chapterRow = [chapters objectAtIndex:row];
        NSString* chapterName = [NSString stringWithFormat:@"%zd: %@",row+1,[chapterRow objectAtIndex:2]];
        //NSLog(@"%i:%@",row,chapterName);
        return chapterName;
    }
    else if (component==1) {
        return [@"" stringByAppendingFormat:@"%zd",row+1];
    }
    return 0;
}

- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component{
    //NSLog(@"[BrowserViewController] didSelectRow");
    if (component==0) {
        [self.versePickerView selectRow:0 inComponent:1 animated:NO];
        [self.versePickerView reloadComponent:1];
    }
    
    
}




@end
