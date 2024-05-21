package com.nhom9.message.screens.entryscreens

import android.util.Log
import android.view.WindowManager
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.nhom9.message.CheckSignedIn
import com.nhom9.message.CommonProgressbar
import com.nhom9.message.DestinationScreen
import com.nhom9.message.MViewModel
import com.nhom9.message.R
import com.nhom9.message.navigateTo
import com.nhom9.message.ui.theme.md_theme_light_onPrimaryContainer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun LoginScreen(navController: NavController, viewModel: MViewModel) {
    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
    val emailState = remember {
        mutableStateOf(TextFieldValue())
    }
    val passwordState = remember {
        mutableStateOf(TextFieldValue())
    }
    val loginFailed = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val clipboardManager = LocalClipboardManager.current
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
                    val passwordVisible = remember {
                        mutableStateOf(false)
                    }
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
                                    text = stringResource(R.string.email),
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
                            value = passwordState.value,
                            onValueChange = {
                                passwordState.value = it
                            },
                            label = {
                                Text(
                                    text = stringResource(R.string.password),
                                    style = MaterialTheme.typography.labelMedium
                                )
                            },
                            visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            trailingIcon = {
                                val image =
                                    if (passwordVisible.value) painterResource(id = R.drawable.outline_visibility_24)
                                    else painterResource(id = R.drawable.outline_visibility_off_24)
                                IconButton(onClick = {
                                    passwordVisible.value = !passwordVisible.value
                                }) {
                                    Icon(painter = image, contentDescription = null)
                                }
                            },
                            shape = RoundedCornerShape(40.dp),
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .fillMaxWidth()
                        )
                    }
                    if (loginFailed.value) {
                        Text(
                            text = "Please check your login information again",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    Button(
                        onClick = {
                            if (emailState.value.text.isEmpty() || passwordState.value.text.isEmpty()
                            ) {
                                Toast.makeText(
                                    context, "Please fill in all fields!", Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                scope.launch {
                                    val localToken = Firebase.messaging.token.await()
                                    clipboardManager.setText(AnnotatedString(localToken))
                                    viewModel.logIn(emailState.value.text, passwordState.value.text, localToken) {
                                        scope.launch {
                                            loginFailed.value = true
                                            delay(5000)
                                            loginFailed.value = false
                                        }
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
                    /*Text(text = stringResource(R.string.forgotten_password),
                        color = Color.Blue,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                navigateTo(navController, DestinationScreen.SignUp.route)
                            })*/
                }
            }
        }
    }
    if (viewModel.inProcess.value) {
        CommonProgressbar()
    }
}
