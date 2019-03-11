
/*
 File: SubLevelViewController.m
 Abstract: The view controller for sublevel 2.
 Version: 1.0
 
 */

#import "WordViewController.h"
#import "DefinitionCells.h"
#import "Example.h"
#import "Word.h"
#import "Definition.h"
#import "Language.h"
#import "AppDelegate.h"
#import "DictionaryDataController.h"
#import "RootWord.h"
#import "Utils.h"
#import "Verse.h"
#import "VerseDrawLabel.h"
#import "VerseViewController.h"

@interface WordViewController()

@end

@implementation WordViewController

@synthesize currentSelectionTitle,selectedIndex,selectedWord;
static int instance_id=0;
NSString* cellDefintionNib = @"DefinitionCells";



- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    //NSLog(@"WordViewController ViewWillAppear");
    [self.navigationController setNavigationBarHidden:NO animated:NO];
    //[[UIApplication sharedApplication] setStatusBarHidden:NO withAnimation:UIStatusBarAnimationSlide];
    //[self addToLog];
    
}


- (void)viewDidLoad{
    [super viewDidLoad];
    //    [[NSNotificationCenter defaultCenter] addObserver:self
    //                                             selector:@selector(orientationChanged:)
    //                                                 name:UIDeviceOrientationDidChangeNotification
    //                                               object:nil];
    // self.hidesBottomBarWhenPushed = YES;
    screenRect = [Utils getScreenSize];
    
    //NSLog(@"In WordViewCintroller:an iPad: (x=%f,y=%f,width=%f,height=%f)",rect.origin.x,rect.origin.y,rect.size.width,rect.size.height);
    heightCushion=1.0;
    maximumLabelSize  = CGSizeMake(screenRect.size.width-6,9999);
    
    
    if (selectedWord==nil) {
        
        AppDelegate * theDelegate = (AppDelegate *) [UIApplication sharedApplication].delegate;
        dataController = (DictionaryDataController *)(theDelegate.dataController);
        selectedWord = [dataController wordOfDay];
        [self initialize];
        self.title = @"Word of the Day";
        [dataController registerController:self];
        self.tableView.separatorStyle=UITableViewCellSeparatorStyleNone;
        instance_id++;
        
    }
    
    UISwipeGestureRecognizer *gestureLeft = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(didSwipe:)];
    gestureLeft.direction = UISwipeGestureRecognizerDirectionLeft;
    [self.tableView addGestureRecognizer:gestureLeft];
    
    //
    UISwipeGestureRecognizer *gestureRight = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(didSwipe:)];
    gestureRight.direction = UISwipeGestureRecognizerDirectionRight;
    [self.tableView addGestureRecognizer:gestureRight];
    
    
    
    self.hidesBottomBarWhenPushed=NO;
    //NSLog(@"Here %@",@"In Subcontroller");
    
    //UIBarButtonItem *newBackButton = [[UIBarButtonItem alloc] initWithTitle:@"Back" style:UIBarButtonItemStyleBordered target:self action:@selector(home:)];
    //self.navigationItem.leftBarButtonItem=newBackButton;
    self.navigationItem.hidesBackButton=YES;
    self.title=@"Featured";
    [self setLayout];
}


-(BOOL) prefersStatusBarHidden {
    return NO;
}


-(void) setCallingController:(UIViewController*) viewController {
    callingController=viewController;
    if(callingController !=nil) [self hideBackButton:NO];
}

- (void) hideBackButton:(bool) yesOrNo {
    self.navigationItem.hidesBackButton=yesOrNo;
    
}

-(void)home:(UIBarButtonItem *)sender {
    //[Utils showMessage:@"Back" Message:@"Back Button clicked"];
    //[self.navigationController pushViewController:callingController Animated:YES];
    //[self.navigationController popToViewController:callingController animated:NO];
    //[self.navigationController popViewControllerAnimated:YES];
    
}



- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation{
    NSLog(@"Orientation message recieved.");
    return YES;//(interfaceOrientation == UIInterfaceOrientationPortrait);
}


- (void)didRotateFromInterfaceOrientation:(UIInterfaceOrientation)fromInterfaceOrientation {
    NSLog(@"Rotate Go!");
    [self reset];
    [self.view setContentMode:UIViewContentModeRedraw];
}

