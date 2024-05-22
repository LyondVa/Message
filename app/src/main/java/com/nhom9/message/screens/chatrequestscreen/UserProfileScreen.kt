package com.nhom9.message.screens.chatrequestscreen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nhom9.message.CommonDivider
import com.nhom9.message.CommonProfileImage
import com.nhom9.message.MViewModel
import com.nhom9.message.R
import com.nhom9.message.data.TOP_BAR_HEIGHT

@Composable
fun UserProfileScreen(navController: NavController, viewModel: MViewModel, userId: String, isMyRequest: Boolean) {
    val chatUser = viewModel.getUserDataFromRequest(userId, isMyRequest)
    Column {
        HeaderBar(navController = navController)
        CommonDivider(0)
        Surface(
            shadowElevation = 2.dp,
            modifier = Modifier
                .height(intrinsicSize = IntrinsicSize.Min)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column {
                ChatUserCard(chatUser?.name!!, chatUser.imageUrl)
                CommonDivider(0)
                ProfileInfoCard("not implemented", chatUser.phoneNumber!!)
                CommonDivider(0)
            }
        }
    }
}

@Composable
fun ChatUserCard(name: String, imageUrl: String?) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Card(
            shape = CircleShape,
            modifier = Modifier
                .size(160.dp)
        ) {
            CommonProfileImage(imageUrl = imageUrl)
        }
        Text(
            text = name,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun ProfileInfoCard(bio: String, phoneNumber: String) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        /*Text(
            text = "Bio: $bio",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(8.dp)
        )*/
        Text(
            text =  stringResource(id = R.string.phone_number)+ ": $phoneNumber",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(8.dp)
        )
    }

}

@Composable
fun HeaderBar(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(TOP_BAR_HEIGHT.dp)
    ) {
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
        }
    }
}
