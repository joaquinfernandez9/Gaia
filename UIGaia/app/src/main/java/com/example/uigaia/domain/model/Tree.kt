package com.example.uigaia.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Tree (
    val username: String,
    val level: Int,
    val xp: Int,
) : Parcelable