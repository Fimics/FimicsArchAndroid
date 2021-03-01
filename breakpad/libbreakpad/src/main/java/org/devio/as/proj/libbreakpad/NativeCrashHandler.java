package org.devio.as.proj.libbreakpad;

public class NativeCrashHandler {
    static {
        System.loadLibrary("breakpad-core");
    }

    //nativecrash 日志文件存放的目录
    public static void init(String crashDir) {
        initBreakPad(crashDir);
    }

    private static native void initBreakPad(String crashDir);
}
