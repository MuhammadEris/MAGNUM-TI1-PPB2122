package com.magnum_ti1.plant

import android.app.Application
import com.magnum_ti1.plant.di.app
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            modules(app)
        }
    }
}