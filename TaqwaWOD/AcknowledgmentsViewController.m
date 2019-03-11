//
//  AcknowledgmentsViewController.m
//  TaqwaWOD
//
//  Created by Wadood Chaudhary on 7/23/12.
//  Copyright (c) 2012 Instinet. All rights reserved.
//

#import "AcknowledgmentsViewController.h"

@interface AcknowledgmentsViewController ()

@end

@implementation AcknowledgmentsViewController
@synthesize webview;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    NSString* fileString = [[NSBundle mainBundle] pathForResource: @"Acknowledgments" ofType: @"html"];
    NSString* htmlString = [[NSString alloc] initWithContentsOfFile: fileString encoding: NSASCIIStringEncoding error: NULL];
    
    NSString *path = [[NSBundle mainBundle] bundlePath];
    NSURL *baseURL = [NSURL fileURLWithPath:path];
    [self.webview loadHTMLString:htmlString baseURL:baseURL];
    // Do any additional setup after loading the view from its nib.
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

@end
