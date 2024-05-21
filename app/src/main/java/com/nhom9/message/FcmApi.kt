package com.nhom9.message

import retrofit2.http.Body
import retrofit2.http.POST

interface FcmApi {
    @POST("/sendMessage")
    suspend fun sendMessage(
        @Body body: SendMessageDto
    )
}