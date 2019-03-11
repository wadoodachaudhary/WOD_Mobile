//
//  VerseViewController.m
//  TaqwaWOD
//
//  Created by Wadood Chaudhary on 6/4/12.
//  Copyright (c) 2012 Instinet. All rights reserved.
//

#import "VerseViewController.h"
#import "DefinitionCells.h"
#import "Constants.h"
#import "VerseDrawLabel.h"
#import "Verse.h"
#import "Utils.h"
#import "DictionaryDataController.h"
#import "Utils.h"
#import "AppDelegate.h"
#import "FontsViewController.h"
#import "HelpViewController.h"

#import "Constants.h"

@interface VerseFont:NSObject {
    //UIFont* font;
    NSString* text;
@private
    
}
@property (retain) UIFont* font;
@property float heightCushion;
@property int fontIndex;
@property int fontPts;
@property int baseFontPts;
@property CGSize charSize;
@property NSString* family;



-(id) init;
-(id) initWithFont:(NSString*) _font atIndex:(int) index withCushion:(float) cushion ofSize:(int)_fontPts usingSample:(NSString*) sample;
-(UIFont*)   getFont;
-(NSString*) getText;
-(NSString*) description;
-(void) setFont: (NSString*) fontName of:(int) _fontPts;
-(void) resizeFont:(int) _fontPts;
-(void) updateFont: (NSString*) fontName;


@end

@implementation VerseFont
@synthesize font,heightCushion,fontIndex,fontPts,charSize,baseFontPts,family;
-(id) init{};
-(id) initWithFont:(NSString*) _font atIndex:(int) index withCushion:(float) cushion ofSize:(int) _fontPts usingSample:(NSString*) sample{
    fontIndex=index;
    heightCushion=cushion;
    text=font.fontName;
    fontPts=_fontPts;
    baseFontPts=fontPts;
    text=sample;
    [self setFont:_font of:fontPts];
    //charSize = [Utils getTextSize:1.0 forText:text fontToSize:font rectSize:CGSizeMake(500.0, 500.00)];
    if (self=[super init]) return self;
};

-(UIFont*) getFont{
    
    return font;
};
-(NSString*) getText{
    return text;
};

-(void) setFont:(NSString*) fontName of:(int) _fontPts {
    font = [UIFont fontWithName:fontName size:_fontPts];
    fontPts=_fontPts;
    charSize = [Utils getTextSize:1.0 forText:text fontToSize:font rectSize:CGSizeMake(500.0, 500.00)];
}

-(void) resizeFont:(int) scalePts {
    fontPts=baseFontPts+scalePts;
    font = [UIFont fontWithName:font.fontName size:fontPts];
    charSize = [Utils getTextSize:1.0 forText:text fontToSize:font rectSize:CGSizeMake(500.0, 500.00)];
}

-(void) updateFont:(NSString*) fontName  {
    font = [UIFont fontWithName:fontName size:baseFontPts];
    fontPts =baseFontPts;
    charSize = [Utils getTextSize:1.0 forText:text fontToSize:font rectSize:CGSizeMake(500.0, 500.00)];
}


-(NSString*) description {
    return [NSString stringWithFormat:@"Verse Font Name=%@",font.fontName];
}

@end



@interface VerseViewController (){
@private
    NSMutableArray *allVerses;
    NSString* root;
    NSMutableArray* pages,*pagesLocs;
    NSMutableDictionary* fonts;
    Verse* selectedVerse;
    NSString* selectedWordInTranslation;
    UIButton *leftBtn,*rightBtn;
    DictionaryDataController* dataController;
    Word* word;
    int chapter,page,pageMax,pageCount,maxVersesPerPage;
    NSString* titleText;
    NSMutableArray* pagesRefreshed, *wordToHighlight;
    NSMutableDictionary *verses,*cellVerse;
    int borderHeight;
    CGSize maximumLabelSize,maximumLabelSize2;
    bool showArabic,showTranslation,showTranslationSplitWord,firstTime,showTransliteration;
    int verseIndex;
    VerseFont* textFont;
    VerseFont* translationFont;
    VerseFont* translationSplitWordFont;
    VerseFont* fontToSize;
    VerseFont* transliterationFont;
    UILabel* lblVerseIndex;
    int verseSettingsHeight;
    NSString* alignmentLTR;
    CGFloat lastScale;
    CGFloat lastRotation;
    NSIndexPath *scrollIndexPath;
    CGFloat firstX;
    CGFloat firstY;
    CGAffineTransform originalTransform;
    UISlider* sliderVerses;
    UIView* navHeader;
    UISlider* navSlider;
    UIStepper* navStepper;
    //UIStepper* fontStepper;
    NSArray* fontFamily;
    UILabel* navVerseIndex;
    UILabel* navMaxVerseSlider;
    BOOL singleton;
    BOOL disableSwipeIcons;
    int verseView;
    NSString* verseMarker;
    UIActivityIndicatorView *spinner;
    NSString* displayID;
    NSInteger rowIncr,rowScrollTo;
    BOOL isOrientationPortrait;
    UIViewController* callingController;
    //float heightCushion;
    UIButton* navBtnTableOrientation;
    int widthCushion;
    NSArray *displayFormat;
    float execTimeDrawHeight;
    float execTimeRowHeight;
    float lineHeightRatio;
    NSMutableArray *rowHeights;
    NSRange rowDisplayWindow;
    BOOL log;
    enum VERSE_DISPLAY vd;
    UIDeviceOrientation currentDeviceOrientation;
}
@end

@implementation VerseViewController

@synthesize tableView,switchArabic,switchTranslation,switchTransliteration,sliderVerses,sliderVerseIndex,sliderMaxVerseIndex,sliderMinVerseIndex;

static int instance_id=0;

- (id)init{
    NSLog(@" init VerseViewController");
}


- (id)initWithRootWord:(NSString*) rootWord controller:(DictionaryDataController*) rootDataController NIB:(NSString*) nibName {
    self = [super initWithNibName:nibName bundle:nil];
    if (self) {
        singleton=NO;
        dataController=rootDataController;
        displayFormat = [self getDisplayAttributesFor:YES transation:YES transliteration:NO splitword:NO];
        [self initializeVariables];
        [self buildSkeletonWithRoot:rootWord];
        root=rootWord;
        log=NO;
        vd=VD_Root;
    }
    return self;
}

- (id)initWithChapter:(int) chapterno verse:(int) verseno verseDisplayFormat:(NSMutableArray*) _displayFormat controller:(DictionaryDataController*) rootDataController NIB:(NSString*) nibName {
    self = [super initWithNibName:nibName bundle:nil];
    if (self) {
        singleton=NO;
        dataController=rootDataController;
        displayFormat=_displayFormat;
        [self initializeVariables];
        chapter=chapterno;
        verseIndex=verseno-1;
        rowScrollTo=verseIndex;
        navVerseIndex.text=[NSString stringWithFormat:@"%i",verseno];
        [self buildSkeletonWithChapter:chapter];
        scrollIndexPath = [NSIndexPath indexPathForRow:0 inSection:verseIndex];
        log=YES;
    }
    vd=VD_Chapter;
    return self;
}

- (id)initWithTranslationWord:(NSString*) wordTranslation verseDisplayFormat:(NSArray*) _displayFormat controller:(DictionaryDataController*) rootDataController NIB:(NSString*) nibName {
    self = [super initWithNibName:nibName bundle:nil];
    if (self) {
        singleton=NO;
        dataController=rootDataController;
        [self initializeVariables];
        [self setVerseDisplayFormat:_displayFormat];
        chapter=1;
        selectedWordInTranslation=wordTranslation;
        [self buildSkeletonWithTranslationWord:wordTranslation];
        log=NO;
    }
    vd = VD_TranslatedWord;
    return self;
}

- (id)initWithSplitword:(NSArray*) _displayFormat controller:(DictionaryDataController*) rootDataController NIB:(NSString*) nibName withVerses:(NSMutableDictionary*)_verses atPositions:(NSMutableArray*) _allVerses andScrollTo:(NSIndexPath*) _scrollIndexPath ofVisibleRows:(NSRange) _rowDisplayWindow{
    self = [super initWithNibName:nibName bundle:nil];
    if (self) {
        //rowDisplayWindow=NSMakeRange(MAX(0,_scrollIndexPath.section-rowIncr),MIN([allVerses count],_scrollIndexPath.section+rowIncr));
        rowDisplayWindow=_rowDisplayWindow;
        singleton=NO;
        showTranslationSplitWord=YES;
        dataController=rootDataController;
        displayFormat=_displayFormat;
        [self initializeVariables];
        verseView=ViewTop;
        allVerses=_allVerses;
        verses=_verses;
        scrollIndexPath = _scrollIndexPath;
        log=NO;
    }
    vd=VD_SplitWord;
    return self;
}



- (id)initWithWord:(Word*) selectedWord controller:(DictionaryDataController*) wordDataController NIB:(NSString*) nibName {
    self = [super initWithNibName:nibName bundle:nil];
    if (self) {
        singleton=NO;
        [self initializeVariables];
        dataController=wordDataController;
        //footerView = [self buildFooterView];
        word=selectedWord;
        [self buildSkeletonWithWord:word];
        NSString* settings = [NSString stringWithFormat:@"showArabic=%i,showTranslation=%i,showTranslationSplitWord=%i,showTransliteration=%i",1,1,0,0];
        displayFormat = [NSMutableArray arrayWithObjects:[NSNumber numberWithBool:YES],[NSNumber numberWithBool:YES],[NSNumber numberWithBool:NO],[NSNumber numberWithBool:NO], nil];
        verseView=ViewTop;
        NSString* wordID= [dataController getWordID:selectedWord];
        //[dataController addLog:1 Settings:settings ChapterNo:0 VerseNo:0 WordID:[wordID intValue]  Place:@"initWithWord-VerseViewController"];
        log=NO;
    }
    vd=VD_Word;
    return self;
}

- (id)initWithVerse:(Verse*) _selectedVerse NIB:(NSString*) nibName {
    self = [super initWithNibName:nibName bundle:nil];
    if (self) {
        singleton=YES;
        [self initializeVariables];
        selectedVerse = _selectedVerse;
        [self buildSkeletonWithVerse:selectedVerse];
        verseView=ViewTop;
        log=NO;
    }
    vd=VD_Verse;
    return self;
}

-(void) initializeFonts {
    lineHeightRatio = 3;
    int textFontPts=18;
    int transFontPts=12;
    int fontToSizePts=16;
    int translationSplitWordFontPts=12;
    if ([Utils isiPad]) {
        textFontPts=21;
        transFontPts=14;
        fontToSizePts=18;
        translationSplitWordFontPts=14;
    }
    NSArray  *fontArabic,*fontEnglish;
    if (SYSTEM_VERSION_LESS_THAN(@"7.0")) {
        fontArabic =  [NSArray arrayWithObjects:@"AlQalamQuranMajeed2",@"_PDMS_Saleem_QuranFont",@"Scheherazade",@"me_quran",@"DamascusBold",nil];
        fontEnglish =  [NSArray arrayWithObjects:@"AmericanTypewriter",@"Courier",@"Avenir-Book",@"Courier-Oblique",@"HoeflerText-BlackItalic",@"Arial",@"Baskerville",@"AvenirNext-UltraLight",@"AvenirNextCondensed-Regular",@"Verdana-BoldItalic",@"AppleSDGothicNeo-Medium",@"HelveticaNeue-UltraLightItalic",nil];
        
    }
    else {
        fontArabic =  [NSArray arrayWithObjects:@"AlQalamQuranMajeed2",@"_PDMS_Saleem_QuranFont",@"Scheherazade",@"me_quran",@"DamascusBold",nil];
        fontEnglish =  [NSArray arrayWithObjects:@"AppleSDGothicNeo-Light",@"Courier",@"Avenir-Book",@"Courier-Oblique",@"HoeflerText-BlackItalic",@"Optima-ExtraBlack",@"Papyrus-Condensed",@"AvenirNext-UltraLight",@"AvenirNextCondensed-Regular",@"Verdana-BoldItalic",@"BodoniSvtyTwoOSITCTT-BookIt",@"HelveticaNeue-UltraLightItalic",nil];
    }
    fontFamily = [NSArray arrayWithObjects:@"Arabic",@"English",nil];
    fonts = [ [NSMutableDictionary alloc] init];
    [fonts setObject: fontArabic forKey:[fontFamily objectAtIndex:0] ];
    [fonts setObject: fontEnglish forKey:[fontFamily objectAtIndex:1] ];
    float heightCushion=1.5;
    textFont = [[VerseFont alloc ] initWithFont:fontArabic[0] atIndex:4 withCushion:1.0 ofSize:textFontPts usingSample:@"بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ"];
    translationFont = [[VerseFont alloc ] initWithFont:fontEnglish[0] atIndex:0 withCushion:1.0 ofSize:transFontPts usingSample:@"Allah"];
    transliterationFont =  [[VerseFont alloc] initWithFont:@"Courier" atIndex:0 withCushion:1.0 ofSize:transFontPts usingSample:@"Allah"];
    translationSplitWordFont =  [[VerseFont alloc ] initWithFont:@"Courier New" atIndex:0 withCushion:1.0 ofSize:translationSplitWordFontPts usingSample:@"Allah"];
    fontToSize =  [[VerseFont alloc ] initWithFont:@"Courier New" atIndex:0 withCushion:1.0 ofSize:fontToSizePts usingSample:@"Allah"];
}

-(NSString*) getFontID{
    NSArray *names = [fonts objectForKey:fontFamily[0]];
    int arabicIndex =(int) [names indexOfObject:textFont.font.fontName];
    names = [fonts objectForKey:fontFamily[1]];
    int englishIndex = (int)[names indexOfObject:translationFont.font.fontName];
    return [NSString stringWithFormat:@"%02d%02d%02d%02d%02d",textFont.fontPts,translationFont.fontPts,fontToSize.fontPts,arabicIndex,englishIndex];
}


-(void) setFontSizes:(int) normalizer {
    [textFont resizeFont:normalizer];
    [translationFont resizeFont:normalizer];
    [fontToSize resizeFont:normalizer];
    [transliterationFont resizeFont:normalizer];
    [translationSplitWordFont resizeFont:normalizer];
}

- (void) setFonts:(NSString*) arabicFont andEnglishFont:(NSString*) englishFont {
    if (arabicFont!=nil) {
        [textFont updateFont:arabicFont];
        [fontToSize updateFont:fontToSize.font.fontName];
    }
    if (englishFont!=nil) {
        [translationFont updateFont:englishFont];
    }
    if (arabicFont!=nil || englishFont!=nil) {
        navStepper.value=0;
        [self reset];
    }
}

-(void) updateFont:(VerseFont*) verseFont ofFamily:(int) familyIndex atFontIndex:(int) index ofSize:(int) fontPts {
    NSArray *names = [fonts objectForKey:fontFamily[familyIndex]];
    if (index < [names count]) {
        [verseFont setFont:names[index] of:fontPts];
    }
    
    
}

