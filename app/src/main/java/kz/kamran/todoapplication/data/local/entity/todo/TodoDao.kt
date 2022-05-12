package kz.kamran.todoapplication.data.local.entity.todo

import androidx.room.*
import kz.kamran.todoapplication.data.local.dto.TodoWithCategory

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todoEntity: TodoEntity)

    @Update
    suspend fun update(todoEntity: TodoEntity)

    @Delete
    suspend fun delete(todoEntity: TodoEntity)

    @Transaction
    @Query("SELECT * FROM todo")
    suspend fun getAll(): List<TodoWithCategory>

    @Transaction
    @Query("SELECT * FROM todo WHERE id=:todoId")
    suspend fun getById(todoId: Int): TodoWithCategory?
}