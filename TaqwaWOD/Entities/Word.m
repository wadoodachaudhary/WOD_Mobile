//
//  Word.m
//  Tabster
//
//  Created by Wadood Chaudhary on 4/20/12.
//  Copyright (c) 2012 Ahmadiyya Muslim Community - Taqwa Group. All rights reserved.
//

#import "Word.h"
#import "Definition.h"


@implementation Word

@dynamic audiofile;
@dynamic origin;
@dynamic transliteration;
@dynamic word;
@dynamic wordsimple;
@dynamic relatedword;
@dynamic favorite;
@dynamic date;
@dynamic definitions;
@dynamic verses;
@dynamic relatedWords;
@dynamic root;
@dynamic lemma;
@dynamic tag;
@dynamic versecount;



- (void)addDefinitionsObject:(Example *)value {
    NSSet *changedObjects = [[NSSet alloc] initWithObjects:&value count:1];
    
    [self willChangeValueForKey:@"definitions"
                withSetMutation:NSKeyValueUnionSetMutation
                   usingObjects:changedObjects];
    [[self primitiveDefinitions] addObject:value];
    [self didChangeValueForKey:@"definitions"
               withSetMutation:NSKeyValueUnionSetMutation
                  usingObjects:changedObjects];
    
}

- (void)removeDefinitionsObject:(Example *)value
{
    NSSet *changedObjects = [[NSSet alloc] initWithObjects:&value count:1];
    
    [self willChangeValueForKey:@"definitions"
                withSetMutation:NSKeyValueMinusSetMutation
                   usingObjects:changedObjects];
    [[self primitiveDefinitions] removeObject:value];
    [self didChangeValueForKey:@"definitions"
               withSetMutation:NSKeyValueMinusSetMutation
                  usingObjects:changedObjects];
    
}

- (void)addDefinitions:(NSSet *)value
{
    [self willChangeValueForKey:@"definitions"
                withSetMutation:NSKeyValueUnionSetMutation
                   usingObjects:value];
    [[self primitiveDefinitions] unionSet:value];
    [self didChangeValueForKey:@"definitions"
               withSetMutation:NSKeyValueUnionSetMutation
                  usingObjects:value];
}

- (void)removeDefinitions:(NSSet *)value
{
    [self willChangeValueForKey:@"definitions"
                withSetMutation:NSKeyValueMinusSetMutation
                   usingObjects:value];
    [[self primitiveDefinitions] minusSet:value];
    [self didChangeValueForKey:@"definitions"
               withSetMutation:NSKeyValueMinusSetMutation
                  usingObjects:value];
}

- (void)addVersesObject:(Example *)value {
    NSSet *changedObjects = [[NSSet alloc] initWithObjects:&value count:1];
    
    [self willChangeValueForKey:@"verses"
                withSetMutation:NSKeyValueUnionSetMutation
                   usingObjects:changedObjects];
    [[self primitiveVerses] addObject:value];
    [self didChangeValueForKey:@"verses"
               withSetMutation:NSKeyValueUnionSetMutation
                  usingObjects:changedObjects];
    
}

- (void)removeVersesObject:(Example *)value
{
    NSSet *changedObjects = [[NSSet alloc] initWithObjects:&value count:1];
    
    [self willChangeValueForKey:@"verses"
                withSetMutation:NSKeyValueMinusSetMutation
                   usingObjects:changedObjects];
    [[self primitiveVerses] removeObject:value];
    [self didChangeValueForKey:@"verses"
               withSetMutation:NSKeyValueMinusSetMutation
                  usingObjects:changedObjects];
    
}

- (void)addVerses:(NSSet *)value
{
    [self willChangeValueForKey:@"verses"
                withSetMutation:NSKeyValueUnionSetMutation
                   usingObjects:value];
    [[self primitiveVerses] unionSet:value];
    [self didChangeValueForKey:@"verses"
               withSetMutation:NSKeyValueUnionSetMutation
                  usingObjects:value];
}

- (void)removeVerses:(NSSet *)value
{
    [self willChangeValueForKey:@"verses"
                withSetMutation:NSKeyValueMinusSetMutation
                   usingObjects:value];
    [[self primitiveVerses] minusSet:value];
    [self didChangeValueForKey:@"verses"
               withSetMutation:NSKeyValueMinusSetMutation
                  usingObjects:value];
}
    //Related Words



- (void)addRelatedWordsObject:(Example *)value {
    NSSet *changedObjects = [[NSSet alloc] initWithObjects:&value count:1];
    
    [self willChangeValueForKey:@"relatedWords"
                withSetMutation:NSKeyValueUnionSetMutation
                   usingObjects:changedObjects];
    [[self primitiveRelatedWords] addObject:value];
    [self didChangeValueForKey:@"relatedWords"
               withSetMutation:NSKeyValueUnionSetMutation
                  usingObjects:changedObjects];
    
}

- (void)removeRelatedWordsObject:(Example *)value
{
    NSSet *changedObjects = [[NSSet alloc] initWithObjects:&value count:1];
    
    [self willChangeValueForKey:@"relatedWords"
                withSetMutation:NSKeyValueMinusSetMutation
                   usingObjects:changedObjects];
    [[self primitiveRelatedWords] removeObject:value];
    [self didChangeValueForKey:@"relatedWords"
               withSetMutation:NSKeyValueMinusSetMutation
                  usingObjects:changedObjects];
    
}

- (void)addRelatedWords:(NSSet *)value
{
    [self willChangeValueForKey:@"relatedWords"
                withSetMutation:NSKeyValueUnionSetMutation
                   usingObjects:value];
    [[self primitiveRelatedWords] unionSet:value];
    [self didChangeValueForKey:@"relatedWords"
               withSetMutation:NSKeyValueUnionSetMutation
                  usingObjects:value];
}

- (void)removeRelatedWords:(NSSet *)value
{
    [self willChangeValueForKey:@"relatedWords"
                withSetMutation:NSKeyValueMinusSetMutation
                   usingObjects:value];
    [[self primitiveRelatedWords] minusSet:value];
    [self didChangeValueForKey:@"relatedWords"
               withSetMutation:NSKeyValueMinusSetMutation
                  usingObjects:value];
}



@end
