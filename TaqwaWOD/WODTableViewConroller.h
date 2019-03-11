//
//  WODTableViewConroller.h
//  Tabster
//
//  Created by Wadood Chaudhary on 4/25/12.
//  Copyright (c) 2012 Ahmadiyya Muslim Community - Taqwa Group. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Constants.h"
 

@protocol WODTableViewConroller <NSObject>
- (void)dataChanged;
- (void)event:(enum EVENTS)evt Sender:(id)sender;
- (void)valueChangedSwitch:(UISwitch*) aSwitch;
- (void)valueChangedSlider:(UISlider*) aSlider;




@end


