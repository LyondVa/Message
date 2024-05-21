package com.nhom9.message.screens

import android.Manifest
import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.rounded.ArrowBack
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
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
import com.nhom9.message.data.MESSAGE_AUDIO
import com.nhom9.message.data.MESSAGE_IMAGE
import com.nhom9.message.data.MESSAGE_TEXT
import com.nhom9.message.data.Message
import com.nhom9.message.data.TOP_BAR_HEIGHT
import com.nhom9.message.getTimeFromTimestamp
import com.nhom9.message.navigateTo
import com.nhom9.message.ui.theme.md_theme_light_onPrimaryContainer
import com.nhom9.message.ui.theme.md_theme_light_primaryContainer
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
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
        viewModel.sendMessage(isBroadcast = false, myUser?.name.toString(), "1")
    }

    val onNotifyVideoCall = {
        viewModel.onRemoteTokenChange(chatUser.deviceToken.toString())
        viewModel.sendMessage(isBroadcast = false, myUser?.name.toString(), "2")
    }

    val onNotifyAudioCall = {
        viewModel.onRemoteTokenChange(chatUser.deviceToken.toString())
        viewModel.sendMessage(isBroadcast = false, myUser?.name.toString(), "3")
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
            currentUserId = myUser?.userId ?: "",
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
                onMessageSend = onMessageSend
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
    currentUserId: String?,
    onMessageImageClick: (String) -> Unit,
    onMessageDelete: (Message) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(chatMessages) {
            val alignment = if (it.sendBy == currentUserId) Alignment.End else Alignment.Start
            if (it.isDeleted) {
                DeletedMessage(message = it, currentUserId = currentUserId, alignment = alignment)
            } else {
                Message(
                    message = it,
                    currentUserId = currentUserId!!,
                    onMessageImageClick = onMessageImageClick,
                    alignment = alignment,
                    onMessageDelete = onMessageDelete
                )

            }
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
                    text = "Message Deleted",
                    color = md_theme_light_onPrimaryContainer,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(12.dp)
                )
            } else {
                Text(
                    text = "Message Deleted",
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
fun Message(
    message: Message,
    currentUserId: String?,
    alignment: Alignment.Horizontal,
    onMessageImageClick: (String) -> Unit,
    onMessageDelete: (Message) -> Unit
) {
    val color = md_theme_light_primaryContainer
    var mDisplayMenu by remember { mutableStateOf(false) }
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
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            mDisplayMenu = !mDisplayMenu
                        }
                    )
                }

        ) {
            if (message.sendBy == currentUserId) {
                Text(
                    text = getTimeFromTimestamp(message.timeStamp!!),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                )
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
            } else {
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
                Text(
                    text = getTimeFromTimestamp(message.timeStamp!!),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                )
            }
        }
    }
    DropdownMenu(
        expanded = mDisplayMenu,
        onDismissRequest = { mDisplayMenu = false }
    ) {
        DropdownMenuItem(
            {
                Text(
                    text = "Delete",
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
fun ChatHeader(name: String, imageUrl: String, onHeaderClick: () -> Unit, onAudioCallClick: () -> Unit, onVideoCallClick: () -> Unit, onNotifyVideoCall: () -> Unit, onNotifyAudioCall: () -> Unit, onBackClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(TOP_BAR_HEIGHT.dp)
            .fillMaxWidth()
    ) {
        IconButton(onClick = { onBackClick.invoke() }) {
            Icon(Icons.Rounded.ArrowBack, contentDescription = null)
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
    onMessageSend: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
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

            }) {
                Icon(painterResource(id = R.drawable.outline_mic_24), contentDescription = null)
            }
            IconButton(onClick = { onImageClick.invoke() }) {
                Icon(painterResource(id = R.drawable.outline_image_24), contentDescription = null)
            }
            OutlinedTextField(
                value = reply,
                onValueChange = onReplyChange,
                maxLines = 3,
                modifier = Modifier
                    .height(40.dp)
                    .weight(0.1f)
            )
            IconButton(onClick = {
                if (reply != "") {
                    onSendReply.invoke()
                    onMessageSend.invoke()
                }
            }) {
                Icon(imageVector = Icons.Outlined.Send, contentDescription = null)
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
            text = name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold
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
            Icon(imageVector = Icons.Outlined.Send, contentDescription = null)
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