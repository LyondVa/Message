package com.nhom9.message.screens.chatrequestscreen

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nhom9.message.CommonDivider
import com.nhom9.message.CommonProfileImageRow
import com.nhom9.message.DestinationScreen
import com.nhom9.message.MViewModel
import com.nhom9.message.TitleBarWithBack
import com.nhom9.message.data.ChatRequest
import com.nhom9.message.navigateTo

@Composable
fun ChatRequestScreen(navController: NavController, viewModel: MViewModel) {
    val context = LocalContext.current
    val onDeleteRequest: (String) -> Unit = {
        viewModel.onDeleteRequest(it) {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
        }
    }
    val onAcceptRequest: (ChatRequest) -> Unit = { viewModel.onAcceptRequest(it) }
    Column {
        TitleBarWithBack(navController, "Requests")
        CommonDivider(0)
        LazyColumn {
            item {
                Text(
                    text = "My Request",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(8.dp)
                )
            }
            if (viewModel.myRequests.value.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "No Requests",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.align(
                                Alignment.Center
                            )
                        )
                    }
                }
            } else {
                items(viewModel.myRequests.value) {
                    MyRequestRow(
                        navController = navController,
                        chatRequest = it,
                        onDeleteRequest = onDeleteRequest,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
            item {
                Column {
                    CommonDivider(0)
                    Text(
                        text = "Pending Request",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
            if (viewModel.friendsRequests.value.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "No Requests",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.align(
                                Alignment.Center
                            )
                        )
                    }
                }
            } else {
                items(viewModel.friendsRequests.value) {
                    FriendsRequestRow(
                        navController = navController,
                        chatRequest = it,
                        onDeleteRequest = onDeleteRequest,
                        onAcceptRequest = onAcceptRequest
                    )
                }
            }
            item {
                CommonDivider(0)
            }
        }
    }
}

@Composable
fun FriendsRequestRow(
    modifier: Modifier = Modifier,
    navController: NavController,
    chatRequest: ChatRequest,
    onDeleteRequest: (String) -> Unit,
    onAcceptRequest: (ChatRequest) -> Unit
) {
    Box(modifier = modifier.fillMaxWidth()) {
        CommonProfileImageRow(
            imageUrl = chatRequest.requester?.imageUrl,
            name = chatRequest.requester?.name,
            modifier = Modifier.align(
                Alignment.CenterStart
            )
        ) {
            navigateTo(
                navController = navController,
                DestinationScreen.UserProfile.createRoute(chatRequest.requester?.userId!!, "false")
            )
        }
        Row(modifier = Modifier.align(Alignment.CenterEnd)) {
            IconButton(onClick = { onAcceptRequest.invoke(chatRequest)  }) {
                Icon(imageVector = Icons.Outlined.Check, contentDescription = null)
            }
            IconButton(onClick = { onDeleteRequest.invoke(chatRequest.requestId!!)}) {
                Icon(imageVector = Icons.Outlined.Clear, contentDescription = null)
            }
        }
    }
}

@Composable
fun MyRequestRow(
    modifier: Modifier = Modifier,
    navController: NavController,
    chatRequest: ChatRequest,
    onDeleteRequest: (String) -> Unit
) {
    Box(modifier = modifier.fillMaxWidth()) {
        CommonProfileImageRow(
            imageUrl = chatRequest.requestee?.imageUrl,
            name = chatRequest.requestee?.name,
            modifier = Modifier.align(Alignment.CenterStart),
        ) {
            navigateTo(
                navController = navController, DestinationScreen.UserProfile.createRoute(
                    chatRequest.requestee?.userId!!, "true"
                )
            )
        }
        IconButton(
            onClick = { onDeleteRequest.invoke(chatRequest.requestId!!) },
            modifier = Modifier.align(
                Alignment.CenterEnd
            )
        ) {
            Icon(
                imageVector = Icons.Outlined.Clear, contentDescription = null
            )
        }
    }
}
