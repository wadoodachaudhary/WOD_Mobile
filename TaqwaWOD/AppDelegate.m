/*
 File: AppDelegate.m 
 
 */

#import "AppDelegate.h"
#import "DictionaryDataController.h"
#import "Favorite.h"
#import "Dictionary.h"
#import "Subscribe.h"
#import "WOD-Swift.h"
    //#import "AppDelegateProtocol.h"


NSString *kWhichTabPrefKey		= @"kWhichTab";
NSString *kTabBarOrderPrefKey	= @"kTabBarOrder";

#define kDefaultTabSelection 0	// default tab value is 0 (tab #1)



@implementation AppDelegate

@synthesize window, myTabBarController,SettingsController;
@synthesize dataController;

int progarmId=0;
int screen_id=0;

//- (void)applicationDidFinishLaunchingWithOptions:(UIApplication *)application {
- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    if (1==1) return;
    
    dataController = [[DictionaryDataController alloc] init];
    ReferenceByUser* referenceByUser = [ReferenceByUser Init];
    [referenceByUser test];
    //NSMutableArray* screen = [dataController getLastScreenFor:-1];
    //if (screen!=nil) screen_id = [[screen objectAtIndex:1] intValue]; else screen_id=4;
    if ([[dataController getUser] length]==0) {
        UITabBarController *tab = self.myTabBarController;
        NSInteger selectedIndex=0;
        for (UITabBarItem* item in tab.tabBar.items) {
            if (item.tag==5) [tab setSelectedIndex:selectedIndex];
            selectedIndex++;
        }
    }else  screen_id=0;
    
    [self.window setRootViewController:myTabBarController];
    
    //[window makeKeyAndVisible];
    [[UIDevice currentDevice] beginGeneratingDeviceOrientationNotifications];
    NSUInteger testValue = [[NSUserDefaults standardUserDefaults] integerForKey:kWhichTabPrefKey];	// test for "kWhichTabPrefKey" key value
    testValue=0;
   
    if (1==1) return;
    if (testValue == 0)    {
        NSDictionary *appDefaults =  [NSDictionary dictionaryWithObjectsAndKeys:[NSNumber numberWithInt:kDefaultTabSelection], kWhichTabPrefKey,nil];
        [[NSUserDefaults standardUserDefaults] registerDefaults:appDefaults];
    }
    self.myTabBarController.moreNavigationController.navigationBar.tintColor = [UIColor grayColor];
    NSArray* classNames = [[NSUserDefaults standardUserDefaults] arrayForKey:kTabBarOrderPrefKey];
    if (classNames.count > 0) {
        NSMutableArray* controllers = [[NSMutableArray alloc] init];
        for (NSString* className in classNames) {
            for (UIViewController* controller in self.myTabBarController.viewControllers) {
                NSString* controllerClassName = nil;
                if ([controller isKindOfClass:[UINavigationController class]]) {
                    controllerClassName = NSStringFromClass([[(UINavigationController*)controller topViewController] class]);
                }
                else {
                    controllerClassName = NSStringFromClass([controller class]);
                }
                NSLog(@"controllerClassName:%@",controllerClassName);
                if ([className isEqualToString:controllerClassName]) {
                    [controllers addObject:controller];
                    break;
                }
            }
        }
        if (controllers.count == self.myTabBarController.viewControllers.count) {
            self.myTabBarController.viewControllers = controllers;
        }
        
        
            //[controllers release];
    }
    
        // re-store previously selected tab from prefs
        //
        //	if ([[NSUserDefaults standardUserDefaults] integerForKey:kWhichTabPrefKey] == NSNotFound) {
        //		self.myTabBarController.selectedViewController = self.myTabBarController.moreNavigationController;
        //	}
        //	else
        //	{
        //		self.myTabBarController.selectedIndex = [[NSUserDefaults standardUserDefaults] integerForKey:kWhichTabPrefKey];
        //	}
    
        // listen for changes in view controller from the More screen
    self.myTabBarController.moreNavigationController.delegate = self;
        //self.myTabBarController.selectedIndex=0;
    
        //Managed object management
    
    
        //	NSManagedObjectContext *context = [self managedObjectContext];
        //	if (!context) {
        //		// Handle the error.
        //	}
   	
    
}

- (NSString *) getValue {
    return @"Allah is Great";
}



- (id) init; {
    return [super init];
}



- (void)saveTabOrder {
    NSMutableArray* classNames = [[NSMutableArray alloc] init];
    for (UIViewController* controller in self.myTabBarController.viewControllers) {
        if ([controller isKindOfClass:[UINavigationController class]]) {
            UINavigationController *navController = (UINavigationController *)controller;
            [classNames addObject:NSStringFromClass([navController.topViewController/*•• figure out how to use: visibleViewController*/ class])];
        }
        else {
            [classNames addObject:NSStringFromClass([controller class])];
        }
    }
    
    [[NSUserDefaults standardUserDefaults] setObject:classNames forKey:kTabBarOrderPrefKey];
}

- (void)applicationDidBecomeActive:(UIApplication *)application {
    [dataController getNewItemsFromCloud];
    [dataController refreshWordOfDay];
    [dataController downloadUserDataFromCloud];
}

- (void)applicationDidEnterBackground:(UIApplication *)application {
    //NSLog(@"**** [AppDelegate] applicationDidEnterBackground");
    [self saveTabOrder];
    [dataController uploadUserDataToCloud];
    
}

- (void)applicationWillTerminate:(UIApplication *)application {
        // This will store off tab ordering in all iOS versions.
    //NSLog(@"**** [AppDelegate] applicationWillTerminate");
    //[dataController uploadUserDataToCloud];

    [self saveTabOrder];
}

- (void)dealloc
{
        //[dataController e];
        //[window release];
        //[super dealloc];
}


#pragma mark -
#pragma mark UITabBarControllerDelegate

- (void)tabBarController:(UITabBarController *)tabBarController didSelectViewController:(UIViewController *)viewController {
        // store the selected tab for next time:
        //		normally we can do this at "applicationDidTerminate", but this is a convenient spot
        // note: if the user has the "More" tab selected, then the value stored is "NSNotFound"
        //
    [[NSUserDefaults standardUserDefaults] setInteger:[tabBarController selectedIndex] forKey:kWhichTabPrefKey];
}


#pragma mark -
#pragma mark UINavigationControllerDelegate (More screen)

- (void)navigationController:(UINavigationController *)navigationController willShowViewController:(UIViewController *)viewController animated:(BOOL)animated
{
    if (viewController == [self.myTabBarController.moreNavigationController.viewControllers objectAtIndex:0])
    {
        NSLog(@"-> returned to More page");
    }
        //dictionary.dataController=aDataController;
}



@end

