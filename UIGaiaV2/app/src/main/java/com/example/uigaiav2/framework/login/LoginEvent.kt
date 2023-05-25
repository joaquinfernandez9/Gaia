package com.example.uigaiav2.framework.login

import com.example.uigaiav2.domain.model.Account
import com.example.uigaiav2.domain.model.dto.AccountDTO

sealed interface LoginEvent {
    class Login(val account: AccountDTO) : LoginEvent
}