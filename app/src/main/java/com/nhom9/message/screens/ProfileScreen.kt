package com.nhom9.message.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nhom9.message.CommonDivider
import com.nhom9.message.CommonProfileImage
import com.nhom9.message.CommonProgressbar
import com.nhom9.message.CommonRow
import com.nhom9.message.DestinationScreen
import com.nhom9.message.MViewModel
import com.nhom9.message.R
import com.nhom9.message.navigateTo

@Composable
fun ProfileScreen(navController: NavController, viewModel: MViewModel) {
    val inProcess = viewModel.inProcess.value
    if (inProcess) {
        CommonProgressbar()
    } else {
        val userData = viewModel.userData.value
        val userId by rememberSaveable {
            mutableStateOf(userData?.userId ?: "")
        }
        val name by rememberSaveable {
            mutableStateOf(userData?.name ?: "")
        }
        val phoneNumber by rememberSaveable {
            mutableStateOf(userData?.phoneNumber ?: "")
        }
        val imageUrl by rememberSaveable {
            mutableStateOf(userData?.imageUrl ?: "")
        }
        val allowEdit = remember {
            mutableStateOf(false)
        }
        Column {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                ProfileImageBar(
                    allowEdit = allowEdit.value,
                    imageUrl = imageUrl,
                    name = name,
                    userId = userId,
                    viewModel = viewModel
                )
                InfoCard(phoneNumber = phoneNumber)
                SettingCard(navController)
                LogOutCard(navController, viewModel)
            }
            CommonDivider(0)
            BottomNavigationMenu(
                selectedItem = BottomNavigationItem.PROFILE,
                navController = navController,
            )
        }
    }
}

@Composable
fun ProfileImageBar(
    allowEdit: Boolean, imageUrl: String?, name: String, userId: String?, viewModel: MViewModel
) {
    var localImageUrl by remember { mutableStateOf<Uri?>(null) }
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                localImageUrl = it
            }
        }
    Surface(
        shadowElevation = 2.dp,
        modifier = Modifier
            .height(intrinsicSize = IntrinsicSize.Min)
            .padding(bottom = 8.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Card(shape = CircleShape, modifier = Modifier
                .size(80.dp)
                .clickable(allowEdit) {
                    launcher.launch("image/*")
                }) {
                CommonProfileImage(imageUrl = imageUrl)
            }
            Column(verticalArrangement = Arrangement.Center, modifier = Modifier.padding(8.dp)) {
                Text(
                    text = name, style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = stringResource(R.string.message_id) + ":" + userId,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        if (viewModel.inProcess.value) {
            CommonProgressbar()
        }
    }
}

@Composable
fun InfoCard(
    phoneNumber: String,
) {
    Surface(
        shadowElevation = 2.dp, modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = stringResource(R.string.info),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = stringResource(R.string.phone_number), color = Color.Gray,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = phoneNumber,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}


@Composable
fun SettingCard(navController: NavController) {
    Surface(
        shadowElevation = 2.dp,
        modifier = Modifier
            .padding(bottom = 8.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.settings),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 20.dp)
            )
            CommonRow(name = stringResource(R.string.account), icon = Icons.Outlined.Person) {
                navigateTo(navController, DestinationScreen.AccountSetting.route)
            }
            CommonDivider(0)
            SwitchRow("DarkMode") {

            }
            CommonDivider(0)
            SwitchRow("Notification") {

            }/*
            CommonRow(name = stringResource(R.string.display), icon = Icons.Outlined.Settings) {
                navigateTo(navController, DestinationScreen.DisplaySetting.route)
            }
            CommonDivider(0)
            CommonRow(
                name = stringResource(R.string.notification_and_sound),
                icon = Icons.Outlined.Notifications
            ) {
                navigateTo(navController, DestinationScreen.NotificationAndSoundSetting.route)
            }
            CommonDivider(0)
            CommonRow(
                name = stringResource(R.string.privacy_and_security), icon = Icons.Outlined.Warning
            ) {
                navigateTo(navController, DestinationScreen.PrivacyAndSecuritySetting.route)
            }*/
        }
    }
}

@Composable
fun LogOutCard(navController: NavController, viewModel: MViewModel) {
    Surface(
        shadowElevation = 2.dp, modifier = Modifier
    ) {
        Box(modifier = Modifier.padding(top = 8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clickable {
                        viewModel.logOut()
                        navigateTo(navController, DestinationScreen.Entry.route)
                    }) {
                Icon(Icons.Outlined.ExitToApp, null)
                Text(
                    text = stringResource(R.string.log_out),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )

            }
        }
    }
}


@Composable
fun SwitchRow(title: String, onToggleTheme: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        var checked by remember {
            mutableStateOf(true)
        }
        val onCheckedChange: (Boolean) -> Unit = {
            onToggleTheme.invoke()
            checked = it
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(start = 20.dp)
                .align(Alignment.CenterStart)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterEnd)
        )
    }
}