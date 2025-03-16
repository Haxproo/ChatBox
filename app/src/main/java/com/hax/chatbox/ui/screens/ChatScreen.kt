package com.hax.chatbox.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.hax.chatbox.model.Message
import com.hax.chatbox.model.MessageType
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    chatId: String,
    onNavigateBack: () -> Unit
) {
    // In a real app, this would come from a ViewModel
    val chatName = remember { "John Doe" }
    val currentUserId = remember { "user1" }
    
    val messages = remember {
        mutableStateListOf(
            Message(
                id = "1",
                senderId = "user2",
                content = "Hey, how are you?",
                timestamp = System.currentTimeMillis() - 3600000,
                type = MessageType.TEXT
            ),
            Message(
                id = "2",
                senderId = "user1",
                content = "I'm good, thanks! How about you?",
                timestamp = System.currentTimeMillis() - 3500000,
                type = MessageType.TEXT
            ),
            Message(
                id = "3",
                senderId = "user2",
                content = "Doing well. What are your plans for the weekend?",
                timestamp = System.currentTimeMillis() - 3400000,
                type = MessageType.TEXT
            ),
            Message(
                id = "4",
                senderId = "user1",
                content = "Nothing much planned yet. Maybe catch a movie.",
                timestamp = System.currentTimeMillis() - 3300000,
                type = MessageType.TEXT
            )
        )
    }
    
    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(chatName) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                state = listState,
                reverseLayout = false
            ) {
                items(messages) { message ->
                    MessageItem(
                        message = message,
                        isFromCurrentUser = message.senderId == currentUserId
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = { Text("Type a message") },
                    modifier = Modifier.weight(1f),
                    maxLines = 3
                )
                
                IconButton(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            val newMessage = Message(
                                id = UUID.randomUUID().toString(),
                                senderId = currentUserId,
                                content = messageText,
                                timestamp = System.currentTimeMillis(),
                                type = MessageType.TEXT
                            )
                            messages.add(newMessage)
                            messageText = ""
                            coroutineScope.launch {
                                listState.animateScrollToItem(messages.size - 1)
                            }
                        }
                    },
                    enabled = messageText.isNotBlank()
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Send")
                }
            }
        }
    }
}

@Composable
fun MessageItem(
    message: Message,
    isFromCurrentUser: Boolean
) {
    val dateFormat = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    val timeString = remember(message.timestamp) {
        dateFormat.format(Date(message.timestamp))
    }
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isFromCurrentUser) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isFromCurrentUser) 16.dp else 4.dp,
                        bottomEnd = if (isFromCurrentUser) 4.dp else 16.dp
                    )
                )
                .background(
                    if (isFromCurrentUser) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surfaceVariant
                )
                .padding(12.dp)
        ) {
            Text(
                text = message.content,
                color = if (isFromCurrentUser) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Text(
                text = timeString,
                style = MaterialTheme.typography.bodySmall,
                color = if (isFromCurrentUser) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                        else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}