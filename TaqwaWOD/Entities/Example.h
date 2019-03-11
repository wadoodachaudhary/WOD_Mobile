//
//  Example.h
//  Tabster
//
//  Created by Wadood Chaudhary on 4/20/12.
//  Copyright (c) 2012 Ahmadiyya Muslim Community - Taqwa Group. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class Definition;

@interface Example : NSManagedObject

@property (nonatomic, retain) NSString * example;
@property (nonatomic, retain) NSString * reference;
@property (nonatomic, retain) Definition *exampleOfDefinition;

@end
