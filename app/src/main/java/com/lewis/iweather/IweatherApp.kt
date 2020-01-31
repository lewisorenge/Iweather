package com.lewis.iweather

import android.app.Application
import com.lewis.iweather.BuildConfig
import timber.log.Timber.DebugTree
import timber.log.Timber

class IweatherApp : Application() {

  override fun onCreate() {
    super.onCreate()

    // Initialize Timber if in debug mode
    if (BuildConfig.DEBUG) {
      Timber.plant(DebugTree())
    }
  }
}