-(NSArray*) applySettings {
    NSMutableArray* screen = [dataController getLastScreenFor:0];
    if (screen!=nil) {
        NSString* settings = [screen objectAtIndex:6];
        //verseView=ViewTop;
        NSValue* _rect =[NSValue valueWithCGRect:CGRectMake(0,0,[[Utils substr:settings from:0 length:4] intValue],[[Utils substr:settings from:4 length:4] intValue])];
        NSValue* _deviceView_orientation =[NSNumber numberWithBool:([[Utils substr:settings from:8 length:1] intValue]==0)];
        NSValue* _viewSideBySide = [NSNumber numberWithBool:([[Utils substr:settings from:9 length:1] intValue]==1)];
        NSValue* _showArabic =[NSNumber numberWithBool:([[Utils substr:settings from:10 length:1] intValue]==0)];
        NSValue* _showTranslation =[NSNumber numberWithBool:([[Utils substr:settings from:11 length:1] intValue]==0)];
        NSValue* _showTransliteration =[NSNumber numberWithBool:([[Utils substr:settings from:12 length:1] intValue]==0)];
        NSValue* _showSplitWord =[NSNumber numberWithBool:([[Utils substr:settings from:13 length:1] intValue]==0)];
        NSValue* _textFontPts =[NSNumber numberWithInt:[[Utils substr:settings from:14 length:2] intValue]];
        NSValue* _transFontPts =[NSNumber numberWithInt:[[Utils substr:settings from:16 length:2] intValue]];
        NSValue* _fontToSizePts =[NSNumber numberWithInt:[[Utils substr:settings from:18 length:2] intValue]];
        NSValue* _textFontIndex =[NSNumber numberWithInt:[[Utils substr:settings from:20 length:2] intValue]];
        NSValue* _translationFontIndex =[NSNumber numberWithInt:[[Utils substr:settings from:22 length:2] intValue]];
        NSValue* _chapterNo =[NSNumber numberWithInt:[[Utils substr:settings from:24 length:3] intValue]];
        NSValue* _verseNo =[NSNumber numberWithInt:[[Utils substr:settings from:27 length:3] intValue]];
        //
        CGRect rect =CGRectMake(0,0,[[Utils substr:settings from:0 length:4] intValue],[[Utils substr:settings from:4 length:4] intValue]);
        bool deviceView_orientation =[[Utils substr:settings from:8 length:1] intValue];
        bool viewSideBySide = [[Utils substr:settings from:9 length:1] intValue];
        showArabic =([[Utils substr:settings from:10 length:1] intValue]==0);
        showTranslation =([[Utils substr:settings from:11 length:1] intValue]==0);
        showTransliteration =([[Utils substr:settings from:12 length:1] intValue]==0);
        showTranslationSplitWord =([[Utils substr:settings from:13 length:1] intValue]==0);
        int textFontPts =[[Utils substr:settings from:14 length:2] intValue];
        int transFontPts =[[Utils substr:settings from:16 length:2] intValue];
        int fontToSizePts =[[Utils substr:settings from:18 length:2] intValue];
        int textFontIndex =[[Utils substr:settings from:20 length:2] intValue];
        int translationFontIndex=[[Utils substr:settings from:22 length:2] intValue];
        int chapterNo=[[Utils substr:settings from:24 length:3] intValue];
        int verseNo=[[Utils substr:settings from:27 length:3] intValue];
        //
        NSArray* _displayFormat= [NSArray arrayWithObjects:_showArabic,_showTranslation,_showSplitWord,_showTransliteration, nil];
        
        //
        verseView=([[Utils substr:settings from:9 length:1] intValue]==1)?ViewSideBySide:ViewTop;
        [navBtnTableOrientation setImage:[UIImage imageNamed:(verseView==ViewTop?IMG_VIEW_SIDE:IMG_VIEW_TOP)] forState:UIControlStateNormal];
        if (true) {
            [self updateFont:textFont ofFamily:0 atFontIndex:textFontIndex ofSize:textFontPts];
            [self updateFont:translationFont ofFamily:1 atFontIndex:translationFontIndex ofSize:transFontPts];
            [self updateFont:transliterationFont ofFamily:1 atFontIndex:translationFontIndex ofSize:transFontPts];
            [self updateFont:translationSplitWordFont ofFamily:1 atFontIndex:translationFontIndex ofSize:transFontPts];
            [self updateFont:fontToSize ofFamily:1 atFontIndex:translationFontIndex ofSize:fontToSizePts];
            navStepper.value = textFont.baseFontPts<textFontPts?textFontPts-textFont.baseFontPts:textFont.baseFontPts-textFontPts;
        }
        //NSLog(@"[Apply Settings] VerseView=%i, Stepper Value:%f",verseView,navStepper.value);
        return _displayFormat;
        
    }
    return nil;
    
}

-(void) initializeDisplay {
    //navHeader = header;//
    navHeader= [[UIView alloc] initWithFrame:CGRectMake(0,0, [Utils getScreenSize].size.width,header.frame.size.height)];
    
    //NSLog(@"Title:%@",self.navigationController.title);
    CGSize titleSize = [Utils getTextSize:1.0 forText:self.navigationController.title fontToSize:translationFont.font rectSize:maximumLabelSize];
    int headerWidth = maximumLabelSize.width - titleSize.width*2;
    // navFavorite
    UIImage* imageFavorite = [UIImage imageNamed:IMG_FAV_ADD];
    UIImage* imageTableOrientationCol = [UIImage imageNamed:IMG_VIEW_SIDE];
    UIImage* imageTableOrientationRow = [UIImage imageNamed:IMG_VIEW_TOP];
    UIImage* imageFont = [UIImage imageNamed:IMG_FONT];
    UIImage* imageStepperDec=[UIImage imageNamed:IMG_FONT_DOWN];
    UIImage* imageStepperInc=[UIImage imageNamed:IMG_FONT_UP];
    int spaceInBetween=(headerWidth - (imageFavorite.size.width+imageTableOrientationCol.size.width+imageFont.size.width+imageStepperDec.size.width+imageStepperInc.size.width))/6;
    
    int x=0;
    int y = 10;
    UIButton* navFavorite= [[UIButton alloc] initWithFrame:CGRectMake(x,y,imageFavorite.size.width,imageFavorite.size.height)];
    [navFavorite setImage:imageFavorite forState:UIControlStateNormal];
    [navFavorite setImage:[UIImage imageNamed:IMG_FAV_RMV] forState:UIControlStateSelected];
    [navFavorite addTarget:self action:@selector(favoriteButtonTouchDown:) forControlEvents:UIControlEventTouchUpInside];
    [navFavorite setSelected:NO];
    [navHeader addSubview:navFavorite];
    x=x+imageFavorite.size.width+spaceInBetween;
    
    //navViewSideBySide
    navBtnTableOrientation= [[UIButton alloc] initWithFrame:CGRectMake(x,y,imageTableOrientationCol.size.width,imageTableOrientationCol.size.height)];
    [navBtnTableOrientation setImage:imageTableOrientationRow forState:UIControlStateNormal];
    [navBtnTableOrientation setImage:imageTableOrientationCol forState:UIControlStateSelected];
    [navBtnTableOrientation addTarget:self action:@selector(viewSideBySideTouchDown:) forControlEvents:UIControlEventTouchUpInside];
    [navBtnTableOrientation setSelected:YES];
    [navHeader addSubview:navBtnTableOrientation];
    x=x+imageTableOrientationRow.size.width+spaceInBetween;
    
    //navViewFonts
    UIButton* navFonts= [[UIButton alloc] initWithFrame:CGRectMake(x,y,imageFont.size.width,imageFont.size.height)];
    [navFonts setImage:imageFont forState:UIControlStateNormal];
    [navFonts setImage:[UIImage imageNamed:IMG_FONT] forState:UIControlStateSelected];
    [navFonts addTarget:self action:@selector(fontsTouchDown:) forControlEvents:UIControlEventTouchUpInside];
    [navFonts setSelected:YES];
    [navFonts setTintColor:[UIColor blueColor]];
    [navHeader addSubview:navFonts];
    x=x+imageFont.size.width+spaceInBetween;
    
    //Stepper
    CGRect rect = CGRectMake(x,y,imageStepperDec.size.width+imageStepperInc.size.width,imageStepperInc.size.height);
    navStepper= [[UIStepper alloc] initWithFrame:rect];
    navStepper.minimumValue=-6.0;
    navStepper.maximumValue=10.0;
    navStepper.value=0.0;
    navStepper.stepValue=2.0;
    [navStepper setDecrementImage:imageStepperDec forState:UIControlStateNormal];
    [navStepper setIncrementImage:imageStepperInc forState:UIControlStateNormal];
    [navStepper addTarget:self action:@selector(valueChangedStepper:) forControlEvents:UIControlEventValueChanged];
    [navStepper setSelected:NO];
    [navHeader addSubview:navStepper];
    [navHeader sendSubviewToBack:navStepper];
    
    //
    
    
    self.navigationItem.titleView = navHeader;
    //self.navigationItem.titleView.hidden=NO;
    
    
    navSlider.minimumValue=1.0;
    navSlider.maximumValue=[allVerses count];
    navSlider.value=verseIndex+1;
    navMaxVerseSlider.text=[NSString stringWithFormat:@"%tu",[allVerses count]];
    navVerseIndex.text=[NSString stringWithFormat:@"%i",verseIndex+1];
    //
    //rowcount = MIN(parentRowCount+rowIncr,[allVerses count]);
    //
    
    switchArabic.on=showArabic;
    switchTranslation.on=showTranslation;
    switchTransliteration.on=showTransliteration;
    header.hidden=NO;
    //
    spinner = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleGray];
    spinner.center = CGPointMake(160, 240);
    spinner.tag = 12;
    spinner.hidesWhenStopped=YES;
    [self.view addSubview:spinner];
    
    
    //
    // NSLog(@" Slider Value:%f row:%f",sliderVerses.value,sliderVerses.maximumValue);
    
}

-(void)dismiss {
    [self dismissViewControllerAnimated:NO completion:nil];
}
//
- (void)singleTapped:(UITapGestureRecognizer*)gesture {
    //if (popup.hidden==NO) return;
    //NSLog(@"Single Tapped called");
    if (self.navigationController.isNavigationBarHidden) {
        [self.navigationController setNavigationBarHidden:NO];
        //[self.navigationController setToolbarHidden:NO];
        //[self.view sendSubviewToBack:self.tableView];
        //[self.navigationItem.titleView.hidden=NO;
        //[self.view sendSubviewToBack:self.tableView];
        //[self.view setFrame: [[UIScreen mainScreen] bounds]];
        //[self.navigationController setNavigationBarHidden:NO animated:NO];
        //[self.navigationController setNavigationBarHidden:NO animated:NO];
        // [self.view setNeedsDisplay];
    }
    else {
        [self.navigationController setNavigationBarHidden:YES];
        //[self.navigationController setToolbarHidden:YES];
        //self.navigationItem.titleView.hidden=YES;
        //NSLog(@"NavHeader Hidden=NO");
        //[self.view setFrame: [[UIScreen mainScreen] bounds]];
        //[[UIApplication sharedApplication] setStatusBarHidden:TRUE withAnimation:NO];
        //[self.view bringSubviewToFront:self.tableView];
    }
    
}



-(void) popupButtonClicked:(id) sender{
    if (sender==popupBack) {
        popup.hidden=YES;
        [self.view sendSubviewToBack:popup];
        [self.view bringSubviewToFront:self.tableView];
    }
    else if (sender==popupSplitWord) {
        popup.hidden=YES;
        [self.view sendSubviewToBack:popup];
        [self.view bringSubviewToFront:self.tableView];
        VerseViewController *verseViewController =  [[VerseViewController alloc] initWithVerse:selectedVerse NIB:@"VerseViewController"];
        [self.navigationController pushViewController:verseViewController animated:YES];
        
    }
    else if (sender==popupMorphology) {
        popup.hidden=YES;
        [self.view sendSubviewToBack:popup];
        [self.view bringSubviewToFront:self.tableView];
    }
    else if (sender==popupSyntacticTree) {
        popup.hidden=YES;
        [self.view sendSubviewToBack:popup];
        [self.view bringSubviewToFront:self.tableView];
    }
    //[self.view removeFromSuperview];
}

-(void) popupSelected:(id) sender{
    popup.hidden=YES;
    [self.view sendSubviewToBack:popup];
    [self.view bringSubviewToFront:self.tableView];
    //[self.view removeFromSuperview];
    [self.navigationController popViewControllerAnimated:YES];
}

//- (void) initPopUpView {
//    [self showHeader:NO];
//        //footer.hidden=YES;
//        //popup.alpha = 0.7;
//        //popup.frame = CGRectMake (160, 240, 0, 0);
//
//    [[popup layer] setBorderWidth:1.0];
//    [[popup layer] setBorderColor:[[UIColor lightGrayColor] CGColor]];
//    [[popup layer] setCornerRadius:5.0];
//    [[popup layer] setMasksToBounds:YES];
//    popupVerseHeader.text=[NSString stringWithFormat:@"Breakdown of verse (%i,%i) by",[selectedVerse.chapterno intValue],[selectedVerse.verseno intValue]];
//
//    popup.hidden=NO;
//        //self.tableView.hidden=YES;
//    [self.view sendSubviewToBack:self.tableView];
//    [self.view bringSubviewToFront:popup];
//        //[self.view addSubview:popup];
//}

- (void) animatePopUpShow {
    [UIView beginAnimations:nil context:NULL];
    [UIView setAnimationDuration:0.3];
    [UIView setAnimationDelegate:self];
    [UIView setAnimationWillStartSelector:@selector(initPopUpView)];
    
    popup.alpha = 1;
    popup.frame = CGRectMake (20, 40, 300, 400);
    
    [UIView commitAnimations];
}


- (NSArray*) getDisplayAttributesFor:(bool) ar transation:(bool) tr transliteration:(bool) tlit splitword:(bool) sw {
    NSValue* arabicOn = [NSNumber numberWithBool:ar];
    NSValue* translationOn = [NSNumber numberWithBool:tr];
    NSValue* transliterationOn = [NSNumber numberWithBool:tlit];
    NSValue* translationSplitWordOn = [NSNumber numberWithBool:sw];
    NSArray* _displayFormat = [NSMutableArray arrayWithObjects:arabicOn,translationOn,translationSplitWordOn,transliterationOn, nil];
    return _displayFormat;
}

-(void)didSwipe:(UISwipeGestureRecognizer *)gestureRecognizer {
    
    if (gestureRecognizer.state == UIGestureRecognizerStateEnded) {
        CGPoint swipeLocation = [gestureRecognizer locationInView:self.tableView];
        NSIndexPath *swipedIndexPath = [self.tableView indexPathForRowAtPoint:swipeLocation];
        if (gestureRecognizer.direction==UISwipeGestureRecognizerDirectionLeft) {
            //NSLog(@"Swipe Methods: Left");
            //   [Utils showMessage:@"Swipe forward" Message:@"Already on last page."];
            //if (page<pageCount-1) page=page+1; else {
            //    [Utils showMessage:@"Swipe forward" Message:@"Already on last page."];
            //    return;
            //}
            //[self refreshPage:page];
            //[self dataChanged];
            //[self setSliderVerseIndexAtPage:page];
            if (showTranslationSplitWord) return;
            //int chapterNo = 1;
            //int verseNo   = 1;
            [spinner startAnimating];
            NSArray* visibleRows = [self.tableView indexPathsForVisibleRows];
            scrollIndexPath = [visibleRows objectAtIndex:0];
            int scrollToRow = (int)([visibleRows count]/2);
            NSIndexPath* scrollIndexPathForSplitword= [visibleRows objectAtIndex:scrollToRow];
            NSArray* _displayFormat = [self getDisplayAttributesFor:NO transation:NO transliteration:NO splitword:YES];
            VerseViewController *verseViewController = [[VerseViewController alloc] initWithSplitword:_displayFormat controller:dataController NIB:@"VerseViewController" withVerses:verses atPositions:allVerses andScrollTo:swipedIndexPath ofVisibleRows:rowDisplayWindow];
            //verseViewController.hidesBottomBarWhenPushed = YES;
            verseViewController.title=@"Verses";
            [verseViewController setCallingController:nil];
            [self.navigationController pushViewController:verseViewController animated:YES];
            //[[Utils getTabController] presentViewController:verseViewController animated:YES completion:nil];
        }
        else if (gestureRecognizer.direction==UISwipeGestureRecognizerDirectionRight) {
            //NSLog(@"[VerseViewController]Swipe Methods: Right");
            //if ( [Utils isTabController:callingController]) {
            //[self dismissViewControllerAnimated:NO completion:nil];
            //}
            //else {
            
            [self.navigationController popViewControllerAnimated:TRUE];
            
            //}
        }
        //[self showHeader:NO];
        //UITableViewCell* swipedCell = [self.tableView cellForRowAtIndexPath:swipedIndexPath];
        //[self.navigationController.navigationBar addSubview:header2];
        
        // ...
    }
}

-(void) addLog {
    if (!log) return;
    scrollIndexPath = [[self.tableView indexPathsForVisibleRows] objectAtIndex:0];
    NSUInteger row = (int)[scrollIndexPath section];
    if (allVerses == nil) return;
    [self refreshIDs];
    CGPoint verseLoc = [[allVerses objectAtIndex:row] CGPointValue];
    NSString* settings= [NSString stringWithFormat:@"%@%03d%03d",displayID,(int)verseLoc.x,(int)verseLoc.y];
    [dataController addLog:0 Settings:settings ChapterNo:verseLoc.x VerseNo: verseLoc.y WordID:0 Place:@"VerseViewController"];
}

-(void) viewWillDisappear:(BOOL)animated {
    //NSLog(@"[VerseViewController] viewWillDisppear");
    if (spinner.isAnimating) [spinner stopAnimating];
    currentDeviceOrientation = [UIDevice currentDevice].orientation;
    [self addLog];
}

- (void)viewWillAppear:(BOOL)animated {
        popup.hidden=YES;
    if (scrollIndexPath != nil) {
        //NSLog(@"ScrollIndexPath.section=%tu",scrollIndexPath.section);
        [[self tableView] reloadData];
        [self.tableView scrollToRowAtIndexPath:scrollIndexPath atScrollPosition:UITableViewScrollPositionTop animated:NO];
    }
    else {
    }
    [self.navigationController setNavigationBarHidden:YES];
    [self.navigationController setToolbarHidden:YES];
    //
    //
    //NSLog(@" Text Font Name :%@  Translation Font Name;%@",textFont.font.fontName,translationFont.font.fontName);
    //[self.navigationController setNavigationBarHidden:YES];
    //[self.navigationController setToolbarHidden:YES];
    //[self.tableView.tableHeaderView.hidden =YES;
    //[self.tableView setContentOffset:CGPointMake(0,100) animated:YES];
    if (currentDeviceOrientation!=[UIDevice currentDevice].orientation) {
        [self reset];
    }
    else [self addLog];
    //NSLog(@"[VerseViewController] ViewWillAppear");
    [super viewWillAppear:animated];
    
}
-(BOOL) prefersStatusBarHidden {
    return YES;
}


