package kz.kamran.todoapplication.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TodoListResponseDto(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("todos")
    val todoList: List<TodoDto>
)
