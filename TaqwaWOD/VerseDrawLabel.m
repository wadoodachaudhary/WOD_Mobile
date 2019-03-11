//
//  VerseDrawLabel.m
//  Tabster
//
//  Created by Wadood Chaudhary on 5/12/12.
//  Copyright (c) 2012 Ahmadiyya Muslim Community - Taqwa Group. All rights reserved.
//

#import "VerseDrawLabel.h"
#import <Foundation/Foundation.h>
#import "Utils.h"


@implementation VerseDrawLabel
@synthesize word,textDirection,translation_split_word,translation,transliteration,locWords,grammarWords;
@synthesize mainTextAlignment,mainTextColor,translationSplitWordFont,translationFont,transliterationFont,fontToSize;

- (id)initWithFrame:(CGRect)frame{
    //frame = CGRectMake(frame.origin.x,frame.origin.y,frame.size.width,frame.size.height);
    CGRect rect = frame;
//NSLog(@"****VerseDrawLabel Frame Rect: (x=%f,y=%f,width=%f,height=%f)",rect.origin.x,rect.origin.y,rect.size.width,rect.size.height);

    self = [super initWithFrame:frame];
    self.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
    self.contentMode = UIViewContentModeRedraw;
    return self;
}

- (id)initWithWord:(CGRect)frame SelectedWord:(NSString*)selectedWord {
    //frame = CGRectMake(frame.origin.x,frame.origin.y,frame.size.width,frame.size.height);
    CGRect rect = frame;

    //NSLog(@"****VerseDrawLabel Frame Rect: (x=%f,y=%f,width=%f,height=%f)",rect.origin.x,rect.origin.y,rect.size.width,rect.size.height);

    self = [super initWithFrame:frame];
    if (self) {
        
        [self setWord:selectedWord==nil?@"":selectedWord];
    }
    
    return self;
}

- (NSArray*) getSegmentsAt:(int) tokenIndex {
    NSMutableArray* segments= [[NSMutableArray alloc] init];
    for (NSArray* entryGrammar in grammarWords) {
        //NSLog(@"Token=%@",[entryGrammar objectAtIndex:1]);
        int token_no=[(NSString*) [entryGrammar objectAtIndex:1] intValue];
        if (token_no==tokenIndex) {
            [segments addObject:entryGrammar];
        }
    }
    return segments;
}
-(void) drawRectangleAt:(CGRect) rect usingContext:(CGContextRef) context {
    //CGContextRef context=ctx;
    CGContextSetLineWidth(context, 1.0);
    CGContextSetStrokeColorWithColor(context,
    [UIColor blueColor].CGColor);
    //CGRect rectangle = CGRectMake(60,170,200,80);
    CGContextAddRect(context, rect);
    CGContextStrokePath(context);
    //CGContextFillRect(ctx, rect);
    
}


-(NSDictionary*) getTextAttributesFor:(UIFont*) font inColor:(UIColor*)  color {
    NSDictionary* attributi = @{ NSForegroundColorAttributeName: color,NSFontAttributeName: font};
    return attributi;
}

