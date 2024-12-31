package com.scanny_project

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {
    // You can override onCreate if you need to initialize anything for the application
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        private lateinit var instance: MyApp
        fun getAppContext(): Context = instance.applicationContext
    }
}