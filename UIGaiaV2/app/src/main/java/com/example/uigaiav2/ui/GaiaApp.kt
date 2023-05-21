package com.example.uigaiav2.ui

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GaiaApp :Application(){
    override fun onCreate() {
        super.onCreate()
    }
}