//
//  Recent.h
//  TaqwaWOD
//
//  Created by Wadood Chaudhary on 7/20/12.
//  Copyright (c) 2012 Instinet. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "WODTableViewConroller.h"


@class DictionaryDataController;
@class WordViewController;



@interface Recent : UITableViewController<WODTableViewConroller>  {
    NSMutableArray *listOfItems;
    NSArray* dataArray; 
}


@property (nonatomic, retain) IBOutlet WordViewController *subLevel;
@property (strong, nonatomic) DictionaryDataController *dataController;

- (void)dataChanged;
- (void)event:(enum EVENTS)evt Sender:(id)sender;

@end
