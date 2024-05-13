package com.nhom9.message.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nhom9.message.CallBox
import com.nhom9.message.CommonDivider
import com.nhom9.message.CommonImage
import com.nhom9.message.MViewModel

@Composable
fun ChatProfileScreen(navController: NavController, viewModel: MViewModel, userId: String) {
    val chatUser = viewModel.getChatUser(userId)

    Column {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            FloatingBar(navController = navController)
            CommonDivider(0)
            ChatUserCard(chatUser?.name!!, chatUser.imageUrl!!)
            ProfileInfoCard("")
        }
    }
}

@Composable
fun ChatUserCard(name: String, imageUrl: String) {
    Surface(
        shadowElevation = 2.dp,
        modifier = Modifier
            .height(intrinsicSize = IntrinsicSize.Min)
            .padding(bottom = 8.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {

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
                CommonImage(data =imageUrl)
            }
            Text(
                text = name,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(8.dp)
            )

        }
    }
}

@Composable
fun ProfileInfoCard(name: String) {


}

@Composable
fun FloatingBar(navController: NavController) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        IconButton(
            onClick = { navController.popBackStack() }, modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.CenterEnd)
        ) {
            CallBox()
            DropDownMenuButton()
        }
    }
}

@Composable
fun DropDownMenuButton() {
    val context = LocalContext.current
    var mDisplayMenu by remember { mutableStateOf(false) }
    IconButton(onClick = { mDisplayMenu = !mDisplayMenu }) {
        Icon(Icons.Outlined.MoreVert, "")
    }
    DropdownMenu(
        expanded = mDisplayMenu,
        onDismissRequest = { mDisplayMenu = false }
    ) {
        DropdownMenuItem(
            {
                Text(
                    text = "Block",
                    style = MaterialTheme.typography.labelMedium
                )
            },
            {
                Toast.makeText(context, "Block", Toast.LENGTH_SHORT).show()
                mDisplayMenu = false

            }
        )
    }
}
