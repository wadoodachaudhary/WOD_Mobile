//
//  Definition.m
//  Tabster
//
//  Created by Wadood Chaudhary on 4/20/12.
//  Copyright (c) 2012 Ahmadiyya Muslim Community - Taqwa Group. All rights reserved.
//

#import "Definition.h"
#import "Example.h"
#import "Language.h"
#import "Word.h"


@implementation Definition

@dynamic meaning;
@dynamic date;
@dynamic definesWord;
@dynamic examples;
@dynamic inLanguage;


- (void) awakeFromInsert {
  [super awakeFromInsert];
  [self setDate:[NSDate date]];
}

- (void)addExamplesObject:(Example *)value {
    NSSet *changedObjects = [[NSSet alloc] initWithObjects:&value count:1];
    
    [self willChangeValueForKey:@"examples"
                withSetMutation:NSKeyValueUnionSetMutation
                   usingObjects:changedObjects];
    [[self primitiveExamples] addObject:value];
    [self didChangeValueForKey:@"examples"
               withSetMutation:NSKeyValueUnionSetMutation
                  usingObjects:changedObjects];
    
}

- (void)removeExamplesObject:(Example *)value {
    NSSet *changedObjects = [[NSSet alloc] initWithObjects:&value count:1];
    
    [self willChangeValueForKey:@"examples"
                withSetMutation:NSKeyValueMinusSetMutation
                   usingObjects:changedObjects];
    [[self primitiveExamples] removeObject:value];
    [self didChangeValueForKey:@"examples"
               withSetMutation:NSKeyValueMinusSetMutation
                  usingObjects:changedObjects];
    
}

- (void)addExamples:(NSSet *)value{
    [self willChangeValueForKey:@"examples"
                withSetMutation:NSKeyValueUnionSetMutation
                   usingObjects:value];
    [[self primitiveExamples] unionSet:value];
    [self didChangeValueForKey:@"examples"
               withSetMutation:NSKeyValueUnionSetMutation
                  usingObjects:value];
}

- (void)removeExamples:(NSSet *)value{
    [self willChangeValueForKey:@"examples"
                withSetMutation:NSKeyValueMinusSetMutation
                   usingObjects:value];
    [[self primitiveExamples] minusSet:value];
    [self didChangeValueForKey:@"examples"
               withSetMutation:NSKeyValueMinusSetMutation
                  usingObjects:value];
}







@end
