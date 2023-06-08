package com.example.uigaiav2.framework.friend

import com.example.uigaiav2.domain.model.Friend
import com.example.uigaiav2.domain.model.Tree

data class FriendsState(
    val error: String? = null,
    val boolean: Boolean? = false,
    val friend: Friend? = null,
    val friends: List<Friend>? = emptyList(),
    val trees: List<Tree>? = emptyList(),
) {
}