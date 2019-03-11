//
//  Definition.h
//  Tabster
//
//  Created by Wadood Chaudhary on 4/20/12.
//  Copyright (c) 2012 Ahmadiyya Muslim Community - Taqwa Group. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class Example, Language, Word;

@interface Definition : NSManagedObject

@property (nonatomic, retain) NSDate * date;
@property (nonatomic, retain) NSString * meaning;
@property (nonatomic, retain) Word *definesWord;
@property (nonatomic, retain) NSSet *examples;
@property (nonatomic, retain) Language *inLanguage;

@end

@interface Definition (CoreDataGeneratedAccessors)

- (void)addExamplesObject:(Example *)value;
- (void)removeExamplesObject:(Example *)value;
- (void)addExamples:(NSSet *)values;
- (void)removeExamples:(NSSet *)values;
- (NSMutableSet*)primitiveExamples;
- (void)setPrimitiveExamples:(NSMutableSet*)value;


@end
