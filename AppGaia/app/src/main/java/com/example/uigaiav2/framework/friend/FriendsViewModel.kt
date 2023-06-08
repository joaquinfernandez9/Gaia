package com.example.uigaiav2.framework.friend

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uigaiav2.data.repository.FriendRepository
import com.example.uigaiav2.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
    @ApplicationContext val appContext: Context,
    private val repoFriend: FriendRepository,
) : ViewModel() {

    private val _state: MutableStateFlow<FriendsState> by lazy {
        MutableStateFlow(FriendsState())
    }

    val state: StateFlow<FriendsState> get() = _state

    fun handleEvent(event: FriendsEvent) {
        when (event) {
            is FriendsEvent.GetFriends -> getFriends(event.username)
            is FriendsEvent.SendRequest -> sendRequest(event.myUsername, event.friendUsername)
            is FriendsEvent.DeclineRequest -> deleteFriend(event.myUsername, event.friendUsername)
            is FriendsEvent.AcceptRequest -> acceptRequest(event.myUsername, event.friendUsername)
            is FriendsEvent.GetPetitions -> getPetitions(event.username)
            is FriendsEvent.ErrorShown -> clearError()
        }
    }

    private fun acceptRequest(myUsername: String, friendUsername: String) {
        viewModelScope.launch {
            if (Utils.hasInternetConnection(context = appContext)) {
                repoFriend.acceptRequest(myUsername, friendUsername).collect {
                    when (it) {
                        is com.example.uigaiav2.utils.NetworkResult.Error -> {
                            _state.value = FriendsState(error = it.message)
                        }
                        is com.example.uigaiav2.utils.NetworkResult.Success -> {
                            _state.value = FriendsState(error = null)
                        }
                        is com.example.uigaiav2.utils.NetworkResult.Loading -> {
                            _state.value = FriendsState(error = null)
                        }
                    }
                }
            } else {
                _state.value = FriendsState(error = "No internet connection")
            }
        }
    }

    private fun getPetitions(username: String) {
        viewModelScope.launch {
            if (Utils.hasInternetConnection(context = appContext)) {
                repoFriend.getRequests(username).collect {
                    when (it) {
                        is com.example.uigaiav2.utils.NetworkResult.Error -> {
                            _state.value = FriendsState(error = it.message)
                        }
                        is com.example.uigaiav2.utils.NetworkResult.Success -> {
                            _state.value = FriendsState(error = null, friends = it.data)
                        }
                        is com.example.uigaiav2.utils.NetworkResult.Loading -> {
                            _state.value = FriendsState(error = null)
                        }
                    }
                }
            } else {
                _state.value = FriendsState(error = "No Internet Connection")
            }
        }
    }

    private fun clearError() {
        _state.value = FriendsState(error = null)
    }

    private fun deleteFriend(myUsername: String, friendUsername: String) {
        viewModelScope.launch {
            if (Utils.hasInternetConnection(context = appContext)) {
                repoFriend.declineRequest(myUsername, friendUsername).collect {
                    when (it) {
                        is com.example.uigaiav2.utils.NetworkResult.Error -> {
                            _state.value = FriendsState(error = it.message)
                        }
                        is com.example.uigaiav2.utils.NetworkResult.Success -> {
                            _state.value = FriendsState(error = null)
                        }
                        is com.example.uigaiav2.utils.NetworkResult.Loading -> {
                            _state.value = FriendsState(error = null)
                        }
                    }
                }
            } else {
                _state.value = FriendsState(error = "No internet connection")
            }
        }
    }

    private fun sendRequest(myUsername: String, friendUSername: String) {
        viewModelScope.launch {
            if (Utils.hasInternetConnection(context = appContext)) {
                repoFriend.sendRequest(myUsername, friendUSername).collect {
                    when (it) {
                        is com.example.uigaiav2.utils.NetworkResult.Error -> {
                            _state.value = FriendsState(error = it.message)
                        }
                        is com.example.uigaiav2.utils.NetworkResult.Success -> {
                            _state.value = FriendsState(error = null)
                        }
                        is com.example.uigaiav2.utils.NetworkResult.Loading -> {
                            _state.value = FriendsState(error = null)
                        }
                    }
                }
            } else {
                _state.value = FriendsState(error = "No internet connection")
            }
        }
    }

    private fun getFriends(username: String) {
        viewModelScope.launch {
            if (Utils.hasInternetConnection(context = appContext)) {
                repoFriend.getFriendsTree(username).collect {
                    when (it) {
                        is com.example.uigaiav2.utils.NetworkResult.Error -> {
                            _state.value = FriendsState(error = it.message)
                        }
                        is com.example.uigaiav2.utils.NetworkResult.Success -> {
                            _state.value = FriendsState(error = null, trees = it.data)
                        }
                        is com.example.uigaiav2.utils.NetworkResult.Loading -> {
                            _state.value = FriendsState(error = null)
                        }
                    }
                }
            } else {
                _state.value = FriendsState(error = "No internet connection")
            }

        }
    }
}