- (void)viewDidLoad {
    [super viewDidLoad];
    [self initializeDisplay];
    
    
    //footer.hidden=YES;
    UIPinchGestureRecognizer *pinchRecognizer = [[UIPinchGestureRecognizer alloc] initWithTarget:self action:@selector(scale:)];
    [pinchRecognizer setDelegate:self];
    [self.tableView addGestureRecognizer:pinchRecognizer];
    
    UILongPressGestureRecognizer *longPress = [[UILongPressGestureRecognizer alloc] initWithTarget:self action:@selector(longPress:)];
    longPress.minimumPressDuration=0.5;
    longPress.cancelsTouchesInView=YES;
    //longPress.delegate = self;
    [self.tableView addGestureRecognizer:longPress];
    
    //
    UITapGestureRecognizer *doubleTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(longPress:)];
    doubleTap.numberOfTapsRequired=2;
    doubleTap.delegate = self;
    [self.tableView addGestureRecognizer:doubleTap];
    
    UITapGestureRecognizer *singleTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(singleTapped:)];
    singleTap.numberOfTapsRequired=1;
    //singleTap.delegate = self;
    //shortPress
    [self.tableView addGestureRecognizer:singleTap];
    
    
    UISwipeGestureRecognizer *gestureLeft = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(didSwipe:)];
    gestureLeft.direction = UISwipeGestureRecognizerDirectionLeft;
    [self.tableView addGestureRecognizer:gestureLeft];
    //gestureLeft.delegate=self;
    
    //
    UISwipeGestureRecognizer *gestureRight = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(didSwipe:)];
    gestureRight.direction = UISwipeGestureRecognizerDirectionRight;
    //gestureRight.delegate=self;
    [self.tableView addGestureRecognizer:gestureRight];
    [singleTap requireGestureRecognizerToFail:doubleTap];
    //[gestureLeft requireGestureRecognizerToFail:doubleTap];
    //[gestureRight requireGestureRecognizerToFail:doubleTap];
    
    if (true) {
        NSString* property = @"Verse Message";
        NSString* verseMessageShown= [dataController getProperty:property];
        NSString* propertyCount = @"Verse Message Count";
        NSString* verseMessageCount= [dataController getProperty:propertyCount];
        NSString* propertyLastDisplayed= @"Verse Message Last Displayed";
        NSString* verseMessageLastDisplayed= [dataController getProperty:propertyLastDisplayed];
        NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
        [dateFormatter setDateFormat:@"dd"];
        int day = [[dateFormatter stringFromDate:[NSDate date]] intValue];
        int lastDisplayDay=0;
        if (verseMessageLastDisplayed!=nil) lastDisplayDay = [verseMessageLastDisplayed intValue] ;
        verseMessageLastDisplayed=[NSString stringWithFormat:@"%i",day];
        //[Utils getTodaysDateInFormat:<#(NSString *)#>]
        int count = 0;
        if (verseMessageCount!=nil) {
            count = [verseMessageCount intValue];
        }
        if (!showTranslationSplitWord && (day!=lastDisplayDay) & (verseMessageShown==nil || [verseMessageShown isEqualToString:@"NO"] || count < 4)) {
//            NSString* message=@"*Long press or double tab on the Arabic or English word to look up its definition.\n*Turn page right to see the verse's split word translation.\n*Tap lightly to see page formatting options.\n*Pinch to zoom in and out.\n*Turn page left to go back.";
//            [Utils showMessage:@"Tips" Message:message];
            [dataController setProperty:property Value:@"YES"];
            [dataController setProperty:propertyCount Value:[NSString stringWithFormat:@"%i",count+1]];
            [dataController setProperty:propertyLastDisplayed Value:verseMessageLastDisplayed];
        
            HelpViewController *help = [[HelpViewController alloc] init];
            [self.view addSubview: help];
            help.scrollEnabled=YES;
            //[self.view sendSubviewToBack:tableView];
            [self.view bringSubviewToFront:help];
        }
    }
    //
    NSArray* lastDisplayFormat=[self applySettings];
    if (displayFormat!=nil) {
        [self setVerseDisplayFormat:displayFormat];
    }
    else {
        if (lastDisplayFormat!=nil) {
            [self setVerseDisplayFormat:lastDisplayFormat];
        }
        else {
            NSValue* arabicOn = [NSNumber numberWithBool:YES];
            NSValue* translationOn = [NSNumber numberWithBool:YES];
            NSValue* transliterationOn = [NSNumber numberWithBool:NO];
            NSValue* translationSplitWordOn = [NSNumber numberWithBool:NO];
            displayFormat= [NSArray arrayWithObjects:arabicOn,translationOn,translationSplitWordOn,transliterationOn, nil];
            [self setVerseDisplayFormat:displayFormat];
            verseView=ViewTop;
        }
    }
    [self refreshIDs];
    self.navigationItem.titleView.hidden=NO;
    [self resetRowHeights];
    self.tableView.alwaysBounceVertical=YES;
    tableView.alwaysBounceVertical=YES;
    //NSLog(@"[VerseViewController] ViewDidLoad");
}


- (void) initializeVariables {
    //NSLog(@"Size:%f",self.tableView.frame.size.width);
    //NSArray* nib = [[NSBundle mainBundle] loadNibNamed:@"DefinitionCells" owner:self options:nil];
    //cellDefinitionFormat =(DefinitionCells *) [nib objectAtIndex:4];
    //[self setFontSizes:0];
    rowScrollTo=0;
    CGRect rect = [Utils getScreenSize];
    currentDeviceOrientation = [UIDevice currentDevice].orientation;
    [self setLabelSize:currentDeviceOrientation];
    // Fonts
    
    //verseMarker=@"XX";
    verseMarker=@"";
    alignmentLTR=@"\u200E";
    borderHeight=0;
    verseSettingsHeight=0;
    rowIncr=[Utils isRetina]?40:25;
    //textFont.font = [UIFont systemFontOfSize:16];
    //    if (textFont.font==nil) textFont.font = [UIFont systemFontOfSize:24];
    //    if (translationFont.font ==nil) translationFont.font = [UIFont fontWithName:@"Helvetica-Oblique" size: 14.0];
    //    if (transliterationFont  ==nil) transliterationFont = [UIFont fontWithName:@"Courier" size: 14.0];
    //    //if (translationFont.font ==nil) translationFont.font = [UIFont fontWithName:@"Courier New" size: 14.0];
    //
    //    if (translationSplitWordFont.font==nil) translationSplitWordFont.font =  [UIFont fontWithName:@"Courier New" size:14];
    //    if (fontToSize.font==nil) fontToSize.font =  [UIFont fontWithName:@"Courier New" size:20];
    //    page=0;maxVersesPerPage=1000;pageMax=maxVersesPerPage;pageCount=999;
    cellVerse = [[NSMutableDictionary alloc] init];
    instance_id++;
    lastScale=1.0f;
    firstTime=true;
    disableSwipeIcons=NO;
    verseView=ViewTop;
    //
    [self initializeFonts];
    
    
    //
    /*
     Al Bayan Plain
     Al Tarikh
     Al-Firat
     Al-Khalil
     Al-Khalil Bold
     Al-Rafidain*/
    //NSLog(@"VerseViewController InstanceID:%i Memeory(%d)",instance_id,[Utils get_free_memory]);
    //verseList = [[NSMutableSet alloc] init];
}

- (NSString*) setVerseDisplayFormat:(NSArray*) _displayFormat{
    displayFormat=_displayFormat;
    showArabic= [[displayFormat objectAtIndex:0] boolValue];
    showTranslation=[[displayFormat objectAtIndex:1] boolValue];
    showTranslationSplitWord=[[displayFormat objectAtIndex:2] boolValue];
    showTransliteration=[[displayFormat objectAtIndex:3] boolValue];
    int bv = showArabic+showTranslation+showTranslationSplitWord+showTransliteration;
    if (bv==1||bv>2||(verseView==ViewSideBySide && bv==2 && (showTransliteration || showTranslationSplitWord))) {
        verseView=ViewTop;
    }
    NSString* result = [NSString stringWithFormat:@"showArabic=%i,showTranslation=%i,showTranslationSplitWord=%i,showTransliteration=%i",showArabic,showTranslation,showTranslationSplitWord,showTransliteration];
    return result;
    //NSLog(@"VerseViewDisplay=%i",verseView);
}


-(void) refreshIDs {
    CGRect rect = [Utils getScreenSize];
    NSString* screenSizeID = [NSString stringWithFormat:@"%04d%04d",(int)rect.size.width,(int)rect.size.height];
    NSString* verseViewID = @(verseView==ViewSideBySide?"1":"0");
    NSString* contentViewID = [NSString stringWithFormat:@"%@%@%@%@",@(showArabic?"1":"0"),@(showTranslation?"1":"0"),@(showTransliteration?"1":"0"),@(showTranslationSplitWord?"1":"0")];
    NSString* deviceViewID = @(UIDeviceOrientationIsLandscape([UIDevice currentDevice].orientation)?"0":"1");
    NSString* fontID = [self getFontID];
    displayID = [NSString stringWithFormat:@"%@%@%@%@%@",screenSizeID,deviceViewID,verseViewID,contentViewID,fontID];//
    //NSLog(@" Display ID:%@",displayID);
    
}

-(void) buildSkeletonWithWord:(Word*) _word {
    //NSSortDescriptor *desc = [[NSSortDescriptor alloc] initWithKey:nil ascending:YES selector:@selector(caseInsensitiveCompare:)];
    NSSortDescriptor *sortChapter = [[NSSortDescriptor alloc] initWithKey: @"chapterno" ascending: YES];
    NSSortDescriptor *sortVerse = [[NSSortDescriptor alloc] initWithKey: @"verseno" ascending: YES];
    NSArray* sortedVerses= [_word.verses sortedArrayUsingDescriptors:@[sortChapter,sortVerse]];
    // NSArray* sortedValues = [values sortedArrayUsingSelector:@selector(comparator)];
    //[arr sortedArrayUsingSelector:@selector(caseInsensitiveCompare:)]
    int verseCount = [_word.versecount intValue];
    allVerses = [NSMutableArray arrayWithCapacity:verseCount];
    verses = [[NSMutableDictionary alloc] initWithCapacity:verseCount];
    titleText=[@"\u200E" stringByAppendingString:_word.word];
    wordToHighlight = [NSMutableArray arrayWithCapacity:verseCount];
    for(Verse* verse in sortedVerses) {
        int chapno =[verse.chapterno intValue];
        int verseno= [verse.verseno intValue];
        NSString* verseId = [NSString stringWithFormat:@"%i:%i",chapno,verseno];
        //NSLog(@"VerseID:%@",verseId);
        [verses setValue:verse forKey:verseId];
        CGPoint versePt= CGPointMake(chapno,verseno);
        [allVerses addObject:[NSValue valueWithCGPoint:versePt]];
        [wordToHighlight addObject:_word.word];
    }
    rowDisplayWindow = NSMakeRange(0, MIN(rowIncr,[allVerses count]));
    
}

-(void) buildSkeletonWithVerse:(Verse*) _verse {
    showTranslationSplitWord=YES;showArabic=NO;showTranslation=NO;showTransliteration=NO;
    word=_verse.has;
    allVerses = [NSMutableArray arrayWithCapacity:1];
    wordToHighlight = [NSMutableArray arrayWithCapacity:1];
    int chap = [_verse.chapterno intValue];
    int vrs  = [_verse.verseno intValue];
    NSString* verseId = [NSString stringWithFormat:@"%i:%i",chap,vrs];
    CGPoint verseIdPt= CGPointMake(chap,vrs);
    [allVerses addObject:[NSValue valueWithCGPoint:verseIdPt]];
    verses = [[NSMutableDictionary alloc] initWithCapacity:1];
    [verses setValue:_verse forKey:verseId];
    [wordToHighlight addObject:_verse.has.word];
    [self addDrawLocsFor:_verse atIndex:verseId];
    rowDisplayWindow = NSMakeRange(0, MIN(rowIncr,[allVerses count]));
    
}


-(void) buildSkeletonWithChapter:(int) _chapter {
    verses = [dataController getVersesInChapter:_chapter];
    NSString* verseId = [NSString stringWithFormat:@"%i:%i",0,0];
    allVerses = (NSMutableArray*) [verses objectForKey:verseId];
    titleText = [NSString stringWithFormat:@"Chapter:%i",_chapter];
    [verses removeObjectForKey:verseId];
    rowDisplayWindow = NSMakeRange(0, MIN(MAX(rowScrollTo+1,rowIncr),[allVerses count]));
    //NSLog(@"[buildSkeletonWithChapter] AllVerses Count =%zd, Verse Count=%zd",[allVerses count],[verses count]);
}

-(void) buildSkeletonWithTranslationWord:(NSString*) wordTranslation {
    verses = [dataController getVersesInTranslatedWord:wordTranslation];
    NSString* verseId = [NSString stringWithFormat:@"%i:%i",0,0];
    allVerses = (NSMutableArray*) [verses objectForKey:verseId];
    [verses removeObjectForKey:verseId];
    titleText = [NSString stringWithFormat:@"Word:%@",wordTranslation];
    rowDisplayWindow = NSMakeRange(0, MIN(rowIncr,[allVerses count]));
    //NSLog(@"[buildSkeletonWithChapter] AllVerses Count =%zd, Verse Count=%zd",[allVerses count],[verses count]);
}


-(void) buildSkeletonWithRoot:(NSString*) rootWord {
    verses = [dataController getVersesInRootWord:rootWord];
    NSString* verseId = [NSString stringWithFormat:@"%i:%i",0,0];
    allVerses = (NSMutableArray*) [verses objectForKey:verseId];
    [verses removeObjectForKey:verseId];
    //
    NSMutableArray* words = [dataController getWordsInRootWord:rootWord];
    int i=0;
    wordToHighlight = [NSMutableArray arrayWithCapacity:[verses count]];
    for (int row=0; row < [allVerses count]; row++) {
        CGPoint verseLoc = [[allVerses objectAtIndex:row] CGPointValue];
        Verse* verse = [self getVerseAt:(int)verseLoc.x _verseNo:(int)verseLoc.y RefreshVerseLocs:NO];
        NSArray* tokens = [verse.verse componentsSeparatedByString:@" "];
        NSString* wordMain = rootWord;
        for(NSArray* worddata in words) {
            if ([worddata[0] intValue]==[verse.chapterno intValue] && [worddata[1] intValue]==[verse.verseno intValue]) {
                int index = [worddata[2] intValue]-1;
                //NSLog(@" Index %i, Tokens(%@,%@) (%i,%i) (%zd,%zd)",index,tokens[index],tokens[index-1],[worddata[0] intValue],[worddata[1] intValue],[verse.chapterno intValue],[verse.verseno intValue]);
                if (index >=0 && index < [tokens count]) wordMain = tokens[index];
                break;
            }
        }
        [wordToHighlight addObject:wordMain];
    }
    rowDisplayWindow = NSMakeRange(0, MIN(rowIncr,[allVerses count]));
    NSLog(@"[buildSkeletonWithRoot] AllVerses Count =%zd, Verse Count=%zd, wordToHighlight Count:%zd",[allVerses count],[verses count],[wordToHighlight count]);
    
}


//
//- (id)initWithFrame:(CGRect)frame{
//    //self = [super initWithFrame:frame];
//    //selfautoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
//    //self.contentMode = UIViewContentModeRedraw;
//
//    //return self;
//}

//-(void) initWithFrame {
//}



- (void) showHeader:(BOOL) visible{
    //NSLog(@" In Show header");
    if (visible) {
        [[self navigationController] setNavigationBarHidden:NO animated:YES];
        navHeader.hidden=YES;
    }
    else {
        //[[self navigationController] setNavigationBarHidden:YES animated:YES];
        navHeader.hidden=NO;
        
    }
}

- (IBAction) valueChangedSlider:(UISlider*) verseSlider {
    navVerseIndex.text=[NSString stringWithFormat:@"%i",(int)(verseSlider.value+0.5f)];
    NSLog(@" SliderValue:%f sliderVerseIndex:%@",verseSlider.value,navVerseIndex.text);
}

- (IBAction) valueChangedStepper:(UIStepper *)sender {
    //NSLog(@" Stepper Value:%f",sender.value);
    [self setFontSizes:sender.value];
    [self reset];
    //NSLog(@"AlahhumaSaalehAlaMuahmmadin wa allah Allay Muhaammadin");
}

-(IBAction) fontsTouchDown:(id)sender {
    //NSLog(@"Font Button Touch Down Event Fired for %@:",sender);
    FontsViewController *fontsViewController = [[FontsViewController alloc] initWithFonts:fonts ofFamily:fontFamily withArabicFont:textFont.font andWithEnglishFont:translationFont.font byController:self];
    [self.navigationController pushViewController:fontsViewController animated:YES];
    
}

-(IBAction) favoriteButtonTouchDown:(id)sender {
    //NSLog(@"Button Touch Down Event Fired for %@:",sender);
    scrollIndexPath = [[self.tableView indexPathsForVisibleRows] objectAtIndex:0];
    NSUInteger row = (int)[scrollIndexPath section];
    CGPoint verseLoc = [[allVerses objectAtIndex:row] CGPointValue];
    int chapter_no = (int)verseLoc.x;
    int verse_no  = (int)verseLoc.y;
    
    
    UIButton* favorite = (UIButton*) sender;
    if ([favorite isSelected]) {
        [favorite setImage:[UIImage imageNamed:IMG_FAV_ADD] forState:UIControlStateNormal];
        [favorite setSelected:NO];
        [dataController updateFavorite:NO chapterNo:chapter_no verse:verse_no];
    }
    else {
        [favorite setImage:[UIImage imageNamed:IMG_FAV_RMV] forState:UIControlStateNormal];
        [favorite setSelected:YES];
        [dataController updateFavorite:YES chapterNo:chapter_no verse:verse_no];
    }
    
}

