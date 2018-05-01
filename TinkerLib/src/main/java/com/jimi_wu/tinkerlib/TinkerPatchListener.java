package com.jimi_wu.tinkerlib;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;

import com.tencent.tinker.lib.listener.DefaultPatchListener;
import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.tencent.tinker.loader.shareutil.SharePatchFileUtil;

import java.io.File;

/**
 * Description:过滤Tinker收到的补丁包的修复、升级请求
 */
public class TinkerPatchListener extends DefaultPatchListener {

    private static final String TAG = "Tinker.TinkerPatchListener";

    protected static final long NEW_PATCH_RESTRICTION_SPACE_SIZE_MIN = 60 * 1024 * 1024;

    private final int maxMemory;

    public TinkerPatchListener(Context context) {
        super(context);
        maxMemory = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        TinkerLog.i(TAG, "application maxMemory:" + maxMemory);
    }

    /**
     * 检查是否要唤起:patch进程去尝试补丁合成
     * <p>
     * 若检查成功，我们会调用TinkerPatchService.runPatchService唤起:patch进程，去尝试完成补丁合成操作。反之，会回调检验失败的接口。
     * 事实上，你只需要复写patchCheck函数即可。若检查失败，会在LoadReporter的onLoadPatchListenerReceiveFail中回调
     *
     * @param path 补丁路径
     * @return
     */
    @Override
    public int patchCheck(String path, String patchMd5) {
        File patchFile = new File(path);
        TinkerLog.i(TAG, "receive a patch file: %s, file size:%d", path, SharePatchFileUtil.getFileOrDirectorySize(patchFile));
        int returnCode = super.patchCheck(path, patchMd5);

        if (returnCode == ShareConstants.ERROR_PATCH_OK) {
            returnCode = TinkerUtils.checkForPatchRecover(NEW_PATCH_RESTRICTION_SPACE_SIZE_MIN, maxMemory);
        }

        if (returnCode == ShareConstants.ERROR_PATCH_OK) {
            SharedPreferences sp = context.getSharedPreferences(ShareConstants.TINKER_SHARE_PREFERENCE_CONFIG, Context.MODE_MULTI_PROCESS);
            //optional, only disable this patch file with md5
            int fastCrashCount = sp.getInt(patchMd5, 0);
            if (fastCrashCount >= TinkerUncaughtExceptionHandler.MAX_CRASH_COUNT) {
                returnCode = TinkerUtils.ERROR_PATCH_CRASH_LIMIT;
            }
        }
        // Warning, it is just a sample case, you don't need to copy all of these
        // Interception some of the request

//        检查platform，不需要
//        if (returnCode == ShareConstants.ERROR_PATCH_OK) {
//            Properties properties = ShareTinkerInternals.fastGetPatchPackageMeta(patchFile);
//            if (properties == null) {
//                returnCode = TinkerUtils.ERROR_PATCH_CONDITION_NOT_SATISFIED;
//            } else {
//                String platform = properties.getProperty(TinkerUtils.PLATFORM);
//                TinkerLog.i(TAG, "get platform:" + platform);
//                // check patch platform require
//                if (platform == null) {
//                    returnCode = TinkerUtils.ERROR_PATCH_CONDITION_NOT_SATISFIED;
//                }
//            }
//        }

        TinkerReport.onTryApply(returnCode == ShareConstants.ERROR_PATCH_OK);
        return returnCode;
    }
}