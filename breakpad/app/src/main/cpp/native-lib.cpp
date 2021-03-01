#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_org_devio_as_proj_breakpad_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT void JNICALL
Java_org_devio_as_proj_breakpad_MainActivity_crash(JNIEnv *env, jobject thiz) {
    int *a = nullptr;
    *a = 1;
}