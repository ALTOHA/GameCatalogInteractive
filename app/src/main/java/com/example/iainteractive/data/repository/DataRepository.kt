package com.example.iainteractive.data.repository

import com.example.iainteractive.core.RetrofitInstance
import com.example.iainteractive.data.model.VideogameModel

class DataRepository{

    private val api = RetrofitInstance.api

    suspend fun getGames(): List<VideogameModel> {
        return api.getGames()
    }
}
