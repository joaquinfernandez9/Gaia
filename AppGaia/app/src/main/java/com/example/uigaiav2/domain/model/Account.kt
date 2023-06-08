package com.example.uigaiav2.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Account(
    val username: String,
    val password: String,
    val email: String,
): Parcelable {

}