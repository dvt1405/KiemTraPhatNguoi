package com.kt.kiemtraphatnguoi.di

import com.google.gson.Gson
import com.kt.kiemtraphatnguoi.BuildConfig
import com.kt.kiemtraphatnguoi.base.Api
import com.kt.kiemtraphatnguoi.base.network.CheckViolationDataSourceImpl
import com.kt.kiemtraphatnguoi.base.network.ICheckViolationDataSource
import com.kt.kiemtraphatnguoi.base.network.KGOViolationCheckImpl
import com.kt.kiemtraphatnguoi.base.network.PhatNguoiXeImpl
import com.kt.kiemtraphatnguoi.di.annotation.BaseUrl
import com.kt.kiemtraphatnguoi.di.annotation.DataSourceMapKeys
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
        this.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }


    @Provides
    @Singleton
    fun provideClient(interceptor: Interceptor): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    @Provides
    @Singleton
    fun provideGson() = Gson()

    @Provides
    @Singleton
    fun provideRetrofitBuilder(client: OkHttpClient, gson: Gson): Retrofit.Builder =
        Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))

    @Provides
    @Singleton
    fun providesApi(
        @Named("baseUrl") baseUrl: String,
        retrofitBuilder: Retrofit.Builder
    ): Api = retrofitBuilder
        .baseUrl(baseUrl)
        .build()
        .create(Api::class.java)


    @Provides
    @IntoMap
    @BaseUrl(ICheckViolationDataSource.Type.KGO)
    fun providesKgoUrl(): String = "https://kgo.life/api/v1/search/cool-fined?license_plate="

    @Provides
    @IntoMap
    @BaseUrl(ICheckViolationDataSource.Type.PhatNguoiXe)
    fun providesPhatNguoiXeoUrl(): String = "https://phatnguoixe.com/"

    @Provides
    @Singleton
    @Named("baseUrl")
    fun providesBaseUrl() = "https://kgo.life/api/v1/"


    @Provides
    @Singleton
    @IntoMap
    @DataSourceMapKeys(value = ICheckViolationDataSource.Type.KGO)
    fun providesKGO(kgoViolationCheckImpl: KGOViolationCheckImpl): ICheckViolationDataSource =
        kgoViolationCheckImpl

    @Provides
    @Singleton
    @IntoMap
    @DataSourceMapKeys(value = ICheckViolationDataSource.Type.PhatNguoiXe)
    fun providesPhatNguoiXe(phatNguoiXeImpl: PhatNguoiXeImpl): ICheckViolationDataSource =
        phatNguoiXeImpl


    @Provides
    @Singleton
    @IntoMap
    @DataSourceMapKeys(value = ICheckViolationDataSource.Type.KiemTraPhatNguoi)
    fun bindsCheckViolation(dataSourceImpl: CheckViolationDataSourceImpl): ICheckViolationDataSource =
        dataSourceImpl

}