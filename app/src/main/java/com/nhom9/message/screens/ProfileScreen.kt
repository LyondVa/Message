package com.nhom9.message.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.nhom9.message.CommonDivider
import com.nhom9.message.CommonImage
import com.nhom9.message.CommonProgressbar
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

        Column (modifier = Modifier.fillMaxSize()){
            ProfileContent(
                allowEdit = allowEdit.value,
                viewModel = viewModel,
                name = name,
                number = number,
                onNameChange = { name = it },
                onNumberChange = { number = it },
                onLogOut = {
                    viewModel.logOut()
                    navigateTo(navController, DestinationScreen.Login.route)
                },
                onSave = onSave,
                onEdit = onEdit,
                onCancel = onCancel,
                onChangeImage = onChangeImage,
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(8.dp)
            )
            BottomNavigationMenu(
                selectedItem = BottomNavigationItem.PROFILE,
                navController = navController
            )
        }
    }
}

@Composable
fun ProfileContent(
    allowEdit: Boolean,
    viewModel: MViewModel,
    name: String,
    number: String,
    onNameChange: (String) -> Unit,
    onNumberChange: (String) -> Unit,
    onLogOut: () -> Unit,
    onEdit: () -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit,
    onChangeImage: (Uri) -> Unit,
    modifier: Modifier
) {
    val imageUrl = viewModel.userData.value?.imageUrl
    Column(
        modifier = Modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            if (allowEdit) {
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
            }
        }
        CommonDivider()
        ProfileImage(allowEdit = allowEdit, imageUrl = imageUrl, viewModel = viewModel, onChangeImage)
        CommonDivider()
        Row(
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
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Logout",
                modifier = Modifier.clickable {
                    onLogOut.invoke()
                }
            )
        }
    }
}

@Composable
fun ProfileImage(
    allowEdit: Boolean,
    imageUrl: String?,
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
    Box(
        modifier = Modifier
            .height(intrinsicSize = IntrinsicSize.Min)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Card(
                shape = CircleShape,
                modifier = Modifier
                    .padding(8.dp)
                    .size(100.dp)
                    .clickable(allowEdit) {
                        launcher.launch("image/*")
                    }
            ) {
                if (allowEdit) {
                    ProfileImagePreview(localImageUrl = localImageUrl, data = imageUrl)
                    if(localImageUrl!=null){
                        onChangeImage.invoke(localImageUrl!!)
                    }
                } else {
                    CommonImage(data = imageUrl)
                }
            }
            Text(text = "Change Profile Picture")
        }
        if (viewModel.inProcess.value) {
            CommonProgressbar()
        }
    }
}

@Composable
fun ProfileImagePreview(
    modifier: Modifier = Modifier.wrapContentSize(),
    localImageUrl: Uri?,
    data: String?,
    contentScale: ContentScale = ContentScale.Crop
) {
    if (localImageUrl != null) {
        AsyncImage(
            model = localImageUrl,
            contentDescription = "Profile Image Preview",
            modifier = modifier
        )
    } else {
        val painter = rememberAsyncImagePainter(model = data)
        Image(
            painter = painter,
            contentDescription = null,
            modifier = modifier,
            contentScale = contentScale
        )
    }
}