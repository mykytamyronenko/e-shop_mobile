package com.school.projettm

import android.app.Application

class AppContext : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: AppContext
            private set
    }
}