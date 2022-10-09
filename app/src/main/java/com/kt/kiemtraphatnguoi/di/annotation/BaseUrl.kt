package com.kt.kiemtraphatnguoi.di.annotation

import com.kt.kiemtraphatnguoi.base.network.ICheckViolationDataSource
import dagger.MapKey

@MapKey
annotation class BaseUrl(val value: ICheckViolationDataSource.Type)
