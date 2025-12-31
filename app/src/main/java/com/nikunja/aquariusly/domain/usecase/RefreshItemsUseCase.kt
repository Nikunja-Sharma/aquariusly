package com.nikunja.aquariusly.domain.usecase

import com.nikunja.aquariusly.domain.repository.ItemRepository
import com.nikunja.aquariusly.domain.util.Resource
import javax.inject.Inject

class RefreshItemsUseCase @Inject constructor(
    private val repository: ItemRepository
) {
    suspend operator fun invoke(): Resource<Unit> = repository.refreshItems()
}
