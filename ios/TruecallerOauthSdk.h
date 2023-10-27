
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNTruecallerOauthSdkSpec.h"

@interface TruecallerOauthSdk : NSObject <NativeTruecallerOauthSdkSpec>
#else
#import <React/RCTBridgeModule.h>

@interface TruecallerOauthSdk : NSObject <RCTBridgeModule>
#endif

@end
