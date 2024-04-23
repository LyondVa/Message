package com.nhom9.message.screens.subsettingscreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nhom9.message.CommonDivider
import com.nhom9.message.CommonImage
import com.nhom9.message.CommonSubSettingRow
import com.nhom9.message.DestinationScreen
import com.nhom9.message.MViewModel
import com.nhom9.message.TitleBarWithBack
import com.nhom9.message.navigateTo

@Composable
fun AccountSettingScreen(navController: NavController, viewModel: MViewModel) {
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
    var userId by rememberSaveable {
        mutableStateOf(userData?.userId ?: "")
    }
    val allowEdit = remember {
        mutableStateOf(false)
    }
    Column {
        TitleBarWithBack(text = "Account")
        AccountInfoCard(navController, imageUrl, name, "N/A", userId, phoneNumber)
    }


}

@Composable
fun AccountInfoCard(
    navController: NavController,
    imageUrl: String,
    name: String,
    handle: String,
    userId: String,
    phoneNumber: String
) {

    val onNameClick: () -> Unit = {
        navigateTo(navController, DestinationScreen.EditName.route)
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        AccountSettingImageRow(imageUrl = imageUrl)
        CommonDivider(0)
        CommonSubSettingRow("Name", name, onNameClick)
        CommonDivider(0)
        /*CommonSubSettingRow("Handle", handle)
        CommonDivider(0)*/
        CommonSubSettingRow("Message Id", userId, {})
        CommonDivider(0)
        CommonSubSettingRow("Phone Number", phoneNumber, {})
        CommonDivider(0)
    }

}

@Composable
fun AccountSettingImageRow(imageUrl: String) {
    Box(modifier = Modifier
        .background(Color.White)
        .clickable {
            //onItemClick.invoke()
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxWidth()
                .background(Color.White)
        ) {
            Text(
                text = "Profile Image",
                modifier = Modifier.padding(start = 20.dp)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(8.dp)
        ) {
            Card(shape = RoundedCornerShape(8.dp)) {
                CommonImage(data = imageUrl, modifier = Modifier.size(80.dp))
            }
            Icon(imageVector = Icons.Outlined.KeyboardArrowRight, contentDescription = null)
        }
    }

}