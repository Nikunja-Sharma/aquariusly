package com.nikunja.testapp.domain.usecase

import com.nikunja.testapp.domain.model.Item
import com.nikunja.testapp.domain.repository.ItemRepository
import com.nikunja.testapp.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetItemsUseCase @Inject constructor(
    private val repository: ItemRepository
) {
    operator fun invoke(): Flow<Resource<List<Item>>> = repository.getItems()
}
