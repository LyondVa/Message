package com.nhom9.message.screens.chatscreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nhom9.message.CallBox
import com.nhom9.message.CommonDivider
import com.nhom9.message.CommonImage
import com.nhom9.message.CommonProfileImage
import com.nhom9.message.DestinationScreen
import com.nhom9.message.MViewModel
import com.nhom9.message.R
import com.nhom9.message.data.TOP_BAR_HEIGHT
import com.nhom9.message.navigateTo
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun ChatProfileScreen(navController: NavController, viewModel: MViewModel, userId: String) {
    val chatUser = viewModel.getChatUser(userId)
    val photoIds = rememberSaveable {
        mutableListOf<String>()
    }
    val context = LocalContext.current
    val chatId = rememberSaveable {
        mutableStateOf(viewModel.getChatId(userId))
    }
    val myUser = viewModel.userData.value
    val onAudioCall = {
        chatId.value?.let {
            viewModel.proceedService(
                myUser?.name.toString(), it, chatUser?.name.toString(), context
            )
        }
        navigateTo(navController, DestinationScreen.AudioCall.route)
    }
    val onVideoCall = {
        chatId.value?.let {
            viewModel.proceedService(
                myUser?.name.toString(), it, chatUser?.name.toString(), context
            )
        }
        navigateTo(navController, DestinationScreen.AudioCall.route)
    }
    val launchCheck = remember {
        mutableStateOf(true)
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

    val onNotifyVideoCall = {
        viewModel.onRemoteTokenChange(chatUser?.deviceToken.toString())
        viewModel.sendMessage(
            myUser?.name.toString(), context = context, type = "2"
        )
    }

    val onNotifyAudioCall = {
        viewModel.onRemoteTokenChange(chatUser?.deviceToken.toString())
        viewModel.sendMessage(
            myUser?.name.toString(), context = context, type = "3"
        )
    }
    LaunchedEffect(key1 = launchCheck) {
        viewModel.getChatPhotos(photoIds)
        launchCheck.value = false
    }
    Column {
        HeaderBar(
            navController = navController,
            userId = userId,
            onAudioCall,
            onVideoCall,
            onNotifyVideoCall,
            onNotifyAudioCall
        )
        CommonDivider(0)
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 128.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp),
            horizontalArrangement = Arrangement.spacedBy(3.dp),
        ) {
            item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                Column {
                    Surface(
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .height(intrinsicSize = IntrinsicSize.Min)
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        Column {
                            ChatUserCard(chatUser?.name!!, chatUser.imageUrl)
                            CommonDivider(0)
                            ProfileInfoCard("not implemented", chatUser.phoneNumber!!)
                            CommonDivider(0)
                        }
                    }
                }
            }
            items(photoIds) {
                Surface(
                    tonalElevation = 3.dp, modifier = Modifier.aspectRatio(1f)
                ) {
                    CommonImage(data = it,
                        modifier = Modifier.clickable { onMessageImageClick.invoke(it) })
                }
            }
        }
    }
}

@Composable
fun ChatUserCard(name: String, imageUrl: String?) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Card(
            shape = CircleShape, modifier = Modifier.size(160.dp)
        ) {
            CommonProfileImage(imageUrl = imageUrl)
        }
        Text(
            text = name,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun ProfileInfoCard(bio: String, phoneNumber: String) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        /*Text(
            text = "Bio: $bio",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(8.dp)
        )*/
        Text(
            text = stringResource(id = R.string.phone_number) + ": $phoneNumber",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(8.dp)
        )
    }

}

@Composable
fun HeaderBar(
    navController: NavController,
    userId: String,
    onAudioCallClick: () -> Unit,
    onVideoCallClick: () -> Unit,
    onNotifyVideoCall: () -> Unit,
    onNotifyAudioCall: () -> Unit
) {
    val onReportClick = {
        navigateTo(navController, DestinationScreen.Report.createRoute(userId))
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(TOP_BAR_HEIGHT.dp)
    ) {
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            CallBox(onAudioCallClick, onVideoCallClick, onNotifyVideoCall, onNotifyAudioCall)
            DropDownMenuButton(onReportClick)
        }
    }
}

@Composable
fun DropDownMenuButton(onReportClick: () -> Unit) {
    val context = LocalContext.current
    var mDisplayMenu by remember { mutableStateOf(false) }
    IconButton(onClick = { mDisplayMenu = !mDisplayMenu }) {
        Icon(Icons.Outlined.MoreVert, "")
    }
    DropdownMenu(expanded = mDisplayMenu, onDismissRequest = { mDisplayMenu = false }) {
        /*DropdownMenuItem(
            {
                Text(
                    text = "Block",
                    style = MaterialTheme.typography.labelMedium
                )
            },
            {
                Toast.makeText(context, "Block", Toast.LENGTH_SHORT).show()
                mDisplayMenu = false
            }
        )*/
        DropdownMenuItem({
            Text(
                text = stringResource(R.string.report),
                style = MaterialTheme.typography.labelMedium
            )
        }, {
            onReportClick.invoke()
            mDisplayMenu = false
        })
    }
}

@Composable
private fun PhotoGrid(photoIds: MutableList<String>, onMessageImageClick: (String) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        items(photoIds) {
            Surface(
                tonalElevation = 3.dp, modifier = Modifier.aspectRatio(1f)
            ) {
                CommonImage(data = it,
                    modifier = Modifier.clickable { onMessageImageClick.invoke(it) })
            }
        }

    }
}