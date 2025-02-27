package com.nhom9.message

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.Timestamp
import com.nhom9.message.data.TOP_BAR_HEIGHT

fun navigateTo(navController: NavController, route: String) {
    navController.navigate(route) {
        popUpTo(route)
        launchSingleTop = true
    }
}

@Composable
fun CommonProgressbar() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .alpha(0.5f)
            .background(Color.LightGray)
            .fillMaxSize()
    )
    {
        CircularProgressIndicator()
    }
}

@Composable
fun CommonDivider(padding: Int = 8) {
    Divider(
        color = Color.LightGray,
        thickness = 1.dp,
        modifier = Modifier
            .alpha(0.3f)
            .padding(top = padding.dp, bottom = padding.dp)
    )
}

@Composable
fun CommonImage(
    data: String?,
    modifier: Modifier = Modifier.wrapContentSize(),
    contentScale: ContentScale = ContentScale.Crop
) {
    AsyncImage(
        model = data,
        contentScale = contentScale,
        contentDescription = "null",
        modifier = modifier
    )
}

@Composable
fun CheckSignedIn(viewModel: MViewModel, navController: NavController) {
    val alreadySignedIn = remember { mutableStateOf(false) }
    val signIn = viewModel.signIn.value
    if (signIn && !alreadySignedIn.value) {
        alreadySignedIn.value = true
        navController.navigate(DestinationScreen.ChatList.route) {
            popUpTo(0)
        }
    }
}

@Composable
fun TitleBar(text: String) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(8.dp)
        )
    }

}

@Composable
fun TitleBarWithBack(navController: NavController, text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(TOP_BAR_HEIGHT.dp)
    ) {
        IconButton(
            modifier = Modifier.align(Alignment.CenterStart),
            onClick = { navController.popBackStack() }
        ) {
            Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = null)
        }
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.Center)
        )
    }

}

@Composable
fun TitleBarWithBackAndRightButton(
    navController: NavController,
    text: String,
    buttonText: String,
    onButtonClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(TOP_BAR_HEIGHT.dp)
    ) {
        IconButton(
            modifier = Modifier.align(Alignment.CenterStart),
            onClick = { navController.popBackStack() }
        ) {
            Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = null)
        }
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineSmall,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.Center)
        )
        Button(
            onClick = {
                onButtonClick.invoke()
                navController.popBackStack()
            },
            modifier = Modifier
                .align(Alignment.CenterEnd)
        ) {
            Text(
                text = buttonText,
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}

@Composable
fun TitleBarWithBackAndRightIcon(
    navController: NavController,
    text: String,
    imageVector: ImageVector? = null,
    painterResource: Int? = null,
    onButtonClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(TOP_BAR_HEIGHT.dp)
            .fillMaxWidth()
    ) {
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.CenterStart)
        ) {
            Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = null)
        }
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineSmall,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.Center)
        )
        IconButton(
            onClick = { onButtonClick.invoke() },
            modifier = Modifier
                .align(Alignment.CenterEnd)
        ) {
            if (imageVector != null) {
                Icon(imageVector = Icons.Outlined.MoreVert, contentDescription = null)
            } else {
                Icon(painter = painterResource(id = painterResource!!), contentDescription = null)
            }

        }
    }
}

@Composable
fun CommonProfileImageRow(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    name: String?,
    onItemClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(75.dp)
            .clickable {
                onItemClick.invoke()
            },
    ) {
        CommonProfileImage(
            imageUrl = imageUrl,
            modifier = Modifier
                .padding(8.dp)
                .size(50.dp)
                .clip(CircleShape)
                .background(Color.Red)
        )
        Text(
            text = name ?: "---",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@Composable
fun CommonRow(
    name: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    clickEnabled: Boolean = true,
    onItemClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable(enabled = clickEnabled) {
                onItemClick.invoke()
            }
    ) {
        if (icon != null) {
            Icon(icon, null, modifier = Modifier.padding(start = 20.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(start = 8.dp)
            )
        } else {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 20.dp)
            )
        }
    }
}

@Composable
fun CommonSubSettingRow(
    title: String,
    text: String = "",
    onItemClick: () -> Unit = {},
    clickEnabled: Boolean = true,
) {
    Box {
        CommonRow(name = title, clickEnabled = clickEnabled) {
            onItemClick.invoke()
        }
        Row(modifier = Modifier.align(Alignment.CenterEnd)) {
            if (text != "") {
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium,
                )
            } else {
                Text(
                    text = stringResource(R.string.error),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Icon(imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight, contentDescription = null)
        }
    }
}

@Composable
fun CallBox(onAudioCallClick: () -> Unit, onVideoCallClick: () -> Unit, onNotifyVideoCall: () -> Unit, onNotifyAudioCall: () -> Unit) {
    Row {
        IconButton(
            onClick = {onAudioCallClick.invoke()
            onNotifyAudioCall.invoke()}
        ) {
            Icon(imageVector = Icons.Outlined.Phone, contentDescription = null)
        }
        IconButton(
            onClick = {onVideoCallClick.invoke()
            onNotifyVideoCall.invoke()}
        ) {
            Icon(painterResource(id = R.drawable.outline_video_call_24), contentDescription = null)
        }
    }
}


fun getTimeFromTimestamp(timestamp: Timestamp): String {
    val time = timestamp.toDate().toString()
    return time.substring(11, 16)
}

@Composable
fun ProfileImageCard(
    isInEdit: Boolean = true,
    imageUrl: String?,
    onChangeImage: (Uri) -> Unit
) {
    var localImageUrl by remember { mutableStateOf<Uri?>(null) }
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                localImageUrl = it
            }
        }
    Card(
        shape = CircleShape,
        modifier = Modifier
            .size(160.dp)
            .clickable(isInEdit) {
                launcher.launch("image/*")
            }
    ) {
        if (isInEdit) {
            ProfileImagePreview(localImageUrl = localImageUrl, data = imageUrl)
            if (localImageUrl != null) {
                onChangeImage.invoke(localImageUrl!!)
            }
        } else CommonProfileImage(imageUrl = imageUrl)
    }
}

@Composable
fun ProfileImagePreview(
    localImageUrl: Uri?,
    data: String?,
) {
    if (localImageUrl != null) {
        CommonImage(data = localImageUrl.toString())
    } else CommonProfileImage(imageUrl = data)
}

@Composable
fun CommonProfileImage(imageUrl: String?, modifier: Modifier = Modifier) {
    if (imageUrl == null || imageUrl == "") {
        Image(
            painter = painterResource(id = R.drawable.blank_profile_picture),
            contentDescription = null,
            modifier = modifier.wrapContentSize()
        )
    } else {
        CommonImage(data = imageUrl, modifier = modifier)
    }
}

fun Context.getActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}
