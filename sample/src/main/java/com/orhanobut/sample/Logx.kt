package com.orhanobut.sample

import android.content.Context
import android.os.HandlerThread
import com.orhanobut.logger.*
import com.orhanobut.logger.CsvFormatStrategy.Builder.MAX_BYTES
import java.io.File


object LogX {

  //全局注册标记
  private var isRegistered = false
  //是否打印日志
  private var isDebug: Boolean = true
  //链路埋点回调
  private var trackCallback: TrackCallback? = null

  //文件写入
  private var diskPath: String? = null
  private lateinit var diskLogAdapter: LogAdapter
  private val loggerDisk = Logger.getInstance(LoggerType.TYPE_FILE)

  //logcat日志输出
  private lateinit var logcatLogAdapter: LogAdapter
  private val loggerLogcat = Logger.getInstance(LoggerType.TYPE_LOGCAT)

  /**
   * 日志初始化入口
   */
  fun register(context: Context, logcatLoggable: Boolean, trackCallback: TrackCallback?, diskPath: String? = null) {
    if (isRegistered) return
    isRegistered = true
    this.isDebug = logcatLoggable
    this.trackCallback = trackCallback

    //logcat日志配置输出
    val logcatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
        .showThreadInfo(false)
        .methodCount(0)
        .methodOffset(0)
        .tag(DEFAULT_TAG)
        .build()
    logcatLogAdapter = object : AndroidLogAdapter(logcatStrategy) {
      override fun isLoggable(priority: Int, tag: String?): Boolean {
        return isDebug
      }
    }
    loggerLogcat.addLogAdapter(logcatLogAdapter)

    //file日志，默认配置输出 /data/data/..pageName../files/course_log/course_logs_0.csv
    this.diskPath = if (diskPath.isNullOrEmpty()) context.filesDir.path + File.separatorChar + "course_log" else diskPath
    val handlerThread = HandlerThread("AndroidFileLogger")
    handlerThread.start()
    val diskStrategy: FormatStrategy = CsvFormatStrategy.newBuilder()
        .logStrategy(DiskLogStrategy(DiskLogStrategy.WriteHandler(handlerThread.looper, this.diskPath!!, MAX_BYTES)))
        .tag(DEFAULT_TAG)
        .build()
    diskLogAdapter = object : DiskLogAdapter(diskStrategy) {
      override fun isLoggable(priority: Int, tag: String?): Boolean {
        return true
      }
    }
    loggerDisk.addLogAdapter(diskLogAdapter)
  }

  /**
   * 仅用于logcat输出
   */
  fun debugOnly(eventId: String) {
    if (!isDebug) return
    loggerLogcat.d(eventId)
  }

  /**
   * 仅用于logcat输出
   */
  fun warnOnly(eventId: String) {
    if (!isDebug) return
    loggerLogcat.w(eventId)
  }

  /**
   * 仅用于logcat输出
   */
  fun errorOnly(eventId: String) {
    if (!isDebug) return
    loggerLogcat.e(eventId)
  }

  /**
   * 仅用于文件输出
   */
  fun fileOnly(eventId: String, eventParam: Map<String, String>? = null) {
    debug(FILE_ONLY, eventId, eventParam)
  }

  /**
   * 仅用于链路埋点
   */
  fun trackOnly(eventId: String, eventParam: Map<String, String>? = null) {
    debug(TRACK_ONLY, eventId, eventParam)
  }

  /**
   * logcat输出 + 文件输出，debug级别
   */
  fun debugFile(eventId: String, eventParam: Map<String, String>? = null) {
    debug(LOGCAT_FILE, eventId, eventParam)
  }

  /**
   * logcat输出 + 文件输出，warn级别
   */
  fun warnFile(eventId: String, eventParam: Map<String, String>? = null) {
    warn(LOGCAT_FILE, eventId, eventParam)
  }

  /**
   * logcat输出 + 文件输出，error级别
   */
  fun errorFile(eventId: String, eventParam: Map<String, String>? = null) {
    error(LOGCAT_FILE, eventId, eventParam)
  }

  /**
   * 控制台 + 文件输出 + 链路埋点
   */
  fun trackEventD(eventId: String, eventParam: Map<String, String>? = null) {
    debug(LOGCAT_FILE_TRACK, eventId, eventParam)
  }

  /**
   * 控制台 + 文件输出 + 链路埋点
   */
  fun trackEventW(eventId: String, eventParam: Map<String, String>? = null) {
    warn(LOGCAT_FILE_TRACK, eventId, eventParam)
  }

  /**
   * 控制台 + 文件输出 + 链路埋点
   */
  fun trackEventE(eventId: String, eventParam: Map<String, String>? = null) {
    error(LOGCAT_FILE_TRACK, eventId, eventParam)
  }

  /**
   * 任意debug信息
   */
  private fun debug(to: Int, eventId: String, eventParam: Map<String, String>? = null) {
    if (to and LOGCAT_ONLY != 0 && isDebug) {
      loggerLogcat.d("$eventId  $eventParam")
    }
    if (to and FILE_ONLY != 0) {
      loggerDisk.d("$eventId  $eventParam")
    }
    if (to and TRACK_ONLY != 0) {
      trackCallback?.onEventImme(eventId, eventParam)
    }
  }

  /**
   * 任意warn信息
   */
  private fun warn(to: Int, eventId: String, eventParam: Map<String, String>? = null) {
    if (to and LOGCAT_ONLY != 0 && isDebug) {
      loggerLogcat.w("$eventId  $eventParam")
    }
    if (to and FILE_ONLY != 0) {
      loggerDisk.w("$eventId  $eventParam")
    }
    if (to and TRACK_ONLY != 0) {
      trackCallback?.onEventImme(eventId, eventParam)
    }
  }

  /**
   * 任意error信息
   */
  private fun error(to: Int, eventId: String, eventParam: Map<String, String>? = null) {
    if (to and LOGCAT_ONLY != 0 && isDebug) {
      loggerLogcat.e("$eventId  $eventParam")
    }
    if (to and FILE_ONLY != 0) {
      loggerDisk.e("$eventId  $eventParam")
    }
    if (to and TRACK_ONLY != 0) {
      trackCallback?.onEventImme(eventId, eventParam)
    }
  }

  /**
   * 清除磁盘file文件
   */
  fun clearDiskLogFile() {
  }

  /**
   * 获取磁盘路径
   */
  fun getDiskLogFile() = diskPath


  /**
   * 专属调试人员，仅用于logcat输出
   */
  fun debugZhen(eventId: String, tag: String? = "zhen") {
    if (!isDebug) return
    loggerLogcat.t(tag).d(eventId)
  }

  /**
   * 专属调试人员，仅用于logcat输出
   */
  fun warnZhen(eventId: String, tag: String? = "zhen") {
    if (!isDebug) return
    loggerLogcat.t(tag).w(eventId)
  }

  /**
   * 专属调试人员，仅用于logcat输出
   */
  fun errorZhen(eventId: String, tag: String? = "zhen") {
    if (!isDebug) return
    loggerLogcat.t(tag).e(eventId)
  }

}