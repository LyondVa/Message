package com.nhom9.message.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nhom9.message.DestinationScreen
import com.nhom9.message.R
import com.nhom9.message.navigateTo

enum class BottomNavigationItem(
    val icon: Int,
    val title: (Context) -> String,
    val navDestination: DestinationScreen
) {
    CHATLIST(R.drawable.bubble_chat, { context: Context -> context.getString(R.string.chats) }, DestinationScreen.ChatList),
    STATUSLIST(R.drawable.status, { context: Context -> context.getString(R.string.statuses) }, DestinationScreen.StatusList),
    PROFILE(R.drawable.user, { context: Context -> context.getString(R.string.profile) }, DestinationScreen.Profile)
}

@Composable
fun BottomNavigationMenu(selectedItem: BottomNavigationItem, navController: NavController) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 4.dp)
    ) {
        for (item in BottomNavigationItem.entries) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        navigateTo(navController, item.navDestination.route)
                    }
                    .padding(4.dp)

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
                    text = item.title.invoke(context),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (selectedItem == item) Color.Black else Color.Gray
                )
            }

        }
    }
}