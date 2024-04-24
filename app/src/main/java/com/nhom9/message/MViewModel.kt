package com.nhom9.message

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.storage.FirebaseStorage
import com.nhom9.message.data.CHATS
import com.nhom9.message.data.ChatData
import com.nhom9.message.data.ChatUser
import com.nhom9.message.data.Event
import com.nhom9.message.data.MESSAGE
import com.nhom9.message.data.Message
import com.nhom9.message.data.STATUS
import com.nhom9.message.data.Status
import com.nhom9.message.data.USER_NODE
import com.nhom9.message.data.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MViewModel @Inject constructor(
    val auth: FirebaseAuth,
    var db: FirebaseFirestore,
    var storage: FirebaseStorage
) : ViewModel() {

    var inProcess = mutableStateOf(false)
    val eventMutableState = mutableStateOf<Event<String>?>(null)
    var signIn = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)
    val chats = mutableStateOf<List<ChatData>>(listOf())
    var inProcessChats = mutableStateOf(false)
    val chatMessages = mutableStateOf<List<Message>>(listOf())
    val inProgressChatMessages = mutableStateOf(false)
    var currentChatMessageListener: ListenerRegistration? = null
    val status = mutableStateOf<List<Status>>(listOf())
    val inProgressStatus = mutableStateOf(false)

    init {
        val currentUser = auth.currentUser
        signIn.value = currentUser != null
        currentUser?.uid?.let {
            getUserData(it)
        }
    }

    fun signUp(name: String, phoneNumber: String, email: String, password: String) {
        if (name.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || password.isEmpty()) {
            handleException(customMessage = "Please Fill In All Fields")
            return
        } else {
            db.collection(USER_NODE).whereEqualTo("phoneNumber", phoneNumber).get().addOnSuccessListener {
                if (it.isEmpty) {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.d("TAG", "signUp: User Logged In")
                            signIn.value = true
                            inProcess.value = false
                            createOrUpdateProfile(name, phoneNumber)
                        } else {
                            Log.d("SIGNUP-ERROR", "auth unsuccessful")
                            handleException(it.exception, "Sign Up failed")
                        }
                    }
                } else {
                    handleException(customMessage = "number already exists")
                }
            }
        }
    }

    fun logIn(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            handleException(customMessage = "Please Fill In All Fields")
            return
        } else {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    signIn.value = true
                    inProcess.value = false
                    auth.currentUser?.uid?.let {
                        getUserData(it)
                    }
                } else {
                    handleException(customMessage = "Login Failed")
                }
            }
        }
    }

    fun createOrUpdateProfile(
        name: String? = null,
        phoneNumber: String? = null,
        imageUrl: String? = null,
    ) {
        val uid = auth.currentUser?.uid
        val userData = UserData(
            userId = uid,
            name = name ?: userData.value?.name,
            phoneNumber = phoneNumber ?: userData.value?.phoneNumber,
            imageUrl = imageUrl ?: userData.value?.imageUrl
        )
        uid?.let {
            inProcess.value = true
            db.collection(USER_NODE).document(uid).get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        db.collection(USER_NODE).document(uid)
                            .update("name", userData.name, "phoneNumber", userData.phoneNumber, "imageUrl", userData.imageUrl)
                        getUserData(uid)
                        inProcess.value = false //custom
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

    fun saveProfileImage(uri: Uri) {
        uploadImage(uri) {
            createOrUpdateProfile(imageUrl = it.toString())
        }
    }

    fun uploadImage(uri: Uri, onSuccess: (Uri) -> Unit) {
        inProcess.value = true
        val storageReference = storage.reference
        val uuid = UUID.randomUUID()
        val imageReference = storageReference.child("image/$uuid")
        val uploadTask = imageReference.putFile(uri)
        uploadTask.addOnSuccessListener {
            val result = it.metadata?.reference?.downloadUrl
            result?.addOnSuccessListener(onSuccess)
            inProcess.value = false
        }
            .addOnFailureListener {
                handleException(it)
            }

    }


    fun getUserData(uid: String) {
        inProcess.value = true
        db.collection(USER_NODE).document(uid).addSnapshotListener { value, error ->
            if (error != null) {
                handleException(error, "cannot retrieve user")
            }
            if (value != null) {
                val user = value.toObject<UserData>()
                userData.value = user
                inProcess.value = false
                populateChats()
                populateStatuses()
            }
        }
    }

    fun handleException(exception: Exception? = null, customMessage: String = "") {
        Log.e("MessageApp", customMessage, exception)
        exception?.printStackTrace()
        val errorMessage = exception?.localizedMessage ?: ""
        val message = if (customMessage.isNullOrEmpty()) errorMessage else customMessage
        eventMutableState.value = Event(message)
        inProcess.value = false
    }

    fun logOut() {
        auth.signOut()
        signIn.value = false
        userData.value = null
        depopulateMessages()
        currentChatMessageListener = null
        eventMutableState.value = Event("Logged Out")
    }

    fun onAddChat(phoneNumber: String) {
        if (phoneNumber.isEmpty()) {
            handleException(customMessage = "Number can only contain digits")
        } else {
            db.collection(CHATS).where(
                Filter.or(
                    Filter.and(
                        Filter.equalTo("user1.phoneNumber", phoneNumber),
                        Filter.equalTo("user2.phoneNumber", userData.value?.phoneNumber)
                    ),
                    Filter.and(
                        Filter.equalTo("user1.phoneNumber", userData.value?.phoneNumber),
                        Filter.equalTo("user2.phoneNumber", phoneNumber)
                    )
                )
            ).get().addOnSuccessListener {
                if (it.isEmpty) {
                    db.collection(USER_NODE).whereEqualTo("phoneNumber", phoneNumber).get()
                        .addOnSuccessListener {
                            Log.d("Message Error", "phoneNumber: $phoneNumber")
                            if (it.isEmpty) {
                                handleException(customMessage = "The number provided cannot be found")
                            } else {
                                val chatPartner = it.toObjects<UserData>()[0]
                                val id = db.collection(CHATS).document().id
                                val chat = ChatData(
                                    chatId = id,
                                    ChatUser(
                                        userData.value?.userId,
                                        userData.value?.name,
                                        userData.value?.imageUrl,
                                        userData.value?.phoneNumber
                                    ),
                                    ChatUser(
                                        chatPartner.userId,
                                        chatPartner.name,
                                        chatPartner.imageUrl,
                                        chatPartner.phoneNumber
                                    )
                                )
                                db.collection(CHATS).document(id).set(chat)
                            }
                        }
                        .addOnFailureListener {
                            handleException(customMessage = "Add chat db.collection error")
                        }
                } else {
                    handleException(customMessage = "Chat already exists")
                }
            }
        }
    }

    fun populateChats() {
        inProcessChats.value = true
        db.collection(CHATS).where(
            Filter.or(
                Filter.equalTo("user1.userId", userData.value?.userId),
                Filter.equalTo("user2.userId", userData.value?.userId)
            )
        ).addSnapshotListener { value, error ->
            if (error != null) {
                handleException(error)
            }
            if (value != null) {
                chats.value = value.documents.mapNotNull {
                    it.toObject<ChatData>()
                }
                inProcessChats.value = false
            }
        }
    }

    fun onSendReply(chatId: String, message: String) {
        val time = Calendar.getInstance().time.toString()
        val message = Message(userData.value?.userId, message, time)
        db.collection(CHATS).document(chatId).collection(MESSAGE).document().set(message)
    }

    fun populateMessages(chatID: String) {
        inProgressChatMessages.value = true
        currentChatMessageListener = db.collection(CHATS).document(chatID).collection(MESSAGE)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    handleException(error)
                }
                if (value != null) {
                    chatMessages.value = value.documents.mapNotNull {
                        it.toObject<Message>()
                    }.sortedBy { it.timeStamp }
                    inProgressChatMessages.value = false
                }
            }
    }

    fun depopulateMessages() {
        chatMessages.value = listOf()
        currentChatMessageListener = null
    }

    fun uploadStatus(uri: Uri) {
        uploadImage(uri) {
            createStatus(it.toString())
        }
    }

    fun createStatus(imageUrl: String) {
        val newStatus = Status(
            ChatUser(
                userData.value?.userId,
                userData.value?.name,
                userData.value?.imageUrl,
                userData.value?.phoneNumber
            ),
            imageUrl,
            System.currentTimeMillis()

        )
        db.collection(STATUS).document().set(newStatus)
    }

    fun populateStatuses() {
        val timeDelta = 24L * 60 * 60 * 1000
        val cutOff = System.currentTimeMillis() - timeDelta
        inProgressStatus.value = true
        db.collection(CHATS).where(
            Filter.or(
                Filter.equalTo("user1.userId", userData.value?.userId),
                Filter.equalTo("user2.userId", userData.value?.userId)
            )
        ).addSnapshotListener { value, error ->
            if (error != null) {
                handleException(error)
                inProgressStatus.value = false
            }
            if (value != null) {
                val currentConnections = arrayListOf(userData.value?.userId)
                val chats = value.toObjects<ChatData>()
                chats.forEach { chat ->
                    if (chat.user1.userId == userData.value?.userId) {
                        currentConnections.add(chat.user2.userId)
                    } else {
                        currentConnections.add(chat.user1.userId)
                    }
                    db.collection(STATUS).whereGreaterThan("timeStamp", cutOff)
                        .whereIn("user.userId", currentConnections)
                        .addSnapshotListener { value, error ->
                            if (error != null) {
                                handleException(error)
                                inProgressStatus.value = false
                            }
                            if (value!=null) {
                                status.value = value.toObjects()
                                inProgressStatus.value = false
                            }
                        }

                }
            }
            inProgressStatus.value = false
        }
    }
}

