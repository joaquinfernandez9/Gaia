package com.example.uigaiav2.framework.task

import com.example.uigaiav2.domain.model.Task
import com.example.uigaiav2.domain.model.dto.TaskDTO

data class TaskState(
    var error: String? = null,
    var boolean: Boolean? = false,
    var task: Task? = null,
    var taskList: List<Task>? = emptyList(),
) {
}