package com.panda912.bandage.interfaces

import android.app.Application

/**
 * 用于配置绑带框架的参数的接口
 */
interface IBandageConfig {
    /**
     * 应用实例
     */
    val application: Application

    /**
     * 日志记录器
     */
    val logger: ILogger?

    /**
     * 是否启用绑带框架
     */
    val isEnable: Boolean

    /**
     * 包名
     */
    val packageName: String

    /**
     * 当前进程名
     */
    val currentProcessName: String

    /**
     * 是否在子线程中捕获崩溃
     */
    val enableSubThreadCrash: Boolean

    /**
     * 是否在子进程中捕获异常
     */
    val enableCatchBadTokenInSubProcess: Boolean

    /**
     * 是否启用动态绑带拦截器
     */
    val enableDynamicBandageInterceptor: Boolean

    /**
     * 是否启用ActivityThreadHook
     */
    val enableActivityThreadHook: Boolean

    /**
     * 是否启用ViewRootImplHandlerHook
     */
    val enableViewRootImplHandlerHook: Boolean

    /**
     * 是否启用修复配置尺寸的函数
     */
    val enableFixReportSizeConfigurations: Boolean

    /**
     * 绑带行为
     */
    val behavior: IBandageBehavior

    /**
     * 拦截器列表
     */
    val interceptors: List<IExceptionInterceptor>?

    /**
     * 检查器列表
     */
    val checkers: List<ICrashChecker>?
}
