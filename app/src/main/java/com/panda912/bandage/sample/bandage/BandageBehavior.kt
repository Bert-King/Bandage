package com.panda912.bandage.sample.bandage

import android.util.Log
import com.panda912.bandage.interfaces.IBandageBehavior
import com.panda912.bandage.internal.data.DynamicBandageData
import com.panda912.bandage.utils.ActivityManager

/**
 * Created by panda on 2021/12/16 10:48
 */

private const val TAG = "BandageBehavior"

class BandageBehavior : IBandageBehavior {

    override fun loadPatch() {

    }

    override fun guideUpgrade(
        guideUpgradeData: DynamicBandageData.GuideUpgradeData,
        closeCurActivity: Boolean,
    ) {

    }

    override fun startRouter(router: String) {

    }

    override fun finishCurActivity(throwable: Throwable) {
        val activity = ActivityManager.getInstance().getCurActivity()
        if (!ActivityManager.getInstance().isDestroyed(activity)) {
            activity?.finish()
            return
        }
        Log.w(TAG, "can not finish fatal activity: $activity", throwable)
    }

    override fun uploadCrash(throwable: Throwable) {
        println("uploadCrash(): " + Log.getStackTraceString(throwable))
    }
}