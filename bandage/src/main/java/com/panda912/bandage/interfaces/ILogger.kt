package com.panda912.bandage.interfaces

import android.util.Log

interface ILogger {
    fun i(tag: String, message: String)
    fun w(tag: String, message: String, throwable: Throwable?)

    companion object DEFAULT : ILogger {
        /**
         * 打印info级别的日志
         *
         * @param tag 日志标签
         * @param message 日志内容
         */
        override fun i(tag: String, message: String) {
            Log.i(tag, message)
        }

        /**
         * 打印warning级别的日志
         *
         * @param tag 日志标签
         * @param message 日志内容
         * @param throwable 异常信息
         */
        override fun w(tag: String, message: String, throwable: Throwable?) {
            Log.w(tag, message, throwable)
        }
    }

}