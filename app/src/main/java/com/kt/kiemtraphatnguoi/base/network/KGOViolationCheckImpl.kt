package com.kt.kiemtraphatnguoi.base.network

import com.google.gson.Gson
import com.kt.kiemtraphatnguoi.model.KGOResponse
import com.kt.kiemtraphatnguoi.model.VehicleType
import com.kt.kiemtraphatnguoi.model.ViolationResult
import io.reactivex.Observable
import okhttp3.*
import java.io.IOException
import javax.inject.Inject

class KGOViolationCheckImpl @Inject constructor(
    private val client: OkHttpClient,
    private val urlMap: Map<ICheckViolationDataSource.Type, String>
) : ICheckViolationDataSource {
    private val url = urlMap[ICheckViolationDataSource.Type.KGO]!!
    override fun checkViolation(bks: String, type: VehicleType): Observable<ViolationResult> {
        return Observable.create { emitter ->
            client.newCall(
                Request.Builder()
                    .url("$url$bks")
                    .build()
            ).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    emitter.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful && response.code in 200..299) {
                        response.body?.string()?.let {
                            val responseDto = Gson().fromJson(it, KGOResponse::class.java)
                            emitter.onNext(ViolationResult.FromKGO(responseDto))
                        } ?: emitter.onError(Throwable("Null"))
                    } else {
                        emitter.onError(Throwable("Null"))
                    }
                    emitter.onComplete()
                }

            })
        }
    }

}
