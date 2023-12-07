package com.panda912.bandage.internal.dynamic

import android.os.SystemClock
import com.panda912.bandage.Bandage
import com.panda912.bandage.internal.data.DynamicBandageData
import com.panda912.bandage.internal.data.removeMatchedCause
import com.panda912.bandage.internal.BandageLogger
import kotlin.math.min

/**
 * 动态补丁数据匹配器
 */
internal object BandageDataMatcher {

    private const val TAG = "BandageDataMatcher"

    /**
     * 判断异常信息是否匹配
     * @param data 动态补丁数据
     * @param th 异常对象
     * @return 如果匹配返回true，否则返回false
     */
    fun isStackMatch(data: DynamicBandageData, th: Throwable): Boolean {
        if (data.stacks.isEmpty()) {
            return false
        }

        val startRecordTime = SystemClock.elapsedRealtime()

        val causes = getThrowableCauses(th)

        val dataStacks = data.stacks.toMutableList()
        var dataCauses: ArrayList<DynamicBandageData.ExceptionMatch>? = null
        if (data.causes?.isNotEmpty() == true) {
            dataCauses = ArrayList(data.causes)
        }

        for (i in 0 until min(causes.size, 10)) {
            val cause = causes[i]
            if (i > 0 && dataCauses != null) {
                removeMatchedCause(dataCauses, cause)
            }
            val stackTrace = cause.stackTrace
            for (j in 0 until min(stackTrace.size, 40)) {
                if (dataStacks.isEmpty()) {
                    break
                }
                val element = stackTrace[j]
                dataStacks.remove("${element.className}.${element.methodName}")
            }
        }

        BandageLogger.i(
            TAG,
            "stacks size: ${dataStacks.size}, data.stacks size: ${data.stacks.size}"
        )
        BandageLogger.i(
            TAG,
            "causes size: ${dataCauses?.size}, data.causes size: ${data.causes?.size}"
        )
        BandageLogger.i(TAG, "match stack cost: ${SystemClock.elapsedRealtime() - startRecordTime}")

        if (dataStacks.isNotEmpty()) {
            return false
        }
        if (dataCauses.isNullOrEmpty()) {
            return true
        }
        return false
    }

    /**
     * 获取异常链路
     * @param th 异常对象
     * @return 异常链路列表
     */
    private fun getThrowableCauses(th: Throwable): List<Throwable> {
        val list = ArrayList<Throwable>()
        list.add(th)

        var cur = th
        for (i in 0..9) {
            val next = cur.cause ?: break
            if (next == cur) break
            list.add(next)
            cur = next
        }
        return list
    }

    /**
     * 判断进程名是否匹配
     * @param data 动态补丁数据
     * @return 如果匹配返回true，否则返回false
     */
    fun isProcessMatch(data: DynamicBandageData): Boolean {
        if (data.process == "all") {
            return true
        }
        val processName = if (data.process == "main") {
            Bandage.config.packageName
        } else {
            Bandage.config.packageName + ":" + data.process
        }
        return processName == Bandage.config.currentProcessName
    }

}
