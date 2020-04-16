package com.orhanobut.sample

import android.app.Application


class App : Application() {

  override fun onCreate() {
    super.onCreate()
    //日志初始化
    LogX.register(this)

  }

}