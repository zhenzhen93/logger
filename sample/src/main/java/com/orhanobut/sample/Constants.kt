package com.orhanobut.sample

/**
 * 日志TAG标签
 */
const val DEFAULT_TAG = "AL_LOG"
/**
 * 只输出到控制台
 */
const val LOGCAT_ONLY = 0x04
/**
 * 只输出到文件系统
 */
const val FILE_ONLY = 0x02
/**
 * 只输出到链路埋点
 */
const val TRACK_ONLY = 0x01

/**
 * 控制台 + 文件输出
 */
const val LOGCAT_FILE = LOGCAT_ONLY + FILE_ONLY
/**
 * 文件输出 + 链路埋点
 */
const val FILE_TRACK = LOGCAT_ONLY + FILE_ONLY
/**
 * 控制台 + 文件输出 + 链路埋点
 */
const val LOGCAT_FILE_TRACK = LOGCAT_ONLY + FILE_ONLY + TRACK_ONLY
