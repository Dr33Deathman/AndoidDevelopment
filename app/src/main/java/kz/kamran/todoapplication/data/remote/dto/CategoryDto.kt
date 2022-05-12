package kz.kamran.todoapplication.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CategoryDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
)