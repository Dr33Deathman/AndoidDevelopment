package kz.kamran.todoapplication.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginRequestDto(
    @SerializedName("login")
    val login: String,
    @SerializedName("password")
    val password: String,
)