-(IBAction) viewSideBySideTouchDown:(id)sender {
    //NSLog(@"Button Touch Down Event Fired for %@:",sender);
    UIButton* viewSideBySideButton = (UIButton*) sender;
    if (verseView==ViewTop) {
        verseView=ViewSideBySide;
        [viewSideBySideButton setImage:[UIImage imageNamed:IMG_VIEW_TOP] forState:UIControlStateNormal];
        [viewSideBySideButton setSelected:NO];
    }
    else {
        verseView=ViewTop;
        [viewSideBySideButton setImage:[UIImage imageNamed:IMG_VIEW_SIDE] forState:UIControlStateNormal];
        [viewSideBySideButton setSelected:YES];
    }
    [self reset];
    
    
}
-(void) resetRowHeights {
    //NSLog(@"****      Reset Row Heights ");
    rowHeights = [NSMutableArray arrayWithCapacity:[allVerses count]];
    for (int i=0; i< [allVerses count]; i++) {
        [rowHeights addObject:[NSNull null]];
    }
}
- (void) reset {
    //NSLog(@"****      Reset");
    UIDeviceOrientation deviceOrientation = [UIDevice currentDevice].orientation;
    [self setLabelSize:deviceOrientation];
    [self refreshIDs];
    [self clearDrawLocs];
    [self resetRowHeights];
    [self dataChanged];
    [self addLog];
}

-(void)dataChanged {
    NSLog(@"Data Changed");
    scrollIndexPath=nil;
    [self dataChanged:nil];
}

-(void)dataChanged:(NSIndexPath *) _scrollIndexPath {
    // [self.tableView beginUpdates];
    [self.tableView reloadData];
    // sleep(0.5f);
    // [tableView scrollToRowAtIndexPath:scrollIndexPath atScrollPosition:UITableViewScrollPositionTop animated:NO];
    // [self.tableView setNeedsLayout];
}

- (void) setLabelSize:(UIDeviceOrientation) deviceOrientation {
    CGRect rect = [Utils getScreenSize];
    widthCushion=[Utils isiPad]?12:6;
    if (UIDeviceOrientationIsLandscape(deviceOrientation) ) {
        //NSLog(@"setLabelSize Landscape");
        maximumLabelSize  = CGSizeMake(rect.size.height-widthCushion*2,9999);
        maximumLabelSize2 = CGSizeMake(rect.size.height/2-widthCushion,9999);
    }
    else{
        //NSLog(@"setLabelSize Portrait");
        maximumLabelSize  = CGSizeMake(rect.size.width-widthCushion,9999);
        maximumLabelSize2 = CGSizeMake(rect.size.width/2-widthCushion,9999);
    }
}

-(void) clearDrawLocs {
    for (NSString* verseID in verses) {
        Verse* verse = [verses objectForKey:verseID];
        //NSLog(@"ClearDrawLocs %i",[verse.verseno intValue]);
        [Utils clearArray:verse.verseDrawArabic];
        if (verse.verseDrawArabic!=nil) {
            [verse.verseDrawArabic removeAllObjects];
            verse.verseDrawArabic=nil;
            
        }
        if (verse.verseDrawTranslation!=nil) {
            [verse.verseDrawTranslation removeAllObjects];
            verse.verseDrawTranslation=nil;
            
        }
        
        [Utils clearArray:verse.verseDrawTranslation];
        [Utils clearArray:verse.verseDrawTranslationSplitWord];
        [Utils clearArray:verse.verseDrawTransliteration];
        [Utils clearArray:verse.verseMorphology];
        
        verse.verseDrawTranslationSplitWord=nil;
        verse.verseDrawTransliteration=nil;
        verse.verseMorphology=nil;
        if (!CGRectIsEmpty(verse.rectText)) verse.rectText=CGRectMake(0,0,0,0);
        if (!CGRectIsEmpty(verse.rectTranslation)) verse.rectTranslation=CGRectMake(0,0,0,0);
        
    }
}


//-(void)viewWillLayoutSubviews{
//    [super viewWillLayoutSubviews];
//    //NSLog (@"Will Layout subviewsOrientation changed");
//    UIInterfaceOrientation devOrientation = self.interfaceOrientation;
//    CGFloat scrollWidth_L = self.view.bounds.size.width;
//    CGFloat scrollWidth_P = self.view.bounds.size.height;
//
//    if (UIInterfaceOrientationLandscapeLeft == devOrientation || UIInterfaceOrientationLandscapeRight == devOrientation) {
//        // Code for landscape setup
//
//    } else {
//        // Code for portrait setup
//
//    }
//}

- (void) resize: (float) increment scale:(float) scale {
    navStepper.value = MIN(navStepper.maximumValue,MAX(navStepper.minimumValue, navStepper.value + floor(increment)));
    [self setFontSizes:navStepper.value];
    
    NSLog(@"Stepper Value=%f Increment: %f,%f",navStepper.value,increment,scale);
    [self reset];
    //[self dataChanged];
}

-(int) getSettingsHeight{
    if (verseSettingsHeight==0) {
        DefinitionCells* cellDefinition;
        NSArray* nib = [[NSBundle mainBundle] loadNibNamed:@"DefinitionCells" owner:self options:nil];
        cellDefinition = (DefinitionCells *) [nib objectAtIndex:8];
        verseSettingsHeight = cellDefinition.frame.size.height;
    }
    return verseSettingsHeight;
}


- (IBAction) valueChangedSwitch:(UISwitch*) aSwitch{
    if (aSwitch==switchArabic) {
        showArabic=aSwitch.isOn;
    }
    else if (aSwitch==switchTranslation) {
        showTranslation=aSwitch.isOn;
    }
    else if (aSwitch==switchTransliteration) {
        showTransliteration=aSwitch.isOn;
    }
    //[self dataChanged];
    //[self setNeedsDisplay];
    
}

- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch {
    if ( [gestureRecognizer isMemberOfClass:[UITapGestureRecognizer class]] ) {
        // Return NO for views that don't support Taps
    } else if ( [gestureRecognizer isMemberOfClass:[UISwipeGestureRecognizer class]] ) {
        if (touch.view == navSlider) {
            return NO;
        }
    }
    
    return YES;
}





- (IBAction)handleSwipeFrom:(UISwipeGestureRecognizer *)recognizer {
    
    CGPoint location = [recognizer locationInView:self.view];
    
    if (recognizer.direction == UISwipeGestureRecognizerDirectionLeft) {
        location.x -= 220.0;
    }
    else {
        location.x += 220.0;
    }
    NSLog(@"Location:%f",location.x);
}

- (IBAction) sliderTouchUp:(UISlider *)slider {
    NSLog(@" Slider Value:%f",slider.value);
    
    //sleep(3);
    //verseIndex=(int)(sliderVerses.value + 0.5f);
    verseIndex=[navVerseIndex.text intValue]-1;
    //NSLog(@"SliderVerse.Value=%f SliderVerseIndex=%@",sliderVerses.value,sliderVerseIndex.text);
    [self scrollToVerseIndex];
    //[self.tableView setNeedsDisplay];
    
    //NSLog(@"Scroll to Verse No.:%i",verseIndex);
    //[[self tableView] scrollToRowAtIndexPath:scrollIndexPath atScrollPosition:UITableViewScrollPositionBottom animated:YES];
    
}

- (void) scrollToVerseIndex{
    //NSLog(@"scrollToVerseIndex");
    int pageToScroll=0;
    int versesFound =0;
    
    while (verseIndex+1 > versesFound) {
        versesFound = versesFound + [self createVersePlaceholder:pageToScroll];
        if (verseIndex+1 > versesFound) pageToScroll++;
    }
    page = pageToScroll;
    int verseToGo = verseIndex - (int)(versesFound - [[pagesLocs objectAtIndex:page] count]);
    scrollIndexPath = [NSIndexPath indexPathForRow:0 inSection:verseToGo];
    [self.tableView scrollToRowAtIndexPath:scrollIndexPath atScrollPosition:UITableViewScrollPositionTop animated:NO];
    //NSLog(@"VersesFound:%i VerseIndex:%i Scroll to Verse No.:%i",versesFound,verseIndex,verseToGo);
    [self dataChanged:scrollIndexPath];
    //NSLog(@"Scroll to Verse No.:%i",scrollIndexPath.row);
    
    
}

- (void) event:(enum EVENTS)evt Sender:(id)sender{
    
}


static BOOL _draggingView = NO;
static bool pulledUp=NO;
static bool pulledDown=NO;

- (void)scrollViewWillBeginDragging:(UIScrollView *)scrollView {
    _draggingView = YES;
}

- (void)scrollViewDidEndDragging:(UIScrollView *)scrollView willDecelerate:(BOOL)decelerate {
    _draggingView = NO;
    if (pulledUp) {
        rowDisplayWindow.length= MIN(rowDisplayWindow.length+rowIncr,[allVerses count]);
        [tableView reloadData];
        pulledUp=NO;
    }
    else if (pulledDown) {
        NSLog(@"Pull Down");
        pulledDown=NO;
    }
    
}

- (void)scrollViewDidScroll:(UIScrollView *)scrollView {
    NSInteger pullingDetectFrom = 50;
    if (tableView.contentOffset.y < -pullingDetectFrom) {
        _draggingView = NO;
        pulledDown=YES;
        
    } else if (tableView.contentSize.height <= tableView.frame.size.height && tableView.contentOffset.y > pullingDetectFrom) {
        _draggingView = NO;
        pulledUp=YES;
    } else if (tableView.contentSize.height > tableView.frame.size.height &&
               tableView.contentSize.height-tableView.frame.size.height-tableView.contentOffset.y < -pullingDetectFrom) {
        _draggingView = NO;
        pulledUp=YES;
    }
}






-(void) showSwipes {
    if (page > 0) {
        leftSwipe.hidden=NO;
        leftSwipe.frame =CGRectMake(0,leftSwipe.frame.origin.y, leftSwipe.frame.size.width,leftSwipe.frame.size.height);
        [self.view bringSubviewToFront:leftSwipe];
    }
    if (page < pageCount-1) {
        rightSwipe.hidden=NO;
        [self.view bringSubviewToFront:rightSwipe];
    }
}



-(void) hideSwipes {
    //NSLog(@"Me is here at 1 minute delay");
    leftSwipe.hidden=YES;
    rightSwipe.hidden=YES;
}

- (void) refreshVersesFrom:(int) verseNo1 toVerseNo:(int) verseNo2 {
    //if (pageFilled==NO) {
    NSMutableArray* verseLocs= [pagesLocs objectAtIndex:page];
    for (int v=verseNo1;v< MIN([verseLocs count],verseNo2);v++) {
        [self getVerseAt:v];
        CGPoint verseLoc = [[verseLocs objectAtIndex:v] CGPointValue];
        natural_t memoryLeft = [Utils get_free_memory];
        //NSLog(@" Verse nos:%i,%i (%d)",(int)verseLoc.x,(int) verseLoc.y,memoryLeft);
    }
}

- (int) createVersePlaceholder:(int) pageNo {
    if (pageNo >= [pagesRefreshed count]) [pagesRefreshed addObject:[NSNumber numberWithBool:NO]];
    BOOL pageFilled = [(NSNumber*)[pagesRefreshed objectAtIndex:pageNo] boolValue];
    if (pageFilled==NO) {
        int _page=0;
        int _verseIndex=0;
        
        while (_page < pageNo)  {
            NSMutableArray* verseLocs= [pagesLocs objectAtIndex:_page];
            _verseIndex = _verseIndex + (int)[verseLocs count];
            _page++;
        }
        NSMutableArray* verseLocs= [[NSMutableArray alloc] init ];
        int verseCount = (int)[allVerses count];
        int verseAdded=0;
        while (verseAdded < pageMax && _verseIndex < verseCount) {
            CGPoint verseLoc = [[allVerses objectAtIndex:_verseIndex] CGPointValue];
            [verseLocs addObject:[allVerses objectAtIndex:_verseIndex]];
            _verseIndex++;
            verseAdded++;
        }
        if (_verseIndex == verseCount) {
            pageCount=pageNo+1;
        }
        [pagesLocs addObject:verseLocs];
        pageFilled=YES;
        [pagesRefreshed replaceObjectAtIndex:pageNo withObject:[NSNumber numberWithBool:pageFilled]];
    }
    return (int)[[pagesLocs objectAtIndex:pageNo] count];
}

- (int) setSliderVerseIndexAtPage:(int) pageNo {
    int _page=0;
    int _verseIndex=0;
    while (_page < pageNo)  {
        NSMutableArray* verseLocs= [pagesLocs objectAtIndex:_page];
        _verseIndex = _verseIndex + (int) [verseLocs count];
        _page++;
    }
    //NSLog(@"VerseIndex:%i Scroll to Verse No.:%i",_verseIndex,scrollIndexPath.section);
    navSlider.value=_verseIndex+1;
    navVerseIndex.text=[NSString stringWithFormat:@"%i",_verseIndex+1];
    return _verseIndex +1;
}




- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldRecognizeSimultaneouslyWithGestureRecognizer:(UIGestureRecognizer*) otherGestureRecognizer{
    return YES;
}

-(void)scale:(id)sender {
    [self.view bringSubviewToFront:[(UIPinchGestureRecognizer*)sender view]];
    if (lastScale==1.0f) {
        CGAffineTransform currentTransform = [(UIPinchGestureRecognizer*)sender view].transform;
        originalTransform = CGAffineTransformScale(currentTransform, 1.0, 1.0);
        //originalTransform = [(UIPinchGestureRecognizer*)sender view].transform;
    }
    if([(UIPinchGestureRecognizer*)sender state] == UIGestureRecognizerStateEnded) {
        //NSLog(@"Sclae:%f",lastScale);
        [[(UIPinchGestureRecognizer*)sender view] setTransform:originalTransform];
        float increment = lastScale < 1.0? -(1/lastScale * 4.0):lastScale*5.0;
        [self resize:increment scale:lastScale];
        lastScale = 1.0;
        [self dataChanged];
    }
    else {
        CGFloat scale = 1.0 - (lastScale - [(UIPinchGestureRecognizer*)sender scale]);
        CGAffineTransform currentTransform = [(UIPinchGestureRecognizer*)sender view].transform;
        CGAffineTransform newTransform = CGAffineTransformScale(currentTransform, scale, scale);
        [[(UIPinchGestureRecognizer*)sender view] setTransform:newTransform];
        lastScale = [(UIPinchGestureRecognizer*)sender scale];
    }
    disableSwipeIcons=YES;
    [self hideSwipes];
    
}

//- (void)Press:(UILongPressGestureRecognizer*)gesture {
//    [self showHeader:NO];
//    CGPoint pressLocation = [gesture locationInView:self.tableView];
//    NSIndexPath* selectedIndexPath = [self.tableView indexPathForRowAtPoint:pressLocation];
//    int row = selectedIndexPath.section;
//    if (row<0) return;
//    selectedVerse=[self getVerseAt:row];
//    [self initPopUpView];
//}



- (void)longPress:(UILongPressGestureRecognizer*)gesture {
    switch (gesture.state) {
        case UIGestureRecognizerStateBegan:{
            //            [self.tableView beginUpdates];
            //            CGPoint tappedLocation = [gesture locationInView:self.tableView];
            //            NSIndexPath* selectedIndexPath = [self.tableView indexPathForRowAtPoint:tappedLocation];
            //            [self.tableView reloadRowsAtIndexPaths:[NSArray arrayWithObjects:selectedIndexPath, nil] withRowAnimation:UITableViewRowAnimationNone];
            //            [self.tableView endUpdates];
        }
            break;
        case UIGestureRecognizerStateEnded:{
            [self showWordAt:[gesture locationInView:self.tableView]];
        }
            break;
        default:
            break;
    }
}

