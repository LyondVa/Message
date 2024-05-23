package com.nhom9.message.screens.reportscreens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.nhom9.message.CommonDivider
import com.nhom9.message.CommonRow
import com.nhom9.message.DestinationScreen
import com.nhom9.message.R
import com.nhom9.message.TitleBarWithBack
import com.nhom9.message.data.ReportOption
import com.nhom9.message.navigateTo
import java.util.Locale

@Composable
fun ReportScreen(navController: NavController, userId: String) {
    Column {
        TitleBarWithBack(navController, stringResource(R.string.report))
        CommonDivider(0)
        ReportOption.entries.forEachIndexed { index, item ->
            CommonRow(name = item.getTranslatedTitle(Locale.getDefault())) {
                navigateTo(navController, DestinationScreen.ReportOption.createRoute(index.toString(), userId))
            }
            CommonDivider(0)
        }
    }
}