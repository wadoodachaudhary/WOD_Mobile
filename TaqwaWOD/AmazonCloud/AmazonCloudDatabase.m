
#import "AmazonCloudDatabase.h"
#import "AmazonCloudConstants.h"
#import "Word.h"
#import "Utils.h"




@interface AmazonCloudDatabase()
-(NSString *)getStringValueForAttribute:(NSString *)theAttribute fromList:(NSArray *)attributeList;
-(int)getIntValueForAttribute:(NSString *)theAttribute fromList:(NSArray *)attributeList;
-(NSString *)getPaddedValue:(NSString*)aNumber;
@end

@implementation AmazonCloudDatabase

int batchSize=25;
@synthesize nextToken,online;


-(id)init
{
    self = [super init];
    if (self) {
            // Initial the SimpleDB Client.
        sdbClient      = [[AmazonSimpleDBClient alloc] initWithAccessKey:ACCESS_KEY_ID withSecretKey:SECRET_KEY];
        self.nextToken = nil;
        sortMethod     = 0;
        SimpleDBListDomainsRequest* listRequest = [[SimpleDBListDomainsRequest alloc] init];
        @try {
            [sdbClient listDomains:listRequest];
            self.online=YES;
        }
        @catch (NSException *exception) {
                //NSLog(@"Cloud Connection Error %@:",exception.description);
            self.online=NO;
        }
        @finally {
                //NSLog(@"Put wrapup code here");
        }     
        //SimpleDBSelectResponse *selectResponse = [sdbClient select:selectRequest];
        
    }
    return self;
}





/*
 * Method returns the number of items in the High Scores Domain.
 */
-(int)getRowCount:(NSString *)tableName {
    @try {
        SimpleDBSelectRequest *selectRequest = [[SimpleDBSelectRequest alloc] initWithSelectExpression:[@"select count(*) from " stringByAppendingString:tableName]];
        selectRequest.consistentRead = YES;
        
        SimpleDBSelectResponse *selectResponse = [sdbClient select:selectRequest];
        SimpleDBItem           *countItem      = [selectResponse.items objectAtIndex:0];
        
        return [self getIntValueForAttribute:@"Count" fromList:countItem.attributes];
    }
    @catch (NSException *exception) {
        NSLog(@"Exception : [%@]", exception);
        return 0;
    }
}

-(NSMutableArray*)retrieveInOrder:(NSString*) query {
        //NSMutableArray* table = [[NSMutableArray alloc] init];
    @try {
        SimpleDBSelectRequest *selectRequest = [[SimpleDBSelectRequest alloc] initWithSelectExpression:query];
        selectRequest.consistentRead = YES;
        if (self.nextToken != nil) {
            selectRequest.nextToken = self.nextToken;
        }
        SimpleDBSelectResponse *selectResponse = [sdbClient select:selectRequest];
        self.nextToken = selectResponse.nextToken;
        NSMutableArray *rows = [[NSMutableArray alloc] initWithCapacity:[selectResponse.items count]] ;
        int i=0;
        for (SimpleDBItem *item in selectResponse.items) {
            i++;
            NSString* idField = item.name;
            NSMutableDictionary *row=[[NSMutableDictionary alloc] init];
                //NSLog (@" Cloud ItemName(%@)",item.name);
            
            for (SimpleDBAttribute *attribute in item.attributes) {
                    //NSLog (@" Cloud %@ (%@)",attribute.name,attribute.value);
                [row setValue:attribute.value forKey:attribute.name];
            }
            [rows addObject:row];
            
        }
        return rows;
    }
    @catch (NSException *exception) {
        NSLog(@"Exception : [%@]", exception);
        return [NSMutableArray array];
    }
}


