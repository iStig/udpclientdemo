//
//  Start.h
//  GDP
//
//  Created by Chua Yu Mei on 17/12/2014.
//  Copyright (c) 2014 User. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "UDPEcho.h"

#pragma mark * Start
@protocol UDPStartDelegate <NSObject>
@optional
- (void)handleReceiveData:(NSString *)data fromAddress:(NSString *)address;
@end

@interface Start :NSObject <UDPEchoDelegate>

- (BOOL)runServerOnPort:(NSUInteger)port;
- (BOOL)runClientWithHost:(NSString *)host port:(NSUInteger)port;

@end

@interface Start ()

@property (nonatomic, strong, readwrite) UDPEcho *      echo;
@property (nonatomic, strong, readwrite) NSTimer *      sendTimer;
@property (nonatomic, assign, readwrite) NSUInteger     sendCount;
@property (nonatomic, assign, readwrite) id <UDPStartDelegate> delegate;


@end