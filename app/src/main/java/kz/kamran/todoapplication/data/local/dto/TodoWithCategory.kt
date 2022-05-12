package kz.kamran.todoapplication.data.local.dto

import androidx.room.Embedded
import androidx.room.Relation
import kz.kamran.todoapplication.data.local.entity.category.CategoryEntity
import kz.kamran.todoapplication.data.local.entity.todo.TodoEntity

data class TodoWithCategory(
    @Embedded val todoEntity: TodoEntity,
    @Relation(
        parentColumn = "category_id",
        entityColumn = "id",
        entity = CategoryEntity::class
    )
    val categoryEntity: CategoryEntity?
)