package kz.kamran.todoapplication.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AuthResponseDto(
    @SerializedName("token")
    val token: String?,
    @SerializedName("success")
    val success: Boolean
)
