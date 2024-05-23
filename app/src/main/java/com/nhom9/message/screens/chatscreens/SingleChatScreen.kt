package com.nhom9.message.screens.chatscreens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.storageMetadata
import com.nhom9.message.CallBox
import com.nhom9.message.CommonDivider
import com.nhom9.message.CommonProfileImage
import com.nhom9.message.DestinationScreen
import com.nhom9.message.MViewModel
import com.nhom9.message.R
import com.nhom9.message.audiorecorder.AndroidAudioRecorder
import com.nhom9.message.audiorecorder.playback.AndroidAudioPlayer
import com.nhom9.message.data.ChatUser
import com.nhom9.message.data.MESSAGE_AUDIO
import com.nhom9.message.data.MESSAGE_IMAGE
import com.nhom9.message.data.MESSAGE_TEXT
import com.nhom9.message.data.Message
import com.nhom9.message.data.TOP_BAR_HEIGHT
import com.nhom9.message.data.UserData
import com.nhom9.message.getTimeFromTimestamp
import com.nhom9.message.navigateTo
import com.nhom9.message.ui.theme.md_theme_light_onPrimaryContainer
import com.nhom9.message.ui.theme.md_theme_light_primaryContainer
import java.io.File
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun SingleChatScreen(navController: NavController, viewModel: MViewModel, chatId: String) {
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                viewModel.onSendImage(chatId, it)
            }
        }

    var reply by rememberSaveable {
        mutableStateOf("")
    }
    val onSendReply = {
        viewModel.onSendText(chatId, reply)
        reply = ""
    }
    val onImageClick = {
        launcher.launch("image/*")
    }

    val onMessageImageClick: (String) -> Unit = {
        navigateTo(
            navController, DestinationScreen.ChatImage.createRoute(
                URLEncoder.encode(
                    it, StandardCharsets.UTF_8.toString()
                )
            )
        )
    }

    val myUser = viewModel.userData.value
    val currentChat = viewModel.chats.value.first { it.chatId == chatId }
    val chatUser =
        if (myUser?.userId == currentChat.user1.userId) currentChat.user2 else currentChat.user1
    val chatMessages = remember {
        viewModel.chatMessages
    }

    val onHeaderClick = {
        navigateTo(
            navController, DestinationScreen.ChatProfile.createRoute(chatUser.userId!!)
        )
    }

    val onVideoCall = {
        viewModel.proceedService(myUser?.name.toString(), chatId, chatUser.name.toString(), context)
        navigateTo(navController, DestinationScreen.VideoCall.route)
    }

    val onAudioCall = {
        viewModel.proceedService(myUser?.name.toString(), chatId, chatUser.name.toString(), context)
        navigateTo(navController, DestinationScreen.AudioCall.route)
    }

    val onSendAudio: (String, StorageMetadata) -> Unit = { string, metadata ->
        viewModel.onSendAudio(chatId, metadata, Uri.parse(string))
    }
    var isRecording by remember {
        mutableStateOf(false)
    }
    val onRecordChange: (Boolean) -> Unit = {
        isRecording = it
    }
    val onMessageDelete: (Message) -> Unit = {
        viewModel.deleteMessage(chatId, it)
    }
    val onMessageSend = {
        viewModel.onRemoteTokenChange(chatUser.deviceToken.toString())
        Log.d("chatusername", chatUser.name.toString())
        Log.d("chatusertoken", chatUser.deviceToken.toString())
        viewModel.sendMessage(
            myUser?.name.toString(),
            context = context,
            type = "1"
        )
    }

    val onNotifyVideoCall = {
        viewModel.onRemoteTokenChange(chatUser.deviceToken.toString())
        viewModel.sendMessage(
            myUser?.name.toString(),
            context = context,
            type = "2"
        )
    }

    val onNotifyAudioCall = {
        viewModel.onRemoteTokenChange(chatUser.deviceToken.toString())
        viewModel.sendMessage(
            myUser?.name.toString(),
            context = context,
            type = "3"
        )
    }

    val onNotifySendRecord = {
        viewModel.onRemoteTokenChange(chatUser.deviceToken.toString())
        viewModel.sendMessage(
            myUser?.name.toString(),
            context = context,
            type = "4"
        )
    }

    val onNotifySendImage = {
        viewModel.onRemoteTokenChange(chatUser.deviceToken.toString())
        viewModel.sendMessage(
            myUser?.name.toString(),
            context = context,
            type = "5"
        )
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.populateMessages(chatId)
    }

    BackHandler {
        viewModel.depopulateMessages()
        navController.popBackStack()
    }

    Column {
        ChatHeader(
            name = chatUser.name ?: "",
            imageUrl = chatUser.imageUrl ?: "",
            onHeaderClick = onHeaderClick,
            onAudioCallClick = onAudioCall,
            onVideoCallClick = onVideoCall,
            onNotifyVideoCall = onNotifyVideoCall,
            onNotifyAudioCall = onNotifyAudioCall
        ) {
            viewModel.depopulateMessages()
            navController.popBackStack()
        }
        CommonDivider(0)
        MessageBox(
            modifier = Modifier.weight(1f),
            chatMessages = chatMessages.value,
            currentUser = myUser!!,
            chatUser = chatUser,
            onMessageImageClick = onMessageImageClick,
            onMessageDelete = onMessageDelete
        )
        CommonDivider(0)
        if (!isRecording) {
            ReplyBox(
                reply = reply,
                onRecordStart = onRecordChange,
                onReplyChange = { reply = it },
                onSendReply = onSendReply,
                onImageClick = onImageClick,
                onMessageSend = onMessageSend,
                onNotifySendRecord = onNotifySendRecord,
                onNotifySendImage = onNotifySendImage
            )
        } else {
            RecordBar(onRecordChange, onSendAudio)
        }
    }
}

