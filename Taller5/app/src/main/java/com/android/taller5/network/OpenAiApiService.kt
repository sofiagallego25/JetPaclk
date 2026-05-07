package com.android.taller5.network

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OpenAiApiService {
    @POST("chat/completions")
    suspend fun getRecipeRecommendation(
        @Header("Authorization") authorization: String,
        @Body request: ChatRequest
    ): ChatResponse
}
