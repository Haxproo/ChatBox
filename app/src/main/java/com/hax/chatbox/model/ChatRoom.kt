package com.hax.chatbox.model

data class ChatRoom(
    val id: String = "",
    val name: String = "",
    val participantIds: List<String> = emptyList(),
    val lastMessage: Message? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val isGroup: Boolean = false,
    val adminIds: List<String> = emptyList()
)