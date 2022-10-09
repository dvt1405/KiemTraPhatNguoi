package com.kt.kiemtraphatnguoi.usecase

import android.util.Log
import com.kt.kiemtraphatnguoi.base.network.ICheckViolationDataSource
import com.kt.kiemtraphatnguoi.model.VehicleType
import com.kt.kiemtraphatnguoi.model.ViolationResult
import io.reactivex.Observable
import javax.inject.Inject

class CheckViolations @Inject constructor(
    private val map: @JvmSuppressWildcards Map<ICheckViolationDataSource.Type, ICheckViolationDataSource>
) : BaseUseCase<ViolationResult>() {
    private val listExecuted by lazy { mutableListOf<ICheckViolationDataSource.Type>() }
    override fun prepareExecute(params: Map<String, Any>): Observable<ViolationResult> {
        val type = params["type"] as ICheckViolationDataSource.Type
        val bks = params["bks"] as String
        val dataSource = map[type] ?: return Observable.error(Throwable())
        listExecuted.add(type)
        return dataSource.checkViolation(bks, VehicleType.CAR)
            .doOnComplete {
                listExecuted.clear()
            }
            .onErrorResumeNext(nextType()?.let {
                invoke(bks, it)
            } ?: Observable.error(Throwable("Cannot get result")))

            .doOnError {
                Log.e("TAG", it.message, it)
            }
    }

    private fun nextType(): ICheckViolationDataSource.Type? {
        val totalSource = ICheckViolationDataSource.Type.values().toMutableList()
        if (totalSource.isEmpty() || totalSource.size == listExecuted.size) return null
        return totalSource.apply {
            this.removeAll(listExecuted)
        }.first()
    }

    operator fun invoke(bks: String, type: ICheckViolationDataSource.Type) = execute(
        mapOf(
            "type" to type,
            "bks" to bks
        )
    )

}