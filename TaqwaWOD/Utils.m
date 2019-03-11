//
//  Utils.m
//  Tabster
//
//  Created by Wadood Chaudhary on 5/11/12.
//  Copyright (c) 2012 Ahmadiyya Muslim Community - Taqwa Group. All rights reserved.
//

#import "Utils.h"
#import "DefinitionCells.h"
#import "VerseDrawLabel.h"
#import "Word.h"
#import <QuartzCore/QuartzCore.h>
#import "AppDelegate.h"
#import "Constants.h"
#import "DictionaryDataController.h"
#include <sys/types.h>
#include <sys/sysctl.h>
#include <CoreText/CoreText.h>


@implementation Utils

static NSDate* start;



+ (UIColor *)lighterColorForColor:(UIColor *)c {
    CGFloat r, g, b, a;
    if ([c getRed:&r green:&g blue:&b alpha:&a])
        return [UIColor colorWithRed:MIN(r + 0.2, 1.0)
                               green:MIN(g + 0.2, 1.0)
                                blue:MIN(b + 0.2, 1.0)
                               alpha:a];
    return nil;
}

+ (UIColor *)darkerColorForColor:(UIColor *)c {
    CGFloat r, g, b, a;
    if ([c getRed:&r green:&g blue:&b alpha:&a])
        return [UIColor colorWithRed:MAX(r - 0.2, 0.0)
                               green:MAX(g - 0.2, 0.0)
                                blue:MAX(b - 0.2, 0.0)
                               alpha:a];
    return nil;
}

+ (NSMutableArray*) getTokens:(NSString*)text SearchString:(NSString*)searchString {
    NSRange textRange;
    NSRange searchRange = NSMakeRange(0, [text length]);
    textRange = [text rangeOfString:searchString
                            options:NSCaseInsensitiveSearch
                              range:searchRange];
    
    if ( textRange.location == NSNotFound ) {
        return nil;
    } else {
        NSRange selectedRange = textRange;
        
        return [NSMutableArray arrayWithObjects:[text substringToIndex:textRange.location],[text substringFromIndex:textRange.location+textRange.length],nil];
    }
}
//sizeWithAttributes
+ (BOOL) ifNull:(NSString*) str {
    if (str == (id)[NSNull null] || str.length == 0 ) return YES; else return NO;
}

+ (CGSize) getTextSize:(float) heightCushion forText: (NSString*)  text fontToSize:(UIFont*) font rectSize:(CGSize)rectSize{
    if ([text length]==0) return CGSizeMake(0,0);
    if (SYSTEM_VERSION_LESS_THAN(@"7.0")) {
        CGSize size = [text sizeWithFont:font constrainedToSize:rectSize];
        //[self printSize:text size:size];
        return size;
    }
    else {
        NSAttributedString *attributedText = [[NSAttributedString alloc] initWithString:text attributes:@{NSFontAttributeName:font}];
        //CGSize size = [text sizeWithFont:font constrainedToSize:rectSize lineBreakMode:NSLineBreakByWordWrapping];
        
        CGSize size= [attributedText boundingRectWithSize:rectSize options:NSStringDrawingUsesLineFragmentOrigin context:nil].size;
        //NSStringDrawingTruncatesLastVisibleLine|
        //NSLog(@"[%@] Font=%@, FontSize=%f Height=%f",text,font.fontName,font.pointSize,size.height);
        
        size.height=ceil([self isEnglish:[text lowercaseString]]?ceil(size.height*1.00):ceil(size.height*heightCushion));
        size.width=ceil(size.width);
        
        return size;
    }
}
+ (CGRect) getScreenSize {
    CGRect screenRect = [[UIScreen mainScreen] bounds];
    CGFloat screenWidth = screenRect.size.width;
    CGFloat screenHeight = screenRect.size.height;
    return screenRect;
}


