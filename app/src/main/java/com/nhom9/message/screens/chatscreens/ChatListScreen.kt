package com.nhom9.message.screens.chatscreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nhom9.message.CommonDivider
import com.nhom9.message.CommonProfileImageRow
import com.nhom9.message.CommonProgressbar
import com.nhom9.message.DestinationScreen
import com.nhom9.message.MViewModel
import com.nhom9.message.R
import com.nhom9.message.TitleBar
import com.nhom9.message.navigateTo
import com.nhom9.message.screens.BottomNavigationItem
import com.nhom9.message.screens.BottomNavigationMenu
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ChatListScreen(navController: NavController, viewModel: MViewModel) {
    val inProgress = viewModel.inProcessChats.value
    if (inProgress) {
        CommonProgressbar()
    } else {
        val chats = viewModel.chats.value
        val userData = viewModel.userData.value
        val showDialogue = remember {
            mutableStateOf(false)
        }
        val onFABClick: () -> Unit = {
            showDialogue.value = true
        }
        val onDismiss: () -> Unit = {
            showDialogue.value = false
        }
        val enteredOwnPhoneNumber = remember {
            mutableStateOf(false)
        }
        val enteredNothing = remember {
            mutableStateOf(false)
        }
        val scope = rememberCoroutineScope()
        val onAddChat: (String) -> Unit = {
            when (it) {
                viewModel.userData.value?.phoneNumber -> {
                    scope.launch {
                        enteredOwnPhoneNumber.value = true
                        delay(5000)
                        enteredOwnPhoneNumber.value = false
                    }
                }

                "" -> {
                    scope.launch {
                        enteredNothing.value = true
                        delay(5000)
                        enteredNothing.value = false
                    }
                }

                else -> {
                    viewModel.onSendChatRequest(it, {}, {}, {})
                    showDialogue.value = false
                }
            }
        }
        val searchText = remember {
            mutableStateOf(TextFieldValue())
        }
        val search = {
            for (chat in viewModel.chats.value) {
                if (chat.user1.name?.contains(
                        searchText.value.text,
                        ignoreCase = true
                    ) == true || chat.user1.name?.contains(
                        searchText.value.text,
                        ignoreCase = true
                    ) == true
                ) {
                    chats + chat
                }
            }
        }

        Scaffold(
            floatingActionButton = {
                FAB(
                    showDialogue = showDialogue.value,
                    onFABClick = onFABClick,
                    onDismiss = onDismiss,
                    enteredOwnPhoneNumber = enteredOwnPhoneNumber.value,
                    enteredNothing = enteredNothing.value,
                    onAddChat = onAddChat
                )
            },
            content = { it ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    Box {
                        TitleBar(text = stringResource(R.string.messages))
                        IconButton(
                            onClick = {
                                navigateTo(navController, DestinationScreen.ChatRequestScreen.route)
                            },
                            modifier = Modifier.align(Alignment.CenterEnd)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = null
                            )
                        }
                    }
                    CommonDivider(0)
                    OutlinedTextField(
                        value = searchText.value,
                        textStyle = MaterialTheme.typography.labelLarge,
                        onValueChange = {
                            searchText.value = it
                            search.invoke()
                        },
                        shape = MaterialTheme.shapes.large,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                    if (chats.isEmpty()) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            Text(
                                text = stringResource(R.string.no_chats_available),
                                style = MaterialTheme.typography.titleMedium,
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            items(chats) { chat ->
                                val chatUser = if (chat.user1.userId == userData?.userId) {
                                    chat.user2
                                } else {
                                    chat.user1
                                }
                                CommonProfileImageRow(
                                    imageUrl = chatUser.imageUrl,
                                    name = chatUser.name
                                ) {
                                    chat.chatId?.let {
                                        navigateTo(
                                            navController,
                                            DestinationScreen.SingleChat.createRoute(id = it)
                                        )
                                    }

                                }
                                CommonDivider(0)
                            }
                        }
                    }
                    CommonDivider(0)
                    BottomNavigationMenu(
                        selectedItem = BottomNavigationItem.CHATLIST,
                        navController = navController
                    )
                }
            },
        )
    }
}


@Composable
fun FAB(
    showDialogue: Boolean,
    onFABClick: () -> Unit,
    onDismiss: () -> Unit,
    enteredOwnPhoneNumber: Boolean,
    enteredNothing: Boolean,
    onAddChat: (String) -> Unit
) {
    val addChatNumber = remember {
        mutableStateOf("")
    }
    if (showDialogue) {
        AlertDialog(
            onDismissRequest = {
                onDismiss.invoke()
                addChatNumber.value = ""
            },
            confirmButton = {
                Button(onClick = {
                    onAddChat(addChatNumber.value)
                    addChatNumber.value = ""
                }) {
                    Text(
                        text = stringResource(R.string.add_chat),
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            },
            title = {
                Text(
                    text = stringResource(R.string.add_chat),
                    style = MaterialTheme.typography.titleLarge,
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = addChatNumber.value,
                        label = { Text(text = stringResource(id = R.string.phone_number)) },
                        onValueChange = { addChatNumber.value = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    if (enteredOwnPhoneNumber) {
                        Text(
                            text = stringResource(R.string.please_enter_another_number),
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    if (enteredNothing) {
                        Text(
                            text = stringResource(R.string.please_enter_a_phone_number),
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            },
            modifier = Modifier.clip(RoundedCornerShape(8.dp))
        )
    }
    FloatingActionButton(
        onClick = onFABClick,
        containerColor = MaterialTheme.colorScheme.secondary,
        shape = CircleShape,
        modifier = Modifier.padding(bottom = 40.dp)
    ) {
        Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
    }
}
