package com.nhom9.message

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.toObject
import com.nhom9.message.data.Event
import com.nhom9.message.data.USER_NODE
import com.nhom9.message.data.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MViewModel @Inject constructor(
    val auth: FirebaseAuth,
    var db: FirebaseFirestore
) : ViewModel() {

    var inProcess = mutableStateOf(false)
    val eventMutableState = mutableStateOf<Event<String>?>(null)
    var signIn = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)

    init {
        val currentUser = auth.currentUser
        signIn.value = currentUser != null
        currentUser?.uid?.let {
            getUserData(it)
        }
    }

    fun signUp(name: String, number: String, email: String, password: String) {
        inProcess.value = true

        if (name.isEmpty() || number.isEmpty() || email.isEmpty() || password.isEmpty()) {
            handleException(customMessage = "Please Fill In All Fields")
            return
        }

        inProcess.value = true
        db.collection(USER_NODE).whereEqualTo("number", number).get().addOnSuccessListener {
            if (it.isEmpty) {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d("TAG", "signUp: User Logged In")
                        signIn.value = true
                        createOrUpdateProfile(name, number)
                    } else {
                        handleException(it.exception, "Sign Up failed")
                    }
                }
            } else {
                handleException(customMessage = "number already exists")
                inProcess.value = false
            }
        }
    }

    fun logIn(email: String, password: String) {
        inProcess.value = true

        if (email.isEmpty() || password.isEmpty()) {
            handleException(customMessage = "Please Fill In All Fields")
            return
        }
        else {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    signIn.value = true
                    inProcess.value = false
                    auth.currentUser?.uid?.let {
                        getUserData(it)
                    }
                }
                else {
                    handleException(customMessage = "Login Failed")
                }
            }
        }
    }

    fun createOrUpdateProfile(
        name: String? = null,
        number: String? = null,
        imageUrl: String? = null
    ) {
        var uid = auth.currentUser?.uid
        val userData = UserData(
            userId = uid,
            name = name ?: userData.value?.name,
            number = number ?: userData.value?.number,
            imageUrl = imageUrl ?: userData.value?.number
        )
        uid?.let {
            inProcess.value = true
            db.collection(USER_NODE).document(uid).get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        //update user data
                    } else {
                        db.collection(USER_NODE).document(uid).set(userData)
                        inProcess.value = false
                        getUserData(uid)
                    }
                }
                .addOnFailureListener {
                    handleException(it, "cannot retrieve user")
                }
        }
    }

    fun getUserData(uid: String) {
        inProcess.value = true
        db.collection(USER_NODE).document(uid).addSnapshotListener { value, error ->
            if (error != null) {
                handleException(error, "cannot retrieve user")
            }
            if (value != null) {
                var user = value.toObject<UserData>()
                userData.value = user
                inProcess.value = false
            }
        }
    }


    fun handleException(exception: Exception? = null, customMessage: String = "") {
        Log.e("MessageApp", "Login exception", exception)
        exception?.printStackTrace()
        val errorMessage = exception?.localizedMessage ?: ""
        val message = if (customMessage.isEmpty()) errorMessage else customMessage
        eventMutableState.value = Event(message)
        inProcess.value = true
    }
}
