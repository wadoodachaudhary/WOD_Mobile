  //
  //  Language.m
  //  Tabster
  //
  //  Created by Wadood Chaudhary on 4/20/12.
  //  Copyright (c) 2012 Ahmadiyya Muslim Community - Taqwa Group. All rights reserved.
  //

#import "Language.h"
#import "Definition.h"


@implementation Language

@dynamic name;
@dynamic containsDefinitions;



- (void)addContainsDefinitionsObject:(Example *)value {
  NSSet *changedObjects = [[NSSet alloc] initWithObjects:&value count:1];
  
  [self willChangeValueForKey:@"containsDefinitions"
              withSetMutation:NSKeyValueUnionSetMutation
                 usingObjects:changedObjects];
  [[self primitiveContainsDefinitions] addObject:value];
  [self didChangeValueForKey:@"containsDefinitions"
             withSetMutation:NSKeyValueUnionSetMutation
                usingObjects:changedObjects];
  
}

- (void)removeContainsDefinitionsObject:(Example *)value{
  NSSet *changedObjects = [[NSSet alloc] initWithObjects:&value count:1];
  
  [self willChangeValueForKey:@"containsDefinitions"
              withSetMutation:NSKeyValueMinusSetMutation
                 usingObjects:changedObjects];
  [[self primitiveContainsDefinitions] removeObject:value];
  [self didChangeValueForKey:@"containsDefinitions"
             withSetMutation:NSKeyValueMinusSetMutation
                usingObjects:changedObjects];
  
}

- (void)addContainsDefinitions:(NSSet *)value {
  [self willChangeValueForKey:@"containsDefinitions"
              withSetMutation:NSKeyValueUnionSetMutation
                 usingObjects:value];
  [[self primitiveContainsDefinitions] unionSet:value];
  [self didChangeValueForKey:@"containsDefinitions"
             withSetMutation:NSKeyValueUnionSetMutation
                usingObjects:value];
}

- (void)removeContainsDefinitions:(NSSet *)value{
  [self willChangeValueForKey:@"containsDefinitions"
              withSetMutation:NSKeyValueMinusSetMutation
                 usingObjects:value];
  [[self primitiveContainsDefinitions] minusSet:value];
  [self didChangeValueForKey:@"containsDefinitions"
             withSetMutation:NSKeyValueMinusSetMutation
                usingObjects:value];
}



@end
