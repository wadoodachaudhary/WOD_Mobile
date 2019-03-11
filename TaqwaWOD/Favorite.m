/*
 
 */

#import "Favorite.h"
#import "WordViewController.h"
#import "DictionaryDataController.h"
#import "Word.h"
#import "AppDelegate.h"
#import "Utils.h"

@class Word;

@interface Favorite()
- (Word*) findEntry: (NSString*) transliteration;

@property (nonatomic, retain) NSArray *dataArray;

@end

@implementation Favorite


@synthesize subLevel, dataArray;



- (void)viewDidLoad {
  NSError *error = nil;
  [super viewDidLoad];
  
  
  letUserSelectRow = YES;
   self.title=@"Favorites";
  
  //[dataController registerController:self];
  self.title=@"Bookmarks";
    //Swipe Gestures
    UISwipeGestureRecognizer *gestureLeft = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(didSwipe:)];
    gestureLeft.direction = UISwipeGestureRecognizerDirectionLeft;
    [self.view addGestureRecognizer:gestureLeft];
    UISwipeGestureRecognizer *gestureRight = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(didSwipe:)];
    gestureRight.direction = UISwipeGestureRecognizerDirectionRight;
    [self.view addGestureRecognizer:gestureRight];
}

-(void)dataChanged {
  [self buildFavoriteList];
  [self.tableView reloadData];
}

- (void) valueChangedSwitch:(UISwitch*) aSwitch{}
- (void) valueChangedSlider:(UISlider*) aSlider{}

- (void)event:(enum EVENTS)evt Sender:(id)sender{
    
}

- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldRecognizeSimultaneouslyWithGestureRecognizer:(UIGestureRecognizer*) otherGestureRecognizer{
    return YES;
}



-(void) buildFavoriteList {
//  NSMutableArray* words = [[NSMutableArray alloc] init];
//  NSMutableArray* words_transliteration = [[NSMutableArray alloc] init];
//  
//  for (Word *word in dataArray) {
//    if ([word.favorite isEqualToString:@"YES"]) { 
//      [words addObject:word.word];
//      [words_transliteration addObject:word.transliteration];
//    }  
//  }
//  
  //listOfItems = [[NSMutableArray alloc] init];
  //[listOfItems addObjectsFromArray:words_transliteration];
    DictionaryDataController *dataController= [Utils getDataModel];
    listOfItems = [dataController getFavorites];
    NSLog(@"Favorite Items:%zd",[listOfItems count]);

}

- (void)viewDidUnload {
	[super viewDidUnload];
	self.dataArray = nil;
}

- (void)dealloc {}

- (void)viewWillAppear:(BOOL)animated {
	[super viewWillAppear:animated];
    [self dataChanged];
	NSIndexPath *tableSelection = [self.tableView indexPathForSelectedRow];
	[self.tableView deselectRowAtIndexPath:tableSelection animated:NO];
}


#pragma mark - UITableView delegate methods

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
  //Word* dict= (Word*)[self findEntry:[listOfItems objectAtIndex:indexPath.row]];
    //First get the dictionary object
  //NSString *cellValue = [[@" (" stringByAppendingString:dict.transliteration] stringByAppendingString:@")    "];
  NSMutableArray *row =[listOfItems objectAtIndex:indexPath.row];
  cell.textLabel.text = [row objectAtIndex:0];
    
  //cell.textLabel.text = [cellValue stringByAppendingString:dict.word];
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
    DictionaryDataController *dataController= [Utils getDataModel];
	UITableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
    NSMutableArray *row =[listOfItems objectAtIndex:indexPath.row];
    NSString* id  = [row objectAtIndex:1];
    int chapterNo  = [[row objectAtIndex:3] intValue];
    int verseNo  = [[row objectAtIndex:4] intValue];
    NSString* type = [row objectAtIndex:5];
    if ([type isEqualToString:@"W"]) {
        Word* dict=  [dataController getWord:id];
        WordViewController *wordViewController = [[WordViewController alloc] initWithWord:dict atIndex:(int)indexPath.row onTitle:cell.textLabel.text controller:dataController NIB:@"WordViewController"];
        [wordViewController initialize];
        [wordViewController setCallingController:self];
        [self.navigationController pushViewController:wordViewController animated:YES];
        //[[Utils getTabController] presentViewController:wordViewController animated:YES completion:nil];
   }
    else {
        NSMutableArray* displayFormat = [NSMutableArray arrayWithObjects:[NSNumber numberWithBool:YES],[NSNumber numberWithBool:YES],[NSNumber numberWithBool:NO],[NSNumber numberWithBool:NO], nil];
        VerseViewController* verseViewController = [[VerseViewController alloc] initWithChapter:chapterNo verse:verseNo verseDisplayFormat:displayFormat controller:dataController NIB:@"VerseViewController"];
        verseViewController.hidesBottomBarWhenPushed = YES;
        verseViewController.title=@"Favorite";
        [verseViewController setCallingController:self];
        //[[Utils getTabController] presentViewController:verseViewController animated:YES completion:nil];
        [self.navigationController pushViewController:verseViewController animated:YES];
    }
}

- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath {
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        //add code here to do what you want when you hit delete
        DictionaryDataController *dataController= [Utils getDataModel];
        NSMutableArray *row =[listOfItems objectAtIndex:indexPath.row];
        NSString* word_id  = [row objectAtIndex:1];
        int chapterNo  = [[row objectAtIndex:3] intValue];
        int verseNo  = [[row objectAtIndex:4] intValue];
        NSString* type = [row objectAtIndex:5];
        if ([type isEqualToString:@"W"]) {
            Word* word=  [dataController getWord:word_id];
            word.favorite = _NO;
            [dataController updateFavorite:word];
        }
        else {
            [dataController updateFavorite:NO chapterNo:chapterNo verse:verseNo];
        }
        [listOfItems removeObjectAtIndex:[indexPath row]];
        [tableView reloadData];
    }
}

- (Word*) findEntry:(NSString*) transliteration {
    DictionaryDataController *dataController= [Utils getDataModel];
    dataArray = dataController.dictionaryArray;

  for (Word *word in dataArray) {
    if ([transliteration isEqualToString:word.transliteration]) {
      return word;
    }
  } 
  return nil;
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
