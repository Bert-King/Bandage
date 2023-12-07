package com.panda912.bandage.interfaces

import com.panda912.bandage.internal.data.DynamicBandageData.GuideUpgradeData

/**
 * 包扎行为接口
 */
interface IBandageBehavior {
    /**
     * 加载补丁
     */
    fun loadPatch()

    /**
     * 指导升级
     * @param guideUpgradeData 指导升级数据
     * @param closeCurActivity 是否关闭当前活动
     */
    fun guideUpgrade(guideUpgradeData: GuideUpgradeData, closeCurActivity: Boolean)

    /**
     * 启动路由
     * @param router 路由器
     */
    fun startRouter(router: String)

    /**
     * 完成当前活动
     * @param throwable 错误信息
     */
    fun finishCurActivity(throwable: Throwable)

    /**
     * 上传崩溃信息
     * @param throwable 错误信息
     */
    fun uploadCrash(throwable: Throwable)
}
