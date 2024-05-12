package com.nhom9.message.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nhom9.message.CommonDivider
import com.nhom9.message.CommonImage
import com.nhom9.message.MViewModel
import com.nhom9.message.TitleBarWithBackAndVertIcon
import com.nhom9.message.downloader.AndroidDownloader

@Composable
fun ChatImageScreen(navController: NavController, viewModel: MViewModel, imageUrl: String) {
    val context = LocalContext.current
    val downloader = AndroidDownloader(context)
    val onButtonClick: () -> Unit = {
        downloader.downloadFile(imageUrl)
        Log.d("chat image", imageUrl)
    }
    Column {
        TitleBarWithBackAndVertIcon(navController = navController, text = "", onButtonClick)
        CommonDivider(0)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CommonImage(data = imageUrl, modifier = Modifier.padding(16.dp))
        }
    }
}
