/*
 
 */

#import "Dictionary.h"
#import "WordViewController.h"
#import "DictionaryDataController.h"
#import "Word.h"
#import "AppDelegate.h"
#import "OverlayViewController.h"
#import "Example.h"
#import "Utils.h"

@class Word;

@interface Dictionary ()
- (Word*) findEntry: (NSString*) transliteration;
@property (nonatomic, retain) NSArray *dataArray;
@end

@implementation Dictionary


@synthesize subLevel, dataArray;

- (id)init {
    if (self = [super init]) {
        [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"AppleLanguages"];
        [[NSUserDefaults standardUserDefaults] setObject:[NSArray arrayWithObjects:@"ar",@"en", nil] forKey:@"AppleLanguages"];
    }
    return self;
}


- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    if (self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil]) {
        [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"AppleLanguages"];
        [[NSUserDefaults standardUserDefaults] setObject:[NSArray arrayWithObjects:@"ar",@"en", nil] forKey:@"AppleLanguages"];
        
    }
    return self;
}

- (void)viewDidLoad {
    self.title=@"Search";
    NSError *error = nil;
    [super viewDidLoad];
    AppDelegate * theDelegate = (AppDelegate *) [UIApplication sharedApplication].delegate;
    if (theDelegate==nil) {
        UIAlertView *message  = [[UIAlertView alloc] initWithTitle:@"Value" message:@"Nil AppDelegate" delegate:nil cancelButtonTitle:@"OK"  otherButtonTitles:nil];
        [message show];
    }
    //dataController    = theDelegate.dataController;
//    NSMutableArray* words = [[NSMutableArray alloc] init];
//    NSMutableArray* words_transliteration = [[NSMutableArray alloc] init];
//    dataArray = [self.dataController dictionaryArray];
//    for (Word *word in dataArray) {
//        [words addObject:word.word];
//        [words_transliteration addObject:word.transliteration];
//    }
        // Add the search bar
    isArabic=NO;
    self.tableView.tableHeaderView = searchBar;
    searchBar.autocorrectionType = UITextAutocorrectionTypeNo;
    searchArabicText=@"";
    searching = NO;
    letUserSelectRow = YES;
        //Swipe Gestures
    UISwipeGestureRecognizer *gestureLeft = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(didSwipe:)];
    gestureLeft.direction = UISwipeGestureRecognizerDirectionLeft;
    [self.view addGestureRecognizer:gestureLeft];
    UISwipeGestureRecognizer *gestureRight = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(didSwipe:)];
    gestureRight.direction = UISwipeGestureRecognizerDirectionRight;
    [self.view addGestureRecognizer:gestureRight];
}


- (void)viewDidUnload {
    [super viewDidUnload];
    self.dataArray = nil;
}

- (void)dealloc {
}

- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldRecognizeSimultaneouslyWithGestureRecognizer:(UIGestureRecognizer*) otherGestureRecognizer{
    return YES;
}



- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
    DictionaryDataController *dataController= [Utils getDataModel];
    listOfItems = [dataController getRecentWords];

    
        //searchBar.text=@"Help is required!";
    NSIndexPath *tableSelection = [self.tableView indexPathForSelectedRow];
    [self.tableView deselectRowAtIndexPath:tableSelection animated:NO];
    NSString* language = [[NSLocale preferredLanguages] objectAtIndex:0];
    if ([language isEqualToString:@"ar"]) {
        isArabic=YES;
    }
    NSString* property = @"Keyboard Message";
    NSString* verseMessageShown= [dataController getProperty:property];
    if (verseMessageShown==nil || [verseMessageShown isEqualToString:@"NO"]) {
        NSString* message=@"Type phonetic equivalent of the Arabic alphabet if you are not using Arabic keyboard. You can add the Arabic keyboard by Setting > General > International > Add a language > Arabic";
        [Utils showMessage:@"Tips" Message:message];
        [dataController setProperty:property Value:@"YES"];
    }
    
        //    if ([language isEqualToString:@"en"]) {
        //        [Utils showMessage:@"Arabic Keyboard" :@"P After adding Arabic Keyboard, you can switch between keyboards using the Globe sign on your keyboard."];
        //
        //    }
        //[Utils showMessage:@"Keyboard" :language];
        //searchBar.keyboardType = UIKeyboardTypeAlphabet;
        //searchBar.text=[RTL_CHAR stringByAppendingString:@""];
        //UITextField *someTextView = searchBar.t;
        //[searchBar setBaseWritingDirection:UITextWritingDirectionLeftToRight] ;//] forRange:[searchBar textRangeFromPosition:[searchBar beginningOfDocument] toPosition:[searchBar endOfDocument]]];
}



- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

    // Customize the number of rows in the table view.
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if (searching)
        return [copyListOfItems count];
    else {
        return [listOfItems count];
    }
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
    if (searching) {
        NSMutableArray *row =[copyListOfItems objectAtIndex:indexPath.row];
        cell.textLabel.text = [row objectAtIndex:0];
    } else {
        NSMutableArray *row =[listOfItems objectAtIndex:indexPath.row];
        cell.textLabel.text = [row objectAtIndex:0];
            //NSString *cellValue = [[@" (" stringByAppendingString:dict.transliteration] stringByAppendingString:@")    "];
            //cell.textLabel.text = [cellValue stringByAppendingString:dict.word];
    }
        //First get the dictionary object
    cell.textLabel.textAlignment = NSTextAlignmentRight;
    return cell;
}



- (NSIndexPath *)tableView :(UITableView *)theTableView willSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    if(letUserSelectRow)
        return indexPath;
    else
        return nil;
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
    DictionaryDataController *dataController= [Utils getDataModel];
        //subLevel.title = cell.textLabel.text;
    Word* dict;
    if (searching) {
        NSMutableArray *row =[copyListOfItems objectAtIndex:indexPath.row];
        dict= (Word*)[self findEntry:[row objectAtIndex:1]];
    }
    else {
        NSMutableArray *row =[listOfItems objectAtIndex:indexPath.row];
        dict= (Word*)[self findEntry:[row objectAtIndex:1]];
    }
        //subLevel.selectedIndex=indexPath.row;
        //subLevel.selectedWord=dict;
        //[subLevel initialize];
    WordViewController *wordViewController = [[WordViewController alloc] initWithWord:dict atIndex:(int)indexPath.row onTitle:cell.textLabel.text controller:dataController NIB:@"WordViewController"];
    [wordViewController initialize];
    [wordViewController setCallingController:self];
    //[wordViewController hideBackButton:NO];
    //[[Utils getTabController] presentViewController:wordViewController animated:YES completion:nil];
    [self.navigationController pushViewController:wordViewController animated:YES];
    //[self.navigationController pushViewController:wordViewController animated:YES];
    
}



- (void) searchBarTextDidBeginEditing:(UISearchBar *)theSearchBar {
    
    if(searching)
        return;
    
        //Add the overlay view.
    if(ovController == nil)
        ovController = [[OverlayViewController alloc] initWithNibName:@"OverlayViewController" bundle:[NSBundle mainBundle]];
    
    CGFloat yaxis = self.navigationController.navigationBar.frame.size.height;
    CGFloat width = self.view.frame.size.width;
    CGFloat height = self.view.frame.size.height;
    
        //Parameters x = origion on x-axis, y = origon on y-axis.
    CGRect frame = CGRectMake(0, yaxis, width, height);
    ovController.view.frame = frame;
    ovController.view.backgroundColor = [UIColor grayColor];
    ovController.view.alpha = 0.5;
    
    ovController.dictController = self;
    
    [self.tableView insertSubview:ovController.view aboveSubview:self.parentViewController.view];
    
    searching = YES;
    letUserSelectRow = NO;
    self.tableView.scrollEnabled = NO;
    
        //Add the done button.
    self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc]
                                              initWithBarButtonSystemItem:UIBarButtonSystemItemDone
                                              target:self action:@selector(doneSearching_Clicked:)];
}


- (Word*) findEntry:(NSString*) id {
    DictionaryDataController *dataController= [Utils getDataModel];
    return [dataController getWord:id] ;
}

- (NSString*) translateLetter:(unichar) englishLetter {
    NSString* arabicLetter = @"";
    if (englishLetter == 'A' || englishLetter == 'a' )
        arabicLetter = @"ا";
    else if (englishLetter == 'B' || englishLetter == 'b' )
        arabicLetter = @"ب";
    else if (englishLetter == 'C' || englishLetter == 'c' )
        arabicLetter = @"ص";
    else if (englishLetter == 'D' || englishLetter == 'd' )
        arabicLetter = @"د";
    else if (englishLetter == 'E' || englishLetter == 'e' )
        arabicLetter = @"ع";
    else if (englishLetter == 'F' || englishLetter == 'f' )
        arabicLetter = @"ف";
    else if (englishLetter == 'G' || englishLetter == 'g' )
        arabicLetter = @"غ";
    else if (englishLetter == 'H' || englishLetter == 'h' )
        arabicLetter = @"ح";
    else if (englishLetter == 'I' || englishLetter == 'i' )
        arabicLetter = @"ي";
    else if (englishLetter == 'J' || englishLetter == 'j' )
        arabicLetter = @"ج";
    else if (englishLetter == 'K' || englishLetter == 'k' )
        arabicLetter = @"ك";
    else if (englishLetter == 'L' || englishLetter == 'l' )
        arabicLetter = @"ل";
    else if (englishLetter == 'M' || englishLetter == 'm' )
        arabicLetter = @"م";
    else if (englishLetter == 'N' || englishLetter == 'n' )
        arabicLetter = @"ن";
    else if (englishLetter == 'O' || englishLetter == 'o' )
        arabicLetter = @"ه";
    else if (englishLetter == 'P' || englishLetter == 'p' )
        arabicLetter = @"ة";
    else if (englishLetter == 'Q' || englishLetter == 'q' )
        arabicLetter = @"ق";
    else if (englishLetter == 'R' || englishLetter == 'r' )
        arabicLetter = @"ر";
    else if (englishLetter == 'S' || englishLetter == 's' )
        arabicLetter = @"س";
    else if (englishLetter == 'T' || englishLetter == 't' )
        arabicLetter = @"ت";
    else if (englishLetter == 'U' || englishLetter == 'u' )
        arabicLetter = @"و";
    else if (englishLetter == 'V' || englishLetter == 'v' )
        arabicLetter = @"ذ";
    else if (englishLetter == 'W' || englishLetter == 'w' )
        arabicLetter = @"ش";
    else if (englishLetter == 'X' || englishLetter == 'x' )
        arabicLetter = @"خ";
    else if (englishLetter == 'Y' || englishLetter == 'y' )
        arabicLetter = @"ط";
    else if (englishLetter == 'Z' || englishLetter == 'z' )
        arabicLetter = @"ز";
    else {
        arabicLetter = nil;
            //return  [NSString stringWithFormat:@"%C",englishLetter];
    }
    return arabicLetter;
}

