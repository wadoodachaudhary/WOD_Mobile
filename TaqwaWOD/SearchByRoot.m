//
//  SearchByRoot.m
//  Tabster
//
//  Created by Wadood Chaudhary on 5/15/12.
//  Copyright (c) 2012 Ahmadiyya Muslim Community - Taqwa Group. All rights reserved.
//

#import "SearchByRoot.h"
#import "AppDelegate.h"
#import "DictionaryDataController.h"
#import "VerseViewController.h"
#import "RootWord.h"
#import "FlatButton.h"
#import "Utils.h"

@interface SearchByRoot() {
@private
    NSString *nibName,*nibNameCurrent;
    UINib *nibLandscape;
    UINib *nibPortrait;
    UIButton* searchButton;
}
@end




@implementation SearchByRoot
@synthesize rootPickerView,rootSearchWord,buttonSearch,displayView;
//BOOL clicked=NO;
- (void)awakeFromNib {
    //NSLog(@"[Serach Root] awakeFromNIB:%@",self.nibName);
    if ([nibName length]==0){
        nibName =self.nibName;
        nibNameCurrent=nibName;
    }
    
}


- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
        nibName =self.nibName;
        nibNameCurrent=nibName;
        self.title=@"Root Search";
        
    }
    return self;
}

- (void)viewDidLoad {
    // Do any additional setup after loading the view from its nib.
    [super viewDidLoad];
    
    if (SYSTEM_VERSION_GREATER_THAN(@"7.0")) {
        searchButton = [[FlatButton alloc] initWithFrame:buttonSearch.frame withBackgroundColor:[Utils getGreen]];
        searchButton.layer.cornerRadius = 10;
        [searchButton setTitle:@"Search" forState:UIControlStateNormal];
        searchButton.titleLabel.font = [UIFont fontWithName:@"Avenir-Black" size:16.0f];
        [searchButton addTarget:self action:@selector(buttonTouchDown:) forControlEvents:UIControlEventTouchUpInside];
        [displayView addSubview:searchButton];
        buttonSearch.hidden=YES;
    }
    else {
        CGRect r = rootSearchWord.frame;
        searchButton = [[FlatButton alloc] initWithFrame:CGRectMake(r.origin.x,r.origin.y-15,r.size.width,r.size.height) withBackgroundColor:[UIColor grayColor]];
        searchButton.layer.cornerRadius = 15;
        [searchButton setTitle:@"Search" forState:UIControlStateNormal];
        searchButton.titleLabel.font = [UIFont fontWithName:@"Avenir-Black" size:20.0f];
        [searchButton addTarget:self action:@selector(buttonTouchDown:) forControlEvents:UIControlEventTouchUpInside];
        [displayView addSubview:searchButton];
        buttonSearch.hidden=YES;
        rootSearchWord.hidden=YES;
        
    }
    
    
    AppDelegate * theDelegate = (AppDelegate *) [UIApplication sharedApplication].delegate;
    if (theDelegate==nil) NSLog(@"Delegate not found!");
    dataController = theDelegate.dataController;
    [self getIndexes:[dataController getAllRootIndexes] index:0 startRow:0];
    //[self setDefaultsForLetter1:6 AndLetter2:1 AndLetter3:1]; //متخ
    [self setDefaultsForLetter1:0 AndLetter2:3 AndLetter3:2]; //لجا
    
    //[Utils printRect:@"displayView" ForRect:displayView.frame];
    //[Utils printRect:@"buttonSearch" ForRect:buttonSearch.frame];
    //[Utils printRect:@"searchButton" ForRect:searchButton.frame];
    //[Utils printRect:@"displayView" ForRect:displayView.frame];
    UISwipeGestureRecognizer *gestureLeft = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(didSwipe:)];
    gestureLeft.direction = UISwipeGestureRecognizerDirectionLeft;
    [self.view addGestureRecognizer:gestureLeft];
    
    //
    UISwipeGestureRecognizer *gestureRight = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(didSwipe:)];
    gestureRight.direction = UISwipeGestureRecognizerDirectionRight;
    [self.view addGestureRecognizer:gestureRight];
    self.hidesBottomBarWhenPushed=NO;
    self.navigationItem.hidesBackButton=NO;
    
    
    
    
    
}

-(void)didSwipe:(UISwipeGestureRecognizer *)gestureRecognizer {
    
    if (gestureRecognizer.state == UIGestureRecognizerStateEnded) {
        if (gestureRecognizer.direction==UISwipeGestureRecognizerDirectionLeft)
            [Utils moveTab:self by:-1];
        else if (gestureRecognizer.direction==UISwipeGestureRecognizerDirectionRight)
            [Utils moveTab:self by:1];
        
        
    }
}


-(BOOL) prefersStatusBarHidden {
    return NO;
}


