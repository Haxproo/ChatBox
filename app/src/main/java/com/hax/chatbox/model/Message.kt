package com.hax.chatbox.model

data class Message(
    val id: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val chatRoomId: String = "",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val type: MessageType = MessageType.TEXT
)

enum class MessageType {
    TEXT, IMAGE, VIDEO, AUDIO, FILE
}