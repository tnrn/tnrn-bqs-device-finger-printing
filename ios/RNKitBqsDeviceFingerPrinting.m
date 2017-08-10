//
//  RNKitBqsDeviceFingerPrinting.m
//  RNKitBqsDeviceFingerPrinting
//
//  Created by SimMan on 2017/8/10.
//  Copyright © 2017年 RNKit.io. All rights reserved.
//

#import "RNKitBqsDeviceFingerPrinting.h"
#import "BqsDeviceFingerPrinting.h"

@interface RNKitBqsDeviceFingerPrinting() <BqsDeviceFingerPrintingDelegate, BqsDeviceFingerPrintingContactsDelegate>

@end

@implementation RNKitBqsDeviceFingerPrinting {
    RCTPromiseResolveBlock _resolveBlock;
    RCTPromiseRejectBlock _rejectBlock;
}

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

@synthesize bridge = _bridge;

RCT_EXPORT_MODULE()

#pragma mark 初始化方法
RCT_EXPORT_METHOD(init:(NSDictionary *)args
                  resolve:(RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject) {
    _resolveBlock = resolve;
    _rejectBlock = reject;
    
    if (args == nil) {
        assert("args is nil!");
    }
    
    NSArray *keys = [args allKeys];
    BOOL isGatherContacts = NO;
    if([keys containsObject:@"isGatherContacts"]) {
        isGatherContacts = [[args objectForKey:@"isGatherContacts"] boolValue];
    }
    
    [BqsDeviceFingerPrinting.sharedBqsDeviceFingerPrinting setBqsDeviceFingerPrintingDelegate:self];
    [BqsDeviceFingerPrinting.sharedBqsDeviceFingerPrinting initBqsDFSdkWithParams:args];
    
    if (isGatherContacts) {
        [BqsDeviceFingerPrinting.sharedBqsDeviceFingerPrinting setBqsDeviceFingerPrintingContactsDelegate:self];
    }
}

#pragma mark 获取tokenkey
RCT_EXPORT_METHOD(getTokenKey:(RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject) {
    NSString *token = [NSString stringWithFormat:@"%@", [BqsDeviceFingerPrinting.sharedBqsDeviceFingerPrinting getTokenKey]];
    resolve(token);
}

#pragma mark 提交通讯录
RCT_EXPORT_METHOD(commitContacts:(RCTPromiseResolveBlock)resolve
                  reject:(RCTPromiseRejectBlock)reject) {
    _resolveBlock = resolve;
    _rejectBlock = reject;
    [BqsDeviceFingerPrinting.sharedBqsDeviceFingerPrinting setBqsDeviceFingerPrintingContactsDelegate:self];
    [BqsDeviceFingerPrinting.sharedBqsDeviceFingerPrinting commitContacts];
}

#pragma mark 设置并提交定位
RCT_EXPORT_METHOD(commitLocaitonWithLongitude:(double) longitude
                  withLatitude:(double) latitude) {
    
    [BqsDeviceFingerPrinting.sharedBqsDeviceFingerPrinting commitLocaitonWithLongitude:longitude withLatitude:latitude];
}

#pragma mark 提交定位信息
RCT_EXPORT_METHOD(commitLocaiton) {
    [BqsDeviceFingerPrinting.sharedBqsDeviceFingerPrinting commitLocaiton];
}

#pragma mark - Delegate
#pragma mark 初始化成功
-(void) onBqsDFInitSuccess:(NSString *) tokenKey
{
    if (_resolveBlock) _resolveBlock([NSString stringWithFormat:@"%@", tokenKey]);
    [self clearUp];
}

#pragma mark 初始化失败
-(void) onBqsDFInitFailure:(NSString *) resultCode withDesc:(NSString *) resultDesc
{
    if (_rejectBlock) _rejectBlock(resultCode, resultDesc, nil);
    [self clearUp];
}

#pragma mark 通讯录上传成功
-(void) onBqsDFContactsUploadSuccess:(NSString *) tokenKey
{
    if (_resolveBlock) _resolveBlock([NSString stringWithFormat:@"%@", tokenKey]);
    [self clearUp];
}

#pragma mark 通讯录上传失败
-(void) onBqsDFContactsUploadFailure:(NSString *) resultCode withDesc:(NSString *) resultDesc
{
    if (_rejectBlock) _rejectBlock(resultCode, resultDesc, nil);
    [self clearUp];
}

- (void) clearUp
{
    [BqsDeviceFingerPrinting.sharedBqsDeviceFingerPrinting setBqsDeviceFingerPrintingDelegate:nil];
    [BqsDeviceFingerPrinting.sharedBqsDeviceFingerPrinting setBqsDeviceFingerPrintingContactsDelegate:nil];
    _resolveBlock = nil;
    _rejectBlock = nil;
}

@end
