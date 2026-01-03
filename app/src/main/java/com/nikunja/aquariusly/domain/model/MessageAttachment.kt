package com.nikunja.aquariusly.domain.model

enum class AttachmentType {
    IMAGE,
    PDF,
    FILE
}

data class MessageAttachment(
    val id: String,
    val type: AttachmentType,
    val uri: String,
    val name: String,
    val size: Long
)
