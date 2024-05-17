package com.nhom9.message.screens.entryscreens

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nhom9.message.CheckSignedIn
import com.nhom9.message.CommonProgressbar
import com.nhom9.message.MViewModel
import com.nhom9.message.ProfileImageCard
import com.nhom9.message.R
import com.nhom9.message.ui.theme.md_theme_light_onPrimaryContainer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(navController: NavController, viewModel: MViewModel) {
    val context = LocalContext.current
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
    val passwordVisible = remember {
        mutableStateOf(false)
    }
    val imageUrl = rememberSaveable {
        mutableStateOf("")
    }
    val onChangeImage: (Uri) -> Unit = {
        imageUrl.value = it.toString()
    }
    val signupFailed = remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    CheckSignedIn(viewModel, navController)
    Box(modifier = Modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Sign Up",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = md_theme_light_onPrimaryContainer,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.padding(16.dp)
            )
            ProfileImageCard(
                imageUrl = null,
                onChangeImage = onChangeImage
            )
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
                    shape = RoundedCornerShape(40.dp),
                    modifier = Modifier
                        .padding(8.dp)
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
                    value = phoneNumberState.value,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = {
                        phoneNumberState.value = it
                    },
                    label = {
                        Text(
                            text = "Phone number",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    shape = RoundedCornerShape(40.dp),
                    modifier = Modifier
                        .padding(8.dp)
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
                    shape = RoundedCornerShape(40.dp),
                    modifier = Modifier
                        .padding(8.dp)
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
                            text = "Password",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val image = if (passwordVisible.value)
                            painterResource(id = R.drawable.outline_visibility_24)
                        else
                            painterResource(id = R.drawable.outline_visibility_off_24)
                        IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                            Icon(painter = image, contentDescription = null)
                        }
                    },
                    shape = RoundedCornerShape(40.dp),
                    modifier = Modifier
                        .padding(8.dp)
                )
            }
            if (signupFailed.value) {
                Text(
                    text = "This number has already been registered",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            Button(
                onClick = {
                    if (emailState.value.text.isEmpty() || passwordState.value.text.isEmpty() || nameState.value.text.isEmpty() || phoneNumberState.value.text.isEmpty()) {
                        Toast.makeText(context, "PLease fill in all fields", Toast.LENGTH_SHORT)
                            .show()
                    } else if (imageUrl.value != "") {
                        viewModel.uploadImage(Uri.parse(imageUrl.value)) {
                            imageUrl.value = it.toString()
                        }
                    } else {
                        viewModel.signUp(
                            name = nameState.value.text,
                            phoneNumber = phoneNumberState.value.text,
                            email = emailState.value.text,
                            password = passwordState.value.text,
                            imageUrl = imageUrl.value,
                        ) {
                            scope.launch {
                                signupFailed.value = true
                                delay(5000)
                                signupFailed.value = false
                            }
                        }
                    }
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
    if (viewModel.inProcess.value) {
        CommonProgressbar()
    }
}


