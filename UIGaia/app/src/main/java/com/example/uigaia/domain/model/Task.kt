package com.example.uigaia.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task (
    val id: Int,
    val taskName: String,
    val completed: String,
    val username: String,
        ) : Parcelable