- (void) setLayout {
    UIDeviceOrientation deviceOrientation = [UIDevice currentDevice].orientation;
    CGRect rect = [Utils getScreenSize];
    if (UIDeviceOrientationIsLandscape(deviceOrientation) ) {
        maximumLabelSize  = CGSizeMake(rect.size.height-8,9999);
    }
    else if (UIDeviceOrientationIsPortrait(deviceOrientation)) {
        maximumLabelSize  = CGSizeMake(rect.size.width-8,9999);
    }
}
- (void) reset {
    NSLog(@"**** Reset *** ");
    [self setLayout];
    [cellMain removeAllObjects];
    [self dataChanged];
//    [self.view setContentMode:UIViewContentModeRedraw];
//    self.view.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
//    self.view.contentMode = UIViewContentModeRedraw;
//    [self.tableView setContentMode:UIViewContentModeRedraw];
//    [self.tableView reloadData];
//    [self.view setNeedsDisplay];
//    [self.view layoutIfNeeded];
//    [self.view layoutSubviews];
//    [self.tableView setNeedsDisplay];
//    [self.tableView layoutIfNeeded];
//    [self.tableView layoutSubviews];
//    
    
}

- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch {
    if ( [gestureRecognizer isMemberOfClass:[UITapGestureRecognizer class]] ) {
        // Return NO for views that don't support Taps
    } else if ( [gestureRecognizer isMemberOfClass:[UISwipeGestureRecognizer class]] ) {
    
    }
    
    return YES;
}

-(void)didSwipe:(UISwipeGestureRecognizer *)gestureRecognizer {
    
    if (gestureRecognizer.state == UIGestureRecognizerStateEnded) {
        //CGPoint swipeLocation = [gestureRecognizer locationInView:self.tableView];
        //NSIndexPath *swipedIndexPath = [self.tableView indexPathForRowAtPoint:swipeLocation];
        if (gestureRecognizer.direction==UISwipeGestureRecognizerDirectionLeft) {
            NSLog(@"[WordViewController].didSwipe Left");
            //self.title=@"Featured";
            [Utils moveTab:self by:1];
        }
        else if (gestureRecognizer.direction==UISwipeGestureRecognizerDirectionRight) {
            NSLog(@"[WordViewController].didSwipe Right");
//            if ( [Utils isTabController:callingController]) {
//                [self dismissViewControllerAnimated:NO completion:nil];
//            }
//            else
            if (callingController!=nil) {
                [self.navigationController popToRootViewControllerAnimated:YES];
                
            }
            else {
                [Utils moveTab:self by:4];

            }
        }
    }
}





- (void) addToLog{
    NSString* wordID= [dataController getWordID:selectedWord];
    NSString* settings= [NSString stringWithFormat:@"11000%@",[NSString stringWithFormat:@"%06d",[wordID intValue]]];
    [dataController addLog:1 Settings:settings ChapterNo:0 VerseNo:0 WordID:[wordID intValue]  Place:@"addToLog-WordViewController"];
}

- (void)orientationChanged:(NSNotification *)notification {
    
    
    
    UIDeviceOrientation deviceOrientation = [UIDevice currentDevice].orientation;
    CGRect rect = [Utils getScreenSize];
    
    
    
    [self.view setContentMode:UIViewContentModeRedraw];
    self.view.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
    self.view.contentMode = UIViewContentModeRedraw;
    [self.tableView setContentMode:UIViewContentModeRedraw];
    [cellMain removeAllObjects];
    [self.tableView reloadData];
    [self.view setNeedsDisplay];
    [self.view layoutIfNeeded];
    [self.view layoutSubviews];
    [self.tableView setNeedsDisplay];
    [self.tableView layoutIfNeeded];
    [self.tableView layoutSubviews];
    
    
    //self.view.frame=rect;
    //NSLog(@"In VerseViewCintrolle Frame:\(x=%f,y=%f,width=%f,height=%f)",rect.origin.x,rect.origin.y,rect.size.width,rect.size.height);
    
    //[self]V
    //[self adjustViewsForOrientation:[[UIDevice currentDevice] orientation]];
    
    
}


