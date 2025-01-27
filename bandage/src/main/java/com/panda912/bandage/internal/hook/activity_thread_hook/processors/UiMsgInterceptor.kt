package com.panda912.bandage.internal.hook.activity_thread_hook.processors

import android.os.Build
import android.os.Bundle
import android.os.ParcelFileDescriptor
import com.android.internal.os.SomeArgs
import com.panda912.bandage.internal.hook.activity_thread_hook.Processor
import com.panda912.bandage.internal.BandageLogger
import java.io.IOException

/**
 * 内部类，UiMsgInterceptor处理器
 */
internal class UiMsgInterceptor : Processor {
    companion object {
        private const val TAG = "UiMsgInterceptor"
    }

    /**
     * 处理方法，用于处理Processor链路
     * @param chain 处理链路
     * @return 处理结果
     */
    override fun process(chain: Processor.Chain): Boolean {
        val message = chain.input()

        if (!"huawei".equals(Build.MANUFACTURER, true) || message.obj == null) {
            return chain.proceed(message)
        }

        if (message.obj is SomeArgs) {
            val someArgs = message.obj as SomeArgs
            if (someArgs.arg2 !is Bundle) {
                return chain.proceed(message)
            }
            val bundle = someArgs.arg2 as? Bundle
            if (bundle?.containsKey("IGrabNodeReceiver") != true) {
                return chain.proceed(message)
            }
            BandageLogger.i(TAG, "huawei is grabing node.")
            closeFD(bundle)
            return true
        } else if (message.obj.javaClass.name != "android.app.ActivityThread\$RequestContentNode") {
            return chain.proceed(message)
        } else {
            message.obj = null
            message.what = -1
            BandageLogger.i(TAG, "huawei is grabing node.")
            return true
        }
    }

    /**
     * 关闭文件描述符
     * @param bundle 文件描述符bundle
     */
    private fun closeFD(bundle: Bundle?) {
        if (bundle?.containsKey("read_file_descripter") == true) {
            val obj = bundle.get("read_file_descripter")
            if (obj is ParcelFileDescriptor) {
                try {
                    obj.close()
                } catch (e: IOException) {
                    BandageLogger.w(TAG, "close fd exception: ${e.message}")
                }
            }
        }
    }
}
