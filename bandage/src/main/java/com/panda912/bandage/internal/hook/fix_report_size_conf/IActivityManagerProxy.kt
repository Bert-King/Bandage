package com.panda912.bandage.internal.hook.fix_report_size_conf

import com.panda912.bandage.TAG
import com.panda912.bandage.internal.BandageLogger
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

/**
 * 用于创建`IActivityManagerProxy`代理类的内部类。
 *
 * @param iActivityManager IActivityManager的实例
 */
internal class IActivityManagerProxy(private val iActivityManager: Any) : InvocationHandler {

    /**
     * 处理方法调用的默认方法。
     *
     * 如果被调用的方法是`reportSizeConfigurations`，则打印日志并调用iActivityManager的对应方法，
     * 否则直接调用iActivityManager的方法。
     *
     * @param proxy 代理对象
     * @param method 被调用的方法
     * @param args 参数列表
     * @return 被调用方法的返回值
     */
    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
        if (method?.name == "reportSizeConfigurations") {
            BandageLogger.i(
                TAG,
                "proxy: android.app.IActivityManager\$Stub\$Proxy.reportSizeConfigurations"
            )
            return try {
                method.invoke(iActivityManager, *(args ?: arrayOfNulls<Any>(0)))
            } catch (th: Throwable) {
                null
            }
        }
        return method?.invoke(iActivityManager, *(args ?: arrayOfNulls<Any>(0)))
    }
}
