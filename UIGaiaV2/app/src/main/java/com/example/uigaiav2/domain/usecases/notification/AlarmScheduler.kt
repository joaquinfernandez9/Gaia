package com.example.uigaiav2.domain.usecases.notification


interface AlarmScheduler {
    fun schedule(item: AlarmItem)
    fun cancel(item: AlarmItem)
}