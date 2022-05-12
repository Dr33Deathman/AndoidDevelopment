package kz.kamran.todoapplication.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kz.kamran.todoapplication.data.local.TodoDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(app: Application): TodoDatabase =
        Room.databaseBuilder(
            app,
            TodoDatabase::class.java,
            TodoDatabase.DATABASE_NAME
        ).build()

    @Provides
    @Singleton
    fun provideCategoryDao(db: TodoDatabase) = db.categoryDao

    @Provides
    @Singleton
    fun provideTodoDao(db: TodoDatabase) = db.todoDao
}
