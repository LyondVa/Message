package com.nhom9.message.screens.subsettingscreens.displaysettingsubscreens

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import androidx.navigation.NavController
import com.nhom9.message.CommonProgressbar
import com.nhom9.message.Language
import com.nhom9.message.MViewModel
import java.util.Locale

@Composable
fun ChangeLanguageScreen(navController: NavController, viewModel: MViewModel) {
    val inProcess = viewModel.inProcess.value
    if (inProcess) {
        CommonProgressbar()
    } else {
        val context = LocalContext.current
        val onButtonClick: (String) -> Unit = {
            localeSelection(context = context, localeTag = Locale(it).toLanguageTag())
        }


        val deviceLocale = context.resources.configuration.locales.get(0)

        val currentLocale = remember { mutableStateOf(deviceLocale.toLanguageTag()) }

        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags("en")
        AppCompatDelegate.setApplicationLocales(appLocale)

        Column {
            TitleBarWithBackAndRightButton(navController, "Change Language")
            Column(modifier = Modifier.padding(8.dp))


            {
                for(i in 0..1){
                    val language = Language.entries[i]
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            localeSelection(context = context, localeTag = Locale(language.tag).toLanguageTag())
                        }) {
                        Text(text = language.localName)
                    }
                }
            }
        }

    }
}

@Composable
fun TitleBarWithBackAndRightButton(
    navController: NavController,
    text: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        IconButton(
            onClick = { navController.popBackStack() }
        ) {
            Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
        }
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineSmall,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.Center)
        )
    }
}
fun localeSelection(context: Context, localeTag: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.getSystemService(LocaleManager::class.java).applicationLocales =
            LocaleList.forLanguageTags(localeTag)
    } else {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(localeTag)
        )
    }
}
