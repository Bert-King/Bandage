package com.panda912.bandage.internal.data

import java.util.regex.Pattern

/**
 * 动态补丁数据类
 * Created by panda on 2021/12/13 18:03
 */
data class DynamicBandageData(
    val process: String,  // 进程名
    val exceptionMatch: ExceptionMatch?,  // 异常匹配对象
    val stacks: List<String>,  // 堆栈信息列表
    val causes: List<ExceptionMatch>?,  // 异常原因列表
    val closeCurActivity: Boolean,  // 是否关闭当前活动
    val router: String? = null,  // 路由器名称，可选
    val loadPatch: Boolean,  // 是否加载补丁
    val guideUpgrade: GuideUpgradeData? = null,  // 升级引导数据，可选
) {

    /**
     * 异常匹配对象类
     */
    data class ExceptionMatch(
        val className: String,  // 类名
        val messageRegular: String? = null,  // 消息正则表达式，可选
    ) {

        /**
         * 判断是否匹配给定的异常
         * @param th 异常对象
         * @return 是否匹配
         */
        fun isMatch(th: Throwable): Boolean {
            if (th.javaClass.name != className) {
                return false
            }
            if (messageRegular.isNullOrEmpty()) {
                return true
            }
            val message = th.message
            if (message.isNullOrEmpty()) {
                return false
            }
            if (messageRegular == th.message || Pattern.matches(messageRegular, message)) {
                return true
            }
            return false
        }
    }

    /**
     * 升级引导数据类
     */
    data class GuideUpgradeData(
        val show: Boolean,  // 是否显示
        val title: String,  // 标题
        val tips: String,  // 提示
        val desc: String,  // 描述
        val okBtnText: String,  // 确定按钮文本
    )
}

/**
 * 移除与给定异常匹配的异常原因
 * @param list 异常原因列表
 * @param th 给定的异常对象
 */
fun removeMatchedCause(list: ArrayList<DynamicBandageData.ExceptionMatch>, th: Throwable) {
    for (exceptionMatch in list) {
        if (exceptionMatch.isMatch(th)) {
            list.remove(exceptionMatch)
            return
        }
    }
}
