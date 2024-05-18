package com.nhom9.message.authentication

import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
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
}