-(NSDictionary*)retrieve:(NSString*) query {
    NSMutableDictionary* table = [[NSMutableDictionary alloc] init];
    @try {
        SimpleDBSelectRequest *selectRequest = [[SimpleDBSelectRequest alloc] initWithSelectExpression:query];
        selectRequest.consistentRead = YES;
        if (self.nextToken != nil) {
            selectRequest.nextToken = self.nextToken;
        }
        SimpleDBSelectResponse *selectResponse = [sdbClient select:selectRequest];
        self.nextToken = selectResponse.nextToken;
        NSMutableArray *rows = [[NSMutableArray alloc] initWithCapacity:[selectResponse.items count]] ;
        int i=0;
        for (SimpleDBItem *item in selectResponse.items) {
            i++;
            NSString* idField = item.name;
            NSMutableDictionary *row=[[NSMutableDictionary alloc] init];
                //NSLog (@" Cloud ItemName(%@)",item.name);
            
            for (SimpleDBAttribute *attribute in item.attributes) {
                    //NSLog (@" Cloud %@ (%@)",attribute.name,attribute.value);
                [row setValue:attribute.value forKey:attribute.name];
            }
            [table setObject:row forKey:idField];
            
        }
        return table;
    }
    @catch (NSException *exception) {
        NSLog(@"Retrieve in order  : [%@]", exception);
        return [NSMutableDictionary alloc];
    }
}

-(void) resetTable:(NSString*) tableName {
    [self dropTable:tableName];
    [self createTable:tableName];
}

- (void) createTable:(NSString *)tableName TableData:(NSDictionary *)tableData ColList:(NSArray*) colList {
    @try {
        [self dropTable:tableName];
        [self createTable:tableName];
        for (id key in tableData) {
            NSMutableArray  *attributes = [[NSMutableArray alloc] initWithCapacity:[colList count]] ;
            NSMutableArray * row = (NSMutableArray*)[tableData objectForKey:key];
            int colIndex=0;
            for (NSString* colName in colList) {
                NSString* colValue = (NSString*)[row objectAtIndex:colIndex];
                SimpleDBReplaceableAttribute *col = [[SimpleDBReplaceableAttribute alloc] initWithName:colName  andValue:colValue andReplace:YES];
                    //NSLog(@" Cols: %@=%@",colName,colValue);
                [attributes addObject:col];
                colIndex++;
            }
            NSString* keyValue = [self getPaddedValue:key];
            SimpleDBPutAttributesRequest *putAttributesRequest = [[SimpleDBPutAttributesRequest alloc] initWithDomainName:tableName andItemName:keyValue andAttributes:attributes];
            [sdbClient putAttributes:putAttributesRequest];
        }
    }
    @catch (NSException *exception) {
        NSLog(@"Exception : [%@]", exception);
    }
}


-(void) createTable:(NSString *)tableName withData:(NSMutableArray *)tableData UsingLayout:(NSMutableArray*)colList {
    [self resetTable:tableName];
    [self addRowsInBatch:tableName Rows:tableData Cols:colList];
}



- (void) addRowsInBatch:(NSString *)tableName Rows:(NSMutableArray *)rows Cols:(NSArray*)cols{
    @try {
        SimpleDBBatchPutAttributesRequest* batch = [[SimpleDBBatchPutAttributesRequest alloc] init];
        [batch setDomainName:tableName];
        
        for (int r=0; r <[rows count]; r++) {
            NSMutableArray* row = [rows objectAtIndex:r];
            if (r % batchSize==0 && r > 0) {
                [sdbClient batchPutAttributes:batch];
                [batch.items removeAllObjects];
            }
            NSString* idValue = [self getPaddedValue:[row objectAtIndex:0]];
            SimpleDBReplaceableItem* item = [[SimpleDBReplaceableItem alloc] initWithName:idValue];
            for (int c=0; c < [cols count];c++) {
                
                NSString* colName=(NSString*)[cols objectAtIndex:c];
                NSString* colValue=(NSString*)[row objectAtIndex:c];
                SimpleDBReplaceableAttribute *colAttr = [[SimpleDBReplaceableAttribute alloc] initWithName:colName  andValue:colValue andReplace:YES];
                [item addAttribute:colAttr];
            }
            [batch addItem:item];

        } 
        if ([batch.items count]>0) [sdbClient batchPutAttributes:batch];
        
    }
    @catch (NSException *exception) {
        NSLog(@"Exception : [%@]", exception);
    }
}



