package com.nhom9.message.data

data class UserData(
    var userId: String? = "",
    var name: String? = "",
    var phoneNumber: String? = "",
    var imageUrl: String? = "",
    //var handle: String?=""
) {
    fun toMap() = mapOf(
        "userId" to userId,
        "name" to name,
        "phoneNumber" to phoneNumber,
        "imageUrl" to imageUrl,
        //"handle" to handle
    )

}
data class BlockedChats(
    val chatId: String? = "",
    val user1Id: String?="",
    val user2Id: String?=""
)
data class ChatData(
    val chatId: String? = "",
    val user1: ChatUser = ChatUser(),
    val user2: ChatUser = ChatUser()
)

data class ChatUser(
    val userId: String? = "",
    val name: String? = "",
    val imageUrl: String? = "",
    val phoneNumber: String? = ""
)

data class Message(
    var type: String? = "",
    var sendBy: String? = "",
    val content: String? = "",
    val timeStamp: String? = ""
)

data class Status(
    val user: ChatUser = ChatUser(),
    val imageUrl: String? = "",
    val timeStamp: Long? = null
)