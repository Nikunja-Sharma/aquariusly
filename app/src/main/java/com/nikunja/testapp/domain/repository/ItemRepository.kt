package com.nikunja.testapp.domain.repository

import com.nikunja.testapp.domain.model.Item
import com.nikunja.testapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    fun getItems(): Flow<Resource<List<Item>>>
    suspend fun refreshItems(): Resource<Unit>
}
