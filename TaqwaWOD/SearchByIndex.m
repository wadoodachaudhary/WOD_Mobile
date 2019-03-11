//
//  SearchByIndex.m
//
//  Created by Wadood Chaudhary on 5/16/12.
//  Copyright (c) 2012 Ahmadiyya Muslim Community - Taqwa Group. All rights reserved.
//

#import "SearchByIndex.h"
#import "AppDelegate.h"
#import "DictionaryDataController.h"
#import "VerseViewController.h"
#import "Word.h"


@interface SearchByIndex ()

@end

@implementation SearchByIndex
@synthesize chapterHeadings;
NSMutableArray* chapterNames;
DictionaryDataController* dataController;





- (id)initWithStyle:(UITableViewStyle)style{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad{
    [super viewDidLoad];

    AppDelegate * theDelegate = (AppDelegate *) [UIApplication sharedApplication].delegate;
    if (theDelegate==nil) NSLog(@"Delegate not found!");
    dataController = theDelegate.dataController;
    chapterNames = (NSMutableArray*)[dataController getAllChapterNames];
}

- (void)viewDidUnload{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return [chapterNames count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
    }
        // Configure the cell.];
    [cell.textLabel setFont:[UIFont systemFontOfSize:12]];
    NSMutableArray* row = (NSMutableArray*) [chapterNames objectAtIndex:indexPath.row];
    cell.textLabel.text  = [NSString stringWithFormat:@"%@. %@ (%@)",[row objectAtIndex:0],[row objectAtIndex:2],[row objectAtIndex:1]];
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [[tableView cellForRowAtIndexPath:indexPath] setSelected:NO animated:YES];
    NSUInteger chapterNo = (indexPath.row+1);
    NSMutableArray* row = (NSMutableArray*) [chapterNames objectAtIndex:indexPath.row];
    NSString* chapterName=[NSString stringWithFormat:@"%@. %@ (%@)",[row objectAtIndex:0],[row objectAtIndex:2],[row objectAtIndex:1]];
    
    NSValue* arabicOn = [NSNumber numberWithBool:TRUE];
    NSValue* translationOn = [NSNumber numberWithBool:TRUE];
    NSValue* transliterationOn = [NSNumber numberWithBool:FALSE];
    NSValue* translationSplitWordOn = [NSNumber numberWithBool:FALSE];
    
    NSMutableArray* displayFormat = [NSMutableArray arrayWithObjects:arabicOn,translationOn,translationSplitWordOn,transliterationOn, nil];
    VerseViewController *verseViewController = [[VerseViewController alloc] initWithChapter:(int)chapterNo verse:1 verseDisplayFormat:displayFormat controller:dataController NIB:@"VerseViewController"];
    verseViewController.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:verseViewController animated:YES];
    
}

@end
