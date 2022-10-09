package com.kt.kiemtraphatnguoi.model

sealed class ViolationResult {
    class FromPhatNguoiXe(
        val data: Map<String, String>,
        val htmlText: String
    ) : ViolationResult()

    class FromKGO(val response: KGOResponse) : ViolationResult()
}

