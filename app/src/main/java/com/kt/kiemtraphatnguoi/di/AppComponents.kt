package com.kt.kiemtraphatnguoi.di

import com.kt.kiemtraphatnguoi.MainActivity
import com.kt.kiemtraphatnguoi.base.network.ICheckViolationDataSource
import com.kt.kiemtraphatnguoi.ui.MainInteractors
import com.kt.kiemtraphatnguoi.usecase.CheckViolations
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, NetworkModule::class])
@Singleton
interface AppComponents {
    fun inject(activity: MainActivity)

    fun mapUrl(): Map<ICheckViolationDataSource.Type, String>

    fun mapDataSource(): Map<ICheckViolationDataSource.Type, @JvmSuppressWildcards ICheckViolationDataSource>

    fun mainInteractors(): MainInteractors

}