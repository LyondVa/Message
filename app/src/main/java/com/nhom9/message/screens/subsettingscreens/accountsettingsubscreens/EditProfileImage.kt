package com.nhom9.message.screens.subsettingscreens.accountsettingsubscreens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.nhom9.message.CommonImage
import com.nhom9.message.CommonProgressbar
import com.nhom9.message.MViewModel
import com.nhom9.message.TitleBarWithBack
import com.nhom9.message.TitleBarWithBackAndRightButton

@Composable
fun EditProfileImageScreen(navController: NavController, viewModel: MViewModel) {
    val inProcess = viewModel.inProcess.value
    if (inProcess) {
        CommonProgressbar()
    } else {
        var imageUrl by rememberSaveable {
            mutableStateOf(viewModel.userData.value?.imageUrl ?: "")
        }
        var allowEdit by rememberSaveable {
            mutableStateOf(false)
        }

        val onEditClick: () -> Unit = {
            allowEdit = true
        }
        val onCancelClick: () -> Unit = {
            allowEdit = false
        }
        val onSaveClick: () -> Unit = {
            viewModel.createOrUpdateProfile(imageUrl = imageUrl)
            allowEdit = false
        }
        val onChangeImage: (Uri) -> Unit = {
            imageUrl = it.toString()
        }

        Column {
            TitleBarWithBack(navController, "Edit Profile Image")
            Column(modifier = Modifier.padding(8.dp)) {
                ProfileImageCard(
                    allowEdit = allowEdit,
                    imageUrl = imageUrl,
                    viewModel = viewModel,
                    onChangeImage = onChangeImage
                )
                if (allowEdit) {
                    Row {
                        Button(onClick = onCancelClick) {
                            Text(text = "Cancel")
                        }
                        Button(onClick = onSaveClick) {
                            Text(text = "Save")
                        }
                    }
                } else {
                    Button(onClick = onEditClick) {
                        Text(text = "Edit")
                    }
                }
            }
        }

    }
}

@Composable
fun ProfileImageCard(
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
