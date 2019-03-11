//
//  OverlayViewController.h
//  Tabster
//
//  Created by Wadood Chaudhary on 4/10/12.
//  Copyright (c) 2012 Ahmadiyya Muslim Community - Taqwa Group. All rights reserved.
//

#import <UIKit/UIKit.h>

@class Dictionary;

@interface OverlayViewController : UIViewController{
    Dictionary *dictController;
}

@property (nonatomic, retain) Dictionary *dictController;

@end
