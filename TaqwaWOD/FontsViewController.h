//
//  FontsViewController.h
//  TaqwaWOD
//
//  Created by Wadood Chaudhary on 5/25/14.
//
//

#import <UIKit/UIKit.h>
#import "VerseViewController.h"

@interface FontsViewController : UIViewController<UITableViewDelegate, UITableViewDataSource>  {

//Data
    NSArray *familyNames;
    NSMutableDictionary *fontNamesForFamilyNames;
    UIFont* textFont;
    UIFont* translationFont;
//Views
    UITableView *tableView;
    VerseViewController* verseViewController;
    
}
- (id) initWithFonts:(NSMutableDictionary*) fonts ofFamily:(NSArray*) fontFamily withArabicFont:(UIFont*) arabicFont andWithEnglishFont:(UIFont*) englishFont byController:(VerseViewController*) verseViewController;

@end

