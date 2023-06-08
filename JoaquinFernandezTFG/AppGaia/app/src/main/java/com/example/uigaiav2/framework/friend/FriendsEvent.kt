package com.example.uigaiav2.framework.friend

sealed interface FriendsEvent {
    //getFriends
    class GetFriends(val username: String) : FriendsEvent
    //deleteFriend
    class DeclineRequest(val myUsername: String, val friendUsername: String) : FriendsEvent
    class AcceptRequest(val myUsername: String, val friendUsername: String) : FriendsEvent
    //addFriend
    class SendRequest(val myUsername: String, val friendUsername: String) : FriendsEvent
    class GetPetitions(val username: String) : FriendsEvent
    //errrorShown
    object ErrorShown : FriendsEvent
}