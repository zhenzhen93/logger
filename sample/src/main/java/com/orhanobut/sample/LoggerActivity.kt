package com.orhanobut.sample

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_logger.*


class LoggerActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_logger)
    checkPermission()

    btnLog.setOnClickListener {
      LogX.debugOnly("仅用于调试阶段，logcat输出")
      LogX.debugZhen("专属调试人员，仅用于logcat输出")
    }

    btnFileOnly.setOnClickListener {
      LogX.fileOnly(
          "fileOnly",
          hashMapOf(
              Pair("fileOnlyKey1", "fileOnlyValue1"),
              Pair("fileOnlyKey2", "fileOnlyValue2")
          )
      )
    }

    btnLogcatFile.setOnClickListener {
      LogX.debugFile(
          "logcat_file",
          hashMapOf(
              Pair("logcat_file_Key1", "logcat_file_Value1"),
              Pair("logcat_file_Key2", "logcat_file_Value2")
          )
      )
    }

    btnLogCatFileTrack.setOnClickListener {
      LogX.trackEventD(
          "logcat_file_track",
          hashMapOf(
              Pair("logcat_file_track_Key1", "logcat_file_track_Value1"),
              Pair("logcat_file_track_Key2", "logcat_file_track_Value2")
          )
      )
    }
  }

  private fun checkPermission() { //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
    if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED
    ) { //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
      if (ActivityCompat.shouldShowRequestPermissionRationale(
              this,
              Manifest.permission.WRITE_EXTERNAL_STORAGE
          )
      ) {
        Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show()
      }
      //申请权限
      ActivityCompat.requestPermissions(
          this,
          arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
          0
      )
    } else {
      Toast.makeText(this, "授权成功！", Toast.LENGTH_SHORT).show()
    }
  }

}