- (void)drawRect:(CGRect)rect {
    //NSLog(@"**************************Rect:%@  (x=%f,y=%f,width=%f,height=%f) [%@]",@"Split Words",rect.origin.x,rect.origin.y,rect.size.width,rect.size.height,translation_split_word);
    //NSLog(@"**************************Rect:%@  (x=%f,y=%f,width=%f,height=%f) [%@]",@"Text",rect.origin.x,rect.origin.y,rect.size.width,rect.size.height,[self text]);
    //rect = CGRectMake(rect.origin.x,rect.origin.y,rect.size.width,rect.size.height+20);
    // UIRectFrame(rect);
    bool hasTransliteration = transliteration != nil;;
    NSString* text = [self text];
    UIFont* font = [self font];
    CGSize currentLineSize = CGSizeMake(0,0),tokenSize;
    NSInteger index = 0;
    NSArray *tokens, *headers;
    bool hasSplitWords = translation_split_word != nil;
    if (hasSplitWords) {
        //NSLog(@"Split Words:%@",translation_split_word);
        headers = [text componentsSeparatedByString:@" "];
        tokens  = [translation_split_word componentsSeparatedByString:SPLIT_WORD_DELIMITER];
        //NSLog(@"Split Words:%i",[tokens count]);
    }
    else {
        tokens = [text componentsSeparatedByString:@" "];
    }
    if ([tokens count]==0) return;
    for (NSString* token in tokens) {
        //NSLog(@"Token:%@",token);
    }
    //NSDictionary *textAttributes = @{NSFontAttributeName: font,NSForegroundColorAttributeName: [UIColor blackColor]};
    // Create string drawing context
    NSStringDrawingContext *drawingContext = [[NSStringDrawingContext alloc] init];
    
    
    int line=0;
    int y=0,x=0;
    if (fontToSize==nil) fontToSize=hasSplitWords?translationSplitWordFont:font;
    currentLineSize  = [[tokens objectAtIndex:0]  sizeWithFont:fontToSize constrainedToSize: CGSizeMake(rect.size.width, rect.size.height) lineBreakMode: NSLineBreakByWordWrapping];
    //
    int lineHeightText=currentLineSize.height;
    CGSize maxLineSize = CGSizeMake(rect.size.width,lineHeightText);
    int lineHeight = (hasSplitWords)?(lineHeightText * 2):lineHeightText;
    int lineHeightText1 = lineHeightText * 2;
    int lineHeightText2 = lineHeightText;
    
    CGPoint point;
    NSValue *pointValue;
    NSString* spacer=@" ";
    NSString* header;
    CGContextRef ctx= UIGraphicsGetCurrentContext();
    CGContextClearRect(ctx, rect);
    //
       //[self drawRectangleAt:rect usingContext:ctx];
    //
    CGContextSetFillColorWithColor(ctx,self.textColor.CGColor);

    UIColor* clr= [UIColor colorWithRed:0.048 green:0.62 blue:0.87 alpha:1.0];   //CGContextSetStrokeColor(ctx,clr);
    CGContextSetLineWidth(ctx, 0.22f);
    int tokenIndex=0;
    NSString* token;
    NSArray* segments;
    float x1Min=0.0,x0Min=0.0;
    float y1Max=0.0,y0Max=0.0;
    
    int prevLineNo=-1;
    for (NSArray* record in locWords) {
        if (tokenIndex < [tokens count])   token=  [tokens objectAtIndex:tokenIndex];
        else token=[record objectAtIndex:0];
        //NSLog(@"Token in drawRect:[%@]:%i",token,tokenIndex);
        //tokenIndex++;
        CGPoint point= [(NSValue*)[record objectAtIndex:1] CGPointValue];
        CGRect  rect = [(NSValue*)[record objectAtIndex:2] CGRectValue];
        bool firstWordOnLine = [(NSNumber*)[record objectAtIndex:3] boolValue];
        bool firstLine = ([(NSNumber*)[record objectAtIndex:4] intValue]==0?true:false);
        int lineNo = [(NSNumber*)[record objectAtIndex:4] intValue];
        lineHeightText2=ceil(rect.size.height/3);
        lineHeightText1=rect.size.height-lineHeightText2;
        
        //NSLog(@"Token:%@  (x=%f,y=%f,width=%f,height=%f) LineHeightText(%i,%i)",token,rect.origin.x,rect.origin.y,rect.size.width,rect.size.height,lineHeightText1,lineHeightText2);
        if (lineNo!=prevLineNo) {
            prevLineNo=lineNo;
            x0Min=x1Min;
            y0Max=y1Max;
            x1Min=rect.origin.x;
            y1Max=rect.origin.y;
        }
        x1Min=MIN(x1Min,rect.origin.x);
        float yOffset = 0;//lineHeightText2/2;//ceil(rect.size.height/1.8);
        yOffset =0;
        if (hasSplitWords) {
            token = [headers objectAtIndex:tokenIndex];
            //segments = [self getSegmentsAt:tokenIndex];
            //NSLog(@"Token:%@  (x=%f,y=%f,width=%f,height=%f)(%i,%i)",token,rect.origin.x,rect.origin.y,rect.size.width,rect.size.height,lineHeightText1,lineHeightText2);
            
            CGContextSetStrokeColorWithColor(ctx,clr.CGColor);
            if (lineNo == 0){
                if (firstWordOnLine) {
                    //CGPoint points[4] = {CGPointMake(rect.origin.x, rect.origin.y+lineHeightText),CGPointMake(rect.origin.x, rect.origin.y-lineHeightText),
                      //  CGPointMake(rect.origin.x + rect.size.width,rect.origin.y-lineHeightText),CGPointMake(rect.origin.x + rect.size.width,rect.origin.y+lineHeightText)};
                    
                    CGPoint points[4] = {CGPointMake(rect.origin.x, rect.origin.y+lineHeightText1),CGPointMake(rect.origin.x, rect.origin.y),
                        CGPointMake(rect.origin.x + rect.size.width,rect.origin.y),CGPointMake(rect.origin.x + rect.size.width,rect.origin.y+lineHeightText1)};
                    //NSLog(@"Pts:(%f,%f),(%f,%f),(%f,%f),(%f,%f)",points[0].x,points[0].y,points[1].x,points[1].y,points[2].x,points[2].y,points[3].x,points[3].y);
                    CGContextStrokeLineSegments(ctx,points,4);
                    //return;
                }
                else {
                    //CGPoint points[2] = { CGPointMake(rect.origin.x, rect.origin.y+lineHeightText),CGPointMake(rect.origin.x, rect.origin.y-lineHeightText)};
                    CGPoint points[2] = { CGPointMake(rect.origin.x, rect.origin.y+lineHeightText1),CGPointMake(rect.origin.x,rect.origin.y)};
                    CGContextStrokeLineSegments(ctx,points,2);
                }
                //CGPoint points[2] = {CGPointMake(rect.origin.x, rect.origin.y-lineHeightText), CGPointMake(rect.origin.x + rect.size.width,rect.origin.y-lineHeightText)};
                CGPoint points[2] = {CGPointMake(rect.origin.x, rect.origin.y), CGPointMake(rect.origin.x + rect.size.width,rect.origin.y)};
                CGContextStrokeLineSegments(ctx,points,2);
            }
            else {
                //NSLog(@"        Token:%@  (First Word on line=%d,First Line=%d,line No=%i,xMin=%f)",token,firstWordOnLine,firstLine,lineNo,x0Min);
                if (firstWordOnLine) {
                    CGPoint points[4] = {CGPointMake(rect.origin.x, rect.origin.y+lineHeightText1),CGPointMake(rect.origin.x, rect.origin.y),
                            CGPointMake(rect.origin.x + rect.size.width,rect.origin.y),CGPointMake(rect.origin.x + rect.size.width,rect.origin.y+lineHeightText1)};

                    CGContextStrokeLineSegments(ctx,points,4);
                }
                else {
                    //CGPoint points[2] = { CGPointMake(rect.origin.x, rect.origin.y+lineHeightText),CGPointMake(rect.origin.x, y0Max)};
                    CGPoint points[2] = { CGPointMake(rect.origin.x, rect.origin.y+lineHeightText1),CGPointMake(rect.origin.x, y0Max)};
                    CGContextStrokeLineSegments(ctx,points,2);
                }
                if (rect.origin.x<x0Min) {
                    //CGPoint points[2] = {CGPointMake(rect.origin.x, rect.origin.y-lineHeightText), CGPointMake(MIN(x0Min,rect.origin.x + rect.size.width),rect.origin.y-lineHeightText)};
                    CGPoint points[2] = {CGPointMake(rect.origin.x, y0Max), CGPointMake(MIN(x0Min,rect.origin.x + rect.size.width),y0Max)};
                    CGContextStrokeLineSegments(ctx,points,2);
                }
                
            }
        }
        self.textColor=[UIColor darkTextColor];
        CGContextSetFillColorWithColor(ctx,self.textColor.CGColor);
        if (textDirection==RTL && word!=nil) {
            NSRange range = [token rangeOfString:word options:NSCaseInsensitiveSearch];
            if(range.location != NSNotFound) {
                //NSLog(@"Draw Label - highlighted word found");
                //CGContextSetFillColorWithColor(ctx,[UIColor redColor].CGColor);
                if (hasSplitWords) {
                    //[self drawString:token withFont:font inRect:CGRectMake(rect.origin.x, point.y+yOffset,rect.size.width,rect.size.height) alignVertically:false];
                    [Utils drawInRect:token insideRect:CGRectMake(rect.origin.x, point.y+yOffset,rect.size.width,rect.size.height) withFont:font inColor:[UIColor redColor]];
                    
                }else {
                    //[token drawAtPoint:point withFont:font];
                    [Utils drawAtPoint:token atPoint:point insideRect:rect withFont:font usingTextAttributes:[self getTextAttributesFor:font inColor:[UIColor redColor]] inContext:drawingContext];
                    if (tokenIndex ==[tokens count]-1)[self drawCircle:point FontSize:font.pointSize];
                }
                CGContextSetFillColorWithColor(ctx,self.textColor.CGColor);
            }
            else {
                if (hasSplitWords) {
                    //[self drawString:token withFont:font inRect:CGRectMake(rect.origin.x, point.y+yOffset,rect.size.width,rect.size.height) alignVertically:false];
                    [Utils drawInRect:token insideRect:CGRectMake(rect.origin.x, point.y+yOffset,rect.size.width,rect.size.height) withFont:font inColor:self.textColor];
                } else {
                    //[token drawAtPoint:point withFont:font];
                    //[Utils drawAtPoint:token atPoint:point withFont:font];
                    [Utils drawAtPoint:token atPoint:point insideRect:rect withFont:font usingTextAttributes:[self getTextAttributesFor:font inColor:self.textColor] inContext:drawingContext];

                    
                    if (tokenIndex ==[tokens count]-1){
                        // draw end marker
                        [self drawCircle:point FontSize:font.pointSize];
                    }
                }
            }
        }
        else if (textDirection==LTR && word!=nil) {
            NSRange range = [token rangeOfString:word options:NSCaseInsensitiveSearch];
            if(range.location != NSNotFound) {
                //[token drawAtPoint:point withFont:font];
                CGContextSetRGBFillColor(ctx,1.0,1.0,0.0,1.0);
                CGContextSetRGBStrokeColor(ctx, 1.0,1.0,0.0,1.0);
                CGContextFillRect(ctx, rect);
                [Utils drawAtPoint:token atPoint:point insideRect:rect withFont:font usingTextAttributes:[self getTextAttributesFor:font inColor:[UIColor blackColor]] inContext:drawingContext];
                CGContextSetFillColorWithColor(ctx,self.textColor.CGColor);
            }
            else {
                [Utils drawAtPoint:token atPoint:point withFont:font];
            }
        }
        else {
            
            if (hasSplitWords) {
                //[self drawString:token withFont:font inRect:CGRectMake(rect.origin.x, point.y+yOffset,rect.size.width,rect.size.height) alignVertically:false];
                [Utils drawInRect:token insideRect:CGRectMake(rect.origin.x, point.y+yOffset,rect.size.width,rect.size.height) withFont:font inColor:self.textColor];
            }
            else if (hasTransliteration) {
                [self drawString:token withFont:font inRect:CGRectMake(rect.origin.x, point.y,rect.size.width,rect.size.height) alignVertically:false];
            }else {
                //NSLog(@"Token:%@  (x=%f,y=%f,width=%f,height=%f)",token,rect.origin.x,rect.origin.y,rect.size.width,rect.size.height);
                //[token drawAtPoint:point withFont:font];
                [Utils drawAtPoint:token atPoint:point withFont:font];
                
                if (textDirection==RTL && tokenIndex ==[tokens count]-1){
                    [self drawCircle:point FontSize:font.pointSize];
                    // draw end marker
                    // UIImage *brushImage = [UIImage imageNamed:@"verse_end_marker.png"];
                    // [brushImage drawAtPoint:CGPointMake(point.x-brushImage.size.width-2, point.y+(rect.size.height-brushImage.size.height)/2)];
                }
                
            }
        }
        //Line#2 for split words
        if (hasSplitWords) {
            //CGContextSetStrokeColor(ctx,clr);
            rect.origin.y = rect.origin.y+lineHeightText1;
            y1Max=MAX(y1Max,rect.origin.y+rect.size.height-lineHeightText1);
            
            CGContextSetStrokeColorWithColor(ctx,clr.CGColor);
            if (firstWordOnLine) {
                //CGContextSetStrokeColor(ctx,clr);
                CGContextStrokeRect(ctx, CGRectMake(rect.origin.x, rect.origin.y, rect.size.width, rect.size.height-lineHeightText1));
            }
            else {
                CGPoint points[6] = { CGPointMake(rect.origin.x + rect.size.width,rect.origin.y+rect.size.height-lineHeightText1), CGPointMake(rect.origin.x, rect.origin.y+rect.size.height-lineHeightText1),
                    CGPointMake(rect.origin.x, rect.origin.y+rect.size.height-lineHeightText1),CGPointMake(rect.origin.x, rect.origin.y),
                    CGPointMake(rect.origin.x, rect.origin.y), CGPointMake(rect.origin.x + rect.size.width,rect.origin.y)
                };
                CGContextStrokeLineSegments(ctx,points,6);
            }
            token = [tokens objectAtIndex:tokenIndex];
            //[token drawAtPoint:CGPointMake(rect.origin.x, rect.origin.y) withFont:translationSplitWordFont];
            [self drawString:token withFont:translationSplitWordFont inRect:CGRectMake(rect.origin.x, rect.origin.y,rect.size.width,rect.size.height) alignVertically:true];
            
        }
        tokenIndex++;
        //if (textDirection==RTL) NSLog(@"%@(%f,%f)",token,point.x,point.y);
    }
    //CGContextSetRGBStrokeColor(ctx, 255, 255, 1, 1);
    //CGContextStrokeRect(ctx, rect);
    
    
}

