package com.example.uigaiav2.domain.usecases.notification

import java.time.LocalDateTime

data class AlarmItem (
    val time: LocalDateTime,
    val message: String
        ){
}