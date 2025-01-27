package com.panda912.bandage.internal.hook.activity_thread_hook.processors

import android.os.Handler
import com.panda912.bandage.internal.BandageLogger
import com.panda912.bandage.internal.hook.activity_thread_hook.Processor

/**
 * Created by panda on 2021/12/6 17:05
 */
internal class OriginCallbackProcessor(private val callback: Handler.Callback?) : Processor {
    companion object {
        private const val TAG = "OriginCallbackProcessor"
    }

    override fun process(chain: Processor.Chain): Boolean {
        val message = chain.input()
        if (callback != null) {
            try {
                if (callback.handleMessage(message)) {
                    return true
                }
            } catch (th: Throwable) {
                BandageLogger.w(TAG, "origin callback handle message error", th)
            }
        }

        return chain.proceed(message)
    }
}