+ (void) drawAtPoint:(NSString*) text atPoint:(CGPoint) point insideRect:(CGRect) drawRect withFont:(UIFont*) font usingTextAttributes: (NSDictionary*) textAttributes inContext:(NSStringDrawingContext*) drawingContext{
    //NSDictionary *textAttributes = @{NSFontAttributeName: [UIFont systemFontOfSize:18.0]};
    //NSDictionary *textAttributes = @{NSFontAttributeName: font,NSForegroundColorAttributeName: [UIColor blackColor]};
    //NSDictionary*attributi = @{ NSForegroundColorAttributeName: [UIColor blackColor],NSFontAttributeName: font, NSParagraphStyleAttributeName: paragraph};
    // Create string drawing context
    //NSStringDrawingContext *drawingContext = [[NSStringDrawingContext alloc] init];
    //drawingContext.minimumScaleFactor = 1.0; // Half the font size
    
    //CGRect drawRect = CGRectMake(point.x, point.y, 200.0, 200.0);
    if (SYSTEM_VERSION_GREATER_THAN(@"7.0")) {
        [text drawWithRect:drawRect
                   options:NSStringDrawingUsesLineFragmentOrigin
                attributes:textAttributes
                   context:drawingContext];
    }
    else {
        [text drawAtPoint:point withFont:font];
    }
    
}

+ (void) drawAtPoint:(NSString*) text atPoint:(CGPoint) point withFont:(UIFont*) font {
    //NSDictionary *textAttributes = @{NSFontAttributeName: [UIFont systemFontOfSize:18.0]};
    if (SYSTEM_VERSION_GREATER_THAN(@"7.0")) {
        NSDictionary *textAttributes = @{NSFontAttributeName: font,NSForegroundColorAttributeName: [UIColor blackColor]};
        // Create string drawing context
        NSStringDrawingContext *drawingContext = [[NSStringDrawingContext alloc] init];
        drawingContext.minimumScaleFactor = 1.0; // Half the font size
        
        CGRect drawRect = CGRectMake(point.x, point.y, 200.0, 200.0);
        [text drawWithRect:drawRect
                   options:NSStringDrawingUsesLineFragmentOrigin
                attributes:textAttributes
                   context:drawingContext];
    }
    else {
        [text drawAtPoint:point withFont:font];
    }
    
}

+(void) drawInRect:(NSString*) text insideRect:(CGRect) rect withFont:(UIFont*) font inColor:(UIColor*) color {
    NSMutableParagraphStyle* p = [NSMutableParagraphStyle new];
    p.alignment = NSTextAlignmentCenter;
    NSAttributedString* centerText =
    [[NSAttributedString alloc] initWithString:text attributes:@{
                                                                 NSFontAttributeName: font,
                                                                 NSForegroundColorAttributeName: color,
                                                                 NSParagraphStyleAttributeName: p
                                                                 }];
    [centerText drawInRect:rect];
    
}

+ (NSRange) findRange:(NSString*)text SearchString:(NSString*) searchString {
    NSRange textRange;
    NSRange searchRange = NSMakeRange(0, [text length]);
    textRange = [text rangeOfString:searchString
                            options:NSCaseInsensitiveSearch
                              range:searchRange];
    
    if ( textRange.location == NSNotFound ) {
        return NSMakeRange(0, 0);
    } else {
        return textRange;
    }
}

+(void) clearArray:(NSMutableArray*) array {
    if (array!=nil) {
        [array removeAllObjects];
        //array=nil;
    }
}


+(NSString*) capitalizeFirstLetterOf:(NSString*) _text{
    _text = [_text stringByReplacingCharactersInRange:NSMakeRange(0,1) withString:[[_text substringToIndex:1] capitalizedString]];
    return _text;
}

+(NSString*) substr:(NSString*) str from:(int) fromPos length:(int) lt {
    return [str substringWithRange:NSMakeRange(fromPos, lt)];
}


+(natural_t) get_free_memory {
    mach_port_t host_port;
    mach_msg_type_number_t host_size;
    vm_size_t pagesize;
    host_port = mach_host_self();
    host_size = sizeof(vm_statistics_data_t) / sizeof(integer_t);
    host_page_size(host_port, &pagesize);
    vm_statistics_data_t vm_stat;
    
    if (host_statistics(host_port, HOST_VM_INFO, (host_info_t)&vm_stat, &host_size) != KERN_SUCCESS) {
        NSLog(@"Failed to fetch vm statistics");
        return 0;
    }
    
    /* Stats in bytes */
    natural_t mem_free = vm_stat.free_count * (int)pagesize;
    return mem_free;
}

