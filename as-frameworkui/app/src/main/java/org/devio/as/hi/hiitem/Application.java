package org.devio.as.hi.hiitem;

import com.android.arc.demo.utils.ActivityManager;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ActivityManager.getInstance().init(this);
    }
}
