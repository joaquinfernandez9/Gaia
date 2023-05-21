package com.example.uigaia.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalTime

@Parcelize
data class Account (
    val email: String,
    val password: String,
    val username: String,
//    val activated: Int,
//    val activationCode: String,
//    val activationTime: LocalTime
        ): Parcelable