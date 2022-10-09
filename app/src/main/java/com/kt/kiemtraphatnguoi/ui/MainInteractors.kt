package com.kt.kiemtraphatnguoi.ui

import com.kt.kiemtraphatnguoi.usecase.CheckViolations
import javax.inject.Inject

class MainInteractors @Inject constructor(
    val checkViolations: CheckViolations
) {

}