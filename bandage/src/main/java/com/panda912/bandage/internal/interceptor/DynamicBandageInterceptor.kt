package com.panda912.bandage.internal.interceptor

import com.panda912.bandage.internal.dynamic.BandageDynamicExceptionManager
import com.panda912.bandage.BandageHelper
import com.panda912.bandage.interfaces.IExceptionInterceptor
import com.panda912.bandage.internal.data.DynamicBandageData

/**
 * Created by panda on 2021/12/13 17:58
 */
internal class DynamicBandageInterceptor : IExceptionInterceptor {

    override fun getName() = "DynamicBandageInterceptor"

    override fun intercept(thread: Thread, throwable: Throwable): Boolean {
        val data = BandageDynamicExceptionManager.getDynamicBandageData(throwable) ?: return false
        handleException(throwable, data)
        return true
    }

    private fun handleException(throwable: Throwable, dynamicBandageData: DynamicBandageData) {
        BandageHelper.uploadCrash(throwable)
        if (!dynamicBandageData.router.isNullOrEmpty()) {
            BandageHelper.startRouter(dynamicBandageData.router)
        }
        if (dynamicBandageData.closeCurActivity) {
            BandageHelper.finishFatalActivity(throwable)
        }
        if (dynamicBandageData.loadPatch) {
            BandageHelper.loadPatch()
        }
        if (dynamicBandageData.guideUpgrade?.show == true) {
            BandageHelper.guideUpgrade(
                dynamicBandageData.guideUpgrade,
                dynamicBandageData.closeCurActivity
            )
        }
    }

}