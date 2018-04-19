//
// Created by Shawn on 2018/4/19.
//


#include <jni.h>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_tal_cmakedemo_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    return env->NewStringUTF("Hello from C++");
}
