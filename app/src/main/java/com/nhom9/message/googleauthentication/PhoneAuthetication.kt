package com.nhom9.message.googleauthentication

/*
class PhoneAuthActivity : Activity() {

    private fun verifyPhoneNumberWithCode(context: Context,verificationId: String, code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithPhoneAuthCredential(context,credential)
    }

    fun onLoginClicked (context: Context, phoneNumber: String,onCodeSent: () -> Unit) {

        auth.setLanguageCode("en")
        val callback = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d("phoneBook","verification completed")
                signInWithPhoneAuthCredential(context,credential)
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Log.d("phoneBook","verification failed" + p0)
            }

            override fun onCodeSent(verificationId: String,
                                    token: PhoneAuthProvider.ForceResendingToken) {
                Log.d("phoneBook","code sent" + verificationId)
                storedVerificationId = verificationId
                onCodeSent()
            }

        }
        val options = context.getActivity()?.let {
            PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber("+91"+phoneNumber)
                .setTimeout(60L,TimeUnit.SECONDS)
                .setActivity(it)
                .setCallbacks(callback)
                .build()
        }
        if (options != null) {
            Log.d("phoneBook",options.toString())
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    private fun signInWithPhoneAuthCredential(context: Context, credential: PhoneAuthCredential) {
        context.getActivity()?.let {
            auth.signInWithCredential(credential)
                .addOnCompleteListener(it) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = task.result?.user
                        Log.d("phoneBook","logged in")
                    } else {
                        // Sign in failed, display a message and update the UI
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            Log.d("phoneBook","wrong otp")
                        }
                        // Update UI
                    }
                }
        }
    }
}*/