-(Word*) getArabicWordAt:(CGPoint) tappedLocation {
    NSIndexPath* selectedIndexPath = [self.tableView indexPathForRowAtPoint:tappedLocation];
    CGRect rectVerse = [self.tableView rectForRowAtIndexPath: selectedIndexPath];
    //NSLog(@"RectVerse=(%f,%f,%f,%f)",rectVerse.origin.x,rectVerse.origin.y,rectVerse.size.width,rectVerse.size.height);
    int row = (int)selectedIndexPath.section;
    if (row<0) return;
    Verse* verse=[self getVerseAt:row RefreshVerseLocs:NO];
    CGRect rectText = verse.rectText;
    if (verse.verseDrawArabic==nil) return nil;
    int c= widthCushion/2;
    NSString* token;
    int tokenCtr=1, tokenID=-1;
    for (NSMutableArray* record in verse.verseDrawArabic) {
        token=[record objectAtIndex:0];
        //NSLog(@"[%@]",token);
        //CGPoint point= [(NSValue*)[record objectAtIndex:1] CGPointValue];
        CGRect  rect = [(NSValue*)[record objectAtIndex:2] CGRectValue];
        CGRect  rectToken;
        if (verseView==ViewSideBySide) {
            rectToken = CGRectMake(rectText.origin.x+rect.origin.x, rectVerse.origin.y+rect.origin.y, rect.size.width, rect.size.height);
        }
        else {
            rectToken = CGRectMake(rectText.origin.x+rect.origin.x - widthCushion*2, rectVerse.origin.y+rect.origin.y, rect.size.width, rect.size.height);
        }
//        NSLog(@"    Point=(%f,%f)",tappedLocation.x,tappedLocation.y);
//        NSLog(@"    VerseRect=(%f,%f,%f,%f)",rectVerse.origin.x,rectVerse.origin.y,rectVerse.size.width,rectVerse.size.height);
//        NSLog(@"    RectText=(%f,%f,%f,%f)",verse.rectText.origin.x,verse.rectText.origin.y,verse.rectText.size.width,verse.rectText.size.height);
//        NSLog(@"    TokenRect=(%f,%f,%f,%f)",rect.origin.x,rect.origin.y,rect.size.width,rect.size.height);
//        NSLog(@"    New Rect=(%f,%f,%f,%f)",rectToken.origin.x,rectToken.origin.y,rectToken.size.width,rectToken.size.height);
//        //NSLog(@"    ChapterNo:%i & VerseNo:%i  [%@]: %i Point=(%f,%f) Rect=(%f,%f,%f,%f)",[verse.chapterno intValue],[verse.verseno intValue],token,tokenCtr,tappedLocation.x,tappedLocation.y,rectToken.origin.x,rectToken.origin.y,rectToken.size.width,rectToken.size.height);
        
        if (CGRectContainsPoint(rectToken, tappedLocation)) {
            tokenID=tokenCtr;
            //NSLog(@"Token=%@",token);
        }
        tokenCtr ++;
        //NSLog(@"ChapterNo:%i & VerseNo:%i  [%@]: %i (%f,%f)",[verse.chapterno intValue],[verse.verseno intValue],token,tokenCt,point.x,point.y);
        //tokenIndex++;
    }
    if (tokenID==-1) return nil;
    //Word* selectedWord = [dataController getWord:@"11"];
    return [dataController getWord:[verse.chapterno intValue] VerseNo:[verse.verseno intValue] TokenNo:tokenID];
    
}

-(NSString*) getTranslationWordAt:(CGPoint) tappedLocation {
    NSIndexPath* selectedIndexPath = [self.tableView indexPathForRowAtPoint:tappedLocation];
    CGRect rectVerse = [self.tableView rectForRowAtIndexPath: selectedIndexPath];
    //NSLog(@"RectVerse=(%f,%f,%f,%f)",rectVerse.origin.x,rectVerse.origin.y,rectVerse.size.width,rectVerse.size.height);
    int row = (int)selectedIndexPath.section;
    if (row<0) return;
    Verse* verse =[self getVerseAt:row RefreshVerseLocs:NO];
    if (verse.verseDrawTranslation==nil) return nil;
    CGRect rectTranslation = verse.rectTranslation;
    
    NSString *token, *selectedToken;
    int tokenCtr=1, tokenID=-1;
    int c= widthCushion/2;
    for (NSMutableArray* record in verse.verseDrawTranslation) {
        token=[record objectAtIndex:0];
        //NSLog(@"[%@]",token);
        CGRect  rect = [(NSValue*)[record objectAtIndex:2] CGRectValue];
        CGRect  rectToken;
        if (verseView==ViewSideBySide) {
            rectToken = CGRectMake(rectTranslation.origin.x+rect.origin.x-c,rectVerse.origin.y+rect.origin.y-c, rect.size.width+c, rect.size.height+c);
        }
        else {
            rectToken = CGRectMake(rectTranslation.origin.x+rect.origin.x + (widthCushion*2)-(widthCushion/2) -c, rectVerse.origin.y+rectTranslation.origin.y+ rect.origin.y-c, rect.size.width+c, rect.size.height+c);
        }
//        NSLog(@"    Point =(%f,%f)",tappedLocation.x,tappedLocation.y);
//        NSLog(@"    Verse =(%f,%f,%f,%f)",rectVerse.origin.x,rectVerse.origin.y,rectVerse.size.width,rectVerse.size.height);
//        NSLog(@"    Trans =(%f,%f,%f,%f)",rectTranslation.origin.x,rectTranslation.origin.y,rectTranslation.size.width,rectTranslation.size.height);
//        NSLog(@"    Token =(%f,%f,%f,%f)",rect.origin.x,rect.origin.y,rect.size.width,rect.size.height);
//        NSLog(@"    New   =(%f,%f,%f,%f)",rectToken.origin.x,rectToken.origin.y,rectToken.size.width,rectToken.size.height);
//        
        CGRectMake(rect.origin.x, rectVerse.origin.y+rect.origin.y, rect.size.width, rect.size.height);
        //NSLog(@"ChapterNo:%i & VerseNo:%i  [%@]: %i Point=(%f,%f) Rect=(%f,%f,%f,%f)",[verse.chapterno intValue],[verse.verseno intValue],token,tokenCtr,tappedLocation.x,tappedLocation.y,rectToken.origin.x,rectToken.origin.y,rectToken.size.width,rectToken.size.height);
        if (CGRectContainsPoint(rectToken, tappedLocation)) {
            tokenID=tokenCtr;
            selectedToken=[record objectAtIndex:0];
            
        }
        tokenCtr ++;
    }
    if (tokenID==-1) return nil;
    return selectedToken;
    
}


-(void) showWordAt:(CGPoint) tappedLocation {
    Word* selectedWord = showArabic?[self getArabicWordAt:tappedLocation]:nil;
    if (selectedWord!=nil) {
        WordViewController *wordViewController = [[WordViewController alloc] initWithWord:selectedWord atIndex:0 onTitle:@"Allah" controller:dataController NIB:@"WordViewController"];
        [wordViewController initialize];
        [wordViewController setCallingController:self];
        [self.navigationController pushViewController:wordViewController animated:NO];
    }
    NSString* selectedTranslationWord = showTranslation?[self getTranslationWordAt:tappedLocation]:nil;
    if (selectedTranslationWord!=nil) {
        //NSLog(@"Translation Word=%@",selectedTranslationWord);
        NSArray* display =[self getDisplayAttributesFor:NO transation:YES transliteration:NO splitword:NO];
        VerseViewController *verseViewController =  [[VerseViewController alloc] initWithTranslationWord:selectedTranslationWord verseDisplayFormat:display controller:dataController NIB:@"VerseViewController"];
        verseViewController.hidesBottomBarWhenPushed = YES;
        verseViewController.title=@"Verses";
        [verseViewController setCallingController:nil];
        //[verseViewController.navigationItem.titleView = [verseViewController navHeader];
        // verseViewController.navigationItem.titleView.hidden=NO;
        
        [self.navigationController pushViewController:verseViewController animated:YES];
        
        //[wordViewController initialize];
        //[wordViewController setCallingController:self];
        //[self.navigationController pushViewController:wordViewController animated:NO];
    }
    
}


-(void) setCallingController:(UIViewController*) viewController {
    callingController=viewController;
}

-(void) tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath {
    if([indexPath row] == 0 && spinner.isAnimating){
        NSLog(@" Animation ended ");
        [spinner stopAnimating];
    }
}


- (void)viewDidUnload {
    [super viewDidUnload];
    NSLog(@"ViewDidUnload");
    
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation{
    NSLog(@"Orientation message recieved.");
    return YES;//(interfaceOrientation == UIInterfaceOrientationPortrait);
}


- (void)didRotateFromInterfaceOrientation:(UIInterfaceOrientation)fromInterfaceOrientation {
    NSLog(@"Rotate Go!");
    [self reset];
    [self.view setContentMode:UIViewContentModeRedraw];
    currentDeviceOrientation=[UIDevice currentDevice].orientation;
    
}


bool isShowingLandscapeView=false;

-(NSUInteger)supportedInterfaceOrientations {
    //NSLog(@"Verse View Controller Supported Orientation.");
    UIDeviceOrientation deviceOrientation = [UIDevice currentDevice].orientation;
    if (UIDeviceOrientationIsLandscape(deviceOrientation) ) isOrientationPortrait=NO; else isOrientationPortrait=YES;
    return UIInterfaceOrientationMaskPortrait | UIInterfaceOrientationMaskLandscapeLeft;
    
}


- (void)orientationChanged:(NSNotification *)notification {
    UIDeviceOrientation deviceOrientation = [UIDevice currentDevice].orientation;
    if ((UIDeviceOrientationIsPortrait(deviceOrientation) && isOrientationPortrait) ||  (UIDeviceOrientationIsLandscape(deviceOrientation) && !isOrientationPortrait)){
        NSLog(@"........Orientation Not Changed");
    }
    else {
        NSLog(@"........Orientation Changed");
        if (UIDeviceOrientationIsLandscape(deviceOrientation) ) isOrientationPortrait=NO; else isOrientationPortrait=YES;
        [self reset];
        [self.view setContentMode:UIViewContentModeRedraw];
    }
    
}




#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    //NSLog(@"Number of Sections: %i",(rowDisplayWindow.length-rowDisplayWindow.location));
    return rowDisplayWindow.length-rowDisplayWindow.location;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 1;
    
}
-(NSString*) getVerseIdAtVerseIndex {
    NSMutableArray* verseLocs= [pagesLocs objectAtIndex:page];
    CGPoint verseLoc = [[verseLocs objectAtIndex:verseIndex] CGPointValue];
    NSString* verseId = [NSString stringWithFormat:@"%i:%i",(int)verseLoc.x,(int)verseLoc.y];
    return verseId;
}

-(Verse*) getVerseAt:(NSInteger) row {
    return [self getVerseAt:row RefreshVerseLocs:YES];
}


-(Verse*) getVerseAt:(NSInteger) row RefreshVerseLocs:(BOOL) refreshLocs {
    //int page = (row/pageMax);
    //row = row - (pageMax*page);
    //NSMutableArray* verseLocs= [pagesLocs objectAtIndex:page];
    //CGPoint verseLoc = [[verseLocs objectAtIndex:row] CGPointValue];
    CGPoint verseLoc = [[allVerses objectAtIndex:row] CGPointValue];
    return [self getVerseAt:(int)verseLoc.x _verseNo:(int)verseLoc.y RefreshVerseLocs:refreshLocs];
}

-(Verse*) getVerseAt:(int)_chapterNo _verseNo:(int) _verseNo RefreshVerseLocs:(BOOL) refreshLocs {
    NSString* verseId = [NSString stringWithFormat:@"%i:%i",_chapterNo,_verseNo];
    //NSLog(@"    [getVerseAt] VerseId:%@",verseId);
    Verse* verse = (Verse*) [verses objectForKey:verseId];
    if (refreshLocs) [self addDrawLocsFor:verse atIndex:verseId];
    return verse;
}


-(int) getVerseHeightAt:(NSInteger) row {
    CGPoint verseLoc = [[allVerses objectAtIndex:row] CGPointValue];
    NSString* verseId = [NSString stringWithFormat:@"%i:%i",(int)verseLoc.x,(int)verseLoc.y];
    Verse* verse = (Verse*) [verses objectForKey:verseId];
    return [self getVerseSizeFor:verse];
}



