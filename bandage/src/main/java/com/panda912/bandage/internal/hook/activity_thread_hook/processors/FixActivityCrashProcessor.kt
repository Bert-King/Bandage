package com.panda912.bandage.internal.hook.activity_thread_hook.processors

import android.app.Activity
import android.os.Handler
import android.os.Message
import android.util.SparseArray
import com.panda912.bandage.internal.hook.activity_thread_hook.ActivityThreadFixMessage
import com.panda912.bandage.internal.BandageLogger
import com.panda912.bandage.internal.hook.activity_thread_hook.Processor
import com.panda912.bandage.utils.ActivityManager
import com.panda912.bandage.utils.isOutOfMemoryError


/**
 * Created by panda on 2021/12/6 17:04
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
        val msgName = message.msgName

        when (msgName) {
            "RESUME_ACTIVITY",
            "PAUSE_ACTIVITY",
            "STOP_ACTIVITY_SHOW",
            "STOP_ACTIVITY_HIDE",
            "EXECUTE_TRANSACTION" -> {
                BandageLogger.w(TAG, "finish fatal activity.", th)
                val activity: Activity? = ActivityManager.getInstance().getCurActivity()
                if (!ActivityManager.getInstance().isDestroyed(activity)) {
                    activity?.finish()
                }
            }
        }
    }
}