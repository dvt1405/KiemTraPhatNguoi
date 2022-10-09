package com.kt.kiemtraphatnguoi.base.network

import com.kt.kiemtraphatnguoi.model.VehicleType
import com.kt.kiemtraphatnguoi.model.ViolationResult
import io.reactivex.Observable

interface ICheckViolationDataSource {
    fun checkViolation(bks: String, type: VehicleType): Observable<ViolationResult>

    enum class Type {
        PhatNguoiXe, KGO, KiemTraPhatNguoi
    }
}