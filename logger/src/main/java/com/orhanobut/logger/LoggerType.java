package com.orhanobut.logger;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({LoggerType.TYPE_LOGCAT, LoggerType.TYPE_FILE})
@Retention(RetentionPolicy.SOURCE)
public @interface LoggerType {
  /**
   * logcat打印
   */
  String TYPE_LOGCAT = "logcat";
  /**
   * 文件打印
   */
  String TYPE_FILE = "file";
}
