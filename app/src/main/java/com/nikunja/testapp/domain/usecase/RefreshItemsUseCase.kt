package com.nikunja.testapp.domain.usecase

import com.nikunja.testapp.domain.repository.ItemRepository
import com.nikunja.testapp.domain.util.Resource
import javax.inject.Inject

class RefreshItemsUseCase @Inject constructor(
    private val repository: ItemRepository
) {
    suspend operator fun invoke(): Resource<Unit> = repository.refreshItems()
}
