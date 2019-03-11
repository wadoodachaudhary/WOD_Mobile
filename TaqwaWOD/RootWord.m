//
//  RootWord.m
//  Tabster
//
//  Created by Wadood Chaudhary on 5/19/12.
//  Copyright (c) 2012 Ahmadiyya Muslim Community - Taqwa Group. All rights reserved.
//

#import "RootWord.h"
#import "DictionaryDataController.h"
#import "DefinitionCells.h"
#import "Constants.h"
#import "VerseDrawLabel.h"
#import "Verse.h"
#import "Utils.h"
#import "VerseViewController.h"



@interface RootWord (){
@private
CGSize maximumLabelSize;
}
@end

@implementation RootWord


- (id)initWithStyle:(UITableViewStyle)style {
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (id)initWithRootWord:(NSString*) rootWord controller:(DictionaryDataController*) _dataController NIB:(NSString*) nibName {
    self = [super initWithNibName:nibName bundle:nil];
    if (self) {
            // Custom initialization
        CGRect rect = [Utils getScreenSize];
        NSLog(@"In RootViewCintroller:an iPad: (x=%f,y=%f,width=%f,height=%f)",rect.origin.x,rect.origin.y,rect.size.width,rect.size.height);
        
        maximumLabelSize  = CGSizeMake(rect.size.width-6,9999);
        
        //maximumLabelSize= CGSizeMake(300,9999) ;
        root=rootWord;
        dataController=_dataController;
        translation=@"";
        translation=[dataController getRootMeaning:root];
        if (translation==nil) translation=@" No definition of the root found.";
        translation= [self format:translation];
            //NSLog(@" Root Meaning:%@",translation);
        
        words = [dataController getWordsForRoot:root];
        
        NSMutableArray* wordsToRemove = [[NSMutableArray alloc] init];
        NSMutableArray* wordsimpleUniqueList = [[NSMutableArray alloc] init];
        
        for (NSMutableArray* row in words) {
            NSString* wordsimple = [row objectAtIndex:1];
            NSUInteger index = [wordsimpleUniqueList indexOfObject:wordsimple]; 
            NSString* meaning=[row objectAtIndex:2];
            meaning=allTrim(meaning);
            if ([meaning length]==0) {
                [wordsToRemove addObject:row];
            }
            else if ([wordsimpleUniqueList containsObject:wordsimple]) {
                [wordsToRemove addObject:row];
            }
            else {
                [wordsimpleUniqueList addObject:wordsimple];
            }
        }
        for (NSMutableArray* row in wordsToRemove) {
            [words removeObject:row];
        }
        int i=0;
        for (NSMutableArray* row in words) {
            NSString* wordWithMeaning = [NSMutableString stringWithFormat:@"%@ (%@)",[row objectAtIndex:0],[row objectAtIndex:2]];     
            if (i==0) allwords=[NSMutableString stringWithFormat:@"%@%@",@"\u200E",wordWithMeaning];
            else [allwords appendFormat:@", %@",wordWithMeaning];
            i++;
        }    
            //NSLog(@"[%@]",allwords);
        verseCount = [dataController getVerseCountForRoot:root];
        rowcount= 3;
            //NSLog(@"rowcount=%i",rowcount);
            //
        
    }
    return self;
}

-(NSString*) format:(NSString*) _translation{
    _translation = [_translation stringByReplacingCharactersInRange:NSMakeRange(0,1) withString:[[_translation substringToIndex:1] capitalizedString]];
    return _translation;
}


- (void)viewDidLoad {
    [super viewDidLoad];
    [[self navigationController] setNavigationBarHidden:NO animated:YES];
    
        //self.

}

- (void)viewDidUnload{
    [super viewDidUnload];
}


- (void)viewWillAppear:(BOOL)animated {  
    [[self navigationController] setNavigationBarHidden:NO animated:YES];  
}  

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    // Return the number of sections.
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    // Return the number of rows in the section.
    return rowcount;
}

- (void) valueChangedSwitch:(UISwitch*) aSwitch{}
- (void) valueChangedSlider:(UISlider*) aSlider{}




- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *kCellID2 = @"cellID2";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:kCellID2];
    if (cell == nil)	{
        NSArray* nib = [[NSBundle mainBundle] loadNibNamed:@"DefinitionCells" owner:self options:nil];
        
        DefinitionCells* cellDefinition;
        if (indexPath.row==0) {
            cellDefinition = (DefinitionCells *) [nib objectAtIndex:5];
            [cellDefinition setController:self];
            [cellDefinition setSelectedBox:RootWordBox];
            [cellDefinition rootword].text = root ;
            UILabel* lblRootMeaning = [cellDefinition rootwordMeaning];
                //translation = allTrim(translation);
            //CGSize lblRootMeaningSize = [translation sizeWithFont:lblRootMeaning.font constrainedToSize:maximumLabelSize lineBreakMode:lblRootMeaning.lineBreakMode];
            
            CGSize lblRootMeaningSize = [Utils getTextSize:1.0 forText:translation  fontToSize:lblRootMeaning.font rectSize:maximumLabelSize];
            
            
            lblRootMeaning.text = translation ;
            lblRootMeaning.frame = CGRectMake(5.0f, lblRootMeaning.frame.origin.y, lblRootMeaningSize.width, lblRootMeaningSize.height);
                //lblRootMeaning.layer.borderColor = [UIColor greenColor].CGColor;
                //lblRootMeaning.layer.borderWidth = 3.0;
            
            
        }
        else if (indexPath.row==1) {
            cellDefinition = (DefinitionCells *) [nib objectAtIndex:7];
            [cellDefinition setController:self];
            [cellDefinition setSelectedBox:RelatedWordBox];
            UILabel* lblChildren = [cellDefinition childrenwords];
            
            //CGSize lblChildrenSize = [allwords sizeWithFont:lblChildren.font constrainedToSize:maximumLabelSize lineBreakMode:lblChildren.lineBreakMode];
            CGSize lblChildrenSize = [Utils getTextSize:1.0 forText:allwords  fontToSize:lblChildren.font rectSize:maximumLabelSize];
            
            lblChildren.frame = CGRectMake(lblChildren.frame.origin.x, lblChildren.frame.origin.y, lblChildrenSize.width, lblChildrenSize.height);
            lblChildren.text = allwords;

        }
//        else if (indexPath.row>=2 && indexPath.row < rowcount-1) {
//                cellDefinition = (DefinitionCells *) [nib objectAtIndex:6];
//                [cellDefinition setSelectedBox:RelatedWordBox];
//                NSMutableArray* row = [words objectAtIndex:indexPath.row -2]; 
//                [cellDefinition relatedWord].text =  [row objectAtIndex:0];
//            [cellDefinition relatedWordMeaning].text =  [self format:[row objectAtIndex:2]];
//            
//        } 
        else if (indexPath.row == rowcount-1) {
            cellDefinition = (DefinitionCells *) [nib objectAtIndex:3];
            [cellDefinition setSelectedBox:VersesBox];
            [cellDefinition setController:self];
            cellDefinition.refcount.text= [NSString stringWithFormat:@"%i",verseCount]; 
                       
                        
        }
        return cellDefinition;
        
    }
    return cell;
}

 
- (void) event:(enum EVENTS)evt Sender:(id)sender {
    if (evt==ShowVerse) {
        VerseViewController* verseViewController =  [[VerseViewController alloc] initWithRootWord:root controller:dataController NIB:@"VerseViewController"];
            verseViewController.hidesBottomBarWhenPushed = YES;
        [self.navigationController pushViewController:verseViewController animated:NO];
    }    
}


-(void)dataChanged {
    [self.tableView reloadData];
    
}
 


-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    NSArray* nib = [[NSBundle mainBundle] loadNibNamed:@"DefinitionCells" owner:self options:nil];
    DefinitionCells* cellDefinition;
    int separator=10;
    int index=0;
    if (indexPath.row==0) {
        index=5;
        cellDefinition = (DefinitionCells *) [nib objectAtIndex:index];
        cellDefinition.controller=self;
        UILabel* lblRootMeaning=[cellDefinition rootwordMeaning];
        //CGSize lblRootMeaningSize = [translation sizeWithFont:lblRootMeaning.font constrainedToSize:maximumLabelSize lineBreakMode:lblRootMeaning.lineBreakMode];
        CGSize lblRootMeaningSize = [Utils getTextSize:1.0 forText:translation  fontToSize:lblRootMeaning.font rectSize:maximumLabelSize];
        
            //lblRootMeaning.text = rtlExample ;
        int rowHeight = lblRootMeaning.frame.origin.y+lblRootMeaningSize.height+separator;
        return lblRootMeaning.frame.origin.y+lblRootMeaningSize.height+separator;
    }  
    if (indexPath.row==1) {
        index=7;
        cellDefinition = (DefinitionCells *) [nib objectAtIndex:index];
        cellDefinition.controller=self;
        UILabel* lblChildren =[cellDefinition childrenwords];
        //CGSize lblChildrenSize = [allwords sizeWithFont:lblChildren.font constrainedToSize:maximumLabelSize lineBreakMode:lblChildren.lineBreakMode];
        CGSize lblChildrenSize = [Utils getTextSize:1.0 forText:allwords  fontToSize:lblChildren.font rectSize:maximumLabelSize];
        
        return lblChildren.frame.origin.y+lblChildrenSize.height+separator;
    }
//    else if (indexPath.row>=2 && indexPath.row < rowcount-1) {
//        index=6;
//        UILabel* lbl;
//        cellDefinition = (DefinitionCells *) [nib objectAtIndex:index];
//        cellDefinition.controller=self;
//        lbl=[cellDefinition relatedWordMeaning];   
//        return lbl.frame.origin.y+lbl.frame.size.height+separator;
//    }    
    else if (indexPath.row == rowcount-1) { 
        index=3;
        cellDefinition = (DefinitionCells *) [nib objectAtIndex:index];
        cellDefinition.controller=self;
        UIButton* btnlbl=[cellDefinition showall];
        return btnlbl.frame.origin.y+btnlbl.frame.size.height+separator;
    }
    return separator;
   
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    // Navigation logic may go here. Create and push another view controller.
    /*
     <#DetailViewController#> *detailViewController = [[<#DetailViewController#> alloc] initWithNibName:@"<#Nib name#>" bundle:nil];
     // ...
     // Pass the selected object to the new view controller.
     [self.navigationController pushViewController:detailViewController animated:YES];
     */
}

@end
