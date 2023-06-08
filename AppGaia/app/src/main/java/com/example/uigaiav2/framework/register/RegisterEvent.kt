package com.example.uigaiav2.framework.register

import com.example.uigaiav2.domain.model.Account

sealed interface RegisterEvent{
    class Register(val account: Account) : RegisterEvent
}