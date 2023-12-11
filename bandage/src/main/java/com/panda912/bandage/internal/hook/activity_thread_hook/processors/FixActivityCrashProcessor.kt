package com.panda912.bandage.internal.hook.activity_thread_hook.processors

import android.app.Activity
import android.os.Handler
import android.os.Message
import android.util.SparseArray
import com.panda912.bandage.internal.hook.activity_thread_hook.ActivityThreadFixMessage
import com.panda912.bandage.internal.BandageLogger
import com.panda912.bandage.internal.hook.activity_thread_hook.Processor
import com.panda912.bandage.utils.ActivityManager
import com.panda912.bandage.utils.isNonexistent
import com.panda912.bandage.utils.isOutOfMemoryError


internal const val RESUME_ACTIVITY = "RESUME_ACTIVITY"
internal const val PAUSE_ACTIVITY = "PAUSE_ACTIVITY"
internal const val STOP_ACTIVITY_SHOW = "STOP_ACTIVITY_SHOW"
internal const val STOP_ACTIVITY_HIDE = "STOP_ACTIVITY_HIDE"
internal const val EXECUTE_TRANSACTION = "EXECUTE_TRANSACTION"

/**
 * 处理Activity的Crash问题
 *
 * 如果发生Crash，则结束 该Activity
 *
 */
internal class FixActivityCrashProcessor(
    private val mH: Handler?,
    private val msgs: SparseArray<ActivityThreadFixMessage>?,
) : Processor {
    companion object {
        private const val TAG = "FixActivityCrashProcessor"
    }

    override fun process(chain: Processor.Chain): Boolean {
        if (mH == null) {
            return false
        }

        val message: Message = chain.input()
        val fixMessage = msgs?.get(message.what) ?: return false

        try {
            mH.handleMessage(message)
        } catch (th: Throwable) {
            if (th.isOutOfMemoryError()) {
                throw th
            } else {
                finishCrashActivity(fixMessage, th)
            }
        }

        return true
    }

    private fun finishCrashActivity(message: ActivityThreadFixMessage, th: Throwable) {
        when (message.msgName) {
            RESUME_ACTIVITY,
            PAUSE_ACTIVITY,
            STOP_ACTIVITY_SHOW,
            STOP_ACTIVITY_HIDE,
            EXECUTE_TRANSACTION,
            -> {
                BandageLogger.w(TAG, "finish fatal activity.", th)
                val activity: Activity? = ActivityManager.getInstance().getCurActivity()
                if (!activity.isNonexistent()) {
                    activity?.finish()
                }
            }
        }
    }
}