- (void)searchBar:(UISearchBar *)theSearchBar textDidChange:(NSString *)searchText {
    if ([searchArabicText isEqualToString:searchText]) {
        return;
    }
    //NSArray* modes = [UITextInputMode activeInputModes];
    //UITextInputMode* current = [modes firstObject];
    NSString* language = [UITextInputMode currentInputMode].primaryLanguage;
    //    NSLog(@"Language:%@,%i",language,[searchText length]);
    searchText = [searchText stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    searchText = [searchText stringByReplacingOccurrencesOfString:RTL_CHAR withString:@""];
    BOOL showRecent=[searchText length]==0?YES:NO;
    if ([language isEqualToString:@"ar"])  {
        searchArabicText=searchText;
    }
    else {
        if ([searchText length] >0) {
        NSString* arabicLetter=[self translateLetter:[searchText characterAtIndex:[searchText length]-1]];
        if (arabicLetter != nil){
            if ([searchArabicText isEqualToString:@""]) {
                searchArabicText=arabicLetter;
            }
            else {
                searchArabicText = [[searchText substringToIndex:[searchText length]-1] stringByAppendingString:arabicLetter];
            }
            searchArabicText = [searchArabicText stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
            searchBar.text=searchArabicText;
        }
    }
        else {
            searchArabicText=[RTL_CHAR stringByAppendingString:@""];
            searchBar.text=searchArabicText;
        }
    }
                
    // NSLog(@"SearchArabic=%@",searchArabicText);
    [copyListOfItems removeAllObjects];
    if(showRecent) {
        [self.tableView insertSubview:ovController.view aboveSubview:self.parentViewController.view];
        searching = NO;
        letUserSelectRow = NO;
        self.tableView.scrollEnabled = NO;
    }
    else {
        [ovController.view removeFromSuperview];
        searching = YES;
        letUserSelectRow = YES;
        self.tableView.scrollEnabled = YES;
        [self searchTableView:searchArabicText];
      }
    [self.tableView reloadData];
}

- (void) searchBarSearchButtonClicked:(UISearchBar *)theSearchBar {
    
    [self searchTableView:theSearchBar.text];
}

- (void) searchTableView:(NSString*) searchText {
    
        //NSString *searchText = searchBar.text;
        //NSMutableArray *searchArray = [[NSMutableArray alloc] init];
    DictionaryDataController *dataController= [Utils getDataModel];
        //[searchArray addObjectsFromArray:listOfItems];
    copyListOfItems = [dataController getSegment:searchText];
    
}

- (void) doneSearching_Clicked:(id)sender {
    searchBar.text = @"";
    [searchBar resignFirstResponder];
    letUserSelectRow = YES;
    searching = NO;
    self.navigationItem.rightBarButtonItem = nil;
    self.tableView.scrollEnabled = YES;
    [ovController.view removeFromSuperview];
        //[ovController release];
    ovController = nil;
    [self.tableView reloadData];
}



#pragma mark -
#pragma mark UIViewControllerRotation

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    return YES; // support all orientations
}


-(void)didSwipe:(UISwipeGestureRecognizer *)gestureRecognizer {
    NSLog(@"[FavoriteViewController].didSwipe Left");
    
    if (gestureRecognizer.state == UIGestureRecognizerStateEnded) {
        if (gestureRecognizer.direction==UISwipeGestureRecognizerDirectionLeft) {
            [Utils moveTab:self by:1];
        }
        else if (gestureRecognizer.direction==UISwipeGestureRecognizerDirectionRight) {
            [Utils moveTab:self by:-1];
        }
    }
}


@end
