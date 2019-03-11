//
//  Settings.m
//  Tabster
//
//  Created by Wadood Chaudhary on 5/1/12.
//  Copyright (c) 2012 Ahmadiyya Muslim Community - Taqwa Group. All rights reserved.
//

#import "Subscribe.h"
#import "DictionaryDataController.h"
#import "Constants.h"
#import "AppDelegate.h"
#import "Utils.h"
#import "FlatButton.h"

@implementation Subscribe
@synthesize email,subscribe,dataController,firstname,lastname,buttonSave,displayFrame;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
      //[[NSUserDefaults standardUserDefaults] removeObjectForKey:@"AppleLanguages"];
        //[[NSUserDefaults standardUserDefaults] registerDefaults:[NSDictionary dictionaryWithObject:[NSArray arrayWithObjects:@"ar",@"ar", @"en", nil] forKey:@"AppleLanguages"]];
      //[[NSUserDefaults standardUserDefaults] setObject:[NSArray arrayWithObjects:@"en", nil] forKey:@"AppleLanguages"];
    
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    self.navigationItem.hidesBackButton=NO;
    
    return self;
}

- (id)initSignup:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    self.navigationItem.hidesBackButton=YES;
    return self;
}


- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

- (void)viewDidLoad {
    [super viewDidLoad];
    AppDelegate * theDelegate = (AppDelegate *) [UIApplication sharedApplication].delegate;
        //if (theDelegate==nil) NSLog(@"Delegate not found!");
    dataController = theDelegate.dataController;
    NSMutableArray* subscriptionInfo = [dataController getSubscription];
    if (subscriptionInfo != nil) {
        email.text=[subscriptionInfo objectAtIndex:0];
        subscribe.on=[[subscriptionInfo objectAtIndex:0] intValue];
    }
    UIButton* saveButton = [[FlatButton alloc] initWithFrame:buttonSave.frame withBackgroundColor:[Utils getGreen]];
    saveButton.layer.cornerRadius = 10;
    [saveButton setTitle:@"Signup" forState:UIControlStateNormal];
    saveButton.titleLabel.font = [UIFont fontWithName:@"Avenir-Black" size:16.0f];
    [saveButton addTarget:self action:@selector(buttonTouchDown:) forControlEvents:UIControlEventTouchUpInside];
    [displayFrame addSubview:saveButton];
    buttonSave.hidden=YES;
    //self.navigationItem.hidesBackButton=YES;
    
}

- (void)viewDidUnload{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

- (BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text {
    
    if([text isEqualToString:@"\n"]) {
        [textView resignFirstResponder];
        return NO;
    }
    
    return YES;
}


-(IBAction) buttonTouchDown:(id)sender {
    if ([sender isKindOfClass:[UIButton class]]) {
        [dataController updateSubscription:subscribe.isOn email:email.text];
        UITabBarController* tabController = [Utils getTabController];
        [tabController setSelectedIndex:0];
        //[self dismissViewControllerAnimated:NO completion:NULL];
        [self.navigationController popViewControllerAnimated:NO];
    }
    else {
        NSLog(@"Button Touch Down Event Fired for switcher: email:%@",email.text);
    }     
    
}

-(IBAction)dismissKeyboard: (id)sender {
    [sender resignFirstResponder];
}


@end
