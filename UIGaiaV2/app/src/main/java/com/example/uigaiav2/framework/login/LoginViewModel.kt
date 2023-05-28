package com.example.uigaiav2.framework.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uigaiav2.data.repository.LoginRepository
import com.example.uigaiav2.domain.model.Account
import com.example.uigaiav2.domain.model.dto.AccountDTO
import com.example.uigaiav2.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext val appContext: Context,
    private val repo: LoginRepository,
): ViewModel(){
    private val _state: MutableStateFlow<LoginState> by lazy {
        MutableStateFlow(LoginState())
    }

    val state: StateFlow<LoginState> get() = _state

    fun handleEvent(event: LoginEvent){
        when(event){
            is LoginEvent.Login -> login(event.account)
        }
    }

    fun clearError(){
        _state.value = _state.value.copy(error = null)
    }

    private fun login(acc: AccountDTO) {
        viewModelScope.launch {
            if (Utils.hasInternetConnection(context = appContext)){
                repo.login(acc).collect{
                    when(it){
                        is com.example.uigaiav2.utils.NetworkResult.Error -> {
                            _state.value = _state.value.copy(
                                error = it.message,
                                boolean = false,
                            )
                        }
                        is com.example.uigaiav2.utils.NetworkResult.Success -> {
                            _state.value = _state.value.copy(
                                error = null,
                                boolean = true
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