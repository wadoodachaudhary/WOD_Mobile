/*
 File: Recent.h
 Abstract: The view controller for page two.
 Version: 1.0
 
 
 Copyright (C) 2011 Apple Inc. All Rights Reserved.
 
 */

#import <UIKit/UIKit.h>
#import "WODTableViewConroller.h"
#import "DictionaryDataController.h"

@class WordViewController;


@interface Favorite : UITableViewController<WODTableViewConroller>{
  @private
  BOOL searching;
  BOOL letUserSelectRow;
  NSMutableArray *listOfItems;
  //DictionaryDataController* dataController;
    
}

-(void) buildFavoriteList;
@property (nonatomic, retain) IBOutlet WordViewController *subLevel;
- (void)dataChanged;
- (void)event:(enum EVENTS)evt Sender:(id)sender;


@end






