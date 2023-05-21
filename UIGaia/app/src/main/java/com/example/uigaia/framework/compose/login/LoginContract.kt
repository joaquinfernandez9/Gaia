package com.example.uigaia.framework.compose.login

class LoginContract {
    sealed interface Event{

    }

    data class State(
        val error: String? = null,
        val isLoading: Boolean = true,
        val username: String = "",
        val password: String = "",
    )
}