package kz.kamran.todoapplication.data.repo

import kz.kamran.todoapplication.data.model.Category
import kz.kamran.todoapplication.data.model.Todo
import kz.kamran.todoapplication.data.remote.api.AuthApi
import kz.kamran.todoapplication.data.remote.api.TodoApi
import kz.kamran.todoapplication.data.remote.mapper.toCategory
import kz.kamran.todoapplication.data.remote.mapper.toTodo
import kz.kamran.todoapplication.data.remote.RemoteRepository
import kz.kamran.todoapplication.data.remote.dto.CategoryRequestDto
import kz.kamran.todoapplication.data.remote.dto.LoginRequestDto
import kz.kamran.todoapplication.data.remote.dto.RegistrationRequestDto
import kz.kamran.todoapplication.data.remote.dto.TodoRequestDto
import kz.kamran.todoapplication.data.remote.provider.UserProvider
import kz.kamran.todoapplication.exception.InternalException
import kz.kamran.todoapplication.exception.UnauthorizedException
import okhttp3.internal.http.HTTP_UNAUTHORIZED
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val todoApi: TodoApi,
    private val userProvider: UserProvider
) : RemoteRepository {
    override suspend fun getCategoryList(): List<Category> {
        val response = todoApi.getCategoryList()
        if (!response.isSuccessful) {

            // no token or invalid token
            if (response.code() == HTTP_UNAUTHORIZED) {
                throw UnauthorizedException()
            }

            // some server error
            throw InternalException()
        }

        val body = response.body()!!
        val remoteCategoryList = body.categoryList
        return remoteCategoryList.map { it.toCategory() }
    }

    override suspend fun getTodoList(): List<Todo> {
        val response = todoApi.getTodoList()
        if (!response.isSuccessful) {

            // no token or invalid token
            if (response.code() == HTTP_UNAUTHORIZED) {
                throw UnauthorizedException()
            }

            // some server error
            throw InternalException()
        }
        val body = response.body()!!
        val remoteTodoList = body.todoList
        return remoteTodoList.map { it.toTodo() }
    }

    override suspend fun saveTodo(todoRequestDto: TodoRequestDto): Boolean {
        val response = todoApi.saveTodo(todoRequestDto)
        if (!response.isSuccessful) {

            // no token or invalid token
            if (response.code() == HTTP_UNAUTHORIZED) {
                throw UnauthorizedException()
            }

            // some server error
            throw InternalException()
        }

        val body = response.body()!!
        return body.success
    }

    override suspend fun saveCategory(categoryRequestDto: CategoryRequestDto): Boolean {
        val response = todoApi.saveCategory(categoryRequestDto)
        if (!response.isSuccessful) {

            // no token or invalid token
            if (response.code() == HTTP_UNAUTHORIZED) {
                throw UnauthorizedException()
            }

            // some server error
            throw InternalException()
        }

        val body = response.body()!!
        return body.success
    }

    override suspend fun login(loginRequestDto: LoginRequestDto): Boolean {
        val response = authApi.login(loginRequestDto)
        if (!response.isSuccessful) throw Exception("Incorrect data")
        val body = response.body()!!
        userProvider.saveToken(body.token)
        return true
    }

    override suspend fun register(registrationRequestDto: RegistrationRequestDto): Boolean {
        val response = authApi.register(registrationRequestDto)
        if (!response.isSuccessful) throw Exception("Invalid data")
        return response.body()!!.success
    }

    override fun isLogged(): Boolean = !userProvider.getToken().isNullOrBlank()
}