-(void) addDrawLocsFor:(Verse*) verse atIndex:(NSString*) verseId{
    //NSLog(@"        addDrawLocs: Chapter No: %i Verse No:%i",[verse.chapterno intValue],[verse.verseno intValue]);
    int height=0,width=0;
    if (verseView==ViewSideBySide) {
        if (showArabic && verse.verseDrawArabic==nil) {
            int x=0,y=0;
            CGRect rect = CGRectMake(x,y,maximumLabelSize2.width,maximumLabelSize2.height);
            verse.verseDrawArabic=[self getVerseDraw:rect textMain:verse.verse splitWordText:nil inTextDirection:RTL fontToSize:textFont withHeightCushion:textFont.heightCushion];
            rect = [self getVerseRect:verse.verseDrawArabic usingFont:textFont];
            if (rect.size.width < maximumLabelSize2.width) rect.origin.x=maximumLabelSize2.width +(maximumLabelSize2.width-rect.size.width);
            else rect.origin.x=maximumLabelSize2.width;
            verse.rectText=rect;
            
        }
        if (showTranslation && verse.verseDrawTranslation==nil) {
            int x=0,y=0;
            CGRect rect = CGRectMake(x,y,maximumLabelSize2.width,maximumLabelSize2.height);
            verse.verseDrawTranslation=[self getVerseDraw:rect textMain:verse.translation splitWordText:nil inTextDirection : LTR fontToSize:translationFont withHeightCushion:1.0];
            rect=[self getVerseRect:verse.verseDrawTranslation usingFont:translationFont];
            verse.rectTranslation=rect;
        }
        height = borderHeight + MAX(verse.rectText.size.height,verse.rectTranslation.size.height);
        width = MAX(verse.rectText.size.width,verse.rectTranslation.size.width);
    }
    else {
        int yOffset=0;borderHeight=0;
        int x=0,y=0;
        if (showArabic && verse.verseDrawArabic==nil) {
            CGRect rect = CGRectMake(x,y,maximumLabelSize.width,maximumLabelSize.height);
            verse.verseDrawArabic=[self getVerseDraw:rect textMain:verse.verse splitWordText:nil inTextDirection:RTL fontToSize:textFont withHeightCushion:textFont.heightCushion];
            rect = [self getVerseRect:verse.verseDrawArabic usingFont:textFont];
            if (rect.size.width < maximumLabelSize.width) rect.origin.x=(maximumLabelSize.width-rect.size.width);
            verse.rectText=rect;
            y = y + verse.rectText.size.height;
        }
        if (showTransliteration && verse.verseDrawTransliteration==nil) {
            CGRect rect = CGRectMake(x,y,maximumLabelSize.width,maximumLabelSize.height);
            verse.verseDrawTransliteration=[self getVerseDraw:rect textMain:[self removeTransliterationMarks:verse.transliteration] splitWordText:nil inTextDirection:LTR fontToSize:transliterationFont withHeightCushion:1.0];
            rect =[self getVerseRect:verse.verseDrawTransliteration usingFont:translationFont];
            rect.origin.y=rect.origin.y+y;
            verse.rectTransliteration=rect;
            y = y + verse.rectTransliteration.size.height;
        }
        if (showTranslation && verse.verseDrawTranslation==nil) {
            CGRect rect = CGRectMake(x,y,maximumLabelSize.width,maximumLabelSize.height);
            verse.verseDrawTranslation=[self getVerseDraw:rect textMain:verse.translation splitWordText:nil inTextDirection:LTR fontToSize:translationFont withHeightCushion:1.0];
            rect=[self getVerseRect:verse.verseDrawTranslation usingFont:translationFont];
            rect.origin.y=rect.origin.y+y;
            verse.rectTranslation=rect;
            y = y + verse.rectTranslation.size.height;
        }
        if (showTranslationSplitWord && verse.verseDrawTranslationSplitWord==nil) {
            CGRect rect = CGRectMake(x,y,maximumLabelSize.width,maximumLabelSize.height);
            verse.verseDrawTranslationSplitWord=[self getVerseDraw:rect textMain:verse.verse splitWordText:verse.translation_split_word inTextDirection : RTL fontToSize:fontToSize withHeightCushion:1.0];
            rect=[self getVerseRect:verse.verseDrawTranslationSplitWord usingFont:fontToSize];
            if (rect.size.width < maximumLabelSize.width) rect.origin.x=maximumLabelSize.width-rect.size.width;
            rect.origin.y=rect.origin.y+y;
            verse.rectTranslationSplitWord=rect;
        }
        if (showArabic) {
            height = height + borderHeight + verse.rectText.size.height;
            width = MAX(width,verse.rectText.size.width);
        }
        if (showTranslation) {
            height = height + borderHeight +verse.rectTranslation.size.height;
            width  = MAX(width,verse.rectTranslation.size.width);
        }
        if (showTransliteration) {
            height = height + borderHeight +verse.rectTransliteration.size.height;
            width = MAX(width,verse.rectTransliteration.size.width);
        }
        if (showTranslationSplitWord) {
            height = height + borderHeight + verse.rectTranslationSplitWord.size.height;
            width = MAX(width,verse.rectTranslationSplitWord.size.width);
        }
    }
    verse.rect = CGRectMake(0,0,width,height);
}
-(NSString*) getCellIdentifierFor:(Verse*) verse {
    NSString* verseId = [NSString stringWithFormat:@"%i:%i",(int)verse.chapterno,(int)verse.verseno];
    NSString* cellIdentifier = [NSString stringWithFormat:@"%i,%@,%@",instance_id,verseId,displayID];
    return cellIdentifier;
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section {
    int row =(int)section;
    //int page = (row /pageMax);
    //row = row - (pageMax*page);
    //NSMutableArray* verseLocs= [pagesLocs objectAtIndex:page];
    //NSLog(@"section=%i,page=%i,row=%i,verseLocsCount=%i",section,page,row,[verseLocs count]);
    //Verse* verse = (Verse*)[allVerses objectAtIndex:row];
    if (row < [allVerses count]){
        CGPoint verseLoc = [[allVerses objectAtIndex:section] CGPointValue];
        NSString* verseId = [NSString stringWithFormat:@"%i:%i",(int)verseLoc.x,(int)verseLoc.y];
        return verseId;
    }
}

//- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section {
//    NSLog(@" Section:%i",section);
//    //if (section==0)return 50; else return 10;
//    //if (pageCount==1) return 0;
//    return 15;
//}
//- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section {
//    int row =(int)section;
//    NSString* verseId=@"";
//    if (row < rowcount){
//        CGPoint verseLoc = [[allVerses objectAtIndex:section] CGPointValue];
//        verseId = [NSString stringWithFormat:@"%i:%i",(int)verseLoc.x,(int)verseLoc.y];
//    }
//    int size = 20;
//    UILabel *myLabel = [[UILabel alloc] init];
//    myLabel.frame = CGRectMake(maximumLabelSize2.width-size/2, 0, size, 20);
//    myLabel.font = [UIFont boldSystemFontOfSize:10];
//    myLabel.text=verseId;
//   // myLabel.textColor = [UIColor whiteColor];
//    myLabel.backgroundColor = [UIColor lightGrayColor];
//    myLabel.opaque = YES;
//    myLabel.textAlignment= NSTextAlignmentCenter;
//    UIView *headerView = [[UIView alloc] init];
//    [headerView addSubview:myLabel];
//    return headerView;
//}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    NSInteger row = indexPath.section;
    NSInteger rowcount= rowDisplayWindow.length;
    //NSLog(@"tableViewCellForRow AtIndexPath:%tu",row);
    
    if (row==-1) {
        //static NSString *kCellID2 = @"VerseViewSettings";
        //UITableViewCell *cell = [self.tableView dequeueReusableCellWithIdentifier:kCellID2];
        NSString* cellIdentifier = [NSString stringWithFormat:@"%i.%@",instance_id,@"VerseViewToolbar"];
        NSObject* _cell = [cellVerse objectForKey:cellIdentifier];
        if (_cell == nil)	{
            NSArray* nib = [[NSBundle mainBundle] loadNibNamed:@"DefinitionCells" owner:self options:nil];
            DefinitionCells* cellDefinition;
            cellDefinition = (DefinitionCells *) [nib objectAtIndex:8];
            [cellDefinition setController:self];
            [cellDefinition switchArabic].on=showArabic;
            [popup addSubview:cellDefinition];
            [cellDefinition switchTranslation].on=showTranslation;
            [cellDefinition switchTranslationSplitWord].on=showTranslationSplitWord;
            sliderVerses = [cellDefinition sliderVerses];
            sliderVerses.minimumValue=1;
            sliderVerses.maximumValue=[allVerses count];
            lblVerseIndex = [cellDefinition verseIndex];
            //sliderVerses.value=12;
            [cellDefinition minVerseIndex].text=@"1";
            [cellDefinition maxVerseIndex].text=[NSString stringWithFormat:@"%zd",[allVerses count]];
            [cellDefinition verseIndex].text=[NSString stringWithFormat:@"%i",verseIndex];
            [cellVerse setValue:cellDefinition forKey:cellIdentifier];
            return cellDefinition;
        }
        else {
            return (DefinitionCells*) _cell;
        }
    }
    else
        if (row<rowcount){
            //int page = (row/pageMax);
            //row = row - (pageMax*page);
            //NSMutableArray* verseLocs= [pagesLocs objectAtIndex:page];
            //CGPoint verseLoc = [[verseLocs objectAtIndex:row] CGPointValue];
            
            //
            CGPoint verseLoc = [[allVerses objectAtIndex:row] CGPointValue];
            //NSLog(@"cellForRow:%i",row);
            Verse* verse = [self getVerseAt:row RefreshVerseLocs:YES];
            //            if (ABS(verse.rect.size.height -[self.tableView rectForRowAtIndexPath:indexPath].size.height)>5) {
            //
            //NSLog(@"***  cellForRow:%i TableHeight=%f(%i) RectHeight=%f (Text+Trans+Split) =(%f+%f+%f)",row,[self.tableView rectForRowAtIndexPath:indexPath].size.height,verse.rowHeight,verse.rect.size.height, CGRectIsEmpty(verse.rectText)?0:verse.rectText.size.height,CGRectIsEmpty(verse.rectTranslation)?0:verse.rectTranslation.size.height,CGRectIsEmpty(verse.rectTranslationSplitWord)?0:verse.rectTranslationSplitWord.size.height);
            //NSIndexSet *indexSet = [NSIndexSet indexSetWithIndex:indexPath.section];
            //                //[self.tableView reloadSections:indexSet withRowAnimation:UITableViewRowAnimationAutomatic];
            //            }
            //
            //NSObject* _cellDefinition = [cellVerse objectForKey:cellIdentifier];
            UITableViewCell* cellDefinition = [self.tableView dequeueReusableCellWithIdentifier:[self getCellIdentifierFor:verse]];
            if (cellDefinition == nil) {
                DefinitionCells* cellDefinition;
                NSArray* nib = [[NSBundle mainBundle] loadNibNamed:@"DefinitionCells" owner:self options:nil];
                cellDefinition = (DefinitionCells *) [nib objectAtIndex:4];
                [cellDefinition setController:self];
                [cellDefinition setSelectedBox:VersesBox];
                //            UILabel* lblVerseId = [cellDefinition verseId];
                //            lblVerseId.text=verseId;
                //            [[lblVerseId layer] setBorderWidth:1.0];
                //            [[lblVerseId layer] setBorderColor:[[UIColor grayColor] CGColor]];
                //            [[lblVerseId layer] setCornerRadius:5.0];
                //            [[lblVerseId layer] setMasksToBounds:YES];
                //            lblVerseId.frame = CGRectMake(lblVerseId.frame.origin.x,0,lblVerseId.frame.size.width,lblVerseId.frame.size.height);
                
                NSString* mainWord = [wordToHighlight objectAtIndex:row];
                //NSLog(@"Verse %@,%@",verseId,verse.verse);
                NSString* translation = verse.translation;
                NSString* text = verse.verse;//[verse.verse stringByAppendingFormat:verseId];
                NSString* translationSplitWord = verse.translation_split_word;
                NSString* transliteration= verse.transliteration;
                
                //[Utils createImageOn:cellDefinition withimage:@"verseborder.png" atLocation:CGRectMake(0, 0, 320, borderHeight)];
                int yOffset=borderHeight;
                int y=0;//yOffset;
                if (verseView==ViewSideBySide) {
                    if (showArabic) {
                        CGRect rect = [self createArabicVerseFor: verse onCell:cellDefinition mainText:text atY:y HighlightWord:mainWord];
                        //NSLog(@"%@  (x=%f,y=%f,width=%f,height=%f)",@"Cell Draw Side By Side",rect.origin.x,rect.origin.y,rect.size.width,rect.size.height);
                    }
                    if (showTranslation) {
                        CGRect rect = [self createTranslationVerseFor:verse onCell: cellDefinition translationText:translation atY:y];
                        y= y+rect.size.height+yOffset;
                    }
                }
                else {
                    if (showArabic) {
                        CGRect rect = [self createArabicVerseFor: verse onCell:cellDefinition mainText:text atY:y HighlightWord:mainWord];
                        y= y+rect.size.height;
                        //NSLog(@"%@  (x=%f,y=%f,width=%f,height=%f)",@"Cell Draw Top Text:",rect.origin.x,rect.origin.y,rect.size.width,rect.size.height);
                    }
                    if (showTranslation) {
                        CGRect rect = [self createTranslationVerseFor:verse onCell: cellDefinition translationText:translation atY:y];
                        //NSLog(@"%@ (%i) (x=%f,y=%f,width=%f,height=%f)",@"CellForRow(Top) Translation:",row,rect.origin.x,rect.origin.y,rect.size.width,rect.size.height);
                        y= y+rect.size.height+yOffset;
                    }
                    if (showTransliteration) {
                        CGRect rect = [self createTransliterationVerseFor:verse onCell: cellDefinition transliterationText:transliteration atY:y];
                        y= y+rect.size.height+yOffset;
                    }
                    if (showTranslationSplitWord) {
                        CGRect rect = [self createTranslationSplitWordVerseFor:verse onCell:cellDefinition mainText:text splitWordText:translationSplitWord atY:y highlightedWord:mainWord];
                        //NSLog(@"Split-Word Rect:%f:%f:%f:%f",rect.origin.x,rect.origin.y,rect.size.width,rect.size.height);
                        //y= y+rect.size.height+yOffset;
                    }
                }
                //y=y+borderHeight;
                //lblVerseId.frame = CGRectMake(lblVerseId.frame.origin.x, y-borderHeight,lblVerseId.frame.size.width,lblVerseId.frame.size.height);
                //[cellVerse setValue:cellDefinition forKey:cellIdentifier];
                return cellDefinition;
            }
            else {
                //return (DefinitionCells*) _cellDefinition;
                return cellDefinition;
            }
        }
        else {
            NSLog(@" In Footer");
            NSString* cellIdentifier = [NSString stringWithFormat:@"%@(%i)",@"Footer",instance_id];
            NSObject* _cellDefinition = [cellVerse objectForKey:cellIdentifier];
            DefinitionCells* cellDefinition;
            if (_cellDefinition != nil) return (DefinitionCells*)_cellDefinition;
            NSArray* nib = [[NSBundle mainBundle] loadNibNamed:@"DefinitionCells" owner:self options:nil];
            cellDefinition = (DefinitionCells *) [nib objectAtIndex:4];
            [cellDefinition setController:self];
            [cellDefinition setSelectedBox:VersesBox];
            [self addFooterView:cellDefinition];
            [cellVerse setValue:cellDefinition forKey:cellIdentifier];
            return cellDefinition;
        }
}

- (int) wordsMaxHeight:(NSArray*) locWords {
    if (locWords==nil) {
        NSLog(@" Error - locWord is NIL ");
        return 0;
    }
    CGRect lastRect = [(NSValue*)[[locWords lastObject] objectAtIndex:2] CGRectValue];
    return ceil(lastRect.origin.y+lastRect.size.height);
    
}


- (CGRect) adjustRect:(CGRect) rect byPoint:(CGPoint) pos {
    rect.origin.x = rect.origin.x+pos.x;
    rect.origin.y = rect.origin.y+pos.y;
    return rect;
}


- (CGRect) getVerseRect:(NSArray*) locWords usingFont:(VerseFont*) verseFont{
    CGRect rect = CGRectMake(0, 0, FLT_MIN,FLT_MIN);
    for (NSArray* locWord in locWords) {
        CGRect rectWord = [(NSValue*)[locWord objectAtIndex:2] CGRectValue];
        rect.origin.x = MIN(rectWord.origin.x,rect.origin.x);
        rect.origin.y = MIN(rectWord.origin.y,rect.origin.y);
        rect.size.width  = MAX(rectWord.origin.x+rectWord.size.width,rect.size.width);
        rect.size.height = MAX(rectWord.origin.y+rectWord.size.height,rect.size.height);
    }
    return rect;
}




- (CGRect) createArabicVerseFor:(Verse*) verse onCell:(DefinitionCells*) cellDefinition mainText:(NSString*) text atY:(int) y HighlightWord:(NSString*) highlightedWord {
    int x=0;
    //    CGSize textSize;
    //    if (verseView==ViewSideBySide) {
    //        x=maximumLabelSize2.width;
    //        textSize = [Utils getTextSize:textFont.heightCushion forText:[text stringByAppendingString:verseMarker] fontToSize:textFont.font rectSize:maximumLabelSize2];
    //        if (textSize.width < maximumLabelSize2.width) x=maximumLabelSize2.width+(maximumLabelSize2.width-textSize.width);
    //    }
    //    else {
    //        textSize = [Utils getTextSize:textFont.heightCushion forText:[text stringByAppendingString:verseMarker] fontToSize:textFont.font rectSize:maximumLabelSize];
    //        if (textSize.width < maximumLabelSize.width) x=maximumLabelSize.width-textSize.width;
    //    }
    
    //CGRect rect = CGRectMake(x,y,textSize.width,[self wordsMaxHeight:verse.verseDrawArabic]);
    //CGRect rect = [(NSValue*)[[verse.verseDrawArabic lastObject] objectAtIndex:2] CGRectValue];
    //CGRect rect = [self getVerseRect:verse.verseDrawArabic];
    //return ceil(lastRect.origin.y+lastRect.size.height);
    CGRect verseLabelRect = CGRectMake(verse.rectText.origin.x, verse.rectText.origin.y, verse.rectText.size.width+widthCushion/2,verse.rectText.size.height);
    VerseDrawLabel *verseLabel = [[VerseDrawLabel alloc] initWithWord:verseLabelRect SelectedWord:highlightedWord];
    verseLabel.locWords=verse.verseDrawArabic;
    verseLabel.text =text;
    verseLabel.backgroundColor = [UIColor clearColor];
    verseLabel.font = textFont.font;
    verseLabel.fontToSize = textFont.font;
    verseLabel.textColor =[UIColor blackColor];
    verseLabel.textAlignment = NSTextAlignmentLeft;
    verseLabel.lineBreakMode = NSLineBreakByWordWrapping;
    [verseLabel setNumberOfLines:0];
    [verseLabel setTextDirection:RTL];
    [cellDefinition addSubview:verseLabel];
    return verseLabelRect;
    
}


- (CGRect) createTranslationVerseFor:(Verse*) verse onCell:(DefinitionCells*) cellDefinition translationText:(NSString*) text atY:(int) y {
    // int x=0;
    // CGSize textSize;
    //NSAttributedString *attributedText = [[NSAttributedString alloc] initWithString:[text stringByAppendingString:verseMarker] attributes:@{NSFontAttributeName:translationFont.font}];
    
    //    if (verseView==ViewSideBySide)  textSize= [attributedText boundingRectWithSize:maximumLabelSize2 options:NSStringDrawingTruncatesLastVisibleLine|NSStringDrawingUsesLineFragmentOrigin context:nil].size;
    //    else  textSize= [attributedText boundingRectWithSize:maximumLabelSize options:NSStringDrawingTruncatesLastVisibleLine|NSStringDrawingUsesLineFragmentOrigin context:nil].size;
    //
    //    if (verseView==ViewSideBySide)
    //        textSize=[Utils getTextSize:translationFont.heightCushion forText:text fontToSize:translationFont.font rectSize:maximumLabelSize2];
    //    else textSize=[Utils getTextSize:translationFont.heightCushion forText:text fontToSize:translationFont.font rectSize:maximumLabelSize];
    //    CGRect rect = CGRectMake(x,y,textSize.width,[self wordsMaxHeight:verse.verseDrawTranslation]);
    CGRect verseLabelRect = CGRectMake(verse.rectTranslation.origin.x+widthCushion/2, verse.rectTranslation.origin.y, verse.rectTranslation.size.width,verse.rectTranslation.size.height);
    VerseDrawLabel *verseLabel = [[VerseDrawLabel alloc] initWithWord:verseLabelRect SelectedWord:selectedWordInTranslation];
    verseLabel.translation=text;
    verseLabel.text=text;
    verseLabel.locWords=verse.verseDrawTranslation;
    verseLabel.backgroundColor = [UIColor clearColor];
    verseLabel.translationFont = translationFont.font;
    verseLabel.font = translationFont.font;
    verseLabel.fontToSize = translationFont.font;
    verseLabel.textColor =[UIColor blackColor];
    verseLabel.textAlignment = NSTextAlignmentLeft;
    verseLabel.lineBreakMode = NSLineBreakByWordWrapping;
    [verseLabel setNumberOfLines:0];
    [verseLabel setTextDirection:LTR];
    [cellDefinition addSubview:verseLabel];
    return verseLabelRect;
    
}

- (NSString*) removeTransliterationMarks:(NSString*) transliteration {
    NSString* _transliteration=transliteration;
    _transliteration = [_transliteration stringByReplacingOccurrencesOfString:@"<B>" withString:@"" options:NSCaseInsensitiveSearch range:NSMakeRange(0, [_transliteration length])];
    _transliteration= [_transliteration stringByReplacingOccurrencesOfString:@"</B>" withString:@"" options:NSCaseInsensitiveSearch range:NSMakeRange(0, [_transliteration length])];
    _transliteration= [_transliteration stringByReplacingOccurrencesOfString:@"<U>" withString:@"" options:NSCaseInsensitiveSearch range:NSMakeRange(0, [_transliteration length])];
    _transliteration= [_transliteration stringByReplacingOccurrencesOfString:@"</U>" withString:@"" options:NSCaseInsensitiveSearch range:NSMakeRange(0, [_transliteration length])];
    return _transliteration;
}


- (CGSize) getTransliterationSize:(NSString*) transliteration {
    NSString* _transliteration= [self removeTransliterationMarks:transliteration];
    //CGSize textSize =[_transliteration sizeWithFont:transliterationFont constrainedToSize:maximumLabelSize lineBreakMode:NSLineBreakByWordWrapping];
    CGSize textSize= [Utils getTextSize:1.0 forText:_transliteration fontToSize:transliterationFont.font rectSize:maximumLabelSize];
    
    
    return textSize;
}


