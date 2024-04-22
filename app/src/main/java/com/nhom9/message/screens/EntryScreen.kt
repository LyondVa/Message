package com.nhom9.message.screens

import android.graphics.drawable.Icon
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nhom9.message.CheckSignedIn
import com.nhom9.message.CommonProgressbar
import com.nhom9.message.DestinationScreen
import com.nhom9.message.MViewModel
import com.nhom9.message.R
import com.nhom9.message.navigateTo
import com.nhom9.message.ui.theme.md_theme_light_onPrimaryContainer
import com.nhom9.message.ui.theme.md_theme_light_primary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryScreen(navController: NavController, viewModel: MViewModel) {
    val emailState = remember {
        mutableStateOf(TextFieldValue())
    }
    val passwordState = remember {
        mutableStateOf(TextFieldValue())
    }

    CheckSignedIn(viewModel, navController)
    Box(modifier = Modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            //verticalArrangement = Arran,
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
                text = "Welcome",
                fontSize = 40.sp,
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
                    text = "Login",
                    color = Color.White,
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
                    text = "Sign Up",
                    color = Color.Black
                )
            }
            Text(
                text = "-Or log in with-",
                color = md_theme_light_onPrimaryContainer,
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

    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.left_corner),
            contentDescription = null,
            modifier = Modifier.size(68.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.right_corner),
            contentDescription = null,
            modifier = Modifier.size(68.dp)
        )
    }

    if (viewModel.inProcess.value) {
        CommonProgressbar()
    }
}
