package com.panda912.bandage.internal.hook.activity_thread_hook

/**
 * ActivityThreadFixMessage类用于表示活动线程修复消息。
 *
 * @param msgId 消息ID
 * @param msgName 消息名称
 * @param keywords 可选参数，关键词的集合
 */
internal data class ActivityThreadFixMessage(
    var msgId: Int,
    val msgName: String,
    val keywords: Set<String>? = null,
)
