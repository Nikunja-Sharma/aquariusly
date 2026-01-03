package com.nikunja.aquariusly.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FcmTokenRequest(
    @SerializedName("token")
    val token: String,
    @SerializedName("platform")
    val platform: String = "android"
)
