package com.opentable.openfoods.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OpenFoodApplication: Application() {


    override fun onCreate() {
        super.onCreate()
    }

}