#import "AmazonCloudConstants.h"

@implementation Constants


+(UIAlertView *)credentialsAlert
{
    return [[UIAlertView alloc] initWithTitle:@"Missing Credentials" message:CREDENTIALS_MESSAGE delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
}

+(NSString *)getRandomPlayerName
{
    NSArray *playerNames = [[NSArray alloc] initWithObjects:@"Norm", @"Jim", @"Jason", @"Zach", @"Matt", @"Glenn", @"Will", @"Wade", @"Trevor", @"Jeremy", @"Ryan", @"Matty", @"Steve", @"Pavel", nil];
    int     name1        = arc4random() % [playerNames count];
    int     name2        = arc4random() % [playerNames count];
    
    return [NSString stringWithFormat:@"%@ %@", [playerNames objectAtIndex:name1], [playerNames objectAtIndex:name2]];
}

+(int)getRandomScore
{
    return (arc4random() % 1000) + 1;
}

@end

