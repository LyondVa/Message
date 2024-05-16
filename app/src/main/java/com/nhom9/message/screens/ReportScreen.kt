package com.nhom9.message.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.nhom9.message.CommonDivider
import com.nhom9.message.CommonRow
import com.nhom9.message.DestinationScreen
import com.nhom9.message.TitleBarWithBack
import com.nhom9.message.data.ReportOption
import com.nhom9.message.navigateTo

@Composable
fun ReportScreen(navController: NavController, userId: String) {
    Column {
        TitleBarWithBack(navController, "Report")
        CommonDivider(0)
        ReportOption.entries.forEachIndexed { index, item ->
            CommonRow(name = item.title) {
                navigateTo(navController, DestinationScreen.ReportOption.createRoute(index.toString(), userId))
            }
            CommonDivider(0)
        }
    }
}