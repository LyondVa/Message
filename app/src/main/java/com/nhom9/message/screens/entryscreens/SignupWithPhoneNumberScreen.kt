package com.nhom9.message.screens.entryscreens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Phone
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.nhom9.message.ProfileImageCard
import com.nhom9.message.R
import com.nhom9.message.navigateTo
import com.nhom9.message.ui.theme.md_theme_light_onPrimaryContainer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SignUpWithPhoneNumberScreen(navController: NavController, viewModel: MViewModel) {
    val context = LocalContext.current
    CheckSignedIn(viewModel, navController)
    val signupFailed = remember {
        mutableStateOf(false)
    }
    val nameState = remember {
        mutableStateOf(TextFieldValue())
    }
    val phoneNumberState = remember {
        mutableStateOf(TextFieldValue())
    }
    val imageUrl = rememberSaveable {
        mutableStateOf("")
    }
    val onChangeImage: (Uri) -> Unit = {
        imageUrl.value = it.toString()
    }
    val verificationIsVisible = remember {
        mutableStateOf(false)
    }
    val otp = rememberSaveable {
        mutableStateOf("")
    }
    val verificationFailed = remember {
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
            BackHandler {
                navigateTo(navController, DestinationScreen.Entry.route)
            }
            Text(
                text = "Sign Up",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = md_theme_light_onPrimaryContainer,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.padding(16.dp)
            )
            ProfileImageCard(
                imageUrl = null, onChangeImage = onChangeImage
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
                OutlinedTextField(value = nameState.value, onValueChange = {
                    nameState.value = it
                }, label = {
                    Text(
                        text = "Name", style = MaterialTheme.typography.bodyMedium
                    )
                }, shape = RoundedCornerShape(40.dp), modifier = Modifier.padding(end = 8.dp)
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
                            text = "Phone number", style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    shape = RoundedCornerShape(40.dp),
                    modifier = Modifier.padding(8.dp)
                )
            }
            if (signupFailed.value) {
                Text(
                    text = "This number has already been registered",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            if (!verificationIsVisible.value) {
                Button(onClick = {
                    if (phoneNumberState.value.text.isEmpty() || nameState.value.text.isEmpty()) {
                        Toast.makeText(context, "Please Fill In All Fields", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        viewModel.sendVerificationCode(
                            context, phoneNumberState.value.text, {verificationIsVisible.value = true}
                        ) {scope.launch {
                            verificationFailed.value = true
                            delay(5000)
                            verificationFailed.value = false
                        }}
                    }
                }) {
                    Text(
                        text = "Get OTP Code", style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.wrapContentSize()
                ) {
                    Image(
                        painter = painterResource(
                            id = R.drawable.key_encircled
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.dp)
                            .padding(start = 8.dp)
                            .padding(top = 8.dp)
                            .padding(end = 8.dp)
                    )
                    OutlinedTextField(
                        value = otp.value,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = {
                            Text(
                                text = "OTP Code", style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        onValueChange = { if (it.length <= 6) otp.value = it },
                        shape = RoundedCornerShape(40.dp),
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Button(
                    onClick = {
                        if (nameState.value.text.isEmpty() || phoneNumberState.value.text.isEmpty() || otp.value.isEmpty()) {
                            Toast.makeText(context, "PLease fill in all fields", Toast.LENGTH_SHORT)
                                .show()
                        } else if (imageUrl.value != "") {
                            viewModel.uploadImage(Uri.parse(imageUrl.value)) {
                                imageUrl.value = it.toString()
                            }
                            viewModel.signUpWithPhoneNumber(
                                otp.value,
                                nameState.value.text,
                                phoneNumberState.value.text,
                                imageUrl.value
                            ) {
                                Toast.makeText(context, "Incorrect OTP code", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } else {
                            viewModel.signUpWithPhoneNumber(
                                otp.value, nameState.value.text, phoneNumberState.value.text
                            ) {
                                Toast.makeText(context, "Incorrect OTP code", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }, modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = "Sign Up", style = MaterialTheme.typography.bodyMedium
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
            Text(
                text = "Or Register With",
                color = Color.Blue,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
            IconButton(onClick = {
                navigateTo(
                    navController = navController, route = DestinationScreen.SignUp.route
                )
            }) {
                Icon(
                    imageVector = Icons.Outlined.MailOutline,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
    if (viewModel.inProcess.value) {
        CommonProgressbar()
    }
}


