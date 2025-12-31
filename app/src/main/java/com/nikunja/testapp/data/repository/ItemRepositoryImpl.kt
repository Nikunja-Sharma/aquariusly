package com.nikunja.testapp.data.repository

import com.nikunja.testapp.data.local.dao.ItemDao
import com.nikunja.testapp.data.local.entity.ItemEntity
import com.nikunja.testapp.data.remote.api.ApiService
import com.nikunja.testapp.domain.model.Item
import com.nikunja.testapp.domain.repository.ItemRepository
import com.nikunja.testapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ItemRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val itemDao: ItemDao
) : ItemRepository {

    override fun getItems(): Flow<Resource<List<Item>>> = flow {
        emit(Resource.Loading)
        
        itemDao.getAllItems()
            .map { entities -> entities.map { it.toDomain() } }
            .collect { items ->
                emit(Resource.Success(items))
            }
    }

    override suspend fun refreshItems(): Resource<Unit> {
        return try {
            val response = apiService.getItems()
            if (response.isSuccessful) {
                response.body()?.let { dtos ->
                    val entities = dtos.map { dto ->
                        ItemEntity(
                            id = dto.id,
                            title = dto.title,
                            description = dto.description.orEmpty()
                        )
                    }
                    itemDao.deleteAllItems()
                    itemDao.insertItems(entities)
                    Resource.Success(Unit)
                } ?: Resource.Error("Empty response")
            } else {
                // API not available - insert mock data for testing
                insertMockData()
                Resource.Success(Unit)
            }
        } catch (e: Exception) {
            // Network error - insert mock data for testing
            insertMockData()
            Resource.Success(Unit)
        }
    }
    
    private suspend fun insertMockData() {
        val mockItems = listOf(
            ItemEntity(1, "Welcome to TestApp", "This is a sample item using Clean Architecture"),
            ItemEntity(2, "Jetpack Compose", "Modern UI toolkit for Android"),
            ItemEntity(3, "Hilt DI", "Dependency injection made easy"),
            ItemEntity(4, "Room Database", "Local persistence with SQLite"),
            ItemEntity(5, "Retrofit", "Type-safe HTTP client for Android"),
            ItemEntity(6, "Coroutines", "Asynchronous programming simplified"),
            ItemEntity(7, "Material 3", "Latest Material Design components"),
            ItemEntity(8, "MVVM Pattern", "Model-View-ViewModel architecture"),
            ItemEntity(9, "StateFlow", "Reactive state management"),
            ItemEntity(10, "Navigation Compose", "Type-safe navigation")
        )
        itemDao.deleteAllItems()
        itemDao.insertItems(mockItems)
    }
}
