package com.kt.kiemtraphatnguoi.utils

fun Map<String, String>.buildCooke(): String {
    val strBuilder = StringBuilder()
    for (i in this.entries) {
        strBuilder.append(i.key)
            .append("=")
            .append(i.value)
            .append(";")
            .append(" ")
    }
    return strBuilder.toString()
        .trim()
        .removeSuffix(";")
        .trim()
}