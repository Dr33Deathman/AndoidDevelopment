package kz.kamran.todoapplication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kz.kamran.todoapplication.data.local.entity.category.CategoryDao
import kz.kamran.todoapplication.data.local.entity.category.CategoryEntity
import kz.kamran.todoapplication.data.local.entity.todo.Converters
import kz.kamran.todoapplication.data.local.entity.todo.TodoDao
import kz.kamran.todoapplication.data.local.entity.todo.TodoEntity

@Database(
    entities = [CategoryEntity::class, TodoEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TodoDatabase : RoomDatabase() {
    abstract val categoryDao: CategoryDao
    abstract val todoDao: TodoDao

    companion object {
        const val DATABASE_NAME = "todo_db"
    }
}