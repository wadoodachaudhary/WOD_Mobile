/*
 File: SubLevelViewController.h
 Abstract: The view controller for sublevel 2.
 Version: 1.0
 
 */

#import <UIKit/UIKit.h>
#import "Word.h"
#import "WODTableViewConroller.h"
#import "Constants.h"
#import "DictionaryDataController.h"
#import "DefinitionCells.h"
#import "VerseViewController.h"


@class ModalViewController;
@class VersesViewController;

@interface WordViewController : UITableViewController<WODTableViewConroller> { 
@private
    int rowCount;
    UIViewController* verseViewController;
    DictionaryDataController* dataController;
    NSMutableArray* rows;
    NSMutableArray* index;
    NSSet*  verses;
    NSArray* cellDefinition_nib;
    NSMutableArray* cellMain;
    UIViewController* verseViewer;
    UIViewController* callingController;
    CGRect screenRect;
    CGSize maximumLabelSize;
    float heightCushion;
    
}


@property (nonatomic, retain) NSString *currentSelectionTitle;
@property (atomic,assign) NSInteger selectedIndex;
@property (atomic,assign) Word* selectedWord;
- (void) event:(enum EVENTS)evt Sender:(id)sender;
- (void) initialize;
- (id)initWithWord:(Word*) _selectedWord atIndex:(int) _selectedIndex onTitle:(NSString*) _titleToDisplay controller:(DictionaryDataController*) wordDataController NIB:(NSString*) nibName;
- (void) setCallingController:(UIViewController*) viewController;
- (void) hideBackButton:(bool) yesOrNo;






@end