- (CGRect) createTransliterationVerseFor:(Verse*) verse onCell:(DefinitionCells*) cellDefinition transliterationText:(NSString*) text atY:(int) y {
    text =[self removeTransliterationMarks:text];
    
    CGRect verseLabelRect = CGRectMake(verse.rectTransliteration.origin.x, verse.rectTransliteration.origin.y, verse.rectTransliteration.size.width,verse.rectTransliteration.size.height);
    VerseDrawLabel *verseLabel = [[VerseDrawLabel alloc] initWithWord:verseLabelRect SelectedWord:@""];
    verseLabel.transliteration=text;
    verseLabel.text=text;
    verseLabel.locWords=verse.verseDrawTransliteration;
    verseLabel.backgroundColor = [UIColor clearColor];
    verseLabel.transliterationFont = transliterationFont.font;
    verseLabel.font = transliterationFont.font;
    verseLabel.fontToSize = transliterationFont.font;
    verseLabel.textColor =[UIColor blackColor];
    verseLabel.textAlignment = NSTextAlignmentLeft;
    verseLabel.lineBreakMode = NSLineBreakByWordWrapping;
    [verseLabel setNumberOfLines:0];
    [verseLabel setTextDirection:LTR];
    [cellDefinition addSubview:verseLabel];
    return verseLabelRect;
    
}


- (CGSize) getSplitWordSize:(NSString*) translationSplitWord Font:(UIFont*) font {
    NSString* _splitWord = [translationSplitWord stringByReplacingOccurrencesOfString:@" " withString:@"*"];
    _splitWord = [_splitWord stringByReplacingOccurrencesOfString:SPLIT_WORD_DELIMITER withString:@" "];
    //CGSize textSize = [_splitWord sizeWithFont:fontToSize.font constrainedToSize:maximumLabelSize lineBreakMode:NSLineBreakByWordWrapping];
    CGSize textSize = [Utils getTextSize:1.0 forText:_splitWord fontToSize:font rectSize:maximumLabelSize];
    CGSize lineSize = [Utils getTextSize:1.0 forText:@"Merciful" fontToSize:font rectSize:maximumLabelSize];
    textSize.height = lineSize.height*3*(textSize.height/lineSize.height);
    return textSize;
}

- (CGRect) createTranslationSplitWordVerseFor:(Verse*) verse onCell:(DefinitionCells*) cellDefinition mainText:(NSString*) text splitWordText:(NSString*) translationSplitWord atY:(int) y highlightedWord:(NSString*) highlightedWord {
    CGRect verseLabelRect = CGRectMake(verse.rectTranslationSplitWord.origin.x, verse.rectTranslationSplitWord.origin.y, verse.rectTranslationSplitWord.size.width,verse.rectTranslationSplitWord.size.height);
    VerseDrawLabel *verseLabel = [[VerseDrawLabel alloc] initWithWord:verseLabelRect SelectedWord:highlightedWord];
    verseLabel.text =text;
    verseLabel.translation_split_word=translationSplitWord;
    verseLabel.grammarWords=verse.verseMorphology;
    verseLabel.backgroundColor = [UIColor clearColor];
    verseLabel.font = textFont.font;
    verseLabel.fontToSize=fontToSize.font;
    verseLabel.locWords=verse.verseDrawTranslationSplitWord;
    verseLabel.translationSplitWordFont = translationSplitWordFont.font;
    verseLabel.textColor =[UIColor blackColor];
    verseLabel.textAlignment = NSTextAlignmentLeft;
    verseLabel.lineBreakMode = NSLineBreakByWordWrapping;
    [verseLabel setNumberOfLines:0];
    [verseLabel setTextDirection:RTL];
    [cellDefinition addSubview:verseLabel];
    return verseLabelRect;
    
}

- (int) getVerseSizeFor:(Verse*) verse {
    NSArray *sizes = [verse.size componentsSeparatedByString:@","];
    for (int i; i<[sizes count]; i++) {
        NSArray *sizeAttributes = [(NSString*)sizes[i] componentsSeparatedByString:@"="];
        if ([(NSString*)sizeAttributes[0] isEqualToString:displayID]) return (int)sizeAttributes[1];
    }
    return 0;
    
}


- (void) addFooterView: (UITableViewCell*) footerView {
    //NSLog(@"Button Creation--------");
    leftBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    CALayer *layer = leftBtn.layer;
    //layer.backgroundColor = [[UIColor lightGrayColor] CGColor];
    layer.borderColor = [[UIColor grayColor] CGColor];
    layer.cornerRadius = 8.0f;
    layer.borderWidth = 3.0f;
    [leftBtn setFrame:CGRectMake(10, 5, 40, 40)];
    UIImage *image = [UIImage imageNamed:@"left.png"];
    [leftBtn setImage:image forState:UIControlStateNormal];
    leftBtn.tag=0;
    leftBtn.hidden=YES;
    [leftBtn addTarget:self action:@selector(btnTouchDown:) forControlEvents:UIControlEventTouchUpInside];
    // Create right button
    rightBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    layer = rightBtn.layer;
    //    layer.backgroundColor = [[UIColor clearColor] CGColor];
    layer.borderColor = [[UIColor grayColor] CGColor];
    layer.cornerRadius = 8.0f;
    layer.borderWidth = 3.0f;
    [rightBtn setFrame:CGRectMake(235, 5, 40, 40)];
    image = [UIImage imageNamed:@"right.png"];
    [rightBtn setImage:image forState:UIControlStateNormal];
    [rightBtn addTarget:self action:@selector(btnTouchDown:) forControlEvents:UIControlEventTouchUpInside];
    rightBtn.tag=1;
    [footerView addSubview:leftBtn];
    [footerView addSubview:rightBtn];
    footerView.userInteractionEnabled = YES;
}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    NSDate *methodStart = [NSDate date];
    NSInteger row = indexPath.section;
    int textHeight=0,translationHeight=0,textSplitHeight=0,transliterationHeight=0;
    int ht=0;
    Verse* verse = [self getVerseAt:row RefreshVerseLocs:NO];
    //    if (![[rowHeights objectAtIndex:row] isEqual:[NSNull null]]) {
    //        //NSLog(@" Row Heights:%zd",row);
    //        return [(NSNumber*) rowHeights[row] intValue];
    //    }
    if (verseView==ViewSideBySide) {
        if (showArabic) textHeight = [self getVerseDrawHeight:verse ofSize:maximumLabelSize2 textMain:verse.verse splitWordText:nil inTextDirection:RTL fontToSize:textFont];
        if (showTranslation) translationHeight = [self getVerseDrawHeight:verse ofSize:maximumLabelSize2 textMain:verse.translation splitWordText:nil inTextDirection:LTR fontToSize:translationFont];
        ht = MAX(textHeight,translationHeight);
    }
    else {
        if (showTranslationSplitWord) textSplitHeight = [self getVerseDrawHeight:verse ofSize:maximumLabelSize textMain:verse.verse splitWordText:verse.translation_split_word inTextDirection:RTL fontToSize:fontToSize];
        if (showArabic) textHeight = [self getVerseDrawHeight:verse ofSize:maximumLabelSize textMain:verse.verse splitWordText:nil inTextDirection:RTL fontToSize:textFont];
        if (showTranslation) translationHeight = [self getVerseDrawHeight:verse ofSize:maximumLabelSize textMain:verse.translation splitWordText:nil inTextDirection:LTR fontToSize:translationFont];
        if (showTransliteration)transliterationHeight = [self getVerseDrawHeight:verse ofSize:maximumLabelSize textMain:[self removeTransliterationMarks:verse.transliteration] splitWordText:nil inTextDirection:LTR fontToSize:translationFont];
        ht=(showArabic?textHeight:0)+(showTranslation?translationHeight:0)+(showTransliteration?transliterationHeight:0)+(showTranslationSplitWord?textSplitHeight:0);
    }
    verse.rowHeight=ht;
    //    [rowHeights setObject:[NSNumber numberWithInt:ht] atIndexedSubscript:row];
    //    NSDate *methodFinish = [NSDate date];
    //    NSTimeInterval executionTime = [methodFinish timeIntervalSinceDate:methodStart];
    //    execTimeRowHeight =execTimeRowHeight+executionTime;
    //    //NSLog(@"[VerseViewController] Row Height %tu=%i",row,ht);
    //    if (row==rowDisplayWindow.length-2)   {
    //        //NSLog(@"[VerseViewController] Row Height Loaded in %f",execTimeRowHeight);
    //        execTimeRowHeight=0.0;
    //    }
    //NSLog(@"      **** Verse Height:%@ (%i)",verse.verse,ht);
    return ht;
}


//
//- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section {
//    return 0;
//        //if (pageCount==1) return 0;
//        //return 44;
//}
//
//
//- (UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section{
//    return nil;
//        //if (pageCount==1) return nil;
//        //if (footerView==Nil) [self buildFooterView];
//        //return footerView;
//}



- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    scrollIndexPath = indexPath;
    
    // Navigation logic may go here. Create and push another view controller.
    /*
     <#DetailViewController#> *detailViewController = [[<#DetailViewController#> alloc] initWithNibName:@"<#Nib name#>" bundle:nil];
     // ...
     // Pass the selected object to the new view controller.
     [self.navigationController pushViewController:detailViewController animated:YES];
     */
}


- (NSMutableArray*) getVerseDraw2: (CGRect)rect textMain: (NSString*) text splitWordText:(NSString*) translation_split_word inTextDirection : (enum TEXT_DIRECTION) textDirection fontToSize:(VerseFont*) verseFont withHeightCushion:(float) heightAdjustment{
    // NSLog(@"**************************Rect:%@  (x=%f,y=%f,width=%f,height=%f) [%@]",@"Split Words verse draw",rect.origin.x,rect.origin.y,rect.size.width,rect.size.height,translation_split_word);
    UIFont* fontToSizeWith = verseFont.font;
    
    NSMutableArray* locWords = [[NSMutableArray alloc] init];
    //UIFont* font = textFont.font;
    CGSize currentLineSize = CGSizeMake(0,0),tokenSize,headerTokenSize;
    NSInteger index = 0;
    NSArray *tokens, *headers;
    bool hasSplitWords = translation_split_word != nil;
    if (hasSplitWords) {
        //NSLog(@"Split Words:%@",translation_split_word);
        headers = [text componentsSeparatedByString:@" "];
        tokens = [translation_split_word componentsSeparatedByString:SPLIT_WORD_DELIMITER];
        //NSLog(@"Split Words:%i",[tokens count]);
        
    }
    else {
        tokens = [text componentsSeparatedByString:@" "];
        
    }
    if ([tokens count]==0) return;
    int line=0;
    int y=0,x=0;
    //currentLineSize  = [[tokens objectAtIndex:0]  sizeWithFont:fontToSize.fontWith constrainedToSize: CGSizeMake(rect.size.width, rect.size.height) lineBreakMode: NSLineBreakByWordWrapping];
    currentLineSize = [Utils getTextSize:heightAdjustment forText:[tokens objectAtIndex:0] fontToSize:fontToSizeWith rectSize:rect.size];
    
    int lineHeightText=currentLineSize.height;
    CGSize maxLineSize = CGSizeMake(rect.size.width,lineHeightText);
    float lineHeight = (hasSplitWords)?(lineHeightText * lineHeightRatio):lineHeightText;
    NSMutableString* token;
    NSMutableString* lblText = [[NSMutableString alloc] initWithString:@""];
    CGPoint point;
    NSValue *pointValue;
    NSString* spacer=textDirection==LTR?@" ":@" ";
    NSMutableString* headerToken;
    //hasSplitWords=NO;
    float fixedHeightForSplitWords=0;
    do {
        //index--;
        int tokensOnLine=0;
        int xlast=0;
        if (textDirection==LTR) x=0; else x=maxLineSize.width;
        do {
            token = [NSMutableString stringWithString:(NSString*)  [tokens objectAtIndex:index]];
            tokenSize  = [Utils getTextSize:heightAdjustment forText:token  fontToSize:fontToSizeWith rectSize:CGSizeMake(maxLineSize.width, rect.size.height)];
            
            //            if (hasSplitWords) {
            //                headerToken = [NSMutableString stringWithString:(NSString*)  [headers objectAtIndex:index]];
            //                headerTokenSize  = [Utils getTextSize:heightCushion forText:headerToken  fontToSize:textFont.font rectSize:CGSizeMake(maxLineSize.width, rect.size.height)];
            //                //NSLog(@"Rect:%@  (x=%f,y=%f,width=%f,height=%f) [%@]",@"Split Words verse draw",rect.origin.x,rect.origin.y,rect.size.width,rect.size.height,translation_split_word);
            //
            //                if (headerTokenSize.width>tokenSize.width) {
            //                   token=headerToken;
            //                   tokenSize.width=headerTokenSize.width;
            //                    NSLog(@"HeaderToken:%@  (HeaderWidth=%f,TokenWidth=%f)",headerToken,headerTokenSize.width,tokenSize.width);
            //
            //                }
            //            }
            if ([lblText isEqualToString:@""]) {
                [lblText setString:token];
            }
            else {
                [lblText appendFormat:@"%@%@",spacer,token];
            }
            //currentLineSize  = [lblText  sizeWithFont:fontToSize.fontWith constrainedToSize: CGSizeMake(maxLineSize.width, rect.size.height) lineBreakMode:NSLineBreakByWordWrapping];
            currentLineSize  = [Utils getTextSize:heightAdjustment forText:lblText  fontToSize:fontToSizeWith rectSize:CGSizeMake(maxLineSize.width, rect.size.height)];
            NSLog(@"VerseDraw0:%@ (maxWidth=%f,currentLineSizeHeight=%f) (x=%f,y=%f,width=%f,height=%f) [%f,%f]",lblText,maxLineSize.width,currentLineSize.height,rect.origin.x,rect.origin.y,rect.size.width,rect.size.height,currentLineSize.width,currentLineSize.width);
            
            //tokenSize = [token  sizeWithFont:fontToSize.fontWith constrainedToSize: CGSizeMake(maxLineSize.width, rect.size.height) lineBreakMode: NSLineBreakByWordWrapping];
            
            if (textDirection==RTL) x=maxLineSize.width-currentLineSize.width;
            if (textDirection==LTR) x=currentLineSize.width-tokenSize.width;
            if (tokensOnLine==0) {
                fixedHeightForSplitWords=tokenSize.height;
            }
            if (tokensOnLine==0 && currentLineSize.height > maxLineSize.height) {
                currentLineSize.height = maxLineSize.height;
                tokenSize.height=maxLineSize.height;
                fixedHeightForSplitWords=tokenSize.height;
            }
            if (currentLineSize.height <= maxLineSize.height) {
                if (x+tokenSize.width < xlast) tokenSize.width = tokenSize.width + (xlast - (x + tokenSize.width));
                //NSLog(@"[%@]  index:%i x:%i old tokenSize.width:%f tokenSize.width: xlast:%f",token,index,x,tokenSize.width);
                NSValue *rectValue;
                if (hasSplitWords) rectValue =  [NSValue valueWithCGRect:CGRectMake(MAX(x,0),y, tokenSize.width, lineHeight)];
                else rectValue =  [NSValue valueWithCGRect:CGRectMake(x,y, tokenSize.width, tokenSize.height)];
                
                point = CGPointMake(x,y);
                NSValue *pointValue = [NSValue valueWithCGPoint:point];
                NSValue *firstTokenValue = [NSNumber numberWithBool:(xlast==0)];
                NSValue *lineValue = [NSNumber numberWithInt:line];
                NSMutableArray *locWord = [NSMutableArray arrayWithObjects:token,pointValue,rectValue,firstTokenValue,lineValue,nil];
                [locWords addObject:locWord];
                if (true) {
                    CGRect rect = [(NSValue*)[locWord objectAtIndex:2] CGRectValue];
                    [Utils printRect:[@"  ...VerseDraw 1" stringByAppendingString:token] ForRect:rect];
                    //NSLog(@"        ...VerseDraw1:[%@]  (x=%f,y=%f,width=%f,height=%f)",token,rect.origin.x,rect.origin.y,rect.size.width,rect.size.height);
                }
                
                
            }
            
            xlast = x;
            tokensOnLine++;
            index++;
            
        } while(currentLineSize.height <= maxLineSize.height && index < [tokens count]);
        //int lengthLasttoken= [token length]; NSLog(@" Token:%@",token);
        if (index < [tokens count]) {
            [lblText setString:@""];
            index --;
            if (hasSplitWords) y=y+lineHeight; else y=y+lineHeight;
            line++;
        }
        else if (currentLineSize.height > maxLineSize.height){
            line++;
            y=y+lineHeight;
            [lblText setString:token];
            CGSize headerSize,tokenSize;
            //currentLineSize  = [lblText  sizeWithFont:fontToSize.fontWith constrainedToSize: CGSizeMake(maxLineSize.width, rect.size.height) lineBreakMode:NSLineBreakByWordWrapping];
            currentLineSize = [Utils getTextSize:heightAdjustment forText:lblText fontToSize:fontToSizeWith rectSize:CGSizeMake(maxLineSize.width, rect.size.height)];
            
            //tokenSize = [token  sizeWithFont:fontToSize.fontWith constrainedToSize: CGSizeMake(maxLineSize.width, rect.size.height) lineBreakMode: NSLineBreakByWordWrapping];
            tokenSize=[Utils getTextSize:heightAdjustment forText:token fontToSize:fontToSizeWith rectSize:CGSizeMake(maxLineSize.width, rect.size.height)];
            if (textDirection==LTR) {
                x=0;
                point = CGPointMake(0,y);
            }
            else {
                x= maxLineSize.width-currentLineSize.width;
                point = CGPointMake(x,y);
            }
            NSValue *rectValue ;
            if (hasSplitWords) rectValue =  [NSValue valueWithCGRect:CGRectMake(x,y, tokenSize.width, lineHeight)];
            else rectValue =  [NSValue valueWithCGRect:CGRectMake(MAX(x,0),y, tokenSize.width, tokenSize.height)];
            NSValue *pointValue = [NSValue valueWithCGPoint:point];
            NSValue *firstTokenValue = [NSNumber numberWithBool:true];
            NSValue *lineValue = [NSNumber numberWithInt:line];
            NSMutableArray *locWord = [NSMutableArray arrayWithObjects:token,pointValue,rectValue,firstTokenValue,lineValue,nil];
            [locWords addObject:locWord];
            if (true) {
                CGRect rect = [(NSValue*)[locWord objectAtIndex:2] CGRectValue];
                NSLog(@"        ...VerseDraw 2:[%@]  (x=%f,y=%f,width=%f,height=%f)",token,rect.origin.x,rect.origin.y,rect.size.width,rect.size.height);
                
            }
            
        }
        
    } while (index < [tokens count]);
    //if (textDirection==RTL) NSLog(@"       ****    Frame:(%f,%f) Rect:(%f,%f)",self.frame.size.width,self.frame.size.height,rect.size.width,rect.size.height);
    
    return locWords;
    
}