-(IBAction) buttonTouchDown:(id)sender {
    int rootWordCount = [dataController getRootWordCountFor:rootSearchWord.text];
    if (rootWordCount>0) {
        RootWord *rootwordViewController = [[RootWord alloc] initWithRootWord:rootSearchWord.text controller:dataController NIB:@"RootWord"];
        [self.navigationController pushViewController:rootwordViewController animated:YES];
    }
    else {
        UIAlertView *message  = [[UIAlertView alloc] initWithTitle:@"Value" message:@"No verses found for the root." delegate:nil cancelButtonTitle:@"OK"  otherButtonTitles:nil];
        [message show];
    }
}

-(NSString*) getValueOf:(NSMutableArray*) _letterIndex atIndex:(int) index{
    id row =  [_letterIndex objectAtIndex:index];
    if ([row isKindOfClass:[NSMutableArray class]]) {
        return [row objectAtIndex:0];
    }
    else if ([row isKindOfClass:[NSString class]])
        return row;
    else {
        return @"Err not found";
    }
}


- (NSMutableArray *) getTestRootIndexes{
    NSMutableArray* rootIndexes = [NSMutableArray arrayWithCapacity:9];
    for (int i=0; i< 4;i++) {
        int jstart = i * 100;
        for (int j=jstart; j< jstart+2;j++) {
            int kstart = j * 100;
            for (int k=kstart; k< kstart+4;k++) {
                NSMutableArray* row = [NSMutableArray arrayWithCapacity:3];
                [row addObject:[NSString stringWithFormat:@"%i",i]];
                [row addObject:[NSString stringWithFormat:@"%i",j]];
                [row addObject:[NSString stringWithFormat:@"%i",k]];
                [rootIndexes addObject:row];
            }
        }
    }
    return rootIndexes;
    
}

- (void) getIndexes:(NSMutableArray*) rootIndexes index:(int) indexNo startRow:(int) r {
    //
    NSString *let1,*let2,*let3;
    NSString *_let1=@"",*_let2=@"",*_let3=@"";
    
    NSMutableArray* row = (NSMutableArray*) [rootIndexes objectAtIndex:r];
    _let1= (NSString*)[row objectAtIndex:indexNo];
    _let2= (NSString*)[row objectAtIndex:indexNo+1];
    _let3= (NSString*)[row objectAtIndex:indexNo+2];
    let1=_let1;let2=_let2;let3=_let3;
    do {
        //NSLog(@"(%i) %@,%@,%@",r,let1,let2,let3);
        if (![let1 isEqualToString:_let1]) {
            if (letterIndex1==nil) letterIndex1 = [[NSMutableArray alloc] init];
            if (letterIndex2==nil) letterIndex2 = [[NSMutableArray alloc] init];
            [letterIndex2 addObject:[NSMutableArray arrayWithObjects:_let2, letterIndex3, nil]];
            [letterIndex1 addObject:[NSMutableArray arrayWithObjects:_let1, letterIndex2, nil]];
            letterIndex2 = [[NSMutableArray alloc] init];
            letterIndex3 = [[NSMutableArray alloc] init];
            
            //letterindex2 is initialized
        }
        else if  (![let2 isEqualToString:_let2]) {
            if (letterIndex2==nil) letterIndex2 = [[NSMutableArray alloc] init];
            [letterIndex2 addObject:[NSMutableArray arrayWithObjects:_let2, letterIndex3, nil]];
            letterIndex3 = [[NSMutableArray alloc] init];
            //letterindex3 is initialized
        }
        if (letterIndex3==nil) letterIndex3 = [[NSMutableArray alloc] init];
        [letterIndex3 addObject:let3];
        _let1=let1;
        _let2=let2;
        
        r++;
        if (r < [rootIndexes count]) {
            NSMutableArray* row = [rootIndexes objectAtIndex:r];
            let1= [row objectAtIndex:indexNo];
            let2= [row objectAtIndex:indexNo+1];
            let3= [row objectAtIndex:indexNo+2];
        }
    } while (r < [rootIndexes count]);
    [letterIndex2 addObject:[NSMutableArray arrayWithObjects:_let2, letterIndex3, nil]];
    [letterIndex1 addObject:[NSMutableArray arrayWithObjects:_let1, letterIndex2, nil]];
    
    
    //    for (int i=0; i< [letterIndex1 count];i++) {
    //        NSMutableArray* i_row = [letterIndex1 objectAtIndex:i];
    //        NSLog(@"Letter Index i rowCount %i",[i_row count]);
    //        let1= [i_row objectAtIndex:0];
    //        letterIndex2= [i_row objectAtIndex:1];
    //        for (int j=0; j< [letterIndex2 count];j++) {
    //            NSMutableArray* j_row = [letterIndex2 objectAtIndex:j];
    //            let2= [j_row objectAtIndex:0];
    //            letterIndex3= [j_row objectAtIndex:1];
    //
    //            for (int k=0; k< [letterIndex3 count];k++) {
    //                let3= [letterIndex3 objectAtIndex:k];
    //                NSLog(@"(New) %@,%@,%@",let1,let2,let3);
    //
    //            }
    //        }
    //    }
    
    //return letterIndex1;
}

