package org.devio.as.proj.biz_home;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.os.TraceCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import org.devio.as.proj.ability.share.ShareManager;
import org.devio.as.proj.common.BuildConfig;
import org.devio.as.proj.common.rn.HiRNCacheManager;
import org.devio.as.proj.common.ui.component.HiBaseActivity;
import org.devio.hi.library.aspectj.MethodTrace;
import org.devio.hi.library.util.HiStatusBar;
import org.devio.hi.library.util.HiViewUtil;
import org.devio.hi.library.util.PermissionConstants;
import org.devio.hi.library.util.PermissionUtil;

import java.util.List;


public class MainActivity extends HiBaseActivity implements MainActivityLogic.ActivityProvider {
    private MainActivityLogic activityLogic;

    @MethodTrace
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        TraceCompat.beginSection("MainActivity_onCreate");
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        activityLogic = new MainActivityLogic(this, savedInstanceState);

        HiStatusBar.INSTANCE.setStatusBar(this, true, Color.WHITE, false);
        //preLoadRN();

        TraceCompat.endSection();


        PermissionUtil.Companion.permission(PermissionConstants.STORAGE, PermissionConstants.PHONE)
                .callback(new PermissionUtil.FullCallback() {
                    @Override
                    public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                        showToast("被拒了,请查看那些被永久拒绝，那些是临时拒绝");
                    }

                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                        showToast("测试权限申请框架,代码在Hi-library，请自行查看代码说明");
                    }
                }).rationale(shouldRequest -> {
            new AlertDialog.Builder(MainActivity.this).setMessage("权限申请被多次拒绝过，可是我们需要该权限，可以弹窗说明").setPositiveButton("可以授权", (dialog, which) -> shouldRequest.again(true)).show();
        }).request();

    }

    @MethodTrace
    private void preLoadRN() {
        Bundle bundle = new Bundle();
        bundle.putString("routeTo", "/browsing");
        HiRNCacheManager.getInstance().preLoad(this, HiRNCacheManager.MODULE_NAME_BROWSING, bundle);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            //音量下键点击事件
            if (BuildConfig.DEBUG) {
                try {
                    Class<?> aClass = Class.forName("org.devio.as.proj.debug.DebugToolDialogFragment");
                    DialogFragment target = (DialogFragment) aClass.getConstructor().newInstance();
                    target.show(getSupportFragmentManager(), "debug_tool");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        activityLogic.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @MethodTrace
    @Override
    protected void onResume() {
        TraceCompat.beginSection("MainActivity_onResume");
        super.onResume();
        TraceCompat.endSection();
    }

    @MethodTrace
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        TraceCompat.beginSection("MainActivity_onWindowFocusChanged");
        super.onWindowFocusChanged(hasFocus);
        TraceCompat.endSection();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        activityLogic.getHiTabBottomLayout().resizeHiTabBottomLayout();
        //(newConfig.uiMode&Configuration.UI_MODE_NIGHT_YES)!=0
        if (HiViewUtil.lightMode()) {
            recreate();
        } else {
            recreate();
        }
    }
}
