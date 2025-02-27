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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nhom9.message.CommonProgressbar
import com.nhom9.message.MViewModel
import com.nhom9.message.R
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
            viewModel.updatePhoneNumber(phoneNumber)
        }
        Column {
            TitleBarWithBackAndRightButton(navController,
                stringResource(R.string.edit_phone_number), stringResource(R.string.save), onButtonClick)
            Column(modifier  = Modifier.padding(8.dp)) {

                OutlinedTextField(value = phoneNumber, onValueChange = {phoneNumber = it}, modifier = Modifier.fillMaxWidth())
                Text(text = stringResource(R.string.make_sure_it_s_a_number_you_have_access_to), color = Color.LightGray, modifier = Modifier.padding(top = 8.dp))
            }
        }

    }
}