    //
    //  Recent.m
    //  TaqwaWOD
    //
    //  Created by Wadood Chaudhary on 7/20/12.
    //  Copyright (c) 2012 Instinet. All rights reserved.
    //

#import "Recent.h"
#import "WordViewController.h"
#import "DictionaryDataController.h"
#import "Word.h"
#import "AppDelegate.h"
#import "Example.h"
#import "Utils.h"
@interface Recent () 

@end

@implementation Recent

@synthesize subLevel;
@synthesize dataController = _dataController;


- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
            // Custom initialization
    }
    return self;
}

- (void)viewDidLoad {
    NSError *error = nil;
    [super viewDidLoad];
    AppDelegate * theDelegate = (AppDelegate *) [UIApplication sharedApplication].delegate;
    if (theDelegate==nil) {
        UIAlertView *message  = [[UIAlertView alloc] initWithTitle:@"Value" message:@"Nil AppDelegate" delegate:nil cancelButtonTitle:@"OK"  otherButtonTitles:nil];
        [message show];
    }
    self.dataController    = theDelegate.dataController;
    
    NSMutableArray* words = [[NSMutableArray alloc] init];
    NSMutableArray* words_transliteration = [[NSMutableArray alloc] init];
    
    dataArray = [self.dataController dictionaryArray];
    for (Word *word in dataArray) {
        [words addObject:word.word];
        [words_transliteration addObject:word.transliteration];
    }
    
        //Add the search bar
    
    
    listOfItems = [[NSMutableArray alloc] init];
    [listOfItems addObjectsFromArray:words_transliteration];
    self.title=@"Search";
    //Swipe Gestures
    UISwipeGestureRecognizer *gestureLeft = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(didSwipe:)];
    gestureLeft.direction = UISwipeGestureRecognizerDirectionLeft;
    //[gestureLeft setDelegate:self.tableView];
    [self.tableView addGestureRecognizer:gestureLeft];
    UISwipeGestureRecognizer *gestureRight = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(didSwipe:)];
    gestureRight.direction = UISwipeGestureRecognizerDirectionRight;
    [self.tableView addGestureRecognizer:gestureRight];
    
    
}

- (void) valueChangedSwitch:(UISwitch*) aSwitch{}
- (void) valueChangedSlider:(UISlider*) aSlider{}

- (void)event:(enum EVENTS)evt Sender:(id)sender{
    
}


- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    NSIndexPath *tableSelection = [self.tableView indexPathForSelectedRow];
    [self.tableView deselectRowAtIndexPath:tableSelection animated:NO];
}


- (void)viewDidUnload {
    [super viewDidUnload];
        // Release any retained subviews of the main view.
        // e.g. self.myOutlet = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldRecognizeSimultaneouslyWithGestureRecognizer:(UIGestureRecognizer*) otherGestureRecognizer{
    return YES;
}


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    
    return 1;
}

    // Customize the number of rows in the table view.
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    return [listOfItems count];
    
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *CellIdentifier = @"Cell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
        cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        cell.selectionStyle = UITableViewCellSelectionStyleBlue;
    }
    Word* dict;
    dict= (Word*)[self findEntry:[listOfItems objectAtIndex:indexPath.row]];
    NSString *cellValue = [[@" (" stringByAppendingString:dict.transliteration] stringByAppendingString:@")    "];
    cell.textLabel.text = [cellValue stringByAppendingString:dict.word];
        //First get the dictionary object
    
    cell.textLabel.textAlignment = NSTextAlignmentRight;
    
    return cell;
}



- (NSIndexPath *)tableView :(UITableView *)theTableView willSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    return indexPath;
}


- (Word*) findEntry:(NSString*) transliteration {
    for (Word *word in dataArray) {
        if ([transliteration isEqualToString:word.transliteration]) {
            return word;
        }
    } 
    return nil;
}
// [Utils moveTab:self by:-1];


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
    
        //subLevel.title = cell.textLabel.text;
    Word* dict;
    dict= (Word*)[self findEntry:[listOfItems objectAtIndex:indexPath.row]];
    WordViewController *wordViewController = [[WordViewController alloc] initWithWord:dict atIndex:(int)indexPath.row onTitle:cell.textLabel.text controller:self.dataController NIB:@"WordViewController"];
    [wordViewController initialize];
    [wordViewController setCallingController:self];
    [[Utils getTabController] presentViewController:wordViewController animated:YES completion:nil];

    
}


-(void)didSwipe:(UISwipeGestureRecognizer *)gestureRecognizer {
    NSLog(@"[Recent]Swipe Methods: Left");
    if (gestureRecognizer.state == UIGestureRecognizerStateEnded) {
        if (gestureRecognizer.direction==UISwipeGestureRecognizerDirectionLeft) {
            [Utils moveTab:self by:1];
            NSLog(@"Swipe Methods: Left");

        }
        else if (gestureRecognizer.direction==UISwipeGestureRecognizerDirectionRight) {
            [Utils moveTab:self by:-1];
            NSLog(@"Swipe Methods: Right");

        }
    }
}











@end
