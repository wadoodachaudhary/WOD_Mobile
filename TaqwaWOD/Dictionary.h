/*
 File: Dictionary.h
 Abstract: The view controller for page one.
 Version: 1.0
 
 
 
 */

#import <UIKit/UIKit.h>

@class DictionaryDataController;
@class WordViewController;
@class OverlayViewController;


@interface Dictionary : UITableViewController{
    IBOutlet UISearchBar *searchBar;
    BOOL searching;
    BOOL letUserSelectRow;
    OverlayViewController *ovController;
    NSMutableArray *copyListOfItems;
    NSMutableArray * listOfItems;
    NSString* searchArabicText;
    BOOL isArabic;
    @private
    //DictionaryDataController *dataController;
}


@property (nonatomic, retain) IBOutlet WordViewController *subLevel;
- (void) searchTableView:(NSString*) searchText;
- (void) doneSearching_Clicked:(id)sender;


@end






