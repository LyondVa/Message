package com.nhom9.message.screens.subsettingscreens.accountsettingsubscreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nhom9.message.CommonProgressbar
import com.nhom9.message.MViewModel
import com.nhom9.message.TitleBarWithBackAndRightButton

@Composable
fun EditPhoneNumberScreen(navController: NavController, viewModel: MViewModel){
    val inProcess = viewModel.inProcess.value
    if (inProcess) {
        CommonProgressbar()
    }
    else{
        var phoneNumber by rememberSaveable {
            mutableStateOf(viewModel.userData.value?.phoneNumber ?: "")
        }

        val onButtonClick:()->Unit={
            viewModel.createOrUpdateProfile(phoneNumber = phoneNumber)
        }
        Column {
            TitleBarWithBackAndRightButton(navController, "Edit Phone Number", "Save", onButtonClick)
            Column(modifier  = Modifier.padding(8.dp)) {

                OutlinedTextField(value = phoneNumber, onValueChange = {phoneNumber = it}, modifier = Modifier.fillMaxWidth())
                Text(text = "Make sure it's a number you have access to!", color = Color.LightGray, modifier = Modifier.padding(top = 8.dp))
            }
        }

    }
}