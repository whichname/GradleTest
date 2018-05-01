package com.jimi_wu.tinkerlib;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.tencent.tinker.loader.app.ApplicationLike;

/**
 * @DefaultLifeCycle(application = "com.jimi_wu.tinkerlib.MyApplication",
 *  flags = ShareConstants.TINKER_ENABLE_ALL,
 *  loadVerifyFlag = false)
 */
public class SampleApplicationLike extends ApplicationLike {


    public SampleApplicationLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }

    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        TinkerManager.installTinker(this);
    }


}
