package com.example.uigaiav2.domain.model

import com.example.uigaiav2.domain.model.dto.TaskDTO
import java.time.LocalDateTime

data class Task(
    val id: Int,
    val taskName: String,
    val initTime: LocalDateTime,
    val endTime: LocalDateTime,
    val completed: Int,
    val username: String,
) {
    fun toTaskDTO(): TaskDTO {
        return TaskDTO(
            name = taskName,
            initTime = initTime,
            endTime = endTime
        )
    }
}