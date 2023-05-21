package com.example.uigaia.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class Friend (
    val firstUser: String,
    val secondUser: String,
    val status: Int,
    val requestDate: LocalDate
        ) : Parcelable