- (void) addRow:(NSString *)tableName Row:(NSMutableArray *)row Key:(NSString*)key ColList:(NSArray*) colList {
    @try {
        NSMutableArray  *attributes = [[NSMutableArray alloc] initWithCapacity:[colList count]] ;
        int colIndex=0;
        for (NSString* colName in colList) {
            NSString* colValue = (NSString*)[row objectAtIndex:colIndex];
            SimpleDBReplaceableAttribute *col = [[SimpleDBReplaceableAttribute alloc] initWithName:colName  andValue:colValue andReplace:YES];
                //NSLog(@" Cols: %@=%@",colName,colValue);
            [attributes addObject:col];
            colIndex++;
        }
        NSString* keyValue = [self getPaddedValue:key];
        SimpleDBPutAttributesRequest *putAttributesRequest = [[SimpleDBPutAttributesRequest alloc] initWithDomainName:tableName andItemName:keyValue andAttributes:attributes];
        [sdbClient putAttributes:putAttributesRequest];
    }
    @catch (NSException *exception) {
        NSLog(@"Exception : [%@]", exception);
    }
}



/*
 * Removes the item from the Words domain.
 */
-(void)removeRow:(NSString *)tableName RowID:(NSString *)rowID {
    @try {
        SimpleDBDeleteAttributesRequest *deleteItem = [[SimpleDBDeleteAttributesRequest alloc] initWithDomainName:tableName andItemName:rowID]  ;
        [sdbClient deleteAttributes:deleteItem];
    }
    @catch (NSException *exception) {
        NSLog(@"Exception : [%@]", exception);
    }
}


-(void)dropTable:(NSString *) tableName {
    @try {
        SimpleDBDeleteDomainRequest *deleteDomain = [[SimpleDBDeleteDomainRequest alloc] initWithDomainName:tableName] ;
        [sdbClient deleteDomain:deleteDomain];
        
    }
    @catch (NSException *exception) {
        NSLog(@"Exception : [%@]", exception);
    }
}


-(void)createTable:(NSString *) tableName {
    @try {
        SimpleDBCreateDomainRequest *createDomain = [[SimpleDBCreateDomainRequest alloc] initWithDomainName:tableName];
        [sdbClient createDomain:createDomain];
    }
    @catch (NSException *exception) {
        NSLog(@"Exception : [%@]", exception);
    }
}

/*
 * Deletes the Word domain.
 */
-(void)clearRows:(NSString *) tableName
{
    @try {
        SimpleDBDeleteDomainRequest *deleteDomain = [[SimpleDBDeleteDomainRequest alloc] initWithDomainName:tableName] ;
        [sdbClient deleteDomain:deleteDomain];
        
        SimpleDBCreateDomainRequest *createDomain = [[SimpleDBCreateDomainRequest alloc] initWithDomainName:tableName] ;
        [sdbClient createDomain:createDomain];
    }
    @catch (NSException *exception) {
        NSLog(@"Exception : [%@]", exception);
    }
}


/*
 * Creates a padded number and returns it as a string.
 * All strings returned will have 10 characters.
 */
-(NSString *) getPaddedValue:(NSString*) value {
    if ([Utils isNumber:value]) {
        
    int aNumber = [value intValue];
    NSString *pad        = @"000000";
    NSString *padValue = [NSString stringWithFormat:@"%d", aNumber];
    
    NSRange  range;
    
    range.location = [pad length] - [padValue length];
    range.length   = [padValue length];
    
    return [pad stringByReplacingCharactersInRange:range withString:padValue];
    }
    return value;
}

-(NSString *)getStringValueForAttribute:(NSString *)theAttribute fromList:(NSArray *)attributeList{
    for (SimpleDBAttribute *attribute in attributeList) {
        if ( [attribute.name isEqualToString:theAttribute]) {
            return attribute.value;
        }
    }
    
    return @"";
}

/*
 * Extracts the value for the given attribute from the list of attributes.
 * Extracted value is returned as an int.
 */
-(int)getIntValueForAttribute:(NSString *)theAttribute fromList:(NSArray *)attributeList {
    for (SimpleDBAttribute *attribute in attributeList) {
        if ( [attribute.name isEqualToString:theAttribute]) {
            return [attribute.value intValue];
        }
    }
    
    return 0;
}


-(void)dealloc {
}

@end
