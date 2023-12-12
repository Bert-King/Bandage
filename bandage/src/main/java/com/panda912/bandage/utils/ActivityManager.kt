package com.panda912.bandage.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.panda912.bandage.internal.BandageLogger
import java.lang.ref.SoftReference
import java.util.concurrent.CopyOnWriteArrayList

/**
 * 收集Activity信息
 *
 * 思考：这里为什么要用SoftReference？
 *
 * 思考：这里为什么要用CopyOnWriteArrayList？
 * 在并发情况下，性能更好
 *
 * Android获取当前的Activty
 * https://blog.csdn.net/Jason_HD/article/details/81702126
 *
 */

class ActivityManager : Application.ActivityLifecycleCallbacks {

    companion object {
        const val TAG = "ActivityManager"

        @Volatile
        private var instance: ActivityManager? = null

        fun getInstance(): ActivityManager =
            instance ?: synchronized(this) {
                instance ?: ActivityManager().also { instance = it }
            }

    }
    // CopyOnWriteArrayList是基于ArrayList实现的，好处是能够保证并行操作的安全性
    private val activityList = CopyOnWriteArrayList<SoftReference<Activity>>()

    fun getCurActivity(): Activity? {
        return activityList.lastOrNull()?.get()
    }

    fun isDestroyed(activity: Activity?): Boolean =
        activity == null || activity.isFinishing || activity.isDestroyed

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        BandageLogger.i(TAG, "onActivityCreated: $activity")
        activityList.add(SoftReference(activity))
    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        for (softReference in activityList) {
            if (softReference.get() === activity) {
                activityList.remove(softReference)
                return
            }
        }
    }
}
