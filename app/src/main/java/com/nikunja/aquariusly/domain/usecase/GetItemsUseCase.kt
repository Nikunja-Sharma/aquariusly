package com.nikunja.aquariusly.domain.usecase

import com.nikunja.aquariusly.domain.model.Item
import com.nikunja.aquariusly.domain.repository.ItemRepository
import com.nikunja.aquariusly.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetItemsUseCase @Inject constructor(
    private val repository: ItemRepository
) {
    operator fun invoke(): Flow<Resource<List<Item>>> = repository.getItems()
}
