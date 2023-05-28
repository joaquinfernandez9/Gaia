package com.example.uigaiav2.framework.task

import com.example.uigaiav2.domain.model.Task
import com.example.uigaiav2.domain.model.dto.TaskDTO

sealed interface TaskEvent {
    class GetTasks(val username: String) : TaskEvent
    class AddTask(val task: TaskDTO) : TaskEvent
    class DeleteTask(val task: TaskDTO) : TaskEvent
    class UpdateTask(val task: TaskDTO) : TaskEvent
    object ErrorShown : TaskEvent
}