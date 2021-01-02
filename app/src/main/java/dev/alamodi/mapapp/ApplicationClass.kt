package dev.alamodi.mapapp

import android.app.Application

class ApplicationClass:Application() {

    override fun onCreate() {
        super.onCreate()
        LocationRepository.initialize(applicationContext)
    }
}