-(NSUInteger)supportedInterfaceOrientations {
    //NSLog(@"Word View Controller Supported Orientation.");
    
    return UIInterfaceOrientationMaskLandscapeRight | UIInterfaceOrientationMaskPortrait | UIInterfaceOrientationMaskLandscapeLeft | UIInterfaceOrientationMaskPortraitUpsideDown;
}








- (id)initWithWord:(Word*) _selectedWord atIndex:(int) _selectedIndex onTitle:(NSString*) _titleToDisplay controller:(DictionaryDataController*) wordDataController NIB:(NSString*) nibName {
    self = [super initWithNibName:nibName bundle:nil];
    if (self) {
        // Custom initialization
        //maximumLabelSize = CGSizeMake(300,9999);
        dataController=wordDataController;
        selectedWord=_selectedWord;
        selectedIndex=_selectedIndex;
        self.title = _titleToDisplay;
        NSLog(@"From initWithWord");
        //[self addToLog];
        //[self initialize];
    }
    return self;
}


-(void)dataChanged {
    [self.tableView reloadData];
    
}

- (void)viewDidUnload {
    [super viewDidUnload];
}

- (void) valueChangedSwitch:(UISwitch*) aSwitch{}
- (void) valueChangedSlider:(UISlider*) aSlider{}


- (void) initialize {
    //NSDateFormatter* dateFormatter = [[NSDateFormatter alloc] init];
    //[dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm:ss.SSS"];
    //cellDefinition_nib = [[NSBundle mainBundle] loadNibNamed:@"DefinitionCells" owner:self options:nil];
    cellDefinition_nib = [[NSBundle mainBundle] loadNibNamed:cellDefintionNib owner:self options:nil];
    
    rows = [[NSMutableArray alloc] init ]; //else [rows removeAllObjects];
    index = [[NSMutableArray alloc] init ]; //else [rows removeAllObjects];
    
    //selectedTransliteration = selectedWord.transliteration;
    NSSet* definitions = selectedWord.definitions;
    
    NSSortDescriptor *dateDescriptor = [NSSortDescriptor sortDescriptorWithKey:@"date" ascending:YES];
    NSArray *sortDescriptors = [NSArray arrayWithObject:dateDescriptor];
    NSArray *sortedDefinitions = [[[NSMutableArray alloc] initWithArray:[definitions allObjects]] sortedArrayUsingDescriptors:sortDescriptors];
    
    
    
    int ct=0;
    int def=0;
    for (Definition *definition in sortedDefinitions) {
        [rows addObject:definition];
        def++;
        [index addObject:[NSNumber numberWithInt:def*100]];
        Language* language = definition.inLanguage;
        //NSLog(@"Date: %@ Definition ID,  %@ Language:%@",[dateFormatter stringFromDate:definition.date],definition.meaning,language.name);
        ct++;
        int exmpl=0;
        if (definition.examples !=nil) {
            ct = ct + (int)[definition.examples count];
            for (Example *example in definition.examples) {
                [rows addObject:example];
                exmpl++;
                [index addObject:[NSNumber numberWithInt:exmpl]];
            }
        }
    }
    verses = selectedWord.verses;
    rowCount = ct+2;
    [self.tableView reloadData];
    
}


#pragma mark -
#pragma mark UITableViewDelegate

- (void)tableView:(UITableView *)tableView accessoryButtonTappedForRowWithIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
    
    
}



#pragma mark -
#pragma mark UITableViewDataSource



- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if (rowCount==0) rowCount=1;
    return rowCount;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    DefinitionCells* cellDefinition;
    UITableViewCell* _cellDefinition;
    int cushion = 3;
    bool rotate=true;
    if (cellMain==nil) cellMain =[[NSMutableArray alloc] initWithCapacity:rowCount];
    if  (indexPath.row < [cellMain count]) {
        _cellDefinition = [cellMain objectAtIndex:indexPath.row];
    }
    if (_cellDefinition !=Nil) return _cellDefinition;
    if (indexPath.row==0) {
        static NSString *kCellID2 = @"Entry";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:kCellID2];
        if (!rotate && cell != nil)	return cell;
        //NSArray* nib = [[NSBundle mainBundle] loadNibNamed:@"DefinitionCells" owner:self options:nil];
        NSArray* nib = [[NSBundle mainBundle] loadNibNamed:cellDefintionNib owner:self options:nil];
        
        cellDefinition = (DefinitionCells *) [nib objectAtIndex:0];
        cellDefinition.controller=self;
        cellDefinition.selectedWord=selectedWord;
        cellDefinition.dataController=dataController;
        [cellDefinition setSelectedBox:EntryBox];
        //
        [cellDefinition word_entry].text =  selectedWord.word;
        [[cellDefinition word_entry] setFont:[UIFont fontWithName:@"AlQalamQuranMajeed2" size:42]];
        UILabel* rootLabel = [cellDefinition root];
        rootLabel.text =  selectedWord.root;
        rootLabel.layer.borderColor = rootLabel.textColor.CGColor;
        rootLabel.layer.borderWidth = 0.75f;
        rootLabel.layer.cornerRadius = 10;
        
        [cellDefinition partsofspeech].text =  [NSString stringWithFormat:@"(%@)",[selectedWord.tag lowercaseString]];
        [cellDefinition transliteration].text =  [NSString stringWithFormat:@"/%@/",selectedWord.transliteration];
        NSArray* relatedWords = [selectedWord.relatedword componentsSeparatedByString:@" "];
        int relatedWordCount =  (int)[relatedWords count];
        cellDefinition.relatedWord1.text=@"";cellDefinition.relatedWord2.text=@"";cellDefinition.relatedWord3.text=@"";
        cellDefinition.relatedWord4.text=@"";cellDefinition.relatedWord5.text=@"";
        UIFont* fontBranch = [UIFont fontWithName:@"AlQalamQuranMajeed2" size:13];
        if  (relatedWordCount>=1) {cellDefinition.relatedWord1.text= (NSString*)[relatedWords objectAtIndex:0]; [cellDefinition.relatedWord1 setFont:fontBranch]; }
        if  (relatedWordCount>=2) {cellDefinition.relatedWord2.text= (NSString*)[relatedWords objectAtIndex:1]; [cellDefinition.relatedWord2 setFont:fontBranch]; }
        if  (relatedWordCount>=3) {cellDefinition.relatedWord3.text= (NSString*)[relatedWords objectAtIndex:2];[cellDefinition.relatedWord3 setFont:fontBranch]; }
        if  (relatedWordCount>=4) {cellDefinition.relatedWord4.text= (NSString*)[relatedWords objectAtIndex:3];[cellDefinition.relatedWord4 setFont:fontBranch]; }
        if  (relatedWordCount>=5) {cellDefinition.relatedWord5.text= (NSString*)[relatedWords objectAtIndex:4];[cellDefinition.relatedWord5 setFont:fontBranch]; }
        
        
        UILabel* trnlitLbl = [cellDefinition transliteration];UILabel* lblPOS= [cellDefinition partsofspeech];
        UILabel* entryLbl = [cellDefinition word_entry];
        CGSize entryLabelSize = [Utils getTextSize:1.0 forText:selectedWord.word fontToSize:entryLbl.font rectSize:maximumLabelSize];
        //[selectedWord.word sizeWithAttributes:@{NSFontAttributeName:entryLbl.font}];
        //CGSize transliterationLabelSize = [[NSString stringWithFormat:@"/%@/",selectedWord.transliteration]
                                           //sizeWithAttributes:@{NSFontAttributeName:trnlitLbl.font];
        
        CGSize transliterationLabelSize = [Utils getTextSize:1.0 forText:lblPOS.text
                                    fontToSize:trnlitLbl.font rectSize:maximumLabelSize];
        
        CGSize labelSizePOS =[Utils getTextSize:1.0 forText:[NSString stringWithFormat:@"/%@/",selectedWord.transliteration]
                                     fontToSize:lblPOS.font rectSize:maximumLabelSize];
        //[lblPOS.text sizeWithAttributes:@{NSFontAttributeName:lblPOS.font}];
        
        //int endPos=trnlitLbl.frame.origin.x+trnlitLbl.frame.size.width;
        int endPos=screenRect.size.width;
        
        int startPos=endPos - entryLabelSize.width - cushion*2;
        //NSLog(@"%@:%i:%i",selectedWord.word,endPos,startPos);
        //
        entryLbl.frame  = CGRectMake(startPos, entryLbl.frame.origin.y, entryLabelSize.width+cushion, entryLabelSize.height);
        int yPOS=entryLbl.frame.origin.y+entryLbl.frame.size.height+cushion;
        trnlitLbl.frame = CGRectMake(startPos, yPOS, entryLabelSize.width+cushion, transliterationLabelSize.height);
        yPOS=yPOS+transliterationLabelSize.height;
        lblPOS.frame = CGRectMake(MIN(startPos,maximumLabelSize.width - labelSizePOS.width), yPOS, labelSizePOS.width+cushion, labelSizePOS.height);
        UIButton* audio = [cellDefinition audio];
        UIButton* favorite = [cellDefinition favorite];
        //favorite.hidden=TRUE;
        if ([selectedWord.favorite isEqualToString:_YES]) {
            [favorite setImage:[UIImage imageNamed:IMG_FAV_RMV] forState:UIControlStateNormal];
        }
        else {
            [favorite setImage:[UIImage imageNamed:IMG_FAV_ADD] forState:UIControlStateNormal];
        }
        audio.frame = CGRectMake(startPos - audio.frame.size.width -10 , audio.frame.origin.y, audio.frame.size.height,audio.frame.size.height);
        favorite.frame = CGRectMake(startPos- audio.frame.size.width - favorite.frame.size.width, favorite.frame.origin.y, favorite.frame.size.height,favorite.frame.size.height);
    }
    else if (indexPath.row>=1 && indexPath.row < rowCount-1) {
        NSObject* obj = [rows objectAtIndex:indexPath.row-1];
        int seqNo = [(NSNumber*)[index objectAtIndex:indexPath.row-1] intValue];
        if ([obj isKindOfClass:[Definition class]]) {
            static NSString *kCellID2 = @"Definition";
            //UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:kCellID2];
            //if (cell != nil)	return cell;
            //NSArray* nib = [[NSBundle mainBundle] loadNibNamed:@"DefinitionCells" owner:self options:nil];
            NSArray* nib = [[NSBundle mainBundle] loadNibNamed:cellDefintionNib owner:self options:nil];
            
            cellDefinition = (DefinitionCells *) [nib objectAtIndex:1];
            [cellDefinition setSelectedBox:DefinitionBox];
            UILabel* hdrDef = [cellDefinition hdrDef];
            UILabel* definitionLabel = [cellDefinition definition];
            Definition* definition = (Definition*)[rows objectAtIndex:indexPath.row-1];
            Language* language = definition.inLanguage;
            //definitionLabelHdr.text=[NSString stringWithFormat:@"Definition (%@)",language.name];
            hdrDef.text=[NSString stringWithFormat:@"%i.",seqNo/100];
            NSString* text = [NSString stringWithFormat:@"%@%@ %@",@"\u200E",hdrDef.text,definition.meaning];
            //CGSize hdrDefSize = [hdrDef.text sizeWithFont:hdrDef.font constrainedToSize:maximumLabelSize lineBreakMode:hdrDef.lineBreakMode];
            //hdrDef.font = [Utils isiPad]?[UIFont fontWithName:hdrDef.font.fontName size:hdrDef.font.pointSize+2]:hdrDef.font;
            CGSize hdrDefSize = [Utils getTextSize:heightCushion forText:hdrDef.text  fontToSize:hdrDef.font rectSize:maximumLabelSize];
            hdrDef.frame = CGRectMake(5.0, 3.0f, 0.0f,0.0f);//hdrDefSize.width, hdrDefSize.height);
            //CGSize definitionLabelSize = [text sizeWithFont:definitionLabel.font constrainedToSize:maximumLabelSize lineBreakMode:definitionLabel.lineBreakMode];
            //definitionLabel.font = [UIFont fontWithName:@"AlQalamQuranMajeed2" size:13];
            definitionLabel.font = [Utils isiPad]?[UIFont fontWithName:definitionLabel.font.fontName size:definitionLabel.font.pointSize+2]:definitionLabel.font;
            text = [Utils trim:text];
            CGSize definitionLabelSize = [Utils getTextSize:heightCushion forText:text  fontToSize:definitionLabel.font rectSize:maximumLabelSize];
            definitionLabel.text = text;
            definitionLabel.frame = CGRectMake(2.0f, 0.0f, definitionLabelSize.width, definitionLabelSize.height);
        }
        else {
            static NSString *kCellID2 = @"Example";
            //UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:kCellID2];
            //if (cell != nil)	return cell;
            //NSArray* nib = [[NSBundle mainBundle] loadNibNamed:@"DefinitionCells" owner:self options:nil];
            NSArray* nib = [[NSBundle mainBundle] loadNibNamed:cellDefintionNib owner:self options:nil];
            
            
            Example* example = (Example*)[rows objectAtIndex:indexPath.row-1];
            cellDefinition = (DefinitionCells *) [nib objectAtIndex:2];
            [cellDefinition setSelectedBox:ExampleBox];
            UILabel* exampleLabel = [cellDefinition example];
            UILabel* hdrRef = [cellDefinition hdrRef]; UILabel* ref = [cellDefinition reference];UILabel* hdrExample= [cellDefinition hdrExample];
            ref.text=example.reference;
            //CGSize exampleLabelSize = [example.example sizeWithFont:exampleLabel.font constrainedToSize:maximumLabelSize lineBreakMode:exampleLabel.lineBreakMode];
            CGSize exampleLabelSize = [Utils getTextSize:heightCushion forText:example.example  fontToSize:exampleLabel.font rectSize:maximumLabelSize];
            
            NSString* rtlExample = [@"\u200E" stringByAppendingString:example.example];
            //[Utils printSize:example.example size:exampleLabelSize ];
            exampleLabel.text = rtlExample ;
            bool last = indexPath.row == [index count];
            if (!last) {
                int nextSeqNo = [(NSNumber*)[index objectAtIndex:indexPath.row] intValue];
                if (nextSeqNo > 100 || nextSeqNo <= seqNo) last=true;
            }
            if (seqNo>1) {
                hdrExample.hidden=YES;
                exampleLabel.frame = CGRectMake(10.0f, 2.0f, exampleLabelSize.width, exampleLabelSize.height);
            }
            else {
                [cellDefinition hdrExample].hidden=NO;
                exampleLabel.frame = CGRectMake(10.0f, 25.0f, exampleLabelSize.width, exampleLabelSize.height);
            }
            if (last) {
                ref.frame    = CGRectMake(ref.frame.origin.x, exampleLabel.frame.origin.y+exampleLabelSize.height+5, ref.frame.size.width, ref.frame.size.height);
                hdrRef.frame = CGRectMake(hdrRef.frame.origin.x, exampleLabel.frame.origin.y+exampleLabelSize.height+5, hdrRef.frame.size.width, hdrRef.frame.size.height);
                if (seqNo==1) hdrExample.text=@"Example";
                ref.hidden=NO;hdrRef.hidden=NO;
            }
            else {
                ref.hidden=YES;hdrRef.hidden=YES;
            }
        }
    }
    else if (indexPath.row == rowCount-1) {
        static NSString *kCellID2 = @"Occurences";
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:kCellID2];
        if (!rotate && cell != nil)	return cell;
        //NSArray* nib = [[NSBundle mainBundle] loadNibNamed:@"DefinitionCells" owner:self options:nil];
        NSArray* nib = [[NSBundle mainBundle] loadNibNamed:cellDefintionNib owner:self options:nil];
        
        cellDefinition = (DefinitionCells *) [nib objectAtIndex:3];
        [cellDefinition setSelectedBox:VersesBox];
        cellDefinition.selectedWord=selectedWord;
        cellDefinition.dataController=dataController;
        cellDefinition.controller=self;
        cellDefinition.refcount.text= [NSString stringWithFormat:@"%i",[selectedWord.versecount intValue]];
    }
    //NSLog(@"Row:%i",indexPath.row);
    [cellMain addObject:cellDefinition];
    return cellDefinition;
}


