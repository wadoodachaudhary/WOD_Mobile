//
//  AmazonCloudConstants.h
//  Tabster
//
//  Created by Wadood Chaudhary on 4/23/12.
//  Copyright (c) 2012 Ahmadiyya Muslim Community - Taqwa Group. All rights reserved.
//

// Constants used to represent your AWS Credentials.
#define ACCESS_KEY_ID          @"0D30PAP4SFJT5E6086G2"
#define SECRET_KEY             @"v7ZlhrN3LZ1ZMICUykxq+H/19GFwaweLdFrsonoG"
#define CREDENTIALS_MESSAGE    @"AWS Credentials not configured correctly.  Please review the README file."


@interface AmazonCloudConstants:NSObject {
}

+(NSString *)getRandomPlayerName;
+(int)getRandomScore;
+(UIAlertView *)credentialsAlert;


@end
