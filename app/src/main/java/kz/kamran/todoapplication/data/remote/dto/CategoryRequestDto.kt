package kz.kamran.todoapplication.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CategoryRequestDto(
    @SerializedName("title")
    val title: String,
)