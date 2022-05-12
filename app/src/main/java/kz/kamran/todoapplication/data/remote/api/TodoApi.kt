package kz.kamran.todoapplication.data.remote.api

import kz.kamran.todoapplication.data.remote.dto.CategoryListResponseDto
import kz.kamran.todoapplication.data.remote.dto.CategoryRequestDto
import kz.kamran.todoapplication.data.remote.dto.TodoRequestDto
import kz.kamran.todoapplication.data.remote.dto.TodoListResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TodoApi {
    @GET("todos")
    suspend fun getTodoList(): Response<TodoListResponseDto>

    @GET("categories")
    suspend fun getCategoryList(): Response<CategoryListResponseDto>

    @POST("todos/upsert")
    suspend fun saveTodo(
        @Body todoRequestDto: TodoRequestDto
    ): Response<TodoListResponseDto>

    @POST("categories/upsert")
    suspend fun saveCategory(
        @Body categoryRequestDto: CategoryRequestDto
    ): Response<CategoryListResponseDto>
}