- (NSMutableArray*) getVerseDraw: (CGRect)rect textMain: (NSString*) text splitWordText:(NSString*) translation_split_word inTextDirection : (enum TEXT_DIRECTION) textDirection fontToSize:(VerseFont*) verseFont withHeightCushion:(float) heightAdjustment{
    // NSLog(@"**************************Rect:%@  (x=%f,y=%f,width=%f,height=%f) [%@]",@"Split Words verse draw",rect.origin.x,rect.origin.y,rect.size.width,rect.size.height,translation_split_word);
    UIFont* fontToSizeWith = verseFont.font;
    
    NSMutableArray* locWords = [[NSMutableArray alloc] init];
    //UIFont* font = textFont.font;
    NSArray *tokens, *headers;
    bool hasSplitWords = translation_split_word != nil;
    if (hasSplitWords) {
        headers = [text componentsSeparatedByString:@" "];
        tokens = [translation_split_word componentsSeparatedByString:SPLIT_WORD_DELIMITER];
    }
    else {
        tokens = [text componentsSeparatedByString:@" "];
    }
    if ([tokens count]==0) return;
    NSString* token;
    CGPoint point;
    NSValue *pointValue;
    NSMutableString* headerToken;
    float fixedHeightForSplitWords=0;
    
    int currentLineSize;
    CGSize tokenSize;
    int line=0;
    int y=0,x=0;
    int lineHeightText = (int)[Utils getTextSize:heightAdjustment forText:[tokens objectAtIndex:0] fontToSize:fontToSizeWith rectSize:rect.size].height;
    
    //int lineHeightText = verseFont.charSize.height;
    CGSize maxLineSize = CGSizeMake(rect.size.width,lineHeightText);
    float lineHeight = (hasSplitWords)?(lineHeightText * lineHeightRatio):lineHeightText;
    int spacer=(int)[Utils getTextSize:heightAdjustment forText:@" " fontToSize:fontToSizeWith rectSize:rect.size].width;
    NSMutableArray* tokenSizes = [[NSMutableArray alloc] initWithCapacity:0];
    int i=0;
    CGSize sizeText=CGSizeMake(0.0,0.0),sizeSplitWord=CGSizeMake(0.0,0.0);
    for (NSString* token in tokens) {
        if (hasSplitWords) {
            sizeText = [Utils getTextSize:heightAdjustment forText:token  fontToSize:translationSplitWordFont.font rectSize:CGSizeMake(maxLineSize.width, rect.size.height)];
            sizeSplitWord = [Utils getTextSize:heightAdjustment forText:headers[i] fontToSize:textFont.font rectSize:CGSizeMake(maxLineSize.width, rect.size.height)];
            [tokenSizes addObject:[NSValue valueWithCGSize:CGSizeMake(MAX(sizeText.width,sizeSplitWord.width),sizeText.height)]];
            
        }
        else {
            sizeText = [Utils getTextSize:heightAdjustment forText:token  fontToSize:fontToSizeWith rectSize:CGSizeMake(maxLineSize.width, rect.size.height)];
            [tokenSizes addObject:[NSValue valueWithCGSize:sizeText]];
            
            
        }
        i++;
    }
    int index=0;
    int indexToken=0;
    y=0;
    do {
        int tokensOnLine=0;
        int xlast=0;
        currentLineSize=0;
        if (textDirection==LTR) x=0; else x=maxLineSize.width;
        
        do {
            token = [tokens objectAtIndex:index];
            tokenSize = [[tokenSizes objectAtIndex:index] CGSizeValue];
            currentLineSize=currentLineSize==0?tokenSize.width:currentLineSize+tokenSize.width+spacer;
            if (textDirection==RTL) x=maxLineSize.width-currentLineSize;
            if (textDirection==LTR) x=currentLineSize-tokenSize.width;
            if (tokensOnLine==0 && currentLineSize > maxLineSize.width) {
                currentLineSize = maxLineSize.width;
            }
            // Add objects
            if (currentLineSize <= maxLineSize.width) {
                //x= MAX(0,x);
                if (x+tokenSize.width < xlast) tokenSize.width = tokenSize.width + (xlast - (x + tokenSize.width));
                NSValue *rectValue;
                if (hasSplitWords) rectValue =  [NSValue valueWithCGRect:CGRectMake(MAX(x,0),y, tokenSize.width, lineHeight)];
                else rectValue =  [NSValue valueWithCGRect:CGRectMake(MAX(x,0),y, tokenSize.width, tokenSize.height)];
                point = CGPointMake(x,y);
                NSValue *pointValue = [NSValue valueWithCGPoint:point];
                NSValue *firstTokenValue = [NSNumber numberWithBool:(xlast==0)];
                NSValue *lineValue = [NSNumber numberWithInt:line];
                NSArray *locWord = [NSArray arrayWithObjects:token,pointValue,rectValue,firstTokenValue,lineValue,nil];
                CGRect rect = [(NSValue*)[locWord objectAtIndex:2] CGRectValue];
                [locWords addObject:locWord];
                //NSLog(@"        ...VerseDraw1:[%@]  (x=%f,y=%f,width=%f,height=%f)",token,rect.origin.x,rect.origin.y,rect.size.width,rect.size.height);
                
            }
            tokensOnLine++;
            index++;
            xlast = x;
            
        } while(currentLineSize <= maxLineSize.width && index < [tokens count]);
        if (index < [tokens count]) {
            index --;
            indexToken=index;
            y=y+lineHeight;
            line++;
        }
        else if (currentLineSize > maxLineSize.width){
            y=y+lineHeight;
            tokenSize=[[tokenSizes objectAtIndex:index-1] CGSizeValue];
            currentLineSize=tokenSize.width;
            if (textDirection==LTR) {
                x=0;
                point = CGPointMake(0,y);
            }
            else {
                x= maxLineSize.width-currentLineSize;
                point = CGPointMake(x,y);
            }
            NSValue *rectValue ;
            if (hasSplitWords) rectValue =  [NSValue valueWithCGRect:CGRectMake(x,y, tokenSize.width, lineHeight)];
            else rectValue =  [NSValue valueWithCGRect:CGRectMake(MAX(x,0),y, tokenSize.width, tokenSize.height)];
            NSValue *pointValue = [NSValue valueWithCGPoint:point];
            NSValue *firstTokenValue = [NSNumber numberWithBool:true];
            NSValue *lineValue = [NSNumber numberWithInt:line];
            NSArray *locWord = [NSArray arrayWithObjects:token,pointValue,rectValue,firstTokenValue,lineValue,nil];
            [locWords addObject:locWord];
            CGRect rect = [(NSValue*)[locWord objectAtIndex:2] CGRectValue];
            //NSLog(@"        ...VerseDraw2:[%@]  (x=%f,y=%f,width=%f,height=%f)",token,rect.origin.x,rect.origin.y,rect.size.width,rect.size.height);
            
        }
        
    } while (index < [tokens count]);
    return locWords;
    
}

- (float) getVerseDrawHeight: (Verse*) verse ofSize:(CGSize)rSize textMain: (NSString*) text splitWordText:(NSString*) translationSplitWord inTextDirection : (enum TEXT_DIRECTION) textDirection fontToSize:(VerseFont*) verseFont {
    UIFont* fontToSizeWith = verseFont.font;
    int heightAdjustment = fontToSize.heightCushion;
    int currentLineSize,tokenSize;
    NSArray *tokens,*headers;
    bool hasSplitWords = translationSplitWord != nil;
    if (hasSplitWords) {
        headers = [text componentsSeparatedByString:@" "];
        tokens = [translationSplitWord componentsSeparatedByString:SPLIT_WORD_DELIMITER];
    }
    else {
        tokens = [text componentsSeparatedByString:@" "];
    }
    //    for (NSString* token in tokens) {
    //       // token = [token stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
    //        NSLog(@" TOKEN ---- [%@]",token);
    //    }
    if ([tokens count]==0) return;
    int line=0;
    int y=0,x=0;
    //int lineHeightText = verseFont.charSize.height;
    int lineHeightText = (int)[Utils getTextSize:heightAdjustment forText:[tokens objectAtIndex:0] fontToSize:fontToSizeWith rectSize:rSize].height;
    NSArray* locWords = nil;//verse.verseDrawArabic;
    
    CGSize maxLineSize = CGSizeMake(rSize.width,lineHeightText);
    float lineHeight = (hasSplitWords)?(lineHeightText * lineHeightRatio):lineHeightText;
    int spacer=(int)[Utils getTextSize:heightAdjustment forText:@" " fontToSize:fontToSizeWith rectSize:rSize].width;
    NSMutableArray* tokenSizes = [[NSMutableArray alloc] initWithCapacity:0];
    int i=0,width=0,widthTrans;
    for (NSString* token in tokens) {
        if (hasSplitWords) {
            width = ([Utils getTextSize:heightAdjustment forText:token fontToSize:translationSplitWordFont.font rectSize:CGSizeMake(maxLineSize.width, rSize.height)].width);
            widthTrans = ([Utils getTextSize:heightAdjustment forText:headers[i] fontToSize:textFont.font rectSize:CGSizeMake(maxLineSize.width, rSize.height)].width);
            [tokenSizes addObject:[NSNumber numberWithInt:MAX(width,widthTrans)]];
            
        }
        else {
            width = (int)([Utils getTextSize:heightAdjustment forText:token  fontToSize:fontToSizeWith rectSize:CGSizeMake(maxLineSize.width, rSize.height)].width);
            [tokenSizes addObject:[NSNumber numberWithInt:width]];
            
        }
        i++;
    }
    int index=0;
    int indexToken=0;
    y=lineHeight;
    do {
        int tokensOnLine=0;
        currentLineSize=0;
        do {
            tokenSize = [[tokenSizes objectAtIndex:index] intValue];
            //            int width =0;
            //            for (int i=indexToken; i<index; i++) {
            //                width = width+ [[tokenSizes objectAtIndex:i] intValue];
            //            }
            currentLineSize=(currentLineSize==0)?tokenSize:currentLineSize+tokenSize+spacer;
            if (tokensOnLine==0 && currentLineSize > maxLineSize.width) {
                currentLineSize = maxLineSize.width;
            }
            tokensOnLine++;
            index++;
        } while(currentLineSize <= maxLineSize.width && index < [tokens count]);
        if (index < [tokens count]) {
            index --;
            indexToken=index;
            y=y+lineHeight;
            line++;
        }
        else if (currentLineSize > maxLineSize.width){
            y=y+lineHeight;
        }
        
    } while (index < [tokens count]);
    return y;
}

- (float) getVerseDrawHeight2: (CGSize)rSize textMain: (NSString*) text splitWordText:(NSString*) translation_split_word inTextDirection : (enum TEXT_DIRECTION) textDirection fontToSize:(VerseFont*) verseFont {
    UIFont* fontToSizeWith = verseFont.font;
    int heightAdjustment = fontToSize.heightCushion;
    NSDate *methodStart = [NSDate date];
    
    
    CGSize currentLineSize = CGSizeMake(0,0),tokenSize,headerTokenSize;
    int index = 0;
    NSArray *tokens, *headers;
    bool hasSplitWords = translation_split_word != nil;
    if (hasSplitWords) {
        headers = [text componentsSeparatedByString:@" "];
        tokens = [translation_split_word componentsSeparatedByString:SPLIT_WORD_DELIMITER];
    }
    else {
        tokens = [text componentsSeparatedByString:@" "];
    }
    if ([tokens count]==0) return;
    int line=0;
    int y=0,x=0;
    currentLineSize = [Utils getTextSize:heightAdjustment forText:[tokens objectAtIndex:0] fontToSize:fontToSizeWith rectSize:rSize];
    int lineHeightText=currentLineSize.height;
    CGSize maxLineSize = CGSizeMake(rSize.width,lineHeightText);
    float lineHeight = (hasSplitWords)?(lineHeightText * lineHeightRatio):lineHeightText;
    NSString* token;
    NSMutableString* lblText = [[NSMutableString alloc] initWithString:@""];
    //CGPoint point;
    //NSValue *pointValue;
    NSString* spacer=@" ";
    //NSMutableString* headerToken;
    NSMutableArray* tokenSizes = [[NSMutableArray alloc] initWithCapacity:0];
    for (NSString* token in tokens) {
        CGSize size = [Utils getTextSize:heightAdjustment forText:token  fontToSize:fontToSizeWith rectSize:CGSizeMake(maxLineSize.width, rSize.height)];
        [tokenSizes addObject:[NSValue valueWithCGSize:size]];
    }
    index=0;
    do {
        int indexToken=0;
        int tokensOnLine=0;
        int xlast=0;
        if (textDirection==LTR) x=0; else x=maxLineSize.width;
        do {
            //token = [NSMutableString stringWithString:(NSString*)  [tokens objectAtIndex:index]];
            token = [tokens objectAtIndex:index];
            tokenSize = [[tokenSizes objectAtIndex:index] CGSizeValue];
            //tokenSize  = [Utils getTextSize:heightAdjustment forText:token  fontToSize:fontToSizeWith rectSize:CGSizeMake(maxLineSize.width, rSize.height)];
            
            if ([lblText isEqualToString:@""]) {
                [lblText setString:token];
            }
            else {
                [lblText appendFormat:@"%@%@",spacer,token];
            }
            //currentLineSize  = [Utils getTextSize:heightAdjustment forText:lblText  fontToSize:fontToSizeWith rectSize:CGSizeMake(maxLineSize.width, rSize.height)];
            
            if (textDirection==RTL) x=maxLineSize.width-currentLineSize.width;
            if (textDirection==LTR) x=currentLineSize.width-tokenSize.width;
            
            if (tokensOnLine==0 && currentLineSize.height > maxLineSize.height) {
                currentLineSize.height = maxLineSize.height;
                tokenSize.height=maxLineSize.height;
            }
            if (currentLineSize.height <= maxLineSize.height) {
                if (x+tokenSize.width < xlast) tokenSize.width = tokenSize.width + (xlast - (x + tokenSize.width));
            }
            xlast = x;
            tokensOnLine++;
            index++;
            indexToken=index;
            
        } while(currentLineSize.height <= maxLineSize.height && index < [tokens count]);
        if (index < [tokens count]) {
            [lblText setString:@""];
            index --;
            indexToken=index;
            if (hasSplitWords) y=y+lineHeight; else y=y+lineHeight;
            line++;
        }
        else if (currentLineSize.height > maxLineSize.height){
            //index--;
            y=y+lineHeight;
            [lblText setString:token];
            CGSize headerSize,tokenSize;
            currentLineSize = [Utils getTextSize:heightAdjustment forText:lblText fontToSize:fontToSizeWith rectSize:CGSizeMake(maxLineSize.width,rSize.height)];
            //tokenSize=[Utils getTextSize:heightAdjustment forText:token fontToSize:fontToSizeWith rectSize:CGSizeMake(maxLineSize.width,rSize.height)];
            //tokenSize = [[tokenSizes objectAtIndex:index] CGSizeValue];
            if (textDirection==LTR) {
                x=0;
            }
            else {
                x= maxLineSize.width-currentLineSize.width;
            }
            y=y+lineHeight;//tokenSize.height;
        }
        else {
            y=y+lineHeight;
        }
        
    } while (index < [tokens count]);
    NSDate *methodFinish = [NSDate date];
    NSTimeInterval executionTime = [methodFinish timeIntervalSinceDate:methodStart];
    execTimeDrawHeight=execTimeDrawHeight+executionTime;
    return y;
    
}


@end
