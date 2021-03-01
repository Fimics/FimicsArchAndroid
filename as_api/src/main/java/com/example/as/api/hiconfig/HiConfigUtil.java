package com.example.as.api.hiconfig;

import com.example.as.api.util.DateUtil;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HiConfigUtil {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    public static String genVersion() {
        return sdf.format(new Date());
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void delete(File file) {
        if (file != null && file.exists()) {
            file.delete();
        }
    }

    public static boolean compareVersion(@NonNull String version1, @Nullable String version2) {
        if (version2 == null) return true;
        return version1.compareTo(version2) > 0;
    }
}
