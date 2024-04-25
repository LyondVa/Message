package com.nhom9.message.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nhom9.message.CommonDivider
import com.nhom9.message.CommonImage
import com.nhom9.message.MViewModel
import com.nhom9.message.R
import com.nhom9.message.data.Message
import com.nhom9.message.ui.theme.md_theme_light_onPrimaryContainer
import com.nhom9.message.ui.theme.md_theme_light_primaryContainer

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
        viewModel.depopulateMessages()
        navController.popBackStack()
    }

    Column {
        ChatHeader(name = chatUser.name ?: "", imageUrl = chatUser.imageUrl ?: "") {
            viewModel.depopulateMessages()
            navController.popBackStack()
        }
        CommonDivider(0)
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
    LazyColumn(modifier = modifier) {
        items(chatMessages) { message ->
            val alignment = if (message.sendBy == currentUserId) Alignment.End else Alignment.Start
            val color = md_theme_light_primaryContainer
            Column(
                horizontalAlignment = alignment,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(color)
                ) {
                    Text(
                        text = message.message ?: "",
                        color = md_theme_light_onPrimaryContainer,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(12.dp)

                    )
                    Box {
                        Text(
                            text = "Time",
                            fontSize = 12.sp,
                            modifier = Modifier.align(Alignment.BottomEnd)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatHeader(name: String, imageUrl: String, onBack: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Icon(
            imageVector = Icons.Rounded.ArrowBack,
            contentDescription = null,
            modifier = Modifier
                .clickable { onBack.invoke() }
                .padding(8.dp)
        )
        ProfileBox(name, imageUrl, modifier = Modifier
            .weight(1f)
            .clickable { })
        CallBox()
    }
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
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.AddCircle,
                contentDescription = null,
                modifier = Modifier
                    .clickable { }
                    .padding(8.dp)
            )
            Icon(
                painterResource(id = R.drawable.outline_image_24),
                contentDescription = null,
                modifier = Modifier
                    .clickable { }
                    .padding(8.dp)
            )
            OutlinedTextField(
                value = reply,
                onValueChange = onReplyChange,
                maxLines = 3,
                modifier = Modifier.height(40.dp).weight(0.1f)
            )
            Icon(
                imageVector = Icons.Outlined.Send,
                contentDescription = null,
                modifier = Modifier
                    .clickable { onSendReply.invoke() }
                    .padding(8.dp)
            )
        }
    }
}

@Composable
fun CallBox() {
    Row {
        Icon(
            imageVector = Icons.Outlined.Phone, contentDescription = null,
            modifier = Modifier
                .clickable { }
                .padding(8.dp)
        )
        Icon(
            painterResource(id = R.drawable.outline_video_call_24), contentDescription = null,
            modifier = Modifier
                .clickable { }
                .padding(8.dp)
        )
    }
}

@Composable
fun ProfileBox(name: String, imageUrl: String, modifier: Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .wrapContentHeight()
    ) {
        CommonImage(
            data = imageUrl, modifier = Modifier
                .padding(8.dp)
                .size(52.dp)
                .clip(CircleShape)
        )
        Text(text = name, fontWeight = FontWeight.Bold)

    }
}