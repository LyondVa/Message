package com.nhom9.message.data

import com.google.firebase.Timestamp
import java.util.Locale

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
    val text3: String,
    val title_vi: String = "", // Vietnamese translations
    val text1_vi: String = "",
    val text2_vi: String = "",
    val text3_vi: String = ""
) {
    OPTION1(
        "Harassment",
        "Someone keeps sending you messages even when you don't respond",
        "People are making fun of you in a chat",
        "A person is threatening to share your private information",
        "Quấy rối", // Vietnamese translations
        "Bạn liên tục nhận được tin nhắn ngay cả khi không trả lời",
        "Mọi người đang chế giễu bạn trong cuộc trò chuyện",
        "Một người đang đe dọa chia sẻ thông tin cá nhân của bạn"
    ),
    OPTION2(
        "Scam and frauds",
        "You gat a message about winning a prize for a contest that you didn't enter",
        "Someone claiming to have lots of money offers to buy you things to get to know you",
        "Someone offers you money to like their posts or leave a good review for a product",
        "Lừa đảo", // Vietnamese translations
        "Bạn nhận được tin nhắn về việc trúng giải thưởng cho một cuộc thi mà bạn không tham gia",
        "Một người tự nhận mình có nhiều tiền đề nghị mua đồ cho bạn để làm quen",
        "Ai đó đề nghị bạn tiền để thích bài đăng của họ hoặc để lại đánh giá tốt cho sản phẩm"
    ),
    OPTION3(
        "Hate speech",
        "A friend is called names because of their religion",
        "Someone makes fun of your sexuality",
        "People are making racist comments in a group chat",
        "Diễn ngôn thù địch", // Vietnamese translations
        "Một người bạn bị gọi tên vì tôn giáo của họ",
        "Ai đó chế giễu xu hướng tình dục của bạn",
        "Mọi người đang đưa ra những bình luận phân biệt chủng tộc trong cuộc trò chuyện nhóm"
    ),
    OPTION4(
        "Pretending to be someone",
        "Someone is imitating you",
        "Someone is imitating your friend",
        "Someone is imitating a famous person",
        "Giả mạo danh tính", // Vietnamese translations
        "Một người đang bắt chước bạn",
        "Một người đang bắt chước bạn của bạn",
        "Một người đang bắt chước một người nổi tiếng"
    ),
    OPTION5(
        "Unauthorised sales",
        "Someone encourages you to buy drug paraphernalia",
        "A friend is recommending you illegal drugs",
        "People selling medicines in a chat",
        "Bán hàng trái phép", // Vietnamese translations
        "Một người khuyến khích bạn mua dụng cụ ma túy",
        "Một người bạn đang giới thiệu cho bạn các loại ma túy bất hợp pháp",
        "Mọi người đang bán thuốc trong cuộc trò chuyện"
    ),
    OPTION6(
        "Sharing inappropriate things",
        "Someone shares a photo of internal organs",
        "Unsolicited sexual content is shared",
        "Content of illegal nature is being sent",
        "Chia sẻ nội dung không phù hợp", // Vietnamese translations
        "Một người chia sẻ ảnh về nội tạng",
        "Nội dung tình dục không mong muốn được chia sẻ",
        "Nội dung bất hợp pháp đang được gửi đi"
    );

    fun getTranslatedTitle(currentLocale: Locale): String {
        return when (currentLocale.language) {
            "vi" -> title_vi
            else -> title
        }
    }

    fun getTranslatedText1(currentLocale: Locale): String {
        return when (currentLocale.language) {
            "vi" -> text1_vi
            else -> text1
        }
    }fun getTranslatedText2(currentLocale: Locale): String {
        return when (currentLocale.language) {
            "vi" -> text2_vi
            else -> text2
        }
    }fun getTranslatedText3(currentLocale: Locale): String {
        return when (currentLocale.language) {
            "vi" -> text3_vi
            else -> text3
        }
    }
}