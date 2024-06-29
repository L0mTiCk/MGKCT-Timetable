package com.l0mtick.mgkcttimetable

import android.app.Application
import com.l0mtick.mgkcttimetable.data.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MGKCTApplication : Application()  {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MGKCTApplication)
            modules(appModule)
        }
    }
}