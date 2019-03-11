/*
     File: main.m
 Abstract: Main source file for the Quran Word a day.
 Copyright (C) 2012 Ahmadiyya Muslim Community. All Rights Reserved.
 
 */

#import <UIKit/UIKit.h>
#import <UIKit/UIView.h>

@class UIKeyboardLayout, UIKeyboardInputManager, UIDelayedAction, NSMutableDictionary, UITextInputTraits, NSArray, NSTimer, UIKeyboardLanguageIndicator, CandWord, NSString, UIAutocorrectInlinePrompt;
@class UIKeyboardCandidate, UITextInputArrowKeyHistory, NSMutableArray;
@protocol UIKeyboardCandidateList, UIKeyboardInput, UIKeyboardRecording, UIApplicationEventRecording;
@protocol UIKeyInput, UIKeyboardImplGeometryDelegate;

int main(int argc, char *argv[]) {
//    if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad) {
//        NSLog(@"In Main --- The device is an iPad running iOS 3.2 or later.");
//    }
//    else {
//        NSLog(@"In Main --- The device is an iPhone or iPod touch.");
//    }
   // NSAutoreleasePool *pool = [[NSAutoreleasePool alloc] init];
   //[[NSUserDefaults standardUserDefaults] removeObjectForKey:@"AppleLanguages"];
        //[[NSUserDefaults standardUserDefaults] registerDefaults:[NSDictionary dictionaryWithObject:[NSArray arrayWithObjects:@"ar",@"ar", @"en", nil] forKey:@"AppleLanguages"]];
        //[[NSUserDefaults standardUserDefaults] setObject:[NSArray arrayWithObjects:@"ar",@"en", nil] forKey:@"AppleLanguages"];
        //[[NSUserDefaults standardUserDefaults] synchronize];
        //[[UIKeyboardImpl sharedInstance] setInputMode:@"ar"];
    
    int retVal = UIApplicationMain(argc, argv, nil, @"AppDelegate");
    
    //[pool release];0-	
    return retVal;
}
