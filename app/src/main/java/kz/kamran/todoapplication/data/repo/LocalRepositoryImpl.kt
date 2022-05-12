package kz.kamran.todoapplication.data.repo

import kz.kamran.todoapplication.data.local.entity.category.CategoryDao
import kz.kamran.todoapplication.data.local.entity.todo.TodoDao
import kz.kamran.todoapplication.data.local.mapper.toCategory
import kz.kamran.todoapplication.data.local.mapper.toTodo
import kz.kamran.todoapplication.data.model.Category
import kz.kamran.todoapplication.data.model.Todo
import kz.kamran.todoapplication.data.local.LocalRepository
import kz.kamran.todoapplication.data.local.mapper.toCategoryEntity
import kz.kamran.todoapplication.data.local.mapper.toTodoEntity
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
    private val todoDao: TodoDao
) : LocalRepository {
    override suspend fun getCategoryList(): List<Category> {
        val fetchedCategoryList = categoryDao.getAll()
        return fetchedCategoryList.map { it.toCategory() }
    }

    override suspend fun getTodoList(): List<Todo> {
        val fetchedTodoList = todoDao.getAll()
        return fetchedTodoList.map { it.toTodo() }
    }

    override suspend fun saveTodo(todo: Todo): Boolean {
        try {
            val categoryEntity = categoryDao.getById(todo.category.id)
            if (categoryEntity == null) {
                val category = todo.category
                categoryDao.insert(category.toCategoryEntity())
            }
            val categoryId = categoryDao.getAll()
                .find { it.title.equals(todo.category.title, ignoreCase = true) }!!.id
            todoDao.insert(todo.toTodoEntity(categoryId))
        } catch (e: Exception) {
            return false
        }
        return true
    }
}