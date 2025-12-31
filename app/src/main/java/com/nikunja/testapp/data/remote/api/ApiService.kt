package com.nikunja.testapp.data.remote.api

import com.nikunja.testapp.data.remote.dto.ItemDto
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    
    @GET("items")
    suspend fun getItems(): Response<List<ItemDto>>
}
