package com.panda912.bandage.internal.hook.activity_thread_hook

/**
 * Created by panda on 2021/12/6 16:38
 */
internal data class ActivityThreadFixMessage(
    var msgId: Int,
    val msgName: String,
    val keywords: Set<String>? = null,
)