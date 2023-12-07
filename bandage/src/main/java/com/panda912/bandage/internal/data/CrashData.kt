package com.panda912.bandage.internal.data

/**
 * CrashData数据类
 * 存储崩溃数据
 * 包括抛出的异常和时间戳
 */
data class CrashData(
    val throwable: Throwable, // 抛出的异常
    val timestamp: Long // 时间戳
)
