package com.example.uigaia.framework.compose.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ViewModelLogin @Inject constructor() : ViewModel(){

    private val _state: MutableStateFlow<LoginContract.State> by lazy {
        MutableStateFlow(LoginContract.State())
    }

    val state: StateFlow<LoginContract.State> = _state


}