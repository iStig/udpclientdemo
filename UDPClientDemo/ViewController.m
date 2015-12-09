//
//  ViewController.m
//  UDPClientDemo
//
//  Created by iStig on 15/12/8.
//  Copyright © 2015年 iStig. All rights reserved.
//

#import "ViewController.h"
#import "Start.h"
@interface ViewController () <UDPStartDelegate>
@property (nonatomic, strong) Start *start;
@end

@implementation ViewController

- (void)viewDidLoad {
  [super viewDidLoad];
  // Do any additional setup after loading the view, typically from a nib.
  
  self.start = [[Start alloc] init];
  self.start.delegate = self;
  [self.start runServerOnPort:59031];
}

#pragma mark - UDPStartDelegate
- (void)handleReceiveData:(NSString *)data fromAddress:(NSString *)address {
  NSLog(@"data:\n%@\n address:\n%@\n",data,address);
}

- (void)didReceiveMemoryWarning {
  [super didReceiveMemoryWarning];
  // Dispose of any resources that can be recreated.
}

@end
