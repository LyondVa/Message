package com.nhom9.message.googleauthentication

import com.nhom9.message.data.UserData

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?,
) {
}