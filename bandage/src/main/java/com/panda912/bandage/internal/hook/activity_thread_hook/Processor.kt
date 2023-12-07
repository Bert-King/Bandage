package com.panda912.bandage.internal.hook.activity_thread_hook

import android.os.Message

/**
 * 处理器接口
 */
internal interface Processor {
    /**
     * 处理过程
     *
     * @param chain 链
     * @return 处理结果
     */
    fun process(chain: Chain): Boolean

    /**
     * 链接口
     */
    interface Chain {
        /**
         * 获取输入消息
         *
         * @return 输入消息
         */
        fun input(): Message
        /**
         * 处理过程
         *
         * @param input 输入消息
         * @return 处理结果
         */
        fun proceed(input: Message): Boolean
    }
}
