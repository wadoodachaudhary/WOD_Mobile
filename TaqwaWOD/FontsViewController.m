//
//  FontsViewController.m
//  TaqwaWOD
//
//  Created by Wadood Chaudhary on 5/25/14.
//
//

#import "FontsViewController.h"

@interface FontsViewController ()

@end

@implementation FontsViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad{
    [super viewDidLoad];
    tableView.allowsMultipleSelection=YES;
    // Do any additional setup after loading the view.
}

- (void)viewWillAppear{
    int section=0;
    NSArray *names = [fontNamesForFamilyNames objectForKey:familyNames[section]];
    int index = (int)[names indexOfObject:textFont.fontName];
    NSIndexPath *indexPath = [NSIndexPath indexPathForRow:index inSection:section];
    [tableView selectRowAtIndexPath:indexPath animated:NO scrollPosition: UITableViewScrollPositionNone];
    
    //
    section=1;
    names = [fontNamesForFamilyNames objectForKey:familyNames[section]];
    index = (int)[names indexOfObject:textFont.fontName];
    indexPath = [NSIndexPath indexPathForRow:index inSection:section];
    [tableView selectRowAtIndexPath:indexPath animated:NO scrollPosition: UITableViewScrollPositionNone];
 
}

- (void) viewDidDisappear:(BOOL)animated {
    if (self.navigationController == nil) {
        NSArray* paths =  [tableView indexPathsForSelectedRows];
//        for (NSIndexPath* path in paths) {
//            //NSLog(@"Section:%zd Row:%zd",path.section,path.row);
//        }
        //NSLog(@" Text Font Name :%@  Translation Font Name;%@",textFont.fontName,translationFont.fontName);
        //[self.navigationController setNavigationBarHidden:YES];
        [verseViewController setFonts:textFont.fontName andEnglishFont:translationFont.fontName];
    }
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (id) init {
    if (self = [super init]) {
        familyNames = [[UIFont familyNames] sortedArrayUsingSelector: @selector(compare:)];
        fontNamesForFamilyNames = [ [NSMutableDictionary alloc] init];
        for (NSString *familyName in familyNames) {
            NSArray *names = [ [UIFont fontNamesForFamilyName: familyName] sortedArrayUsingSelector: @selector(compare:)];
            [fontNamesForFamilyNames setObject: names forKey: familyName];
        }
    }
    return self;
}

- (id) initWithFonts:(NSMutableDictionary*) fonts ofFamily:(NSArray*) fontFamily withArabicFont:(UIFont*) arabicFont andWithEnglishFont:(UIFont*) englishFont byController:(VerseViewController*) _verseViewController{
    if (self = [super init]) {
        familyNames=fontFamily;
        fontNamesForFamilyNames=fonts;
        textFont = arabicFont;
        translationFont=englishFont;
        verseViewController=_verseViewController;
    }
    return self;
}


- (void) loadView {
    [super loadView];
    
    tableView = [ [UITableView alloc] initWithFrame: self.view.bounds style: UITableViewStylePlain];
    tableView.delegate=self;
    tableView.dataSource = self;
    [self.view addSubview: tableView];
}

- (void) dealloc {
    
}

- (void) viewDidUnload {
    tableView = nil;
    
    [super viewDidUnload];
}

#pragma - UITableViewDataSource methods

- (UITableViewCell *) tableView: (UITableView *) theTableView cellForRowAtIndexPath: (NSIndexPath *) indexPath {
    static NSString *cellIdentifier = @"FontCellIdentifier";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier: cellIdentifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle: UITableViewCellStyleDefault reuseIdentifier: cellIdentifier];
    }
    NSString *familyName = [familyNames objectAtIndex: indexPath.section];
    NSString *fontName = [ [fontNamesForFamilyNames objectForKey: familyName] objectAtIndex: indexPath.row];
    cell.textLabel.text = [familyName isEqualToString:@"Arabic"] ?@"بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ":@"In the name of God, the Gracious, the Merciful";
    cell.textLabel.font = [UIFont fontWithName: fontName size: [familyName isEqualToString:@"Arabic"] ?16:10];
    if ([cell.textLabel.font.fontName isEqualToString:textFont.fontName] || [cell.textLabel.font.fontName isEqualToString:translationFont.fontName]) {
        [tableView selectRowAtIndexPath:indexPath animated:TRUE scrollPosition:UITableViewScrollPositionNone];
        [[tableView delegate] tableView:tableView didSelectRowAtIndexPath:indexPath];
        cell.accessoryType = UITableViewCellAccessoryCheckmark;
        
    }
    
    return cell;
}

- (void) tableView:(UITableView *) _tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [_tableView cellForRowAtIndexPath:indexPath];
    for ( NSIndexPath* selectedIndexPath in tableView.indexPathsForSelectedRows ) {
        if ( selectedIndexPath.section == indexPath.section &&  selectedIndexPath.row != indexPath.row) {
            [tableView deselectRowAtIndexPath:selectedIndexPath animated:NO] ;
            UITableViewCell *cell = [_tableView cellForRowAtIndexPath:selectedIndexPath];
            cell.accessoryType = UITableViewCellAccessoryNone;
        }
    }
    //NSLog(@"If the cell isn't checked and there aren't the maximum allowed selected yet");
    if (cell.accessoryType != UITableViewCellAccessoryCheckmark) {
        cell.accessoryType = UITableViewCellAccessoryCheckmark;
        if (indexPath.section==0) textFont=cell.textLabel.font; else translationFont=cell.textLabel.font;
    }
    else {
        cell.accessoryType = UITableViewCellAccessoryNone;
    }
    
}

- (NSInteger) numberOfSectionsInTableView: (UITableView *) tableView {
    return [familyNames count];
}

- (NSInteger) tableView: (UITableView *) tableView numberOfRowsInSection: (NSInteger) section {
    NSString *familyName = [familyNames objectAtIndex: section];
    return [ [fontNamesForFamilyNames objectForKey: familyName] count];
}

- (NSString *) tableView: (UITableView *) tableView titleForHeaderInSection: (NSInteger) section {
    return [familyNames objectAtIndex: section];
}

#pragma - UITableViewDelegate methods


- (void)tableView:(UITableView *)_tableView didDeselectRowAtIndexPath:(NSIndexPath *)indexPath{
    UITableViewCell *tableViewCell = [_tableView cellForRowAtIndexPath:indexPath];
    tableViewCell.accessoryView.hidden = YES;
    // if you don't use custom image tableViewCell.accessoryType = UITableViewCellAccessoryNone;
}

@end