@Composable
fun MessageBox(
    modifier: Modifier,
    chatMessages: List<Message>,
    currentUser: UserData,
    chatUser: ChatUser,
    onMessageImageClick: (String) -> Unit,
    onMessageDelete: (Message) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(chatMessages) {
            val currentUserImageUrl =
                if (it.sendBy == currentUser.userId) currentUser.imageUrl else chatUser.imageUrl
            MessageHolder(
                message = it,
                currentUserId = currentUser.userId!!,
                currentUserImageUrl = currentUserImageUrl,
                onMessageImageClick = onMessageImageClick,
                onMessageDelete = onMessageDelete
            )
        }
    }
}


@Composable
fun DeletedMessage(
    message: Message,
    currentUserId: String?,
    alignment: Alignment.Horizontal
) {
    val color = md_theme_light_primaryContainer
    Column(
        horizontalAlignment = alignment, modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .padding(end = 8.dp)
                .background(
                    color, MaterialTheme.shapes.medium
                )
        ) {
            if (message.sendBy == currentUserId) {
                Text(
                    text = getTimeFromTimestamp(message.timeStamp!!),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                )
                Text(
                    text = stringResource(R.string.message_deleted),
                    color = md_theme_light_onPrimaryContainer,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(12.dp)
                )
            } else {
                Text(
                    text = stringResource(R.string.message_deleted),
                    color = md_theme_light_onPrimaryContainer,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(12.dp)
                )
                Text(
                    text = getTimeFromTimestamp(message.timeStamp!!),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                )
            }
        }
    }
}

@Composable
fun MessageHolder(
    message: Message,
    currentUserId: String,
    currentUserImageUrl: String?,
    onMessageImageClick: (String) -> Unit,
    onMessageDelete: (Message) -> Unit
) {
    message.let {
        val alignment = if (it.sendBy == currentUserId) Alignment.End else Alignment.Start
        Column(
            horizontalAlignment = alignment, modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            if (message.sendBy == currentUserId) {
                Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(8.dp)) {
                    TimeStamp(message = it)
                    if (it.isDeleted) {
                        DeletedMessage(
                            message = it,
                            currentUserId = currentUserId,
                            alignment = alignment
                        )
                    } else {
                        Message(
                            message = it,
                            onMessageImageClick = onMessageImageClick,
                            onMessageDelete = onMessageDelete,
                        )
                    }
                    MessageProfileImage(imageUrl = currentUserImageUrl)
                }
            } else {
                Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(8.dp)) {
                    MessageProfileImage(imageUrl = currentUserImageUrl)
                    if (it.isDeleted) {
                        DeletedMessage(
                            message = it,
                            currentUserId = currentUserId,
                            alignment = alignment
                        )
                    } else {
                        Message(
                            message = it,
                            onMessageImageClick = onMessageImageClick,
                            onMessageDelete = onMessageDelete,
                        )
                    }
                    TimeStamp(message = it)
                }
            }
        }
    }
}

@Composable
fun Message(
    message: Message,
    onMessageImageClick: (String) -> Unit,
    onMessageDelete: (Message) -> Unit
) {
    val color = md_theme_light_primaryContainer
    var mDisplayMenu by remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier
            .padding(4.dp)
            .background(
                color, MaterialTheme.shapes.medium
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        mDisplayMenu = !mDisplayMenu
                    }
                )
            }

    ) {
        when (message.type) {
            MESSAGE_TEXT -> Text(
                text = message.content ?: "",
                color = md_theme_light_onPrimaryContainer,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(12.dp)
            )

            MESSAGE_IMAGE -> AsyncImage(model = message.content,
                contentDescription = "null",
                modifier = Modifier
                    .padding(12.dp)
                    .clickable { onMessageImageClick(message.content!!) })

            MESSAGE_AUDIO -> AudioMessage(message.content!!)
        }
    }
    DropdownMenu(
        expanded = mDisplayMenu,
        onDismissRequest = { mDisplayMenu = false }
    ) {
        DropdownMenuItem(
            {
                Text(
                    text = stringResource(R.string.delete),
                    style = MaterialTheme.typography.labelMedium
                )
            },
            {
                onMessageDelete.invoke(message)
                mDisplayMenu = false
                Log.d("MenuItem", message.content.toString())
            }
        )
    }
}

