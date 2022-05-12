package kz.kamran.todoapplication.data.remote.mapper

import kz.kamran.todoapplication.data.model.Category
import kz.kamran.todoapplication.data.model.Todo
import kz.kamran.todoapplication.data.remote.dto.CategoryDto
import kz.kamran.todoapplication.data.remote.dto.TodoDto
import kz.kamran.todoapplication.data.remote.dto.TodoRequestDto
import kz.kamran.todoapplication.data.remote.dto.TodoUpdateRequestDto
import java.util.*

fun TodoDto.toTodo() =
    Todo(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted,
        category = categoryDto.toCategory(),
        deadline = Date(deadline) // convert millis to date
    )

fun CategoryDto.toCategory() =
    Category(
        id = id,
        title = title
    )

fun Todo.toCreateRequestDto() =
    TodoRequestDto(
        title = title,
        description = description,
        isCompleted = isCompleted,
        deadline = deadline.time,
        categoryId = category.id
    )

fun Todo.toUpdateRequestDto() =
    TodoUpdateRequestDto(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted,
        deadline = deadline.time,
        categoryId = category.id
    )

fun Todo.toReversedTodo() =
    Todo(
        id = id,
        title = title,
        description = description,
        isCompleted = !isCompleted,
        category = category,
        deadline = deadline
    )