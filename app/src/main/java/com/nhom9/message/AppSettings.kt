package com.nhom9.message

import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val language: Language = Language.ENGLISH,
)
enum class Language (val tag: String, val localName: String){
    ENGLISH("en", "English"), VIETNAMESE("vn", "Tiếng Việt")
}