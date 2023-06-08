package com.example.uigaiav2.domain.model.dto

import android.os.Parcelable
import com.example.uigaiav2.domain.model.Account
import kotlinx.parcelize.Parcelize

@Parcelize
data class AccountDTO(
    val username: String,
    val password: String,
) : Parcelable {
    fun toAccount(): Account {
        return Account(
            username = username,
            password = password,
            email = "",
        )
    }
}