+(void) createImageOn:(DefinitionCells*) cellDefinition withimage:(NSString*) imageName atLocation : (CGRect) rect {
    UIImageView *img = [[UIImageView alloc] initWithFrame:rect];
    img.image = [UIImage imageNamed:imageName];
    [cellDefinition addSubview:img];
    
}

+(void) createLabel:(DefinitionCells*)cellDefinition Text:(NSString*)text Rect:(CGRect)rect Font:(UIFont*)font TextAlignment:(NSTextAlignment)textAlignment TextColor:(UIColor*)textColor TextDirection:(enum TEXT_DIRECTION)textDirection WordToHighlight:(NSString*) wordToHighlight{
    VerseDrawLabel *verseLabel = [[VerseDrawLabel alloc] initWithWord:rect SelectedWord:wordToHighlight];
    verseLabel.text =text;
    verseLabel.backgroundColor = [UIColor clearColor]; // [UIColor brownColor];
    verseLabel.font = font;
    //verseLabel.shadowColor = [UIColor grayColor];
    //verseLabel.shadowOffset = CGSizeMake(1,1);
    verseLabel.textColor = textColor;
    verseLabel.textAlignment = textAlignment; // UITextAlignmentCenter, UITextAlignmentLeft
    verseLabel.lineBreakMode = NSLineBreakByWordWrapping;
    [verseLabel setNumberOfLines:0];
    //[verseLabel sizeToFit];
    [verseLabel setTextDirection:textDirection];
    [cellDefinition addSubview:verseLabel];
    
    
    
}

//CreateLabel:(DefinitionCells*)cellDefinition Text:(NSString*)text TranslationSplitWord:(NSString*)translation_split_word Rect:(CGRect)rect Font:(UIFont*)font SplitWordFont:(UIFont*)splitWordFont TextAlignment:(UITextAlignment)textAlignment TextColor:(UIColor*)textColor TextDirection:(enum TEXT_DIRECTION)textDirection WordToHighlight:(NSString*)wordToHighlight;


+(void) createLabel:(DefinitionCells*)cellDefinition Text:(NSString*) text TranslationSplitWord:(NSString*)translation_split_word Rect:(CGRect)rect Font:(UIFont*)font SplitWordFont:(UIFont*)splitWordFont TextAlignment:(NSTextAlignment)textAlignment TextColor:(UIColor*)textColor TextDirection:(enum TEXT_DIRECTION) textDirection WordToHighlight:(NSString*) wordToHighlight{
    VerseDrawLabel *verseLabel = [[VerseDrawLabel alloc] initWithWord:rect SelectedWord:wordToHighlight];
    verseLabel.text =text;
    verseLabel.backgroundColor = [UIColor clearColor]; // [UIColor brownColor];
    verseLabel.font = font;
    //verseLabel.shadowColor = [UIColor grayColor];
    //verseLabel.shadowOffset = CGSizeMake(1,1);
    verseLabel.textColor = textColor;
    verseLabel.textAlignment = textAlignment; // UITextAlignmentCenter, UITextAlignmentLeft
    verseLabel.lineBreakMode = NSLineBreakByWordWrapping;
    [verseLabel setNumberOfLines:0];
    //[verseLabel sizeToFit];
    [verseLabel setTextDirection:textDirection];
    [verseLabel setTranslation_split_word:translation_split_word];
    [cellDefinition addSubview:verseLabel];
    
    
    
}

+ (void) addBorderTo:(UILabel*) lbl {
    lbl.layer.borderColor = [UIColor greenColor].CGColor;
    lbl.layer.borderWidth = 3.0;
}

+ (void) start{
    start = [NSDate date];
}

+ (BOOL) isNumber:(NSString*) numberStr {
    NSCharacterSet *alphaNums = [NSCharacterSet decimalDigitCharacterSet];
    NSCharacterSet *inStringSet = [NSCharacterSet characterSetWithCharactersInString:numberStr];
    return [alphaNums isSupersetOfSet:inStringSet];
}


+ (BOOL) isEnglish:(NSString*) text {
    NSCharacterSet *inStringSet = [NSCharacterSet characterSetWithCharactersInString:[text substringToIndex:1]];
    NSCharacterSet *alphaNums= [NSCharacterSet characterSetWithCharactersInString:@"abcdefghijklmnopqrstuvwxyz"];
    return [alphaNums isSupersetOfSet:inStringSet];
}


