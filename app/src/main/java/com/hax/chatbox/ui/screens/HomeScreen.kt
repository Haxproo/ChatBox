package com.hax.chatbox.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.hax.chatbox.model.ChatRoom
import com.hax.chatbox.model.Message
import com.hax.chatbox.model.MessageType
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToChat: (String) -> Unit,
    onNavigateToProfile: () -> Unit
) {
    // In a real app, this would come from a ViewModel
    val chatRooms = remember {
        mutableStateListOf(
            ChatRoom(
                id = "1",
                name = "John Doe",
                participantIds = listOf("user1", "user2"),
                lastMessage = Message(
                    content = "Hey, how are you?",
                    timestamp = System.currentTimeMillis() - 3600000,
                    type = MessageType.TEXT
                ),
                isGroup = false
            ),
            ChatRoom(
                id = "2",
                name = "Family Group",
                participantIds = listOf("user1", "user3", "user4"),
                lastMessage = Message(
                    content = "Dinner at 8pm tonight?",
                    timestamp = System.currentTimeMillis() - 7200000,
                    type = MessageType.TEXT
                ),
                isGroup = true
            ),
            ChatRoom(
                id = "3",
                name = "Jane Smith",
                participantIds = listOf("user1", "user5"),
                lastMessage = Message(
                    content = "I'll send you the documents soon",
                    timestamp = System.currentTimeMillis() - 86400000,
                    type = MessageType.TEXT
                ),
                isGroup = false
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ChatBox") },
                actions = {
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Open new chat dialog */ }) {
                Icon(Icons.Default.Add, contentDescription = "New Chat")
            }
        }
    ) { padding ->
        if (chatRooms.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No chats yet. Start a new conversation!")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(chatRooms) { chatRoom ->
                    ChatRoomItem(
                        chatRoom = chatRoom,
                        onClick = { onNavigateToChat(chatRoom.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun ChatRoomItem(
    chatRoom: ChatRoom,
    onClick: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    val timeString = remember(chatRoom.lastMessage?.timestamp) {
        chatRoom.lastMessage?.timestamp?.let { dateFormat.format(Date(it)) } ?: ""
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile picture placeholder
            Surface(
                modifier = Modifier.size(48.dp),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.primary
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = chatRoom.name.first().toString(),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = chatRoom.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = timeString,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = chatRoom.lastMessage?.content ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}