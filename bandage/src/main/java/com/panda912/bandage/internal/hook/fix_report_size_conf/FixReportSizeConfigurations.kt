package com.panda912.bandage.internal.hook.fix_report_size_conf

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.os.Build
import com.panda912.bandage.internal.BandageLogger
import java.lang.reflect.Proxy

/**
 * 固定bug报告的尺寸配置
 */
internal object FixReportSizeConfigurations {
    private const val TAG = "FixReportSizeConfigurations"

    @SuppressLint("PrivateApi", "DiscouragedPrivateApi")
    fun hook() {
        // 如果Android版本在26到28之间
        if (Build.VERSION.SDK_INT in 26..28) {
            try {
                // 获取android.util.Singleton类
                val SingletonCls = Class.forName("android.util.Singleton")
                // 获取android.app.ActivityManager中的IActivityManagerSingleton字段
                val IActivityManagerSingleton = ActivityManager::class.java.getDeclaredField("IActivityManagerSingleton")
                    .apply {
                        isAccessible = true
                    }.get(null)
                // 如果IActivityManagerSingleton为null
                if (IActivityManagerSingleton == null) {
                    BandageLogger.w(TAG, "IActivityManagerSingleton is null")
                    return
                }
                // 获取SingletonCls中的mInstance字段
                val mInstanceField = SingletonCls.getDeclaredField("mInstance").apply {
                    isAccessible = true
                }
                // 获取iActivityManager值
                val iActivityManager = mInstanceField.get(IActivityManagerSingleton)
                // 如果iActivityManager为null
                if (iActivityManager == null) {
                    BandageLogger.w(TAG, "iActivityManager is null")
                    return
                }

                // 获取android.app.IActivityManager类
                val IActivityManagerCls = Class.forName("android.app.IActivityManager")
                // 设置IActivityManagerSingleton的mInstance字段为一个代理实例
                mInstanceField.set(
                    IActivityManagerSingleton,
                    Proxy.newProxyInstance(
                        IActivityManagerCls.classLoader,
                        arrayOf(IActivityManagerCls),
                        IActivityManagerProxy(iActivityManager)
                    )
                )
                BandageLogger.w(TAG, "hook IActivityManagerProxy success")
            } catch (th: Throwable) {
                BandageLogger.w(TAG, "hook IActivityManagerProxy failed", th)
            }
        }
    }
}
