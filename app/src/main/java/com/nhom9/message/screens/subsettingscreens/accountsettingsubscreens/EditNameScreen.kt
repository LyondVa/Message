package com.nhom9.message.screens.subsettingscreens.accountsettingsubscreens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.nhom9.message.CommonProgressbar
import com.nhom9.message.MViewModel
import com.nhom9.message.TitleBarWithBack
import com.nhom9.message.TitleBarWithBackAndRightButton

@Composable
fun EditNameScreen(navController: NavController, viewModel: MViewModel){
    val inProcess = viewModel.inProcess.value
    if (inProcess) {
        CommonProgressbar()
    }
    else{
        var name by rememberSaveable {
            mutableStateOf(viewModel.userData.value?.name ?: "")
        }

        val onButtonClick:()->Unit={
            viewModel.createOrUpdateProfile(name = name)
        }

        Column {
            TitleBarWithBackAndRightButton(text = "Edit Name", "Save", onButtonClick)
            OutlinedTextField(value = name, onValueChange = {name = it})

        }
    }
}