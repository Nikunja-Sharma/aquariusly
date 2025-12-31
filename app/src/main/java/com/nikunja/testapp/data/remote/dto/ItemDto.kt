package com.nikunja.testapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.nikunja.testapp.domain.model.Item

data class ItemDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String?
) {
    fun toDomain(): Item = Item(
        id = id,
        title = title,
        description = description.orEmpty()
    )
}
