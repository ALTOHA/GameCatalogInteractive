package com.example.iainteractive.data.network

import com.example.iainteractive.data.model.VideogameModel
import retrofit2.http.GET

interface ApiService {
    @GET("games")
    suspend fun getGames(): List<VideogameModel>
}