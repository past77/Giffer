package com.testtask.giphy.giffer.api

import retrofit2.http.GET
import retrofit2.http.Query


interface GiphyApi {

    companion object {
        const val BASE_URL = "https://api.giphy.com/v1/gifs/"
        const val API_KEY = "ow3yxv4c5OWmkLrCmyue27oGPXwCbLVF"
    }

    @GET("search")
    suspend fun searchImages(
        @Query("q")
        searchQueryPhrase: String = "all",
        @Query("api_key")
        apiKey: String = API_KEY,
        @Query("offset")
        startResultPosition: Int = 0,
        @Query("rating")
        rating: String = "g",
        @Query("limit")
        maxObjectsNumber: Int = 5,
        @Query("lang")
        lang: String = "en"

    )
}