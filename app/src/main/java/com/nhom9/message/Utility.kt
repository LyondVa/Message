package com.nhom9.message

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
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.nhom9.message.ui.theme.bar_gray

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
        contentDescription = "Profile Image Preview",
        modifier = modifier
    )
}

@Composable
fun CheckSignedIn(viewModel: MViewModel, navController: NavController) {
    var alreadySignedIn = remember { mutableStateOf(false) }
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
            .background(color = bar_gray)
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
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
            .background(color = bar_gray)
    ) {
        Icon(
            imageVector = Icons.Outlined.ArrowBack,
            contentDescription = null,
            modifier = Modifier.align(Alignment.CenterStart).padding(16.dp).clickable { navController.popBackStack() }
        )
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
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
            .background(color = bar_gray)
    ) {
        Icon(
            imageVector = Icons.Outlined.ArrowBack,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp)
                .clickable { navController.popBackStack() }
        )
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
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
            Text(text = buttonText)
        }
    }

}

@Composable
fun CommonImageRow(imageUrl: String?, name: String?, onItemClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .clickable {
                onItemClick.invoke()
            }
    ) {
        CommonImage(
            data = imageUrl,
            modifier = Modifier
                .padding(8.dp)
                .size(50.dp)
                .clip(CircleShape)
                .background(Color.Red)
        )
        Text(
            text = name ?: "---",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@Composable
fun CommonSettingRow(
    name: String,
    icon: ImageVector? = null,
    onItemClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color.White)
            .clickable {
                onItemClick.invoke()
            }
    ) {
        if (icon != null) {
            Icon(icon, null, modifier = Modifier.padding(start = 20.dp))
            Text(
                text = name,
                modifier = Modifier
                    .padding(start = 8.dp)
            )
        } else {
            Text(
                text = name,
                modifier = Modifier.padding(start = 20.dp)
            )
        }
    }
}

@Composable
fun CommonSubSettingRow(title: String, text: String = "", onItemClick: () -> Unit = {}) {
    Box(modifier = Modifier.background(Color.White)) {
        CommonSettingRow(name = title) {
            onItemClick.invoke()
        }
        Row(modifier = Modifier.align(Alignment.CenterEnd)) {

            if (text != "") {
                Text(text = text, color = Color.LightGray)
            } else {
                Text(text = "Error!")
            }
            Icon(imageVector = Icons.Outlined.KeyboardArrowRight, contentDescription = null)
        }
    }

}