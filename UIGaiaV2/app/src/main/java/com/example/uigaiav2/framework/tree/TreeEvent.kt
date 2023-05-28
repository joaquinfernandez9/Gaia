package com.example.uigaiav2.framework.tree

sealed interface TreeEvent {
        class GetTree(val username: String) : TreeEvent
}