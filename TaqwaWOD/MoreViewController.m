    //
    //  MoreViewController.m
    //  Tabster
    //
    //  Created by Wadood Chaudhary on 5/14/12.
    //  Copyright (c) 2012 Ahmadiyya Muslim Community - Taqwa Group. All rights reserved.
    //

#import "MoreViewController.h"
#import "Subscribe.h"
#import "SearchByRoot.h"
#import "SearchByIndex.h"
#import "BrowserViewController.h"
#import "Recent.h"
#import "AcknowledgmentsViewController.h"
#import "Utils.h"


@interface MoreViewController ()

@end

@implementation MoreViewController

- (id)initWithStyle:(UITableViewStyle)style {
    self = [super initWithStyle:style];
    if (self) {
            // Custom initialization
    }
    return self;
}

- (void)viewDidLoad{
    [super viewDidLoad];
    self.title=@"More";
    
        // Uncomment the following line to preserve selection between presentations.
        // self.clearsSelectionOnViewWillAppear = NO;
    
        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem;
    UISwipeGestureRecognizer *gestureLeft = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(didSwipe:)];
    gestureLeft.direction = UISwipeGestureRecognizerDirectionLeft;
    [self.view addGestureRecognizer:gestureLeft];
    UISwipeGestureRecognizer *gestureRight = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(didSwipe:)];
    gestureRight.direction = UISwipeGestureRecognizerDirectionRight;
    [self.view addGestureRecognizer:gestureRight];
    
    
}

- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldRecognizeSimultaneouslyWithGestureRecognizer:(UIGestureRecognizer*) otherGestureRecognizer{
    return YES;
}



- (void)viewDidUnload {
    [super viewDidUnload];
        // Release any retained subviews of the main view.
        // e.g. self.myOutlet = nil;
}


- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:YES];
    DictionaryDataController* dataController = [Utils getDataModel];
    NSString* user = [dataController getUser];
    if ([user length]==0) {
        Subscribe *subscribeViewController = [[Subscribe alloc] initSignup:@"Subscribe" bundle:nil];
        subscribeViewController.navigationController.hidesBottomBarWhenPushed=YES;
        subscribeViewController.navigationController.navigationBar.hidden=YES;
        [self.navigationController pushViewController:subscribeViewController animated:NO];
        self.navigationItem.hidesBackButton=YES;
    }
    
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
        // Return the number of sections.
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
        // Return the number of rows in the section.
    return 4;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    if (indexPath.row==0) {
        SearchByRoot *searchRootViewController = [[SearchByRoot alloc] initWithNibName:@"SearchByRoot" bundle:nil];
        [self.navigationController pushViewController:searchRootViewController animated:YES];
        //[self presentViewController:searchRootViewController animated:YES completion:NULL];

    }
    else if (indexPath.row==1) {
        Subscribe *subscribeViewController = [[Subscribe alloc] initWithNibName:@"Subscribe" bundle:nil];
        subscribeViewController.navigationController.hidesBottomBarWhenPushed=NO;
        subscribeViewController.navigationController.navigationBar.hidden=NO;
         self.navigationItem.hidesBackButton=NO;
        [self.navigationController pushViewController:subscribeViewController animated:YES];
         //[self presentViewController:subscribeViewController animated:YES completion:NULL];
    }
    else if (indexPath.row==2) {
        AcknowledgmentsViewController *ack = [[AcknowledgmentsViewController alloc] initWithNibName:@"AcknowledgmentsViewController" bundle:nil];
        [self.navigationController pushViewController:ack animated:YES];
         //[self presentViewController:ack animated:YES completion:NULL];
    }
    else if (indexPath.row==3) {
        // Email Subject
        NSString *emailTitle = @"Feedback";
        // Email Content
        NSString *messageBody = @"Feedback";
        // To address
        NSArray *toRecipents = [NSArray arrayWithObject:@"56684b6f31590b3bd5fd19299d11d396_ijkustcefuytcnrqha2dqoi@n.testflightapp.com"];
        
        MFMailComposeViewController *mc = [[MFMailComposeViewController alloc] init];
        mc.mailComposeDelegate = self;
        [mc setSubject:emailTitle];
        [mc setMessageBody:messageBody isHTML:NO];
        [mc setToRecipients:toRecipents];
        
        // Present mail view controller on screen
        [self presentViewController:mc animated:YES completion:NULL];
        //[self.navigationController pushViewController:mc animated:YES];
        
    }
}

-(void)didSwipe:(UISwipeGestureRecognizer *)gestureRecognizer {
    NSLog(@"[FavoriteViewController].didSwipe Left");
    
    if (gestureRecognizer.state == UIGestureRecognizerStateEnded) {
        if (gestureRecognizer.direction==UISwipeGestureRecognizerDirectionLeft) {
            [Utils moveTab:self by:-4];
        }
        else if (gestureRecognizer.direction==UISwipeGestureRecognizerDirectionRight) {
            [Utils moveTab:self by:-1];
        }
    }
}



- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *CellIdentifier = @"Cell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
        cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        cell.selectionStyle = UITableViewCellSelectionStyleBlue;
    }
    if (indexPath.row==0) {
        cell.textLabel.text=@"Search By Root";
    }
    else if (indexPath.row==1) {
        cell.textLabel.text=@"Subscribe to daily emails";
    }
   else if (indexPath.row==2) {
        cell.textLabel.text=@"Acknowledgments";
    }
    else if (indexPath.row==3) {
        cell.textLabel.text=@"Feedback";
    }
    return cell;
}

-(BOOL) prefersStatusBarHidden {
    return NO;
}




- (void) mailComposeController:(MFMailComposeViewController *)controller didFinishWithResult:(MFMailComposeResult)result error:(NSError *)error {
    switch (result) {
        case MFMailComposeResultCancelled:
            NSLog(@"Mail cancelled");
            break;
        case MFMailComposeResultSaved:
            NSLog(@"Mail saved");
            break;
        case MFMailComposeResultSent:
            NSLog(@"Mail sent");
            break;
        case MFMailComposeResultFailed:
            NSLog(@"Mail sent failure: %@", [error localizedDescription]);
            break;
        default:
            break;
    }
    
    // Close the Mail Interface
    [self dismissViewControllerAnimated:YES completion:NULL];
}

/*
 // Override to support conditional editing of the table view.
 - (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
 {
 // Return NO if you do not want the specified item to be editable.
 return YES;
 }
 */

/*
 // Override to support editing the table view.
 - (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
 {
 if (editingStyle == UITableViewCellEditingStyleDelete) {
 // Delete the row from the data source
 [tableView deleteRowsAtIndexPaths:[NSArray arrayWithObject:indexPath] withRowAnimation:UITableViewRowAnimationFade];
 }   
 else if (editingStyle == UITableViewCellEditingStyleInsert) {
 // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
 }   
 }
 */

/*
 // Override to support rearranging the table view.
 - (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath
 {
 }
 */

/*
 // Override to support conditional rearranging of the table view.
 - (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath
 {
 // Return NO if you do not want the item to be re-orderable.
 return YES;
 }
 */


@end
