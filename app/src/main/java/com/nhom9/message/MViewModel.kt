package com.nhom9.message

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.messaging.messaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.nhom9.message.authentication.PhoneAuthentication
import com.nhom9.message.data.BLOCKED_CHATS
import com.nhom9.message.data.BlockedChats
import com.nhom9.message.data.CHATS
import com.nhom9.message.data.CHAT_REQUESTS
import com.nhom9.message.data.ChatData
import com.nhom9.message.data.ChatRequest
import com.nhom9.message.data.ChatUser
import com.nhom9.message.data.Event
import com.nhom9.message.data.IMAGEURL
import com.nhom9.message.data.MESSAGE
import com.nhom9.message.data.MESSAGE_AUDIO
import com.nhom9.message.data.MESSAGE_IMAGE
import com.nhom9.message.data.MESSAGE_TEXT
import com.nhom9.message.data.Message
import com.nhom9.message.data.PHONE_NUMBER
import com.nhom9.message.data.REPORTS
import com.nhom9.message.data.REQUESTEE
import com.nhom9.message.data.REQUESTER
import com.nhom9.message.data.STATUS
import com.nhom9.message.data.Status
import com.nhom9.message.data.USERID
import com.nhom9.message.data.USERNAME
import com.nhom9.message.data.USER_NODE
import com.nhom9.message.data.UserData
import com.nhom9.message.data.UserReport
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.log.Priority
import io.getstream.video.android.core.Call
import io.getstream.video.android.core.GEO
import io.getstream.video.android.core.StreamVideo
import io.getstream.video.android.core.StreamVideoBuilder
import io.getstream.video.android.core.logging.LoggingLevel
import io.getstream.video.android.model.User
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.io.IOException
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MViewModel @Inject constructor(
    val auth: FirebaseAuth, var db: FirebaseFirestore, var storage: FirebaseStorage
) : ViewModel() {
    var inProcess = mutableStateOf(false)
    val eventMutableState = mutableStateOf<Event<String>?>(null)
    var signIn = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)
    val chats = mutableStateOf<List<ChatData>>(listOf())
    val phoneAuth = PhoneAuthentication(auth)

    //val chatUserIds = mutableStateOf<List<String>>(listOf())
    var inProcessChats = mutableStateOf(false)
    val chatMessages = mutableStateOf<List<Message>>(listOf())
    val inProgressChatMessages = mutableStateOf(false)
    var currentChatMessageListener: ListenerRegistration? = null
    val status = mutableStateOf<List<Status>>(listOf())
    val inProgressStatus = mutableStateOf(false)
    lateinit var call: Call
    lateinit var members: List<String>
    val onToggleTheme = {

    }
    val blockedChats = mutableStateOf<List<BlockedChats>>(listOf())

    init {
        val currentUser = auth.currentUser
        signIn.value = currentUser != null
        currentUser?.uid?.let {
            getUserData(it)
        }
    }

    val myRequests = mutableStateOf<List<ChatRequest>>(listOf())
    val friendsRequests = mutableStateOf<List<ChatRequest>>(listOf())
    fun sendVerificationCode(
        context: Context,
        phoneNumber: String,
        onCodeSend: () -> Unit,
        onVerificationFailed: () -> Unit
    ) {
        var phoneNumberEdited = phoneNumber
        if (phoneNumber.startsWith("0")) {
            phoneNumberEdited = phoneNumber.drop(1)
        }
        phoneAuth.sendVerificationCode(
            context, phoneNumberEdited, onCodeSend, onVerificationFailed
        )
    }

    fun signUpWithPhoneNumber(
        otp: String,
        name: String,
        phoneNumber: String,
        imageUrl: String? = null,
        onNumberAlreadyRegistered: () -> Unit,
        onSignupFailed: () -> Unit
    ) {
        var phoneNumberDisplay = phoneNumber
        if (name.isEmpty() || phoneNumber.isEmpty()) {
            handleException(customMessage = "Please Fill In All Fields")
            return
        } else {
            if (!phoneNumber.startsWith("0")) {
                phoneNumberDisplay = "0$phoneNumber"
            }
            db.collection(USER_NODE).whereEqualTo("phoneNumber", phoneNumber).get()
                .addOnSuccessListener {
                    if (it.isEmpty) {
                        val credential =
                            PhoneAuthProvider.getCredential(phoneAuth.storedVerificationId, otp)
                        auth.signInWithCredential(credential).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                signIn.value = true
                                inProcess.value = false
                                if (imageUrl == "" || imageUrl == null) {
                                    createProfileWithPhoneNumber(name, phoneNumberDisplay)
                                } else {
                                    createProfileWithPhoneNumber(name, phoneNumberDisplay, imageUrl)
                                }
                            } else {
                                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                                    onSignupFailed.invoke()
                                }
                            }
                        }
                    } else {
                        onNumberAlreadyRegistered.invoke()
                    }
                }
        }
    }

    fun signInWithPhoneNumber(
        otp: String,
        phoneNumber: String,
        onSignInFailed: () -> Unit
    ) {
        if (phoneNumber.isEmpty()) {
            handleException(customMessage = "Please Fill In All Fields")
            return
        } else {
            val credential =
                PhoneAuthProvider.getCredential(phoneAuth.storedVerificationId, otp)
            auth.signInWithCredential(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    signIn.value = true
                    inProcess.value = false
                    auth.currentUser?.uid?.let {
                        getUserData(it)
                    }
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        onSignInFailed.invoke()
                    } else {
                        handleException(exception = task.exception)
                    }
                }
            }
        }
    }

    private fun createProfileWithPhoneNumber(
        name: String,
        phoneNumber: String,
        imageUrl: String? = null,
    ) {
        val userId = auth.currentUser?.uid
        val userData = UserData(
            userId = userId, name = name, phoneNumber = phoneNumber, imageUrl = imageUrl
        )
        try {
            inProcess.value = true
            db.collection(USER_NODE).document(userId!!).set(userData)
            inProcess.value = false
            getUserData(userId)
        } catch (e: Exception) {
            handleException(e)
        }
    }


    val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
    var state by mutableStateOf(ChatState())
        private set
    private val api: FcmApi = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8081/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create()

    fun onMessageChange(message: String) {
        state = state.copy(
            messageText = message
        )
    }

    fun signUp(
        name: String,
        phoneNumber: String,
        email: String,
        password: String,
        imageUrl: String,
        onSignupFailed: () -> Unit
    ) {
        if (name.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || password.isEmpty()) {
            handleException(customMessage = "Please Fill In All Fields")
            return
        } else {
            db.collection(USER_NODE).whereEqualTo("phoneNumber", phoneNumber).get()
                .addOnSuccessListener {
                    if (it.isEmpty) {
                        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                            if (it.isSuccessful) {
                                Log.d("TAG", "signUp: User Logged In")
                                signIn.value = true
                                inProcess.value = false
                                if (imageUrl == "") {
                                    createOrUpdateProfile(name, phoneNumber)
                                } else {
                                    createOrUpdateProfile(name, phoneNumber, imageUrl)
                                }
                            } else {
                                Log.d("SIGNUP-ERROR", "auth unsuccessful")
                                handleException(it.exception, "Sign Up failed")
                            }
                        }
                    } else {
                        onSignupFailed.invoke()
                    }
                }
        }
    }

    fun logIn(email: String, password: String, token: String, onLoginFailed: () -> Unit) {
        if (email.isEmpty() || password.isEmpty()) {
            handleException(customMessage = "Please Fill In All Fields")
            return
        } else {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    signIn.value = true
                    inProcess.value = false
                    Log.d("local", token)
                    auth.currentUser?.uid?.let {
                        getUserData(it)
                        createDeviceToken(token)
                    }
                } else {
                    onLoginFailed.invoke()
                }
            }
        }
    }

    fun createDeviceToken(
        token: String = ""
    ) {
        val uid = auth.currentUser?.uid
        val userData = UserData(
            userId = uid,
            deviceToken = token
        )
        uid?.let {
            inProcess.value = true
            db.collection(USER_NODE).document(uid).get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        db.collection(USER_NODE).document(uid)
                            .update(
                                "deviceToken",
                                userData.deviceToken
                            )
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
            db.collection(USER_NODE).document(uid).get().addOnSuccessListener {
                if (it.exists()) {
                    db.collection(USER_NODE).document(uid).update(
                        "name",
                        userData.name,
                        "phoneNumber",
                        userData.phoneNumber,
                        "imageUrl",
                        userData.imageUrl
                    )
                    getUserData(uid)
                    inProcess.value = false //custom
                } else {
                    db.collection(USER_NODE).document(uid).set(userData)
                    inProcess.value = false
                    getUserData(uid)
                }
            }.addOnFailureListener {
                handleException(it, "cannot retrieve user")
            }
        }
    }

    fun saveProfileImage(uri: Uri) {
        uploadImage(uri) {
            updateImageUrl(it.toString())
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
        }.addOnFailureListener {
            handleException(it)
        }

    }

    fun uploadAudio(uri: Uri, metadata: StorageMetadata, onSuccess: (Uri) -> Unit) {
        inProcess.value = true
        val storageReference = storage.reference
        val uuid = UUID.randomUUID()
        val audioReference = storageReference.child("audio/$uuid")
        val uploadTask = audioReference.putFile(uri, metadata)
        uploadTask.addOnSuccessListener {
            val result = it.metadata?.reference?.downloadUrl
            result?.addOnSuccessListener(onSuccess)
            inProcess.value = false
        }.addOnFailureListener {
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
                populateMyRequests()
                populateFriendsRequests()
                getBlockedChats()
            }
        }
    }

    fun handleException(exception: Exception? = null, customMessage: String = "") {
        exception?.printStackTrace()
        val errorMessage = exception?.localizedMessage ?: ""
        val message = if (customMessage.isEmpty()) errorMessage else customMessage
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
                    ), Filter.and(
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
                                    chatId = id, ChatUser(
                                        userData.value?.userId,
                                        userData.value?.name,
                                        userData.value?.imageUrl,
                                        userData.value?.phoneNumber,
                                        userData.value?.deviceToken

                                    ), ChatUser(
                                        chatPartner.userId,
                                        chatPartner.name,
                                        chatPartner.imageUrl,
                                        chatPartner.phoneNumber,
                                        chatPartner.deviceToken
                                    )
                                )
                                db.collection(CHATS).document(id).set(chat)
                            }
                        }.addOnFailureListener {
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

    fun onSendText(chatId: String, content: String) {
        val messageId = db.collection(CHATS).document(chatId).collection(MESSAGE).document().id
        val time = Timestamp.now()
        val message = Message(messageId, MESSAGE_TEXT, userData.value?.userId, content, time)
        db.collection(CHATS).document(chatId).collection(MESSAGE).document(messageId).set(message)
        onMessageChange(content)
    }

    fun onSendImage(chatId: String, imageUri: Uri) {
        val messageId = db.collection(CHATS).document(chatId).collection(MESSAGE).document().id
        val time = Timestamp(Calendar.getInstance().time)
        uploadImage(imageUri) {
            val imageMessage = Message(
                messageId, MESSAGE_IMAGE, userData.value?.userId, it.toString(), time
            )
            db.collection(CHATS).document(chatId).collection(MESSAGE).document(messageId)
                .set(imageMessage)
        }
    }

    fun onSendAudio(chatId: String, metadata: StorageMetadata, audioUri: Uri) {
        val messageId = db.collection(CHATS).document(chatId).collection(MESSAGE).document().id
        val time = Timestamp(Calendar.getInstance().time)
        uploadAudio(audioUri, metadata) {
            val audioMessage = Message(
                messageId, MESSAGE_AUDIO, userData.value?.userId, it.toString(), time
            )
            db.collection(CHATS).document(chatId).collection(MESSAGE).document(messageId)
                .set(audioMessage)
        }

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
            populateStatuses()
        }

    }

    fun createStatus(imageUrl: String) {
        val id = db.collection(STATUS).document().id
        val newStatus = Status(
            statusId = id, ChatUser(
                userData.value?.userId,
                userData.value?.name,
                userData.value?.imageUrl,
                userData.value?.phoneNumber
            ), imageUrl, System.currentTimeMillis()

        )
        db.collection(STATUS).document(id).set(newStatus)
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
                            if (value != null) {
                                status.value = value.toObjects()
                                inProgressStatus.value = false
                            }
                        }

                }
            }
            inProgressStatus.value = false
        }
    }


    fun updateName(name: String) {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            db.collection(USER_NODE).document(uid).update(USERNAME, name)
        } else {
            handleException(customMessage = "User Id is null")
            return
        }
        db.collection(CHATS).where(
            Filter.equalTo("user1.$USERID", uid)
        ).addSnapshotListener { value, error ->
            if (error != null) {
                handleException(error)
            }
            if (value != null) {
                for (document in value.documents) {
                    db.collection(CHATS).document(document.id).update("user1.$USERNAME", name)
                }
            }
        }
        db.collection(CHATS).where(
            Filter.equalTo("user2.$USERID", uid)
        ).addSnapshotListener { value, error ->
            if (error != null) {
                handleException(error)
            }
            if (value != null) {
                for (document in value.documents) {
                    db.collection(CHATS).document(document.id).update("user2.$USERNAME", name)
                }
            }
        }
        for (status in status.value) {
            if (status.user.userId == uid) {
                db.collection(STATUS).document(status.statusId!!).update("user.$USERNAME", name)
            }
        }
        getUserData(uid)
    }

    fun updateImageUrl(imageUrl: String) {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            db.collection(USER_NODE).document(uid).update(IMAGEURL, imageUrl)
        } else {
            handleException(customMessage = "User Id is null")
            return
        }
        db.collection(CHATS).where(
            Filter.equalTo("user1.$USERID", uid)
        ).addSnapshotListener { value, error ->
            if (error != null) {
                handleException(error)
            }
            if (value != null) {
                for (document in value.documents) {
                    db.collection(CHATS).document(document.id).update("user1.$IMAGEURL", imageUrl)
                }
            }
        }
        db.collection(CHATS).where(
            Filter.equalTo("user2.$USERID", uid)
        ).addSnapshotListener { value, error ->
            if (error != null) {
                handleException(error)
            }
            if (value != null) {
                for (document in value.documents) {
                    db.collection(CHATS).document(document.id).update("user2.$IMAGEURL", imageUrl)
                }
            }
        }
        for (status in status.value) {
            if (status.user.userId == uid) {
                db.collection(STATUS).document(status.statusId!!).update("user.$IMAGEURL", imageUrl)
            }
        }
        getUserData(uid)
    }

    fun updatePhoneNumber(phoneNumber: String) {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            db.collection(USER_NODE).document(uid).update(PHONE_NUMBER, phoneNumber)
        } else {
            handleException(customMessage = "User Id is null")
            return
        }
        db.collection(CHATS).where(
            Filter.equalTo("user1.$USERID", uid)
        ).addSnapshotListener { value, error ->
            if (error != null) {
                handleException(error)
            }
            if (value != null) {
                for (document in value.documents) {
                    db.collection(CHATS).document(document.id)
                        .update("user1.$PHONE_NUMBER", phoneNumber)
                }
            }
        }
        db.collection(CHATS).where(
            Filter.equalTo("user2.$USERID", uid)
        ).addSnapshotListener { value, error ->
            if (error != null) {
                handleException(error)
            }
            if (value != null) {
                for (document in value.documents) {
                    db.collection(CHATS).document(document.id)
                        .update("user2.$PHONE_NUMBER", phoneNumber)
                }
            }
        }
        for (status in status.value) {
            if (status.user.userId == uid) {
                db.collection(STATUS).document(status.statusId!!)
                    .update("user.$PHONE_NUMBER", phoneNumber)
            }
        }
        getUserData(uid)
    }

    fun getChatUser(userId: String): ChatUser? {
        for (chat in chats.value) {
            if (chat.user1.userId == userId) {
                return chat.user1
            } else if (chat.user2.userId == userId) {
                return chat.user2
            }
        }
        return null
    }

    fun getBlockedChats() {
        db.collection(BLOCKED_CHATS).where(
            Filter.or(
                Filter.equalTo("blocker1Id", userData.value?.userId),
                Filter.equalTo("blocker2Id", userData.value?.userId)
            )
        ).addSnapshotListener { value, error ->
            if (error != null) {
                handleException(error)
            }
            if (value != null) {
                blockedChats.value = value.documents.mapNotNull {
                    it.toObject<BlockedChats>()
                }
                inProcessChats.value = false
            }
        }
    }

    private fun updateMessage(chatId: String, message: Message, content: String = "") {
        if (content == "") {
            db.collection(CHATS).document(chatId).collection(MESSAGE).document(message.messageId!!)
                .update("deleted", true)
        } else {
            db.collection(CHATS).document(chatId).collection(MESSAGE).document(message.messageId!!)
                .update("edited", true)
            db.collection(CHATS).document(chatId).collection(MESSAGE).document(message.messageId!!)
                .update("content", content)
        }
        populateMessages(chatId)
    }

    fun deleteMessage(chatId: String, message: Message) {
        message.isDeleted = true
        updateMessage(chatId, message)
    }

    fun getChatPhotos(photoList: MutableList<String>) {
        for (message in chatMessages.value) {
            if (message.type == MESSAGE_IMAGE) {
                photoList.add(message.content!!)
            }
        }
    }

    fun reportUser(userId: String, reportOption: String, reportContent: String) {
        val id = db.collection(REPORTS).document().id
        val userReport = UserReport(
            userId = userId, reportOption = reportOption, reportContent = reportContent
        )
        db.collection(REPORTS).document(id).set(userReport)
    }

    fun onSendChatRequest(
        phoneNumber: String,
        onChatExist: () -> Unit,
        onRequestExisted: () -> Unit,
        onPhoneNumberNotFound: () -> Unit
    ) {
        if (phoneNumber.isEmpty()) {
            handleException(customMessage = "Number can only contain digits")
        } else {
            db.collection(CHATS).where(
                Filter.or(
                    Filter.and(
                        Filter.equalTo("user1.phoneNumber", phoneNumber),
                        Filter.equalTo("user2.phoneNumber", userData.value?.phoneNumber)
                    ), Filter.and(
                        Filter.equalTo("user1.phoneNumber", userData.value?.phoneNumber),
                        Filter.equalTo("user2.phoneNumber", phoneNumber)
                    )
                )
            ).get().addOnSuccessListener {
                if (it.isEmpty) {
                    db.collection(USER_NODE).whereEqualTo("phoneNumber", phoneNumber).get()
                        .addOnSuccessListener {
                            if (it.isEmpty) {
                                onPhoneNumberNotFound.invoke()
                                handleException(customMessage = "The number provided cannot be found")
                            } else {
                                val chatPartner = it.toObjects<UserData>()[0]
                                val requesteeId = chatPartner.userId
                                db.collection(CHAT_REQUESTS)
                                    .whereEqualTo("$REQUESTEE.$USERID", requesteeId).get()
                                    .addOnSuccessListener {
                                        if (it.isEmpty) {
                                            val requestId =
                                                db.collection(CHAT_REQUESTS).document().id
                                            val chatRequest = ChatRequest(
                                                requestId = requestId,
                                                requester = ChatUser(
                                                    userId = userData.value?.userId,
                                                    name = userData.value?.name,
                                                    imageUrl = userData.value?.imageUrl,
                                                    phoneNumber = userData.value?.phoneNumber
                                                ),
                                                requestee = ChatUser(
                                                    userId = chatPartner.userId,
                                                    name = chatPartner.name,
                                                    imageUrl = chatPartner.imageUrl,
                                                    phoneNumber = chatPartner.phoneNumber
                                                ),
                                                timeStamp = Timestamp.now()
                                            )
                                            db.collection(CHAT_REQUESTS).document(requestId)
                                                .set(chatRequest)
                                        } else {
                                            onRequestExisted.invoke()
                                        }
                                    }
                            }
                        }.addOnFailureListener {
                            handleException(customMessage = "Add chat db.collection error")
                        }
                } else {
                    onChatExist.invoke()
                    handleException(customMessage = "Chat already exists")
                }
            }
        }
    }


    fun populateMyRequests() {
        db.collection(CHAT_REQUESTS).whereEqualTo("$REQUESTER.$USERID", userData.value?.userId)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    handleException(error)
                }
                if (value != null) {
                    myRequests.value = value.documents.mapNotNull {
                        it.toObject<ChatRequest>()
                    }.sortedBy { it.timeStamp }
                }
            }
    }

    fun populateFriendsRequests() {
        db.collection(CHAT_REQUESTS).whereEqualTo("$REQUESTEE.$USERID", userData.value?.userId)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    handleException(error)
                }
                if (value != null) {
                    friendsRequests.value = value.documents.mapNotNull {
                        it.toObject<ChatRequest>()
                    }.sortedBy { it.timeStamp }
                }
            }
    }

    fun getUserDataFromRequest(userId: String, isMyRequest: Boolean): ChatUser? {
        if (isMyRequest) {
            myRequests.value.forEach {
                if (it.requestee?.userId == userId) {
                    return it.requestee
                }
            }
        } else {
            friendsRequests.value.forEach {
                if (it.requester?.userId == userId) {
                    return it.requester
                }
            }
        }
        return null
    }

    fun onDeleteRequest(requestId: String, onRequestNotExisting: () -> Unit) {
        val docRef = db.collection(CHAT_REQUESTS).document(requestId)
        val updates = hashMapOf<String, Any>(
            "requestId" to FieldValue.delete(),
            "requester" to FieldValue.delete(),
            "requestee" to FieldValue.delete(),
            "requestAccepted" to FieldValue.delete(),
            "timeStamp" to FieldValue.delete(),
        )
        docRef.update(updates).addOnCompleteListener {
            if (it.isSuccessful) {
                docRef.delete()
            } else {
                onRequestNotExisting.invoke()
            }
        }
    }

    fun onAcceptRequest(chatRequest: ChatRequest) {
        onAddChat(chatRequest.requester?.phoneNumber!!)
        onDeleteRequest(chatRequest.requestId!!) {
            handleException(customMessage = "onAcceptRequest: ${chatRequest.requester.userId} to ${chatRequest.requestee?.userId} failed")
        }
    }

    fun proceedService(userID: String, chatID: String, chatuserID: String, context: Context) {
        val userId = userID
        val userToken = tokenGenerate(userId)
        val callId = chatID
        val user = User(
            id = userId,
            name = userId
        )

        StreamVideo.removeClient()
        val client = StreamVideoBuilder(
            context = context,
            apiKey = "zecrkhvczah3",
            geo = GEO.GlobalEdgeNetwork,
            user = user,
            token = userToken!!,
            loggingLevel = LoggingLevel(priority = Priority.DEBUG)
        ).build()

        members = listOf(userID, chatuserID)
        call = client.call("default", callId)
        Log.d("TAG", "Done")

    }

    private fun tokenGenerate(
        userId: String
    ): String? {
        val algorithm =
            Algorithm.HMAC256("52pr5gfwf725q42ygr3uvndybxyxu7kghz4z375jkgnta9puvc3ywkkwk75vphyh")

        return JWT.create()
            .withIssuer("example.com")
            .withClaim("user_id", userId)
//            .withExpiresAt(Date(System.currentTimeMillis() + 3600000)) // Token expires in 1 hour
            .sign(algorithm)
    }


    init {
        viewModelScope.launch {
            Firebase.messaging.subscribeToTopic("chat").await()
        }
    }

    fun onRemoteTokenChange(newToken: String) {
        state = state.copy(
            remoteToken = newToken
        )
    }

    fun sendMessage(isBroadcast: Boolean, title: String, type: String, context: Context) {
        val messageText: String
        if (type == "1") {
            messageText = state.messageText
        } else if (type == "2") {
            messageText = context.getString(R.string.there_is_video_call_for_you)
        } else {
            messageText = context.getString(R.string.there_is_audio_call_for_you)
        }
        viewModelScope.launch {
            val messageDto = SendMessageDto(
                to = if (isBroadcast) null else state.remoteToken,
                notification = NotificationBody(
                    title = title,
                    body = messageText
                )
            )
            Log.d("name", messageDto.notification.title)
            Log.d("text", messageDto.notification.body)
            Log.d("token", messageDto.to.toString())
            try {
                if (isBroadcast) {

                } else {
                    api.sendMessage(messageDto)
                }

                state = state.copy(
                    messageText = ""
                )
            } catch (e: HttpException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun getChatId(userId: String): String? {
        for (chat in chats.value) {
            if (chat.user1.userId == userId || chat.user2.userId == userId) {
                return chat.chatId
            }
        }
        return null
    }
    //hi
}
