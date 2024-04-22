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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.nhom9.message.CommonDivider
import com.nhom9.message.CommonImage
import com.nhom9.message.CommonProgressbar
import com.nhom9.message.CommonSettingRow
import com.nhom9.message.DestinationScreen
import com.nhom9.message.MViewModel
import com.nhom9.message.navigateTo

@Composable
fun ProfileScreen(navController: NavController, viewModel: MViewModel) {
    val inProcess = viewModel.inProcess.value
    if (inProcess) {
        CommonProgressbar()
    } else {
        val userData = viewModel.userData.value
        var tempUserData = userData
        var name by rememberSaveable {
            mutableStateOf(userData?.name ?: "")
        }
        var number by rememberSaveable {
            mutableStateOf(userData?.number ?: "")
        }
        var imageUrl by rememberSaveable {
            mutableStateOf(userData?.imageUrl ?: "")
        }
        val allowEdit = remember {
            mutableStateOf(false)
        }

        val onEdit: () -> Unit = {
            tempUserData = userData
            allowEdit.value = true
        }
        val onCancel: () -> Unit = {
            name = tempUserData?.name!!
            number = tempUserData?.number!!
            allowEdit.value = false
        }
        val onSave: () -> Unit = {
            viewModel.saveProfile(
                name = name,
                number = number,
                uri = Uri.parse(imageUrl)
            )
        }
        val onChangeImage: (Uri) -> Unit = {
            imageUrl = it.toString()
        }

        Column() {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                ProfileImageBar(
                    allowEdit = allowEdit.value,
                    imageUrl = imageUrl,
                    name = name,
                    viewModel = viewModel,
                    onChangeImage
                )
                InfoCard(number = number, "@25")
                SettingCard(navController)
                LogOutCard(navController, viewModel)
            }

            BottomNavigationMenu(
                selectedItem = BottomNavigationItem.PROFILE,
                navController = navController,
            )
        }
    }
}


@Composable
fun ProfileContent(
    modifier: Modifier,
    allowEdit: Boolean,
    viewModel: MViewModel,
    name: String,
    number: String,
    onNameChange: (String) -> Unit,
    onNumberChange: (String) -> Unit,
    onEdit: () -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit,
    onChangeImage: (Uri) -> Unit
) {
    val imageUrl = viewModel.userData.value?.imageUrl
    Column(
        modifier = modifier
            .padding(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            /*if (allowEdit) {
                Text(text = "Cancel",
                    modifier = Modifier
                        .clickable {
                            onCancel.invoke()
                        }
                )
                Text(text = "Save",
                    modifier = Modifier
                        .clickable {
                            onSave.invoke()
                        }
                )
            } else {
                Text(text = "Edit",
                    modifier = Modifier
                        .clickable {
                            onEdit.invoke()
                        }

                )
            }*/
        }
        /*Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Text(
                text = "Name: ",
                modifier = Modifier.width(100.dp)
            )
            TextField(
                value = name,
                onValueChange = onNameChange,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedTextColor = Color.Gray,
                    unfocusedContainerColor = Color.Transparent
                ),
                readOnly = !allowEdit
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Text(
                text = "Number: ",
                modifier = Modifier.width(100.dp)
            )
            TextField(
                value = number,
                onValueChange = onNumberChange,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedTextColor = Color.Gray,
                    unfocusedContainerColor = Color.Transparent
                ),
                readOnly = !allowEdit
            )
        }
        CommonDivider()
    }*/
    }
}

@Composable
fun ProfileImageBar(
    allowEdit: Boolean,
    imageUrl: String?,
    name: String,
    viewModel: MViewModel,
    onChangeImage: (Uri) -> Unit
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
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Card(
                shape = CircleShape,
                modifier = Modifier
                    .size(80.dp)
                    .clickable(allowEdit) {
                        launcher.launch("image/*")
                    }
            ) {
                if (allowEdit) {
                    ProfileImagePreview(localImageUrl = localImageUrl, data = imageUrl)
                    if (localImageUrl != null) {
                        onChangeImage.invoke(localImageUrl!!)
                    }
                } else {
                    CommonImage(data = imageUrl)
                }
            }
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = name,
                    fontSize = 24.sp,
                    modifier = Modifier
                )
                Text(text = "Message id: $name", color = Color.Gray)
            }
        }
        if (viewModel.inProcess.value) {
            CommonProgressbar()
        }
    }
}

@Composable
fun InfoCard(
    number: String,
    handle: String? = null
) {
    Surface(
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp))
        {
            Text(
                text = "Info",
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(text = "number", color = Color.Gray)
            Text(text = number)
            CommonDivider()
            Text(text = "handle", color = Color.Gray)
            Text(text = handle ?: "N/A")
        }
    }
}

@Composable
fun ProfileImagePreview(
    localImageUrl: Uri?,
    data: String?,
    contentScale: ContentScale = ContentScale.Crop,
) {
    if (localImageUrl != null) {
        AsyncImage(
            model = localImageUrl,
            contentScale = contentScale,
            contentDescription = "Profile Image Preview",
            modifier = Modifier.wrapContentSize()
        )
    } else {
        CommonImage(data = data)
    }
}

@Composable
fun SettingCard(navController: NavController) {
    Column(
        Modifier
            .background(Color.White)
            .padding(top = 20.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Settings",
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 20.dp)
        )
        CommonSettingRow(name = "Account", Icons.Outlined.Person) {
            navigateTo(navController, DestinationScreen.AccountSetting.route)
        }
        CommonDivider(0)
        CommonSettingRow(name = "Display",Icons.Outlined.Settings) {
            navigateTo(navController, DestinationScreen.DisplaySetting.route)
        }
        CommonDivider(0)
        CommonSettingRow(name = "Notification and Sound", Icons.Outlined.Notifications) {
            navigateTo(navController, DestinationScreen.NotificationAndSoundSetting.route)
        }
        CommonDivider(0)
        CommonSettingRow(name = "Privacy and Security", Icons.Outlined.Warning) {
            navigateTo(navController, DestinationScreen.PrivacyAndSecuritySetting.route)
        }
    }
}

@Composable
fun LogOutCard(navController: NavController, viewModel: MViewModel){
    Box(modifier = Modifier.padding(top = 8.dp)){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(Color.White)
                .clickable {
                    viewModel.logOut()
                    navigateTo(navController, DestinationScreen.Entry.route)
                }
        ) {
                Icon(Icons.Outlined.ExitToApp, null)
                Text(
                    text = "Log Out",
                    modifier = Modifier
                        .padding(start = 8.dp)
                )

        }
    }
}