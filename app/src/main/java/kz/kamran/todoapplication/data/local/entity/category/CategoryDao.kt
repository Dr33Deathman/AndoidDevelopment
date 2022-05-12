package kz.kamran.todoapplication.data.local.entity.category

import androidx.room.*

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(categoryEntity: CategoryEntity)

    @Delete
    suspend fun delete(categoryEntity: CategoryEntity)

    @Query("SELECT * FROM category")
    suspend fun getAll(): List<CategoryEntity>

    @Query("SELECT * FROM category WHERE id=:categoryId")
    suspend fun getById(categoryId: Int): CategoryEntity?
}