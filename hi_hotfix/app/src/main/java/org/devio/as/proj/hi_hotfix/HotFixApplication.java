package org.devio.as.proj.hi_hotfix;

import android.app.Application;
import android.content.Context;
import android.os.Debug;
import android.os.Environment;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

public class HotFixApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        File source = new File(Environment.getExternalStorageDirectory(), "patch.dex");
        File dest = new File(base.getApplicationInfo().dataDir, "patch.dex");
        FileUtil.copyFile(source, dest);

        try {
            HotFix.fix(base, dest.getAbsolutePath());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