+ (NSString*) end {
    NSDate* end=[NSDate date];
    NSTimeInterval timeInterval = [end timeIntervalSinceDate:start];
    NSLog(@" Process took :%f",timeInterval);
}

+ (NSString*) getTodaysDateInFormat:(NSString*) format  {
    NSDateFormatter *dateFormat = [[NSDateFormatter alloc] init];
    [dateFormat setDateFormat:format];
    //NSDateFormatter *timeFormat = [[NSDateFormatter alloc] init];
    //[timeFormat setDateFormat:@"HH:mm:ss"];
    
    NSDate *now = [[NSDate alloc] init];
    
    NSString *theDate = [dateFormat stringFromDate:now];
    //NSString *theTime = [timeFormat stringFromDate:now];
    return theDate;
}

+ (void) showMessage:(NSString*)title Message:(NSString*)message{
    UIAlertView *messageView  = [[UIAlertView alloc] initWithTitle:title message:message delegate:nil cancelButtonTitle:@"OK"  otherButtonTitles:nil];
    for (UIView *view in messageView.subviews) {
        if([[view class] isSubclassOfClass:[UILabel class]]) {
            ((UILabel*)view).textAlignment = NSTextAlignmentRight;
        }
    }
    [messageView show];
}
+ (UITabBarController*) getTabController {
    AppDelegate* theDelegate = (AppDelegate*) [UIApplication sharedApplication].delegate;
    return theDelegate.myTabBarController;
}

+(bool) isTabController:(UIViewController*) callingController {
    if (callingController!=nil) {
        UITabBarController* tabController = [self getTabController];
        for ( UIViewController *childVC in tabController.viewControllers ) {
            if ([childVC.title isEqualToString:callingController.title]) {
                return true;
            }
        }
    }
    return false;
}

+(void) moveTab:(UIViewController*) callingController by:(int) incr {
    int index=0;
    int selectedIndex=-1;
    if (callingController!=nil) {
        UITabBarController* tabController = [self getTabController];
        for ( UIViewController *childVC in tabController.viewControllers ) {
            if ([childVC.title isEqualToString:callingController.title]) {
                selectedIndex = index;
            }
            index++;
        }
        if (selectedIndex+incr >=0 && selectedIndex+incr < index) {
            [tabController setSelectedIndex:selectedIndex+incr];
        }
        
    }
    
}
+ (DictionaryDataController*) getDataModel {
    return ((AppDelegate *) [UIApplication sharedApplication].delegate).dataController;
}