@Composable
fun ChatHeader(
    name: String,
    imageUrl: String,
    onHeaderClick: () -> Unit,
    onAudioCallClick: () -> Unit,
    onVideoCallClick: () -> Unit,
    onNotifyVideoCall: () -> Unit,
    onNotifyAudioCall: () -> Unit,
    onBackClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(TOP_BAR_HEIGHT.dp)
            .fillMaxWidth()
    ) {
        IconButton(onClick = { onBackClick.invoke() }) {
            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = null)
        }
        ProfileBox(name,
            imageUrl,
            modifier = Modifier
                .weight(1f)
                .clickable { onHeaderClick.invoke() })
        CallBox(onAudioCallClick, onVideoCallClick, onNotifyVideoCall, onNotifyAudioCall)
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ReplyBox(
    reply: String,
    onRecordStart: (Boolean) -> Unit,
    onReplyChange: (String) -> Unit,
    onSendReply: () -> Unit,
    onImageClick: () -> Unit,
    onMessageSend: () -> Unit,
    onNotifySendRecord: () -> Unit,
    onNotifySendImage: () -> Unit
) {
    val context = LocalContext.current
    val permissionState = rememberPermissionState(permission = Manifest.permission.RECORD_AUDIO)
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            IconButton(onClick = {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.RECORD_AUDIO
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    onRecordStart.invoke(true)
                    onNotifySendRecord.invoke()
                } else {
                    permissionState.launchPermissionRequest()
                }
            }) {
                Icon(painterResource(id = R.drawable.outline_mic_24), contentDescription = null)
            }
            IconButton(onClick = { onImageClick.invoke()
            onNotifySendImage.invoke()}) {
                Icon(
                    painterResource(id = R.drawable.outline_image_24),
                    contentDescription = null
                )
            }
            OutlinedTextField(
                value = reply,
                onValueChange = onReplyChange,
                maxLines = 3,
                modifier = Modifier
                    .weight(0.1f)
            )
            IconButton(onClick = {
                if (reply != "") {
                    onSendReply.invoke()
                    onMessageSend.invoke()
                }
            }) {
                Icon(imageVector = Icons.AutoMirrored.Outlined.Send, contentDescription = null)
            }
        }
    }
}


@Composable
fun ProfileBox(name: String, imageUrl: String, modifier: Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = modifier.wrapContentHeight()
    ) {
        CommonProfileImage(
            imageUrl = imageUrl, modifier = Modifier
                .padding(8.dp)
                .size(52.dp)
                .clip(CircleShape)
        )
        Text(
            text = name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

    }
}

@Composable
fun RecordBar(onRecordStop: (Boolean) -> Unit, onSendAudio: (String, StorageMetadata) -> Unit) {
    val context = LocalContext.current
    var isPaused by remember {
        mutableStateOf(false)
    }
    val recorder by lazy {
        AndroidAudioRecorder(context)
    }
    var audioFile: File
    val metadata = storageMetadata {
        setContentType("audio/aac")
    }
    File(context.cacheDir, "audio.mp3").also {
        recorder.start(it)
        audioFile = it
    }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        IconButton(onClick = {
            audioFile.delete()
            recorder.stop()
            isPaused = true
            onRecordStop.invoke(false)
        }) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_pause_24),
                contentDescription = null
            )
        }
        Text(text = stringResource(R.string.recording))
        IconButton(onClick = {
            recorder.stop()
            onSendAudio.invoke(audioFile.toURI().toString(), metadata)
            onRecordStop.invoke(false)
        }) {
            Icon(imageVector = Icons.AutoMirrored.Outlined.Send, contentDescription = null)
        }
    }
}

@Composable
fun AudioMessage(content: String) {
    val context = LocalContext.current
    val player by lazy {
        AndroidAudioPlayer(context)
    }
    var hasStarted by remember {
        mutableStateOf(false)
    }
    Row {
        if (!hasStarted) {
            IconButton(onClick = {
                player.playFromUrl(content)
                hasStarted = true
            }) {
                Icon(Icons.Outlined.PlayArrow, contentDescription = null)
            }
        } else {
            IconButton(onClick = {
                player.stop()
                hasStarted = false
            }) {
                Icon(
                    painterResource(id = R.drawable.baseline_pause_24),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun TimeStamp(message: Message) {
    Text(
        text = getTimeFromTimestamp(message.timeStamp!!),
        style = MaterialTheme.typography.labelSmall,
        modifier = Modifier.padding(4.dp)
    )
}

@Composable
fun MessageProfileImage(imageUrl: String?) {
    Card(
        shape = CircleShape,
        modifier = Modifier
            .size(20.dp)
    ) {
        CommonProfileImage(imageUrl = imageUrl)
    }
}