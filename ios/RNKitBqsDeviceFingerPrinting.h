//
//  RNKitBqsDeviceFingerPrinting.h
//  RNKitBqsDeviceFingerPrinting
//
//  Created by SimMan on 2017/8/10.
//  Copyright © 2017年 RNKit.io. All rights reserved.
//

#import <Foundation/Foundation.h>

#if __has_include(<React/RCTBridge.h>)
#import <React/RCTBridgeModule.h>
#import <React/RCTRootView.h>
#else
#import "RCTBridgeModule.h"
#import "RCTRootView.h"
#endif

@interface RNKitBqsDeviceFingerPrinting : NSObject <RCTBridgeModule>

@end
