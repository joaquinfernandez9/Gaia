package com.example.uigaiav2.framework

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GaiaApp :Application(){
    override fun onCreate() {
        super.onCreate()
    }
}