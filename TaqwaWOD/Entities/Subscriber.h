//
//  Subscriber.h
//  Tabster
//
//  Created by Wadood Chaudhary on 4/20/12.
//  Copyright (c) 2012 Ahmadiyya Muslim Community - Taqwa Group. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>


@interface Subscriber : NSManagedObject

@property (nonatomic, retain) NSString * email;
@property (nonatomic, retain) NSString * firstname;
@property (nonatomic, retain) NSString * lastname;
@property (nonatomic, retain) NSNumber * subscriberid;

@end
