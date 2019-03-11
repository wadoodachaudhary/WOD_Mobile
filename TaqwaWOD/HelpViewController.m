//
//  FontsViewController.m
//  TaqwaWOD
//
//  Created by Wadood Chaudhary on 5/25/14.
//
//

#import "HelpViewController.h"
#import "Utils.h"
#import <QuartzCore/QuartzCore.h>

@interface HelpViewController (){
@private
    NSArray  *images,*hints;
    UIImageView* img,*imageClose;
}
@end

@implementation HelpViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil{
    if (self) {
        // Custom initialization
    }
    return self;
}




- (void)viewDidLoad{
    // Do any additional setup after loading the view.
}

- (void)viewWillAppear{
    
}

- (void) viewDidDisappear:(BOOL)animated {
        NSArray* paths =  [self indexPathsForSelectedRows];
}

- (id) init {
    if (self = [super init]) {
    }
    self.delegate = self;
    self.dataSource = self;
    images = [NSArray arrayWithObjects:
                       [UIImage imageNamed:@"swipe_right.png"],
                       [UIImage imageNamed:@"doubletap.png"],
                       [UIImage imageNamed:@"tap.png"],
                       [UIImage imageNamed:@"swipe_left.png"],
                       [UIImage imageNamed:@"swipe_vertically.png"],
                       [UIImage imageNamed:@"swipe.png"],
                       nil];
    hints = [NSArray arrayWithObjects:@"Swipe right to see the split word translation of the verse instantly",
             @"Double tap or long press to search on the English or Arabic word.",
             @"Tap anywhere on the screen to bring up tools menu to change fonts, size and orienattion of the display.",
             @"Swipe right to go back to the previous screen.",
             @"Swipe vertically to read.",
             @"Swipe left or right to go back to the previous or next screen.",
                       nil];

   img =  [self getImageView:@"swipe_right.png" normalizer:[Utils isiPad]?1:2];
   CGRect rect = [Utils getScreenSize];
    int offsetWidth = rect.size.width/6;
    int offsetHeight = rect.size.height/6;
    self.frame= CGRectMake(offsetWidth, offsetHeight, rect.size.width - offsetWidth*2, rect.size.height-offsetHeight*2);
    self.scrollEnabled = YES;
    self.bounces = YES;
    self.layer.borderWidth = 4;
    self.layer.borderColor = [[UIColor grayColor] CGColor];
    imageClose = [self getImageView:@"close.png" normalizer:[Utils isiPad]?1:2];
    
    //imageClose = [UIImage imageNamed:@"close.png"];
    //imageViewSwipe =[self getImage:@"Swipe.png"];
    //NSLog(@"Here!");
    return self;
}



- (void) loadView {
   
    
}

- (void) dealloc {
    
}

- (UIImage*) getImage:(NSString*) imgName {
    UIImage* image = [UIImage imageNamed:imgName];
    return image;
}


- (UIImageView*) getImageView:(NSString*) imgName normalizer:(int) normalizer{
    UIImage* image = [UIImage imageNamed:imgName];
    //int normalizer=[Utils isiPad]?1:2;
    UIImageView* imageView = [[UIImageView alloc] initWithFrame:CGRectMake(0,0,image.size.width/normalizer,image.size.height/normalizer)];
    imageView.image = image;
    return imageView;
}



#pragma - UITableViewDataSource methods

- (UITableViewCell *) tableView: (UITableView *) theTableView cellForRowAtIndexPath: (NSIndexPath *) indexPath {
    static NSString *CellIdentifier = @"HelpCellIdentifier";
    
    UITableViewCell *cell = [self dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
    	cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
    }
    
    [[cell imageView] setImage:images[indexPath.row]];
    [[cell imageView] setFrame:img.frame];
    
    UILabel* lbl = cell.textLabel;
    lbl.frame=CGRectMake(img.frame.size.width, 0, cell.frame.size.width - img.frame.size.width,cell.frame.size.height );
    lbl.text = hints[indexPath.row];
    [lbl setFont:[UIFont fontWithName:@"Helvetica-Oblique" size:14]];
    lbl.numberOfLines = 0;
    lbl.lineBreakMode = NSLineBreakByWordWrapping;
    return cell;

}

- (void) tableView:(UITableView *) _tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
}

- (NSInteger) numberOfSectionsInTableView: (UITableView *) tableView {
    return 1;
}

- (NSInteger) tableView: (UITableView *) tableView numberOfRowsInSection: (NSInteger) section {
    
    return [images count];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    //if (imageViewSwipe==Nil) imageViewSwipe =[self getImage:@"Swipe.png"];
    //return 30;
    return img.frame.size.height;
    
}
-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    //if (imageViewSwipe==Nil) imageViewSwipe =[self getImage:@"bookmark.png"];
    return imageClose.frame.size.height;
    //return 100;
}


- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    UIView* headerView = [tableView dequeueReusableHeaderFooterViewWithIdentifier:@"HeaderView"];
    if (headerView == Nil) {
        headerView=[[UIView alloc] initWithFrame:CGRectMake(0, 0,self.frame.size.width, imageClose.frame.size.height)];
        UIButton *addButton=[UIButton buttonWithType:UIButtonTypeCustom];
        //addButton.titleLabel.text=@"Instructions";
        addButton.frame=CGRectMake((self.frame.size.width-imageClose.frame.size.width)/2, 0, imageClose.frame.size.width, imageClose.frame.size.height);
        [addButton setImage:imageClose.image forState:UIControlStateNormal];
        [addButton addTarget:self action:@selector(buttonPressed:) forControlEvents:UIControlEventTouchDown];
        //addButton.center = CGPointMake(addButton.frame.size.width / 2, addButton.frame.size.height / 2);
        //[addButton setImageEdgeInsets:UIEdgeInsetsMake(0.0, 0.0, 50.0, 0.0)];
        //[addButton setTitleEdgeInsets:UIEdgeInsetsMake(5.0, 0.0, 0.0, 0.0)];
        [headerView addSubview:addButton];
        
    }
    return headerView;
}

-(IBAction) buttonPressed:(id)sender {
    //NSLog(@"Font Button Touch Down Event Fired for %@:",sender);
    //[self hide]
    [self.superview sendSubviewToBack:self];
    [self removeFromSuperview];
}



//- (NSString *) tableView: (UITableView *) tableView titleForHeaderInSection: (NSInteger) section {
//    return @"Instructions";
//}

#pragma - UITableViewDelegate methods


- (void)tableView:(UITableView *)_tableView didDeselectRowAtIndexPath:(NSIndexPath *)indexPath{
}

@end
