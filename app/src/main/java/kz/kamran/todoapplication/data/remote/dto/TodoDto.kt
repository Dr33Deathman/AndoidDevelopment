package kz.kamran.todoapplication.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TodoDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("is_completed")
    val isCompleted: Boolean,
    @SerializedName("deadline")
    val deadline: Long,
    @SerializedName("category")
    val categoryDto: CategoryDto
)

