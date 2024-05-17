package com.nhom9.message.authentication

import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.nhom9.message.getActivity
import java.util.concurrent.TimeUnit

class PhoneAuthentication(private val auth: FirebaseAuth) {
    var storedVerificationId: String = ""
    fun sendVerificationCode(context: Context, phoneNumber: String, onCodeSent: () -> Unit, onVerificationFailed:()->Unit) {

        auth.setLanguageCode("en")
        val callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d("phoneBook", "verification completed")
                //signInWithPhoneAuthCredential(context, credential)
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Log.d("phoneBook", "verification failed$p0")
                onVerificationFailed.invoke()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d("phoneBook", "code sent$verificationId")
                storedVerificationId = verificationId
                onCodeSent()
            }

        }
        val options = context.getActivity()?.let {
            PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber("+84$phoneNumber")
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(it)
                .setCallbacks(callback)
                .build()
        }
        if (options != null) {
            Log.d("phoneBook", options.toString())
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    private fun signInWithPhoneAuthCredential(
        context: Context,
        credential: PhoneAuthCredential,
        onSignIn: (String) -> Unit,
    ) {
        context.getActivity()?.let {
            auth.signInWithCredential(credential)
                .addOnCompleteListener(it) { task ->
                    if (task.isSuccessful) {
                        val uid = task.result.user?.uid.toString()
                        Log.d("phoneBook", "signInWithPhoneAuthCredential: $uid")
                        Log.d("phoneBook", "signInWithPhoneAuthCredential currentUserUserId: ${auth.currentUser?.uid}")
                        onSignIn.invoke(uid)
                    } else {
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            Log.d("phoneBook", "wrong otp")
                        }
                    }
                }
        }
    }
}

/*


@Composable
fun CompleteDialogContent(auth: FirebaseAuth) {
    val phoneAuth = PhoneAuthentication(auth)
    val context = LocalContext.current
    var phoneNumber by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var otp by remember {
        mutableStateOf(TextFieldValue(""))
    }
    var isOtpVisible by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .height(300.dp)
            .fillMaxWidth(1f)
            .wrapContentHeight(),
        shape = RoundedCornerShape(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(1f)
                .wrapContentHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Login with phone number", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            TextField(
                placeholder = { Text("Enter phone number") },
                value = phoneNumber,
                onValueChange = {
                    if (it.text.length <= 10) phoneNumber = it
                },
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(top = 4.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            if(isOtpVisible) {
                TextField(
                    value = otp,
                    placeholder = { Text("Enter otp") },
                    onValueChange = { otp = it },
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(top = 4.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            if(!isOtpVisible) {
                Button(
                    onClick = { phoneAuth.onLoginClicked(context,phoneNumber.text) {
                        Log.d("phoneBook","setting otp visible")
                        isOtpVisible = true
                    }
                    },
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(top = 8.dp)
                ) {
                    Text(text = "Send otp", color = Color.White)
                }
            }


            if(isOtpVisible) {
                Button(
                    onClick = { phoneAuth.verifyPhoneNumberWithCode(context, phoneAuth.storedVerificationId,otp.text) },
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(top = 8.dp)
                ) {
                    Text(text = "Verify", color = Color.White)
                }
            }
        }
    }
}*/
