package com.nhom9.message.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nhom9.message.CheckSignedIn
import com.nhom9.message.CommonProgressbar
import com.nhom9.message.DestinationScreen
import com.nhom9.message.MViewModel
import com.nhom9.message.R
import com.nhom9.message.navigateTo
import com.nhom9.message.ui.theme.md_theme_light_onPrimaryContainer
import com.nhom9.message.ui.theme.md_theme_light_primary

@Composable
fun EntryScreen(navController: NavController, viewModel: MViewModel) {

    CheckSignedIn(viewModel, navController)
    Box(modifier = Modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = painterResource(id = R.drawable.undraw_welcome_re_h3d9),
                contentDescription = null,
                modifier = Modifier
                    .height(200.dp)
                    .padding(top = 16.dp)
                    .padding(8.dp)
            )
            Text(
                text = stringResource(R.string.welcome),
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                color = md_theme_light_onPrimaryContainer,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier
            )
            Button(
                onClick = {
                    navigateTo(navController, DestinationScreen.Login.route)
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.login),
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier
                )
            }
            Button(
                onClick = {
                    navigateTo(navController, DestinationScreen.SignUp.route)
                },

                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        BorderStroke(4.dp, md_theme_light_primary),
                        shape = RoundedCornerShape(24.dp)
                    )

            ) {
                Text(
                    text = stringResource(R.string.sign_up),
                    color = Color.Black,
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Text(
                text = stringResource(R.string.or_log_in_with),
                color = md_theme_light_onPrimaryContainer,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(8.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.google__g__logo),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .clickable {

                    }
            )
        }
    }
    if (viewModel.inProcess.value) {
        CommonProgressbar()
    }
}
