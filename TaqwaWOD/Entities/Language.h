//
//  Language.h
//  Tabster
//
//  Created by Wadood Chaudhary on 4/20/12.
//  Copyright (c) 2012 Ahmadiyya Muslim Community - Taqwa Group. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class Meaning;

@interface Language : NSManagedObject

@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) NSSet *containsDefinitions;
@end

@interface Language (CoreDataGeneratedAccessors)

- (void)addContainsDefinitionsObject:(Meaning *)value;
- (void)removeContainsDefinitionsObject:(Meaning *)value;
- (void)addContainsDefinitions:(NSSet *)values;
- (void)removeContainsDefinitions:(NSSet *)values;
- (NSMutableSet*)primitiveContainsDefinitions;
- (void)setPrimitiveContainsDefinitions:(NSMutableSet*)value;


@end
