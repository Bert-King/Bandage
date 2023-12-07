package com.panda912.bandage.interfaces

import com.panda912.bandage.internal.data.CrashData

/**
 * Created by panda on 2021/12/13 17:59
 */
fun interface ICrashChecker {

    /**
     * 检查是否需要抛出崩溃并退出应用。
     * @return 如果拦截了崩溃并且不抛出异常则返回true，否则返回false。
     */
    fun isHopeful(
        crashDataList: List<CrashData>,
        times: Int,
        thread: Thread,
        throwable: Throwable
    ): Boolean
}
