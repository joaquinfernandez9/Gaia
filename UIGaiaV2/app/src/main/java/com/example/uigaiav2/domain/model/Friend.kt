package com.example.uigaiav2.domain.model

import java.time.LocalDateTime

data class Friend(
    val username1: String,
    val username2: String,
    val value: Int,
    val requestDate: LocalDateTime,
) {
}