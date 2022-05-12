package kz.kamran.todoapplication.data.local.mapper

import kz.kamran.todoapplication.data.local.entity.category.CategoryEntity
import kz.kamran.todoapplication.data.local.dto.TodoWithCategory
import kz.kamran.todoapplication.data.local.entity.todo.TodoEntity
import kz.kamran.todoapplication.data.model.Category
import kz.kamran.todoapplication.data.model.Todo

fun TodoWithCategory.toTodo() =
    Todo(
        id = todoEntity.id,
        title = todoEntity.title,
        description = todoEntity.description,
        isCompleted = todoEntity.isCompleted,
        category = categoryEntity?.toCategory() ?: Category(title = "123123"),
        deadline = todoEntity.deadline
    )

fun CategoryEntity.toCategory() =
    Category(
        id = id,
        title = title
    )

fun Todo.toTodoEntity() =
    TodoEntity(
        title = title,
        description = description,
        isCompleted = isCompleted,
        categoryId = category.id,
        deadline = deadline
    )

fun Todo.toTodoEntity(id: Int, categoryId: Int) =
    TodoEntity(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted,
        categoryId = categoryId,
        deadline = deadline
    )

fun Todo.toTodoEntity(categoryId: Int) =
    TodoEntity(
        title = title,
        description = description,
        isCompleted = isCompleted,
        categoryId = categoryId,
        deadline = deadline
    )

fun Category.toCategoryEntity() =
    CategoryEntity(
        title = title
    )