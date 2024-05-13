package com.nhom9.message.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nhom9.message.CheckSignedIn
import com.nhom9.message.CommonProgressbar
import com.nhom9.message.DestinationScreen
import com.nhom9.message.MViewModel
import com.nhom9.message.R
import com.nhom9.message.navigateTo
import com.nhom9.message.ui.theme.md_theme_light_onPrimaryContainer

@Composable
fun SignUpScreen(navController: NavController, viewModel: MViewModel) {
    CheckSignedIn(viewModel, navController)
    Box(modifier = Modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
                .verticalScroll(rememberScrollState())
        ) {
            val nameState = remember {
                mutableStateOf(TextFieldValue())
            }
            val phoneNumberState = remember {
                mutableStateOf(TextFieldValue())
            }
            val emailState = remember {
                mutableStateOf(TextFieldValue())
            }
            val passwordState = remember {
                mutableStateOf(TextFieldValue())
            }

            BackHandler {
                navigateTo(navController, DestinationScreen.Entry.route)
            }

            Image(
                painter = painterResource(id = R.drawable.undraw_welcome_cats_thqn),
                contentDescription = null,
                modifier = Modifier
                    .height(200.dp)
                    .padding(top = 16.dp)
                    .padding(8.dp)
            )
            Text(
                text = "Sign Up",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = md_theme_light_onPrimaryContainer,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier
            )
            OutlinedTextField(
                value = nameState.value,
                onValueChange = {
                    nameState.value = it
                },
                label = {
                    Text(
                        text = "Name",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                modifier = Modifier
                    .padding(8.dp)
            )
            OutlinedTextField(
                value = phoneNumberState.value,
                onValueChange = {
                    phoneNumberState.value = it
                },
                label = {
                    Text(
                        text = "Phone number",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                modifier = Modifier
                    .padding(8.dp)
            )
            OutlinedTextField(
                value = emailState.value,
                onValueChange = {
                    emailState.value = it
                },
                label = {
                    Text(
                        text = "Email",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                modifier = Modifier
                    .padding(8.dp)
            )
            OutlinedTextField(
                value = passwordState.value,
                onValueChange = {
                    passwordState.value = it
                },
                label = {
                    Text(
                        text = "Password",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                modifier = Modifier
                    .padding(8.dp)
            )
            Button(
                onClick = {
                    viewModel.signUp(
                        name = nameState.value.text,
                        phoneNumber = phoneNumberState.value.text,
                        email = emailState.value.text,
                        password = passwordState.value.text
                    )
                },
                modifier = Modifier
                    .padding(8.dp)
            ) {

                Text(
                    text = "Sign Up",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
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