-(void) showVerses {
    if (verseViewController==nil) {
        verseViewController = [[VerseViewController alloc] initWithWord:selectedWord controller:dataController NIB:@"VerseViewController"];
        verseViewController.hidesBottomBarWhenPushed = YES;
    }
    verseViewController.title=@"Verse Viewer (From Word)";
    [(VerseViewController*)verseViewController setCallingController:nil];
    //verseViewController
    [self.navigationController pushViewController:verseViewController animated:YES];
    
    
}

-(void) showRoot {
    int rootWordCount = [dataController getRootWordCountFor:selectedWord.root];
    if (rootWordCount>0) {
        RootWord *rootwordViewController = [[RootWord alloc] initWithRootWord:selectedWord.root controller:dataController NIB:@"RootWord"];
        [self.navigationController pushViewController:rootwordViewController animated:YES];
    }
    else {
        UIAlertView *message  = [[UIAlertView alloc] initWithTitle:@"Value" message:@"No verses found for the root." delegate:nil cancelButtonTitle:@"OK"  otherButtonTitles:nil];
        [message show];
    }
}
- (void) event:(enum EVENTS)evt Sender:(id) sender{
    if (evt==ShowVerse) {
        [self showVerses];
    }  else if (evt==ShowRoot) {
        //NSLog(@"Button Touch Down Event Fired for Root.");
        [self showRoot];
    }
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    //NSLog(@"Index Path:%i",indexPath.row);
    if (indexPath.row ==rowCount-1) {
        DefinitionCells* cellDefinition = (DefinitionCells *) [cellDefinition_nib objectAtIndex:3];
        UIButton* btn= [cellDefinition showall];
        return btn.frame.origin.y+btn.frame.size.height+30;
    }
    else if (indexPath.row==0) {
        DefinitionCells* cellDefinition = (DefinitionCells *) [cellDefinition_nib objectAtIndex:0];
        UILabel* entryLbl = [cellDefinition partsofspeech];
        //CGSize entryLabelSize = [selectedWord.word sizeWithFont:entryLbl.font constrainedToSize:maximumLabelSize lineBreakMode:entryLbl.lineBreakMode];
        return entryLbl.frame.size.height+entryLbl.frame.origin.y+20;
    }
    else if (indexPath.row>=1) {
        NSObject* obj = [rows objectAtIndex:indexPath.row-1];
        int seqNo = [(NSNumber*)[index objectAtIndex:indexPath.row-1] intValue];
        if ([obj isKindOfClass:[Definition class]]) {
            Definition* definition = (Definition*)[rows objectAtIndex:indexPath.row-1];
            //NSArray* nib = [[NSBundle mainBundle] loadNibNamed:@"DefinitionCells" owner:self options:nil];
            DefinitionCells* cellDefinition = (DefinitionCells *) [cellDefinition_nib objectAtIndex:1];
            UILabel* definitionLabel = [cellDefinition definition];
            NSString* text = [NSString stringWithFormat:@"%i.  %@",seqNo/100,definition.meaning];
            //CGSize definitionLabelSize = [text sizeWithFont:defLbl.font constrainedToSize:maximumLabelSize lineBreakMode:defLbl.lineBreakMode];
            //CGSize definitionLabelSize = [Utils getTextSize:heightCushion forText:text  fontToSize:defLbl.font rectSize:maximumLabelSize];
            UIFont* font = [Utils isiPad]?[UIFont fontWithName:definitionLabel.font.fontName size:definitionLabel.font.pointSize+2]:definitionLabel.font;
            text = [Utils trim:text];
            CGSize definitionLabelSize = [Utils getTextSize:heightCushion forText:text  fontToSize:font rectSize:maximumLabelSize];
            return definitionLabelSize.height+10;
        }
        else  {
            Example* example = (Example*)[rows objectAtIndex:indexPath.row-1];
            DefinitionCells* cellDefinition = (DefinitionCells *) [cellDefinition_nib objectAtIndex:2];
            UILabel* exampleLbl = [cellDefinition example];
            //CGSize exampleLabelSize = [example.example sizeWithFont:exampleLbl.font constrainedToSize:maximumLabelSize lineBreakMode:exampleLbl.lineBreakMode];
            CGSize exampleLabelSize = [Utils getTextSize:heightCushion forText:example.example  fontToSize:exampleLbl.font rectSize:maximumLabelSize];
            
            //NSLog(@"Example Height = %f %@",exampleLabelSize.height,example.example);
            int yOffset=15;
            bool last = indexPath.row == [index count];
            if (!last) {
                int nextSeqNo = [(NSNumber*)[index objectAtIndex:indexPath.row] intValue];
                if (nextSeqNo > 100 || nextSeqNo <= seqNo) last=true;
            }
            if (seqNo==1) {
                yOffset=yOffset+15;
            }
            if (last) {
                yOffset=yOffset+15;
            }
            return exampleLabelSize.height+yOffset;
        }
    }
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    
    return 1;
}



#pragma mark -
#pragma mark UIViewControllerRotation

@end
