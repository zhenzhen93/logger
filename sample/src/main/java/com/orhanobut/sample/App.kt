package com.orhanobut.sample

import android.app.Application

/**
 * @author      : zhenzhen.xie
 * @E-mail      : zhenzhen.xie@zhangmen.com
 * @date        : 2020/4/16 11:57
 * @version     : v0.3.9
 * @desc        :
 */
class App : Application() {

  override fun onCreate() {
    super.onCreate()
    //日志初始化
    LogX.register(this, true, object : TrackCallback {
      override fun onEventImme(eventId: String, eventParam: Map<String, String>?) {
        LogX.errorZhen("全链路日志埋点： $eventId  $eventParam")
      }
    })

  }

}