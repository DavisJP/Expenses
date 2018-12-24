package com.davismiyashiro.expenses.injection

import android.app.Application

/**
 * @Author Davis Miyashiro
 */

class App : Application() {

    lateinit var component: ApplicationComponent
        private set

    override fun onCreate() {
        super.onCreate()

        component = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .openTabModule(OpenTabModule())
                .build()
    }
}
