package com.nhom9.message.screens.subsettingscreens.accountsettingsubscreens

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nhom9.message.CommonDivider
import com.nhom9.message.CommonProgressbar
import com.nhom9.message.MViewModel
import com.nhom9.message.ProfileImageCard
import com.nhom9.message.R
import com.nhom9.message.TitleBarWithBack

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
            viewModel.saveProfileImage(
                uri = Uri.parse(imageUrl)
            )
            allowEdit = false
        }
        val onChangeImage: (Uri) -> Unit = {
            imageUrl = it.toString()
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TitleBarWithBack(navController, stringResource(R.string.edit_profile_image))
            CommonDivider(0)
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                ProfileImageCard(
                    isInEdit = allowEdit,
                    imageUrl = imageUrl,
                    onChangeImage = onChangeImage
                )
                if (allowEdit) {
                    Row {
                        Button(onClick = onCancelClick, modifier = Modifier.padding(8.dp)) {
                            Text(text = stringResource(R.string.cancel))
                        }
                        Button(onClick = onSaveClick, modifier = Modifier.padding(8.dp)) {
                            Text(text = stringResource(R.string.save))
                        }
                    }
                } else {
                    Button(onClick = onEditClick, modifier = Modifier.padding(8.dp)) {
                        Text(text = stringResource(R.string.edit))
                    }
                }
            }
        }

    }
}
