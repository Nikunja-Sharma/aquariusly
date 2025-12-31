package com.nikunja.aquariusly.domain.repository

import com.nikunja.aquariusly.domain.model.Item
import com.nikunja.aquariusly.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    fun getItems(): Flow<Resource<List<Item>>>
    suspend fun refreshItems(): Resource<Unit>
}
