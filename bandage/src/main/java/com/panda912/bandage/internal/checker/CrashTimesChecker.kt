package com.panda912.bandage.internal.checker

import com.panda912.bandage.interfaces.ICrashChecker
import com.panda912.bandage.internal.data.CrashData

/**
 * CrashTimesChecker 类是一个实现接口 ICrashChecker 的类，用于检查崩溃次数是否在允许范围内。
 */
class CrashTimesChecker : ICrashChecker {

    /**
     * isHopeful 方法用于判断崩溃次数是否希望重试。
     *
     * @param crashDataList 崩溃数据列表
     * @param times 崩溃次数
     * @param thread 线程
     * @param throwable 异常
     * @return 崩溃次数是否希望重试
     */
    override fun isHopeful(
        crashDataList: List<CrashData>,
        times: Int,
        thread: Thread,
        throwable: Throwable,
    ): Boolean {
        return times <= 6
    }
}