- (void)drawCircle:(CGPoint) point FontSize:(CGFloat) fontSize {
    //NSLog(@"fontSize%f:",fontSize);
    fontSize = fontSize/5;
    CGRect rect = CGRectMake(point.x-fontSize-1,point.y+fontSize*2-1,fontSize,fontSize);
    CGContextRef context = UIGraphicsGetCurrentContext();
    CGContextSetLineWidth(context,2.0);
    CGContextSetStrokeColorWithColor(context, [UIColor lightGrayColor].CGColor);
    CGContextAddEllipseInRect(context, rect);
    CGContextStrokePath(context);
}

- (void) drawString: (NSString*) s withFont: (UIFont*) font inRect: (CGRect) contextRect alignVertically:(bool) alignVertical {
    //NSCharacterSet* quotes = [NSCharacterSet characterSetWithCharactersInString:@"'`"];
    //s = [[s componentsSeparatedByCharactersInSet:quotes] componentsJoinedByString:@" "];
    //NSLog(@"drawString-> s:%@  (x=%f,y=%f,width=%f,height=%f)",s,contextRect.origin.x,contextRect.origin.y,contextRect.size.width,contextRect.size.height);
    CGFloat fontHeight = font.pointSize;
    CGFloat yOffset = (contextRect.size.height - fontHeight) / 2.0;
    if (!alignVertical) yOffset=0;
    yOffset=0;
    NSRange range = [s rangeOfString:@"<" options:NSCaseInsensitiveSearch];
    //if(range.location != NSNotFound) {
    if (false) {
        //NSLog(@"Main:%@",s);
        //if ([s isEqualToString:@"All<u>a</U>hi"]) {};
        int x=contextRect.origin.x;
        CGSize textSize;
        NSRange range1,range2;
        while (range.location != NSNotFound) {
            //Bismi All<u>a</U>hi a<b>l</B>rra<u>h</U>m<u>a</U>ni a<b>l</B>rra<u>h</U>eem<b>i</b>
            NSRange rangeSearch = NSMakeRange(0, [s length]);
            NSString* transliteral = [[s substringWithRange:NSMakeRange(range.location+1,1)]lowercaseString];
            if ([transliteral isEqualToString:@"i"]) {
                range1 = [s rangeOfString:@"<i><u>"  options:NSCaseInsensitiveSearch];
                range2 = [s rangeOfString:@"</u></i>" options:NSCaseInsensitiveSearch];
                NSString* sb= [s substringWithRange:NSMakeRange(range1.location+range1.length, (range2.location-(range1.location+range1.length)))];
                if (range1.location>0) {
                    NSString* s1 = [s substringToIndex:range1.location];
                    //[s1 drawAtPoint:CGPointMake(x,contextRect.origin.y) withFont:font];
                    [Utils drawAtPoint:s1 atPoint:CGPointMake(x,contextRect.origin.y) withFont:font];
                    //NSLog(@"s1:%@ (%i,%i)",s1,x,(int) contextRect.origin.y);
                    //textSize  = [s1  sizeWithFont:font constrainedToSize: CGSizeMake(contextRect.size.width, contextRect.size.height) lineBreakMode: NSLineBreakByWordWrapping];
                    textSize = [Utils getTextSize:1.0 forText:s1  fontToSize:font rectSize:CGSizeMake(contextRect.size.width, contextRect.size.height)];
                    x = x+textSize.width;
                }
                UIFont* boldItalicFont = [UIFont fontWithName:@"Courier-BoldOblique" size:font.pointSize];
                //[sb drawAtPoint:CGPointMake(x,contextRect.origin.y) withFont:boldItalicFont];
                [Utils drawAtPoint:sb atPoint:CGPointMake(x,contextRect.origin.y) withFont:boldItalicFont];
                
                //textSize  = [sb  sizeWithFont:boldItalicFont constrainedToSize: CGSizeMake(contextRect.size.width, contextRect.size.height) lineBreakMode: NSLineBreakByWordWrapping];
                textSize = [Utils getTextSize:1.0 forText:sb  fontToSize:boldItalicFont rectSize:CGSizeMake(contextRect.size.width, contextRect.size.height)];
                
                //NSLog(@"sb:%@ (%i,%i)",sb,x,(int) contextRect.origin.y);
                x=x+textSize.width;
            }
            else if ([transliteral isEqualToString:@"b"])  {
                //while  (pos < [s length]) {
                range1 = [s rangeOfString:@"<B>"  options:NSCaseInsensitiveSearch];
                range2 = [s rangeOfString:@"</B>" options:NSCaseInsensitiveSearch];
                NSString* sb= [s substringWithRange:NSMakeRange(range1.location+range1.length, (range2.location-(range1.location+range1.length)))];
                if (range1.location>0) {
                    NSString* s1 = [s substringToIndex:range1.location];
                    //[s1 drawAtPoint:CGPointMake(x,contextRect.origin.y) withFont:font];
                    [Utils drawAtPoint:s1 atPoint:CGPointMake(x,contextRect.origin.y) withFont:font];
                    
                    //NSLog(@"s1:%@ (%i,%i)",s1,x,(int) contextRect.origin.y);
                    //textSize  = [s1  sizeWithFont:font constrainedToSize: CGSizeMake(contextRect.size.width, contextRect.size.height) lineBreakMode: NSLineBreakByWordWrapping];
                    textSize = [Utils getTextSize:1.0 forText:s1  fontToSize:font rectSize:CGSizeMake(contextRect.size.width, contextRect.size.height)];
                    
                    x = x+textSize.width;
                }
                UIFont* boldFont = [UIFont fontWithName:@"Courier-Bold" size:font.pointSize];
                //[sb drawAtPoint:CGPointMake(x,contextRect.origin.y) withFont:boldFont];
                [Utils drawAtPoint:sb atPoint:CGPointMake(x,contextRect.origin.y) withFont:boldFont];
                
                //textSize  = [sb  sizeWithFont:boldFont constrainedToSize: CGSizeMake(contextRect.size.width, contextRect.size.height) lineBreakMode: NSLineBreakByWordWrapping];
                textSize = [Utils getTextSize:1.0 forText:sb  fontToSize:boldFont rectSize:CGSizeMake(contextRect.size.width, contextRect.size.height)];
                
                //NSLog(@"sb:%@ (%i,%i)",sb,x,(int) contextRect.origin.y);
                x=x+textSize.width;
            }
            else if ([transliteral isEqualToString:@"u"])  {
                //while  (pos < [s length]) {
                range1 = [s rangeOfString:@"<U>" options:NSCaseInsensitiveSearch];
                range2 = [s rangeOfString:@"</U>" options:NSCaseInsensitiveSearch];
                NSString* su= [s substringWithRange:NSMakeRange(range1.location+range1.length, (range2.location-(range1.location+range1.length)))];
                if (range1.location>0) {
                    NSString* s1 = [s substringToIndex:range1.location];
                    //[s1 drawAtPoint:CGPointMake(x,contextRect.origin.y) withFont:font];
                    [Utils drawAtPoint:s1 atPoint:CGPointMake(x,contextRect.origin.y) withFont:font];
                    
                    //textSize  = [s1  sizeWithFont:font constrainedToSize: CGSizeMake(contextRect.size.width, contextRect.size.height) lineBreakMode: NSLineBreakByWordWrapping];
                    textSize = [Utils getTextSize:1.0 forText:s1  fontToSize:font rectSize:CGSizeMake(contextRect.size.width, contextRect.size.height)];
                    
                    //NSLog(@"s1:%@ (%i,%i)",s1,x,(int) contextRect.origin.y);
                    x = x+textSize.width;
                }
                //textSize  = [su sizeWithFont:font constrainedToSize: CGSizeMake(contextRect.size.width, contextRect.size.height) lineBreakMode: NSLineBreakByWordWrapping];
                textSize = [Utils getTextSize:1.0 forText:su  fontToSize:font rectSize:CGSizeMake(contextRect.size.width, contextRect.size.height)];
                
                //[su drawAtPoint:CGPointMake(x,contextRect.origin.y) withFont:font];
                [Utils drawAtPoint:su atPoint:CGPointMake(x,contextRect.origin.y) withFont:font];
                
                CGContextRef ctx= UIGraphicsGetCurrentContext();
                CGContextSetLineWidth(ctx, 0.5f);
                CGPoint points[2]={CGPointMake(x,contextRect.origin.y+textSize.height), CGPointMake(x+textSize.width,contextRect.origin.y+textSize.height)};
                //CGContextSetStrokeColor(ctx,CGColorGetComponents([UIColor blackColor].CGColor));
                CGFloat black[4] = {0, 0, 0, 1};
                CGContextSetStrokeColor(ctx,black);
                CGContextStrokeLineSegments(ctx,points,2);
                //[@"_" drawAtPoint:CGPointMake(x,contextRect.origin.y+1) withFont:font];
                
                //NSLog(@"su:%@ (%i,%i)",su,x,(int) contextRect.origin.y);
                
                x=x+textSize.width;
                
            }
            if (range2.location+range2.length+1 < [s length]) s= [s substringFromIndex:range2.location+range2.length];else s=@"";
            range = [s rangeOfString:@"<" options:NSCaseInsensitiveSearch];
        }
        if (s.length>0) {
            //[s drawAtPoint:CGPointMake(x,contextRect.origin.y) withFont:font];
            [Utils drawAtPoint:s atPoint:CGPointMake(x,contextRect.origin.y) withFont:font];
            
        }
    }
    else {
        CGRect textRect = CGRectMake(contextRect.origin.x, contextRect.origin.y+ yOffset, contextRect.size.width, fontHeight*2);
        //[s drawInRect: textRect withFont: font lineBreakMode: NSLineBreakByClipping alignment: NSTextAlignmentCenter];
        
        //UIFont *font = [UIFont fontWithName:@"Courier" size:kCellFontSize];
        
        /// Make a copy of the default paragraph style
        NSMutableParagraphStyle *paragraphStyle = [[NSParagraphStyle defaultParagraphStyle] mutableCopy];
        /// Set line break mode
        //paragraphStyle.lineBreakMode = NSLineBreakByTruncatingTail;
        /// Set text alignment
        paragraphStyle.alignment = NSTextAlignmentCenter;
        
        NSDictionary *attributes = @{ NSFontAttributeName: font,
                                      NSParagraphStyleAttributeName: paragraphStyle };
        
        //[s drawInRect:textRect withAttributes:attributes];
        [Utils drawAtPoint:s atPoint:CGPointMake(contextRect.origin.x,contextRect.origin.y) withFont:font];
    }
}
@end






