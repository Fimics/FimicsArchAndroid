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

package com.tencent.tinker.lib.patch;

import android.content.Context;
import android.os.Build;

import com.tencent.tinker.lib.service.PatchResult;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.loader.shareutil.ShareTinkerLog;
import com.tencent.tinker.lib.util.UpgradePatchRetry;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.tencent.tinker.loader.shareutil.SharePatchFileUtil;
import com.tencent.tinker.loader.shareutil.SharePatchInfo;
import com.tencent.tinker.loader.shareutil.ShareSecurityCheck;
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals;

import java.io.File;
import java.io.IOException;
import java.util.Map;


/**
 * generate new patch, you can implement your own patch processor class
 * Created by zhangshaowen on 16/3/14.
 */
public class UpgradePatch extends AbstractPatch {
    private static final String TAG = "Tinker.UpgradePatch";

    @Override
    public boolean tryPatch(Context context, String tempPatchPath, PatchResult patchResult) {
        Tinker manager = Tinker.with(context);

        final File patchFile = new File(tempPatchPath);

        //再一次检查已安装的APP，是否开启了tinkerEnable也就意味着
        //是否可以进行patch操作
        if (!manager.isTinkerEnabled() || !ShareTinkerInternals.isTinkerEnableWithSharedPreferences(context)) {
            ShareTinkerLog.e(TAG, "UpgradePatch tryPatch:patch is disabled, just return");
            return false;
        }

        //检验patch.apk是否存在，是否可读，并且文件内容不为0
        if (!SharePatchFileUtil.isLegalFile(patchFile)) {
            ShareTinkerLog.e(TAG, "UpgradePatch tryPatch:patch file is not found, just return");
            return false;
        }
        //构建检查patch文件信息的对象
        //check the signature, we should create a new checker
        ShareSecurityCheck signatureCheck = new ShareSecurityCheck(context);

        //校验patch包的签名和 本地安装包的签名是否一致
        int returnCode = ShareTinkerInternals.checkTinkerPackage(context, manager.getTinkerFlags(), patchFile, signatureCheck);
        if (returnCode != ShareConstants.ERROR_PACKAGE_CHECK_OK) {
            ShareTinkerLog.e(TAG, "UpgradePatch tryPatch:onPatchPackageCheckFail");
            manager.getPatchReporter().onPatchPackageCheckFail(patchFile, returnCode);
            return false;
        }

        //md5= e08e3479895cae19572e26e799dcabb9
        String patchMd5 = SharePatchFileUtil.getMD5(patchFile);
        if (patchMd5 == null) {
            ShareTinkerLog.e(TAG, "UpgradePatch tryPatch:patch md5 is null, just return");
            return false;
        }
        //use md5 as version
        patchResult.patchVersion = patchMd5;

        ShareTinkerLog.i(TAG, "UpgradePatch tryPatch:patchMd5:%s", patchMd5);

        //check ok, we can real recover a new patch
        ///data/user/0/tinker.sample.android/tinker
        final String patchDirectory = manager.getPatchDirectory().getAbsolutePath();

        //data/user/0/tinker.sample.android/tinker/info.lock
        File patchInfoLockFile = SharePatchFileUtil.getPatchInfoLockFile(patchDirectory);
        ///data/user/0/tinker.sample.android/tinker/patch.info
        File patchInfoFile = SharePatchFileUtil.getPatchInfoFile(patchDirectory);

         //从补丁包patch.apk 的  "assets/package_meta.txt" 读取出以下信息，该文件在打补丁包时生成
        //patchMessage -> tinker is sample to use
        //TINKER_ID -> tinker_id_baseApk-1.0.0
        //patchVersion -> 1.0
        //NEW_TINKER_ID -> tinker_id_patch-1.0.0
        //is_protected_app -> 0
        //platform -> all
        final Map<String, String> pkgProps = signatureCheck.getPackagePropertiesIfPresent();
        if (pkgProps == null) {
            ShareTinkerLog.e(TAG, "UpgradePatch packageProperties is null, do we process a valid patch apk ?");
            return false;
        }

        //判断 是否声明自己是否是个加固的APP
        final String isProtectedAppStr = pkgProps.get(ShareConstants.PKGMETA_KEY_IS_PROTECTED_APP);
        final boolean isProtectedApp = (isProtectedAppStr != null && !isProtectedAppStr.isEmpty() && !"0".equals(isProtectedAppStr));

        //从/data/user/0/tinker.sample.android/tinker/patch.info
        //读取出之前是否已经patch过一次了
        SharePatchInfo oldInfo = SharePatchInfo.readAndCheckPropertyWithLock(patchInfoFile, patchInfoLockFile);

        //it is a new patch, so we should not find a exist
        SharePatchInfo newInfo;

        //already have patch
        if (oldInfo != null) {
            //说明该APP 已经patch 一次了，但是读取到的patch信息 均为空，则不能进行本次patch
            if (oldInfo.oldVersion == null || oldInfo.newVersion == null || oldInfo.oatDir == null) {
                ShareTinkerLog.e(TAG, "UpgradePatch tryPatch:onPatchInfoCorrupted");
                manager.getPatchReporter().onPatchInfoCorrupted(patchFile, oldInfo.oldVersion, oldInfo.newVersion);
                return false;
            }

            //再一次校验补丁文件的md5 是否有效
            if (!SharePatchFileUtil.checkIfMd5Valid(patchMd5)) {
                ShareTinkerLog.e(TAG, "UpgradePatch tryPatch:onPatchVersionCheckFail md5 %s is valid", patchMd5);
                manager.getPatchReporter().onPatchVersionCheckFail(patchFile, oldInfo, patchMd5);
                return false;
            }

            //判断上一次 patch合成是否使用了解释器编译，仅仅只有当系统升级过，tinker在dex优化时，才会使用解释器编译模式
            final boolean usingInterpret = oldInfo.oatDir.equals(ShareConstants.INTERPRET_DEX_OPTIMIZE_PATH);
            //上一次合成的patch的newVersion.equals(patchMd5) 说明这两次是同一个文件
            if (!usingInterpret && !ShareTinkerInternals.isNullOrNil(oldInfo.newVersion) && oldInfo.newVersion.equals(patchMd5) && !oldInfo.isRemoveNewVersion) {
                ShareTinkerLog.e(TAG, "patch already applied, md5: %s", patchMd5);

                // Reset patch apply retry count to let us be able to reapply without triggering
                // patch apply disable when we apply it successfully previously.
                UpgradePatchRetry.getInstance(context).onPatchResetMaxCheck(patchMd5);

                return true;
            }
            // if it is interpret now, use changing flag to wait main process
            // 决定dex2oat的文件目录 interpet
            final String finalOatDir = usingInterpret ? ShareConstants.CHANING_DEX_OPTIMIZE_PATH : oldInfo.oatDir;
            newInfo = new SharePatchInfo(oldInfo.oldVersion, patchMd5, isProtectedApp, false, Build.FINGERPRINT, finalOatDir, false);
        } else {
            // 决定dex2oat的文件目录odex
            newInfo = new SharePatchInfo("", patchMd5, isProtectedApp, false, Build.FINGERPRINT, ShareConstants.DEFAULT_DEX_OPTIMIZE_PATH, false);
        }

        // it is a new patch, we first delete if there is any files
        // don't delete dir for faster retry
        // SharePatchFileUtil.deleteDir(patchVersionDirectory);

        //根据 md5的前8位 当做patch合成文件所在的 目录名和 文件名
        //md5 =e08e3479895cae19572e26e799dcabb9
        //patch-name= patch-e08e3479
        final String patchName = SharePatchFileUtil.getPatchVersionDirectory(patchMd5);

        ///data/user/0/tinker.sample.android/tinker/patch-e08e3479
        final String patchVersionDirectory = patchDirectory + "/" + patchName;

        ShareTinkerLog.i(TAG, "UpgradePatch tryPatch:patchVersionDirectory:%s", patchVersionDirectory);

        //copy file
        ///data/user/0/tinker.sample.android/tinker/patch-e08e3479/patch-e08e3479.apk
        //构建 用于内部存储 patch补丁文件 的file对象
        File destPatchFile = new File(patchVersionDirectory + "/" + SharePatchFileUtil.getPatchVersionFile(patchMd5));

        try {
            // check md5 first
            if (!patchMd5.equals(SharePatchFileUtil.getMD5(destPatchFile))) {
                //文件拷贝。把外部存储卡的patch.apk文件拷贝到内部目录
                SharePatchFileUtil.copyFileUsingStream(patchFile, destPatchFile);
                ShareTinkerLog.w(TAG, "UpgradePatch copy patch file, src file: %s size: %d, dest file: %s size:%d", patchFile.getAbsolutePath(), patchFile.length(),
                    destPatchFile.getAbsolutePath(), destPatchFile.length());
            }
        } catch (IOException e) {
            ShareTinkerLog.e(TAG, "UpgradePatch tryPatch:copy patch file fail from %s to %s", patchFile.getPath(), destPatchFile.getPath());
            manager.getPatchReporter().onPatchTypeExtractFail(patchFile, destPatchFile, patchFile.getName(), ShareConstants.TYPE_PATCH_FILE);
            return false;
        }

        //we use destPatchFile instead of patchFile, because patchFile may be deleted during the patch process
        //这里就开始了patch.apk 和已安装的base.apk中 dex文件的合并工作
        if (!DexDiffPatchInternal.tryRecoverDexFiles(manager, signatureCheck, context, patchVersionDirectory, destPatchFile)) {
            ShareTinkerLog.e(TAG, "UpgradePatch tryPatch:new patch recover, try patch dex failed");
            return false;
        }

        //针对华为 方舟 进行动态库的合并
        if (!ArkHotDiffPatchInternal.tryRecoverArkHotLibrary(manager, signatureCheck,
                context, patchVersionDirectory, destPatchFile)) {
            return false;
        }

        //合并 动态库
        if (!BsDiffPatchInternal.tryRecoverLibraryFiles(manager, signatureCheck, context, patchVersionDirectory, destPatchFile)) {
            ShareTinkerLog.e(TAG, "UpgradePatch tryPatch:new patch recover, try patch library failed");
            return false;
        }

         //合并资源文件
        if (!ResDiffPatchInternal.tryRecoverResourceFiles(manager, signatureCheck, context, patchVersionDirectory, destPatchFile)) {
            ShareTinkerLog.e(TAG, "UpgradePatch tryPatch:new patch recover, try patch resource failed");
            return false;
        }

        // check dex opt file at last, some phone such as VIVO/OPPO like to change dex2oat to interpreted
        if (!DexDiffPatchInternal.waitAndCheckDexOptFile(patchFile, manager)) {
            ShareTinkerLog.e(TAG, "UpgradePatch tryPatch:new patch recover, check dex opt file failed");
            return false;
        }
        //合并成功后 把补丁文件patch.apk 的文件信息  写入本地，在重启后动态加载时，会用到
        if (!SharePatchInfo.rewritePatchInfoFileWithLock(patchInfoFile, newInfo, patchInfoLockFile)) {
            ShareTinkerLog.e(TAG, "UpgradePatch tryPatch:new patch recover, rewrite patch info failed");
            manager.getPatchReporter().onPatchInfoCorrupted(patchFile, newInfo.oldVersion, newInfo.newVersion);
            return false;
        }

        // Reset patch apply retry count to let us be able to reapply without triggering
        // patch apply disable when we apply it successfully previously.
        UpgradePatchRetry.getInstance(context).onPatchResetMaxCheck(patchMd5);

        ShareTinkerLog.w(TAG, "UpgradePatch tryPatch: done, it is ok");
        return true;
    }

}
