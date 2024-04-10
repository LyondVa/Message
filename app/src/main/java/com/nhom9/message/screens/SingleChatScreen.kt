package com.nhom9.message.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nhom9.message.CommonDivider
import com.nhom9.message.CommonImage
import com.nhom9.message.MViewModel
import com.nhom9.message.data.Message

@Composable
fun SingleChatScreen(navController: NavController, viewModel: MViewModel, chatId: String) {
    var reply by rememberSaveable {
        mutableStateOf("")
    }
    val onSendReply = {
        viewModel.onSendReply(chatId, reply)
        reply = ""
    }
    val myUser = viewModel.userData.value
    var currentChat = viewModel.chats.value.first { it.chatId == chatId }
    val chatUser =
        if (myUser?.userId == currentChat.user1.userId) currentChat.user2 else currentChat.user1
    var chatMessages = viewModel.chatMessages

    LaunchedEffect(key1 = Unit) {
        viewModel.populateMessages(chatId)
    }

    BackHandler() {
        // Code to execute when the back button is pressed
        viewModel.depopulateMessages()
        navController.popBackStack()
    }

    Column {
        ChatHeader(name = chatUser.name ?: "", imageUrl = chatUser.imageUrl ?: "") {
            navController.popBackStack()
            viewModel.depopulateMessages()
        }
        MessageBox(
            modifier = Modifier.weight(1f),
            chatMessages = chatMessages.value,
            currentUserId = myUser?.userId ?: ""
        )
        ReplyBox(reply = reply, onReplyChange = { reply = it }, onSendReply = onSendReply)
    }
}

@Composable
fun MessageBox(modifier: Modifier, chatMessages: List<Message>, currentUserId: String?) {
    LazyColumn(modifier = Modifier) {
        items(chatMessages) { message ->
            val alignment = if (message.sendBy == currentUserId) Alignment.End else Alignment.Start
            val color = if (message.sendBy == currentUserId) Color.Green else Color.Cyan
            Column(
                horizontalAlignment =alignment,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = message.message ?: "",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(color)
                        .padding(12.dp)
                )
            }
        }
    }
}

@Composable
fun ChatHeader(name: String, imageUrl: String, onBack: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Icon(
            imageVector = Icons.Rounded.ArrowBack,
            contentDescription = null,
            modifier = Modifier
                .clickable { onBack.invoke() }
                .padding(8.dp)
        )
        CommonImage(
            data = imageUrl, modifier = Modifier
                .padding(8.dp)
                .size(52.dp)
                .clip(CircleShape)
        )
    }
    Text(
        text = name,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 4.dp)
    )
}

@Composable
fun ReplyBox(reply: String, onReplyChange: (String) -> Unit, onSendReply: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        CommonDivider()
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            TextField(value = reply, onValueChange = onReplyChange, maxLines = 3)
            Button(onClick = onSendReply) {
                Text(text = "Send")
            }
        }
    }
}