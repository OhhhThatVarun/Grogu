package com.varun.grogu

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import logcat.AndroidLogcatLogger
import logcat.LogPriority

@HiltAndroidApp
class GroguApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            AndroidLogcatLogger.installOnDebuggableApp(this, minPriority = LogPriority.VERBOSE)
        }
//        else {
//            LogcatLogger.install(GroguLogcatLogger())
//        }
    }

}