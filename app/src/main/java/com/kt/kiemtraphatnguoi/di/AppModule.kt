package com.kt.kiemtraphatnguoi.di

import com.kt.kiemtraphatnguoi.base.network.ICheckViolationDataSource
import dagger.Module
import dagger.multibindings.Multibinds


@Module
abstract class AppModule {

    @Multibinds
    abstract fun mapDataSource(): Map<ICheckViolationDataSource.Type, ICheckViolationDataSource>

    @Multibinds
    abstract fun mapUrl(): Map<ICheckViolationDataSource.Type, String>

}