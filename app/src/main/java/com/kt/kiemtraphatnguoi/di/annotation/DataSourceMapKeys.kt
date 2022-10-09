package com.kt.kiemtraphatnguoi.di.annotation

import com.kt.kiemtraphatnguoi.base.network.ICheckViolationDataSource
import dagger.MapKey


@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@MapKey
annotation class DataSourceMapKeys(
    val value: ICheckViolationDataSource.Type,
)
