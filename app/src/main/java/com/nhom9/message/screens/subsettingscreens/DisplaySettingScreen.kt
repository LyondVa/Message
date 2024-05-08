package com.nhom9.message.screens.subsettingscreens

import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
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
import com.nhom9.message.CommonSettingRow
import com.nhom9.message.CommonSubSettingRow
import com.nhom9.message.MViewModel
import com.nhom9.message.TitleBarWithBack

@Composable
fun DisplaySettingScreen(navController: NavController, viewModel: MViewModel) {
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


    Column {
        TitleBarWithBack(navController = navController, text = "Account")
        DisplaySettings(viewModel.onToggleTheme)
    }

}

@Composable
fun DisplaySettings(onToggleTheme: () -> Unit) {


    Column {
        DarkModeRow(onToggleTheme)
        CommonDivider(0)
        CommonSubSettingRow("Font Size")
        CommonDivider(0)
        CommonSubSettingRow("Language")
        CommonDivider(0)
    }

}

@Composable
fun DarkModeRow(onToggleTheme:()->Unit) {
    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
    ) {
        var checked by remember {
            mutableStateOf(true)
        }
        val onCheckedChange: (Boolean) -> Unit = {
            onToggleTheme.invoke()
            checked = it
        }
        Text(
            text = "Dark Mode",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(start = 8.dp)
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