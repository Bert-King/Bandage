package com.panda912.bandage.internal.hook.activity_thread_hook

import android.annotation.SuppressLint
import android.os.Build
import android.os.Handler
import android.util.SparseArray
import com.panda912.bandage.internal.hook.activity_thread_hook.processors.FixActivityCrashProcessor
import com.panda912.bandage.internal.hook.activity_thread_hook.processors.FixMsg2StrCrashProcessor
import com.panda912.bandage.internal.hook.activity_thread_hook.processors.FixSpAnrProcessor
import com.panda912.bandage.internal.hook.activity_thread_hook.processors.OriginCallbackProcessor
import com.panda912.bandage.internal.hook.activity_thread_hook.processors.UiMsgInterceptor
import com.panda912.bandage.internal.BandageLogger
import java.lang.reflect.Field

/**
 * ActivityThreadHandlerHooker
 * 功能1：通过反射获取到ActivityThread单例对象
 */
internal object ActivityThreadHandlerHooker {
    private const val TAG = "ActivityThreadHandlerHooker"

    /**
     * 获取ActivityThread类的静态方法currentActivityThread的返回值
     */
    private val activityThreadClass by lazy { Class.forName("android.app.ActivityThread") }

    /**
     * 反射 获取ActivityThread类的currentActivityThread方法的返回值
     */
    private val activityThreadInstance by lazy {
        activityThreadClass.getDeclaredMethod("currentActivityThread").invoke(null)!!
    }

    /**
     * 用于卸载ActivityThreadHandlerCallback的函数
     */
    private var uninstallActivityThreadHandlerCallback: (() -> Unit)? = null

    /**
     * 开始Hook的ActivityThread的Handler
     */
    fun hook() {
        check(uninstallActivityThreadHandlerCallback == null) {
            "ActivityThreadHandlerHooker already hooked"
        }
        try {
            swapActivityThreadHandlerCallback { mH, mCallback ->
                uninstallActivityThreadHandlerCallback = {
                    swapActivityThreadHandlerCallback { _, _ ->
                        mCallback
                    }
                }
                val processors = listOf(
                    UiMsgInterceptor(),
                    FixSpAnrProcessor(),
                    FixMsg2StrCrashProcessor(),
                    OriginCallbackProcessor(mCallback),
                    FixActivityCrashProcessor(mH, getFixMessages())
                )
                Handler.Callback { msg ->
                    if (msg == null) {
                        return@Callback false
                    }
                    val chain = MessageProcessorChain(processors, 0, msg)
                    return@Callback chain.proceed(msg)
                }
            }
        } catch (th: Throwable) {
            BandageLogger.w(TAG, "ActivityThreadHandlerHooker init failure.", th)
        }
    }

    /**
     * 卸载ActivityThreadHandlerCallback的函数
     */
    private fun unhook() {
        uninstallActivityThreadHandlerCallback?.invoke()
        uninstallActivityThreadHandlerCallback = null
    }

    /**
     * 用于替换系统ActivityThread的Handler的函数
     * @param swap 用于替换系统ActivityThread的Handler的函数
     */
    private fun swapActivityThreadHandlerCallback(swap: (Handler, Handler.Callback?) -> Handler.Callback?) {
        val mHField: Field = activityThreadClass.getDeclaredField("mH").apply { isAccessible = true }
        val mH = mHField[activityThreadInstance] as Handler

        val mCallbackField =
            Handler::class.java.getDeclaredField("mCallback").apply { isAccessible = true }
        val mCallback = mCallbackField[mH] as? Handler.Callback
        mCallbackField[mH] = swap(mH, mCallback)
    }

    /**
     * 获取修复消息的SparseArray
     * @return 修复消息的SparseArray，如果不存在则返回null
     */
    private fun getFixMessages(): SparseArray<ActivityThreadFixMessage>? {
        val msgs = SparseArray<ActivityThreadFixMessage>()
        val sdkInt = Build.VERSION.SDK_INT
        if (sdkInt >= Build.VERSION_CODES.P) {
            msgs.put(110, ActivityThreadFixMessage(110, "BIND_APPLICATION"))
            msgs.put(134, ActivityThreadFixMessage(134, "SCHEDULE_CRASH"))
            msgs.put(137, ActivityThreadFixMessage(137, "SLEEPING"))
            msgs.put(143, ActivityThreadFixMessage(143, "REQUEST_ASSIST_CONTEXT_EXTRAS"))
            if (sdkInt <= 30) {
                msgs.put(159, ActivityThreadFixMessage(159, "EXECUTE_TRANSACTION"))
            }
        } else {
            msgs.put(100, ActivityThreadFixMessage(100, "LAUNCH_ACTIVITY"))
            msgs.put(101, ActivityThreadFixMessage(101, "PAUSE_ACTIVITY"))
            msgs.put(103, ActivityThreadFixMessage(103, "STOP_ACTIVITY_SHOW"))
            msgs.put(104, ActivityThreadFixMessage(104, "STOP_ACTIVITY_HIDE"))
            msgs.put(107, ActivityThreadFixMessage(107, "RESUME_ACTIVITY"))
            msgs.put(109, ActivityThreadFixMessage(109, "DESTROY_ACTIVITY"))
            msgs.put(110, ActivityThreadFixMessage(110, "BIND_APPLICATION"))
            msgs.put(134, ActivityThreadFixMessage(134, "SCHEDULE_CRASH"))
            msgs.put(137, ActivityThreadFixMessage(137, "SLEEPING"))
            msgs.put(143, ActivityThreadFixMessage(143, "REQUEST_ASSIST_CONTEXT_EXTRAS"))
        }

        return if (msgs.size() != 0) filterValidMessages(msgs) else null
    }

    /**
     * 过滤有效的消息的函数
     * @param msgs 消息的SparseArray
     * @return 过滤后的有效消息的SparseArray，如果不存在则返回null
     */
    private fun filterValidMessages(msgs: SparseArray<ActivityThreadFixMessage>): SparseArray<ActivityThreadFixMessage> {
        val size = msgs.size()
        val validMsgs = SparseArray<ActivityThreadFixMessage>(size)
        for (i in 0 until size) {
            var message: ActivityThreadFixMessage? = null
            try {
                message = msgs.valueAt(i)
                val clazz = Class.forName("android.app.ActivityThread\$H")
                val code = clazz.getDeclaredField(message.msgName).get(clazz) as Int
                message.msgId = code
                validMsgs.put(code, message)
            } catch (th: Throwable) {
                BandageLogger.w(TAG, "check $message fail", th)
            }
        }
        return validMsgs
    }

}
