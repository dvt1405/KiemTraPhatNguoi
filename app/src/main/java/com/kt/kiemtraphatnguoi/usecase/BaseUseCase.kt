package com.kt.kiemtraphatnguoi.usecase

import io.reactivex.Observable

abstract class BaseUseCase<T : Any>(private val transformer: AsyncTransformer<T> = AsyncTransformer()) {
    abstract fun prepareExecute(params: Map<String, Any>): Observable<T>

    fun execute(params: Map<String, Any> = mapOf()) = prepareExecute(params)
        .compose(transformer)
}