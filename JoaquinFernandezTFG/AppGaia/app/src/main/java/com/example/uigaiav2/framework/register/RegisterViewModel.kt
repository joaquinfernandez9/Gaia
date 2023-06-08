package com.example.uigaiav2.framework.register

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uigaiav2.data.repository.LoginRepository
import com.example.uigaiav2.domain.model.Account
import com.example.uigaiav2.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    @ApplicationContext val appContext: Context,
    private val repo: LoginRepository
) : ViewModel() {
    private val _state: MutableStateFlow<RegisterState> by lazy {
        MutableStateFlow(RegisterState())
    }
    val state: StateFlow<RegisterState> get() = _state
    fun handleEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.Register -> register(event.account)
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }


    private fun register(acc: Account) {
        viewModelScope.launch {
            if (Utils.hasInternetConnection(context = appContext)) {
                repo.register(acc).collect {
                    when (it) {
                        is com.example.uigaiav2.utils.NetworkResult.Success -> {
                            _state.value = _state.value.copy(
                                error = null,
                                boolean = true
                            )
                        }
                        is com.example.uigaiav2.utils.NetworkResult.Error -> {
                            _state.value = _state.value.copy(
                                error = it.message,
                                boolean = false
                            )
                        }
                        is com.example.uigaiav2.utils.NetworkResult.Loading -> {
                            _state.value = _state.value.copy(
                                error = null,
                                boolean = false
                            )
                        }
                    }
                }
            } else {
                _state.value = _state.value.copy(
                    error = "No internet connection",
                )
            }
        }
    }
}