/*
 * Tencent is pleased to support the open source community by making Tinker available.
 *
 * Copyright (C) 2016 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tencent.tinker.lib.listener;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;

import com.tencent.tinker.lib.service.TinkerPatchForeService;
import com.tencent.tinker.lib.service.TinkerPatchService;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.tinker.TinkerLoadResult;
import com.tencent.tinker.lib.util.TinkerServiceInternals;
import com.tencent.tinker.lib.util.UpgradePatchRetry;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.tencent.tinker.loader.shareutil.SharePatchFileUtil;
import com.tencent.tinker.loader.shareutil.SharePatchInfo;
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals;

import java.io.File;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by zhangshaowen on 16/3/14.
 */
public class DefaultPatchListener implements PatchListener {
    protected final Context context;
    private ServiceConnection connection;

    public DefaultPatchListener(Context context) {
        this.context = context;
    }

    /**
     * when we receive a patch, what would we do?
     * you can overwrite it
     *
     * @param path
     * @return
     */
    @Override
    public int onPatchReceived(String path) {
        final File patchFile = new File(path);
        final String patchMD5 = SharePatchFileUtil.getMD5(patchFile);
        //1. 判断当前安装包是否开启tinkerEnable,也就代表是否可以进行patch操作
        //2. 判断patch文件是否存在
        //3. 判断当前运行是否在主进程
        //4. 判断当前patchService服务进程是否运行中
        //5. 判断当前设备是否开启了davik jit 即时编译模式
        //6. 判断当前patch是否已经合成过了
        //7. 判断当前pacth合成失败，是否以达到最大次数
        int returnCode = patchCheck(path, patchMD5);
        //强行返回OK，否则会因为检查不通过，导致无法debug跟进流程
        returnCode = ShareConstants.ERROR_PATCH_OK;
        if (returnCode == ShareConstants.ERROR_PATCH_OK) {
            runForgService();
            TinkerPatchService.runPatchService(context, path);
        } else {
            Tinker.with(context).getLoadReporter().onLoadPatchListenerReceiveFail(new File(path), returnCode);
        }
        return returnCode;
    }


    private void runForgService() {
        try {
            connection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    if (context != null && connection != null) {
                        try {
                            //Tinker在完成补丁后会尝试kill掉patch进程，如果不unbind会导致patch进程重启
                            context.unbindService(connection);
                        } catch (Throwable ignored) {
                            // Ignored.
                        }
                    }
                }

                @Override
                public void onBindingDied(ComponentName name) {
                }
            };
            Intent innerForgIntent = new Intent(context, TinkerPatchForeService.class);
            context.bindService(innerForgIntent, connection, BIND_AUTO_CREATE);
        } catch (Throwable ex) {
            //ignore forground service start error
        }
    }

    protected int patchCheck(String path, String patchMd5) {
        final Tinker manager = Tinker.with(context);
        //check SharePreferences also
        if (!manager.isTinkerEnabled() || !ShareTinkerInternals.isTinkerEnableWithSharedPreferences(context)) {
            return ShareConstants.ERROR_PATCH_DISABLE;
        }
        if (TextUtils.isEmpty(patchMd5)) {
            return ShareConstants.ERROR_PATCH_NOTEXIST;
        }
        final File file = new File(path);
        if (!SharePatchFileUtil.isLegalFile(file)) {
            return ShareConstants.ERROR_PATCH_NOTEXIST;
        }

        //patch service can not send request
        if (manager.isPatchProcess()) {
            return ShareConstants.ERROR_PATCH_INSERVICE;
        }

        //if the patch service is running, pending
        if (TinkerServiceInternals.isTinkerPatchServiceRunning(context)) {
            return ShareConstants.ERROR_PATCH_RUNNING;
        }
        if (ShareTinkerInternals.isVmJit()) {
            return ShareConstants.ERROR_PATCH_JIT;
        }

        final TinkerLoadResult loadResult = manager.getTinkerLoadResultIfPresent();
        // only call repair on main process
        final boolean repairOptNeeded = manager.isMainProcess()
                && loadResult != null && loadResult.useInterpretMode;

        if (!repairOptNeeded) {
            if (manager.isTinkerLoaded() && loadResult != null) {
                String currentVersion = loadResult.currentVersion;
                if (patchMd5.equals(currentVersion)) {
                    return ShareConstants.ERROR_PATCH_ALREADY_APPLY;
                }
            }

            // Hit if we have already applied patch but main process did not restart.
            final String patchDirectory = manager.getPatchDirectory().getAbsolutePath();
            File patchInfoLockFile = SharePatchFileUtil.getPatchInfoLockFile(patchDirectory);
            File patchInfoFile = SharePatchFileUtil.getPatchInfoFile(patchDirectory);
            try {
                final SharePatchInfo currInfo = SharePatchInfo.readAndCheckPropertyWithLock(patchInfoFile, patchInfoLockFile);
                if (currInfo != null && !ShareTinkerInternals.isNullOrNil(currInfo.newVersion) && !currInfo.isRemoveNewVersion) {
                    if (patchMd5.equals(currInfo.newVersion)) {
                        return ShareConstants.ERROR_PATCH_ALREADY_APPLY;
                    }
                }
            } catch (Throwable ignored) {
                // Ignored.
            }
        }

        if (!UpgradePatchRetry.getInstance(context).onPatchListenerCheck(patchMd5)) {
            return ShareConstants.ERROR_PATCH_RETRY_COUNT_LIMIT;
        }

        return ShareConstants.ERROR_PATCH_OK;
    }

}
