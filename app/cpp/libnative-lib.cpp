#include <jni.h>
#include <string>


extern "C" jstring
Java_com_nsicyber_barblend_common_ApiKeyProvider_00024Companion_getApiKey(
        JNIEnv *env,
        jobject
) {
    std::string app_secret = "653938e95amsh42426f8d578c3f2p167abejsn4dde4a9cfda3";
    return env->NewStringUTF(app_secret.c_str());
}