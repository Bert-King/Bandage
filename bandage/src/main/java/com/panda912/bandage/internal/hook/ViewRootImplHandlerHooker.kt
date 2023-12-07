package com.panda912.bandage.internal.hook

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import com.panda912.bandage.internal.BandageLogger

/**
 * https://android.googlesource.com/platform/frameworks/base/+/525caa44ceda39cf5bc0823f1ef293865b5a5e30%5E%21/#F0
 *
 */
internal object ViewRootImplHandlerHooker : Application.ActivityLifecycleCallbacks {
    private const val TAG = "ViewRootImplHandlerHooker"

    /**
     * 触发 ViewRootImplHandlerHooker 的 hook 方法
     * @param application 应用的 Application 对象
     */
    fun hook(application: Application) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // == enough?
            application.registerActivityLifecycleCallbacks(this)
        }
    }

    /**
     * 活动开始
     * @param activity 活动的 Activity 对象
     */
    override fun onActivityStarted(activity: Activity) {
        activity.window.decorView.addOnAttachStateChangeListener(object :
            View.OnAttachStateChangeListener {
            /**
             * 视图附加到窗口上的时候调用
             * @param v 视图对象
             */
            override fun onViewAttachedToWindow(v: View) {
                try {
                    val handler = v.handler
                    if (handler != null) {
                        val field = Handler::class.java.getDeclaredField("mCallback")
                            .apply { isAccessible = true }
                        val callback = field.get(handler)
                        if (callback == null || callback.javaClass.name != ProtectViewRootHandlerCallback::class.java.name) {
                            field.set(handler, ProtectViewRootHandlerCallback(handler))
                            BandageLogger.i(TAG, "ViewRootImpl handler hook successful.")
                        }
                    }
                } catch (th: Throwable) {
                    BandageLogger.w(TAG, "ViewRootImpl handler hook failed.", th)
                }
            }

            /**
             * 视图从窗口上移除的时候调用
             * @param v 视图对象
             */
            override fun onViewDetachedFromWindow(v: View) {

            }
        })
    }

    /**
     * 活动创建
     * @param activity 活动的 Activity 对象
     * @param savedInstanceState 保存的实例状态数据的 Bundle 对象
     */
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    /**
     * 活动恢复
     * @param activity 活动的 Activity 对象
     */
    override fun onActivityResumed(activity: Activity) {

    }

    /**
     * 活动暂停
     * @param activity 活动的 Activity 对象
     */
    override fun onActivityPaused(activity: Activity) {

    }

    /**
     * 活动停止
     * @param activity 活动的 Activity 对象
     */
    override fun onActivityStopped(activity: Activity) {

    }

    /**
     * 活动保存实例状态
     * @param activity 活动的 Activity 对象
     * @param outState 保存实例状态数据的 Bundle 对象
     */
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    /**
     * 活动销毁
     * @param activity 活动的 Activity 对象
     */
    override fun onActivityDestroyed(activity: Activity) {

    }

    /**
     * ViewRootImpl Handler 回调的保护类
     */
    class ProtectViewRootHandlerCallback(private val handler: Handler) : Handler.Callback {

        /**
         * 处理消息
         * @param msg 消息对象
         * @return 消息是否被处理
         */
        override fun handleMessage(msg: Message): Boolean {
            try {
                handler.handleMessage(msg)
            } catch (th: Throwable) {
                BandageLogger.w(TAG, "handle message occur error, message: $msg", th)
            }
            return true
        }
    }
}
