package com.kt.kiemtraphatnguoi.base.network

import com.kt.kiemtraphatnguoi.base.Api
import com.kt.kiemtraphatnguoi.model.VehicleType
import com.kt.kiemtraphatnguoi.model.ViolationResult
import io.reactivex.Observable
import javax.inject.Inject

class CheckViolationDataSourceImpl @Inject constructor(
    private val api: Api
) : ICheckViolationDataSource {
    override fun checkViolation(bks: String, type: VehicleType): Observable<ViolationResult> {
        return Observable.create {
            it.onError(Throwable())
        }
    }
}