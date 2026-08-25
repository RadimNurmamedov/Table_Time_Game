package com.lavalama.tabletimegame

import android.app.Application
import com.lavalama.tabletimegame.presentation.startup.AppStartupManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TTApp: Application() {
    override fun onCreate() {
        super.onCreate()

    }
}