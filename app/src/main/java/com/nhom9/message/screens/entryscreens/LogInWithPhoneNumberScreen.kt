package com.nhom9.message.screens.entryscreens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LogInWithPhoneNumberScreen(navController: NavController, viewModel: MViewModel) {
    val phoneNumberState = remember {
        mutableStateOf(TextFieldValue())
    }
    val otpCodeState = remember {
        mutableStateOf(TextFieldValue())
    }
    val loginFailed = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val verificationIsVisible = remember {
        mutableStateOf(false)
    }
    val verificationFailed = remember {
        mutableStateOf(false)
    }
    BackHandler {
        navigateTo(navController, DestinationScreen.Entry.route)
    }

    CheckSignedIn(viewModel, navController)

    Scaffold { padding ->
        Box {
            Box(modifier = Modifier.padding(padding)) {
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
                        text = stringResource(id = R.string.login),
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = md_theme_light_onPrimaryContainer,
                        fontFamily = FontFamily.SansSerif,
                        modifier = Modifier
                    )
                    Text(
                        text = stringResource(R.string.sign_in_to_continue),
                        style = MaterialTheme.typography.headlineSmall,
                        color = md_theme_light_onPrimaryContainer
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.wrapContentSize()
                    ) {
                        Image(
                            imageVector = Icons.Outlined.Phone,
                            contentDescription = null,
                            modifier = Modifier
                                .size(60.dp)
                                .padding(start = 8.dp)
                                .padding(top = 8.dp)
                                .padding(end = 8.dp)
                        )
                        OutlinedTextField(
                            value = phoneNumberState.value,
                            onValueChange = {
                                if (it.text.length <= 10) {
                                    phoneNumberState.value = it
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            label = {
                                Text(
                                    text = "Phone Number",
                                    style = MaterialTheme.typography.labelMedium
                                )
                            },
                            shape = RoundedCornerShape(40.dp),
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .fillMaxWidth()
                        )
                    }
                    if (!verificationIsVisible.value) {
                        Button(
                            onClick = {
                                if (phoneNumberState.value.text.isEmpty()) {
                                    Toast.makeText(
                                        context, "Please fill in all fields!", Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    viewModel.sendVerificationCode(
                                        context, phoneNumberState.value.text, {verificationIsVisible.value = true}
                                    ) {scope.launch {
                                        verificationFailed.value = true
                                        delay(5000)
                                        verificationFailed.value = false
                                    }}
                                }
                            }, modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Get Verification Code",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.wrapContentSize()
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
                                value = otpCodeState.value,
                                onValueChange = {
                                    if (it.text.length <= 6) {
                                        otpCodeState.value = it
                                    }
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                label = {
                                    Text(
                                        text = "Verification Code",
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
                                if (phoneNumberState.value.text.isEmpty() || otpCodeState.value.text.isEmpty()) {
                                    Toast.makeText(
                                        context, "Please fill in all fields!", Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    viewModel.signInWithPhoneNumber(
                                        otpCodeState.value.text, phoneNumberState.value.text
                                    ) {
                                        scope.launch {
                                            loginFailed.value = true
                                            delay(5000)
                                            loginFailed.value = false
                                        }
                                    }
                                }
                            }, modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {

                            Text(
                                text = stringResource(id = R.string.login),
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                    if(verificationFailed.value){
                        Text(
                            text = "Please Check Your Phone Number",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    if (loginFailed.value) {
                        Text(
                            text = "Please Check Your OTP Code",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    Text(
                        text = "Or Sign In With",
                        color = Color.Blue,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(8.dp)
                    )
                    IconButton(onClick = {
                        navigateTo(
                            navController = navController,
                            route = DestinationScreen.Login.route
                        )
                    }) {
                        Icon(
                            imageVector =  Icons.Outlined.MailOutline,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }
    if (viewModel.inProcess.value) {
        CommonProgressbar()
    }
}
