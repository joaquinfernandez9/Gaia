package com.example.uigaiav2.framework.tree

import com.example.uigaiav2.domain.model.Tree

data class TreeState (
    var error: String? = null,
    var boolean: Boolean = false,
    var tree: Tree? = null,
    var completedTasks: Int? = 0
)