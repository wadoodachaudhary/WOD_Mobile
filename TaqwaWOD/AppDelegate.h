/*
     File: AppDelegate.h
 Abstract: The application delegate class used for installing our navigation controller.
  Version: 1.0
 
 */

#import <UIKit/UIKit.h>
#import "Dictionary.h"
  //#import "AppDelegateProtocol.h"
#import <CoreData/CoreData.h>

@class DictionaryDataController;

@interface AppDelegate : NSObject <UIApplicationDelegate, UITabBarControllerDelegate, UINavigationControllerDelegate> {
    UIWindow *window;
    UITabBarController *myTabBarController;
    //DictionaryDataController *dataController;
  
}

@property (nonatomic, retain) IBOutlet UIWindow *window;
@property (nonatomic, retain) IBOutlet UITabBarController *myTabBarController;
@property (nonatomic, retain) IBOutlet UIViewController *SettingsController;

@property (nonatomic, retain) DictionaryDataController *dataController;

@end






