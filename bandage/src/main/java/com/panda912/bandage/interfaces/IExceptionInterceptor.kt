package com.panda912.bandage.interfaces

/**
 * 异常拦截器接口
 */
interface IExceptionInterceptor {
    //获取拦截器名称
    fun getName(): String
    //是否启用优化
    fun shouldEnableOpt(): Boolean = true
    //拦截线程并处理异常
    fun intercept(thread: Thread, throwable: Throwable): Boolean
}
