package com.nhom9.message.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nhom9.message.CommonDivider
import com.nhom9.message.CommonImageRow
import com.nhom9.message.CommonProgressbar
import com.nhom9.message.DestinationScreen
import com.nhom9.message.MViewModel
import com.nhom9.message.TitleBar
import com.nhom9.message.navigateTo

@Composable
fun StatusScreen(navController: NavController, viewModel: MViewModel) {
    val inProgress = viewModel.inProgressStatus.value
    if (inProgress) {
        CommonProgressbar()
    } else {
        val statuses = viewModel.status.value
        val userData = viewModel.userData.value
        val myStatuses = statuses.filter {
            it.user.userId == userData?.userId
        }
        val otherStatuses = statuses.filter {
            it.user.userId != userData?.userId
        }
        val launcher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
                uri?.let {
                    viewModel.uploadStatus(uri)
                }
            }

        Scaffold(
            floatingActionButton = {
                FAB {
                    launcher.launch("image/*")
                }
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    TitleBar(text = "Status")
                    if (statuses.isEmpty()) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            Text(
                                text = "No Statuses Available",
                                style = MaterialTheme.typography.titleMedium,
                            )
                        }
                    } else {
                        if (myStatuses.isNotEmpty()) {
                            CommonImageRow(
                                imageUrl = myStatuses[0].user.imageUrl,
                                name = myStatuses[0].user.name
                            ) {
                                navigateTo(
                                    navController = navController,
                                    DestinationScreen.SingleStatus.createRoute(myStatuses[0].user.userId!!)
                                )
                            }
                            CommonDivider()
                            val uniqueUsers = otherStatuses.map { it.user }.toSet().toList()
                            LazyColumn(modifier = Modifier.weight(1f)) {
                                items(uniqueUsers) { user ->
                                    CommonImageRow(imageUrl = user.imageUrl, name = user.name) {
                                        navigateTo(
                                            navController = navController,
                                            DestinationScreen.SingleStatus.createRoute(user.userId!!)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    BottomNavigationMenu(
                        selectedItem = BottomNavigationItem.STATUSLIST,
                        navController = navController
                    )
                }
            }
        )
    }
}

@Composable
fun FAB(
    onFABClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onFABClick,
        containerColor = MaterialTheme.colorScheme.secondary,
        shape = CircleShape,
        modifier = Modifier.padding(bottom = 40.dp)
    ) {
        Icon(imageVector = Icons.Rounded.Edit, contentDescription = null)
    }
}