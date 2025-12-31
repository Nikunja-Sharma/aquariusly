package com.nikunja.aquariusly.data.remote.api

import com.nikunja.aquariusly.data.remote.dto.ItemDto
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    
    @GET("items")
    suspend fun getItems(): Response<List<ItemDto>>
}
