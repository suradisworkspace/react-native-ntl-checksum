
#import "RNBundleChecksum.h"

#include <CommonCrypto/CommonDigest.h>

@implementation RNBundleChecksum

- (dispatch_queue_t)methodQueue
{
    return dispatch_queue_create("com.appirio.React.RNBundleChecksum", DISPATCH_QUEUE_SERIAL);
}

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(getChecksum:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    NSString *path = [[NSBundle mainBundle] pathForResource:@"main" ofType:@"jsbundle"];
    NSString *data = [NSString stringWithContentsOfFile:path encoding:NSUTF8StringEncoding error:nil];

    if (data == nil) {
        resolve(nil);
        return;
    }
    
    const char* str = [data UTF8String];
    
    unsigned char result[CC_SHA256_DIGEST_LENGTH];
    CC_SHA256(str, strlen(str), result);

    NSMutableString *ret = [NSMutableString stringWithCapacity:CC_SHA256_DIGEST_LENGTH*2];
    for(int i = 0; i<CC_SHA256_DIGEST_LENGTH; i++)
    {
        [ret appendFormat:@"%02x",result[i]];
    }

    resolve(ret);
}

RCT_EXPORT_METHOD(getChecksumCert: (NSString *)certName resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    NSError* error = nil;
    
    NSString *path = [[NSBundle mainBundle] pathForResource:certName ofType:@"cer"];
    NSData *nsData = [NSData dataWithContentsOfFile:path];
    
   if (nsData == nil) {
       resolve(nil);
       return;
   }
    
    unsigned char result[CC_SHA256_DIGEST_LENGTH];
    CC_SHA256([nsData bytes], [nsData length], result);

    NSMutableString *ret = [NSMutableString stringWithCapacity:CC_SHA256_DIGEST_LENGTH*2];
    for(int i = 0; i<CC_SHA256_DIGEST_LENGTH; i++)
    {
        [ret appendFormat:@"%02x",result[i]];
    }

    resolve(ret);
}

@end
