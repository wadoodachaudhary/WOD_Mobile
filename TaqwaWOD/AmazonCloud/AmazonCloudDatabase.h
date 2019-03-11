#import <Foundation/Foundation.h>
#import <AWSRuntime/AWSRuntime.h>
#import <AWSSimpleDB/AWSSimpleDB.h>
//#import <AWSiOSSDK/SimpleDB/AmazonSimpleDB.h>
#import "Word.h"



@interface AmazonCloudDatabase:NSObject {
    AmazonSimpleDBClient *sdbClient;
    NSString             *nextToken;
    int                  sortMethod;
}

@property (nonatomic, retain) NSString *nextToken;
@property (nonatomic) BOOL online;


-(int)getRowCount:(NSString *)tableName;
-(NSDictionary*)retrieve:(NSString*) query;
-(NSMutableArray*)retrieveInOrder:(NSString*) query;
-(void)removeRow:(NSString*)tableName RowID:(NSString *)rowID;
-(void)createTable:(NSString *) tableName;
-(void)dropTable:(NSString *) tableName;
-(void)clearRows:(NSString*)tableName;
-(NSString *)getPaddedValue:(NSString*)value;
-(void) createTable:(NSString *)tableName TableData:(NSDictionary *)tableData  ColList:(NSArray*) colList;
-(void) createTable:(NSString *)tableName withData:(NSMutableArray *)tableData UsingLayout:(NSArray*) colList;
-(void) addRow:(NSString *)tableName Row:(NSMutableArray *)row Key:(NSString*)key ColList:(NSArray*) colList;
-(void) addRowsInBatch:(NSString *)tableName Rows:(NSMutableArray *)rows Cols:(NSMutableArray*) cols;


@end
