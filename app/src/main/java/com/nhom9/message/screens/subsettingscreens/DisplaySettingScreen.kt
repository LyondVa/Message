package com.nhom9.message.screens.subsettingscreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nhom9.message.CommonDivider
import com.nhom9.message.CommonSubSettingRow
import com.nhom9.message.DestinationScreen
import com.nhom9.message.MViewModel
import com.nhom9.message.R
import com.nhom9.message.TitleBarWithBack
import com.nhom9.message.navigateTo

@Composable
fun DisplaySettingScreen(navController: NavController, viewModel: MViewModel) {
    Column {
        TitleBarWithBack(navController = navController, text = stringResource(R.string.display))
        DisplaySettings(navController, viewModel.onToggleTheme)
    }

}

@Composable
fun DisplaySettings(navController: NavController, onToggleTheme: () -> Unit) {
    Column {
        DarkModeRow(onToggleTheme)
        CommonDivider(0)
        CommonSubSettingRow("Font Size")
        CommonDivider(0)
        CommonSubSettingRow("Language", onItemClick = {
            navigateTo(navController, DestinationScreen.ChangeLanguage.route)
        })
        CommonDivider(0)
    }

}

@Composable
fun DarkModeRow(onToggleTheme: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        var checked by remember {
            mutableStateOf(true)
        }
        val onCheckedChange: (Boolean) -> Unit = {
            onToggleTheme.invoke()
            checked = it
        }
        Text(
            text = stringResource(R.string.dark_mode),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(start = 20.dp)
                .align(Alignment.CenterStart)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterEnd)
        )
    }
}