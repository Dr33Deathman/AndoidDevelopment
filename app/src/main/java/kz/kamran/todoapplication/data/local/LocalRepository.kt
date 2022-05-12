package kz.kamran.todoapplication.data.local

import kz.kamran.todoapplication.data.model.Category
import kz.kamran.todoapplication.data.model.Todo

interface LocalRepository {
    suspend fun getCategoryList(): List<Category>

    suspend fun getTodoList(): List<Todo>

    suspend fun saveTodo(todo: Todo): Boolean

}