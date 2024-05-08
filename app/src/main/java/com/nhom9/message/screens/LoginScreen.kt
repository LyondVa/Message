package com.nhom9.message.screens

import android.graphics.drawable.Icon
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, viewModel: MViewModel) {
    val emailState = remember {
        mutableStateOf(TextFieldValue())
    }
    val passwordState = remember {
        mutableStateOf(TextFieldValue())
    }

    BackHandler {
        navigateTo(navController, DestinationScreen.Entry.route)
    }

    CheckSignedIn(viewModel, navController)

    Scaffold(
        content = { padding ->
            Box() {
                Box(modifier = Modifier.padding(padding)) {
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
                            text = "Login",
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold,
                            color = md_theme_light_onPrimaryContainer,
                            fontFamily = FontFamily.SansSerif,
                            modifier = Modifier
                        )
                        Text(
                            text = "Sign in to continue",
                            style = MaterialTheme.typography.headlineSmall,
                            color = md_theme_light_onPrimaryContainer
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .wrapContentSize()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.mail_encircled),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(60.dp)
                                    .padding(start = 8.dp)
                                    .padding(top = 8.dp)
                                    .padding(end = 8.dp)
                            )
                            OutlinedTextField(
                                value = emailState.value,
                                onValueChange = {
                                    emailState.value = it
                                },

                                label = {
                                    Text(
                                        text = "Email",
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                },
                                shape = RoundedCornerShape(40.dp),
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .fillMaxWidth()
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .wrapContentSize()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.key_encircled),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(60.dp)
                                    .padding(start = 8.dp)
                                    .padding(top = 8.dp)
                                    .padding(end = 8.dp)
                            )
                            OutlinedTextField(
                                value = passwordState.value,
                                onValueChange = {
                                    passwordState.value = it
                                },
                                label = {
                                    Text(
                                        text = "Password",
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                },
                                shape = RoundedCornerShape(40.dp),
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .fillMaxWidth()
                            )
                        }
                        Button(
                            onClick = {
                                viewModel.logIn(emailState.value.text, passwordState.value.text)
                            },
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {

                            Text(
                                text = "Log in",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                        Text(text = "Forgotten password?",
                            color = Color.Blue,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    navigateTo(navController, DestinationScreen.SignUp.route)
                                }
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
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
            }

        }
    )

    /*Box(modifier = Modifier) {
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
                text = "Login",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = md_theme_light_onPrimaryContainer,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier
            )
            Text(
                text = "Sign in to continue",
                color = md_theme_light_onPrimaryContainer
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .wrapContentSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.mail_encircled),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .padding(start = 8.dp)
                        .padding(top = 8.dp)
                        .padding(end = 8.dp)
                )
                OutlinedTextField(
                    value = emailState.value,
                    onValueChange = {
                        emailState.value = it
                    },

                    label = { Text(text = "Email") },
                    shape = RoundedCornerShape(40.dp),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .fillMaxWidth()
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .wrapContentSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.key_encircled),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .padding(start = 8.dp)
                        .padding(top = 8.dp)
                        .padding(end = 8.dp)
                )
                OutlinedTextField(
                    value = passwordState.value,
                    onValueChange = {
                        passwordState.value = it
                    },
                    label = { Text(text = "Password") },
                    shape = RoundedCornerShape(40.dp),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .fillMaxWidth()
                )
            }
            Button(
                onClick = {
                    viewModel.logIn(emailState.value.text, passwordState.value.text)
                },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {

                Text(text = "Log in")
            }
            Text(text = "Forgotten password?",
                color = Color.Blue,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        navigateTo(navController, DestinationScreen.SignUp.route)
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
    }*/



    if (viewModel.inProcess.value) {
        CommonProgressbar()
    }
}
