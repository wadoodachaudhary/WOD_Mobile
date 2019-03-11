    //
    //  DefinitionCells.m
    //  Tabster
    //
    //  Created by Wadood Chaudhary on 4/19/12.
    //  Copyright (c) 2012 Ahmadiyya Muslim Community - Taqwa Group. All rights reserved.
    //

#import "DefinitionCells.h"
#import "Constants.h"
#import "WODTableViewConroller.h"
#import "VerseViewController.h"


@implementation DefinitionCells
@synthesize selectedBox,definition,word_entry,transliteration,example,favorite,audio,reference,selectedWord,dataController,sliderVerses,minVerseIndex,maxVerseIndex,verseIndex,verseId;
@synthesize verses,root,partsofspeech, hdrDef,hdrExample,hdrRef;
@synthesize msgdisplaycount,refcount,relatedWord1,relatedWord2,relatedWord3,relatedWord4,relatedWord5,rootword,rootwordMeaning,relatedWord,relatedWordMeaning,showall,controller,childrenwords,btnRoot;
@synthesize switchArabic,switchTranslation,switchTranslationSplitWord;
- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier{
    
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
            // Initialization code
        root.userInteractionEnabled = YES;
        UITapGestureRecognizer *tapGesture = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(rootTap)];
        [root addGestureRecognizer:tapGesture];
    }
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];
    
}

- (void)drawRect:(CGRect)rect {
    NSObject* obj;
    
    if (selectedBox==EntryBox) {
        CGContextRef ref = UIGraphicsGetCurrentContext();
        [super drawRect:rect];
        CGContextSetRGBFillColor(ref, 0.0f, 0.0f, 0.0f, 1.0f); // set color to black again after super.
        //CGRect writeInHere = CGRectMake(0,0,100,20);
            //[@"teststring" drawInRect:writeInHere withFont:[UIFont fontWithName:@"Helvetica" size:12] lineBreakMode:UILineBreakModeTailTruncation];
    }  
}

- (void)rootTap:(UITapGestureRecognizer*)recognizer {
        // Do Your thing. 
    NSLog(@"Tap gesture Fired for Root.");
    if (recognizer.state == UIGestureRecognizerStateEnded)  {

    }
}


- (void)valueChangedSwitch:(UISwitch*)aSwitch{
        //BOOL flag = theSwitch.on;
        //NSLog(@"Value changed for %i:",theSwitch.tag);
    [controller valueChangedSwitch:aSwitch];
}

- (IBAction) valueChangedSlider: (UISlider *) verseSlider {
    [controller event:ValueChanged Sender:verseSlider];
}



-(IBAction) buttonTouchDown:(id)sender {
        //NSLog(@"Button Touch Down Event Fired for %@:",sender);
    if (sender==btnRoot) {
            //NSLog(@"Button Touch Down Event Fired for Root.");
        [controller event:ShowRoot Sender:sender];
    }
    else if (sender==favorite) {  
        if ([selectedWord.favorite isEqualToString:_NO]) { 
            [favorite setImage:[UIImage imageNamed:IMG_FAV_RMV] forState:UIControlStateNormal];
            selectedWord.favorite=_YES;
        }  
        else {
            [favorite setImage:[UIImage imageNamed:IMG_FAV_ADD] forState:UIControlStateNormal]; 
            selectedWord.favorite=_NO;
        }
        [dataController updateFavorite:selectedWord];
    }
    else if (sender==showall) {
            //NSLog(@"Button Touch Down Event Fired for show all");
        [controller event:ShowVerse Sender:sender];
  
    }
    else if (sender==sliderVerses) {
        [controller event:ButtonTouch Sender:sender];
    }
    
}


@end
