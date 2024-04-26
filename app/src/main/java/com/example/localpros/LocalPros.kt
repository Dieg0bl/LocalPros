package com.example.localpros

import android.app.Application
import com.example.localpros.utils.LogUtils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LocalPros : Application() {

    override fun onCreate() {
        super.onCreate()
        LogUtils.init(this)
    }
}

