package com.example.uigaiav2.domain.model.dto

import com.example.uigaiav2.domain.model.Task
import java.time.LocalDateTime
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Local

data class TaskDTO(
    val name: String,
    val initTime: LocalDateTime,
    val endTime: LocalDateTime,
    val username: String,
) {
    fun toTask(): Task {
        return Task(
            taskName = name,
            initTime = initTime,
            endTime = endTime,
            username = username,
            id = 0,
            completed = 0
        )
    }
}