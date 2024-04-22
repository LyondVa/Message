package com.nhom9.message.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nhom9.message.DestinationScreen
import com.nhom9.message.R
import com.nhom9.message.navigateTo
import com.nhom9.message.ui.theme.bar_gray

enum class BottomNavigationItem(
    val icon: Int,
    val title: String,
    val navDestination: DestinationScreen
) {
    CHATLIST(R.drawable.bubble_chat, "Chats", DestinationScreen.ChatList),
    STATUSLIST(R.drawable.status, "Statuses", DestinationScreen.StatusList),
    PROFILE(R.drawable.user, "Profile", DestinationScreen.Profile)
}

@Composable
fun BottomNavigationMenu(selectedItem: BottomNavigationItem, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 4.dp)
            .background(color = bar_gray)
    ) {
        for (item in BottomNavigationItem.entries) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(4.dp)
                    .weight(1f)
                    .clickable {
                        navigateTo(navController, item.navDestination.route)
                    }

            ) {
                Image(
                    painter = painterResource(id = item.icon),
                    contentDescription = null,
                    colorFilter = if (selectedItem == item) {
                        ColorFilter.tint(color = Color.Black)
                    } else {
                        ColorFilter.tint(color = Color.Gray)
                    },
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = item.title,
                    fontSize = 12.sp,
                    color = if (selectedItem == item) Color.Black else Color.Gray
                )
            }

        }
    }
}