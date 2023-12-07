package com.panda912.bandage.internal.dynamic

import com.panda912.bandage.internal.data.DynamicBandageData

/**
 * 动态异常处理程序管理器
 */
internal object BandageDynamicExceptionManager {

    private val dynamicBandageDataList = arrayListOf<DynamicBandageData>()

    /**
     * 添加数据
     * @param list 待添加的动态补丁数据列表
     */
    fun addData(list: List<DynamicBandageData>?) {
        synchronized(this) {
            dynamicBandageDataList.clear()
            if (!list.isNullOrEmpty()) {
                dynamicBandageDataList.addAll(list.filter { BandageDataMatcher.isProcessMatch(it) })
            }
        }
    }

    /**
     * 获取动态补丁数据
     * @param th 异常对象
     * @return 动态补丁数据对象，如果不存在则返回null
     */
    @Synchronized
    fun getDynamicBandageData(th: Throwable): DynamicBandageData? {
        if (dynamicBandageDataList.isEmpty()) {
            return null
        }

        for (data in dynamicBandageDataList) {
            if (BandageDataMatcher.isProcessMatch(data) &&    // process √
                data.exceptionMatch?.isMatch(th) == true &&     // class name && message √
                BandageDataMatcher.isStackMatch(data, th)       // stack √
            ) {
                return data
            }
        }
        return null
    }

}