- (void)viewDidUnload{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation{
    NSLog(@"[SearchByRoot] Orientation message recieved.");
    return YES;//(interfaceOrientation == UIInterfaceOrientationPortrait);
}


- (void)didRotateFromInterfaceOrientation:(UIInterfaceOrientation)fromInterfaceOrientation {
    NSLog(@"[SearchByRoot] Rotate");
    //[self reset];
    //[self.view setContentMode:UIViewContentModeRedraw];
    //[self viewDidLoad];
    
    
}


- (void)viewWillAppear:(BOOL)animated {
    //[self initDisplay];
    NSLog(@"[SearchByRoot] ViewWillAppear");
    [[self navigationController] setNavigationBarHidden:NO animated:YES];
    //[[self navigationController] s setStatusBarHidden:NO animated:YES];
    
    [super viewWillAppear:true];
    //[self reset];
}



-(NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView{
    //One column
    return 3;
}

-(NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component{
    //set number of rows
    if (component==0) {
        return [letterIndex3 count];
    }
    else if (component==1) {
        return [letterIndex2 count];
    }
    else if (component==2) {
        return [letterIndex1 count];
    }
    return 0;
}

-(NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component{
    if (component==0) {
        return [letterIndex3 objectAtIndex:row];
    }
    else if (component==1) {
        NSMutableArray* letter_row = [letterIndex2 objectAtIndex:row];
        return [letter_row objectAtIndex:0];
    }
    else if (component==2) {
        NSMutableArray* letter_row = [letterIndex1 objectAtIndex:row];
        return (NSString*)[letter_row objectAtIndex:0];
        
    }
    return 0;
}



-(NSString*) getSelectionFromPicker:(UIPickerView *)pickerView {
    NSString* let1= [self getValueOf:letterIndex1 atIndex:(int)[pickerView selectedRowInComponent:2]];
    NSString* let2= [self getValueOf:letterIndex2 atIndex:(int)[pickerView selectedRowInComponent:1]];
    NSString* let3= [self getValueOf:letterIndex3 atIndex:(int)[pickerView selectedRowInComponent:0]];
    return [NSString stringWithFormat:@"%@%@%@",let1,let2,let3];
}

-(void) setDefaultsForLetter1:(int) index1 AndLetter2:(int) index2 AndLetter3:(int) index3 {
    NSMutableArray* i_row = [letterIndex1 objectAtIndex:index1];
    letterIndex2= [i_row objectAtIndex:1];
    NSMutableArray* j_row = [letterIndex2 objectAtIndex:index2];
    letterIndex3= [j_row objectAtIndex:1];
    
    [self.rootPickerView selectRow:index1 inComponent:2 animated:NO];
    [self.rootPickerView selectRow:index2 inComponent:1 animated:NO];
    [self.rootPickerView selectRow:index3 inComponent:0 animated:NO];
    rootSearchWord.text=[self getSelectionFromPicker:rootPickerView];
    NSString* title = [self getSelectionFromPicker:rootPickerView];
    [searchButton setTitle:title forState:UIControlStateNormal];
    
}

- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component{
    if (component==2) {
        NSMutableArray* i_row = [letterIndex1 objectAtIndex:row];
        letterIndex2= [i_row objectAtIndex:1];
        NSMutableArray* j_row = [letterIndex2 objectAtIndex:0];
        letterIndex3= [j_row objectAtIndex:1];
        [self.rootPickerView selectRow:0 inComponent:1 animated:NO];
        [self.rootPickerView selectRow:0 inComponent:0 animated:NO];
        [self.rootPickerView reloadComponent:1];
        [self.rootPickerView reloadComponent:0];
    }
    else if (component==1) {
        NSMutableArray* j_row = [letterIndex2 objectAtIndex:row];
        letterIndex3= [j_row objectAtIndex:1];
        [self.rootPickerView selectRow:0 inComponent:0 animated:NO];
        [self.rootPickerView reloadComponent:0];
    }
    rootSearchWord.text=[self getSelectionFromPicker:pickerView];
    NSString* title = [self getSelectionFromPicker:rootPickerView];
    [searchButton setTitle:title forState:UIControlStateNormal];
    
    
}

-(void) reset{
    UIDeviceOrientation deviceOrientation = [UIDevice currentDevice].orientation;
    NSString* landscape=[NSString stringWithFormat:@"%@Landscape",nibName];
    NSString* portrait=[NSString stringWithFormat:@"%@", nibName];
    NSLog(@"%@,%@,%@",nibName,landscape,portrait);
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
}



@end
