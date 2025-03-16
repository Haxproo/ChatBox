package com.hax.chatbox.model

data class User(
    val id: String = "",
    val username: String = "",
    val email: String = "",
    val profilePictureUrl: String = "",
    val status: String = "Available",
    val lastSeen: Long = System.currentTimeMillis()
)