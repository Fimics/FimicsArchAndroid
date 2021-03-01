package org.devio.as.proj.hi_hotfix;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

public class FileUtil {
    public static void copyFile(File source, File dest) {
        FileInputStream is = null;
        FileOutputStream os = null;
        File parent = dest.getParentFile();
        if (parent != null && (!parent.exists())) {
            parent.mkdirs();
        }
        if (!dest.exists()) {
            try {
                dest.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);

            byte[] buffer = new byte[16384];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String optimizedPathFor(File path) {
        // dex_location = /foo/bar/baz.jar
        // odex_location = /foo/bar/oat/<isa>/baz.odex

        String currentInstructionSet;
        try {
            currentInstructionSet = getCurrentInstructionSet();
        } catch (Exception e) {
            throw new RuntimeException("getCurrentInstructionSet fail:", e);
        }

        File parentFile = path.getParentFile();
        String fileName = path.getName();
        int index = fileName.lastIndexOf('.');
        if (index > 0) {
            fileName = fileName.substring(0, index);
        }

        String result = parentFile.getAbsolutePath() + "/oat/"
                + currentInstructionSet + "/" + fileName + ".odex";
        return result;
    }

    public static String getCurrentInstructionSet() throws Exception {
        Class<?> clazz = Class.forName("dalvik.system.VMRuntime");
        Method currentGet = clazz.getDeclaredMethod("getCurrentInstructionSet");
        return (String) currentGet.invoke(null);

    }
}
