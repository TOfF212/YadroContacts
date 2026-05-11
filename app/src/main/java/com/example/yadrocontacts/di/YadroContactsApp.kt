package com.example.yadrocontacts.di


import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class YadroContactsApp : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}
