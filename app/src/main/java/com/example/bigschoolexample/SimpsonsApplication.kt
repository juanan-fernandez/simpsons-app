package com.example.bigschoolexample

import android.app.Application

class SimpsonsApplication : Application() {

    val appContainer: AppContainer by lazy {
        AppContainer(this)
    }
}
