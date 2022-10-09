package com.kt.kiemtraphatnguoi

import android.app.Application
import com.kt.kiemtraphatnguoi.di.AppComponents
import com.kt.kiemtraphatnguoi.di.DaggerAppComponents

class App : Application() {
    val component: AppComponents by lazy {
        DaggerAppComponents.builder()
            .build()
    }
    override fun onCreate() {
        super.onCreate()
        app = this

    }

    companion object {
        private lateinit var app: App
        fun get() = app
    }
}