package com.nhom9.message.screens.subsettingscreens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.nhom9.message.CommonDivider
import com.nhom9.message.CommonSubSettingRow
import com.nhom9.message.MViewModel

@Composable
fun NotificationAndSoundSettingScreen(navController: NavController, viewModel: MViewModel){
    val userData = viewModel.userData.value
    val tempUserData = userData

    var name by rememberSaveable {
        mutableStateOf(userData?.name ?: "")
    }
    var phoneNumber by rememberSaveable {
        mutableStateOf(userData?.phoneNumber ?: "")
    }
    var imageUrl by rememberSaveable {
        mutableStateOf(userData?.imageUrl ?: "")
    }
    val allowEdit = remember {
        mutableStateOf(false)
    }

    NASSettings()

}

@Composable
fun NASSettings(){


    Column {
        CallsCard()
        InAppNotificationsCard()
    }

}

@Composable
fun CallsCard(){

    CommonSubSettingRow("Vibrate")
    CommonDivider()
    CommonSubSettingRow("Ringtone")
    CommonDivider()
}

@Composable
fun InAppNotificationsCard(){

    CommonSubSettingRow("In-App Sounds")
    CommonDivider()
    CommonSubSettingRow("In-App Vibrate")
    CommonDivider()
    CommonSubSettingRow("In-Chat Sounds")
    CommonDivider()
}