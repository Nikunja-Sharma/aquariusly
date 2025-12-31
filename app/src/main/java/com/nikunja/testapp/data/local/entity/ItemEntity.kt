package com.nikunja.testapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nikunja.testapp.domain.model.Item

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val description: String
) {
    fun toDomain(): Item = Item(
        id = id,
        title = title,
        description = description
    )
    
    companion object {
        fun fromDomain(item: Item): ItemEntity = ItemEntity(
            id = item.id,
            title = item.title,
            description = item.description
        )
    }
}
