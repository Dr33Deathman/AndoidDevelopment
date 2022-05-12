package kz.kamran.todoapplication.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kz.kamran.todoapplication.data.local.LocalRepository
import kz.kamran.todoapplication.data.remote.RemoteRepository
import kz.kamran.todoapplication.data.repo.LocalRepositoryImpl
import kz.kamran.todoapplication.data.repo.RemoteRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideLocalRepository(localRepositoryImpl: LocalRepositoryImpl): LocalRepository

    @Binds
    @Singleton
    abstract fun provideRemoteRepository(remoteRepositoryImpl: RemoteRepositoryImpl): RemoteRepository
}