package com.nhom9.message.data

import com.google.firebase.Timestamp

data class UserData(
    var userId: String? = "",
    var name: String? = "",
    var phoneNumber: String? = "",
    var imageUrl: String? = "",
    var deviceToken: String? = ""
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
    val user1Id: String? = "",
    val user2Id: String? = ""
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
    val phoneNumber: String? = "",
    val deviceToken: String? = ""
)

data class Message(
    var messageId: String? = "",
    var type: String? = "",
    var sendBy: String? = "",
    val content: String? = "",
    val timeStamp: Timestamp? = Timestamp.now(),
    var isEdited: Boolean = false,
    var isDeleted: Boolean = false
)

data class Status(
    val statusId: String? = "",
    val user: ChatUser = ChatUser(),
    val imageUrl: String? = "",
    val timeStamp: Long? = null
)

data class UserReport(
    val userId: String,
    val reportOption: String,
    val reportContent: String
)


data class ChatRequest(
    val requestId: String? = "",
    val requester: ChatUser? = null,
    val requestee: ChatUser? = null,
    val requestAccepted: Boolean? = false,
    val timeStamp: Timestamp? = Timestamp.now()
)

enum class ReportOption(
    val title: String,
    val text1: String,
    val text2: String,
    val text3: String
) {
    OPTION1(
        "Harassment",
        "Someone keeps sanding you messages even when you don't respond",
        "People are making fun of you in a chat",
        "A person is threatening to share your private information"
    ),
    OPTION2(
        "Scam and frauds",
        "You gat a message about winning a prize for a contest that you didn't enter",
        "Someone claiming to have lots of money offers to buy you things to get to know you",
        "Someone offers you money to like their posts or leave a good review for a product"
    ),
    OPTION3(
        "Hate speech",
        "A friend is called names because of their religion",
        "Someone makes fun of your sexuality",
        "People are making racist comments in a group chat"
    ),
    OPTION4(
        "Pretending to be someone",
        "Someone is imitating you",
        "Someone is imitating your friend",
        "Someone is imitating a famous person"
    ),
    OPTION5(
        "Unauthorised sales",
        "Someone encourages you to buy drug paraphernalia",
        "A friend is recommending you illegal drugs",
        "People selling medicines in a chat"
    ),
    OPTION6(
        "Sharing inappropriate things",
        "Someone shares a photo of internal organs",
        "Unsolicited sexual content is shared",
        "Content of illegal nature is being sent"
    ),
}