+ (NSString*) getDeviceName {
    size_t size = 100;
    char *hw_machine = malloc(size);
    int name[] = {CTL_HW,HW_MACHINE};
    sysctl(name, 2, hw_machine, &size, NULL, 0);
    NSString *hardware = [NSString stringWithUTF8String:hw_machine];
    free(hw_machine);
    NSLog(@"%@",hardware);
    if ([hardware isEqualToString:@"iPhone1,1"]) return @"iPhone 2G";
    if ([hardware isEqualToString:@"iPhone1,2"]) return @"iPhone 3G";
    if ([hardware isEqualToString:@"iPhone2,1"]) return @"iPhone 3GS";
    if ([hardware isEqualToString:@"iPhone3,1"]) return @"iPhone 4";
    if ([hardware isEqualToString:@"iPhone3,2"]) return @"iPhone 4";
    if ([hardware isEqualToString:@"iPhone3,3"]) return @"iPhone 4 (CDMA)";
    if ([hardware isEqualToString:@"iPhone4,1"]) return @"iPhone 4S";
    if ([hardware isEqualToString:@"iPhone5,1"]) return @"iPhone 5";
    if ([hardware isEqualToString:@"iPhone5,2"]) return @"iPhone 5 (GSM+CDMA)";
    
    if ([hardware isEqualToString:@"iPod1,1"]) return @"iPod Touch (1 Gen)";
    if ([hardware isEqualToString:@"iPod2,1"]) return @"iPod Touch (2 Gen)";
    if ([hardware isEqualToString:@"iPod3,1"]) return @"iPod Touch (3 Gen)";
    if ([hardware isEqualToString:@"iPod4,1"]) return @"iPod Touch (4 Gen)";
    if ([hardware isEqualToString:@"iPod5,1"]) return @"iPod Touch (5 Gen)";
    
    if ([hardware isEqualToString:@"iPad1,1"]) return @"iPad";
    if ([hardware isEqualToString:@"iPad1,2"]) return @"iPad 3G";
    if ([hardware isEqualToString:@"iPad2,1"]) return @"iPad 2 (WiFi)";
    if ([hardware isEqualToString:@"iPad2,2"]) return @"iPad 2";
    if ([hardware isEqualToString:@"iPad2,3"]) return @"iPad 2 (CDMA)";
    if ([hardware isEqualToString:@"iPad2,4"]) return @"iPad 2";
    if ([hardware isEqualToString:@"iPad2,5"]) return @"iPad Mini (WiFi)";
    if ([hardware isEqualToString:@"iPad2,6"]) return @"iPad Mini";
    if ([hardware isEqualToString:@"iPad2,7"]) return @"iPad Mini (GSM+CDMA)";
    if ([hardware isEqualToString:@"iPad3,1"]) return @"iPad 3 (WiFi)";
    if ([hardware isEqualToString:@"iPad3,2"]) return @"iPad 3 (GSM+CDMA)";
    if ([hardware isEqualToString:@"iPad3,3"]) return @"iPad 3";
    if ([hardware isEqualToString:@"iPad3,4"]) return @"iPad 4 (WiFi)";
    if ([hardware isEqualToString:@"iPad3,5"]) return @"iPad 4";
    if ([hardware isEqualToString:@"iPad3,6"]) return @"iPad 4 (GSM+CDMA)";
    
    if ([hardware isEqualToString:@"i386"]) return @"Simulator";
    if ([hardware isEqualToString:@"x86_64"]) return @"Simulator";
    
    return hardware;
}
+ (void)downloadFont:(NSString *)fontName {
	UIFont* aFont = [UIFont fontWithName:fontName size:12.];
    // If the font is already downloaded
	if (aFont && ([aFont.fontName compare:fontName] == NSOrderedSame || [aFont.familyName compare:fontName] == NSOrderedSame)) {
        // Go ahead and display the sample text.
		//NSUInteger sampleIndex = [_fontNames indexOfObject:fontName];
		//_fTextView.text = [_fontSamples objectAtIndex:sampleIndex];
		//_fTextView.font = [UIFont fontWithName:fontName size:24.];
		return;
	}
	
    // Create a dictionary with the font's PostScript name.
	NSMutableDictionary *attrs = [NSMutableDictionary dictionaryWithObjectsAndKeys:fontName, kCTFontNameAttribute, nil];
    
    // Create a new font descriptor reference from the attributes dictionary.
	CTFontDescriptorRef desc = CTFontDescriptorCreateWithAttributes((__bridge CFDictionaryRef)attrs);
    
    NSMutableArray *descs = [NSMutableArray arrayWithCapacity:0];
    [descs addObject:(__bridge id)desc];
    CFRelease(desc);
    
	__block BOOL errorDuringDownload = NO;
	
	// Start processing the font descriptor..
    // This function returns immediately, but can potentially take long time to process.
    // The progress is notified via the callback block of CTFontDescriptorProgressHandler type.
    // See CTFontDescriptor.h for the list of progress states and keys for progressParameter dictionary.
    CTFontDescriptorMatchFontDescriptorsWithProgressHandler( (__bridge CFArrayRef)descs, NULL,  ^(CTFontDescriptorMatchingState state, CFDictionaryRef progressParameter) {
        
		//NSLog( @"state %d - %@", state, progressParameter);
		
		double progressValue = [[(__bridge NSDictionary *)progressParameter objectForKey:(id)kCTFontDescriptorMatchingPercentage] doubleValue];
		
		if (state == kCTFontDescriptorMatchingDidBegin) {
			dispatch_async( dispatch_get_main_queue(), ^ {
                // Show an activity indicator
				//[_fActivityIndicatorView startAnimating];
				//_fActivityIndicatorView.hidden = NO;
                
                // Show something in the text view to indicate that we are downloading
                //_fTextView.text= [NSString stringWithFormat:@"Downloading %@", fontName];
				//_fTextView.font = [UIFont systemFontOfSize:14.];
				
				NSLog(@"Begin Matching");
			});
		} else if (state == kCTFontDescriptorMatchingDidFinish) {
			dispatch_async( dispatch_get_main_queue(), ^ {
                // Remove the activity indicator
				//[_fActivityIndicatorView stopAnimating];
				//_fActivityIndicatorView.hidden = YES;
                
                // Display the sample text for the newly downloaded font
				//NSUInteger sampleIndex = [_fontNames indexOfObject:fontName];
				//_fTextView.text = [_fontSamples objectAtIndex:sampleIndex];
				//_fTextView.font = [UIFont fontWithName:fontName size:24.];
				
                // Log the font URL in the console
				CTFontRef fontRef = CTFontCreateWithName((__bridge CFStringRef)fontName, 0., NULL);
                CFStringRef fontURL = CTFontCopyAttribute(fontRef, kCTFontURLAttribute);
				//NSLog(@"%@", (__bridge NSURL*)(fontURL));
                CFRelease(fontURL);
				CFRelease(fontRef);
                
				if (!errorDuringDownload) {
					NSLog(@"%@ downloaded", fontName);
				}
			});
		} else if (state == kCTFontDescriptorMatchingWillBeginDownloading) {
			dispatch_async( dispatch_get_main_queue(), ^ {
                // Show a progress bar
				//_fProgressView.progress = 0.0;
				//_fProgressView.hidden = NO;
				//NSLog(@"Begin Downloading");
			});
		} else if (state == kCTFontDescriptorMatchingDidFinishDownloading) {
			dispatch_async( dispatch_get_main_queue(), ^ {
                // Remove the progress bar
				//_fProgressView.hidden = YES;
				//NSLog(@"Finish downloading");
			});
		} else if (state == kCTFontDescriptorMatchingDownloading) {
			dispatch_async( dispatch_get_main_queue(), ^ {
                // Use the progress bar to indicate the progress of the downloading
				//[_fProgressView setProgress:progressValue / 100.0 animated:YES];
				NSLog(@"Downloading %.0f%% complete", progressValue);
			});
		} else if (state == kCTFontDescriptorMatchingDidFailWithError) {
            // An error has occurred.
            // Get the error message
            NSError *error = [(__bridge NSDictionary *)progressParameter objectForKey:(id)kCTFontDescriptorMatchingError];
            if (error != nil) {
                //errorMessage = [error description];
            } else {
                //_errorMessage = @"ERROR MESSAGE IS NOT AVAILABLE!";
            }
            // Set our flag
            errorDuringDownload = YES;
            
            dispatch_async( dispatch_get_main_queue(), ^ {
                //_fProgressView.hidden = YES;
				//NSLog(@"Download error: %@", _errorMessage);
			});
		}
        
		return (bool)YES;
	});
    
}

