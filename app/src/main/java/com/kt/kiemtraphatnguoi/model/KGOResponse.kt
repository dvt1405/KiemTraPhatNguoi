package com.kt.kiemtraphatnguoi.model

import com.google.gson.Gson

class KGOResponse : ArrayList<KgoItem>() {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}

data class KgoItem(
    val contact_number: String,
    val fined_at: String,
    val fined_by_unit: String,
    val fined_place: String,
    val license_plate: String,
    val license_plate_color: String,
    val status: String,
    val type: String
)