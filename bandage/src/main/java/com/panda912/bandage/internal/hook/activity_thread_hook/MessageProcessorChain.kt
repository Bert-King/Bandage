package com.panda912.bandage.internal.hook.activity_thread_hook

import android.os.Message

/**
 * 消息处理链
 *
 * @param processors 处理器列表
 * @param index 当前处理器索引
 * @param input 输入消息
 */
internal class MessageProcessorChain(
    private val processors: List<Processor>,
    private val index: Int,
    private val input: Message,
) : Processor.Chain {

    /**
     * 获取输入消息
     *
     * @return 输入消息
     */
    override fun input(): Message = input

    /**
     * 处理消息
     *
     * @param input 输入消息
     * @return 处理是否成功
     */
    override fun proceed(input: Message): Boolean {
        check(index < processors.size)
        val processor = processors[index]
        val next = MessageProcessorChain(processors, index + 1, input)
        return processor.process(next)
    }
}
