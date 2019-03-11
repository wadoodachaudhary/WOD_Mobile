//
//  FlatButton.h
//  TaqwaWOD
//
//  Created by Wadood Chaudhary on 5/20/14.
//
//

#import <UIKit/UIKit.h>

@interface FlatButton : UIButton
@property(strong, nonatomic)UIColor *myColor;
-(id)initWithFrame:(CGRect)frame withBackgroundColor:(UIColor*)backgroundColor;
@end
