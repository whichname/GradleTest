package com.example.jimi_wu.test;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.jimi_wu.tinkerlib.TinkerManager;
import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.loader.shareutil.ShareConstants;

@DefaultLifeCycle(application = "com.example.jimi_wu.test.TestApplication",
        flags = ShareConstants.TINKER_ENABLE_ALL,
        loadVerifyFlag = false)
public class TinkerApplicationLike extends com.tencent.tinker.loader.app.ApplicationLike {

    public TinkerApplicationLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }

    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        TinkerManager.installTinker(this);
    }

}
