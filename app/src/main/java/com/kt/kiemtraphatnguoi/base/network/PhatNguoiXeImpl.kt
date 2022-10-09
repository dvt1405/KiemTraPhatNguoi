package com.kt.kiemtraphatnguoi.base.network

import com.kt.kiemtraphatnguoi.model.VehicleType
import com.kt.kiemtraphatnguoi.model.ViolationResult
import com.kt.kiemtraphatnguoi.utils.buildCooke
import io.reactivex.Observable
import okhttp3.*
import org.jsoup.Jsoup
import java.io.IOException
import javax.inject.Inject

class PhatNguoiXeImpl @Inject constructor(
    private val client: OkHttpClient,
    private val urlMap: Map<ICheckViolationDataSource.Type, String>
) : ICheckViolationDataSource {
    private val baseUrl by lazy {
        urlMap[ICheckViolationDataSource.Type.PhatNguoiXe]!!
    }
    private val cookie by lazy { mutableMapOf<String, String>() }

    override fun checkViolation(bks: String, type: VehicleType): Observable<ViolationResult> {
        return Observable.create { emmiter ->
            if (cookie.isEmpty()) {
                val connection = Jsoup.connect(baseUrl).execute()
                cookie.putAll(connection.cookies())
            }
            val request = Request.Builder()
                .url("$baseUrl/1026")
                .post(
                    FormBody.Builder()
                        .add("BienSo", bks)
                        .add("LoaiXe", "${type.id}")
                        .build()
                )
                .header("cookie", cookie.buildCooke())
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    emmiter.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    val reponseStr: String? = response.body?.string()
                    if (reponseStr == null) {
                        emmiter.onError(Throwable("null"))
                        return
                    }

                    emmiter.onNext(
                        ViolationResult.FromPhatNguoiXe(
                            mapOf(), htmlText = reponseStr
                        )
                    )

                    emmiter.onComplete()
                }
            })
        }
    }
}