+(void) displayAllFonts{
    for (NSString* family in [UIFont familyNames]){
        NSLog(@"%@", family);
        for (NSString* name in [UIFont fontNamesForFamilyName: family]){
            NSLog(@"  %@", name);
        }
    }
}

+(void) printRect:(NSString*) title ForRect:(CGRect) rect{
    NSLog(@"%@ :(x=%f,y=%f,width=%f,height=%f)",title,rect.origin.x,rect.origin.y,rect.size.width,rect.size.height);
}

+(void) printSize:(NSString*) title size:(CGSize) size{
    NSLog(@"%@ :(width=%f,height=%f)",title,size.width,size.height);
}


+(void) loadNib{
    
}

+ (UIColor*) getGreen {
    UIColor* green = [UIColor colorWithRed:0.001 green:0.99 blue:0.50 alpha:1];
    for (int c=0; c<1; c++) {
        green = [Utils darkerColorForColor:green];
    }
    return green;
    
}


+(bool) isiPad {
    return ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPad);
}

+(bool) isRetina{
    //NSLog(@"device type = %i",[self getDeviceType]);
    if ([[UIScreen mainScreen] respondsToSelector:@selector(displayLinkWithTarget:selector:)] &&
        ([UIScreen mainScreen].scale == 2.0)) {
        return true;// Retina display
    } else {
        return false;
    }
}
+(NSString*) trim:(NSString*) string {
    return [string stringByTrimmingCharactersInSet:
            [NSCharacterSet whitespaceCharacterSet]];
}

@end
